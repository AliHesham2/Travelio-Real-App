package com.example.bezo.view.util

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bezo.R
import com.example.bezo.view.registration.RegistrationActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class PopUpMsg {

    companion object{

        const val BASE_URL = "https://hatours.herokuapp.com"

        private lateinit var  mProgressDialog : Dialog

        fun alertMsg(view: View, msg: String){
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).also { snackBar ->
                snackBar.setBackgroundTint(ContextCompat.getColor(view.context, R.color.red))
            }.show()
        }

        fun toastMsg(context: Context, msg:String){
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
        }

        fun showLoginAgainDialogue(currentFragment: Fragment) {
            MaterialAlertDialogBuilder(currentFragment.requireContext())
                .setTitle(currentFragment.resources.getString(R.string.title))
                .setMessage(currentFragment.resources.getString(R.string.supporting_text))
                .setPositiveButton(currentFragment.resources.getString(R.string.cancel)) { dialog, _ ->
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