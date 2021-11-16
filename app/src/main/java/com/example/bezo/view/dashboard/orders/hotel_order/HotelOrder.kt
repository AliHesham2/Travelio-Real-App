package com.example.bezo.view.dashboard.orders.hotel_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.databinding.FragmentHotelOrderBinding

class HotelOrder : Fragment() {
    private lateinit var binding: FragmentHotelOrderBinding
    private lateinit var viewModel: HotelOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialization
        binding = FragmentHotelOrderBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = HotelOrderViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(HotelOrderViewModel::class.java)
        binding.lifecycleOwner = this

        return binding.root
    }
}