package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f17052a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ f9 f17053b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v9(f9 f9Var, zzn zznVar) {
        this.f17052a = zznVar;
        this.f17053b = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        dVar = this.f17053b.f16529d;
        if (dVar == null) {
            this.f17053b.i().E().a("Failed to send consent settings to service");
            return;
        }
        try {
            n6.j.l(this.f17052a);
            dVar.m(this.f17052a);
            this.f17053b.f0();
        } catch (RemoteException e8) {
            this.f17053b.i().E().b("Failed to send consent settings to the service", e8);
        }
    }
}
