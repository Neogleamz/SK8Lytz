package com.google.android.material.radiobutton;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.widget.c;
import k7.b;
import k7.k;
import n7.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MaterialRadioButton extends AppCompatRadioButton {

    /* renamed from: g  reason: collision with root package name */
    private static final int f18339g = k.f21254z;

    /* renamed from: h  reason: collision with root package name */
    private static final int[][] f18340h = {new int[]{16842910, 16842912}, new int[]{16842910, -16842912}, new int[]{-16842910, 16842912}, new int[]{-16842910, -16842912}};

    /* renamed from: e  reason: collision with root package name */
    private ColorStateList f18341e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f18342f;

    public MaterialRadioButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, b.M);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MaterialRadioButton(android.content.Context r8, android.util.AttributeSet r9, int r10) {
        /*
            r7 = this;
            int r4 = com.google.android.material.radiobutton.MaterialRadioButton.f18339g
            android.content.Context r8 = y7.a.c(r8, r9, r10, r4)
            r7.<init>(r8, r9, r10)
            android.content.Context r8 = r7.getContext()
            int[] r2 = k7.l.F4
            r6 = 0
            int[] r5 = new int[r6]
            r0 = r8
            r1 = r9
            r3 = r10
            android.content.res.TypedArray r9 = com.google.android.material.internal.m.h(r0, r1, r2, r3, r4, r5)
            int r10 = k7.l.G4
            boolean r0 = r9.hasValue(r10)
            if (r0 == 0) goto L28
            android.content.res.ColorStateList r8 = u7.c.a(r8, r9, r10)
            androidx.core.widget.c.c(r7, r8)
        L28:
            int r8 = k7.l.H4
            boolean r8 = r9.getBoolean(r8, r6)
            r7.f18342f = r8
            r9.recycle()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.radiobutton.MaterialRadioButton.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private ColorStateList getMaterialThemeColorsTintList() {
        if (this.f18341e == null) {
            int d8 = a.d(this, b.f21061m);
            int d9 = a.d(this, b.f21064p);
            int d10 = a.d(this, b.f21066s);
            int[][] iArr = f18340h;
            int[] iArr2 = new int[iArr.length];
            iArr2[0] = a.h(d10, d8, 1.0f);
            iArr2[1] = a.h(d10, d9, 0.54f);
            iArr2[2] = a.h(d10, d9, 0.38f);
            iArr2[3] = a.h(d10, d9, 0.38f);
            this.f18341e = new ColorStateList(iArr, iArr2);
        }
        return this.f18341e;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f18342f && c.b(this) == null) {
            setUseMaterialThemeColors(true);
        }
    }

    public void setUseMaterialThemeColors(boolean z4) {
        this.f18342f = z4;
        c.c(this, z4 ? getMaterialThemeColorsTintList() : null);
    }
}
