package com.google.android.gms.internal.measurement;

import java.util.HashMap;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e9 extends m {

    /* renamed from: c  reason: collision with root package name */
    private final d f12164c;

    public e9(d dVar) {
        super("internal.eventLogger");
        this.f12164c = dVar;
    }

    @Override // com.google.android.gms.internal.measurement.m
    public final r c(g6 g6Var, List<r> list) {
        e5.g(this.f12328a, 3, list);
        String e8 = g6Var.b(list.get(0)).e();
        long a9 = (long) e5.a(g6Var.b(list.get(1)).d().doubleValue());
        r b9 = g6Var.b(list.get(2));
        this.f12164c.c(e8, a9, b9 instanceof q ? e5.e((q) b9) : new HashMap<>());
        return r.f12463r;
    }
}
