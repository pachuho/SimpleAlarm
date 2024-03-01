package com.pachuho.sleepAlarm.view.creation

import android.view.View
import android.widget.CheckedTextView
import androidx.lifecycle.viewModelScope
import com.pachuho.sleepAlarm.base.BaseViewModel
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.datasource.model.DayOfWeek
import com.pachuho.sleepAlarm.data.datasource.model.Time
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import com.pachuho.sleepAlarm.utils.calculateTime
import com.pachuho.sleepAlarm.utils.getCurrentDayWeek
import com.pachuho.sleepAlarm.utils.getDiffComment
import com.pachuho.sleepAlarm.utils.setCurrentTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class CreationAlarmViewModel(
    private val alarmRepository: AlarmRepository
): BaseViewModel() {
    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    // 현재 시간
    private var currentTime: Time = setCurrentTime()

    // TimePicker 시간
    private var targetTime = addMinute(currentTime)
        set(value) {
            field = value
            setDayOfWeek()
        }

    // 현재 요일
    private val currentDayOfWeek = getCurrentDayWeek()

    // 적용된 요일
    val targetDayOfWeek = MutableStateFlow(DayOfWeek())

    var timeFromNow = MutableStateFlow("지금부터 " + 1 +"분 뒤 알람이 울립니다.")

    fun onTimeChanged(hour: Int, minute: Int){
        targetTime = Time(hour, minute)
    }

    /*
    주어진 시간에 1분 추가
     */
    private fun addMinute(time: Time): Time{
        var hour = time.hour
        var minute = time.minute
        minute += 1

        if(time.minute > 59){
            minute = 0
            if(time.hour + 1 > 23){
                hour = 0
            }
        }
        return Time(hour, minute)
    }

    // 선택한 요일 체크 상태 수정
    fun setToggle(view: View){
        (view as CheckedTextView).isChecked = !view.isChecked
        event(Event.CheckDayOfWeek(view))
    }

    // 알람 생성
    fun createAlarm(alarm: Alarm){
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.insertAlarm(alarm).run {
                    event(Event.UpdateAlarm(alarm))
            }
        }
    }

    // 알람 수정
    fun updateAlarm(alarm: Alarm){
        CoroutineScope(Dispatchers.IO).launch {
            alarmRepository.updateAlarm(alarm).run {
                event(Event.UpdateAlarm(alarm))
            }
        }
    }

    fun setDayOfWeek(){
        val (day, hour, minute, none) = calculateTime(currentDayOfWeek, targetDayOfWeek.value, currentTime, targetTime)
        timeFromNow.value = getDiffComment(day, hour, minute, none)
    }

    private fun event(event: Event){
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }


    sealed class Event{
        data class UpdateAlarm(val alarm: Alarm) : Event()
        data class CheckDayOfWeek(val view: View) : Event()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared() AlarmViewModel!")
    }
}