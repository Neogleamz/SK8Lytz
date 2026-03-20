package com.google.android.material.navigationrail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.j0;
import com.google.android.material.internal.m;
import com.google.android.material.navigation.NavigationBarView;
import k7.d;
import k7.k;
import k7.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class NavigationRailView extends NavigationBarView {

    /* renamed from: h  reason: collision with root package name */
    private final int f18235h;

    /* renamed from: j  reason: collision with root package name */
    private View f18236j;

    public NavigationRailView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.J);
    }

    public NavigationRailView(Context context, AttributeSet attributeSet, int i8) {
        this(context, attributeSet, i8, k.F);
    }

    public NavigationRailView(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(context, attributeSet, i8, i9);
        this.f18235h = getResources().getDimensionPixelSize(d.f21109j0);
        j0 i10 = m.i(getContext(), attributeSet, l.f21431t5, i8, i9, new int[0]);
        int n8 = i10.n(l.f21440u5, 0);
        if (n8 != 0) {
            g(n8);
        }
        setMenuGravity(i10.k(l.f21449v5, 49));
        i10.w();
    }

    private b getNavigationRailMenuView() {
        return (b) getMenuView();
    }

    private boolean j() {
        View view = this.f18236j;
        return (view == null || view.getVisibility() == 8) ? false : true;
    }

    private int k(int i8) {
        int suggestedMinimumWidth = getSuggestedMinimumWidth();
        if (View.MeasureSpec.getMode(i8) == 1073741824 || suggestedMinimumWidth <= 0) {
            return i8;
        }
        return View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(i8), suggestedMinimumWidth + getPaddingLeft() + getPaddingRight()), 1073741824);
    }

    public void g(int i8) {
        h(LayoutInflater.from(getContext()).inflate(i8, (ViewGroup) this, false));
    }

    public View getHeaderView() {
        return this.f18236j;
    }

    @Override // com.google.android.material.navigation.NavigationBarView
    public int getMaxItemCount() {
        return 7;
    }

    public int getMenuGravity() {
        return getNavigationRailMenuView().getMenuGravity();
    }

    public void h(View view) {
        l();
        this.f18236j = view;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 49;
        layoutParams.topMargin = this.f18235h;
        addView(view, 0, layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.navigation.NavigationBarView
    /* renamed from: i */
    public b e(Context context) {
        return new b(context);
    }

    public void l() {
        View view = this.f18236j;
        if (view != null) {
            removeView(view);
            this.f18236j = null;
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        b navigationRailMenuView = getNavigationRailMenuView();
        int i12 = 0;
        if (j()) {
            int bottom = this.f18236j.getBottom() + this.f18235h;
            int top = navigationRailMenuView.getTop();
            if (top < bottom) {
                i12 = bottom - top;
            }
        } else if (navigationRailMenuView.l()) {
            i12 = this.f18235h;
        }
        if (i12 > 0) {
            navigationRailMenuView.layout(navigationRailMenuView.getLeft(), navigationRailMenuView.getTop() + i12, navigationRailMenuView.getRight(), navigationRailMenuView.getBottom() + i12);
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        int k8 = k(i8);
        super.onMeasure(k8, i9);
        if (j()) {
            measureChild(getNavigationRailMenuView(), k8, View.MeasureSpec.makeMeasureSpec((getMeasuredHeight() - this.f18236j.getMeasuredHeight()) - this.f18235h, Integer.MIN_VALUE));
        }
    }

    public void setMenuGravity(int i8) {
        getNavigationRailMenuView().setMenuGravity(i8);
    }
}
