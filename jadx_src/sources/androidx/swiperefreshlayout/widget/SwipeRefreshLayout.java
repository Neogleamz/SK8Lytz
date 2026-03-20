package androidx.swiperefreshlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ListView;
import androidx.core.view.c0;
import androidx.core.view.p;
import androidx.core.view.q;
import androidx.core.view.t;
import androidx.core.view.u;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SwipeRefreshLayout extends ViewGroup implements t, p {

    /* renamed from: f0  reason: collision with root package name */
    private static final String f7295f0 = SwipeRefreshLayout.class.getSimpleName();

    /* renamed from: g0  reason: collision with root package name */
    private static final int[] f7296g0 = {16842766};
    androidx.swiperefreshlayout.widget.a A;
    private int B;
    protected int C;
    float E;
    protected int F;
    int G;
    int H;
    androidx.swiperefreshlayout.widget.b K;
    private Animation L;
    private Animation O;
    private Animation P;
    private Animation Q;
    private Animation R;
    boolean T;
    private int W;

    /* renamed from: a  reason: collision with root package name */
    private View f7297a;

    /* renamed from: a0  reason: collision with root package name */
    boolean f7298a0;

    /* renamed from: b  reason: collision with root package name */
    j f7299b;

    /* renamed from: b0  reason: collision with root package name */
    private i f7300b0;

    /* renamed from: c  reason: collision with root package name */
    boolean f7301c;

    /* renamed from: c0  reason: collision with root package name */
    private Animation.AnimationListener f7302c0;

    /* renamed from: d  reason: collision with root package name */
    private int f7303d;

    /* renamed from: d0  reason: collision with root package name */
    private final Animation f7304d0;

    /* renamed from: e  reason: collision with root package name */
    private float f7305e;

    /* renamed from: e0  reason: collision with root package name */
    private final Animation f7306e0;

    /* renamed from: f  reason: collision with root package name */
    private float f7307f;

    /* renamed from: g  reason: collision with root package name */
    private final u f7308g;

    /* renamed from: h  reason: collision with root package name */
    private final q f7309h;

    /* renamed from: j  reason: collision with root package name */
    private final int[] f7310j;

    /* renamed from: k  reason: collision with root package name */
    private final int[] f7311k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f7312l;

    /* renamed from: m  reason: collision with root package name */
    private int f7313m;

    /* renamed from: n  reason: collision with root package name */
    int f7314n;

    /* renamed from: p  reason: collision with root package name */
    private float f7315p;
    private float q;

    /* renamed from: t  reason: collision with root package name */
    private boolean f7316t;

    /* renamed from: w  reason: collision with root package name */
    private int f7317w;

    /* renamed from: x  reason: collision with root package name */
    boolean f7318x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f7319y;

    /* renamed from: z  reason: collision with root package name */
    private final DecelerateInterpolator f7320z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Animation.AnimationListener {
        a() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            j jVar;
            SwipeRefreshLayout swipeRefreshLayout = SwipeRefreshLayout.this;
            if (!swipeRefreshLayout.f7301c) {
                swipeRefreshLayout.r();
                return;
            }
            swipeRefreshLayout.K.setAlpha(255);
            SwipeRefreshLayout.this.K.start();
            SwipeRefreshLayout swipeRefreshLayout2 = SwipeRefreshLayout.this;
            if (swipeRefreshLayout2.T && (jVar = swipeRefreshLayout2.f7299b) != null) {
                jVar.a();
            }
            SwipeRefreshLayout swipeRefreshLayout3 = SwipeRefreshLayout.this;
            swipeRefreshLayout3.f7314n = swipeRefreshLayout3.A.getTop();
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends Animation {
        b() {
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f5, Transformation transformation) {
            SwipeRefreshLayout.this.setAnimationProgress(f5);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends Animation {
        c() {
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f5, Transformation transformation) {
            SwipeRefreshLayout.this.setAnimationProgress(1.0f - f5);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends Animation {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f7324a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f7325b;

        d(int i8, int i9) {
            this.f7324a = i8;
            this.f7325b = i9;
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f5, Transformation transformation) {
            androidx.swiperefreshlayout.widget.b bVar = SwipeRefreshLayout.this.K;
            int i8 = this.f7324a;
            bVar.setAlpha((int) (i8 + ((this.f7325b - i8) * f5)));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements Animation.AnimationListener {
        e() {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            SwipeRefreshLayout swipeRefreshLayout = SwipeRefreshLayout.this;
            if (swipeRefreshLayout.f7318x) {
                return;
            }
            swipeRefreshLayout.x(null);
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class f extends Animation {
        f() {
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f5, Transformation transformation) {
            SwipeRefreshLayout swipeRefreshLayout = SwipeRefreshLayout.this;
            int abs = !swipeRefreshLayout.f7298a0 ? swipeRefreshLayout.G - Math.abs(swipeRefreshLayout.F) : swipeRefreshLayout.G;
            SwipeRefreshLayout swipeRefreshLayout2 = SwipeRefreshLayout.this;
            int i8 = swipeRefreshLayout2.C;
            SwipeRefreshLayout.this.setTargetOffsetTopAndBottom((i8 + ((int) ((abs - i8) * f5))) - swipeRefreshLayout2.A.getTop());
            SwipeRefreshLayout.this.K.e(1.0f - f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class g extends Animation {
        g() {
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f5, Transformation transformation) {
            SwipeRefreshLayout.this.p(f5);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h extends Animation {
        h() {
        }

        @Override // android.view.animation.Animation
        public void applyTransformation(float f5, Transformation transformation) {
            SwipeRefreshLayout swipeRefreshLayout = SwipeRefreshLayout.this;
            float f8 = swipeRefreshLayout.E;
            swipeRefreshLayout.setAnimationProgress(f8 + ((-f8) * f5));
            SwipeRefreshLayout.this.p(f5);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface i {
        boolean a(SwipeRefreshLayout swipeRefreshLayout, View view);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface j {
        void a();
    }

    public SwipeRefreshLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f7301c = false;
        this.f7305e = -1.0f;
        this.f7310j = new int[2];
        this.f7311k = new int[2];
        this.f7317w = -1;
        this.B = -1;
        this.f7302c0 = new a();
        this.f7304d0 = new f();
        this.f7306e0 = new g();
        this.f7303d = ViewConfiguration.get(context).getScaledTouchSlop();
        this.f7313m = getResources().getInteger(17694721);
        setWillNotDraw(false);
        this.f7320z = new DecelerateInterpolator(2.0f);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.W = (int) (displayMetrics.density * 40.0f);
        d();
        setChildrenDrawingOrderEnabled(true);
        int i8 = (int) (displayMetrics.density * 64.0f);
        this.G = i8;
        this.f7305e = i8;
        this.f7308g = new u(this);
        this.f7309h = new q(this);
        setNestedScrollingEnabled(true);
        int i9 = -this.W;
        this.f7314n = i9;
        this.F = i9;
        p(1.0f);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f7296g0);
        setEnabled(obtainStyledAttributes.getBoolean(0, true));
        obtainStyledAttributes.recycle();
    }

    private void a(int i8, Animation.AnimationListener animationListener) {
        this.C = i8;
        this.f7304d0.reset();
        this.f7304d0.setDuration(200L);
        this.f7304d0.setInterpolator(this.f7320z);
        if (animationListener != null) {
            this.A.b(animationListener);
        }
        this.A.clearAnimation();
        this.A.startAnimation(this.f7304d0);
    }

    private void b(int i8, Animation.AnimationListener animationListener) {
        if (this.f7318x) {
            y(i8, animationListener);
            return;
        }
        this.C = i8;
        this.f7306e0.reset();
        this.f7306e0.setDuration(200L);
        this.f7306e0.setInterpolator(this.f7320z);
        if (animationListener != null) {
            this.A.b(animationListener);
        }
        this.A.clearAnimation();
        this.A.startAnimation(this.f7306e0);
    }

    private void d() {
        this.A = new androidx.swiperefreshlayout.widget.a(getContext(), -328966);
        androidx.swiperefreshlayout.widget.b bVar = new androidx.swiperefreshlayout.widget.b(getContext());
        this.K = bVar;
        bVar.l(1);
        this.A.setImageDrawable(this.K);
        this.A.setVisibility(8);
        addView(this.A);
    }

    private void e() {
        if (this.f7297a == null) {
            for (int i8 = 0; i8 < getChildCount(); i8++) {
                View childAt = getChildAt(i8);
                if (!childAt.equals(this.A)) {
                    this.f7297a = childAt;
                    return;
                }
            }
        }
    }

    private void f(float f5) {
        if (f5 > this.f7305e) {
            s(true, true);
            return;
        }
        this.f7301c = false;
        this.K.j(0.0f, 0.0f);
        b(this.f7314n, this.f7318x ? null : new e());
        this.K.d(false);
    }

    private boolean g(Animation animation) {
        return (animation == null || !animation.hasStarted() || animation.hasEnded()) ? false : true;
    }

    private void i(float f5) {
        this.K.d(true);
        float min = Math.min(1.0f, Math.abs(f5 / this.f7305e));
        float max = (((float) Math.max(min - 0.4d, 0.0d)) * 5.0f) / 3.0f;
        float abs = Math.abs(f5) - this.f7305e;
        int i8 = this.H;
        if (i8 <= 0) {
            i8 = this.f7298a0 ? this.G - this.F : this.G;
        }
        float f8 = i8;
        double max2 = Math.max(0.0f, Math.min(abs, f8 * 2.0f) / f8) / 4.0f;
        float pow = ((float) (max2 - Math.pow(max2, 2.0d))) * 2.0f;
        int i9 = this.F + ((int) ((f8 * min) + (f8 * pow * 2.0f)));
        if (this.A.getVisibility() != 0) {
            this.A.setVisibility(0);
        }
        if (!this.f7318x) {
            this.A.setScaleX(1.0f);
            this.A.setScaleY(1.0f);
        }
        if (this.f7318x) {
            setAnimationProgress(Math.min(1.0f, f5 / this.f7305e));
        }
        if (f5 < this.f7305e) {
            if (this.K.getAlpha() > 76 && !g(this.P)) {
                w();
            }
        } else if (this.K.getAlpha() < 255 && !g(this.Q)) {
            v();
        }
        this.K.j(0.0f, Math.min(0.8f, max * 0.8f));
        this.K.e(Math.min(1.0f, max));
        this.K.g((((max * 0.4f) - 0.25f) + (pow * 2.0f)) * 0.5f);
        setTargetOffsetTopAndBottom(i9 - this.f7314n);
    }

    private void q(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.f7317w) {
            this.f7317w = motionEvent.getPointerId(actionIndex == 0 ? 1 : 0);
        }
    }

    private void s(boolean z4, boolean z8) {
        if (this.f7301c != z4) {
            this.T = z8;
            e();
            this.f7301c = z4;
            if (z4) {
                a(this.f7314n, this.f7302c0);
            } else {
                x(this.f7302c0);
            }
        }
    }

    private void setColorViewAlpha(int i8) {
        this.A.getBackground().setAlpha(i8);
        this.K.setAlpha(i8);
    }

    private Animation t(int i8, int i9) {
        d dVar = new d(i8, i9);
        dVar.setDuration(300L);
        this.A.b(null);
        this.A.clearAnimation();
        this.A.startAnimation(dVar);
        return dVar;
    }

    private void u(float f5) {
        float f8 = this.q;
        int i8 = this.f7303d;
        if (f5 - f8 <= i8 || this.f7316t) {
            return;
        }
        this.f7315p = f8 + i8;
        this.f7316t = true;
        this.K.setAlpha(76);
    }

    private void v() {
        this.Q = t(this.K.getAlpha(), 255);
    }

    private void w() {
        this.P = t(this.K.getAlpha(), 76);
    }

    private void y(int i8, Animation.AnimationListener animationListener) {
        this.C = i8;
        this.E = this.A.getScaleX();
        h hVar = new h();
        this.R = hVar;
        hVar.setDuration(150L);
        if (animationListener != null) {
            this.A.b(animationListener);
        }
        this.A.clearAnimation();
        this.A.startAnimation(this.R);
    }

    private void z(Animation.AnimationListener animationListener) {
        this.A.setVisibility(0);
        this.K.setAlpha(255);
        b bVar = new b();
        this.L = bVar;
        bVar.setDuration(this.f7313m);
        if (animationListener != null) {
            this.A.b(animationListener);
        }
        this.A.clearAnimation();
        this.A.startAnimation(this.L);
    }

    public boolean c() {
        i iVar = this.f7300b0;
        if (iVar != null) {
            return iVar.a(this, this.f7297a);
        }
        View view = this.f7297a;
        return view instanceof ListView ? androidx.core.widget.i.a((ListView) view, -1) : view.canScrollVertically(-1);
    }

    @Override // android.view.View
    public boolean dispatchNestedFling(float f5, float f8, boolean z4) {
        return this.f7309h.a(f5, f8, z4);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreFling(float f5, float f8) {
        return this.f7309h.b(f5, f8);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreScroll(int i8, int i9, int[] iArr, int[] iArr2) {
        return this.f7309h.c(i8, i9, iArr, iArr2);
    }

    @Override // android.view.View
    public boolean dispatchNestedScroll(int i8, int i9, int i10, int i11, int[] iArr) {
        return this.f7309h.f(i8, i9, i10, i11, iArr);
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int i8, int i9) {
        int i10 = this.B;
        return i10 < 0 ? i9 : i9 == i8 + (-1) ? i10 : i9 >= i10 ? i9 + 1 : i9;
    }

    @Override // android.view.ViewGroup
    public int getNestedScrollAxes() {
        return this.f7308g.a();
    }

    public int getProgressCircleDiameter() {
        return this.W;
    }

    public int getProgressViewEndOffset() {
        return this.G;
    }

    public int getProgressViewStartOffset() {
        return this.F;
    }

    public boolean h() {
        return this.f7301c;
    }

    @Override // android.view.View
    public boolean hasNestedScrollingParent() {
        return this.f7309h.j();
    }

    @Override // android.view.View, androidx.core.view.p
    public boolean isNestedScrollingEnabled() {
        return this.f7309h.l();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        r();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        e();
        int actionMasked = motionEvent.getActionMasked();
        if (this.f7319y && actionMasked == 0) {
            this.f7319y = false;
        }
        if (!isEnabled() || this.f7319y || c() || this.f7301c || this.f7312l) {
            return false;
        }
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int i8 = this.f7317w;
                    if (i8 == -1) {
                        Log.e(f7295f0, "Got ACTION_MOVE event but don't have an active pointer id.");
                        return false;
                    }
                    int findPointerIndex = motionEvent.findPointerIndex(i8);
                    if (findPointerIndex < 0) {
                        return false;
                    }
                    u(motionEvent.getY(findPointerIndex));
                } else if (actionMasked != 3) {
                    if (actionMasked == 6) {
                        q(motionEvent);
                    }
                }
            }
            this.f7316t = false;
            this.f7317w = -1;
        } else {
            setTargetOffsetTopAndBottom(this.F - this.A.getTop());
            int pointerId = motionEvent.getPointerId(0);
            this.f7317w = pointerId;
            this.f7316t = false;
            int findPointerIndex2 = motionEvent.findPointerIndex(pointerId);
            if (findPointerIndex2 < 0) {
                return false;
            }
            this.q = motionEvent.getY(findPointerIndex2);
        }
        return this.f7316t;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        if (this.f7297a == null) {
            e();
        }
        View view = this.f7297a;
        if (view == null) {
            return;
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        view.layout(paddingLeft, paddingTop, ((measuredWidth - getPaddingLeft()) - getPaddingRight()) + paddingLeft, ((measuredHeight - getPaddingTop()) - getPaddingBottom()) + paddingTop);
        int measuredWidth2 = this.A.getMeasuredWidth();
        int measuredHeight2 = this.A.getMeasuredHeight();
        int i12 = measuredWidth / 2;
        int i13 = measuredWidth2 / 2;
        int i14 = this.f7314n;
        this.A.layout(i12 - i13, i14, i12 + i13, measuredHeight2 + i14);
    }

    @Override // android.view.View
    public void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        if (this.f7297a == null) {
            e();
        }
        View view = this.f7297a;
        if (view == null) {
            return;
        }
        view.measure(View.MeasureSpec.makeMeasureSpec((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824), View.MeasureSpec.makeMeasureSpec((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), 1073741824));
        this.A.measure(View.MeasureSpec.makeMeasureSpec(this.W, 1073741824), View.MeasureSpec.makeMeasureSpec(this.W, 1073741824));
        this.B = -1;
        for (int i10 = 0; i10 < getChildCount(); i10++) {
            if (getChildAt(i10) == this.A) {
                this.B = i10;
                return;
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onNestedFling(View view, float f5, float f8, boolean z4) {
        return dispatchNestedFling(f5, f8, z4);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onNestedPreFling(View view, float f5, float f8) {
        return dispatchNestedPreFling(f5, f8);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedPreScroll(View view, int i8, int i9, int[] iArr) {
        if (i9 > 0) {
            float f5 = this.f7307f;
            if (f5 > 0.0f) {
                float f8 = i9;
                if (f8 > f5) {
                    iArr[1] = i9 - ((int) f5);
                    this.f7307f = 0.0f;
                } else {
                    this.f7307f = f5 - f8;
                    iArr[1] = i9;
                }
                i(this.f7307f);
            }
        }
        if (this.f7298a0 && i9 > 0 && this.f7307f == 0.0f && Math.abs(i9 - iArr[1]) > 0) {
            this.A.setVisibility(8);
        }
        int[] iArr2 = this.f7310j;
        if (dispatchNestedPreScroll(i8 - iArr[0], i9 - iArr[1], iArr2, null)) {
            iArr[0] = iArr[0] + iArr2[0];
            iArr[1] = iArr[1] + iArr2[1];
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedScroll(View view, int i8, int i9, int i10, int i11) {
        dispatchNestedScroll(i8, i9, i10, i11, this.f7311k);
        int i12 = i11 + this.f7311k[1];
        if (i12 >= 0 || c()) {
            return;
        }
        float abs = this.f7307f + Math.abs(i12);
        this.f7307f = abs;
        i(abs);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedScrollAccepted(View view, View view2, int i8) {
        this.f7308g.b(view, view2, i8);
        startNestedScroll(i8 & 2);
        this.f7307f = 0.0f;
        this.f7312l = true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onStartNestedScroll(View view, View view2, int i8) {
        return (!isEnabled() || this.f7319y || this.f7301c || (i8 & 2) == 0) ? false : true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onStopNestedScroll(View view) {
        this.f7308g.d(view);
        this.f7312l = false;
        float f5 = this.f7307f;
        if (f5 > 0.0f) {
            f(f5);
            this.f7307f = 0.0f;
        }
        stopNestedScroll();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (this.f7319y && actionMasked == 0) {
            this.f7319y = false;
        }
        if (!isEnabled() || this.f7319y || c() || this.f7301c || this.f7312l) {
            return false;
        }
        if (actionMasked == 0) {
            this.f7317w = motionEvent.getPointerId(0);
            this.f7316t = false;
        } else if (actionMasked == 1) {
            int findPointerIndex = motionEvent.findPointerIndex(this.f7317w);
            if (findPointerIndex < 0) {
                Log.e(f7295f0, "Got ACTION_UP event but don't have an active pointer id.");
                return false;
            }
            if (this.f7316t) {
                this.f7316t = false;
                f((motionEvent.getY(findPointerIndex) - this.f7315p) * 0.5f);
            }
            this.f7317w = -1;
            return false;
        } else if (actionMasked == 2) {
            int findPointerIndex2 = motionEvent.findPointerIndex(this.f7317w);
            if (findPointerIndex2 < 0) {
                Log.e(f7295f0, "Got ACTION_MOVE event but have an invalid active pointer id.");
                return false;
            }
            float y8 = motionEvent.getY(findPointerIndex2);
            u(y8);
            if (this.f7316t) {
                float f5 = (y8 - this.f7315p) * 0.5f;
                if (f5 <= 0.0f) {
                    return false;
                }
                i(f5);
            }
        } else if (actionMasked == 3) {
            return false;
        } else {
            if (actionMasked == 5) {
                int actionIndex = motionEvent.getActionIndex();
                if (actionIndex < 0) {
                    Log.e(f7295f0, "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                this.f7317w = motionEvent.getPointerId(actionIndex);
            } else if (actionMasked == 6) {
                q(motionEvent);
            }
        }
        return true;
    }

    void p(float f5) {
        int i8 = this.C;
        setTargetOffsetTopAndBottom((i8 + ((int) ((this.F - i8) * f5))) - this.A.getTop());
    }

    void r() {
        this.A.clearAnimation();
        this.K.stop();
        this.A.setVisibility(8);
        setColorViewAlpha(255);
        if (this.f7318x) {
            setAnimationProgress(0.0f);
        } else {
            setTargetOffsetTopAndBottom(this.F - this.f7314n);
        }
        this.f7314n = this.A.getTop();
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z4) {
        if (Build.VERSION.SDK_INT >= 21 || !(this.f7297a instanceof AbsListView)) {
            View view = this.f7297a;
            if (view == null || c0.X(view)) {
                super.requestDisallowInterceptTouchEvent(z4);
            }
        }
    }

    void setAnimationProgress(float f5) {
        this.A.setScaleX(f5);
        this.A.setScaleY(f5);
    }

    @Deprecated
    public void setColorScheme(int... iArr) {
        setColorSchemeResources(iArr);
    }

    public void setColorSchemeColors(int... iArr) {
        e();
        this.K.f(iArr);
    }

    public void setColorSchemeResources(int... iArr) {
        Context context = getContext();
        int[] iArr2 = new int[iArr.length];
        for (int i8 = 0; i8 < iArr.length; i8++) {
            iArr2[i8] = androidx.core.content.a.d(context, iArr[i8]);
        }
        setColorSchemeColors(iArr2);
    }

    public void setDistanceToTriggerSync(int i8) {
        this.f7305e = i8;
    }

    @Override // android.view.View
    public void setEnabled(boolean z4) {
        super.setEnabled(z4);
        if (z4) {
            return;
        }
        r();
    }

    @Override // android.view.View
    public void setNestedScrollingEnabled(boolean z4) {
        this.f7309h.m(z4);
    }

    public void setOnChildScrollUpCallback(i iVar) {
        this.f7300b0 = iVar;
    }

    public void setOnRefreshListener(j jVar) {
        this.f7299b = jVar;
    }

    @Deprecated
    public void setProgressBackgroundColor(int i8) {
        setProgressBackgroundColorSchemeResource(i8);
    }

    public void setProgressBackgroundColorSchemeColor(int i8) {
        this.A.setBackgroundColor(i8);
    }

    public void setProgressBackgroundColorSchemeResource(int i8) {
        setProgressBackgroundColorSchemeColor(androidx.core.content.a.d(getContext(), i8));
    }

    public void setRefreshing(boolean z4) {
        if (!z4 || this.f7301c == z4) {
            s(z4, false);
            return;
        }
        this.f7301c = z4;
        setTargetOffsetTopAndBottom((!this.f7298a0 ? this.G + this.F : this.G) - this.f7314n);
        this.T = false;
        z(this.f7302c0);
    }

    public void setSize(int i8) {
        if (i8 == 0 || i8 == 1) {
            this.W = (int) (getResources().getDisplayMetrics().density * (i8 == 0 ? 56.0f : 40.0f));
            this.A.setImageDrawable(null);
            this.K.l(i8);
            this.A.setImageDrawable(this.K);
        }
    }

    public void setSlingshotDistance(int i8) {
        this.H = i8;
    }

    void setTargetOffsetTopAndBottom(int i8) {
        this.A.bringToFront();
        c0.d0(this.A, i8);
        this.f7314n = this.A.getTop();
    }

    @Override // android.view.View
    public boolean startNestedScroll(int i8) {
        return this.f7309h.o(i8);
    }

    @Override // android.view.View, androidx.core.view.p
    public void stopNestedScroll() {
        this.f7309h.q();
    }

    void x(Animation.AnimationListener animationListener) {
        c cVar = new c();
        this.O = cVar;
        cVar.setDuration(150L);
        this.A.b(animationListener);
        this.A.clearAnimation();
        this.A.startAnimation(this.O);
    }
}
