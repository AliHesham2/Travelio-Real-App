package com.example.travelio.view.dashboard.orders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.travelio.R
import com.example.travelio.databinding.ActivityOrderBinding
import com.example.travelio.requests.fcm.UpdateToken
import com.example.travelio.view.dashboard.DashBoardActivity


class OrderActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOrderBinding
    private var bundle:Bundle? = null
    private var foreground:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_order)
        bundle = intent.extras
        val navController = findNavController(R.id.nav_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        if (bundle != null){
            binding.bottomNavigation.selectedItemId = UpdateToken.setDestination(bundle!!.get(this.resources.getString(R.string.TYPE_NAVIGATION)) as Int)
            foreground = bundle!!.get(this.resources.getString(R.string.Current_NAVIGATION)) as String?
        }
        binding.bottomNavigation.setOnItemReselectedListener { return@setOnItemReselectedListener }
        binding.topAppBar.setNavigationOnClickListener {  if (bundle != null){ if (foreground != null){ finish() }else{ handleBackButtonFromFcm() }
        }else{
            finish()
        } }
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
        if (bundle != null){
            if (foreground != null){
                finish()
            }else{
                handleBackButtonFromFcm()
            }
        }else{
           finish()
        }
    }
    private fun handleBackButtonFromFcm(){
        startActivity(Intent(this@OrderActivity, DashBoardActivity::class.java))
        finish()
    }

}