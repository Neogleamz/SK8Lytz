package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f16733a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ f9 f16734b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k9(f9 f9Var, zzn zznVar) {
        this.f16733a = zznVar;
        this.f16734b = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        dVar = this.f16734b.f16529d;
        if (dVar == null) {
            this.f16734b.i().E().a("Failed to reset data on the service: not connected to service");
            return;
        }
        try {
            n6.j.l(this.f16733a);
            dVar.r0(this.f16733a);
        } catch (RemoteException e8) {
            this.f16734b.i().E().b("Failed to reset data on the service: remote exception", e8);
        }
        this.f16734b.f0();
    }
}
