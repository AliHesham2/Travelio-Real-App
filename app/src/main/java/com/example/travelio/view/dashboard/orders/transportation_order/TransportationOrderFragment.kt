package com.example.travelio.view.dashboard.orders.transportation_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travelio.R
import com.example.travelio.databinding.FragmentTransportationOrder2Binding
import com.google.android.material.tabs.TabLayoutMediator


class TransportationOrderFragment : Fragment() {
    private lateinit var binding : FragmentTransportationOrder2Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTransportationOrder2Binding.inflate(inflater)
            val adapter = TabAdapter(childFragmentManager, lifecycle)
            binding.pager2.adapter = adapter
            binding.tabLayout.clearOnTabSelectedListeners()
            TabLayoutMediator(binding.tabLayout, binding.pager2) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = resources.getString(R.string.RECENT)
                    }
                    1 -> {
                        tab.text = resources.getString(R.string.EXPIRED)
                    }
                }
            }.attach()
            return binding.root
        }
    object Instance{val instance : TransportationOrderFragment by lazy { TransportationOrderFragment() } }
}