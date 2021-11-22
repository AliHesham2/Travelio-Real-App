package com.example.bezo.view.dashboard.fulltrip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.model.data.FullTrip
import com.example.bezo.model.data.FullTrips
import com.example.bezo.model.preference.Token
import com.example.bezo.requests.fulltrip.FullTripRequests
import com.example.bezo.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FullTripViewModel(private val app:Application):AndroidViewModel(app) {
    private var pageNumber = 0

    private var _data = MutableLiveData<List<FullTrip>>()
    val data: LiveData<List<FullTrip>>
        get() = _data

    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?>
        get() = _loading

    private val _noAuth = MutableLiveData<Boolean?>()
    val noAuth: LiveData<Boolean?>
        get() = _noAuth

    private val _loadMore = MutableLiveData<Boolean?>()
    val loadMore: LiveData<Boolean?>
        get() = _loadMore

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    init {
        callRequest()
    }

    fun callRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getFullTrips()
            } catch (t: Exception) {
                handleException(t)
            }
        }
    }

    private suspend fun getFullTrips() {
        loading()
        pageNumber++
        FullTripRequests.getFullPackage(pageNumber,app.resources){ data, error, success ->
            if(success){
                whenSuccess(data)
            }else{
                if(error == null){
                    authFail()
                }else{
                    whenFail(error)
                }
            }
        }
    }

    private fun whenSuccess(data: FullTrips?) {
        if (data != null) {
            stopLoading()
            val tripData = data.data.Package.data
            if (tripData.isNotEmpty()) {
                _loadMore.value = true
                if (_data.value.isNullOrEmpty()) {
                    _data.value = tripData
                } else {
                    _data.value = _data.value!!.plus(tripData)
                }
            } else {
                _loadMore.value = false
            }
        }
    }

    //Show Loading spinner
    private suspend fun loading() {
        withContext(Dispatchers.Main) {
            _loading.value = true
        }
    }

    //No Network Handler
    private suspend fun handleException(t: Exception) {
        stopLoading()
        withContext(Dispatchers.Main) {
            PopUpMsg.handleError(app.applicationContext, t)
        }
    }

    //Hide loading spinner
    private fun stopLoading() {
        _loading.value = false
    }

    //Handle Backend Errors
    private fun whenFail(msg: String) {
        stopLoading()
        _error.value = msg
    }

    //Session Expired
    private fun authFail() {
        stopLoading()
        Token.removeToken()
        _noAuth.value = true
    }
}