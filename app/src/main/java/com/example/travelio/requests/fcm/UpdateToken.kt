package com.example.travelio.requests.fcm

import android.content.res.Resources
import android.util.Log
import com.example.travelio.R
import com.example.travelio.model.data.FcmModel
import com.example.travelio.model.data.SendNotificationPostRequest
import com.example.travelio.model.data.UpdateToken
import com.example.travelio.model.preference.DeviceToken
import com.example.travelio.model.service.AppApi
import com.example.travelio.model.service.FcmApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


object UpdateToken {
    fun updateDeviceToken(token: String) {
        DeviceToken.saveDeviceToken(token)
    }

    suspend fun updateToken(token: String, resources: Resources, isSuccess: (error: String?, success: Boolean) -> Unit) {
        val response = AppApi.appData.updateDeviceToken(UpdateToken(token))
        if (response.isSuccessful) {
            Log.i("updated", "updatedSuccess ")
            withContext(Dispatchers.Main) { isSuccess(null, true) }
        } else {
            withContext(Dispatchers.Main) {
                if (response.code() == 401) {
                    isSuccess(null, false)
                } else {
                    val error = response.errorBody()?.charStream()?.readText()
                    if (error != null) {
                        val errorMsg =
                            JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                        isSuccess(errorMsg, false)
                    }
                }
            }
        }
    }

    suspend fun sendNotificationToFCM(resources: Resources, data: FcmModel, isSuccess: (error: String?, success: Boolean) -> Unit) {
        val response = FcmApi.fcm.sendNotification(data)
        if (response.isSuccessful) {
            withContext(Dispatchers.Main) { isSuccess(null, true) }
        } else {
            withContext(Dispatchers.Main) {
                if (response.code() == 401) {
                    isSuccess(null, false)
                } else {
                    val error = response.errorBody()?.charStream()?.readText()
                    if (error != null) {
                        val errorMsg =
                            JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                        isSuccess(errorMsg, false)
                    }
                }
            }
        }
    }

    suspend fun sendNotificationToServer(resources: Resources, data: SendNotificationPostRequest, isSuccess: (error: String?, success: Boolean) -> Unit) {
        val response = AppApi.appData.sendNotification(data)
        if (response.isSuccessful) {
            withContext(Dispatchers.Main) { isSuccess(null, true) }
        } else {
            withContext(Dispatchers.Main) {
                if (response.code() == 401) {
                    isSuccess(null, false)
                } else {
                    val error = response.errorBody()?.charStream()?.readText()
                    if (error != null) {
                        val errorMsg =
                            JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                        isSuccess(errorMsg, false)
                    }
                }

            }
        }
    }
}