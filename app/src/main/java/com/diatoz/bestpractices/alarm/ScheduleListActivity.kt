package com.diatoz.bestpractices.alarm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.diatoz.bestpractices.databinding.ActivityScheduleListBinding

class ScheduleListActivity : AppCompatActivity() {

    private lateinit var adapterScheduleList: AdapterScheduleList
    private lateinit var binding: ActivityScheduleListBinding

    private val viewModel: AlarmViewModel by lazy { AlarmViewModel(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.scheduleList.observe(this) {
            adapterScheduleList = AdapterScheduleList(this@ScheduleListActivity, it)
            binding.rvSchedules.layoutManager = LinearLayoutManager(this@ScheduleListActivity)
            binding.rvSchedules.adapter = adapterScheduleList
            adapterScheduleList.notifyDataSetChanged()
        }

        binding.fabAddSchedule.setOnClickListener {
            startActivity(Intent(this, AddTimeActivity::class.java))
        }

        binding.backArrowScheduleTimer.setOnClickListener { onBackPressed() }

        binding.ivLogs.setOnClickListener {
            startActivity(Intent(this, AlarmLogsActivity::class.java))
        }
    }

}