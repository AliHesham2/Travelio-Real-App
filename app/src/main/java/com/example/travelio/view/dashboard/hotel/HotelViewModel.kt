package com.example.travelio.view.dashboard.hotel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelio.R
import com.example.travelio.model.data.Hotel
import com.example.travelio.model.data.HotelFilterCollection
import com.example.travelio.model.data.Hotels
import com.example.travelio.model.preference.Token
import com.example.travelio.requests.hotel.HotelRequests
import com.example.travelio.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HotelViewModel(private val app:Application): AndroidViewModel(app) {

    private var pageNumber = 0

    private var _data = MutableLiveData<MutableList<Hotel>?>()
    val data : LiveData<MutableList<Hotel>?>
        get() = _data

    private var _filterData = MutableLiveData<HotelFilterCollection?>()
    val filterData : LiveData<HotelFilterCollection?>
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

      fun callRequest(hotelID:String?=app.resources.getString(R.string.EMPTY), hotelCityID:String?=app.resources.getString(R.string.EMPTY), mealID:String?=app.resources.getString(R.string.EMPTY), stars:String?=app.resources.getString(R.string.EMPTY), perRoom:String?=app.resources.getString(R.string.EMPTY), minPrice:String?=app.resources.getString(R.string.EMPTY), maxPrice:String?=app.resources.getString(R.string.EMPTY)){
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
        HotelRequests.getHotels(pageNumber,app.resources,hotelID!!,hotelCityID!!,mealID!!,stars!!,perRoom!!,minPrice!!,maxPrice!!,app.resources.getString(R.string._0)){ data, error, success ->
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
            val hotelData = data.data.hotels.data
            if(hotelData.isNotEmpty()){
                _loadMore.value = true
                if(_data.value.isNullOrEmpty()){
                    _data.value = hotelData.toMutableList()
                    _data.value = _data.value
                }else{
                    _data.value!!.addAll(hotelData)
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
    //Stop loading spinner
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

    fun saveFilterData(data: HotelFilterCollection) {
        _filterData.value = data
    }

    fun swipeLoadHandle(){
        resetData()
        _swipeLoading.value = true
        if(_filterData.value != null){
            callRequest(_filterData.value!!.hotels_list_id, _filterData.value!!.hotels_list_city_id, _filterData.value!!.meal_id, _filterData.value!!.stars, _filterData.value!!.perRoom, _filterData.value!!.minPrice, _filterData.value!!.maxPrice)
        }else{
            callRequest()
        }
    }
}