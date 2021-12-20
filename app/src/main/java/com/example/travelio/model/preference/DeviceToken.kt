package com.example.travelio.model.preference

import android.content.Context
import android.content.SharedPreferences
import com.example.travelio.R

object DeviceToken {
    private  lateinit  var preferenceDevice: SharedPreferences
    fun initialize(context: Context){
        preferenceDevice = context.applicationContext.getSharedPreferences(context.resources.getString(R.string.TOKEN_DEVICE_SAVED_AT), Context.MODE_PRIVATE)
    }
    fun saveDeviceToken (token:String){
        preferenceDevice.edit().putString("DeviceToken", token).apply()
    }
    fun getDeviceToken():String?{
        return preferenceDevice.getString("DeviceToken", null)
    }
}