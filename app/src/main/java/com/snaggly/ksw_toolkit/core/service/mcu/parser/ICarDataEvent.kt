package com.snaggly.ksw_toolkit.core.service.mcu.parser

import com.snaggly.ksw_toolkit.util.list.eventtype.EventManagerTypes

interface ICarDataEvent {
    fun getCarDataEvent(data: ByteArray) : EventManagerTypes
}