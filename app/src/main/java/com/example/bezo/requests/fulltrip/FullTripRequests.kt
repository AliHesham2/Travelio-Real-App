package com.example.bezo.requests.fulltrip

import android.content.res.Resources
import android.util.Log
import com.example.bezo.R
import com.example.bezo.model.data.FullTrips
import com.example.bezo.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class FullTripRequests {
    companion object{
        suspend fun getFullPackage(pageNumber: Int, resources: Resources,  hotels_list_id:String,hotels_list_city_id:String, minPrice:String, maxPrice:String, fromDate:String, toDate:String,isSuccess: (data: FullTrips?, error: String?, success: Boolean) -> Unit) {
            Log.i("Data::FullTrip","hotels_list_id:$hotels_list_id  hotels_list_city_id:$hotels_list_city_id  minPrice:$minPrice maxPrice:$maxPrice  fromDate:$fromDate   toDate:$toDate")
            val response = AppApi.appData.getFullTrips(5, pageNumber, hotels_list_id,hotels_list_city_id, minPrice, maxPrice, fromDate, toDate)
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