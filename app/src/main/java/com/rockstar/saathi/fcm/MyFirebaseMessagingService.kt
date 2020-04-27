package com.rockstar.saathi.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rockstar.saathi.R
import com.rockstar.saathi.activity.DashBoardActivity


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var notificationManager: NotificationManager
    private val ADMIN_CHANNEL_ID = "chat"
    var receivedMessage:String?=""

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("token", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage?.let { message ->

            //Log.e("inonmessagereceived","inonmessagereceived"+remoteMessage.data.toString() )
            //Log.e("inonmessagereceived","inonmessagereceived"+remoteMessage.data.get("message"))
            //Log.e("inonmessagereceived","inonmessagereceived"+remoteMessage.data.get("title"))

            receivedMessage=remoteMessage.data.get("message").toString()
            Log.e("inonmessagereceived","inonmessagereceived"+receivedMessage)

            if(remoteMessage.data.get("message").equals("")){
                receivedMessage="Photo"
            }
            sendNotification(receivedMessage.toString())

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //Setting up Notification channels for android O and above
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupNotificationChannels()
            }

            // Check if message contains a notification payload.
            remoteMessage.notification?.let {
                Log.e("notification", "Message Notification Body: ${it.body}")
            }

        }
    }

    /*fun sendNotification(messageBody: String) {
        val intent= Intent(this, LatestMessagesActivity::class.java)
        intent.putExtra("notification","Notification")
        startActivity(intent)
        val random = Random()
        val m = random.nextInt(9999 - 1000) + 1000
        //intent.putExtra("key", messageBody)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
       /* val pendingIntent = PendingIntent.getActivity(this, 0  , intent,
            PendingIntent.FLAG_ONE_SHOT)
*/
        val pendingIntent = PendingIntent.getActivity(this, 0  , intent,
            0)

        val icon2 = BitmapFactory.decodeResource(resources,
            R.mipmap.digishare_icon)
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.digishare_icon)
            .setContentTitle("Digishare")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setLargeIcon(icon2)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //notificationManager.notify(m, notificationBuilder.build())
        notificationManager.notify(0, notificationBuilder.build())


        // notificationManager.notify(Random().nextInt() /* ID of notification */, notificationBuilder.build())
    }*/

    fun sendNotification(messageBody: String){
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle("Saathi")
        bigTextStyle.bigText(messageBody)

        val builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) //set icon for notification
                .setContentTitle("Saathi") //set title of notification
                .setContentText(messageBody) //this is notification message
                .setAutoCancel(true) // makes auto cancel of notification
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) //set priority of notification
                .setStyle(bigTextStyle)

        val intent = Intent(this, DashBoardActivity::class.java)
        intent.putExtra("notification","Notification")
        intent.putExtra("key", messageBody)


        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        /* intent = PendingIntent.getActivity(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?f=d&daddr="+ CommonMethods.getPrefrence(applicationContext,CommonMethods.LAT)+","+CommonMethods.getPrefrence(applicationContext,CommonMethods.LONG)))
        intent.component = ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)*/
        builder.setContentIntent(pendingIntent)

        // Add as notification
        // Add as notification
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannels() {
        val sound: Uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.getPackageName() + "/" + R.raw.wrong)

        val attributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        val adminChannelName = getString(R.string.notifications_admin_channel_name)
        val adminChannelDescription = getString(R.string.notifications_admin_channel_description)

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH)
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        adminChannel.setSound(sound, attributes);
        notificationManager.createNotificationChannel(adminChannel)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val adminChannelName: CharSequence = "New notification"
        val adminChannelDescription = "Device to devie notification"
        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(
            ADMIN_CHANNEL_ID,
            adminChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)
    }
}