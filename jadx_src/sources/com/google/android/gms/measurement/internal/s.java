package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ f7 f16956a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ t f16957b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public s(t tVar, f7 f7Var) {
        this.f16956a = f7Var;
        this.f16957b = tVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f16956a.b();
        if (d.a()) {
            this.f16956a.l().B(this);
            return;
        }
        boolean e8 = this.f16957b.e();
        this.f16957b.f16991c = 0L;
        if (e8) {
            this.f16957b.d();
        }
    }
}
