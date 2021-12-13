package com.example.bezo.requests.transportation

import android.content.res.Resources
import android.util.Log
import com.example.bezo.R
import com.example.bezo.model.data.Transportations
import com.example.bezo.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class TransportationRequests {
    companion object{
        suspend fun getTransportations(pageNumber: Int, resources: Resources, city_from_id:String, city_to_id:String, level_id:String, type_id:String, minPrice:String, maxPrice:String , fromDate:String, toDate:String, isSuccess: (data: Transportations?, error: String?, success: Boolean) -> Unit) {
            Log.i("Data::Hotel","city_from_id:$city_from_id  city_to_id:$city_to_id  level_id:$level_id type_id:$type_id  minPrice:$minPrice   maxPrice:$maxPrice   fromDate:$fromDate     toDate:$toDate")
            val response = AppApi.appData.getTransportations(5, pageNumber,   city_from_id, city_to_id, level_id, type_id, minPrice, maxPrice , fromDate, toDate)
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