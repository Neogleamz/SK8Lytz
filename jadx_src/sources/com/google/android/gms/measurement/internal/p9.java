package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f16881a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ f9 f16882b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p9(f9 f9Var, zzn zznVar) {
        this.f16881a = zznVar;
        this.f16882b = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        dVar = this.f16882b.f16529d;
        if (dVar == null) {
            this.f16882b.i().E().a("Discarding data. Failed to send app launch");
            return;
        }
        try {
            n6.j.l(this.f16881a);
            dVar.Z(this.f16881a);
            this.f16882b.o().H();
            this.f16882b.M(dVar, null, this.f16881a);
            this.f16882b.f0();
        } catch (RemoteException e8) {
            this.f16882b.i().E().b("Failed to send app launch to the service", e8);
        }
    }
}
