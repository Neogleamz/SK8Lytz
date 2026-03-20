package com.google.android.material.progressindicator;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import androidx.core.view.c0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class LinearProgressIndicator extends a<LinearProgressIndicatorSpec> {

    /* renamed from: t  reason: collision with root package name */
    public static final int f18241t = k7.k.C;

    public LinearProgressIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.A);
    }

    public LinearProgressIndicator(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8, f18241t);
        s();
    }

    private void s() {
        setIndeterminateDrawable(i.t(getContext(), (LinearProgressIndicatorSpec) this.f18245a));
        setProgressDrawable(e.v(getContext(), (LinearProgressIndicatorSpec) this.f18245a));
    }

    public int getIndeterminateAnimationType() {
        return ((LinearProgressIndicatorSpec) this.f18245a).f18242g;
    }

    public int getIndicatorDirection() {
        return ((LinearProgressIndicatorSpec) this.f18245a).f18243h;
    }

    @Override // com.google.android.material.progressindicator.a
    public void o(int i8, boolean z4) {
        S s8 = this.f18245a;
        if (s8 != 0 && ((LinearProgressIndicatorSpec) s8).f18242g == 0 && isIndeterminate()) {
            return;
        }
        super.o(i8, z4);
    }

    @Override // android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        S s8 = this.f18245a;
        LinearProgressIndicatorSpec linearProgressIndicatorSpec = (LinearProgressIndicatorSpec) s8;
        boolean z8 = true;
        if (((LinearProgressIndicatorSpec) s8).f18243h != 1 && ((c0.E(this) != 1 || ((LinearProgressIndicatorSpec) this.f18245a).f18243h != 2) && (c0.E(this) != 0 || ((LinearProgressIndicatorSpec) this.f18245a).f18243h != 3))) {
            z8 = false;
        }
        linearProgressIndicatorSpec.f18244i = z8;
    }

    @Override // android.widget.ProgressBar, android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        int paddingLeft = i8 - (getPaddingLeft() + getPaddingRight());
        int paddingTop = i9 - (getPaddingTop() + getPaddingBottom());
        i<LinearProgressIndicatorSpec> indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable != null) {
            indeterminateDrawable.setBounds(0, 0, paddingLeft, paddingTop);
        }
        e<LinearProgressIndicatorSpec> progressDrawable = getProgressDrawable();
        if (progressDrawable != null) {
            progressDrawable.setBounds(0, 0, paddingLeft, paddingTop);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.a
    /* renamed from: r */
    public LinearProgressIndicatorSpec i(Context context, AttributeSet attributeSet) {
        return new LinearProgressIndicatorSpec(context, attributeSet);
    }

    public void setIndeterminateAnimationType(int i8) {
        i<LinearProgressIndicatorSpec> indeterminateDrawable;
        h<ObjectAnimator> lVar;
        if (((LinearProgressIndicatorSpec) this.f18245a).f18242g == i8) {
            return;
        }
        if (q() && isIndeterminate()) {
            throw new IllegalStateException("Cannot change indeterminate animation type while the progress indicator is show in indeterminate mode.");
        }
        S s8 = this.f18245a;
        ((LinearProgressIndicatorSpec) s8).f18242g = i8;
        ((LinearProgressIndicatorSpec) s8).e();
        if (i8 == 0) {
            indeterminateDrawable = getIndeterminateDrawable();
            lVar = new k((LinearProgressIndicatorSpec) this.f18245a);
        } else {
            indeterminateDrawable = getIndeterminateDrawable();
            lVar = new l(getContext(), (LinearProgressIndicatorSpec) this.f18245a);
        }
        indeterminateDrawable.w(lVar);
        invalidate();
    }

    @Override // com.google.android.material.progressindicator.a
    public void setIndicatorColor(int... iArr) {
        super.setIndicatorColor(iArr);
        ((LinearProgressIndicatorSpec) this.f18245a).e();
    }

    public void setIndicatorDirection(int i8) {
        S s8 = this.f18245a;
        ((LinearProgressIndicatorSpec) s8).f18243h = i8;
        LinearProgressIndicatorSpec linearProgressIndicatorSpec = (LinearProgressIndicatorSpec) s8;
        boolean z4 = true;
        if (i8 != 1 && ((c0.E(this) != 1 || ((LinearProgressIndicatorSpec) this.f18245a).f18243h != 2) && (c0.E(this) != 0 || i8 != 3))) {
            z4 = false;
        }
        linearProgressIndicatorSpec.f18244i = z4;
        invalidate();
    }

    @Override // com.google.android.material.progressindicator.a
    public void setTrackCornerRadius(int i8) {
        super.setTrackCornerRadius(i8);
        ((LinearProgressIndicatorSpec) this.f18245a).e();
        invalidate();
    }
}
