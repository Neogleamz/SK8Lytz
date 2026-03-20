package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ g7 f16547a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ f6 f16548b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g6(f6 f6Var, g7 g7Var) {
        this.f16547a = g7Var;
        this.f16548b = f6Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        f6.e(this.f16548b, this.f16547a);
        this.f16548b.c(this.f16547a.f16555g);
    }
}
