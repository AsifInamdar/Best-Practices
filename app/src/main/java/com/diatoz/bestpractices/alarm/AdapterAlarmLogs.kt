package com.diatoz.bestpractices.alarm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diatoz.bestpractices.R
import com.diatoz.bestpractices.database.AlarmLogs

class AdapterAlarmLogs(val context: Context, private val list: MutableList<AlarmLogs>) :
    RecyclerView.Adapter<AdapterAlarmLogs.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTime: TextView = view.findViewById(R.id.tv_time)
        val tvName: TextView = view.findViewById(R.id.tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.adapter_alarm_logs, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTime.text = getDateTime(list[position].time.toLong())
        holder.tvName.text = list[position].schedule
    }

    override fun getItemCount(): Int {
        return list.size
    }

}