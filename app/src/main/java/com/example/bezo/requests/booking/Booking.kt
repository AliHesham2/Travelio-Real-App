package com.example.bezo.requests.booking

import android.content.res.Resources
import com.example.bezo.R
import com.example.bezo.model.data.FullTripBookingData
import com.example.bezo.model.data.HotelBookingData
import com.example.bezo.model.data.TransportsBookingData
import com.example.bezo.model.data.TripsBookingData
import com.example.bezo.model.service.AppApi
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