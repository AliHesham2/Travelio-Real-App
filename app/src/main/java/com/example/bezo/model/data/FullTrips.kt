package com.example.bezo.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FullTrips(val data:FullTripData): Parcelable

@Parcelize
data class FullTripData(val Package:FullTripModel): Parcelable

@Parcelize
data class FullTripModel(val data:List<FullTrip>): Parcelable

@Parcelize
data class FullTrip(val id:Int,
                 val country:String,
                 val city:String,
                 val price:String,
                 val details:String,
                 val title:String,
                 val date:String,
                 val company_id:Int,
                 val images:List<FullTripImages>,
                 val companies:FullTripCompanies,
                 val hotels_list:List<FullTripHotelData> ): Parcelable

@Parcelize
data class FullTripImages(val id:Int,val imageUrl:String, val package_id:Int): Parcelable

@Parcelize
data class FullTripCompanies(val id:Int,
                         val name:String,
                         val city:String,
                         val address:String,
                         val email:String,
                         val phone:String,
                         val email_verified_at:Int? ): Parcelable

@Parcelize
data class FullTripHotelData(val id:Int,
                     val name:String,
                     val country:String,
                     val city:String,
                     val star:Int,
                     val pivot:SomeData):Parcelable

@Parcelize
data class SomeData(val package_id:Int,
                    val hotelsList_id:Int ):Parcelable