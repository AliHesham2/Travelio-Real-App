package com.example.bezo.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(val data:User): Parcelable

@Parcelize
data class User(val token:String,val user:UserData): Parcelable

@Parcelize
data class UserData(val id:Int,
                val name:String,
                val city:String,
                val email:String,
                val phone:String): Parcelable