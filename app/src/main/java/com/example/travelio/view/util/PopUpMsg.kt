package com.example.travelio.view.util

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.travelio.R
import com.example.travelio.view.registration.RegistrationActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class PopUpMsg {

    companion object{
        const val BASE_URL_FCM = "https://fcm.googleapis.com/"
        const val KEY = "AAAAT7QyuUM:APA91bGHf27UAoC2LjQU3AJpmsM_0AoKTXGjCb8j9CdlNRJX0xXl6vhMoPZ00Y1_DT6cL4levhjB4YTsqAQgX51auYqP1wtWY73emAr1Ikse0sqKNvR389bSbmG3p8lE1qDI0pKpXKkC"
        const val BASE_URL = "https://traveliofront.herokuapp.com"
        const val NOTIFICATION_ID = 0
        private lateinit var  mProgressDialog : Dialog

        fun handleError(context: Context, e: Exception) {
            if (e is IOException) {
                toastMsg(context,context.resources.getString(R.string.NO_INTERNET))
            } else {
                toastMsg(context,context.resources.getString(R.string.WRONG))
            }
        }

        fun alertMsg(view: View, msg: String){
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).also { snackBar ->
                snackBar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.red))
            }.show()
        }

        fun toastMsg(context: Context, msg:String){
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
        }

        fun deleteAlertDialogue(context: Context,resources: Resources,isOk:(remove:Boolean) -> Unit){
            MaterialAlertDialogBuilder(context)
                .setMessage(resources.getString(R.string.MSG))
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                    isOk(false)
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.submit)) { dialog, _ ->
                    isOk(true)
                    dialog.dismiss()
                }
                .show()
        }

        fun showLoginAgainDialogue(currentFragment: Fragment) {
            MaterialAlertDialogBuilder(currentFragment.requireContext())
                .setTitle(currentFragment.resources.getString(R.string.title))
                .setMessage(currentFragment.resources.getString(R.string.supporting_text))
                .setCancelable(false)
                .setPositiveButton(currentFragment.resources.getString(R.string.submit)) { dialog, _ ->
                    val intent = Intent(currentFragment.context, RegistrationActivity::class.java)
                    currentFragment.startActivity(intent)
                    currentFragment.requireActivity().finish()
                    dialog.dismiss()
                }
                .show()
        }

        fun showDialogue(context: Context){
            mProgressDialog = Dialog(context)
            mProgressDialog.setContentView(R.layout.loading)
            mProgressDialog.setCancelable(false)
            mProgressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mProgressDialog.show()
        }

        fun hideDialogue(){
            mProgressDialog.dismiss()
        }

    }
}