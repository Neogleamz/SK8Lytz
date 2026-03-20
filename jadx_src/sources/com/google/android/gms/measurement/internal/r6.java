package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class r6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ com.google.android.gms.internal.measurement.h2 f16943a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f16944b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f16945c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ boolean f16946d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ AppMeasurementDynamiteService f16947e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r6(AppMeasurementDynamiteService appMeasurementDynamiteService, com.google.android.gms.internal.measurement.h2 h2Var, String str, String str2, boolean z4) {
        this.f16943a = h2Var;
        this.f16944b = str;
        this.f16945c = str2;
        this.f16946d = z4;
        this.f16947e = appMeasurementDynamiteService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16947e.f16288a.H().E(this.f16943a, this.f16944b, this.f16945c, this.f16946d);
    }
}
