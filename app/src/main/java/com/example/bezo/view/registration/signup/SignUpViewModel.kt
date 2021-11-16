package com.example.bezo.view.registration.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bezo.R
import com.example.bezo.model.data.UserSignUpData
import com.example.bezo.model.preference.Token
import com.example.bezo.model.service.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

class SignUpViewModel (private val app:Application):AndroidViewModel(app) {

    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?>
        get() = _loading


    private val _error = MutableLiveData<String?>()
    val error : LiveData<String?>
        get() = _error

    private val _isSuccess = MutableLiveData<Boolean?>()
    val isSuccess : LiveData<Boolean?>
        get() = _isSuccess

    fun getData(requestBody: UserSignUpData) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendRequest(requestBody)
            }catch (t: Exception){
                if (t is IOException) {
                    whenFail(app.resources.getString(R.string.NO_INTERNET))
                } else {
                    whenFail(app.resources.getString(R.string.WRONG))
                }
            }
        }
    }

    private suspend fun sendRequest(requestBody: UserSignUpData) {
        val result = UserApi.user.signUp(requestBody)
        if(result.isSuccessful){
            val bodyResponse = result.body()?.charStream()?.readText()
            val token = JSONObject(bodyResponse!!).getJSONObject(app.resources.getString(R.string.DATA)).getString(app.resources.getString(R.string.TOKEN))
            whenSuccess(token)
        }else{
            val error = result.errorBody()?.charStream()?.readText()
            if (error != null) {
                whenFail(JSONObject(error).getString(app.resources.getString(R.string.MESSAGE)))
            }
        }
    }

    private suspend fun whenSuccess(token: String) {
        withContext(Dispatchers.Main){
            Token.saveToken(token)
            _loading.value = false
            _isSuccess.value = true
        }
    }

    private suspend fun whenFail(msg:String){
        withContext(Dispatchers.Main){
            _loading.value = false
            _error.value = msg
        }
    }

    override fun onCleared() {
        super.onCleared()
        _error.value = null
        _loading.value = null
        _isSuccess.value = null
    }
}