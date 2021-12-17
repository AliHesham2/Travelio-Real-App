package com.example.travelio.requests.booking

import android.content.res.Resources
import com.example.travelio.R
import com.example.travelio.model.data.FullTripBookingData
import com.example.travelio.model.data.HotelBookingData
import com.example.travelio.model.data.TransportsBookingData
import com.example.travelio.model.data.TripsBookingData
import com.example.travelio.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Booking{
    companion object{

        suspend fun hotelBooking(hotelBookingData: HotelBookingData, resources: Resources,isSuccess:(success:Boolean,error:String?) ->Unit) {
            val response = AppApi.appData.hotelBooking(hotelBookingData)
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    isSuccess(true,null)
                }
            }else {
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(false,null) }
                    else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(false,errorMsg) } }
                }
            }
        }

        suspend fun tripBooking(tripsBookingData: TripsBookingData, resources: Resources,isSuccess:(success:Boolean,error:String?) ->Unit) {
            val response = AppApi.appData.tripBooking(tripsBookingData)
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    isSuccess(true,null)
                }
            }else {
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(false,null) }
                    else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(false,errorMsg) } }
                }
            }
        }

        suspend fun transportsBooking(transportsBookingData: TransportsBookingData, resources: Resources,isSuccess:(success:Boolean,error:String?) ->Unit) {
            val response = AppApi.appData.transportsBooking(transportsBookingData)
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    isSuccess(true,null)
                }
            }else {
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(false,null) }
                    else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(false,errorMsg) } }
                }
            }
        }

        suspend fun fullTripBooking(fullTripBookingData: FullTripBookingData, resources: Resources,isSuccess:(success:Boolean,error:String?) ->Unit) {
            val response = AppApi.appData.fullTripBooking(fullTripBookingData)
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    isSuccess(true,null)
                }
            }else {
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(false,null) }
                    else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(false,errorMsg) } }
                }
            }
        }
    }
}