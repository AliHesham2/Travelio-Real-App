package com.example.bezo.view.single.transportation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bezo.model.data.Transportation

class SingleTransportationViewModel(private val receivedData: Transportation, app: Application): AndroidViewModel(app) {

    private var _data = MutableLiveData<Transportation?>()
    val data : LiveData<Transportation?>
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