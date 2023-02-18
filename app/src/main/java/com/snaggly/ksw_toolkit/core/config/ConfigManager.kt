package com.snaggly.ksw_toolkit.core.config

import com.google.gson.Gson
import com.snaggly.ksw_toolkit.core.config.beans.SystemOptions
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import java.io.*

class ConfigManager private constructor() {
    companion object {
        private val gson = Gson()

        fun getConfigFromJson(json : String) : ConfigData{
            return gson.fromJson(json, ConfigData::class.java)
        }
    }
}