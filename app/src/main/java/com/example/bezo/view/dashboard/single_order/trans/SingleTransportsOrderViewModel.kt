package com.example.bezo.view.dashboard.single_order.trans

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bezo.model.data.TransportReserveData

class SingleTransportsOrderViewModel(app: Application,private val transportData: TransportReserveData): AndroidViewModel(app) {

    private var _data = MutableLiveData<TransportReserveData>()
    val data : LiveData<TransportReserveData>
        get() = _data

    private val _noImage = MutableLiveData<Boolean?>()
    val noImage: LiveData<Boolean?>
        get() = _noImage

    init {
        displayData()
        checkImageExistence()

    }

    private fun displayData() {
        _data.value = transportData
    }

    private fun checkImageExistence(){
        _noImage.value =  transportData.images.isNullOrEmpty()
    }



}