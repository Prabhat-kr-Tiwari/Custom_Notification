package com.example.notification

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class MainActivity : AppCompatActivity() {
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)


        //at first ask permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(POST_NOTIFICATIONS), 101)
            }
        }

        button.setOnClickListener {
            makeNotification()
        }

        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.img, null)

        val bitmapDrawable = drawable as BitmapDrawable
        val largeIcon = bitmapDrawable.bitmap

        var notification = Notification()
        var notification1 = NotificationCompat.Builder(this, CHANNEL_ID)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, REQUEST_CODE, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(
                this, REQUEST_CODE, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        /*  val pendingIntent =
              PendingIntent.getActivity(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)*/

//        createNotificationChannel()

        //big picture style
        val bigPictureStyle = Notification.BigPictureStyle()
            .bigPicture(
                ((ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.img,
                    null
                )) as BitmapDrawable).bitmap
            )
            .bigLargeIcon(largeIcon)
            .setBigContentTitle("Image sent by Raman")
            .setSummaryText("Image Message")

        //inbox style
        val inboxStyle=Notification.InboxStyle()
            .addLine("A")
            .addLine("B")
            .addLine("C")
            .addLine("D")
            .addLine("E")
            .addLine("F")
            .addLine("G")
            .addLine("H")
            .addLine("I")
            .addLine("J")
            .setBigContentTitle("Full message")
            .setSummaryText("Message from Raman")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = Notification.Builder(this, CHANNEL_ID)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.img)
                .setContentText("New Message")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .setStyle(inboxStyle)
                //                .setChannelId(CHANNEL_ID)
                .setSubText("New message from raman")
                .build()
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "New Channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )

        } else {
            notification = Notification.Builder(this, CHANNEL_ID)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.img)
                .setContentText("New Message")
                .setStyle(inboxStyle)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setSubText("New message from raman")
                .build()
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "New Channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        notificationManager.notify(NOTIFICATION_ID, notification)

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification name"
            val descriptionText = "Notification description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun makeNotification() {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.img, null)

        val bitmapDrawable = drawable as BitmapDrawable
        val largeIcon = bitmapDrawable.bitmap
        val channelId = "CHANNEL_ID_NOTIFICATION"
        val builder = NotificationCompat.Builder(applicationContext, channelId)
        builder.setSmallIcon(R.drawable.img)
            .setLargeIcon(largeIcon)
            .setContentTitle("Notification Title")
            .setContentText("Some text for notification")
            .setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_HIGH)
        val intent = Intent(this, NotificationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("data", "Some value to be passed here")

        val pendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0, intent, PendingIntent.FLAG_MUTABLE
            )
        builder.setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            var notificationChannel = notificationManager.getNotificationChannel(channelId)
            if (notificationChannel == null) {
                val importance = NotificationManager.IMPORTANCE_HIGH
                notificationChannel = NotificationChannel(channelId, "some description", importance)
                notificationManager.createNotificationChannel(notificationChannel)
                notificationChannel!!.enableVibration(true)
                notificationChannel!!.lightColor = Color.GREEN
                notificationManager.createNotificationChannel(notificationChannel)

            }
        }
        notificationManager.notify(0, builder.build())


    }

    companion object {
        private const val CHANNEL_ID = "Message Channel"
        private const val NOTIFICATION_ID = 1000
        private const val REQUEST_CODE = 10
    }
}