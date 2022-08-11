package com.snaggly.ksw_toolkit.gui.viewmodels

import android.app.Application
import android.content.Context
import android.provider.Settings
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.snaggly.ksw_toolkit.IKSWToolKitService
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.core.config.ConfigManager
import com.snaggly.ksw_toolkit.core.config.beans.SystemOptions
import com.snaggly.ksw_toolkit.core.service.adb.AdbServiceConnection
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import com.snaggly.ksw_toolkit.core.service.helper.ServiceAliveCheck
import com.snaggly.ksw_toolkit.util.adb.AdbManager
import com.snaggly.ksw_toolkit.util.adb.ShellObserver
import com.snaggly.ksw_toolkit.util.github.GitHubRelease
import com.snaggly.ksw_toolkit.util.github.VersionTag
import projekt.auto.mcu.ksw.serial.collection.McuCommands
import projekt.auto.mcu.ksw.serial.enums.SOUND_SRC_TYPE
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class ToolKitViewModel(application: Application) : AndroidViewModel(application) {

    private val setSourceOEM     = McuCommands.SWITCH_TO_OEM
    private val setScreenOff     = McuCommands.SYS_SCREEN_OFF
    private val setSourceRadio   = McuCommands.SetMusicSource(SOUND_SRC_TYPE.SRC_RADIO)
    private val setAUX           = McuCommands.SetMusicSource(SOUND_SRC_TYPE.SRC_AUX)
    private val setDVR           = McuCommands.SetMusicSource(SOUND_SRC_TYPE.SRC_DVR)
    private val setDVD           = McuCommands.SetMusicSource(SOUND_SRC_TYPE.SRC_DVD)

    var coreServiceClient : CoreServiceClient? = null

    fun getBrightness(context: Context) : Int {
        return (Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, 255)* 100) / 255
    }

    fun setBrightness(context: Context, brightness: Int) {
        Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness*255/100)
        val cmd = McuCommands.SetBrightnessLevel(brightness.toByte())
        sendCommand(cmd.command, cmd.data)
    }

    fun openOEMScreen() {
        sendCommand(setSourceOEM.command, setSourceOEM.data)
    }

    fun openRadioScreen() {
        sendCommand(setSourceRadio.command, setSourceRadio.data)
    }

    fun openFCamScreen() {
        sendCommand(0x67, byteArrayOf(0xB))
    }

    fun openAuxScreen() {
        sendCommand(setAUX.command, setAUX.data)
    }

    fun openDvrScreen() {
        sendCommand(setDVR.command, setDVR.data)
    }

    fun openDvdScreen() {
        sendCommand(setDVD.command, setDVD.data)
    }

    fun openDtvScreen() {
        sendCommand(0x67, byteArrayOf(0x9))
    }

    fun closeScreen() {
        sendCommand(setScreenOff.command, setScreenOff.data)
    }

    private fun sendCommand(cmdType: Int, data: ByteArray) {
        coreServiceClient!!.coreService!!.sendMcuCommand(cmdType, data)
    }

    fun setStartAtBootOption(value: Boolean) {
        if (coreServiceClient != null)
            ConfigManager.setStartOnBoot(value, coreServiceClient!!)
    }

    fun getStartAtBootOption() : Boolean {
        return ConfigManager.getStartOnBoot(coreServiceClient)
    }

     fun checkService(context: Context) {
         try{
             ServiceAliveCheck.checkIfServiceIsAlive(context)
         }catch (exception : Exception) {
             Toast.makeText(context, context.resources.getText(R.string.could_not_check_service), Toast.LENGTH_LONG).show()
         }
    }
}