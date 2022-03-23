package com.pachuho.sleepAlarm.views.alarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pachuho.sleepAlarm.Event
import com.pachuho.sleepAlarm.base.BaseViewModel
import timber.log.Timber

class AlarmViewModel: BaseViewModel() {
    private val _isFabOpen = MutableLiveData<Event<Boolean>>()
    val isFabOpen: LiveData<Event<Boolean>> get() = _isFabOpen

    init {
        _isFabOpen.value = Event(false)
    }

    fun changeFabVisibility(){
        _isFabOpen.value = Event(!_isFabOpen.value?.peekContent()!!)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared() AlarmViewModel!")
    }
}