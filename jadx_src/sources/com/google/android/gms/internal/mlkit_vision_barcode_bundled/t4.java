package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t4 {

    /* renamed from: a  reason: collision with root package name */
    private static final Class f14861a;

    /* renamed from: b  reason: collision with root package name */
    private static final h5 f14862b;

    /* renamed from: c  reason: collision with root package name */
    private static final h5 f14863c;

    /* renamed from: d  reason: collision with root package name */
    public static final /* synthetic */ int f14864d = 0;

    static {
        Class<?> cls;
        Class<?> cls2;
        h5 h5Var = null;
        try {
            cls = Class.forName("com.google.protobuf.GeneratedMessage");
        } catch (Throwable unused) {
            cls = null;
        }
        f14861a = cls;
        try {
            cls2 = Class.forName("com.google.protobuf.UnknownFieldSetSchema");
        } catch (Throwable unused2) {
            cls2 = null;
        }
        if (cls2 != null) {
            try {
                h5Var = (h5) cls2.getConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (Throwable unused3) {
            }
        }
        f14862b = h5Var;
        f14863c = new k5();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int A(List list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof q2) {
            q2 q2Var = (q2) list;
            i8 = 0;
            while (i9 < size) {
                i8 += w1.w(q2Var.g(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += w1.w(((Integer) list.get(i9)).intValue());
                i9++;
            }
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int B(int i8, List list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * (w1.A(i8 << 3) + 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int C(List list) {
        return list.size() * 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int D(int i8, List list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * (w1.A(i8 << 3) + 8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int E(List list) {
        return list.size() * 8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int F(int i8, List list, r4 r4Var) {
        int size = list.size();
        if (size != 0) {
            int i9 = 0;
            for (int i10 = 0; i10 < size; i10++) {
                i9 += w1.v(i8, (x3) list.get(i10), r4Var);
            }
            return i9;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int G(int i8, List list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return H(list) + (size * w1.A(i8 << 3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int H(List list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof q2) {
            q2 q2Var = (q2) list;
            i8 = 0;
            while (i9 < size) {
                i8 += w1.w(q2Var.g(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += w1.w(((Integer) list.get(i9)).intValue());
                i9++;
            }
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int I(int i8, List list, boolean z4) {
        if (list.size() == 0) {
            return 0;
        }
        return J(list) + (list.size() * w1.A(i8 << 3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int J(List list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof n3) {
            n3 n3Var = (n3) list;
            i8 = 0;
            while (i9 < size) {
                i8 += w1.B(n3Var.g(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += w1.B(((Long) list.get(i9)).longValue());
                i9++;
            }
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int K(int i8, Object obj, r4 r4Var) {
        if (!(obj instanceof d3)) {
            return w1.A(i8 << 3) + w1.y((x3) obj, r4Var);
        }
        int i9 = w1.f14874d;
        int a9 = ((d3) obj).a();
        return w1.A(i8 << 3) + w1.A(a9) + a9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int L(int i8, List list, r4 r4Var) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int A = w1.A(i8 << 3) * size;
        for (int i9 = 0; i9 < size; i9++) {
            Object obj = list.get(i9);
            if (obj instanceof d3) {
                int a9 = ((d3) obj).a();
                A += w1.A(a9) + a9;
            } else {
                A += w1.y((x3) obj, r4Var);
            }
        }
        return A;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int M(int i8, List list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return N(list) + (size * w1.A(i8 << 3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int N(List list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof q2) {
            q2 q2Var = (q2) list;
            i8 = 0;
            while (i9 < size) {
                int g8 = q2Var.g(i9);
                i8 += w1.A((g8 >> 31) ^ (g8 + g8));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                int intValue = ((Integer) list.get(i9)).intValue();
                i8 += w1.A((intValue >> 31) ^ (intValue + intValue));
                i9++;
            }
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int O(int i8, List list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return P(list) + (size * w1.A(i8 << 3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int P(List list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof n3) {
            n3 n3Var = (n3) list;
            i8 = 0;
            while (i9 < size) {
                long g8 = n3Var.g(i9);
                i8 += w1.B((g8 >> 63) ^ (g8 + g8));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                long longValue = ((Long) list.get(i9)).longValue();
                i8 += w1.B((longValue >> 63) ^ (longValue + longValue));
                i9++;
            }
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int Q(int i8, List list) {
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        boolean z4 = list instanceof f3;
        int A = w1.A(i8 << 3) * size;
        if (z4) {
            f3 f3Var = (f3) list;
            while (i9 < size) {
                Object m8 = f3Var.m(i9);
                if (m8 instanceof zzdb) {
                    int i10 = ((zzdb) m8).i();
                    A += w1.A(i10) + i10;
                } else {
                    A += w1.z((String) m8);
                }
                i9++;
            }
        } else {
            while (i9 < size) {
                Object obj = list.get(i9);
                if (obj instanceof zzdb) {
                    int i11 = ((zzdb) obj).i();
                    A += w1.A(i11) + i11;
                } else {
                    A += w1.z((String) obj);
                }
                i9++;
            }
        }
        return A;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int R(int i8, List list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return S(list) + (size * w1.A(i8 << 3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int S(List list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof q2) {
            q2 q2Var = (q2) list;
            i8 = 0;
            while (i9 < size) {
                i8 += w1.A(q2Var.g(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += w1.A(((Integer) list.get(i9)).intValue());
                i9++;
            }
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int T(int i8, List list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return U(list) + (size * w1.A(i8 << 3));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int U(List list) {
        int i8;
        int size = list.size();
        int i9 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof n3) {
            n3 n3Var = (n3) list;
            i8 = 0;
            while (i9 < size) {
                i8 += w1.B(n3Var.g(i9));
                i9++;
            }
        } else {
            i8 = 0;
            while (i9 < size) {
                i8 += w1.B(((Long) list.get(i9)).longValue());
                i9++;
            }
        }
        return i8;
    }

    public static h5 V() {
        return f14862b;
    }

    public static h5 W() {
        return f14863c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object a(Object obj, int i8, int i9, Object obj2, h5 h5Var) {
        if (obj2 == null) {
            obj2 = h5Var.c(obj);
        }
        h5Var.f(obj2, i8, i9);
        return obj2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(c2 c2Var, Object obj, Object obj2) {
        g2 b9 = c2Var.b(obj2);
        if (b9.f14766a.isEmpty()) {
            return;
        }
        c2Var.c(obj).h(b9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(h5 h5Var, Object obj, Object obj2) {
        h5Var.h(obj, h5Var.e(h5Var.d(obj), h5Var.d(obj2)));
    }

    public static void d(Class cls) {
        Class cls2;
        if (!p2.class.isAssignableFrom(cls) && (cls2 = f14861a) != null && !cls2.isAssignableFrom(cls)) {
            throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
        }
    }

    public static void e(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.a(i8, list, z4);
    }

    public static void f(int i8, List list, y5 y5Var) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.c(i8, list);
    }

    public static void g(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.f(i8, list, z4);
    }

    public static void h(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.d(i8, list, z4);
    }

    public static void i(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.b(i8, list, z4);
    }

    public static void j(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.e(i8, list, z4);
    }

    public static void k(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.D(i8, list, z4);
    }

    public static void l(int i8, List list, y5 y5Var, r4 r4Var) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (int i9 = 0; i9 < list.size(); i9++) {
            ((x1) y5Var).s(i8, list.get(i9), r4Var);
        }
    }

    public static void m(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.p(i8, list, z4);
    }

    public static void n(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.z(i8, list, z4);
    }

    public static void o(int i8, List list, y5 y5Var, r4 r4Var) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (int i9 = 0; i9 < list.size(); i9++) {
            ((x1) y5Var).G(i8, list.get(i9), r4Var);
        }
    }

    public static void p(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.j(i8, list, z4);
    }

    public static void q(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.o(i8, list, z4);
    }

    public static void r(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.u(i8, list, z4);
    }

    public static void s(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.C(i8, list, z4);
    }

    public static void t(int i8, List list, y5 y5Var) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.H(i8, list);
    }

    public static void u(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.n(i8, list, z4);
    }

    public static void v(int i8, List list, y5 y5Var, boolean z4) {
        if (list == null || list.isEmpty()) {
            return;
        }
        y5Var.x(i8, list, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean w(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int x(int i8, List list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return size * (w1.A(i8 << 3) + 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int y(int i8, List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int A = size * w1.A(i8 << 3);
        for (int i9 = 0; i9 < list.size(); i9++) {
            int i10 = ((zzdb) list.get(i9)).i();
            A += w1.A(i10) + i10;
        }
        return A;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int z(int i8, List list, boolean z4) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        return A(list) + (size * w1.A(i8 << 3));
    }
}
