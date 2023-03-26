package com.snaggly.ksw_toolkit.gui.viewmodels

import android.app.Application
import android.os.ParcelFileDescriptor
import androidx.lifecycle.AndroidViewModel
import com.snaggly.ksw_toolkit.IKSWToolKitService
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import java.io.*

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    fun exportSettings(parcelFileDescriptor: ParcelFileDescriptor?, coreServiceClient: IKSWToolKitService) {
        val fileWriter = FileWriter(parcelFileDescriptor?.fileDescriptor)
        fileWriter.write(coreServiceClient.config)
        fileWriter.close()
        parcelFileDescriptor?.close()
    }
    fun importSettings(parcelFileDescriptor: ParcelFileDescriptor?, coreServiceClient: IKSWToolKitService) : Boolean {
        val fileReader = FileReader(parcelFileDescriptor?.fileDescriptor)
        val jsonLines = fileReader.readLines()
        fileReader.close()
        if (jsonLines.isEmpty()) {
            return false
        }
        val json = jsonLines.flatten()
        if (json == "") {
            return false
        }

        coreServiceClient.config = json
        parcelFileDescriptor?.close()
        return true
    }

    fun initializeServiceOptions(coreServiceClient: CoreServiceClient) {
        if (coreServiceClient.coreService?.systemOptionsController?.hijackCS == false) {
            coreServiceClient.coreService?.systemOptionsController?.hijackCS = true
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