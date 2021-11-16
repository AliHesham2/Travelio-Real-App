package com.example.bezo.view.dashboard.fulltrip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.databinding.ActivityFullTripBinding


class FullTripActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFullTripBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialization
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_trip)

    }
}