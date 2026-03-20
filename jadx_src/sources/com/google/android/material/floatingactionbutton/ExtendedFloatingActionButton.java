package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.c0;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ExtendedFloatingActionButton extends MaterialButton implements CoordinatorLayout.b {
    private static final int T = k7.k.B;
    static final Property<View, Float> W = new d(Float.class, "width");

    /* renamed from: a0  reason: collision with root package name */
    static final Property<View, Float> f17892a0 = new e(Float.class, "height");

    /* renamed from: b0  reason: collision with root package name */
    static final Property<View, Float> f17893b0 = new f(Float.class, "paddingStart");

    /* renamed from: c0  reason: collision with root package name */
    static final Property<View, Float> f17894c0 = new g(Float.class, "paddingEnd");
    private final com.google.android.material.floatingactionbutton.a A;
    private final com.google.android.material.floatingactionbutton.f B;
    private final com.google.android.material.floatingactionbutton.f C;
    private final com.google.android.material.floatingactionbutton.f E;
    private final com.google.android.material.floatingactionbutton.f F;
    private final int G;
    private int H;
    private int K;
    private final CoordinatorLayout.Behavior<ExtendedFloatingActionButton> L;
    private boolean O;
    private boolean P;
    private boolean Q;
    protected ColorStateList R;

    /* renamed from: z  reason: collision with root package name */
    private int f17895z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    protected static class ExtendedFloatingActionButtonBehavior<T extends ExtendedFloatingActionButton> extends CoordinatorLayout.Behavior<T> {

        /* renamed from: a  reason: collision with root package name */
        private Rect f17896a;

        /* renamed from: b  reason: collision with root package name */
        private j f17897b;

        /* renamed from: c  reason: collision with root package name */
        private j f17898c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f17899d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f17900e;

        public ExtendedFloatingActionButtonBehavior() {
            this.f17899d = false;
            this.f17900e = true;
        }

        public ExtendedFloatingActionButtonBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, k7.l.f21482z2);
            this.f17899d = obtainStyledAttributes.getBoolean(k7.l.A2, false);
            this.f17900e = obtainStyledAttributes.getBoolean(k7.l.B2, true);
            obtainStyledAttributes.recycle();
        }

        private static boolean G(View view) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.e) {
                return ((CoordinatorLayout.e) layoutParams).f() instanceof BottomSheetBehavior;
            }
            return false;
        }

        private boolean J(View view, ExtendedFloatingActionButton extendedFloatingActionButton) {
            return (this.f17899d || this.f17900e) && ((CoordinatorLayout.e) extendedFloatingActionButton.getLayoutParams()).e() == view.getId();
        }

        private boolean L(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, ExtendedFloatingActionButton extendedFloatingActionButton) {
            if (J(appBarLayout, extendedFloatingActionButton)) {
                if (this.f17896a == null) {
                    this.f17896a = new Rect();
                }
                Rect rect = this.f17896a;
                com.google.android.material.internal.c.a(coordinatorLayout, appBarLayout, rect);
                if (rect.bottom <= appBarLayout.getMinimumHeightForVisibleOverlappingContent()) {
                    K(extendedFloatingActionButton);
                    return true;
                }
                E(extendedFloatingActionButton);
                return true;
            }
            return false;
        }

        private boolean M(View view, ExtendedFloatingActionButton extendedFloatingActionButton) {
            if (J(view, extendedFloatingActionButton)) {
                if (view.getTop() < (extendedFloatingActionButton.getHeight() / 2) + ((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.e) extendedFloatingActionButton.getLayoutParams())).topMargin) {
                    K(extendedFloatingActionButton);
                    return true;
                }
                E(extendedFloatingActionButton);
                return true;
            }
            return false;
        }

        protected void E(ExtendedFloatingActionButton extendedFloatingActionButton) {
            boolean z4 = this.f17900e;
            extendedFloatingActionButton.A(z4 ? extendedFloatingActionButton.C : extendedFloatingActionButton.E, z4 ? this.f17898c : this.f17897b);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: F */
        public boolean b(CoordinatorLayout coordinatorLayout, ExtendedFloatingActionButton extendedFloatingActionButton, Rect rect) {
            return super.b(coordinatorLayout, extendedFloatingActionButton, rect);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: H */
        public boolean h(CoordinatorLayout coordinatorLayout, ExtendedFloatingActionButton extendedFloatingActionButton, View view) {
            if (view instanceof AppBarLayout) {
                L(coordinatorLayout, (AppBarLayout) view, extendedFloatingActionButton);
                return false;
            } else if (G(view)) {
                M(view, extendedFloatingActionButton);
                return false;
            } else {
                return false;
            }
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: I */
        public boolean l(CoordinatorLayout coordinatorLayout, ExtendedFloatingActionButton extendedFloatingActionButton, int i8) {
            List<View> v8 = coordinatorLayout.v(extendedFloatingActionButton);
            int size = v8.size();
            for (int i9 = 0; i9 < size; i9++) {
                View view = v8.get(i9);
                if (!(view instanceof AppBarLayout)) {
                    if (G(view) && M(view, extendedFloatingActionButton)) {
                        break;
                    }
                } else if (L(coordinatorLayout, (AppBarLayout) view, extendedFloatingActionButton)) {
                    break;
                }
            }
            coordinatorLayout.M(extendedFloatingActionButton, i8);
            return true;
        }

        protected void K(ExtendedFloatingActionButton extendedFloatingActionButton) {
            boolean z4 = this.f17900e;
            extendedFloatingActionButton.A(z4 ? extendedFloatingActionButton.B : extendedFloatingActionButton.F, z4 ? this.f17898c : this.f17897b);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void g(CoordinatorLayout.e eVar) {
            if (eVar.f4391h == 0) {
                eVar.f4391h = 80;
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements l {
        a() {
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int a() {
            return ExtendedFloatingActionButton.this.K;
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public ViewGroup.LayoutParams b() {
            return new ViewGroup.LayoutParams(-2, -2);
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int c() {
            return ExtendedFloatingActionButton.this.H;
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int getHeight() {
            return ExtendedFloatingActionButton.this.getMeasuredHeight();
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int getWidth() {
            return (ExtendedFloatingActionButton.this.getMeasuredWidth() - (ExtendedFloatingActionButton.this.getCollapsedPadding() * 2)) + ExtendedFloatingActionButton.this.H + ExtendedFloatingActionButton.this.K;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements l {
        b() {
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int a() {
            return ExtendedFloatingActionButton.this.getCollapsedPadding();
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public ViewGroup.LayoutParams b() {
            return new ViewGroup.LayoutParams(getWidth(), getHeight());
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int c() {
            return ExtendedFloatingActionButton.this.getCollapsedPadding();
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int getHeight() {
            return ExtendedFloatingActionButton.this.getCollapsedSize();
        }

        @Override // com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.l
        public int getWidth() {
            return ExtendedFloatingActionButton.this.getCollapsedSize();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        private boolean f17903a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ com.google.android.material.floatingactionbutton.f f17904b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ j f17905c;

        c(com.google.android.material.floatingactionbutton.f fVar, j jVar) {
            this.f17904b = fVar;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.f17903a = true;
            this.f17904b.b();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f17904b.g();
            if (this.f17903a) {
                return;
            }
            this.f17904b.j(this.f17905c);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.f17904b.onAnimationStart(animator);
            this.f17903a = false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class d extends Property<View, Float> {
        d(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(View view) {
            return Float.valueOf(view.getLayoutParams().width);
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(View view, Float f5) {
            view.getLayoutParams().width = f5.intValue();
            view.requestLayout();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e extends Property<View, Float> {
        e(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(View view) {
            return Float.valueOf(view.getLayoutParams().height);
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(View view, Float f5) {
            view.getLayoutParams().height = f5.intValue();
            view.requestLayout();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class f extends Property<View, Float> {
        f(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(View view) {
            return Float.valueOf(c0.J(view));
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(View view, Float f5) {
            c0.J0(view, f5.intValue(), view.getPaddingTop(), c0.I(view), view.getPaddingBottom());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class g extends Property<View, Float> {
        g(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(View view) {
            return Float.valueOf(c0.I(view));
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(View view, Float f5) {
            c0.J0(view, c0.J(view), view.getPaddingTop(), f5.intValue(), view.getPaddingBottom());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class h extends com.google.android.material.floatingactionbutton.b {

        /* renamed from: g  reason: collision with root package name */
        private final l f17907g;

        /* renamed from: h  reason: collision with root package name */
        private final boolean f17908h;

        h(com.google.android.material.floatingactionbutton.a aVar, l lVar, boolean z4) {
            super(ExtendedFloatingActionButton.this, aVar);
            this.f17907g = lVar;
            this.f17908h = z4;
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public int c() {
            return this.f17908h ? k7.a.f21041h : k7.a.f21040g;
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public void d() {
            ExtendedFloatingActionButton.this.O = this.f17908h;
            ViewGroup.LayoutParams layoutParams = ExtendedFloatingActionButton.this.getLayoutParams();
            if (layoutParams == null) {
                return;
            }
            layoutParams.width = this.f17907g.b().width;
            layoutParams.height = this.f17907g.b().height;
            c0.J0(ExtendedFloatingActionButton.this, this.f17907g.c(), ExtendedFloatingActionButton.this.getPaddingTop(), this.f17907g.a(), ExtendedFloatingActionButton.this.getPaddingBottom());
            ExtendedFloatingActionButton.this.requestLayout();
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public boolean f() {
            return this.f17908h == ExtendedFloatingActionButton.this.O || ExtendedFloatingActionButton.this.getIcon() == null || TextUtils.isEmpty(ExtendedFloatingActionButton.this.getText());
        }

        @Override // com.google.android.material.floatingactionbutton.b, com.google.android.material.floatingactionbutton.f
        public void g() {
            super.g();
            ExtendedFloatingActionButton.this.P = false;
            ExtendedFloatingActionButton.this.setHorizontallyScrolling(false);
            ViewGroup.LayoutParams layoutParams = ExtendedFloatingActionButton.this.getLayoutParams();
            if (layoutParams == null) {
                return;
            }
            layoutParams.width = this.f17907g.b().width;
            layoutParams.height = this.f17907g.b().height;
        }

        @Override // com.google.android.material.floatingactionbutton.b, com.google.android.material.floatingactionbutton.f
        public AnimatorSet h() {
            l7.h m8 = m();
            if (m8.j("width")) {
                PropertyValuesHolder[] g8 = m8.g("width");
                g8[0].setFloatValues(ExtendedFloatingActionButton.this.getWidth(), this.f17907g.getWidth());
                m8.l("width", g8);
            }
            if (m8.j("height")) {
                PropertyValuesHolder[] g9 = m8.g("height");
                g9[0].setFloatValues(ExtendedFloatingActionButton.this.getHeight(), this.f17907g.getHeight());
                m8.l("height", g9);
            }
            if (m8.j("paddingStart")) {
                PropertyValuesHolder[] g10 = m8.g("paddingStart");
                g10[0].setFloatValues(c0.J(ExtendedFloatingActionButton.this), this.f17907g.c());
                m8.l("paddingStart", g10);
            }
            if (m8.j("paddingEnd")) {
                PropertyValuesHolder[] g11 = m8.g("paddingEnd");
                g11[0].setFloatValues(c0.I(ExtendedFloatingActionButton.this), this.f17907g.a());
                m8.l("paddingEnd", g11);
            }
            if (m8.j("labelOpacity")) {
                PropertyValuesHolder[] g12 = m8.g("labelOpacity");
                boolean z4 = this.f17908h;
                g12[0].setFloatValues(z4 ? 0.0f : 1.0f, z4 ? 1.0f : 0.0f);
                m8.l("labelOpacity", g12);
            }
            return super.l(m8);
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public void j(j jVar) {
        }

        @Override // com.google.android.material.floatingactionbutton.b, com.google.android.material.floatingactionbutton.f
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            ExtendedFloatingActionButton.this.O = this.f17908h;
            ExtendedFloatingActionButton.this.P = true;
            ExtendedFloatingActionButton.this.setHorizontallyScrolling(true);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class i extends com.google.android.material.floatingactionbutton.b {

        /* renamed from: g  reason: collision with root package name */
        private boolean f17910g;

        public i(com.google.android.material.floatingactionbutton.a aVar) {
            super(ExtendedFloatingActionButton.this, aVar);
        }

        @Override // com.google.android.material.floatingactionbutton.b, com.google.android.material.floatingactionbutton.f
        public void b() {
            super.b();
            this.f17910g = true;
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public int c() {
            return k7.a.f21042i;
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public void d() {
            ExtendedFloatingActionButton.this.setVisibility(8);
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public boolean f() {
            return ExtendedFloatingActionButton.this.y();
        }

        @Override // com.google.android.material.floatingactionbutton.b, com.google.android.material.floatingactionbutton.f
        public void g() {
            super.g();
            ExtendedFloatingActionButton.this.f17895z = 0;
            if (this.f17910g) {
                return;
            }
            ExtendedFloatingActionButton.this.setVisibility(8);
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public void j(j jVar) {
        }

        @Override // com.google.android.material.floatingactionbutton.b, com.google.android.material.floatingactionbutton.f
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            this.f17910g = false;
            ExtendedFloatingActionButton.this.setVisibility(0);
            ExtendedFloatingActionButton.this.f17895z = 1;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class j {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class k extends com.google.android.material.floatingactionbutton.b {
        public k(com.google.android.material.floatingactionbutton.a aVar) {
            super(ExtendedFloatingActionButton.this, aVar);
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public int c() {
            return k7.a.f21043j;
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public void d() {
            ExtendedFloatingActionButton.this.setVisibility(0);
            ExtendedFloatingActionButton.this.setAlpha(1.0f);
            ExtendedFloatingActionButton.this.setScaleY(1.0f);
            ExtendedFloatingActionButton.this.setScaleX(1.0f);
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public boolean f() {
            return ExtendedFloatingActionButton.this.z();
        }

        @Override // com.google.android.material.floatingactionbutton.b, com.google.android.material.floatingactionbutton.f
        public void g() {
            super.g();
            ExtendedFloatingActionButton.this.f17895z = 0;
        }

        @Override // com.google.android.material.floatingactionbutton.f
        public void j(j jVar) {
        }

        @Override // com.google.android.material.floatingactionbutton.b, com.google.android.material.floatingactionbutton.f
        public void onAnimationStart(Animator animator) {
            super.onAnimationStart(animator);
            ExtendedFloatingActionButton.this.setVisibility(0);
            ExtendedFloatingActionButton.this.f17895z = 2;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface l {
        int a();

        ViewGroup.LayoutParams b();

        int c();

        int getHeight();

        int getWidth();
    }

    public ExtendedFloatingActionButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21071x);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ExtendedFloatingActionButton(android.content.Context r17, android.util.AttributeSet r18, int r19) {
        /*
            r16 = this;
            r0 = r16
            r7 = r18
            r8 = r19
            int r9 = com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.T
            r1 = r17
            android.content.Context r1 = y7.a.c(r1, r7, r8, r9)
            r0.<init>(r1, r7, r8)
            r10 = 0
            r0.f17895z = r10
            com.google.android.material.floatingactionbutton.a r1 = new com.google.android.material.floatingactionbutton.a
            r1.<init>()
            r0.A = r1
            com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$k r11 = new com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$k
            r11.<init>(r1)
            r0.E = r11
            com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$i r12 = new com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$i
            r12.<init>(r1)
            r0.F = r12
            r13 = 1
            r0.O = r13
            r0.P = r10
            r0.Q = r10
            android.content.Context r14 = r16.getContext()
            com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$ExtendedFloatingActionButtonBehavior r1 = new com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$ExtendedFloatingActionButtonBehavior
            r1.<init>(r14, r7)
            r0.L = r1
            int[] r3 = k7.l.f21428t2
            int[] r6 = new int[r10]
            r1 = r14
            r2 = r18
            r4 = r19
            r5 = r9
            android.content.res.TypedArray r1 = com.google.android.material.internal.m.h(r1, r2, r3, r4, r5, r6)
            int r2 = k7.l.f21464x2
            l7.h r2 = l7.h.c(r14, r1, r2)
            int r3 = k7.l.f21455w2
            l7.h r3 = l7.h.c(r14, r1, r3)
            int r4 = k7.l.f21446v2
            l7.h r4 = l7.h.c(r14, r1, r4)
            int r5 = k7.l.f21473y2
            l7.h r5 = l7.h.c(r14, r1, r5)
            int r6 = k7.l.f21437u2
            r15 = -1
            int r6 = r1.getDimensionPixelSize(r6, r15)
            r0.G = r6
            int r6 = androidx.core.view.c0.J(r16)
            r0.H = r6
            int r6 = androidx.core.view.c0.I(r16)
            r0.K = r6
            com.google.android.material.floatingactionbutton.a r6 = new com.google.android.material.floatingactionbutton.a
            r6.<init>()
            com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$h r15 = new com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$h
            com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$a r10 = new com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$a
            r10.<init>()
            r15.<init>(r6, r10, r13)
            r0.C = r15
            com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$h r10 = new com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$h
            com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$b r13 = new com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton$b
            r13.<init>()
            r7 = 0
            r10.<init>(r6, r13, r7)
            r0.B = r10
            r11.a(r2)
            r12.a(r3)
            r15.a(r4)
            r10.a(r5)
            r1.recycle()
            x7.c r1 = x7.m.f24211m
            r2 = r18
            x7.m$b r1 = x7.m.g(r14, r2, r8, r9, r1)
            x7.m r1 = r1.m()
            r0.setShapeAppearanceModel(r1)
            r16.B()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void A(com.google.android.material.floatingactionbutton.f fVar, j jVar) {
        if (fVar.f()) {
            return;
        }
        if (!C()) {
            fVar.d();
            fVar.j(jVar);
            return;
        }
        measure(0, 0);
        AnimatorSet h8 = fVar.h();
        h8.addListener(new c(fVar, jVar));
        for (Animator.AnimatorListener animatorListener : fVar.i()) {
            h8.addListener(animatorListener);
        }
        h8.start();
    }

    private void B() {
        this.R = getTextColors();
    }

    private boolean C() {
        return (c0.W(this) || (!z() && this.Q)) && !isInEditMode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean y() {
        return getVisibility() == 0 ? this.f17895z == 1 : this.f17895z != 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean z() {
        return getVisibility() != 0 ? this.f17895z == 2 : this.f17895z != 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void D(ColorStateList colorStateList) {
        super.setTextColor(colorStateList);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.b
    public CoordinatorLayout.Behavior<ExtendedFloatingActionButton> getBehavior() {
        return this.L;
    }

    int getCollapsedPadding() {
        return (getCollapsedSize() - getIconSize()) / 2;
    }

    int getCollapsedSize() {
        int i8 = this.G;
        return i8 < 0 ? (Math.min(c0.J(this), c0.I(this)) * 2) + getIconSize() : i8;
    }

    public l7.h getExtendMotionSpec() {
        return this.C.e();
    }

    public l7.h getHideMotionSpec() {
        return this.F.e();
    }

    public l7.h getShowMotionSpec() {
        return this.E.e();
    }

    public l7.h getShrinkMotionSpec() {
        return this.B.e();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.button.MaterialButton, android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.O && TextUtils.isEmpty(getText()) && getIcon() != null) {
            this.O = false;
            this.B.d();
        }
    }

    public void setAnimateShowBeforeLayout(boolean z4) {
        this.Q = z4;
    }

    public void setExtendMotionSpec(l7.h hVar) {
        this.C.a(hVar);
    }

    public void setExtendMotionSpecResource(int i8) {
        setExtendMotionSpec(l7.h.d(getContext(), i8));
    }

    public void setExtended(boolean z4) {
        if (this.O == z4) {
            return;
        }
        com.google.android.material.floatingactionbutton.f fVar = z4 ? this.C : this.B;
        if (fVar.f()) {
            return;
        }
        fVar.d();
    }

    public void setHideMotionSpec(l7.h hVar) {
        this.F.a(hVar);
    }

    public void setHideMotionSpecResource(int i8) {
        setHideMotionSpec(l7.h.d(getContext(), i8));
    }

    @Override // android.widget.TextView, android.view.View
    public void setPadding(int i8, int i9, int i10, int i11) {
        super.setPadding(i8, i9, i10, i11);
        if (!this.O || this.P) {
            return;
        }
        this.H = c0.J(this);
        this.K = c0.I(this);
    }

    @Override // android.widget.TextView, android.view.View
    public void setPaddingRelative(int i8, int i9, int i10, int i11) {
        super.setPaddingRelative(i8, i9, i10, i11);
        if (!this.O || this.P) {
            return;
        }
        this.H = i8;
        this.K = i10;
    }

    public void setShowMotionSpec(l7.h hVar) {
        this.E.a(hVar);
    }

    public void setShowMotionSpecResource(int i8) {
        setShowMotionSpec(l7.h.d(getContext(), i8));
    }

    public void setShrinkMotionSpec(l7.h hVar) {
        this.B.a(hVar);
    }

    public void setShrinkMotionSpecResource(int i8) {
        setShrinkMotionSpec(l7.h.d(getContext(), i8));
    }

    @Override // android.widget.TextView
    public void setTextColor(int i8) {
        super.setTextColor(i8);
        B();
    }

    @Override // android.widget.TextView
    public void setTextColor(ColorStateList colorStateList) {
        super.setTextColor(colorStateList);
        B();
    }
}
