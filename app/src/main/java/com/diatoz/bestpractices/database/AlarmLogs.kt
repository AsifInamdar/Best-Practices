package com.diatoz.bestpractices.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_logs")
class AlarmLogs(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var time: String,
    var schedule: String
)