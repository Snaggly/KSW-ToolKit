// IKSWToolKitService.aidl
package com.snaggly.ksw_toolkit;

import com.snaggly.ksw_toolkit.IMcuListener;

interface IKSWToolKitService {
    boolean sendMcuCommand(int cmdType, in byte[] data);
    boolean changeBtnConfig(int btnType, int cmdType, String cmdValue);
    void setDefaultBtnLayout();
    String getConfig();
    boolean setConfig(String configJson);
    String[] getSettingsTypes();
    boolean setMcuPath(String path);
    boolean registerMcuListener(IMcuListener listener);
    boolean unregisterMcuListener(IMcuListener listener);
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

    boolean getHideStartMessage();
    void setHideStartMessage(boolean value);

    boolean getDecoupleNAVBtn();
    void setDecoupleNAVBtn(boolean value);

    boolean getAdvBri_IsTimeBased();
    void setAdvBri_IsTimeBased(boolean value);
    boolean getAdvBri_IsUSBBased();
    void setAdvBri_IsUSBBased(boolean value);
    String getAdvBri_SunriseAt();
    void setAdvBri_SunriseAt(String value);
    String getAdvBri_SunsetAt();
    void setAdvBri_SunsetAt(String value);
    boolean getAdvBri_Autotimes();
    void setAdvBri_Autotimes(boolean value);
    int getAdvBri_DaylightBri();
    void setAdvBri_DaylightBri(int value);
    int getAdvBri_DaylightHLBri();
    void setAdvBri_DaylightHLBri(int value);
    int getAdvBri_NightBri();
    void setAdvBri_NightBri(int value);
    int getAdvBri_NightHLBri();
    void setAdvBri_NightHLBri(int value);
}