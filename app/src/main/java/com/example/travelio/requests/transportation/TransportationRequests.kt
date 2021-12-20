package com.example.travelio.requests.transportation

import android.content.res.Resources
import com.example.travelio.R
import com.example.travelio.model.data.Transportations
import com.example.travelio.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class TransportationRequests {
    companion object{
        suspend fun getTransportations(pageNumber: Int, resources: Resources, city_from_id:String, city_to_id:String, level_id:String, type_id:String, minPrice:String, maxPrice:String , fromDate:String, toDate:String,homePage:String, isSuccess: (data: Transportations?, error: String?, success: Boolean) -> Unit) {
            val response = AppApi.appData.getTransportations(5, pageNumber,   city_from_id, city_to_id, level_id, type_id, minPrice, maxPrice , fromDate, toDate,homePage)
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