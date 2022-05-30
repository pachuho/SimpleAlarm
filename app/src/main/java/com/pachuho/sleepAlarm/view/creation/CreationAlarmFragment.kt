package com.pachuho.sleepAlarm.view.creation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pachuho.sleepAlarm.ViewModelFactory
import com.pachuho.sleepAlarm.base.BaseFragment
import com.pachuho.sleepAlarm.data.datasource.model.Time
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import com.pachuho.sleepAlarm.utils.showSnackBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sleepAlarm.R
import sleepAlarm.databinding.FragmentCreationAlarmBinding
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class CreationAlarmFragment : BaseFragment<FragmentCreationAlarmBinding, CreationAlarmViewModel>(R.layout.fragment_creation_alarm) {
    override val viewModel: CreationAlarmViewModel by viewModels { ViewModelFactory(AlarmRepository()) }
    private var alarmJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.time.value = getCurrentTime()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime(): Time {
        val date = Date(System.currentTimeMillis())
        val currentTime = SimpleDateFormat("hh:mm").format(date).split(":").map { it.toInt() }
        return Time(currentTime[0], currentTime[1])
    }

    private fun handleEvent(event: CreationAlarmViewModel.Event) = when (event) {
        is CreationAlarmViewModel.Event.UpdateAlarm -> completeCreationAlarm()
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

    private fun completeCreationAlarm(){
        view?.showSnackBar("알람이 생성되었습니다.")
        findNavController().popBackStack()
    }

}