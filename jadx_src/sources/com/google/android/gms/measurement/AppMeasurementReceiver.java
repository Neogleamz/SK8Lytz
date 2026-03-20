package com.google.android.gms.measurement;

import android.content.Context;
import android.content.Intent;
import androidx.legacy.content.WakefulBroadcastReceiver;
import f7.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class AppMeasurementReceiver extends WakefulBroadcastReceiver implements j.a {

    /* renamed from: c  reason: collision with root package name */
    private j f16283c;

    @Override // f7.j.a
    public final void a(Context context, Intent intent) {
        WakefulBroadcastReceiver.c(context, intent);
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (this.f16283c == null) {
            this.f16283c = new j(this);
        }
        this.f16283c.a(context, intent);
    }
}
