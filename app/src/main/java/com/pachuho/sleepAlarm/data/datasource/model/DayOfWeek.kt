package com.pachuho.sleepAlarm.data.datasource.model

data class DayOfWeek(
    val monday: Boolean = true,
    val tuesday: Boolean = true,
    val wednesday: Boolean = true,
    val thursday: Boolean = true,
    val friday: Boolean = true,
    val saturday: Boolean = false,
    val sunday: Boolean = false
)