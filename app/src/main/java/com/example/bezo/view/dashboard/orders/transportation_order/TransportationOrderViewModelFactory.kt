package com.example.bezo.view.dashboard.orders.transportation_order

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class TransportationOrderViewModelFactory (private val application: Application): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransportationOrderViewModel::class.java)) {
            return TransportationOrderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}