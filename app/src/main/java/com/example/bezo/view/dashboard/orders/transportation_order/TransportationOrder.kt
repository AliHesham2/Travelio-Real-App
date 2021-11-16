package com.example.bezo.view.dashboard.orders.transportation_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.databinding.FragmentTransportationOrderBinding



class TransportationOrder : Fragment() {
    private lateinit var binding: FragmentTransportationOrderBinding
    private lateinit var viewModel: TransportationOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialization
        binding = FragmentTransportationOrderBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = TransportationOrderViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(TransportationOrderViewModel::class.java)
        binding.lifecycleOwner = this

        return binding.root
    }


}