package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ x8 f16343a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ x8 f16344b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ long f16345c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ boolean f16346d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ z8 f16347e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b9(z8 z8Var, x8 x8Var, x8 x8Var2, long j8, boolean z4) {
        this.f16343a = x8Var;
        this.f16344b = x8Var2;
        this.f16345c = j8;
        this.f16346d = z4;
        this.f16347e = z8Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16347e.H(this.f16343a, this.f16344b, this.f16345c, this.f16346d, null);
    }
}
