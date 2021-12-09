package com.example.bezo.view.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import com.example.bezo.R
import com.example.bezo.databinding.CustomFilterHotelBinding
import com.example.bezo.model.data.Collection
import com.example.bezo.model.data.HotelFilterCollection
import com.google.android.material.slider.RangeSlider
import java.text.NumberFormat
import java.util.*

class HotelFilterPopUp {
companion object {
    private lateinit var hProgressDialog: Dialog
    private var cityValidation = true
    private var mealValidation = true
    private var hotelValidation = true

    fun handleHotelFilter(context: Context, collection: Collection,hotelParams:(data:HotelFilterCollection) -> Unit) {
        val hotelBinding = CustomFilterHotelBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        var prices = mutableListOf(0F, 20000F)
        setupCities(hotelBinding, context, collection)
        setupMeals(hotelBinding, context, collection)
        setupHotels(hotelBinding, context, collection)
        hProgressDialog = Dialog(context)
        hProgressDialog.setContentView(hotelBinding.root)
        hProgressDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        hProgressDialog.setCancelable(false)
        hProgressDialog.show()

        //update price Slider Currency
        hotelBinding.slider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("EGP")
            format.format(value.toDouble())
        }
        hotelBinding.submit.setOnClickListener {
            val wantedCity = collection.city.data.citiesList.find {
                "${it.name} (${it.country.iso3})" == hotelBinding.City.editText?.text.toString().trim()
            }
            val wantedMeal = collection.meals.data.MealsList.find {
                it.name == hotelBinding.Meal.editText?.text.toString().trim()
            }
            val wantedHotel = collection.hotelName.data.hotelsList.find {
                it.name == hotelBinding.HotelName.editText?.text.toString().trim()
            }

            if (hotelBinding.City.editText?.text.toString().trim().isNotEmpty()) { if (wantedCity == null) { cityValidation = false
                    hotelBinding.City.editText?.error = context.resources.getString(R.string.FROM_LIST) } else { cityValidation = true }  }

            if (hotelBinding.Meal.editText?.text.toString().trim().isNotEmpty()) { if (wantedMeal == null) { mealValidation = false
                hotelBinding.Meal.editText?.error = context.resources.getString(R.string.FROM_LIST) } else { mealValidation = true } }

            if (hotelBinding.HotelName.editText?.text.toString().trim().isNotEmpty()) { if (wantedHotel == null) { hotelValidation = false
                    hotelBinding.HotelName.editText?.error = context.resources.getString(R.string.FROM_LIST) } else { hotelValidation = true }  }

            var rating = hotelBinding.rBar.rating.toString()
            val priceF = hotelBinding.priceF.editText?.text.toString().trim().toIntOrNull()
            val priceT = hotelBinding.priceT.editText?.text.toString().trim().toIntOrNull()
            val perPerson = hotelBinding.id.editText?.text.toString().trim().toIntOrNull()

             rating =  if(rating == "0.0"){""}else{ rating }
            val perPersonText = if(perPerson == 0 || perPerson == null){""}else{perPerson.toString()}


            if(cityValidation && mealValidation && hotelValidation){
                HotelFilterCollection(perPersonText,wantedHotel?.id?.toString() ?: "",wantedCity?.id?.toString() ?: "",wantedMeal?.id?.toString() ?: "",rating,priceF?.toString() ?: "",priceT?.toString() ?: "")
            }

            // hProgressDialog.dismiss()
        }
        hotelBinding.reset.setOnClickListener {
            hotelBinding.rBar.rating = 0F
        }
        hotelBinding.close.setOnClickListener {
            hProgressDialog.dismiss()
        }
        hotelBinding.addButton.setOnClickListener {
            onPlusPress(hotelBinding)
        }
        hotelBinding.minusButton.setOnClickListener {
            onMinusPress(hotelBinding)
        }
        hotelBinding.slider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                val values = slider.values
                hotelBinding.priceF.editText?.setText(values[0].toInt().toString())
                hotelBinding.priceT.editText?.setText(values[1].toInt().toString())
            }

        })
        hotelBinding.priceF.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null && text.isNotEmpty()) {
                prices = handleFirstValue(prices, text)
                handleSliders(hotelBinding, prices)
            }
        }
        hotelBinding.priceT.editText?.doOnTextChanged { text, _, _, _ ->
            if (text != null && text.isNotEmpty()) {
                prices = handleSecondValue(prices, text)
                handleSliders(hotelBinding, prices)
            }
        }

    }

    fun handleFirstValue(prices: MutableList<Float>, text: CharSequence): MutableList<Float> {
        if (text.toString().isEmpty() || !text.toString()
                .matches("-?\\d+(\\.\\d+)?".toRegex()) || text.toString().toFloat() < 0
        ) {
            prices[0] = 0F
        } else {
            prices[0] = text.toString().toFloat()
        }
        return prices
    }

    fun handleSecondValue(prices: MutableList<Float>, text: CharSequence): MutableList<Float> {
        if (text.toString().isEmpty() || !text.toString()
                .matches("-?\\d+(\\.\\d+)?".toRegex()) || text.toString().toFloat() < 0
        ) {
            prices[1] = 20000F
        } else {

            prices[1] = text.toString().toFloat()
        }
        return prices
    }

    private fun handleSliders(binding: CustomFilterHotelBinding, prices: MutableList<Float>) {
        binding.slider.setValues(prices[0], prices[1])
    }

    private fun setupCities(binding: CustomFilterHotelBinding, context: Context, collection: Collection) {
        val x = mutableListOf<String>()
        collection.city.data.citiesList.map {
            x.add("${it.name} (${it.country.iso3})")
        }
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, x)
        (binding.City.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.City.editText as? AutoCompleteTextView)?.threshold = 1
    }

    private fun setupMeals(binding: CustomFilterHotelBinding, context: Context, collection: Collection) {
        val x = mutableListOf<String>()
        collection.meals.data.MealsList.map { x.add(it.name) }
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, x)
        (binding.Meal.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.Meal.editText as? AutoCompleteTextView)?.threshold = 1
    }

    private fun setupHotels(binding: CustomFilterHotelBinding, context: Context, collection: Collection) {
        val x = mutableListOf<String>()
        collection.hotelName.data.hotelsList.map { x.add(it.name) }
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, x)
        (binding.HotelName.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.HotelName.editText as? AutoCompleteTextView)?.threshold = 1
    }

    private fun onPlusPress(binding: CustomFilterHotelBinding) {
        val numberText = binding.id.editText?.text.toString()
        var number: Int =
            if (numberText.isEmpty() || !numberText.matches("-?\\d+(\\.\\d+)?".toRegex()) || numberText.toInt() < 0) {
                0
            } else {
                numberText.toInt()
            }
        number++
        binding.id.editText?.setText(number.toString())
    }

    private fun onMinusPress(binding: CustomFilterHotelBinding) {
        val numberText = binding.id.editText?.text.toString()
        var number: Int =
            if (numberText.isEmpty() || !numberText.matches("-?\\d+(\\.\\d+)?".toRegex()) || numberText.toInt() < 0) {
                0
            } else {
                numberText.toInt()
            }
        if (number > 0) {
            number--
        }
        binding.id.editText?.setText(number.toString())
    }
}
}