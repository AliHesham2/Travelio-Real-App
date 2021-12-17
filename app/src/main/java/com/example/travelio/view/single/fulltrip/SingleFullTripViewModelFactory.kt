package com.example.travelio.view.single.fulltrip

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelio.R
import com.example.travelio.model.data.FullTrip

class SingleFullTripViewModelFactory(private val receivedData: FullTrip, private val application: Application): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleFullTripViewModel::class.java)) {
            return SingleFullTripViewModel(receivedData,application) as T
        }
        throw IllegalArgumentException(application.resources.getString(R.string.UN_KNOW_VIEW_MODEL))
    }
}