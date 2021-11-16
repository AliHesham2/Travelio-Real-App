package com.example.bezo.view.dashboard.hotel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.bezo.R
import com.example.bezo.databinding.ActivityHotelBinding


class HotelActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHotelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initialization
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hotel)
    }
}