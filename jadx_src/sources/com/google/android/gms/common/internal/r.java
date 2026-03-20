package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r extends n6.b0 {

    /* renamed from: a  reason: collision with root package name */
    private b f11860a;

    /* renamed from: b  reason: collision with root package name */
    private final int f11861b;

    public r(b bVar, int i8) {
        this.f11860a = bVar;
        this.f11861b = i8;
    }

    @Override // n6.f
    public final void D0(int i8, IBinder iBinder, Bundle bundle) {
        n6.j.m(this.f11860a, "onPostInitComplete can be called only once per call to getRemoteService");
        this.f11860a.L(i8, iBinder, bundle, this.f11861b);
        this.f11860a = null;
    }

    @Override // n6.f
    public final void O1(int i8, IBinder iBinder, zzk zzkVar) {
        b bVar = this.f11860a;
        n6.j.m(bVar, "onPostInitCompleteWithConnectionInfo can be called only once per call togetRemoteService");
        n6.j.l(zzkVar);
        b.a0(bVar, zzkVar);
        D0(i8, iBinder, zzkVar.f11889a);
    }

    @Override // n6.f
    public final void a0(int i8, Bundle bundle) {
        Log.wtf("GmsClient", "received deprecated onAccountValidationComplete callback, ignoring", new Exception());
    }
}
