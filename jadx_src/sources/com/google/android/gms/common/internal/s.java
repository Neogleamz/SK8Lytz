package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s implements ServiceConnection {

    /* renamed from: a  reason: collision with root package name */
    private final int f11862a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ b f11863b;

    public s(b bVar, int i8) {
        this.f11863b = bVar;
        this.f11862a = i8;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Object obj;
        b bVar = this.f11863b;
        if (iBinder == null) {
            b.b0(bVar, 16);
            return;
        }
        obj = bVar.f11834p;
        synchronized (obj) {
            b bVar2 = this.f11863b;
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
            bVar2.q = (queryLocalInterface == null || !(queryLocalInterface instanceof n6.g)) ? new n(iBinder) : (n6.g) queryLocalInterface;
        }
        this.f11863b.c0(0, null, this.f11862a);
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        Object obj;
        obj = this.f11863b.f11834p;
        synchronized (obj) {
            this.f11863b.q = null;
        }
        b bVar = this.f11863b;
        int i8 = this.f11862a;
        Handler handler = bVar.f11832m;
        handler.sendMessage(handler.obtainMessage(6, i8, 1));
    }
}
