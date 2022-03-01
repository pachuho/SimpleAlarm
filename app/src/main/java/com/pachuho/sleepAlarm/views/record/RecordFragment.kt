package com.pachuho.sleepAlarm.views.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.pachuho.sleepAlarm.base.BaseFragment
import sleepAlarm.R
import sleepAlarm.databinding.FragmentRecordBinding

class RecordFragment : BaseFragment<FragmentRecordBinding, RecordViewModel>(R.layout.fragment_record) {
    override val viewModel: RecordViewModel  by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel



    }
}