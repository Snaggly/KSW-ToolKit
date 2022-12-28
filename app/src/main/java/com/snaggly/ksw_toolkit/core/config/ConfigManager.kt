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

        @Throws(UnsupportedEncodingException::class)
        fun importConfig(applicationFilePath: String, toImportFilePath: String) : ConfigManager {
            //val inputFile = File(toImportFilePath)
            //inputFile.copyTo(File("$applicationFilePath/$fileName"), true)
            //config.configFile = null
            //getConfig(applicationFilePath) TODO(actually send new config to remote)
            //return config
            return ConfigManager()
        }

        fun exportConfig(filePath: String) {
            //val outputFile = File(filePath) TODO(actually write the current json)
            //config.configFile!!.copyTo(outputFile, true)
        }

        fun getSettingsBool(serviceConfig: SystemOptions) : BooleanArray {
            val settingsBool = BooleanArray(11)
            settingsBool[0] = serviceConfig.startAtBoot!!
            settingsBool[1] = serviceConfig.hijackCS!!
            settingsBool[2] = serviceConfig.soundRestorer!!
            settingsBool[3] = serviceConfig.autoTheme!!
            settingsBool[4] = serviceConfig.autoVolume!!
            settingsBool[5] = serviceConfig.maxVolume!!
            settingsBool[6] = serviceConfig.logMcuEvent!!
            settingsBool[7] = serviceConfig.interceptMcuCommand!!
            settingsBool[8] = serviceConfig.extraMediaButtonHandle!!
            settingsBool[9] = serviceConfig.nightBrightness!!
            settingsBool[10] = serviceConfig.hideStartMessage!!
            return settingsBool
        }

        fun initializeServiceOptions(coreServiceClient: CoreServiceClient) {
            val serviceConfig = getConfigFromJson(coreServiceClient.coreService?.config!!).systemOptions
            val settingsBool = getSettingsBool(serviceConfig)
            if (!settingsBool[1]) {
                settingsBool[1] = true
                coreServiceClient.coreService?.setDefaultBtnLayout()
                coreServiceClient.coreService?.setOptions(settingsBool)
            }
        }

        fun getStartOnBoot(coreServiceClient: CoreServiceClient?) :Boolean {
            if (coreServiceClient?.coreService == null)
                return false
            val serviceConfig = getConfigFromJson(coreServiceClient.coreService?.config!!).systemOptions
            return serviceConfig.startAtBoot!!
        }

        fun setStartOnBoot(value: Boolean, coreServiceClient: CoreServiceClient) {
            val serviceConfig = getConfigFromJson(coreServiceClient.coreService?.config!!).systemOptions
            val settingsBool = getSettingsBool(serviceConfig)
            settingsBool[0] = value
            coreServiceClient.coreService?.setOptions(settingsBool)
        }
    }
}