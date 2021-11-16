package com.example.bezo.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Transportations(val data:TransportationData): Parcelable

@Parcelize
data class TransportationData(val Transports:TransportationModel): Parcelable

@Parcelize
data class TransportationModel(val data:List<Transportation>): Parcelable

@Parcelize
data class Transportation (val id:Int,
                  val type:String,
                  val level:String,
                  val date:String,
                  val from:String,
                  val to:String,
                  val price:String,
                  val company_id:Int,
                  val images:List<TransportationImages>,
                  val companies:TransportationCompanies): Parcelable

@Parcelize
data class TransportationImages(val id:Int,val imageUrl:String, val transport_id:Int):Parcelable

@Parcelize
data class TransportationCompanies(val id:Int,
                     val name:String,
                     val city:String,
                     val address:String,
                     val email:String,
                     val phone:String,
                     val email_verified_at:Int? ):Parcelable