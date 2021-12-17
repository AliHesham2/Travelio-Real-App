package com.example.travelio.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

//Hotel_Object
@Parcelize
data class Hotels(val data:HotelsData):Parcelable

@Parcelize
data class HotelsData(val hotels:HotelsModel):Parcelable

@Parcelize
data class HotelsModel(val data:List<Hotel>):Parcelable

//Displayed_data
@Parcelize
data class Hotel (val id:Int,
                  val persons_per_room:String,
                  val meal_id:Int,
                  val price:String,
                  val hotelsList_id:Int,
                  val company_id:Int,
                  val images:List<Images>,
                  val companies:Companies,
                  val hotels_list:HotelData,
                  val meals: MealsData): Parcelable

@Parcelize
data class Images(val id:Int,val imageUrl:String, val hotel_id:Int):Parcelable

@Parcelize
data class Companies(val id:Int,
                     val name:String,
                     val city_id:Int,
                     val address:String,
                     val device_token:String?,
                     val email:String,
                     val phone:String,
                     val email_verified_at:Int? ):Parcelable
@Parcelize
data class HotelData(val id:Int,
                     val name:String,
                     val city_id:Int,
                     val star:Int,
                     val cities:HotelCitiesData,):Parcelable
@Parcelize
data class HotelCitiesData(val id:Int,
                      val name:String,
                      val country_id:Int,
                      val country:HotelCountriesData):Parcelable
@Parcelize
data class HotelCountriesData(val id:Int,val name:String,val iso3:String):Parcelable

@Parcelize
data class MealsData(val id: Int,val name:String):Parcelable

@Parcelize
data class HotelMainReserve(val data:ReserveHotel):Parcelable
@Parcelize
data class ReserveHotel(val Hotels:HotelReserve):Parcelable
@Parcelize
data class HotelReserve(val id:Int,
                     val name:String,
                     val city_id:Int,
                     val email:String,
                     val phone:String,
                     val hotels:List<HotelReserveData>):Parcelable
@Parcelize
data class HotelReserveData(val id:Int,
                            val persons_per_room:String,
                            val meal_id:Int,
                            val price:String,
                            val hotelsList_id:Int,
                            val company_id:Int,
                            val pivot:HotelPivotData,
                            val hotels_list:HotelData,
                            val meals:MealsData,
                            val companies:Companies,
                            val images:List<Images>):Parcelable
@Parcelize
data class HotelPivotData(val user_id:Int,val hotel_id:Int,val quantity:Int,val notes:String?,val status:String,val id:Int):Parcelable
