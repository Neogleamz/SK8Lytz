package com.google.android.gms.internal.measurement;

import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k3 {

    /* renamed from: a  reason: collision with root package name */
    private final d0 f12274a;

    /* renamed from: b  reason: collision with root package name */
    final g6 f12275b;

    /* renamed from: c  reason: collision with root package name */
    final g6 f12276c;

    /* renamed from: d  reason: collision with root package name */
    private final ca f12277d;

    public k3() {
        d0 d0Var = new d0();
        this.f12274a = d0Var;
        g6 g6Var = new g6(null, d0Var);
        this.f12276c = g6Var;
        this.f12275b = g6Var.d();
        ca caVar = new ca();
        this.f12277d = caVar;
        g6Var.h("require", new og(caVar));
        caVar.b("internal.platform", new Callable() { // from class: com.google.android.gms.internal.measurement.v2
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return new ng();
            }
        });
        g6Var.h("runtime.counter", new j(Double.valueOf(0.0d)));
    }

    public final r a(g6 g6Var, zzgb$zzd... zzgb_zzdArr) {
        r rVar = r.f12463r;
        for (zzgb$zzd zzgb_zzd : zzgb_zzdArr) {
            rVar = g8.a(zzgb_zzd);
            e5.b(this.f12276c);
            if ((rVar instanceof u) || (rVar instanceof s)) {
                rVar = this.f12274a.a(g6Var, rVar);
            }
        }
        return rVar;
    }

    public final void b(String str, Callable<? extends m> callable) {
        this.f12277d.b(str, callable);
    }
}
