package com.dexterous.flutterlocalnotifications;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ForegroundService extends Service {
    private static int a(ArrayList<Integer> arrayList) {
        int intValue = arrayList.get(0).intValue();
        for (int i8 = 1; i8 < arrayList.size(); i8++) {
            intValue |= arrayList.get(i8).intValue();
        }
        return intValue;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i8, int i9) {
        int i10 = Build.VERSION.SDK_INT;
        ForegroundServiceStartParameter foregroundServiceStartParameter = (ForegroundServiceStartParameter) (i10 >= 33 ? intent.getSerializableExtra("com.dexterous.flutterlocalnotifications.ForegroundServiceStartParameter", ForegroundServiceStartParameter.class) : intent.getSerializableExtra("com.dexterous.flutterlocalnotifications.ForegroundServiceStartParameter"));
        Notification createNotification = FlutterLocalNotificationsPlugin.createNotification(this, foregroundServiceStartParameter.f8857a);
        if (foregroundServiceStartParameter.f8859c == null || i10 < 29) {
            startForeground(foregroundServiceStartParameter.f8857a.id.intValue(), createNotification);
        } else {
            startForeground(foregroundServiceStartParameter.f8857a.id.intValue(), createNotification, a(foregroundServiceStartParameter.f8859c));
        }
        return foregroundServiceStartParameter.f8858b;
    }
}
