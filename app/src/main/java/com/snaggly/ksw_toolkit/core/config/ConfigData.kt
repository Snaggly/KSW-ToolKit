package com.snaggly.ksw_toolkit.core.config

import com.snaggly.ksw_toolkit.core.config.beans.AdvancedBrightness
import com.snaggly.ksw_toolkit.core.config.beans.EventManager
import com.snaggly.ksw_toolkit.core.config.beans.SystemOptions
import com.snaggly.ksw_toolkit.util.list.eventtype.EventManagerTypes

data class ConfigData(
        var systemOptions: SystemOptions,
        var eventManagers : HashMap<EventManagerTypes, EventManager>,
        var advancedBrightness: AdvancedBrightness
)
