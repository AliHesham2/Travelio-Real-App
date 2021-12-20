package com.example.travelio.firebase

import android.app.NotificationManager
import androidx.core.content.ContextCompat
import com.example.travelio.R
import com.example.travelio.notification.sendNotification
import com.example.travelio.requests.fcm.UpdateToken
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(data: RemoteMessage) {
        sendNotification(data.notification?.body ?: resources.getString(R.string.WRONG), data.notification?.title ?:resources.getString(R.string.WRONG) ,data.data["type"] ?: resources.getString(R.string.HOTEL))
    }


    override fun onNewToken(token: String) {
        UpdateToken.updateDeviceToken(token)
    }

    //Send notification in foreground
    private fun sendNotification(body: String, title: String, type: String) {
        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        notificationManager.sendNotification(body,title,type,applicationContext)
    }

}