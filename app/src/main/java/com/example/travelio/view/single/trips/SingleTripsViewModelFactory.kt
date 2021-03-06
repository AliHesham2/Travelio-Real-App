package com.example.travelio.view.single.trips

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelio.R
import com.example.travelio.model.data.Trip

class SingleTripsViewModelFactory(private val receivedData: Trip, private val application: Application): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleTripsViewModel::class.java)) {
            return SingleTripsViewModel(receivedData,application) as T
        }
        throw IllegalArgumentException(application.resources.getString(R.string.UN_KNOW_VIEW_MODEL))
    }
}