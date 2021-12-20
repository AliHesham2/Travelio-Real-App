package com.example.travelio.view.single.trips

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
import com.example.travelio.requests.fcm.UpdateToken
import com.example.travelio.view.util.BookingPopUp
import com.example.travelio.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SingleTripsViewModel(private val receivedData: Trip,private val app: Application): AndroidViewModel(app) {

    private var _data = MutableLiveData<Trip?>()
    val data : LiveData<Trip?>
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
    private fun book(quantity:Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendBookingRequest(TripsBookingData(quantity,receivedData.id))
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

    private suspend fun sendBookingRequest(tripsBookingData: TripsBookingData) {
        loading()
        Booking.tripBooking(tripsBookingData, app.resources){ success, error ->
            if(success){
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

    private suspend fun sendNotificationToFcm(){
        if (receivedData.companies.device_token != null){
            val fcmMsg = app.resources.getString(R.string.RESERVATION_MSG) +  app.resources.getString(R.string.EMPTY_WITH_SPACE) + receivedData.title + app.resources.getString(R.string.EMPTY_WITH_SPACE) + app.resources.getString(R.string.TRIPS)
            val fcm = FcmModel(receivedData.companies.device_token,FcmDetail(app.resources.getString(R.string.TRIPS_RESERVATION),fcmMsg),FcmData(app.resources.getString(R.string.TRIPS) ))
            UpdateToken.sendNotificationToFCM(app.resources,fcm){ error, success ->
                if(success){
                    stopLoading()
                }else{
                    if(error != null){
                        whenFail(error)
                    }else{
                        authFail()
                    }
                }
            }
        }
    }

    private suspend fun sendNotificationToServer(){
        val server = SendNotificationPostRequest(receivedData.id,app.resources.getString(R.string.TRIPS))
        UpdateToken.sendNotificationToServer(app.resources,server){ error, success ->
            if(success){
                PopUpMsg.toastMsg(app.applicationContext,app.resources.getString(R.string.BOOKING_DONE))
                stopLoading()
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