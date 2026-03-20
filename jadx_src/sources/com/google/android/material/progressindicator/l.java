package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Property;
import android.view.animation.Interpolator;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l extends h<ObjectAnimator> {

    /* renamed from: l  reason: collision with root package name */
    private static final int[] f18327l = {533, 567, 850, 750};

    /* renamed from: m  reason: collision with root package name */
    private static final int[] f18328m = {1267, 1000, 333, 0};

    /* renamed from: n  reason: collision with root package name */
    private static final Property<l, Float> f18329n = new b(Float.class, "animationFraction");

    /* renamed from: d  reason: collision with root package name */
    private ObjectAnimator f18330d;

    /* renamed from: e  reason: collision with root package name */
    private final Interpolator[] f18331e;

    /* renamed from: f  reason: collision with root package name */
    private final com.google.android.material.progressindicator.b f18332f;

    /* renamed from: g  reason: collision with root package name */
    private int f18333g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f18334h;

    /* renamed from: i  reason: collision with root package name */
    private float f18335i;

    /* renamed from: j  reason: collision with root package name */
    private boolean f18336j;

    /* renamed from: k  reason: collision with root package name */
    androidx.vectordrawable.graphics.drawable.b f18337k;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            if (l.this.f18336j) {
                l.this.f18330d.setRepeatCount(-1);
                l lVar = l.this;
                lVar.f18337k.a(lVar.f18311a);
                l.this.f18336j = false;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            super.onAnimationRepeat(animator);
            l lVar = l.this;
            lVar.f18333g = (lVar.f18333g + 1) % l.this.f18332f.f18265c.length;
            l.this.f18334h = true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b extends Property<l, Float> {
        b(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(l lVar) {
            return Float.valueOf(lVar.q());
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(l lVar, Float f5) {
            lVar.u(f5.floatValue());
        }
    }

    public l(Context context, LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        super(2);
        this.f18333g = 0;
        this.f18337k = null;
        this.f18332f = linearProgressIndicatorSpec;
        this.f18331e = new Interpolator[]{androidx.vectordrawable.graphics.drawable.d.b(context, k7.a.f21036c), androidx.vectordrawable.graphics.drawable.d.b(context, k7.a.f21037d), androidx.vectordrawable.graphics.drawable.d.b(context, k7.a.f21038e), androidx.vectordrawable.graphics.drawable.d.b(context, k7.a.f21039f)};
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float q() {
        return this.f18335i;
    }

    private void r() {
        if (this.f18330d == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, f18329n, 0.0f, 1.0f);
            this.f18330d = ofFloat;
            ofFloat.setDuration(1800L);
            this.f18330d.setInterpolator(null);
            this.f18330d.setRepeatCount(-1);
            this.f18330d.addListener(new a());
        }
    }

    private void s() {
        if (this.f18334h) {
            Arrays.fill(this.f18313c, n7.a.a(this.f18332f.f18265c[this.f18333g], this.f18311a.getAlpha()));
            this.f18334h = false;
        }
    }

    private void v(int i8) {
        for (int i9 = 0; i9 < 4; i9++) {
            this.f18312b[i9] = Math.max(0.0f, Math.min(1.0f, this.f18331e[i9].getInterpolation(b(i8, f18328m[i9], f18327l[i9]))));
        }
    }

    @Override // com.google.android.material.progressindicator.h
    public void a() {
        ObjectAnimator objectAnimator = this.f18330d;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    @Override // com.google.android.material.progressindicator.h
    public void c() {
        t();
    }

    @Override // com.google.android.material.progressindicator.h
    public void d(androidx.vectordrawable.graphics.drawable.b bVar) {
        this.f18337k = bVar;
    }

    @Override // com.google.android.material.progressindicator.h
    public void f() {
        if (!this.f18311a.isVisible()) {
            a();
            return;
        }
        this.f18336j = true;
        this.f18330d.setRepeatCount(0);
    }

    @Override // com.google.android.material.progressindicator.h
    public void g() {
        r();
        t();
        this.f18330d.start();
    }

    @Override // com.google.android.material.progressindicator.h
    public void h() {
        this.f18337k = null;
    }

    void t() {
        this.f18333g = 0;
        int a9 = n7.a.a(this.f18332f.f18265c[0], this.f18311a.getAlpha());
        int[] iArr = this.f18313c;
        iArr[0] = a9;
        iArr[1] = a9;
    }

    void u(float f5) {
        this.f18335i = f5;
        v((int) (f5 * 1800.0f));
        s();
        this.f18311a.invalidateSelf();
    }
}
