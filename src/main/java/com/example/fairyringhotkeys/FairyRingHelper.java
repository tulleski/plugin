package com.example.fairyringhotkeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.ChatMessageType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FairyRingHelper
{
    private final Client client;
    private final ClientThread clientThread;
    private final FairyRingHotkeysConfig cfg;

    private DetectResult cached;

    public void teleportToCode(String code)
    {
        if (code == null) return;
        final String norm = code.replaceAll("[^A-Za-z]", "").toUpperCase(Locale.ROOT);
        if (norm.length() != 3) { info("Fairy code '"+code+"' is invalid (need 3 letters)."); return; }

        clientThread.invoke(() ->
        {
            DetectResult ids = getIds();
            if (ids == null)
            {
                info("Fairy Ring Hotkeys: could not detect widget IDs. Open the fairy ring interface and press the hotkey again.");
                return;
            }

            // Spin each dial (log-only for now to keep this build compatible with your client API)
            spinToLetter(ids, ids.leftLabel,  ids.leftInc,  ids.leftDec,  norm.charAt(0));
            spinToLetter(ids, ids.midLabel,   ids.midInc,   ids.midDec,   norm.charAt(1));
            spinToLetter(ids, ids.rightLabel, ids.rightInc, ids.rightDec, norm.charAt(2));

            Widget t = client.getWidget(ids.groupId, ids.teleportBtn);
            if (t != null)
            {
                info("Fairy Ring Hotkeys: would click Teleport at " + ids.groupId + ":" + ids.teleportBtn);
            }
        });
    }

    private DetectResult getIds()
    {
        if (cfg.groupId() >= 0 && cfg.leftLabel() >= 0 && cfg.midLabel() >= 0 && cfg.rightLabel() >= 0
                && cfg.leftInc() >= 0 && cfg.leftDec() >= 0 && cfg.midInc() >= 0 && cfg.midDec() >= 0
                && cfg.rightInc() >= 0 && cfg.rightDec() >= 0 && cfg.teleportBtn() >= 0)
        {
            return new DetectResult(cfg.groupId(),
                    cfg.leftLabel(), cfg.leftInc(), cfg.leftDec(),
                    cfg.midLabel(),  cfg.midInc(),  cfg.midDec(),
                    cfg.rightLabel(),cfg.rightInc(),cfg.rightDec(),
                    cfg.teleportBtn());
        }

        if (cached != null) return cached;
        if (!cfg.autoDetect()) return null;

        List<DetectResult> candidates = new ArrayList<>();
        for (int group = 0; group < 1000; group++)
        {
            for (int child = 0; child < 200; child++)
            {
                Widget w = client.getWidget(group, child);
                if (w == null || w.isHidden()) continue;

                String text = safe(w.getText());
                if (!text.equalsIgnoreCase("Teleport")) continue;

                DetectResult res = scanGroup(group);
                if (res != null) candidates.add(res);
            }
        }

        if (candidates.isEmpty())
        {
            info("Fairy Ring Hotkeys: auto-detect found no candidates. Make sure the fairy ring interface is open.");
            return null;
        }

        cached = candidates.get(0);
        info("Auto-detected IDs → group " + cached.groupId
                + " | labels [" + cached.leftLabel + "," + cached.midLabel + "," + cached.rightLabel + "]"
                + " | teleport child " + cached.teleportBtn
                + ". Paste these into config to save them (Advanced — Manual Widget IDs).");
        return cached;
    }

    private DetectResult scanGroup(int group)
    {
        Integer teleportChild = null;
        List<Integer> rotateChildren = new ArrayList<>();
        List<Integer> labelChildren  = new ArrayList<>();

        for (int child = 0; child < 200; child++)
        {
            Widget w = client.getWidget(group, child);
            if (w == null || w.isHidden()) continue;

            String text = safe(w.getText());
            if (text.equalsIgnoreCase("Teleport")) teleportChild = child;

            if (text.length() == 1 && Character.isLetter(text.charAt(0)))
            {
                labelChildren.add(child);
            }

            String[] actions = w.getActions();
            if (actions != null)
            {
                for (String a : actions)
                {
                    if (a != null && a.toLowerCase(Locale.ROOT).contains("rotate"))
                    {
                        rotateChildren.add(child);
                        break;
                    }
                }
            }
        }

        if (teleportChild == null || labelChildren.size() < 3 || rotateChildren.size() < 3) return null;

        labelChildren.sort(Integer::compareTo);
        rotateChildren.sort(Integer::compareTo);

        int leftLabel  = labelChildren.get(0);
        int midLabel   = labelChildren.get(1);
        int rightLabel = labelChildren.get(2);

        int[] leftRot  = nearestTwo(rotateChildren, leftLabel);
        int[] midRot   = nearestTwo(rotateChildren, midLabel);
        int[] rightRot = nearestTwo(rotateChildren, rightLabel);
        if (leftRot == null || midRot == null || rightRot == null) return null;

        return new DetectResult(group,
                leftLabel,  leftRot[0],  leftRot[1],
                midLabel,   midRot[0],   midRot[1],
                rightLabel, rightRot[0], rightRot[1],
                teleportChild);
    }

    private int[] nearestTwo(List<Integer> arr, int target)
    {
        if (arr.isEmpty()) return null;
        int best1 = -1, best2 = -1;
        int d1 = Integer.MAX_VALUE, d2 = Integer.MAX_VALUE;
        for (int v : arr)
        {
            int d = Math.abs(v - target);
            if (d < d1) { d2 = d1; best2 = best1; d1 = d; best1 = v; }
            else if (d < d2 && v != best1) { d2 = d; best2 = v; }
        }
        if (best1 < 0 || best2 < 0) return null;
        return new int[]{best1, best2};
    }

    private void spinToLetter(DetectResult ids, int labelChild, int incChild, int decChild, char target)
    {
        final char T = Character.toUpperCase(target);
        for (int i = 0; i < 2; i++) // keep it light; log-only for now
        {
            Widget label = client.getWidget(ids.groupId, labelChild);
            String txt = (label != null) ? safe(label.getText()) : "";
            info("Would rotate dial at " + ids.groupId + ":" + labelChild + " towards '" + T + "' (currently '" + txt + "') via " +
                    ids.groupId + ":" + incChild + " and " + ids.groupId + ":" + decChild);
        }
    }

    private void info(String msg)
    {
        try { client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", msg, null); }
        catch (Throwable t) { log.info(msg); }
    }

    private static String safe(String s) { return s == null ? "" : s; }

    private static final class DetectResult
    {
        final int groupId;
        final int leftLabel, leftInc, leftDec;
        final int midLabel,  midInc,  midDec;
        final int rightLabel,rightInc,rightDec;
        final int teleportBtn;
        DetectResult(int groupId, int leftLabel, int leftInc, int leftDec,
                     int midLabel, int midInc, int midDec,
                     int rightLabel, int rightInc, int rightDec,
                     int teleportBtn)
        {
            this.groupId = groupId;
            this.leftLabel = leftLabel; this.leftInc = leftInc; this.leftDec = leftDec;
            this.midLabel = midLabel;   this.midInc = midInc;   this.midDec = midDec;
            this.rightLabel = rightLabel; this.rightInc = rightInc; this.rightDec = rightDec;
            this.teleportBtn = teleportBtn;
        }
    }
}
