// IKSWToolKitService.aidl
package com.snaggly.ksw_toolkit;

import com.snaggly.ksw_toolkit.IMcuListener;
import com.snaggly.ksw_toolkit.ISystemOptionsControl;
import com.snaggly.ksw_toolkit.IAdvancedBrightnessControl;

interface IKSWToolKitService {
    boolean sendMcuCommand(int cmdType, in byte[] data);

    boolean changeBtnConfig(int btnType, int cmdType, String cmdValue);
    void setDefaultBtnLayout();

    String getConfig();
    boolean setConfig(String configJson);

    boolean registerMcuListener(IMcuListener listener);
    boolean unregisterMcuListener(IMcuListener listener);

    ISystemOptionsControl getSystemOptionsController();
    IAdvancedBrightnessControl getAdvancedBrightnessController();
}