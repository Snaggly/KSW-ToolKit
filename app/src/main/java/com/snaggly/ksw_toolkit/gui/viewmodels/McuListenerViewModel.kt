package com.snaggly.ksw_toolkit.gui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.snaggly.ksw_toolkit.util.adapters.McuEventRVAdapter
import com.snaggly.ksw_toolkit.util.list.mcu.McuData

class McuListenerViewModel(application: Application) : AndroidViewModel(application) {

    private var mcuEventRVAdapter: McuEventRVAdapter = McuEventRVAdapter(null)
    private var mcuEventFilterRVAdapter: McuEventRVAdapter = McuEventRVAdapter { mcuData ->
        if (mcuData == null)
            return@McuEventRVAdapter
        mcuFilter.remove(mcuData)
        removeFilterEntryFromAdapter(mcuData)
    }

    val mcuFilter = ArrayList<McuData>()
    var filterListChangedObserver : (() -> Unit)? = null

    fun getLastSelectedEvent() : McuData? {
        return mcuEventRVAdapter.lastSelectedMcuData
    }

    fun getMcuEventAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder> {
        return mcuEventRVAdapter
    }
    fun getMcuEventFilterAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder> {
        return mcuEventFilterRVAdapter
    }

    fun addEntryToAdapter(mcuEvent: McuData) {
        mcuEventRVAdapter.addNewEntry(mcuEvent)
    }

    fun addFilterEntryToAdapter(mcuEvent: McuData) {
        mcuEventFilterRVAdapter.addNewEntry(mcuEvent)
        filterListChangedObserver?.invoke()
    }

    fun clearEvents() {
        mcuEventRVAdapter.clear()
    }

    private fun removeFilterEntryFromAdapter(mcuEvent: McuData) {
        mcuEventFilterRVAdapter.removeEntry(mcuEvent)
        filterListChangedObserver?.invoke()
    }
}