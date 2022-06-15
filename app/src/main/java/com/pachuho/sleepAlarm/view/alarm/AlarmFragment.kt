package com.pachuho.sleepAlarm.view.alarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pachuho.sleepAlarm.ViewModelFactory
import com.pachuho.sleepAlarm.base.BaseFragment
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import com.pachuho.sleepAlarm.utils.showSnackBar
import com.pachuho.sleepAlarm.view.alarm.AlarmViewModel.Event
import com.pachuho.sleepAlarm.view.main.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sleepAlarm.R
import sleepAlarm.databinding.FragmentAlarmBinding
import timber.log.Timber

class AlarmFragment : BaseFragment<FragmentAlarmBinding, AlarmViewModel>(R.layout.fragment_alarm) {
    override val viewModel: AlarmViewModel by viewModels { ViewModelFactory(AlarmRepository())}
    private val sharedViewModel: MainViewModel by activityViewModels () // 액티비티 뷰모델
    private var alarmJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.rvAlarm.adapter = viewModel.adapter

        viewModel.getAllAlarm()
    }

    private fun setAlarm(alarms: List<Alarm>){
        viewModel.adapter.submitList(alarms)
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
