package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class za extends t {

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ ab f17247e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public za(ab abVar, f7 f7Var) {
        super(f7Var);
        this.f17247e = abVar;
    }

    @Override // com.google.android.gms.measurement.internal.t
    public final void d() {
        this.f17247e.x();
        this.f17247e.i().I().a("Starting upload from DelayedRunnable");
        this.f17247e.f16446b.u0();
    }
}
