package com.example.bezo.view.dashboard.transportation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.R
import com.example.bezo.model.data.Transportation
import com.example.bezo.model.data.Transportations
import com.example.bezo.model.preference.Token
import com.example.bezo.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

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

    private val _loading1 = MutableLiveData<Boolean?>()
    val loading1: LiveData<Boolean?>
        get() = _loading1

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
                if (t is IOException) {
                    whenFail(app.resources.getString(R.string.NO_INTERNET))
                } else {
                    whenFail(app.resources.getString(R.string.WRONG))
                }
            }
        }
    }

    private suspend fun getTransports() {
        toggleLoading()
        pageNumber++
        val response = AppApi.appData.getTransportations(5, pageNumber)
        if (response.isSuccessful) {
            val data = response.body()
            whenSuccess(data)
        }else{
            if(response.code() == 401){
                authFail()
            }else{
                val error = response.errorBody()?.charStream()?.readText()
                if (error != null) {
                    whenFail(JSONObject(error).getString(app.resources.getString(R.string.MESSAGE)))
                }
            }
        }

    }
    private suspend fun toggleLoading(){
        withContext(Dispatchers.Main){
            if(_data.value == null){
                _loading.value = true
            }else{
                _loading1.value = true
            }
        }
    }

    private suspend fun whenSuccess(data: Transportations?) {
        withContext(Dispatchers.Main){
            if (data != null) {
                val transportData = data.data.Transports.data
                if(transportData.isNotEmpty()){
                    _loadMore.value = true
                    if(_data.value.isNullOrEmpty()){
                        _loading.value =false
                        _data.value = transportData
                    }else{
                        _loading1.value =false
                        _data.value = _data.value!!.plus(transportData)
                    }
                }else{
                    _loadMore.value = false
                }
            }
        }
    }

    private suspend fun authFail(){
        withContext(Dispatchers.Main){
            Token.removeToken()
            _noAuth.value = true
        }
    }

    private suspend fun whenFail(msg:String){
        withContext(Dispatchers.Main){
            _loading.value = false
            _loading1.value = false
            _error.value = msg
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("cleared","Cleared")
        _error.value = null
        _loading.value = null
        _loadMore.value = null
        _loading1.value = null
        _noAuth.value = null
    }
}