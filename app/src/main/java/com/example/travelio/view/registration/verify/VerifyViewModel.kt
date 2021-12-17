package com.example.travelio.view.registration.verify

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class VerifyViewModel(app:Application): AndroidViewModel(app) {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess : LiveData<Boolean>
        get() = _isSuccess

    private val _error = MutableLiveData<String>()
    val error : LiveData<String>
        get() = _error
}