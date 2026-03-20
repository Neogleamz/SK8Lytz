package com.google.android.gms.measurement.internal;

import android.os.RemoteException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ x8 f16861a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ f9 f16862b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o9(f9 f9Var, x8 x8Var) {
        this.f16861a = x8Var;
        this.f16862b = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        long j8;
        String str;
        String str2;
        String packageName;
        dVar = this.f16862b.f16529d;
        if (dVar == null) {
            this.f16862b.i().E().a("Failed to send current screen to service");
            return;
        }
        try {
            x8 x8Var = this.f16861a;
            if (x8Var == null) {
                j8 = 0;
                str = null;
                str2 = null;
                packageName = this.f16862b.zza().getPackageName();
            } else {
                j8 = x8Var.f17130c;
                str = x8Var.f17128a;
                str2 = x8Var.f17129b;
                packageName = this.f16862b.zza().getPackageName();
            }
            dVar.m0(j8, str, str2, packageName);
            this.f16862b.f0();
        } catch (RemoteException e8) {
            this.f16862b.i().E().b("Failed to send current screen to the service", e8);
        }
    }
}
