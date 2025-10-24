package com.example.fairyringhotkeys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
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
     * Minimal helper that validates input and logs.
     * We removed direct ScriptID/WidgetInfo references so this compiles
     * across RuneLite versions. After we confirm the plugin loads for you,
     * we’ll add a widget-based fallback using your client’s widget IDs.
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
            log.warn("Fairy code '{}' is invalid (need 3 letters).", code);
            return;
        }

        // Placeholder action so the hotkey does something visible for now.
        clientThread.invoke(() ->
            log.info("Fairy Ring Hotkeys: would set code '{}'. Script/widget calls are disabled in this build.", norm)
        );
    }
}
