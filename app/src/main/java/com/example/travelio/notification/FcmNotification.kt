package com.example.travelio.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.travelio.view.util.PopUpMsg
import com.example.travelio.R
import android.os.Build
import com.example.travelio.requests.fcm.UpdateToken
import com.example.travelio.view.dashboard.orders.OrderActivity


fun NotificationManager.sendNotification(body: String,  title: String?,type: String, applicationContext: Context) {
    val intent =  Intent(applicationContext, OrderActivity::class.java)
    intent.putExtra(applicationContext.resources.getString(R.string.TYPE_NAVIGATION),UpdateToken.setDestinationID(applicationContext,type))
    intent.putExtra(applicationContext.resources.getString(R.string.Current_NAVIGATION),applicationContext.resources.getString(R.string.FOREGROUND))
    val contentPendingIntent = checkSystem(intent, applicationContext)
    val iconImage = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.icon)
    val bigPicStyle = NotificationCompat.BigPictureStyle().bigPicture(iconImage).bigLargeIcon(null)

    // Build the notification
    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.CHANNEL_ID))
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




@SuppressLint("UnspecifiedImmutableFlag")
fun checkSystem(contentIntent: Intent, applicationContext: Context): PendingIntent? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(applicationContext, PopUpMsg.NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
    } else {
        PendingIntent.getActivity(applicationContext, PopUpMsg.NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}