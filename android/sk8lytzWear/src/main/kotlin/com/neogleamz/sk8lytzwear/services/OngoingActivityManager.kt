package com.neogleamz.sk8lytzwear.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import com.neogleamz.sk8lytzwear.MainActivity
import com.neogleamz.sk8lytzwear.R

object OngoingActivityManager {
    private const val ONGOING_CHANNEL_ID = "sk8lytz_session"
    private const val ONGOING_NOTIFICATION_ID = 101

    fun startOngoingActivity(context: Context) {
        // Create notification channel (idempotent)
        val channel = NotificationChannel(
            ONGOING_CHANNEL_ID,
            "Skate Session",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Active skating session indicator"
        }
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)

        // PendingIntent to reopen the app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification. Use our brandmark icon!
        val notificationBuilder = NotificationCompat.Builder(context, ONGOING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_sk8lytz_brandmark)
            .setContentTitle("SK8Lytz")
            .setContentText("Skating session active")
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_WORKOUT)
            .setContentIntent(pendingIntent)

        // Build OngoingActivity
        val ongoingActivity = OngoingActivity.Builder(context, ONGOING_NOTIFICATION_ID, notificationBuilder)
            .setStaticIcon(R.drawable.ic_sk8lytz_brandmark)
            .setTouchIntent(pendingIntent)
            .setStatus(
                Status.Builder()
                    .addTemplate("Skating")
                    .build()
            )
            .build()

        ongoingActivity.apply(context)

        if (context is android.app.Service) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                context.startForeground(ONGOING_NOTIFICATION_ID, notificationBuilder.build(), android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH)
            } else {
                context.startForeground(ONGOING_NOTIFICATION_ID, notificationBuilder.build())
            }
        } else {
            notificationManager?.notify(ONGOING_NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    fun stopOngoingActivity(context: Context) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        if (context is android.app.Service) {
            context.stopForeground(true)
        } else {
            notificationManager?.cancel(ONGOING_NOTIFICATION_ID)
        }
    }
}
