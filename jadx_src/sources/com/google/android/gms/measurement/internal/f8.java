package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ com.google.android.gms.internal.measurement.h2 f16524a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f16525b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ String f16526c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ AppMeasurementDynamiteService f16527d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f8(AppMeasurementDynamiteService appMeasurementDynamiteService, com.google.android.gms.internal.measurement.h2 h2Var, String str, String str2) {
        this.f16524a = h2Var;
        this.f16525b = str;
        this.f16526c = str2;
        this.f16527d = appMeasurementDynamiteService;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16527d.f16288a.H().D(this.f16524a, this.f16525b, this.f16526c);
    }
}
