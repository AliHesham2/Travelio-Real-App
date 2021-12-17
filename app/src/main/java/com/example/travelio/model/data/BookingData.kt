package com.example.travelio.model.data



data class HotelBookingData(val quantity:Int,val hotel_id :Int)
data class TransportsBookingData(val quantity:Int,val transport_id :Int)
data class TripsBookingData(val quantity:Int,val trip_id :Int)
data class FullTripBookingData(val quantity:Int,val package_id :Int)
data class DeleteData(val id:Int)
