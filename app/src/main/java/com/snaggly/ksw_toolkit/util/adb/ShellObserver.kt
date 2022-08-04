package com.snaggly.ksw_toolkit.util.adb

interface ShellObserver {
    fun update(newLines: ArrayList<String>)
}