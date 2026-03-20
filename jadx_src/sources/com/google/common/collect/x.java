package com.google.common.collect;

import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class x {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object a(int i8) {
        if (i8 >= 2 && i8 <= 1073741824 && Integer.highestOneBit(i8) == i8) {
            return i8 <= 256 ? new byte[i8] : i8 <= 65536 ? new short[i8] : new int[i8];
        }
        StringBuilder sb = new StringBuilder(52);
        sb.append("must be power of 2 between 2^1 and 2^30: ");
        sb.append(i8);
        throw new IllegalArgumentException(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(int i8, int i9) {
        return i8 & (~i9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(int i8, int i9) {
        return i8 & i9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int d(int i8, int i9, int i10) {
        return (i8 & (~i10)) | (i9 & i10);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int e(int i8) {
        return (i8 < 32 ? 4 : 2) * (i8 + 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int f(Object obj, Object obj2, int i8, Object obj3, int[] iArr, Object[] objArr, Object[] objArr2) {
        int i9;
        int i10;
        int d8 = v0.d(obj);
        int i11 = d8 & i8;
        int h8 = h(obj3, i11);
        if (h8 == 0) {
            return -1;
        }
        int b9 = b(d8, i8);
        int i12 = -1;
        while (true) {
            i9 = h8 - 1;
            i10 = iArr[i9];
            if (b(i10, i8) != b9 || !com.google.common.base.k.a(obj, objArr[i9]) || (objArr2 != null && !com.google.common.base.k.a(obj2, objArr2[i9]))) {
                int c9 = c(i10, i8);
                if (c9 == 0) {
                    return -1;
                }
                i12 = i9;
                h8 = c9;
            }
        }
        int c10 = c(i10, i8);
        if (i12 == -1) {
            i(obj3, i11, c10);
        } else {
            iArr[i12] = d(iArr[i12], c10, i8);
        }
        return i9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void g(Object obj) {
        if (obj instanceof byte[]) {
            Arrays.fill((byte[]) obj, (byte) 0);
        } else if (obj instanceof short[]) {
            Arrays.fill((short[]) obj, (short) 0);
        } else {
            Arrays.fill((int[]) obj, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int h(Object obj, int i8) {
        return obj instanceof byte[] ? ((byte[]) obj)[i8] & 255 : obj instanceof short[] ? ((short[]) obj)[i8] & 65535 : ((int[]) obj)[i8];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void i(Object obj, int i8, int i9) {
        if (obj instanceof byte[]) {
            ((byte[]) obj)[i8] = (byte) i9;
        } else if (obj instanceof short[]) {
            ((short[]) obj)[i8] = (short) i9;
        } else {
            ((int[]) obj)[i8] = i9;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int j(int i8) {
        return Math.max(4, v0.a(i8 + 1, 1.0d));
    }
}
