package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.b;
import androidx.core.view.c0;
import k7.f;
import k7.l;
import x7.h;
import x7.k;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class RadialViewGroup extends ConstraintLayout {
    private final Runnable E;
    private int F;
    private h G;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            RadialViewGroup.this.D();
        }
    }

    public RadialViewGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RadialViewGroup(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        LayoutInflater.from(context).inflate(k7.h.q, this);
        c0.x0(this, z());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, l.X5, i8, 0);
        this.F = obtainStyledAttributes.getDimensionPixelSize(l.Y5, 0);
        this.E = new a();
        obtainStyledAttributes.recycle();
    }

    private static boolean C(View view) {
        return "skip".equals(view.getTag());
    }

    private void E() {
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.E);
            handler.post(this.E);
        }
    }

    private Drawable z() {
        h hVar = new h();
        this.G = hVar;
        hVar.Y(new k(0.5f));
        this.G.a0(ColorStateList.valueOf(-1));
        return this.G;
    }

    public int A() {
        return this.F;
    }

    public void B(int i8) {
        this.F = i8;
        D();
    }

    protected void D() {
        int childCount = getChildCount();
        int i8 = 1;
        for (int i9 = 0; i9 < childCount; i9++) {
            if (C(getChildAt(i9))) {
                i8++;
            }
        }
        b bVar = new b();
        bVar.j(this);
        float f5 = 0.0f;
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            int id = childAt.getId();
            int i11 = f.f21151b;
            if (id != i11 && !C(childAt)) {
                bVar.l(childAt.getId(), i11, this.F, f5);
                f5 += 360.0f / (childCount - i8);
            }
        }
        bVar.d(this);
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public void addView(View view, int i8, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i8, layoutParams);
        if (view.getId() == -1) {
            view.setId(c0.m());
        }
        E();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        D();
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        E();
    }

    @Override // android.view.View
    public void setBackgroundColor(int i8) {
        this.G.a0(ColorStateList.valueOf(i8));
    }
}
