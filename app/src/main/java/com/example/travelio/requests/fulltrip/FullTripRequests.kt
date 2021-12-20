package com.example.travelio.requests.fulltrip

import android.content.res.Resources
import com.example.travelio.R
import com.example.travelio.model.data.FullTrips
import com.example.travelio.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class FullTripRequests {
    companion object{
        suspend fun getFullPackage(pageNumber: Int, resources: Resources,  hotels_list_id:String,hotels_list_city_id:String, minPrice:String, maxPrice:String, fromDate:String, toDate:String , homePage:String,isSuccess: (data: FullTrips?, error: String?, success: Boolean) -> Unit) {
            val response = AppApi.appData.getFullTrips(5, pageNumber, hotels_list_id,hotels_list_city_id, minPrice, maxPrice, fromDate, toDate,homePage)
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