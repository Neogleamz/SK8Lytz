package com.google.android.material.tabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.o0;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import androidx.core.view.z;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.s;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import k7.k;
@ViewPager.e
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class TabLayout extends HorizontalScrollView {

    /* renamed from: h0  reason: collision with root package name */
    private static final int f18480h0 = k.f21244o;

    /* renamed from: i0  reason: collision with root package name */
    private static final androidx.core.util.e<g> f18481i0 = new androidx.core.util.g(16);
    private final int A;
    private int B;
    int C;
    int E;
    int F;
    int G;
    boolean H;
    boolean K;
    int L;
    boolean O;
    private com.google.android.material.tabs.b P;
    private c Q;
    private final ArrayList<c> R;
    private c T;
    private ValueAnimator W;

    /* renamed from: a  reason: collision with root package name */
    private final ArrayList<g> f18482a;

    /* renamed from: a0  reason: collision with root package name */
    ViewPager f18483a0;

    /* renamed from: b  reason: collision with root package name */
    private g f18484b;

    /* renamed from: b0  reason: collision with root package name */
    private androidx.viewpager.widget.a f18485b0;

    /* renamed from: c  reason: collision with root package name */
    final f f18486c;

    /* renamed from: c0  reason: collision with root package name */
    private DataSetObserver f18487c0;

    /* renamed from: d  reason: collision with root package name */
    int f18488d;

    /* renamed from: d0  reason: collision with root package name */
    private h f18489d0;

    /* renamed from: e  reason: collision with root package name */
    int f18490e;

    /* renamed from: e0  reason: collision with root package name */
    private b f18491e0;

    /* renamed from: f  reason: collision with root package name */
    int f18492f;

    /* renamed from: f0  reason: collision with root package name */
    private boolean f18493f0;

    /* renamed from: g  reason: collision with root package name */
    int f18494g;

    /* renamed from: g0  reason: collision with root package name */
    private final androidx.core.util.e<i> f18495g0;

    /* renamed from: h  reason: collision with root package name */
    int f18496h;

    /* renamed from: j  reason: collision with root package name */
    ColorStateList f18497j;

    /* renamed from: k  reason: collision with root package name */
    ColorStateList f18498k;

    /* renamed from: l  reason: collision with root package name */
    ColorStateList f18499l;

    /* renamed from: m  reason: collision with root package name */
    Drawable f18500m;

    /* renamed from: n  reason: collision with root package name */
    private int f18501n;

    /* renamed from: p  reason: collision with root package name */
    PorterDuff.Mode f18502p;
    float q;

    /* renamed from: t  reason: collision with root package name */
    float f18503t;

    /* renamed from: w  reason: collision with root package name */
    final int f18504w;

    /* renamed from: x  reason: collision with root package name */
    int f18505x;

    /* renamed from: y  reason: collision with root package name */
    private final int f18506y;

    /* renamed from: z  reason: collision with root package name */
    private final int f18507z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {
        a() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            TabLayout.this.scrollTo(((Integer) valueAnimator.getAnimatedValue()).intValue(), 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements ViewPager.h {

        /* renamed from: a  reason: collision with root package name */
        private boolean f18509a;

        b() {
        }

        void a(boolean z4) {
            this.f18509a = z4;
        }

        @Override // androidx.viewpager.widget.ViewPager.h
        public void c(ViewPager viewPager, androidx.viewpager.widget.a aVar, androidx.viewpager.widget.a aVar2) {
            TabLayout tabLayout = TabLayout.this;
            if (tabLayout.f18483a0 == viewPager) {
                tabLayout.I(aVar2, this.f18509a);
            }
        }
    }

    @Deprecated
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c<T extends g> {
        void onTabReselected(T t8);

        void onTabSelected(T t8);

        void onTabUnselected(T t8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d extends c<g> {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e extends DataSetObserver {
        e() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            TabLayout.this.A();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            TabLayout.this.A();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f extends LinearLayout {

        /* renamed from: a  reason: collision with root package name */
        ValueAnimator f18512a;

        /* renamed from: b  reason: collision with root package name */
        int f18513b;

        /* renamed from: c  reason: collision with root package name */
        float f18514c;

        /* renamed from: d  reason: collision with root package name */
        private int f18515d;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a implements ValueAnimator.AnimatorUpdateListener {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ View f18517a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ View f18518b;

            a(View view, View view2) {
                this.f18517a = view;
                this.f18518b = view2;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                f.this.g(this.f18517a, this.f18518b, valueAnimator.getAnimatedFraction());
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class b extends AnimatorListenerAdapter {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ int f18520a;

            b(int i8) {
                this.f18520a = i8;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                f.this.f18513b = this.f18520a;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                f.this.f18513b = this.f18520a;
            }
        }

        f(Context context) {
            super(context);
            this.f18513b = -1;
            this.f18515d = -1;
            setWillNotDraw(false);
        }

        private void d() {
            View childAt = getChildAt(this.f18513b);
            com.google.android.material.tabs.b bVar = TabLayout.this.P;
            TabLayout tabLayout = TabLayout.this;
            bVar.d(tabLayout, childAt, tabLayout.f18500m);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void g(View view, View view2, float f5) {
            if (view != null && view.getWidth() > 0) {
                com.google.android.material.tabs.b bVar = TabLayout.this.P;
                TabLayout tabLayout = TabLayout.this;
                bVar.c(tabLayout, view, view2, f5, tabLayout.f18500m);
            } else {
                Drawable drawable = TabLayout.this.f18500m;
                drawable.setBounds(-1, drawable.getBounds().top, -1, TabLayout.this.f18500m.getBounds().bottom);
            }
            c0.j0(this);
        }

        private void h(boolean z4, int i8, int i9) {
            View childAt = getChildAt(this.f18513b);
            View childAt2 = getChildAt(i8);
            if (childAt2 == null) {
                d();
                return;
            }
            a aVar = new a(childAt, childAt2);
            if (!z4) {
                this.f18512a.removeAllUpdateListeners();
                this.f18512a.addUpdateListener(aVar);
                return;
            }
            ValueAnimator valueAnimator = new ValueAnimator();
            this.f18512a = valueAnimator;
            valueAnimator.setInterpolator(l7.a.f21787b);
            valueAnimator.setDuration(i9);
            valueAnimator.setFloatValues(0.0f, 1.0f);
            valueAnimator.addUpdateListener(aVar);
            valueAnimator.addListener(new b(i8));
            valueAnimator.start();
        }

        void b(int i8, int i9) {
            ValueAnimator valueAnimator = this.f18512a;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.f18512a.cancel();
            }
            h(true, i8, i9);
        }

        boolean c() {
            int childCount = getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                if (getChildAt(i8).getWidth() <= 0) {
                    return true;
                }
            }
            return false;
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x004f  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void draw(android.graphics.Canvas r6) {
            /*
                r5 = this;
                com.google.android.material.tabs.TabLayout r0 = com.google.android.material.tabs.TabLayout.this
                android.graphics.drawable.Drawable r0 = r0.f18500m
                android.graphics.Rect r0 = r0.getBounds()
                int r0 = r0.height()
                if (r0 >= 0) goto L16
                com.google.android.material.tabs.TabLayout r0 = com.google.android.material.tabs.TabLayout.this
                android.graphics.drawable.Drawable r0 = r0.f18500m
                int r0 = r0.getIntrinsicHeight()
            L16:
                com.google.android.material.tabs.TabLayout r1 = com.google.android.material.tabs.TabLayout.this
                int r1 = r1.F
                r2 = 0
                if (r1 == 0) goto L37
                r3 = 1
                r4 = 2
                if (r1 == r3) goto L28
                if (r1 == r4) goto L41
                r0 = 3
                if (r1 == r0) goto L3d
                r0 = r2
                goto L41
            L28:
                int r1 = r5.getHeight()
                int r1 = r1 - r0
                int r2 = r1 / 2
                int r1 = r5.getHeight()
                int r1 = r1 + r0
                int r0 = r1 / 2
                goto L41
            L37:
                int r1 = r5.getHeight()
                int r2 = r1 - r0
            L3d:
                int r0 = r5.getHeight()
            L41:
                com.google.android.material.tabs.TabLayout r1 = com.google.android.material.tabs.TabLayout.this
                android.graphics.drawable.Drawable r1 = r1.f18500m
                android.graphics.Rect r1 = r1.getBounds()
                int r1 = r1.width()
                if (r1 <= 0) goto L8e
                com.google.android.material.tabs.TabLayout r1 = com.google.android.material.tabs.TabLayout.this
                android.graphics.drawable.Drawable r1 = r1.f18500m
                android.graphics.Rect r1 = r1.getBounds()
                com.google.android.material.tabs.TabLayout r3 = com.google.android.material.tabs.TabLayout.this
                android.graphics.drawable.Drawable r3 = r3.f18500m
                int r4 = r1.left
                int r1 = r1.right
                r3.setBounds(r4, r2, r1, r0)
                com.google.android.material.tabs.TabLayout r0 = com.google.android.material.tabs.TabLayout.this
                android.graphics.drawable.Drawable r1 = r0.f18500m
                int r0 = com.google.android.material.tabs.TabLayout.b(r0)
                if (r0 == 0) goto L8b
                android.graphics.drawable.Drawable r1 = androidx.core.graphics.drawable.a.r(r1)
                int r0 = android.os.Build.VERSION.SDK_INT
                r2 = 21
                if (r0 != r2) goto L82
                com.google.android.material.tabs.TabLayout r0 = com.google.android.material.tabs.TabLayout.this
                int r0 = com.google.android.material.tabs.TabLayout.b(r0)
                android.graphics.PorterDuff$Mode r2 = android.graphics.PorterDuff.Mode.SRC_IN
                r1.setColorFilter(r0, r2)
                goto L8b
            L82:
                com.google.android.material.tabs.TabLayout r0 = com.google.android.material.tabs.TabLayout.this
                int r0 = com.google.android.material.tabs.TabLayout.b(r0)
                androidx.core.graphics.drawable.a.n(r1, r0)
            L8b:
                r1.draw(r6)
            L8e:
                super.draw(r6)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.tabs.TabLayout.f.draw(android.graphics.Canvas):void");
        }

        void e(int i8, float f5) {
            ValueAnimator valueAnimator = this.f18512a;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.f18512a.cancel();
            }
            this.f18513b = i8;
            this.f18514c = f5;
            g(getChildAt(i8), getChildAt(this.f18513b + 1), this.f18514c);
        }

        void f(int i8) {
            Rect bounds = TabLayout.this.f18500m.getBounds();
            TabLayout.this.f18500m.setBounds(bounds.left, 0, bounds.right, i8);
            requestLayout();
        }

        @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
            super.onLayout(z4, i8, i9, i10, i11);
            ValueAnimator valueAnimator = this.f18512a;
            if (valueAnimator == null || !valueAnimator.isRunning()) {
                d();
            } else {
                h(false, this.f18513b, -1);
            }
        }

        @Override // android.widget.LinearLayout, android.view.View
        protected void onMeasure(int i8, int i9) {
            super.onMeasure(i8, i9);
            if (View.MeasureSpec.getMode(i8) != 1073741824) {
                return;
            }
            TabLayout tabLayout = TabLayout.this;
            boolean z4 = true;
            if (tabLayout.C == 1 || tabLayout.G == 2) {
                int childCount = getChildCount();
                int i10 = 0;
                for (int i11 = 0; i11 < childCount; i11++) {
                    View childAt = getChildAt(i11);
                    if (childAt.getVisibility() == 0) {
                        i10 = Math.max(i10, childAt.getMeasuredWidth());
                    }
                }
                if (i10 <= 0) {
                    return;
                }
                if (i10 * childCount <= getMeasuredWidth() - (((int) s.c(getContext(), 16)) * 2)) {
                    boolean z8 = false;
                    for (int i12 = 0; i12 < childCount; i12++) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildAt(i12).getLayoutParams();
                        if (layoutParams.width != i10 || layoutParams.weight != 0.0f) {
                            layoutParams.width = i10;
                            layoutParams.weight = 0.0f;
                            z8 = true;
                        }
                    }
                    z4 = z8;
                } else {
                    TabLayout tabLayout2 = TabLayout.this;
                    tabLayout2.C = 0;
                    tabLayout2.P(false);
                }
                if (z4) {
                    super.onMeasure(i8, i9);
                }
            }
        }

        @Override // android.widget.LinearLayout, android.view.View
        public void onRtlPropertiesChanged(int i8) {
            super.onRtlPropertiesChanged(i8);
            if (Build.VERSION.SDK_INT >= 23 || this.f18515d == i8) {
                return;
            }
            requestLayout();
            this.f18515d = i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class g {

        /* renamed from: a  reason: collision with root package name */
        private Object f18522a;

        /* renamed from: b  reason: collision with root package name */
        private Drawable f18523b;

        /* renamed from: c  reason: collision with root package name */
        private CharSequence f18524c;

        /* renamed from: d  reason: collision with root package name */
        private CharSequence f18525d;

        /* renamed from: f  reason: collision with root package name */
        private View f18527f;

        /* renamed from: h  reason: collision with root package name */
        public TabLayout f18529h;

        /* renamed from: i  reason: collision with root package name */
        public i f18530i;

        /* renamed from: e  reason: collision with root package name */
        private int f18526e = -1;

        /* renamed from: g  reason: collision with root package name */
        private int f18528g = 1;

        /* renamed from: j  reason: collision with root package name */
        private int f18531j = -1;

        public View e() {
            return this.f18527f;
        }

        public Drawable f() {
            return this.f18523b;
        }

        public int g() {
            return this.f18526e;
        }

        public int h() {
            return this.f18528g;
        }

        public CharSequence i() {
            return this.f18524c;
        }

        public boolean j() {
            TabLayout tabLayout = this.f18529h;
            if (tabLayout != null) {
                return tabLayout.getSelectedTabPosition() == this.f18526e;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        void k() {
            this.f18529h = null;
            this.f18530i = null;
            this.f18522a = null;
            this.f18523b = null;
            this.f18531j = -1;
            this.f18524c = null;
            this.f18525d = null;
            this.f18526e = -1;
            this.f18527f = null;
        }

        public void l() {
            TabLayout tabLayout = this.f18529h;
            if (tabLayout == null) {
                throw new IllegalArgumentException("Tab not attached to a TabLayout");
            }
            tabLayout.G(this);
        }

        public g m(CharSequence charSequence) {
            this.f18525d = charSequence;
            t();
            return this;
        }

        public g n(int i8) {
            return o(LayoutInflater.from(this.f18530i.getContext()).inflate(i8, (ViewGroup) this.f18530i, false));
        }

        public g o(View view) {
            this.f18527f = view;
            t();
            return this;
        }

        public g p(Drawable drawable) {
            this.f18523b = drawable;
            TabLayout tabLayout = this.f18529h;
            if (tabLayout.C == 1 || tabLayout.G == 2) {
                tabLayout.P(true);
            }
            t();
            if (com.google.android.material.badge.a.f17456a && this.f18530i.l() && this.f18530i.f18539e.isVisible()) {
                this.f18530i.invalidate();
            }
            return this;
        }

        void q(int i8) {
            this.f18526e = i8;
        }

        public g r(int i8) {
            TabLayout tabLayout = this.f18529h;
            if (tabLayout != null) {
                return s(tabLayout.getResources().getText(i8));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public g s(CharSequence charSequence) {
            if (TextUtils.isEmpty(this.f18525d) && !TextUtils.isEmpty(charSequence)) {
                this.f18530i.setContentDescription(charSequence);
            }
            this.f18524c = charSequence;
            t();
            return this;
        }

        void t() {
            i iVar = this.f18530i;
            if (iVar != null) {
                iVar.t();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h implements ViewPager.i {

        /* renamed from: a  reason: collision with root package name */
        private final WeakReference<TabLayout> f18532a;

        /* renamed from: b  reason: collision with root package name */
        private int f18533b;

        /* renamed from: c  reason: collision with root package name */
        private int f18534c;

        public h(TabLayout tabLayout) {
            this.f18532a = new WeakReference<>(tabLayout);
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void a(int i8) {
            TabLayout tabLayout = this.f18532a.get();
            if (tabLayout == null || tabLayout.getSelectedTabPosition() == i8 || i8 >= tabLayout.getTabCount()) {
                return;
            }
            int i9 = this.f18534c;
            tabLayout.H(tabLayout.x(i8), i9 == 0 || (i9 == 2 && this.f18533b == 0));
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void b(int i8, float f5, int i9) {
            TabLayout tabLayout = this.f18532a.get();
            if (tabLayout != null) {
                int i10 = this.f18534c;
                boolean z4 = false;
                boolean z8 = i10 != 2 || this.f18533b == 1;
                if (i10 != 2 || this.f18533b != 0) {
                    z4 = true;
                }
                tabLayout.K(i8, f5, z8, z4);
            }
        }

        void c() {
            this.f18534c = 0;
            this.f18533b = 0;
        }

        @Override // androidx.viewpager.widget.ViewPager.i
        public void d(int i8) {
            this.f18533b = this.f18534c;
            this.f18534c = i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class i extends LinearLayout {

        /* renamed from: a  reason: collision with root package name */
        private g f18535a;

        /* renamed from: b  reason: collision with root package name */
        private TextView f18536b;

        /* renamed from: c  reason: collision with root package name */
        private ImageView f18537c;

        /* renamed from: d  reason: collision with root package name */
        private View f18538d;

        /* renamed from: e  reason: collision with root package name */
        private BadgeDrawable f18539e;

        /* renamed from: f  reason: collision with root package name */
        private View f18540f;

        /* renamed from: g  reason: collision with root package name */
        private TextView f18541g;

        /* renamed from: h  reason: collision with root package name */
        private ImageView f18542h;

        /* renamed from: j  reason: collision with root package name */
        private Drawable f18543j;

        /* renamed from: k  reason: collision with root package name */
        private int f18544k;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a implements View.OnLayoutChangeListener {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ View f18546a;

            a(View view) {
                this.f18546a = view;
            }

            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15) {
                if (this.f18546a.getVisibility() == 0) {
                    i.this.s(this.f18546a);
                }
            }
        }

        public i(Context context) {
            super(context);
            this.f18544k = 2;
            u(context);
            c0.J0(this, TabLayout.this.f18488d, TabLayout.this.f18490e, TabLayout.this.f18492f, TabLayout.this.f18494g);
            setGravity(17);
            setOrientation(!TabLayout.this.H ? 1 : 0);
            setClickable(true);
            c0.K0(this, z.b(getContext(), 1002));
        }

        private void f(View view) {
            if (view == null) {
                return;
            }
            view.addOnLayoutChangeListener(new a(view));
        }

        private float g(Layout layout, int i8, float f5) {
            return layout.getLineWidth(i8) * (f5 / layout.getPaint().getTextSize());
        }

        private BadgeDrawable getBadge() {
            return this.f18539e;
        }

        private BadgeDrawable getOrCreateBadge() {
            if (this.f18539e == null) {
                this.f18539e = BadgeDrawable.c(getContext());
            }
            r();
            BadgeDrawable badgeDrawable = this.f18539e;
            if (badgeDrawable != null) {
                return badgeDrawable;
            }
            throw new IllegalStateException("Unable to create badge");
        }

        private void h(boolean z4) {
            setClipChildren(z4);
            setClipToPadding(z4);
            ViewGroup viewGroup = (ViewGroup) getParent();
            if (viewGroup != null) {
                viewGroup.setClipChildren(z4);
                viewGroup.setClipToPadding(z4);
            }
        }

        private FrameLayout i() {
            FrameLayout frameLayout = new FrameLayout(getContext());
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
            return frameLayout;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void j(Canvas canvas) {
            Drawable drawable = this.f18543j;
            if (drawable != null) {
                drawable.setBounds(getLeft(), getTop(), getRight(), getBottom());
                this.f18543j.draw(canvas);
            }
        }

        private FrameLayout k(View view) {
            if ((view == this.f18537c || view == this.f18536b) && com.google.android.material.badge.a.f17456a) {
                return (FrameLayout) view.getParent();
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean l() {
            return this.f18539e != null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void m() {
            FrameLayout frameLayout;
            if (com.google.android.material.badge.a.f17456a) {
                frameLayout = i();
                addView(frameLayout, 0);
            } else {
                frameLayout = this;
            }
            ImageView imageView = (ImageView) LayoutInflater.from(getContext()).inflate(k7.h.f21183e, (ViewGroup) frameLayout, false);
            this.f18537c = imageView;
            frameLayout.addView(imageView, 0);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void n() {
            FrameLayout frameLayout;
            if (com.google.android.material.badge.a.f17456a) {
                frameLayout = i();
                addView(frameLayout);
            } else {
                frameLayout = this;
            }
            TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(k7.h.f21184f, (ViewGroup) frameLayout, false);
            this.f18536b = textView;
            frameLayout.addView(textView);
        }

        private void p(View view) {
            if (l() && view != null) {
                h(false);
                com.google.android.material.badge.a.a(this.f18539e, view, k(view));
                this.f18538d = view;
            }
        }

        private void q() {
            if (l()) {
                h(true);
                View view = this.f18538d;
                if (view != null) {
                    com.google.android.material.badge.a.d(this.f18539e, view);
                    this.f18538d = null;
                }
            }
        }

        private void r() {
            g gVar;
            View view;
            View view2;
            g gVar2;
            if (l()) {
                if (this.f18540f == null) {
                    if (this.f18537c != null && (gVar2 = this.f18535a) != null && gVar2.f() != null) {
                        View view3 = this.f18538d;
                        view = this.f18537c;
                        if (view3 != view) {
                            q();
                            view2 = this.f18537c;
                            p(view2);
                            return;
                        }
                        s(view);
                        return;
                    } else if (this.f18536b != null && (gVar = this.f18535a) != null && gVar.h() == 1) {
                        View view4 = this.f18538d;
                        view = this.f18536b;
                        if (view4 != view) {
                            q();
                            view2 = this.f18536b;
                            p(view2);
                            return;
                        }
                        s(view);
                        return;
                    }
                }
                q();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void s(View view) {
            if (l() && view == this.f18538d) {
                com.google.android.material.badge.a.e(this.f18539e, view, k(view));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v3, types: [android.graphics.drawable.RippleDrawable] */
        /* JADX WARN: Type inference failed for: r2v3, types: [android.graphics.drawable.LayerDrawable] */
        public void u(Context context) {
            int i8 = TabLayout.this.f18504w;
            if (i8 != 0) {
                Drawable b9 = h.a.b(context, i8);
                this.f18543j = b9;
                if (b9 != null && b9.isStateful()) {
                    this.f18543j.setState(getDrawableState());
                }
            } else {
                this.f18543j = null;
            }
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(0);
            if (TabLayout.this.f18499l != null) {
                GradientDrawable gradientDrawable2 = new GradientDrawable();
                gradientDrawable2.setCornerRadius(1.0E-5f);
                gradientDrawable2.setColor(-1);
                ColorStateList a9 = v7.b.a(TabLayout.this.f18499l);
                if (Build.VERSION.SDK_INT >= 21) {
                    boolean z4 = TabLayout.this.O;
                    if (z4) {
                        gradientDrawable = null;
                    }
                    gradientDrawable = new RippleDrawable(a9, gradientDrawable, z4 ? null : gradientDrawable2);
                } else {
                    Drawable r4 = androidx.core.graphics.drawable.a.r(gradientDrawable2);
                    androidx.core.graphics.drawable.a.o(r4, a9);
                    gradientDrawable = new LayerDrawable(new Drawable[]{gradientDrawable, r4});
                }
            }
            c0.x0(this, gradientDrawable);
            TabLayout.this.invalidate();
        }

        private void w(TextView textView, ImageView imageView) {
            g gVar = this.f18535a;
            Drawable mutate = (gVar == null || gVar.f() == null) ? null : androidx.core.graphics.drawable.a.r(this.f18535a.f()).mutate();
            g gVar2 = this.f18535a;
            CharSequence i8 = gVar2 != null ? gVar2.i() : null;
            if (imageView != null) {
                if (mutate != null) {
                    imageView.setImageDrawable(mutate);
                    imageView.setVisibility(0);
                    setVisibility(0);
                } else {
                    imageView.setVisibility(8);
                    imageView.setImageDrawable(null);
                }
            }
            boolean z4 = !TextUtils.isEmpty(i8);
            if (textView != null) {
                if (z4) {
                    textView.setText(i8);
                    if (this.f18535a.f18528g == 1) {
                        textView.setVisibility(0);
                    } else {
                        textView.setVisibility(8);
                    }
                    setVisibility(0);
                } else {
                    textView.setVisibility(8);
                    textView.setText((CharSequence) null);
                }
            }
            if (imageView != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                int c9 = (z4 && imageView.getVisibility() == 0) ? (int) s.c(getContext(), 8) : 0;
                if (TabLayout.this.H) {
                    if (c9 != androidx.core.view.i.a(marginLayoutParams)) {
                        androidx.core.view.i.c(marginLayoutParams, c9);
                        marginLayoutParams.bottomMargin = 0;
                        imageView.setLayoutParams(marginLayoutParams);
                        imageView.requestLayout();
                    }
                } else if (c9 != marginLayoutParams.bottomMargin) {
                    marginLayoutParams.bottomMargin = c9;
                    androidx.core.view.i.c(marginLayoutParams, 0);
                    imageView.setLayoutParams(marginLayoutParams);
                    imageView.requestLayout();
                }
            }
            g gVar3 = this.f18535a;
            CharSequence charSequence = gVar3 != null ? gVar3.f18525d : null;
            int i9 = Build.VERSION.SDK_INT;
            if (i9 < 21 || i9 > 23) {
                if (!z4) {
                    i8 = charSequence;
                }
                o0.a(this, i8);
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void drawableStateChanged() {
            super.drawableStateChanged();
            int[] drawableState = getDrawableState();
            Drawable drawable = this.f18543j;
            boolean z4 = false;
            if (drawable != null && drawable.isStateful()) {
                z4 = false | this.f18543j.setState(drawableState);
            }
            if (z4) {
                invalidate();
                TabLayout.this.invalidate();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getContentHeight() {
            View[] viewArr = {this.f18536b, this.f18537c, this.f18540f};
            int i8 = 0;
            int i9 = 0;
            boolean z4 = false;
            for (int i10 = 0; i10 < 3; i10++) {
                View view = viewArr[i10];
                if (view != null && view.getVisibility() == 0) {
                    i9 = z4 ? Math.min(i9, view.getTop()) : view.getTop();
                    i8 = z4 ? Math.max(i8, view.getBottom()) : view.getBottom();
                    z4 = true;
                }
            }
            return i8 - i9;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getContentWidth() {
            View[] viewArr = {this.f18536b, this.f18537c, this.f18540f};
            int i8 = 0;
            int i9 = 0;
            boolean z4 = false;
            for (int i10 = 0; i10 < 3; i10++) {
                View view = viewArr[i10];
                if (view != null && view.getVisibility() == 0) {
                    i9 = z4 ? Math.min(i9, view.getLeft()) : view.getLeft();
                    i8 = z4 ? Math.max(i8, view.getRight()) : view.getRight();
                    z4 = true;
                }
            }
            return i8 - i9;
        }

        public g getTab() {
            return this.f18535a;
        }

        void o() {
            setTab(null);
            setSelected(false);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            BadgeDrawable badgeDrawable = this.f18539e;
            if (badgeDrawable != null && badgeDrawable.isVisible()) {
                CharSequence contentDescription = getContentDescription();
                accessibilityNodeInfo.setContentDescription(((Object) contentDescription) + ", " + ((Object) this.f18539e.h()));
            }
            androidx.core.view.accessibility.c I0 = androidx.core.view.accessibility.c.I0(accessibilityNodeInfo);
            I0.f0(c.C0043c.a(0, 1, this.f18535a.g(), 1, false, isSelected()));
            if (isSelected()) {
                I0.d0(false);
                I0.T(c.a.f4911i);
            }
            I0.w0(getResources().getString(k7.j.f21212h));
        }

        @Override // android.widget.LinearLayout, android.view.View
        public void onMeasure(int i8, int i9) {
            Layout layout;
            int size = View.MeasureSpec.getSize(i8);
            int mode = View.MeasureSpec.getMode(i8);
            int tabMaxWidth = TabLayout.this.getTabMaxWidth();
            if (tabMaxWidth > 0 && (mode == 0 || size > tabMaxWidth)) {
                i8 = View.MeasureSpec.makeMeasureSpec(TabLayout.this.f18505x, Integer.MIN_VALUE);
            }
            super.onMeasure(i8, i9);
            if (this.f18536b != null) {
                float f5 = TabLayout.this.q;
                int i10 = this.f18544k;
                ImageView imageView = this.f18537c;
                boolean z4 = true;
                if (imageView == null || imageView.getVisibility() != 0) {
                    TextView textView = this.f18536b;
                    if (textView != null && textView.getLineCount() > 1) {
                        f5 = TabLayout.this.f18503t;
                    }
                } else {
                    i10 = 1;
                }
                float textSize = this.f18536b.getTextSize();
                int lineCount = this.f18536b.getLineCount();
                int d8 = androidx.core.widget.k.d(this.f18536b);
                int i11 = (f5 > textSize ? 1 : (f5 == textSize ? 0 : -1));
                if (i11 != 0 || (d8 >= 0 && i10 != d8)) {
                    if (TabLayout.this.G == 1 && i11 > 0 && lineCount == 1 && ((layout = this.f18536b.getLayout()) == null || g(layout, 0, f5) > (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight())) {
                        z4 = false;
                    }
                    if (z4) {
                        this.f18536b.setTextSize(0, f5);
                        this.f18536b.setMaxLines(i10);
                        super.onMeasure(i8, i9);
                    }
                }
            }
        }

        @Override // android.view.View
        public boolean performClick() {
            boolean performClick = super.performClick();
            if (this.f18535a != null) {
                if (!performClick) {
                    playSoundEffect(0);
                }
                this.f18535a.l();
                return true;
            }
            return performClick;
        }

        @Override // android.view.View
        public void setSelected(boolean z4) {
            boolean z8 = isSelected() != z4;
            super.setSelected(z4);
            if (z8 && z4 && Build.VERSION.SDK_INT < 16) {
                sendAccessibilityEvent(4);
            }
            TextView textView = this.f18536b;
            if (textView != null) {
                textView.setSelected(z4);
            }
            ImageView imageView = this.f18537c;
            if (imageView != null) {
                imageView.setSelected(z4);
            }
            View view = this.f18540f;
            if (view != null) {
                view.setSelected(z4);
            }
        }

        void setTab(g gVar) {
            if (gVar != this.f18535a) {
                this.f18535a = gVar;
                t();
            }
        }

        final void t() {
            g gVar = this.f18535a;
            Drawable drawable = null;
            View e8 = gVar != null ? gVar.e() : null;
            if (e8 != null) {
                ViewParent parent = e8.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(e8);
                    }
                    addView(e8);
                }
                this.f18540f = e8;
                TextView textView = this.f18536b;
                if (textView != null) {
                    textView.setVisibility(8);
                }
                ImageView imageView = this.f18537c;
                if (imageView != null) {
                    imageView.setVisibility(8);
                    this.f18537c.setImageDrawable(null);
                }
                TextView textView2 = (TextView) e8.findViewById(16908308);
                this.f18541g = textView2;
                if (textView2 != null) {
                    this.f18544k = androidx.core.widget.k.d(textView2);
                }
                this.f18542h = (ImageView) e8.findViewById(16908294);
            } else {
                View view = this.f18540f;
                if (view != null) {
                    removeView(view);
                    this.f18540f = null;
                }
                this.f18541g = null;
                this.f18542h = null;
            }
            if (this.f18540f == null) {
                if (this.f18537c == null) {
                    m();
                }
                if (gVar != null && gVar.f() != null) {
                    drawable = androidx.core.graphics.drawable.a.r(gVar.f()).mutate();
                }
                if (drawable != null) {
                    androidx.core.graphics.drawable.a.o(drawable, TabLayout.this.f18498k);
                    PorterDuff.Mode mode = TabLayout.this.f18502p;
                    if (mode != null) {
                        androidx.core.graphics.drawable.a.p(drawable, mode);
                    }
                }
                if (this.f18536b == null) {
                    n();
                    this.f18544k = androidx.core.widget.k.d(this.f18536b);
                }
                androidx.core.widget.k.q(this.f18536b, TabLayout.this.f18496h);
                ColorStateList colorStateList = TabLayout.this.f18497j;
                if (colorStateList != null) {
                    this.f18536b.setTextColor(colorStateList);
                }
                w(this.f18536b, this.f18537c);
                r();
                f(this.f18537c);
                f(this.f18536b);
            } else {
                TextView textView3 = this.f18541g;
                if (textView3 != null || this.f18542h != null) {
                    w(textView3, this.f18542h);
                }
            }
            if (gVar != null && !TextUtils.isEmpty(gVar.f18525d)) {
                setContentDescription(gVar.f18525d);
            }
            setSelected(gVar != null && gVar.j());
        }

        final void v() {
            ImageView imageView;
            setOrientation(!TabLayout.this.H ? 1 : 0);
            TextView textView = this.f18541g;
            if (textView == null && this.f18542h == null) {
                textView = this.f18536b;
                imageView = this.f18537c;
            } else {
                imageView = this.f18542h;
            }
            w(textView, imageView);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class j implements d {

        /* renamed from: a  reason: collision with root package name */
        private final ViewPager f18548a;

        public j(ViewPager viewPager) {
            this.f18548a = viewPager;
        }

        @Override // com.google.android.material.tabs.TabLayout.c
        public void onTabReselected(g gVar) {
        }

        @Override // com.google.android.material.tabs.TabLayout.c
        public void onTabSelected(g gVar) {
            this.f18548a.setCurrentItem(gVar.g());
        }

        @Override // com.google.android.material.tabs.TabLayout.c
        public void onTabUnselected(g gVar) {
        }
    }

    public TabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.X);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public TabLayout(android.content.Context r12, android.util.AttributeSet r13, int r14) {
        /*
            Method dump skipped, instructions count: 444
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.tabs.TabLayout.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private void F(int i8) {
        i iVar = (i) this.f18486c.getChildAt(i8);
        this.f18486c.removeViewAt(i8);
        if (iVar != null) {
            iVar.o();
            this.f18495g0.a(iVar);
        }
        requestLayout();
    }

    private void M(ViewPager viewPager, boolean z4, boolean z8) {
        ViewPager viewPager2 = this.f18483a0;
        if (viewPager2 != null) {
            h hVar = this.f18489d0;
            if (hVar != null) {
                viewPager2.J(hVar);
            }
            b bVar = this.f18491e0;
            if (bVar != null) {
                this.f18483a0.I(bVar);
            }
        }
        c cVar = this.T;
        if (cVar != null) {
            D(cVar);
            this.T = null;
        }
        if (viewPager != null) {
            this.f18483a0 = viewPager;
            if (this.f18489d0 == null) {
                this.f18489d0 = new h(this);
            }
            this.f18489d0.c();
            viewPager.c(this.f18489d0);
            j jVar = new j(viewPager);
            this.T = jVar;
            c(jVar);
            androidx.viewpager.widget.a adapter = viewPager.getAdapter();
            if (adapter != null) {
                I(adapter, z4);
            }
            if (this.f18491e0 == null) {
                this.f18491e0 = new b();
            }
            this.f18491e0.a(z4);
            viewPager.b(this.f18491e0);
            J(viewPager.getCurrentItem(), 0.0f, true);
        } else {
            this.f18483a0 = null;
            I(null, false);
        }
        this.f18493f0 = z8;
    }

    private void N() {
        int size = this.f18482a.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.f18482a.get(i8).t();
        }
    }

    private void O(LinearLayout.LayoutParams layoutParams) {
        float f5;
        if (this.G == 1 && this.C == 0) {
            layoutParams.width = 0;
            f5 = 1.0f;
        } else {
            layoutParams.width = -2;
            f5 = 0.0f;
        }
        layoutParams.weight = f5;
    }

    private int getDefaultHeight() {
        int size = this.f18482a.size();
        boolean z4 = false;
        int i8 = 0;
        while (true) {
            if (i8 < size) {
                g gVar = this.f18482a.get(i8);
                if (gVar != null && gVar.f() != null && !TextUtils.isEmpty(gVar.i())) {
                    z4 = true;
                    break;
                }
                i8++;
            } else {
                break;
            }
        }
        return (!z4 || this.H) ? 48 : 72;
    }

    private int getTabMinWidth() {
        int i8 = this.f18506y;
        if (i8 != -1) {
            return i8;
        }
        int i9 = this.G;
        if (i9 == 0 || i9 == 2) {
            return this.A;
        }
        return 0;
    }

    private int getTabScrollRange() {
        return Math.max(0, ((this.f18486c.getWidth() - getWidth()) - getPaddingLeft()) - getPaddingRight());
    }

    private void h(TabItem tabItem) {
        g z4 = z();
        CharSequence charSequence = tabItem.f18477a;
        if (charSequence != null) {
            z4.s(charSequence);
        }
        Drawable drawable = tabItem.f18478b;
        if (drawable != null) {
            z4.p(drawable);
        }
        int i8 = tabItem.f18479c;
        if (i8 != 0) {
            z4.n(i8);
        }
        if (!TextUtils.isEmpty(tabItem.getContentDescription())) {
            z4.m(tabItem.getContentDescription());
        }
        e(z4);
    }

    private void i(g gVar) {
        i iVar = gVar.f18530i;
        iVar.setSelected(false);
        iVar.setActivated(false);
        this.f18486c.addView(iVar, gVar.g(), q());
    }

    private void j(View view) {
        if (!(view instanceof TabItem)) {
            throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
        }
        h((TabItem) view);
    }

    private void k(int i8) {
        if (i8 == -1) {
            return;
        }
        if (getWindowToken() == null || !c0.W(this) || this.f18486c.c()) {
            J(i8, 0.0f, true);
            return;
        }
        int scrollX = getScrollX();
        int n8 = n(i8, 0.0f);
        if (scrollX != n8) {
            w();
            this.W.setIntValues(scrollX, n8);
            this.W.start();
        }
        this.f18486c.b(i8, this.E);
    }

    private void l(int i8) {
        f fVar;
        int i9;
        if (i8 != 0) {
            i9 = 1;
            if (i8 == 1) {
                fVar = this.f18486c;
                fVar.setGravity(i9);
            } else if (i8 != 2) {
                return;
            }
        } else {
            Log.w("TabLayout", "MODE_SCROLLABLE + GRAVITY_FILL is not supported, GRAVITY_START will be used instead");
        }
        fVar = this.f18486c;
        i9 = 8388611;
        fVar.setGravity(i9);
    }

    private void m() {
        int i8 = this.G;
        c0.J0(this.f18486c, (i8 == 0 || i8 == 2) ? Math.max(0, this.B - this.f18488d) : 0, 0, 0, 0);
        int i9 = this.G;
        if (i9 == 0) {
            l(this.C);
        } else if (i9 == 1 || i9 == 2) {
            if (this.C == 2) {
                Log.w("TabLayout", "GRAVITY_START is not supported with the current tab mode, GRAVITY_CENTER will be used instead");
            }
            this.f18486c.setGravity(1);
        }
        P(true);
    }

    private int n(int i8, float f5) {
        int i9 = this.G;
        if (i9 == 0 || i9 == 2) {
            View childAt = this.f18486c.getChildAt(i8);
            int i10 = i8 + 1;
            View childAt2 = i10 < this.f18486c.getChildCount() ? this.f18486c.getChildAt(i10) : null;
            int width = childAt != null ? childAt.getWidth() : 0;
            int width2 = childAt2 != null ? childAt2.getWidth() : 0;
            int left = (childAt.getLeft() + (width / 2)) - (getWidth() / 2);
            int i11 = (int) ((width + width2) * 0.5f * f5);
            return c0.E(this) == 0 ? left + i11 : left - i11;
        }
        return 0;
    }

    private void o(g gVar, int i8) {
        gVar.q(i8);
        this.f18482a.add(i8, gVar);
        int size = this.f18482a.size();
        while (true) {
            i8++;
            if (i8 >= size) {
                return;
            }
            this.f18482a.get(i8).q(i8);
        }
    }

    private static ColorStateList p(int i8, int i9) {
        return new ColorStateList(new int[][]{HorizontalScrollView.SELECTED_STATE_SET, HorizontalScrollView.EMPTY_STATE_SET}, new int[]{i9, i8});
    }

    private LinearLayout.LayoutParams q() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
        O(layoutParams);
        return layoutParams;
    }

    private i s(g gVar) {
        androidx.core.util.e<i> eVar = this.f18495g0;
        i b9 = eVar != null ? eVar.b() : null;
        if (b9 == null) {
            b9 = new i(getContext());
        }
        b9.setTab(gVar);
        b9.setFocusable(true);
        b9.setMinimumWidth(getTabMinWidth());
        b9.setContentDescription(TextUtils.isEmpty(gVar.f18525d) ? gVar.f18524c : gVar.f18525d);
        return b9;
    }

    private void setSelectedTabView(int i8) {
        int childCount = this.f18486c.getChildCount();
        if (i8 < childCount) {
            int i9 = 0;
            while (i9 < childCount) {
                View childAt = this.f18486c.getChildAt(i9);
                boolean z4 = true;
                childAt.setSelected(i9 == i8);
                if (i9 != i8) {
                    z4 = false;
                }
                childAt.setActivated(z4);
                i9++;
            }
        }
    }

    private void t(g gVar) {
        for (int size = this.R.size() - 1; size >= 0; size--) {
            this.R.get(size).onTabReselected(gVar);
        }
    }

    private void u(g gVar) {
        for (int size = this.R.size() - 1; size >= 0; size--) {
            this.R.get(size).onTabSelected(gVar);
        }
    }

    private void v(g gVar) {
        for (int size = this.R.size() - 1; size >= 0; size--) {
            this.R.get(size).onTabUnselected(gVar);
        }
    }

    private void w() {
        if (this.W == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.W = valueAnimator;
            valueAnimator.setInterpolator(l7.a.f21787b);
            this.W.setDuration(this.E);
            this.W.addUpdateListener(new a());
        }
    }

    void A() {
        int currentItem;
        C();
        androidx.viewpager.widget.a aVar = this.f18485b0;
        if (aVar != null) {
            int e8 = aVar.e();
            for (int i8 = 0; i8 < e8; i8++) {
                g(z().s(this.f18485b0.g(i8)), false);
            }
            ViewPager viewPager = this.f18483a0;
            if (viewPager == null || e8 <= 0 || (currentItem = viewPager.getCurrentItem()) == getSelectedTabPosition() || currentItem >= getTabCount()) {
                return;
            }
            G(x(currentItem));
        }
    }

    protected boolean B(g gVar) {
        return f18481i0.a(gVar);
    }

    public void C() {
        for (int childCount = this.f18486c.getChildCount() - 1; childCount >= 0; childCount--) {
            F(childCount);
        }
        Iterator<g> it = this.f18482a.iterator();
        while (it.hasNext()) {
            g next = it.next();
            it.remove();
            next.k();
            B(next);
        }
        this.f18484b = null;
    }

    @Deprecated
    public void D(c cVar) {
        this.R.remove(cVar);
    }

    public void E(d dVar) {
        D(dVar);
    }

    public void G(g gVar) {
        H(gVar, true);
    }

    public void H(g gVar, boolean z4) {
        g gVar2 = this.f18484b;
        if (gVar2 == gVar) {
            if (gVar2 != null) {
                t(gVar);
                k(gVar.g());
                return;
            }
            return;
        }
        int g8 = gVar != null ? gVar.g() : -1;
        if (z4) {
            if ((gVar2 == null || gVar2.g() == -1) && g8 != -1) {
                J(g8, 0.0f, true);
            } else {
                k(g8);
            }
            if (g8 != -1) {
                setSelectedTabView(g8);
            }
        }
        this.f18484b = gVar;
        if (gVar2 != null) {
            v(gVar2);
        }
        if (gVar != null) {
            u(gVar);
        }
    }

    void I(androidx.viewpager.widget.a aVar, boolean z4) {
        DataSetObserver dataSetObserver;
        androidx.viewpager.widget.a aVar2 = this.f18485b0;
        if (aVar2 != null && (dataSetObserver = this.f18487c0) != null) {
            aVar2.u(dataSetObserver);
        }
        this.f18485b0 = aVar;
        if (z4 && aVar != null) {
            if (this.f18487c0 == null) {
                this.f18487c0 = new e();
            }
            aVar.m(this.f18487c0);
        }
        A();
    }

    public void J(int i8, float f5, boolean z4) {
        K(i8, f5, z4, true);
    }

    public void K(int i8, float f5, boolean z4, boolean z8) {
        int round = Math.round(i8 + f5);
        if (round < 0 || round >= this.f18486c.getChildCount()) {
            return;
        }
        if (z8) {
            this.f18486c.e(i8, f5);
        }
        ValueAnimator valueAnimator = this.W;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.W.cancel();
        }
        scrollTo(n(i8, f5), 0);
        if (z4) {
            setSelectedTabView(round);
        }
    }

    public void L(ViewPager viewPager, boolean z4) {
        M(viewPager, z4, false);
    }

    void P(boolean z4) {
        for (int i8 = 0; i8 < this.f18486c.getChildCount(); i8++) {
            View childAt = this.f18486c.getChildAt(i8);
            childAt.setMinimumWidth(getTabMinWidth());
            O((LinearLayout.LayoutParams) childAt.getLayoutParams());
            if (z4) {
                childAt.requestLayout();
            }
        }
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view) {
        j(view);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view, int i8) {
        j(view);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public void addView(View view, int i8, ViewGroup.LayoutParams layoutParams) {
        j(view);
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup, android.view.ViewManager
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        j(view);
    }

    @Deprecated
    public void c(c cVar) {
        if (this.R.contains(cVar)) {
            return;
        }
        this.R.add(cVar);
    }

    public void d(d dVar) {
        c(dVar);
    }

    public void e(g gVar) {
        g(gVar, this.f18482a.isEmpty());
    }

    public void f(g gVar, int i8, boolean z4) {
        if (gVar.f18529h != this) {
            throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
        }
        o(gVar, i8);
        i(gVar);
        if (z4) {
            gVar.l();
        }
    }

    public void g(g gVar, boolean z4) {
        f(gVar, this.f18482a.size(), z4);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return generateDefaultLayoutParams();
    }

    public int getSelectedTabPosition() {
        g gVar = this.f18484b;
        if (gVar != null) {
            return gVar.g();
        }
        return -1;
    }

    public int getTabCount() {
        return this.f18482a.size();
    }

    public int getTabGravity() {
        return this.C;
    }

    public ColorStateList getTabIconTint() {
        return this.f18498k;
    }

    public int getTabIndicatorAnimationMode() {
        return this.L;
    }

    public int getTabIndicatorGravity() {
        return this.F;
    }

    int getTabMaxWidth() {
        return this.f18505x;
    }

    public int getTabMode() {
        return this.G;
    }

    public ColorStateList getTabRippleColor() {
        return this.f18499l;
    }

    public Drawable getTabSelectedIndicator() {
        return this.f18500m;
    }

    public ColorStateList getTabTextColors() {
        return this.f18497j;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        x7.i.e(this);
        if (this.f18483a0 == null) {
            ViewParent parent = getParent();
            if (parent instanceof ViewPager) {
                M((ViewPager) parent, true, true);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.f18493f0) {
            setupWithViewPager(null);
            this.f18493f0 = false;
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        for (int i8 = 0; i8 < this.f18486c.getChildCount(); i8++) {
            View childAt = this.f18486c.getChildAt(i8);
            if (childAt instanceof i) {
                ((i) childAt).j(canvas);
            }
        }
        super.onDraw(canvas);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        androidx.core.view.accessibility.c.I0(accessibilityNodeInfo).e0(c.b.b(1, getTabCount(), false, 1));
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0073, code lost:
        if (r0 != 2) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x007e, code lost:
        if (r7.getMeasuredWidth() != getMeasuredWidth()) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0080, code lost:
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x008a, code lost:
        if (r7.getMeasuredWidth() < getMeasuredWidth()) goto L25;
     */
    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onMeasure(int r7, int r8) {
        /*
            r6 = this;
            android.content.Context r0 = r6.getContext()
            int r1 = r6.getDefaultHeight()
            float r0 = com.google.android.material.internal.s.c(r0, r1)
            int r0 = java.lang.Math.round(r0)
            int r1 = android.view.View.MeasureSpec.getMode(r8)
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = 1073741824(0x40000000, float:2.0)
            r4 = 0
            r5 = 1
            if (r1 == r2) goto L2e
            if (r1 == 0) goto L1f
            goto L41
        L1f:
            int r8 = r6.getPaddingTop()
            int r0 = r0 + r8
            int r8 = r6.getPaddingBottom()
            int r0 = r0 + r8
            int r8 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r3)
            goto L41
        L2e:
            int r1 = r6.getChildCount()
            if (r1 != r5) goto L41
            int r1 = android.view.View.MeasureSpec.getSize(r8)
            if (r1 < r0) goto L41
            android.view.View r1 = r6.getChildAt(r4)
            r1.setMinimumHeight(r0)
        L41:
            int r0 = android.view.View.MeasureSpec.getSize(r7)
            int r1 = android.view.View.MeasureSpec.getMode(r7)
            if (r1 == 0) goto L5f
            int r1 = r6.f18507z
            if (r1 <= 0) goto L50
            goto L5d
        L50:
            float r0 = (float) r0
            android.content.Context r1 = r6.getContext()
            r2 = 56
            float r1 = com.google.android.material.internal.s.c(r1, r2)
            float r0 = r0 - r1
            int r1 = (int) r0
        L5d:
            r6.f18505x = r1
        L5f:
            super.onMeasure(r7, r8)
            int r7 = r6.getChildCount()
            if (r7 != r5) goto Lad
            android.view.View r7 = r6.getChildAt(r4)
            int r0 = r6.G
            if (r0 == 0) goto L82
            if (r0 == r5) goto L76
            r1 = 2
            if (r0 == r1) goto L82
            goto L8d
        L76:
            int r0 = r7.getMeasuredWidth()
            int r1 = r6.getMeasuredWidth()
            if (r0 == r1) goto L8d
        L80:
            r4 = r5
            goto L8d
        L82:
            int r0 = r7.getMeasuredWidth()
            int r1 = r6.getMeasuredWidth()
            if (r0 >= r1) goto L8d
            goto L80
        L8d:
            if (r4 == 0) goto Lad
            int r0 = r6.getPaddingTop()
            int r1 = r6.getPaddingBottom()
            int r0 = r0 + r1
            android.view.ViewGroup$LayoutParams r1 = r7.getLayoutParams()
            int r1 = r1.height
            int r8 = android.widget.HorizontalScrollView.getChildMeasureSpec(r8, r0, r1)
            int r0 = r6.getMeasuredWidth()
            int r0 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r3)
            r7.measure(r0, r8)
        Lad:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.tabs.TabLayout.onMeasure(int, int):void");
    }

    protected g r() {
        g b9 = f18481i0.b();
        return b9 == null ? new g() : b9;
    }

    @Override // android.view.View
    public void setElevation(float f5) {
        super.setElevation(f5);
        x7.i.d(this, f5);
    }

    public void setInlineLabel(boolean z4) {
        if (this.H != z4) {
            this.H = z4;
            for (int i8 = 0; i8 < this.f18486c.getChildCount(); i8++) {
                View childAt = this.f18486c.getChildAt(i8);
                if (childAt instanceof i) {
                    ((i) childAt).v();
                }
            }
            m();
        }
    }

    public void setInlineLabelResource(int i8) {
        setInlineLabel(getResources().getBoolean(i8));
    }

    @Deprecated
    public void setOnTabSelectedListener(c cVar) {
        c cVar2 = this.Q;
        if (cVar2 != null) {
            D(cVar2);
        }
        this.Q = cVar;
        if (cVar != null) {
            c(cVar);
        }
    }

    @Deprecated
    public void setOnTabSelectedListener(d dVar) {
        setOnTabSelectedListener((c) dVar);
    }

    void setScrollAnimatorListener(Animator.AnimatorListener animatorListener) {
        w();
        this.W.addListener(animatorListener);
    }

    public void setSelectedTabIndicator(int i8) {
        setSelectedTabIndicator(i8 != 0 ? h.a.b(getContext(), i8) : null);
    }

    public void setSelectedTabIndicator(Drawable drawable) {
        if (this.f18500m != drawable) {
            if (drawable == null) {
                drawable = new GradientDrawable();
            }
            this.f18500m = drawable;
        }
    }

    public void setSelectedTabIndicatorColor(int i8) {
        this.f18501n = i8;
    }

    public void setSelectedTabIndicatorGravity(int i8) {
        if (this.F != i8) {
            this.F = i8;
            c0.j0(this.f18486c);
        }
    }

    @Deprecated
    public void setSelectedTabIndicatorHeight(int i8) {
        this.f18486c.f(i8);
    }

    public void setTabGravity(int i8) {
        if (this.C != i8) {
            this.C = i8;
            m();
        }
    }

    public void setTabIconTint(ColorStateList colorStateList) {
        if (this.f18498k != colorStateList) {
            this.f18498k = colorStateList;
            N();
        }
    }

    public void setTabIconTintResource(int i8) {
        setTabIconTint(h.a.a(getContext(), i8));
    }

    public void setTabIndicatorAnimationMode(int i8) {
        com.google.android.material.tabs.b bVar;
        this.L = i8;
        if (i8 == 0) {
            bVar = new com.google.android.material.tabs.b();
        } else if (i8 != 1) {
            throw new IllegalArgumentException(i8 + " is not a valid TabIndicatorAnimationMode");
        } else {
            bVar = new com.google.android.material.tabs.a();
        }
        this.P = bVar;
    }

    public void setTabIndicatorFullWidth(boolean z4) {
        this.K = z4;
        c0.j0(this.f18486c);
    }

    public void setTabMode(int i8) {
        if (i8 != this.G) {
            this.G = i8;
            m();
        }
    }

    public void setTabRippleColor(ColorStateList colorStateList) {
        if (this.f18499l != colorStateList) {
            this.f18499l = colorStateList;
            for (int i8 = 0; i8 < this.f18486c.getChildCount(); i8++) {
                View childAt = this.f18486c.getChildAt(i8);
                if (childAt instanceof i) {
                    ((i) childAt).u(getContext());
                }
            }
        }
    }

    public void setTabRippleColorResource(int i8) {
        setTabRippleColor(h.a.a(getContext(), i8));
    }

    public void setTabTextColors(ColorStateList colorStateList) {
        if (this.f18497j != colorStateList) {
            this.f18497j = colorStateList;
            N();
        }
    }

    @Deprecated
    public void setTabsFromPagerAdapter(androidx.viewpager.widget.a aVar) {
        I(aVar, false);
    }

    public void setUnboundedRipple(boolean z4) {
        if (this.O != z4) {
            this.O = z4;
            for (int i8 = 0; i8 < this.f18486c.getChildCount(); i8++) {
                View childAt = this.f18486c.getChildAt(i8);
                if (childAt instanceof i) {
                    ((i) childAt).u(getContext());
                }
            }
        }
    }

    public void setUnboundedRippleResource(int i8) {
        setUnboundedRipple(getResources().getBoolean(i8));
    }

    public void setupWithViewPager(ViewPager viewPager) {
        L(viewPager, true);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return getTabScrollRange() > 0;
    }

    public g x(int i8) {
        if (i8 < 0 || i8 >= getTabCount()) {
            return null;
        }
        return this.f18482a.get(i8);
    }

    public boolean y() {
        return this.K;
    }

    public g z() {
        g r4 = r();
        r4.f18529h = this;
        r4.f18530i = s(r4);
        if (r4.f18531j != -1) {
            r4.f18530i.setId(r4.f18531j);
        }
        return r4;
    }
}
