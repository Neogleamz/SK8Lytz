package com.google.android.gms.common.api.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zabx extends BroadcastReceiver {

    /* renamed from: a  reason: collision with root package name */
    Context f11716a;

    /* renamed from: b  reason: collision with root package name */
    private final l6.p f11717b;

    public zabx(l6.p pVar) {
        this.f11717b = pVar;
    }

    public final void a(Context context) {
        this.f11716a = context;
    }

    public final synchronized void b() {
        Context context = this.f11716a;
        if (context != null) {
            context.unregisterReceiver(this);
        }
        this.f11716a = null;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        Uri data = intent.getData();
        if ("com.google.android.gms".equals(data != null ? data.getSchemeSpecificPart() : null)) {
            this.f11717b.a();
            b();
        }
    }
}
