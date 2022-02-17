package com.diatoz.bestpractices.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_table")
class ScheduleEntity(
    @PrimaryKey
    var id: Int = 0,
    var scheduleName: String? = null,
    var scheduleStartHourTime: Int = 0,
    var scheduleEndHourTime: Int = 0,
    var scheduleStartMinuteTime: Int = 0,
    var scheduleEndMinuteTime: Int = 0,
    var sunday: Boolean = false,
    var monday: Boolean = false,
    var tuesday: Boolean = false,
    var wednesday: Boolean = false,
    var thusday: Boolean = false,
    var friday: Boolean = false,
    var saturday: Boolean = false,
    var isSelected: Boolean = false,
    var startAlarmId: Int = 0,
    var endAlarmId: Int = 0,
    var timeLapse: Int = 0,
)