package com.example.bezo.view.user.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.databinding.ActivityEditProfileBinding


class EditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialization
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile)
        val application = requireNotNull(this).application
        val viewModelFactory = EditProfileViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(EditProfileViewModel::class.java)
        binding.lifecycleOwner = this


    }
}