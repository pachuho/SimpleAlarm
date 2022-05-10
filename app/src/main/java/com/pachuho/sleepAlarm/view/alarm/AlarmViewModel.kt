package com.pachuho.sleepAlarm.view.alarm

import com.pachuho.sleepAlarm.base.BaseViewModel
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class AlarmViewModel(
    private val alarmRepository: AlarmRepository
): BaseViewModel() {
    fun insertAlarm(alarm: Alarm){
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.insertAlarm(alarm).let {
                alarm -> Timber.i("Insert Alarm, $alarm")
            }
        }
    }

    fun getAllAlarm(){
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.getAllAlarm().let {
                    alarms -> Timber.i("get Alarms, $alarms")
            }
        }
    }

    sealed class Event{
        data class ShowSnackBar(val text: String) : Event()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared() AlarmViewModel!")
    }
}