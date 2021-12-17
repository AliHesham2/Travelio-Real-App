package com.example.travelio.view.dashboard.single_order.hotel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelio.R
import com.example.travelio.model.data.HotelReserveData

class SingleHotelOrderViewModelFactory(
    private val application: Application,
    private val hotelData: HotelReserveData
): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleHotelOrderViewModel::class.java)) {
            return SingleHotelOrderViewModel(application,hotelData) as T
        }
        throw IllegalArgumentException(application.resources.getString(R.string.UN_KNOW_VIEW_MODEL))
    }
}