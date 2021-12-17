package com.example.travelio.view.dashboard.fulltrip

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.travelio.R
import com.example.travelio.model.data.FullTrip
import com.example.travelio.model.data.FullTripFilterCollection
import com.example.travelio.model.data.FullTrips
import com.example.travelio.model.preference.Token
import com.example.travelio.requests.fulltrip.FullTripRequests
import com.example.travelio.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FullTripViewModel(private val app:Application):AndroidViewModel(app) {
    private var pageNumber = 0

    private var _data = MutableLiveData<MutableList<FullTrip>?>()
    val data: LiveData<MutableList<FullTrip>?>
        get() = _data

    private var _filterData = MutableLiveData<FullTripFilterCollection?>()
    val filterData : LiveData<FullTripFilterCollection?>
        get() = _filterData

    private val _loading = MutableLiveData<Boolean?>()
    val loading: LiveData<Boolean?>
        get() = _loading

    private val _swipeLoading = MutableLiveData<Boolean?>()
    val swipeLoading: LiveData<Boolean?>
        get() = _swipeLoading

    private val _noAuth = MutableLiveData<Boolean?>()
    val noAuth: LiveData<Boolean?>
        get() = _noAuth

    private val _loadMore = MutableLiveData<Boolean?>()
    val loadMore: LiveData<Boolean?>
        get() = _loadMore

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    init {
        callRequest()
    }

    fun callRequest(hotels_list_id:String?=app.resources.getString(R.string.EMPTY), hotels_list_city_id:String?=app.resources.getString(R.string.EMPTY), minPrice:String?=app.resources.getString(R.string.EMPTY), maxPrice:String?=app.resources.getString(R.string.EMPTY), fromDate:String?=app.resources.getString(R.string.EMPTY), toDate:String?=app.resources.getString(R.string.EMPTY)) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getFullTrips(hotels_list_id,hotels_list_city_id, minPrice, maxPrice, fromDate, toDate)
            } catch (t: Exception) {
                handleException(t)
            }
        }
    }

    private suspend fun getFullTrips(hotels_list_id: String?, hotels_list_city_id: String?, minPrice: String?, maxPrice: String?, fromDate: String?, toDate: String?) {
        loading()
        pageNumber++
        FullTripRequests.getFullPackage(pageNumber,app.resources,hotels_list_id!!,hotels_list_city_id!!, minPrice!!, maxPrice!!, fromDate!!, toDate!!){ data, error, success ->
            if(success){
                whenSuccess(data)
            }else{
                if(error == null){
                    authFail()
                }else{
                    whenFail(error)
                }
            }
        }
    }

    private fun whenSuccess(data: FullTrips?) {
        if (data != null) {
            val tripData = data.data.Packages.data
            if (tripData.isNotEmpty()) {
                _loadMore.value = true
                if (_data.value.isNullOrEmpty()) {
                    _data.value = tripData.toMutableList()
                    _data.value =_data.value
                } else {
                    _data.value!!.addAll(tripData)
                    _data.value = _data.value
                }
            } else {
                _loadMore.value = false
            }
            stopLoading()
        }
    }

    fun resetData(){
        _data.value = null
        pageNumber = 0
    }

    //Show Loading spinner
    private suspend fun loading() {
        withContext(Dispatchers.Main) {
            _loading.value = true
        }
    }

    //No Network Handler
    private suspend fun handleException(t: Exception) {
        withContext(Dispatchers.Main) {
            stopLoading()
            PopUpMsg.handleError(app.applicationContext, t)
        }
    }

    //Hide loading spinner
    private fun stopLoading() {
        _swipeLoading.value = false
        _loading.value = false
    }

    //Handle Backend Errors
    private fun whenFail(msg: String) {
        stopLoading()
        _error.value = msg
    }

    //Session Expired
    private fun authFail() {
        stopLoading()
        Token.removeToken()
        _noAuth.value = true
    }
    fun saveFilterData(data: FullTripFilterCollection) {
        _filterData.value = data
    }
    fun swipeLoadHandle(){
        resetData()
        _swipeLoading.value = true
        if(_filterData.value != null){
            callRequest(_filterData.value!!.hotels_list_id, _filterData.value!!.hotels_list_city_id, _filterData.value!!.minPrice, _filterData.value!!.maxPrice, _filterData.value!!.fromDate, _filterData.value!!.toDate)
        }else{
            callRequest()
        }
    }
}