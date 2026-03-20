package com.google.android.material.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class HideBottomViewOnScrollBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    /* renamed from: a  reason: collision with root package name */
    private int f17457a;

    /* renamed from: b  reason: collision with root package name */
    private int f17458b;

    /* renamed from: c  reason: collision with root package name */
    private int f17459c;

    /* renamed from: d  reason: collision with root package name */
    private ViewPropertyAnimator f17460d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            HideBottomViewOnScrollBehavior.this.f17460d = null;
        }
    }

    public HideBottomViewOnScrollBehavior() {
        this.f17457a = 0;
        this.f17458b = 2;
        this.f17459c = 0;
    }

    public HideBottomViewOnScrollBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f17457a = 0;
        this.f17458b = 2;
        this.f17459c = 0;
    }

    private void F(V v8, int i8, long j8, TimeInterpolator timeInterpolator) {
        this.f17460d = v8.animate().translationY(i8).setInterpolator(timeInterpolator).setDuration(j8).setListener(new a());
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean A(CoordinatorLayout coordinatorLayout, V v8, View view, View view2, int i8, int i9) {
        return i8 == 2;
    }

    public void G(V v8, int i8) {
        this.f17459c = i8;
        if (this.f17458b == 1) {
            v8.setTranslationY(this.f17457a + i8);
        }
    }

    public void H(V v8) {
        if (this.f17458b == 1) {
            return;
        }
        ViewPropertyAnimator viewPropertyAnimator = this.f17460d;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
            v8.clearAnimation();
        }
        this.f17458b = 1;
        F(v8, this.f17457a + this.f17459c, 175L, l7.a.f21788c);
    }

    public void I(V v8) {
        if (this.f17458b == 2) {
            return;
        }
        ViewPropertyAnimator viewPropertyAnimator = this.f17460d;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
            v8.clearAnimation();
        }
        this.f17458b = 2;
        F(v8, 0, 225L, l7.a.f21789d);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean l(CoordinatorLayout coordinatorLayout, V v8, int i8) {
        this.f17457a = v8.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) v8.getLayoutParams()).bottomMargin;
        return super.l(coordinatorLayout, v8, i8);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void t(CoordinatorLayout coordinatorLayout, V v8, View view, int i8, int i9, int i10, int i11, int i12, int[] iArr) {
        if (i9 > 0) {
            H(v8);
        } else if (i9 < 0) {
            I(v8);
        }
    }
}
