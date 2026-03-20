package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.RemoteException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f16951a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ Bundle f16952b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ f9 f16953c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r9(f9 f9Var, zzn zznVar, Bundle bundle) {
        this.f16951a = zznVar;
        this.f16952b = bundle;
        this.f16953c = f9Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f7.d dVar;
        dVar = this.f16953c.f16529d;
        if (dVar == null) {
            this.f16953c.i().E().a("Failed to send default event parameters to service");
            return;
        }
        try {
            n6.j.l(this.f16951a);
            dVar.G1(this.f16952b, this.f16951a);
        } catch (RemoteException e8) {
            this.f16953c.i().E().b("Failed to send default event parameters to service", e8);
        }
    }
}
