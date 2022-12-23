package com.snaggly.ksw_toolkit.gui

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import com.snaggly.ksw_toolkit.gui.viewmodels.SystemTwaksViewModel

class SystemTwaks(val coreServiceClient: CoreServiceClient) : Fragment() {

    private lateinit var viewModel: SystemTwaksViewModel
    private lateinit var hideTopBarSwitch: SwitchCompat
    private lateinit var hideTopBarTxt: TextView
    private lateinit var shrinkTopBarSwitch: SwitchCompat
    private lateinit var shrinkTopBarTxt: TextView
    private lateinit var autoThemeToggle: SwitchCompat
    private lateinit var autoVolumeSwitch: SwitchCompat
    private lateinit var maxVolumeOnBootSwitch: SwitchCompat
    private lateinit var giveTaskerLogcatPermBtn: Button
    private lateinit var soundRestorerToggle: SwitchCompat
    private lateinit var extraBtnHandleToggle: SwitchCompat
    private lateinit var nightBrightnessToggle: SwitchCompat
    private lateinit var nightBrightnessSeekBar: SeekBar
    private lateinit var extraBtnHandleToggleTxt: TextView
    private lateinit var tabletModeToggle: SwitchCompat
    private lateinit var tabletModeToggleTxt: TextView
    private lateinit var hideStartMessageToggle: SwitchCompat

    private var settingsBool = BooleanArray(11)

