package com.example.travelio.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.VideoView
import com.example.travelio.R
import com.example.travelio.model.preference.DeviceToken
import com.example.travelio.model.preference.Local
import com.example.travelio.model.preference.Token
import com.example.travelio.view.dashboard.DashBoardActivity
import com.example.travelio.view.registration.RegistrationActivity
import java.lang.StringBuilder
import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.example.travelio.requests.fcm.UpdateToken
import com.example.travelio.view.dashboard.orders.OrderActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Token.initialize(this.applicationContext)
        DeviceToken.initialize(this.applicationContext)
        Local.initialize(this.applicationContext)
        if (Local.getLanguage() != null){setLocale(this,Local.getLanguage()!!)}
        val bundle = intent.extras
        val video = findViewById<VideoView>(R.id.videos)
        val videoPath = StringBuilder("android.resource://").append(packageName).append("/raw/video").toString()
        video.setVideoPath(videoPath)
        video.start()
        video.setOnCompletionListener {
            when {
                bundle != null -> {
                    val intent = Intent(this,OrderActivity::class.java)
                    intent.putExtra(this.resources.getString(R.string.TYPE_NAVIGATION),UpdateToken.setDestinationID(this.applicationContext,
                        (bundle.get(this.resources.getString(R.string.TYPE_NAVIGATION)) ?: this.resources.getString(R.string.HOTEL)) as String))
                    startActivity(intent)
                    finish()
                }
                Token.getToken() != null -> {
                    startActivity(Intent(this@MainActivity, DashBoardActivity::class.java))
                    finish()
                }
                else -> {
                    startActivity(Intent(this@MainActivity, RegistrationActivity::class.java))
                    finish()
                }
            }
        }
    }
    @Suppress("DEPRECATION")
    private fun setLocale(activity: Activity, languageCode: String) {
        val locale = Locale(languageCode)
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        val metric = resources.displayMetrics
        config.setLocale(locale)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){ this.applicationContext.createConfigurationContext(config)
        }else{ resources.updateConfiguration(config,metric) }
    }

}