package com.example.bezo.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Locations (val data:LocationList): Parcelable
@Parcelize
data class LocationList (val TripLocationsList:List<LocationListData>): Parcelable
@Parcelize
data class LocationListData (val id:Int, val name:String): Parcelable