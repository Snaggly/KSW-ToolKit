package com.snaggly.ksw_toolkit;

import com.snaggly.ksw_toolkit.IAutoTimeListener;

interface IAdvancedBrightnessControl {
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

    void registerAutoTimeListener(IAutoTimeListener listener);
    void unregisterAutoTimeListener(IAutoTimeListener listener);
}