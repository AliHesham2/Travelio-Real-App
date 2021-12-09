package com.example.bezo.view.util


import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.example.bezo.R
import com.example.bezo.databinding.CustomFilterFullTripBinding
import com.example.bezo.model.data.Collection
import com.example.bezo.model.data.FullTripFilterCollection
import com.example.bezo.model.data.HotelFilterCollection
import com.example.bezo.model.data.TripFilterCollection
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class FullTripFilterPopUp {
    companion object{
        private lateinit var fProgressDialog: Dialog
        private var cityValidation = true
        private var hotelValidation = true
        private val myCalendar: Calendar = Calendar.getInstance()

        fun handleFullTripFilter(context: Context, collection: Collection,fullTripParams:(data: FullTripFilterCollection) -> Unit) {
            val binding = CustomFilterFullTripBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            var prices = mutableListOf(0F, 20000F)
            setupCities(binding, context, collection)
            setupHotels(binding, context, collection)

            fProgressDialog = Dialog(context)
            fProgressDialog.setContentView(binding.root)
            fProgressDialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fProgressDialog.setCancelable(false)
            fProgressDialog.show()

            //update price Slider Currency
            binding.slider.setLabelFormatter { value: Float ->
                val format = NumberFormat.getCurrencyInstance()
                format.maximumFractionDigits = 0
                format.currency = Currency.getInstance("EGP")
                format.format(value.toDouble())
            }
            //date
            val format = SimpleDateFormat(context.resources.getString(R.string.DATE_FORM), Locale.US)

            binding.Submit.setOnClickListener {
                val wantedCity = collection.city.data.citiesList.find { "${it.name} (${it.country.iso3})" == binding.City.editText?.text.toString().trim() }

                val wantedHotel = collection.hotelName.data.hotelsList.find { it.name == binding.HotelName.editText?.text.toString().trim() }

                if (binding.City.editText?.text.toString().isNotEmpty()) { if (wantedCity == null) { cityValidation = false
                    binding.City.editText?.error = context.resources.getString(R.string.FROM_LIST) } } else { cityValidation = true }


                if (binding.HotelName.editText?.text.toString().isNotEmpty()) { if (wantedHotel == null) { hotelValidation = false
                    binding.HotelName.editText?.error = context.resources.getString(R.string.FROM_LIST) } } else { hotelValidation = true }

                val priceF = binding.PriceF.editText?.text.toString().trim().toIntOrNull()
                val priceT = binding.PriceT.editText?.text.toString().trim().toIntOrNull()

                val dateF = binding.DateF.editText?.text.toString()
                val dateT = binding.DateT.editText?.text.toString()
                if(cityValidation && hotelValidation ){
                   FullTripFilterCollection(wantedHotel?.id?.toString() ?: "",wantedCity?.id?.toString() ?: "" ,priceF?.toString() ?: "" , priceT?.toString() ?: "" ,dateF, dateT)
                }

            }
            binding.DateF.editText?.setOnClickListener {
                val currentDate = DatePickerDialog(context, updateDateLabel(binding.DateF.editText, format), myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH])
                currentDate.datePicker.minDate = System.currentTimeMillis() - 1000
                currentDate.show()
            }
            binding.DateT.editText?.setOnClickListener {
                val currentDate = DatePickerDialog(context, updateDateLabel(binding.DateT.editText, format), myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH])
                currentDate.datePicker.minDate = System.currentTimeMillis() - 1000
                currentDate.show()
            }
            binding.close.setOnClickListener {
                fProgressDialog.dismiss()
            }
            binding.slider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    val values = slider.values
                    binding.PriceF.editText?.setText(values[0].toInt().toString())
                    binding.PriceT.editText?.setText(values[1].toInt().toString())
                }

            })
            binding.PriceF.editText?.doOnTextChanged { text, _, _, _ ->
                if (text != null && text.isNotEmpty()) {
                    prices = HotelFilterPopUp.handleFirstValue(prices, text)
                    handleSliders(binding, prices)
                }
            }
            binding.PriceF.editText?.doOnTextChanged { text, _, _, _ ->
                if (text != null && text.isNotEmpty()) {
                    prices = HotelFilterPopUp.handleSecondValue(prices, text)
                    handleSliders(binding, prices)
                }
            }

        }

        private fun handleSliders(binding: CustomFilterFullTripBinding, prices: MutableList<Float>) {
            binding.slider.setValues(prices[0], prices[1])
        }

        private fun setupHotels(binding: CustomFilterFullTripBinding, context: Context, collection: Collection) {
            val x = mutableListOf<String>()
            collection.hotelName.data.hotelsList.map { x.add(it.name) }
            val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, x)
            (binding.HotelName.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (binding.HotelName.editText as? AutoCompleteTextView)?.threshold = 1
        }

        private fun setupCities(binding: CustomFilterFullTripBinding, context: Context, collection: Collection) {
            val x = mutableListOf<String>()
            collection.city.data.citiesList.map {
                x.add("${it.name} (${it.country.iso3})")
            }
            val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, x)
            (binding.City.editText as? AutoCompleteTextView)?.setAdapter(adapter)
            (binding.City.editText as? AutoCompleteTextView)?.threshold = 1
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