package com.snaggly.ksw_toolkit.util.list.eventtype

enum class EventMode(val value: Int) {
    NoAssignment(0),
    KeyEvent(1),
    StartApp(2),
    McuCommand(3),
    TaskerTask(4);

    companion object {
        private val types = values().associateBy { it.value }
        fun findByValue(value: Int) = types[value]
    }
}