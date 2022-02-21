package com.diatoz.bestpractices.alarm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.diatoz.bestpractices.R
import com.diatoz.bestpractices.database.ScheduleEntity

class AdapterScheduleList(val context: Context, private val list: List<ScheduleEntity>) :
    RecyclerView.Adapter<AdapterScheduleList.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTime: TextView = view.findViewById(R.id.tv_time)
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvSun: TextView = view.findViewById(R.id.day_sun)
        val tvMon: TextView = view.findViewById(R.id.day_mon)
        val tvTue: TextView = view.findViewById(R.id.day_tue)
        val tvWed: TextView = view.findViewById(R.id.day_wed)
        val tvThu: TextView = view.findViewById(R.id.day_thu)
        val tvFri: TextView = view.findViewById(R.id.day_fri)
        val tvSat: TextView = view.findViewById(R.id.day_sat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.adapter_schedule_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val time = get12HourFormat(
            list[position].scheduleStartHourTime,
            list[position].scheduleStartMinuteTime
        )
        holder.tvTime.text = time
        holder.tvName.text = list[position].scheduleName

        if (list[position].sunday)
            holder.tvSun.setTextColor(ContextCompat.getColor(context, R.color.theme_positive_color))

        if (list[position].monday)
            holder.tvMon.setTextColor(ContextCompat.getColor(context, R.color.theme_positive_color))

        if (list[position].tuesday)
            holder.tvTue.setTextColor(ContextCompat.getColor(context, R.color.theme_positive_color))

        if (list[position].wednesday)
            holder.tvWed.setTextColor(ContextCompat.getColor(context, R.color.theme_positive_color))

        if (list[position].thusday)
            holder.tvThu.setTextColor(ContextCompat.getColor(context, R.color.theme_positive_color))

        if (list[position].friday)
            holder.tvFri.setTextColor(ContextCompat.getColor(context, R.color.theme_positive_color))

        if (list[position].saturday)
            holder.tvSat.setTextColor(ContextCompat.getColor(context, R.color.theme_positive_color))
    }

    override fun getItemCount(): Int {
        return list.size
    }

}