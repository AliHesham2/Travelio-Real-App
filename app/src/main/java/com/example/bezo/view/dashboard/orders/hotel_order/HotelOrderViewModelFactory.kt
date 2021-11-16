package com.example.bezo.view.dashboard.orders.hotel_order

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class HotelOrderViewModelFactory (private val application: Application): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HotelOrderViewModel::class.java)) {
            return HotelOrderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}