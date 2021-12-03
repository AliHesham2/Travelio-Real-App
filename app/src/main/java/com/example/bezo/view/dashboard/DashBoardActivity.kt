package com.example.bezo.view.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.bezo.R
import com.example.bezo.databinding.ActivityDashBoardBinding
import com.example.bezo.model.data.UserData
import com.example.bezo.view.dashboard.fulltrip.FullTripActivity
import com.example.bezo.view.dashboard.hotel.HotelActivity
import com.example.bezo.view.dashboard.orders.OrderActivity
import com.example.bezo.view.dashboard.transportation.TransportationActivity
import com.example.bezo.view.dashboard.trips.TripsActivity

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDashBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board)
        val userData = intent.extras?.getParcelable<UserData>(this.resources.getString(R.string.USER))

        //OverFlowMenu
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.changePassword -> {
                    true
                }
                R.id.editProfile ->{
                    true
                }
                else -> false
            }
        }
            //Drawer Setting
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Trans ->{
                    binding.drawerLayout.close()
                    startActivity(Intent(this@DashBoardActivity, TransportationActivity::class.java))
                    true
                }
                R.id.hotel ->{
                    binding.drawerLayout.close()
                    startActivity(Intent(this@DashBoardActivity, HotelActivity::class.java))
                    true
                }
                R.id.trip ->{
                    binding.drawerLayout.close()
                    startActivity(Intent(this@DashBoardActivity, TripsActivity::class.java))
                    true
                }
                R.id.fullTrip ->{
                    binding.drawerLayout.close()
                    startActivity(Intent(this@DashBoardActivity, FullTripActivity::class.java))
                    true
                }
                R.id.order ->{
                    binding.drawerLayout.close()
                    startActivity(Intent(this@DashBoardActivity, OrderActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}