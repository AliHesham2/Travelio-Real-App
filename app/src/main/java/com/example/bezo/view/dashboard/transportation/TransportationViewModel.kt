package com.example.bezo.view.dashboard.transportation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.model.data.Transportation
import com.example.bezo.model.data.Transportations
import com.example.bezo.model.preference.Token
import com.example.bezo.requests.transportation.TransportationRequests
import com.example.bezo.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TransportationViewModel(private val app:Application):AndroidViewModel(app) {
    private var pageNumber = 0

    private var _data = MutableLiveData<List<Transportation>>()
    val data : LiveData<List<Transportation>>
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
    val error : LiveData<String?>
        get() = _error

    init {
        callRequest()
    }

    fun callRequest(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                getTransports()
            }catch (t: Exception){
                handleException(t)
            }
        }
    }
    //send pageNumber and get the response (Transports Data)
    private suspend fun getTransports() {
        loading()
        pageNumber++
       TransportationRequests.getTransportations(pageNumber,app.resources){ data, error, success ->
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

    //Display Transports Data
    private  fun whenSuccess(data: Transportations?) {
        if (data != null) {
            stopLoading()
            val transportData = data.data.Transports.data
            if(transportData.isNotEmpty()){
                _loadMore.value = true
                if(_data.value.isNullOrEmpty()){
                    _data.value = transportData
                }else{
                    _data.value = _data.value!!.plus(transportData)
                }
            }else{
                _loadMore.value = false
            }
        }
    }


    //Show Loading spinner
    private suspend fun loading(){
        withContext(Dispatchers.Main){
            _loading.value = true
        }
    }
    //No Network Handler
    private suspend fun handleException(t: Exception) {
        withContext(Dispatchers.Main){
            stopLoading()
            PopUpMsg.handleError(app.applicationContext,t)
        }
    }
    //Hide loading spinner
    private  fun stopLoading(){
        _loading.value = false
    }
    //Handle Backend Errors
    private  fun whenFail(msg:String){
        stopLoading()
        _error.value = msg
    }
    //Session Expired
    private  fun authFail(){
        stopLoading()
        Token.removeToken()
        _noAuth.value = true
    }

}