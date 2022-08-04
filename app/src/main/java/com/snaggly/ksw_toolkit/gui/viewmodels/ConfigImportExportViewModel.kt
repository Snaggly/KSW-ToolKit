package com.snaggly.ksw_toolkit.gui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.snaggly.ksw_toolkit.core.config.ConfigManager
import com.snaggly.ksw_toolkit.util.adb.AdbManager

class ConfigImportExportViewModel(application: Application) : AndroidViewModel(application) {
    fun importConfig() : String {
        val storagePath = "${getApplication<Application>().applicationContext.getExternalFilesDir(null)?.absolutePath}/KSW-Config.json"
        storagePath.let { ConfigManager.importConfig(getApplication<Application>().applicationContext.filesDir.absolutePath, it) }
        return storagePath
    }

    fun exportConfig() : String {
        val storagePath = "${getApplication<Application>().applicationContext.getExternalFilesDir(null)?.absolutePath}/KSW-Config.json"
        storagePath.let { ConfigManager.exportConfig(it) }
        return storagePath
    }

    fun restartSystem(context: Context) {
        AdbManager.sendCommand("reboot", context)
    }
}