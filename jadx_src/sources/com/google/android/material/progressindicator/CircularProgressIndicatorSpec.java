package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.google.android.material.internal.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class CircularProgressIndicatorSpec extends b {

    /* renamed from: g  reason: collision with root package name */
    public int f18238g;

    /* renamed from: h  reason: collision with root package name */
    public int f18239h;

    /* renamed from: i  reason: collision with root package name */
    public int f18240i;

    public CircularProgressIndicatorSpec(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21059k);
    }

    public CircularProgressIndicatorSpec(Context context, AttributeSet attributeSet, int i8) {
        this(context, attributeSet, i8, CircularProgressIndicator.f18237t);
    }

    public CircularProgressIndicatorSpec(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(context, attributeSet, i8, i9);
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(k7.d.f21113l0);
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(k7.d.f21111k0);
        TypedArray h8 = m.h(context, attributeSet, k7.l.A1, i8, i9, new int[0]);
        this.f18238g = Math.max(u7.c.c(context, h8, k7.l.D1, dimensionPixelSize), this.f18263a * 2);
        this.f18239h = u7.c.c(context, h8, k7.l.C1, dimensionPixelSize2);
        this.f18240i = h8.getInt(k7.l.B1, 0);
        h8.recycle();
        e();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.b
    public void e() {
    }
}
