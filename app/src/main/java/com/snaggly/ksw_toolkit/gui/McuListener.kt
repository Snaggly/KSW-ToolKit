package com.snaggly.ksw_toolkit.gui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.snaggly.ksw_toolkit.IMcuListener
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.core.service.helper.CoreServiceClient
import com.snaggly.ksw_toolkit.gui.viewmodels.McuListenerViewModel
import com.snaggly.ksw_toolkit.util.list.mcu.McuData
import java.lang.NumberFormatException

class McuListener(private val coreServiceClient: CoreServiceClient) : Fragment() {

    private lateinit var viewModel: McuListenerViewModel
    private lateinit var mcuEventRV: RecyclerView
    private lateinit var mcuEventFilterRV: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mcu_listener_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(McuListenerViewModel::class.java)
        initElements()
    }

    override fun onResume() {
        super.onResume()
        coreServiceClient.coreService?.registerMcuListener(mcuObserver)
    }

    override fun onPause() {
        coreServiceClient.coreService?.unregisterMcuListener(mcuObserver)
        super.onPause()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) {
            viewModel.getLastSelectedEvent()?.let { addFilterElement(it) }
        }
        return super.onContextItemSelected(item)
    }

    private fun addFilterElement(mcuData: McuData) {
        if (!viewModel.mcuFilter.contains(mcuData))
            viewModel.mcuFilter.add(mcuData)
        viewModel.addFilterEntryToAdapter(mcuData)
    }

    private fun initElements() {
        mcuEventRV = requireView().findViewById(R.id.McuEventsRV);
        mcuEventRV.layoutManager = LinearLayoutManager(context)
        mcuEventRV.adapter = viewModel.getMcuEventAdapter()

        mcuEventFilterRV = requireView().findViewById(R.id.filterListRV);
        mcuEventFilterRV.layoutManager = LinearLayoutManager(context)
        mcuEventFilterRV.adapter = viewModel.getMcuEventFilterAdapter()

        val filterNoticeTV = requireView().findViewById<TextView>(R.id.noFiltersNoticeTV).apply {
            viewModel.filterListChangedObserver = {
                visibility = if (mcuEventFilterRV.visibility == View.GONE || viewModel.mcuFilter.size > 0)
                    View.GONE
                else
                    View.VISIBLE
            }
        }

        requireView().findViewById<Button>(R.id.showFiltersBtn).apply {
            setOnClickListener {
                if (mcuEventFilterRV.visibility == View.GONE) {
                    mcuEventFilterRV.visibility = View.VISIBLE
                    this.setText(R.string.hide_filters)
                    filterNoticeTV.visibility = if (viewModel.mcuFilter.size > 0)
                        View.GONE
                    else
                        View.VISIBLE
                }
                else {
                    mcuEventFilterRV.visibility = View.GONE
                    this.setText(R.string.show_filters)
                    filterNoticeTV.visibility = View.GONE
                }
            }
        }

        requireView().findViewById<Button>(R.id.clearMcuEventsBtn).apply {
            setOnClickListener {
                viewModel.clearEvents()
            }
        }

        //Debug
        val cmdInput = requireView().findViewById<TextInputEditText>(R.id.cmdTypeEditTxt)
        val dataInput = requireView().findViewById<TextInputEditText>(R.id.dataEditText)
        val sendBtn = requireView().findViewById<Button>(R.id.sendCmdBtn)
        sendBtn.setOnClickListener {
            val cmdTypeStr = cmdInput.text.toString()
            if (cmdTypeStr.isBlank()){
                return@setOnClickListener
            }
            val cmdType : Int
            try {
                cmdType = cmdTypeStr.toInt()
            } catch (nfe : NumberFormatException) {
                return@setOnClickListener
            }
            val dataStr = dataInput.text.toString()
            if (dataStr.isBlank()){
                return@setOnClickListener
            }
            val dataSplitStr = dataStr.split(',')
            if (dataSplitStr.isEmpty()) {
                return@setOnClickListener
            }
            val data = ByteArray(dataSplitStr.size)
            try {
                for (i in data.indices) {
                    data[i] = dataSplitStr[i].toByte()
                }
            } catch (nfe : NumberFormatException) {
                return@setOnClickListener
            }
            coreServiceClient.coreService?.sendMcuCommand(cmdType, data)
        }
    }

    private val mcuObserver = object : IMcuListener.Stub() {
        override fun updateMcu(eventName: String?, cmdType: Int, data: ByteArray?) {
            if (data != null) {
                if (data.size > 2) {
                    if (cmdType == 0xA1 && data[0] == 0x17.toByte() && data[2] == 0x0.toByte())
                        return
                }

                val eventPName = eventName ?: "Unknown Event"
                val mcuData = McuData(eventPName, cmdType, data)
                if (viewModel.mcuFilter.contains(mcuData))
                    return
                requireActivity().runOnUiThread {
                    viewModel.addEntryToAdapter(mcuData)
                }
            }
        }
    }
}