package com.example.travelio.view.splash

import android.bluetooth.BluetoothClass
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import com.example.travelio.R
import com.example.travelio.model.preference.DeviceToken
import com.example.travelio.model.preference.Token
import com.example.travelio.view.dashboard.DashBoardActivity
import com.example.travelio.view.registration.RegistrationActivity
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Token.initialize(this.applicationContext)
        DeviceToken.initialize(this.applicationContext)
        val video = findViewById<VideoView>(R.id.videos)
        val videoPath = StringBuilder("android.resource://").append(packageName).append("/raw/video").toString()
        video.setVideoPath(videoPath)
        video.start()
        video.setOnCompletionListener {
            if(Token.getToken() != null){
                startActivity(Intent(this@MainActivity, DashBoardActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@MainActivity, RegistrationActivity::class.java))
                finish()
            }
        }
    }
}