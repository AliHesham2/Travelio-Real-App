package com.example.bezo.view.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.example.bezo.R
import com.example.bezo.databinding.CustomFilterTransportBinding
import  com.example.bezo.model.data.Collection
import com.example.bezo.model.data.HotelFilterCollection
import com.example.bezo.model.data.TransportFilterCollection
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
        private val myCalendar: Calendar = Calendar.getInstance()

    fun handleTransportFilter(context: Context, collection:Collection,transportParams:(data: TransportFilterCollection) -> Unit ) {
        val transportBinding =
            CustomFilterTransportBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        var prices = mutableListOf(0F, 20000F)
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
            format.currency = Currency.getInstance("EGP")
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

            val dateF = transportBinding.transportDateF.editText?.text?.toString() ?: ""
            val dateT = transportBinding.transportDateT.editText?.text?.toString() ?: ""

            if(cityValidation && levelValidation && typeValidation && cityTValidation){
               TransportFilterCollection(wantedCity?.id?.toString() ?: "",wantedCityT?.id?.toString() ?: "",wantedTransportLevel?.id?.toString() ?: "",wantedTransportType?.id?.toString() ?: "",priceF?.toString() ?: "" , priceT?.toString() ?: "" ,dateF, dateT)
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
            if (text != null && text.isNotEmpty()) {
                prices = HotelFilterPopUp.handleFirstValue(prices, text)
                handleSliders(transportBinding, prices)
            }
        }
        transportBinding.transportPriceT.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null && text.isNotEmpty()) {
                prices = HotelFilterPopUp.handleSecondValue(prices, text)
                handleSliders(transportBinding, prices)
            }
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
    }
}


