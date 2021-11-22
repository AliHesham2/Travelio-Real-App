package com.example.bezo.requests.registration

import android.content.res.Resources
import com.example.bezo.R
import com.example.bezo.model.data.UserLoginData
import com.example.bezo.model.data.UserSignUpData
import com.example.bezo.model.preference.Token
import com.example.bezo.model.service.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Registration {
    companion object {

        //Send User Data to create account and  get response
      suspend  fun signUpSendRequest(requestBody: UserSignUpData, resources: Resources,isSuccess:(error:String?,success:Boolean) ->Unit) {
            val result = UserApi.user.signUp(requestBody)
            if(result.isSuccessful){
                val bodyResponse = result.body()?.charStream()?.readText()
                val token = JSONObject(bodyResponse!!).getJSONObject(resources.getString(R.string.DATA)).getString(resources.getString(R.string.TOKEN))
                Token.saveToken(token)
                withContext(Dispatchers.Main){
                    isSuccess(null,true)
                }
            }else{
                val error = result.errorBody()?.charStream()?.readText()
                if (error != null) {
                    val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                    withContext(Dispatchers.Main){
                        isSuccess(errorMsg,false)
                    }
                }
            }
        }

        //Send phone and password  to login  and  get response
      suspend fun signInSendRequest(requestBody: UserLoginData,resources: Resources,isSuccess:(error:String?,success:Boolean) ->Unit) {
            val result = UserApi.user.signIn(requestBody)
            if (result.isSuccessful) {
                val bodyResponse = result.body()?.charStream()?.readText()
                val token = JSONObject(bodyResponse!!).getJSONObject(resources.getString(R.string.DATA)).getString(resources.getString(R.string.TOKEN))
                Token.saveToken(token)
                withContext(Dispatchers.Main){
                    isSuccess(null,true)
                }
            } else {
                val error = result.errorBody()?.charStream()?.readText()
                if (error != null) {
                    val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                    withContext(Dispatchers.Main){
                        isSuccess(errorMsg,false)
                    }
                }
            }
        }
    }
}