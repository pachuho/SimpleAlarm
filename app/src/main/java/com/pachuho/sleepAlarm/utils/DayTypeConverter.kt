package com.pachuho.sleepAlarm.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pachuho.sleepAlarm.data.datasource.model.Day

@ProvidedTypeConverter
class DayTypeConverter(private val gson: Gson) {

    @TypeConverter
    fun listToJson(value: Day): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): Day {
        return gson.fromJson(value, Day::class.java)
    }
}
