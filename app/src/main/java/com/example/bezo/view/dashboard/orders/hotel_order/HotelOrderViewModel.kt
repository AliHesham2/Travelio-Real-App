package com.example.bezo.view.dashboard.orders.hotel_order

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.model.data.HotelReserveData
import com.example.bezo.model.preference.Token
import com.example.bezo.requests.orders.DeleteOrders
import com.example.bezo.requests.orders.Orders
import com.example.bezo.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class HotelOrderViewModel (private val app: Application) : AndroidViewModel(app) {

    private var _data = MutableLiveData<List<HotelReserveData>>()
    val data: LiveData<List<HotelReserveData>>
        get() = _data

    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?>
        get() = _loading

    private val _noAuth = MutableLiveData<Boolean?>()
    val noAuth: LiveData<Boolean?>
        get() = _noAuth


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    init {
        getHotelOrders()
    }

    private fun getHotelOrders(){
        viewModelScope.launch(Dispatchers.IO) {
        try {
            loading()
            getHotelOrdersData()
        }catch (e:Exception){
            handleException(e)
        }
        }
    }
    private suspend fun getHotelOrdersData() {
        Orders.getHotelOrdersData(app.resources) { data, error, success ->
            stopLoading()
            if(success){
                if(data != null){
                    _data.value = data.data.Hotels.hotels
                }
            }else{
                if(error.isNullOrEmpty()){authFail()}else{whenFail(error)}
            }
        }
    }

    fun sendDeleteRequest(data: HotelReserveData) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                DeleteOrders.deleteHotelOrder(data.pivot.id,app.resources){ done, error ->
                    if(done){
                        _data.value = _data.value!!.minus(data)
                    }else{
                        if(error != null){
                            whenFail(error)
                        }else{
                            authFail()
                        }
                    }
                }
            }catch (e:Exception){
                Log.i("errorzz",e.message.toString())
                handleException(e)
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
    //Stop loading spinner
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


