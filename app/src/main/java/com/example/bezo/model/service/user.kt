package com.example.bezo.model.service

import com.example.bezo.model.data.UserLoginData
import com.example.bezo.model.data.UserSignUpData
import com.example.bezo.model.data.Users
import com.example.bezo.view.util.PopUpMsg
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private val  client_timeout = OkHttpClient.Builder()
    .connectTimeout(20, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS )
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit =
    Retrofit.Builder()
        .client(client_timeout)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(PopUpMsg.BASE_URL)
        .build()

interface UserAuth {
    @POST("/api/userRegister")
    suspend fun signUp(@Body requestBody: UserSignUpData): Response<Users>

    @POST("/api/userLogin")
    suspend fun signIn(@Body requestBody: UserLoginData): Response<Users>
}

object UserApi {
    val user : UserAuth by lazy { retrofit.create(UserAuth::class.java) }
}
