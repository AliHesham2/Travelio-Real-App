package com.example.travelio.view.util

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.travelio.R
import com.example.travelio.model.data.*
import com.example.travelio.view.dashboard.fulltrip.FullTripAdapter
import com.example.travelio.view.dashboard.hotel.HotelAdapter
import com.example.travelio.view.dashboard.orders.full_trip_order.FullTripOrderAdapter
import com.example.travelio.view.dashboard.orders.hotel_order.HotelOrderAdapter
import com.example.travelio.view.dashboard.orders.transportation_order.TransportsOrderAdapter
import com.example.travelio.view.dashboard.orders.trips_order.TripsOrderAdapter
import com.example.travelio.view.dashboard.transportation.TransportAdapter
import com.example.travelio.view.dashboard.trips.TripAdapter
import com.example.travelio.view.single.fulltrip.SingleFullTripAdapter
import com.example.travelio.view.single.hotel.SingleHotelAdapter
import com.example.travelio.view.single.transportation.SingleTransportAdapter
import com.example.travelio.view.single.trips.SingleTripAdapter
import com.example.travelio.view.single.fulltrip.SingleTripHotelAdapter


//All
@SuppressLint("SetTextI18n")
@BindingAdapter("price")
fun price(txt: TextView, data: String?) {
    txt.text = data + txt.resources.getString(R.string.EMPTY_WITH_SPACE)  + txt.resources.getString(R.string.LE)
}

