package com.dexterous.flutterlocalnotifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.annotation.Keep;
import androidx.core.app.n;
import com.dexterous.flutterlocalnotifications.models.NotificationDetails;
import com.dexterous.flutterlocalnotifications.utils.StringUtils;
@Keep
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ScheduledNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "ScheduledNotifReceiver";

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends com.google.gson.reflect.a<NotificationDetails> {
        a() {
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra(FlutterLocalNotificationsPlugin.NOTIFICATION_DETAILS);
        if (!StringUtils.isNullOrEmpty(stringExtra).booleanValue()) {
            NotificationDetails notificationDetails = (NotificationDetails) FlutterLocalNotificationsPlugin.buildGson().m(stringExtra, new a().getType());
            FlutterLocalNotificationsPlugin.showNotification(context, notificationDetails);
            FlutterLocalNotificationsPlugin.scheduleNextNotification(context, notificationDetails);
            return;
        }
        int intExtra = intent.getIntExtra("notification_id", 0);
        Notification notification = (Notification) (Build.VERSION.SDK_INT >= 33 ? intent.getParcelableExtra("notification", Notification.class) : intent.getParcelableExtra("notification"));
        if (notification == null) {
            FlutterLocalNotificationsPlugin.removeNotificationFromCache(context, Integer.valueOf(intExtra));
            Log.e(TAG, "Failed to parse a notification from  Intent. ID: " + intExtra);
            return;
        }
        notification.when = System.currentTimeMillis();
        n.e(context).h(intExtra, notification);
        if (intent.getBooleanExtra("repeat", false)) {
            return;
        }
        FlutterLocalNotificationsPlugin.removeNotificationFromCache(context, Integer.valueOf(intExtra));
    }
}
