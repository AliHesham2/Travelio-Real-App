package com.example.bezo.view.single.hotel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bezo.model.data.Hotel

class SingleHotelViewModel(private val receivedData: Hotel, app: Application):AndroidViewModel(app) {

    private var _data = MutableLiveData<Hotel?>()
    val data : LiveData<Hotel?>
    get() = _data

    private val _noImage = MutableLiveData<Boolean?>()
    val noImage: LiveData<Boolean?>
        get() = _noImage


    init {
        _data.value = receivedData
        checkImage()
    }

    private fun checkImage(){
        if(receivedData.images.isNullOrEmpty()){
            _noImage.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        _data.value = null
        _noImage.value = null
    }

}