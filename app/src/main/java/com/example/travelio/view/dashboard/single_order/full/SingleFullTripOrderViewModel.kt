package com.example.travelio.view.dashboard.single_order.full

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.travelio.model.data.FullTripReserveData


class SingleFullTripOrderViewModel(app: Application,private val fullData: FullTripReserveData): AndroidViewModel(app) {

    private var _data = MutableLiveData<FullTripReserveData>()
    val data : LiveData<FullTripReserveData>
        get() = _data

    private val _noImage = MutableLiveData<Boolean?>()
    val noImage: LiveData<Boolean?>
        get() = _noImage

    private val _noHotel = MutableLiveData<Boolean?>()
    val noHotel: LiveData<Boolean?>
        get() = _noHotel

    init {
        displayData()
        checkImageExistence()
        checkHotelExistence()
    }

    private fun displayData() {
        _data.value = fullData
    }

    private fun checkImageExistence(){
       _noImage.value =  fullData.images.isNullOrEmpty()
    }
    private fun checkHotelExistence(){
        _noHotel.value =  fullData.hotels_list.isNullOrEmpty()
    }

}