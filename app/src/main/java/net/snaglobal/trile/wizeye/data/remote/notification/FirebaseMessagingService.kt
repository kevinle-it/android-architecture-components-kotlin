package net.snaglobal.trile.wizeye.data.remote.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Firebase Cloud Messaging.
 *
 * @author lmtri
 * @since Dec 03, 2018 at 10:46 AM
 */
class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        // "data" key in JSON.
        // Data payload is of Map<String, String> type due to its custom key-value pairs.
        // Handled here BOTH in foreground and background.
        remoteMessage?.data?.isNotEmpty()?.let {
            if (it) {
                // TODO: Dec-04-2018 Show Notification with the above data when App is in background
            } else {

            }
        }

        // "notification" key in JSON.
        // Notification payload is of Notification type due to its predefined keys by FCM.
        // Handled here ONLY in foreground.
        // If the app is in background -> an automatically generated notification is displayed.
        // User taps on notification -> they are returned to the app.
        // Messages containing both notification and data payloads are treated as notification messages.
        // Firebase console always sends notiification messages.
        remoteMessage?.notification?.let {
            showNotification(it.title, it.body)
        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)

        Log.d("FCM_TOKEN", "FCM Token: $token")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageTitle FCM message title received.
     * @param messageBody FCM message body received.
     */
    private fun showNotification(messageTitle: String?, messageBody: String?) {

    }
}