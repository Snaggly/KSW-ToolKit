package com.snaggly.ksw_toolkit.core.service.adb

import android.content.Context
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import com.snaggly.ksw_toolkit.util.adb.AdbManager

object AdbServiceConnection {
    fun stopKsw(context: Context) {
        AdbManager.sendCommand("am stopservice --user 0 com.wits.pms/com.wits.pms.mcu.McuService\nappops set com.wits.pms SYSTEM_ALERT_WINDOW deny", context)
    }

    fun startKsw(context: Context) {
        AdbManager.sendCommand("am startservice --user 0 com.wits.pms/com.wits.pms.mcu.McuService\nappops set com.wits.pms SYSTEM_ALERT_WINDOW allow", context)
    }

    fun startThisService(context: Context) {
        AdbManager.sendCommand("am startservice --user 0 ${CoreServiceClient.packageName}/${CoreServiceClient.className}", context)
        //AdbManager.sendCommand("am start-foreground-service com.snaggly.wits.ksw_toolkit.service/com.snaggly.ksw_toolkit.core.service.CoreService", context)
    }

    fun stopThisService(context: Context) {
        AdbManager.sendCommand("am stopservice --user 0 ${CoreServiceClient.packageName}/${CoreServiceClient.className}", context)
    }

    fun sendKeyEvent(code: Int, context: Context) {
        AdbManager.sendCommand("input keyevent $code", context)
    }

    fun startApp(appId: String, context: Context) {
        AdbManager.sendCommand("monkey -p $appId -c android.intent.category.LAUNCHER 1", context)
    }

    fun sendCommand(command: String, context: Context) {
        AdbManager.sendCommand("$command\n", context)
    }

    fun checkIfThisServiceIsAlive(context: Context) {
        AdbManager.sendCommand("dumpsys activity services ${CoreServiceClient.packageName} | grep \"ServiceRecord\" | awk '{print $4}' | sed 's/.$//'", context)
    }
}