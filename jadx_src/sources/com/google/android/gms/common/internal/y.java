package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import n6.k0;
import n6.m0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class y implements ServiceConnection, m0 {

    /* renamed from: a  reason: collision with root package name */
    private final Map f11867a = new HashMap();

    /* renamed from: b  reason: collision with root package name */
    private int f11868b = 2;

    /* renamed from: c  reason: collision with root package name */
    private boolean f11869c;

    /* renamed from: d  reason: collision with root package name */
    private IBinder f11870d;

    /* renamed from: e  reason: collision with root package name */
    private final k0 f11871e;

    /* renamed from: f  reason: collision with root package name */
    private ComponentName f11872f;

    /* renamed from: g  reason: collision with root package name */
    final /* synthetic */ a0 f11873g;

    public y(a0 a0Var, k0 k0Var) {
        this.f11873g = a0Var;
        this.f11871e = k0Var;
    }

    public final int a() {
        return this.f11868b;
    }

    public final ComponentName b() {
        return this.f11872f;
    }

    public final IBinder c() {
        return this.f11870d;
    }

    public final void d(ServiceConnection serviceConnection, ServiceConnection serviceConnection2, String str) {
        this.f11867a.put(serviceConnection, serviceConnection2);
    }

    public final void e(String str, Executor executor) {
        t6.a aVar;
        Context context;
        Context context2;
        t6.a aVar2;
        Context context3;
        Handler handler;
        Handler handler2;
        long j8;
        this.f11868b = 3;
        StrictMode.VmPolicy vmPolicy = StrictMode.getVmPolicy();
        if (u6.m.l()) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder(vmPolicy).permitUnsafeIntentLaunch().build());
        }
        try {
            a0 a0Var = this.f11873g;
            aVar = a0Var.f11817j;
            context = a0Var.f11814g;
            k0 k0Var = this.f11871e;
            context2 = a0Var.f11814g;
            boolean d8 = aVar.d(context, str, k0Var.b(context2), this, 4225, executor);
            this.f11869c = d8;
            if (d8) {
                handler = this.f11873g.f11815h;
                Message obtainMessage = handler.obtainMessage(1, this.f11871e);
                handler2 = this.f11873g.f11815h;
                j8 = this.f11873g.f11819l;
                handler2.sendMessageDelayed(obtainMessage, j8);
            } else {
                this.f11868b = 2;
                try {
                    a0 a0Var2 = this.f11873g;
                    aVar2 = a0Var2.f11817j;
                    context3 = a0Var2.f11814g;
                    aVar2.c(context3, this);
                } catch (IllegalArgumentException unused) {
                }
            }
        } finally {
            StrictMode.setVmPolicy(vmPolicy);
        }
    }

    public final void f(ServiceConnection serviceConnection, String str) {
        this.f11867a.remove(serviceConnection);
    }

    public final void g(String str) {
        Handler handler;
        t6.a aVar;
        Context context;
        k0 k0Var = this.f11871e;
        handler = this.f11873g.f11815h;
        handler.removeMessages(1, k0Var);
        a0 a0Var = this.f11873g;
        aVar = a0Var.f11817j;
        context = a0Var.f11814g;
        aVar.c(context, this);
        this.f11869c = false;
        this.f11868b = 2;
    }

    public final boolean h(ServiceConnection serviceConnection) {
        return this.f11867a.containsKey(serviceConnection);
    }

    public final boolean i() {
        return this.f11867a.isEmpty();
    }

    public final boolean j() {
        return this.f11869c;
    }

    @Override // android.content.ServiceConnection
    public final void onBindingDied(ComponentName componentName) {
        onServiceDisconnected(componentName);
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        HashMap hashMap;
        Handler handler;
        hashMap = this.f11873g.f11813f;
        synchronized (hashMap) {
            handler = this.f11873g.f11815h;
            handler.removeMessages(1, this.f11871e);
            this.f11870d = iBinder;
            this.f11872f = componentName;
            for (ServiceConnection serviceConnection : this.f11867a.values()) {
                serviceConnection.onServiceConnected(componentName, iBinder);
            }
            this.f11868b = 1;
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        HashMap hashMap;
        Handler handler;
        hashMap = this.f11873g.f11813f;
        synchronized (hashMap) {
            handler = this.f11873g.f11815h;
            handler.removeMessages(1, this.f11871e);
            this.f11870d = null;
            this.f11872f = componentName;
            for (ServiceConnection serviceConnection : this.f11867a.values()) {
                serviceConnection.onServiceDisconnected(componentName);
            }
            this.f11868b = 2;
        }
    }
}
