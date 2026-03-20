package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.md;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class w6 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ zzn f17072a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ j6 f17073b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w6(j6 j6Var, zzn zznVar) {
        this.f17072a = zznVar;
        this.f17073b = j6Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        gb gbVar;
        gb gbVar2;
        gbVar = this.f17073b.f16700a;
        gbVar.p0();
        gbVar2 = this.f17073b.f16700a;
        zzn zznVar = this.f17072a;
        gbVar2.l().k();
        gbVar2.q0();
        n6.j.f(zznVar.f17288a);
        zziq i8 = zziq.i(zznVar.B, (md.a() && gbVar2.c0().r(c0.S0)) ? zznVar.H : 100);
        zziq Q = gbVar2.Q(zznVar.f17288a);
        gbVar2.i().I().c("Setting consent, package, consent", zznVar.f17288a, i8);
        gbVar2.C(zznVar.f17288a, i8);
        if (i8.u(Q)) {
            gbVar2.b0(zznVar);
        }
        if (md.a() && gbVar2.c0().r(c0.S0)) {
            v d8 = v.d(zznVar.K);
            if (v.f17035f.equals(d8)) {
                return;
            }
            gbVar2.i().I().c("Setting DMA consent. package, consent", zznVar.f17288a, d8);
            gbVar2.B(zznVar.f17288a, d8);
        }
    }
}
