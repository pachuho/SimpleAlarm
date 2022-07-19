package com.pachuho.sleepAlarm.data.datasource.model

data class DayOfWeek(
    var monday: Boolean = true,
    var tuesday: Boolean = true,
    var wednesday: Boolean = true,
    var thursday: Boolean = true,
    var friday: Boolean = true,
    var saturday: Boolean = false,
    var sunday: Boolean = false
)