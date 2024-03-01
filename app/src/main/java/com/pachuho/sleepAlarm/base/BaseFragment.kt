package com.pachuho.sleepAlarm.base

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.R
import androidx.navigation.fragment.findNavController
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.support.AlarmReceiver
import timber.log.Timber
import java.util.*

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel>(@LayoutRes val layoutId: Int) : Fragment() {
    private var _binding: B? = null
    val binding get() = _binding!!

    protected abstract val viewModel: VM

    //알람 매니저 가져오기.
    private val alarmManager by lazy { requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun setUseAlarm(alarm: Alarm){
        if(alarm.use){
            onAlarm(alarm)
        } else{
            offAlarm(alarm)
        }
    }

    private fun onAlarm(alarm: Alarm){
        Timber.e("onAlarm: ${alarm.id} ${alarm.hour}시 ${alarm.minute}분 알람 울림")
        val calender = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)

            // 지나간 시간의 경우 다음날 알람으로 울리도록
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1) // 하루 더하기
            }
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            alarm.id,
            Intent(requireContext(), AlarmReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        ) // 있으면 새로 만든거로 업데이트

        // 잠자기 모드에서도 허용 하는 방법
//                alarmManager.setAndAllowWhileIdle()
//                alarmManager.setExactAndAllowWhileIdle()

        // Doze 모드
        alarmManager.setExactAndAllowWhileIdle( // 정확성 보장 및 Doze 모드에서도 알람 발생
            AlarmManager.RTC_WAKEUP, // 실제 시간 기준 + Doze 해제
            calender.timeInMillis,
            pendingIntent
        )
    }

    private fun offAlarm(alarm: Alarm){
        Timber.e("offAlarm: ${alarm.id} ${alarm.hour}시 ${alarm.minute}분 알람 종료")
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            alarm.id,
            Intent(requireContext(), AlarmReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}