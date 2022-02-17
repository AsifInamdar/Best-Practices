package com.diatoz.bestpractices.alarm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.diatoz.bestpractices.R
import kotlinx.android.synthetic.main.activity_schedule_list.*

class ScheduleListActivity : AppCompatActivity() {

    private lateinit var adapterScheduleList: AdapterScheduleList

    private val viewModel: AlarmViewModel by lazy { AlarmViewModel(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_list)

        viewModel.scheduleList.observe(this) {
            adapterScheduleList = AdapterScheduleList(this@ScheduleListActivity, it)
            rv_schedules.layoutManager = LinearLayoutManager(this@ScheduleListActivity)
            rv_schedules.adapter = adapterScheduleList
            adapterScheduleList.notifyDataSetChanged()
        }

        fab_add_schedule.setOnClickListener {
            startActivity(Intent(this, AddTimeActivity::class.java))
        }

        backArrowScheduleTimer.setOnClickListener { onBackPressed() }

        iv_logs.setOnClickListener {
            startActivity(Intent(this, AlarmLogsActivity::class.java))
        }
    }

}