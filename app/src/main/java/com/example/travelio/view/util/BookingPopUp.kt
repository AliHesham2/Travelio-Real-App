package com.example.travelio.view.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageButton
import android.widget.TextView
import com.example.travelio.R
import com.google.android.material.textfield.TextInputLayout

class BookingPopUp {
    companion object{
        private lateinit var  nProgressDialog : Dialog
        fun handleBookingPopUp(context: Context,clickListener: (text: Int) -> Unit){
            nProgressDialog = Dialog(context)
            nProgressDialog.setContentView(R.layout.custom_booking)
            nProgressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            nProgressDialog.setCancelable(false)
            nProgressDialog.show()
            val ok = nProgressDialog.findViewById<TextView>(R.id.add)
            val cancel = nProgressDialog.findViewById<TextView>(R.id.cancel)
            val quantity = nProgressDialog.findViewById<TextInputLayout>(R.id.id)
            val add = nProgressDialog.findViewById<ImageButton>(R.id.addButton)
            val minus = nProgressDialog.findViewById<ImageButton>(R.id.minusButton)
            add.setOnClickListener {
                onPlusPress(quantity)
            }
            minus.setOnClickListener {
                onMinusPress(quantity)
            }
            cancel.setOnClickListener {
                hideDialogue()
            }
            ok.setOnClickListener {
                val quantityData = quantity.editText?.text.toString().trim()
                val quantityDataInt = if(quantityData.isEmpty()){0}else{quantityData.toInt()}
                if(quantityDataInt <= 0){
                    PopUpMsg.toastMsg(context,context.resources.getString(R.string.INVALID_NUMBER))
                }else{
                    nProgressDialog.hide()
                    clickListener(quantityDataInt)
                }
            }
        }


        private fun onPlusPress(quantity: TextInputLayout) {
            val numberText = quantity.editText?.text.toString()
            var number :Int = if(numberText.isEmpty() || !numberText.matches("-?\\d+(\\.\\d+)?".toRegex()) || numberText.toInt() < 0 ){
                0
            }else{
                numberText.toInt()
            }
            number++
            quantity.editText?.setText(number.toString())
        }

        private fun onMinusPress(quantity: TextInputLayout) {
            val numberText = quantity.editText?.text.toString()
            var number :Int = if(numberText.isEmpty() || !numberText.matches("-?\\d+(\\.\\d+)?".toRegex()) || numberText.toInt() < 0){
                0
            }else{
                numberText.toInt()
            }
            if(number > 0){
                number--
            }
            quantity.editText?.setText(number.toString())
        }

        private fun hideDialogue(){
            nProgressDialog.hide()
        }
    }
}