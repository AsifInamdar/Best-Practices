package com.diatoz.bestpractices.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.diatoz.bestpractices.database.AlarmLogs
import com.diatoz.bestpractices.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class AlarmReceiver : BroadcastReceiver(), CoroutineScope {
    lateinit var job: Job

    lateinit var myDatabase: AppDatabase

    override fun onReceive(context: Context, intent: Intent) {

        myDatabase = AppDatabase.invoke(context)
        job = Job()

        launch {

            val scheduleId = intent.getIntExtra("scheduleId", 0)
            Log.e("AlarmReceiver", "scheduleId : $scheduleId")

            val entity = myDatabase.scheduleDao().getScheduleBYId(scheduleId)

            val calendar = Calendar.getInstance()

            val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
            var shouldChange = false
            if (currentDay == 1 && entity.sunday) {
                shouldChange = true
            } else if (currentDay == 2 && entity.monday) {
                shouldChange = true
            } else if (currentDay == 3 && entity.tuesday) {
                shouldChange = true
            } else if (currentDay == 4 && entity.wednesday) {
                shouldChange = true
            } else if (currentDay == 5 && entity.thusday) {
                shouldChange = true
            } else if (currentDay == 6 && entity.friday) {
                shouldChange = true
            } else if (currentDay == 7 && entity.saturday) {
                shouldChange = true
            }
            Log.e("AlarmReceiver", "shouldChange : $shouldChange")

            val log = AlarmLogs(
                time = System.currentTimeMillis().toString(),
                schedule = entity.scheduleName!!
            )
            myDatabase.alarmLogsDao().insertLog(log)

            var day = calendar.get(Calendar.DAY_OF_MONTH) + 1

            setAlarm(
                scheduleId,
                entity.scheduleStartHourTime,
                entity.scheduleStartMinuteTime,
                scheduleId,
                context,
                day
            )
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}