package com.example.travelio.requests.user

import android.content.res.Resources
import com.example.travelio.R
import com.example.travelio.model.data.UserUpdateData
import com.example.travelio.model.data.Users
import com.example.travelio.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class UserRequests {
    companion object {
        suspend fun getUserData(resources: Resources, isSuccess: (data: Users?, error: String?, success: Boolean) -> Unit) {
            val response = AppApi.appData.getUserData()
            if (response.isSuccessful) {
                val data = response.body()
                withContext(Dispatchers.Main) {
                    isSuccess(data, null, true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(null, null, false)
                    } else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg =
                                JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(null, errorMsg, false)
                        }
                    }
                }
            }
        }

        suspend fun updateUserData(updateData: UserUpdateData, resources: Resources, isSuccess: (data: Users?, error: String?, success: Boolean) -> Unit){
            val response = AppApi.appData.updateUserData(updateData)
            if (response.isSuccessful) {
                val data = response.body()
                withContext(Dispatchers.Main) {
                    isSuccess(data, null, true)
                }
            }else {
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(null, null, false)
                    } else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(null, errorMsg, false)
                        }
                    }
                }
            }
        }


    }
}