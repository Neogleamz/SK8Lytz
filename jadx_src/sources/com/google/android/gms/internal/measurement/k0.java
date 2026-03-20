package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k0 extends z {
    /* JADX INFO: Access modifiers changed from: protected */
    public k0() {
        this.f12724a.add(zzbv.FOR_IN);
        this.f12724a.add(zzbv.FOR_IN_CONST);
        this.f12724a.add(zzbv.FOR_IN_LET);
        this.f12724a.add(zzbv.FOR_LET);
        this.f12724a.add(zzbv.FOR_OF);
        this.f12724a.add(zzbv.FOR_OF_CONST);
        this.f12724a.add(zzbv.FOR_OF_LET);
        this.f12724a.add(zzbv.WHILE);
    }

    private static r c(o0 o0Var, r rVar, r rVar2) {
        return d(o0Var, rVar.f(), rVar2);
    }

    private static r d(o0 o0Var, Iterator<r> it, r rVar) {
        if (it != null) {
            while (it.hasNext()) {
                r a9 = o0Var.a(it.next()).a((g) rVar);
                if (a9 instanceof k) {
                    k kVar = (k) a9;
                    if ("break".equals(kVar.h())) {
                        return r.f12463r;
                    }
                    if ("return".equals(kVar.h())) {
                        return kVar;
                    }
                }
            }
        }
        return r.f12463r;
    }

    private static r e(o0 o0Var, r rVar, r rVar2) {
        if (rVar instanceof Iterable) {
            return d(o0Var, ((Iterable) rVar).iterator(), rVar2);
        }
        throw new IllegalArgumentException("Non-iterable type in for...of loop.");
    }

    @Override // com.google.android.gms.internal.measurement.z
    public final r b(String str, g6 g6Var, List<r> list) {
        switch (n0.f12359a[e5.c(str).ordinal()]) {
            case 1:
                e5.f(zzbv.FOR_IN, 3, list);
                if (list.get(0) instanceof t) {
                    return c(new r0(g6Var, list.get(0).e()), g6Var.b(list.get(1)), g6Var.b(list.get(2)));
                }
                throw new IllegalArgumentException("Variable name in FOR_IN must be a string");
            case 2:
                e5.f(zzbv.FOR_IN_CONST, 3, list);
                if (list.get(0) instanceof t) {
                    return c(new m0(g6Var, list.get(0).e()), g6Var.b(list.get(1)), g6Var.b(list.get(2)));
                }
                throw new IllegalArgumentException("Variable name in FOR_IN_CONST must be a string");
            case 3:
                e5.f(zzbv.FOR_IN_LET, 3, list);
                if (list.get(0) instanceof t) {
                    return c(new p0(g6Var, list.get(0).e()), g6Var.b(list.get(1)), g6Var.b(list.get(2)));
                }
                throw new IllegalArgumentException("Variable name in FOR_IN_LET must be a string");
            case 4:
                e5.f(zzbv.FOR_LET, 4, list);
                r b9 = g6Var.b(list.get(0));
                if (b9 instanceof g) {
                    g gVar = (g) b9;
                    r rVar = list.get(1);
                    r rVar2 = list.get(2);
                    r b10 = g6Var.b(list.get(3));
                    g6 d8 = g6Var.d();
                    for (int i8 = 0; i8 < gVar.u(); i8++) {
                        String e8 = gVar.p(i8).e();
                        d8.h(e8, g6Var.c(e8));
                    }
                    while (g6Var.b(rVar).b().booleanValue()) {
                        r a9 = g6Var.a((g) b10);
                        if (a9 instanceof k) {
                            k kVar = (k) a9;
                            if ("break".equals(kVar.h())) {
                                return r.f12463r;
                            }
                            if ("return".equals(kVar.h())) {
                                return kVar;
                            }
                        }
                        g6 d9 = g6Var.d();
                        for (int i9 = 0; i9 < gVar.u(); i9++) {
                            String e9 = gVar.p(i9).e();
                            d9.h(e9, d8.c(e9));
                        }
                        d9.b(rVar2);
                        d8 = d9;
                    }
                    return r.f12463r;
                }
                throw new IllegalArgumentException("Initializer variables in FOR_LET must be an ArrayList");
            case 5:
                e5.f(zzbv.FOR_OF, 3, list);
                if (list.get(0) instanceof t) {
                    return e(new r0(g6Var, list.get(0).e()), g6Var.b(list.get(1)), g6Var.b(list.get(2)));
                }
                throw new IllegalArgumentException("Variable name in FOR_OF must be a string");
            case 6:
                e5.f(zzbv.FOR_OF_CONST, 3, list);
                if (list.get(0) instanceof t) {
                    return e(new m0(g6Var, list.get(0).e()), g6Var.b(list.get(1)), g6Var.b(list.get(2)));
                }
                throw new IllegalArgumentException("Variable name in FOR_OF_CONST must be a string");
            case 7:
                e5.f(zzbv.FOR_OF_LET, 3, list);
                if (list.get(0) instanceof t) {
                    return e(new p0(g6Var, list.get(0).e()), g6Var.b(list.get(1)), g6Var.b(list.get(2)));
                }
                throw new IllegalArgumentException("Variable name in FOR_OF_LET must be a string");
            case 8:
                e5.f(zzbv.WHILE, 4, list);
                r rVar3 = list.get(0);
                r rVar4 = list.get(1);
                r b11 = g6Var.b(list.get(3));
                if (g6Var.b(list.get(2)).b().booleanValue()) {
                    r a10 = g6Var.a((g) b11);
                    if (a10 instanceof k) {
                        k kVar2 = (k) a10;
                        if (!"break".equals(kVar2.h())) {
                            if ("return".equals(kVar2.h())) {
                                return kVar2;
                            }
                        }
                        return r.f12463r;
                    }
                }
                while (g6Var.b(rVar3).b().booleanValue()) {
                    r a11 = g6Var.a((g) b11);
                    if (a11 instanceof k) {
                        k kVar3 = (k) a11;
                        if ("break".equals(kVar3.h())) {
                            return r.f12463r;
                        }
                        if ("return".equals(kVar3.h())) {
                            return kVar3;
                        }
                    }
                    g6Var.b(rVar4);
                }
                return r.f12463r;
            default:
                return super.a(str);
        }
    }
}
