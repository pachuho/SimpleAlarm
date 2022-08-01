package com.pachuho.sleepAlarm.data.repository

import com.pachuho.sleepAlarm.base.GlobalApplication
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.datasource.model.DayOfWeek

class AlarmRepository {
    private val instance = GlobalApplication.appDataBaseInstance.alarmDao()

    suspend fun insertAlarm(alarm: Alarm) = instance.insertAlarm(alarm)
    suspend fun deleteAlarm(alarm: Alarm) = instance.deleteAlarm(alarm)
    suspend fun getAllAlarm() = instance.getAll()
    suspend fun getCurrentId() = instance.getCurrentId()
    suspend fun setUseAlarm(id: Long, use: Boolean) = instance.setUseAlarm(id, use)
    suspend fun updateAlarm(alarm: Alarm) = instance.updateAlarm(
        alarm.id.toLong(), alarm.use, alarm.hour, alarm.minute, alarm.repetition,
        alarm.sound, alarm.vibration)
}