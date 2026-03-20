package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g0 {
    private static g a(g gVar, g6 g6Var, m mVar) {
        return b(gVar, g6Var, mVar, null, null);
    }

    private static g b(g gVar, g6 g6Var, m mVar, Boolean bool, Boolean bool2) {
        g gVar2 = new g();
        Iterator<Integer> C = gVar.C();
        while (C.hasNext()) {
            int intValue = C.next().intValue();
            if (gVar.A(intValue)) {
                r c9 = mVar.c(g6Var, Arrays.asList(gVar.p(intValue), new j(Double.valueOf(intValue)), gVar));
                if (c9.b().equals(bool)) {
                    return gVar2;
                }
                if (bool2 == null || c9.b().equals(bool2)) {
                    gVar2.y(intValue, c9);
                }
            }
        }
        return gVar2;
    }

    private static r c(g gVar, g6 g6Var, List<r> list, boolean z4) {
        r rVar;
        e5.k("reduce", 1, list);
        e5.n("reduce", 2, list);
        r b9 = g6Var.b(list.get(0));
        if (b9 instanceof m) {
            if (list.size() == 2) {
                rVar = g6Var.b(list.get(1));
                if (rVar instanceof k) {
                    throw new IllegalArgumentException("Failed to parse initial value");
                }
            } else {
                rVar = null;
                if (gVar.u() == 0) {
                    throw new IllegalStateException("Empty array with no initial value error");
                }
            }
            m mVar = (m) b9;
            int u8 = gVar.u();
            int i8 = z4 ? 0 : u8 - 1;
            int i9 = z4 ? u8 - 1 : 0;
            int i10 = z4 ? 1 : -1;
            if (rVar == null) {
                rVar = gVar.p(i8);
                i8 += i10;
            }
            while ((i9 - i8) * i10 >= 0) {
                if (gVar.A(i8)) {
                    rVar = mVar.c(g6Var, Arrays.asList(rVar, gVar.p(i8), new j(Double.valueOf(i8)), gVar));
                    if (rVar instanceof k) {
                        throw new IllegalStateException("Reduce operation failed");
                    }
                    i8 += i10;
                } else {
                    i8 += i10;
                }
            }
            return rVar;
        }
        throw new IllegalArgumentException("Callback should be a method");
    }

    public static r d(String str, g gVar, g6 g6Var, List<r> list) {
        String str2;
        m mVar;
        g6 g6Var2;
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1776922004:
                if (str.equals("toString")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1354795244:
                if (str.equals("concat")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1274492040:
                if (str.equals("filter")) {
                    c9 = 2;
                    break;
                }
                break;
            case -934873754:
                if (str.equals("reduce")) {
                    c9 = 3;
                    break;
                }
                break;
            case -895859076:
                if (str.equals("splice")) {
                    c9 = 4;
                    break;
                }
                break;
            case -678635926:
                if (str.equals("forEach")) {
                    c9 = 5;
                    break;
                }
                break;
            case -467511597:
                if (str.equals("lastIndexOf")) {
                    c9 = 6;
                    break;
                }
                break;
            case -277637751:
                if (str.equals("unshift")) {
                    c9 = 7;
                    break;
                }
                break;
            case 107868:
                if (str.equals("map")) {
                    c9 = '\b';
                    break;
                }
                break;
            case 111185:
                if (str.equals("pop")) {
                    c9 = '\t';
                    break;
                }
                break;
            case 3267882:
                if (str.equals("join")) {
                    c9 = '\n';
                    break;
                }
                break;
            case 3452698:
                if (str.equals("push")) {
                    c9 = 11;
                    break;
                }
                break;
            case 3536116:
                if (str.equals("some")) {
                    c9 = '\f';
                    break;
                }
                break;
            case 3536286:
                if (str.equals("sort")) {
                    c9 = '\r';
                    break;
                }
                break;
            case 96891675:
                if (str.equals("every")) {
                    c9 = 14;
                    break;
                }
                break;
            case 109407362:
                if (str.equals("shift")) {
                    c9 = 15;
                    break;
                }
                break;
            case 109526418:
                if (str.equals("slice")) {
                    c9 = 16;
                    break;
                }
                break;
            case 965561430:
                if (str.equals("reduceRight")) {
                    c9 = 17;
                    break;
                }
                break;
            case 1099846370:
                if (str.equals("reverse")) {
                    c9 = 18;
                    break;
                }
                break;
            case 1943291465:
                if (str.equals("indexOf")) {
                    c9 = 19;
                    break;
                }
                break;
        }
        double d8 = 0.0d;
        switch (c9) {
            case 0:
                e5.g("toString", 0, list);
                return new t(gVar.toString());
            case 1:
                g gVar2 = (g) gVar.a();
                if (!list.isEmpty()) {
                    for (r rVar : list) {
                        r b9 = g6Var.b(rVar);
                        if (b9 instanceof k) {
                            throw new IllegalStateException("Failed evaluation of arguments");
                        }
                        int u8 = gVar2.u();
                        if (b9 instanceof g) {
                            g gVar3 = (g) b9;
                            Iterator<Integer> C = gVar3.C();
                            while (C.hasNext()) {
                                Integer next = C.next();
                                gVar2.y(next.intValue() + u8, gVar3.p(next.intValue()));
                            }
                        } else {
                            gVar2.y(u8, b9);
                        }
                    }
                }
                return gVar2;
            case 2:
                e5.g("filter", 1, list);
                r b10 = g6Var.b(list.get(0));
                if (b10 instanceof s) {
                    if (gVar.i() == 0) {
                        return new g();
                    }
                    g gVar4 = (g) gVar.a();
                    g b11 = b(gVar, g6Var, (s) b10, null, Boolean.TRUE);
                    g gVar5 = new g();
                    Iterator<Integer> C2 = b11.C();
                    while (C2.hasNext()) {
                        gVar5.t(gVar4.p(C2.next().intValue()));
                    }
                    return gVar5;
                }
                throw new IllegalArgumentException("Callback should be a method");
            case 3:
                return c(gVar, g6Var, list, true);
            case 4:
                if (list.isEmpty()) {
                    return new g();
                }
                int a9 = (int) e5.a(g6Var.b(list.get(0)).d().doubleValue());
                if (a9 < 0) {
                    a9 = Math.max(0, a9 + gVar.u());
                } else if (a9 > gVar.u()) {
                    a9 = gVar.u();
                }
                int u9 = gVar.u();
                g gVar6 = new g();
                if (list.size() <= 1) {
                    while (a9 < u9) {
                        gVar6.t(gVar.p(a9));
                        gVar.y(a9, null);
                        a9++;
                    }
                    return gVar6;
                }
                int max = Math.max(0, (int) e5.a(g6Var.b(list.get(1)).d().doubleValue()));
                if (max > 0) {
                    for (int i8 = a9; i8 < Math.min(u9, a9 + max); i8++) {
                        gVar6.t(gVar.p(a9));
                        gVar.x(a9);
                    }
                }
                if (list.size() > 2) {
                    for (int i9 = 2; i9 < list.size(); i9++) {
                        r b12 = g6Var.b(list.get(i9));
                        if (b12 instanceof k) {
                            throw new IllegalArgumentException("Failed to parse elements to add");
                        }
                        gVar.q((a9 + i9) - 2, b12);
                    }
                }
                return gVar6;
            case 5:
                e5.g("forEach", 1, list);
                r b13 = g6Var.b(list.get(0));
                if (b13 instanceof s) {
                    if (gVar.i() == 0) {
                        return r.f12463r;
                    }
                    a(gVar, g6Var, (s) b13);
                    return r.f12463r;
                }
                throw new IllegalArgumentException("Callback should be a method");
            case 6:
                e5.n("lastIndexOf", 2, list);
                r rVar2 = r.f12463r;
                if (!list.isEmpty()) {
                    rVar2 = g6Var.b(list.get(0));
                }
                double u10 = gVar.u() - 1;
                if (list.size() > 1) {
                    r b14 = g6Var.b(list.get(1));
                    u10 = Double.isNaN(b14.d().doubleValue()) ? gVar.u() - 1 : e5.a(b14.d().doubleValue());
                    if (u10 < 0.0d) {
                        u10 += gVar.u();
                    }
                }
                if (u10 < 0.0d) {
                    return new j(Double.valueOf(-1.0d));
                }
                for (int min = (int) Math.min(gVar.u(), u10); min >= 0; min--) {
                    if (gVar.A(min) && e5.h(gVar.p(min), rVar2)) {
                        return new j(Double.valueOf(min));
                    }
                }
                return new j(Double.valueOf(-1.0d));
            case 7:
                if (!list.isEmpty()) {
                    g gVar7 = new g();
                    for (r rVar3 : list) {
                        r b15 = g6Var.b(rVar3);
                        if (b15 instanceof k) {
                            throw new IllegalStateException("Argument evaluation failed");
                        }
                        gVar7.t(b15);
                    }
                    int u11 = gVar7.u();
                    Iterator<Integer> C3 = gVar.C();
                    while (C3.hasNext()) {
                        Integer next2 = C3.next();
                        gVar7.y(next2.intValue() + u11, gVar.p(next2.intValue()));
                    }
                    gVar.E();
                    Iterator<Integer> C4 = gVar7.C();
                    while (C4.hasNext()) {
                        Integer next3 = C4.next();
                        gVar.y(next3.intValue(), gVar7.p(next3.intValue()));
                    }
                }
                return new j(Double.valueOf(gVar.u()));
            case '\b':
                e5.g("map", 1, list);
                r b16 = g6Var.b(list.get(0));
                if (b16 instanceof s) {
                    return gVar.u() == 0 ? new g() : a(gVar, g6Var, (s) b16);
                }
                throw new IllegalArgumentException("Callback should be a method");
            case '\t':
                e5.g("pop", 0, list);
                int u12 = gVar.u();
                if (u12 == 0) {
                    return r.f12463r;
                }
                int i10 = u12 - 1;
                r p8 = gVar.p(i10);
                gVar.x(i10);
                return p8;
            case '\n':
                e5.n("join", 1, list);
                if (gVar.u() == 0) {
                    return r.M;
                }
                if (list.isEmpty()) {
                    str2 = ",";
                } else {
                    r b17 = g6Var.b(list.get(0));
                    str2 = ((b17 instanceof p) || (b17 instanceof y)) ? BuildConfig.FLAVOR : b17.e();
                }
                return new t(gVar.v(str2));
            case 11:
                if (!list.isEmpty()) {
                    for (r rVar4 : list) {
                        gVar.t(g6Var.b(rVar4));
                    }
                }
                return new j(Double.valueOf(gVar.u()));
            case '\f':
                e5.g("some", 1, list);
                r b18 = g6Var.b(list.get(0));
                if (b18 instanceof m) {
                    if (gVar.u() != 0) {
                        m mVar2 = (m) b18;
                        Iterator<Integer> C5 = gVar.C();
                        while (C5.hasNext()) {
                            int intValue = C5.next().intValue();
                            if (gVar.A(intValue) && mVar2.c(g6Var, Arrays.asList(gVar.p(intValue), new j(Double.valueOf(intValue)), gVar)).b().booleanValue()) {
                                return r.I;
                            }
                        }
                    }
                    return r.J;
                }
                throw new IllegalArgumentException("Callback should be a method");
            case '\r':
                e5.n("sort", 1, list);
                if (gVar.u() >= 2) {
                    List<r> D = gVar.D();
                    if (list.isEmpty()) {
                        mVar = null;
                    } else {
                        r b19 = g6Var.b(list.get(0));
                        if (!(b19 instanceof m)) {
                            throw new IllegalArgumentException("Comparator should be a method");
                        }
                        mVar = (m) b19;
                    }
                    Collections.sort(D, new j0(mVar, g6Var));
                    gVar.E();
                    int i11 = 0;
                    for (r rVar5 : D) {
                        gVar.y(i11, rVar5);
                        i11++;
                    }
                }
                return gVar;
            case 14:
                e5.g("every", 1, list);
                r b20 = g6Var.b(list.get(0));
                if (b20 instanceof s) {
                    return (gVar.u() == 0 || b(gVar, g6Var, (s) b20, Boolean.FALSE, Boolean.TRUE).u() == gVar.u()) ? r.I : r.J;
                }
                throw new IllegalArgumentException("Callback should be a method");
            case 15:
                e5.g("shift", 0, list);
                if (gVar.u() == 0) {
                    return r.f12463r;
                }
                r p9 = gVar.p(0);
                gVar.x(0);
                return p9;
            case 16:
                e5.n("slice", 2, list);
                if (list.isEmpty()) {
                    return gVar.a();
                }
                double u13 = gVar.u();
                double a10 = e5.a(g6Var.b(list.get(0)).d().doubleValue());
                double max2 = a10 < 0.0d ? Math.max(a10 + u13, 0.0d) : Math.min(a10, u13);
                if (list.size() == 2) {
                    double a11 = e5.a(g6Var.b(list.get(1)).d().doubleValue());
                    u13 = a11 < 0.0d ? Math.max(u13 + a11, 0.0d) : Math.min(u13, a11);
                }
                g gVar8 = new g();
                for (int i12 = (int) max2; i12 < u13; i12++) {
                    gVar8.t(gVar.p(i12));
                }
                return gVar8;
            case 17:
                return c(gVar, g6Var, list, false);
            case 18:
                e5.g("reverse", 0, list);
                int u14 = gVar.u();
                if (u14 != 0) {
                    for (int i13 = 0; i13 < u14 / 2; i13++) {
                        if (gVar.A(i13)) {
                            r p10 = gVar.p(i13);
                            gVar.y(i13, null);
                            int i14 = (u14 - 1) - i13;
                            if (gVar.A(i14)) {
                                gVar.y(i13, gVar.p(i14));
                            }
                            gVar.y(i14, p10);
                        }
                    }
                }
                return gVar;
            case 19:
                e5.n("indexOf", 2, list);
                r rVar6 = r.f12463r;
                if (list.isEmpty()) {
                    g6Var2 = g6Var;
                } else {
                    g6Var2 = g6Var;
                    rVar6 = g6Var2.b(list.get(0));
                }
                if (list.size() > 1) {
                    double a12 = e5.a(g6Var2.b(list.get(1)).d().doubleValue());
                    if (a12 >= gVar.u()) {
                        return new j(Double.valueOf(-1.0d));
                    }
                    d8 = a12 < 0.0d ? gVar.u() + a12 : a12;
                }
                Iterator<Integer> C6 = gVar.C();
                while (C6.hasNext()) {
                    int intValue2 = C6.next().intValue();
                    double d9 = intValue2;
                    if (d9 >= d8 && e5.h(gVar.p(intValue2), rVar6)) {
                        return new j(Double.valueOf(d9));
                    }
                }
                return new j(Double.valueOf(-1.0d));
            default:
                throw new IllegalArgumentException("Command not supported");
        }
    }
}
