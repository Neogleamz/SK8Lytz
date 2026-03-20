package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class l6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f16759a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ j6 f16760b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l6(j6 j6Var, zzn zznVar) {
        this.f16759a = zznVar;
        this.f16760b = j6Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f16760b.f16700a;
        gbVar.p0();
        gbVar2 = this.f16760b.f16700a;
        zzn zznVar = this.f16759a;
        gbVar2.l().k();
        gbVar2.q0();
        n6.j.f(zznVar.f17288a);
        gbVar2.f(zznVar);
    }
}
