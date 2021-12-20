package com.example.travelio.view.dashboard.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelio.R
import com.example.travelio.model.data.*
import com.example.travelio.model.data.Collection
import com.example.travelio.model.preference.DeviceToken
import com.example.travelio.model.preference.Token
import com.example.travelio.requests.fcm.UpdateToken
import com.example.travelio.requests.fulltrip.FullTripRequests
import com.example.travelio.requests.hotel.HotelRequests
import com.example.travelio.requests.static_list.*
import com.example.travelio.requests.transportation.TransportationRequests
import com.example.travelio.requests.trip.TripRequests
import com.example.travelio.requests.user.UserRequests
import com.example.travelio.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class DashBoardViewModel(private val app: Application,private val  userData: UserData?):AndroidViewModel(app) {

    private var _data = MutableLiveData<Users?>()
    val data : LiveData<Users?>
        get() = _data

    private val _hotelListData = MutableLiveData<MutableList<Hotel>?>()
    val hotelListData : LiveData<MutableList<Hotel>?>
        get() = _hotelListData

    private val _tripListData = MutableLiveData<MutableList<Trip>?>()
    val tripListData : LiveData<MutableList<Trip>?>
        get() = _tripListData

    private val _transportListData = MutableLiveData<MutableList<Transportation>?>()
    val transportListData : LiveData<MutableList<Transportation>?>
        get() = _transportListData

    private val _packageListData = MutableLiveData<MutableList<FullTrip>?>()
    val packageListData : LiveData<MutableList<FullTrip>?>
        get() = _packageListData

    private var hotelPageNumber = 0
    private var transportPageNumber = 0
    private var packagePageNumber = 0
    private var tripPageNumber = 0

    private val _hotelLoadMore = MutableLiveData<Boolean?>()
    val hotelLoadMore: LiveData<Boolean?>
        get() = _hotelLoadMore

    private val _transportLoadMore = MutableLiveData<Boolean?>()
    val transportLoadMore: LiveData<Boolean?>
        get() = _transportLoadMore

    private val _tripLoadMore = MutableLiveData<Boolean?>()
    val tripLoadMore: LiveData<Boolean?>
        get() = _tripLoadMore

    private val _fullTripLoadMore = MutableLiveData<Boolean?>()
    val fullTripLoadMore: LiveData<Boolean?>
        get() = _fullTripLoadMore


    private val _token = DeviceToken.getDeviceToken()

    private val _citiesListData = MutableLiveData<Cities>()

    private val _typesListData = MutableLiveData<Types>()

    private val _levelsListData = MutableLiveData<Levels>()

    private val _locationsListData = MutableLiveData<Locations>()

    private val _mealsListData = MutableLiveData<Meals>()

    private val _hotelNamesListData = MutableLiveData<HotelNames>()


    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?>
        get() = _loading

    private val _noAuth = MutableLiveData<Boolean?>()
    val noAuth: LiveData<Boolean?>
        get() = _noAuth


    private val _error = MutableLiveData<String?>()
    val error : LiveData<String?>
        get() = _error

    init {
        getStatics()
    }

    private fun getStatics() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loading()
                if (userData == null){
                    launch { checkIfUserDataExist() }
                }else{
                    if (userData.device_token != _token){
                        launch { updateToken(DeviceToken.getDeviceToken()!!) }
                    }
                }
                launch { getCities() }
                launch { getLevels() }
                launch { getMeals()  }
                launch { getTypes()  }
                launch { getLocations() }
                launch { getHotelList() }
                launch { getHotelsData() }
                launch { getTransports() }
                launch { getTrips() }
                launch { getFullTrips() }
            }catch (e:Exception){
                handleException(e)
            }
        }
    }
     fun callRequestHotel(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                getHotelsData()
            }catch (t: Exception){
                handleException(t)
            }
        }
    }
     fun callRequestTransport(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                getTransports()
            }catch (t: Exception){
                handleException(t)
            }
        }
    }
     fun callRequestTrip(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                getTrips()
            }catch (t: Exception){
                handleException(t)
            }
        }
    }
     fun callRequestPackage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getFullTrips()
            } catch (t: Exception) {
                handleException(t)
            }
        }
    }

    private suspend fun getHotelsData() {
        hotelPageNumber++
        HotelRequests.getHotels(hotelPageNumber,app.resources,app.resources.getString(R.string.EMPTY),app.resources.getString(R.string.EMPTY),app.resources.getString(R.string.EMPTY),app.resources.getString(R.string.EMPTY),app.resources.getString(R.string.EMPTY),app.resources.getString(R.string.EMPTY),app.resources.getString(R.string.EMPTY),app.resources.getString(R.string._1)){ data, error, success ->
            if(success){
                if (data != null) {
                    val hotelData = data.data.hotels.data
                    if(hotelData.isNotEmpty()){
                        _hotelLoadMore.value = true
                        if(_hotelListData.value.isNullOrEmpty()){
                            _hotelListData.value = hotelData.toMutableList()
                            _hotelListData.value = _hotelListData.value
                        }else{
                            _hotelListData.value!!.addAll(hotelData)
                            _hotelListData.value = _hotelListData.value
                        }
                    }else{
                        _hotelLoadMore.value = false
                    }
                }
            }else{
                if(error == null){
                    authFail()
                }else{
                    whenFail(error)
                }
            }

        }
    }


    private suspend fun getTransports() {
        transportPageNumber++
        TransportationRequests.getTransportations(transportPageNumber,app.resources,app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY) , app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY),app.resources.getString(R.string._1)){ data, error, success ->
            if(success){
                if (data != null) {
                    val transportData = data.data.Transports.data
                    if(transportData.isNotEmpty()){
                        _transportLoadMore.value = true
                        if(_transportListData.value.isNullOrEmpty()){
                            _transportListData.value = transportData.toMutableList()
                            _transportListData.value = _transportListData.value
                        }else{
                            _transportListData.value!!.addAll(transportData)
                            _transportListData.value = _transportListData.value
                        }
                    }else{
                        _transportLoadMore.value = false
                    }
                }
            }else{
                if(error == null){
                    authFail()
                }else{
                    whenFail(error)
                }
            }
        }
    }

    //send pageNumber and get the response (Trips Data)
    private suspend fun getTrips() {
        tripPageNumber++
        TripRequests.getTrips(tripPageNumber,app.resources,app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY),app.resources.getString(R.string.EMPTY),app.resources.getString(R.string.EMPTY),app.resources.getString(R.string._1)){ data, error, success ->
            if(success){
                if (data != null) {
                    val tripData = data.data.trips.data
                    if(tripData.isNotEmpty()){
                        _tripLoadMore.value = true
                        if(_tripListData.value.isNullOrEmpty()){
                            _tripListData.value = tripData.toMutableList()
                            _tripListData.value = _tripListData.value
                        }else{
                            _tripListData.value!!.addAll(tripData)
                            _tripListData.value = _tripListData.value

                        }
                    }else{
                        _tripLoadMore.value = false
                    }
                }
            }else{
                if(error == null){
                    authFail()
                }else{
                    whenFail(error)
                }
            }
        }
    }

    private suspend fun getFullTrips() {
        packagePageNumber++
        FullTripRequests.getFullPackage(packagePageNumber,app.resources,app.resources.getString(R.string.EMPTY),app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY), app.resources.getString(R.string.EMPTY),app.resources.getString(R.string._1)){ data, error, success ->
            if(success){
                if (data != null) {
                    val tripData = data.data.Packages.data
                    if (tripData.isNotEmpty()) {
                        _fullTripLoadMore.value = true
                        if (_packageListData.value.isNullOrEmpty()) {
                            _packageListData.value = tripData.toMutableList()
                            _packageListData.value =_packageListData.value
                        } else {
                            _packageListData.value!!.addAll(tripData)
                            _packageListData.value = _packageListData.value
                        }
                    } else {
                        _fullTripLoadMore.value = false
                    }
                }
            }else{
                if(error == null){
                    authFail()
                }else{
                    whenFail(error)
                }
            }
        }
    }


    // UPDATE USER TOKEN IN THE SERVER
    private suspend fun updateToken(token: String) {
        UpdateToken.updateToken(token,app.resources){error, success ->
            if(success){
                stopLoading()
            }else{
                if(error.isNullOrEmpty()){authFail()}else{whenFail(error)}
            }
        }
    }
    // CALL TO UPDATE TOKEN
    private fun callUpdateToken(token: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateToken(token)
        }catch (e:Exception){
            handleException(e)
        }
        }
    }

    // GET CITIES
    private suspend fun getCities(){
        CityRequests.getCityDataList(app.resources){ data, error, success ->
            if(success){
                _citiesListData.value = data
                if (userData != null ){stopLoading()}
            }else{
                whenFail(error!!)
            }
        }
    }
    // GET Levels
    private suspend fun getLevels(){
        LevelRequests.getLevelDataList(app.resources){ data, error, success ->
            if(success){
                _levelsListData.value = data
            }else{
                whenFail(error!!)
            }
        }
    }
    // GET Meals
    private suspend fun getMeals(){
        MealsRequests.getMealDataList(app.resources){ data, error, success ->
            if(success){
                _mealsListData.value = data
            }else{
                whenFail(error!!)
            }
        }
    }
    // GET Types
    private suspend fun getTypes(){
        TypeRequests.getTypeDataList(app.resources){ data, error, success ->
            if(success){
                _typesListData.value = data
            }else{
                whenFail(error!!)
            }
        }
    }

    private suspend fun getLocations(){
        LocationRequests.getLocationDataList(app.resources){ data, error, success ->
            if(success){
                _locationsListData.value = data
            }else{
                whenFail(error!!)
            }
        }
    }

    private suspend fun getHotelList(){
        HotelNamesRequests.getHotelNamesDataList(app.resources){ data, error, success ->
            if(success){
                _hotelNamesListData.value = data
            }else{
                whenFail(error!!)
            }
        }
    }

     fun getCollections():Collection?{
        if(_citiesListData.value != null && _levelsListData.value != null &&_mealsListData.value != null &&_typesListData.value != null &&_locationsListData.value != null && _hotelNamesListData.value != null ){
          return   Collection(_citiesListData.value!!, _levelsListData.value!!, _mealsListData.value!!, _typesListData.value!!, _locationsListData.value!!,_hotelNamesListData.value!!)
        }else{
            whenFail(app.resources.getString(R.string.WRONG))
        }
        return  null
    }

    // if the userData is not exist load it.
    private suspend  fun checkIfUserDataExist() {
        UserRequests.getUserData(app.resources){ data, error, success ->
            if(success){
                _data.value = data
                if(_data.value?.data?.user?.device_token != _token){
                    callUpdateToken(DeviceToken.getDeviceToken()!!)
                }else{
                    stopLoading()
                }
            }else{
                if(error.isNullOrEmpty()){authFail()}else{whenFail(error)}
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
        if(_noAuth.value != true){
            stopLoading()
            Token.removeToken()
            _noAuth.value = true
        }
    }

      fun resetData(){
        _data.value = null
    }

}