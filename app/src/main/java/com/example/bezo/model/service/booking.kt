package com.example.bezo.model.service

import com.example.bezo.model.data.*
import com.example.bezo.model.preference.Token
import com.example.bezo.view.util.PopUpMsg
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody

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
    @GET("/api/user/hotels/getAll")
    suspend fun getHotels(@Query("perPage") page:Int,
                          @Query("page") pageNumber:Int,
                          @Query("hotels_list_id") hotelID:String,
                          @Query("hotels_list_city_id") hotels_list_city_id:String,
                          @Query("meal_id") mealID:String,
                          @Query("stars") stars:String,
                          @Query("perRoom") perRoom:String,
                          @Query("minPrice") minPrice:String ,
                          @Query("maxPrice") maxPrice:String): Response<Hotels>

    @GET("/api/user/trips/getAll")
    suspend fun getTrips(@Query("perPage") page:Int,
                         @Query("page") pageNumber:Int,
                         @Query("city_id") city_id:String,
                         @Query("location_id") location_id:String,
                         @Query("minPrice") minPrice:String,
                         @Query("maxPrice") maxPrice:String,
                         @Query("fromDate") fromDate:String,
                         @Query("toDate") toDate:String ): Response<Trips>

    @GET("/api/user/transports/getAll")
    suspend fun getTransportations(@Query("perPage") page:Int,
                                   @Query("page") pageNumber:Int,
                                   @Query("city_from_id") city_from_id:String,
                                   @Query("city_to_id") city_to_id:String,
                                   @Query("level_id") level_id:String,
                                   @Query("type_id") type_id:String,
                                   @Query("minPrice") minPrice:String,
                                   @Query("maxPrice") maxPrice:String ,
                                   @Query("fromDate") fromDate:String,
                                   @Query("toDate") toDate:String): Response<Transportations>


    @GET("/api/user/packages/getAll")
    suspend fun getFullTrips(@Query("perPage") page:Int,
                             @Query("page") pageNumber:Int,
                             @Query("hotels_list_id") hotels_list_id:String,
                             @Query("hotels_list_city_id") hotels_list_city_id:String,
                             @Query("minPrice") minPrice:String,
                             @Query("maxPrice") maxPrice:String,
                             @Query("fromDate") fromDate:String,
                             @Query("toDate") toDate:String): Response<FullTrips>


    @POST("/api/user/hotels/booking")
    suspend fun hotelBooking(@Body requestBody: HotelBookingData): Response<ResponseBody>

    @POST("/api/user/trips/booking")
    suspend fun tripBooking(@Body requestBody: TripsBookingData): Response<ResponseBody>

    @POST("/api/user/transports/booking")
    suspend fun transportsBooking(@Body requestBody: TransportsBookingData): Response<ResponseBody>

    @POST("/api/user/packages/booking")
    suspend fun fullTripBooking(@Body requestBody: FullTripBookingData): Response<ResponseBody>

    @GET("/api/user/hotels/reservationsHistory")
    suspend fun getReserveHotels (): Response<HotelMainReserve>

    @GET("/api/user/trips/reservationsHistory")
    suspend fun getReserveTrips(@Query("valid") valid:Int): Response<TripMainReserve>

    @GET("/api/user/transports/reservationsHistory")
    suspend fun getReserveTransportations(@Query("valid") valid:Int): Response<TransportMainReserve>

    @GET("/api/user/packages/reservationsHistory")
    suspend fun getReserveFullTrips(@Query("valid") valid:Int): Response<FullTripMainReserve>

    @HTTP(method = "DELETE", path = "/api/user/hotels/reservationsDelete", hasBody = true)
    suspend fun deleteReserveHotels (@Body requestBody: DeleteData): Response<ResponseBody>

    @HTTP(method = "DELETE", path = "/api/user/trips/reservationsDelete", hasBody = true)
    suspend fun deleteReserveTrips(@Body requestBody: DeleteData): Response<ResponseBody>

    @HTTP(method = "DELETE", path = "/api/user/transports/reservationsDelete", hasBody = true)
    suspend fun deleteReserveTransportations(@Body requestBody: DeleteData): Response<ResponseBody>

    @HTTP(method = "DELETE", path = "/api/user/packages/reservationsDelete", hasBody = true)
    suspend fun deleteReserveFullTrips(@Body requestBody: DeleteData): Response<ResponseBody>

    @GET("/api/user/profile")
    suspend fun getUserData() : Response<Users>

    @POST("/api/user/editProfile")
    suspend fun updateUserData(@Body requestBody: UserUpdateData) : Response<Users>
    @GET("/api/user/hotelsList")
    suspend fun getHotelList(@Query("perPage") page:Int = 0,
                              @Query("page") pageNumber:Int = 0, @Query("getAll") boolean: Boolean = true): Response<HotelNames>
}

object AppApi {
    val appData : AppAuth by lazy { retrofit_app.create(AppAuth::class.java) }
}
