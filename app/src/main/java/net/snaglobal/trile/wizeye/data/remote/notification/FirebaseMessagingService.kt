package net.snaglobal.trile.wizeye.data.remote.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import net.snaglobal.trile.wizeye.R
import net.snaglobal.trile.wizeye.ui.fragment.MainFragmentContract

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
        val args = Bundle()
        args.putInt(
                FirebaseMessagingContract.DEEP_LINK_ARGUMENT_VIEW_PAGER_KEY,
                MainFragmentContract.VIEW_PAGER_INDEX_ALERT
        )

        val deepLink = NavDeepLinkBuilder(this)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.splashFragment)
                .setArguments(args)
                .createPendingIntent()

        val defaultChannelId = getString(R.string.alert_default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, defaultChannelId)
                .setSmallIcon(R.mipmap.ic_wizeye_launcher)
                .setContentTitle(
                        if (messageTitle.isNullOrBlank())
                            getString(R.string.alert_default_notification_title)
                        else
                            messageTitle
                )
                .setContentText(
                        if (messageBody.isNullOrBlank())
                            getString(R.string.alert_default_notification_body)
                        else
                            messageBody
                )
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(deepLink)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo, notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val defaultChannel = NotificationChannel(
                    defaultChannelId,
                    getString(R.string.alert_default_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(defaultChannel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}