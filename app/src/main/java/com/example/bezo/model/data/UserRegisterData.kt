package com.example.bezo.model.data

data class UserLoginData (val phone:String,val password:String)

data class UserSignUpData(val name:String,
                          val city_id:Int,
                          val email:String,
                          val phone:String,
                          val password:String)