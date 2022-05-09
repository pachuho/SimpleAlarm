package com.pachuho.sleepAlarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pachuho.sleepAlarm.data.AlarmRepository


class ViewModelFactory(private val alarmRepository: AlarmRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AlarmRepository::class.java).newInstance(alarmRepository)
    }
}
