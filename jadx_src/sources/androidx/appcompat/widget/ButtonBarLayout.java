package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ButtonBarLayout extends LinearLayout {

    /* renamed from: a  reason: collision with root package name */
    private boolean f1216a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f1217b;

    /* renamed from: c  reason: collision with root package name */
    private int f1218c;

    public ButtonBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1218c = -1;
        int[] iArr = g.j.P0;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr);
        androidx.core.view.c0.r0(this, context, iArr, attributeSet, obtainStyledAttributes, 0, 0);
        this.f1216a = obtainStyledAttributes.getBoolean(g.j.Q0, true);
        obtainStyledAttributes.recycle();
        if (getOrientation() == 1) {
            setStacked(this.f1216a);
        }
    }

    private int a(int i8) {
        int childCount = getChildCount();
        while (i8 < childCount) {
            if (getChildAt(i8).getVisibility() == 0) {
                return i8;
            }
            i8++;
        }
        return -1;
    }

    private boolean b() {
        return this.f1217b;
    }

    private void setStacked(boolean z4) {
        if (this.f1217b != z4) {
            if (!z4 || this.f1216a) {
                this.f1217b = z4;
                setOrientation(z4 ? 1 : 0);
                setGravity(z4 ? 8388613 : 80);
                View findViewById = findViewById(g.f.M);
                if (findViewById != null) {
                    findViewById.setVisibility(z4 ? 8 : 4);
                }
                for (int childCount = getChildCount() - 2; childCount >= 0; childCount--) {
                    bringChildToFront(getChildAt(childCount));
                }
            }
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        int i10;
        boolean z4;
        int size = View.MeasureSpec.getSize(i8);
        int i11 = 0;
        if (this.f1216a) {
            if (size > this.f1218c && b()) {
                setStacked(false);
            }
            this.f1218c = size;
        }
        if (b() || View.MeasureSpec.getMode(i8) != 1073741824) {
            i10 = i8;
            z4 = false;
        } else {
            i10 = View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
            z4 = true;
        }
        super.onMeasure(i10, i9);
        if (this.f1216a && !b()) {
            if ((getMeasuredWidthAndState() & (-16777216)) == 16777216) {
                setStacked(true);
                z4 = true;
            }
        }
        if (z4) {
            super.onMeasure(i8, i9);
        }
        int a9 = a(0);
        if (a9 >= 0) {
            View childAt = getChildAt(a9);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            int paddingTop = getPaddingTop() + childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin + 0;
            if (b()) {
                int a10 = a(a9 + 1);
                if (a10 >= 0) {
                    paddingTop += getChildAt(a10).getPaddingTop() + ((int) (getResources().getDisplayMetrics().density * 16.0f));
                }
                i11 = paddingTop;
            } else {
                i11 = paddingTop + getPaddingBottom();
            }
        }
        if (androidx.core.view.c0.F(this) != i11) {
            setMinimumHeight(i11);
            if (i9 == 0) {
                super.onMeasure(i8, i9);
            }
        }
    }

    public void setAllowStacking(boolean z4) {
        if (this.f1216a != z4) {
            this.f1216a = z4;
            if (!z4 && b()) {
                setStacked(false);
            }
            requestLayout();
        }
    }
}
