package com.example.travelio.requests.trip

import android.content.res.Resources
import android.util.Log
import com.example.travelio.R
import com.example.travelio.model.data.Trips
import com.example.travelio.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class TripRequests {
    companion object{
        suspend fun getTrips(pageNumber: Int, resources: Resources,  city_id:String, location_id:String, minPrice:String, maxPrice:String, fromDate:String, toDate:String, isSuccess: (data: Trips?, error: String?, success: Boolean) -> Unit) {
            Log.i("Data::Trip","city_id:$city_id  location_id:$location_id  minPrice:$minPrice maxPrice:$maxPrice  fromDate:$fromDate   toDate:$toDate")
            val response = AppApi.appData.getTrips(5, pageNumber,city_id, location_id, minPrice, maxPrice,fromDate,toDate)
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