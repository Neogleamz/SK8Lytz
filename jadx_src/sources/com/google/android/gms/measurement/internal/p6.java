package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class p6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzac f16871a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ j6 f16872b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p6(j6 j6Var, zzac zzacVar) {
        this.f16871a = zzacVar;
        this.f16872b = j6Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        gb gbVar;
        gb gbVar2;
        gb gbVar3;
        gbVar = this.f16872b.f16700a;
        gbVar.p0();
        if (this.f16871a.f17252c.t() == null) {
            gbVar3 = this.f16872b.f16700a;
            gbVar3.q(this.f16871a);
            return;
        }
        gbVar2 = this.f16872b.f16700a;
        gbVar2.S(this.f16871a);
    }
}
