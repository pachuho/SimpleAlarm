package com.pachuho.sleepAlarm.view.alarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.pachuho.sleepAlarm.base.BaseFragment
import com.pachuho.sleepAlarm.utils.repeatOnStarted
import com.pachuho.sleepAlarm.utils.showSnackBar
import com.pachuho.sleepAlarm.view.alarm.AlarmViewModel.Event
import sleepAlarm.R
import sleepAlarm.databinding.FragmentAlarmBinding
import timber.log.Timber

class AlarmFragment : BaseFragment<FragmentAlarmBinding, AlarmViewModel>(R.layout.fragment_alarm) {
    override val viewModel: AlarmViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel

        repeatOnStarted {
            viewModel.eventFlow.collect{ event -> handleEvent(event) }
        }
    }

    private fun handleEvent(event: Event) = when (event) {
        is Event.ShowToast -> view?.showSnackBar(event.text)

    }

    override fun onDestroyView() {
        Timber.i("onDestroyView!")
        super.onDestroyView()
    }
}
