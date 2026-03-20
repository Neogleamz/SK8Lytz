package com.google.common.collect;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class v0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(int i8, double d8) {
        int max = Math.max(i8, 2);
        int highestOneBit = Integer.highestOneBit(max);
        if (max > ((int) (d8 * highestOneBit))) {
            int i9 = highestOneBit << 1;
            if (i9 > 0) {
                return i9;
            }
            return 1073741824;
        }
        return highestOneBit;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b(int i8, int i9, double d8) {
        return ((double) i8) > d8 * ((double) i9) && i9 < 1073741824;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(int i8) {
        return (int) (Integer.rotateLeft((int) (i8 * (-862048943)), 15) * 461845907);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int d(Object obj) {
        return c(obj == null ? 0 : obj.hashCode());
    }
}
