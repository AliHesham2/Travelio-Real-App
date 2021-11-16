package com.example.bezo.view.dashboard.orders.trips_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.databinding.FragmentTripOrderBinding



class TripOrder : Fragment() {
    private lateinit var binding: FragmentTripOrderBinding
    private lateinit var viewModel: TripOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialization
        binding = FragmentTripOrderBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = TripOrderViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(TripOrderViewModel::class.java)
        binding.lifecycleOwner = this

        return binding.root
    }
}