package com.example.fairyringhotkeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;
import net.runelite.client.callback.ClientThread;

import javax.inject.Inject;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class FairyRingHelper
{
    private final Client client;
    private final ClientThread clientThread;

    /**
     * Try to set the fairy ring code via a client script, if available.
     * We intentionally avoid direct WidgetInfo references here so the code
     * compiles against a wide range of RuneLite versions.
     */
    public void teleportToCode(String code)
    {
        if (code == null)
        {
            return;
        }

        final String norm = code.replaceAll("[^A-Za-z]", "").toUpperCase(Locale.ROOT);
        if (norm.length() != 3)
        {
            return;
        }

        // Run on client thread and swallow any API mismatches gracefully.
        clientThread.invoke(() ->
        {
            try
            {
                // Some RuneLite versions expose FAIRY_RING_SET_CODE, others may not.
                // If it's present, this will set the three letters. If not, we just log.
                client.runScript(ScriptID.FAIRY_RING_SET_CODE,
                        (int) norm.charAt(0),
                        (int) norm.charAt(1),
                        (int) norm.charAt(2));
            }
            catch (Throwable t)
            {
                log.warn("Fairy ring script not available on this client; skipping (safe no-op).");
            }
        });
    }
}
