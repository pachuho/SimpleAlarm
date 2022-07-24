package com.pachuho.sleepAlarm.utils

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pachuho.sleepAlarm.data.datasource.model.Day
import com.pachuho.sleepAlarm.data.datasource.model.DayOfWeek
import com.pachuho.sleepAlarm.data.datasource.model.Time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}

//현재 요일 가져오기
fun getCurrentDayWeek(): Day{
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

// 현재 시간 가져오기
@SuppressLint("SimpleDateFormat")
fun setCurrentTime(): Time {
    val date = Date(System.currentTimeMillis())
    val currentTime = SimpleDateFormat("hh:mm").format(date).split(":").map { it.toInt() }

    val hour = "%02d".format(if (currentTime[0] < 12) currentTime[0] else currentTime[0] - 12).toInt()

    return Time(hour, currentTime[1])
}

// 현재 시간과 차이 계산
@SuppressLint("BinaryOperationInTimber")
fun calculateTime(currentDayOfWeek: Day, targetDayOfWeek: DayOfWeek, currentTime: Time, targetTime: Time): List<Int>{
    Timber.e("calculateTime, currentDay: $currentDayOfWeek, targetDay: $targetDayOfWeek, " +
            "currentTime: $currentTime, targetTime: $targetTime")

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
    var none = 1

    for(i in 0..6){
        var index = currentDayOfWeek.day + i
        if(index >= targetDays.size){
            index -= 7
        }
        if(targetDays[index]){
            diffDay = i
            none = 0
            break
        }
    }

//        Timber.e("diffDay: $diffDay, none: $none")

//        Timber.e("currentTime: ${currentTime}, targetTime: ${targetTime}")

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

    return listOf(diffDay, diffHour, diffMinute, none)
}

fun getDiffComment(diffDay: Int, diffHour: Int, diffMinute: Int, none: Int): String{
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
        } else if(diffDay == 0 && none == 1){
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

    return commentList.joinToString("")
}