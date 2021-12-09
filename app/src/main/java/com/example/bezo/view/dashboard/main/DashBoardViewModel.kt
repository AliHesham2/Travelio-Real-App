package com.example.bezo.view.dashboard.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.R
import com.example.bezo.model.data.*
import com.example.bezo.model.data.Collection
import com.example.bezo.model.preference.Token
import com.example.bezo.requests.static_list.*
import com.example.bezo.requests.user.UserRequests
import com.example.bezo.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class DashBoardViewModel(private val app: Application,private val  userData: UserData?):AndroidViewModel(app) {

    private var _data = MutableLiveData<Users?>()
    val data : LiveData<Users?>
        get() = _data

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
                }
                launch { getCities() }
                launch { getLevels() }
                launch { getMeals()  }
                launch { getTypes()  }
                launch { getLocations() }
                launch { getHotelList() }
            }catch (e:Exception){
                Log.i("Error11","Error")
                handleException(e)
            }
        }
    }

    private suspend fun getCities(){
        CityRequests.getCityDataList(app.resources){ data, error, success ->
            if(success){
                _citiesListData.value = data
            }else{
                whenFail(error!!)
            }
            stopLoading()
        }
    }

    private suspend fun getLevels(){
        LevelRequests.getLevelDataList(app.resources){ data, error, success ->
            if(success){
                _levelsListData.value = data
            }else{
                whenFail(error!!)
            }
            stopLoading()
        }
    }

    private suspend fun getMeals(){
        MealsRequests.getMealDataList(app.resources){ data, error, success ->
            if(success){
                _mealsListData.value = data
            }else{
                whenFail(error!!)
            }
            stopLoading()
        }
    }

    private suspend fun getTypes(){
        TypeRequests.getTypeDataList(app.resources){ data, error, success ->
            if(success){
                _typesListData.value = data
            }else{
                whenFail(error!!)
            }
            stopLoading()
        }
    }
    private suspend fun getLocations(){
        LocationRequests.getLocationDataList(app.resources){ data, error, success ->
            if(success){
                _locationsListData.value = data
            }else{
                whenFail(error!!)
            }
            stopLoading()
        }
    }
    private suspend fun getHotelList(){
        HotelNamesRequests.getHotelNamesDataList(app.resources){ data, error, success ->
            if(success){
                _hotelNamesListData.value = data
            }else{
                whenFail(error!!)
            }
            stopLoading()
        }
    }

     fun getCollections():Collection?{
        if(_citiesListData.value != null && _levelsListData.value != null &&_mealsListData.value != null &&_typesListData.value != null &&_locationsListData.value != null && _hotelNamesListData.value != null ){
          return   Collection(_citiesListData.value!!, _levelsListData.value!!, _mealsListData.value!!, _typesListData.value!!, _locationsListData.value!!,_hotelNamesListData.value!!)
        }else{
            Log.i("Error","Error")
            whenFail(app.resources.getString(R.string.WRONG))
        }
        return  null
    }

    // if the userData is not exist load it.
    private suspend  fun checkIfUserDataExist() {
        UserRequests.getUserData(app.resources){ data, error, success ->
            if(success){
                _data.value = data
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
        stopLoading()
        Token.removeToken()
        _noAuth.value = true
    }

      fun resetData(){
        _data.value = null
    }
}