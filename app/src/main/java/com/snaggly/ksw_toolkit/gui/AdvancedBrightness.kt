package com.snaggly.ksw_toolkit.gui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import java.util.*

class AdvancedBrightness(val coreServiceClient: CoreServiceClient) : Fragment() {
    private lateinit var enableAdvBrightnessTimeSW : SwitchCompat
    private lateinit var enableAdvBrightnessUSBSW : SwitchCompat
    private lateinit var advBrightnessTimeRoot : LinearLayout
    private lateinit var sunriseTimeEditText: EditText
    private lateinit var sunsetTimeEditText: EditText
    private lateinit var autoTimesCheckBox: CheckBox
    private lateinit var dayBrightnessPercentageTV : TextView
    private lateinit var dayBrightnessSeekBar: SeekBar
    private lateinit var dayBrightnessHLPercentageTV : TextView
    private lateinit var dayBrightnessHLSeekBar: SeekBar
    private lateinit var nightBrightnessPercentageTV : TextView
    private lateinit var nightBrightnessSeekBar: SeekBar
    private lateinit var nightBrightnessHLPercentageTV : TextView
    private lateinit var nightBrightnessHLSeekBar: SeekBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.adv_brightness_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        initElements()
        initButtonClickEvents()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        enableAdvBrightnessTimeSW.isChecked = coreServiceClient.coreService?.advBri_IsTimeBased?: false
        enableAdvBrightnessUSBSW.isChecked = coreServiceClient.coreService?.advBri_IsUSBBased?: false
        sunriseTimeEditText.setText(coreServiceClient.coreService?.advBri_SunriseAt?: "06:00")
        sunsetTimeEditText.setText(coreServiceClient.coreService?.advBri_SunsetAt?: "20:00")
        autoTimesCheckBox.isChecked = coreServiceClient.coreService?.advBri_Autotimes?: true

        dayBrightnessSeekBar.progress = coreServiceClient.coreService?.advBri_DaylightBri?: 100
        dayBrightnessHLSeekBar.progress = coreServiceClient.coreService?.advBri_DaylightHLBri?: 85
        nightBrightnessSeekBar.progress = coreServiceClient.coreService?.advBri_NightBri?: 50
        nightBrightnessHLSeekBar.progress = coreServiceClient.coreService?.advBri_NightHLBri?: 30

        switchMode()
    }

    private fun initElements() {
        enableAdvBrightnessTimeSW = requireView().findViewById(R.id.enableAdvBrightnessTimeSW)
        enableAdvBrightnessUSBSW = requireView().findViewById(R.id.enableAdvBrightnessUSBSW)
        advBrightnessTimeRoot = requireView().findViewById(R.id.adv_brightness_time_fragment_ll_root)
        sunriseTimeEditText = requireView().findViewById(R.id.sunriseTimeET)
        sunriseTimeEditText.inputType = InputType.TYPE_NULL
        sunsetTimeEditText = requireView().findViewById(R.id.sunsetTimeET)
        sunsetTimeEditText.inputType = InputType.TYPE_NULL
        autoTimesCheckBox = requireView().findViewById(R.id.autoTimesCheckBox)
        dayBrightnessPercentageTV = requireView().findViewById(R.id.dayBrightnessPercentageTV)
        dayBrightnessSeekBar = requireView().findViewById(R.id.dayBrightnessSeekBar)
        dayBrightnessHLPercentageTV = requireView().findViewById(R.id.dayBrightnessHLPercentageTV)
        dayBrightnessHLSeekBar = requireView().findViewById(R.id.dayBrightnessHLSeekBar)
        nightBrightnessPercentageTV = requireView().findViewById(R.id.nightBrightnessPercentageTV)
        nightBrightnessSeekBar = requireView().findViewById(R.id.nightBrightnessSeekBar)
        nightBrightnessHLPercentageTV = requireView().findViewById(R.id.nightBrightnessHLPercentageTV)
        nightBrightnessHLSeekBar = requireView().findViewById(R.id.nightBrightnessHLSeekBar)
    }

    private fun switchMode() {
        for (child in advBrightnessTimeRoot.children) {
            child.isVisible = enableAdvBrightnessTimeSW.isChecked
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initButtonClickEvents() {
        enableAdvBrightnessTimeSW.setOnClickListener {
            enableAdvBrightnessUSBSW.isChecked = !enableAdvBrightnessTimeSW.isChecked
            switchMode()
            try {
                coreServiceClient.coreService?.advBri_IsTimeBased = enableAdvBrightnessTimeSW.isChecked
                coreServiceClient.coreService?.advBri_IsUSBBased = !enableAdvBrightnessTimeSW.isChecked
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit")
                    .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        enableAdvBrightnessUSBSW.setOnClickListener {
            enableAdvBrightnessTimeSW.isChecked = !enableAdvBrightnessUSBSW.isChecked
            switchMode()
            try {
                coreServiceClient.coreService?.advBri_IsUSBBased = enableAdvBrightnessUSBSW.isChecked
                coreServiceClient.coreService?.advBri_IsTimeBased = !enableAdvBrightnessUSBSW.isChecked
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit")
                    .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        sunsetTimeEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, sH, sMin ->
                    sunsetTimeEditText.setText("${String.format("%02d", sH)}:${String.format("%02d", sMin)}")
                    try {
                        coreServiceClient.coreService?.advBri_SunsetAt = sunsetTimeEditText.text.toString()
                    } catch (exception: Exception) {
                        val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit")
                            .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                        alertExc.show()
                    }
                },
                hour,
                minutes,
                true)
            timePicker.show()
        }

        sunriseTimeEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, sH, sMin ->
                    sunriseTimeEditText.setText("${String.format("%02d", sH)}:${String.format("%02d", sMin)}")
                    try {
                        coreServiceClient.coreService?.advBri_SunriseAt = sunriseTimeEditText.text.toString()
                    } catch (exception: Exception) {
                        val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit")
                            .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                        alertExc.show()
                    }
                },
                hour,
                minutes,
                true)
            timePicker.show()
        }

        autoTimesCheckBox.setOnClickListener {
            try {
                coreServiceClient.coreService?.advBri_Autotimes = autoTimesCheckBox.isChecked
            } catch (exception: Exception) {
                val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit")
                    .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                alertExc.show()
            }
        }

        dayBrightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                dayBrightnessPercentageTV.text = "$progress%"
                try {
                    coreServiceClient.coreService?.advBri_DaylightBri = progress
                } catch (exception: Exception) {
                    val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                    alertExc.show()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        dayBrightnessHLSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                dayBrightnessHLPercentageTV.text = "$progress%"
                try {
                    coreServiceClient.coreService?.advBri_DaylightHLBri = progress
                } catch (exception: Exception) {
                    val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                    alertExc.show()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        nightBrightnessSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                nightBrightnessPercentageTV.text = "$progress%"
                try {
                    coreServiceClient.coreService?.advBri_NightBri = progress
                } catch (exception: Exception) {
                    val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                    alertExc.show()
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        nightBrightnessHLSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                nightBrightnessHLPercentageTV.text = "$progress%"
                try {
                    coreServiceClient.coreService?.advBri_NightHLBri = progress
                } catch (exception: Exception) {
                    val alertExc = AlertDialog.Builder(activity, R.style.alertDialogNight).setTitle("KSW-ToolKit")
                        .setMessage("Could not restart McuReader!\n\n${exception.stackTrace}").create()
                    alertExc.show()
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { }
            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })
    }
}