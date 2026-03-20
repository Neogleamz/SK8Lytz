package com.google.android.material.appbar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.c0;
import androidx.core.view.f;
import androidx.core.view.m0;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class HeaderScrollingViewBehavior extends ViewOffsetBehavior<View> {

    /* renamed from: d  reason: collision with root package name */
    final Rect f17403d;

    /* renamed from: e  reason: collision with root package name */
    final Rect f17404e;

    /* renamed from: f  reason: collision with root package name */
    private int f17405f;

    /* renamed from: g  reason: collision with root package name */
    private int f17406g;

    public HeaderScrollingViewBehavior() {
        this.f17403d = new Rect();
        this.f17404e = new Rect();
        this.f17405f = 0;
    }

    public HeaderScrollingViewBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f17403d = new Rect();
        this.f17404e = new Rect();
        this.f17405f = 0;
    }

    private static int N(int i8) {
        if (i8 == 0) {
            return 8388659;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.appbar.ViewOffsetBehavior
    public void F(CoordinatorLayout coordinatorLayout, View view, int i8) {
        int i9;
        View H = H(coordinatorLayout.v(view));
        if (H != null) {
            CoordinatorLayout.e eVar = (CoordinatorLayout.e) view.getLayoutParams();
            Rect rect = this.f17403d;
            rect.set(coordinatorLayout.getPaddingLeft() + ((ViewGroup.MarginLayoutParams) eVar).leftMargin, H.getBottom() + ((ViewGroup.MarginLayoutParams) eVar).topMargin, (coordinatorLayout.getWidth() - coordinatorLayout.getPaddingRight()) - ((ViewGroup.MarginLayoutParams) eVar).rightMargin, ((coordinatorLayout.getHeight() + H.getBottom()) - coordinatorLayout.getPaddingBottom()) - ((ViewGroup.MarginLayoutParams) eVar).bottomMargin);
            m0 lastWindowInsets = coordinatorLayout.getLastWindowInsets();
            if (lastWindowInsets != null && c0.B(coordinatorLayout) && !c0.B(view)) {
                rect.left += lastWindowInsets.k();
                rect.right -= lastWindowInsets.l();
            }
            Rect rect2 = this.f17404e;
            f.a(N(eVar.f4386c), view.getMeasuredWidth(), view.getMeasuredHeight(), rect, rect2, i8);
            int I = I(H);
            view.layout(rect2.left, rect2.top - I, rect2.right, rect2.bottom - I);
            i9 = rect2.top - H.getBottom();
        } else {
            super.F(coordinatorLayout, view, i8);
            i9 = 0;
        }
        this.f17405f = i9;
    }

    abstract View H(List<View> list);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int I(View view) {
        if (this.f17406g == 0) {
            return 0;
        }
        float J = J(view);
        int i8 = this.f17406g;
        return t0.a.c((int) (J * i8), 0, i8);
    }

    float J(View view) {
        return 1.0f;
    }

    public final int K() {
        return this.f17406g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int L(View view) {
        return view.getMeasuredHeight();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int M() {
        return this.f17405f;
    }

    public final void O(int i8) {
        this.f17406g = i8;
    }

    protected boolean P() {
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean m(CoordinatorLayout coordinatorLayout, View view, int i8, int i9, int i10, int i11) {
        View H;
        m0 lastWindowInsets;
        int i12 = view.getLayoutParams().height;
        if ((i12 == -1 || i12 == -2) && (H = H(coordinatorLayout.v(view))) != null) {
            int size = View.MeasureSpec.getSize(i10);
            if (size <= 0) {
                size = coordinatorLayout.getHeight();
            } else if (c0.B(H) && (lastWindowInsets = coordinatorLayout.getLastWindowInsets()) != null) {
                size += lastWindowInsets.m() + lastWindowInsets.j();
            }
            int L = size + L(H);
            int measuredHeight = H.getMeasuredHeight();
            if (P()) {
                view.setTranslationY(-measuredHeight);
            } else {
                L -= measuredHeight;
            }
            coordinatorLayout.N(view, i8, i9, View.MeasureSpec.makeMeasureSpec(L, i12 == -1 ? 1073741824 : Integer.MIN_VALUE), i11);
            return true;
        }
        return false;
    }
}
