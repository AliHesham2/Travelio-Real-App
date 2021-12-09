package com.example.bezo.view.user.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.R
import com.example.bezo.model.data.CitesData
import com.example.bezo.model.data.Collection
import com.example.bezo.model.data.UserData
import com.example.bezo.model.data.UserUpdateData
import com.example.bezo.model.preference.Token
import com.example.bezo.requests.user.UserRequests
import com.example.bezo.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class EditProfileViewModel(private val app: Application, private val userData: UserData, private val collection: Collection
):AndroidViewModel(app) {

    private var _data = MutableLiveData<UserData>()
    val data : LiveData<UserData>
        get() = _data

    private var _newData = MutableLiveData<UserData>()
    val newData : LiveData<UserData>
        get() = _newData

    private val _citiesList = MutableLiveData<List<String>?>()
    val citiesList : LiveData<List<String>?>
        get() = _citiesList

    private val _citiesListData = MutableLiveData<List<CitesData>?>()

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
        citiesList.value.isNullOrEmpty()
        displayUserData()
        getCitiesList()
    }

    private fun displayUserData() {
        _data.value = userData
    }

    fun getData(name: String, email: String, phone: String, city: String) {
        val sendData = UserUpdateData(name,getID(city)?:0,email,phone,null,app.resources.getString(R.string.PATCH))
        viewModelScope.launch(Dispatchers.IO) {
            loading()
            try {
                UserRequests.updateUserData(sendData,app.resources){ data, error, success ->
                    if(success){
                        stopLoading()
                        _newData.value = data!!.data.user
                    }else{
                        if(error.isNullOrEmpty()){authFail()}else{whenFail(error)}
                    }
                }
            }catch (e:Exception){
                handleException(e)
            }
        }
    }
    private fun getID(name:String): Int? {
        val wantedData = _citiesListData.value?.find { "${it.name} (${it.country.iso3})" == name }
        if(wantedData != null){
            return wantedData.id
        }
        return null
    }

    private fun getCitiesList() {
        val x = mutableListOf<String>()
        _citiesListData.value = collection.city.data.citiesList
        _citiesListData.value!!.map {
            x.add("${it.name} (${it.country.iso3})")
        }
        _citiesList.value = x
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