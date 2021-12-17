package com.example.travelio.view.registration.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelio.model.data.CitesData
import com.example.travelio.model.data.UserData
import com.example.travelio.model.data.UserSignUpData
import com.example.travelio.requests.static_list.CityRequests
import com.example.travelio.requests.registration.Registration
import com.example.travelio.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel (private val app:Application):AndroidViewModel(app) {

    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?>
        get() = _loading

    private val _citiesList = MutableLiveData<List<String>?>()
    val citiesList : LiveData<List<String>?>
        get() = _citiesList

    private val _citiesListData = MutableLiveData<List<CitesData>?>()

    private val _error = MutableLiveData<String?>()
    val error : LiveData<String?>
        get() = _error

    private val _isSuccess = MutableLiveData<UserData>()
    val isSuccess : LiveData<UserData>
        get() = _isSuccess

    init {
        getCitiesList()
    }
    //Get the user data from ui and call sendRequest fun in background thread
    fun getData(requestBody: UserSignUpData) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendRequest(requestBody)
            }catch (t: Exception){
                handleException(t)
            }
        }
    }
    fun getID(name:String): Int? {
        val wantedData = _citiesListData.value?.find { "${it.name} (${it.country.iso3})" == name }
        if(wantedData != null){
            return wantedData.id
        }
        return null
    }

    private fun getCitiesList(){
        val x = mutableListOf<String>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                CityRequests.getCityDataList(app.resources){data, error, success ->
                    if(success && data != null){
                        _citiesListData.value = data.data.citiesList
                        _citiesListData.value!!.map {
                            x.add("${it.name} (${it.country.iso3})")
                        }
                        _citiesList.value = x
                    }else{
                        _error.value = error
                    }
                }
            }catch (t: Exception){
                handleException(t)
            }
        }
    }

    // fun run in background  to send request and get response
    private suspend fun sendRequest(requestBody: UserSignUpData) {
        Registration.signUpSendRequest(requestBody,app.resources){ data,error, success ->
            if (success){
                _loading.value = false
                _isSuccess.value = data
            }else{
                _loading.value = false
                _error.value = error
            }
        }
    }

    private suspend fun handleException(t: Exception) {
        withContext(Dispatchers.Main){
            PopUpMsg.handleError(app.applicationContext,t)
        }
    }
}
