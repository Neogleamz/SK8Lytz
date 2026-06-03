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
        val notification = NotificationCompat.Builder(context, ONGOING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_sk8lytz_brandmark)
            .setContentTitle("SK8Lytz")
            .setContentText("Skating session active")
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_WORKOUT)
            .setContentIntent(pendingIntent)
            .build()

        // Build OngoingActivity
        val ongoingActivity = OngoingActivity.Builder(context, ONGOING_NOTIFICATION_ID, notification)
            .setStaticIcon(R.drawable.ic_sk8lytz_brandmark)
            .setTouchIntent(pendingIntent)
            .setStatus(
                Status.Builder()
                    .addTemplate("Skating")
                    .build()
            )
            .build()

        ongoingActivity.apply(context)
        notificationManager?.notify(ONGOING_NOTIFICATION_ID, notification)
    }

    fun stopOngoingActivity(context: Context) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.cancel(ONGOING_NOTIFICATION_ID)
    }
}
