package com.google.android.gms.measurement.internal;

import android.os.Bundle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class mb implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ String f16803a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f16804b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ Bundle f16805c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ jb f16806d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public mb(jb jbVar, String str, String str2, Bundle bundle) {
        this.f16803a = str;
        this.f16804b = str2;
        this.f16805c = bundle;
        this.f16806d = jbVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16806d.f16715a.t((zzbf) n6.j.l(this.f16806d.f16715a.o0().F(this.f16803a, this.f16804b, this.f16805c, "auto", this.f16806d.f16715a.zzb().a(), false, true)), this.f16803a);
    }
}
