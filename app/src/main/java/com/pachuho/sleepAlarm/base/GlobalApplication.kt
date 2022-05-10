package com.pachuho.sleepAlarm.base

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.pachuho.sleepAlarm.data.datasource.local.AlarmDatabase
import com.pachuho.sleepAlarm.utils.DayTypeConverter
import sleepAlarm.BuildConfig
import timber.log.Timber

class GlobalApplication: Application() {
    companion object {
        lateinit var appInstance: GlobalApplication
            private set

        lateinit var appDataBaseInstance: AlarmDatabase
            private set

    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        appDataBaseInstance = Room.databaseBuilder(
            appInstance.applicationContext,
            AlarmDatabase::class.java, "alarmApp.db"
        )
            .fallbackToDestructiveMigration() // DB 변경 시 DB 초기화
//            .allowMainThreadQueries() // 메인 스레드에서 접근 허용
            .addTypeConverter(DayTypeConverter(Gson()))
            .build()

        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}