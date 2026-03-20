package com.google.android.material.appbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.c0;
import com.google.android.material.internal.n;
import k7.k;
import x7.h;
import x7.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MaterialToolbar extends Toolbar {

    /* renamed from: o0  reason: collision with root package name */
    private static final int f17407o0 = k.K;

    /* renamed from: l0  reason: collision with root package name */
    private Integer f17408l0;

    /* renamed from: m0  reason: collision with root package name */
    private boolean f17409m0;

    /* renamed from: n0  reason: collision with root package name */
    private boolean f17410n0;

    public MaterialToolbar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21049b0);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MaterialToolbar(android.content.Context r8, android.util.AttributeSet r9, int r10) {
        /*
            r7 = this;
            int r4 = com.google.android.material.appbar.MaterialToolbar.f17407o0
            android.content.Context r8 = y7.a.c(r8, r9, r10, r4)
            r7.<init>(r8, r9, r10)
            android.content.Context r8 = r7.getContext()
            int[] r2 = k7.l.U4
            r6 = 0
            int[] r5 = new int[r6]
            r0 = r8
            r1 = r9
            r3 = r10
            android.content.res.TypedArray r9 = com.google.android.material.internal.m.h(r0, r1, r2, r3, r4, r5)
            int r10 = k7.l.V4
            boolean r0 = r9.hasValue(r10)
            if (r0 == 0) goto L29
            r0 = -1
            int r10 = r9.getColor(r10, r0)
            r7.setNavigationIconTint(r10)
        L29:
            int r10 = k7.l.X4
            boolean r10 = r9.getBoolean(r10, r6)
            r7.f17409m0 = r10
            int r10 = k7.l.W4
            boolean r10 = r9.getBoolean(r10, r6)
            r7.f17410n0 = r10
            r9.recycle()
            r7.T(r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.appbar.MaterialToolbar.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private Pair<Integer, Integer> S(TextView textView, TextView textView2) {
        int measuredWidth = getMeasuredWidth();
        int i8 = measuredWidth / 2;
        int paddingLeft = getPaddingLeft();
        int paddingRight = measuredWidth - getPaddingRight();
        for (int i9 = 0; i9 < getChildCount(); i9++) {
            View childAt = getChildAt(i9);
            if (childAt.getVisibility() != 8 && childAt != textView && childAt != textView2) {
                if (childAt.getRight() < i8 && childAt.getRight() > paddingLeft) {
                    paddingLeft = childAt.getRight();
                }
                if (childAt.getLeft() > i8 && childAt.getLeft() < paddingRight) {
                    paddingRight = childAt.getLeft();
                }
            }
        }
        return new Pair<>(Integer.valueOf(paddingLeft), Integer.valueOf(paddingRight));
    }

    private void T(Context context) {
        Drawable background = getBackground();
        if (background == null || (background instanceof ColorDrawable)) {
            h hVar = new h();
            hVar.a0(ColorStateList.valueOf(background != null ? ((ColorDrawable) background).getColor() : 0));
            hVar.P(context);
            hVar.Z(c0.y(this));
            c0.x0(this, hVar);
        }
    }

    private void U(View view, Pair<Integer, Integer> pair) {
        int measuredWidth = getMeasuredWidth();
        int measuredWidth2 = view.getMeasuredWidth();
        int i8 = (measuredWidth / 2) - (measuredWidth2 / 2);
        int i9 = measuredWidth2 + i8;
        int max = Math.max(Math.max(((Integer) pair.first).intValue() - i8, 0), Math.max(i9 - ((Integer) pair.second).intValue(), 0));
        if (max > 0) {
            i8 += max;
            i9 -= max;
            view.measure(View.MeasureSpec.makeMeasureSpec(i9 - i8, 1073741824), view.getMeasuredHeightAndState());
        }
        view.layout(i8, view.getTop(), i9, view.getBottom());
    }

    private void V() {
        if (this.f17409m0 || this.f17410n0) {
            TextView c9 = n.c(this);
            TextView a9 = n.a(this);
            if (c9 == null && a9 == null) {
                return;
            }
            Pair<Integer, Integer> S = S(c9, a9);
            if (this.f17409m0 && c9 != null) {
                U(c9, S);
            }
            if (!this.f17410n0 || a9 == null) {
                return;
            }
            U(a9, S);
        }
    }

    private Drawable W(Drawable drawable) {
        if (drawable == null || this.f17408l0 == null) {
            return drawable;
        }
        Drawable r4 = androidx.core.graphics.drawable.a.r(drawable);
        androidx.core.graphics.drawable.a.n(r4, this.f17408l0.intValue());
        return r4;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        i.e(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.Toolbar, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        V();
    }

    @Override // android.view.View
    public void setElevation(float f5) {
        super.setElevation(f5);
        i.d(this, f5);
    }

    @Override // androidx.appcompat.widget.Toolbar
    public void setNavigationIcon(Drawable drawable) {
        super.setNavigationIcon(W(drawable));
    }

    public void setNavigationIconTint(int i8) {
        this.f17408l0 = Integer.valueOf(i8);
        Drawable navigationIcon = getNavigationIcon();
        if (navigationIcon != null) {
            setNavigationIcon(navigationIcon);
        }
    }

    public void setSubtitleCentered(boolean z4) {
        if (this.f17410n0 != z4) {
            this.f17410n0 = z4;
            requestLayout();
        }
    }

    public void setTitleCentered(boolean z4) {
        if (this.f17409m0 != z4) {
            this.f17409m0 = z4;
            requestLayout();
        }
    }
}
