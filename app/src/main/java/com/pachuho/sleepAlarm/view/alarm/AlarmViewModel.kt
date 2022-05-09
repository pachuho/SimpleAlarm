package com.pachuho.sleepAlarm.view.alarm

import androidx.lifecycle.viewModelScope
import com.pachuho.sleepAlarm.base.BaseViewModel
import com.pachuho.sleepAlarm.data.Alarm
import com.pachuho.sleepAlarm.data.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class AlarmViewModel(
    private val alarmRepository: AlarmRepository
): BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun showToast(msg: String) {
        event(Event.ShowToast(msg))
    }

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

    private fun event(event: Event){
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class Event{
        data class ShowToast(val text: String) : Event()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared() AlarmViewModel!")
    }
}