package com.example.bezo.view.dashboard.transportation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.databinding.ActivityTransportationBinding

class TransportationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTransportationBinding
    private lateinit var viewModel: TransportationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialization
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transportation)
        val application = this.application
        val viewModelFactory = TransportationViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(TransportationViewModel::class.java)
        binding.lifecycleOwner = this

    }
}