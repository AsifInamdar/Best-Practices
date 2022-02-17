package com.diatoz.bestpractices.alarm

import android.app.Application
import androidx.lifecycle.LiveData
import com.diatoz.bestpractices.database.AppDatabase
import com.diatoz.bestpractices.database.ScheduleEntity
import kotlinx.coroutines.delay

class LocalRepository(context: Application) {

    val schedules: LiveData<List<ScheduleEntity>>

    init {
        val database = context.applicationContext?.let { AppDatabase.invoke(it) }!!
        schedules = database.scheduleDao().getSchedule()
    }

    suspend fun loop(){
        for (i in 1..10){
            println("i value is $i")
            delay(1000)
        }
    }

}