package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.appcompat.widget.j0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m {

    /* renamed from: a  reason: collision with root package name */
    private static final int[] f18149a = {k7.b.q};

    /* renamed from: b  reason: collision with root package name */
    private static final int[] f18150b = {k7.b.f21065r};

    public static void a(Context context) {
        e(context, f18149a, "Theme.AppCompat");
    }

    private static void b(Context context, AttributeSet attributeSet, int i8, int i9) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, k7.l.z9, i8, i9);
        boolean z4 = obtainStyledAttributes.getBoolean(k7.l.B9, false);
        obtainStyledAttributes.recycle();
        if (z4) {
            TypedValue typedValue = new TypedValue();
            if (!context.getTheme().resolveAttribute(k7.b.f21073z, typedValue, true) || (typedValue.type == 18 && typedValue.data == 0)) {
                c(context);
            }
        }
        a(context);
    }

    public static void c(Context context) {
        e(context, f18150b, "Theme.MaterialComponents");
    }

    private static void d(Context context, AttributeSet attributeSet, int[] iArr, int i8, int i9, int... iArr2) {
        boolean z4;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, k7.l.z9, i8, i9);
        if (!obtainStyledAttributes.getBoolean(k7.l.C9, false)) {
            obtainStyledAttributes.recycle();
            return;
        }
        if (iArr2 == null || iArr2.length == 0) {
            z4 = obtainStyledAttributes.getResourceId(k7.l.A9, -1) != -1;
        } else {
            z4 = f(context, attributeSet, iArr, i8, i9, iArr2);
        }
        obtainStyledAttributes.recycle();
        if (!z4) {
            throw new IllegalArgumentException("This component requires that you specify a valid TextAppearance attribute. Update your app theme to inherit from Theme.MaterialComponents (or a descendant).");
        }
    }

    private static void e(Context context, int[] iArr, String str) {
        if (g(context, iArr)) {
            return;
        }
        throw new IllegalArgumentException("The style on this component requires your app theme to be " + str + " (or a descendant).");
    }

    private static boolean f(Context context, AttributeSet attributeSet, int[] iArr, int i8, int i9, int... iArr2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i8, i9);
        for (int i10 : iArr2) {
            if (obtainStyledAttributes.getResourceId(i10, -1) == -1) {
                obtainStyledAttributes.recycle();
                return false;
            }
        }
        obtainStyledAttributes.recycle();
        return true;
    }

    private static boolean g(Context context, int[] iArr) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(iArr);
        for (int i8 = 0; i8 < iArr.length; i8++) {
            if (!obtainStyledAttributes.hasValue(i8)) {
                obtainStyledAttributes.recycle();
                return false;
            }
        }
        obtainStyledAttributes.recycle();
        return true;
    }

    public static TypedArray h(Context context, AttributeSet attributeSet, int[] iArr, int i8, int i9, int... iArr2) {
        b(context, attributeSet, i8, i9);
        d(context, attributeSet, iArr, i8, i9, iArr2);
        return context.obtainStyledAttributes(attributeSet, iArr, i8, i9);
    }

    public static j0 i(Context context, AttributeSet attributeSet, int[] iArr, int i8, int i9, int... iArr2) {
        b(context, attributeSet, i8, i9);
        d(context, attributeSet, iArr, i8, i9, iArr2);
        return j0.v(context, attributeSet, iArr, i8, i9);
    }
}
