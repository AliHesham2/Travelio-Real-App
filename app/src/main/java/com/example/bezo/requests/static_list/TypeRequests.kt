package com.example.bezo.requests.static_list

import android.content.res.Resources
import com.example.bezo.R
import com.example.bezo.model.data.Types
import com.example.bezo.model.service.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class TypeRequests {
    companion object{
        suspend fun getTypeDataList(resources: Resources, isSuccess: (data: Types?, error: String?, success: Boolean) -> Unit){
            val response = UserApi.user.getTypesList()
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