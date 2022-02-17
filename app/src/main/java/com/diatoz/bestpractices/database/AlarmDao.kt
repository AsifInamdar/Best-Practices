package com.diatoz.bestpractices.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(
        scheduleEntity: ScheduleEntity
    )

    @Query("SELECT * FROM schedule_table")
    fun getSchedule(): LiveData<List<ScheduleEntity>>


    @Query("DELETE FROM schedule_table WHERE id = :id")
    suspend fun deleteSchedule(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSchedule(
        scheduleEntity: ScheduleEntity
    )

    @Query("SELECT * FROM schedule_table WHERE id = :id")
    suspend fun getScheduleBYId(id: Int): ScheduleEntity
}