package androidx.core.app;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Build;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static void a(AlarmManager alarmManager, int i8, long j8, PendingIntent pendingIntent) {
            alarmManager.setExact(i8, j8, pendingIntent);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {
        static AlarmManager.AlarmClockInfo a(long j8, PendingIntent pendingIntent) {
            return new AlarmManager.AlarmClockInfo(j8, pendingIntent);
        }

        static void b(AlarmManager alarmManager, Object obj, PendingIntent pendingIntent) {
            alarmManager.setAlarmClock((AlarmManager.AlarmClockInfo) obj, pendingIntent);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c {
        static void a(AlarmManager alarmManager, int i8, long j8, PendingIntent pendingIntent) {
            alarmManager.setAndAllowWhileIdle(i8, j8, pendingIntent);
        }

        static void b(AlarmManager alarmManager, int i8, long j8, PendingIntent pendingIntent) {
            alarmManager.setExactAndAllowWhileIdle(i8, j8, pendingIntent);
        }
    }

    @SuppressLint({"MissingPermission"})
    public static void a(AlarmManager alarmManager, long j8, PendingIntent pendingIntent, PendingIntent pendingIntent2) {
        if (Build.VERSION.SDK_INT >= 21) {
            b.b(alarmManager, b.a(j8, pendingIntent), pendingIntent2);
        } else {
            c(alarmManager, 0, j8, pendingIntent2);
        }
    }

    public static void b(AlarmManager alarmManager, int i8, long j8, PendingIntent pendingIntent) {
        if (Build.VERSION.SDK_INT >= 23) {
            c.a(alarmManager, i8, j8, pendingIntent);
        } else {
            alarmManager.set(i8, j8, pendingIntent);
        }
    }

    public static void c(AlarmManager alarmManager, int i8, long j8, PendingIntent pendingIntent) {
        if (Build.VERSION.SDK_INT >= 19) {
            a.a(alarmManager, i8, j8, pendingIntent);
        } else {
            alarmManager.set(i8, j8, pendingIntent);
        }
    }

    public static void d(AlarmManager alarmManager, int i8, long j8, PendingIntent pendingIntent) {
        if (Build.VERSION.SDK_INT >= 23) {
            c.b(alarmManager, i8, j8, pendingIntent);
        } else {
            c(alarmManager, i8, j8, pendingIntent);
        }
    }
}
