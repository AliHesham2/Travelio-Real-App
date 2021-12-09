package com.example.bezo.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collection(val city:Cities,
                      val levels:Levels,
                      val meals:Meals,
                      val types:Types,
                      val location:Locations,val hotelName:HotelNames): Parcelable

data class HotelFilterCollection(val perRoom:String,
                                 val hotels_list_id:String,
                                 val hotels_list_city_id:String,
                                 val meal_id:String,
                                 val stars:String,
                                 val minPrice:String,
                                 val maxPrice:String)

data class TransportFilterCollection(val city_from_id:String,
                                     val city_to_id:String,
                                     val level_id:String,
                                     val type_id:String,
                                     val minPrice:String,
                                     val maxPrice:String,
                                     val fromDate:String,
                                     val toDate:String)

data class TripFilterCollection(val city_id:String,
                                val location_id:String,
                                val minPrice:String,
                                val maxPrice:String,
                                val fromDate:String,
                                val toDate:String)

data class FullTripFilterCollection(val hotels_list_id:String,
                                    val hotels_list_city_id:String,
                                    val minPrice:String,
                                    val maxPrice:String,
                                    val fromDate:String,
                                    val toDate:String)

