package androidx.appcompat.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.OverScroller;
import androidx.appcompat.view.menu.m;
import androidx.core.view.m0;
import com.google.android.libraries.barhopper.RecognitionOptions;
@SuppressLint({"UnknownNullness"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ActionBarOverlayLayout extends ViewGroup implements r, androidx.core.view.t, androidx.core.view.r, androidx.core.view.s {
    static final int[] Q = {g.a.f19863b, 16842841};
    private androidx.core.view.m0 A;
    private androidx.core.view.m0 B;
    private androidx.core.view.m0 C;
    private androidx.core.view.m0 E;
    private d F;
    private OverScroller G;
    ViewPropertyAnimator H;
    final AnimatorListenerAdapter K;
    private final Runnable L;
    private final Runnable O;
    private final androidx.core.view.u P;

    /* renamed from: a  reason: collision with root package name */
    private int f1055a;

    /* renamed from: b  reason: collision with root package name */
    private int f1056b;

    /* renamed from: c  reason: collision with root package name */
    private ContentFrameLayout f1057c;

    /* renamed from: d  reason: collision with root package name */
    ActionBarContainer f1058d;

    /* renamed from: e  reason: collision with root package name */
    private s f1059e;

    /* renamed from: f  reason: collision with root package name */
    private Drawable f1060f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f1061g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f1062h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f1063j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f1064k;

    /* renamed from: l  reason: collision with root package name */
    boolean f1065l;

    /* renamed from: m  reason: collision with root package name */
    private int f1066m;

    /* renamed from: n  reason: collision with root package name */
    private int f1067n;

    /* renamed from: p  reason: collision with root package name */
    private final Rect f1068p;
    private final Rect q;

    /* renamed from: t  reason: collision with root package name */
    private final Rect f1069t;

    /* renamed from: w  reason: collision with root package name */
    private final Rect f1070w;

    /* renamed from: x  reason: collision with root package name */
    private final Rect f1071x;

    /* renamed from: y  reason: collision with root package name */
    private final Rect f1072y;

    /* renamed from: z  reason: collision with root package name */
    private final Rect f1073z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(int i8, int i9) {
            super(i8, i9);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.H = null;
            actionBarOverlayLayout.f1065l = false;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.H = null;
            actionBarOverlayLayout.f1065l = false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ActionBarOverlayLayout.this.u();
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.H = actionBarOverlayLayout.f1058d.animate().translationY(0.0f).setListener(ActionBarOverlayLayout.this.K);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ActionBarOverlayLayout.this.u();
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.H = actionBarOverlayLayout.f1058d.animate().translationY(-ActionBarOverlayLayout.this.f1058d.getHeight()).setListener(ActionBarOverlayLayout.this.K);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        void a();

        void b();

        void c(boolean z4);

        void d();

        void e();

        void f(int i8);
    }

    public ActionBarOverlayLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f1056b = 0;
        this.f1068p = new Rect();
        this.q = new Rect();
        this.f1069t = new Rect();
        this.f1070w = new Rect();
        this.f1071x = new Rect();
        this.f1072y = new Rect();
        this.f1073z = new Rect();
        androidx.core.view.m0 m0Var = androidx.core.view.m0.f5037b;
        this.A = m0Var;
        this.B = m0Var;
        this.C = m0Var;
        this.E = m0Var;
        this.K = new a();
        this.L = new b();
        this.O = new c();
        v(context);
        this.P = new androidx.core.view.u(this);
    }

    private void A() {
        u();
        this.L.run();
    }

    private boolean B(float f5) {
        this.G.fling(0, 0, 0, (int) f5, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return this.G.getFinalY() > this.f1058d.getHeight();
    }

    private void p() {
        u();
        this.O.run();
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0016  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean q(android.view.View r3, android.graphics.Rect r4, boolean r5, boolean r6, boolean r7, boolean r8) {
        /*
            r2 = this;
            android.view.ViewGroup$LayoutParams r3 = r3.getLayoutParams()
            androidx.appcompat.widget.ActionBarOverlayLayout$LayoutParams r3 = (androidx.appcompat.widget.ActionBarOverlayLayout.LayoutParams) r3
            r0 = 1
            if (r5 == 0) goto L13
            int r5 = r3.leftMargin
            int r1 = r4.left
            if (r5 == r1) goto L13
            r3.leftMargin = r1
            r5 = r0
            goto L14
        L13:
            r5 = 0
        L14:
            if (r6 == 0) goto L1f
            int r6 = r3.topMargin
            int r1 = r4.top
            if (r6 == r1) goto L1f
            r3.topMargin = r1
            r5 = r0
        L1f:
            if (r8 == 0) goto L2a
            int r6 = r3.rightMargin
            int r8 = r4.right
            if (r6 == r8) goto L2a
            r3.rightMargin = r8
            r5 = r0
        L2a:
            if (r7 == 0) goto L35
            int r6 = r3.bottomMargin
            int r4 = r4.bottom
            if (r6 == r4) goto L35
            r3.bottomMargin = r4
            goto L36
        L35:
            r0 = r5
        L36:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ActionBarOverlayLayout.q(android.view.View, android.graphics.Rect, boolean, boolean, boolean, boolean):boolean");
    }

    private s t(View view) {
        if (view instanceof s) {
            return (s) view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getWrapper();
        }
        throw new IllegalStateException("Can't make a decor toolbar out of " + view.getClass().getSimpleName());
    }

    private void v(Context context) {
        TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(Q);
        this.f1055a = obtainStyledAttributes.getDimensionPixelSize(0, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(1);
        this.f1060f = drawable;
        setWillNotDraw(drawable == null);
        obtainStyledAttributes.recycle();
        this.f1061g = context.getApplicationInfo().targetSdkVersion < 19;
        this.G = new OverScroller(context);
    }

    private void x() {
        u();
        postDelayed(this.O, 600L);
    }

    private void y() {
        u();
        postDelayed(this.L, 600L);
    }

    @Override // androidx.appcompat.widget.r
    public void a(Menu menu, m.a aVar) {
        z();
        this.f1059e.a(menu, aVar);
    }

    @Override // androidx.appcompat.widget.r
    public boolean b() {
        z();
        return this.f1059e.b();
    }

    @Override // androidx.appcompat.widget.r
    public void c() {
        z();
        this.f1059e.c();
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // androidx.appcompat.widget.r
    public boolean d() {
        z();
        return this.f1059e.d();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.f1060f == null || this.f1061g) {
            return;
        }
        int bottom = this.f1058d.getVisibility() == 0 ? (int) (this.f1058d.getBottom() + this.f1058d.getTranslationY() + 0.5f) : 0;
        this.f1060f.setBounds(0, bottom, getWidth(), this.f1060f.getIntrinsicHeight() + bottom);
        this.f1060f.draw(canvas);
    }

    @Override // androidx.appcompat.widget.r
    public boolean e() {
        z();
        return this.f1059e.e();
    }

    @Override // androidx.appcompat.widget.r
    public boolean f() {
        z();
        return this.f1059e.f();
    }

    @Override // android.view.View
    protected boolean fitSystemWindows(Rect rect) {
        if (Build.VERSION.SDK_INT >= 21) {
            return super.fitSystemWindows(rect);
        }
        z();
        boolean q = q(this.f1058d, rect, true, true, false, true);
        this.f1070w.set(rect);
        u0.a(this, this.f1070w, this.f1068p);
        if (!this.f1071x.equals(this.f1070w)) {
            this.f1071x.set(this.f1070w);
            q = true;
        }
        if (!this.q.equals(this.f1068p)) {
            this.q.set(this.f1068p);
            q = true;
        }
        if (q) {
            requestLayout();
        }
        return true;
    }

    @Override // androidx.appcompat.widget.r
    public boolean g() {
        z();
        return this.f1059e.g();
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public int getActionBarHideOffset() {
        ActionBarContainer actionBarContainer = this.f1058d;
        if (actionBarContainer != null) {
            return -((int) actionBarContainer.getTranslationY());
        }
        return 0;
    }

    @Override // android.view.ViewGroup
    public int getNestedScrollAxes() {
        return this.P.a();
    }

    public CharSequence getTitle() {
        z();
        return this.f1059e.getTitle();
    }

    @Override // androidx.appcompat.widget.r
    public void h(int i8) {
        z();
        if (i8 == 2) {
            this.f1059e.u();
        } else if (i8 == 5) {
            this.f1059e.v();
        } else if (i8 != 109) {
        } else {
            setOverlayMode(true);
        }
    }

    @Override // androidx.appcompat.widget.r
    public void i() {
        z();
        this.f1059e.h();
    }

    @Override // androidx.core.view.s
    public void j(View view, int i8, int i9, int i10, int i11, int i12, int[] iArr) {
        k(view, i8, i9, i10, i11, i12);
    }

    @Override // androidx.core.view.r
    public void k(View view, int i8, int i9, int i10, int i11, int i12) {
        if (i12 == 0) {
            onNestedScroll(view, i8, i9, i10, i11);
        }
    }

    @Override // androidx.core.view.r
    public boolean l(View view, View view2, int i8, int i9) {
        return i9 == 0 && onStartNestedScroll(view, view2, i8);
    }

    @Override // androidx.core.view.r
    public void m(View view, View view2, int i8, int i9) {
        if (i9 == 0) {
            onNestedScrollAccepted(view, view2, i8);
        }
    }

    @Override // androidx.core.view.r
    public void n(View view, int i8) {
        if (i8 == 0) {
            onStopNestedScroll(view);
        }
    }

    @Override // androidx.core.view.r
    public void o(View view, int i8, int i9, int[] iArr, int i10) {
        if (i10 == 0) {
            onNestedPreScroll(view, i8, i9, iArr);
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        z();
        androidx.core.view.m0 z4 = androidx.core.view.m0.z(windowInsets, this);
        boolean q = q(this.f1058d, new Rect(z4.k(), z4.m(), z4.l(), z4.j()), true, true, false, true);
        androidx.core.view.c0.h(this, z4, this.f1068p);
        Rect rect = this.f1068p;
        androidx.core.view.m0 o5 = z4.o(rect.left, rect.top, rect.right, rect.bottom);
        this.A = o5;
        boolean z8 = true;
        if (!this.B.equals(o5)) {
            this.B = this.A;
            q = true;
        }
        if (this.q.equals(this.f1068p)) {
            z8 = q;
        } else {
            this.q.set(this.f1068p);
        }
        if (z8) {
            requestLayout();
        }
        return z4.a().c().b().x();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        v(getContext());
        androidx.core.view.c0.q0(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        u();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        for (int i12 = 0; i12 < childCount; i12++) {
            View childAt = getChildAt(i12);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i13 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + paddingLeft;
                int i14 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + paddingTop;
                childAt.layout(i13, i14, measuredWidth + i13, measuredHeight + i14);
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        int measuredHeight;
        androidx.core.view.m0 a9;
        z();
        measureChildWithMargins(this.f1058d, i8, 0, i9, 0);
        LayoutParams layoutParams = (LayoutParams) this.f1058d.getLayoutParams();
        int max = Math.max(0, this.f1058d.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin);
        int max2 = Math.max(0, this.f1058d.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
        int combineMeasuredStates = View.combineMeasuredStates(0, this.f1058d.getMeasuredState());
        boolean z4 = (androidx.core.view.c0.P(this) & RecognitionOptions.QR_CODE) != 0;
        if (z4) {
            measuredHeight = this.f1055a;
            if (this.f1063j && this.f1058d.getTabContainer() != null) {
                measuredHeight += this.f1055a;
            }
        } else {
            measuredHeight = this.f1058d.getVisibility() != 8 ? this.f1058d.getMeasuredHeight() : 0;
        }
        this.f1069t.set(this.f1068p);
        int i10 = Build.VERSION.SDK_INT;
        if (i10 >= 21) {
            this.C = this.A;
        } else {
            this.f1072y.set(this.f1070w);
        }
        if (!this.f1062h && !z4) {
            Rect rect = this.f1069t;
            rect.top += measuredHeight;
            rect.bottom += 0;
            if (i10 >= 21) {
                a9 = this.C.o(0, measuredHeight, 0, 0);
                this.C = a9;
            }
        } else if (i10 >= 21) {
            a9 = new m0.b(this.C).c(androidx.core.graphics.c.b(this.C.k(), this.C.m() + measuredHeight, this.C.l(), this.C.j() + 0)).a();
            this.C = a9;
        } else {
            Rect rect2 = this.f1072y;
            rect2.top += measuredHeight;
            rect2.bottom += 0;
        }
        q(this.f1057c, this.f1069t, true, true, true, true);
        if (i10 >= 21 && !this.E.equals(this.C)) {
            androidx.core.view.m0 m0Var = this.C;
            this.E = m0Var;
            androidx.core.view.c0.i(this.f1057c, m0Var);
        } else if (i10 < 21 && !this.f1073z.equals(this.f1072y)) {
            this.f1073z.set(this.f1072y);
            this.f1057c.a(this.f1072y);
        }
        measureChildWithMargins(this.f1057c, i8, 0, i9, 0);
        LayoutParams layoutParams2 = (LayoutParams) this.f1057c.getLayoutParams();
        int max3 = Math.max(max, this.f1057c.getMeasuredWidth() + ((ViewGroup.MarginLayoutParams) layoutParams2).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).rightMargin);
        int max4 = Math.max(max2, this.f1057c.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin);
        int combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates, this.f1057c.getMeasuredState());
        setMeasuredDimension(View.resolveSizeAndState(Math.max(max3 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i8, combineMeasuredStates2), View.resolveSizeAndState(Math.max(max4 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i9, combineMeasuredStates2 << 16));
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onNestedFling(View view, float f5, float f8, boolean z4) {
        if (this.f1064k && z4) {
            if (B(f8)) {
                p();
            } else {
                A();
            }
            this.f1065l = true;
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onNestedPreFling(View view, float f5, float f8) {
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedPreScroll(View view, int i8, int i9, int[] iArr) {
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedScroll(View view, int i8, int i9, int i10, int i11) {
        int i12 = this.f1066m + i9;
        this.f1066m = i12;
        setActionBarHideOffset(i12);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedScrollAccepted(View view, View view2, int i8) {
        this.P.b(view, view2, i8);
        this.f1066m = getActionBarHideOffset();
        u();
        d dVar = this.F;
        if (dVar != null) {
            dVar.e();
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onStartNestedScroll(View view, View view2, int i8) {
        if ((i8 & 2) == 0 || this.f1058d.getVisibility() != 0) {
            return false;
        }
        return this.f1064k;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onStopNestedScroll(View view) {
        if (this.f1064k && !this.f1065l) {
            if (this.f1066m <= this.f1058d.getHeight()) {
                y();
            } else {
                x();
            }
        }
        d dVar = this.F;
        if (dVar != null) {
            dVar.b();
        }
    }

    @Override // android.view.View
    @Deprecated
    public void onWindowSystemUiVisibilityChanged(int i8) {
        if (Build.VERSION.SDK_INT >= 16) {
            super.onWindowSystemUiVisibilityChanged(i8);
        }
        z();
        int i9 = this.f1067n ^ i8;
        this.f1067n = i8;
        boolean z4 = (i8 & 4) == 0;
        boolean z8 = (i8 & RecognitionOptions.QR_CODE) != 0;
        d dVar = this.F;
        if (dVar != null) {
            dVar.c(!z8);
            if (z4 || !z8) {
                this.F.a();
            } else {
                this.F.d();
            }
        }
        if ((i9 & RecognitionOptions.QR_CODE) == 0 || this.F == null) {
            return;
        }
        androidx.core.view.c0.q0(this);
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i8) {
        super.onWindowVisibilityChanged(i8);
        this.f1056b = i8;
        d dVar = this.F;
        if (dVar != null) {
            dVar.f(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: r */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.view.ViewGroup
    /* renamed from: s */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public void setActionBarHideOffset(int i8) {
        u();
        this.f1058d.setTranslationY(-Math.max(0, Math.min(i8, this.f1058d.getHeight())));
    }

    public void setActionBarVisibilityCallback(d dVar) {
        this.F = dVar;
        if (getWindowToken() != null) {
            this.F.f(this.f1056b);
            int i8 = this.f1067n;
            if (i8 != 0) {
                onWindowSystemUiVisibilityChanged(i8);
                androidx.core.view.c0.q0(this);
            }
        }
    }

    public void setHasNonEmbeddedTabs(boolean z4) {
        this.f1063j = z4;
    }

    public void setHideOnContentScrollEnabled(boolean z4) {
        if (z4 != this.f1064k) {
            this.f1064k = z4;
            if (z4) {
                return;
            }
            u();
            setActionBarHideOffset(0);
        }
    }

    public void setIcon(int i8) {
        z();
        this.f1059e.setIcon(i8);
    }

    public void setIcon(Drawable drawable) {
        z();
        this.f1059e.setIcon(drawable);
    }

    public void setLogo(int i8) {
        z();
        this.f1059e.m(i8);
    }

    public void setOverlayMode(boolean z4) {
        this.f1062h = z4;
        this.f1061g = z4 && getContext().getApplicationInfo().targetSdkVersion < 19;
    }

    public void setShowingForActionMode(boolean z4) {
    }

    public void setUiOptions(int i8) {
    }

    @Override // androidx.appcompat.widget.r
    public void setWindowCallback(Window.Callback callback) {
        z();
        this.f1059e.setWindowCallback(callback);
    }

    @Override // androidx.appcompat.widget.r
    public void setWindowTitle(CharSequence charSequence) {
        z();
        this.f1059e.setWindowTitle(charSequence);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    void u() {
        removeCallbacks(this.L);
        removeCallbacks(this.O);
        ViewPropertyAnimator viewPropertyAnimator = this.H;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
    }

    public boolean w() {
        return this.f1062h;
    }

    void z() {
        if (this.f1057c == null) {
            this.f1057c = (ContentFrameLayout) findViewById(g.f.f19937b);
            this.f1058d = (ActionBarContainer) findViewById(g.f.f19938c);
            this.f1059e = t(findViewById(g.f.f19936a));
        }
    }
}
