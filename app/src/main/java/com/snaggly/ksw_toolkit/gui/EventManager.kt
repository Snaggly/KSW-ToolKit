package com.snaggly.ksw_toolkit.gui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import com.snaggly.ksw_toolkit.gui.viewmodels.EventManagerViewModel
import com.snaggly.ksw_toolkit.util.list.eventtype.EventManagerTypes
import com.snaggly.ksw_toolkit.util.list.eventtype.EventMode

class EventManager(private val coreServiceClient: CoreServiceClient) : Fragment() {
    private var previousBtn : Button? = null
    private var previousTypes: EventManagerTypes? = null

    private lateinit var resetDefaultBtn : Button

    private lateinit var telephoneBtn : Button
    private lateinit var telephonePickUpBtn : Button
    private lateinit var telephoneHangUpBtn : Button
    private lateinit var telephoneLongBtn : Button
    private lateinit var voiceBtn : Button
    private lateinit var mediaPreviousBtn : Button
    private lateinit var mediaNextBtn : Button
    private lateinit var mediaPauseBtn : Button
    private lateinit var volumeDecBtn : Button
    private lateinit var volumeIncBtn : Button
    private lateinit var modeBtn : Button
    private lateinit var knobPressBtn : Button
    private lateinit var knobTiltUpBtn : Button
    private lateinit var knobTiltDownBtn : Button
    private lateinit var knobTiltLeftBtn : Button
    private lateinit var knobTiltRightBtn : Button
    private lateinit var knobTurnLeftBtn : Button
    private lateinit var knobTurnRightBtn : Button
    private lateinit var menuBtn : Button
    private lateinit var backBtn : Button
    private lateinit var optionsBtn : Button
    private lateinit var naviBtn : Button
    private lateinit var hiCarAppBtn : Button
    private lateinit var hiCarVoiceBtn : Button

    private lateinit var activeFragment: EventManagerSelectAction
    private lateinit var mViewModel: EventManagerViewModel

