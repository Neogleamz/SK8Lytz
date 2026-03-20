package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final /* synthetic */ class ng implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    public final /* synthetic */ qg f13844a;

    /* renamed from: b  reason: collision with root package name */
    public final /* synthetic */ zzpk f13845b;

    /* renamed from: c  reason: collision with root package name */
    public final /* synthetic */ Object f13846c;

    /* renamed from: d  reason: collision with root package name */
    public final /* synthetic */ long f13847d;

    /* renamed from: e  reason: collision with root package name */
    public final /* synthetic */ com.google.mlkit.vision.barcode.internal.h f13848e;

    public /* synthetic */ ng(qg qgVar, zzpk zzpkVar, Object obj, long j8, com.google.mlkit.vision.barcode.internal.h hVar) {
        this.f13844a = qgVar;
        this.f13845b = zzpkVar;
        this.f13846c = obj;
        this.f13847d = j8;
        this.f13848e = hVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f13844a.h(this.f13845b, this.f13846c, this.f13847d, this.f13848e);
    }
}
