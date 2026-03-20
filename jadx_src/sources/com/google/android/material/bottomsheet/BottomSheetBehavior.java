package com.google.android.material.bottomsheet;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.accessibility.c;
import androidx.core.view.accessibility.f;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.internal.s;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import k7.j;
import k7.k;
import k7.l;
import w0.c;
import x7.m;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class BottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    private static final int Y = k.f21239j;
    float A;
    int B;
    float C;
    boolean D;
    private boolean E;
    private boolean F;
    int G;
    w0.c H;
    private boolean I;
    private int J;
    private boolean K;
    private int L;
    int M;
    int N;
    WeakReference<V> O;
    WeakReference<View> P;
    private final ArrayList<g> Q;
    private VelocityTracker R;
    int S;
    private int T;
    boolean U;
    private Map<View, Integer> V;
    private int W;
    private final c.AbstractC0221c X;

    /* renamed from: a  reason: collision with root package name */
    private int f17525a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f17526b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f17527c;

    /* renamed from: d  reason: collision with root package name */
    private float f17528d;

    /* renamed from: e  reason: collision with root package name */
    private int f17529e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f17530f;

    /* renamed from: g  reason: collision with root package name */
    private int f17531g;

    /* renamed from: h  reason: collision with root package name */
    private int f17532h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f17533i;

    /* renamed from: j  reason: collision with root package name */
    private x7.h f17534j;

    /* renamed from: k  reason: collision with root package name */
    private int f17535k;

    /* renamed from: l  reason: collision with root package name */
    private int f17536l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f17537m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f17538n;

    /* renamed from: o  reason: collision with root package name */
    private boolean f17539o;

    /* renamed from: p  reason: collision with root package name */
    private boolean f17540p;
    private boolean q;

    /* renamed from: r  reason: collision with root package name */
    private int f17541r;

    /* renamed from: s  reason: collision with root package name */
    private int f17542s;

    /* renamed from: t  reason: collision with root package name */
    private m f17543t;

    /* renamed from: u  reason: collision with root package name */
    private boolean f17544u;

    /* renamed from: v  reason: collision with root package name */
    private BottomSheetBehavior<V>.h f17545v;

    /* renamed from: w  reason: collision with root package name */
    private ValueAnimator f17546w;

    /* renamed from: x  reason: collision with root package name */
    int f17547x;

    /* renamed from: y  reason: collision with root package name */
    int f17548y;

    /* renamed from: z  reason: collision with root package name */
    int f17549z;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        final int f17550c;

        /* renamed from: d  reason: collision with root package name */
        int f17551d;

        /* renamed from: e  reason: collision with root package name */
        boolean f17552e;

        /* renamed from: f  reason: collision with root package name */
        boolean f17553f;

        /* renamed from: g  reason: collision with root package name */
        boolean f17554g;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, (ClassLoader) null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f17550c = parcel.readInt();
            this.f17551d = parcel.readInt();
            this.f17552e = parcel.readInt() == 1;
            this.f17553f = parcel.readInt() == 1;
            this.f17554g = parcel.readInt() == 1;
        }

        public SavedState(Parcelable parcelable, BottomSheetBehavior<?> bottomSheetBehavior) {
            super(parcelable);
            this.f17550c = bottomSheetBehavior.G;
            this.f17551d = ((BottomSheetBehavior) bottomSheetBehavior).f17529e;
            this.f17552e = ((BottomSheetBehavior) bottomSheetBehavior).f17526b;
            this.f17553f = bottomSheetBehavior.D;
            this.f17554g = ((BottomSheetBehavior) bottomSheetBehavior).E;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeInt(this.f17550c);
            parcel.writeInt(this.f17551d);
            parcel.writeInt(this.f17552e ? 1 : 0);
            parcel.writeInt(this.f17553f ? 1 : 0);
            parcel.writeInt(this.f17554g ? 1 : 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f17555a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ ViewGroup.LayoutParams f17556b;

        a(View view, ViewGroup.LayoutParams layoutParams) {
            this.f17555a = view;
            this.f17556b = layoutParams;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f17555a.setLayoutParams(this.f17556b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f17558a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ int f17559b;

        b(View view, int i8) {
            this.f17558a = view;
            this.f17559b = i8;
        }

        @Override // java.lang.Runnable
        public void run() {
            BottomSheetBehavior.this.B0(this.f17558a, this.f17559b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements ValueAnimator.AnimatorUpdateListener {
        c() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (BottomSheetBehavior.this.f17534j != null) {
                BottomSheetBehavior.this.f17534j.b0(floatValue);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements s.e {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ boolean f17562a;

        d(boolean z4) {
            this.f17562a = z4;
        }

        @Override // com.google.android.material.internal.s.e
        public m0 a(View view, m0 m0Var, s.f fVar) {
            BottomSheetBehavior.this.f17542s = m0Var.m();
            boolean h8 = s.h(view);
            int paddingBottom = view.getPaddingBottom();
            int paddingLeft = view.getPaddingLeft();
            int paddingRight = view.getPaddingRight();
            if (BottomSheetBehavior.this.f17538n) {
                BottomSheetBehavior.this.f17541r = m0Var.j();
                paddingBottom = fVar.f18169d + BottomSheetBehavior.this.f17541r;
            }
            if (BottomSheetBehavior.this.f17539o) {
                paddingLeft = (h8 ? fVar.f18168c : fVar.f18166a) + m0Var.k();
            }
            if (BottomSheetBehavior.this.f17540p) {
                paddingRight = (h8 ? fVar.f18166a : fVar.f18168c) + m0Var.l();
            }
            view.setPadding(paddingLeft, view.getPaddingTop(), paddingRight, paddingBottom);
            if (this.f17562a) {
                BottomSheetBehavior.this.f17536l = m0Var.g().f4711d;
            }
            if (BottomSheetBehavior.this.f17538n || this.f17562a) {
                BottomSheetBehavior.this.I0(false);
            }
            return m0Var;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e extends c.AbstractC0221c {
        e() {
        }

        private boolean n(View view) {
            int top = view.getTop();
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
            return top > (bottomSheetBehavior.N + bottomSheetBehavior.d0()) / 2;
        }

        @Override // w0.c.AbstractC0221c
        public int a(View view, int i8, int i9) {
            return view.getLeft();
        }

        @Override // w0.c.AbstractC0221c
        public int b(View view, int i8, int i9) {
            int d02 = BottomSheetBehavior.this.d0();
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
            return t0.a.c(i8, d02, bottomSheetBehavior.D ? bottomSheetBehavior.N : bottomSheetBehavior.B);
        }

        @Override // w0.c.AbstractC0221c
        public int e(View view) {
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
            return bottomSheetBehavior.D ? bottomSheetBehavior.N : bottomSheetBehavior.B;
        }

        @Override // w0.c.AbstractC0221c
        public void j(int i8) {
            if (i8 == 1 && BottomSheetBehavior.this.F) {
                BottomSheetBehavior.this.z0(1);
            }
        }

        @Override // w0.c.AbstractC0221c
        public void k(View view, int i8, int i9, int i10, int i11) {
            BottomSheetBehavior.this.a0(i9);
        }

        /* JADX WARN: Code restructure failed: missing block: B:27:0x0079, code lost:
            if (java.lang.Math.abs(r7.getTop() - r6.f17564a.d0()) < java.lang.Math.abs(r7.getTop() - r6.f17564a.f17549z)) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:0x007b, code lost:
            r8 = r6.f17564a.d0();
         */
        /* JADX WARN: Code restructure failed: missing block: B:38:0x00b7, code lost:
            if (java.lang.Math.abs(r8 - r6.f17564a.f17549z) < java.lang.Math.abs(r8 - r6.f17564a.B)) goto L31;
         */
        /* JADX WARN: Code restructure failed: missing block: B:44:0x00de, code lost:
            if (java.lang.Math.abs(r8 - r6.f17564a.f17548y) < java.lang.Math.abs(r8 - r6.f17564a.B)) goto L5;
         */
        /* JADX WARN: Code restructure failed: missing block: B:49:0x00f0, code lost:
            if (r8 < java.lang.Math.abs(r8 - r9.B)) goto L30;
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x0102, code lost:
            if (java.lang.Math.abs(r8 - r0) < java.lang.Math.abs(r8 - r6.f17564a.B)) goto L31;
         */
        @Override // w0.c.AbstractC0221c
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void l(android.view.View r7, float r8, float r9) {
            /*
                Method dump skipped, instructions count: 268
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.bottomsheet.BottomSheetBehavior.e.l(android.view.View, float, float):void");
        }

        @Override // w0.c.AbstractC0221c
        public boolean m(View view, int i8) {
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.this;
            int i9 = bottomSheetBehavior.G;
            if (i9 == 1 || bottomSheetBehavior.U) {
                return false;
            }
            if (i9 == 3 && bottomSheetBehavior.S == i8) {
                WeakReference<View> weakReference = bottomSheetBehavior.P;
                View view2 = weakReference != null ? weakReference.get() : null;
                if (view2 != null && view2.canScrollVertically(-1)) {
                    return false;
                }
            }
            WeakReference<V> weakReference2 = BottomSheetBehavior.this.O;
            return weakReference2 != null && weakReference2.get() == view;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f implements androidx.core.view.accessibility.f {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f17565a;

        f(int i8) {
            this.f17565a = i8;
        }

        @Override // androidx.core.view.accessibility.f
        public boolean a(View view, f.a aVar) {
            BottomSheetBehavior.this.y0(this.f17565a);
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class g {
        public abstract void a(View view, float f5);

        public abstract void b(View view, int i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private final View f17567a;

        /* renamed from: b  reason: collision with root package name */
        private boolean f17568b;

        /* renamed from: c  reason: collision with root package name */
        int f17569c;

        h(View view, int i8) {
            this.f17567a = view;
            this.f17569c = i8;
        }

        @Override // java.lang.Runnable
        public void run() {
            w0.c cVar = BottomSheetBehavior.this.H;
            if (cVar == null || !cVar.n(true)) {
                BottomSheetBehavior.this.z0(this.f17569c);
            } else {
                c0.l0(this.f17567a, this);
            }
            this.f17568b = false;
        }
    }

    public BottomSheetBehavior() {
        this.f17525a = 0;
        this.f17526b = true;
        this.f17527c = false;
        this.f17535k = -1;
        this.f17545v = null;
        this.A = 0.5f;
        this.C = -1.0f;
        this.F = true;
        this.G = 4;
        this.Q = new ArrayList<>();
        this.W = -1;
        this.X = new e();
    }

    public BottomSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int i8;
        this.f17525a = 0;
        this.f17526b = true;
        this.f17527c = false;
        this.f17535k = -1;
        this.f17545v = null;
        this.A = 0.5f;
        this.C = -1.0f;
        this.F = true;
        this.G = 4;
        this.Q = new ArrayList<>();
        this.W = -1;
        this.X = new e();
        this.f17532h = context.getResources().getDimensionPixelSize(k7.d.f21101f0);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, l.f21321h0);
        this.f17533i = obtainStyledAttributes.hasValue(l.f21471y0);
        int i9 = l.f21348k0;
        boolean hasValue = obtainStyledAttributes.hasValue(i9);
        if (hasValue) {
            Y(context, attributeSet, hasValue, u7.c.a(context, obtainStyledAttributes, i9));
        } else {
            X(context, attributeSet, hasValue);
        }
        Z();
        if (Build.VERSION.SDK_INT >= 21) {
            this.C = obtainStyledAttributes.getDimension(l.f21339j0, -1.0f);
        }
        int i10 = l.f21330i0;
        if (obtainStyledAttributes.hasValue(i10)) {
            t0(obtainStyledAttributes.getDimensionPixelSize(i10, -1));
        }
        int i11 = l.f21400q0;
        TypedValue peekValue = obtainStyledAttributes.peekValue(i11);
        if (peekValue == null || (i8 = peekValue.data) != -1) {
            u0(obtainStyledAttributes.getDimensionPixelSize(i11, -1));
        } else {
            u0(i8);
        }
        s0(obtainStyledAttributes.getBoolean(l.f21392p0, false));
        q0(obtainStyledAttributes.getBoolean(l.f21426t0, false));
        p0(obtainStyledAttributes.getBoolean(l.f21375n0, true));
        x0(obtainStyledAttributes.getBoolean(l.f21417s0, false));
        n0(obtainStyledAttributes.getBoolean(l.f21357l0, true));
        w0(obtainStyledAttributes.getInt(l.f21409r0, 0));
        r0(obtainStyledAttributes.getFloat(l.f21384o0, 0.5f));
        int i12 = l.f21366m0;
        TypedValue peekValue2 = obtainStyledAttributes.peekValue(i12);
        o0((peekValue2 == null || peekValue2.type != 16) ? obtainStyledAttributes.getDimensionPixelOffset(i12, 0) : peekValue2.data);
        this.f17538n = obtainStyledAttributes.getBoolean(l.f21435u0, false);
        this.f17539o = obtainStyledAttributes.getBoolean(l.f21444v0, false);
        this.f17540p = obtainStyledAttributes.getBoolean(l.f21453w0, false);
        this.q = obtainStyledAttributes.getBoolean(l.f21462x0, true);
        obtainStyledAttributes.recycle();
        this.f17528d = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }

    private void A0(View view) {
        boolean z4 = (Build.VERSION.SDK_INT < 29 || h0() || this.f17530f) ? false : true;
        if (this.f17538n || this.f17539o || this.f17540p || z4) {
            s.b(view, new d(z4));
        }
    }

    private void C0(int i8) {
        V v8 = this.O.get();
        if (v8 == null) {
            return;
        }
        ViewParent parent = v8.getParent();
        if (parent != null && parent.isLayoutRequested() && c0.V(v8)) {
            v8.post(new b(v8, i8));
        } else {
            B0(v8, i8);
        }
    }

    private void F0() {
        V v8;
        int i8;
        c.a aVar;
        WeakReference<V> weakReference = this.O;
        if (weakReference == null || (v8 = weakReference.get()) == null) {
            return;
        }
        c0.n0(v8, 524288);
        c0.n0(v8, 262144);
        c0.n0(v8, 1048576);
        int i9 = this.W;
        if (i9 != -1) {
            c0.n0(v8, i9);
        }
        if (!this.f17526b && this.G != 6) {
            this.W = R(v8, j.f21205a, 6);
        }
        if (this.D && this.G != 5) {
            k0(v8, c.a.f4926y, 5);
        }
        int i10 = this.G;
        if (i10 == 3) {
            i8 = this.f17526b ? 4 : 6;
            aVar = c.a.f4925x;
        } else if (i10 != 4) {
            if (i10 != 6) {
                return;
            }
            k0(v8, c.a.f4925x, 4);
            k0(v8, c.a.f4924w, 3);
            return;
        } else {
            i8 = this.f17526b ? 3 : 6;
            aVar = c.a.f4924w;
        }
        k0(v8, aVar, i8);
    }

    private void G0(int i8) {
        ValueAnimator valueAnimator;
        if (i8 == 2) {
            return;
        }
        boolean z4 = i8 == 3;
        if (this.f17544u != z4) {
            this.f17544u = z4;
            if (this.f17534j == null || (valueAnimator = this.f17546w) == null) {
                return;
            }
            if (valueAnimator.isRunning()) {
                this.f17546w.reverse();
                return;
            }
            float f5 = z4 ? 0.0f : 1.0f;
            this.f17546w.setFloatValues(1.0f - f5, f5);
            this.f17546w.start();
        }
    }

    private void H0(boolean z4) {
        Map<View, Integer> map;
        int intValue;
        WeakReference<V> weakReference = this.O;
        if (weakReference == null) {
            return;
        }
        ViewParent parent = weakReference.get().getParent();
        if (parent instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) parent;
            int childCount = coordinatorLayout.getChildCount();
            if (Build.VERSION.SDK_INT >= 16 && z4) {
                if (this.V != null) {
                    return;
                }
                this.V = new HashMap(childCount);
            }
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = coordinatorLayout.getChildAt(i8);
                if (childAt != this.O.get()) {
                    if (z4) {
                        if (Build.VERSION.SDK_INT >= 16) {
                            this.V.put(childAt, Integer.valueOf(childAt.getImportantForAccessibility()));
                        }
                        if (this.f17527c) {
                            intValue = 4;
                            c0.E0(childAt, intValue);
                        }
                    } else if (this.f17527c && (map = this.V) != null && map.containsKey(childAt)) {
                        intValue = this.V.get(childAt).intValue();
                        c0.E0(childAt, intValue);
                    }
                }
            }
            if (!z4) {
                this.V = null;
            } else if (this.f17527c) {
                this.O.get().sendAccessibilityEvent(8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void I0(boolean z4) {
        V v8;
        if (this.O != null) {
            T();
            if (this.G != 4 || (v8 = this.O.get()) == null) {
                return;
            }
            if (z4) {
                C0(this.G);
            } else {
                v8.requestLayout();
            }
        }
    }

    private int R(V v8, int i8, int i9) {
        return c0.c(v8, v8.getResources().getString(i8), W(i9));
    }

    private void T() {
        int V = V();
        if (this.f17526b) {
            this.B = Math.max(this.N - V, this.f17548y);
        } else {
            this.B = this.N - V;
        }
    }

    private void U() {
        this.f17549z = (int) (this.N * (1.0f - this.A));
    }

    private int V() {
        int i8;
        return this.f17530f ? Math.min(Math.max(this.f17531g, this.N - ((this.M * 9) / 16)), this.L) + this.f17541r : (this.f17537m || this.f17538n || (i8 = this.f17536l) <= 0) ? this.f17529e + this.f17541r : Math.max(this.f17529e, i8 + this.f17532h);
    }

    private androidx.core.view.accessibility.f W(int i8) {
        return new f(i8);
    }

    private void X(Context context, AttributeSet attributeSet, boolean z4) {
        Y(context, attributeSet, z4, null);
    }

    private void Y(Context context, AttributeSet attributeSet, boolean z4, ColorStateList colorStateList) {
        if (this.f17533i) {
            this.f17543t = m.e(context, attributeSet, k7.b.f21055g, Y).m();
            x7.h hVar = new x7.h(this.f17543t);
            this.f17534j = hVar;
            hVar.P(context);
            if (z4 && colorStateList != null) {
                this.f17534j.a0(colorStateList);
                return;
            }
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(16842801, typedValue, true);
            this.f17534j.setTint(typedValue.data);
        }
    }

    private void Z() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.f17546w = ofFloat;
        ofFloat.setDuration(500L);
        this.f17546w.addUpdateListener(new c());
    }

    public static <V extends View> BottomSheetBehavior<V> c0(V v8) {
        ViewGroup.LayoutParams layoutParams = v8.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.e) {
            CoordinatorLayout.Behavior f5 = ((CoordinatorLayout.e) layoutParams).f();
            if (f5 instanceof BottomSheetBehavior) {
                return (BottomSheetBehavior) f5;
            }
            throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
        }
        throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
    }

    private float g0() {
        VelocityTracker velocityTracker = this.R;
        if (velocityTracker == null) {
            return 0.0f;
        }
        velocityTracker.computeCurrentVelocity(1000, this.f17528d);
        return this.R.getYVelocity(this.S);
    }

    private void k0(V v8, c.a aVar, int i8) {
        c0.p0(v8, aVar, null, W(i8));
    }

    private void l0() {
        this.S = -1;
        VelocityTracker velocityTracker = this.R;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.R = null;
        }
    }

    private void m0(SavedState savedState) {
        int i8 = this.f17525a;
        if (i8 == 0) {
            return;
        }
        if (i8 == -1 || (i8 & 1) == 1) {
            this.f17529e = savedState.f17551d;
        }
        if (i8 == -1 || (i8 & 2) == 2) {
            this.f17526b = savedState.f17552e;
        }
        if (i8 == -1 || (i8 & 4) == 4) {
            this.D = savedState.f17553f;
        }
        if (i8 == -1 || (i8 & 8) == 8) {
            this.E = savedState.f17554g;
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean A(CoordinatorLayout coordinatorLayout, V v8, View view, View view2, int i8, int i9) {
        this.J = 0;
        this.K = false;
        return (i8 & 2) != 0;
    }

    void B0(View view, int i8) {
        int i9;
        int i10;
        if (i8 == 4) {
            i9 = this.B;
        } else if (i8 == 6) {
            int i11 = this.f17549z;
            if (!this.f17526b || i11 > (i10 = this.f17548y)) {
                i9 = i11;
            } else {
                i8 = 3;
                i9 = i10;
            }
        } else if (i8 == 3) {
            i9 = d0();
        } else if (!this.D || i8 != 5) {
            throw new IllegalArgumentException("Illegal state argument: " + i8);
        } else {
            i9 = this.N;
        }
        E0(view, i8, i9, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x006b, code lost:
        if (java.lang.Math.abs(r3 - r2.f17548y) < java.lang.Math.abs(r3 - r2.B)) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x007a, code lost:
        if (r3 < java.lang.Math.abs(r3 - r2.B)) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x008a, code lost:
        if (java.lang.Math.abs(r3 - r1) < java.lang.Math.abs(r3 - r2.B)) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00a8, code lost:
        if (java.lang.Math.abs(r3 - r2.f17549z) < java.lang.Math.abs(r3 - r2.B)) goto L40;
     */
    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void C(androidx.coordinatorlayout.widget.CoordinatorLayout r3, V r4, android.view.View r5, int r6) {
        /*
            r2 = this;
            int r3 = r4.getTop()
            int r6 = r2.d0()
            r0 = 3
            if (r3 != r6) goto Lf
            r2.z0(r0)
            return
        Lf:
            java.lang.ref.WeakReference<android.view.View> r3 = r2.P
            if (r3 == 0) goto Lb3
            java.lang.Object r3 = r3.get()
            if (r5 != r3) goto Lb3
            boolean r3 = r2.K
            if (r3 != 0) goto L1f
            goto Lb3
        L1f:
            int r3 = r2.J
            r5 = 4
            r6 = 6
            if (r3 <= 0) goto L3e
            boolean r3 = r2.f17526b
            if (r3 == 0) goto L2d
        L29:
            int r3 = r2.f17548y
            goto Lad
        L2d:
            int r3 = r4.getTop()
            int r5 = r2.f17549z
            if (r3 <= r5) goto L38
            r3 = r5
            goto Lac
        L38:
            int r3 = r2.d0()
            goto Lad
        L3e:
            boolean r3 = r2.D
            if (r3 == 0) goto L50
            float r3 = r2.g0()
            boolean r3 = r2.D0(r4, r3)
            if (r3 == 0) goto L50
            int r3 = r2.N
            r0 = 5
            goto Lad
        L50:
            int r3 = r2.J
            if (r3 != 0) goto L8d
            int r3 = r4.getTop()
            boolean r1 = r2.f17526b
            if (r1 == 0) goto L6e
            int r6 = r2.f17548y
            int r6 = r3 - r6
            int r6 = java.lang.Math.abs(r6)
            int r1 = r2.B
            int r3 = r3 - r1
            int r3 = java.lang.Math.abs(r3)
            if (r6 >= r3) goto L91
            goto L29
        L6e:
            int r1 = r2.f17549z
            if (r3 >= r1) goto L7d
            int r5 = r2.B
            int r5 = r3 - r5
            int r5 = java.lang.Math.abs(r5)
            if (r3 >= r5) goto Laa
            goto L38
        L7d:
            int r0 = r3 - r1
            int r0 = java.lang.Math.abs(r0)
            int r1 = r2.B
            int r3 = r3 - r1
            int r3 = java.lang.Math.abs(r3)
            if (r0 >= r3) goto L91
            goto Laa
        L8d:
            boolean r3 = r2.f17526b
            if (r3 == 0) goto L95
        L91:
            int r3 = r2.B
            r0 = r5
            goto Lad
        L95:
            int r3 = r4.getTop()
            int r0 = r2.f17549z
            int r0 = r3 - r0
            int r0 = java.lang.Math.abs(r0)
            int r1 = r2.B
            int r3 = r3 - r1
            int r3 = java.lang.Math.abs(r3)
            if (r0 >= r3) goto L91
        Laa:
            int r3 = r2.f17549z
        Lac:
            r0 = r6
        Lad:
            r5 = 0
            r2.E0(r4, r0, r3, r5)
            r2.K = r5
        Lb3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.bottomsheet.BottomSheetBehavior.C(androidx.coordinatorlayout.widget.CoordinatorLayout, android.view.View, android.view.View, int):void");
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean D(CoordinatorLayout coordinatorLayout, V v8, MotionEvent motionEvent) {
        if (v8.isShown()) {
            int actionMasked = motionEvent.getActionMasked();
            if (this.G == 1 && actionMasked == 0) {
                return true;
            }
            w0.c cVar = this.H;
            if (cVar != null) {
                cVar.G(motionEvent);
            }
            if (actionMasked == 0) {
                l0();
            }
            if (this.R == null) {
                this.R = VelocityTracker.obtain();
            }
            this.R.addMovement(motionEvent);
            if (this.H != null && actionMasked == 2 && !this.I && Math.abs(this.T - motionEvent.getY()) > this.H.A()) {
                this.H.c(v8, motionEvent.getPointerId(motionEvent.getActionIndex()));
            }
            return !this.I;
        }
        return false;
    }

    boolean D0(View view, float f5) {
        if (this.E) {
            return true;
        }
        if (view.getTop() < this.B) {
            return false;
        }
        return Math.abs((((float) view.getTop()) + (f5 * 0.1f)) - ((float) this.B)) / ((float) V()) > 0.5f;
    }

    void E0(View view, int i8, int i9, boolean z4) {
        w0.c cVar = this.H;
        if (!(cVar != null && (!z4 ? !cVar.R(view, view.getLeft(), i9) : !cVar.P(view.getLeft(), i9)))) {
            z0(i8);
            return;
        }
        z0(2);
        G0(i8);
        if (this.f17545v == null) {
            this.f17545v = new h(view, i8);
        }
        if (((h) this.f17545v).f17568b) {
            this.f17545v.f17569c = i8;
            return;
        }
        BottomSheetBehavior<V>.h hVar = this.f17545v;
        hVar.f17569c = i8;
        c0.l0(view, hVar);
        ((h) this.f17545v).f17568b = true;
    }

    public void S(g gVar) {
        if (this.Q.contains(gVar)) {
            return;
        }
        this.Q.add(gVar);
    }

    void a0(int i8) {
        float f5;
        float f8;
        V v8 = this.O.get();
        if (v8 == null || this.Q.isEmpty()) {
            return;
        }
        int i9 = this.B;
        if (i8 > i9 || i9 == d0()) {
            int i10 = this.B;
            f5 = i10 - i8;
            f8 = this.N - i10;
        } else {
            int i11 = this.B;
            f5 = i11 - i8;
            f8 = i11 - d0();
        }
        float f9 = f5 / f8;
        for (int i12 = 0; i12 < this.Q.size(); i12++) {
            this.Q.get(i12).a(v8, f9);
        }
    }

    View b0(View view) {
        if (c0.X(view)) {
            return view;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                View b02 = b0(viewGroup.getChildAt(i8));
                if (b02 != null) {
                    return b02;
                }
            }
            return null;
        }
        return null;
    }

    public int d0() {
        if (this.f17526b) {
            return this.f17548y;
        }
        return Math.max(this.f17547x, this.q ? 0 : this.f17542s);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public x7.h e0() {
        return this.f17534j;
    }

    public int f0() {
        return this.G;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void g(CoordinatorLayout.e eVar) {
        super.g(eVar);
        this.O = null;
        this.H = null;
    }

    public boolean h0() {
        return this.f17537m;
    }

    public boolean i0() {
        return this.D;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void j() {
        super.j();
        this.O = null;
        this.H = null;
    }

    public void j0(g gVar) {
        this.Q.remove(gVar);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean k(CoordinatorLayout coordinatorLayout, V v8, MotionEvent motionEvent) {
        w0.c cVar;
        if (!v8.isShown() || !this.F) {
            this.I = true;
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            l0();
        }
        if (this.R == null) {
            this.R = VelocityTracker.obtain();
        }
        this.R.addMovement(motionEvent);
        if (actionMasked == 0) {
            int x8 = (int) motionEvent.getX();
            this.T = (int) motionEvent.getY();
            if (this.G != 2) {
                WeakReference<View> weakReference = this.P;
                View view = weakReference != null ? weakReference.get() : null;
                if (view != null && coordinatorLayout.F(view, x8, this.T)) {
                    this.S = motionEvent.getPointerId(motionEvent.getActionIndex());
                    this.U = true;
                }
            }
            this.I = this.S == -1 && !coordinatorLayout.F(v8, x8, this.T);
        } else if (actionMasked == 1 || actionMasked == 3) {
            this.U = false;
            this.S = -1;
            if (this.I) {
                this.I = false;
                return false;
            }
        }
        if (this.I || (cVar = this.H) == null || !cVar.Q(motionEvent)) {
            WeakReference<View> weakReference2 = this.P;
            View view2 = weakReference2 != null ? weakReference2.get() : null;
            return (actionMasked != 2 || view2 == null || this.I || this.G == 1 || coordinatorLayout.F(view2, (int) motionEvent.getX(), (int) motionEvent.getY()) || this.H == null || Math.abs(((float) this.T) - motionEvent.getY()) <= ((float) this.H.A())) ? false : true;
        }
        return true;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean l(CoordinatorLayout coordinatorLayout, V v8, int i8) {
        int i9;
        x7.h hVar;
        if (c0.B(coordinatorLayout) && !c0.B(v8)) {
            v8.setFitsSystemWindows(true);
        }
        if (this.O == null) {
            this.f17531g = coordinatorLayout.getResources().getDimensionPixelSize(k7.d.f21106i);
            A0(v8);
            this.O = new WeakReference<>(v8);
            if (this.f17533i && (hVar = this.f17534j) != null) {
                c0.x0(v8, hVar);
            }
            x7.h hVar2 = this.f17534j;
            if (hVar2 != null) {
                float f5 = this.C;
                if (f5 == -1.0f) {
                    f5 = c0.y(v8);
                }
                hVar2.Z(f5);
                boolean z4 = this.G == 3;
                this.f17544u = z4;
                this.f17534j.b0(z4 ? 0.0f : 1.0f);
            }
            F0();
            if (c0.C(v8) == 0) {
                c0.E0(v8, 1);
            }
            int measuredWidth = v8.getMeasuredWidth();
            int i10 = this.f17535k;
            if (measuredWidth > i10 && i10 != -1) {
                ViewGroup.LayoutParams layoutParams = v8.getLayoutParams();
                layoutParams.width = this.f17535k;
                v8.post(new a(v8, layoutParams));
            }
        }
        if (this.H == null) {
            this.H = w0.c.p(coordinatorLayout, this.X);
        }
        int top = v8.getTop();
        coordinatorLayout.M(v8, i8);
        this.M = coordinatorLayout.getWidth();
        this.N = coordinatorLayout.getHeight();
        int height = v8.getHeight();
        this.L = height;
        int i11 = this.N;
        int i12 = i11 - height;
        int i13 = this.f17542s;
        if (i12 < i13) {
            if (this.q) {
                this.L = i11;
            } else {
                this.L = i11 - i13;
            }
        }
        this.f17548y = Math.max(0, i11 - this.L);
        U();
        T();
        int i14 = this.G;
        if (i14 == 3) {
            i9 = d0();
        } else if (i14 == 6) {
            i9 = this.f17549z;
        } else if (this.D && i14 == 5) {
            i9 = this.N;
        } else if (i14 != 4) {
            if (i14 == 1 || i14 == 2) {
                c0.d0(v8, top - v8.getTop());
            }
            this.P = new WeakReference<>(b0(v8));
            return true;
        } else {
            i9 = this.B;
        }
        c0.d0(v8, i9);
        this.P = new WeakReference<>(b0(v8));
        return true;
    }

    public void n0(boolean z4) {
        this.F = z4;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean o(CoordinatorLayout coordinatorLayout, V v8, View view, float f5, float f8) {
        WeakReference<View> weakReference = this.P;
        if (weakReference == null || view != weakReference.get()) {
            return false;
        }
        return this.G != 3 || super.o(coordinatorLayout, v8, view, f5, f8);
    }

    public void o0(int i8) {
        if (i8 < 0) {
            throw new IllegalArgumentException("offset must be greater than or equal to 0");
        }
        this.f17547x = i8;
    }

    public void p0(boolean z4) {
        if (this.f17526b == z4) {
            return;
        }
        this.f17526b = z4;
        if (this.O != null) {
            T();
        }
        z0((this.f17526b && this.G == 6) ? 3 : this.G);
        F0();
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void q(CoordinatorLayout coordinatorLayout, V v8, View view, int i8, int i9, int[] iArr, int i10) {
        int i11;
        if (i10 == 1) {
            return;
        }
        WeakReference<View> weakReference = this.P;
        if (view != (weakReference != null ? weakReference.get() : null)) {
            return;
        }
        int top = v8.getTop();
        int i12 = top - i9;
        if (i9 > 0) {
            if (i12 < d0()) {
                iArr[1] = top - d0();
                c0.d0(v8, -iArr[1]);
                i11 = 3;
                z0(i11);
            } else if (!this.F) {
                return;
            } else {
                iArr[1] = i9;
                c0.d0(v8, -i9);
                z0(1);
            }
        } else if (i9 < 0 && !view.canScrollVertically(-1)) {
            int i13 = this.B;
            if (i12 > i13 && !this.D) {
                iArr[1] = top - i13;
                c0.d0(v8, -iArr[1]);
                i11 = 4;
                z0(i11);
            } else if (!this.F) {
                return;
            } else {
                iArr[1] = i9;
                c0.d0(v8, -i9);
                z0(1);
            }
        }
        a0(v8.getTop());
        this.J = i9;
        this.K = true;
    }

    public void q0(boolean z4) {
        this.f17537m = z4;
    }

    public void r0(float f5) {
        if (f5 <= 0.0f || f5 >= 1.0f) {
            throw new IllegalArgumentException("ratio must be a float value between 0 and 1");
        }
        this.A = f5;
        if (this.O != null) {
            U();
        }
    }

    public void s0(boolean z4) {
        if (this.D != z4) {
            this.D = z4;
            if (!z4 && this.G == 5) {
                y0(4);
            }
            F0();
        }
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void t(CoordinatorLayout coordinatorLayout, V v8, View view, int i8, int i9, int i10, int i11, int i12, int[] iArr) {
    }

    public void t0(int i8) {
        this.f17535k = i8;
    }

    public void u0(int i8) {
        v0(i8, false);
    }

    public final void v0(int i8, boolean z4) {
        boolean z8 = true;
        if (i8 == -1) {
            if (!this.f17530f) {
                this.f17530f = true;
            }
            z8 = false;
        } else {
            if (this.f17530f || this.f17529e != i8) {
                this.f17530f = false;
                this.f17529e = Math.max(0, i8);
            }
            z8 = false;
        }
        if (z8) {
            I0(z4);
        }
    }

    public void w0(int i8) {
        this.f17525a = i8;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public void x(CoordinatorLayout coordinatorLayout, V v8, Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.x(coordinatorLayout, v8, savedState.a());
        m0(savedState);
        int i8 = savedState.f17550c;
        this.G = (i8 == 1 || i8 == 2) ? 4 : 4;
    }

    public void x0(boolean z4) {
        this.E = z4;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public Parcelable y(CoordinatorLayout coordinatorLayout, V v8) {
        return new SavedState(super.y(coordinatorLayout, v8), (BottomSheetBehavior<?>) this);
    }

    public void y0(int i8) {
        if (i8 == this.G) {
            return;
        }
        if (this.O != null) {
            C0(i8);
        } else if (i8 == 4 || i8 == 3 || i8 == 6 || (this.D && i8 == 5)) {
            this.G = i8;
        }
    }

    void z0(int i8) {
        V v8;
        if (this.G == i8) {
            return;
        }
        this.G = i8;
        WeakReference<V> weakReference = this.O;
        if (weakReference == null || (v8 = weakReference.get()) == null) {
            return;
        }
        if (i8 == 3) {
            H0(true);
        } else if (i8 == 6 || i8 == 5 || i8 == 4) {
            H0(false);
        }
        G0(i8);
        for (int i9 = 0; i9 < this.Q.size(); i9++) {
            this.Q.get(i9).b(v8, i8);
        }
        F0();
    }
}
