package com.google.android.material.snackbar;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.c0;
import androidx.core.view.m0;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.b;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class BaseTransientBottomBar<B extends BaseTransientBottomBar<B>> {

    /* renamed from: u  reason: collision with root package name */
    static final Handler f18397u;

    /* renamed from: v  reason: collision with root package name */
    private static final boolean f18398v;

    /* renamed from: w  reason: collision with root package name */
    private static final int[] f18399w;

    /* renamed from: x  reason: collision with root package name */
    private static final String f18400x;

    /* renamed from: a  reason: collision with root package name */
    private final ViewGroup f18401a;

    /* renamed from: b  reason: collision with root package name */
    private final Context f18402b;

    /* renamed from: c  reason: collision with root package name */
    protected final w f18403c;

    /* renamed from: d  reason: collision with root package name */
    private final com.google.android.material.snackbar.a f18404d;

    /* renamed from: e  reason: collision with root package name */
    private int f18405e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f18406f;

    /* renamed from: g  reason: collision with root package name */
    private View f18407g;

    /* renamed from: k  reason: collision with root package name */
    private Rect f18411k;

    /* renamed from: l  reason: collision with root package name */
    private int f18412l;

    /* renamed from: m  reason: collision with root package name */
    private int f18413m;

    /* renamed from: n  reason: collision with root package name */
    private int f18414n;

    /* renamed from: o  reason: collision with root package name */
    private int f18415o;

    /* renamed from: p  reason: collision with root package name */
    private int f18416p;
    private List<s<B>> q;

    /* renamed from: r  reason: collision with root package name */
    private Behavior f18417r;

    /* renamed from: s  reason: collision with root package name */
    private final AccessibilityManager f18418s;

    /* renamed from: h  reason: collision with root package name */
    private boolean f18408h = false;

    /* renamed from: i  reason: collision with root package name */
    private final ViewTreeObserver.OnGlobalLayoutListener f18409i = new k();

    /* renamed from: j  reason: collision with root package name */
    private final Runnable f18410j = new l();

    /* renamed from: t  reason: collision with root package name */
    b.InterfaceC0140b f18419t = new o();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class Behavior extends SwipeDismissBehavior<View> {

        /* renamed from: k  reason: collision with root package name */
        private final t f18420k = new t(this);

        /* JADX INFO: Access modifiers changed from: private */
        public void P(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.f18420k.c(baseTransientBottomBar);
        }

        @Override // com.google.android.material.behavior.SwipeDismissBehavior
        public boolean E(View view) {
            return this.f18420k.a(view);
        }

        @Override // com.google.android.material.behavior.SwipeDismissBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean k(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            this.f18420k.b(coordinatorLayout, view, motionEvent);
            return super.k(coordinatorLayout, view, motionEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            w wVar = BaseTransientBottomBar.this.f18403c;
            if (wVar == null) {
                return;
            }
            if (wVar.getParent() != null) {
                BaseTransientBottomBar.this.f18403c.setVisibility(0);
            }
            if (BaseTransientBottomBar.this.f18403c.getAnimationMode() == 1) {
                BaseTransientBottomBar.this.T();
            } else {
                BaseTransientBottomBar.this.V();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends AnimatorListenerAdapter {
        b() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BaseTransientBottomBar.this.L();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f18423a;

        c(int i8) {
            this.f18423a = i8;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BaseTransientBottomBar.this.K(this.f18423a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements ValueAnimator.AnimatorUpdateListener {
        d() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            BaseTransientBottomBar.this.f18403c.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements ValueAnimator.AnimatorUpdateListener {
        e() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            BaseTransientBottomBar.this.f18403c.setScaleX(floatValue);
            BaseTransientBottomBar.this.f18403c.setScaleY(floatValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f extends AnimatorListenerAdapter {
        f() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BaseTransientBottomBar.this.L();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            BaseTransientBottomBar.this.f18404d.a(70, 180);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a  reason: collision with root package name */
        private int f18428a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f18429b;

        g(int i8) {
            this.f18429b = i8;
            this.f18428a = i8;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            if (BaseTransientBottomBar.f18398v) {
                c0.d0(BaseTransientBottomBar.this.f18403c, intValue - this.f18428a);
            } else {
                BaseTransientBottomBar.this.f18403c.setTranslationY(intValue);
            }
            this.f18428a = intValue;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f18431a;

        h(int i8) {
            this.f18431a = i8;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BaseTransientBottomBar.this.K(this.f18431a);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            BaseTransientBottomBar.this.f18404d.b(0, 180);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class i implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a  reason: collision with root package name */
        private int f18433a = 0;

        i() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
            if (BaseTransientBottomBar.f18398v) {
                c0.d0(BaseTransientBottomBar.this.f18403c, intValue - this.f18433a);
            } else {
                BaseTransientBottomBar.this.f18403c.setTranslationY(intValue);
            }
            this.f18433a = intValue;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class j implements Handler.Callback {
        j() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            int i8 = message.what;
            if (i8 == 0) {
                ((BaseTransientBottomBar) message.obj).R();
                return true;
            } else if (i8 != 1) {
                return false;
            } else {
                ((BaseTransientBottomBar) message.obj).H(message.arg1);
                return true;
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class k implements ViewTreeObserver.OnGlobalLayoutListener {
        k() {
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (BaseTransientBottomBar.this.f18408h) {
                BaseTransientBottomBar baseTransientBottomBar = BaseTransientBottomBar.this;
                baseTransientBottomBar.f18416p = baseTransientBottomBar.u();
                BaseTransientBottomBar.this.X();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class l implements Runnable {
        l() {
        }

        @Override // java.lang.Runnable
        public void run() {
            int C;
            BaseTransientBottomBar baseTransientBottomBar = BaseTransientBottomBar.this;
            if (baseTransientBottomBar.f18403c == null || baseTransientBottomBar.f18402b == null || (C = (BaseTransientBottomBar.this.C() - BaseTransientBottomBar.this.F()) + ((int) BaseTransientBottomBar.this.f18403c.getTranslationY())) >= BaseTransientBottomBar.this.f18415o) {
                return;
            }
            ViewGroup.LayoutParams layoutParams = BaseTransientBottomBar.this.f18403c.getLayoutParams();
            if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                Log.w(BaseTransientBottomBar.f18400x, "Unable to apply gesture inset because layout params are not MarginLayoutParams");
                return;
            }
            ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin += BaseTransientBottomBar.this.f18415o - C;
            BaseTransientBottomBar.this.f18403c.requestLayout();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class m implements androidx.core.view.v {
        m() {
        }

        @Override // androidx.core.view.v
        public m0 a(View view, m0 m0Var) {
            BaseTransientBottomBar.this.f18412l = m0Var.j();
            BaseTransientBottomBar.this.f18413m = m0Var.k();
            BaseTransientBottomBar.this.f18414n = m0Var.l();
            BaseTransientBottomBar.this.X();
            return m0Var;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class n extends androidx.core.view.a {
        n() {
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            super.g(view, cVar);
            cVar.a(1048576);
            cVar.h0(true);
        }

        @Override // androidx.core.view.a
        public boolean j(View view, int i8, Bundle bundle) {
            if (i8 == 1048576) {
                BaseTransientBottomBar.this.v();
                return true;
            }
            return super.j(view, i8, bundle);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class o implements b.InterfaceC0140b {
        o() {
        }

        @Override // com.google.android.material.snackbar.b.InterfaceC0140b
        public void a() {
            Handler handler = BaseTransientBottomBar.f18397u;
            handler.sendMessage(handler.obtainMessage(0, BaseTransientBottomBar.this));
        }

        @Override // com.google.android.material.snackbar.b.InterfaceC0140b
        public void b(int i8) {
            Handler handler = BaseTransientBottomBar.f18397u;
            handler.sendMessage(handler.obtainMessage(1, i8, 0, BaseTransientBottomBar.this));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class p implements u {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                BaseTransientBottomBar.this.K(3);
            }
        }

        p() {
        }

        @Override // com.google.android.material.snackbar.BaseTransientBottomBar.u
        public void onViewAttachedToWindow(View view) {
            WindowInsets rootWindowInsets;
            if (Build.VERSION.SDK_INT < 29 || (rootWindowInsets = BaseTransientBottomBar.this.f18403c.getRootWindowInsets()) == null) {
                return;
            }
            BaseTransientBottomBar.this.f18415o = rootWindowInsets.getMandatorySystemGestureInsets().bottom;
            BaseTransientBottomBar.this.X();
        }

        @Override // com.google.android.material.snackbar.BaseTransientBottomBar.u
        public void onViewDetachedFromWindow(View view) {
            if (BaseTransientBottomBar.this.I()) {
                BaseTransientBottomBar.f18397u.post(new a());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class q implements v {
        q() {
        }

        @Override // com.google.android.material.snackbar.BaseTransientBottomBar.v
        public void a(View view, int i8, int i9, int i10, int i11) {
            BaseTransientBottomBar.this.f18403c.setOnLayoutChangeListener(null);
            BaseTransientBottomBar.this.S();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class r implements SwipeDismissBehavior.c {
        r() {
        }

        @Override // com.google.android.material.behavior.SwipeDismissBehavior.c
        public void a(View view) {
            if (view.getParent() != null) {
                view.setVisibility(8);
            }
            BaseTransientBottomBar.this.w(0);
        }

        @Override // com.google.android.material.behavior.SwipeDismissBehavior.c
        public void b(int i8) {
            if (i8 == 0) {
                com.google.android.material.snackbar.b.c().k(BaseTransientBottomBar.this.f18419t);
            } else if (i8 == 1 || i8 == 2) {
                com.google.android.material.snackbar.b.c().j(BaseTransientBottomBar.this.f18419t);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class s<B> {
        public void a(B b9, int i8) {
        }

        public void b(B b9) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class t {

        /* renamed from: a  reason: collision with root package name */
        private b.InterfaceC0140b f18444a;

        public t(SwipeDismissBehavior<?> swipeDismissBehavior) {
            swipeDismissBehavior.L(0.1f);
            swipeDismissBehavior.J(0.6f);
            swipeDismissBehavior.M(0);
        }

        public boolean a(View view) {
            return view instanceof w;
        }

        public void b(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                if (coordinatorLayout.F(view, (int) motionEvent.getX(), (int) motionEvent.getY())) {
                    com.google.android.material.snackbar.b.c().j(this.f18444a);
                }
            } else if (actionMasked == 1 || actionMasked == 3) {
                com.google.android.material.snackbar.b.c().k(this.f18444a);
            }
        }

        public void c(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.f18444a = baseTransientBottomBar.f18419t;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface u {
        void onViewAttachedToWindow(View view);

        void onViewDetachedFromWindow(View view);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface v {
        void a(View view, int i8, int i9, int i10, int i11);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class w extends FrameLayout {

        /* renamed from: h  reason: collision with root package name */
        private static final View.OnTouchListener f18445h = new a();

        /* renamed from: a  reason: collision with root package name */
        private v f18446a;

        /* renamed from: b  reason: collision with root package name */
        private u f18447b;

        /* renamed from: c  reason: collision with root package name */
        private int f18448c;

        /* renamed from: d  reason: collision with root package name */
        private final float f18449d;

        /* renamed from: e  reason: collision with root package name */
        private final float f18450e;

        /* renamed from: f  reason: collision with root package name */
        private ColorStateList f18451f;

        /* renamed from: g  reason: collision with root package name */
        private PorterDuff.Mode f18452g;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements View.OnTouchListener {
            a() {
            }

            @Override // android.view.View.OnTouchListener
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public w(Context context, AttributeSet attributeSet) {
            super(y7.a.c(context, attributeSet, 0, 0), attributeSet);
            Context context2 = getContext();
            TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet, k7.l.f21273b7);
            int i8 = k7.l.f21337i7;
            if (obtainStyledAttributes.hasValue(i8)) {
                c0.B0(this, obtainStyledAttributes.getDimensionPixelSize(i8, 0));
            }
            this.f18448c = obtainStyledAttributes.getInt(k7.l.f21302e7, 0);
            this.f18449d = obtainStyledAttributes.getFloat(k7.l.f21310f7, 1.0f);
            setBackgroundTintList(u7.c.a(context2, obtainStyledAttributes, k7.l.f21319g7));
            setBackgroundTintMode(com.google.android.material.internal.s.i(obtainStyledAttributes.getInt(k7.l.f21328h7, -1), PorterDuff.Mode.SRC_IN));
            this.f18450e = obtainStyledAttributes.getFloat(k7.l.f21293d7, 1.0f);
            obtainStyledAttributes.recycle();
            setOnTouchListener(f18445h);
            setFocusable(true);
            if (getBackground() == null) {
                c0.x0(this, a());
            }
        }

        private Drawable a() {
            float dimension = getResources().getDimension(k7.d.f21128t0);
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(0);
            gradientDrawable.setCornerRadius(dimension);
            gradientDrawable.setColor(n7.a.i(this, k7.b.f21066s, k7.b.f21064p, getBackgroundOverlayColorAlpha()));
            if (this.f18451f != null) {
                Drawable r4 = androidx.core.graphics.drawable.a.r(gradientDrawable);
                androidx.core.graphics.drawable.a.o(r4, this.f18451f);
                return r4;
            }
            return androidx.core.graphics.drawable.a.r(gradientDrawable);
        }

        float getActionTextColorAlpha() {
            return this.f18450e;
        }

        int getAnimationMode() {
            return this.f18448c;
        }

        float getBackgroundOverlayColorAlpha() {
            return this.f18449d;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            u uVar = this.f18447b;
            if (uVar != null) {
                uVar.onViewAttachedToWindow(this);
            }
            c0.q0(this);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            u uVar = this.f18447b;
            if (uVar != null) {
                uVar.onViewDetachedFromWindow(this);
            }
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
            super.onLayout(z4, i8, i9, i10, i11);
            v vVar = this.f18446a;
            if (vVar != null) {
                vVar.a(this, i8, i9, i10, i11);
            }
        }

        void setAnimationMode(int i8) {
            this.f18448c = i8;
        }

        @Override // android.view.View
        public void setBackground(Drawable drawable) {
            setBackgroundDrawable(drawable);
        }

        @Override // android.view.View
        public void setBackgroundDrawable(Drawable drawable) {
            if (drawable != null && this.f18451f != null) {
                drawable = androidx.core.graphics.drawable.a.r(drawable.mutate());
                androidx.core.graphics.drawable.a.o(drawable, this.f18451f);
                androidx.core.graphics.drawable.a.p(drawable, this.f18452g);
            }
            super.setBackgroundDrawable(drawable);
        }

        @Override // android.view.View
        public void setBackgroundTintList(ColorStateList colorStateList) {
            this.f18451f = colorStateList;
            if (getBackground() != null) {
                Drawable r4 = androidx.core.graphics.drawable.a.r(getBackground().mutate());
                androidx.core.graphics.drawable.a.o(r4, colorStateList);
                androidx.core.graphics.drawable.a.p(r4, this.f18452g);
                if (r4 != getBackground()) {
                    super.setBackgroundDrawable(r4);
                }
            }
        }

        @Override // android.view.View
        public void setBackgroundTintMode(PorterDuff.Mode mode) {
            this.f18452g = mode;
            if (getBackground() != null) {
                Drawable r4 = androidx.core.graphics.drawable.a.r(getBackground().mutate());
                androidx.core.graphics.drawable.a.p(r4, mode);
                if (r4 != getBackground()) {
                    super.setBackgroundDrawable(r4);
                }
            }
        }

        void setOnAttachStateChangeListener(u uVar) {
            this.f18447b = uVar;
        }

        @Override // android.view.View
        public void setOnClickListener(View.OnClickListener onClickListener) {
            setOnTouchListener(onClickListener != null ? null : f18445h);
            super.setOnClickListener(onClickListener);
        }

        void setOnLayoutChangeListener(v vVar) {
            this.f18446a = vVar;
        }
    }

    static {
        int i8 = Build.VERSION.SDK_INT;
        f18398v = i8 >= 16 && i8 <= 19;
        f18399w = new int[]{k7.b.P};
        f18400x = BaseTransientBottomBar.class.getSimpleName();
        f18397u = new Handler(Looper.getMainLooper(), new j());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BaseTransientBottomBar(Context context, ViewGroup viewGroup, View view, com.google.android.material.snackbar.a aVar) {
        if (viewGroup == null) {
            throw new IllegalArgumentException("Transient bottom bar must have non-null parent");
        }
        if (view == null) {
            throw new IllegalArgumentException("Transient bottom bar must have non-null content");
        }
        if (aVar == null) {
            throw new IllegalArgumentException("Transient bottom bar must have non-null callback");
        }
        this.f18401a = viewGroup;
        this.f18404d = aVar;
        this.f18402b = context;
        com.google.android.material.internal.m.a(context);
        w wVar = (w) LayoutInflater.from(context).inflate(D(), viewGroup, false);
        this.f18403c = wVar;
        if (view instanceof SnackbarContentLayout) {
            ((SnackbarContentLayout) view).c(wVar.getActionTextColorAlpha());
        }
        wVar.addView(view);
        ViewGroup.LayoutParams layoutParams = wVar.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            this.f18411k = new Rect(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
        }
        c0.v0(wVar, 1);
        c0.E0(wVar, 1);
        c0.C0(wVar, true);
        c0.I0(wVar, new m());
        c0.t0(wVar, new n());
        this.f18418s = (AccessibilityManager) context.getSystemService("accessibility");
    }

    private ValueAnimator B(float... fArr) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setInterpolator(l7.a.f21789d);
        ofFloat.addUpdateListener(new e());
        return ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int C() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) this.f18402b.getSystemService("window")).getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private int E() {
        int height = this.f18403c.getHeight();
        ViewGroup.LayoutParams layoutParams = this.f18403c.getLayoutParams();
        return layoutParams instanceof ViewGroup.MarginLayoutParams ? height + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin : height;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int F() {
        int[] iArr = new int[2];
        this.f18403c.getLocationOnScreen(iArr);
        return iArr[1] + this.f18403c.getHeight();
    }

    private boolean J() {
        ViewGroup.LayoutParams layoutParams = this.f18403c.getLayoutParams();
        return (layoutParams instanceof CoordinatorLayout.e) && (((CoordinatorLayout.e) layoutParams).f() instanceof SwipeDismissBehavior);
    }

    private void N(CoordinatorLayout.e eVar) {
        SwipeDismissBehavior<? extends View> swipeDismissBehavior = this.f18417r;
        if (swipeDismissBehavior == null) {
            swipeDismissBehavior = A();
        }
        if (swipeDismissBehavior instanceof Behavior) {
            ((Behavior) swipeDismissBehavior).P(this);
        }
        swipeDismissBehavior.K(new r());
        eVar.o(swipeDismissBehavior);
        if (this.f18407g == null) {
            eVar.f4390g = 80;
        }
    }

    private boolean P() {
        return this.f18415o > 0 && !this.f18406f && J();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void S() {
        if (O()) {
            s();
            return;
        }
        if (this.f18403c.getParent() != null) {
            this.f18403c.setVisibility(0);
        }
        L();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void T() {
        ValueAnimator x8 = x(0.0f, 1.0f);
        ValueAnimator B = B(0.8f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(x8, B);
        animatorSet.setDuration(150L);
        animatorSet.addListener(new b());
        animatorSet.start();
    }

    private void U(int i8) {
        ValueAnimator x8 = x(1.0f, 0.0f);
        x8.setDuration(75L);
        x8.addListener(new c(i8));
        x8.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void V() {
        int E = E();
        if (f18398v) {
            c0.d0(this.f18403c, E);
        } else {
            this.f18403c.setTranslationY(E);
        }
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(E, 0);
        valueAnimator.setInterpolator(l7.a.f21787b);
        valueAnimator.setDuration(250L);
        valueAnimator.addListener(new f());
        valueAnimator.addUpdateListener(new g(E));
        valueAnimator.start();
    }

    private void W(int i8) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0, E());
        valueAnimator.setInterpolator(l7.a.f21787b);
        valueAnimator.setDuration(250L);
        valueAnimator.addListener(new h(i8));
        valueAnimator.addUpdateListener(new i());
        valueAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void X() {
        Rect rect;
        ViewGroup.LayoutParams layoutParams = this.f18403c.getLayoutParams();
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams) || (rect = this.f18411k) == null) {
            Log.w(f18400x, "Unable to update margins because layout params are not MarginLayoutParams");
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
        marginLayoutParams.bottomMargin = rect.bottom + (this.f18407g != null ? this.f18416p : this.f18412l);
        marginLayoutParams.leftMargin = rect.left + this.f18413m;
        marginLayoutParams.rightMargin = rect.right + this.f18414n;
        this.f18403c.requestLayout();
        if (Build.VERSION.SDK_INT < 29 || !P()) {
            return;
        }
        this.f18403c.removeCallbacks(this.f18410j);
        this.f18403c.post(this.f18410j);
    }

    private void t(int i8) {
        if (this.f18403c.getAnimationMode() == 1) {
            U(i8);
        } else {
            W(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int u() {
        View view = this.f18407g;
        if (view == null) {
            return 0;
        }
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        int i8 = iArr[1];
        int[] iArr2 = new int[2];
        this.f18401a.getLocationOnScreen(iArr2);
        return (iArr2[1] + this.f18401a.getHeight()) - i8;
    }

    private ValueAnimator x(float... fArr) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setInterpolator(l7.a.f21786a);
        ofFloat.addUpdateListener(new d());
        return ofFloat;
    }

    protected SwipeDismissBehavior<? extends View> A() {
        return new Behavior();
    }

    protected int D() {
        return G() ? k7.h.A : k7.h.f21181c;
    }

    protected boolean G() {
        TypedArray obtainStyledAttributes = this.f18402b.obtainStyledAttributes(f18399w);
        int resourceId = obtainStyledAttributes.getResourceId(0, -1);
        obtainStyledAttributes.recycle();
        return resourceId != -1;
    }

    final void H(int i8) {
        if (O() && this.f18403c.getVisibility() == 0) {
            t(i8);
        } else {
            K(i8);
        }
    }

    public boolean I() {
        return com.google.android.material.snackbar.b.c().e(this.f18419t);
    }

    void K(int i8) {
        com.google.android.material.snackbar.b.c().h(this.f18419t);
        List<s<B>> list = this.q;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.q.get(size).a(this, i8);
            }
        }
        ViewParent parent = this.f18403c.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(this.f18403c);
        }
    }

    void L() {
        com.google.android.material.snackbar.b.c().i(this.f18419t);
        List<s<B>> list = this.q;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.q.get(size).b(this);
            }
        }
    }

    public B M(int i8) {
        this.f18405e = i8;
        return this;
    }

    boolean O() {
        AccessibilityManager accessibilityManager = this.f18418s;
        if (accessibilityManager == null) {
            return true;
        }
        List<AccessibilityServiceInfo> enabledAccessibilityServiceList = accessibilityManager.getEnabledAccessibilityServiceList(1);
        return enabledAccessibilityServiceList != null && enabledAccessibilityServiceList.isEmpty();
    }

    public void Q() {
        com.google.android.material.snackbar.b.c().m(z(), this.f18419t);
    }

    final void R() {
        this.f18403c.setOnAttachStateChangeListener(new p());
        if (this.f18403c.getParent() == null) {
            ViewGroup.LayoutParams layoutParams = this.f18403c.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.e) {
                N((CoordinatorLayout.e) layoutParams);
            }
            this.f18416p = u();
            X();
            this.f18403c.setVisibility(4);
            this.f18401a.addView(this.f18403c);
        }
        if (c0.W(this.f18403c)) {
            S();
        } else {
            this.f18403c.setOnLayoutChangeListener(new q());
        }
    }

    void s() {
        this.f18403c.post(new a());
    }

    public void v() {
        w(3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void w(int i8) {
        com.google.android.material.snackbar.b.c().b(this.f18419t, i8);
    }

    public Context y() {
        return this.f18402b;
    }

    public int z() {
        return this.f18405e;
    }
}
