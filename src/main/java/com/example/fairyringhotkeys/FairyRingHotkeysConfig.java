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

    // ---------- Auto-detect ----------
    @ConfigItem(
            keyName = "autoDetect",
            name = "Auto-detect IDs",
            description = "Try to discover the fairy ring widget IDs automatically when you press a hotkey.",
            position = 40
    )
    default boolean autoDetect() { return true; }

    // ---------- Manual override (optional) ----------
    @ConfigSection(
            name = "Advanced — Manual Widget IDs (optional)",
            description = "If auto-detect fails on your client, fill these from Dev Tools → Widget Inspector.",
            position = 50
    )
    String advanced = "advanced";

    @ConfigItem(keyName="groupId", name="Group ID", description="Widget groupId", position=51, section="advanced")
    default int groupId() { return -1; }

    @ConfigItem(keyName="leftLabel",  name="Left label childId",   description="childId", position=52, section="advanced") default int leftLabel()  { return -1; }
    @ConfigItem(keyName="leftInc",    name="Left + childId",       description="childId", position=53, section="advanced") default int leftInc()    { return -1; }
    @ConfigItem(keyName="leftDec",    name="Left - childId",       description="childId", position=54, section="advanced") default int leftDec()    { return -1; }
    @ConfigItem(keyName="midLabel",   name="Middle label childId", description="childId", position=55, section="advanced") default int midLabel()   { return -1; }
    @ConfigItem(keyName="midInc",     name="Middle + childId",     description="childId", position=56, section="advanced") default int midInc()     { return -1; }
    @ConfigItem(keyName="midDec",     name="Middle - childId",     description="childId", position=57, section="advanced") default int midDec()     { return -1; }
    @ConfigItem(keyName="rightLabel", name="Right label childId",  description="childId", position=58, section="advanced") default int rightLabel() { return -1; }
    @ConfigItem(keyName="rightInc",   name="Right + childId",      description="childId", position=59, section="advanced") default int rightInc()   { return -1; }
    @ConfigItem(keyName="rightDec",   name="Right - childId",      description="childId", position=60, section="advanced") default int rightDec()   { return -1; }
    @ConfigItem(keyName="teleportBtn",name="Teleport button childId", description="childId", position=61, section="advanced") default int teleportBtn(){ return -1; }
}
