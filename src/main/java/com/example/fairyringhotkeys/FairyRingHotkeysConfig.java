package com.example.fairyringhotkeys;

import net.runelite.client.config.*;

@ConfigGroup("fairyringhotkeys")
public interface FairyRingHotkeysConfig extends Config
{
    @ConfigItem(
            keyName = "fav1",
            name = "Favorite 1",
            description = "3-letter fairy ring code for hotkey 1",
            position = 1
    )
    default String favorite1() { return "CIP"; }

    @ConfigItem(
            keyName = "fav2",
            name = "Favorite 2",
            description = "3-letter fairy ring code for hotkey 2",
            position = 2
    )
    default String favorite2() { return "DKR"; }

    @ConfigItem(
            keyName = "fav3",
            name = "Favorite 3",
            description = "3-letter fairy ring code for hotkey 3",
            position = 3
    )
    default String favorite3() { return "ALP"; }

    @ConfigItem(
            keyName = "fav4",
            name = "Favorite 4",
            description = "3-letter fairy ring code for hotkey 4",
            position = 4
    )
    default String favorite4() { return "BIP"; }

    @ConfigItem(
            keyName = "useNumpad",
            name = "Use Numpad 1–4",
            description = "Treat numpad 1–4 as hotkeys as well",
            position = 10
    )
    default boolean useNumpad() { return true; }
}