// IMcuListener.aidl
package com.snaggly.ksw_toolkit;

interface IMcuListener {
    void updateMcu(String eventName, int cmdType, in byte[] data);
}