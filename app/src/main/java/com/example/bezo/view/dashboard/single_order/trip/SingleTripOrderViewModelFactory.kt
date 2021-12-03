package com.example.bezo.view.dashboard.single_order.trip

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.model.data.TripReserveData

class SingleTripOrderViewModelFactory(
    private val application: Application,
    private val tripData: TripReserveData
): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleTripOrderViewModel::class.java)) {
            return SingleTripOrderViewModel(application,tripData) as T
        }
        throw IllegalArgumentException(application.resources.getString(R.string.UN_KNOW_VIEW_MODEL))
    }
}