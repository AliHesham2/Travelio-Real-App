package com.example.bezo.view.single.fulltrip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bezo.model.data.FullTrip


class SingleFullTripViewModel(private val receivedData: FullTrip, app: Application): AndroidViewModel(app) {

    private var _data = MutableLiveData<FullTrip?>()
    val data : LiveData<FullTrip?>
        get() = _data

    private val _noImage = MutableLiveData<Boolean?>()
    val noImage: LiveData<Boolean?>
        get() = _noImage

    private val _noHotel = MutableLiveData<Boolean?>()
    val noHotel: LiveData<Boolean?>
        get() = _noHotel


    init {
        _data.value = receivedData
        checkImage()
        checkHotel()
    }

    private fun checkImage(){
        if(receivedData.images.isNullOrEmpty()){
            _noImage.value = true
        }
    }
    private fun checkHotel(){
        if(receivedData.hotels_list.isNullOrEmpty()){
            _noHotel.value = true
        }
    }


    override fun onCleared() {
        super.onCleared()
        _data.value = null
        _noImage.value = null
        _noHotel.value = null
    }
}