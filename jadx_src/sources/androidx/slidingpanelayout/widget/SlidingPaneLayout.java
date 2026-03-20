package androidx.slidingpanelayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.c0;
import androidx.customview.view.AbsSavedState;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import w0.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SlidingPaneLayout extends ViewGroup {
    private Method A;
    private Field B;
    private boolean C;

    /* renamed from: a  reason: collision with root package name */
    private int f7260a;

    /* renamed from: b  reason: collision with root package name */
    private int f7261b;

    /* renamed from: c  reason: collision with root package name */
    private Drawable f7262c;

    /* renamed from: d  reason: collision with root package name */
    private Drawable f7263d;

    /* renamed from: e  reason: collision with root package name */
    private final int f7264e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f7265f;

    /* renamed from: g  reason: collision with root package name */
    View f7266g;

    /* renamed from: h  reason: collision with root package name */
    float f7267h;

    /* renamed from: j  reason: collision with root package name */
    private float f7268j;

    /* renamed from: k  reason: collision with root package name */
    int f7269k;

    /* renamed from: l  reason: collision with root package name */
    boolean f7270l;

    /* renamed from: m  reason: collision with root package name */
    private int f7271m;

    /* renamed from: n  reason: collision with root package name */
    private float f7272n;

    /* renamed from: p  reason: collision with root package name */
    private float f7273p;
    private d q;

    /* renamed from: t  reason: collision with root package name */
    final w0.c f7274t;

    /* renamed from: w  reason: collision with root package name */
    boolean f7275w;

    /* renamed from: x  reason: collision with root package name */
    private boolean f7276x;

    /* renamed from: y  reason: collision with root package name */
    private final Rect f7277y;

    /* renamed from: z  reason: collision with root package name */
    final ArrayList<b> f7278z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        /* renamed from: e  reason: collision with root package name */
        private static final int[] f7279e = {16843137};

        /* renamed from: a  reason: collision with root package name */
        public float f7280a;

        /* renamed from: b  reason: collision with root package name */
        boolean f7281b;

        /* renamed from: c  reason: collision with root package name */
        boolean f7282c;

        /* renamed from: d  reason: collision with root package name */
        Paint f7283d;

        public LayoutParams() {
            super(-1, -1);
            this.f7280a = 0.0f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f7280a = 0.0f;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f7279e);
            this.f7280a = obtainStyledAttributes.getFloat(0, 0.0f);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f7280a = 0.0f;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f7280a = 0.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        boolean f7284c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f7284c = parcel.readInt() != 0;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeInt(this.f7284c ? 1 : 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends androidx.core.view.a {

        /* renamed from: d  reason: collision with root package name */
        private final Rect f7285d = new Rect();

        a() {
        }

        private void n(androidx.core.view.accessibility.c cVar, androidx.core.view.accessibility.c cVar2) {
            Rect rect = this.f7285d;
            cVar2.m(rect);
            cVar.X(rect);
            cVar2.n(rect);
            cVar.Y(rect);
            cVar.G0(cVar2.N());
            cVar.r0(cVar2.v());
            cVar.c0(cVar2.p());
            cVar.g0(cVar2.r());
            cVar.i0(cVar2.F());
            cVar.d0(cVar2.E());
            cVar.k0(cVar2.G());
            cVar.l0(cVar2.H());
            cVar.V(cVar2.B());
            cVar.z0(cVar2.L());
            cVar.o0(cVar2.I());
            cVar.a(cVar2.k());
            cVar.q0(cVar2.t());
        }

        @Override // androidx.core.view.a
        public void f(View view, AccessibilityEvent accessibilityEvent) {
            super.f(view, accessibilityEvent);
            accessibilityEvent.setClassName(SlidingPaneLayout.class.getName());
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            androidx.core.view.accessibility.c Q = androidx.core.view.accessibility.c.Q(cVar);
            super.g(view, Q);
            n(cVar, Q);
            Q.S();
            cVar.c0(SlidingPaneLayout.class.getName());
            cVar.B0(view);
            ViewParent K = c0.K(view);
            if (K instanceof View) {
                cVar.t0((View) K);
            }
            int childCount = SlidingPaneLayout.this.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = SlidingPaneLayout.this.getChildAt(i8);
                if (!o(childAt) && childAt.getVisibility() == 0) {
                    c0.E0(childAt, 1);
                    cVar.c(childAt);
                }
            }
        }

        @Override // androidx.core.view.a
        public boolean i(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (o(view)) {
                return false;
            }
            return super.i(viewGroup, view, accessibilityEvent);
        }

        public boolean o(View view) {
            return SlidingPaneLayout.this.h(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final View f7287a;

        b(View view) {
            this.f7287a = view;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.f7287a.getParent() == SlidingPaneLayout.this) {
                this.f7287a.setLayerType(0, null);
                SlidingPaneLayout.this.g(this.f7287a);
            }
            SlidingPaneLayout.this.f7278z.remove(this);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class c extends c.AbstractC0221c {
        c() {
        }

        @Override // w0.c.AbstractC0221c
        public int a(View view, int i8, int i9) {
            LayoutParams layoutParams = (LayoutParams) SlidingPaneLayout.this.f7266g.getLayoutParams();
            if (SlidingPaneLayout.this.i()) {
                int width = SlidingPaneLayout.this.getWidth() - ((SlidingPaneLayout.this.getPaddingRight() + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin) + SlidingPaneLayout.this.f7266g.getWidth());
                return Math.max(Math.min(i8, width), width - SlidingPaneLayout.this.f7269k);
            }
            int paddingLeft = SlidingPaneLayout.this.getPaddingLeft() + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
            return Math.min(Math.max(i8, paddingLeft), SlidingPaneLayout.this.f7269k + paddingLeft);
        }

        @Override // w0.c.AbstractC0221c
        public int b(View view, int i8, int i9) {
            return view.getTop();
        }

        @Override // w0.c.AbstractC0221c
        public int d(View view) {
            return SlidingPaneLayout.this.f7269k;
        }

        @Override // w0.c.AbstractC0221c
        public void f(int i8, int i9) {
            SlidingPaneLayout slidingPaneLayout = SlidingPaneLayout.this;
            slidingPaneLayout.f7274t.c(slidingPaneLayout.f7266g, i9);
        }

        @Override // w0.c.AbstractC0221c
        public void i(View view, int i8) {
            SlidingPaneLayout.this.p();
        }

        @Override // w0.c.AbstractC0221c
        public void j(int i8) {
            SlidingPaneLayout slidingPaneLayout;
            boolean z4;
            if (SlidingPaneLayout.this.f7274t.B() == 0) {
                SlidingPaneLayout slidingPaneLayout2 = SlidingPaneLayout.this;
                if (slidingPaneLayout2.f7267h == 0.0f) {
                    slidingPaneLayout2.r(slidingPaneLayout2.f7266g);
                    SlidingPaneLayout slidingPaneLayout3 = SlidingPaneLayout.this;
                    slidingPaneLayout3.d(slidingPaneLayout3.f7266g);
                    slidingPaneLayout = SlidingPaneLayout.this;
                    z4 = false;
                } else {
                    slidingPaneLayout2.e(slidingPaneLayout2.f7266g);
                    slidingPaneLayout = SlidingPaneLayout.this;
                    z4 = true;
                }
                slidingPaneLayout.f7275w = z4;
            }
        }

        @Override // w0.c.AbstractC0221c
        public void k(View view, int i8, int i9, int i10, int i11) {
            SlidingPaneLayout.this.l(i8);
            SlidingPaneLayout.this.invalidate();
        }

        @Override // w0.c.AbstractC0221c
        public void l(View view, float f5, float f8) {
            int paddingLeft;
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (SlidingPaneLayout.this.i()) {
                int paddingRight = SlidingPaneLayout.this.getPaddingRight() + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                if (f5 < 0.0f || (f5 == 0.0f && SlidingPaneLayout.this.f7267h > 0.5f)) {
                    paddingRight += SlidingPaneLayout.this.f7269k;
                }
                paddingLeft = (SlidingPaneLayout.this.getWidth() - paddingRight) - SlidingPaneLayout.this.f7266g.getWidth();
            } else {
                paddingLeft = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + SlidingPaneLayout.this.getPaddingLeft();
                int i8 = (f5 > 0.0f ? 1 : (f5 == 0.0f ? 0 : -1));
                if (i8 > 0 || (i8 == 0 && SlidingPaneLayout.this.f7267h > 0.5f)) {
                    paddingLeft += SlidingPaneLayout.this.f7269k;
                }
            }
            SlidingPaneLayout.this.f7274t.P(paddingLeft, view.getTop());
            SlidingPaneLayout.this.invalidate();
        }

        @Override // w0.c.AbstractC0221c
        public boolean m(View view, int i8) {
            if (SlidingPaneLayout.this.f7270l) {
                return false;
            }
            return ((LayoutParams) view.getLayoutParams()).f7281b;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        void a(View view, float f5);

        void b(View view);

        void c(View view);
    }

    public SlidingPaneLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlidingPaneLayout(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f7260a = -858993460;
        this.f7276x = true;
        this.f7277y = new Rect();
        this.f7278z = new ArrayList<>();
        float f5 = context.getResources().getDisplayMetrics().density;
        this.f7264e = (int) ((32.0f * f5) + 0.5f);
        setWillNotDraw(false);
        c0.t0(this, new a());
        c0.E0(this, 1);
        w0.c o5 = w0.c.o(this, 0.5f, new c());
        this.f7274t = o5;
        o5.O(f5 * 400.0f);
    }

    private boolean b(View view, int i8) {
        if (this.f7276x || q(0.0f, i8)) {
            this.f7275w = false;
            return true;
        }
        return false;
    }

    private void c(View view, float f5, int i8) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (f5 > 0.0f && i8 != 0) {
            int i9 = (((int) ((((-16777216) & i8) >>> 24) * f5)) << 24) | (i8 & 16777215);
            if (layoutParams.f7283d == null) {
                layoutParams.f7283d = new Paint();
            }
            layoutParams.f7283d.setColorFilter(new PorterDuffColorFilter(i9, PorterDuff.Mode.SRC_OVER));
            if (view.getLayerType() != 2) {
                view.setLayerType(2, layoutParams.f7283d);
            }
            g(view);
        } else if (view.getLayerType() != 0) {
            Paint paint = layoutParams.f7283d;
            if (paint != null) {
                paint.setColorFilter(null);
            }
            b bVar = new b(view);
            this.f7278z.add(bVar);
            c0.l0(this, bVar);
        }
    }

    private boolean n(View view, int i8) {
        if (this.f7276x || q(1.0f, i8)) {
            this.f7275w = true;
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0023  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void o(float r10) {
        /*
            r9 = this;
            boolean r0 = r9.i()
            android.view.View r1 = r9.f7266g
            android.view.ViewGroup$LayoutParams r1 = r1.getLayoutParams()
            androidx.slidingpanelayout.widget.SlidingPaneLayout$LayoutParams r1 = (androidx.slidingpanelayout.widget.SlidingPaneLayout.LayoutParams) r1
            boolean r2 = r1.f7282c
            r3 = 0
            if (r2 == 0) goto L1c
            if (r0 == 0) goto L16
            int r1 = r1.rightMargin
            goto L18
        L16:
            int r1 = r1.leftMargin
        L18:
            if (r1 > 0) goto L1c
            r1 = 1
            goto L1d
        L1c:
            r1 = r3
        L1d:
            int r2 = r9.getChildCount()
        L21:
            if (r3 >= r2) goto L57
            android.view.View r4 = r9.getChildAt(r3)
            android.view.View r5 = r9.f7266g
            if (r4 != r5) goto L2c
            goto L54
        L2c:
            float r5 = r9.f7268j
            r6 = 1065353216(0x3f800000, float:1.0)
            float r5 = r6 - r5
            int r7 = r9.f7271m
            float r8 = (float) r7
            float r5 = r5 * r8
            int r5 = (int) r5
            r9.f7268j = r10
            float r8 = r6 - r10
            float r7 = (float) r7
            float r8 = r8 * r7
            int r7 = (int) r8
            int r5 = r5 - r7
            if (r0 == 0) goto L42
            int r5 = -r5
        L42:
            r4.offsetLeftAndRight(r5)
            if (r1 == 0) goto L54
            float r5 = r9.f7268j
            if (r0 == 0) goto L4d
            float r5 = r5 - r6
            goto L4f
        L4d:
            float r5 = r6 - r5
        L4f:
            int r6 = r9.f7261b
            r9.c(r4, r5, r6)
        L54:
            int r3 = r3 + 1
            goto L21
        L57:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slidingpanelayout.widget.SlidingPaneLayout.o(float):void");
    }

    private static boolean s(View view) {
        Drawable background;
        if (view.isOpaque()) {
            return true;
        }
        return Build.VERSION.SDK_INT < 18 && (background = view.getBackground()) != null && background.getOpacity() == -1;
    }

    public boolean a() {
        return b(this.f7266g, 0);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && super.checkLayoutParams(layoutParams);
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.f7274t.n(true)) {
            if (this.f7265f) {
                c0.j0(this);
            } else {
                this.f7274t.a();
            }
        }
    }

    void d(View view) {
        d dVar = this.q;
        if (dVar != null) {
            dVar.c(view);
        }
        sendAccessibilityEvent(32);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int i8;
        int i9;
        super.draw(canvas);
        Drawable drawable = i() ? this.f7263d : this.f7262c;
        View childAt = getChildCount() > 1 ? getChildAt(1) : null;
        if (childAt == null || drawable == null) {
            return;
        }
        int top = childAt.getTop();
        int bottom = childAt.getBottom();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        if (i()) {
            i9 = childAt.getRight();
            i8 = intrinsicWidth + i9;
        } else {
            int left = childAt.getLeft();
            int i10 = left - intrinsicWidth;
            i8 = left;
            i9 = i10;
        }
        drawable.setBounds(i9, top, i8, bottom);
        drawable.draw(canvas);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j8) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int save = canvas.save();
        if (this.f7265f && !layoutParams.f7281b && this.f7266g != null) {
            canvas.getClipBounds(this.f7277y);
            if (i()) {
                Rect rect = this.f7277y;
                rect.left = Math.max(rect.left, this.f7266g.getRight());
            } else {
                Rect rect2 = this.f7277y;
                rect2.right = Math.min(rect2.right, this.f7266g.getLeft());
            }
            canvas.clipRect(this.f7277y);
        }
        boolean drawChild = super.drawChild(canvas, view, j8);
        canvas.restoreToCount(save);
        return drawChild;
    }

    void e(View view) {
        d dVar = this.q;
        if (dVar != null) {
            dVar.b(view);
        }
        sendAccessibilityEvent(32);
    }

    void f(View view) {
        d dVar = this.q;
        if (dVar != null) {
            dVar.a(view, this.f7267h);
        }
    }

    void g(View view) {
        Field field;
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 17) {
            c0.G0(view, ((LayoutParams) view.getLayoutParams()).f7283d);
            return;
        }
        if (i8 >= 16) {
            if (!this.C) {
                try {
                    this.A = View.class.getDeclaredMethod("getDisplayList", null);
                } catch (NoSuchMethodException e8) {
                    Log.e("SlidingPaneLayout", "Couldn't fetch getDisplayList method; dimming won't work right.", e8);
                }
                try {
                    Field declaredField = View.class.getDeclaredField("mRecreateDisplayList");
                    this.B = declaredField;
                    declaredField.setAccessible(true);
                } catch (NoSuchFieldException e9) {
                    Log.e("SlidingPaneLayout", "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", e9);
                }
                this.C = true;
            }
            if (this.A == null || (field = this.B) == null) {
                view.invalidate();
                return;
            }
            try {
                field.setBoolean(view, true);
                this.A.invoke(view, null);
            } catch (Exception e10) {
                Log.e("SlidingPaneLayout", "Error refreshing display list state", e10);
            }
        }
        c0.k0(this, view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams) : new LayoutParams(layoutParams);
    }

    public int getCoveredFadeColor() {
        return this.f7261b;
    }

    public int getParallaxDistance() {
        return this.f7271m;
    }

    public int getSliderFadeColor() {
        return this.f7260a;
    }

    boolean h(View view) {
        if (view == null) {
            return false;
        }
        return this.f7265f && ((LayoutParams) view.getLayoutParams()).f7282c && this.f7267h > 0.0f;
    }

    boolean i() {
        return c0.E(this) == 1;
    }

    public boolean j() {
        return !this.f7265f || this.f7267h == 1.0f;
    }

    public boolean k() {
        return this.f7265f;
    }

    void l(int i8) {
        if (this.f7266g == null) {
            this.f7267h = 0.0f;
            return;
        }
        boolean i9 = i();
        LayoutParams layoutParams = (LayoutParams) this.f7266g.getLayoutParams();
        int width = this.f7266g.getWidth();
        if (i9) {
            i8 = (getWidth() - i8) - width;
        }
        float paddingRight = (i8 - ((i9 ? getPaddingRight() : getPaddingLeft()) + (i9 ? ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin : ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin))) / this.f7269k;
        this.f7267h = paddingRight;
        if (this.f7271m != 0) {
            o(paddingRight);
        }
        if (layoutParams.f7282c) {
            c(this.f7266g, this.f7267h, this.f7260a);
        }
        f(this.f7266g);
    }

    public boolean m() {
        return n(this.f7266g, 0);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f7276x = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f7276x = true;
        int size = this.f7278z.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.f7278z.get(i8).run();
        }
        this.f7278z.clear();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z4;
        View childAt;
        int actionMasked = motionEvent.getActionMasked();
        if (!this.f7265f && actionMasked == 0 && getChildCount() > 1 && (childAt = getChildAt(1)) != null) {
            this.f7275w = !this.f7274t.F(childAt, (int) motionEvent.getX(), (int) motionEvent.getY());
        }
        if (!this.f7265f || (this.f7270l && actionMasked != 0)) {
            this.f7274t.b();
            return super.onInterceptTouchEvent(motionEvent);
        } else if (actionMasked == 3 || actionMasked == 1) {
            this.f7274t.b();
            return false;
        } else {
            if (actionMasked == 0) {
                this.f7270l = false;
                float x8 = motionEvent.getX();
                float y8 = motionEvent.getY();
                this.f7272n = x8;
                this.f7273p = y8;
                if (this.f7274t.F(this.f7266g, (int) x8, (int) y8) && h(this.f7266g)) {
                    z4 = true;
                    return this.f7274t.Q(motionEvent) || z4;
                }
            } else if (actionMasked == 2) {
                float x9 = motionEvent.getX();
                float y9 = motionEvent.getY();
                float abs = Math.abs(x9 - this.f7272n);
                float abs2 = Math.abs(y9 - this.f7273p);
                if (abs > this.f7274t.A() && abs2 > abs) {
                    this.f7274t.b();
                    this.f7270l = true;
                    return false;
                }
            }
            z4 = false;
            if (this.f7274t.Q(motionEvent)) {
                return true;
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        int i12;
        int i13;
        int i14;
        int i15;
        boolean i16 = i();
        w0.c cVar = this.f7274t;
        if (i16) {
            cVar.N(2);
        } else {
            cVar.N(1);
        }
        int i17 = i10 - i8;
        int paddingRight = i16 ? getPaddingRight() : getPaddingLeft();
        int paddingLeft = i16 ? getPaddingLeft() : getPaddingRight();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        if (this.f7276x) {
            this.f7267h = (this.f7265f && this.f7275w) ? 1.0f : 0.0f;
        }
        int i18 = paddingRight;
        for (int i19 = 0; i19 < childCount; i19++) {
            View childAt = getChildAt(i19);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                if (layoutParams.f7281b) {
                    int i20 = i17 - paddingLeft;
                    int min = (Math.min(paddingRight, i20 - this.f7264e) - i18) - (((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin);
                    this.f7269k = min;
                    int i21 = i16 ? ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin : ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                    layoutParams.f7282c = ((i18 + i21) + min) + (measuredWidth / 2) > i20;
                    int i22 = (int) (min * this.f7267h);
                    i18 += i21 + i22;
                    this.f7267h = i22 / min;
                    i12 = 0;
                } else if (!this.f7265f || (i13 = this.f7271m) == 0) {
                    i18 = paddingRight;
                    i12 = 0;
                } else {
                    i12 = (int) ((1.0f - this.f7267h) * i13);
                    i18 = paddingRight;
                }
                if (i16) {
                    i15 = (i17 - i18) + i12;
                    i14 = i15 - measuredWidth;
                } else {
                    i14 = i18 - i12;
                    i15 = i14 + measuredWidth;
                }
                childAt.layout(i14, paddingTop, i15, childAt.getMeasuredHeight() + paddingTop);
                paddingRight += childAt.getWidth();
            }
        }
        if (this.f7276x) {
            if (this.f7265f) {
                if (this.f7271m != 0) {
                    o(this.f7267h);
                }
                if (((LayoutParams) this.f7266g.getLayoutParams()).f7282c) {
                    c(this.f7266g, this.f7267h, this.f7260a);
                }
            } else {
                for (int i23 = 0; i23 < childCount; i23++) {
                    c(getChildAt(i23), 0.0f, this.f7260a);
                }
            }
            r(this.f7266g);
        }
        this.f7276x = false;
    }

    /* JADX WARN: Removed duplicated region for block: B:123:0x01d9  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x01ef  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0115  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onMeasure(int r21, int r22) {
        /*
            Method dump skipped, instructions count: 557
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slidingpanelayout.widget.SlidingPaneLayout.onMeasure(int, int):void");
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        if (savedState.f7284c) {
            m();
        } else {
            a();
        }
        this.f7275w = savedState.f7284c;
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f7284c = k() ? j() : this.f7275w;
        return savedState;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        super.onSizeChanged(i8, i9, i10, i11);
        if (i8 != i10) {
            this.f7276x = true;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.f7265f) {
            this.f7274t.G(motionEvent);
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                float x8 = motionEvent.getX();
                float y8 = motionEvent.getY();
                this.f7272n = x8;
                this.f7273p = y8;
            } else if (actionMasked == 1 && h(this.f7266g)) {
                float x9 = motionEvent.getX();
                float y9 = motionEvent.getY();
                float f5 = x9 - this.f7272n;
                float f8 = y9 - this.f7273p;
                int A = this.f7274t.A();
                if ((f5 * f5) + (f8 * f8) < A * A && this.f7274t.F(this.f7266g, (int) x9, (int) y9)) {
                    b(this.f7266g, 0);
                }
            }
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    void p() {
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            if (childAt.getVisibility() == 4) {
                childAt.setVisibility(0);
            }
        }
    }

    boolean q(float f5, int i8) {
        int paddingLeft;
        if (this.f7265f) {
            boolean i9 = i();
            LayoutParams layoutParams = (LayoutParams) this.f7266g.getLayoutParams();
            if (i9) {
                paddingLeft = (int) (getWidth() - (((getPaddingRight() + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin) + (f5 * this.f7269k)) + this.f7266g.getWidth()));
            } else {
                paddingLeft = (int) (getPaddingLeft() + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + (f5 * this.f7269k));
            }
            w0.c cVar = this.f7274t;
            View view = this.f7266g;
            if (cVar.R(view, paddingLeft, view.getTop())) {
                p();
                c0.j0(this);
                return true;
            }
            return false;
        }
        return false;
    }

    void r(View view) {
        int i8;
        int i9;
        int i10;
        int i11;
        View childAt;
        boolean z4;
        View view2 = view;
        boolean i12 = i();
        int width = i12 ? getWidth() - getPaddingRight() : getPaddingLeft();
        int paddingLeft = i12 ? getPaddingLeft() : getWidth() - getPaddingRight();
        int paddingTop = getPaddingTop();
        int height = getHeight() - getPaddingBottom();
        if (view2 == null || !s(view)) {
            i8 = 0;
            i9 = 0;
            i10 = 0;
            i11 = 0;
        } else {
            i8 = view.getLeft();
            i9 = view.getRight();
            i10 = view.getTop();
            i11 = view.getBottom();
        }
        int childCount = getChildCount();
        int i13 = 0;
        while (i13 < childCount && (childAt = getChildAt(i13)) != view2) {
            if (childAt.getVisibility() == 8) {
                z4 = i12;
            } else {
                z4 = i12;
                childAt.setVisibility((Math.max(i12 ? paddingLeft : width, childAt.getLeft()) < i8 || Math.max(paddingTop, childAt.getTop()) < i10 || Math.min(i12 ? width : paddingLeft, childAt.getRight()) > i9 || Math.min(height, childAt.getBottom()) > i11) ? 0 : 4);
            }
            i13++;
            view2 = view;
            i12 = z4;
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        super.requestChildFocus(view, view2);
        if (isInTouchMode() || this.f7265f) {
            return;
        }
        this.f7275w = view == this.f7266g;
    }

    public void setCoveredFadeColor(int i8) {
        this.f7261b = i8;
    }

    public void setPanelSlideListener(d dVar) {
        this.q = dVar;
    }

    public void setParallaxDistance(int i8) {
        this.f7271m = i8;
        requestLayout();
    }

    @Deprecated
    public void setShadowDrawable(Drawable drawable) {
        setShadowDrawableLeft(drawable);
    }

    public void setShadowDrawableLeft(Drawable drawable) {
        this.f7262c = drawable;
    }

    public void setShadowDrawableRight(Drawable drawable) {
        this.f7263d = drawable;
    }

    @Deprecated
    public void setShadowResource(int i8) {
        setShadowDrawable(getResources().getDrawable(i8));
    }

    public void setShadowResourceLeft(int i8) {
        setShadowDrawableLeft(androidx.core.content.a.f(getContext(), i8));
    }

    public void setShadowResourceRight(int i8) {
        setShadowDrawableRight(androidx.core.content.a.f(getContext(), i8));
    }

    public void setSliderFadeColor(int i8) {
        this.f7260a = i8;
    }
}
