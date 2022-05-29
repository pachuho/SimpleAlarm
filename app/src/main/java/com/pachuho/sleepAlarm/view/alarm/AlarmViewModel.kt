package com.pachuho.sleepAlarm.view.alarm

import androidx.lifecycle.viewModelScope
import com.pachuho.sleepAlarm.base.BaseViewModel
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import com.pachuho.sleepAlarm.support.AlarmAdapter
import com.pachuho.sleepAlarm.view.alarm.AlarmViewModel.*
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

    val adapter by lazy { AlarmAdapter() }

    fun insertAlarm(alarm: Alarm){
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.insertAlarm(alarm).let {
                alarm -> Timber.i("Insert Alarm, $alarm")
            }
        }
    }

    fun moveFragment(fragName: String){
        event(Event.MoveFragment(fragName))
    }


    fun getAllAlarm(){
        CoroutineScope(Dispatchers.IO).launch {
            event(Event.GetAlarms(alarmRepository.getAllAlarm()))
        }
    }

    private fun event(event: Event){
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class Event{
        data class ShowSnackBar(val text: String) : Event()
        data class GetAlarms(val alarms: List<Alarm>) : Event()
        data class MoveFragment(val fragName: String) : Event()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared() AlarmViewModel!")
    }
}