package l6;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.internal.b;
import java.util.Collections;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g implements a.f, ServiceConnection {

    /* renamed from: m  reason: collision with root package name */
    private static final String f21747m = g.class.getSimpleName();

    /* renamed from: a  reason: collision with root package name */
    private final String f21748a;

    /* renamed from: b  reason: collision with root package name */
    private final String f21749b;

    /* renamed from: c  reason: collision with root package name */
    private final ComponentName f21750c;

    /* renamed from: d  reason: collision with root package name */
    private final Context f21751d;

    /* renamed from: e  reason: collision with root package name */
    private final c f21752e;

    /* renamed from: f  reason: collision with root package name */
    private final Handler f21753f;

    /* renamed from: g  reason: collision with root package name */
    private final h f21754g;

    /* renamed from: h  reason: collision with root package name */
    private IBinder f21755h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f21756j;

    /* renamed from: k  reason: collision with root package name */
    private String f21757k;

    /* renamed from: l  reason: collision with root package name */
    private String f21758l;

    private final void q() {
        if (Thread.currentThread() != this.f21753f.getLooper().getThread()) {
            throw new IllegalStateException("This method should only run on the NonGmsServiceBrokerClient's handler thread.");
        }
    }

    @Override // com.google.android.gms.common.api.a.f
    public final Set<Scope> a() {
        return Collections.emptySet();
    }

    @Override // com.google.android.gms.common.api.a.f
    public final void b(com.google.android.gms.common.internal.e eVar, Set<Scope> set) {
    }

    @Override // com.google.android.gms.common.api.a.f
    public final void c(String str) {
        q();
        this.f21757k = str;
        disconnect();
    }

    @Override // com.google.android.gms.common.api.a.f
    public final void disconnect() {
        q();
        String.valueOf(this.f21755h);
        try {
            this.f21751d.unbindService(this);
        } catch (IllegalArgumentException unused) {
        }
        this.f21756j = false;
        this.f21755h = null;
    }

    @Override // com.google.android.gms.common.api.a.f
    public final boolean e() {
        q();
        return this.f21756j;
    }

    @Override // com.google.android.gms.common.api.a.f
    public final String f() {
        String str = this.f21748a;
        if (str != null) {
            return str;
        }
        n6.j.l(this.f21750c);
        return this.f21750c.getPackageName();
    }

    @Override // com.google.android.gms.common.api.a.f
    public final void g(b.c cVar) {
        q();
        String.valueOf(this.f21755h);
        if (isConnected()) {
            try {
                c("connect() called when already connected");
            } catch (Exception unused) {
            }
        }
        try {
            Intent intent = new Intent();
            ComponentName componentName = this.f21750c;
            if (componentName != null) {
                intent.setComponent(componentName);
            } else {
                intent.setPackage(this.f21748a).setAction(this.f21749b);
            }
            boolean bindService = this.f21751d.bindService(intent, this, com.google.android.gms.common.internal.d.a());
            this.f21756j = bindService;
            if (!bindService) {
                this.f21755h = null;
                this.f21754g.e(new ConnectionResult(16));
            }
            String.valueOf(this.f21755h);
        } catch (SecurityException e8) {
            this.f21756j = false;
            this.f21755h = null;
            throw e8;
        }
    }

    @Override // com.google.android.gms.common.api.a.f
    public final void h(b.e eVar) {
    }

    @Override // com.google.android.gms.common.api.a.f
    public final boolean i() {
        return false;
    }

    @Override // com.google.android.gms.common.api.a.f
    public final boolean isConnected() {
        q();
        return this.f21755h != null;
    }

    @Override // com.google.android.gms.common.api.a.f
    public final int j() {
        return 0;
    }

    @Override // com.google.android.gms.common.api.a.f
    public final Feature[] k() {
        return new Feature[0];
    }

    @Override // com.google.android.gms.common.api.a.f
    public final String l() {
        return this.f21757k;
    }

    @Override // com.google.android.gms.common.api.a.f
    public final boolean m() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void n() {
        this.f21756j = false;
        this.f21755h = null;
        this.f21752e.d(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void o(IBinder iBinder) {
        this.f21756j = false;
        this.f21755h = iBinder;
        String.valueOf(iBinder);
        this.f21752e.f(new Bundle());
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
        this.f21753f.post(new Runnable() { // from class: l6.s
            @Override // java.lang.Runnable
            public final void run() {
                g.this.o(iBinder);
            }
        });
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        this.f21753f.post(new Runnable() { // from class: l6.r
            @Override // java.lang.Runnable
            public final void run() {
                g.this.n();
            }
        });
    }

    public final void p(String str) {
        this.f21758l = str;
    }
}
