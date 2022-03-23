package com.pachuho.sleepAlarm.views.creation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.pachuho.sleepAlarm.base.BaseFragment
import sleepAlarm.R
import sleepAlarm.databinding.FragmentCreationAlarmBinding
import timber.log.Timber


class CreationAlarmFragment : BaseFragment<FragmentCreationAlarmBinding, CreationAlarmViewModel>(R.layout.fragment_creation_alarm) {
    override val viewModel: CreationAlarmViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        Timber.i("onViewCreated!, ${viewModel.isTest1.value}")

        viewModel.isTest1.observe(viewLifecycleOwner) {
            Timber.i("isTest, ${viewModel.isTest1.value}")
        }
    }
}