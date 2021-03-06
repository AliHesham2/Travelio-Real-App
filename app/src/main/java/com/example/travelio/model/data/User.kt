package com.example.travelio.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(val data:User): Parcelable

@Parcelize
data class User(val token:String?,val user:UserData): Parcelable

@Parcelize
data class UserData(val id:Int,
                    val name:String,
                    val city_id:Int,
                    val email:String,
                    val phone:String,
                    val device_token:String?,
                    val cities:HotelCitiesData): Parcelable

data class UserUpdateData(val name:String,
                          val city_id:Int,
                          val email:String,
                          val phone:String,
                          val password:String?=null,
                          val _method:String)




