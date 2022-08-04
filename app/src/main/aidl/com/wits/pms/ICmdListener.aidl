package com.wits.pms;

interface ICmdListener {
    boolean handleCommand(String str);
    void updateStatusInfo(String str);
}