package com.google.android.material.textview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import k7.l;
import u7.b;
import u7.c;
import y7.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MaterialTextView extends AppCompatTextView {
    public MaterialTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842884);
    }

    public MaterialTextView(Context context, AttributeSet attributeSet, int i8) {
        this(context, attributeSet, i8, 0);
    }

    public MaterialTextView(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(a.c(context, attributeSet, i8, i9), attributeSet, i8);
        int t8;
        Context context2 = getContext();
        if (s(context2)) {
            Resources.Theme theme = context2.getTheme();
            if (v(context2, theme, attributeSet, i8, i9) || (t8 = t(theme, attributeSet, i8, i9)) == -1) {
                return;
            }
            r(theme, t8);
        }
    }

    private void r(Resources.Theme theme, int i8) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(i8, l.L4);
        int u8 = u(getContext(), obtainStyledAttributes, l.N4, l.O4);
        obtainStyledAttributes.recycle();
        if (u8 >= 0) {
            setLineHeight(u8);
        }
    }

    private static boolean s(Context context) {
        return b.b(context, k7.b.Y, true);
    }

    private static int t(Resources.Theme theme, AttributeSet attributeSet, int i8, int i9) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(attributeSet, l.P4, i8, i9);
        int resourceId = obtainStyledAttributes.getResourceId(l.Q4, -1);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    private static int u(Context context, TypedArray typedArray, int... iArr) {
        int i8 = -1;
        for (int i9 = 0; i9 < iArr.length && i8 < 0; i9++) {
            i8 = c.c(context, typedArray, iArr[i9], -1);
        }
        return i8;
    }

    private static boolean v(Context context, Resources.Theme theme, AttributeSet attributeSet, int i8, int i9) {
        TypedArray obtainStyledAttributes = theme.obtainStyledAttributes(attributeSet, l.P4, i8, i9);
        int u8 = u(context, obtainStyledAttributes, l.R4, l.S4);
        obtainStyledAttributes.recycle();
        return u8 != -1;
    }

    @Override // androidx.appcompat.widget.AppCompatTextView, android.widget.TextView
    public void setTextAppearance(Context context, int i8) {
        super.setTextAppearance(context, i8);
        if (s(context)) {
            r(context.getTheme(), i8);
        }
    }
}
