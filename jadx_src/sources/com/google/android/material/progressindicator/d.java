package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Property;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends h<ObjectAnimator> {

    /* renamed from: l  reason: collision with root package name */
    private static final int[] f18273l = {0, 1350, 2700, 4050};

    /* renamed from: m  reason: collision with root package name */
    private static final int[] f18274m = {667, 2017, 3367, 4717};

    /* renamed from: n  reason: collision with root package name */
    private static final int[] f18275n = {1000, 2350, 3700, 5050};

    /* renamed from: o  reason: collision with root package name */
    private static final Property<d, Float> f18276o = new c(Float.class, "animationFraction");

    /* renamed from: p  reason: collision with root package name */
    private static final Property<d, Float> f18277p = new C0139d(Float.class, "completeEndFraction");

    /* renamed from: d  reason: collision with root package name */
    private ObjectAnimator f18278d;

    /* renamed from: e  reason: collision with root package name */
    private ObjectAnimator f18279e;

    /* renamed from: f  reason: collision with root package name */
    private final d1.b f18280f;

    /* renamed from: g  reason: collision with root package name */
    private final com.google.android.material.progressindicator.b f18281g;

    /* renamed from: h  reason: collision with root package name */
    private int f18282h;

    /* renamed from: i  reason: collision with root package name */
    private float f18283i;

    /* renamed from: j  reason: collision with root package name */
    private float f18284j;

    /* renamed from: k  reason: collision with root package name */
    androidx.vectordrawable.graphics.drawable.b f18285k;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            super.onAnimationRepeat(animator);
            d dVar = d.this;
            dVar.f18282h = (dVar.f18282h + 4) % d.this.f18281g.f18265c.length;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends AnimatorListenerAdapter {
        b() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            d.this.a();
            d dVar = d.this;
            dVar.f18285k.a(dVar.f18311a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c extends Property<d, Float> {
        c(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(d dVar) {
            return Float.valueOf(dVar.o());
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(d dVar, Float f5) {
            dVar.t(f5.floatValue());
        }
    }

    /* renamed from: com.google.android.material.progressindicator.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class C0139d extends Property<d, Float> {
        C0139d(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(d dVar) {
            return Float.valueOf(dVar.p());
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(d dVar, Float f5) {
            dVar.u(f5.floatValue());
        }
    }

    public d(CircularProgressIndicatorSpec circularProgressIndicatorSpec) {
        super(1);
        this.f18282h = 0;
        this.f18285k = null;
        this.f18281g = circularProgressIndicatorSpec;
        this.f18280f = new d1.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float o() {
        return this.f18283i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float p() {
        return this.f18284j;
    }

    private void q() {
        if (this.f18278d == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, f18276o, 0.0f, 1.0f);
            this.f18278d = ofFloat;
            ofFloat.setDuration(5400L);
            this.f18278d.setInterpolator(null);
            this.f18278d.setRepeatCount(-1);
            this.f18278d.addListener(new a());
        }
        if (this.f18279e == null) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, f18277p, 0.0f, 1.0f);
            this.f18279e = ofFloat2;
            ofFloat2.setDuration(333L);
            this.f18279e.setInterpolator(this.f18280f);
            this.f18279e.addListener(new b());
        }
    }

    private void r(int i8) {
        for (int i9 = 0; i9 < 4; i9++) {
            float b9 = b(i8, f18275n[i9], 333);
            if (b9 >= 0.0f && b9 <= 1.0f) {
                int i10 = i9 + this.f18282h;
                int[] iArr = this.f18281g.f18265c;
                int length = i10 % iArr.length;
                this.f18313c[0] = l7.c.b().evaluate(this.f18280f.getInterpolation(b9), Integer.valueOf(n7.a.a(iArr[length], this.f18311a.getAlpha())), Integer.valueOf(n7.a.a(this.f18281g.f18265c[(length + 1) % iArr.length], this.f18311a.getAlpha()))).intValue();
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void u(float f5) {
        this.f18284j = f5;
    }

    private void v(int i8) {
        float[] fArr = this.f18312b;
        float f5 = this.f18283i;
        fArr[0] = (f5 * 1520.0f) - 20.0f;
        fArr[1] = f5 * 1520.0f;
        for (int i9 = 0; i9 < 4; i9++) {
            float b9 = b(i8, f18273l[i9], 667);
            float[] fArr2 = this.f18312b;
            fArr2[1] = fArr2[1] + (this.f18280f.getInterpolation(b9) * 250.0f);
            float b10 = b(i8, f18274m[i9], 667);
            float[] fArr3 = this.f18312b;
            fArr3[0] = fArr3[0] + (this.f18280f.getInterpolation(b10) * 250.0f);
        }
        float[] fArr4 = this.f18312b;
        fArr4[0] = fArr4[0] + ((fArr4[1] - fArr4[0]) * this.f18284j);
        fArr4[0] = fArr4[0] / 360.0f;
        fArr4[1] = fArr4[1] / 360.0f;
    }

    @Override // com.google.android.material.progressindicator.h
    void a() {
        ObjectAnimator objectAnimator = this.f18278d;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    @Override // com.google.android.material.progressindicator.h
    public void c() {
        s();
    }

    @Override // com.google.android.material.progressindicator.h
    public void d(androidx.vectordrawable.graphics.drawable.b bVar) {
        this.f18285k = bVar;
    }

    @Override // com.google.android.material.progressindicator.h
    void f() {
        if (this.f18279e.isRunning()) {
            return;
        }
        if (this.f18311a.isVisible()) {
            this.f18279e.start();
        } else {
            a();
        }
    }

    @Override // com.google.android.material.progressindicator.h
    void g() {
        q();
        s();
        this.f18278d.start();
    }

    @Override // com.google.android.material.progressindicator.h
    public void h() {
        this.f18285k = null;
    }

    void s() {
        this.f18282h = 0;
        this.f18313c[0] = n7.a.a(this.f18281g.f18265c[0], this.f18311a.getAlpha());
        this.f18284j = 0.0f;
    }

    void t(float f5) {
        this.f18283i = f5;
        int i8 = (int) (f5 * 5400.0f);
        v(i8);
        r(i8);
        this.f18311a.invalidateSelf();
    }
}
