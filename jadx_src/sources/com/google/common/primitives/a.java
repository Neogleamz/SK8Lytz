package com.google.common.primitives;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {
    public static int a(boolean z4, boolean z8) {
        if (z4 == z8) {
            return 0;
        }
        return z4 ? 1 : -1;
    }

    public static boolean b(boolean[] zArr, boolean z4) {
        for (boolean z8 : zArr) {
            if (z8 == z4) {
                return true;
            }
        }
        return false;
    }
}
