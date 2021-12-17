package com.example.travelio.model.data

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
                           val type_id:Int,
                           val level_id:Int,
                           val date:String,
                           val city_from_id:Int,
                           val city_to_id:Int,
                           val price:String,
                           val company_id:Int,
                           val images:List<TransportationImages>,
                           val companies:TransportationCompanies,
                           val types: TransportsStaticData,
                           val levels:TransportsStaticData,
                           val from_cities:TransportsFromCity,
                           val to_cities:TransportsFromCity): Parcelable

@Parcelize
data class TransportationImages(val id:Int,val imageUrl:String, val transport_id:Int):Parcelable

@Parcelize
data class TransportationCompanies(val id:Int,
                     val name:String,
                     val city_id: Int,
                     val address:String,
                     val device_token:String?,
                     val email:String,
                     val phone:String,
                     val email_verified_at:Int? ):Parcelable

@Parcelize
data class TransportsFromCity(val id:Int,val name:String,val country_id:Int,val country:TransportsCountries):Parcelable

@Parcelize
data class TransportsCountries(val id:Int,val name:String,val iso3:String):Parcelable

@Parcelize
data class TransportsStaticData(val id:Int,val name:String):Parcelable



@Parcelize
data class TransportMainReserve(val data:ReserveTransport):Parcelable

@Parcelize
data class ReserveTransport(val Transports:TransportReserve):Parcelable

@Parcelize
data class TransportReserve(val id:Int,
                        val name:String,
                        val city_id:Int,
                        val email:String,
                        val phone:String,
                        val expired_transports:List<TransportReserveData>?,
                        val valid_transports:List<TransportReserveData>?):Parcelable

@Parcelize
data class TransportReserveData(val id:Int,
                                val type_id:Int,
                                val level_id:Int,
                                val date:String,
                                val city_from_id:Int,
                                val city_to_id:Int,
                                val price:String,
                                val company_id:Int,
                                val pivot:TransportPivotData,
                                val images:List<TransportationImages>,
                                val companies:TransportationCompanies,
                                val types: TransportsStaticData,
                                val levels:TransportsStaticData,
                                val from_cities:TransportsFromCity,
                                val to_cities:TransportsFromCity):Parcelable
@Parcelize
data class TransportPivotData(val user_id:Int,
                              val transport_id:Int,
                              val quantity:Int,
                              val notes:String?,
                              val status:String,
                              val id:Int):Parcelable

