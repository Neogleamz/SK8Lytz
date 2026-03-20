package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class ea implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ com.google.android.gms.internal.measurement.h2 f16490a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ AppMeasurementDynamiteService f16491b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ea(AppMeasurementDynamiteService appMeasurementDynamiteService, com.google.android.gms.internal.measurement.h2 h2Var) {
        this.f16490a = h2Var;
        this.f16491b = appMeasurementDynamiteService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16491b.f16288a.J().S(this.f16490a, this.f16491b.f16288a.m());
    }
}
