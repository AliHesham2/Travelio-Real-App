package com.example.bezo.view.dashboard.single_order.trans

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.model.data.TransportReserveData

class SingleTransportsOrderViewModelFactory(
    private val application: Application,
    private val transportData: TransportReserveData
): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleTransportsOrderViewModel::class.java)) {
            return SingleTransportsOrderViewModel(application,transportData) as T
        }
        throw IllegalArgumentException(application.resources.getString(R.string.UN_KNOW_VIEW_MODEL))
    }
}