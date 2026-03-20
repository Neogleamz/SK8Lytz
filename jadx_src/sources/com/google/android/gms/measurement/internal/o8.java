package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.lf;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zziq f16856a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ long f16857b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ boolean f16858c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ zziq f16859d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ h7 f16860e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o8(h7 h7Var, zziq zziqVar, long j8, boolean z4, zziq zziqVar2) {
        this.f16856a = zziqVar;
        this.f16857b = j8;
        this.f16858c = z4;
        this.f16859d = zziqVar2;
        this.f16860e = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16860e.I(this.f16856a);
        h7.K(this.f16860e, this.f16856a, this.f16857b, false, this.f16858c);
        if (lf.a() && this.f16860e.a().r(c0.f16409t0)) {
            h7.L(this.f16860e, this.f16856a, this.f16859d);
        }
    }
}
