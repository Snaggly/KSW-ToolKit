package com.snaggly.ksw_toolkit.util.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snaggly.ksw_toolkit.R

class McuEventRVAdapter : RecyclerView.Adapter<McuEventRVAdapter.McuEventViewHolder>() {
    private var names = ArrayList<String>(arrayListOf(""))
    private var datas = ArrayList<String>(arrayListOf(""))

    inner class McuEventViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName : TextView = itemView.findViewById(R.id.mcuEventName)
        val dataString : TextView = itemView.findViewById(R.id.mcuDataString)
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

    override fun onBindViewHolder(holder: McuEventViewHolder, position: Int) {
        holder.eventName.text = names[position]
        holder.dataString.text = datas[position]
    }

    override fun getItemCount(): Int {
        return names.size;
    }

    fun addNewEntry(name: String, dataString: String) {
        names.add(1, name)
        datas.add(1, dataString)
        notifyItemInserted(1)
    }
}