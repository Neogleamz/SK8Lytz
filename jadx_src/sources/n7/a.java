package n7;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import androidx.core.graphics.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {
    public static int a(int i8, int i9) {
        return b.p(i8, (Color.alpha(i8) * i9) / 255);
    }

    public static int b(Context context, int i8, int i9) {
        TypedValue a9 = u7.b.a(context, i8);
        return a9 != null ? a9.data : i9;
    }

    public static int c(Context context, int i8, String str) {
        return u7.b.c(context, i8, str);
    }

    public static int d(View view, int i8) {
        return u7.b.d(view, i8);
    }

    public static int e(View view, int i8, int i9) {
        return b(view.getContext(), i8, i9);
    }

    public static boolean f(int i8) {
        return i8 != 0 && b.f(i8) > 0.5d;
    }

    public static int g(int i8, int i9) {
        return b.k(i9, i8);
    }

    public static int h(int i8, int i9, float f5) {
        return g(i8, b.p(i9, Math.round(Color.alpha(i9) * f5)));
    }

    public static int i(View view, int i8, int i9, float f5) {
        return h(d(view, i8), d(view, i9), f5);
    }
}
