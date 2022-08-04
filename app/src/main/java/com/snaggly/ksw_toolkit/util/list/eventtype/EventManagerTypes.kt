package com.snaggly.ksw_toolkit.util.list.eventtype

enum class EventManagerTypes {
    VoiceCommandButton,
    TelephoneButton,
    TelephoneButtonPickUp,
    TelephoneButtonHangUp,
    TelephoneButtonLongPress,
    VolumeDecrease,
    VolumeIncrease,
    MediaPrevious,
    MediaNext,
    MediaPlayPause,
    ModeButton,
    KnobPress,
    KnobTiltUp,
    KnobTiltDown,
    KnobTiltLeft,
    KnobTiltRight,
    KnobTurnLeft,
    KnobTurnRight,
    MenuButton,
    BackButton,
    OptionsButton,
    NavigationButton,
    HiCarAppButton,
    HiCarVoiceButton,
    Idle,
    CarData,
    BenzData,
    ScreenSwitch,
    Dummy,
    Time,
    AccEvent,
    PowerEvent,
    McuVersionEvent,
    TouchEvent,
    CanStatusCheck,
    MediaDataEvent,
    BluetoothStatusEvent,
    EQDataEvent,
    TxzInfoEvent,
    SystemStatusEvent;

    companion object {
        private val types = EventManagerTypes.values().associateBy { it.name }
        fun findByName(value: String) = types[value]
    }
}