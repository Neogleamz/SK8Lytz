package com.google.android.gms.internal.measurement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g6 {

    /* renamed from: a  reason: collision with root package name */
    private final g6 f12199a;

    /* renamed from: b  reason: collision with root package name */
    private d0 f12200b;

    /* renamed from: c  reason: collision with root package name */
    private Map<String, r> f12201c = new HashMap();

    /* renamed from: d  reason: collision with root package name */
    private Map<String, Boolean> f12202d = new HashMap();

    public g6(g6 g6Var, d0 d0Var) {
        this.f12199a = g6Var;
        this.f12200b = d0Var;
    }

    public final r a(g gVar) {
        r rVar = r.f12463r;
        Iterator<Integer> C = gVar.C();
        while (C.hasNext()) {
            rVar = this.f12200b.a(this, gVar.p(C.next().intValue()));
            if (rVar instanceof k) {
                break;
            }
        }
        return rVar;
    }

    public final r b(r rVar) {
        return this.f12200b.a(this, rVar);
    }

    public final r c(String str) {
        g6 g6Var = this;
        while (!g6Var.f12201c.containsKey(str)) {
            g6Var = g6Var.f12199a;
            if (g6Var == null) {
                throw new IllegalArgumentException(String.format("%s is not defined", str));
            }
        }
        return g6Var.f12201c.get(str);
    }

    public final g6 d() {
        return new g6(this, this.f12200b);
    }

    public final void e(String str, r rVar) {
        if (this.f12202d.containsKey(str)) {
            return;
        }
        if (rVar == null) {
            this.f12201c.remove(str);
        } else {
            this.f12201c.put(str, rVar);
        }
    }

    public final void f(String str, r rVar) {
        e(str, rVar);
        this.f12202d.put(str, Boolean.TRUE);
    }

    public final boolean g(String str) {
        g6 g6Var = this;
        while (!g6Var.f12201c.containsKey(str)) {
            g6Var = g6Var.f12199a;
            if (g6Var == null) {
                return false;
            }
        }
        return true;
    }

    public final void h(String str, r rVar) {
        g6 g6Var;
        g6 g6Var2 = this;
        while (!g6Var2.f12201c.containsKey(str) && (g6Var = g6Var2.f12199a) != null && g6Var.g(str)) {
            g6Var2 = g6Var2.f12199a;
        }
        if (g6Var2.f12202d.containsKey(str)) {
            return;
        }
        if (rVar == null) {
            g6Var2.f12201c.remove(str);
        } else {
            g6Var2.f12201c.put(str, rVar);
        }
    }
}
