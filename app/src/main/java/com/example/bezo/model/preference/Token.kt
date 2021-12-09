package com.example.bezo.model.preference

import android.content.Context
import android.content.SharedPreferences
import com.example.bezo.R


object Token{
    private  lateinit  var preference: SharedPreferences
    fun initialize(context: Context){
        preference = context.applicationContext.getSharedPreferences(context.resources.getString(R.string.TOKEN_SAVED_AT), Context.MODE_PRIVATE)
    }
    fun saveToken (token:String){
         preference.edit().putString("Token", token).apply()
    }
    fun getToken():String?{
        return preference.getString("Token", null)
    }
    fun removeToken(){
        preference.edit().clear().apply()
    }

}