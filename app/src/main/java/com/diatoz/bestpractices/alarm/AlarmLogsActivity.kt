package com.diatoz.bestpractices.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.diatoz.bestpractices.database.AppDatabase
import com.diatoz.bestpractices.databinding.ActivityAlarmLogsBinding
import kotlinx.coroutines.launch

class AlarmLogsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmLogsBinding

    private lateinit var adapterAlarmLogs: AdapterAlarmLogs

    private val myDatabase: AppDatabase by lazy { AppDatabase.invoke(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmLogsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lifecycleScope.launch {
            val list = myDatabase.alarmLogsDao().getLogs()

            adapterAlarmLogs = AdapterAlarmLogs(this@AlarmLogsActivity, list)
            binding.rvLogs.layoutManager = LinearLayoutManager(this@AlarmLogsActivity)
            binding.rvLogs.adapter = adapterAlarmLogs
        }

        binding.backArrowScheduleTimer.setOnClickListener { onBackPressed() }
    }

}