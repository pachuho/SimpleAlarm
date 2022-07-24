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
import java.lang.StrictMath.abs
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
    private var targetTime = addMinute(currentTime)
        set(value) {
            field = value
            calculateTime()
        }

    // 현재 요일
    private val currentDayOfWeek = getCurrentDayWeek()

    // 적용된 요일
    var targetDayOfWeek = DayOfWeek()

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

    fun setToggle(view: View){
        (view as CheckedTextView).isChecked = !view.isChecked
        event(Event.CheckDayOfWeek(view))
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

    /*
    현재 요일 가져오기
     */
    private fun getCurrentDayWeek(): Day{
        val date = Date(System.currentTimeMillis())
        val cal = Calendar.getInstance()
        cal.time = date

        val result = when(cal.get(Calendar.DAY_OF_WEEK)){
            1 -> Day.Sunday
            2 -> Day.Monday
            3 -> Day.Tuesday
            4 -> Day.Wednesday
            5 -> Day.Thursday
            6 -> Day.Friday
            7 -> Day.Saturday
            else -> {
                Day.Sunday
            }
        }
        return result
    }

    fun setDayOfWeek(){
        calculateTime()
    }

    // 요일, 시간, 분 계산
    @SuppressLint("BinaryOperationInTimber")
    private fun calculateTime(){
//        Timber.e("calculateTime, currentDay: $currentDayOfWeek, targetDay: $targetDayOfWeek, " +
//                "currentTime: $currentTime, targetTime: $targetTime")

        val targetDays = ArrayList<Boolean>().apply {
            targetDayOfWeek.apply {
                add(sunday)
                add(monday)
                add(tuesday)
                add(wednesday)
                add(thursday)
                add(friday)
                add(saturday)
            }
        }

        var diffDay = 0
        var none = true

        for(i in 0..6){
            var index = currentDayOfWeek.day + i
            if(index >= targetDays.size){
                index -= 7
            }
            if(targetDays[index]){
                diffDay = i
                none = false
                break
            }
        }

//        Timber.e("diffDay: $diffDay, none: $none")

        Timber.e("currentTime: ${currentTime}, targetTime: ${targetTime}")

        val diffHour = if(targetTime.hour >= currentTime.hour){
            targetTime.hour - currentTime.hour
        } else {
            24 - (currentTime.hour - targetTime.hour)
        }
        val diffMinute = if(targetTime.minute >= currentTime.minute){
            targetTime.minute - currentTime.minute
        } else {
            60 - (currentTime.minute - targetTime.minute)
        }

        val commentStart = "지금부터 "
        val commentDay = "일 "
        val commentHour = "시간 "
        val commentMinute = "분 "
        val commentEnd = "뒤 알람이 울립니다."

        val commentList = ArrayList<String>().apply {
            add(commentStart)

            // 일
            if(diffDay > 0){
                add(diffDay.toString())
                add(commentDay)
            } else if(diffDay == 0 && none){
                this.clear()
                add("설정한 요일이 존재하지 않아 알람이 울리지 않습니다.")
                return@apply
            }

            // 시간
            if(diffHour > 0) {
                add(diffHour.toString())
                add(commentHour)
            }

            // 분
            if(diffMinute > 0) {
                add(diffMinute.toString())
                add(commentMinute)
            }

            // 요일을 체크했으며 동일 시간인 경우
            if(diffDay == 0 && diffHour == 0 && diffMinute == 0){
                add("7일 ")
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
        data class CheckDayOfWeek(val view: View) : Event()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared() AlarmViewModel!")
    }
}