package com.google.android.material.switchmaterial;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.internal.s;
import k7.b;
import k7.d;
import k7.k;
import q7.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SwitchMaterial extends SwitchCompat {

    /* renamed from: p0  reason: collision with root package name */
    private static final int f18471p0 = k.A;

    /* renamed from: q0  reason: collision with root package name */
    private static final int[][] f18472q0 = {new int[]{16842910, 16842912}, new int[]{16842910, -16842912}, new int[]{-16842910, 16842912}, new int[]{-16842910, -16842912}};

    /* renamed from: l0  reason: collision with root package name */
    private final a f18473l0;

    /* renamed from: m0  reason: collision with root package name */
    private ColorStateList f18474m0;

    /* renamed from: n0  reason: collision with root package name */
    private ColorStateList f18475n0;

    /* renamed from: o0  reason: collision with root package name */
    private boolean f18476o0;

    public SwitchMaterial(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, b.W);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SwitchMaterial(android.content.Context r7, android.util.AttributeSet r8, int r9) {
        /*
            r6 = this;
            int r4 = com.google.android.material.switchmaterial.SwitchMaterial.f18471p0
            android.content.Context r7 = y7.a.c(r7, r8, r9, r4)
            r6.<init>(r7, r8, r9)
            android.content.Context r0 = r6.getContext()
            q7.a r7 = new q7.a
            r7.<init>(r0)
            r6.f18473l0 = r7
            int[] r2 = k7.l.f21407q7
            r7 = 0
            int[] r5 = new int[r7]
            r1 = r8
            r3 = r9
            android.content.res.TypedArray r8 = com.google.android.material.internal.m.h(r0, r1, r2, r3, r4, r5)
            int r9 = k7.l.f21415r7
            boolean r7 = r8.getBoolean(r9, r7)
            r6.f18476o0 = r7
            r8.recycle()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.switchmaterial.SwitchMaterial.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private ColorStateList getMaterialThemeColorsThumbTintList() {
        if (this.f18474m0 == null) {
            int d8 = n7.a.d(this, b.f21066s);
            int d9 = n7.a.d(this, b.f21061m);
            float dimension = getResources().getDimension(d.f21130u0);
            if (this.f18473l0.e()) {
                dimension += s.g(this);
            }
            int c9 = this.f18473l0.c(d8, dimension);
            int[][] iArr = f18472q0;
            int[] iArr2 = new int[iArr.length];
            iArr2[0] = n7.a.h(d8, d9, 1.0f);
            iArr2[1] = c9;
            iArr2[2] = n7.a.h(d8, d9, 0.38f);
            iArr2[3] = c9;
            this.f18474m0 = new ColorStateList(iArr, iArr2);
        }
        return this.f18474m0;
    }

    private ColorStateList getMaterialThemeColorsTrackTintList() {
        if (this.f18475n0 == null) {
            int[][] iArr = f18472q0;
            int[] iArr2 = new int[iArr.length];
            int d8 = n7.a.d(this, b.f21066s);
            int d9 = n7.a.d(this, b.f21061m);
            int d10 = n7.a.d(this, b.f21064p);
            iArr2[0] = n7.a.h(d8, d9, 0.54f);
            iArr2[1] = n7.a.h(d8, d10, 0.32f);
            iArr2[2] = n7.a.h(d8, d9, 0.12f);
            iArr2[3] = n7.a.h(d8, d10, 0.12f);
            this.f18475n0 = new ColorStateList(iArr, iArr2);
        }
        return this.f18475n0;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.f18476o0 && getThumbTintList() == null) {
            setThumbTintList(getMaterialThemeColorsThumbTintList());
        }
        if (this.f18476o0 && getTrackTintList() == null) {
            setTrackTintList(getMaterialThemeColorsTrackTintList());
        }
    }

    public void setUseMaterialThemeColors(boolean z4) {
        ColorStateList colorStateList;
        this.f18476o0 = z4;
        if (z4) {
            setThumbTintList(getMaterialThemeColorsThumbTintList());
            colorStateList = getMaterialThemeColorsTrackTintList();
        } else {
            colorStateList = null;
            setThumbTintList(null);
        }
        setTrackTintList(colorStateList);
    }
}
