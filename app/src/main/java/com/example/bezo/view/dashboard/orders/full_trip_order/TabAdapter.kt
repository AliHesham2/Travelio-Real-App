package com.example.bezo.view.dashboard.orders.full_trip_order

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter
    (fragmentManager,lifecycle){

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return    when (position) {
            0 -> {
                FullTripOrder.Instance.instance
            }
            1 -> {
                FullTripExpiredOrder.Instance.instance
            }
            else -> Fragment()
        }
    }


}