package com.snaggly.ksw_toolkit.gui

import android.annotation.SuppressLint
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
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.core.config.ConfigManager
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
    private lateinit var nightBrightnessPercentageTV : TextView

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
        if (nightBrightnessToggle.isChecked)
            nightBrightnessPercentageTV.visibility = View.VISIBLE
        else
            nightBrightnessPercentageTV.visibility = View.GONE
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
        nightBrightnessPercentageTV = requireView().findViewById(R.id.nightBrightnessPercentageTV)
    }

    @SuppressLint("SetTextI18n")
    private fun populateSettings() {
        val json = coreServiceClient.coreService?.config ?: return
        val config = ConfigManager.getConfigFromJson(json).systemOptions
        autoThemeToggle.isChecked = config.autoTheme ?: false
        autoVolumeSwitch.isChecked = config.autoVolume ?: false
        maxVolumeOnBootSwitch.isChecked = config.maxVolume ?: false
        soundRestorerToggle.isChecked = config.soundRestorer ?: false
        extraBtnHandleToggle.isChecked = config.extraMediaButtonHandle ?: false
        nightBrightnessToggle.isChecked = config.nightBrightness ?: false
        nightBrightnessSeekBar.progress = config.nightBrightnessLevel ?: 0
        if (sharedPref != null) {
            hideTopBarSwitch.isChecked = sharedPref?.getBoolean("HideTopBar", false) ?: false
            shrinkTopBarSwitch.isChecked = sharedPref?.getBoolean("ShrinkTopBar", false) ?: false
        }
        tabletModeToggle.isChecked = config.tabletMode ?: false
        hideStartMessageToggle.isChecked = config.hideStartMessage ?: false
        decoupleNAVButtonToggle.isChecked = config.decoupleNAVBtn?: false
        nightBrightnessPercentageTV.text = "${config.nightBrightnessLevel ?: 0}%"
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
            if (it.isChecked)
                nightBrightnessPercentageTV.visibility = View.VISIBLE
            else
                nightBrightnessPercentageTV.visibility = View.GONE

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
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                nightBrightnessPercentageTV.text = "$progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                try {
                    coreServiceClient.coreService?.nightBrightnessLevel = nightBrightnessSeekBar.progress
                } catch (exception: Exception) {
                    val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                    alertExc.show()
                }
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