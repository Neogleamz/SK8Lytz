package com.google.common.primitives;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {
    public static char a(long j8) {
        char c9 = (char) j8;
        com.google.common.base.l.h(((long) c9) == j8, "Out of range: %s", j8);
        return c9;
    }

    public static boolean b(char[] cArr, char c9) {
        for (char c10 : cArr) {
            if (c10 == c9) {
                return true;
            }
        }
        return false;
    }

    public static char c(byte b9, byte b10) {
        return (char) ((b9 << 8) | (b10 & 255));
    }
}
