package com.example.bezo.view.dashboard.orders.full_trip_order

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FullTripExpiredOrderViewModelFactory (private val application: Application): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FullTripExpiredOrderViewModel::class.java)) {
            return FullTripExpiredOrderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}