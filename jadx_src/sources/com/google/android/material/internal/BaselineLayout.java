package com.google.android.material.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class BaselineLayout extends ViewGroup {

    /* renamed from: a  reason: collision with root package name */
    private int f18024a;

    public BaselineLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        this.f18024a = -1;
    }

    public BaselineLayout(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f18024a = -1;
    }

    @Override // android.view.View
    public int getBaseline() {
        return this.f18024a;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingRight = ((i10 - i8) - getPaddingRight()) - paddingLeft;
        int paddingTop = getPaddingTop();
        for (int i12 = 0; i12 < childCount; i12++) {
            View childAt = getChildAt(i12);
            if (childAt.getVisibility() != 8) {
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i13 = ((paddingRight - measuredWidth) / 2) + paddingLeft;
                int baseline = (this.f18024a == -1 || childAt.getBaseline() == -1) ? paddingTop : (this.f18024a + paddingTop) - childAt.getBaseline();
                childAt.layout(i13, baseline, measuredWidth + i13, measuredHeight + baseline);
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        int childCount = getChildCount();
        int i10 = -1;
        int i11 = -1;
        int i12 = 0;
        int i13 = 0;
        int i14 = 0;
        for (int i15 = 0; i15 < childCount; i15++) {
            View childAt = getChildAt(i15);
            if (childAt.getVisibility() != 8) {
                measureChild(childAt, i8, i9);
                int baseline = childAt.getBaseline();
                if (baseline != -1) {
                    i10 = Math.max(i10, baseline);
                    i11 = Math.max(i11, childAt.getMeasuredHeight() - baseline);
                }
                i13 = Math.max(i13, childAt.getMeasuredWidth());
                i12 = Math.max(i12, childAt.getMeasuredHeight());
                i14 = View.combineMeasuredStates(i14, childAt.getMeasuredState());
            }
        }
        if (i10 != -1) {
            i12 = Math.max(i12, Math.max(i11, getPaddingBottom()) + i10);
            this.f18024a = i10;
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(i13, getSuggestedMinimumWidth()), i8, i14), View.resolveSizeAndState(Math.max(i12, getSuggestedMinimumHeight()), i9, i14 << 16));
    }
}
