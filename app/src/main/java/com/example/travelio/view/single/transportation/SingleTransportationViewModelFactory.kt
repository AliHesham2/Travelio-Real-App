package com.example.travelio.view.single.transportation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelio.R
import com.example.travelio.model.data.Transportation

class SingleTransportationViewModelFactory(
    private val receivedData: Transportation,
    private val application: Application
): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SingleTransportationViewModel::class.java)) {
            return SingleTransportationViewModel(receivedData,application) as T
        }
        throw IllegalArgumentException(application.resources.getString(R.string.UN_KNOW_VIEW_MODEL))
    }
}