package com.example.travelio.view.dashboard.orders.full_trip_order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelio.model.data.FullTripReserveData
import com.example.travelio.model.preference.Token
import com.example.travelio.requests.orders.DeleteOrders
import com.example.travelio.requests.orders.Orders
import com.example.travelio.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class FullTripOrderViewModel (private val app: Application) : AndroidViewModel(app) {

    private var _data = MutableLiveData<List<FullTripReserveData>>()
    val data: LiveData<List<FullTripReserveData>>
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
        getTripOrders()
    }

    private fun getTripOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loading()
                getValidOrdersData()
            }catch(e: Exception){
                handleException(e)
            }
        }
    }

    private suspend fun getValidOrdersData(){
        Orders.getFullTripOrdersData(1,app.resources){ data, error, success ->
            stopLoading()
            if (success){if(data != null){_data.value = data.data.Packages.valid_packages!! }
            }else{
                if(error.isNullOrEmpty()){authFail()}else{whenFail(error)}
            }
        }
    }

    fun sendDeleteRequest(data: FullTripReserveData) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                DeleteOrders.deleteFullTripOrder(data.pivot.id,app.resources){ done, error ->
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
}