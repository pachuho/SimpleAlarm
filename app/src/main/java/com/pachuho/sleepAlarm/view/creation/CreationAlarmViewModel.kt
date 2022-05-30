package com.pachuho.sleepAlarm.view.creation

import android.view.View
import android.widget.CheckedTextView
import androidx.lifecycle.viewModelScope
import com.pachuho.sleepAlarm.base.BaseViewModel
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.datasource.model.Day
import com.pachuho.sleepAlarm.data.datasource.model.Time
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber


class CreationAlarmViewModel(
    private val alarmRepository: AlarmRepository
): BaseViewModel() {
    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    var time = MutableStateFlow(Time(0,0))

    fun onTimeChanged(hour: Int, minute: Int){
        time.update { Time(hour, minute) }
    }

    fun setToggle(view: View){
        (view as CheckedTextView).isChecked = !view.isChecked
    }

    fun creationAlarm(){
        CoroutineScope(Dispatchers.IO).launch {
            Timber.e("currentId: ${alarmRepository.getCurrentId()}")
            val id = alarmRepository.getCurrentId() + 1
            val alarm = Alarm(id, true, time.value.hour, time.value.minute, Day(monday = true, tuesday = true, wednesday = true, thursday = true, friday = true, saturday = false, sunday = false), 1, true)

            alarmRepository.insertAlarm(alarm).run {
                    event(Event.UpdateAlarm(alarm))
            }
        }
    }

    private fun event(event: Event){
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class Event{
        data class UpdateAlarm(val alarm: Alarm) : Event()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared() AlarmViewModel!")
    }
}