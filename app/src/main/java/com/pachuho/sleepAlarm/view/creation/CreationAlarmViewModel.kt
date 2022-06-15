package com.pachuho.sleepAlarm.view.creation

import android.annotation.SuppressLint
import android.view.View
import android.widget.CheckedTextView
import androidx.lifecycle.viewModelScope
import com.pachuho.sleepAlarm.base.BaseViewModel
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.datasource.model.Day
import com.pachuho.sleepAlarm.data.datasource.model.DayOfWeek
import com.pachuho.sleepAlarm.data.datasource.model.Time
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class CreationAlarmViewModel(
    private val alarmRepository: AlarmRepository
): BaseViewModel() {
    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    // 현재 시간
    private var currentTime: Time = setCurrentTime()
        set(value) {
            calculateTime(value, null, targetDayOfWeek)
        field = value
    }

    // TimePicker 시간
    var timePickerTime: Time? = null
        set(value) {
            value?.let { calculateTime(currentTime, value, targetDayOfWeek) }
            field = value
        }

    lateinit var currentDayOfWeek: Day

    // 적용된 요일
    var targetDayOfWeek = DayOfWeek()
        set(value) {
            calculateTime(currentTime, timePickerTime, value)
            field = value
        }

    var timeFromNow = MutableStateFlow("")

    fun onTimeChanged(hour: Int, minute: Int){
        timePickerTime = Time(hour, minute)
    }

    fun setToggle(view: View){
        (view as CheckedTextView).isChecked = !view.isChecked
    }

    fun createAlarm(alarm: Alarm){
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.insertAlarm(alarm).run {
                    event(Event.UpdateAlarm(alarm))
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setCurrentTime(): Time {
        val date = Date(System.currentTimeMillis())
        val currentTime = SimpleDateFormat("hh:mm").format(date).split(":").map { it.toInt() }

        return Time(currentTime[0], currentTime[1])
    }

    // 요일, 시간, 분 계산
    private fun calculateTime(current: Time, target: Time?, dayOfWeek: DayOfWeek){
        val diffTime: Int = if(target == null){
            1
        } else {
            val targetTime = (target.hour.toString() + target.minute.toString()).toInt()
            val currentTime = (current.hour.toString() + current.minute.toString()).toInt()
            targetTime - currentTime
        }

        timeFromNow.value = "지금부터 " + diffTime +"분 이내로 알람이 울립니다."
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