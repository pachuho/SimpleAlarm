package com.pachuho.sleepAlarm.views.record

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import com.pachuho.sleepAlarm.base.BaseFragment
import kotlinx.coroutines.*
import sleepAlarm.R
import sleepAlarm.databinding.FragmentRecordingBinding


class RecordingFragment : BaseFragment<FragmentRecordingBinding, RecordingViewModel>(R.layout.fragment_recording) {
    override val viewModel: RecordingViewModel  by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
    }
}