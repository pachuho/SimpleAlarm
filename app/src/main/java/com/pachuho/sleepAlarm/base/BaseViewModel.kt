package com.pachuho.sleepAlarm.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    override fun onCleared() {

        super.onCleared()
    }

    fun showProgress() {
        _isLoading.value = true
    }

    fun hideProgress() {
        _isLoading.value = false
    }
}