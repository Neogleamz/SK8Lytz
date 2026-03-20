package com.google.android.gms.internal.mlkit_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(int i8, int i9) {
        if (i9 >= 0) {
            int i10 = i8 + (i8 >> 1) + 1;
            if (i10 < i9) {
                int highestOneBit = Integer.highestOneBit(i9 - 1);
                i10 = highestOneBit + highestOneBit;
            }
            if (i10 < 0) {
                return Integer.MAX_VALUE;
            }
            return i10;
        }
        throw new AssertionError("cannot store more than MAX_VALUE elements");
    }
}
