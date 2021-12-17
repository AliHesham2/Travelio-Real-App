package com.example.travelio.view.dashboard.orders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.travelio.R
import com.example.travelio.databinding.ActivityOrderBinding


class OrderActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order)
        val navController = findNavController(R.id.nav_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemReselectedListener { return@setOnItemReselectedListener }
        binding.topAppBar.setNavigationOnClickListener { finish() }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.hotelOrderData -> show()
                R.id.transportationOrderFragmentData -> show()
                R.id.tripOrderFragmentData -> show()
                R.id.fullTripOrderFragmentData -> show()
                else -> hide()
            }
        }
    }
    private fun show() {
        binding.bottomNavigation.visibility = View.VISIBLE
        binding.appBarLayout.visibility = View.VISIBLE
    }

    private fun hide() {
        binding.bottomNavigation.visibility = View.GONE
        binding.appBarLayout.visibility = View.GONE
    }
    override fun onBackPressed() {
        super.onBackPressed()
        binding.bottomNavigation.selectedItemId = R.id.s1
    }


}