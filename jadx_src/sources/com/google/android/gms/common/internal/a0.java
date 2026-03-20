package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Looper;
import java.util.HashMap;
import java.util.concurrent.Executor;
import n6.k0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a0 extends d {

    /* renamed from: f  reason: collision with root package name */
    private final HashMap f11813f = new HashMap();

    /* renamed from: g  reason: collision with root package name */
    private final Context f11814g;

    /* renamed from: h  reason: collision with root package name */
    private volatile Handler f11815h;

    /* renamed from: i  reason: collision with root package name */
    private final z f11816i;

    /* renamed from: j  reason: collision with root package name */
    private final t6.a f11817j;

    /* renamed from: k  reason: collision with root package name */
    private final long f11818k;

    /* renamed from: l  reason: collision with root package name */
    private final long f11819l;

    /* renamed from: m  reason: collision with root package name */
    private volatile Executor f11820m;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a0(Context context, Looper looper, Executor executor) {
        z zVar = new z(this, null);
        this.f11816i = zVar;
        this.f11814g = context.getApplicationContext();
        this.f11815h = new com.google.android.gms.internal.common.j(looper, zVar);
        this.f11817j = t6.a.b();
        this.f11818k = 5000L;
        this.f11819l = 300000L;
        this.f11820m = executor;
    }

    @Override // com.google.android.gms.common.internal.d
    protected final void d(k0 k0Var, ServiceConnection serviceConnection, String str) {
        n6.j.m(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.f11813f) {
            y yVar = (y) this.f11813f.get(k0Var);
            if (yVar == null) {
                String obj = k0Var.toString();
                throw new IllegalStateException("Nonexistent connection status for service config: " + obj);
            } else if (!yVar.h(serviceConnection)) {
                String obj2 = k0Var.toString();
                throw new IllegalStateException("Trying to unbind a GmsServiceConnection  that was not bound before.  config=" + obj2);
            } else {
                yVar.f(serviceConnection, str);
                if (yVar.i()) {
                    this.f11815h.sendMessageDelayed(this.f11815h.obtainMessage(0, k0Var), this.f11818k);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.common.internal.d
    public final boolean f(k0 k0Var, ServiceConnection serviceConnection, String str, Executor executor) {
        boolean j8;
        n6.j.m(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.f11813f) {
            y yVar = (y) this.f11813f.get(k0Var);
            if (executor == null) {
                executor = this.f11820m;
            }
            if (yVar == null) {
                yVar = new y(this, k0Var);
                yVar.d(serviceConnection, serviceConnection, str);
                yVar.e(str, executor);
                this.f11813f.put(k0Var, yVar);
            } else {
                this.f11815h.removeMessages(0, k0Var);
                if (yVar.h(serviceConnection)) {
                    String obj = k0Var.toString();
                    throw new IllegalStateException("Trying to bind a GmsServiceConnection that was already connected before.  config=" + obj);
                }
                yVar.d(serviceConnection, serviceConnection, str);
                int a9 = yVar.a();
                if (a9 == 1) {
                    serviceConnection.onServiceConnected(yVar.b(), yVar.c());
                } else if (a9 == 2) {
                    yVar.e(str, executor);
                }
            }
            j8 = yVar.j();
        }
        return j8;
    }
}
