package com.pachuho.sleepAlarm.data

import androidx.room.*

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
            "SET use = :use, time = :time, repetition = :repetition, sound = :sound, vibration = :vibration " +
            "WHERE id = :id")
    fun updateAlarm(id: Long, use: Boolean, time: Int, repetition: Day, sound: Int, vibration: Boolean)
}