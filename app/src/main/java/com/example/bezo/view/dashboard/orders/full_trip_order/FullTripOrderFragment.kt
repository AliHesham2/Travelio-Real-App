package com.example.bezo.view.dashboard.orders.full_trip_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bezo.R
import com.example.bezo.databinding.FragmentFullTripOrder2Binding
import com.google.android.material.tabs.TabLayoutMediator

class FullTripOrderFragment : Fragment() {
    private lateinit var binding : FragmentFullTripOrder2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentFullTripOrder2Binding.inflate(inflater)
        val adapter = TabAdapter(childFragmentManager,lifecycle)
        binding.pager2.adapter = adapter
        binding.tabLayout.clearOnTabSelectedListeners()
        TabLayoutMediator(binding.tabLayout,binding.pager2){ tab,position ->
            when(position){
                0->{
                    tab.text= resources.getString(R.string.RECENT)
                }
                1->{
                    tab.text= resources.getString(R.string.EXPIRED)
                }
            }
        }.attach()
        return binding.root
    }
    object Instance{val instance : FullTripOrderFragment by lazy { FullTripOrderFragment() } }
}