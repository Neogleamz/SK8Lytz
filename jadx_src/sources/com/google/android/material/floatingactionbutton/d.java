package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.core.view.c0;
import java.util.ArrayList;
import java.util.Iterator;
import x7.m;
import x7.p;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d {
    static final TimeInterpolator F = l7.a.f21788c;
    static final int[] G = {16842919, 16842910};
    static final int[] H = {16843623, 16842908, 16842910};
    static final int[] I = {16842908, 16842910};
    static final int[] J = {16843623, 16842910};
    static final int[] K = {16842910};
    static final int[] L = new int[0];
    private ViewTreeObserver.OnPreDrawListener E;

    /* renamed from: a  reason: collision with root package name */
    m f17962a;

    /* renamed from: b  reason: collision with root package name */
    x7.h f17963b;

    /* renamed from: c  reason: collision with root package name */
    Drawable f17964c;

    /* renamed from: d  reason: collision with root package name */
    com.google.android.material.floatingactionbutton.c f17965d;

    /* renamed from: e  reason: collision with root package name */
    Drawable f17966e;

    /* renamed from: f  reason: collision with root package name */
    boolean f17967f;

    /* renamed from: h  reason: collision with root package name */
    float f17969h;

    /* renamed from: i  reason: collision with root package name */
    float f17970i;

    /* renamed from: j  reason: collision with root package name */
    float f17971j;

    /* renamed from: k  reason: collision with root package name */
    int f17972k;

    /* renamed from: l  reason: collision with root package name */
    private final com.google.android.material.internal.h f17973l;

    /* renamed from: m  reason: collision with root package name */
    private l7.h f17974m;

    /* renamed from: n  reason: collision with root package name */
    private l7.h f17975n;

    /* renamed from: o  reason: collision with root package name */
    private Animator f17976o;

    /* renamed from: p  reason: collision with root package name */
    private l7.h f17977p;
    private l7.h q;

    /* renamed from: r  reason: collision with root package name */
    private float f17978r;

    /* renamed from: t  reason: collision with root package name */
    private int f17980t;

    /* renamed from: v  reason: collision with root package name */
    private ArrayList<Animator.AnimatorListener> f17982v;

    /* renamed from: w  reason: collision with root package name */
    private ArrayList<Animator.AnimatorListener> f17983w;

    /* renamed from: x  reason: collision with root package name */
    private ArrayList<i> f17984x;

    /* renamed from: y  reason: collision with root package name */
    final FloatingActionButton f17985y;

    /* renamed from: z  reason: collision with root package name */
    final w7.b f17986z;

    /* renamed from: g  reason: collision with root package name */
    boolean f17968g = true;

    /* renamed from: s  reason: collision with root package name */
    private float f17979s = 1.0f;

    /* renamed from: u  reason: collision with root package name */
    private int f17981u = 0;
    private final Rect A = new Rect();
    private final RectF B = new RectF();
    private final RectF C = new RectF();
    private final Matrix D = new Matrix();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        private boolean f17987a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ boolean f17988b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ j f17989c;

        a(boolean z4, j jVar) {
            this.f17988b = z4;
            this.f17989c = jVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f17987a = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            d.this.f17981u = 0;
            d.this.f17976o = null;
            if (this.f17987a) {
                return;
            }
            FloatingActionButton floatingActionButton = d.this.f17985y;
            boolean z4 = this.f17988b;
            floatingActionButton.b(z4 ? 8 : 4, z4);
            j jVar = this.f17989c;
            if (jVar != null) {
                jVar.b();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            d.this.f17985y.b(0, this.f17988b);
            d.this.f17981u = 1;
            d.this.f17976o = animator;
            this.f17987a = false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ boolean f17991a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ j f17992b;

        b(boolean z4, j jVar) {
            this.f17991a = z4;
            this.f17992b = jVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            d.this.f17981u = 0;
            d.this.f17976o = null;
            j jVar = this.f17992b;
            if (jVar != null) {
                jVar.a();
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            d.this.f17985y.b(0, this.f17991a);
            d.this.f17981u = 2;
            d.this.f17976o = animator;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends l7.g {
        c() {
        }

        @Override // android.animation.TypeEvaluator
        /* renamed from: a */
        public Matrix evaluate(float f5, Matrix matrix, Matrix matrix2) {
            d.this.f17979s = f5;
            return super.a(f5, matrix, matrix2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.android.material.floatingactionbutton.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0134d implements TypeEvaluator<Float> {

        /* renamed from: a  reason: collision with root package name */
        FloatEvaluator f17995a = new FloatEvaluator();

        C0134d() {
        }

        @Override // android.animation.TypeEvaluator
        /* renamed from: a */
        public Float evaluate(float f5, Float f8, Float f9) {
            float floatValue = this.f17995a.evaluate(f5, (Number) f8, (Number) f9).floatValue();
            if (floatValue < 0.1f) {
                floatValue = 0.0f;
            }
            return Float.valueOf(floatValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements ViewTreeObserver.OnPreDrawListener {
        e() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            d.this.H();
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class f extends l {
        f() {
            super(d.this, null);
        }

        @Override // com.google.android.material.floatingactionbutton.d.l
        protected float a() {
            return 0.0f;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class g extends l {
        g() {
            super(d.this, null);
        }

        @Override // com.google.android.material.floatingactionbutton.d.l
        protected float a() {
            d dVar = d.this;
            return dVar.f17969h + dVar.f17970i;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class h extends l {
        h() {
            super(d.this, null);
        }

        @Override // com.google.android.material.floatingactionbutton.d.l
        protected float a() {
            d dVar = d.this;
            return dVar.f17969h + dVar.f17971j;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface i {
        void a();

        void b();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface j {
        void a();

        void b();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class k extends l {
        k() {
            super(d.this, null);
        }

        @Override // com.google.android.material.floatingactionbutton.d.l
        protected float a() {
            return d.this.f17969h;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private abstract class l extends AnimatorListenerAdapter implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a  reason: collision with root package name */
        private boolean f18002a;

        /* renamed from: b  reason: collision with root package name */
        private float f18003b;

        /* renamed from: c  reason: collision with root package name */
        private float f18004c;

        private l() {
        }

        /* synthetic */ l(d dVar, a aVar) {
            this();
        }

        protected abstract float a();

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            d.this.g0((int) this.f18004c);
            this.f18002a = false;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (!this.f18002a) {
                x7.h hVar = d.this.f17963b;
                this.f18003b = hVar == null ? 0.0f : hVar.w();
                this.f18004c = a();
                this.f18002a = true;
            }
            d dVar = d.this;
            float f5 = this.f18003b;
            dVar.g0((int) (f5 + ((this.f18004c - f5) * valueAnimator.getAnimatedFraction())));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(FloatingActionButton floatingActionButton, w7.b bVar) {
        this.f17985y = floatingActionButton;
        this.f17986z = bVar;
        com.google.android.material.internal.h hVar = new com.google.android.material.internal.h();
        this.f17973l = hVar;
        hVar.a(G, i(new h()));
        hVar.a(H, i(new g()));
        hVar.a(I, i(new g()));
        hVar.a(J, i(new g()));
        hVar.a(K, i(new k()));
        hVar.a(L, i(new f()));
        this.f17978r = floatingActionButton.getRotation();
    }

    private boolean a0() {
        return c0.W(this.f17985y) && !this.f17985y.isInEditMode();
    }

    private void g(float f5, Matrix matrix) {
        matrix.reset();
        Drawable drawable = this.f17985y.getDrawable();
        if (drawable == null || this.f17980t == 0) {
            return;
        }
        RectF rectF = this.B;
        RectF rectF2 = this.C;
        rectF.set(0.0f, 0.0f, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        int i8 = this.f17980t;
        rectF2.set(0.0f, 0.0f, i8, i8);
        matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
        int i9 = this.f17980t;
        matrix.postScale(f5, f5, i9 / 2.0f, i9 / 2.0f);
    }

    private AnimatorSet h(l7.h hVar, float f5, float f8, float f9) {
        ArrayList arrayList = new ArrayList();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.f17985y, View.ALPHA, f5);
        hVar.h("opacity").a(ofFloat);
        arrayList.add(ofFloat);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.f17985y, View.SCALE_X, f8);
        hVar.h("scale").a(ofFloat2);
        h0(ofFloat2);
        arrayList.add(ofFloat2);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.f17985y, View.SCALE_Y, f8);
        hVar.h("scale").a(ofFloat3);
        h0(ofFloat3);
        arrayList.add(ofFloat3);
        g(f9, this.D);
        ObjectAnimator ofObject = ObjectAnimator.ofObject(this.f17985y, new l7.f(), new c(), new Matrix(this.D));
        hVar.h("iconScale").a(ofObject);
        arrayList.add(ofObject);
        AnimatorSet animatorSet = new AnimatorSet();
        l7.b.a(animatorSet, arrayList);
        return animatorSet;
    }

    private void h0(ObjectAnimator objectAnimator) {
        if (Build.VERSION.SDK_INT != 26) {
            return;
        }
        objectAnimator.setEvaluator(new C0134d());
    }

    private ValueAnimator i(l lVar) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(F);
        valueAnimator.setDuration(100L);
        valueAnimator.addListener(lVar);
        valueAnimator.addUpdateListener(lVar);
        valueAnimator.setFloatValues(0.0f, 1.0f);
        return valueAnimator;
    }

    private l7.h l() {
        if (this.f17975n == null) {
            this.f17975n = l7.h.d(this.f17985y.getContext(), k7.a.f21034a);
        }
        return (l7.h) androidx.core.util.h.h(this.f17975n);
    }

    private l7.h m() {
        if (this.f17974m == null) {
            this.f17974m = l7.h.d(this.f17985y.getContext(), k7.a.f21035b);
        }
        return (l7.h) androidx.core.util.h.h(this.f17974m);
    }

    private ViewTreeObserver.OnPreDrawListener r() {
        if (this.E == null) {
            this.E = new e();
        }
        return this.E;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A() {
        this.f17973l.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void B() {
        x7.h hVar = this.f17963b;
        if (hVar != null) {
            x7.i.f(this.f17985y, hVar);
        }
        if (K()) {
            this.f17985y.getViewTreeObserver().addOnPreDrawListener(r());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void C() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D() {
        ViewTreeObserver viewTreeObserver = this.f17985y.getViewTreeObserver();
        ViewTreeObserver.OnPreDrawListener onPreDrawListener = this.E;
        if (onPreDrawListener != null) {
            viewTreeObserver.removeOnPreDrawListener(onPreDrawListener);
            this.E = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void E(int[] iArr) {
        this.f17973l.d(iArr);
    }

    void F(float f5, float f8, float f9) {
        f0();
        g0(f5);
    }

    void G(Rect rect) {
        w7.b bVar;
        Drawable drawable;
        androidx.core.util.h.i(this.f17966e, "Didn't initialize content background");
        if (Z()) {
            drawable = new InsetDrawable(this.f17966e, rect.left, rect.top, rect.right, rect.bottom);
            bVar = this.f17986z;
        } else {
            bVar = this.f17986z;
            drawable = this.f17966e;
        }
        bVar.c(drawable);
    }

    void H() {
        float rotation = this.f17985y.getRotation();
        if (this.f17978r != rotation) {
            this.f17978r = rotation;
            d0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void I() {
        ArrayList<i> arrayList = this.f17984x;
        if (arrayList != null) {
            Iterator<i> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().b();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J() {
        ArrayList<i> arrayList = this.f17984x;
        if (arrayList != null) {
            Iterator<i> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().a();
            }
        }
    }

    boolean K() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L(ColorStateList colorStateList) {
        x7.h hVar = this.f17963b;
        if (hVar != null) {
            hVar.setTintList(colorStateList);
        }
        com.google.android.material.floatingactionbutton.c cVar = this.f17965d;
        if (cVar != null) {
            cVar.c(colorStateList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void M(PorterDuff.Mode mode) {
        x7.h hVar = this.f17963b;
        if (hVar != null) {
            hVar.setTintMode(mode);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void N(float f5) {
        if (this.f17969h != f5) {
            this.f17969h = f5;
            F(f5, this.f17970i, this.f17971j);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void O(boolean z4) {
        this.f17967f = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void P(l7.h hVar) {
        this.q = hVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void Q(float f5) {
        if (this.f17970i != f5) {
            this.f17970i = f5;
            F(this.f17969h, f5, this.f17971j);
        }
    }

    final void R(float f5) {
        this.f17979s = f5;
        Matrix matrix = this.D;
        g(f5, matrix);
        this.f17985y.setImageMatrix(matrix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void S(int i8) {
        if (this.f17980t != i8) {
            this.f17980t = i8;
            e0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void T(int i8) {
        this.f17972k = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void U(float f5) {
        if (this.f17971j != f5) {
            this.f17971j = f5;
            F(this.f17969h, this.f17970i, f5);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void V(ColorStateList colorStateList) {
        Drawable drawable = this.f17964c;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.o(drawable, v7.b.d(colorStateList));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void W(boolean z4) {
        this.f17968g = z4;
        f0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void X(m mVar) {
        this.f17962a = mVar;
        x7.h hVar = this.f17963b;
        if (hVar != null) {
            hVar.setShapeAppearanceModel(mVar);
        }
        Drawable drawable = this.f17964c;
        if (drawable instanceof p) {
            ((p) drawable).setShapeAppearanceModel(mVar);
        }
        com.google.android.material.floatingactionbutton.c cVar = this.f17965d;
        if (cVar != null) {
            cVar.f(mVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void Y(l7.h hVar) {
        this.f17977p = hVar;
    }

    boolean Z() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean b0() {
        return !this.f17967f || this.f17985y.getSizeDimension() >= this.f17972k;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c0(j jVar, boolean z4) {
        if (z()) {
            return;
        }
        Animator animator = this.f17976o;
        if (animator != null) {
            animator.cancel();
        }
        if (!a0()) {
            this.f17985y.b(0, z4);
            this.f17985y.setAlpha(1.0f);
            this.f17985y.setScaleY(1.0f);
            this.f17985y.setScaleX(1.0f);
            R(1.0f);
            if (jVar != null) {
                jVar.a();
                return;
            }
            return;
        }
        if (this.f17985y.getVisibility() != 0) {
            this.f17985y.setAlpha(0.0f);
            this.f17985y.setScaleY(0.0f);
            this.f17985y.setScaleX(0.0f);
            R(0.0f);
        }
        l7.h hVar = this.f17977p;
        if (hVar == null) {
            hVar = m();
        }
        AnimatorSet h8 = h(hVar, 1.0f, 1.0f, 1.0f);
        h8.addListener(new b(z4, jVar));
        ArrayList<Animator.AnimatorListener> arrayList = this.f17982v;
        if (arrayList != null) {
            Iterator<Animator.AnimatorListener> it = arrayList.iterator();
            while (it.hasNext()) {
                h8.addListener(it.next());
            }
        }
        h8.start();
    }

    public void d(Animator.AnimatorListener animatorListener) {
        if (this.f17983w == null) {
            this.f17983w = new ArrayList<>();
        }
        this.f17983w.add(animatorListener);
    }

    void d0() {
        FloatingActionButton floatingActionButton;
        int i8;
        if (Build.VERSION.SDK_INT == 19) {
            if (this.f17978r % 90.0f != 0.0f) {
                i8 = 1;
                if (this.f17985y.getLayerType() != 1) {
                    floatingActionButton = this.f17985y;
                    floatingActionButton.setLayerType(i8, null);
                }
            } else if (this.f17985y.getLayerType() != 0) {
                floatingActionButton = this.f17985y;
                i8 = 0;
                floatingActionButton.setLayerType(i8, null);
            }
        }
        x7.h hVar = this.f17963b;
        if (hVar != null) {
            hVar.h0((int) this.f17978r);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(Animator.AnimatorListener animatorListener) {
        if (this.f17982v == null) {
            this.f17982v = new ArrayList<>();
        }
        this.f17982v.add(animatorListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void e0() {
        R(this.f17979s);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(i iVar) {
        if (this.f17984x == null) {
            this.f17984x = new ArrayList<>();
        }
        this.f17984x.add(iVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void f0() {
        Rect rect = this.A;
        s(rect);
        G(rect);
        this.f17986z.a(rect.left, rect.top, rect.right, rect.bottom);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g0(float f5) {
        x7.h hVar = this.f17963b;
        if (hVar != null) {
            hVar.Z(f5);
        }
    }

    x7.h j() {
        return new x7.h((m) androidx.core.util.h.h(this.f17962a));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Drawable k() {
        return this.f17966e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float n() {
        return this.f17969h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean o() {
        return this.f17967f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final l7.h p() {
        return this.q;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float q() {
        return this.f17970i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(Rect rect) {
        int sizeDimension = this.f17967f ? (this.f17972k - this.f17985y.getSizeDimension()) / 2 : 0;
        float n8 = this.f17968g ? n() + this.f17971j : 0.0f;
        int max = Math.max(sizeDimension, (int) Math.ceil(n8));
        int max2 = Math.max(sizeDimension, (int) Math.ceil(n8 * 1.5f));
        rect.set(max, max2, max, max2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float t() {
        return this.f17971j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final m u() {
        return this.f17962a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final l7.h v() {
        return this.f17977p;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(j jVar, boolean z4) {
        if (y()) {
            return;
        }
        Animator animator = this.f17976o;
        if (animator != null) {
            animator.cancel();
        }
        if (!a0()) {
            this.f17985y.b(z4 ? 8 : 4, z4);
            if (jVar != null) {
                jVar.b();
                return;
            }
            return;
        }
        l7.h hVar = this.q;
        if (hVar == null) {
            hVar = l();
        }
        AnimatorSet h8 = h(hVar, 0.0f, 0.0f, 0.0f);
        h8.addListener(new a(z4, jVar));
        ArrayList<Animator.AnimatorListener> arrayList = this.f17983w;
        if (arrayList != null) {
            Iterator<Animator.AnimatorListener> it = arrayList.iterator();
            while (it.hasNext()) {
                h8.addListener(it.next());
            }
        }
        h8.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(ColorStateList colorStateList, PorterDuff.Mode mode, ColorStateList colorStateList2, int i8) {
        x7.h j8 = j();
        this.f17963b = j8;
        j8.setTintList(colorStateList);
        if (mode != null) {
            this.f17963b.setTintMode(mode);
        }
        this.f17963b.g0(-12303292);
        this.f17963b.P(this.f17985y.getContext());
        v7.a aVar = new v7.a(this.f17963b.D());
        aVar.setTintList(v7.b.d(colorStateList2));
        this.f17964c = aVar;
        this.f17966e = new LayerDrawable(new Drawable[]{(Drawable) androidx.core.util.h.h(this.f17963b), aVar});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean y() {
        return this.f17985y.getVisibility() == 0 ? this.f17981u == 1 : this.f17981u != 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean z() {
        return this.f17985y.getVisibility() != 0 ? this.f17981u == 2 : this.f17981u != 1;
    }
}
