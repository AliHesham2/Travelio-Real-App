package com.example.bezo.requests.static_list

import android.content.res.Resources
import com.example.bezo.R
import com.example.bezo.model.data.HotelNames
import com.example.bezo.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class HotelNamesRequests {
    companion object{
        suspend fun getHotelNamesDataList(resources: Resources, isSuccess: (data: HotelNames?, error: String?, success: Boolean) -> Unit){
            val response = AppApi.appData.getHotelList()
            if (response.isSuccessful) {
                val data = response.body()
                withContext(Dispatchers.Main) {
                    isSuccess(data, null, true) } }
            else {
                withContext(Dispatchers.Main) {
                    val error = response.errorBody()?.charStream()?.readText()
                    if (error != null) {
                        val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                        isSuccess(null, errorMsg, false) }
                }
            }
        }
    }
}