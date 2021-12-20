package com.example.travelio.view.util

import android.content.Context
import com.example.travelio.R
import com.example.travelio.model.preference.Local
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ChangeLanguagePopUp {
    companion object{
        fun changeLanguage(context: Context,oldSelectedLanguage:Int?,selected:(lang:String)->Unit){
            var selectedID:Int = oldSelectedLanguage ?: 0
            MaterialAlertDialogBuilder(context)
                .setTitle(context.resources.getString(R.string.change_language))
                .setNeutralButton(context.resources.getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(context.resources.getString(R.string.ok)) { dialog, _ ->
                    if (selectedID == 0){selected(context.resources.getString(R.string.ENGLISH))}else{selected(context.resources.getString(R.string.Arabic))}
                    dialog.dismiss()
                }
                .setSingleChoiceItems(R.array.Lang, selectedID) { _, which ->
                    selectedID =  which
                }
                .show()
        }

        fun checkIfItCurrentLanguage(lang: String,isCurrentLang:(isCurrent:Boolean)->Unit){
            val currentLanguage = Local.getLanguage()
            if (currentLanguage == null){
                if (lang == Local.getDeviceLanguage()){
                    isCurrentLang(true)
                }else{
                    isCurrentLang(false)
                }
            }else{
                if (currentLanguage == lang){
                    isCurrentLang(true)
                }else{
                    isCurrentLang(false)
                }
            }
        }
        fun returnLanguageID(context:Context):Int?{
            val currentLanguage = Local.getLanguage()
            return if (currentLanguage == null){
                null
            }else{
                if (currentLanguage == context.resources.getString(R.string.Arabic)){
                    1
                }else{
                    0
                }
            }
        }
    }
}