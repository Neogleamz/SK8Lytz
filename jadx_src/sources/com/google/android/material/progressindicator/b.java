package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.google.android.material.internal.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b {

    /* renamed from: a  reason: collision with root package name */
    public int f18263a;

    /* renamed from: b  reason: collision with root package name */
    public int f18264b;

    /* renamed from: c  reason: collision with root package name */
    public int[] f18265c = new int[0];

    /* renamed from: d  reason: collision with root package name */
    public int f18266d;

    /* renamed from: e  reason: collision with root package name */
    public int f18267e;

    /* renamed from: f  reason: collision with root package name */
    public int f18268f;

    /* JADX INFO: Access modifiers changed from: protected */
    public b(Context context, AttributeSet attributeSet, int i8, int i9) {
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(k7.d.f21115m0);
        TypedArray h8 = m.h(context, attributeSet, k7.l.K, i8, i9, new int[0]);
        this.f18263a = u7.c.c(context, h8, k7.l.S, dimensionPixelSize);
        this.f18264b = Math.min(u7.c.c(context, h8, k7.l.R, 0), this.f18263a / 2);
        this.f18267e = h8.getInt(k7.l.O, 0);
        this.f18268f = h8.getInt(k7.l.L, 0);
        c(context, h8);
        d(context, h8);
        h8.recycle();
    }

    private void c(Context context, TypedArray typedArray) {
        int i8 = k7.l.M;
        if (!typedArray.hasValue(i8)) {
            this.f18265c = new int[]{n7.a.b(context, k7.b.q, -1)};
        } else if (typedArray.peekValue(i8).type != 1) {
            this.f18265c = new int[]{typedArray.getColor(i8, -1)};
        } else {
            int[] intArray = context.getResources().getIntArray(typedArray.getResourceId(i8, -1));
            this.f18265c = intArray;
            if (intArray.length == 0) {
                throw new IllegalArgumentException("indicatorColors cannot be empty when indicatorColor is not used.");
            }
        }
    }

    private void d(Context context, TypedArray typedArray) {
        int a9;
        int i8 = k7.l.Q;
        if (typedArray.hasValue(i8)) {
            a9 = typedArray.getColor(i8, -1);
        } else {
            this.f18266d = this.f18265c[0];
            TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(new int[]{16842803});
            float f5 = obtainStyledAttributes.getFloat(0, 0.2f);
            obtainStyledAttributes.recycle();
            a9 = n7.a.a(this.f18266d, (int) (f5 * 255.0f));
        }
        this.f18266d = a9;
    }

    public boolean a() {
        return this.f18268f != 0;
    }

    public boolean b() {
        return this.f18267e != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void e();
}
