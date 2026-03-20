package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.c0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class FlowLayout extends ViewGroup {

    /* renamed from: a  reason: collision with root package name */
    private int f18031a;

    /* renamed from: b  reason: collision with root package name */
    private int f18032b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f18033c;

    /* renamed from: d  reason: collision with root package name */
    private int f18034d;

    public FlowLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f18033c = false;
        d(context, attributeSet);
    }

    private static int a(int i8, int i9, int i10) {
        return i9 != Integer.MIN_VALUE ? i9 != 1073741824 ? i10 : i8 : Math.min(i10, i8);
    }

    private void d(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, k7.l.U2, 0, 0);
        this.f18031a = obtainStyledAttributes.getDimensionPixelSize(k7.l.W2, 0);
        this.f18032b = obtainStyledAttributes.getDimensionPixelSize(k7.l.V2, 0);
        obtainStyledAttributes.recycle();
    }

    public int b(View view) {
        Object tag = view.getTag(k7.f.Q);
        if (tag instanceof Integer) {
            return ((Integer) tag).intValue();
        }
        return -1;
    }

    public boolean c() {
        return this.f18033c;
    }

    protected int getItemSpacing() {
        return this.f18032b;
    }

    protected int getLineSpacing() {
        return this.f18031a;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getRowCount() {
        return this.f18034d;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        int i12;
        int i13;
        if (getChildCount() == 0) {
            this.f18034d = 0;
            return;
        }
        this.f18034d = 1;
        boolean z8 = c0.E(this) == 1;
        int paddingRight = z8 ? getPaddingRight() : getPaddingLeft();
        int paddingLeft = z8 ? getPaddingLeft() : getPaddingRight();
        int paddingTop = getPaddingTop();
        int i14 = (i10 - i8) - paddingLeft;
        int i15 = paddingRight;
        int i16 = paddingTop;
        for (int i17 = 0; i17 < getChildCount(); i17++) {
            View childAt = getChildAt(i17);
            if (childAt.getVisibility() == 8) {
                childAt.setTag(k7.f.Q, -1);
            } else {
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    i13 = androidx.core.view.i.b(marginLayoutParams);
                    i12 = androidx.core.view.i.a(marginLayoutParams);
                } else {
                    i12 = 0;
                    i13 = 0;
                }
                int measuredWidth = i15 + i13 + childAt.getMeasuredWidth();
                if (!this.f18033c && measuredWidth > i14) {
                    i16 = this.f18031a + paddingTop;
                    this.f18034d++;
                    i15 = paddingRight;
                }
                childAt.setTag(k7.f.Q, Integer.valueOf(this.f18034d - 1));
                int i18 = i15 + i13;
                int measuredWidth2 = childAt.getMeasuredWidth() + i18;
                int measuredHeight = childAt.getMeasuredHeight() + i16;
                if (z8) {
                    i18 = i14 - measuredWidth2;
                    measuredWidth2 = (i14 - i15) - i13;
                }
                childAt.layout(i18, i16, measuredWidth2, measuredHeight);
                i15 += i13 + i12 + childAt.getMeasuredWidth() + this.f18032b;
                paddingTop = measuredHeight;
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        int i10;
        int i11;
        int i12;
        int size = View.MeasureSpec.getSize(i8);
        int mode = View.MeasureSpec.getMode(i8);
        int size2 = View.MeasureSpec.getSize(i9);
        int mode2 = View.MeasureSpec.getMode(i9);
        int i13 = (mode == Integer.MIN_VALUE || mode == 1073741824) ? size : Integer.MAX_VALUE;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = i13 - getPaddingRight();
        int i14 = paddingTop;
        int i15 = 0;
        for (int i16 = 0; i16 < getChildCount(); i16++) {
            View childAt = getChildAt(i16);
            if (childAt.getVisibility() != 8) {
                measureChild(childAt, i8, i9);
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    i10 = marginLayoutParams.leftMargin + 0;
                    i11 = marginLayoutParams.rightMargin + 0;
                } else {
                    i10 = 0;
                    i11 = 0;
                }
                int i17 = paddingLeft;
                if (paddingLeft + i10 + childAt.getMeasuredWidth() <= paddingRight || c()) {
                    i12 = i17;
                } else {
                    i12 = getPaddingLeft();
                    i14 = this.f18031a + paddingTop;
                }
                int measuredWidth = i12 + i10 + childAt.getMeasuredWidth();
                int measuredHeight = i14 + childAt.getMeasuredHeight();
                if (measuredWidth > i15) {
                    i15 = measuredWidth;
                }
                paddingLeft = i12 + i10 + i11 + childAt.getMeasuredWidth() + this.f18032b;
                if (i16 == getChildCount() - 1) {
                    i15 += i11;
                }
                paddingTop = measuredHeight;
            }
        }
        setMeasuredDimension(a(size, mode, i15 + getPaddingRight()), a(size2, mode2, paddingTop + getPaddingBottom()));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setItemSpacing(int i8) {
        this.f18032b = i8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setLineSpacing(int i8) {
        this.f18031a = i8;
    }

    public void setSingleLine(boolean z4) {
        this.f18033c = z4;
    }
}
