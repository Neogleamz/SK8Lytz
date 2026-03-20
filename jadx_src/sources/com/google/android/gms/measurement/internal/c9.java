package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c9 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ x8 f16441a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ long f16442b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ z8 f16443c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c9(z8 z8Var, x8 x8Var, long j8) {
        this.f16441a = x8Var;
        this.f16442b = j8;
        this.f16443c = z8Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16443c.I(this.f16441a, false, this.f16442b);
        z8 z8Var = this.f16443c;
        z8Var.f17234e = null;
        z8Var.r().H(null);
    }
}
