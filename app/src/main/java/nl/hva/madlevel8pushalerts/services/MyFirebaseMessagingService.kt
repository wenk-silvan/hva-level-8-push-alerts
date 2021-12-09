package nl.hva.madlevel8pushalerts.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import nl.hva.madlevel8pushalerts.models.NotificationEvent


const val TAG: String = "NOTIFICATION"

object EVENTS {
    val newNotification: MutableLiveData<NotificationEvent> by lazy {
        MutableLiveData<NotificationEvent>()
    }
}

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "The token refreshed: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i(TAG, "Notification received from: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty() && remoteMessage.notification != null) {
            Log.d(TAG, "Notification data payload: ${remoteMessage.data}")
            EVENTS.newNotification.postValue(
                NotificationEvent(
                    if (remoteMessage.notification!!.title == null) ""
                    else remoteMessage.notification!!.title!!,
                    remoteMessage.data.getOrDefault("description", ""),
                    remoteMessage.data.getOrDefault("source", "")
                )
            )
        }

        remoteMessage.notification?.let {
            Log.d(TAG, "Notification body: ${it.body}")
        }
    }
}