package com.example.bezo.requests.static_list

import android.content.res.Resources
import com.example.bezo.R
import com.example.bezo.model.data.Cities
import com.example.bezo.model.service.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class CityRequests {
    companion object{
        suspend fun getCityDataList(resources: Resources, isSuccess: (data: Cities?, error: String?, success: Boolean) -> Unit){
            val response = UserApi.user.getCitiesList()
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