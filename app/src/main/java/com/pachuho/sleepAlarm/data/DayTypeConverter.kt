package com.pachuho.sleepAlarm.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import org.json.JSONObject
import javax.xml.transform.Source

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
