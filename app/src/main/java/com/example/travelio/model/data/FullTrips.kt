package com.example.travelio.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FullTrips(val data:FullTripData): Parcelable

@Parcelize
data class FullTripData(val Packages:FullTripModel): Parcelable

@Parcelize
data class FullTripModel(val data:List<FullTrip>): Parcelable

@Parcelize
data class FullTrip(val id:Int,
                 val city_id:Int,
                 val date:String,
                 val price:String,
                 val details:String,
                 val title:String,
                 val company_id:Int,
                 val images:List<FullTripImages>,
                 val companies:FullTripCompanies,
                 val hotels_list:List<FullTripHotelData>,
                 val cities:FullTripCitiesData,): Parcelable

@Parcelize
data class FullTripImages(val id:Int,val imageUrl:String, val package_id:Int): Parcelable

@Parcelize
data class FullTripCompanies(val id:Int,
                             val name:String,
                             val city_id:Int,
                             val address:String,
                             val device_token:String?,
                             val email:String,
                             val phone:String,
                             val email_verified_at:Int? ): Parcelable

@Parcelize
data class FullTripHotelData(val id:Int,
                             val name:String,
                             val city_id:Int,
                             val star:Int,
                             val pivot:SomeData,
                             val cities:HotelCitiesData):Parcelable

@Parcelize
data class SomeData(val package_id:Int,
                    val hotelsList_id:Int ):Parcelable

@Parcelize
data class FullTripCitiesData(val id:Int,
                          val name:String,
                          val country_id:Int,
                          val country:FullTripCountriesData):Parcelable
@Parcelize
data class FullTripCountriesData(val id:Int,val name:String,val iso3:String):Parcelable


@Parcelize
data class FullTripMainReserve(val data:ReserveFullTrip):Parcelable
@Parcelize
data class ReserveFullTrip(val Packages:FullTripReserve):Parcelable
@Parcelize
data class FullTripReserve(val id:Int,
                       val name:String,
                       val city_id: Int,
                       val email:String,
                       val phone:String,
                       val valid_packages:List<FullTripReserveData>?,
                       val expired_packages:List<FullTripReserveData>?):Parcelable
@Parcelize
data class FullTripReserveData(val id:Int,
                               val city_id:Int,
                               val date:String,
                               val price:String,
                               val details:String,
                               val title:String,
                               val company_id:Int,
                                val pivot:FullTripPivotData,
                               val hotels_list:List<FullTripHotelData>,
                               val companies:FullTripCompanies,
                               val images:List<FullTripImages>,
                               val cities:FullTripCitiesData,):Parcelable
@Parcelize
data class FullTripPivotData(val user_id:Int,val package_id:Int,val quantity:Int,val notes:String?,val status:String,val id:Int):Parcelable