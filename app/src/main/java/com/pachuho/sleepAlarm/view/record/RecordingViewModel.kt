package com.pachuho.sleepAlarm.view.record

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pachuho.sleepAlarm.base.BaseViewModel

class RecordingViewModel: BaseViewModel() {
    // 라이브데이터 선언
    private val _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean> get() = _isVisible

    private val isMessage = MutableLiveData<String>()

    // 데이터 초기화
    init {
        _isVisible.value = false
    }




    // 데이터 변경
    fun changeVisible(){ _isVisible.value = !_isVisible.value!!}

    override fun onCleared() {
        super.onCleared()
    }
}