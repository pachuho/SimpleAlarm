package com.pachuho.sleepAlarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pachuho.sleepAlarm.data.datasource.model.Time
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import com.pachuho.sleepAlarm.view.alarm.AlarmViewModel
import com.pachuho.sleepAlarm.view.creation.CreationAlarmViewModel


class ViewModelFactory(private val alarmRepository: AlarmRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T = with(modelClass) {
        when{
            isAssignableFrom(AlarmViewModel::class.java) ->
                modelClass.getConstructor(AlarmRepository::class.java).newInstance(alarmRepository)
            isAssignableFrom(CreationAlarmViewModel::class.java) ->
                modelClass.getConstructor(AlarmRepository::class.java).newInstance(alarmRepository)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}
