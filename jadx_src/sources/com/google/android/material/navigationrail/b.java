package com.google.android.material.navigationrail;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.example.seedpoint.R;
import com.google.android.material.navigation.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b extends c {
    private final FrameLayout.LayoutParams B;

    public b(Context context) {
        super(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
        this.B = layoutParams;
        layoutParams.gravity = 49;
        setLayoutParams(layoutParams);
    }

    private int m(int i8, int i9, int i10) {
        return View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(i8), i9 / Math.max(1, i10)), 0);
    }

    private int n(View view, int i8, int i9) {
        if (view.getVisibility() != 8) {
            view.measure(i8, i9);
            return view.getMeasuredHeight();
        }
        return 0;
    }

    private int o(int i8, int i9, int i10, View view) {
        m(i8, i9, i10);
        int m8 = view == null ? m(i8, i9, i10) : View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), 0);
        int childCount = getChildCount();
        int i11 = 0;
        for (int i12 = 0; i12 < childCount; i12++) {
            View childAt = getChildAt(i12);
            if (childAt != view) {
                i11 += n(childAt, i8, m8);
            }
        }
        return i11;
    }

    private int p(int i8, int i9, int i10) {
        int i11;
        View childAt = getChildAt(getSelectedItemPosition());
        if (childAt != null) {
            i11 = n(childAt, i8, m(i8, i9, i10));
            i9 -= i11;
            i10--;
        } else {
            i11 = 0;
        }
        return i11 + o(i8, i9, i10, childAt);
    }

    @Override // com.google.android.material.navigation.c
    protected com.google.android.material.navigation.a f(Context context) {
        return new a(context);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getMenuGravity() {
        return this.B.gravity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean l() {
        return (this.B.gravity & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle) == 48;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        int childCount = getChildCount();
        int i12 = i10 - i8;
        int i13 = 0;
        for (int i14 = 0; i14 < childCount; i14++) {
            View childAt = getChildAt(i14);
            if (childAt.getVisibility() != 8) {
                int measuredHeight = childAt.getMeasuredHeight() + i13;
                childAt.layout(0, i13, i12, measuredHeight);
                i13 = measuredHeight;
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        int size = View.MeasureSpec.getSize(i9);
        int size2 = getMenu().G().size();
        setMeasuredDimension(View.resolveSizeAndState(View.MeasureSpec.getSize(i8), i8, 0), View.resolveSizeAndState((size2 <= 1 || !g(getLabelVisibilityMode(), size2)) ? o(i8, size, size2, null) : p(i8, size, size2), i9, 0));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMenuGravity(int i8) {
        FrameLayout.LayoutParams layoutParams = this.B;
        if (layoutParams.gravity != i8) {
            layoutParams.gravity = i8;
            setLayoutParams(layoutParams);
        }
    }
}
