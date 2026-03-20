package com.google.android.material.progressindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.google.android.material.internal.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class LinearProgressIndicatorSpec extends b {

    /* renamed from: g  reason: collision with root package name */
    public int f18242g;

    /* renamed from: h  reason: collision with root package name */
    public int f18243h;

    /* renamed from: i  reason: collision with root package name */
    boolean f18244i;

    public LinearProgressIndicatorSpec(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.A);
    }

    public LinearProgressIndicatorSpec(Context context, AttributeSet attributeSet, int i8) {
        this(context, attributeSet, i8, LinearProgressIndicator.f18241t);
    }

    public LinearProgressIndicatorSpec(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(context, attributeSet, i8, i9);
        TypedArray h8 = m.h(context, attributeSet, k7.l.f21420s3, k7.b.A, LinearProgressIndicator.f18241t, new int[0]);
        this.f18242g = h8.getInt(k7.l.f21429t3, 1);
        this.f18243h = h8.getInt(k7.l.f21438u3, 0);
        h8.recycle();
        e();
        this.f18244i = this.f18243h == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.material.progressindicator.b
    public void e() {
        if (this.f18242g == 0) {
            if (this.f18264b > 0) {
                throw new IllegalArgumentException("Rounded corners are not supported in contiguous indeterminate animation.");
            }
            if (this.f18265c.length < 3) {
                throw new IllegalArgumentException("Contiguous indeterminate animation must be used with 3 or more indicator colors.");
            }
        }
    }
}
