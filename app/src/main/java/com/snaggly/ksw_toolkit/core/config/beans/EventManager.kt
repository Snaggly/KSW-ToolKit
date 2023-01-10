package com.snaggly.ksw_toolkit.core.config.beans

import com.snaggly.ksw_toolkit.util.list.eventtype.EventMode

class EventManager(var eventMode: EventMode, var keyCode: Int, var appName: String,
                   var mcuCommandMode: Int, var taskerTaskName: String
)