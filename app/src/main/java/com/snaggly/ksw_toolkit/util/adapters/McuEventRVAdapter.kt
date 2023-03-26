package com.snaggly.ksw_toolkit.util.adapters

import android.annotation.SuppressLint
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snaggly.ksw_toolkit.R
import com.snaggly.ksw_toolkit.util.list.mcu.McuData

class McuEventRVAdapter(val removeFilterCallback : ((mcuData: McuData?) -> Unit)?) : RecyclerView.Adapter<McuEventRVAdapter.McuEventViewHolder>() {
    private var mcuEvents = ArrayList<McuData>(arrayListOf(McuData("", 0, byteArrayOf())))

    var lastSelectedMcuData : McuData? = null

    inner class McuEventViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        var holdingMcuData: McuData? = null
        val eventName : TextView = itemView.findViewById(R.id.mcuEventName)
        val dataString : TextView = itemView.findViewById(R.id.mcuDataString)
        private val removeBtn = itemView.findViewById<View>(R.id.mcuEventRemoveFromFilterBtn).apply {
            if (removeFilterCallback != null) visibility = View.VISIBLE
        }

        init {
            if (removeFilterCallback == null) {
                itemView.setOnCreateContextMenuListener(this)
                itemView.setOnClickListener { itemView.showContextMenu() }
            }
            else {
                itemView.setOnClickListener { removeFilterCallback.invoke(holdingMcuData) }
                removeBtn.setOnClickListener { removeFilterCallback.invoke(holdingMcuData) }
            }
        }

        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            menu?.apply {
                lastSelectedMcuData = holdingMcuData
                add(Menu.NONE, 1, Menu.NONE, "Add $holdingMcuData to filter?")
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position > 0) 1
        else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): McuEventViewHolder {
        return when (viewType) {
            1 -> McuEventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.mcu_event, parent, false))
            else -> McuEventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dummy_list, parent, false))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: McuEventViewHolder, position: Int) {
        val currentMcuData = mcuEvents[position]
        holder.holdingMcuData = mcuEvents[position]
        holder.eventName.text = "${currentMcuData.eventName} - ${currentMcuData.cmdType}"
        holder.dataString.text = currentMcuData.dataBytesToString(currentMcuData.bytes)
    }

    override fun onViewRecycled(holder: McuEventViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        return mcuEvents.size;
    }

    fun addNewEntry(mcuEvent: McuData) {
        mcuEvents.add(1, mcuEvent)
        notifyItemInserted(1)
    }

    fun removeEntry(mcuEvent: McuData) {
        val index = mcuEvents.indexOf(mcuEvent)
        mcuEvents.remove(mcuEvent)
        notifyItemRemoved(index)
    }

    fun clear() {
        val count = mcuEvents.size
        mcuEvents.clear()
        mcuEvents.addAll(arrayListOf(McuData("", 0, byteArrayOf())))
        notifyItemRangeRemoved(0, count)
    }
}