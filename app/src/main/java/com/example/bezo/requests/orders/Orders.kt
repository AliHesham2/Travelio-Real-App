package com.example.bezo.requests.orders

import android.content.res.Resources
import com.example.bezo.R
import com.example.bezo.model.data.*
import com.example.bezo.model.service.AppApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class Orders {
    companion object {

        suspend fun getHotelOrdersData(resources: Resources, isSuccess: (data: HotelMainReserve?, error: String?, success: Boolean) -> Unit) {
            val response = AppApi.appData.getReserveHotels()
            if (response.isSuccessful) {
                val data = response.body()
                withContext(Dispatchers.Main) {
                    isSuccess(data, null, true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(null, null, false)
                    } else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg =
                                JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(null, errorMsg, false)
                        }
                    }
                }
            }
        }

        suspend fun getTransportsOrdersData(validity: Int, resources: Resources, isSuccess: (data: TransportMainReserve?, error: String?, success: Boolean) -> Unit) {
            val response = AppApi.appData.getReserveTransportations(validity)
            if (response.isSuccessful) {
                val data = response.body()
                withContext(Dispatchers.Main) {
                    isSuccess(data, null, true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(null, null, false)
                    } else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg = JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(null, errorMsg, false)
                        }
                    }
                }
            }
        }

        suspend fun getTripsOrdersData(validity: Int, resources: Resources, isSuccess: (data: TripMainReserve?, error: String?, success: Boolean) -> Unit) {
            val response = AppApi.appData.getReserveTrips(validity)
            if (response.isSuccessful) {
                val data = response.body()
                withContext(Dispatchers.Main) {
                    isSuccess(data, null, true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(null, null, false)
                    } else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg =
                                JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(null, errorMsg, false)
                        }
                    }
                }
            }
        }

        suspend fun getFullTripOrdersData(validity: Int, resources: Resources, isSuccess: (data: FullTripMainReserve?, error: String?, success: Boolean) -> Unit) {
            val response = AppApi.appData.getReserveFullTrips(validity)
            if (response.isSuccessful) {
                val data = response.body()
                withContext(Dispatchers.Main) {
                    isSuccess(data, null, true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    if (response.code() == 401) {
                        isSuccess(null, null, false)
                    } else {
                        val error = response.errorBody()?.charStream()?.readText()
                        if (error != null) {
                            val errorMsg =
                                JSONObject(error).getString(resources.getString(R.string.MESSAGE))
                            isSuccess(null, errorMsg, false)
                        }
                    }
                }
            }
        }
    }
}