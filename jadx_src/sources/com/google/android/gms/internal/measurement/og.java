package com.google.android.gms.internal.measurement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class og extends m {

    /* renamed from: c  reason: collision with root package name */
    private final ca f12415c;

    /* renamed from: d  reason: collision with root package name */
    private final Map<String, m> f12416d;

    public og(ca caVar) {
        super("require");
        this.f12416d = new HashMap();
        this.f12415c = caVar;
    }

    @Override // com.google.android.gms.internal.measurement.m
    public final r c(g6 g6Var, List<r> list) {
        e5.g("require", 1, list);
        String e8 = g6Var.b(list.get(0)).e();
        if (this.f12416d.containsKey(e8)) {
            return this.f12416d.get(e8);
        }
        r a9 = this.f12415c.a(e8);
        if (a9 instanceof m) {
            this.f12416d.put(e8, (m) a9);
        }
        return a9;
    }
}
