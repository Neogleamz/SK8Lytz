package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f16974a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ f9 f16975b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s9(f9 f9Var, zzn zznVar) {
        this.f16974a = zznVar;
        this.f16975b = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        dVar = this.f16975b.f16529d;
        if (dVar == null) {
            this.f16975b.i().E().a("Failed to send measurementEnabled to service");
            return;
        }
        try {
            n6.j.l(this.f16974a);
            dVar.w(this.f16974a);
            this.f16975b.f0();
        } catch (RemoteException e8) {
            this.f16975b.i().E().b("Failed to send measurementEnabled to the service", e8);
        }
    }
}
