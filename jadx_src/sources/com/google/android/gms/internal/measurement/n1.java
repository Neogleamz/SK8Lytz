package com.google.android.gms.internal.measurement;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n1 {

    /* renamed from: a  reason: collision with root package name */
    private static final int f12360a;

    /* renamed from: b  reason: collision with root package name */
    public static final int f12361b;

    static {
        int i8 = Build.VERSION.SDK_INT;
        f12360a = i8 >= 23 ? 67108864 : 0;
        f12361b = i8 >= 31 ? 33554432 : 0;
    }

    public static PendingIntent a(Context context, int i8, Intent intent, int i9) {
        return PendingIntent.getBroadcast(context, 0, intent, i9);
    }
}
