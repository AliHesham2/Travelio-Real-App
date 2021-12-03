package com.example.bezo.view.dashboard.orders.trips_order

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TripExpiredOrderViewModelFactory (private val application: Application): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripExpiredOrderViewModel::class.java)) {
            return TripExpiredOrderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}