package com.snaggly.ksw_toolkit.gui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.snaggly.ksw_toolkit.util.adapters.McuEventRVAdapter

class McuListenerViewModel(application: Application) : AndroidViewModel(application) {

    private var mcuEventRVAdapter: McuEventRVAdapter = McuEventRVAdapter()

    fun getMcuEventAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder> {
        return mcuEventRVAdapter
    }

    fun addEntryToAdapter(eventName: String, dataString: String) {
        mcuEventRVAdapter.addNewEntry(eventName, dataString)
    }

    fun dataBytesToString(data: ByteArray): String {
        var result = ""
        if (data.isEmpty())
            return result
        for (i in 0..data.size - 2) {
            result += data[i].toString(16) + "-"
        }
        result += data[data.size - 1].toString(16)

        return result
    }
}