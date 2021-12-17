package com.example.travelio.view.user.edit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelio.model.data.Collection
import com.example.travelio.model.data.UserData

class EditProfileViewModelFactory(
    private val application: Application,
    private val userData: UserData,
    private val collection: Collection
): ViewModelProvider.Factory {
    @Suppress(("unchecked_cast"))
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(application,userData,collection) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}