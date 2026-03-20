package com.google.android.material.navigationrail;

import android.content.Context;
import android.view.View;
import k7.d;
import k7.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class a extends com.google.android.material.navigation.a {
    public a(Context context) {
        super(context);
    }

    @Override // com.google.android.material.navigation.a
    protected int getItemDefaultMarginResId() {
        return d.f21107i0;
    }

    @Override // com.google.android.material.navigation.a
    protected int getItemLayoutResId() {
        return h.C;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        if (View.MeasureSpec.getMode(i9) == 0) {
            setMeasuredDimension(getMeasuredWidthAndState(), View.resolveSizeAndState(Math.max(getMeasuredHeight(), View.MeasureSpec.getSize(i9)), i9, 0));
        }
    }
}
