package com.pachuho.sleepAlarm.base

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pachuho.sleepAlarm.view.alarm.AlarmViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private val _eventFlow = MutableSharedFlow<AlarmViewModel.Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun showSnackBar(msg: String) {
        event(AlarmViewModel.Event.ShowSnackBar(msg))
    }

    private fun event(event: AlarmViewModel.Event){
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }
}