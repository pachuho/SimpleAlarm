package com.pachuho.sleepAlarm.view.alarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pachuho.sleepAlarm.ViewModelFactory
import com.pachuho.sleepAlarm.base.BaseFragment
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.datasource.model.Time
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import com.pachuho.sleepAlarm.utils.*
import com.pachuho.sleepAlarm.view.alarm.AlarmViewModel.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sleepAlarm.R
import sleepAlarm.databinding.FragmentAlarmBinding
import timber.log.Timber

class AlarmFragment : BaseFragment<FragmentAlarmBinding, AlarmViewModel>(R.layout.fragment_alarm) {
    override val viewModel: AlarmViewModel by viewModels { ViewModelFactory(AlarmRepository())}
    private var alarmJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.rvAlarm.adapter = viewModel.adapter


        viewModel.getAllAlarm()
    }

    private fun setAlarm(alarms: ArrayList<Alarm>){
        viewModel.alarms = viewModel.setOrderAlarms(alarms)

        val currentDayOfWeek = getCurrentDayWeek()
        val currentTime: Time = setCurrentTime()
        var use = false

        var minDay = 0
        var minHour = 0
        var minMinute = 0
        var minNone = 1
        var sum = 0

        alarms.forEach {
            if(it.use){
                val (day, hour, minute, none) = calculateTime(currentDayOfWeek,
                    it.repetition, currentTime, Time(it.hour, it.minute))
                val h = "%02d".format(if (hour < 12) hour else hour - 12)
                val m = "%02d".format(minute)
                val tempSum = day + h.toInt() + m.toInt()

                if(sum == 0 || sum > tempSum){
                    sum = tempSum
                    minDay = day
                    minHour = hour
                    minMinute = minute
                    minNone = none
                }

                use = true
            }
        }
        if(!use){
            viewModel.timeFromNow.value = "예정된 알람이 없습니다."
            return
        }

        viewModel.timeFromNow.value = getDiffComment(minDay, minHour, minMinute, minNone)
    }

    private fun navigate(alarm: Alarm?) = with(findNavController()){
        val action = AlarmFragmentDirections.actionAlarmFragmentToCreationAlarmFragment(alarm)
        navigate(action)
    }

    private fun handleEvent(event: Event) = when (event) {
        is Event.ShowSnackBar -> view?.showSnackBar(event.text)
        is Event.GetAlarms -> setAlarm(event.alarms as ArrayList<Alarm>)
        is Event.MoveCreationFragment -> navigate(event.alarm)
    }

    override fun onStart() {
        super.onStart()
        alarmJob = lifecycleScope.launch {
            viewModel.eventFlow.collect {
                    event -> handleEvent(event)
            }
        }
    }

    override fun onStop() {
        alarmJob?.cancel()
        super.onStop()
    }


    override fun onDestroyView() {
        Timber.i("onDestroyView! Alarm")
        super.onDestroyView()
    }
}
