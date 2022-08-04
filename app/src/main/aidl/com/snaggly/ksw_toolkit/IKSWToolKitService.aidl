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
}