@BindingAdapter("quantity")
fun quantity(txt: TextView, data: Int?) {
    txt.text = "$data"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("city")
fun city(txt: TextView, data: UserData?) {
    if (data != null) {
        txt.text = "${data.cities.name} (${data.cities.country.iso3})"
    }
}


@BindingAdapter("status")
fun status(img: ImageView, data: String?) {
    if(data != null){
        when(data){
            img.resources.getString(R.string.ACCEPTED) -> {img.setImageDrawable(ContextCompat.getDrawable(img.context,R.drawable.accepted))}
            img.resources.getString(R.string.REJECTED) -> {img.setImageDrawable(ContextCompat.getDrawable(img.context,R.drawable.reject))}
            img.resources.getString(R.string.PENDING)  -> {img.setImageDrawable(ContextCompat.getDrawable(img.context,R.drawable.pending))}
        }
    }
}


@BindingAdapter("showImage")
fun showImage(img: ImageView, data: String?) {
    if (data != null){
        val imageUrl = img.resources.getString(R.string.PHOTO_LINK) + data
        val imgUri = imageUrl.toUri().buildUpon().scheme("https").build()
        Glide
            .with(img.context)
            .load(imgUri)
            .centerCrop()
            .error(img.resources.getString(R.string.PHOTO_Default_LINK))
            .into(img)
    }
}


//Hotels
@BindingAdapter("hotelAdapter")
fun hotelAdapter(recyclerView: RecyclerView, data: MutableList<Hotel>?) {
    val adapter = recyclerView.adapter as HotelAdapter
        adapter.submitList(data?.toMutableList())
}

@BindingAdapter("hotelArray")
fun hotelArray(txt: TextView, data: HotelData?) {
    if (data != null) {
        txt.text = data.name
    }
}

@BindingAdapter("hotelReserveAdapter")
fun hotelReserveAdapter(recyclerView: RecyclerView, data: List<HotelReserveData>?) {
    val adapter = recyclerView.adapter as HotelOrderAdapter
    adapter.submitList(data)
}
@BindingAdapter("hotelImageAdapter")
fun hotelImageAdapter(recyclerView: RecyclerView, data: List<Images>?) {
    val adapter = recyclerView.adapter as SingleHotelAdapter
    adapter.submitList(data)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("hotelPerPerson")
fun hotelPerPerson(txt: TextView, data: String?) {
    txt.text = data + txt.resources.getString(R.string.EMPTY_WITH_SPACE) + txt.resources.getString(R.string.PER_PERSON)
}
@BindingAdapter("hotelRate")
fun hotelRate(rate: RatingBar, data: Int?) {
    if(data != null){
        rate.rating = data.toFloat()
    }else{
        rate.rating = 0F
    }
}


//Transports
@BindingAdapter("transportAdapter")
fun transportAdapter(recyclerView: RecyclerView, data: List<Transportation>?) {
    val adapter = recyclerView.adapter as TransportAdapter
        adapter.submitList(data?.toMutableList())
}

@BindingAdapter("transportOrderAdapter")
fun transportOrderAdapter(recyclerView: RecyclerView, data: List<TransportReserveData>?) {
    val adapter = recyclerView.adapter as TransportsOrderAdapter
    adapter.submitList(data)
}

@BindingAdapter("transportImageAdapter")
fun transportImageAdapter(recyclerView: RecyclerView, data: List<TransportationImages>?) {
    val adapter = recyclerView.adapter as SingleTransportAdapter
    adapter.submitList(data)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("transportFrom")
fun transportFrom(txt: TextView, data: String?) {
    txt.text = txt.resources.getString(R.string.FROM) + txt.resources.getString(R.string.EMPTY_WITH_SPACE) + data
}

@BindingAdapter("transportDrawable")
fun transportDrawable(img: ImageView, data: String?) {
    when(data){
        "Car" -> img.setBackgroundResource(R.drawable.ic_car_s)
        "Bus" -> img.setBackgroundResource(R.drawable.ic_bus_s)
        "Plane" -> img.setBackgroundResource(R.drawable.ic_plane_s)
        else ->  img.setBackgroundResource(R.drawable.ic_car_s)
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("transportTo")
fun transportTo(txt: TextView, data: String?) {
    txt.text = txt.resources.getString(R.string.TO) + txt.resources.getString(R.string.EMPTY_WITH_SPACE) + data
}



@SuppressLint("SetTextI18n")
@BindingAdapter("transportClass")
fun transportClass(txt: TextView, data: String?) {
    txt.text = txt.resources.getString(R.string.CLASS) + txt.resources.getString(R.string.EMPTY_WITH_SPACE) + data
}

//Trips
@BindingAdapter("tripAdapter")
fun tripAdapter(recyclerView: RecyclerView, data: MutableList<Trip>?) {
    val adapter = recyclerView.adapter as TripAdapter
    adapter.submitList(data?.toMutableList())
}

@BindingAdapter("tripOrderAdapter")
fun tripOrderAdapter(recyclerView: RecyclerView, data: List<TripReserveData>?) {
    val adapter = recyclerView.adapter as TripsOrderAdapter
    adapter.submitList(data)
}

@BindingAdapter("tripImageAdapter")
fun tripImageAdapter(recyclerView: RecyclerView, data: List<TripImages>?) {
    val adapter = recyclerView.adapter as SingleTripAdapter
    adapter.submitList(data)
}


//FullTrips
@BindingAdapter("fullTripAdapter")
fun fullTripAdapter(recyclerView: RecyclerView, data: MutableList<FullTrip>?) {
    val adapter = recyclerView.adapter as FullTripAdapter
    adapter.submitList(data?.toMutableList())
}
@BindingAdapter("fullTripReserveAdapter")
fun fullTripReserveAdapter(recyclerView: RecyclerView, data: List<FullTripReserveData>?) {
    val adapter = recyclerView.adapter as FullTripOrderAdapter
    adapter.submitList(data)
}
@BindingAdapter("fullTripImageAdapter")
fun fullTripImageAdapter(recyclerView: RecyclerView, data: List<FullTripImages>?) {
    val adapter = recyclerView.adapter as SingleFullTripAdapter
    adapter.submitList(data)
}
@BindingAdapter("fullTripHotelImageAdapter")
fun fullTripHotelImageAdapter(recyclerView: RecyclerView, data: List<FullTripHotelData>?) {
    val adapter = recyclerView.adapter as SingleTripHotelAdapter
    if(data != null){
        adapter.submitList(data)
    }
}



