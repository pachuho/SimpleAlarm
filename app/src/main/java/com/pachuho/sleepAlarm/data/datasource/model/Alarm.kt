package com.pachuho.sleepAlarm.data.datasource.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo var use: Boolean,
    @ColumnInfo val hour: Int,
    @ColumnInfo val minute: Int,
    @ColumnInfo val repetition: DayOfWeek,
    @ColumnInfo val sound: Int,
    @ColumnInfo val vibration: Boolean
){

    val timeText: String
    get() {
        val h = "%02d".format(if (hour < 12) hour else hour - 12)
        val m = "%02d".format(minute)
        return "$h:$m"
    }

    val amPmText: String
    get() {
        return if (hour < 12) "오전" else "오후"
    }

}
