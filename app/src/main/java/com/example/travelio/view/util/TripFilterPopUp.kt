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
import com.example.travelio.databinding.CustomFilterTripBinding
import com.example.travelio.model.data.Collection
import com.example.travelio.model.data.TripFilterCollection
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class TripFilterPopUp {
    companion object{
        private lateinit var tProgressDialog: Dialog
        private var cityValidation = true
        private var locationValidation = true
        private var priceValidation = true
        private var prices = mutableListOf(0F, 20000F)
        private val myCalendar: Calendar = Calendar.getInstance()

        fun handleTripFilter(context: Context, collection: Collection,oldFilter:TripFilterCollection?,tripParams:(data: TripFilterCollection) -> Unit){
            val tripBinding = CustomFilterTripBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            if(oldFilter != null){ displayOldFilter(tripBinding,oldFilter,context) }
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
                format.currency = Currency.getInstance(context.resources.getString(R.string.Currency))
                format.format(value.toDouble())
            }

            //date
            val format = SimpleDateFormat(context.resources.getString(R.string.DATE_FORM), Locale.US)

            tripBinding.submit.setOnClickListener {
                val wantedCity = collection.city.data.citiesList.find { "${it.name} (${it.country.iso3})" == tripBinding.City.editText?.text.toString().trim() }
                val wantedLocations = collection.location.data.TripLocationsList.find { it.name == tripBinding.location.editText?.text.toString().trim() }

                if (tripBinding.City.editText?.text.toString().isNotEmpty()) {
                    if (wantedCity == null) { cityValidation = false
                        tripBinding.City.editText?.error = context.resources.getString(R.string.FROM_LIST) } else { cityValidation = true } }

                if (tripBinding.location.editText?.text.toString().isNotEmpty()) { if (wantedLocations == null) { locationValidation = false
                    tripBinding.location.editText?.error = context.resources.getString(R.string.FROM_LIST) } else { locationValidation = true } }

                val priceF = tripBinding.priceF.editText?.text.toString().trim().toIntOrNull()
                val priceT = tripBinding.priceT.editText?.text.toString().trim().toIntOrNull()

                priceValidation = !(priceF == null || priceT == null || priceF > priceT || priceF < 0 || priceT < 0 || priceT > 20000 || priceF > 20000)
                if(!priceValidation){PopUpMsg.toastMsg(context,context.resources.getString(R.string.CHECK_PRICE))}

                val dateF = tripBinding.dateF.editText?.text?.toString() ?: context.resources.getString(R.string.EMPTY)
                val dateT = tripBinding.dateT.editText?.text?.toString() ?: context.resources.getString(R.string.EMPTY)
                if(cityValidation && locationValidation && priceValidation ){
                    tripParams(TripFilterCollection(
                        wantedCity?.id?.toString() ?: context.resources.getString(R.string.EMPTY),  wantedCity?.name ?: context.resources.getString(R.string.EMPTY) ,  wantedCity?.country?.iso3 ?:context.resources.getString(R.string.EMPTY) ,
                        wantedLocations?.id?.toString() ?: context.resources.getString(R.string.EMPTY) ,  wantedLocations?.name ?: context.resources.getString(R.string.EMPTY),
                        priceF?.toString() ?: context.resources.getString(R.string.EMPTY) ,
                        priceT?.toString() ?: context.resources.getString(R.string.EMPTY) ,
                        dateF, dateT))
                    tProgressDialog.hide()
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
            tripBinding.resetAll.setOnClickListener {
               resetAll(tripBinding,context)
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
                if (text != null && text.isNotEmpty() && text.toString().matches("-?\\d+(\\.\\d+)?".toRegex()) && text.toString().toFloat() > 0 && text.toString().toInt() <= 20000 ) {
                    prices[0] = text.toString().toFloat()
                }else{
                    prices[0] = 0F
                }
                handleSliders(tripBinding, prices)
            }
            tripBinding.priceT.editText?.doOnTextChanged { text, _, _, _ ->
                 if (text != null && text.isNotEmpty() && text.toString().matches("-?\\d+(\\.\\d+)?".toRegex()) && text.toString().toFloat() > 0 && text.toString().toInt() <= 20000 ) {
                     prices[1] =  text.toString().toFloat()
                }else{
                     prices[1] = 20000F
                }
                handleSliders(tripBinding, prices)
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
        private fun resetAll(binding:CustomFilterTripBinding, context: Context) {
            binding.City.editText?.text?.clear()
            binding.location.editText?.text?.clear()
            binding.priceF.editText?.text?.clear()
            binding.priceT.editText?.text?.clear()
            binding.dateF.editText?.text?.clear()
            binding.dateT.editText?.text?.clear()
            binding.slider.setValues(context.resources.getString(R.string.DEFAULT_VALUE).toFloat(), context.resources.getString(R.string.MAX_PRICE).toFloat())
            cityValidation = true
            locationValidation = true
        }
        private fun displayOldFilter(binding: CustomFilterTripBinding, oldFilter: TripFilterCollection, context: Context) {
            val city = if (oldFilter.cityName.isNullOrEmpty()){context.resources.getString(R.string.EMPTY)}else{ "${oldFilter.cityName} (${oldFilter.countryName})"}
            binding.City.editText?.setText(city)
            binding.location.editText?.setText(oldFilter.locationName)
            binding.priceF.editText?.setText(oldFilter.minPrice)
            binding.priceT.editText?.setText(oldFilter.maxPrice)
            binding.dateF.editText?.setText(oldFilter.fromDate)
            binding.dateT.editText?.setText(oldFilter.toDate)
        }
    }

}