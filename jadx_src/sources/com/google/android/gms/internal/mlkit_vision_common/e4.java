package com.google.android.gms.internal.mlkit_vision_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e4 {
    public static int a(int i8, int i9, String str) {
        String a9;
        if (i8 < 0 || i8 >= i9) {
            if (i8 < 0) {
                a9 = f5.a("%s (%s) must not be negative", "index", Integer.valueOf(i8));
            } else if (i9 < 0) {
                throw new IllegalArgumentException("negative size: " + i9);
            } else {
                a9 = f5.a("%s (%s) must be less than size (%s)", "index", Integer.valueOf(i8), Integer.valueOf(i9));
            }
            throw new IndexOutOfBoundsException(a9);
        }
        return i8;
    }

    public static int b(int i8, int i9, String str) {
        if (i8 < 0 || i8 > i9) {
            throw new IndexOutOfBoundsException(d(i8, i9, "index"));
        }
        return i8;
    }

    public static void c(int i8, int i9, int i10) {
        if (i8 < 0 || i9 < i8 || i9 > i10) {
            throw new IndexOutOfBoundsException((i8 < 0 || i8 > i10) ? d(i8, i10, "start index") : (i9 < 0 || i9 > i10) ? d(i9, i10, "end index") : f5.a("end index (%s) must not be less than start index (%s)", Integer.valueOf(i9), Integer.valueOf(i8)));
        }
    }

    private static String d(int i8, int i9, String str) {
        if (i8 < 0) {
            return f5.a("%s (%s) must not be negative", str, Integer.valueOf(i8));
        }
        if (i9 >= 0) {
            return f5.a("%s (%s) must not be greater than size (%s)", str, Integer.valueOf(i8), Integer.valueOf(i9));
        }
        throw new IllegalArgumentException("negative size: " + i9);
    }
}
