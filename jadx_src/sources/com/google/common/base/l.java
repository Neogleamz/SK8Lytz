package com.google.common.base;

import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l {
    private static String a(int i8, int i9, String str) {
        if (i8 < 0) {
            return q.a("%s (%s) must not be negative", str, Integer.valueOf(i8));
        }
        if (i9 >= 0) {
            return q.a("%s (%s) must be less than size (%s)", str, Integer.valueOf(i8), Integer.valueOf(i9));
        }
        StringBuilder sb = new StringBuilder(26);
        sb.append("negative size: ");
        sb.append(i9);
        throw new IllegalArgumentException(sb.toString());
    }

    private static String b(int i8, int i9, String str) {
        if (i8 < 0) {
            return q.a("%s (%s) must not be negative", str, Integer.valueOf(i8));
        }
        if (i9 >= 0) {
            return q.a("%s (%s) must not be greater than size (%s)", str, Integer.valueOf(i8), Integer.valueOf(i9));
        }
        StringBuilder sb = new StringBuilder(26);
        sb.append("negative size: ");
        sb.append(i9);
        throw new IllegalArgumentException(sb.toString());
    }

    private static String c(int i8, int i9, int i10) {
        return (i8 < 0 || i8 > i10) ? b(i8, i10, "start index") : (i9 < 0 || i9 > i10) ? b(i9, i10, "end index") : q.a("end index (%s) must not be less than start index (%s)", Integer.valueOf(i9), Integer.valueOf(i8));
    }

    public static void d(boolean z4) {
        if (!z4) {
            throw new IllegalArgumentException();
        }
    }

    public static void e(boolean z4, Object obj) {
        if (!z4) {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
    }

    public static void f(boolean z4, String str, int i8) {
        if (!z4) {
            throw new IllegalArgumentException(q.a(str, Integer.valueOf(i8)));
        }
    }

    public static void g(boolean z4, String str, int i8, int i9) {
        if (!z4) {
            throw new IllegalArgumentException(q.a(str, Integer.valueOf(i8), Integer.valueOf(i9)));
        }
    }

    public static void h(boolean z4, String str, long j8) {
        if (!z4) {
            throw new IllegalArgumentException(q.a(str, Long.valueOf(j8)));
        }
    }

    public static void i(boolean z4, String str, Object obj) {
        if (!z4) {
            throw new IllegalArgumentException(q.a(str, obj));
        }
    }

    public static void j(boolean z4, String str, Object obj, Object obj2) {
        if (!z4) {
            throw new IllegalArgumentException(q.a(str, obj, obj2));
        }
    }

    public static void k(boolean z4, String str, Object obj, Object obj2, Object obj3, Object obj4) {
        if (!z4) {
            throw new IllegalArgumentException(q.a(str, obj, obj2, obj3, obj4));
        }
    }

    public static int l(int i8, int i9) {
        return m(i8, i9, "index");
    }

    public static int m(int i8, int i9, String str) {
        if (i8 < 0 || i8 >= i9) {
            throw new IndexOutOfBoundsException(a(i8, i9, str));
        }
        return i8;
    }

    public static <T> T n(T t8) {
        Objects.requireNonNull(t8);
        return t8;
    }

    public static <T> T o(T t8, Object obj) {
        if (t8 != null) {
            return t8;
        }
        throw new NullPointerException(String.valueOf(obj));
    }

    public static int p(int i8, int i9) {
        return q(i8, i9, "index");
    }

    public static int q(int i8, int i9, String str) {
        if (i8 < 0 || i8 > i9) {
            throw new IndexOutOfBoundsException(b(i8, i9, str));
        }
        return i8;
    }

    public static void r(int i8, int i9, int i10) {
        if (i8 < 0 || i9 < i8 || i9 > i10) {
            throw new IndexOutOfBoundsException(c(i8, i9, i10));
        }
    }

    public static void s(boolean z4) {
        if (!z4) {
            throw new IllegalStateException();
        }
    }

    public static void t(boolean z4, Object obj) {
        if (!z4) {
            throw new IllegalStateException(String.valueOf(obj));
        }
    }

    public static void u(boolean z4, String str, int i8) {
        if (!z4) {
            throw new IllegalStateException(q.a(str, Integer.valueOf(i8)));
        }
    }

    public static void v(boolean z4, String str, Object obj) {
        if (!z4) {
            throw new IllegalStateException(q.a(str, obj));
        }
    }
}
