package com.bs.ecommerce.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.bs.ecommerce.R
import com.bs.ecommerce.main.MainActivity
import com.bs.ecommerce.main.SplashScreenActivity
import com.bs.ecommerce.utils.AcceptPolicyPreference
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.utils.PrefSingleton
import com.bs.ecommerce.utils.showLog
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import java.io.IOException

class MessagingService : FirebaseMessagingService() {


    private var mNotificationManager: NotificationManager? = null
    private lateinit var prefManager: AcceptPolicyPreference

    override fun onNewToken(refreshedToken: String)
    {
        super.onNewToken(refreshedToken)

        prefManager = AcceptPolicyPreference(this)

        TAG.showLog("New Fcm token: $refreshedToken")

        PrefSingleton.setPrefs(PrefSingleton.FCM_TOKEN, refreshedToken)

        prefManager.fcmTokenChanged = true
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage)
    {
        super.onMessageReceived(remoteMessage)

        TAG.showLog(remoteMessage.data.toString())

        if (remoteMessage.data.isNotEmpty())
            sendNotification(remoteMessage.data)

    }


    private fun sendNotification(data: Map<String, String>)
    {
        val title = data[TITLE]
        val textContent = data[BODY]
        val bigPicture = data[BANNER]


        val targetActivityClass : Class<*>

        if(MyApplication.isJwtActive)
            targetActivityClass = MainActivity::class.java
        else
            targetActivityClass = SplashScreenActivity::class.java

        val intent = Intent(this, targetActivityClass)
        val bundle = Bundle()
        for (key in data.keys) {
            bundle.putString(key, data[key])
        }
        intent.putExtras(bundle)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(targetActivityClass)
        stackBuilder.addNextIntent(intent)

        val pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setSmallIcon(R.drawable.ic_notification)
                .setStyle(NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(textContent))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            var notificationChannel = mNotificationManager?.getNotificationChannel(NOTIFICATION_CHANNEL_ID)

            if(notificationChannel == null)
            {
                val importance = NotificationManager.IMPORTANCE_HIGH
                notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance)

                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                assert(mNotificationManager != null)
                notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
                mNotificationManager?.createNotificationChannel(notificationChannel)
            }


        }

        var bitmap: Bitmap? = null
        try {
            bitmap = Picasso.with(this).load(bigPicture).get()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        if (bitmap != null)
        {
            val style = NotificationCompat.BigPictureStyle()
            style.setBigContentTitle(title)
            style.setSummaryText(textContent)
            style.bigPicture(bitmap)
            notificationBuilder.setStyle(style)
        }
        mNotificationManager?.notify(NOTIFICATION_ID, notificationBuilder.build())

    }

    companion object
    {
        val TAG = "fcm"
        val REQUEST_CODE = 0
        val NOTIFICATION_ID = 1

        val TITLE = "title"
        val BODY = "body"
        val BANNER = "bigPicture"
        val ITEM_TYPE = "itemType"
        val ITEM_ID = "itemId"

        val NOTIFICATION_CHANNEL_ID = "10002"
        val NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME"
    }
}
/*{
    "to":    "fWHVRkiD-kU:APA91bGm4JbBqC4TJQ4S58p3lGfoZ8LbL1q_gHRrC1MF4_ETLp8Re-5AAh2ThNsRfumGVZrHlNeVOnLuxnnuAnrhgT5BlP9IwyrwqeHRvfcHzjsZ0-nPFYfbPndBRi8U0TdZmmHQwAv8",

    "data": {
    "mediaType":"image",
    "bigPicture" : "https://media.sproutsocial.com/uploads/2017/01/facebook-cover-photo.png",
    "body": "NopStation23",
    "title": "Hello BrainStation23",
    "id" : "23538",
    "type" : 1
}
}*/
