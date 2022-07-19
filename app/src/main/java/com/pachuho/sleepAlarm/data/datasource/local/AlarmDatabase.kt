package com.pachuho.sleepAlarm.data.datasource.local

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.utils.DayTypeConverter

@Database(entities = [Alarm::class], version = 3)
@TypeConverters(
    value = [
        DayTypeConverter::class
    ]
)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao() : AlarmDao

    companion object {
        private var instance: AlarmDatabase? = null

        @Synchronized
        fun getInstance(context: Context, gson: Gson): AlarmDatabase? {
            if (instance == null) {
                synchronized(AlarmDatabase::class){
                    instance = Room
                        .databaseBuilder(context.applicationContext, AlarmDatabase::class.java, "alarm-database")
                        .addTypeConverter(DayTypeConverter(gson))
                        .build()
                }
            }
            return instance
        }
    }
}