package com.pachuho.sleepAlarm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private val _baseEventFlow = MutableSharedFlow<Event>()
    val baseEventFlow = _baseEventFlow.asSharedFlow()

    private fun event(event: Event){
        viewModelScope.launch {
            _baseEventFlow.emit(event)
        }
    }

    sealed class Event{
        data class ShowSnackBar(val text: String) : Event()
    }


}