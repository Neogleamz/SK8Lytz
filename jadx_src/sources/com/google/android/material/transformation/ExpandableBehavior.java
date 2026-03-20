package com.google.android.material.transformation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.c0;
import java.util.List;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ExpandableBehavior extends CoordinatorLayout.Behavior<View> {

    /* renamed from: a  reason: collision with root package name */
    private int f18738a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements ViewTreeObserver.OnPreDrawListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f18739a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f18740b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ r7.a f18741c;

        a(View view, int i8, r7.a aVar) {
            this.f18739a = view;
            this.f18740b = i8;
            this.f18741c = aVar;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            this.f18739a.getViewTreeObserver().removeOnPreDrawListener(this);
            if (ExpandableBehavior.this.f18738a == this.f18740b) {
                ExpandableBehavior expandableBehavior = ExpandableBehavior.this;
                r7.a aVar = this.f18741c;
                expandableBehavior.H((View) aVar, this.f18739a, aVar.a(), false);
            }
            return false;
        }
    }

    public ExpandableBehavior() {
        this.f18738a = 0;
    }

    public ExpandableBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f18738a = 0;
    }

    private boolean F(boolean z4) {
        if (!z4) {
            return this.f18738a == 1;
        }
        int i8 = this.f18738a;
        return i8 == 0 || i8 == 2;
    }

    protected r7.a G(CoordinatorLayout coordinatorLayout, View view) {
        List<View> v8 = coordinatorLayout.v(view);
        int size = v8.size();
        for (int i8 = 0; i8 < size; i8++) {
            View view2 = v8.get(i8);
            if (e(coordinatorLayout, view, view2)) {
                return (r7.a) view2;
            }
        }
        return null;
    }

    protected abstract boolean H(View view, View view2, boolean z4, boolean z8);

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public abstract boolean e(CoordinatorLayout coordinatorLayout, View view, View view2);

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean h(CoordinatorLayout coordinatorLayout, View view, View view2) {
        r7.a aVar = (r7.a) view2;
        if (F(aVar.a())) {
            this.f18738a = aVar.a() ? 1 : 2;
            return H((View) aVar, view, aVar.a(), true);
        }
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean l(CoordinatorLayout coordinatorLayout, View view, int i8) {
        r7.a G;
        if (c0.W(view) || (G = G(coordinatorLayout, view)) == null || !F(G.a())) {
            return false;
        }
        int i9 = G.a() ? 1 : 2;
        this.f18738a = i9;
        view.getViewTreeObserver().addOnPreDrawListener(new a(view, i9, G));
        return false;
    }
}
