package com.google.android.material.appbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.core.view.v;
import com.google.android.material.appbar.AppBarLayout;
import k7.d;
import k7.f;
import k7.k;
import k7.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class CollapsingToolbarLayout extends FrameLayout {
    private static final int O = k.f21240k;
    private int A;
    private AppBarLayout.d B;
    int C;
    private int E;
    m0 F;
    private int G;
    private boolean H;
    private int K;
    private boolean L;

    /* renamed from: a  reason: collision with root package name */
    private boolean f17369a;

    /* renamed from: b  reason: collision with root package name */
    private int f17370b;

    /* renamed from: c  reason: collision with root package name */
    private ViewGroup f17371c;

    /* renamed from: d  reason: collision with root package name */
    private View f17372d;

    /* renamed from: e  reason: collision with root package name */
    private View f17373e;

    /* renamed from: f  reason: collision with root package name */
    private int f17374f;

    /* renamed from: g  reason: collision with root package name */
    private int f17375g;

    /* renamed from: h  reason: collision with root package name */
    private int f17376h;

    /* renamed from: j  reason: collision with root package name */
    private int f17377j;

    /* renamed from: k  reason: collision with root package name */
    private final Rect f17378k;

    /* renamed from: l  reason: collision with root package name */
    final com.google.android.material.internal.a f17379l;

    /* renamed from: m  reason: collision with root package name */
    final q7.a f17380m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f17381n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f17382p;
    private Drawable q;

    /* renamed from: t  reason: collision with root package name */
    Drawable f17383t;

    /* renamed from: w  reason: collision with root package name */
    private int f17384w;

    /* renamed from: x  reason: collision with root package name */
    private boolean f17385x;

    /* renamed from: y  reason: collision with root package name */
    private ValueAnimator f17386y;

    /* renamed from: z  reason: collision with root package name */
    private long f17387z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends FrameLayout.LayoutParams {

        /* renamed from: a  reason: collision with root package name */
        int f17388a;

        /* renamed from: b  reason: collision with root package name */
        float f17389b;

        public LayoutParams(int i8, int i9) {
            super(i8, i9);
            this.f17388a = 0;
            this.f17389b = 0.5f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f17388a = 0;
            this.f17389b = 0.5f;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, l.f21314g2);
            this.f17388a = obtainStyledAttributes.getInt(l.f21323h2, 0);
            a(obtainStyledAttributes.getFloat(l.f21332i2, 0.5f));
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f17388a = 0;
            this.f17389b = 0.5f;
        }

        public void a(float f5) {
            this.f17389b = f5;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements v {
        a() {
        }

        @Override // androidx.core.view.v
        public m0 a(View view, m0 m0Var) {
            return CollapsingToolbarLayout.this.n(m0Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {
        b() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            CollapsingToolbarLayout.this.setScrimAlpha(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class c implements AppBarLayout.d {
        c() {
        }

        @Override // com.google.android.material.appbar.AppBarLayout.c
        public void a(AppBarLayout appBarLayout, int i8) {
            int height;
            int c9;
            CollapsingToolbarLayout collapsingToolbarLayout = CollapsingToolbarLayout.this;
            collapsingToolbarLayout.C = i8;
            m0 m0Var = collapsingToolbarLayout.F;
            int m8 = m0Var != null ? m0Var.m() : 0;
            int childCount = CollapsingToolbarLayout.this.getChildCount();
            for (int i9 = 0; i9 < childCount; i9++) {
                View childAt = CollapsingToolbarLayout.this.getChildAt(i9);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                com.google.android.material.appbar.a j8 = CollapsingToolbarLayout.j(childAt);
                int i10 = layoutParams.f17388a;
                if (i10 == 1) {
                    c9 = t0.a.c(-i8, 0, CollapsingToolbarLayout.this.h(childAt));
                } else if (i10 == 2) {
                    c9 = Math.round((-i8) * layoutParams.f17389b);
                }
                j8.f(c9);
            }
            CollapsingToolbarLayout.this.u();
            CollapsingToolbarLayout collapsingToolbarLayout2 = CollapsingToolbarLayout.this;
            if (collapsingToolbarLayout2.f17383t != null && m8 > 0) {
                c0.j0(collapsingToolbarLayout2);
            }
            int height2 = (CollapsingToolbarLayout.this.getHeight() - c0.F(CollapsingToolbarLayout.this)) - m8;
            float f5 = height2;
            CollapsingToolbarLayout.this.f17379l.r0(Math.min(1.0f, (height - CollapsingToolbarLayout.this.getScrimVisibleHeightTrigger()) / f5));
            CollapsingToolbarLayout collapsingToolbarLayout3 = CollapsingToolbarLayout.this;
            collapsingToolbarLayout3.f17379l.f0(collapsingToolbarLayout3.C + height2);
            CollapsingToolbarLayout.this.f17379l.p0(Math.abs(i8) / f5);
        }
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21060l);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public CollapsingToolbarLayout(android.content.Context r10, android.util.AttributeSet r11, int r12) {
        /*
            Method dump skipped, instructions count: 314
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.appbar.CollapsingToolbarLayout.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private void a(int i8) {
        c();
        ValueAnimator valueAnimator = this.f17386y;
        if (valueAnimator == null) {
            ValueAnimator valueAnimator2 = new ValueAnimator();
            this.f17386y = valueAnimator2;
            valueAnimator2.setDuration(this.f17387z);
            this.f17386y.setInterpolator(i8 > this.f17384w ? l7.a.f21788c : l7.a.f21789d);
            this.f17386y.addUpdateListener(new b());
        } else if (valueAnimator.isRunning()) {
            this.f17386y.cancel();
        }
        this.f17386y.setIntValues(this.f17384w, i8);
        this.f17386y.start();
    }

    private void b(AppBarLayout appBarLayout) {
        if (k()) {
            appBarLayout.setLiftOnScroll(false);
        }
    }

    private void c() {
        if (this.f17369a) {
            ViewGroup viewGroup = null;
            this.f17371c = null;
            this.f17372d = null;
            int i8 = this.f17370b;
            if (i8 != -1) {
                ViewGroup viewGroup2 = (ViewGroup) findViewById(i8);
                this.f17371c = viewGroup2;
                if (viewGroup2 != null) {
                    this.f17372d = d(viewGroup2);
                }
            }
            if (this.f17371c == null) {
                int childCount = getChildCount();
                int i9 = 0;
                while (true) {
                    if (i9 >= childCount) {
                        break;
                    }
                    View childAt = getChildAt(i9);
                    if (l(childAt)) {
                        viewGroup = (ViewGroup) childAt;
                        break;
                    }
                    i9++;
                }
                this.f17371c = viewGroup;
            }
            t();
            this.f17369a = false;
        }
    }

    private View d(View view) {
        for (ViewParent parent = view.getParent(); parent != this && parent != null; parent = parent.getParent()) {
            if (parent instanceof View) {
                view = (View) parent;
            }
        }
        return view;
    }

    private static int g(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            return view.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
        }
        return view.getMeasuredHeight();
    }

    private static CharSequence i(View view) {
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getTitle();
        }
        if (Build.VERSION.SDK_INT < 21 || !(view instanceof android.widget.Toolbar)) {
            return null;
        }
        return ((android.widget.Toolbar) view).getTitle();
    }

    static com.google.android.material.appbar.a j(View view) {
        int i8 = f.f21154c0;
        com.google.android.material.appbar.a aVar = (com.google.android.material.appbar.a) view.getTag(i8);
        if (aVar == null) {
            com.google.android.material.appbar.a aVar2 = new com.google.android.material.appbar.a(view);
            view.setTag(i8, aVar2);
            return aVar2;
        }
        return aVar;
    }

    private boolean k() {
        return this.E == 1;
    }

    private static boolean l(View view) {
        return (view instanceof Toolbar) || (Build.VERSION.SDK_INT >= 21 && (view instanceof android.widget.Toolbar));
    }

    private boolean m(View view) {
        View view2 = this.f17372d;
        if (view2 == null || view2 == this) {
            if (view == this.f17371c) {
                return true;
            }
        } else if (view == view2) {
            return true;
        }
        return false;
    }

    private void p(boolean z4) {
        int i8;
        int i9;
        int i10;
        View view = this.f17372d;
        if (view == null) {
            view = this.f17371c;
        }
        int h8 = h(view);
        com.google.android.material.internal.c.a(this, this.f17373e, this.f17378k);
        ViewGroup viewGroup = this.f17371c;
        int i11 = 0;
        if (viewGroup instanceof Toolbar) {
            Toolbar toolbar = (Toolbar) viewGroup;
            i11 = toolbar.getTitleMarginStart();
            i9 = toolbar.getTitleMarginEnd();
            i10 = toolbar.getTitleMarginTop();
            i8 = toolbar.getTitleMarginBottom();
        } else if (Build.VERSION.SDK_INT < 24 || !(viewGroup instanceof android.widget.Toolbar)) {
            i8 = 0;
            i9 = 0;
            i10 = 0;
        } else {
            android.widget.Toolbar toolbar2 = (android.widget.Toolbar) viewGroup;
            i11 = toolbar2.getTitleMarginStart();
            i9 = toolbar2.getTitleMarginEnd();
            i10 = toolbar2.getTitleMarginTop();
            i8 = toolbar2.getTitleMarginBottom();
        }
        com.google.android.material.internal.a aVar = this.f17379l;
        Rect rect = this.f17378k;
        int i12 = rect.left + (z4 ? i9 : i11);
        int i13 = rect.top + h8 + i10;
        int i14 = rect.right;
        if (!z4) {
            i11 = i9;
        }
        aVar.X(i12, i13, i14 - i11, (rect.bottom + h8) - i8);
    }

    private void q() {
        setContentDescription(getTitle());
    }

    private void r(Drawable drawable, int i8, int i9) {
        s(drawable, this.f17371c, i8, i9);
    }

    private void s(Drawable drawable, View view, int i8, int i9) {
        if (k() && view != null && this.f17381n) {
            i9 = view.getBottom();
        }
        drawable.setBounds(0, 0, i8, i9);
    }

    private void t() {
        View view;
        if (!this.f17381n && (view = this.f17373e) != null) {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.f17373e);
            }
        }
        if (!this.f17381n || this.f17371c == null) {
            return;
        }
        if (this.f17373e == null) {
            this.f17373e = new View(getContext());
        }
        if (this.f17373e.getParent() == null) {
            this.f17371c.addView(this.f17373e, -1, -1);
        }
    }

    private void v(int i8, int i9, int i10, int i11, boolean z4) {
        View view;
        if (!this.f17381n || (view = this.f17373e) == null) {
            return;
        }
        boolean z8 = c0.V(view) && this.f17373e.getVisibility() == 0;
        this.f17382p = z8;
        if (z8 || z4) {
            boolean z9 = c0.E(this) == 1;
            p(z9);
            this.f17379l.g0(z9 ? this.f17376h : this.f17374f, this.f17378k.top + this.f17375g, (i10 - i8) - (z9 ? this.f17374f : this.f17376h), (i11 - i9) - this.f17377j);
            this.f17379l.V(z4);
        }
    }

    private void w() {
        if (this.f17371c != null && this.f17381n && TextUtils.isEmpty(this.f17379l.K())) {
            setTitle(i(this.f17371c));
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        Drawable drawable;
        super.draw(canvas);
        c();
        if (this.f17371c == null && (drawable = this.q) != null && this.f17384w > 0) {
            drawable.mutate().setAlpha(this.f17384w);
            this.q.draw(canvas);
        }
        if (this.f17381n && this.f17382p) {
            if (this.f17371c == null || this.q == null || this.f17384w <= 0 || !k() || this.f17379l.D() >= this.f17379l.E()) {
                this.f17379l.m(canvas);
            } else {
                int save = canvas.save();
                canvas.clipRect(this.q.getBounds(), Region.Op.DIFFERENCE);
                this.f17379l.m(canvas);
                canvas.restoreToCount(save);
            }
        }
        if (this.f17383t == null || this.f17384w <= 0) {
            return;
        }
        m0 m0Var = this.F;
        int m8 = m0Var != null ? m0Var.m() : 0;
        if (m8 > 0) {
            this.f17383t.setBounds(0, -this.C, getWidth(), m8 - this.C);
            this.f17383t.mutate().setAlpha(this.f17384w);
            this.f17383t.draw(canvas);
        }
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j8) {
        boolean z4;
        if (this.q == null || this.f17384w <= 0 || !m(view)) {
            z4 = false;
        } else {
            s(this.q, view, getWidth(), getHeight());
            this.q.mutate().setAlpha(this.f17384w);
            this.q.draw(canvas);
            z4 = true;
        }
        return super.drawChild(canvas, view, j8) || z4;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.f17383t;
        boolean z4 = false;
        if (drawable != null && drawable.isStateful()) {
            z4 = false | drawable.setState(drawableState);
        }
        Drawable drawable2 = this.q;
        if (drawable2 != null && drawable2.isStateful()) {
            z4 |= drawable2.setState(drawableState);
        }
        com.google.android.material.internal.a aVar = this.f17379l;
        if (aVar != null) {
            z4 |= aVar.z0(drawableState);
        }
        if (z4) {
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup
    /* renamed from: e */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup
    /* renamed from: f */
    public FrameLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public int getCollapsedTitleGravity() {
        return this.f17379l.r();
    }

    public Typeface getCollapsedTitleTypeface() {
        return this.f17379l.v();
    }

    public Drawable getContentScrim() {
        return this.q;
    }

    public int getExpandedTitleGravity() {
        return this.f17379l.A();
    }

    public int getExpandedTitleMarginBottom() {
        return this.f17377j;
    }

    public int getExpandedTitleMarginEnd() {
        return this.f17376h;
    }

    public int getExpandedTitleMarginStart() {
        return this.f17374f;
    }

    public int getExpandedTitleMarginTop() {
        return this.f17375g;
    }

    public Typeface getExpandedTitleTypeface() {
        return this.f17379l.C();
    }

    public int getHyphenationFrequency() {
        return this.f17379l.F();
    }

    public int getLineCount() {
        return this.f17379l.G();
    }

    public float getLineSpacingAdd() {
        return this.f17379l.H();
    }

    public float getLineSpacingMultiplier() {
        return this.f17379l.I();
    }

    public int getMaxLines() {
        return this.f17379l.J();
    }

    int getScrimAlpha() {
        return this.f17384w;
    }

    public long getScrimAnimationDuration() {
        return this.f17387z;
    }

    public int getScrimVisibleHeightTrigger() {
        int i8 = this.A;
        if (i8 >= 0) {
            return i8 + this.G + this.K;
        }
        m0 m0Var = this.F;
        int m8 = m0Var != null ? m0Var.m() : 0;
        int F = c0.F(this);
        return F > 0 ? Math.min((F * 2) + m8, getHeight()) : getHeight() / 3;
    }

    public Drawable getStatusBarScrim() {
        return this.f17383t;
    }

    public CharSequence getTitle() {
        if (this.f17381n) {
            return this.f17379l.K();
        }
        return null;
    }

    public int getTitleCollapseMode() {
        return this.E;
    }

    final int h(View view) {
        return ((getHeight() - j(view).b()) - view.getHeight()) - ((FrameLayout.LayoutParams) ((LayoutParams) view.getLayoutParams())).bottomMargin;
    }

    m0 n(m0 m0Var) {
        m0 m0Var2 = c0.B(this) ? m0Var : null;
        if (!androidx.core.util.c.a(this.F, m0Var2)) {
            this.F = m0Var2;
            requestLayout();
        }
        return m0Var.c();
    }

    public void o(boolean z4, boolean z8) {
        if (this.f17385x != z4) {
            if (z8) {
                a(z4 ? 255 : 0);
            } else {
                setScrimAlpha(z4 ? 255 : 0);
            }
            this.f17385x = z4;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            AppBarLayout appBarLayout = (AppBarLayout) parent;
            b(appBarLayout);
            c0.C0(this, c0.B(appBarLayout));
            if (this.B == null) {
                this.B = new c();
            }
            appBarLayout.b(this.B);
            c0.q0(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        ViewParent parent = getParent();
        AppBarLayout.d dVar = this.B;
        if (dVar != null && (parent instanceof AppBarLayout)) {
            ((AppBarLayout) parent).p(dVar);
        }
        super.onDetachedFromWindow();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        m0 m0Var = this.F;
        if (m0Var != null) {
            int m8 = m0Var.m();
            int childCount = getChildCount();
            for (int i12 = 0; i12 < childCount; i12++) {
                View childAt = getChildAt(i12);
                if (!c0.B(childAt) && childAt.getTop() < m8) {
                    c0.d0(childAt, m8);
                }
            }
        }
        int childCount2 = getChildCount();
        for (int i13 = 0; i13 < childCount2; i13++) {
            j(getChildAt(i13)).d();
        }
        v(i8, i9, i10, i11, false);
        w();
        u();
        int childCount3 = getChildCount();
        for (int i14 = 0; i14 < childCount3; i14++) {
            j(getChildAt(i14)).a();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        c();
        super.onMeasure(i8, i9);
        int mode = View.MeasureSpec.getMode(i9);
        m0 m0Var = this.F;
        int m8 = m0Var != null ? m0Var.m() : 0;
        if ((mode == 0 || this.H) && m8 > 0) {
            this.G = m8;
            super.onMeasure(i8, View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() + m8, 1073741824));
        }
        if (this.L && this.f17379l.J() > 1) {
            w();
            v(0, 0, getMeasuredWidth(), getMeasuredHeight(), true);
            int G = this.f17379l.G();
            if (G > 1) {
                this.K = Math.round(this.f17379l.z()) * (G - 1);
                super.onMeasure(i8, View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() + this.K, 1073741824));
            }
        }
        ViewGroup viewGroup = this.f17371c;
        if (viewGroup != null) {
            View view = this.f17372d;
            setMinimumHeight((view == null || view == this) ? g(viewGroup) : g(view));
        }
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        super.onSizeChanged(i8, i9, i10, i11);
        Drawable drawable = this.q;
        if (drawable != null) {
            r(drawable, i8, i9);
        }
    }

    public void setCollapsedTitleGravity(int i8) {
        this.f17379l.c0(i8);
    }

    public void setCollapsedTitleTextAppearance(int i8) {
        this.f17379l.Z(i8);
    }

    public void setCollapsedTitleTextColor(int i8) {
        setCollapsedTitleTextColor(ColorStateList.valueOf(i8));
    }

    public void setCollapsedTitleTextColor(ColorStateList colorStateList) {
        this.f17379l.b0(colorStateList);
    }

    public void setCollapsedTitleTypeface(Typeface typeface) {
        this.f17379l.d0(typeface);
    }

    public void setContentScrim(Drawable drawable) {
        Drawable drawable2 = this.q;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            Drawable mutate = drawable != null ? drawable.mutate() : null;
            this.q = mutate;
            if (mutate != null) {
                r(mutate, getWidth(), getHeight());
                this.q.setCallback(this);
                this.q.setAlpha(this.f17384w);
            }
            c0.j0(this);
        }
    }

    public void setContentScrimColor(int i8) {
        setContentScrim(new ColorDrawable(i8));
    }

    public void setContentScrimResource(int i8) {
        setContentScrim(androidx.core.content.a.f(getContext(), i8));
    }

    public void setExpandedTitleColor(int i8) {
        setExpandedTitleTextColor(ColorStateList.valueOf(i8));
    }

    public void setExpandedTitleGravity(int i8) {
        this.f17379l.l0(i8);
    }

    public void setExpandedTitleMarginBottom(int i8) {
        this.f17377j = i8;
        requestLayout();
    }

    public void setExpandedTitleMarginEnd(int i8) {
        this.f17376h = i8;
        requestLayout();
    }

    public void setExpandedTitleMarginStart(int i8) {
        this.f17374f = i8;
        requestLayout();
    }

    public void setExpandedTitleMarginTop(int i8) {
        this.f17375g = i8;
        requestLayout();
    }

    public void setExpandedTitleTextAppearance(int i8) {
        this.f17379l.i0(i8);
    }

    public void setExpandedTitleTextColor(ColorStateList colorStateList) {
        this.f17379l.k0(colorStateList);
    }

    public void setExpandedTitleTypeface(Typeface typeface) {
        this.f17379l.n0(typeface);
    }

    public void setExtraMultilineHeightEnabled(boolean z4) {
        this.L = z4;
    }

    public void setForceApplySystemWindowInsetTop(boolean z4) {
        this.H = z4;
    }

    public void setHyphenationFrequency(int i8) {
        this.f17379l.s0(i8);
    }

    public void setLineSpacingAdd(float f5) {
        this.f17379l.u0(f5);
    }

    public void setLineSpacingMultiplier(float f5) {
        this.f17379l.v0(f5);
    }

    public void setMaxLines(int i8) {
        this.f17379l.w0(i8);
    }

    public void setRtlTextDirectionHeuristicsEnabled(boolean z4) {
        this.f17379l.y0(z4);
    }

    void setScrimAlpha(int i8) {
        ViewGroup viewGroup;
        if (i8 != this.f17384w) {
            if (this.q != null && (viewGroup = this.f17371c) != null) {
                c0.j0(viewGroup);
            }
            this.f17384w = i8;
            c0.j0(this);
        }
    }

    public void setScrimAnimationDuration(long j8) {
        this.f17387z = j8;
    }

    public void setScrimVisibleHeightTrigger(int i8) {
        if (this.A != i8) {
            this.A = i8;
            u();
        }
    }

    public void setScrimsShown(boolean z4) {
        o(z4, c0.W(this) && !isInEditMode());
    }

    public void setStatusBarScrim(Drawable drawable) {
        Drawable drawable2 = this.f17383t;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            Drawable mutate = drawable != null ? drawable.mutate() : null;
            this.f17383t = mutate;
            if (mutate != null) {
                if (mutate.isStateful()) {
                    this.f17383t.setState(getDrawableState());
                }
                androidx.core.graphics.drawable.a.m(this.f17383t, c0.E(this));
                this.f17383t.setVisible(getVisibility() == 0, false);
                this.f17383t.setCallback(this);
                this.f17383t.setAlpha(this.f17384w);
            }
            c0.j0(this);
        }
    }

    public void setStatusBarScrimColor(int i8) {
        setStatusBarScrim(new ColorDrawable(i8));
    }

    public void setStatusBarScrimResource(int i8) {
        setStatusBarScrim(androidx.core.content.a.f(getContext(), i8));
    }

    public void setTitle(CharSequence charSequence) {
        this.f17379l.A0(charSequence);
        q();
    }

    public void setTitleCollapseMode(int i8) {
        this.E = i8;
        boolean k8 = k();
        this.f17379l.q0(k8);
        ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            b((AppBarLayout) parent);
        }
        if (k8 && this.q == null) {
            setContentScrimColor(this.f17380m.d(getResources().getDimension(d.f21090a)));
        }
    }

    public void setTitleEnabled(boolean z4) {
        if (z4 != this.f17381n) {
            this.f17381n = z4;
            q();
            t();
            requestLayout();
        }
    }

    @Override // android.view.View
    public void setVisibility(int i8) {
        super.setVisibility(i8);
        boolean z4 = i8 == 0;
        Drawable drawable = this.f17383t;
        if (drawable != null && drawable.isVisible() != z4) {
            this.f17383t.setVisible(z4, false);
        }
        Drawable drawable2 = this.q;
        if (drawable2 == null || drawable2.isVisible() == z4) {
            return;
        }
        this.q.setVisible(z4, false);
    }

    final void u() {
        if (this.q == null && this.f17383t == null) {
            return;
        }
        setScrimsShown(getHeight() + this.C < getScrimVisibleHeightTrigger());
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.q || drawable == this.f17383t;
    }
}