    private var hasOpenedSelectAction = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.event_manager_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(EventManagerViewModel::class.java)
        mViewModel.coreServiceClient = coreServiceClient
        initBtns()
        voiceBtn.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        initBtnClick()
    }

    private fun initBtns() {
        resetDefaultBtn = requireView().findViewById(R.id.resetDefaultsBtn)
        telephoneBtn = requireView().findViewById(R.id.telefonBtn)
        telephonePickUpBtn = requireView().findViewById(R.id.telefonPickUpBtn)
        telephoneHangUpBtn = requireView().findViewById(R.id.telefonHangUpBtn)
        telephoneLongBtn = requireView().findViewById(R.id.telefonLongPressBtn)
        voiceBtn = requireView().findViewById(R.id.voiceBtn)
        mediaPreviousBtn = requireView().findViewById(R.id.mediaPreviousBtn)
        mediaNextBtn = requireView().findViewById(R.id.mediaNextBtn)
        mediaPauseBtn = requireView().findViewById(R.id.mediaPauseBtn)
        volumeDecBtn = requireView().findViewById(R.id.volumeDecBtn)
        volumeIncBtn = requireView().findViewById(R.id.volumeIncBtn)
        modeBtn = requireView().findViewById(R.id.modeBtn)
        knobPressBtn = requireView().findViewById(R.id.knobPressBtn)
        knobTiltUpBtn = requireView().findViewById(R.id.knobTiltUpBtn)
        knobTiltDownBtn = requireView().findViewById(R.id.knobTiltDownBtn)
        knobTiltLeftBtn = requireView().findViewById(R.id.knobTiltLeftBtn)
        knobTiltRightBtn = requireView().findViewById(R.id.knobTiltRightBtn)
        knobTurnLeftBtn = requireView().findViewById(R.id.knobTurnLeftBtn)
        knobTurnRightBtn = requireView().findViewById(R.id.knobTurnRightBtn)
        menuBtn = requireView().findViewById(R.id.menuButtonBtn)
        backBtn = requireView().findViewById(R.id.backButtonBtn)
        optionsBtn = requireView().findViewById(R.id.optionsButtonBtn)
        naviBtn = requireView().findViewById(R.id.navigationButtonBtn)
        hiCarAppBtn = requireView().findViewById(R.id.hiCarAppBtn)
        hiCarVoiceBtn = requireView().findViewById(R.id.hiCarVoiceBtn)
    }

    private fun initBtnClick() {
        resetDefaultBtn.setOnClickListener {
            coreServiceClient.coreService?.setDefaultBtnLayout()
            initBtnClick()
        }
        setOnClickEvent(telephoneBtn, EventManagerTypes.TelephoneButton)
        setOnClickEvent(telephonePickUpBtn, EventManagerTypes.TelephoneButtonPickUp)
        setOnClickEvent(telephoneHangUpBtn, EventManagerTypes.TelephoneButtonHangUp)
        setOnClickEvent(telephoneLongBtn, EventManagerTypes.TelephoneButtonLongPress)
        setOnClickEvent(voiceBtn, EventManagerTypes.VoiceCommandButton)
        setOnClickEvent(mediaPreviousBtn, EventManagerTypes.MediaPrevious)
        setOnClickEvent(mediaNextBtn, EventManagerTypes.MediaNext)
        setOnClickEvent(mediaPauseBtn, EventManagerTypes.MediaPlayPause)
        setOnClickEvent(volumeDecBtn, EventManagerTypes.VolumeDecrease)
        setOnClickEvent(volumeIncBtn, EventManagerTypes.VolumeIncrease)
        setOnClickEvent(modeBtn, EventManagerTypes.ModeButton)
        setOnClickEvent(knobPressBtn, EventManagerTypes.KnobPress)
        setOnClickEvent(knobTiltUpBtn, EventManagerTypes.KnobTiltUp)
        setOnClickEvent(knobTiltDownBtn, EventManagerTypes.KnobTiltDown)
        setOnClickEvent(knobTiltLeftBtn, EventManagerTypes.KnobTiltLeft)
        setOnClickEvent(knobTiltRightBtn, EventManagerTypes.KnobTiltRight)
        setOnClickEvent(knobTurnLeftBtn, EventManagerTypes.KnobTurnLeft)
        setOnClickEvent(knobTurnRightBtn, EventManagerTypes.KnobTurnRight)
        setOnClickEvent(menuBtn, EventManagerTypes.MenuButton)
        setOnClickEvent(backBtn, EventManagerTypes.BackButton)
        setOnClickEvent(optionsBtn, EventManagerTypes.OptionsButton)
        setOnClickEvent(naviBtn, EventManagerTypes.NavigationButton)
        setOnClickEvent(hiCarAppBtn, EventManagerTypes.HiCarAppButton)
        setOnClickEvent(hiCarVoiceBtn, EventManagerTypes.HiCarVoiceButton)
    }

    private fun setBtnLabel(button: Button, eventManagerTypes: EventManagerTypes) {
        if (mViewModel.getConfig()?.get(eventManagerTypes) != null && mViewModel.getConfig()?.get(eventManagerTypes)?.eventMode != EventMode.NoAssignment)
            button.text = getString(R.string.assigned)
        else
            button.text = getString(R.string.unassigned)
    }

    private fun setOnClickEvent(button: Button, types: EventManagerTypes) {
        setBtnLabel(button, types)
        button.setOnClickListener {
            if (hasOpenedSelectAction)
                previousBtn?.let { setBtnLabel(it, previousTypes!!) }
            previousBtn = button
            previousTypes = types
            hasOpenedSelectAction = true
            activeFragment = EventManagerSelectAction(coreServiceClient, types, object : OnActionResult {
                override fun notifyView() {
                    setBtnLabel(button, types)
                    childFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_enter_right_left, R.anim.slide_exit_right_left)
                            .remove(activeFragment)
                            .commit()
                    button.requestFocus()
                    hasOpenedSelectAction = false
                }
            }, mViewModel.getConfig()?.get(types))
            button.text = "..."
            childFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_enter_right_left, R.anim.slide_exit_right_left)
                    .replace(R.id.eventManagerSelectActionFrame, activeFragment)
                    .commit()
        }
    }

    interface OnActionResult{
        fun notifyView()
    }
}