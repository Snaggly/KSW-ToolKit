package com.snaggly.ksw_toolkit.core.service.helper

import android.content.Context
import androidx.lifecycle.Observer
import com.snaggly.ksw_toolkit.core.service.adb.AdbServiceConnection
import com.snaggly.ksw_toolkit.util.adb.AdbManager
import com.snaggly.ksw_toolkit.util.adb.ShellObserver
import java.util.*
import kotlin.collections.ArrayList

object ServiceAliveCheck {
    val serviceAliveObservers = LinkedList<Observer<Boolean>>()

    fun checkIfServiceIsAlive(context: Context) {
        AdbManager.connect(context)
        AdbManager.registerShellListener( object : ShellObserver {
            override fun update(newLines: ArrayList<String>) {
                var isRunning = false
                for (line in newLines) {
                    if (line.contains("${CoreServiceClient.packageName}/${CoreServiceClient.className}")) {
                        isRunning = true
                        break
                    }
                }
                if (isRunning || newLines.size > 1) {
                    AdbManager.unregisterShellListener()
                    serviceAliveObservers.forEach {
                        it.onChanged(isRunning)
                    }
                }
            }
        }, context)
        AdbServiceConnection.checkIfThisServiceIsAlive(context)
    }
}