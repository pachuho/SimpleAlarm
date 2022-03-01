package com.pachuho.sleepAlarm.base

import android.app.Application
import sleepAlarm.BuildConfig
import timber.log.Timber

class SleepApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}