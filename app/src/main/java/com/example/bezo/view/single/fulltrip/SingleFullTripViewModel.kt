package com.example.bezo.view.single.fulltrip

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.R
import com.example.bezo.model.data.FullTrip
import com.example.bezo.model.data.FullTripBookingData
import com.example.bezo.model.preference.Token
import com.example.bezo.requests.booking.Booking
import com.example.bezo.view.util.BookingPopUp
import com.example.bezo.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception


class SingleFullTripViewModel(private val receivedData: FullTrip,private val  app: Application): AndroidViewModel(app) {

    private var _data = MutableLiveData<FullTrip?>()
    val data : LiveData<FullTrip?>
        get() = _data

    private val _noImage = MutableLiveData<Boolean?>()
    val noImage: LiveData<Boolean?>
        get() = _noImage

    private val _noHotel = MutableLiveData<Boolean?>()
    val noHotel: LiveData<Boolean?>
        get() = _noHotel

    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?>
        get() = _loading

    private val _error = MutableLiveData<String?>()
    val error : LiveData<String?>
        get() = _error

    private val _noAuth = MutableLiveData<Boolean?>()
    val noAuth: LiveData<Boolean?>
        get() = _noAuth

    init {
        _data.value = receivedData
        checkImage()
        checkHotel()
    }

    private fun checkImage(){
        if(receivedData.images.isNullOrEmpty()){
            _noImage.value = true
        }
    }
    private fun checkHotel(){
        if(receivedData.hotels_list.isNullOrEmpty()){
            _noHotel.value = true
        }
    }

    private fun book(quantity:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendBookingRequest(FullTripBookingData(quantity,receivedData.id))
            }catch (t: Exception){
                handleException(t)
            }
        }
    }

    fun onBookClick(view: View) {
        BookingPopUp.handleBookingPopUp(view.context){
            book(it)
        }
    }

    private suspend fun sendBookingRequest(fullTripBookingData: FullTripBookingData) {
        loading()
        Booking.fullTripBooking(fullTripBookingData, app.resources){ success, error ->
            stopLoading()
            if(success){
                PopUpMsg.toastMsg(app.applicationContext,app.resources.getString(R.string.BOOKING_DONE))
            }else{
                if(error != null){
                    whenFail(error)
                }else{
                    authFail()
                }
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