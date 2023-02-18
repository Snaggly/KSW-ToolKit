package com.snaggly.ksw_toolkit.gui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.snaggly.ksw_toolkit.IKSWToolKitService
import com.snaggly.ksw_toolkit.core.config.ConfigManager
import com.snaggly.ksw_toolkit.core.config.beans.SystemOptions
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import com.snaggly.ksw_toolkit.util.adb.AdbManager

class SystemTwaksViewModel(application: Application) : AndroidViewModel(application) {
    fun shrinkTopBar() {
        AdbManager.sendCommand("wm overscan 0,-9,0,0", getApplication<Application>().applicationContext)
    }

    fun restoreTopBar() {
        AdbManager.sendCommand("wm overscan 0,0,0,0", getApplication<Application>().applicationContext)
    }

    fun showTopBar() {
        AdbManager.sendCommand("settings put global policy_control null*", getApplication<Application>().applicationContext)
    }

    fun hideTopBar() {
        AdbManager.sendCommand("settings put global policy_control immersive.full=*", getApplication<Application>().applicationContext)
    }

    fun giveTaskerPerm() {
        AdbManager.sendCommand("pm grant net.dinglisch.android.taskerm android.permission.READ_LOGS", getApplication<Application>().applicationContext)
    }
}