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
import com.example.travelio.databinding.CustomFilterFullTripBinding
import com.example.travelio.model.data.Collection
import com.example.travelio.model.data.FullTripFilterCollection
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class FullTripFilterPopUp {
    companion object{
        private lateinit var fProgressDialog: Dialog
        private var cityValidation = true
        private var hotelValidation = true
        private var priceValidation = true
        private var prices = mutableListOf(0F, 20000F)
        private val myCalendar: Calendar = Calendar.getInstance()

        fun handleFullTripFilter(context: Context, collection: Collection,oldFilter:FullTripFilterCollection?,fullTripParams:(data: FullTripFilterCollection) -> Unit) {
            val binding = CustomFilterFullTripBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            if(oldFilter != null) { displayOldFilter(binding, oldFilter,context) }
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
                format.currency = Currency.getInstance(context.resources.getString(R.string.Currency))
                format.format(value.toDouble())
            }
            //date
            val format = SimpleDateFormat(context.resources.getString(R.string.DATE_FORM), Locale.US)

            binding.Submit.setOnClickListener {
                val wantedCity = collection.city.data.citiesList.find { "${it.name} (${it.country.iso3})" == binding.City.editText?.text.toString().trim() }

                val wantedHotel = collection.hotelName.data.hotelsList.find { it.name == binding.HotelName.editText?.text.toString().trim() }

                if (binding.City.editText?.text.toString().isNotEmpty()) { if (wantedCity == null) { cityValidation = false
                    binding.City.editText?.error = context.resources.getString(R.string.FROM_LIST) } else { cityValidation = true} }


                if (binding.HotelName.editText?.text.toString().isNotEmpty()) { if (wantedHotel == null) { hotelValidation = false
                    binding.HotelName.editText?.error = context.resources.getString(R.string.FROM_LIST) } else { hotelValidation = true }  }

                val priceF = binding.PriceF.editText?.text.toString().trim().toIntOrNull()
                val priceT = binding.PriceT.editText?.text.toString().trim().toIntOrNull()

                priceValidation = !(priceF == null || priceT == null || priceF > priceT || priceF < 0 || priceT < 0 || priceT > 20000 || priceF > 20000)
                if(!priceValidation){PopUpMsg.toastMsg(context,context.resources.getString(R.string.CHECK_PRICE))}

                val dateF = binding.DateF.editText?.text.toString()
                val dateT = binding.DateT.editText?.text.toString()
                if(cityValidation && hotelValidation && priceValidation){
                  fullTripParams(FullTripFilterCollection(
                      wantedHotel?.id?.toString() ?: context.resources.getString(R.string.EMPTY), wantedHotel?.name ?:context.resources.getString(R.string.EMPTY) ,
                      wantedCity?.id?.toString() ?: context.resources.getString(R.string.EMPTY) ,   wantedCity?.name ?: context.resources.getString(R.string.EMPTY), wantedCity?.country?.iso3?: context.resources.getString(R.string.EMPTY),
                      priceF?.toString() ?: context.resources.getString(R.string.EMPTY) ,
                      priceT?.toString() ?: context.resources.getString(R.string.EMPTY) ,
                      dateF, dateT))
                    fProgressDialog.hide()
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
            binding.resetAll.setOnClickListener {
                resetAll(binding,context)
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
                if (text != null && text.isNotEmpty()&& text.toString().matches("-?\\d+(\\.\\d+)?".toRegex()) && text.toString().toFloat() > 0 && text.toString().toInt() <= 20000 ) {
                    prices[0] =  text.toString().toFloat()
                }else{
                    prices[0] = 0F
                }
                handleSliders(binding, prices)
            }
            binding.PriceT.editText?.doOnTextChanged { text, _, _, _ ->
                 if (text != null && text.isNotEmpty()&& text.toString().matches("-?\\d+(\\.\\d+)?".toRegex()) && text.toString().toFloat() > 0 && text.toString().toInt() <= 20000 ) {
                     prices[1] = text.toString().toFloat()
                }else{
                     prices[1] = 20000F
                }
                handleSliders(binding, prices)
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
        private fun resetAll(binding:CustomFilterFullTripBinding, context: Context) {
            binding.HotelName.editText?.text?.clear()
            binding.City.editText?.text?.clear()
            binding.PriceF.editText?.text?.clear()
            binding.PriceT.editText?.text?.clear()
            binding.DateF.editText?.text?.clear()
            binding.DateT.editText?.text?.clear()
            binding.slider.setValues(context.resources.getString(R.string.DEFAULT_VALUE).toFloat(), context.resources.getString(R.string.MAX_PRICE).toFloat())
            cityValidation = true
            hotelValidation = true
        }
        private fun displayOldFilter(binding: CustomFilterFullTripBinding, oldFilter: FullTripFilterCollection, context: Context) {
            val city = if (oldFilter.cityName.isNullOrEmpty()){context.resources.getString(R.string.EMPTY)}else{ "${oldFilter.cityName} (${oldFilter.countryName})"}
            binding.HotelName.editText?.setText(oldFilter.hotelName)
            binding.City.editText?.setText(city)
            binding.PriceF.editText?.setText(oldFilter.minPrice)
            binding.PriceT.editText?.setText(oldFilter.maxPrice)
            binding.DateF.editText?.setText(oldFilter.fromDate)
            binding.DateT.editText?.setText(oldFilter.toDate)
        }
    }
}