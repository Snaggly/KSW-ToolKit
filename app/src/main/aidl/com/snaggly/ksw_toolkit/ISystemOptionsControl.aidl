package com.snaggly.ksw_toolkit;

interface ISystemOptionsControl {
    int getNightBrightnessLevel();
    void setNightBrightnessLevel(int value);

    boolean getTabletMode();
    void setTabletMode(boolean value);

    boolean getStartAtBoot();
    void setStartAtBoot(boolean value);

    boolean getHijackCS();
    void setHijackCS(boolean value);

    boolean getSoundRestorer();
    void setSoundRestorer(boolean value);

    boolean getAutoTheme();
    void setAutoTheme(boolean value);

    boolean getAutoVolume();
    void setAutoVolume(boolean value);

    boolean getMaxVolume();
    void setMaxVolume(boolean value);

    boolean getLogMcuEvent();
    void setLogMcuEvent(boolean value);

    boolean getInterceptMcuCommand();
    void setInterceptMcuCommand(boolean value);

    boolean getExtraMediaButtonHandle();
    void setExtraMediaButtonHandle(boolean value);

    boolean getNightBrightness();
    void setNightBrightness(boolean value);

    String getMcuPath();
    boolean setMcuPath(String path);

    boolean getHideStartMessage();
    void setHideStartMessage(boolean value);

    boolean getDecoupleNAVBtn();
    void setDecoupleNAVBtn(boolean value);
}