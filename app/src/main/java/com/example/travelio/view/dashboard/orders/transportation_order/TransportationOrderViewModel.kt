package com.example.travelio.view.dashboard.orders.transportation_order

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelio.model.data.TransportReserveData
import com.example.travelio.model.preference.Token
import com.example.travelio.requests.orders.DeleteOrders
import com.example.travelio.requests.orders.Orders
import com.example.travelio.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class TransportationOrderViewModel(private val app: Application) : AndroidViewModel(app) {

    private var _data = MutableLiveData<List<TransportReserveData>>()
    val data: LiveData<List<TransportReserveData>>
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
        getTransportOrders()
    }

    private fun getTransportOrders() {
       viewModelScope.launch(Dispatchers.IO) {
       try {
           loading()
           getValidOrdersData()
       }catch(e:Exception){
           handleException(e)
       }
       }
    }

    private suspend fun getValidOrdersData(){
        Orders.getTransportsOrdersData(1,app.resources){data, error, success ->
            stopLoading()
            if (success){if(data != null){_data.value = data.data.Transports.valid_transports!! }
            }else{
                if(error.isNullOrEmpty()){authFail()}else{whenFail(error)}
            }
        }
    }

    fun sendDeleteRequest(data: TransportReserveData) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                DeleteOrders.deleteTransportOrder(data.pivot.id,app.resources){ done, error ->
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
    override fun onCleared() {
        Log.i("ClearedDataTrans","Clear")
        super.onCleared()
    }

}