package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o5 implements ServiceConnection {

    /* renamed from: a  reason: collision with root package name */
    private final String f16849a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ p5 f16850b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o5(p5 p5Var, String str) {
        this.f16850b = p5Var;
        this.f16849a = str;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (iBinder == null) {
            this.f16850b.f16870a.i().J().a("Install Referrer connection returned with null binder");
            return;
        }
        try {
            com.google.android.gms.internal.measurement.z0 e8 = com.google.android.gms.internal.measurement.b1.e(iBinder);
            if (e8 == null) {
                this.f16850b.f16870a.i().J().a("Install Referrer Service implementation was not found");
                return;
            }
            this.f16850b.f16870a.i().I().a("Install Referrer Service connected");
            this.f16850b.f16870a.l().B(new q5(this, e8, this));
        } catch (RuntimeException e9) {
            this.f16850b.f16870a.i().J().b("Exception occurred while calling Install Referrer API", e9);
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        this.f16850b.f16870a.i().I().a("Install Referrer Service disconnected");
    }
}
