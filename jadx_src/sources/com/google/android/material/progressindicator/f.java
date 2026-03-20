package com.google.android.material.progressindicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Property;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class f extends Drawable implements Animatable {
    private static final Property<f, Float> q = new c(Float.class, "growFraction");

    /* renamed from: a  reason: collision with root package name */
    final Context f18293a;

    /* renamed from: b  reason: collision with root package name */
    final com.google.android.material.progressindicator.b f18294b;

    /* renamed from: d  reason: collision with root package name */
    private ValueAnimator f18296d;

    /* renamed from: e  reason: collision with root package name */
    private ValueAnimator f18297e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f18298f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f18299g;

    /* renamed from: h  reason: collision with root package name */
    private float f18300h;

    /* renamed from: j  reason: collision with root package name */
    private List<androidx.vectordrawable.graphics.drawable.b> f18301j;

    /* renamed from: k  reason: collision with root package name */
    private androidx.vectordrawable.graphics.drawable.b f18302k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f18303l;

    /* renamed from: m  reason: collision with root package name */
    private float f18304m;

    /* renamed from: p  reason: collision with root package name */
    private int f18306p;

    /* renamed from: n  reason: collision with root package name */
    final Paint f18305n = new Paint();

    /* renamed from: c  reason: collision with root package name */
    t7.a f18295c = new t7.a();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            f.this.e();
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
            f.super.setVisible(false, false);
            f.this.d();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c extends Property<f, Float> {
        c(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(f fVar) {
            return Float.valueOf(fVar.g());
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(f fVar, Float f5) {
            fVar.m(f5.floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(Context context, com.google.android.material.progressindicator.b bVar) {
        this.f18293a = context;
        this.f18294b = bVar;
        setAlpha(255);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        androidx.vectordrawable.graphics.drawable.b bVar = this.f18302k;
        if (bVar != null) {
            bVar.a(this);
        }
        List<androidx.vectordrawable.graphics.drawable.b> list = this.f18301j;
        if (list == null || this.f18303l) {
            return;
        }
        for (androidx.vectordrawable.graphics.drawable.b bVar2 : list) {
            bVar2.a(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        androidx.vectordrawable.graphics.drawable.b bVar = this.f18302k;
        if (bVar != null) {
            bVar.b(this);
        }
        List<androidx.vectordrawable.graphics.drawable.b> list = this.f18301j;
        if (list == null || this.f18303l) {
            return;
        }
        for (androidx.vectordrawable.graphics.drawable.b bVar2 : list) {
            bVar2.b(this);
        }
    }

    private void f(ValueAnimator... valueAnimatorArr) {
        boolean z4 = this.f18303l;
        this.f18303l = true;
        for (ValueAnimator valueAnimator : valueAnimatorArr) {
            valueAnimator.end();
        }
        this.f18303l = z4;
    }

    private void k() {
        if (this.f18296d == null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, q, 0.0f, 1.0f);
            this.f18296d = ofFloat;
            ofFloat.setDuration(500L);
            this.f18296d.setInterpolator(l7.a.f21787b);
            o(this.f18296d);
        }
        if (this.f18297e == null) {
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, q, 1.0f, 0.0f);
            this.f18297e = ofFloat2;
            ofFloat2.setDuration(500L);
            this.f18297e.setInterpolator(l7.a.f21787b);
            n(this.f18297e);
        }
    }

    private void n(ValueAnimator valueAnimator) {
        ValueAnimator valueAnimator2 = this.f18297e;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            throw new IllegalArgumentException("Cannot set hideAnimator while the current hideAnimator is running.");
        }
        this.f18297e = valueAnimator;
        valueAnimator.addListener(new b());
    }

    private void o(ValueAnimator valueAnimator) {
        ValueAnimator valueAnimator2 = this.f18296d;
        if (valueAnimator2 != null && valueAnimator2.isRunning()) {
            throw new IllegalArgumentException("Cannot set showAnimator while the current showAnimator is running.");
        }
        this.f18296d = valueAnimator;
        valueAnimator.addListener(new a());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float g() {
        if (this.f18294b.b() || this.f18294b.a()) {
            return (this.f18299g || this.f18298f) ? this.f18300h : this.f18304m;
        }
        return 1.0f;
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f18306p;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public boolean h() {
        return p(false, false, false);
    }

    public boolean i() {
        ValueAnimator valueAnimator = this.f18297e;
        return (valueAnimator != null && valueAnimator.isRunning()) || this.f18299g;
    }

    public boolean isRunning() {
        return j() || i();
    }

    public boolean j() {
        ValueAnimator valueAnimator = this.f18296d;
        return (valueAnimator != null && valueAnimator.isRunning()) || this.f18298f;
    }

    public void l(androidx.vectordrawable.graphics.drawable.b bVar) {
        if (this.f18301j == null) {
            this.f18301j = new ArrayList();
        }
        if (this.f18301j.contains(bVar)) {
            return;
        }
        this.f18301j.add(bVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(float f5) {
        if (this.f18304m != f5) {
            this.f18304m = f5;
            invalidateSelf();
        }
    }

    public boolean p(boolean z4, boolean z8, boolean z9) {
        return q(z4, z8, z9 && this.f18295c.a(this.f18293a.getContentResolver()) > 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean q(boolean z4, boolean z8, boolean z9) {
        k();
        if (isVisible() || z4) {
            ValueAnimator valueAnimator = z4 ? this.f18296d : this.f18297e;
            if (!z9) {
                if (valueAnimator.isRunning()) {
                    valueAnimator.end();
                } else {
                    f(valueAnimator);
                }
                return super.setVisible(z4, false);
            } else if (z9 && valueAnimator.isRunning()) {
                return false;
            } else {
                boolean z10 = !z4 || super.setVisible(z4, false);
                if (!(z4 ? this.f18294b.b() : this.f18294b.a())) {
                    f(valueAnimator);
                    return z10;
                }
                if (z8 || Build.VERSION.SDK_INT < 19 || !valueAnimator.isPaused()) {
                    valueAnimator.start();
                } else {
                    valueAnimator.resume();
                }
                return z10;
            }
        }
        return false;
    }

    public boolean r(androidx.vectordrawable.graphics.drawable.b bVar) {
        List<androidx.vectordrawable.graphics.drawable.b> list = this.f18301j;
        if (list == null || !list.contains(bVar)) {
            return false;
        }
        this.f18301j.remove(bVar);
        if (this.f18301j.isEmpty()) {
            this.f18301j = null;
            return true;
        }
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        this.f18306p = i8;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f18305n.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z4, boolean z8) {
        return p(z4, z8, true);
    }

    public void start() {
        q(true, true, false);
    }

    public void stop() {
        q(false, true, false);
    }
}
