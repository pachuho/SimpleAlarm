package com.pachuho.sleepAlarm.view.creation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pachuho.sleepAlarm.base.BaseViewModel

class CreationAlarmViewModel: BaseViewModel() {
    private val _isTest = MutableLiveData(false)
    val isTest1: LiveData<Boolean> get() = _isTest

    fun show(){
        _isTest.value = true
    }

    fun reset(){
        _isTest.value = false
    }

}