package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f5 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ boolean f16497a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ g5 f16498b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f5(g5 g5Var, boolean z4) {
        this.f16497a = z4;
        this.f16498b = g5Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        gb gbVar;
        gbVar = this.f16498b.f16544a;
        gbVar.H(this.f16497a);
    }
}
