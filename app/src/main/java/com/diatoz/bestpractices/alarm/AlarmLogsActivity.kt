package com.diatoz.bestpractices.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.diatoz.bestpractices.R
import com.diatoz.bestpractices.database.AppDatabase
import kotlinx.android.synthetic.main.activity_alarm_logs.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AlarmLogsActivity : AppCompatActivity() {

    private lateinit var adapterAlarmLogs: AdapterAlarmLogs

    private val myDatabase: AppDatabase by lazy { AppDatabase.invoke(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_logs)

        lifecycleScope.launch {
            val list = myDatabase.alarmLogsDao().getLogs()

            adapterAlarmLogs = AdapterAlarmLogs(this@AlarmLogsActivity, list)
            rv_logs.layoutManager = LinearLayoutManager(this@AlarmLogsActivity)
            rv_logs.adapter = adapterAlarmLogs
        }

        backArrowScheduleTimer.setOnClickListener { onBackPressed() }
    }

}