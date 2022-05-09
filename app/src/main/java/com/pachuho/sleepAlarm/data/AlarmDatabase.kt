package com.pachuho.sleepAlarm.data

import android.content.Context
import androidx.room.*
import com.google.gson.Gson

@Database(entities = [Alarm::class], version = 1)
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