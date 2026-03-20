package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ba implements ServiceConnection, b.a, b.InterfaceC0125b {

    /* renamed from: a  reason: collision with root package name */
    private volatile boolean f16348a;

    /* renamed from: b  reason: collision with root package name */
    private volatile u4 f16349b;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ f9 f16350c;

    /* JADX INFO: Access modifiers changed from: protected */
    public ba(f9 f9Var) {
        this.f16350c = f9Var;
    }

    public final void a() {
        this.f16350c.k();
        Context zza = this.f16350c.zza();
        synchronized (this) {
            if (this.f16348a) {
                this.f16350c.i().I().a("Connection attempt already in progress");
            } else if (this.f16349b != null && (this.f16349b.e() || this.f16349b.isConnected())) {
                this.f16350c.i().I().a("Already awaiting connection attempt");
            } else {
                this.f16349b = new u4(zza, Looper.getMainLooper(), this, this);
                this.f16350c.i().I().a("Connecting to remote service");
                this.f16348a = true;
                n6.j.l(this.f16349b);
                this.f16349b.o();
            }
        }
    }

    public final void b(Intent intent) {
        ba baVar;
        this.f16350c.k();
        Context zza = this.f16350c.zza();
        t6.a b9 = t6.a.b();
        synchronized (this) {
            if (this.f16348a) {
                this.f16350c.i().I().a("Connection attempt already in progress");
                return;
            }
            this.f16350c.i().I().a("Using local app measurement service");
            this.f16348a = true;
            baVar = this.f16350c.f16528c;
            b9.a(zza, intent, baVar, 129);
        }
    }

    @Override // com.google.android.gms.common.internal.b.a
    public final void d(int i8) {
        n6.j.e("MeasurementServiceConnection.onConnectionSuspended");
        this.f16350c.i().D().a("Service connection suspended");
        this.f16350c.l().B(new ga(this));
    }

    @Override // com.google.android.gms.common.internal.b.InterfaceC0125b
    public final void e(ConnectionResult connectionResult) {
        n6.j.e("MeasurementServiceConnection.onConnectionFailed");
        x4 C = this.f16350c.f16485a.C();
        if (C != null) {
            C.J().b("Service connection failed", connectionResult);
        }
        synchronized (this) {
            this.f16348a = false;
            this.f16349b = null;
        }
        this.f16350c.l().B(new fa(this));
    }

    @Override // com.google.android.gms.common.internal.b.a
    public final void f(Bundle bundle) {
        n6.j.e("MeasurementServiceConnection.onConnected");
        synchronized (this) {
            try {
                n6.j.l(this.f16349b);
                this.f16350c.l().B(new ca(this, this.f16349b.B()));
            } catch (DeadObjectException | IllegalStateException unused) {
                this.f16349b = null;
                this.f16348a = false;
            }
        }
    }

    public final void g() {
        if (this.f16349b != null && (this.f16349b.isConnected() || this.f16349b.e())) {
            this.f16349b.disconnect();
        }
        this.f16349b = null;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ba baVar;
        n6.j.e("MeasurementServiceConnection.onServiceConnected");
        synchronized (this) {
            if (iBinder == null) {
                this.f16348a = false;
                this.f16350c.i().E().a("Service connected with null binder");
                return;
            }
            f7.d dVar = null;
            try {
                String interfaceDescriptor = iBinder.getInterfaceDescriptor();
                if ("com.google.android.gms.measurement.internal.IMeasurementService".equals(interfaceDescriptor)) {
                    IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.measurement.internal.IMeasurementService");
                    dVar = queryLocalInterface instanceof f7.d ? (f7.d) queryLocalInterface : new p4(iBinder);
                    this.f16350c.i().I().a("Bound to IMeasurementService interface");
                } else {
                    this.f16350c.i().E().b("Got binder with a wrong descriptor", interfaceDescriptor);
                }
            } catch (RemoteException unused) {
                this.f16350c.i().E().a("Service connect failed to get IMeasurementService");
            }
            if (dVar == null) {
                this.f16348a = false;
                try {
                    t6.a b9 = t6.a.b();
                    Context zza = this.f16350c.zza();
                    baVar = this.f16350c.f16528c;
                    b9.c(zza, baVar);
                } catch (IllegalArgumentException unused2) {
                }
            } else {
                this.f16350c.l().B(new aa(this, dVar));
            }
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        n6.j.e("MeasurementServiceConnection.onServiceDisconnected");
        this.f16350c.i().D().a("Service disconnected");
        this.f16350c.l().B(new da(this, componentName));
    }
}
