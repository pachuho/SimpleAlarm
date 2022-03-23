package com.pachuho.sleepAlarm.views.alarm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.pachuho.sleepAlarm.EventObserver
import com.pachuho.sleepAlarm.base.BaseFragment
import sleepAlarm.R
import sleepAlarm.databinding.FragmentAlarmBinding
import timber.log.Timber

class AlarmFragment : BaseFragment<FragmentAlarmBinding, AlarmViewModel>(R.layout.fragment_alarm) {
    override val viewModel: AlarmViewModel by viewModels()

    private val fabOpenAnimation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open) }
    private val fabCloseAnimation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel

        observe()
    }

    private fun observe() = with(viewModel) {
        isFabOpen.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                binding.apply {
                    fab1.startAnimation(fabOpenAnimation)
                    fab2.startAnimation(fabOpenAnimation)
                    fab1.visibility = View.VISIBLE
                    fab2.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    fab1.startAnimation(fabCloseAnimation)
                    fab2.startAnimation(fabCloseAnimation)
                    fab1.visibility = View.GONE
                    fab2.visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroyView() {
        Timber.i("onDestroyView!")
        super.onDestroyView()
    }
}
