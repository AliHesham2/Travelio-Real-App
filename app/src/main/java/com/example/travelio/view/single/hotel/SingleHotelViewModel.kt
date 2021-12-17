package com.example.travelio.view.single.hotel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelio.R
import com.example.travelio.model.data.*
import com.example.travelio.model.preference.Token
import com.example.travelio.requests.booking.Booking
import com.example.travelio.view.util.BookingPopUp
import com.example.travelio.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SingleHotelViewModel(private val receivedData: Hotel,private val app: Application):AndroidViewModel(app) {

    private var _data = MutableLiveData<Hotel?>()
    val data : LiveData<Hotel?>
    get() = _data

    private val _noImage = MutableLiveData<Boolean?>()
    val noImage: LiveData<Boolean?>
        get() = _noImage

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
    }

    private fun checkImage(){
        if(receivedData.images.isNullOrEmpty()){
            _noImage.value = true
        }
    }

    private fun book(quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendBookingRequest(HotelBookingData(quantity,receivedData.id))
            }catch (t:Exception){
                handleException(t)
            }
        }
    }

    fun onBookClick(view: View) {
        BookingPopUp.handleBookingPopUp(view.context){
            book(it)
        }
    }

    private suspend fun sendBookingRequest(hotelBookingData: HotelBookingData) {
        loading()
        Booking.hotelBooking(hotelBookingData,app.resources){ success, error ->
            stopLoading()
            if(success){
                PopUpMsg.toastMsg(app.applicationContext,app.resources.getString(R.string.BOOKING_DONE))
                sendNotifications()
            }else{
                if(error != null){
                    whenFail(error)
                }else{
                    authFail()
                }
            }
        }
    }

    private  fun sendNotifications(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                launch { sendNotificationToFcm() }
                launch { sendNotificationToServer() }
            }catch (e:Exception){
                handleException(e)
            }
        }
    }
//TODO don't forget to stop loading
    private suspend fun sendNotificationToFcm(){
        if (receivedData.companies.device_token != null){
            val fcmMsg = app.resources.getString(R.string.RESERVATION_MSG) +  receivedData.hotels_list.name + app.resources.getString(R.string.EMPTY_WITH_SPACE) + app.resources.getString(R.string.HOTEL)
            val fcm = FcmModel(receivedData.companies.device_token, FcmDetail(app.resources.getString(R.string.HOTEL_RESERVATION),fcmMsg), FcmData(app.resources.getString(R.string.HOTEL) ) )
        }
    }

    private suspend fun sendNotificationToServer(){
        val server = SendNotificationPostRequest(receivedData.id,app.resources.getString(R.string.HOTEL))
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