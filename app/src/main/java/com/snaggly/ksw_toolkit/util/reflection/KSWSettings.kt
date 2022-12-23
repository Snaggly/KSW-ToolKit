package com.snaggly.ksw_toolkit.util.reflection

import android.content.Context
import android.provider.Settings
import com.snaggly.ksw_toolkit.util.adb.AdbManager
import dalvik.system.DexClassLoader
import java.lang.reflect.Method

class KSWSettings(val context: Context) {
    private val powerManagerApp : Class<*>
    private val getSettingsInt : Method
    private val setSettingsInt : Method

    init {
        if (!checkIfHiddenApiPolicyIsSet()) {
            setHiddenApiPolicy()
        }

        powerManagerApp = DexClassLoader(
            "/system/app/KswPLauncher/KswPLauncher.apk",
            "/data/tmp/",
            "/data/tmp/",
            ClassLoader.getSystemClassLoader()
        ).loadClass("com.wits.pms.statuscontrol.PowerManagerApp")

        getSettingsInt = powerManagerApp.getDeclaredMethod(
            "getSettingsInt",
            String::class.java)

        setSettingsInt = powerManagerApp.getDeclaredMethod(
            "setSettingsInt",
            String::class.java,
            Int::class.javaPrimitiveType)
    }

    private fun setHiddenApiPolicy() {
        AdbManager.sendCommand("settings put global hidden_api_policy 1",context)
    }

    private fun checkIfHiddenApiPolicyIsSet() : Boolean {
        return Settings.Global.getInt(context.contentResolver, "hidden_api_policy", 0) == 1
    }

    fun getSettingsInt(setting : String) : Int {
        val result = getSettingsInt.invoke(null, setting)
        return if (result==null)
            -1
        else
            result as Int
    }

    fun setSettingsInt(setting : String, value : Int) {
        setSettingsInt.invoke(null, setting, value)
    }
}