package com.diatoz.bestpractices.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ScheduleEntity::class, AlarmLogs::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scheduleDao(): AlarmDao
    abstract fun alarmLogsDao(): AlarmLogsDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "TestDatabase.db"
            ).fallbackToDestructiveMigration().build()

    }
}