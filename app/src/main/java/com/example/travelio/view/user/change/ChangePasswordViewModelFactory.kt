package com.example.travelio.view.user.change

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelio.model.data.UserData

class ChangePasswordViewModelFactory(private val application: Application,private val userData: UserData): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            return ChangePasswordViewModel(application,userData) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}