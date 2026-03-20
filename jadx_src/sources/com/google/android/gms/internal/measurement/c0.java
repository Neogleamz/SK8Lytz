package com.google.android.gms.internal.measurement;

import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c0 extends z {
    public c0() {
        this.f12724a.add(zzbv.EQUALS);
        this.f12724a.add(zzbv.GREATER_THAN);
        this.f12724a.add(zzbv.GREATER_THAN_EQUALS);
        this.f12724a.add(zzbv.IDENTITY_EQUALS);
        this.f12724a.add(zzbv.IDENTITY_NOT_EQUALS);
        this.f12724a.add(zzbv.LESS_THAN);
        this.f12724a.add(zzbv.LESS_THAN_EQUALS);
        this.f12724a.add(zzbv.NOT_EQUALS);
    }

    private static boolean c(r rVar, r rVar2) {
        r jVar;
        r jVar2;
        while (!rVar.getClass().equals(rVar2.getClass())) {
            if (((rVar instanceof y) || (rVar instanceof p)) && ((rVar2 instanceof y) || (rVar2 instanceof p))) {
                return true;
            }
            boolean z4 = rVar instanceof j;
            if (z4 && (rVar2 instanceof t)) {
                jVar2 = new j(rVar2.d());
            } else {
                boolean z8 = rVar instanceof t;
                if (z8 && (rVar2 instanceof j)) {
                    jVar = new j(rVar.d());
                } else if (rVar instanceof h) {
                    jVar = new j(rVar.d());
                } else if (rVar2 instanceof h) {
                    jVar2 = new j(rVar2.d());
                } else if ((z8 || z4) && (rVar2 instanceof l)) {
                    jVar2 = new t(rVar2.e());
                } else if (!(rVar instanceof l) || (!(rVar2 instanceof t) && !(rVar2 instanceof j))) {
                    return false;
                } else {
                    jVar = new t(rVar.e());
                }
                rVar = jVar;
            }
            rVar2 = jVar2;
        }
        if ((rVar instanceof y) || (rVar instanceof p)) {
            return true;
        }
        return rVar instanceof j ? (Double.isNaN(rVar.d().doubleValue()) || Double.isNaN(rVar2.d().doubleValue()) || rVar.d().doubleValue() != rVar2.d().doubleValue()) ? false : true : rVar instanceof t ? rVar.e().equals(rVar2.e()) : rVar instanceof h ? rVar.b().equals(rVar2.b()) : rVar == rVar2;
    }

    private static boolean d(r rVar, r rVar2) {
        if (rVar instanceof l) {
            rVar = new t(rVar.e());
        }
        if (rVar2 instanceof l) {
            rVar2 = new t(rVar2.e());
        }
        if ((rVar instanceof t) && (rVar2 instanceof t)) {
            return rVar.e().compareTo(rVar2.e()) < 0;
        }
        double doubleValue = rVar.d().doubleValue();
        double doubleValue2 = rVar2.d().doubleValue();
        return (Double.isNaN(doubleValue) || Double.isNaN(doubleValue2) || (doubleValue == 0.0d && doubleValue2 == -0.0d) || ((doubleValue == -0.0d && doubleValue2 == 0.0d) || Double.compare(doubleValue, doubleValue2) >= 0)) ? false : true;
    }

    private static boolean e(r rVar, r rVar2) {
        if (rVar instanceof l) {
            rVar = new t(rVar.e());
        }
        if (rVar2 instanceof l) {
            rVar2 = new t(rVar2.e());
        }
        return (((rVar instanceof t) && (rVar2 instanceof t)) || !(Double.isNaN(rVar.d().doubleValue()) || Double.isNaN(rVar2.d().doubleValue()))) && !d(rVar2, rVar);
    }

    @Override // com.google.android.gms.internal.measurement.z
    public final r b(String str, g6 g6Var, List<r> list) {
        boolean c9;
        boolean h8;
        e5.f(e5.c(str), 2, list);
        r b9 = g6Var.b(list.get(0));
        r b10 = g6Var.b(list.get(1));
        switch (f0.f12171a[e5.c(str).ordinal()]) {
            case 1:
                c9 = c(b9, b10);
                break;
            case 2:
                c9 = d(b10, b9);
                break;
            case 3:
                c9 = e(b10, b9);
                break;
            case 4:
                c9 = e5.h(b9, b10);
                break;
            case 5:
                h8 = e5.h(b9, b10);
                c9 = !h8;
                break;
            case 6:
                c9 = d(b9, b10);
                break;
            case 7:
                c9 = e(b9, b10);
                break;
            case 8:
                h8 = c(b9, b10);
                c9 = !h8;
                break;
            default:
                return super.a(str);
        }
        return c9 ? r.I : r.J;
    }
}
