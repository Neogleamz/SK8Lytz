package androidx.core.view;

import android.os.Build;
import android.view.ViewGroup;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static int a(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.getLayoutDirection();
        }

        static int b(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.getMarginEnd();
        }

        static int c(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.getMarginStart();
        }

        static boolean d(ViewGroup.MarginLayoutParams marginLayoutParams) {
            return marginLayoutParams.isMarginRelative();
        }

        static void e(ViewGroup.MarginLayoutParams marginLayoutParams, int i8) {
            marginLayoutParams.resolveLayoutDirection(i8);
        }

        static void f(ViewGroup.MarginLayoutParams marginLayoutParams, int i8) {
            marginLayoutParams.setLayoutDirection(i8);
        }

        static void g(ViewGroup.MarginLayoutParams marginLayoutParams, int i8) {
            marginLayoutParams.setMarginEnd(i8);
        }

        static void h(ViewGroup.MarginLayoutParams marginLayoutParams, int i8) {
            marginLayoutParams.setMarginStart(i8);
        }
    }

    public static int a(ViewGroup.MarginLayoutParams marginLayoutParams) {
        return Build.VERSION.SDK_INT >= 17 ? a.b(marginLayoutParams) : marginLayoutParams.rightMargin;
    }

    public static int b(ViewGroup.MarginLayoutParams marginLayoutParams) {
        return Build.VERSION.SDK_INT >= 17 ? a.c(marginLayoutParams) : marginLayoutParams.leftMargin;
    }

    public static void c(ViewGroup.MarginLayoutParams marginLayoutParams, int i8) {
        if (Build.VERSION.SDK_INT >= 17) {
            a.g(marginLayoutParams, i8);
        } else {
            marginLayoutParams.rightMargin = i8;
        }
    }

    public static void d(ViewGroup.MarginLayoutParams marginLayoutParams, int i8) {
        if (Build.VERSION.SDK_INT >= 17) {
            a.h(marginLayoutParams, i8);
        } else {
            marginLayoutParams.leftMargin = i8;
        }
    }
}
