package com.pachuho.sleepAlarm.view.alarm

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pachuho.sleepAlarm.ViewModelFactory
import com.pachuho.sleepAlarm.base.BaseFragment
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.datasource.model.Day
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import com.pachuho.sleepAlarm.support.AlarmAdapter
import com.pachuho.sleepAlarm.utils.repeatOnStarted
import com.pachuho.sleepAlarm.utils.showSnackBar
import com.pachuho.sleepAlarm.view.alarm.AlarmViewModel.Event
import sleepAlarm.R
import sleepAlarm.databinding.FragmentAlarmBinding
import timber.log.Timber

class AlarmFragment : BaseFragment<FragmentAlarmBinding, AlarmViewModel>(R.layout.fragment_alarm) {
    override val viewModel: AlarmViewModel by viewModels { ViewModelFactory(AlarmRepository()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> handleEvent(event) }
        }

//        viewModel.insertAlarm(Alarm(2, true, 1, Day(monday = true, tuesday = true, wednesday = true, thursday = true, friday = true, saturday = true, sunday = true), 1, true))
        viewModel.getAllAlarm()
    }

    private fun setAlarm(alarms: List<Alarm>){
        binding.rvAlarm.adapter = viewModel.adapter
        alarms.forEach { alarm ->
            Timber.i("alarm: $alarm")

        }

        // todo fix error
        viewModel.adapter.submitList(alarms.toMutableList())

        Timber.i("alarm:... ${viewModel.adapter.itemCount}")
    }

    private fun handleEvent(event: Event) = when (event) {
        is Event.ShowSnackBar -> view?.showSnackBar(event.text)
        is Event.GetAlarms -> setAlarm(event.alarms)
    }

    override fun onDestroyView() {
        Timber.i("onDestroyView!")
        super.onDestroyView()
    }
}
