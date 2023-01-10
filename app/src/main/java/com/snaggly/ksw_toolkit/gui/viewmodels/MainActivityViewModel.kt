package com.snaggly.ksw_toolkit.gui.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.lifecycle.AndroidViewModel
import com.snaggly.ksw_toolkit.IKSWToolKitService
import com.snaggly.ksw_toolkit.core.config.ConfigManager
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import com.snaggly.ksw_toolkit.util.list.eventtype.EventMode
import java.io.*
import java.net.URI

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    fun exportSettings(context: Context, coreServiceClient: IKSWToolKitService, uri: Uri) {
        val fileWriter = FileWriter(context.contentResolver.openFileDescriptor(uri, "w")?.fileDescriptor)
        fileWriter.write(coreServiceClient.config)
        fileWriter.close()
    }
    fun importSettings(context: Context, coreServiceClient: IKSWToolKitService, uri: Uri) {
        val fileReader = FileReader(context.contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor)
        val json = fileReader.readLines()
        fileReader.close()
        val importedConfig = ConfigManager.getConfigFromJson(json.flatten())
        importedConfig.eventManagers.forEach {
            val cmdString = when(it.value.eventMode) {
                EventMode.KeyEvent -> it.value.keyCode.toString()
                EventMode.StartApp -> it.value.appName
                EventMode.McuCommand -> it.value.mcuCommandMode.toString()
                EventMode.NoAssignment -> ""
                EventMode.TaskerTask -> it.value.taskerTaskName
            }
            coreServiceClient.changeBtnConfig(it.key.ordinal, it.value.eventMode.ordinal, cmdString)
        }
        coreServiceClient.setOptions(ConfigManager.getSettingsBool(importedConfig.systemOptions))
        coreServiceClient.tabletMode = importedConfig.systemOptions.tabletMode?: false
    }

    fun initializeServiceOptions(coreServiceClient: CoreServiceClient) {
        ConfigManager.initializeServiceOptions(coreServiceClient)
    }

    private fun List<String>.flatten(): String {
        var result = ""
        this.forEach {
            result += it
        }
        return result
    }
}