package com.example.travelio.requests.registration

import android.content.res.Resources
import com.example.travelio.R
import com.example.travelio.model.data.UserData
import com.example.travelio.model.data.UserLoginData
import com.example.travelio.model.data.UserSignUpData
import com.example.travelio.model.preference.Token
import com.example.travelio.model.service.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Registration {
    companion object {

        //Send User Data to create account and  get response
      suspend  fun signUpSendRequest(requestBody: UserSignUpData, resources: Resources,isSuccess:(data:UserData?,error:String?,success:Boolean) ->Unit) {
            val result = UserApi.user.signUp(requestBody)
            if(result.isSuccessful){
                val bodyResponse = result.body()
                Token.saveToken(bodyResponse!!.data.token!!)
                withContext(Dispatchers.Main){
                    isSuccess(bodyResponse.data.user,null,true)
                }
            }else{
                val error = result.errorBody()?.charStream()?.readText()
                if (error != null) {
                    val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                    withContext(Dispatchers.Main){
                        isSuccess(null,errorMsg,false)
                    }
                }
            }
        }

        //Send phone and password  to login  and  get response
      suspend fun signInSendRequest(requestBody: UserLoginData,resources: Resources,isSuccess:(data:UserData?,error:String?,success:Boolean) ->Unit) {
            val result = UserApi.user.signIn(requestBody)
            if (result.isSuccessful) {
                val bodyResponse = result.body()
                Token.saveToken(bodyResponse!!.data.token!!)
                withContext(Dispatchers.Main){
                    isSuccess(bodyResponse.data.user,null,true)
                }
            } else {
                val error = result.errorBody()?.charStream()?.readText()
                if (error != null) {
                    val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                    withContext(Dispatchers.Main){
                        isSuccess(null,errorMsg,false)
                    }
                }
            }
        }
    }
}