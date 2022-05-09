package com.pachuho.sleepAlarm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
    @PrimaryKey val id: Long,
    @ColumnInfo val use: Boolean,
    @ColumnInfo val time: Int,
    @ColumnInfo val repetition: Day,
    @ColumnInfo val sound: Int,
    @ColumnInfo val vibration: Boolean
)
