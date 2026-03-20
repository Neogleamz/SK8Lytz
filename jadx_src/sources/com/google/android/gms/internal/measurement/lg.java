package com.google.android.gms.internal.measurement;

import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class lg extends m {

    /* renamed from: c  reason: collision with root package name */
    private b f12327c;

    public lg(b bVar) {
        super("internal.registerCallback");
        this.f12327c = bVar;
    }

    @Override // com.google.android.gms.internal.measurement.m
    public final r c(g6 g6Var, List<r> list) {
        e5.g(this.f12328a, 3, list);
        String e8 = g6Var.b(list.get(0)).e();
        r b9 = g6Var.b(list.get(1));
        if (b9 instanceof s) {
            r b10 = g6Var.b(list.get(2));
            if (b10 instanceof q) {
                q qVar = (q) b10;
                if (qVar.k("type")) {
                    this.f12327c.c(e8, qVar.k("priority") ? e5.i(qVar.h("priority").d().doubleValue()) : 1000, (s) b9, qVar.h("type").e());
                    return r.f12463r;
                }
                throw new IllegalArgumentException("Undefined rule type");
            }
            throw new IllegalArgumentException("Invalid callback params");
        }
        throw new IllegalArgumentException("Invalid callback type");
    }
}
