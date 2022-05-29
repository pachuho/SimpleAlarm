package com.pachuho.sleepAlarm.view.creation

import android.view.View
import android.widget.CheckedTextView
import androidx.lifecycle.viewModelScope
import com.pachuho.sleepAlarm.base.BaseViewModel
import com.pachuho.sleepAlarm.data.datasource.model.Day
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber


class CreationAlarmViewModel: BaseViewModel() {
    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _repetition = MutableSharedFlow<Day>()
    val repetition = _repetition.asSharedFlow()

    fun onTimeChanged(hour: Int, minute: Int){
        Timber.e("onTimeChanged! $hour:$minute")
    }

    fun setToggle(v: View){
        (v as CheckedTextView).isChecked = !v.isChecked
    }

    fun setEveryDay(){

    }

    private fun event(event: Event){
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class Event{
        data class ShowSnackBar(val text: String) : Event()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared() AlarmViewModel!")
    }
}