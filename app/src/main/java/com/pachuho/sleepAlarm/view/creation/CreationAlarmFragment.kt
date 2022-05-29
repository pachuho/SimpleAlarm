package com.pachuho.sleepAlarm.view.creation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.pachuho.sleepAlarm.base.BaseFragment
import sleepAlarm.R
import sleepAlarm.databinding.FragmentCreationAlarmBinding
import timber.log.Timber


class CreationAlarmFragment : BaseFragment<FragmentCreationAlarmBinding, CreationAlarmViewModel>(R.layout.fragment_creation_alarm) {
    override val viewModel: CreationAlarmViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }
}