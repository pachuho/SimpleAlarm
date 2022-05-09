package com.pachuho.sleepAlarm.data

import com.pachuho.sleepAlarm.base.GlobalApplication

class AlarmRepository {
    private val instance = GlobalApplication.appDataBaseInstance.alarmDao()

    suspend fun insertAlarm(alarm: Alarm) = instance.insertAlarm(alarm)
    suspend fun deleteAlarm(alarm: Alarm) = instance.deleteAlarm(alarm)
    suspend fun getAllAlarm() = instance.getAll()
    suspend fun setUseAlarm(id: Long, use: Boolean) = instance.setUseAlarm(id, use)
    suspend fun updateAlarm(
        id: Long,
        use: Boolean,
        time: Int,
        repetition: Day,
        sound: Int,
        vibration: Boolean
    ) = instance.updateAlarm(id, use, time, repetition, sound, vibration)
}