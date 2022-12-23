package com.snaggly.ksw_toolkit.core.config.beans

class SystemOptions(
    var startAtBoot: Boolean?,
    var hijackCS: Boolean?,
    var soundRestorer: Boolean?,
    var autoTheme: Boolean?,
    var autoVolume: Boolean?,
    var maxVolume: Boolean?,
    var logMcuEvent: Boolean?,
    var interceptMcuCommand: Boolean?,
    var extraMediaButtonHandle: Boolean?,
    var nightBrightness: Boolean?,
    var nightBrightnessLevel: Int?,
    var mcuPath: String?,
    var tabletMode: Boolean?,
    var hideStartMessage: Boolean?
)