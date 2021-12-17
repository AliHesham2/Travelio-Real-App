package com.example.travelio.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collection(val city:Cities,
                      val levels:Levels,
                      val meals:Meals,
                      val types:Types,
                      val location:Locations,
                      val hotelName:HotelNames): Parcelable

data class HotelFilterCollection(val perRoom:String,
                                 val hotels_list_id:String,val hotelName:String?,
                                 val hotels_list_city_id:String, val cityName:String?, val countryName:String?,
                                 val meal_id:String, val mealName:String?,
                                 val stars:String,
                                 val minPrice:String,
                                 val maxPrice:String)

data class TransportFilterCollection(val city_from_id:String, val cityFName:String?, val countryFName:String?,
                                     val city_to_id:String, val cityTName:String?, val countryTName:String?,
                                     val level_id:String, val levelName:String?,
                                     val type_id:String, val typeName:String?,
                                     val minPrice:String,
                                     val maxPrice:String,
                                     val fromDate:String,
                                     val toDate:String)

data class TripFilterCollection(val city_id:String,  val cityName:String?, val countryName:String?,
                                val location_id:String, val locationName:String?,
                                val minPrice:String,
                                val maxPrice:String,
                                val fromDate:String,
                                val toDate:String)

data class FullTripFilterCollection(val hotels_list_id:String, val hotelName:String?,
                                    val hotels_list_city_id:String, val cityName:String?, val countryName:String?,
                                    val minPrice:String,
                                    val maxPrice:String,
                                    val fromDate:String,
                                    val toDate:String)

