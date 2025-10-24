package com.example.fairyringhotkeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;

import javax.inject.Inject;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FairyRingHelper
{
    private final Client client;
    private final ClientThread clientThread;

    public void teleportToCode(String code)
    {
        if (code == null || code.length() < 3)
        {
            return;
        }

        final String norm = code.replaceAll("[^A-Za-z]", "").toUpperCase(Locale.ROOT);
        if (norm.length() != 3)
        {
            return;
        }

        if (!isFairyRingOpen())
        {
            return;
        }

        clientThread.invoke(() ->
        {
            try
            {
                client.runScript(ScriptID.FAIRY_RING_SET_CODE, (int) norm.charAt(0), (int) norm.charAt(1), (int) norm.charAt(2));
            }
            catch (Throwable t)
            {
                log.warn("FAIRY_RING_SET_CODE script not available; falling back to dial rotation.");
                rotateDialsFallback(norm);
            }

            final Widget teleport = client.getWidget(WidgetInfo.FAIRY_RING_TELEPORT);
            if (teleport != null)
            {
                teleport.interact("Teleport");
            }
        });
    }

    private boolean isFairyRingOpen()
    {
        final Widget root = client.getWidget(WidgetInfo.FAIRY_RING_PANEL);
        return root != null && !root.isHidden();
    }

    private void rotateDialsFallback(String norm)
    {
        final Widget left = client.getWidget(WidgetInfo.FAIRY_RING_LEFT_ORB);
        final Widget mid = client.getWidget(WidgetInfo.FAIRY_RING_MIDDLE_ORB);
        final Widget right = client.getWidget(WidgetInfo.FAIRY_RING_RIGHT_ORB);
        if (left == null || mid == null || right == null)
        {
            return;
        }

        final Widget leftDec = client.getWidget(WidgetInfo.FAIRY_RING_LEFT_DECR);
        final Widget leftInc = client.getWidget(WidgetInfo.FAIRY_RING_LEFT_INCR);
        final Widget midDec = client.getWidget(WidgetInfo.FAIRY_RING_MIDDLE_DECR);
        final Widget midInc = client.getWidget(WidgetInfo.FAIRY_RING_MIDDLE_INCR);
        final Widget rightDec = client.getWidget(WidgetInfo.FAIRY_RING_RIGHT_DECR);
        final Widget rightInc = client.getWidget(WidgetInfo.FAIRY_RING_RIGHT_INCR);

        spinToLetter(left, leftInc, leftDec, norm.charAt(0));
        spinToLetter(mid, midInc, midDec, norm.charAt(1));
        spinToLetter(right, rightInc, rightDec, norm.charAt(2));
    }

    private void spinToLetter(Widget orbLabel, Widget incBtn, Widget decBtn, char target)
    {
        if (orbLabel == null || incBtn == null || decBtn == null)
        {
            return;
        }
        final char t = Character.toUpperCase(target);
        int safety = 8;
        while (safety-- > 0)
        {
            final String text = orbLabel.getText();
            if (text != null && !text.isEmpty() && Character.toUpperCase(text.charAt(0)) == t)
            {
                return;
            }
            if (!incBtn.interact("Rotate"))
            {
                decBtn.interact("Rotate");
            }
        }
    }
}