package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e0 extends z {
    /* JADX INFO: Access modifiers changed from: protected */
    public e0() {
        this.f12724a.add(zzbv.APPLY);
        this.f12724a.add(zzbv.BLOCK);
        this.f12724a.add(zzbv.BREAK);
        this.f12724a.add(zzbv.CASE);
        this.f12724a.add(zzbv.DEFAULT);
        this.f12724a.add(zzbv.CONTINUE);
        this.f12724a.add(zzbv.DEFINE_FUNCTION);
        this.f12724a.add(zzbv.FN);
        this.f12724a.add(zzbv.IF);
        this.f12724a.add(zzbv.QUOTE);
        this.f12724a.add(zzbv.RETURN);
        this.f12724a.add(zzbv.SWITCH);
        this.f12724a.add(zzbv.TERNARY);
    }

    private static r c(g6 g6Var, List<r> list) {
        e5.j(zzbv.FN, 2, list);
        r b9 = g6Var.b(list.get(0));
        r b10 = g6Var.b(list.get(1));
        if (b10 instanceof g) {
            List<r> D = ((g) b10).D();
            List<r> arrayList = new ArrayList<>();
            if (list.size() > 2) {
                arrayList = list.subList(2, list.size());
            }
            return new s(b9.e(), D, arrayList, g6Var);
        }
        throw new IllegalArgumentException(String.format("FN requires an ArrayValue of parameter names found %s", b10.getClass().getCanonicalName()));
    }

    @Override // com.google.android.gms.internal.measurement.z
    public final r b(String str, g6 g6Var, List<r> list) {
        int i8 = 0;
        switch (h0.f12223a[e5.c(str).ordinal()]) {
            case 1:
                e5.f(zzbv.APPLY, 3, list);
                r b9 = g6Var.b(list.get(0));
                String e8 = g6Var.b(list.get(1)).e();
                r b10 = g6Var.b(list.get(2));
                if (b10 instanceof g) {
                    if (e8.isEmpty()) {
                        throw new IllegalArgumentException("Function name for apply is undefined");
                    }
                    return b9.g(e8, g6Var, ((g) b10).D());
                }
                throw new IllegalArgumentException(String.format("Function arguments for Apply are not a list found %s", b10.getClass().getCanonicalName()));
            case 2:
                return g6Var.d().a(new g(list));
            case 3:
                e5.f(zzbv.BREAK, 0, list);
                return r.f12466v;
            case 4:
            case 5:
                if (!list.isEmpty()) {
                    r b11 = g6Var.b(list.get(0));
                    if (b11 instanceof g) {
                        return g6Var.a((g) b11);
                    }
                }
                return r.f12463r;
            case 6:
                e5.f(zzbv.BREAK, 0, list);
                return r.f12465u;
            case 7:
                e5.j(zzbv.DEFINE_FUNCTION, 2, list);
                s sVar = (s) c(g6Var, list);
                g6Var.h(sVar.i() == null ? BuildConfig.FLAVOR : sVar.i(), sVar);
                return sVar;
            case 8:
                return c(g6Var, list);
            case 9:
                e5.j(zzbv.IF, 2, list);
                r b12 = g6Var.b(list.get(0));
                r b13 = g6Var.b(list.get(1));
                r b14 = list.size() > 2 ? g6Var.b(list.get(2)) : null;
                r rVar = r.f12463r;
                r a9 = b12.b().booleanValue() ? g6Var.a((g) b13) : b14 != null ? g6Var.a((g) b14) : rVar;
                return a9 instanceof k ? a9 : rVar;
            case 10:
                return new g(list);
            case 11:
                if (list.isEmpty()) {
                    return r.D;
                }
                e5.f(zzbv.RETURN, 1, list);
                return new k("return", g6Var.b(list.get(0)));
            case 12:
                e5.f(zzbv.SWITCH, 3, list);
                r b15 = g6Var.b(list.get(0));
                r b16 = g6Var.b(list.get(1));
                r b17 = g6Var.b(list.get(2));
                if (b16 instanceof g) {
                    if (b17 instanceof g) {
                        g gVar = (g) b16;
                        g gVar2 = (g) b17;
                        boolean z4 = false;
                        while (true) {
                            if (i8 < gVar.u()) {
                                if (z4 || b15.equals(g6Var.b(gVar.p(i8)))) {
                                    r b18 = g6Var.b(gVar2.p(i8));
                                    if (!(b18 instanceof k)) {
                                        z4 = true;
                                    } else if (!((k) b18).h().equals("break")) {
                                        return b18;
                                    }
                                }
                                i8++;
                            } else if (gVar.u() + 1 == gVar2.u()) {
                                r b19 = g6Var.b(gVar2.p(gVar.u()));
                                if (b19 instanceof k) {
                                    String h8 = ((k) b19).h();
                                    if (h8.equals("return") || h8.equals("continue")) {
                                        return b19;
                                    }
                                }
                            }
                        }
                        return r.f12463r;
                    }
                    throw new IllegalArgumentException("Malformed SWITCH statement, case statements are not a list");
                }
                throw new IllegalArgumentException("Malformed SWITCH statement, cases are not a list");
            case 13:
                e5.f(zzbv.TERNARY, 3, list);
                return g6Var.b(list.get(0)).b().booleanValue() ? g6Var.b(list.get(1)) : g6Var.b(list.get(2));
            default:
                return super.a(str);
        }
    }
}
