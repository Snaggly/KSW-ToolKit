package com.snaggly.ksw_toolkit.util.list.mcu

enum class McuCommandsEnum {
    ScreenOff,
    MediaVolumeInc,
    MediaVolumeDec,
    BrightnessInc,
    BrightnessDec,
    CarInfo,
    Radio,
    F_CAM,
    AUX,
    DVR,
    DVD,
    DTV;

    companion object {
        val values = values()
    }
}