// IKSWToolKitService.aidl
package com.snaggly.ksw_toolkit;

import com.snaggly.ksw_toolkit.IMcuListener;

interface IKSWToolKitService {
    boolean sendMcuCommand(int cmdType, in byte[] data);
    boolean changeBtnConfig(int btnType, int cmdType, String cmdValue);
    void setDefaultBtnLayout();
    String getConfig();
    boolean setOptions(in boolean[] allSettings);
    String[] getSettingsTypes();
    boolean setMcuPath(String path);
    boolean registerMcuListener(IMcuListener listener);
    boolean unregisterMcuListener(IMcuListener listener);
    int getNightBrightnessSetting();
    void setNightBrightnessSetting(int value);

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
}