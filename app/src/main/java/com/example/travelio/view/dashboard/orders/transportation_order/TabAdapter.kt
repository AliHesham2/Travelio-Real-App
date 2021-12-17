package com.example.travelio.view.dashboard.orders.transportation_order


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter ( fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter
    (fragmentManager,lifecycle){

    override fun getItemCount(): Int {
        return 2
    }


    override fun createFragment(position: Int): Fragment {
            when (position) {
              0 -> {
                  return  TransportationOrder.Instance.instance
              }
              1 -> {
                  return TransportationExpiredOrder.Instance.instance
              }
        }
        return   Fragment()
    }


}