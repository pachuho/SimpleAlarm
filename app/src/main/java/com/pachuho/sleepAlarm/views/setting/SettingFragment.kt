package com.pachuho.sleepAlarm.views.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.pachuho.sleepAlarm.base.BaseFragment
import sleepAlarm.R
import sleepAlarm.databinding.FragmentSettingBinding

class SettingFragment : BaseFragment<FragmentSettingBinding, SettingViewModel>(R.layout.fragment_setting) {
    override val viewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }
}