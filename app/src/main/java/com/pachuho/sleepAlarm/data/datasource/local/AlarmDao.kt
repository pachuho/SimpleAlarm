package com.pachuho.sleepAlarm.data.datasource.local

import androidx.room.*
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.datasource.model.DayOfWeek

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm")
    fun getAll(): List<Alarm>

    @Query("SELECT MAX(id) FROM alarm")
    fun getCurrentId(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(alarm: Alarm)

    @Delete
    fun deleteAlarm(alarm: Alarm)

    @Query("UPDATE Alarm SET use = :use WHERE id = :id")
    fun setUseAlarm(id: Long, use: Boolean)

    @Query("UPDATE Alarm " +
            "SET use = :use, hour = :hour, minute = :minute, repetition = :repetition, sound = :sound, vibration = :vibration " +
            "WHERE id = :id")
    fun updateAlarm(id: Long, use: Boolean, hour: Int, minute: Int, repetition: DayOfWeek, sound: Int, vibration: Boolean)
}