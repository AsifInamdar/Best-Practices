package com.diatoz.bestpractices.database

import androidx.room.*

@Dao
interface AlarmLogsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(
        log: AlarmLogs
    )

    @Query("SELECT * FROM alarm_logs")
    suspend fun getLogs(): MutableList<AlarmLogs>
}