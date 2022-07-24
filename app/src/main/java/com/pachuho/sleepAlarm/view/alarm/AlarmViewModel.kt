package com.pachuho.sleepAlarm.view.alarm

import androidx.lifecycle.viewModelScope
import com.pachuho.sleepAlarm.base.BaseViewModel
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import com.pachuho.sleepAlarm.support.AlarmAdapter
import com.pachuho.sleepAlarm.view.alarm.AlarmViewModel.*
import com.pachuho.sleepAlarm.view.creation.CreationAlarmViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class AlarmViewModel(
    private val alarmRepository: AlarmRepository
): BaseViewModel() {
    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    var alarms = ArrayList<Alarm>()
    set(value) {
        field = value
        adapter.submitList(alarms)
    }

    val adapter by lazy { object: AlarmAdapter(){
        override fun checkUsing(id: Int, use: Boolean) {
            alarms.forEach {
                if (it.id == id){
                    it.use = use
                    updateAlarm(it)
                    return@forEach
                }
            }
        }
    }}

    var timeFromNow = MutableStateFlow("지금부터 " + 1 +"분 뒤 알람이 울립니다.")

    fun moveFragment(fragName: String){
        event(Event.MoveFragment(fragName))
    }

    fun getAllAlarm(){
        CoroutineScope(Dispatchers.IO).launch {
            event(Event.GetAlarms(alarmRepository.getAllAlarm()))
        }
    }

    fun updateAlarm(alarm: Alarm){
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.updateAlarm(alarm).run {
                event(Event.GetAlarms(alarms))
            }
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