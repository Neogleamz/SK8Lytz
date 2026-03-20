package com.google.common.primitives;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m {
    public static int a(long j8, long j9) {
        return i.a(c(j8), c(j9));
    }

    public static long b(long j8, long j9) {
        if (j9 < 0) {
            return a(j8, j9) < 0 ? 0L : 1L;
        } else if (j8 >= 0) {
            return j8 / j9;
        } else {
            long j10 = ((j8 >>> 1) / j9) << 1;
            return j10 + (a(j8 - (j10 * j9), j9) < 0 ? 0 : 1);
        }
    }

    private static long c(long j8) {
        return j8 ^ Long.MIN_VALUE;
    }

    public static String d(long j8) {
        return e(j8, 10);
    }

    public static String e(long j8, int i8) {
        com.google.common.base.l.f(i8 >= 2 && i8 <= 36, "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", i8);
        int i9 = (j8 > 0L ? 1 : (j8 == 0L ? 0 : -1));
        if (i9 == 0) {
            return "0";
        }
        if (i9 > 0) {
            return Long.toString(j8, i8);
        }
        int i10 = 64;
        char[] cArr = new char[64];
        int i11 = i8 - 1;
        if ((i8 & i11) == 0) {
            int numberOfTrailingZeros = Integer.numberOfTrailingZeros(i8);
            do {
                i10--;
                cArr[i10] = Character.forDigit(((int) j8) & i11, i8);
                j8 >>>= numberOfTrailingZeros;
            } while (j8 != 0);
        } else {
            long b9 = (i8 & 1) == 0 ? (j8 >>> 1) / (i8 >>> 1) : b(j8, i8);
            long j9 = i8;
            cArr[63] = Character.forDigit((int) (j8 - (b9 * j9)), i8);
            i10 = 63;
            while (b9 > 0) {
                i10--;
                cArr[i10] = Character.forDigit((int) (b9 % j9), i8);
                b9 /= j9;
            }
        }
        return new String(cArr, i10, 64 - i10);
    }
}
