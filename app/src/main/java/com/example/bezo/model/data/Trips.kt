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
                 val country:String,
                 val city:String,
                 val date:String,
                 val location:String,
                 val price:String,
                 val details:String,
                 val title:String,
                 val company_id:Int,
                 val images:List<TripImages>,
                 val companies:TripCompanies): Parcelable

@Parcelize
data class TripImages(val id:Int,val imageUrl:String, val trip_id:Int): Parcelable

@Parcelize
data class TripCompanies(val id:Int,
                         val name:String,
                         val city:String,
                         val address:String,
                         val email:String,
                         val phone:String,
                         val email_verified_at:Int? ): Parcelable