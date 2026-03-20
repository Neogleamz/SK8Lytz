package com.google.common.primitives;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l {
    public static int a(int i8, int i9) {
        return g.e(b(i8), b(i9));
    }

    static int b(int i8) {
        return i8 ^ Integer.MIN_VALUE;
    }

    public static long c(int i8) {
        return i8 & 4294967295L;
    }

    public static String d(int i8, int i9) {
        return Long.toString(i8 & 4294967295L, i9);
    }
}
