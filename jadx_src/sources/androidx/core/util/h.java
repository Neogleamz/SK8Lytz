package androidx.core.util;

import java.util.Locale;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {
    public static void a(boolean z4) {
        if (!z4) {
            throw new IllegalArgumentException();
        }
    }

    public static void b(boolean z4, Object obj) {
        if (!z4) {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
    }

    public static float c(float f5, String str) {
        if (Float.isNaN(f5)) {
            throw new IllegalArgumentException(str + " must not be NaN");
        } else if (Float.isInfinite(f5)) {
            throw new IllegalArgumentException(str + " must not be infinite");
        } else {
            return f5;
        }
    }

    public static int d(int i8, int i9, int i10, String str) {
        if (i8 >= i9) {
            if (i8 <= i10) {
                return i8;
            }
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", str, Integer.valueOf(i9), Integer.valueOf(i10)));
        }
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", str, Integer.valueOf(i9), Integer.valueOf(i10)));
    }

    public static int e(int i8) {
        if (i8 >= 0) {
            return i8;
        }
        throw new IllegalArgumentException();
    }

    public static int f(int i8, String str) {
        if (i8 >= 0) {
            return i8;
        }
        throw new IllegalArgumentException(str);
    }

    public static int g(int i8, int i9) {
        if ((i8 & i9) == i8) {
            return i8;
        }
        throw new IllegalArgumentException("Requested flags 0x" + Integer.toHexString(i8) + ", but only 0x" + Integer.toHexString(i9) + " are allowed");
    }

    public static <T> T h(T t8) {
        Objects.requireNonNull(t8);
        return t8;
    }

    public static <T> T i(T t8, Object obj) {
        if (t8 != null) {
            return t8;
        }
        throw new NullPointerException(String.valueOf(obj));
    }

    public static void j(boolean z4) {
        k(z4, null);
    }

    public static void k(boolean z4, String str) {
        if (!z4) {
            throw new IllegalStateException(str);
        }
    }
}
