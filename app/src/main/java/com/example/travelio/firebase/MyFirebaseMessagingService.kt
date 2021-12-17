package com.example.travelio.firebase

import android.util.Log
import com.example.travelio.model.preference.DeviceToken
import com.example.travelio.requests.fcm.UpdateToken
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    //Get the data from notification
    override fun onMessageReceived(data: RemoteMessage) {
        data.notification?.let {
            Log.i("NotificationData:","body: ${it.body} + title: ${it.title}")
        }
    }


    override fun onNewToken(token: String) {
        Log.i("NotificationData::",token)
        UpdateToken.updateDeviceToken(token)
    }

    //Send notification in foreground
    private fun sendNotification(body: String, title: String?) {
      //  val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
       // notificationManager.sendNotification(body,title,applicationContext)
    }

}