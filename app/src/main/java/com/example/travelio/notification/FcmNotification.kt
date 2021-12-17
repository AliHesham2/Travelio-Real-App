package com.example.travelio.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.travelio.view.splash.MainActivity
import com.example.travelio.view.util.PopUpMsg
import com.example.travelio.R
import android.os.Build


fun NotificationManager.sendNotification(body: String, title: String?, applicationContext: Context) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent =   checkSystem(contentIntent,applicationContext)
    val iconImage = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.icon)
    val bigPicStyle = NotificationCompat.BigPictureStyle().bigPicture(iconImage).bigLargeIcon(null)
    // Build the notification
    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.CHANNEL_ID))
        //small icon behind the name of app
        .setSmallIcon(R.drawable.icon)
        .setContentTitle(title)
        .setContentText(body)
        .setContentIntent(contentPendingIntent)
        .setStyle(bigPicStyle)
        .setLargeIcon(iconImage)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(PopUpMsg.NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}

@SuppressLint("UnspecifiedImmutableFlag")
fun checkSystem(contentIntent: Intent, applicationContext: Context): PendingIntent? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(applicationContext, PopUpMsg.NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getActivity(applicationContext, PopUpMsg.NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}