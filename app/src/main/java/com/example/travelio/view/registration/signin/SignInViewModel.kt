package com.example.travelio.view.registration.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelio.model.data.UserData
import com.example.travelio.model.data.UserLoginData
import com.example.travelio.requests.registration.Registration
import com.example.travelio.view.util.PopUpMsg
import kotlinx.coroutines.*


class SignInViewModel(private val app:Application) : AndroidViewModel(app) {

    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?>
        get() = _loading

    private val _isSuccess = MutableLiveData<UserData>()
    val isSuccess : LiveData<UserData>
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
        Registration.signInSendRequest(requestBody, app.resources) { data,error, success ->
            if (success) {
                _loading.value = false
                _isSuccess.value = data
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