package com.example.travelio.model.service

import com.example.travelio.model.data.FcmModel
import com.example.travelio.view.util.PopUpMsg
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
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
        .baseUrl(PopUpMsg.BASE_URL_FCM)
        .build()

interface FcmAuth {
    @Headers("Content-Type:application/json", "Authorization:key=${PopUpMsg.KEY}")
    @POST("fcm/send")
    suspend fun sendNotification(@Body requestBody: FcmModel): Response<ResponseBody>
}

object FcmApi {
    val fcm : FcmAuth by lazy { retrofit.create(FcmAuth::class.java) }
}
