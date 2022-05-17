package com.pachuho.sleepAlarm.data.datasource.local

import androidx.room.*
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.datasource.model.Day

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): List<Alarm>

    @Insert
    fun insertAlarm(alarm: Alarm)

    @Delete
    fun deleteAlarm(alarm: Alarm)

    @Query("UPDATE Alarm SET use = :use WHERE id = :id")
    fun setUseAlarm(id: Long, use: Boolean)

    @Query("UPDATE Alarm " +
            "SET use = :use, hour = :hour, minute = :minute, repetition = :repetition, sound = :sound, vibration = :vibration " +
            "WHERE id = :id")
    fun updateAlarm(id: Long, use: Boolean, hour: Int, minute: Int, repetition: Day, sound: Int, vibration: Boolean)
}