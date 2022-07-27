package com.pachuho.sleepAlarm.base

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
        val calender = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)

            // 지나간 시간의 경우 다음날 알람으로 울리도록
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1) // 하루 더하기
            }
        }

        //알람 매니저 가져오기.
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(requireContext(), AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            alarm.id,
            intent,
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
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            alarm.id,
            Intent(requireContext(), AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val M_ALARM_REQUEST_CODE = 1001
    }
}