    private var sharedPref: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.system_twaks_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        initElements()
        initButtonClickEvents()
        autoThemeToggle.requestFocus()
        if (Build.VERSION.SDK_INT >= 30) {
            hideTopBarSwitch.isChecked = false
            hideTopBarTxt.setTextAppearance(R.style.disabledSwitchTextStyle)
            shrinkTopBarSwitch.isChecked = false
            shrinkTopBarTxt.setTextAppearance(R.style.disabledSwitchTextStyle)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SystemTwaksViewModel::class.java)
        viewModel.coreServiceClient = coreServiceClient
    }

    override fun onResume() {
        super.onResume()
        sharedPref = requireContext().getSharedPreferences(SystemTwaks::javaClass.name, Context.MODE_PRIVATE)
        viewModel.resetConfig()
        setSettings()
    }

    private fun initElements() {
        autoThemeToggle = requireView().findViewById(R.id.autoThemeToggle)
        autoVolumeSwitch = requireView().findViewById(R.id.autoVolumeToggle)
        maxVolumeOnBootSwitch = requireView().findViewById(R.id.maxVolumeAtBootToggle)
        hideTopBarSwitch = requireView().findViewById(R.id.hideTopBarToggle)
        hideTopBarTxt = requireView().findViewById(R.id.hideTopBarTxt)
        shrinkTopBarSwitch = requireView().findViewById(R.id.shrinkTopBarToggle)
        shrinkTopBarTxt = requireView().findViewById(R.id.shrinkTopBarTxt)
        giveTaskerLogcatPermBtn = requireView().findViewById(R.id.giveTaskerLogcat)
        soundRestorerToggle = requireView().findViewById(R.id.soundRestorerToggle)
        extraBtnHandleToggle = requireView().findViewById(R.id.extraBtnHandleToggle)
        nightBrightnessToggle = requireView().findViewById(R.id.nightBrightnessToggle)
        nightBrightnessSeekBar = requireView().findViewById(R.id.nightBrightnessSeekBar)
        extraBtnHandleToggleTxt = requireView().findViewById(R.id.extraBtnHandleToggleTxt)
        tabletModeToggle = requireView().findViewById(R.id.tabletModeToggle)
        tabletModeToggleTxt = requireView().findViewById(R.id.tabletModeToggleTxt)
        hideStartMessageToggle = requireView().findViewById(R.id.hideStartMessageToggle)
    }

    private fun setSettings() {
        autoThemeToggle.isChecked = viewModel.getConfig()?.autoTheme ?: false
        autoVolumeSwitch.isChecked = viewModel.getConfig()?.autoVolume ?: false
        maxVolumeOnBootSwitch.isChecked = viewModel.getConfig()?.maxVolume ?: false
        soundRestorerToggle.isChecked = viewModel.getConfig()?.soundRestorer ?: false
        extraBtnHandleToggle.isChecked = viewModel.getConfig()?.extraMediaButtonHandle ?: false
        nightBrightnessToggle.isChecked = viewModel.getConfig()?.nightBrightness ?: false
        nightBrightnessSeekBar.progress = coreServiceClient.coreService?.nightBrightnessSetting ?: 0
        if (sharedPref != null) {
            hideTopBarSwitch.isChecked = sharedPref?.getBoolean("HideTopBar", false) ?: false
            shrinkTopBarSwitch.isChecked = sharedPref?.getBoolean("ShrinkTopBar", false) ?: false
        }
        tabletModeToggle.isChecked = viewModel.getConfig()?.tabletMode ?: false
        hideStartMessageToggle.isChecked = viewModel.getConfig()?.hideStartMessage ?: false

        settingsBool[0] = viewModel.getConfig()?.startAtBoot ?: false
        settingsBool[1] = viewModel.getConfig()?.hijackCS ?: true
        settingsBool[2] = soundRestorerToggle.isChecked
        settingsBool[3] = autoThemeToggle.isChecked
        settingsBool[4] = autoVolumeSwitch.isChecked
        settingsBool[5] = maxVolumeOnBootSwitch.isChecked
        settingsBool[6] = viewModel.getConfig()?.logMcuEvent ?: true
        settingsBool[7] = viewModel.getConfig()?.interceptMcuCommand ?: true
        settingsBool[8] = extraBtnHandleToggle.isChecked
        settingsBool[9] = nightBrightnessToggle.isChecked
        settingsBool[10] = hideStartMessageToggle.isChecked
    }

    private fun isTopBarFuncEnabledAndroid(): Boolean {
        if (Build.VERSION.SDK_INT >= 30) {
            val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("Cannot turn on")
                .setMessage("TOPBAR function is not available in Android 11 or later").create()
            alertExc.show()
            hideTopBarSwitch.isChecked = false
            shrinkTopBarSwitch.isChecked = false
            return false
        } else {
            return true
        }
    }

    private fun initButtonClickEvents() {
        autoThemeToggle.setOnClickListener {
            viewModel.getConfig()?.autoTheme = (it as SwitchCompat).isChecked
            try {
                settingsBool[3] = it.isChecked
                coreServiceClient.coreService?.setOptions(settingsBool)
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        autoVolumeSwitch.setOnClickListener {
            viewModel.getConfig()?.autoVolume = (it as SwitchCompat).isChecked
            try {
                settingsBool[4] = it.isChecked
                coreServiceClient.coreService?.setOptions(settingsBool)
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        maxVolumeOnBootSwitch.setOnClickListener {
            viewModel.getConfig()?.maxVolume = (it as SwitchCompat).isChecked
            try {
                settingsBool[5] = it.isChecked
                coreServiceClient.coreService?.setOptions(settingsBool)
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        hideTopBarSwitch.setOnClickListener {
            try {
                if (isTopBarFuncEnabledAndroid()) {
                    if ((it as SwitchCompat).isChecked) {
                        shrinkTopBarSwitch.isChecked = false
                        viewModel.hideTopBar()
                    } else {
                        viewModel.showTopBar()
                    }
                    sharedPref?.edit()?.putBoolean("HideTopBar", it.isChecked)?.apply()
                }
            } catch (exception: Exception) {
                val alert = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                    .setMessage("Unable to mess with TopBar!\n\n${exception.stackTrace}").create()
                alert.show()
            }
        }

        shrinkTopBarSwitch.setOnClickListener {
            try {
                if (isTopBarFuncEnabledAndroid()) {
                    if ((it as SwitchCompat).isChecked) {
                        hideTopBarSwitch.isChecked = false
                        viewModel.shrinkTopBar()
                    } else {
                        viewModel.restoreTopBar()
                    }
                    sharedPref?.edit()?.putBoolean("ShrinkTopBar", it.isChecked)?.apply()
                }
            } catch (exception: Exception) {
                val alert = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                    .setMessage("Unable to mess with TopBar!\n\n${exception.stackTrace}").create()
                alert.show()
            }
        }

        giveTaskerLogcatPermBtn.setOnClickListener {
            try {
                viewModel.giveTaskerPerm()
            } catch (exception: Exception) {
                val alert = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                    .setMessage("Unable to give Tasker Logcat Permission!\n\n${exception.stackTrace}").create()
                alert.show()
            }
        }

        soundRestorerToggle.setOnClickListener {
            viewModel.getConfig()?.soundRestorer = (it as SwitchCompat).isChecked
            try {
                settingsBool[2] = it.isChecked
                coreServiceClient.coreService?.setOptions(settingsBool)
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        extraBtnHandleToggle.setOnClickListener {
            viewModel.getConfig()?.extraMediaButtonHandle = (it as SwitchCompat).isChecked

            try {
                settingsBool[8] = it.isChecked
                coreServiceClient.coreService?.setOptions(settingsBool)
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        nightBrightnessToggle.setOnClickListener {
            viewModel.getConfig()?.nightBrightness = (it as SwitchCompat).isChecked
            nightBrightnessSeekBar.isEnabled = it.isChecked

            try {
                settingsBool[9] = it.isChecked
                coreServiceClient.coreService?.setOptions(settingsBool)
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        nightBrightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.getConfig()?.nightBrightnessLevel = progress
                try {
                    coreServiceClient.coreService?.nightBrightnessSetting = progress
                } catch (exception: Exception) {
                    val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                    alertExc.show()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        tabletModeToggle.setOnClickListener {
            viewModel.getConfig()?.tabletMode = (it as SwitchCompat).isChecked

            try {
                coreServiceClient.coreService?.tabletMode = it.isChecked
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        hideStartMessageToggle.setOnClickListener {
            viewModel.getConfig()?.hideStartMessage = (it as SwitchCompat).isChecked
            try {
                settingsBool[10] = it.isChecked
                coreServiceClient.coreService?.setOptions(settingsBool)
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }
    }
}