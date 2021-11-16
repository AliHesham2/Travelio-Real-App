package com.example.bezo.model.service

import com.example.bezo.model.data.*
import com.example.bezo.model.preference.Token
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

private val retrofit_app =
    Retrofit.Builder()
        .client(client_timeout)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(PopUpMsg.BASE_URL)
        .client(OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${Token.getToken()}").build()
            chain.proceed(request)
        }.build())
        .build()

interface AppAuth {
    @GET("/api/user/hotels")
    suspend fun getHotels(@Query("perPage") page:Int,
                          @Query("page") pageNumber:Int): Response<Hotels>

    @GET("/api/user/trips")
    suspend fun getTrips(@Query("perPage") page:Int,
                          @Query("page") pageNumber:Int): Response<Trips>

    @GET("/api/user/transports")
    suspend fun getTransportations(@Query("perPage") page:Int,
                                   @Query("page") pageNumber:Int): Response<Transportations>

    @GET("/api/user/packages")
    suspend fun getFullTrips(@Query("perPage") page:Int,
                          @Query("page") pageNumber:Int): Response<FullTrips>

}

object AppApi {
    val appData : AppAuth by lazy { retrofit_app.create(AppAuth::class.java) }
}
