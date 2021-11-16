package com.example.bezo.view.dashboard.trips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.databinding.ActivityTripsBinding


class TripsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTripsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialization
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trips)
    }
}