package com.diatoz.bestpractices.alarm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diatoz.bestpractices.database.ScheduleEntity
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : ViewModel() {

    private val localRepository = LocalRepository(application)
    val scheduleList: LiveData<List<ScheduleEntity>> = localRepository.schedules

    fun callLoop() {
        viewModelScope.launch {
            localRepository.loop()
            println("printline")
        }
    }
}