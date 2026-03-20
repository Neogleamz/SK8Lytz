package com.google.android.material.timepicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.core.view.c0;
import java.util.ArrayList;
import java.util.List;
import k7.k;
import k7.l;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ClockHandView extends View {

    /* renamed from: a  reason: collision with root package name */
    private ValueAnimator f18706a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f18707b;

    /* renamed from: c  reason: collision with root package name */
    private float f18708c;

    /* renamed from: d  reason: collision with root package name */
    private float f18709d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f18710e;

    /* renamed from: f  reason: collision with root package name */
    private int f18711f;

    /* renamed from: g  reason: collision with root package name */
    private final List<d> f18712g;

    /* renamed from: h  reason: collision with root package name */
    private final int f18713h;

    /* renamed from: j  reason: collision with root package name */
    private final float f18714j;

    /* renamed from: k  reason: collision with root package name */
    private final Paint f18715k;

    /* renamed from: l  reason: collision with root package name */
    private final RectF f18716l;

    /* renamed from: m  reason: collision with root package name */
    private final int f18717m;

    /* renamed from: n  reason: collision with root package name */
    private float f18718n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f18719p;
    private c q;

    /* renamed from: t  reason: collision with root package name */
    private double f18720t;

    /* renamed from: w  reason: collision with root package name */
    private int f18721w;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {
        a() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ClockHandView.this.m(((Float) valueAnimator.getAnimatedValue()).floatValue(), true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends AnimatorListenerAdapter {
        b() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            animator.end();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        void a(float f5, boolean z4);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        void a(float f5, boolean z4);
    }

    public ClockHandView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.H);
    }

    public ClockHandView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f18712g = new ArrayList();
        Paint paint = new Paint();
        this.f18715k = paint;
        this.f18716l = new RectF();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, l.H1, i8, k.J);
        this.f18721w = obtainStyledAttributes.getDimensionPixelSize(l.J1, 0);
        this.f18713h = obtainStyledAttributes.getDimensionPixelSize(l.K1, 0);
        Resources resources = getResources();
        this.f18717m = resources.getDimensionPixelSize(k7.d.f21129u);
        this.f18714j = resources.getDimensionPixelSize(k7.d.f21125s);
        int color = obtainStyledAttributes.getColor(l.I1, 0);
        paint.setAntiAlias(true);
        paint.setColor(color);
        k(0.0f);
        this.f18711f = ViewConfiguration.get(context).getScaledTouchSlop();
        c0.E0(this, 2);
        obtainStyledAttributes.recycle();
    }

    private void c(Canvas canvas) {
        int width;
        int height = getHeight() / 2;
        float width2 = getWidth() / 2;
        float f5 = height;
        this.f18715k.setStrokeWidth(0.0f);
        canvas.drawCircle((this.f18721w * ((float) Math.cos(this.f18720t))) + width2, (this.f18721w * ((float) Math.sin(this.f18720t))) + f5, this.f18713h, this.f18715k);
        double sin = Math.sin(this.f18720t);
        double cos = Math.cos(this.f18720t);
        this.f18715k.setStrokeWidth(this.f18717m);
        canvas.drawLine(width2, f5, width + ((int) (cos * r6)), height + ((int) (r6 * sin)), this.f18715k);
        canvas.drawCircle(width2, f5, this.f18714j, this.f18715k);
    }

    private int e(float f5, float f8) {
        int degrees = ((int) Math.toDegrees(Math.atan2(f8 - (getHeight() / 2), f5 - (getWidth() / 2)))) + 90;
        return degrees < 0 ? degrees + 360 : degrees;
    }

    private Pair<Float, Float> h(float f5) {
        float f8 = f();
        if (Math.abs(f8 - f5) > 180.0f) {
            if (f8 > 180.0f && f5 < 180.0f) {
                f5 += 360.0f;
            }
            if (f8 < 180.0f && f5 > 180.0f) {
                f8 += 360.0f;
            }
        }
        return new Pair<>(Float.valueOf(f8), Float.valueOf(f5));
    }

    private boolean i(float f5, float f8, boolean z4, boolean z8, boolean z9) {
        float e8 = e(f5, f8);
        boolean z10 = false;
        boolean z11 = f() != e8;
        if (z8 && z11) {
            return true;
        }
        if (z11 || z4) {
            if (z9 && this.f18707b) {
                z10 = true;
            }
            l(e8, z10);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m(float f5, boolean z4) {
        float f8 = f5 % 360.0f;
        this.f18718n = f8;
        this.f18720t = Math.toRadians(f8 - 90.0f);
        float width = (getWidth() / 2) + (this.f18721w * ((float) Math.cos(this.f18720t)));
        float height = (getHeight() / 2) + (this.f18721w * ((float) Math.sin(this.f18720t)));
        RectF rectF = this.f18716l;
        int i8 = this.f18713h;
        rectF.set(width - i8, height - i8, width + i8, height + i8);
        for (d dVar : this.f18712g) {
            dVar.a(f8, z4);
        }
        invalidate();
    }

    public void b(d dVar) {
        this.f18712g.add(dVar);
    }

    public RectF d() {
        return this.f18716l;
    }

    public float f() {
        return this.f18718n;
    }

    public int g() {
        return this.f18713h;
    }

    public void j(int i8) {
        this.f18721w = i8;
        invalidate();
    }

    public void k(float f5) {
        l(f5, false);
    }

    public void l(float f5, boolean z4) {
        ValueAnimator valueAnimator = this.f18706a;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (!z4) {
            m(f5, false);
            return;
        }
        Pair<Float, Float> h8 = h(f5);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(((Float) h8.first).floatValue(), ((Float) h8.second).floatValue());
        this.f18706a = ofFloat;
        ofFloat.setDuration(200L);
        this.f18706a.addUpdateListener(new a());
        this.f18706a.addListener(new b());
        this.f18706a.start();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        c(canvas);
    }

    @Override // android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        k(f());
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z4;
        boolean z8;
        boolean z9;
        c cVar;
        int actionMasked = motionEvent.getActionMasked();
        float x8 = motionEvent.getX();
        float y8 = motionEvent.getY();
        if (actionMasked == 0) {
            this.f18708c = x8;
            this.f18709d = y8;
            this.f18710e = true;
            this.f18719p = false;
            z4 = false;
            z8 = false;
            z9 = true;
        } else if (actionMasked == 1 || actionMasked == 2) {
            int i8 = (int) (x8 - this.f18708c);
            int i9 = (int) (y8 - this.f18709d);
            this.f18710e = (i8 * i8) + (i9 * i9) > this.f18711f;
            boolean z10 = this.f18719p;
            z4 = actionMasked == 1;
            z9 = false;
            z8 = z10;
        } else {
            z4 = false;
            z8 = false;
            z9 = false;
        }
        boolean i10 = i(x8, y8, z8, z9, z4) | this.f18719p;
        this.f18719p = i10;
        if (i10 && z4 && (cVar = this.q) != null) {
            cVar.a(e(x8, y8), this.f18710e);
        }
        return true;
    }
}
