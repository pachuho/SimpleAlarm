package com.pachuho.sleepAlarm.support

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import sleepAlarm.R
import timber.log.Timber

class AlarmReceiver: BroadcastReceiver() {
    interface GoOffListener {
        fun onGoOffCallback()
    }

    fun registerCallback(goOffListener: GoOffListener) {
        this.goOffListener = goOffListener
    }

    lateinit var goOffListener: GoOffListener

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1001"
        const val NOTIFICATION_ID = 101

    }


    override fun onReceive(context: Context, intent: Intent) {
        Timber.e("test!!!, onReceive")
        // 채널 생성
        createNotificationChannel(context)
        // 알림
        notifyNotification(context)

        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent("goOff"))
    }

    private fun createNotificationChannel(context: Context) {
        // context : 실행하고 있는 앱의 상태나 맥락을 담고 있음
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "기상 알람",
            NotificationManager.IMPORTANCE_HIGH
        )

        NotificationManagerCompat.from(context)
            .createNotificationChannel(notificationChannel)
    }

    private fun notifyNotification(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("알람")
                .setContentText("일어날 시간입니다.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)

            notify(NOTIFICATION_ID, build.build())
        }
    }

}