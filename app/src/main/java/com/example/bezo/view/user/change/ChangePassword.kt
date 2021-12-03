package com.example.bezo.view.user.change

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.databinding.ActivityChangePasswordBinding


class ChangePassword : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var viewModel: ChangePasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialization
        binding = DataBindingUtil.setContentView(this,R.layout.activity_change_password)
        val application = requireNotNull(this).application
        val viewModelFactory = ChangePasswordViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ChangePasswordViewModel::class.java)
        binding.lifecycleOwner = this

    }

}