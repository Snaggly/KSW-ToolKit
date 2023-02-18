package com.snaggly.ksw_toolkit.gui.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.snaggly.ksw_toolkit.IKSWToolKitService
import com.snaggly.ksw_toolkit.R
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
        val jsonLines = fileReader.readLines()
        fileReader.close()
        if (jsonLines.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.empty_or_unreadable_file), Toast.LENGTH_SHORT).show()
        }
        val json = jsonLines.flatten()
        if (json == "") {
            Toast.makeText(context, context.getString(R.string.empty_or_unreadable_file), Toast.LENGTH_SHORT).show()
        }

        coreServiceClient.config = json
    }

    fun initializeServiceOptions(coreServiceClient: CoreServiceClient) {
        if (coreServiceClient.coreService?.hijackCS == false) {
            coreServiceClient.coreService?.hijackCS = true
            coreServiceClient.coreService?.setDefaultBtnLayout()
        }
    }

    private fun List<String>.flatten(): String {
        var result = ""
        this.forEach {
            result += it
        }
        return result
    }
}