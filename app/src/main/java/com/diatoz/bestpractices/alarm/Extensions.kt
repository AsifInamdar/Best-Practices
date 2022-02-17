package com.diatoz.bestpractices.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun setAlarm(
    alarmId: Int,
    hour: Int,
    minute: Int,
    scheduleId: Int,
    context: Context,
    day: Int
) {

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Intent part
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("alarmId", alarmId)
    intent.putExtra("scheduleId", scheduleId)

    val pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0)

    val calendarEnd: Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    /*alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendarEnd.timeInMillis,
        pendingIntent
    )*/
    alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(calendarEnd.timeInMillis, pendingIntent), pendingIntent)
    debug("Alarm", "has been set for $hour : $minute for alarm id : $alarmId")
}

fun debug(tag: String, message: String) {
    Log.e(tag, message)
}

fun get12HourFormat(hour: Int, minute: Int): String {
    val _24HourTime = "$hour:$minute"
    val _24HourSDF = SimpleDateFormat("HH:mm")
    val _12HourSDF = SimpleDateFormat("hh:mm a")
    val _24HourDt: Date = _24HourSDF.parse(_24HourTime)
    return _12HourSDF.format(_24HourDt)
}

fun getDateTime(milliSeconds: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return formatter.format(calendar.time)
}