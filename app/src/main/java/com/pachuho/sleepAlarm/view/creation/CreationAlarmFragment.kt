package com.pachuho.sleepAlarm.view.creation

import android.os.Bundle
import android.view.View
import android.widget.CheckedTextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pachuho.sleepAlarm.ViewModelFactory
import com.pachuho.sleepAlarm.base.BaseFragment
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import com.pachuho.sleepAlarm.data.datasource.model.DayOfWeek
import com.pachuho.sleepAlarm.data.repository.AlarmRepository
import com.pachuho.sleepAlarm.utils.showSnackBar
import com.pachuho.sleepAlarm.view.main.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import sleepAlarm.R
import sleepAlarm.databinding.FragmentCreationAlarmBinding
import kotlin.collections.ArrayList


class CreationAlarmFragment : BaseFragment<FragmentCreationAlarmBinding, CreationAlarmViewModel>(R.layout.fragment_creation_alarm) {
    override val viewModel: CreationAlarmViewModel by viewModels { ViewModelFactory(AlarmRepository()) } // 프래그먼트 뷰모델
    private var alarmJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.tpAlarm.minute += 1
        setButtonClickEvent()
    }

    private fun handleEvent(event: CreationAlarmViewModel.Event) = when (event) {
        is CreationAlarmViewModel.Event.UpdateAlarm -> completeCreationAlarm(event.alarm)
        is CreationAlarmViewModel.Event.CheckDayOfWeek -> modifyDayOfWeek(event.view)
    }

    private fun modifyDayOfWeek(view: View) = with(binding){
        viewModel.targetDayOfWeek.apply {
            when(view.id){
                ctvDayMon.id -> monday = !monday
                ctvDayTue.id -> tuesday = !tuesday
                ctvDayWen.id -> wednesday = !wednesday
                ctvDayThu.id -> thursday = !thursday
                ctvDayFri.id -> friday = !friday
                ctvDaySat.id -> saturday = !saturday
                ctvDaySun.id -> sunday = !sunday
            }
        }
        viewModel.setDayOfWeek()
    }

    private fun completeCreationAlarm(alarm: Alarm){
        setUseAlarm(alarm)
        view?.showSnackBar("알람이 생성되었습니다.")
        popBackStack()
    }

    private fun popBackStack(){
        findNavController().popBackStack()
    }

    private fun sendCheckStatus(){
        binding.apply {
            val viewList: List<CheckedTextView> = listOf(
                ctvDayMon,
                ctvDayTue,
                ctvDayWen,
                ctvDayThu,
                ctvDayFri,
                ctvDaySat,
                ctvDaySun
            )

            val checkList = ArrayList<Boolean>()

            viewList.forEach {
                checkList.add(it.isChecked)
            }

            if(checkList.contains(false)){
                viewList.forEach {
                    it.isChecked = true
                }
            } else{
                viewList.forEach {
                    it.isChecked = false
                }
            }

            viewModel.targetDayOfWeek.apply {
                sunday = ctvDaySun.isChecked
                monday = ctvDayMon.isChecked
                tuesday = ctvDayTue.isChecked
                wednesday = ctvDayWen.isChecked
                thursday = ctvDayThu.isChecked
                friday = ctvDayFri.isChecked
                saturday = ctvDaySat.isChecked
            }

        }
        viewModel.setDayOfWeek()
    }

    private fun getDayOfWeek(): DayOfWeek = with(binding) {
        return DayOfWeek(
            monday = ctvDayMon.isChecked, tuesday = ctvDayTue.isChecked,
            wednesday = ctvDayWen.isChecked, thursday = ctvDayThu.isChecked,
            friday = ctvDayFri.isChecked, saturday = ctvDaySat.isChecked,
            sunday = ctvDaySun.isChecked
        )
    }

    private fun createAlarm() = with(binding){
        val days = getDayOfWeek()
        val alarm = Alarm(0, true, tpAlarm.hour, tpAlarm.minute, days, sbVolume.progress, cbVibration.isChecked)
        viewModel.createAlarm(alarm)
    }

    private fun onClick(view: View) = with(binding){
        when(view){
            ibBackPress -> popBackStack()
            btnCreationSave -> createAlarm()
            cbEveryDay -> sendCheckStatus()
        }
    }

    private fun setButtonClickEvent() = with(binding){
        val list = listOf(
            ibBackPress,
            btnCreationSave,
            cbEveryDay
        )

        list.forEach { it ->
            it.setOnClickListener { onClick(it) }
        }
    }

    override fun onStart() {
        super.onStart()
        alarmJob = lifecycleScope.launch {
            viewModel.eventFlow.collect {
                    event -> handleEvent(event)
            }
        }
    }

    override fun onStop() {
        alarmJob?.cancel()
        super.onStop()
    }
}