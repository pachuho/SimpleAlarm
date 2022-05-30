package com.pachuho.sleepAlarm.view.alarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pachuho.sleepAlarm.ViewModelFactory
import com.pachuho.sleepAlarm.base.BaseFragment
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.datasource.model.Day
import com.pachuho.sleepAlarm.data.datasource.model.Time
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import com.pachuho.sleepAlarm.utils.showSnackBar
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

//        viewModel.insertAlarm(Alarm(1, true, 12, 0, Day(monday = true, tuesday = true, wednesday = true, thursday = true, friday = true, saturday = true, sunday = true), 1, true))
        viewModel.getAllAlarm()
    }

    private fun setAlarm(alarms: List<Alarm>){
        binding.rvAlarm.adapter = viewModel.adapter
        viewModel.adapter.submitList(alarms)

        Timber.e("alarm:... $alarms")
    }

    private fun navigate(fragName: String) = with(findNavController()){
        when(fragName){
            "creation" -> navigate(R.id.action_alarmFragment_to_creationAlarmFragment)
        }
    }

    private fun handleEvent(event: Event) = when (event) {
        is Event.ShowSnackBar -> view?.showSnackBar(event.text)
        is Event.GetAlarms -> setAlarm(event.alarms)
        is Event.MoveFragment -> navigate(event.fragName)
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
