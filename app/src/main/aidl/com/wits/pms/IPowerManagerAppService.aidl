package com.wits.pms;

interface IPowerManagerAppService {
       boolean sendCommand(String str);
       boolean sendStatus(String str);
       void registerCmdListener(com.wits.pms.ICmdListener iCmdListener);
       void unregisterCmdListener(com.wits.pms.ICmdListener iCmdListener);
       void registerObserver(String str, com.wits.pms.IContentObserver iContentObserver);
       void unregisterObserver(com.wits.pms.IContentObserver iContentObserver);
       boolean getStatusBoolean(String str);
       int getStatusInt(String str);
       String getStatusString(String str);
       int getSettingsInt(String str);
       String getSettingsString(String str);
       void setSettingsInt(String str, int i);
       void setSettingsString(String str, String str2);
       void addIntStatus(String str, int i);
       void addBooleanStatus(String str, boolean z);
       void addStringStatus(String str, String str2);
       void saveJsonConfig(String str, String str2);
       String getJsonConfig(String str);
}
