package com.pachuho.sleepAlarm.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pachuho.sleepAlarm.data.datasource.model.DayOfWeek

@ProvidedTypeConverter
class DayTypeConverter(private val gson: Gson) {

    @TypeConverter
    fun listToJson(value: DayOfWeek): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): DayOfWeek {
        return gson.fromJson(value, DayOfWeek::class.java)
    }
}
