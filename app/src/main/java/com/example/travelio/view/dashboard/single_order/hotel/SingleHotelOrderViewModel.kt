package com.example.travelio.view.dashboard.single_order.hotel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.travelio.model.data.HotelReserveData


class SingleHotelOrderViewModel(app: Application, private val hotelData: HotelReserveData):AndroidViewModel(app) {

    private var _data = MutableLiveData<HotelReserveData>()
    val data : LiveData<HotelReserveData>
        get() = _data

    private val _noImage = MutableLiveData<Boolean?>()
    val noImage: LiveData<Boolean?>
        get() = _noImage

    init {
        displayData()
        checkImageExistence()
    }

    private fun displayData() {
        _data.value = hotelData
    }

    private fun checkImageExistence(){
        _noImage.value =  hotelData.images.isNullOrEmpty()
    }


}