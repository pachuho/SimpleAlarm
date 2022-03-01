package com.pachuho.sleepAlarm.views.alarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.pachuho.sleepAlarm.base.BaseFragment
import sleepAlarm.R
import sleepAlarm.databinding.FragmentAlarmBinding

class AlarmFragment : BaseFragment<FragmentAlarmBinding, AlarmViewModel>(R.layout.fragment_alarm) {
    override val viewModel: AlarmViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        viewModel.isLoading

    }
}