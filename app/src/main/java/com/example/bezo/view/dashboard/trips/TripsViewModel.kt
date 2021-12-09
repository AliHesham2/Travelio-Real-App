package com.example.bezo.view.dashboard.trips

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.model.data.Trip
import com.example.bezo.model.data.Trips
import com.example.bezo.model.preference.Token
import com.example.bezo.requests.trip.TripRequests
import com.example.bezo.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TripsViewModel(private val app:Application) : AndroidViewModel(app) {
    private var pageNumber = 0

    private var _data = MutableLiveData<List<Trip>>()
    val data : LiveData<List<Trip>>
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

    fun callRequest( city_id:String?="", location_id:String?="", minPrice:String?="", maxPrice:String?="", fromDate:String?="", toDate:String?=""){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                getTrips(city_id, location_id, minPrice, maxPrice,fromDate,toDate)
            }catch (t: Exception){
                handleException(t)
            }
        }
    }

    //send pageNumber and get the response (Trips Data)
    private suspend fun getTrips(city_id: String?, location_id: String?, minPrice: String?, maxPrice: String?, fromDate: String?, toDate: String?) {
        loading()
        pageNumber++
        TripRequests.getTrips(pageNumber,app.resources,city_id!!, location_id!!, minPrice!!, maxPrice!!,fromDate!!,toDate!!){ data, error, success ->
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

    //Display Trips data
    private  fun whenSuccess(data: Trips?) {
        if (data != null) {
            stopLoading()
            val tripData = data.data.trips.data
            if(tripData.isNotEmpty()){
                _loadMore.value = true
                if(_data.value.isNullOrEmpty()){
                    _data.value = tripData
                }else{
                    _data.value = _data.value!!.plus(tripData)
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