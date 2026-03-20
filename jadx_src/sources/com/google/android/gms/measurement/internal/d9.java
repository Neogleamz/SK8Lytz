package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ long f16463a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ z8 f16464b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d9(z8 z8Var, long j8) {
        this.f16463a = j8;
        this.f16464b = z8Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16464b.m().t(this.f16463a);
        this.f16464b.f17234e = null;
    }
}
