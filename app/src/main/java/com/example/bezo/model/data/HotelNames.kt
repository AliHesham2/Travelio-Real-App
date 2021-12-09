package com.example.bezo.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HotelNames (val data:HotelNamesList): Parcelable
@Parcelize
data class HotelNamesList (val hotelsList:List<HotelNamesListData>): Parcelable
@Parcelize
data class HotelNamesListData (val id:Int, val name:String): Parcelable
