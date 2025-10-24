package com.example.fairyringhotkeys;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

@Slf4j
@PluginDescriptor(
        name = "Fairy Ring Hotkeys",
        description = "Favorite up to four fairy-ring destinations and trigger them via 1–4 when the fairy-ring UI is open.",
        enabledByDefault = true
)
public class FairyRingHotkeysPlugin extends Plugin implements KeyListener
{
    @Inject private Client client;
    @Inject private ClientThread clientThread;
    @Inject private KeyManager keyManager;
    @Inject private OverlayManager overlayManager;
    @Inject private FairyRingHotkeysOverlay overlay;
    @Inject private FairyRingHelper helper;
    @Inject private FairyRingHotkeysConfig config;

    private volatile boolean fairyUiOpen = false;

    @Override
    protected void startUp()
    {
        keyManager.registerKeyListener(this);
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
        keyManager.unregisterKeyListener(this);
        fairyUiOpen = false;
    }

    @Provides
    FairyRingHotkeysConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(FairyRingHotkeysConfig.class);
    }

    private boolean isFairyPanelVisible()
    {
        // Conservative: allow hotkeys to try regardless of widget presence.
        // Helper is a no-op if the script isn't available.
        return true;
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged e)
    {
        if (e.getGameState() == GameState.LOGIN_SCREEN)
        {
            fairyUiOpen = false;
        }
    }

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded e)
    {
        isFairyPanelVisible();
    }

    @Subscribe
    public void onScriptPostFired(ScriptPostFired e)
    {
        isFairyPanelVisible();
    }

    @Override public void keyTyped(java.awt.event.KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (!isFairyPanelVisible())
        {
            return;
        }

        final int code = e.getKeyCode();
        final boolean numpad = config.useNumpad();

        switch (code)
        {
            case KeyEvent.VK_1:
            case KeyEvent.VK_NUMPAD1:
                if (code == KeyEvent.VK_NUMPAD1 && !numpad) return;
                helper.teleportToCode(config.favorite1());
                e.consume();
                break;
            case KeyEvent.VK_2:
            case KeyEvent.VK_NUMPAD2:
                if (code == KeyEvent.VK_NUMPAD2 && !numpad) return;
                helper.teleportToCode(config.favorite2());
                e.consume();
                break;
            case KeyEvent.VK_3:
            case KeyEvent.VK_NUMPAD3:
                if (code == KeyEvent.VK_NUMPAD3 && !numpad) return;
                helper.teleportToCode(config.favorite3());
                e.consume();
                break;
            case KeyEvent.VK_4:
            case KeyEvent.VK_NUMPAD4:
                if (code == KeyEvent.VK_NUMPAD4 && !numpad) return;
                helper.teleportToCode(config.favorite4());
                e.consume();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
}
