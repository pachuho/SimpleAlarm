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
import kotlin.collections.ArrayList

class CreationAlarmViewModel(
    private val alarmRepository: AlarmRepository
): BaseViewModel() {
    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    // 현재 시간
    private var currentTime: Time = setCurrentTime()

    // TimePicker 시간
    var timePickerTime: Time? = null
        set(value) {
            value?.let { calculateTime(currentTime, value, targetDayOfWeek) }
            field = value
        }

    // 현재 요일
    lateinit var currentDayOfWeek: Day

    // 적용된 요일
    var targetDayOfWeek = DayOfWeek()
        set(value) {
            calculateTime(currentTime, timePickerTime!!, value)
            field = value
        }

    var timeFromNow = MutableStateFlow("지금부터 " + 1 +"분 뒤 알람이 울립니다.")

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

        val hour = "%02d".format(if (currentTime[0] < 12) currentTime[0] else currentTime[0] - 12).toInt()

        return Time(hour, currentTime[1])
    }

    // 요일, 시간, 분 계산
    private fun calculateTime(current: Time, target: Time, dayOfWeek: DayOfWeek){
        Timber.i("calculateTime, currentTime: $current, targetTime: $target")
        val diffHour = target.hour - current.hour
        val diffMinute = target.minute - current.minute

        val commentStart = "지금부터 "
        val commentDay = "일 "
        val commentHour = "시간 "
        val commentMinute = "분 "
        val commentEnd = "뒤 알람이 울립니다."

        val commentList = ArrayList<String>().apply {
            add(commentStart)

            if(diffHour > 0) {
                add(diffHour.toString())
                add(commentHour)
            }
            if(diffMinute > 0) {
                add(diffMinute.toString())
                add(commentMinute)
            }
            add(commentEnd)
        }

        timeFromNow.value = commentList.joinToString("")
    }

//        // 요일, 시간, 분 계산
//    private fun calculateTime(current: Time, target: Time, dayOfWeek: DayOfWeek){
//
//    }


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