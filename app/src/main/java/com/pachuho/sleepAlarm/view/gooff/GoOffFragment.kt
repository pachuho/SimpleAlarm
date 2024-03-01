package com.pachuho.sleepAlarm.view.gooff

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pachuho.sleepAlarm.base.BaseFragment
import com.pachuho.sleepAlarm.utils.getCurrentDate
import com.pachuho.sleepAlarm.utils.getCurrentDayWeek
import com.pachuho.sleepAlarm.utils.getCurrentDayWeekString
import com.pachuho.sleepAlarm.utils.getCurrentTime
import sleepAlarm.R
import sleepAlarm.databinding.FragmentGoOffBinding

class GoOffFragment : BaseFragment<FragmentGoOffBinding, GoOffViewModel>(R.layout.fragment_go_off) {
    override val viewModel: GoOffViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = getCurrentDate() + " " + getCurrentDayWeekString()
        binding.tvTime1.text = date
        binding.tvTime2.text = getCurrentTime()
        onClick()
    }

    private fun onClick() = with(binding){
        btnGoOff.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}