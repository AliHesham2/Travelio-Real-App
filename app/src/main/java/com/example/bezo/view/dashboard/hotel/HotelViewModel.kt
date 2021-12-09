package com.example.bezo.view.dashboard.hotel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.model.data.Hotel
import com.example.bezo.model.data.Hotels
import com.example.bezo.model.preference.Token
import com.example.bezo.requests.hotel.HotelRequests
import com.example.bezo.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HotelViewModel(private val app:Application): AndroidViewModel(app) {

    private var pageNumber = 0

    private var _data = MutableLiveData<List<Hotel>?>()
    val data : LiveData<List<Hotel>?>
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

      fun callRequest(hotelID:String?="",hotelCityID:String?="",mealID:String?="", stars:String?="",perRoom:String?="",minPrice:String?="",maxPrice:String?=""){
             viewModelScope.launch(Dispatchers.IO) {
                 try{
                     getHotelsData(hotelID,hotelCityID,mealID,stars,perRoom,minPrice,maxPrice)
                 }catch (t: Exception){
                     handleException(t)
                 }
             }
      }

    // send pageNumber and get the response (Hotels Data)
    private suspend fun getHotelsData(hotelID: String?, hotelCityID: String?, mealID: String?, stars: String?, perRoom: String?, minPrice: String?, maxPrice: String?) {
        loading()
        pageNumber++
        HotelRequests.getHotels(pageNumber,app.resources,hotelID!!,hotelCityID!!,mealID!!,stars!!,perRoom!!,minPrice!!,maxPrice!!){ data, error, success ->
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
    //Display data
    private  fun whenSuccess(data: Hotels?) {
        if (data != null) {
            stopLoading()
            val hotelData = data.data.hotels.data
            if(hotelData.isNotEmpty()){
                _loadMore.value = true
                if(_data.value.isNullOrEmpty()){
                    _data.value = hotelData
                }else{
                    _data.value = _data.value!!.plus(hotelData)
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