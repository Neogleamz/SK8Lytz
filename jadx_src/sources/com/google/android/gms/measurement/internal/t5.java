package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class t5 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ com.google.android.gms.internal.measurement.h2 f16997a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ AppMeasurementDynamiteService f16998b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public t5(AppMeasurementDynamiteService appMeasurementDynamiteService, com.google.android.gms.internal.measurement.h2 h2Var) {
        this.f16997a = h2Var;
        this.f16998b = appMeasurementDynamiteService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16998b.f16288a.H().B(this.f16997a);
    }
}
