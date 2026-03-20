package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class ab {

    /* renamed from: a  reason: collision with root package name */
    private static final Class<?> f12074a = E();

    /* renamed from: b  reason: collision with root package name */
    private static final tb<?, ?> f12075b = A();

    /* renamed from: c  reason: collision with root package name */
    private static final tb<?, ?> f12076c = new ub();

    private static tb<?, ?> A() {
        try {
            Class<?> I = I();
            if (I == null) {
                return null;
            }
            return (tb) I.getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static void B(int i8, List<Integer> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.a(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int C(int i8, List<?> list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzja.i(i8, 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int D(List<?> list) {
        return list.size() << 3;
    }

    private static Class<?> E() {
        try {
            return Class.forName("com.google.protobuf.GeneratedMessage");
        } catch (Throwable unused) {
            return null;
        }
    }

    public static void F(int i8, List<Integer> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.D(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int G(int i8, List<Integer> list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return H(list) + (size * zzja.k0(i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int H(List<Integer> list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof y8) {
            y8 y8Var = (y8) list;
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.T(y8Var.g(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.T(list.get(i9).intValue());
                i9++;
            }
        }
        return i8;
    }

    private static Class<?> I() {
        try {
            return Class.forName("com.google.protobuf.UnknownFieldSetSchema");
        } catch (Throwable unused) {
            return null;
        }
    }

    public static void J(int i8, List<Long> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.z(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int K(int i8, List<Long> list, boolean z4) {
        if (list.size() == 0) {
            return 0;
        }
        return L(list) + (list.size() * zzja.k0(i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int L(List<Long> list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof t9) {
            t9 t9Var = (t9) list;
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.F(t9Var.j(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.F(list.get(i9).longValue());
                i9++;
            }
        }
        return i8;
    }

    public static void M(int i8, List<Float> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.K(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int N(int i8, List<Integer> list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return O(list) + (size * zzja.k0(i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int O(List<Integer> list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof y8) {
            y8 y8Var = (y8) list;
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.g0(y8Var.g(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.g0(list.get(i9).intValue());
                i9++;
            }
        }
        return i8;
    }

    public static void P(int i8, List<Integer> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.f(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int Q(int i8, List<Long> list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return R(list) + (size * zzja.k0(i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int R(List<Long> list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof t9) {
            t9 t9Var = (t9) list;
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.e0(t9Var.j(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.e0(list.get(i9).longValue());
                i9++;
            }
        }
        return i8;
    }

    public static void S(int i8, List<Long> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.n(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int T(int i8, List<Integer> list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return U(list) + (size * zzja.k0(i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int U(List<Integer> list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof y8) {
            y8 y8Var = (y8) list;
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.o0(y8Var.g(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.o0(list.get(i9).intValue());
                i9++;
            }
        }
        return i8;
    }

    public static void V(int i8, List<Integer> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.h(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int W(int i8, List<Long> list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return X(list) + (size * zzja.k0(i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int X(List<Long> list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof t9) {
            t9 t9Var = (t9) list;
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.j0(t9Var.j(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.j0(list.get(i9).longValue());
                i9++;
            }
        }
        return i8;
    }

    public static void Y(int i8, List<Long> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.d(i8, list, z4);
    }

    public static void Z(int i8, List<Integer> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.u(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(int i8, Object obj, xa xaVar) {
        return obj instanceof m9 ? zzja.D(i8, (m9) obj) : zzja.E(i8, (ia) obj, xaVar);
    }

    public static void a0(int i8, List<Long> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.b(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(int i8, List<zzij> list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int k02 = size * zzja.k0(i8);
        for (int i9 = 0; i9 < list.size(); i9++) {
            k02 += zzja.q(list.get(i9));
        }
        return k02;
    }

    public static void b0(int i8, List<Integer> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.F(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(int i8, List<ia> list, xa xaVar) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i9 = 0;
        for (int i10 = 0; i10 < size; i10++) {
            i9 += zzja.m(i8, list.get(i10), xaVar);
        }
        return i9;
    }

    public static void c0(int i8, List<Long> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.e(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int d(int i8, List<?> list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzja.o(i8, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int e(List<?> list) {
        return list.size();
    }

    public static tb<?, ?> f() {
        return f12075b;
    }

    static <UT, UB> UB g(Object obj, int i8, int i9, UB ub, tb<UT, UB> tbVar) {
        if (ub == null) {
            ub = tbVar.i(obj);
        }
        tbVar.f(ub, i8, i9);
        return ub;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <UT, UB> UB h(Object obj, int i8, List<Integer> list, b9 b9Var, UB ub, tb<UT, UB> tbVar) {
        if (b9Var == null) {
            return ub;
        }
        if (list instanceof RandomAccess) {
            int size = list.size();
            int i9 = 0;
            for (int i10 = 0; i10 < size; i10++) {
                int intValue = list.get(i10).intValue();
                if (b9Var.c(intValue)) {
                    if (i10 != i9) {
                        list.set(i9, Integer.valueOf(intValue));
                    }
                    i9++;
                } else {
                    ub = (UB) g(obj, i8, intValue, ub, tbVar);
                }
            }
            if (i9 != size) {
                list.subList(i9, size).clear();
            }
        } else {
            Iterator<Integer> it = list.iterator();
            while (it.hasNext()) {
                int intValue2 = it.next().intValue();
                if (!b9Var.c(intValue2)) {
                    ub = (UB) g(obj, i8, intValue2, ub, tbVar);
                    it.remove();
                }
            }
        }
        return ub;
    }

    public static void i(int i8, List<zzij> list, rc rcVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.N(i8, list);
    }

    public static void j(int i8, List<?> list, rc rcVar, xa xaVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.E(i8, list, xaVar);
    }

    public static void k(int i8, List<Boolean> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.l(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T, FT extends q8<FT>> void l(n8<FT> n8Var, T t8, T t9) {
        o8<FT> b9 = n8Var.b(t9);
        if (b9.f12402a.isEmpty()) {
            return;
        }
        n8Var.f(t8).h(b9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> void m(ba baVar, T t8, T t9, long j8) {
        yb.j(t8, j8, baVar.g(yb.B(t8, j8), yb.B(t9, j8)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T, UT, UB> void n(tb<UT, UB> tbVar, T t8, T t9) {
        tbVar.j(t8, tbVar.b(tbVar.k(t8), tbVar.k(t9)));
    }

    public static void o(Class<?> cls) {
        Class<?> cls2;
        if (!x8.class.isAssignableFrom(cls) && (cls2 = f12074a) != null && !cls2.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean p(Object obj, Object obj2) {
        if (obj != obj2) {
            return obj != null && obj.equals(obj2);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int q(int i8, List<?> list) {
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        int k02 = zzja.k0(i8) * size;
        if (list instanceof o9) {
            o9 o9Var = (o9) list;
            while (i9 < size) {
                Object j8 = o9Var.j(i9);
                k02 += j8 instanceof zzij ? zzja.q((zzij) j8) : zzja.u((String) j8);
                i9++;
            }
        } else {
            while (i9 < size) {
                Object obj = list.get(i9);
                k02 += obj instanceof zzij ? zzja.q((zzij) obj) : zzja.u((String) obj);
                i9++;
            }
        }
        return k02;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int r(int i8, List<?> list, xa xaVar) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int k02 = zzja.k0(i8) * size;
        for (int i9 = 0; i9 < size; i9++) {
            Object obj = list.get(i9);
            k02 += obj instanceof m9 ? zzja.r((m9) obj) : zzja.t((ia) obj, xaVar);
        }
        return k02;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int s(int i8, List<Integer> list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return t(list) + (size * zzja.k0(i8));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int t(List<Integer> list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof y8) {
            y8 y8Var = (y8) list;
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.e(y8Var.g(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += zzja.e(list.get(i9).intValue());
                i9++;
            }
        }
        return i8;
    }

    public static tb<?, ?> u() {
        return f12076c;
    }

    public static void v(int i8, List<String> list, rc rcVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.H(i8, list);
    }

    public static void w(int i8, List<?> list, rc rcVar, xa xaVar) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.A(i8, list, xaVar);
    }

    public static void x(int i8, List<Double> list, rc rcVar, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        rcVar.s(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int y(int i8, List<?> list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * zzja.A(i8, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int z(List<?> list) {
        return list.size() << 2;
    }
}
