package com.google.common.primitives;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i {
    public static int a(long j8, long j9) {
        int i8 = (j8 > j9 ? 1 : (j8 == j9 ? 0 : -1));
        if (i8 < 0) {
            return -1;
        }
        return i8 > 0 ? 1 : 0;
    }

    public static int b(long j8) {
        return (int) (j8 ^ (j8 >>> 32));
    }

    public static long c(long... jArr) {
        com.google.common.base.l.d(jArr.length > 0);
        long j8 = jArr[0];
        for (int i8 = 1; i8 < jArr.length; i8++) {
            if (jArr[i8] > j8) {
                j8 = jArr[i8];
            }
        }
        return j8;
    }
}
