package b6;

import android.text.TextUtils;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {
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

    public static int c(int i8, int i9, int i10) {
        if (i8 < i9 || i8 >= i10) {
            throw new IndexOutOfBoundsException();
        }
        return i8;
    }

    public static String d(String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException();
        }
        return str;
    }

    public static <T> T e(T t8) {
        Objects.requireNonNull(t8);
        return t8;
    }

    public static void f(boolean z4) {
        if (!z4) {
            throw new IllegalStateException();
        }
    }

    public static void g(boolean z4, Object obj) {
        if (!z4) {
            throw new IllegalStateException(String.valueOf(obj));
        }
    }

    public static <T> T h(T t8) {
        if (t8 != null) {
            return t8;
        }
        throw new IllegalStateException();
    }

    public static <T> T i(T t8, Object obj) {
        if (t8 != null) {
            return t8;
        }
        throw new IllegalStateException(String.valueOf(obj));
    }
}
