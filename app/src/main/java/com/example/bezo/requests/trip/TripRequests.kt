package com.example.bezo.requests.trip

import android.content.res.Resources
import com.example.bezo.R
import com.example.bezo.model.data.Trips
import com.example.bezo.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class TripRequests {
    companion object{
        suspend fun getTrips(pageNumber: Int, resources: Resources, isSuccess: (data: Trips?, error: String?, success: Boolean) -> Unit) {
            val response = AppApi.appData.getTrips(5, pageNumber)
            if (response.isSuccessful) {
                val data = response.body()
                withContext(Dispatchers.Main){
                    isSuccess(data, null, true)
                }
            }else {
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(null, null, false) }
                    else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(null, errorMsg, false) } }
                }
            }
        }
    }

}