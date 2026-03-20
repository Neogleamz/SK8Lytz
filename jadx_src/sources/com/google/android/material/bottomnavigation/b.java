package com.google.android.material.bottomnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.FrameLayout;
import androidx.appcompat.view.menu.g;
import androidx.core.view.c0;
import com.google.android.material.navigation.c;
import k7.d;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b extends c {
    private final int B;
    private final int C;
    private final int E;
    private final int F;
    private final int G;
    private boolean H;
    private int[] K;

    public b(Context context) {
        super(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        setLayoutParams(layoutParams);
        Resources resources = getResources();
        this.B = resources.getDimensionPixelSize(d.f21098e);
        this.C = resources.getDimensionPixelSize(d.f21100f);
        this.E = resources.getDimensionPixelSize(d.f21092b);
        this.F = resources.getDimensionPixelSize(d.f21094c);
        this.G = resources.getDimensionPixelSize(d.f21096d);
        this.K = new int[5];
    }

    @Override // com.google.android.material.navigation.c
    protected com.google.android.material.navigation.a f(Context context) {
        return new a(context);
    }

    public boolean l() {
        return this.H;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        int childCount = getChildCount();
        int i12 = i10 - i8;
        int i13 = i11 - i9;
        int i14 = 0;
        for (int i15 = 0; i15 < childCount; i15++) {
            View childAt = getChildAt(i15);
            if (childAt.getVisibility() != 8) {
                if (c0.E(this) == 1) {
                    int i16 = i12 - i14;
                    childAt.layout(i16 - childAt.getMeasuredWidth(), 0, i16, i13);
                } else {
                    childAt.layout(i14, 0, childAt.getMeasuredWidth() + i14, i13);
                }
                i14 += childAt.getMeasuredWidth();
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        g menu = getMenu();
        int size = View.MeasureSpec.getSize(i8);
        int size2 = menu.G().size();
        int childCount = getChildCount();
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.G, 1073741824);
        if (g(getLabelVisibilityMode(), size2) && l()) {
            View childAt = getChildAt(getSelectedItemPosition());
            int i10 = this.F;
            if (childAt.getVisibility() != 8) {
                childAt.measure(View.MeasureSpec.makeMeasureSpec(this.E, Integer.MIN_VALUE), makeMeasureSpec);
                i10 = Math.max(i10, childAt.getMeasuredWidth());
            }
            int i11 = size2 - (childAt.getVisibility() != 8 ? 1 : 0);
            int min = Math.min(size - (this.C * i11), Math.min(i10, this.E));
            int i12 = size - min;
            int min2 = Math.min(i12 / (i11 == 0 ? 1 : i11), this.B);
            int i13 = i12 - (i11 * min2);
            int i14 = 0;
            while (i14 < childCount) {
                if (getChildAt(i14).getVisibility() != 8) {
                    this.K[i14] = i14 == getSelectedItemPosition() ? min : min2;
                    if (i13 > 0) {
                        int[] iArr = this.K;
                        iArr[i14] = iArr[i14] + 1;
                        i13--;
                    }
                } else {
                    this.K[i14] = 0;
                }
                i14++;
            }
        } else {
            int min3 = Math.min(size / (size2 == 0 ? 1 : size2), this.E);
            int i15 = size - (size2 * min3);
            for (int i16 = 0; i16 < childCount; i16++) {
                if (getChildAt(i16).getVisibility() != 8) {
                    int[] iArr2 = this.K;
                    iArr2[i16] = min3;
                    if (i15 > 0) {
                        iArr2[i16] = iArr2[i16] + 1;
                        i15--;
                    }
                } else {
                    this.K[i16] = 0;
                }
            }
        }
        int i17 = 0;
        for (int i18 = 0; i18 < childCount; i18++) {
            View childAt2 = getChildAt(i18);
            if (childAt2.getVisibility() != 8) {
                childAt2.measure(View.MeasureSpec.makeMeasureSpec(this.K[i18], 1073741824), makeMeasureSpec);
                childAt2.getLayoutParams().width = childAt2.getMeasuredWidth();
                i17 += childAt2.getMeasuredWidth();
            }
        }
        setMeasuredDimension(View.resolveSizeAndState(i17, View.MeasureSpec.makeMeasureSpec(i17, 1073741824), 0), View.resolveSizeAndState(this.G, makeMeasureSpec, 0));
    }

    public void setItemHorizontalTranslationEnabled(boolean z4) {
        this.H = z4;
    }
}
