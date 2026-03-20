package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.lf;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zziq f16875a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ long f16876b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ long f16877c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ boolean f16878d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ zziq f16879e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ h7 f16880f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p8(h7 h7Var, zziq zziqVar, long j8, long j9, boolean z4, zziq zziqVar2) {
        this.f16875a = zziqVar;
        this.f16876b = j8;
        this.f16877c = j9;
        this.f16878d = z4;
        this.f16879e = zziqVar2;
        this.f16880f = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16880f.I(this.f16875a);
        this.f16880f.C(this.f16876b, false);
        h7.K(this.f16880f, this.f16875a, this.f16877c, true, this.f16878d);
        if (lf.a() && this.f16880f.a().r(c0.f16409t0)) {
            h7.L(this.f16880f, this.f16875a, this.f16879e);
        }
    }
}
