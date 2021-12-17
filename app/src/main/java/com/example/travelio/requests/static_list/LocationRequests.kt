package com.example.travelio.requests.static_list

import android.content.res.Resources
import com.example.travelio.R
import com.example.travelio.model.data.Locations
import com.example.travelio.model.service.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LocationRequests {
    companion object{
        suspend fun getLocationDataList(resources: Resources, isSuccess: (data: Locations?, error: String?, success: Boolean) -> Unit){
            val response = UserApi.user.getLocationsList()
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