package com.example.travelio.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Types (val data:TypesList): Parcelable
@Parcelize
data class TypesList (val TransportTypesList:List<TypesListData>): Parcelable
@Parcelize
data class TypesListData (val id:Int, val name:String): Parcelable