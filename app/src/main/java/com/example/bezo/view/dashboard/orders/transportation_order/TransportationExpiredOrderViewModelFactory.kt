package com.example.bezo.view.dashboard.orders.transportation_order

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class TransportationExpiredOrderViewModelFactory (private val application: Application): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransportationExpiredOrderViewModel::class.java)) {
            return TransportationExpiredOrderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}