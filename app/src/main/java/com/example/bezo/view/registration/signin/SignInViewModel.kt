package com.example.bezo.view.registration.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.model.data.UserLoginData
import com.example.bezo.requests.registration.Registration
import com.example.bezo.view.util.PopUpMsg
import kotlinx.coroutines.*


class SignInViewModel(private val app:Application) : AndroidViewModel(app) {

    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?>
        get() = _loading

    private val _isSuccess = MutableLiveData<Boolean?>()
    val isSuccess : LiveData<Boolean?>
        get() = _isSuccess

    private val _error = MutableLiveData<String?>()
    val error : LiveData<String?>
        get() = _error

    //Get the user data from ui and call sendRequest fun in background thread
    fun getData(requestBody: UserLoginData){
        _loading.value = true
         viewModelScope.launch(Dispatchers.IO) {
             try {
                 sendRequest(requestBody)
             }catch (t: Exception){
                 handleException(t)
             }
        }
    }

    // fun run in background  to send request and get response
    private suspend fun sendRequest(requestBody: UserLoginData){
        Registration.signInSendRequest(requestBody, app.resources) { error, success ->
            if (success) {
                _loading.value = false
                _isSuccess.value = true
            } else {
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