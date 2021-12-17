package com.example.travelio.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cities(val data:CitesList): Parcelable
@Parcelize
data class CitesList(val citiesList:List<CitesData>): Parcelable
@Parcelize
data class CitesData(val id:Int,val name:String,val country_id:Int,val country:Country): Parcelable
@Parcelize
data class Country(val id:Int,val name:String,val iso3:String): Parcelable
