package com.example.travelio.requests.orders

import android.content.res.Resources
import com.example.travelio.R
import com.example.travelio.model.data.DeleteData
import com.example.travelio.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DeleteOrders {
    companion object{

        suspend fun deleteHotelOrder(id: Int, resources: Resources,isSuccess:(done:Boolean,error:String?)->Unit){
            val response = AppApi.appData.deleteReserveHotels(DeleteData(id))
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    isSuccess(true,null)
                }
            }else{
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(false,null)
                    } else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg =
                                JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(false, errorMsg)
                        }
                    }
                }
            }
        }

        suspend fun deleteTransportOrder(id: Int, resources: Resources,isSuccess:(done:Boolean,error:String?)->Unit){
            val response = AppApi.appData.deleteReserveTransportations(DeleteData(id))
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    isSuccess(true,null)
                }
            }else{
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(false,null)
                    } else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg =
                                JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(false, errorMsg)
                        }
                    }
                }
            }
        }

        suspend fun deleteTripOrder(id: Int, resources: Resources,isSuccess:(done:Boolean,error:String?)->Unit){
            val response = AppApi.appData.deleteReserveTrips(DeleteData(id))
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    isSuccess(true,null)
                }
            }else{
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(false,null)
                    } else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg =
                                JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(false, errorMsg)
                        }
                    }
                }
            }
        }

        suspend fun deleteFullTripOrder(id: Int, resources: Resources,isSuccess:(done:Boolean,error:String?)->Unit){
            val response = AppApi.appData.deleteReserveFullTrips(DeleteData(id))
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    isSuccess(true,null)
                }
            }else{
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(false,null)
                    } else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg =
                                JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(false, errorMsg)
                        }
                    }
                }
            }
        }
    }
}