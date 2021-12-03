package com.example.bezo.view.dashboard.single_order.trip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bezo.model.data.TripReserveData


class SingleTripOrderViewModel(app: Application,private val tripData: TripReserveData): AndroidViewModel(app) {

    private var _data = MutableLiveData<TripReserveData>()
    val data : LiveData<TripReserveData>
        get() = _data

    private val _noImage = MutableLiveData<Boolean?>()
    val noImage: LiveData<Boolean?>
        get() = _noImage

    init {
        displayData()
        checkImageExistence()
    }

    private fun displayData() {
        _data.value = tripData
    }

    private fun checkImageExistence(){
        _noImage.value =  tripData.images.isNullOrEmpty()
    }


}