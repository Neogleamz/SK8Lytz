package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e0 {

    /* renamed from: a  reason: collision with root package name */
    private static final ThreadLocal<TypedValue> f1467a = new ThreadLocal<>();

    /* renamed from: b  reason: collision with root package name */
    static final int[] f1468b = {-16842910};

    /* renamed from: c  reason: collision with root package name */
    static final int[] f1469c = {16842908};

    /* renamed from: d  reason: collision with root package name */
    static final int[] f1470d = {16843518};

    /* renamed from: e  reason: collision with root package name */
    static final int[] f1471e = {16842919};

    /* renamed from: f  reason: collision with root package name */
    static final int[] f1472f = {16842912};

    /* renamed from: g  reason: collision with root package name */
    static final int[] f1473g = {16842913};

    /* renamed from: h  reason: collision with root package name */
    static final int[] f1474h = {-16842919, -16842908};

    /* renamed from: i  reason: collision with root package name */
    static final int[] f1475i = new int[0];

    /* renamed from: j  reason: collision with root package name */
    private static final int[] f1476j = new int[1];

    public static void a(View view, Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(g.j.A0);
        try {
            if (!obtainStyledAttributes.hasValue(g.j.F0)) {
                Log.e("ThemeUtils", "View " + view.getClass() + " is an AppCompat widget that can only be used with a Theme.AppCompat theme (or descendant).");
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public static int b(Context context, int i8) {
        ColorStateList e8 = e(context, i8);
        if (e8 == null || !e8.isStateful()) {
            TypedValue f5 = f();
            context.getTheme().resolveAttribute(16842803, f5, true);
            return d(context, i8, f5.getFloat());
        }
        return e8.getColorForState(f1468b, e8.getDefaultColor());
    }

    public static int c(Context context, int i8) {
        int[] iArr = f1476j;
        iArr[0] = i8;
        j0 u8 = j0.u(context, null, iArr);
        try {
            return u8.b(0, 0);
        } finally {
            u8.w();
        }
    }

    static int d(Context context, int i8, float f5) {
        int c9 = c(context, i8);
        return androidx.core.graphics.b.p(c9, Math.round(Color.alpha(c9) * f5));
    }

    public static ColorStateList e(Context context, int i8) {
        int[] iArr = f1476j;
        iArr[0] = i8;
        j0 u8 = j0.u(context, null, iArr);
        try {
            return u8.c(0);
        } finally {
            u8.w();
        }
    }

    private static TypedValue f() {
        ThreadLocal<TypedValue> threadLocal = f1467a;
        TypedValue typedValue = threadLocal.get();
        if (typedValue == null) {
            TypedValue typedValue2 = new TypedValue();
            threadLocal.set(typedValue2);
            return typedValue2;
        }
        return typedValue;
    }
}
