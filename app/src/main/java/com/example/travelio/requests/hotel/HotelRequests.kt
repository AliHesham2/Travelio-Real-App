package com.example.travelio.requests.hotel

import android.content.res.Resources
import com.example.travelio.R
import com.example.travelio.model.data.Hotels
import com.example.travelio.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class HotelRequests {
    companion object {
        suspend fun getHotels(pageNumber: Int,resources: Resources,hotelID:String,hotelCityID:String,mealID:String, stars:String,perRoom:String,minPrice:String,maxPrice:String,homePage:String ,isSuccess: (data: Hotels?, error: String?, success: Boolean) -> Unit) {
            val response = AppApi.appData.getHotels(5, pageNumber,hotelID,hotelCityID,mealID,stars,perRoom,minPrice,maxPrice,homePage)
            if (response.isSuccessful) {
                val data = response.body()
                withContext(Dispatchers.Main) {
                    isSuccess(data, null, true) } }
            else {
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