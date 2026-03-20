package b6;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
@SuppressLint({"InlinedApi"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x {
    public static void a(Context context, String str, int i8, int i9, int i10) {
        if (l0.f8063a >= 26) {
            NotificationManager notificationManager = (NotificationManager) a.e((NotificationManager) context.getSystemService("notification"));
            NotificationChannel notificationChannel = new NotificationChannel(str, context.getString(i8), i10);
            if (i9 != 0) {
                notificationChannel.setDescription(context.getString(i9));
            }
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
