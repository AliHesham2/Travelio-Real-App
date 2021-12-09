package com.example.bezo.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Trips(val data:TripData): Parcelable

@Parcelize
data class TripData(val trips:TripModel): Parcelable

@Parcelize
data class TripModel(val data:List<Trip>): Parcelable

@Parcelize
data class Trip (val id:Int,
                 val city_id:Int,
                 val date:String,
                 val location_id:Int,
                 val price:String,
                 val details:String,
                 val title:String,
                 val company_id:Int,
                 val images:List<TripImages>,
                 val companies:TripCompanies,
                 val cities:TripCitiesData,
                 val locations:LocationsData): Parcelable

@Parcelize
data class TripImages(val id:Int,val imageUrl:String, val trip_id:Int): Parcelable

@Parcelize
data class TripCompanies(val id:Int,
                         val name:String,
                         val city_id: Int,
                         val address:String,
                         val email:String,
                         val phone:String,
                         val email_verified_at:Int? ): Parcelable
@Parcelize
data class TripCitiesData(val id:Int,
                      val name:String,
                      val country_id:Int,
                      val country:TripCountriesData):Parcelable
@Parcelize
data class TripCountriesData(val id:Int,val name:String,val iso3:String):Parcelable
@Parcelize
data class LocationsData(val id: Int,val name:String):Parcelable

@Parcelize
data class TripMainReserve(val data:ReserveTrip): Parcelable
@Parcelize
data class ReserveTrip(val Trips:TripReserve): Parcelable
@Parcelize
data class TripReserve(val id:Int,
                            val name:String,
                            val city_id: Int,
                            val email:String,
                            val phone:String,
                            val expired_trips:List<TripReserveData>?,
                            val valid_trips:List<TripReserveData>?): Parcelable
@Parcelize
data class TripReserveData(val id:Int,
                           val city_id:Int,
                           val date:String,
                           val location_id:Int,
                           val price:String,
                           val details:String,
                           val title:String,
                           val company_id:Int,
                           val pivot:TripPivotData,
                           val images:List<TripImages>,
                           val companies:TripCompanies,
                           val cities:TripCitiesData,
                           val locations:LocationsData ): Parcelable
@Parcelize
data class TripPivotData(val user_id:Int,val trip_id:Int,val quantity:Int,val notes:String?,val status:String,val id:Int): Parcelable