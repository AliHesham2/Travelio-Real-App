package com.example.travelio.model.preference

import android.content.Context
import android.content.SharedPreferences
import com.example.travelio.R
import java.util.*

object Local {
    private  lateinit  var preferenceLocal: SharedPreferences
    fun initialize(context: Context){
        preferenceLocal = context.applicationContext.getSharedPreferences(context.resources.getString(R.string.Language_SAVED_AT), Context.MODE_PRIVATE)
    }
    fun getDeviceLanguage(): String? {
        return Locale.getDefault().language
    }
    fun saveLanguage (lang:String){
        preferenceLocal.edit().putString("Lang", lang).apply()
    }
    fun getLanguage():String?{
        return preferenceLocal.getString("Lang", null)
    }
}