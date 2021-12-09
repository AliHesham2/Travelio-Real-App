package com.example.bezo.view.user.change

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.R
import com.example.bezo.model.data.UserData
import com.example.bezo.model.data.UserUpdateData
import com.example.bezo.model.preference.Token
import com.example.bezo.requests.user.UserRequests
import com.example.bezo.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ChangePasswordViewModel(private val app: Application,private val userData: UserData):AndroidViewModel(app) {

    private var _newData = MutableLiveData<UserData>()
    val newData : LiveData<UserData>
        get() = _newData

    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?>
        get() = _loading

    private val _noAuth = MutableLiveData<Boolean?>()
    val noAuth: LiveData<Boolean?>
        get() = _noAuth


    private val _error = MutableLiveData<String?>()
    val error : LiveData<String?>
        get() = _error



    fun getData(password: String) {
        val sendData = UserUpdateData(userData.name,userData.city_id,userData.email, userData.phone,password,app.resources.getString(R.string.PATCH))
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
            }catch (e: Exception){
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