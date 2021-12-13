package com.example.bezo.view.dashboard.transportation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.R
import com.example.bezo.model.data.TransportFilterCollection
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

    private var _data = MutableLiveData<MutableList<Transportation>?>()
    val data : LiveData<MutableList<Transportation>?>
        get() = _data

    private var _filterData = MutableLiveData<TransportFilterCollection?>()
    val filterData : LiveData<TransportFilterCollection?>
        get() = _filterData

    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?>
        get() = _loading

    private val _swipeLoading = MutableLiveData<Boolean?>()
    val swipeLoading: LiveData<Boolean?>
        get() = _swipeLoading


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

    fun callRequest(city_from_id:String?=app.resources.getString(R.string.EMPTY), city_to_id:String?=app.resources.getString(R.string.EMPTY), level_id:String?=app.resources.getString(R.string.EMPTY), type_id:String?=app.resources.getString(R.string.EMPTY), minPrice:String?=app.resources.getString(R.string.EMPTY), maxPrice:String?=app.resources.getString(R.string.EMPTY), fromDate:String?=app.resources.getString(R.string.EMPTY), toDate:String?=app.resources.getString(R.string.EMPTY)){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                getTransports(city_from_id!!, city_to_id!!, level_id!!, type_id!!, minPrice!!, maxPrice!! , fromDate!!, toDate!!)
            }catch (t: Exception){
                handleException(t)
            }
        }
    }
    //send pageNumber and get the response (Transports Data)
    private suspend fun getTransports(city_from_id: String, city_to_id: String, level_id: String, type_id: String, minPrice: String, maxPrice: String, fromDate: String, toDate: String) {
        loading()
        pageNumber++
       TransportationRequests.getTransportations(pageNumber,app.resources,city_from_id, city_to_id, level_id, type_id, minPrice, maxPrice , fromDate, toDate){ data, error, success ->
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
    fun resetData(){
        _data.value = null
        pageNumber = 0
    }
    //Display Transports Data
    private  fun whenSuccess(data: Transportations?) {
        if (data != null) {
            val transportData = data.data.Transports.data
            if(transportData.isNotEmpty()){
                _loadMore.value = true
                if(_data.value.isNullOrEmpty()){
                    _data.value = transportData.toMutableList()
                    _data.value = _data.value
                }else{
                    _data.value!!.addAll(transportData)
                    _data.value = _data.value
                }
            }else{
                _loadMore.value = false
            }
            stopLoading()
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
        _swipeLoading.value = false
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

    fun saveFilterData(data: TransportFilterCollection) {
        _filterData.value = data
    }
    fun swipeLoadHandle(){
        resetData()
        _swipeLoading.value = true
        if(_filterData.value != null){
            callRequest(_filterData.value!!.city_from_id, _filterData.value!!.city_to_id, _filterData.value!!.level_id, _filterData.value!!.type_id, _filterData.value!!.minPrice, _filterData.value!!.maxPrice, _filterData.value!!.fromDate,_filterData.value!!.toDate)
        }else{
            callRequest()
        }
    }
}