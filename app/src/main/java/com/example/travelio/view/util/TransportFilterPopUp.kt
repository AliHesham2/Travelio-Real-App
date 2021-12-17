package com.example.travelio.view.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.example.travelio.R
import com.example.travelio.databinding.CustomFilterTransportBinding
import  com.example.travelio.model.data.Collection
import com.example.travelio.model.data.TransportFilterCollection
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TransportFilterPopUp {
    companion object{
        private lateinit var tProgressDialog: Dialog
        private var typeValidation = true
        private var levelValidation = true
        private var cityValidation = true
        private var cityTValidation = true
        private var priceValidation = true
        private var prices = mutableListOf(0F, 20000F)
        private val myCalendar: Calendar = Calendar.getInstance()

    fun handleTransportFilter(context: Context, collection:Collection,oldFilter:TransportFilterCollection?,transportParams:(data: TransportFilterCollection) -> Unit ) {
        val transportBinding = CustomFilterTransportBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        if (oldFilter != null){ displayOldFilter(transportBinding,oldFilter,context) }
        setupCities(transportBinding, context, collection)
        setupCitiesT(transportBinding, context, collection)
        setupLevels(transportBinding, context, collection)
        setupTypes(transportBinding, context, collection)
        tProgressDialog = Dialog(context)
        tProgressDialog.setContentView(transportBinding.root)
        tProgressDialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        tProgressDialog.setCancelable(false)
        tProgressDialog.show()

        //update price Slider Currency
        transportBinding.transportSlider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance(context.resources.getString(R.string.Currency))
            format.format(value.toDouble())
        }
        //date
        val format = SimpleDateFormat(context.resources.getString(R.string.DATE_FORM), Locale.US)

        transportBinding.transportSubmit.setOnClickListener {
            val wantedCity = collection.city.data.citiesList.find { "${it.name} (${it.country.iso3})" == transportBinding.transportCity.editText?.text.toString().trim() }
            val wantedCityT = collection.city.data.citiesList.find { "${it.name} (${it.country.iso3})" == transportBinding.transportCityT.editText?.text.toString().trim() }
            val wantedTransportType = collection.types.data.TransportTypesList.find { it.name == transportBinding.transportType.editText?.text.toString().trim() }
            val wantedTransportLevel = collection.levels.data.TransportLevelsList.find { it.name == transportBinding.TransportClass.editText?.text.toString().trim() }

            if (transportBinding.transportCity.editText?.text.toString().isNotEmpty()) {
                if (wantedCity == null) { cityValidation = false
                    transportBinding.transportCity.editText?.error = context.resources.getString(R.string.FROM_LIST) } else { cityValidation = true} }

            if (transportBinding.transportCityT.editText?.text.toString().isNotEmpty()) {
                if (wantedCityT == null) { cityTValidation = false
                    transportBinding.transportCityT.editText?.error = context.resources.getString(R.string.FROM_LIST) } else { cityTValidation = true} }

            if (transportBinding.TransportClass.editText?.text.toString().isNotEmpty()) { if (wantedTransportLevel == null) { levelValidation = false
                    transportBinding.TransportClass.editText?.error = context.resources.getString(R.string.FROM_LIST) } else { levelValidation = true} }

            if (transportBinding.transportType.editText?.text.toString().isNotEmpty()) { if (wantedTransportType == null) { typeValidation = false
                    transportBinding.transportType.editText?.error = context.resources.getString(R.string.FROM_LIST) } else { typeValidation = true} }

            val priceF = transportBinding.transportPriceF.editText?.text.toString().trim().toIntOrNull()
            val priceT = transportBinding.transportPriceT.editText?.text.toString().trim().toIntOrNull()

            priceValidation = !(priceF == null || priceT == null || priceF > priceT || priceF < 0 || priceT < 0 || priceT > 20000 || priceF > 20000)
            if(!priceValidation){PopUpMsg.toastMsg(context,context.resources.getString(R.string.CHECK_PRICE))}

            val dateF = transportBinding.transportDateF.editText?.text?.toString() ?: context.resources.getString(R.string.EMPTY)
            val dateT = transportBinding.transportDateT.editText?.text?.toString() ?: context.resources.getString(R.string.EMPTY)

            if(cityValidation && levelValidation && typeValidation && cityTValidation && priceValidation){
                transportParams(TransportFilterCollection(
                    wantedCity?.id?.toString() ?: context.resources.getString(R.string.EMPTY), wantedCity?.name ?:context.resources.getString(R.string.EMPTY) , wantedCity?.country?.iso3 ?:context.resources.getString(R.string.EMPTY) ,
                    wantedCityT?.id?.toString() ?: context.resources.getString(R.string.EMPTY), wantedCityT?.name ?: context.resources.getString(R.string.EMPTY) , wantedCityT?.country?.iso3 ?:context.resources.getString(R.string.EMPTY),
                    wantedTransportLevel?.id?.toString() ?: context.resources.getString(R.string.EMPTY), wantedTransportLevel?.name ?: context.resources.getString(R.string.EMPTY) ,
                    wantedTransportType?.id?.toString() ?: context.resources.getString(R.string.EMPTY), wantedTransportType?.name ?:context.resources.getString(R.string.EMPTY) ,
                    priceF?.toString() ?: context.resources.getString(R.string.EMPTY) ,
                    priceT?.toString() ?: context.resources.getString(R.string.EMPTY),
                    dateF, dateT))
                tProgressDialog.hide()
            }
        }

        transportBinding.transportDateF.editText?.setOnClickListener {
            val currentDate = DatePickerDialog(context, updateDateLabel(transportBinding.transportDateF.editText,format), myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH])
            currentDate.datePicker.minDate = System.currentTimeMillis() - 1000
            currentDate.show()
        }
        transportBinding.transportDateT.editText?.setOnClickListener {
            val currentDate = DatePickerDialog(context, updateDateLabel(transportBinding.transportDateT.editText,format), myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH])
            currentDate.datePicker.minDate = System.currentTimeMillis() - 1000
            currentDate.show()
        }
        transportBinding.transClose.setOnClickListener {
            tProgressDialog.dismiss()
        }
        transportBinding.resetAll.setOnClickListener {
            resetAll(transportBinding,context)
        }
        transportBinding.transportSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = slider.values
                transportBinding.transportPriceF.editText?.setText(values[0].toInt().toString())
                transportBinding.transportPriceT.editText?.setText(values[1].toInt().toString())
            }

        })
        transportBinding.transportPriceF.editText?.doOnTextChanged { text, _, _, _ ->
             if (text != null && text.isNotEmpty() && text.toString().matches("-?\\d+(\\.\\d+)?".toRegex()) && text.toString().toFloat() > 0 && text.toString().toInt() <= 20000 ) {
                 prices[0] = text.toString().toFloat()
            }else{
                 prices[0] = 0F
            }
            handleSliders(transportBinding, prices)
        }
        transportBinding.transportPriceT.editText?.doOnTextChanged { text, _, _, _ ->
              if (text != null && text.isNotEmpty() && text.toString().matches("-?\\d+(\\.\\d+)?".toRegex()) && text.toString().toFloat() > 0 && text.toString().toInt() <= 20000 ) {
                  prices[1] = text.toString().toFloat()
            }else{
                  prices[1] =  20000F
            }
            handleSliders(transportBinding, prices)
        }

    }

    private fun handleSliders(binding: CustomFilterTransportBinding, prices: MutableList<Float>) {
        binding.transportSlider.setValues(prices[0], prices[1])
    }

    private fun setupTypes(binding: CustomFilterTransportBinding, context: Context, collection: Collection) {
        val x = mutableListOf<String>()
        collection.types.data.TransportTypesList.map {
            x.add(it.name)
        }
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, x)
        (binding.transportType.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.transportType.editText as? AutoCompleteTextView)?.threshold = 1
    }

    private fun setupLevels(binding: CustomFilterTransportBinding, context: Context, collection: Collection) {
        val x = mutableListOf<String>()
        collection.levels.data.TransportLevelsList.map {
            x.add(it.name)
        }
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, x)
        (binding.TransportClass.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.TransportClass.editText as? AutoCompleteTextView)?.threshold = 1
    }

    private fun setupCities(binding: CustomFilterTransportBinding, context: Context, collection: Collection) {
        val x = mutableListOf<String>()
        collection.city.data.citiesList.map {
            x.add("${it.name} (${it.country.iso3})")
        }
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, x)
        (binding.transportCity.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.transportCity.editText as? AutoCompleteTextView)?.threshold = 1
    }

        private fun setupCitiesT(binding: CustomFilterTransportBinding, context: Context, collection: Collection) {
            val x = mutableListOf<String>()
            collection.city.data.citiesList.map {
                x.add("${it.name} (${it.country.iso3})")
            }
            val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, x)
            (binding.transportCityT.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (binding.transportCityT.editText as? AutoCompleteTextView)?.threshold = 1
        }
        private fun updateDateLabel(editText: EditText?, format: SimpleDateFormat): DatePickerDialog.OnDateSetListener {
            return DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                editText?.setText(format.format(myCalendar.time))
            }
        }
        private fun resetAll(binding:CustomFilterTransportBinding, context: Context) {
            binding.transportType.editText?.text?.clear()
            binding.TransportClass.editText?.text?.clear()
            binding.transportCity.editText?.text?.clear()
            binding.transportCityT.editText?.text?.clear()
            binding.transportPriceF.editText?.setText(context.resources.getString(R.string.DEFAULT_VALUE))
            binding.transportPriceT.editText?.setText(context.resources.getString(R.string.MAX_PRICE))
            binding.transportDateF.editText?.text?.clear()
            binding.transportDateT.editText?.text?.clear()
            binding.transportSlider.setValues(context.resources.getString(R.string.DEFAULT_VALUE).toFloat(), context.resources.getString(R.string.MAX_PRICE).toFloat())
            typeValidation = true
            levelValidation = true
            cityValidation = true
            cityTValidation = true
        }
        private fun displayOldFilter(binding: CustomFilterTransportBinding, oldFilter: TransportFilterCollection, context: Context) {
            val cityF = if (oldFilter.cityFName.isNullOrEmpty()){context.resources.getString(R.string.EMPTY)}else{ "${oldFilter.cityFName} (${oldFilter.countryFName})"}
            val cityT = if (oldFilter.cityTName.isNullOrEmpty()){context.resources.getString(R.string.EMPTY)}else{ "${oldFilter.cityTName} (${oldFilter.countryTName})"}
            binding.transportCity.editText?.setText(cityF)
            binding.transportCityT.editText?.setText(cityT)

            binding.transportType.editText?.setText(oldFilter.typeName)
            binding.TransportClass.editText?.setText(oldFilter.levelName)
            binding.transportPriceF.editText?.setText(oldFilter.minPrice)
            binding.transportPriceT.editText?.setText(oldFilter.maxPrice)
            binding.transportDateF.editText?.setText(oldFilter.fromDate)
            binding.transportDateT.editText?.setText(oldFilter.toDate)
        }
    }
}


