package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e5 {
    public static double a(double d8) {
        int i8;
        if (Double.isNaN(d8)) {
            return 0.0d;
        }
        if (Double.isInfinite(d8) || d8 == 0.0d || d8 == -0.0d) {
            return d8;
        }
        return (i8 > 0 ? 1 : -1) * Math.floor(Math.abs(d8));
    }

    public static int b(g6 g6Var) {
        int i8 = i(g6Var.c("runtime.counter").d().doubleValue() + 1.0d);
        if (i8 <= 1000000) {
            g6Var.h("runtime.counter", new j(Double.valueOf(i8)));
            return i8;
        }
        throw new IllegalStateException("Instructions allowed exceeded");
    }

    public static zzbv c(String str) {
        zzbv c9 = (str == null || str.isEmpty()) ? null : zzbv.c(Integer.parseInt(str));
        if (c9 != null) {
            return c9;
        }
        throw new IllegalArgumentException(String.format("Unsupported commandId %s", str));
    }

    public static Object d(r rVar) {
        if (r.f12464s.equals(rVar)) {
            return null;
        }
        if (r.f12463r.equals(rVar)) {
            return BuildConfig.FLAVOR;
        }
        if (rVar instanceof q) {
            return e((q) rVar);
        }
        if (!(rVar instanceof g)) {
            return !rVar.d().isNaN() ? rVar.d() : rVar.e();
        }
        ArrayList arrayList = new ArrayList();
        Iterator<r> it = ((g) rVar).iterator();
        while (it.hasNext()) {
            Object d8 = d(it.next());
            if (d8 != null) {
                arrayList.add(d8);
            }
        }
        return arrayList;
    }

    public static Map<String, Object> e(q qVar) {
        HashMap hashMap = new HashMap();
        for (String str : qVar.c()) {
            Object d8 = d(qVar.h(str));
            if (d8 != null) {
                hashMap.put(str, d8);
            }
        }
        return hashMap;
    }

    public static void f(zzbv zzbvVar, int i8, List<r> list) {
        g(zzbvVar.name(), i8, list);
    }

    public static void g(String str, int i8, List<r> list) {
        if (list.size() != i8) {
            throw new IllegalArgumentException(String.format("%s operation requires %s parameters found %s", str, Integer.valueOf(i8), Integer.valueOf(list.size())));
        }
    }

    public static boolean h(r rVar, r rVar2) {
        if (rVar.getClass().equals(rVar2.getClass())) {
            if ((rVar instanceof y) || (rVar instanceof p)) {
                return true;
            }
            if (!(rVar instanceof j)) {
                return rVar instanceof t ? rVar.e().equals(rVar2.e()) : rVar instanceof h ? rVar.b().equals(rVar2.b()) : rVar == rVar2;
            } else if (Double.isNaN(rVar.d().doubleValue()) || Double.isNaN(rVar2.d().doubleValue())) {
                return false;
            } else {
                return rVar.d().equals(rVar2.d());
            }
        }
        return false;
    }

    public static int i(double d8) {
        int i8;
        if (Double.isNaN(d8) || Double.isInfinite(d8) || d8 == 0.0d) {
            return 0;
        }
        return (int) (((i8 > 0 ? 1 : -1) * Math.floor(Math.abs(d8))) % 4.294967296E9d);
    }

    public static void j(zzbv zzbvVar, int i8, List<r> list) {
        k(zzbvVar.name(), i8, list);
    }

    public static void k(String str, int i8, List<r> list) {
        if (list.size() < i8) {
            throw new IllegalArgumentException(String.format("%s operation requires at least %s parameters found %s", str, Integer.valueOf(i8), Integer.valueOf(list.size())));
        }
    }

    public static boolean l(r rVar) {
        if (rVar == null) {
            return false;
        }
        Double d8 = rVar.d();
        return !d8.isNaN() && d8.doubleValue() >= 0.0d && d8.equals(Double.valueOf(Math.floor(d8.doubleValue())));
    }

    public static long m(double d8) {
        return i(d8) & 4294967295L;
    }

    public static void n(String str, int i8, List<r> list) {
        if (list.size() > i8) {
            throw new IllegalArgumentException(String.format("%s operation requires at most %s parameters found %s", str, Integer.valueOf(i8), Integer.valueOf(list.size())));
        }
    }
}
