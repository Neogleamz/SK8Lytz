package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class j7 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ com.google.android.gms.internal.measurement.h2 f16703a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ zzbf f16704b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f16705c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ AppMeasurementDynamiteService f16706d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j7(AppMeasurementDynamiteService appMeasurementDynamiteService, com.google.android.gms.internal.measurement.h2 h2Var, zzbf zzbfVar, String str) {
        this.f16703a = h2Var;
        this.f16704b = zzbfVar;
        this.f16705c = str;
        this.f16706d = appMeasurementDynamiteService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16706d.f16288a.H().C(this.f16703a, this.f16704b, this.f16705c);
    }
}
