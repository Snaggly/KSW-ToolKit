package com.wits.pms.custom;

interface ICallBack {
    void onReverse(int z);
    void onLRReverse(int i);
    void onRRadar(int i, int i2, int i3, int i4, int i5);
    void onFRadar(int i, int i2, int i3, int i4, int i5);
    void onAngle(int i);
    void onDoor(boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6);
    void onAcc(boolean z);
}