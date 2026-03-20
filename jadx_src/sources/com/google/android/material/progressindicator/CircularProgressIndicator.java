package com.google.android.material.progressindicator;

import android.content.Context;
import android.util.AttributeSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class CircularProgressIndicator extends a<CircularProgressIndicatorSpec> {

    /* renamed from: t  reason: collision with root package name */
    public static final int f18237t = k7.k.f21252x;

    public CircularProgressIndicator(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21059k);
    }

    public CircularProgressIndicator(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8, f18237t);
        s();
    }

    private void s() {
        setIndeterminateDrawable(i.s(getContext(), (CircularProgressIndicatorSpec) this.f18245a));
        setProgressDrawable(e.u(getContext(), (CircularProgressIndicatorSpec) this.f18245a));
    }

    public int getIndicatorDirection() {
        return ((CircularProgressIndicatorSpec) this.f18245a).f18240i;
    }

    public int getIndicatorInset() {
        return ((CircularProgressIndicatorSpec) this.f18245a).f18239h;
    }

    public int getIndicatorSize() {
        return ((CircularProgressIndicatorSpec) this.f18245a).f18238g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.a
    /* renamed from: r */
    public CircularProgressIndicatorSpec i(Context context, AttributeSet attributeSet) {
        return new CircularProgressIndicatorSpec(context, attributeSet);
    }

    public void setIndicatorDirection(int i8) {
        ((CircularProgressIndicatorSpec) this.f18245a).f18240i = i8;
        invalidate();
    }

    public void setIndicatorInset(int i8) {
        S s8 = this.f18245a;
        if (((CircularProgressIndicatorSpec) s8).f18239h != i8) {
            ((CircularProgressIndicatorSpec) s8).f18239h = i8;
            invalidate();
        }
    }

    public void setIndicatorSize(int i8) {
        int max = Math.max(i8, getTrackThickness() * 2);
        S s8 = this.f18245a;
        if (((CircularProgressIndicatorSpec) s8).f18238g != max) {
            ((CircularProgressIndicatorSpec) s8).f18238g = max;
            ((CircularProgressIndicatorSpec) s8).e();
            invalidate();
        }
    }

    @Override // com.google.android.material.progressindicator.a
    public void setTrackThickness(int i8) {
        super.setTrackThickness(i8);
        ((CircularProgressIndicatorSpec) this.f18245a).e();
    }
}
