package com.example.travelio.view.util

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.travelio.R

class FullScreenImage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)
        val fullScreenImageView  =  findViewById<ImageView>(R.id.fullScreenImageView)
        val callingActivityIntent = intent
        if(callingActivityIntent != null) {
            val imageUri = callingActivityIntent.data
            if(imageUri != null && fullScreenImageView != null) {
                Glide.with(this)
                    .load(imageUri)
                    .into(fullScreenImageView)
            }
        }
    }
}