package com.snaggly.ksw_toolkit.gui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.snaggly.ksw_toolkit.IKSWToolKitService
import com.snaggly.ksw_toolkit.core.config.ConfigManager
import com.snaggly.ksw_toolkit.core.config.beans.EventManager
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import com.snaggly.ksw_toolkit.util.list.eventtype.EventManagerTypes

class EventManagerViewModel(application: Application) : AndroidViewModel(application) {
    var coreServiceClient : CoreServiceClient? = null

    fun getConfig() : HashMap<EventManagerTypes, EventManager>? {
        val configJson = coreServiceClient?.coreService?.config
        return if (configJson != null) {
            ConfigManager.getConfigFromJson(coreServiceClient?.coreService?.config!!).eventManagers
        } else {
            null
        }
    }
}