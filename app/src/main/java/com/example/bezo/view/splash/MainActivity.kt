package com.example.bezo.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.widget.VideoView
import com.example.bezo.R
import com.example.bezo.model.preference.Token
import com.example.bezo.view.dashboard.DashBoardActivity
import com.example.bezo.view.registration.RegistrationActivity
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Token.initialize(this.applicationContext)
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