package com.snaggly.ksw_toolkit.core.config.beans

class AdvancedBrightness (
    var isTimeBasedEnabled: Boolean?,
    var isUSBBasedEnabled: Boolean?,
    var sunriseAt: String?,
    var sunsetAt: String?,
    var autoTimes: Boolean?,
    var daylightBrightness: Int?,
    var daylightHLBrightness: Int?,
    var nightBrightnessLevel: Int?,
    var nightHLBrightnessLevel: Int?
)