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
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import com.snaggly.ksw_toolkit.gui.viewmodels.SystemTwaksViewModel

class SystemTwaks(val coreServiceClient: CoreServiceClient) : Fragment() {
    private lateinit var viewModel: SystemTwaksViewModel
    private lateinit var hideTopBarSwitch: SwitchCompat
    private lateinit var hideTopBarLayout: LinearLayout
    private lateinit var shrinkTopBarLayout: LinearLayout
    private lateinit var shrinkTopBarSwitch: SwitchCompat
    private lateinit var autoThemeToggle: SwitchCompat
    private lateinit var autoVolumeSwitch: SwitchCompat
    private lateinit var maxVolumeOnBootSwitch: SwitchCompat
    private lateinit var giveTaskerLogcatPermBtn: Button
    private lateinit var soundRestorerToggle: SwitchCompat
    private lateinit var extraBtnHandleToggle: SwitchCompat
    private lateinit var nightBrightnessToggle: SwitchCompat
    private lateinit var nightBrightnessSeekBar: SeekBar
    private lateinit var tabletModeToggle: SwitchCompat
    private lateinit var hideStartMessageToggle: SwitchCompat
    private lateinit var decoupleNAVButtonToggle: SwitchCompat

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideTopBarSwitch.isChecked = false
            hideTopBarSwitch.isGone = true
            hideTopBarLayout.isGone = true
            shrinkTopBarSwitch.isChecked = false
            shrinkTopBarSwitch.isGone = true
            shrinkTopBarLayout.isGone = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SystemTwaksViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        sharedPref = requireContext().getSharedPreferences(SystemTwaks::javaClass.name, Context.MODE_PRIVATE)
        populateSettings()
        nightBrightnessSeekBar.isEnabled = nightBrightnessToggle.isChecked
    }

    private fun initElements() {
        autoThemeToggle = requireView().findViewById(R.id.autoThemeToggle)
        autoVolumeSwitch = requireView().findViewById(R.id.autoVolumeToggle)
        maxVolumeOnBootSwitch = requireView().findViewById(R.id.maxVolumeAtBootToggle)
        hideTopBarSwitch = requireView().findViewById(R.id.hideTopBarToggle)
        hideTopBarLayout = requireView().findViewById(R.id.hideTopBarLayout)
        shrinkTopBarLayout = requireView().findViewById(R.id.shrinkTopBarLayout)
        shrinkTopBarSwitch = requireView().findViewById(R.id.shrinkTopBarToggle)
        giveTaskerLogcatPermBtn = requireView().findViewById(R.id.giveTaskerLogcat)
        soundRestorerToggle = requireView().findViewById(R.id.soundRestorerToggle)
        extraBtnHandleToggle = requireView().findViewById(R.id.extraBtnHandleToggle)
        nightBrightnessToggle = requireView().findViewById(R.id.nightBrightnessToggle)
        nightBrightnessSeekBar = requireView().findViewById(R.id.nightBrightnessSeekBar)
        tabletModeToggle = requireView().findViewById(R.id.tabletModeToggle)
        hideStartMessageToggle = requireView().findViewById(R.id.hideStartMessageToggle)
        decoupleNAVButtonToggle = requireView().findViewById(R.id.decoupleNAVButtonToggle)
    }

    private fun populateSettings() {
        autoThemeToggle.isChecked = coreServiceClient.coreService?.autoTheme ?: false
        autoVolumeSwitch.isChecked = coreServiceClient.coreService?.autoVolume ?: false
        maxVolumeOnBootSwitch.isChecked = coreServiceClient.coreService?.maxVolume ?: false
        soundRestorerToggle.isChecked = coreServiceClient.coreService?.soundRestorer ?: false
        extraBtnHandleToggle.isChecked = coreServiceClient.coreService?.extraMediaButtonHandle ?: false
        nightBrightnessToggle.isChecked = coreServiceClient.coreService?.nightBrightness ?: false
        nightBrightnessSeekBar.progress = coreServiceClient.coreService?.nightBrightnessLevel ?: 0
        if (sharedPref != null) {
            hideTopBarSwitch.isChecked = sharedPref?.getBoolean("HideTopBar", false) ?: false
            shrinkTopBarSwitch.isChecked = sharedPref?.getBoolean("ShrinkTopBar", false) ?: false
        }
        tabletModeToggle.isChecked = coreServiceClient.coreService?.tabletMode ?: false
        hideStartMessageToggle.isChecked = coreServiceClient.coreService?.hideStartMessage ?: false
        decoupleNAVButtonToggle.isChecked = coreServiceClient.coreService?.decoupleNAVBtn?:false
    }

    private fun initButtonClickEvents() {
        autoThemeToggle.setOnClickListener {
            try {
                coreServiceClient.coreService?.autoTheme = (it as SwitchCompat).isChecked
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        autoVolumeSwitch.setOnClickListener {
            try {
                coreServiceClient.coreService?.autoVolume = (it as SwitchCompat).isChecked
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        maxVolumeOnBootSwitch.setOnClickListener {
            try {
                coreServiceClient.coreService?.maxVolume = (it as SwitchCompat).isChecked
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        hideTopBarSwitch.setOnClickListener {
            try {
                if ((it as SwitchCompat).isChecked) {
                    shrinkTopBarSwitch.isChecked = false
                    viewModel.hideTopBar()
                } else {
                    viewModel.showTopBar()
                }
                sharedPref?.edit()?.putBoolean("HideTopBar", it.isChecked)?.apply()
            } catch (exception: Exception) {
                val alert = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                    .setMessage("Unable to mess with TopBar!\n\n${exception.stackTrace}").create()
                alert.show()
            }
        }

        shrinkTopBarSwitch.setOnClickListener {
            try {
                if ((it as SwitchCompat).isChecked) {
                    hideTopBarSwitch.isChecked = false
                    viewModel.shrinkTopBar()
                } else {
                    viewModel.restoreTopBar()
                }
                sharedPref?.edit()?.putBoolean("ShrinkTopBar", it.isChecked)?.apply()
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
            try {
                coreServiceClient.coreService?.soundRestorer = (it as SwitchCompat).isChecked
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        extraBtnHandleToggle.setOnClickListener {
            try {
                coreServiceClient.coreService?.extraMediaButtonHandle = (it as SwitchCompat).isChecked
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        nightBrightnessToggle.setOnClickListener {
            nightBrightnessSeekBar.isEnabled = (it as SwitchCompat).isChecked

            try {
                coreServiceClient.coreService?.nightBrightness = it.isChecked
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        nightBrightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                try {
                    coreServiceClient.coreService?.nightBrightnessLevel = progress
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
            try {
                coreServiceClient.coreService?.tabletMode = (it as SwitchCompat).isChecked
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        hideStartMessageToggle.setOnClickListener {
            try {
                coreServiceClient.coreService?.hideStartMessage = (it as SwitchCompat).isChecked
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        decoupleNAVButtonToggle.setOnClickListener {
            try {
                coreServiceClient.coreService?.decoupleNAVBtn = (it as SwitchCompat).isChecked
            } catch (exception: Exception) {
                val alertExc =
                    AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit-SystemTweaks")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }
    }
}