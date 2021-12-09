package com.example.bezo.view.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.bezo.R
import com.example.bezo.databinding.ActivityDashBoardBinding

class DashBoardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDashBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board)
    }
}