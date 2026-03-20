package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Property;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k extends h<ObjectAnimator> {

    /* renamed from: j  reason: collision with root package name */
    private static final Property<k, Float> f18319j = new b(Float.class, "animationFraction");

    /* renamed from: d  reason: collision with root package name */
    private ObjectAnimator f18320d;

    /* renamed from: e  reason: collision with root package name */
    private d1.b f18321e;

    /* renamed from: f  reason: collision with root package name */
    private final com.google.android.material.progressindicator.b f18322f;

    /* renamed from: g  reason: collision with root package name */
    private int f18323g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f18324h;

    /* renamed from: i  reason: collision with root package name */
    private float f18325i;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            super.onAnimationRepeat(animator);
            k kVar = k.this;
            kVar.f18323g = (kVar.f18323g + 1) % k.this.f18322f.f18265c.length;
            k.this.f18324h = true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b extends Property<k, Float> {
        b(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(k kVar) {
            return Float.valueOf(kVar.n());
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(k kVar, Float f5) {
            kVar.r(f5.floatValue());
        }
    }

    public k(LinearProgressIndicatorSpec linearProgressIndicatorSpec) {
        super(3);
        this.f18323g = 1;
        this.f18322f = linearProgressIndicatorSpec;
        this.f18321e = new d1.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float n() {
        return this.f18325i;
    }

    private void o() {
        if (this.f18320d == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, f18319j, 0.0f, 1.0f);
            this.f18320d = ofFloat;
            ofFloat.setDuration(333L);
            this.f18320d.setInterpolator(null);
            this.f18320d.setRepeatCount(-1);
            this.f18320d.addListener(new a());
        }
    }

    private void p() {
        if (!this.f18324h || this.f18312b[3] >= 1.0f) {
            return;
        }
        int[] iArr = this.f18313c;
        iArr[2] = iArr[1];
        iArr[1] = iArr[0];
        iArr[0] = n7.a.a(this.f18322f.f18265c[this.f18323g], this.f18311a.getAlpha());
        this.f18324h = false;
    }

    private void s(int i8) {
        this.f18312b[0] = 0.0f;
        float b9 = b(i8, 0, 667);
        float[] fArr = this.f18312b;
        float interpolation = this.f18321e.getInterpolation(b9);
        fArr[2] = interpolation;
        fArr[1] = interpolation;
        float[] fArr2 = this.f18312b;
        float interpolation2 = this.f18321e.getInterpolation(b9 + 0.49925038f);
        fArr2[4] = interpolation2;
        fArr2[3] = interpolation2;
        this.f18312b[5] = 1.0f;
    }

    @Override // com.google.android.material.progressindicator.h
    public void a() {
        ObjectAnimator objectAnimator = this.f18320d;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    @Override // com.google.android.material.progressindicator.h
    public void c() {
        q();
    }

    @Override // com.google.android.material.progressindicator.h
    public void d(androidx.vectordrawable.graphics.drawable.b bVar) {
    }

    @Override // com.google.android.material.progressindicator.h
    public void f() {
    }

    @Override // com.google.android.material.progressindicator.h
    public void g() {
        o();
        q();
        this.f18320d.start();
    }

    @Override // com.google.android.material.progressindicator.h
    public void h() {
    }

    void q() {
        this.f18324h = true;
        this.f18323g = 1;
        Arrays.fill(this.f18313c, n7.a.a(this.f18322f.f18265c[0], this.f18311a.getAlpha()));
    }

    void r(float f5) {
        this.f18325i = f5;
        s((int) (f5 * 333.0f));
        p();
        this.f18311a.invalidateSelf();
    }
}
