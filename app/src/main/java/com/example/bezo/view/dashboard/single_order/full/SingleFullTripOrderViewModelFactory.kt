package com.example.bezo.view.dashboard.single_order.full

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.model.data.FullTripReserveData

class SingleFullTripOrderViewModelFactory(
    private val application: Application,
    private val fullData: FullTripReserveData
): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleFullTripOrderViewModel::class.java)) {
            return SingleFullTripOrderViewModel(application,fullData) as T
        }
        throw IllegalArgumentException(application.resources.getString(R.string.UN_KNOW_VIEW_MODEL))
    }
}