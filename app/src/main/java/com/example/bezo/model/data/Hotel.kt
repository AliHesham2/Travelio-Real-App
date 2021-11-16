package com.example.bezo.model.data

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
                  val meal:String,
                  val price:String,
                  val hotelsList_id:Int,
                  val company_id:Int,
                  val images:List<Images>,
                  val companies:Companies,
                  val hotels_list:HotelData): Parcelable

@Parcelize
data class Images(val id:Int,val imageUrl:String, val hotel_id:Int):Parcelable

@Parcelize
data class Companies(val id:Int,
                     val name:String,
                     val city:String,
                     val address:String,
                     val email:String,
                     val phone:String,
                     val email_verified_at:Int? ):Parcelable
@Parcelize
data class HotelData(val id:Int,
                     val name:String,
                     val country:String,
                     val city:String,
                     val star:Int):Parcelable