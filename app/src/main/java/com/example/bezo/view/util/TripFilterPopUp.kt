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
import com.example.bezo.databinding.CustomFilterTripBinding
import com.example.bezo.model.data.Collection
import com.example.bezo.model.data.HotelFilterCollection
import com.example.bezo.model.data.TripFilterCollection
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TripFilterPopUp {
    companion object{
        private lateinit var tProgressDialog: Dialog
        private var cityValidation = true
        private var locationValidation = true
        private val myCalendar: Calendar = Calendar.getInstance()

        fun handleTripFilter(context: Context, collection: Collection,tripParams:(data: TripFilterCollection) -> Unit){
            val tripBinding = CustomFilterTripBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            var prices = mutableListOf(0F, 20000F)
            setupCities(tripBinding, context, collection)
            setupLocation(tripBinding, context, collection)
            tProgressDialog = Dialog(context)
            tProgressDialog.setContentView(tripBinding.root)
            tProgressDialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            tProgressDialog.setCancelable(false)
            tProgressDialog.show()

            //update price Slider Currency
            tripBinding.slider.setLabelFormatter{ value: Float ->
                val format = NumberFormat.getCurrencyInstance()
                format.maximumFractionDigits = 0
                format.currency = Currency.getInstance("EGP")
                format.format(value.toDouble())
            }

            //date
            val format = SimpleDateFormat(context.resources.getString(R.string.DATE_FORM), Locale.US)

            tripBinding.submit.setOnClickListener {
                val wantedCity = collection.city.data.citiesList.find { "${it.name} (${it.country.iso3})" == tripBinding.City.editText?.text.toString().trim() }
                val wantedLocations = collection.types.data.TransportTypesList.find { it.name == tripBinding.location.editText?.text.toString().trim() }

                if (tripBinding.City.editText?.text.toString().isNotEmpty()) {
                    if (wantedCity == null) { cityValidation = false
                        tripBinding.City.editText?.error = context.resources.getString(R.string.FROM_LIST) } } else { cityValidation = true }

                if (tripBinding.location.editText?.text.toString().isNotEmpty()) { if (wantedLocations == null) { locationValidation = false
                    tripBinding.location.editText?.error = context.resources.getString(R.string.FROM_LIST) } } else { locationValidation = true }

                val priceF = tripBinding.priceF.editText?.text.toString().trim().toIntOrNull()
                val priceT = tripBinding.priceT.editText?.text.toString().trim().toIntOrNull()

                val dateF = tripBinding.dateF.editText?.text?.toString() ?: ""
                val dateT = tripBinding.dateT.editText?.text?.toString() ?: ""
                if(cityValidation && locationValidation ){
                  TripFilterCollection(wantedCity?.id?.toString() ?: "",wantedLocations?.id?.toString() ?: "" ,priceF?.toString() ?: "" , priceT?.toString() ?: "" ,dateF, dateT)
                }
            }

            tripBinding.dateF.editText?.setOnClickListener {
                val currentDate = DatePickerDialog(context, updateDateLabel( tripBinding.dateF.editText,format), myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH])
                currentDate.datePicker.minDate = System.currentTimeMillis() - 1000
                currentDate.show()
            }
            tripBinding.dateT.editText?.setOnClickListener {
                val currentDate = DatePickerDialog(context, updateDateLabel( tripBinding.dateT.editText,format), myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH])
                currentDate.datePicker.minDate = System.currentTimeMillis() - 1000
                currentDate.show()
            }
            tripBinding.close.setOnClickListener {
                tProgressDialog.dismiss()
            }

            tripBinding.slider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    val values = slider.values
                    tripBinding.priceF.editText?.setText(values[0].toInt().toString())
                    tripBinding.priceT.editText?.setText(values[1].toInt().toString())
                }

            })
            tripBinding.priceF.editText?.doOnTextChanged { text, _, _, _ ->
                if (text != null && text.isNotEmpty()) {
                    prices = HotelFilterPopUp.handleFirstValue(prices, text)
                    handleSliders(tripBinding, prices)
                }
            }
            tripBinding.priceT.editText?.doOnTextChanged { text, _, _, _ ->
                if (text != null && text.isNotEmpty()) {
                    prices = HotelFilterPopUp.handleSecondValue(prices, text)
                    handleSliders(tripBinding, prices)
                }
            }

        }
        private fun handleSliders(tripBinding: CustomFilterTripBinding, prices: MutableList<Float>) {
            tripBinding.slider.setValues(prices[0], prices[1])
        }


        private fun setupLocation(tripBinding: CustomFilterTripBinding, context: Context, collection: Collection) {
            val x = mutableListOf<String>()
            collection.location.data.TripLocationsList.map { x.add(it.name) }
            val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, x)
            (tripBinding.location.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (tripBinding.location.editText as? AutoCompleteTextView)?.threshold = 1
        }

        private fun setupCities(tripBinding: CustomFilterTripBinding, context: Context, collection: Collection) {
            val x = mutableListOf<String>()
            collection.city.data.citiesList.map {
                x.add("${it.name} (${it.country.iso3})")
            }
            val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, x)
            (tripBinding.City.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (tripBinding.City.editText as? AutoCompleteTextView)?.threshold = 1
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