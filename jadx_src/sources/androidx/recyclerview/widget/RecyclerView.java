package androidx.recyclerview.widget;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Observable;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.FocusFinder;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.OverScroller;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import androidx.core.view.e0;
import androidx.customview.view.AbsSavedState;
import androidx.recyclerview.widget.a;
import androidx.recyclerview.widget.a0;
import androidx.recyclerview.widget.b0;
import androidx.recyclerview.widget.f;
import androidx.recyclerview.widget.k;
import androidx.recyclerview.widget.w;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class RecyclerView extends ViewGroup implements androidx.core.view.p {
    private static final int[] M0 = {16843830};
    static final boolean N0;
    static final boolean O0;
    static final boolean P0;
    static final boolean Q0;
    private static final boolean R0;
    private static final boolean S0;
    private static final Class<?>[] T0;
    static final Interpolator U0;
    boolean A;
    private l.b A0;
    private int B;
    boolean B0;
    boolean C;
    androidx.recyclerview.widget.w C0;
    private j D0;
    boolean E;
    private final int[] E0;
    private boolean F;
    private androidx.core.view.q F0;
    private int G;
    private final int[] G0;
    boolean H;
    private final int[] H0;
    final int[] I0;
    final List<b0> J0;
    private final AccessibilityManager K;
    private Runnable K0;
    private List<p> L;
    private final b0.b L0;
    boolean O;
    boolean P;
    private int Q;
    private int R;
    private k T;
    private EdgeEffect W;

    /* renamed from: a  reason: collision with root package name */
    private final w f6568a;

    /* renamed from: a0  reason: collision with root package name */
    private EdgeEffect f6569a0;

    /* renamed from: b  reason: collision with root package name */
    final u f6570b;

    /* renamed from: b0  reason: collision with root package name */
    private EdgeEffect f6571b0;

    /* renamed from: c  reason: collision with root package name */
    private SavedState f6572c;

    /* renamed from: c0  reason: collision with root package name */
    private EdgeEffect f6573c0;

    /* renamed from: d  reason: collision with root package name */
    androidx.recyclerview.widget.a f6574d;

    /* renamed from: d0  reason: collision with root package name */
    l f6575d0;

    /* renamed from: e  reason: collision with root package name */
    androidx.recyclerview.widget.f f6576e;

    /* renamed from: e0  reason: collision with root package name */
    private int f6577e0;

    /* renamed from: f  reason: collision with root package name */
    final androidx.recyclerview.widget.b0 f6578f;

    /* renamed from: f0  reason: collision with root package name */
    private int f6579f0;

    /* renamed from: g  reason: collision with root package name */
    boolean f6580g;

    /* renamed from: g0  reason: collision with root package name */
    private VelocityTracker f6581g0;

    /* renamed from: h  reason: collision with root package name */
    final Runnable f6582h;

    /* renamed from: h0  reason: collision with root package name */
    private int f6583h0;

    /* renamed from: i0  reason: collision with root package name */
    private int f6584i0;

    /* renamed from: j  reason: collision with root package name */
    final Rect f6585j;

    /* renamed from: j0  reason: collision with root package name */
    private int f6586j0;

    /* renamed from: k  reason: collision with root package name */
    private final Rect f6587k;

    /* renamed from: k0  reason: collision with root package name */
    private int f6588k0;

    /* renamed from: l  reason: collision with root package name */
    final RectF f6589l;

    /* renamed from: l0  reason: collision with root package name */
    private int f6590l0;

    /* renamed from: m  reason: collision with root package name */
    g f6591m;

    /* renamed from: m0  reason: collision with root package name */
    private q f6592m0;

    /* renamed from: n  reason: collision with root package name */
    o f6593n;

    /* renamed from: n0  reason: collision with root package name */
    private final int f6594n0;

    /* renamed from: o0  reason: collision with root package name */
    private final int f6595o0;

    /* renamed from: p  reason: collision with root package name */
    v f6596p;

    /* renamed from: p0  reason: collision with root package name */
    private float f6597p0;
    final ArrayList<n> q;

    /* renamed from: q0  reason: collision with root package name */
    private float f6598q0;

    /* renamed from: r0  reason: collision with root package name */
    private boolean f6599r0;

    /* renamed from: s0  reason: collision with root package name */
    final a0 f6600s0;

    /* renamed from: t  reason: collision with root package name */
    private final ArrayList<r> f6601t;

    /* renamed from: t0  reason: collision with root package name */
    androidx.recyclerview.widget.k f6602t0;

    /* renamed from: u0  reason: collision with root package name */
    k.b f6603u0;

    /* renamed from: v0  reason: collision with root package name */
    final y f6604v0;

    /* renamed from: w  reason: collision with root package name */
    private r f6605w;

    /* renamed from: w0  reason: collision with root package name */
    private s f6606w0;

    /* renamed from: x  reason: collision with root package name */
    boolean f6607x;

    /* renamed from: x0  reason: collision with root package name */
    private List<s> f6608x0;

    /* renamed from: y  reason: collision with root package name */
    boolean f6609y;

    /* renamed from: y0  reason: collision with root package name */
    boolean f6610y0;

    /* renamed from: z  reason: collision with root package name */
    boolean f6611z;

    /* renamed from: z0  reason: collision with root package name */
    boolean f6612z0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        /* renamed from: a  reason: collision with root package name */
        b0 f6613a;

        /* renamed from: b  reason: collision with root package name */
        final Rect f6614b;

        /* renamed from: c  reason: collision with root package name */
        boolean f6615c;

        /* renamed from: d  reason: collision with root package name */
        boolean f6616d;

        public LayoutParams(int i8, int i9) {
            super(i8, i9);
            this.f6614b = new Rect();
            this.f6615c = true;
            this.f6616d = false;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f6614b = new Rect();
            this.f6615c = true;
            this.f6616d = false;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f6614b = new Rect();
            this.f6615c = true;
            this.f6616d = false;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f6614b = new Rect();
            this.f6615c = true;
            this.f6616d = false;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.LayoutParams) layoutParams);
            this.f6614b = new Rect();
            this.f6615c = true;
            this.f6616d = false;
        }

        public int a() {
            return this.f6613a.m();
        }

        public boolean b() {
            return this.f6613a.y();
        }

        public boolean c() {
            return this.f6613a.v();
        }

        public boolean d() {
            return this.f6613a.t();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        Parcelable f6617c;

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
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.f6617c = parcel.readParcelable(classLoader == null ? o.class.getClassLoader() : classLoader);
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        void b(SavedState savedState) {
            this.f6617c = savedState.f6617c;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeParcelable(this.f6617c, 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            RecyclerView recyclerView = RecyclerView.this;
            if (!recyclerView.A || recyclerView.isLayoutRequested()) {
                return;
            }
            RecyclerView recyclerView2 = RecyclerView.this;
            if (!recyclerView2.f6607x) {
                recyclerView2.requestLayout();
            } else if (recyclerView2.E) {
                recyclerView2.C = true;
            } else {
                recyclerView2.v();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a0 implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private int f6619a;

        /* renamed from: b  reason: collision with root package name */
        private int f6620b;

        /* renamed from: c  reason: collision with root package name */
        OverScroller f6621c;

        /* renamed from: d  reason: collision with root package name */
        Interpolator f6622d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f6623e;

        /* renamed from: f  reason: collision with root package name */
        private boolean f6624f;

        a0() {
            Interpolator interpolator = RecyclerView.U0;
            this.f6622d = interpolator;
            this.f6623e = false;
            this.f6624f = false;
            this.f6621c = new OverScroller(RecyclerView.this.getContext(), interpolator);
        }

        private int a(int i8, int i9, int i10, int i11) {
            int i12;
            int abs = Math.abs(i8);
            int abs2 = Math.abs(i9);
            boolean z4 = abs > abs2;
            int sqrt = (int) Math.sqrt((i10 * i10) + (i11 * i11));
            int sqrt2 = (int) Math.sqrt((i8 * i8) + (i9 * i9));
            RecyclerView recyclerView = RecyclerView.this;
            int width = z4 ? recyclerView.getWidth() : recyclerView.getHeight();
            int i13 = width / 2;
            float f5 = width;
            float f8 = i13;
            float b9 = f8 + (b(Math.min(1.0f, (sqrt2 * 1.0f) / f5)) * f8);
            if (sqrt > 0) {
                i12 = Math.round(Math.abs(b9 / sqrt) * 1000.0f) * 4;
            } else {
                if (!z4) {
                    abs = abs2;
                }
                i12 = (int) (((abs / f5) + 1.0f) * 300.0f);
            }
            return Math.min(i12, 2000);
        }

        private float b(float f5) {
            return (float) Math.sin((f5 - 0.5f) * 0.47123894f);
        }

        private void d() {
            RecyclerView.this.removeCallbacks(this);
            c0.l0(RecyclerView.this, this);
        }

        public void c(int i8, int i9) {
            RecyclerView.this.setScrollState(2);
            this.f6620b = 0;
            this.f6619a = 0;
            Interpolator interpolator = this.f6622d;
            Interpolator interpolator2 = RecyclerView.U0;
            if (interpolator != interpolator2) {
                this.f6622d = interpolator2;
                this.f6621c = new OverScroller(RecyclerView.this.getContext(), interpolator2);
            }
            this.f6621c.fling(0, 0, i8, i9, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            e();
        }

        void e() {
            if (this.f6623e) {
                this.f6624f = true;
            } else {
                d();
            }
        }

        public void f(int i8, int i9, int i10, Interpolator interpolator) {
            if (i10 == Integer.MIN_VALUE) {
                i10 = a(i8, i9, 0, 0);
            }
            int i11 = i10;
            if (interpolator == null) {
                interpolator = RecyclerView.U0;
            }
            if (this.f6622d != interpolator) {
                this.f6622d = interpolator;
                this.f6621c = new OverScroller(RecyclerView.this.getContext(), interpolator);
            }
            this.f6620b = 0;
            this.f6619a = 0;
            RecyclerView.this.setScrollState(2);
            this.f6621c.startScroll(0, 0, i8, i9, i11);
            if (Build.VERSION.SDK_INT < 23) {
                this.f6621c.computeScrollOffset();
            }
            e();
        }

        public void g() {
            RecyclerView.this.removeCallbacks(this);
            this.f6621c.abortAnimation();
        }

        @Override // java.lang.Runnable
        public void run() {
            int i8;
            int i9;
            RecyclerView recyclerView = RecyclerView.this;
            if (recyclerView.f6593n == null) {
                g();
                return;
            }
            this.f6624f = false;
            this.f6623e = true;
            recyclerView.v();
            OverScroller overScroller = this.f6621c;
            if (overScroller.computeScrollOffset()) {
                int currX = overScroller.getCurrX();
                int currY = overScroller.getCurrY();
                int i10 = currX - this.f6619a;
                int i11 = currY - this.f6620b;
                this.f6619a = currX;
                this.f6620b = currY;
                RecyclerView recyclerView2 = RecyclerView.this;
                int[] iArr = recyclerView2.I0;
                iArr[0] = 0;
                iArr[1] = 0;
                if (recyclerView2.G(i10, i11, iArr, null, 1)) {
                    int[] iArr2 = RecyclerView.this.I0;
                    i10 -= iArr2[0];
                    i11 -= iArr2[1];
                }
                if (RecyclerView.this.getOverScrollMode() != 2) {
                    RecyclerView.this.u(i10, i11);
                }
                RecyclerView recyclerView3 = RecyclerView.this;
                if (recyclerView3.f6591m != null) {
                    int[] iArr3 = recyclerView3.I0;
                    iArr3[0] = 0;
                    iArr3[1] = 0;
                    recyclerView3.k1(i10, i11, iArr3);
                    RecyclerView recyclerView4 = RecyclerView.this;
                    int[] iArr4 = recyclerView4.I0;
                    i9 = iArr4[0];
                    i8 = iArr4[1];
                    i10 -= i9;
                    i11 -= i8;
                    x xVar = recyclerView4.f6593n.f6667g;
                    if (xVar != null && !xVar.g() && xVar.h()) {
                        int b9 = RecyclerView.this.f6604v0.b();
                        if (b9 == 0) {
                            xVar.r();
                        } else {
                            if (xVar.f() >= b9) {
                                xVar.p(b9 - 1);
                            }
                            xVar.j(i9, i8);
                        }
                    }
                } else {
                    i8 = 0;
                    i9 = 0;
                }
                if (!RecyclerView.this.q.isEmpty()) {
                    RecyclerView.this.invalidate();
                }
                RecyclerView recyclerView5 = RecyclerView.this;
                int[] iArr5 = recyclerView5.I0;
                iArr5[0] = 0;
                iArr5[1] = 0;
                recyclerView5.H(i9, i8, i10, i11, null, 1, iArr5);
                RecyclerView recyclerView6 = RecyclerView.this;
                int[] iArr6 = recyclerView6.I0;
                int i12 = i10 - iArr6[0];
                int i13 = i11 - iArr6[1];
                if (i9 != 0 || i8 != 0) {
                    recyclerView6.J(i9, i8);
                }
                if (!RecyclerView.this.awakenScrollBars()) {
                    RecyclerView.this.invalidate();
                }
                boolean z4 = overScroller.isFinished() || (((overScroller.getCurrX() == overScroller.getFinalX()) || i12 != 0) && ((overScroller.getCurrY() == overScroller.getFinalY()) || i13 != 0));
                x xVar2 = RecyclerView.this.f6593n.f6667g;
                if ((xVar2 != null && xVar2.g()) || !z4) {
                    e();
                    RecyclerView recyclerView7 = RecyclerView.this;
                    androidx.recyclerview.widget.k kVar = recyclerView7.f6602t0;
                    if (kVar != null) {
                        kVar.f(recyclerView7, i9, i8);
                    }
                } else {
                    if (RecyclerView.this.getOverScrollMode() != 2) {
                        int currVelocity = (int) overScroller.getCurrVelocity();
                        int i14 = i12 < 0 ? -currVelocity : i12 > 0 ? currVelocity : 0;
                        if (i13 < 0) {
                            currVelocity = -currVelocity;
                        } else if (i13 <= 0) {
                            currVelocity = 0;
                        }
                        RecyclerView.this.a(i14, currVelocity);
                    }
                    if (RecyclerView.Q0) {
                        RecyclerView.this.f6603u0.b();
                    }
                }
            }
            x xVar3 = RecyclerView.this.f6593n.f6667g;
            if (xVar3 != null && xVar3.g()) {
                xVar3.j(0, 0);
            }
            this.f6623e = false;
            if (this.f6624f) {
                d();
                return;
            }
            RecyclerView.this.setScrollState(0);
            RecyclerView.this.x1(1);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            l lVar = RecyclerView.this.f6575d0;
            if (lVar != null) {
                lVar.v();
            }
            RecyclerView.this.B0 = false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b0 {

        /* renamed from: s  reason: collision with root package name */
        private static final List<Object> f6627s = Collections.emptyList();

        /* renamed from: a  reason: collision with root package name */
        public final View f6628a;

        /* renamed from: b  reason: collision with root package name */
        WeakReference<RecyclerView> f6629b;

        /* renamed from: j  reason: collision with root package name */
        int f6637j;

        /* renamed from: r  reason: collision with root package name */
        RecyclerView f6644r;

        /* renamed from: c  reason: collision with root package name */
        int f6630c = -1;

        /* renamed from: d  reason: collision with root package name */
        int f6631d = -1;

        /* renamed from: e  reason: collision with root package name */
        long f6632e = -1;

        /* renamed from: f  reason: collision with root package name */
        int f6633f = -1;

        /* renamed from: g  reason: collision with root package name */
        int f6634g = -1;

        /* renamed from: h  reason: collision with root package name */
        b0 f6635h = null;

        /* renamed from: i  reason: collision with root package name */
        b0 f6636i = null;

        /* renamed from: k  reason: collision with root package name */
        List<Object> f6638k = null;

        /* renamed from: l  reason: collision with root package name */
        List<Object> f6639l = null;

        /* renamed from: m  reason: collision with root package name */
        private int f6640m = 0;

        /* renamed from: n  reason: collision with root package name */
        u f6641n = null;

        /* renamed from: o  reason: collision with root package name */
        boolean f6642o = false;

        /* renamed from: p  reason: collision with root package name */
        private int f6643p = 0;
        int q = -1;

        public b0(View view) {
            if (view == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.f6628a = view;
        }

        private void g() {
            if (this.f6638k == null) {
                ArrayList arrayList = new ArrayList();
                this.f6638k = arrayList;
                this.f6639l = Collections.unmodifiableList(arrayList);
            }
        }

        void A(int i8, boolean z4) {
            if (this.f6631d == -1) {
                this.f6631d = this.f6630c;
            }
            if (this.f6634g == -1) {
                this.f6634g = this.f6630c;
            }
            if (z4) {
                this.f6634g += i8;
            }
            this.f6630c += i8;
            if (this.f6628a.getLayoutParams() != null) {
                ((LayoutParams) this.f6628a.getLayoutParams()).f6615c = true;
            }
        }

        void B(RecyclerView recyclerView) {
            int i8 = this.q;
            if (i8 == -1) {
                i8 = c0.C(this.f6628a);
            }
            this.f6643p = i8;
            recyclerView.n1(this, 4);
        }

        void C(RecyclerView recyclerView) {
            recyclerView.n1(this, this.f6643p);
            this.f6643p = 0;
        }

        void D() {
            this.f6637j = 0;
            this.f6630c = -1;
            this.f6631d = -1;
            this.f6632e = -1L;
            this.f6634g = -1;
            this.f6640m = 0;
            this.f6635h = null;
            this.f6636i = null;
            d();
            this.f6643p = 0;
            this.q = -1;
            RecyclerView.s(this);
        }

        void E() {
            if (this.f6631d == -1) {
                this.f6631d = this.f6630c;
            }
        }

        void F(int i8, int i9) {
            this.f6637j = (i8 & i9) | (this.f6637j & (~i9));
        }

        public final void G(boolean z4) {
            int i8;
            int i9 = this.f6640m;
            int i10 = z4 ? i9 - 1 : i9 + 1;
            this.f6640m = i10;
            if (i10 < 0) {
                this.f6640m = 0;
                Log.e("View", "isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for " + this);
                return;
            }
            if (!z4 && i10 == 1) {
                i8 = this.f6637j | 16;
            } else if (!z4 || i10 != 0) {
                return;
            } else {
                i8 = this.f6637j & (-17);
            }
            this.f6637j = i8;
        }

        void H(u uVar, boolean z4) {
            this.f6641n = uVar;
            this.f6642o = z4;
        }

        boolean I() {
            return (this.f6637j & 16) != 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean J() {
            return (this.f6637j & RecognitionOptions.ITF) != 0;
        }

        void K() {
            this.f6641n.J(this);
        }

        boolean L() {
            return (this.f6637j & 32) != 0;
        }

        void a(Object obj) {
            if (obj == null) {
                b(RecognitionOptions.UPC_E);
            } else if ((1024 & this.f6637j) == 0) {
                g();
                this.f6638k.add(obj);
            }
        }

        void b(int i8) {
            this.f6637j = i8 | this.f6637j;
        }

        void c() {
            this.f6631d = -1;
            this.f6634g = -1;
        }

        void d() {
            List<Object> list = this.f6638k;
            if (list != null) {
                list.clear();
            }
            this.f6637j &= -1025;
        }

        void e() {
            this.f6637j &= -33;
        }

        void f() {
            this.f6637j &= -257;
        }

        boolean h() {
            return (this.f6637j & 16) == 0 && c0.T(this.f6628a);
        }

        void i(int i8, int i9, boolean z4) {
            b(8);
            A(i9, z4);
            this.f6630c = i8;
        }

        public final int j() {
            RecyclerView recyclerView = this.f6644r;
            if (recyclerView == null) {
                return -1;
            }
            return recyclerView.d0(this);
        }

        public final long k() {
            return this.f6632e;
        }

        public final int l() {
            return this.f6633f;
        }

        public final int m() {
            int i8 = this.f6634g;
            return i8 == -1 ? this.f6630c : i8;
        }

        public final int n() {
            return this.f6631d;
        }

        List<Object> o() {
            if ((this.f6637j & RecognitionOptions.UPC_E) == 0) {
                List<Object> list = this.f6638k;
                return (list == null || list.size() == 0) ? f6627s : this.f6639l;
            }
            return f6627s;
        }

        boolean p(int i8) {
            return (i8 & this.f6637j) != 0;
        }

        boolean q() {
            return (this.f6637j & RecognitionOptions.UPC_A) != 0 || t();
        }

        boolean r() {
            return (this.f6628a.getParent() == null || this.f6628a.getParent() == this.f6644r) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean s() {
            return (this.f6637j & 1) != 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean t() {
            return (this.f6637j & 4) != 0;
        }

        public String toString() {
            String simpleName = getClass().isAnonymousClass() ? "ViewHolder" : getClass().getSimpleName();
            StringBuilder sb = new StringBuilder(simpleName + "{" + Integer.toHexString(hashCode()) + " position=" + this.f6630c + " id=" + this.f6632e + ", oldPos=" + this.f6631d + ", pLpos:" + this.f6634g);
            if (w()) {
                sb.append(" scrap ");
                sb.append(this.f6642o ? "[changeScrap]" : "[attachedScrap]");
            }
            if (t()) {
                sb.append(" invalid");
            }
            if (!s()) {
                sb.append(" unbound");
            }
            if (z()) {
                sb.append(" update");
            }
            if (v()) {
                sb.append(" removed");
            }
            if (J()) {
                sb.append(" ignored");
            }
            if (x()) {
                sb.append(" tmpDetached");
            }
            if (!u()) {
                sb.append(" not recyclable(" + this.f6640m + ")");
            }
            if (q()) {
                sb.append(" undefined adapter position");
            }
            if (this.f6628a.getParent() == null) {
                sb.append(" no parent");
            }
            sb.append("}");
            return sb.toString();
        }

        public final boolean u() {
            return (this.f6637j & 16) == 0 && !c0.T(this.f6628a);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean v() {
            return (this.f6637j & 8) != 0;
        }

        boolean w() {
            return this.f6641n != null;
        }

        boolean x() {
            return (this.f6637j & RecognitionOptions.QR_CODE) != 0;
        }

        boolean y() {
            return (this.f6637j & 2) != 0;
        }

        boolean z() {
            return (this.f6637j & 2) != 0;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c implements Interpolator {
        c() {
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f5) {
            float f8 = f5 - 1.0f;
            return (f8 * f8 * f8 * f8 * f8) + 1.0f;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d implements b0.b {
        d() {
        }

        @Override // androidx.recyclerview.widget.b0.b
        public void a(b0 b0Var) {
            RecyclerView recyclerView = RecyclerView.this;
            recyclerView.f6593n.n1(b0Var.f6628a, recyclerView.f6570b);
        }

        @Override // androidx.recyclerview.widget.b0.b
        public void b(b0 b0Var, l.c cVar, l.c cVar2) {
            RecyclerView.this.m(b0Var, cVar, cVar2);
        }

        @Override // androidx.recyclerview.widget.b0.b
        public void c(b0 b0Var, l.c cVar, l.c cVar2) {
            RecyclerView.this.f6570b.J(b0Var);
            RecyclerView.this.o(b0Var, cVar, cVar2);
        }

        @Override // androidx.recyclerview.widget.b0.b
        public void d(b0 b0Var, l.c cVar, l.c cVar2) {
            b0Var.G(false);
            RecyclerView recyclerView = RecyclerView.this;
            boolean z4 = recyclerView.O;
            l lVar = recyclerView.f6575d0;
            if (z4) {
                if (!lVar.b(b0Var, b0Var, cVar, cVar2)) {
                    return;
                }
            } else if (!lVar.d(b0Var, cVar, cVar2)) {
                return;
            }
            RecyclerView.this.P0();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e implements f.b {
        e() {
        }

        @Override // androidx.recyclerview.widget.f.b
        public View a(int i8) {
            return RecyclerView.this.getChildAt(i8);
        }

        @Override // androidx.recyclerview.widget.f.b
        public void b(View view) {
            b0 i02 = RecyclerView.i0(view);
            if (i02 != null) {
                i02.B(RecyclerView.this);
            }
        }

        @Override // androidx.recyclerview.widget.f.b
        public int c() {
            return RecyclerView.this.getChildCount();
        }

        @Override // androidx.recyclerview.widget.f.b
        public void d() {
            int c9 = c();
            for (int i8 = 0; i8 < c9; i8++) {
                View a9 = a(i8);
                RecyclerView.this.A(a9);
                a9.clearAnimation();
            }
            RecyclerView.this.removeAllViews();
        }

        @Override // androidx.recyclerview.widget.f.b
        public int e(View view) {
            return RecyclerView.this.indexOfChild(view);
        }

        @Override // androidx.recyclerview.widget.f.b
        public b0 f(View view) {
            return RecyclerView.i0(view);
        }

        @Override // androidx.recyclerview.widget.f.b
        public void g(int i8) {
            b0 i02;
            View a9 = a(i8);
            if (a9 != null && (i02 = RecyclerView.i0(a9)) != null) {
                if (i02.x() && !i02.J()) {
                    throw new IllegalArgumentException("called detach on an already detached child " + i02 + RecyclerView.this.Q());
                }
                i02.b(RecognitionOptions.QR_CODE);
            }
            RecyclerView.this.detachViewFromParent(i8);
        }

        @Override // androidx.recyclerview.widget.f.b
        public void h(View view) {
            b0 i02 = RecyclerView.i0(view);
            if (i02 != null) {
                i02.C(RecyclerView.this);
            }
        }

        @Override // androidx.recyclerview.widget.f.b
        public void i(View view, int i8) {
            RecyclerView.this.addView(view, i8);
            RecyclerView.this.z(view);
        }

        @Override // androidx.recyclerview.widget.f.b
        public void j(int i8) {
            View childAt = RecyclerView.this.getChildAt(i8);
            if (childAt != null) {
                RecyclerView.this.A(childAt);
                childAt.clearAnimation();
            }
            RecyclerView.this.removeViewAt(i8);
        }

        @Override // androidx.recyclerview.widget.f.b
        public void k(View view, int i8, ViewGroup.LayoutParams layoutParams) {
            b0 i02 = RecyclerView.i0(view);
            if (i02 != null) {
                if (!i02.x() && !i02.J()) {
                    throw new IllegalArgumentException("Called attach on a child which is not detached: " + i02 + RecyclerView.this.Q());
                }
                i02.f();
            }
            RecyclerView.this.attachViewToParent(view, i8, layoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f implements a.InterfaceC0072a {
        f() {
        }

        @Override // androidx.recyclerview.widget.a.InterfaceC0072a
        public void a(int i8, int i9) {
            RecyclerView.this.F0(i8, i9);
            RecyclerView.this.f6610y0 = true;
        }

        @Override // androidx.recyclerview.widget.a.InterfaceC0072a
        public void b(a.b bVar) {
            i(bVar);
        }

        @Override // androidx.recyclerview.widget.a.InterfaceC0072a
        public void c(int i8, int i9, Object obj) {
            RecyclerView.this.A1(i8, i9, obj);
            RecyclerView.this.f6612z0 = true;
        }

        @Override // androidx.recyclerview.widget.a.InterfaceC0072a
        public void d(a.b bVar) {
            i(bVar);
        }

        @Override // androidx.recyclerview.widget.a.InterfaceC0072a
        public b0 e(int i8) {
            b0 b02 = RecyclerView.this.b0(i8, true);
            if (b02 == null || RecyclerView.this.f6576e.n(b02.f6628a)) {
                return null;
            }
            return b02;
        }

        @Override // androidx.recyclerview.widget.a.InterfaceC0072a
        public void f(int i8, int i9) {
            RecyclerView.this.G0(i8, i9, false);
            RecyclerView.this.f6610y0 = true;
        }

        @Override // androidx.recyclerview.widget.a.InterfaceC0072a
        public void g(int i8, int i9) {
            RecyclerView.this.E0(i8, i9);
            RecyclerView.this.f6610y0 = true;
        }

        @Override // androidx.recyclerview.widget.a.InterfaceC0072a
        public void h(int i8, int i9) {
            RecyclerView.this.G0(i8, i9, true);
            RecyclerView recyclerView = RecyclerView.this;
            recyclerView.f6610y0 = true;
            recyclerView.f6604v0.f6717d += i9;
        }

        void i(a.b bVar) {
            int i8 = bVar.f6778a;
            if (i8 == 1) {
                RecyclerView recyclerView = RecyclerView.this;
                recyclerView.f6593n.S0(recyclerView, bVar.f6779b, bVar.f6781d);
            } else if (i8 == 2) {
                RecyclerView recyclerView2 = RecyclerView.this;
                recyclerView2.f6593n.V0(recyclerView2, bVar.f6779b, bVar.f6781d);
            } else if (i8 == 4) {
                RecyclerView recyclerView3 = RecyclerView.this;
                recyclerView3.f6593n.X0(recyclerView3, bVar.f6779b, bVar.f6781d, bVar.f6780c);
            } else if (i8 != 8) {
            } else {
                RecyclerView recyclerView4 = RecyclerView.this;
                recyclerView4.f6593n.U0(recyclerView4, bVar.f6779b, bVar.f6781d, 1);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class g<VH extends b0> {

        /* renamed from: a  reason: collision with root package name */
        private final h f6648a = new h();

        /* renamed from: b  reason: collision with root package name */
        private boolean f6649b = false;

        public void A(boolean z4) {
            if (f()) {
                throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
            }
            this.f6649b = z4;
        }

        public void B(i iVar) {
            this.f6648a.unregisterObserver(iVar);
        }

        public final void a(VH vh, int i8) {
            vh.f6630c = i8;
            if (g()) {
                vh.f6632e = d(i8);
            }
            vh.F(1, 519);
            androidx.core.os.o.a("RV OnBindView");
            s(vh, i8, vh.o());
            vh.d();
            ViewGroup.LayoutParams layoutParams = vh.f6628a.getLayoutParams();
            if (layoutParams instanceof LayoutParams) {
                ((LayoutParams) layoutParams).f6615c = true;
            }
            androidx.core.os.o.b();
        }

        public final VH b(ViewGroup viewGroup, int i8) {
            try {
                androidx.core.os.o.a("RV CreateView");
                VH t8 = t(viewGroup, i8);
                if (t8.f6628a.getParent() == null) {
                    t8.f6633f = i8;
                    return t8;
                }
                throw new IllegalStateException("ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
            } finally {
                androidx.core.os.o.b();
            }
        }

        public abstract int c();

        public long d(int i8) {
            return -1L;
        }

        public int e(int i8) {
            return 0;
        }

        public final boolean f() {
            return this.f6648a.a();
        }

        public final boolean g() {
            return this.f6649b;
        }

        public final void h() {
            this.f6648a.b();
        }

        public final void i(int i8) {
            this.f6648a.d(i8, 1);
        }

        public final void j(int i8) {
            this.f6648a.f(i8, 1);
        }

        public final void k(int i8, int i9) {
            this.f6648a.c(i8, i9);
        }

        public final void l(int i8, int i9) {
            this.f6648a.d(i8, i9);
        }

        public final void m(int i8, int i9, Object obj) {
            this.f6648a.e(i8, i9, obj);
        }

        public final void n(int i8, int i9) {
            this.f6648a.f(i8, i9);
        }

        public final void o(int i8, int i9) {
            this.f6648a.g(i8, i9);
        }

        public final void p(int i8) {
            this.f6648a.g(i8, 1);
        }

        public void q(RecyclerView recyclerView) {
        }

        public abstract void r(VH vh, int i8);

        public void s(VH vh, int i8, List<Object> list) {
            r(vh, i8);
        }

        public abstract VH t(ViewGroup viewGroup, int i8);

        public void u(RecyclerView recyclerView) {
        }

        public boolean v(VH vh) {
            return false;
        }

        public void w(VH vh) {
        }

        public void x(VH vh) {
        }

        public void y(VH vh) {
        }

        public void z(i iVar) {
            this.f6648a.registerObserver(iVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h extends Observable<i> {
        h() {
        }

        public boolean a() {
            return !((Observable) this).mObservers.isEmpty();
        }

        public void b() {
            for (int size = ((Observable) this).mObservers.size() - 1; size >= 0; size--) {
                ((i) ((Observable) this).mObservers.get(size)).a();
            }
        }

        public void c(int i8, int i9) {
            for (int size = ((Observable) this).mObservers.size() - 1; size >= 0; size--) {
                ((i) ((Observable) this).mObservers.get(size)).e(i8, i9, 1);
            }
        }

        public void d(int i8, int i9) {
            e(i8, i9, null);
        }

        public void e(int i8, int i9, Object obj) {
            for (int size = ((Observable) this).mObservers.size() - 1; size >= 0; size--) {
                ((i) ((Observable) this).mObservers.get(size)).c(i8, i9, obj);
            }
        }

        public void f(int i8, int i9) {
            for (int size = ((Observable) this).mObservers.size() - 1; size >= 0; size--) {
                ((i) ((Observable) this).mObservers.get(size)).d(i8, i9);
            }
        }

        public void g(int i8, int i9) {
            for (int size = ((Observable) this).mObservers.size() - 1; size >= 0; size--) {
                ((i) ((Observable) this).mObservers.get(size)).f(i8, i9);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class i {
        public void a() {
        }

        public void b(int i8, int i9) {
        }

        public void c(int i8, int i9, Object obj) {
            b(i8, i9);
        }

        public void d(int i8, int i9) {
        }

        public void e(int i8, int i9, int i10) {
        }

        public void f(int i8, int i9) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface j {
        int a(int i8, int i9);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class k {
        protected EdgeEffect a(RecyclerView recyclerView, int i8) {
            return new EdgeEffect(recyclerView.getContext());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class l {

        /* renamed from: a  reason: collision with root package name */
        private b f6650a = null;

        /* renamed from: b  reason: collision with root package name */
        private ArrayList<a> f6651b = new ArrayList<>();

        /* renamed from: c  reason: collision with root package name */
        private long f6652c = 120;

        /* renamed from: d  reason: collision with root package name */
        private long f6653d = 120;

        /* renamed from: e  reason: collision with root package name */
        private long f6654e = 250;

        /* renamed from: f  reason: collision with root package name */
        private long f6655f = 250;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface a {
            void a();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface b {
            void a(b0 b0Var);
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class c {

            /* renamed from: a  reason: collision with root package name */
            public int f6656a;

            /* renamed from: b  reason: collision with root package name */
            public int f6657b;

            /* renamed from: c  reason: collision with root package name */
            public int f6658c;

            /* renamed from: d  reason: collision with root package name */
            public int f6659d;

            public c a(b0 b0Var) {
                return b(b0Var, 0);
            }

            public c b(b0 b0Var, int i8) {
                View view = b0Var.f6628a;
                this.f6656a = view.getLeft();
                this.f6657b = view.getTop();
                this.f6658c = view.getRight();
                this.f6659d = view.getBottom();
                return this;
            }
        }

        static int e(b0 b0Var) {
            int i8 = b0Var.f6637j & 14;
            if (b0Var.t()) {
                return 4;
            }
            if ((i8 & 4) == 0) {
                int n8 = b0Var.n();
                int j8 = b0Var.j();
                return (n8 == -1 || j8 == -1 || n8 == j8) ? i8 : i8 | RecognitionOptions.PDF417;
            }
            return i8;
        }

        public abstract boolean a(b0 b0Var, c cVar, c cVar2);

        public abstract boolean b(b0 b0Var, b0 b0Var2, c cVar, c cVar2);

        public abstract boolean c(b0 b0Var, c cVar, c cVar2);

        public abstract boolean d(b0 b0Var, c cVar, c cVar2);

        public abstract boolean f(b0 b0Var);

        public boolean g(b0 b0Var, List<Object> list) {
            return f(b0Var);
        }

        public final void h(b0 b0Var) {
            s(b0Var);
            b bVar = this.f6650a;
            if (bVar != null) {
                bVar.a(b0Var);
            }
        }

        public final void i() {
            int size = this.f6651b.size();
            for (int i8 = 0; i8 < size; i8++) {
                this.f6651b.get(i8).a();
            }
            this.f6651b.clear();
        }

        public abstract void j(b0 b0Var);

        public abstract void k();

        public long l() {
            return this.f6652c;
        }

        public long m() {
            return this.f6655f;
        }

        public long n() {
            return this.f6654e;
        }

        public long o() {
            return this.f6653d;
        }

        public abstract boolean p();

        public final boolean q(a aVar) {
            boolean p8 = p();
            if (aVar != null) {
                if (p8) {
                    this.f6651b.add(aVar);
                } else {
                    aVar.a();
                }
            }
            return p8;
        }

        public c r() {
            return new c();
        }

        public void s(b0 b0Var) {
        }

        public c t(y yVar, b0 b0Var) {
            return r().a(b0Var);
        }

        public c u(y yVar, b0 b0Var, int i8, List<Object> list) {
            return r().a(b0Var);
        }

        public abstract void v();

        void w(b bVar) {
            this.f6650a = bVar;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class m implements l.b {
        m() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.l.b
        public void a(b0 b0Var) {
            b0Var.G(true);
            if (b0Var.f6635h != null && b0Var.f6636i == null) {
                b0Var.f6635h = null;
            }
            b0Var.f6636i = null;
            if (b0Var.I() || RecyclerView.this.Y0(b0Var.f6628a) || !b0Var.x()) {
                return;
            }
            RecyclerView.this.removeDetachedView(b0Var.f6628a, false);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class n {
        @Deprecated
        public void f(Rect rect, int i8, RecyclerView recyclerView) {
            rect.set(0, 0, 0, 0);
        }

        public void g(Rect rect, View view, RecyclerView recyclerView, y yVar) {
            f(rect, ((LayoutParams) view.getLayoutParams()).a(), recyclerView);
        }

        @Deprecated
        public void h(Canvas canvas, RecyclerView recyclerView) {
        }

        public void i(Canvas canvas, RecyclerView recyclerView, y yVar) {
            h(canvas, recyclerView);
        }

        @Deprecated
        public void j(Canvas canvas, RecyclerView recyclerView) {
        }

        public void k(Canvas canvas, RecyclerView recyclerView, y yVar) {
            j(canvas, recyclerView);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class o {

        /* renamed from: a  reason: collision with root package name */
        androidx.recyclerview.widget.f f6661a;

        /* renamed from: b  reason: collision with root package name */
        RecyclerView f6662b;

        /* renamed from: c  reason: collision with root package name */
        private final a0.b f6663c;

        /* renamed from: d  reason: collision with root package name */
        private final a0.b f6664d;

        /* renamed from: e  reason: collision with root package name */
        androidx.recyclerview.widget.a0 f6665e;

        /* renamed from: f  reason: collision with root package name */
        androidx.recyclerview.widget.a0 f6666f;

        /* renamed from: g  reason: collision with root package name */
        x f6667g;

        /* renamed from: h  reason: collision with root package name */
        boolean f6668h;

        /* renamed from: i  reason: collision with root package name */
        boolean f6669i;

        /* renamed from: j  reason: collision with root package name */
        boolean f6670j;

        /* renamed from: k  reason: collision with root package name */
        private boolean f6671k;

        /* renamed from: l  reason: collision with root package name */
        private boolean f6672l;

        /* renamed from: m  reason: collision with root package name */
        int f6673m;

        /* renamed from: n  reason: collision with root package name */
        boolean f6674n;

        /* renamed from: o  reason: collision with root package name */
        private int f6675o;

        /* renamed from: p  reason: collision with root package name */
        private int f6676p;
        private int q;

        /* renamed from: r  reason: collision with root package name */
        private int f6677r;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements a0.b {
            a() {
            }

            @Override // androidx.recyclerview.widget.a0.b
            public View a(int i8) {
                return o.this.J(i8);
            }

            @Override // androidx.recyclerview.widget.a0.b
            public int b(View view) {
                return o.this.R(view) - ((ViewGroup.MarginLayoutParams) ((LayoutParams) view.getLayoutParams())).leftMargin;
            }

            @Override // androidx.recyclerview.widget.a0.b
            public int c() {
                return o.this.f0();
            }

            @Override // androidx.recyclerview.widget.a0.b
            public int d() {
                return o.this.p0() - o.this.g0();
            }

            @Override // androidx.recyclerview.widget.a0.b
            public int e(View view) {
                return o.this.U(view) + ((ViewGroup.MarginLayoutParams) ((LayoutParams) view.getLayoutParams())).rightMargin;
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class b implements a0.b {
            b() {
            }

            @Override // androidx.recyclerview.widget.a0.b
            public View a(int i8) {
                return o.this.J(i8);
            }

            @Override // androidx.recyclerview.widget.a0.b
            public int b(View view) {
                return o.this.V(view) - ((ViewGroup.MarginLayoutParams) ((LayoutParams) view.getLayoutParams())).topMargin;
            }

            @Override // androidx.recyclerview.widget.a0.b
            public int c() {
                return o.this.h0();
            }

            @Override // androidx.recyclerview.widget.a0.b
            public int d() {
                return o.this.X() - o.this.e0();
            }

            @Override // androidx.recyclerview.widget.a0.b
            public int e(View view) {
                return o.this.P(view) + ((ViewGroup.MarginLayoutParams) ((LayoutParams) view.getLayoutParams())).bottomMargin;
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface c {
            void a(int i8, int i9);
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class d {

            /* renamed from: a  reason: collision with root package name */
            public int f6680a;

            /* renamed from: b  reason: collision with root package name */
            public int f6681b;

            /* renamed from: c  reason: collision with root package name */
            public boolean f6682c;

            /* renamed from: d  reason: collision with root package name */
            public boolean f6683d;
        }

        public o() {
            a aVar = new a();
            this.f6663c = aVar;
            b bVar = new b();
            this.f6664d = bVar;
            this.f6665e = new androidx.recyclerview.widget.a0(aVar);
            this.f6666f = new androidx.recyclerview.widget.a0(bVar);
            this.f6668h = false;
            this.f6669i = false;
            this.f6670j = false;
            this.f6671k = true;
            this.f6672l = true;
        }

        /* JADX WARN: Code restructure failed: missing block: B:9:0x0017, code lost:
            if (r5 == 1073741824) goto L8;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public static int L(int r4, int r5, int r6, int r7, boolean r8) {
            /*
                int r4 = r4 - r6
                r6 = 0
                int r4 = java.lang.Math.max(r6, r4)
                r0 = -2
                r1 = -1
                r2 = -2147483648(0xffffffff80000000, float:-0.0)
                r3 = 1073741824(0x40000000, float:2.0)
                if (r8 == 0) goto L1a
                if (r7 < 0) goto L11
                goto L1c
            L11:
                if (r7 != r1) goto L2f
                if (r5 == r2) goto L20
                if (r5 == 0) goto L2f
                if (r5 == r3) goto L20
                goto L2f
            L1a:
                if (r7 < 0) goto L1e
            L1c:
                r5 = r3
                goto L31
            L1e:
                if (r7 != r1) goto L22
            L20:
                r7 = r4
                goto L31
            L22:
                if (r7 != r0) goto L2f
                if (r5 == r2) goto L2c
                if (r5 != r3) goto L29
                goto L2c
            L29:
                r7 = r4
                r5 = r6
                goto L31
            L2c:
                r7 = r4
                r5 = r2
                goto L31
            L2f:
                r5 = r6
                r7 = r5
            L31:
                int r4 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r5)
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.o.L(int, int, int, int, boolean):int");
        }

        private int[] M(View view, Rect rect) {
            int[] iArr = new int[2];
            int f02 = f0();
            int h02 = h0();
            int p02 = p0() - g0();
            int X = X() - e0();
            int left = (view.getLeft() + rect.left) - view.getScrollX();
            int top = (view.getTop() + rect.top) - view.getScrollY();
            int width = rect.width() + left;
            int height = rect.height() + top;
            int i8 = left - f02;
            int min = Math.min(0, i8);
            int i9 = top - h02;
            int min2 = Math.min(0, i9);
            int i10 = width - p02;
            int max = Math.max(0, i10);
            int max2 = Math.max(0, height - X);
            if (a0() != 1) {
                if (min == 0) {
                    min = Math.min(i8, max);
                }
                max = min;
            } else if (max == 0) {
                max = Math.max(min, i10);
            }
            if (min2 == 0) {
                min2 = Math.min(i9, max2);
            }
            iArr[0] = max;
            iArr[1] = min2;
            return iArr;
        }

        private void g(View view, int i8, boolean z4) {
            b0 i02 = RecyclerView.i0(view);
            if (z4 || i02.v()) {
                this.f6662b.f6578f.b(i02);
            } else {
                this.f6662b.f6578f.p(i02);
            }
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            if (i02.L() || i02.w()) {
                if (i02.w()) {
                    i02.K();
                } else {
                    i02.e();
                }
                this.f6661a.c(view, i8, view.getLayoutParams(), false);
            } else if (view.getParent() == this.f6662b) {
                int m8 = this.f6661a.m(view);
                if (i8 == -1) {
                    i8 = this.f6661a.g();
                }
                if (m8 == -1) {
                    throw new IllegalStateException("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:" + this.f6662b.indexOfChild(view) + this.f6662b.Q());
                } else if (m8 != i8) {
                    this.f6662b.f6593n.C0(m8, i8);
                }
            } else {
                this.f6661a.a(view, i8, false);
                layoutParams.f6615c = true;
                x xVar = this.f6667g;
                if (xVar != null && xVar.h()) {
                    this.f6667g.k(view);
                }
            }
            if (layoutParams.f6616d) {
                i02.f6628a.invalidate();
                layoutParams.f6616d = false;
            }
        }

        public static d j0(Context context, AttributeSet attributeSet, int i8, int i9) {
            d dVar = new d();
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, p1.d.f22277f, i8, i9);
            dVar.f6680a = obtainStyledAttributes.getInt(p1.d.f22278g, 1);
            dVar.f6681b = obtainStyledAttributes.getInt(p1.d.q, 1);
            dVar.f6682c = obtainStyledAttributes.getBoolean(p1.d.f22287p, false);
            dVar.f6683d = obtainStyledAttributes.getBoolean(p1.d.f22288r, false);
            obtainStyledAttributes.recycle();
            return dVar;
        }

        public static int o(int i8, int i9, int i10) {
            int mode = View.MeasureSpec.getMode(i8);
            int size = View.MeasureSpec.getSize(i8);
            return mode != Integer.MIN_VALUE ? mode != 1073741824 ? Math.max(i9, i10) : size : Math.min(size, Math.max(i9, i10));
        }

        private boolean u0(RecyclerView recyclerView, int i8, int i9) {
            View focusedChild = recyclerView.getFocusedChild();
            if (focusedChild == null) {
                return false;
            }
            int f02 = f0();
            int h02 = h0();
            int p02 = p0() - g0();
            int X = X() - e0();
            Rect rect = this.f6662b.f6585j;
            Q(focusedChild, rect);
            return rect.left - i8 < p02 && rect.right - i8 > f02 && rect.top - i9 < X && rect.bottom - i9 > h02;
        }

        private void w1(u uVar, int i8, View view) {
            b0 i02 = RecyclerView.i0(view);
            if (i02.J()) {
                return;
            }
            if (i02.t() && !i02.v() && !this.f6662b.f6591m.g()) {
                r1(i8);
                uVar.C(i02);
                return;
            }
            y(i8);
            uVar.D(view);
            this.f6662b.f6578f.k(i02);
        }

        private static boolean x0(int i8, int i9, int i10) {
            int mode = View.MeasureSpec.getMode(i9);
            int size = View.MeasureSpec.getSize(i9);
            if (i10 <= 0 || i8 == i10) {
                if (mode == Integer.MIN_VALUE) {
                    return size >= i8;
                } else if (mode != 0) {
                    return mode == 1073741824 && size == i8;
                } else {
                    return true;
                }
            }
            return false;
        }

        private void z(int i8, View view) {
            this.f6661a.d(i8);
        }

        void A(RecyclerView recyclerView) {
            this.f6669i = true;
            H0(recyclerView);
        }

        public void A0(View view, int i8, int i9, int i10, int i11) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            Rect rect = layoutParams.f6614b;
            view.layout(i8 + rect.left + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, i9 + rect.top + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, (i10 - rect.right) - ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, (i11 - rect.bottom) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
        }

        void A1(RecyclerView recyclerView) {
            B1(View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), 1073741824));
        }

        void B(RecyclerView recyclerView, u uVar) {
            this.f6669i = false;
            J0(recyclerView, uVar);
        }

        public void B0(View view, int i8, int i9) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            Rect n02 = this.f6662b.n0(view);
            int i10 = i8 + n02.left + n02.right;
            int i11 = i9 + n02.top + n02.bottom;
            int L = L(p0(), q0(), f0() + g0() + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + i10, ((ViewGroup.MarginLayoutParams) layoutParams).width, l());
            int L2 = L(X(), Y(), h0() + e0() + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + i11, ((ViewGroup.MarginLayoutParams) layoutParams).height, m());
            if (G1(view, L, L2, layoutParams)) {
                view.measure(L, L2);
            }
        }

        void B1(int i8, int i9) {
            this.q = View.MeasureSpec.getSize(i8);
            int mode = View.MeasureSpec.getMode(i8);
            this.f6675o = mode;
            if (mode == 0 && !RecyclerView.O0) {
                this.q = 0;
            }
            this.f6677r = View.MeasureSpec.getSize(i9);
            int mode2 = View.MeasureSpec.getMode(i9);
            this.f6676p = mode2;
            if (mode2 != 0 || RecyclerView.O0) {
                return;
            }
            this.f6677r = 0;
        }

        public View C(View view) {
            View T;
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView == null || (T = recyclerView.T(view)) == null || this.f6661a.n(T)) {
                return null;
            }
            return T;
        }

        public void C0(int i8, int i9) {
            View J = J(i8);
            if (J != null) {
                y(i8);
                i(J, i9);
                return;
            }
            throw new IllegalArgumentException("Cannot move a child from non-existing index:" + i8 + this.f6662b.toString());
        }

        public void C1(int i8, int i9) {
            this.f6662b.setMeasuredDimension(i8, i9);
        }

        public View D(int i8) {
            int K = K();
            for (int i9 = 0; i9 < K; i9++) {
                View J = J(i9);
                b0 i02 = RecyclerView.i0(J);
                if (i02 != null && i02.m() == i8 && !i02.J() && (this.f6662b.f6604v0.e() || !i02.v())) {
                    return J;
                }
            }
            return null;
        }

        public void D0(int i8) {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView != null) {
                recyclerView.C0(i8);
            }
        }

        public void D1(Rect rect, int i8, int i9) {
            C1(o(i8, rect.width() + f0() + g0(), d0()), o(i9, rect.height() + h0() + e0(), c0()));
        }

        public abstract LayoutParams E();

        public void E0(int i8) {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView != null) {
                recyclerView.D0(i8);
            }
        }

        void E1(int i8, int i9) {
            int K = K();
            if (K == 0) {
                this.f6662b.x(i8, i9);
                return;
            }
            int i10 = Integer.MIN_VALUE;
            int i11 = Integer.MAX_VALUE;
            int i12 = Integer.MAX_VALUE;
            int i13 = Integer.MIN_VALUE;
            for (int i14 = 0; i14 < K; i14++) {
                View J = J(i14);
                Rect rect = this.f6662b.f6585j;
                Q(J, rect);
                int i15 = rect.left;
                if (i15 < i11) {
                    i11 = i15;
                }
                int i16 = rect.right;
                if (i16 > i10) {
                    i10 = i16;
                }
                int i17 = rect.top;
                if (i17 < i12) {
                    i12 = i17;
                }
                int i18 = rect.bottom;
                if (i18 > i13) {
                    i13 = i18;
                }
            }
            this.f6662b.f6585j.set(i11, i12, i10, i13);
            D1(this.f6662b.f6585j, i8, i9);
        }

        public LayoutParams F(Context context, AttributeSet attributeSet) {
            return new LayoutParams(context, attributeSet);
        }

        public void F0(g gVar, g gVar2) {
        }

        void F1(RecyclerView recyclerView) {
            int height;
            if (recyclerView == null) {
                this.f6662b = null;
                this.f6661a = null;
                height = 0;
                this.q = 0;
            } else {
                this.f6662b = recyclerView;
                this.f6661a = recyclerView.f6576e;
                this.q = recyclerView.getWidth();
                height = recyclerView.getHeight();
            }
            this.f6677r = height;
            this.f6675o = 1073741824;
            this.f6676p = 1073741824;
        }

        public LayoutParams G(ViewGroup.LayoutParams layoutParams) {
            return layoutParams instanceof LayoutParams ? new LayoutParams((LayoutParams) layoutParams) : layoutParams instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams) : new LayoutParams(layoutParams);
        }

        public boolean G0(RecyclerView recyclerView, ArrayList<View> arrayList, int i8, int i9) {
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean G1(View view, int i8, int i9, LayoutParams layoutParams) {
            return (!view.isLayoutRequested() && this.f6671k && x0(view.getWidth(), i8, ((ViewGroup.MarginLayoutParams) layoutParams).width) && x0(view.getHeight(), i9, ((ViewGroup.MarginLayoutParams) layoutParams).height)) ? false : true;
        }

        public int H() {
            return -1;
        }

        public void H0(RecyclerView recyclerView) {
        }

        boolean H1() {
            return false;
        }

        public int I(View view) {
            return ((LayoutParams) view.getLayoutParams()).f6614b.bottom;
        }

        @Deprecated
        public void I0(RecyclerView recyclerView) {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean I1(View view, int i8, int i9, LayoutParams layoutParams) {
            return (this.f6671k && x0(view.getMeasuredWidth(), i8, ((ViewGroup.MarginLayoutParams) layoutParams).width) && x0(view.getMeasuredHeight(), i9, ((ViewGroup.MarginLayoutParams) layoutParams).height)) ? false : true;
        }

        public View J(int i8) {
            androidx.recyclerview.widget.f fVar = this.f6661a;
            if (fVar != null) {
                return fVar.f(i8);
            }
            return null;
        }

        public void J0(RecyclerView recyclerView, u uVar) {
            I0(recyclerView);
        }

        public void J1(RecyclerView recyclerView, y yVar, int i8) {
            Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
        }

        public int K() {
            androidx.recyclerview.widget.f fVar = this.f6661a;
            if (fVar != null) {
                return fVar.g();
            }
            return 0;
        }

        public View K0(View view, int i8, u uVar, y yVar) {
            return null;
        }

        public void K1(x xVar) {
            x xVar2 = this.f6667g;
            if (xVar2 != null && xVar != xVar2 && xVar2.h()) {
                this.f6667g.r();
            }
            this.f6667g = xVar;
            xVar.q(this.f6662b, this);
        }

        public void L0(AccessibilityEvent accessibilityEvent) {
            RecyclerView recyclerView = this.f6662b;
            M0(recyclerView.f6570b, recyclerView.f6604v0, accessibilityEvent);
        }

        void L1() {
            x xVar = this.f6667g;
            if (xVar != null) {
                xVar.r();
            }
        }

        public void M0(u uVar, y yVar, AccessibilityEvent accessibilityEvent) {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView == null || accessibilityEvent == null) {
                return;
            }
            boolean z4 = true;
            if (!recyclerView.canScrollVertically(1) && !this.f6662b.canScrollVertically(-1) && !this.f6662b.canScrollHorizontally(-1) && !this.f6662b.canScrollHorizontally(1)) {
                z4 = false;
            }
            accessibilityEvent.setScrollable(z4);
            g gVar = this.f6662b.f6591m;
            if (gVar != null) {
                accessibilityEvent.setItemCount(gVar.c());
            }
        }

        public boolean M1() {
            return false;
        }

        public boolean N() {
            RecyclerView recyclerView = this.f6662b;
            return recyclerView != null && recyclerView.f6580g;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void N0(androidx.core.view.accessibility.c cVar) {
            RecyclerView recyclerView = this.f6662b;
            O0(recyclerView.f6570b, recyclerView.f6604v0, cVar);
        }

        public int O(u uVar, y yVar) {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView == null || recyclerView.f6591m == null || !l()) {
                return 1;
            }
            return this.f6662b.f6591m.c();
        }

        public void O0(u uVar, y yVar, androidx.core.view.accessibility.c cVar) {
            if (this.f6662b.canScrollVertically(-1) || this.f6662b.canScrollHorizontally(-1)) {
                cVar.a(8192);
                cVar.y0(true);
            }
            if (this.f6662b.canScrollVertically(1) || this.f6662b.canScrollHorizontally(1)) {
                cVar.a(RecognitionOptions.AZTEC);
                cVar.y0(true);
            }
            cVar.e0(c.b.b(l0(uVar, yVar), O(uVar, yVar), w0(uVar, yVar), m0(uVar, yVar)));
        }

        public int P(View view) {
            return view.getBottom() + I(view);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void P0(View view, androidx.core.view.accessibility.c cVar) {
            b0 i02 = RecyclerView.i0(view);
            if (i02 == null || i02.v() || this.f6661a.n(i02.f6628a)) {
                return;
            }
            RecyclerView recyclerView = this.f6662b;
            Q0(recyclerView.f6570b, recyclerView.f6604v0, view, cVar);
        }

        public void Q(View view, Rect rect) {
            RecyclerView.k0(view, rect);
        }

        public void Q0(u uVar, y yVar, View view, androidx.core.view.accessibility.c cVar) {
            cVar.f0(c.C0043c.a(m() ? i0(view) : 0, 1, l() ? i0(view) : 0, 1, false, false));
        }

        public int R(View view) {
            return view.getLeft() - b0(view);
        }

        public View R0(View view, int i8) {
            return null;
        }

        public int S(View view) {
            Rect rect = ((LayoutParams) view.getLayoutParams()).f6614b;
            return view.getMeasuredHeight() + rect.top + rect.bottom;
        }

        public void S0(RecyclerView recyclerView, int i8, int i9) {
        }

        public int T(View view) {
            Rect rect = ((LayoutParams) view.getLayoutParams()).f6614b;
            return view.getMeasuredWidth() + rect.left + rect.right;
        }

        public void T0(RecyclerView recyclerView) {
        }

        public int U(View view) {
            return view.getRight() + k0(view);
        }

        public void U0(RecyclerView recyclerView, int i8, int i9, int i10) {
        }

        public int V(View view) {
            return view.getTop() - n0(view);
        }

        public void V0(RecyclerView recyclerView, int i8, int i9) {
        }

        public View W() {
            View focusedChild;
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView == null || (focusedChild = recyclerView.getFocusedChild()) == null || this.f6661a.n(focusedChild)) {
                return null;
            }
            return focusedChild;
        }

        public void W0(RecyclerView recyclerView, int i8, int i9) {
        }

        public int X() {
            return this.f6677r;
        }

        public void X0(RecyclerView recyclerView, int i8, int i9, Object obj) {
            W0(recyclerView, i8, i9);
        }

        public int Y() {
            return this.f6676p;
        }

        public void Y0(u uVar, y yVar) {
            Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
        }

        public int Z() {
            RecyclerView recyclerView = this.f6662b;
            g adapter = recyclerView != null ? recyclerView.getAdapter() : null;
            if (adapter != null) {
                return adapter.c();
            }
            return 0;
        }

        public void Z0(y yVar) {
        }

        public int a0() {
            return c0.E(this.f6662b);
        }

        public void a1(u uVar, y yVar, int i8, int i9) {
            this.f6662b.x(i8, i9);
        }

        public int b0(View view) {
            return ((LayoutParams) view.getLayoutParams()).f6614b.left;
        }

        @Deprecated
        public boolean b1(RecyclerView recyclerView, View view, View view2) {
            return y0() || recyclerView.x0();
        }

        public void c(View view) {
            d(view, -1);
        }

        public int c0() {
            return c0.F(this.f6662b);
        }

        public boolean c1(RecyclerView recyclerView, y yVar, View view, View view2) {
            return b1(recyclerView, view, view2);
        }

        public void d(View view, int i8) {
            g(view, i8, true);
        }

        public int d0() {
            return c0.G(this.f6662b);
        }

        public void d1(Parcelable parcelable) {
        }

        public void e(View view) {
            f(view, -1);
        }

        public int e0() {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView != null) {
                return recyclerView.getPaddingBottom();
            }
            return 0;
        }

        public Parcelable e1() {
            return null;
        }

        public void f(View view, int i8) {
            g(view, i8, false);
        }

        public int f0() {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView != null) {
                return recyclerView.getPaddingLeft();
            }
            return 0;
        }

        public void f1(int i8) {
        }

        public int g0() {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView != null) {
                return recyclerView.getPaddingRight();
            }
            return 0;
        }

        void g1(x xVar) {
            if (this.f6667g == xVar) {
                this.f6667g = null;
            }
        }

        public void h(String str) {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView != null) {
                recyclerView.p(str);
            }
        }

        public int h0() {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView != null) {
                return recyclerView.getPaddingTop();
            }
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean h1(int i8, Bundle bundle) {
            RecyclerView recyclerView = this.f6662b;
            return i1(recyclerView.f6570b, recyclerView.f6604v0, i8, bundle);
        }

        public void i(View view, int i8) {
            j(view, i8, (LayoutParams) view.getLayoutParams());
        }

        public int i0(View view) {
            return ((LayoutParams) view.getLayoutParams()).a();
        }

        public boolean i1(u uVar, y yVar, int i8, Bundle bundle) {
            int X;
            int p02;
            int i9;
            int i10;
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView == null) {
                return false;
            }
            if (i8 == 4096) {
                X = recyclerView.canScrollVertically(1) ? (X() - h0()) - e0() : 0;
                if (this.f6662b.canScrollHorizontally(1)) {
                    p02 = (p0() - f0()) - g0();
                    i9 = X;
                    i10 = p02;
                }
                i9 = X;
                i10 = 0;
            } else if (i8 != 8192) {
                i10 = 0;
                i9 = 0;
            } else {
                X = recyclerView.canScrollVertically(-1) ? -((X() - h0()) - e0()) : 0;
                if (this.f6662b.canScrollHorizontally(-1)) {
                    p02 = -((p0() - f0()) - g0());
                    i9 = X;
                    i10 = p02;
                }
                i9 = X;
                i10 = 0;
            }
            if (i9 == 0 && i10 == 0) {
                return false;
            }
            this.f6662b.s1(i10, i9, null, Integer.MIN_VALUE, true);
            return true;
        }

        public void j(View view, int i8, LayoutParams layoutParams) {
            b0 i02 = RecyclerView.i0(view);
            if (i02.v()) {
                this.f6662b.f6578f.b(i02);
            } else {
                this.f6662b.f6578f.p(i02);
            }
            this.f6661a.c(view, i8, layoutParams, i02.v());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean j1(View view, int i8, Bundle bundle) {
            RecyclerView recyclerView = this.f6662b;
            return k1(recyclerView.f6570b, recyclerView.f6604v0, view, i8, bundle);
        }

        public void k(View view, Rect rect) {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView == null) {
                rect.set(0, 0, 0, 0);
            } else {
                rect.set(recyclerView.n0(view));
            }
        }

        public int k0(View view) {
            return ((LayoutParams) view.getLayoutParams()).f6614b.right;
        }

        public boolean k1(u uVar, y yVar, View view, int i8, Bundle bundle) {
            return false;
        }

        public boolean l() {
            return false;
        }

        public int l0(u uVar, y yVar) {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView == null || recyclerView.f6591m == null || !m()) {
                return 1;
            }
            return this.f6662b.f6591m.c();
        }

        public void l1(u uVar) {
            for (int K = K() - 1; K >= 0; K--) {
                if (!RecyclerView.i0(J(K)).J()) {
                    o1(K, uVar);
                }
            }
        }

        public boolean m() {
            return false;
        }

        public int m0(u uVar, y yVar) {
            return 0;
        }

        void m1(u uVar) {
            int j8 = uVar.j();
            for (int i8 = j8 - 1; i8 >= 0; i8--) {
                View n8 = uVar.n(i8);
                b0 i02 = RecyclerView.i0(n8);
                if (!i02.J()) {
                    i02.G(false);
                    if (i02.x()) {
                        this.f6662b.removeDetachedView(n8, false);
                    }
                    l lVar = this.f6662b.f6575d0;
                    if (lVar != null) {
                        lVar.j(i02);
                    }
                    i02.G(true);
                    uVar.y(n8);
                }
            }
            uVar.e();
            if (j8 > 0) {
                this.f6662b.invalidate();
            }
        }

        public boolean n(LayoutParams layoutParams) {
            return layoutParams != null;
        }

        public int n0(View view) {
            return ((LayoutParams) view.getLayoutParams()).f6614b.top;
        }

        public void n1(View view, u uVar) {
            q1(view);
            uVar.B(view);
        }

        public void o0(View view, boolean z4, Rect rect) {
            Matrix matrix;
            if (z4) {
                Rect rect2 = ((LayoutParams) view.getLayoutParams()).f6614b;
                rect.set(-rect2.left, -rect2.top, view.getWidth() + rect2.right, view.getHeight() + rect2.bottom);
            } else {
                rect.set(0, 0, view.getWidth(), view.getHeight());
            }
            if (this.f6662b != null && (matrix = view.getMatrix()) != null && !matrix.isIdentity()) {
                RectF rectF = this.f6662b.f6589l;
                rectF.set(rect);
                matrix.mapRect(rectF);
                rect.set((int) Math.floor(rectF.left), (int) Math.floor(rectF.top), (int) Math.ceil(rectF.right), (int) Math.ceil(rectF.bottom));
            }
            rect.offset(view.getLeft(), view.getTop());
        }

        public void o1(int i8, u uVar) {
            View J = J(i8);
            r1(i8);
            uVar.B(J);
        }

        public void p(int i8, int i9, y yVar, c cVar) {
        }

        public int p0() {
            return this.q;
        }

        public boolean p1(Runnable runnable) {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView != null) {
                return recyclerView.removeCallbacks(runnable);
            }
            return false;
        }

        public void q(int i8, c cVar) {
        }

        public int q0() {
            return this.f6675o;
        }

        public void q1(View view) {
            this.f6661a.p(view);
        }

        public int r(y yVar) {
            return 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean r0() {
            int K = K();
            for (int i8 = 0; i8 < K; i8++) {
                ViewGroup.LayoutParams layoutParams = J(i8).getLayoutParams();
                if (layoutParams.width < 0 && layoutParams.height < 0) {
                    return true;
                }
            }
            return false;
        }

        public void r1(int i8) {
            if (J(i8) != null) {
                this.f6661a.q(i8);
            }
        }

        public int s(y yVar) {
            return 0;
        }

        public boolean s0() {
            return this.f6669i;
        }

        public boolean s1(RecyclerView recyclerView, View view, Rect rect, boolean z4) {
            return t1(recyclerView, view, rect, z4, false);
        }

        public int t(y yVar) {
            return 0;
        }

        public boolean t0() {
            return this.f6670j;
        }

        public boolean t1(RecyclerView recyclerView, View view, Rect rect, boolean z4, boolean z8) {
            int[] M = M(view, rect);
            int i8 = M[0];
            int i9 = M[1];
            if ((!z8 || u0(recyclerView, i8, i9)) && !(i8 == 0 && i9 == 0)) {
                if (z4) {
                    recyclerView.scrollBy(i8, i9);
                } else {
                    recyclerView.p1(i8, i9);
                }
                return true;
            }
            return false;
        }

        public int u(y yVar) {
            return 0;
        }

        public void u1() {
            RecyclerView recyclerView = this.f6662b;
            if (recyclerView != null) {
                recyclerView.requestLayout();
            }
        }

        public int v(y yVar) {
            return 0;
        }

        public final boolean v0() {
            return this.f6672l;
        }

        public void v1() {
            this.f6668h = true;
        }

        public int w(y yVar) {
            return 0;
        }

        public boolean w0(u uVar, y yVar) {
            return false;
        }

        public void x(u uVar) {
            for (int K = K() - 1; K >= 0; K--) {
                w1(uVar, K, J(K));
            }
        }

        public int x1(int i8, u uVar, y yVar) {
            return 0;
        }

        public void y(int i8) {
            z(i8, J(i8));
        }

        public boolean y0() {
            x xVar = this.f6667g;
            return xVar != null && xVar.h();
        }

        public void y1(int i8) {
        }

        public boolean z0(View view, boolean z4, boolean z8) {
            boolean z9 = this.f6665e.b(view, 24579) && this.f6666f.b(view, 24579);
            return z4 ? z9 : !z9;
        }

        public int z1(int i8, u uVar, y yVar) {
            return 0;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface p {
        void b(View view);

        void d(View view);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class q {
        public abstract boolean a(int i8, int i9);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface r {
        void a(RecyclerView recyclerView, MotionEvent motionEvent);

        boolean c(RecyclerView recyclerView, MotionEvent motionEvent);

        void e(boolean z4);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class s {
        public void a(RecyclerView recyclerView, int i8) {
        }

        public void b(RecyclerView recyclerView, int i8, int i9) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class t {

        /* renamed from: a  reason: collision with root package name */
        SparseArray<a> f6684a = new SparseArray<>();

        /* renamed from: b  reason: collision with root package name */
        private int f6685b = 0;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class a {

            /* renamed from: a  reason: collision with root package name */
            final ArrayList<b0> f6686a = new ArrayList<>();

            /* renamed from: b  reason: collision with root package name */
            int f6687b = 5;

            /* renamed from: c  reason: collision with root package name */
            long f6688c = 0;

            /* renamed from: d  reason: collision with root package name */
            long f6689d = 0;

            a() {
            }
        }

        private a g(int i8) {
            a aVar = this.f6684a.get(i8);
            if (aVar == null) {
                a aVar2 = new a();
                this.f6684a.put(i8, aVar2);
                return aVar2;
            }
            return aVar;
        }

        void a() {
            this.f6685b++;
        }

        public void b() {
            for (int i8 = 0; i8 < this.f6684a.size(); i8++) {
                this.f6684a.valueAt(i8).f6686a.clear();
            }
        }

        void c() {
            this.f6685b--;
        }

        void d(int i8, long j8) {
            a g8 = g(i8);
            g8.f6689d = j(g8.f6689d, j8);
        }

        void e(int i8, long j8) {
            a g8 = g(i8);
            g8.f6688c = j(g8.f6688c, j8);
        }

        public b0 f(int i8) {
            a aVar = this.f6684a.get(i8);
            if (aVar == null || aVar.f6686a.isEmpty()) {
                return null;
            }
            ArrayList<b0> arrayList = aVar.f6686a;
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (!arrayList.get(size).r()) {
                    return arrayList.remove(size);
                }
            }
            return null;
        }

        void h(g gVar, g gVar2, boolean z4) {
            if (gVar != null) {
                c();
            }
            if (!z4 && this.f6685b == 0) {
                b();
            }
            if (gVar2 != null) {
                a();
            }
        }

        public void i(b0 b0Var) {
            int l8 = b0Var.l();
            ArrayList<b0> arrayList = g(l8).f6686a;
            if (this.f6684a.get(l8).f6687b <= arrayList.size()) {
                return;
            }
            b0Var.D();
            arrayList.add(b0Var);
        }

        long j(long j8, long j9) {
            return j8 == 0 ? j9 : ((j8 / 4) * 3) + (j9 / 4);
        }

        boolean k(int i8, long j8, long j9) {
            long j10 = g(i8).f6689d;
            return j10 == 0 || j8 + j10 < j9;
        }

        boolean l(int i8, long j8, long j9) {
            long j10 = g(i8).f6688c;
            return j10 == 0 || j8 + j10 < j9;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class u {

        /* renamed from: a  reason: collision with root package name */
        final ArrayList<b0> f6690a;

        /* renamed from: b  reason: collision with root package name */
        ArrayList<b0> f6691b;

        /* renamed from: c  reason: collision with root package name */
        final ArrayList<b0> f6692c;

        /* renamed from: d  reason: collision with root package name */
        private final List<b0> f6693d;

        /* renamed from: e  reason: collision with root package name */
        private int f6694e;

        /* renamed from: f  reason: collision with root package name */
        int f6695f;

        /* renamed from: g  reason: collision with root package name */
        t f6696g;

        public u() {
            ArrayList<b0> arrayList = new ArrayList<>();
            this.f6690a = arrayList;
            this.f6691b = null;
            this.f6692c = new ArrayList<>();
            this.f6693d = Collections.unmodifiableList(arrayList);
            this.f6694e = 2;
            this.f6695f = 2;
        }

        private boolean H(b0 b0Var, int i8, int i9, long j8) {
            b0Var.f6644r = RecyclerView.this;
            int l8 = b0Var.l();
            long nanoTime = RecyclerView.this.getNanoTime();
            if (j8 == Long.MAX_VALUE || this.f6696g.k(l8, nanoTime, j8)) {
                RecyclerView.this.f6591m.a(b0Var, i8);
                this.f6696g.d(b0Var.l(), RecyclerView.this.getNanoTime() - nanoTime);
                b(b0Var);
                if (RecyclerView.this.f6604v0.e()) {
                    b0Var.f6634g = i9;
                    return true;
                }
                return true;
            }
            return false;
        }

        private void b(b0 b0Var) {
            if (RecyclerView.this.w0()) {
                View view = b0Var.f6628a;
                if (c0.C(view) == 0) {
                    c0.E0(view, 1);
                }
                androidx.recyclerview.widget.w wVar = RecyclerView.this.C0;
                if (wVar == null) {
                    return;
                }
                androidx.core.view.a n8 = wVar.n();
                if (n8 instanceof w.a) {
                    ((w.a) n8).o(view);
                }
                c0.t0(view, n8);
            }
        }

        private void q(ViewGroup viewGroup, boolean z4) {
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                if (childAt instanceof ViewGroup) {
                    q((ViewGroup) childAt, true);
                }
            }
            if (z4) {
                if (viewGroup.getVisibility() == 4) {
                    viewGroup.setVisibility(0);
                    viewGroup.setVisibility(4);
                    return;
                }
                int visibility = viewGroup.getVisibility();
                viewGroup.setVisibility(4);
                viewGroup.setVisibility(visibility);
            }
        }

        private void r(b0 b0Var) {
            View view = b0Var.f6628a;
            if (view instanceof ViewGroup) {
                q((ViewGroup) view, false);
            }
        }

        void A(int i8) {
            a(this.f6692c.get(i8), true);
            this.f6692c.remove(i8);
        }

        public void B(View view) {
            b0 i02 = RecyclerView.i0(view);
            if (i02.x()) {
                RecyclerView.this.removeDetachedView(view, false);
            }
            if (i02.w()) {
                i02.K();
            } else if (i02.L()) {
                i02.e();
            }
            C(i02);
            if (RecyclerView.this.f6575d0 == null || i02.u()) {
                return;
            }
            RecyclerView.this.f6575d0.j(i02);
        }

        void C(b0 b0Var) {
            boolean z4;
            boolean z8 = true;
            if (b0Var.w() || b0Var.f6628a.getParent() != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Scrapped or attached views may not be recycled. isScrap:");
                sb.append(b0Var.w());
                sb.append(" isAttached:");
                sb.append(b0Var.f6628a.getParent() != null);
                sb.append(RecyclerView.this.Q());
                throw new IllegalArgumentException(sb.toString());
            } else if (b0Var.x()) {
                throw new IllegalArgumentException("Tmp detached view should be removed from RecyclerView before it can be recycled: " + b0Var + RecyclerView.this.Q());
            } else if (b0Var.J()) {
                throw new IllegalArgumentException("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle." + RecyclerView.this.Q());
            } else {
                boolean h8 = b0Var.h();
                g gVar = RecyclerView.this.f6591m;
                if ((gVar != null && h8 && gVar.v(b0Var)) || b0Var.u()) {
                    if (this.f6695f <= 0 || b0Var.p(526)) {
                        z4 = false;
                    } else {
                        int size = this.f6692c.size();
                        if (size >= this.f6695f && size > 0) {
                            A(0);
                            size--;
                        }
                        if (RecyclerView.Q0 && size > 0 && !RecyclerView.this.f6603u0.d(b0Var.f6630c)) {
                            int i8 = size - 1;
                            while (i8 >= 0) {
                                if (!RecyclerView.this.f6603u0.d(this.f6692c.get(i8).f6630c)) {
                                    break;
                                }
                                i8--;
                            }
                            size = i8 + 1;
                        }
                        this.f6692c.add(size, b0Var);
                        z4 = true;
                    }
                    if (z4) {
                        z8 = false;
                    } else {
                        a(b0Var, true);
                    }
                    r1 = z4;
                } else {
                    z8 = false;
                }
                RecyclerView.this.f6578f.q(b0Var);
                if (r1 || z8 || !h8) {
                    return;
                }
                b0Var.f6644r = null;
            }
        }

        void D(View view) {
            ArrayList<b0> arrayList;
            b0 i02 = RecyclerView.i0(view);
            if (!i02.p(12) && i02.y() && !RecyclerView.this.q(i02)) {
                if (this.f6691b == null) {
                    this.f6691b = new ArrayList<>();
                }
                i02.H(this, true);
                arrayList = this.f6691b;
            } else if (i02.t() && !i02.v() && !RecyclerView.this.f6591m.g()) {
                throw new IllegalArgumentException("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool." + RecyclerView.this.Q());
            } else {
                i02.H(this, false);
                arrayList = this.f6690a;
            }
            arrayList.add(i02);
        }

        void E(t tVar) {
            t tVar2 = this.f6696g;
            if (tVar2 != null) {
                tVar2.c();
            }
            this.f6696g = tVar;
            if (tVar == null || RecyclerView.this.getAdapter() == null) {
                return;
            }
            this.f6696g.a();
        }

        void F(z zVar) {
        }

        public void G(int i8) {
            this.f6694e = i8;
            K();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0037  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x005c  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x005f  */
        /* JADX WARN: Removed duplicated region for block: B:62:0x0130  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x014d  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x0170  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x017f  */
        /* JADX WARN: Removed duplicated region for block: B:85:0x01a9  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x01b7  */
        /* JADX WARN: Removed duplicated region for block: B:93:0x01cc A[ADDED_TO_REGION] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public androidx.recyclerview.widget.RecyclerView.b0 I(int r17, boolean r18, long r19) {
            /*
                Method dump skipped, instructions count: 523
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.u.I(int, boolean, long):androidx.recyclerview.widget.RecyclerView$b0");
        }

        void J(b0 b0Var) {
            (b0Var.f6642o ? this.f6691b : this.f6690a).remove(b0Var);
            b0Var.f6641n = null;
            b0Var.f6642o = false;
            b0Var.e();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void K() {
            o oVar = RecyclerView.this.f6593n;
            this.f6695f = this.f6694e + (oVar != null ? oVar.f6673m : 0);
            for (int size = this.f6692c.size() - 1; size >= 0 && this.f6692c.size() > this.f6695f; size--) {
                A(size);
            }
        }

        boolean L(b0 b0Var) {
            if (b0Var.v()) {
                return RecyclerView.this.f6604v0.e();
            }
            int i8 = b0Var.f6630c;
            if (i8 >= 0 && i8 < RecyclerView.this.f6591m.c()) {
                if (RecyclerView.this.f6604v0.e() || RecyclerView.this.f6591m.e(b0Var.f6630c) == b0Var.l()) {
                    return !RecyclerView.this.f6591m.g() || b0Var.k() == RecyclerView.this.f6591m.d(b0Var.f6630c);
                }
                return false;
            }
            throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder adapter position" + b0Var + RecyclerView.this.Q());
        }

        void M(int i8, int i9) {
            int i10;
            int i11 = i9 + i8;
            for (int size = this.f6692c.size() - 1; size >= 0; size--) {
                b0 b0Var = this.f6692c.get(size);
                if (b0Var != null && (i10 = b0Var.f6630c) >= i8 && i10 < i11) {
                    b0Var.b(2);
                    A(size);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void a(b0 b0Var, boolean z4) {
            RecyclerView.s(b0Var);
            View view = b0Var.f6628a;
            androidx.recyclerview.widget.w wVar = RecyclerView.this.C0;
            if (wVar != null) {
                androidx.core.view.a n8 = wVar.n();
                c0.t0(view, n8 instanceof w.a ? ((w.a) n8).n(view) : null);
            }
            if (z4) {
                g(b0Var);
            }
            b0Var.f6644r = null;
            i().i(b0Var);
        }

        public void c() {
            this.f6690a.clear();
            z();
        }

        void d() {
            int size = this.f6692c.size();
            for (int i8 = 0; i8 < size; i8++) {
                this.f6692c.get(i8).c();
            }
            int size2 = this.f6690a.size();
            for (int i9 = 0; i9 < size2; i9++) {
                this.f6690a.get(i9).c();
            }
            ArrayList<b0> arrayList = this.f6691b;
            if (arrayList != null) {
                int size3 = arrayList.size();
                for (int i10 = 0; i10 < size3; i10++) {
                    this.f6691b.get(i10).c();
                }
            }
        }

        void e() {
            this.f6690a.clear();
            ArrayList<b0> arrayList = this.f6691b;
            if (arrayList != null) {
                arrayList.clear();
            }
        }

        public int f(int i8) {
            if (i8 >= 0 && i8 < RecyclerView.this.f6604v0.b()) {
                return !RecyclerView.this.f6604v0.e() ? i8 : RecyclerView.this.f6574d.m(i8);
            }
            throw new IndexOutOfBoundsException("invalid position " + i8 + ". State item count is " + RecyclerView.this.f6604v0.b() + RecyclerView.this.Q());
        }

        void g(b0 b0Var) {
            v vVar = RecyclerView.this.f6596p;
            if (vVar != null) {
                vVar.a(b0Var);
            }
            g gVar = RecyclerView.this.f6591m;
            if (gVar != null) {
                gVar.y(b0Var);
            }
            RecyclerView recyclerView = RecyclerView.this;
            if (recyclerView.f6604v0 != null) {
                recyclerView.f6578f.q(b0Var);
            }
        }

        b0 h(int i8) {
            int size;
            int m8;
            ArrayList<b0> arrayList = this.f6691b;
            if (arrayList != null && (size = arrayList.size()) != 0) {
                for (int i9 = 0; i9 < size; i9++) {
                    b0 b0Var = this.f6691b.get(i9);
                    if (!b0Var.L() && b0Var.m() == i8) {
                        b0Var.b(32);
                        return b0Var;
                    }
                }
                if (RecyclerView.this.f6591m.g() && (m8 = RecyclerView.this.f6574d.m(i8)) > 0 && m8 < RecyclerView.this.f6591m.c()) {
                    long d8 = RecyclerView.this.f6591m.d(m8);
                    for (int i10 = 0; i10 < size; i10++) {
                        b0 b0Var2 = this.f6691b.get(i10);
                        if (!b0Var2.L() && b0Var2.k() == d8) {
                            b0Var2.b(32);
                            return b0Var2;
                        }
                    }
                }
            }
            return null;
        }

        t i() {
            if (this.f6696g == null) {
                this.f6696g = new t();
            }
            return this.f6696g;
        }

        int j() {
            return this.f6690a.size();
        }

        public List<b0> k() {
            return this.f6693d;
        }

        b0 l(long j8, int i8, boolean z4) {
            for (int size = this.f6690a.size() - 1; size >= 0; size--) {
                b0 b0Var = this.f6690a.get(size);
                if (b0Var.k() == j8 && !b0Var.L()) {
                    if (i8 == b0Var.l()) {
                        b0Var.b(32);
                        if (b0Var.v() && !RecyclerView.this.f6604v0.e()) {
                            b0Var.F(2, 14);
                        }
                        return b0Var;
                    } else if (!z4) {
                        this.f6690a.remove(size);
                        RecyclerView.this.removeDetachedView(b0Var.f6628a, false);
                        y(b0Var.f6628a);
                    }
                }
            }
            int size2 = this.f6692c.size();
            while (true) {
                size2--;
                if (size2 < 0) {
                    return null;
                }
                b0 b0Var2 = this.f6692c.get(size2);
                if (b0Var2.k() == j8 && !b0Var2.r()) {
                    if (i8 == b0Var2.l()) {
                        if (!z4) {
                            this.f6692c.remove(size2);
                        }
                        return b0Var2;
                    } else if (!z4) {
                        A(size2);
                        return null;
                    }
                }
            }
        }

        b0 m(int i8, boolean z4) {
            View e8;
            int size = this.f6690a.size();
            for (int i9 = 0; i9 < size; i9++) {
                b0 b0Var = this.f6690a.get(i9);
                if (!b0Var.L() && b0Var.m() == i8 && !b0Var.t() && (RecyclerView.this.f6604v0.f6721h || !b0Var.v())) {
                    b0Var.b(32);
                    return b0Var;
                }
            }
            if (z4 || (e8 = RecyclerView.this.f6576e.e(i8)) == null) {
                int size2 = this.f6692c.size();
                for (int i10 = 0; i10 < size2; i10++) {
                    b0 b0Var2 = this.f6692c.get(i10);
                    if (!b0Var2.t() && b0Var2.m() == i8 && !b0Var2.r()) {
                        if (!z4) {
                            this.f6692c.remove(i10);
                        }
                        return b0Var2;
                    }
                }
                return null;
            }
            b0 i02 = RecyclerView.i0(e8);
            RecyclerView.this.f6576e.s(e8);
            int m8 = RecyclerView.this.f6576e.m(e8);
            if (m8 != -1) {
                RecyclerView.this.f6576e.d(m8);
                D(e8);
                i02.b(8224);
                return i02;
            }
            throw new IllegalStateException("layout index should not be -1 after unhiding a view:" + i02 + RecyclerView.this.Q());
        }

        View n(int i8) {
            return this.f6690a.get(i8).f6628a;
        }

        public View o(int i8) {
            return p(i8, false);
        }

        View p(int i8, boolean z4) {
            return I(i8, z4, Long.MAX_VALUE).f6628a;
        }

        void s() {
            int size = this.f6692c.size();
            for (int i8 = 0; i8 < size; i8++) {
                LayoutParams layoutParams = (LayoutParams) this.f6692c.get(i8).f6628a.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.f6615c = true;
                }
            }
        }

        void t() {
            int size = this.f6692c.size();
            for (int i8 = 0; i8 < size; i8++) {
                b0 b0Var = this.f6692c.get(i8);
                if (b0Var != null) {
                    b0Var.b(6);
                    b0Var.a(null);
                }
            }
            g gVar = RecyclerView.this.f6591m;
            if (gVar == null || !gVar.g()) {
                z();
            }
        }

        void u(int i8, int i9) {
            int size = this.f6692c.size();
            for (int i10 = 0; i10 < size; i10++) {
                b0 b0Var = this.f6692c.get(i10);
                if (b0Var != null && b0Var.f6630c >= i8) {
                    b0Var.A(i9, true);
                }
            }
        }

        void v(int i8, int i9) {
            int i10;
            int i11;
            int i12;
            int i13;
            if (i8 < i9) {
                i10 = -1;
                i12 = i8;
                i11 = i9;
            } else {
                i10 = 1;
                i11 = i8;
                i12 = i9;
            }
            int size = this.f6692c.size();
            for (int i14 = 0; i14 < size; i14++) {
                b0 b0Var = this.f6692c.get(i14);
                if (b0Var != null && (i13 = b0Var.f6630c) >= i12 && i13 <= i11) {
                    if (i13 == i8) {
                        b0Var.A(i9 - i8, false);
                    } else {
                        b0Var.A(i10, false);
                    }
                }
            }
        }

        void w(int i8, int i9, boolean z4) {
            int i10 = i8 + i9;
            for (int size = this.f6692c.size() - 1; size >= 0; size--) {
                b0 b0Var = this.f6692c.get(size);
                if (b0Var != null) {
                    int i11 = b0Var.f6630c;
                    if (i11 >= i10) {
                        b0Var.A(-i9, z4);
                    } else if (i11 >= i8) {
                        b0Var.b(8);
                        A(size);
                    }
                }
            }
        }

        void x(g gVar, g gVar2, boolean z4) {
            c();
            i().h(gVar, gVar2, z4);
        }

        void y(View view) {
            b0 i02 = RecyclerView.i0(view);
            i02.f6641n = null;
            i02.f6642o = false;
            i02.e();
            C(i02);
        }

        void z() {
            for (int size = this.f6692c.size() - 1; size >= 0; size--) {
                A(size);
            }
            this.f6692c.clear();
            if (RecyclerView.Q0) {
                RecyclerView.this.f6603u0.b();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface v {
        void a(b0 b0Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class w extends i {
        w() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.i
        public void a() {
            RecyclerView.this.p(null);
            RecyclerView recyclerView = RecyclerView.this;
            recyclerView.f6604v0.f6720g = true;
            recyclerView.S0(true);
            if (RecyclerView.this.f6574d.p()) {
                return;
            }
            RecyclerView.this.requestLayout();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.i
        public void c(int i8, int i9, Object obj) {
            RecyclerView.this.p(null);
            if (RecyclerView.this.f6574d.r(i8, i9, obj)) {
                g();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.i
        public void d(int i8, int i9) {
            RecyclerView.this.p(null);
            if (RecyclerView.this.f6574d.s(i8, i9)) {
                g();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.i
        public void e(int i8, int i9, int i10) {
            RecyclerView.this.p(null);
            if (RecyclerView.this.f6574d.t(i8, i9, i10)) {
                g();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.i
        public void f(int i8, int i9) {
            RecyclerView.this.p(null);
            if (RecyclerView.this.f6574d.u(i8, i9)) {
                g();
            }
        }

        void g() {
            if (RecyclerView.P0) {
                RecyclerView recyclerView = RecyclerView.this;
                if (recyclerView.f6609y && recyclerView.f6607x) {
                    c0.l0(recyclerView, recyclerView.f6582h);
                    return;
                }
            }
            RecyclerView recyclerView2 = RecyclerView.this;
            recyclerView2.H = true;
            recyclerView2.requestLayout();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class x {

        /* renamed from: b  reason: collision with root package name */
        private RecyclerView f6700b;

        /* renamed from: c  reason: collision with root package name */
        private o f6701c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f6702d;

        /* renamed from: e  reason: collision with root package name */
        private boolean f6703e;

        /* renamed from: f  reason: collision with root package name */
        private View f6704f;

        /* renamed from: h  reason: collision with root package name */
        private boolean f6706h;

        /* renamed from: a  reason: collision with root package name */
        private int f6699a = -1;

        /* renamed from: g  reason: collision with root package name */
        private final a f6705g = new a(0, 0);

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class a {

            /* renamed from: a  reason: collision with root package name */
            private int f6707a;

            /* renamed from: b  reason: collision with root package name */
            private int f6708b;

            /* renamed from: c  reason: collision with root package name */
            private int f6709c;

            /* renamed from: d  reason: collision with root package name */
            private int f6710d;

            /* renamed from: e  reason: collision with root package name */
            private Interpolator f6711e;

            /* renamed from: f  reason: collision with root package name */
            private boolean f6712f;

            /* renamed from: g  reason: collision with root package name */
            private int f6713g;

            public a(int i8, int i9) {
                this(i8, i9, Integer.MIN_VALUE, null);
            }

            public a(int i8, int i9, int i10, Interpolator interpolator) {
                this.f6710d = -1;
                this.f6712f = false;
                this.f6713g = 0;
                this.f6707a = i8;
                this.f6708b = i9;
                this.f6709c = i10;
                this.f6711e = interpolator;
            }

            private void e() {
                if (this.f6711e != null && this.f6709c < 1) {
                    throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
                }
                if (this.f6709c < 1) {
                    throw new IllegalStateException("Scroll duration must be a positive number");
                }
            }

            boolean a() {
                return this.f6710d >= 0;
            }

            public void b(int i8) {
                this.f6710d = i8;
            }

            void c(RecyclerView recyclerView) {
                int i8 = this.f6710d;
                if (i8 >= 0) {
                    this.f6710d = -1;
                    recyclerView.z0(i8);
                    this.f6712f = false;
                } else if (!this.f6712f) {
                    this.f6713g = 0;
                } else {
                    e();
                    recyclerView.f6600s0.f(this.f6707a, this.f6708b, this.f6709c, this.f6711e);
                    int i9 = this.f6713g + 1;
                    this.f6713g = i9;
                    if (i9 > 10) {
                        Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                    }
                    this.f6712f = false;
                }
            }

            public void d(int i8, int i9, int i10, Interpolator interpolator) {
                this.f6707a = i8;
                this.f6708b = i9;
                this.f6709c = i10;
                this.f6711e = interpolator;
                this.f6712f = true;
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface b {
            PointF a(int i8);
        }

        public PointF a(int i8) {
            o e8 = e();
            if (e8 instanceof b) {
                return ((b) e8).a(i8);
            }
            Log.w("RecyclerView", "You should override computeScrollVectorForPosition when the LayoutManager does not implement " + b.class.getCanonicalName());
            return null;
        }

        public View b(int i8) {
            return this.f6700b.f6593n.D(i8);
        }

        public int c() {
            return this.f6700b.f6593n.K();
        }

        public int d(View view) {
            return this.f6700b.g0(view);
        }

        public o e() {
            return this.f6701c;
        }

        public int f() {
            return this.f6699a;
        }

        public boolean g() {
            return this.f6702d;
        }

        public boolean h() {
            return this.f6703e;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void i(PointF pointF) {
            float f5 = pointF.x;
            float f8 = pointF.y;
            float sqrt = (float) Math.sqrt((f5 * f5) + (f8 * f8));
            pointF.x /= sqrt;
            pointF.y /= sqrt;
        }

        void j(int i8, int i9) {
            PointF a9;
            RecyclerView recyclerView = this.f6700b;
            if (this.f6699a == -1 || recyclerView == null) {
                r();
            }
            if (this.f6702d && this.f6704f == null && this.f6701c != null && (a9 = a(this.f6699a)) != null) {
                float f5 = a9.x;
                if (f5 != 0.0f || a9.y != 0.0f) {
                    recyclerView.k1((int) Math.signum(f5), (int) Math.signum(a9.y), null);
                }
            }
            this.f6702d = false;
            View view = this.f6704f;
            if (view != null) {
                if (d(view) == this.f6699a) {
                    o(this.f6704f, recyclerView.f6604v0, this.f6705g);
                    this.f6705g.c(recyclerView);
                    r();
                } else {
                    Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
                    this.f6704f = null;
                }
            }
            if (this.f6703e) {
                l(i8, i9, recyclerView.f6604v0, this.f6705g);
                boolean a10 = this.f6705g.a();
                this.f6705g.c(recyclerView);
                if (a10 && this.f6703e) {
                    this.f6702d = true;
                    recyclerView.f6600s0.e();
                }
            }
        }

        protected void k(View view) {
            if (d(view) == f()) {
                this.f6704f = view;
            }
        }

        protected abstract void l(int i8, int i9, y yVar, a aVar);

        protected abstract void m();

        protected abstract void n();

        protected abstract void o(View view, y yVar, a aVar);

        public void p(int i8) {
            this.f6699a = i8;
        }

        void q(RecyclerView recyclerView, o oVar) {
            recyclerView.f6600s0.g();
            if (this.f6706h) {
                Log.w("RecyclerView", "An instance of " + getClass().getSimpleName() + " was started more than once. Each instance of" + getClass().getSimpleName() + " is intended to only be used once. You should create a new instance for each use.");
            }
            this.f6700b = recyclerView;
            this.f6701c = oVar;
            int i8 = this.f6699a;
            if (i8 == -1) {
                throw new IllegalArgumentException("Invalid target position");
            }
            recyclerView.f6604v0.f6714a = i8;
            this.f6703e = true;
            this.f6702d = true;
            this.f6704f = b(f());
            m();
            this.f6700b.f6600s0.e();
            this.f6706h = true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final void r() {
            if (this.f6703e) {
                this.f6703e = false;
                n();
                this.f6700b.f6604v0.f6714a = -1;
                this.f6704f = null;
                this.f6699a = -1;
                this.f6702d = false;
                this.f6701c.g1(this);
                this.f6701c = null;
                this.f6700b = null;
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class y {

        /* renamed from: b  reason: collision with root package name */
        private SparseArray<Object> f6715b;

        /* renamed from: m  reason: collision with root package name */
        int f6726m;

        /* renamed from: n  reason: collision with root package name */
        long f6727n;

        /* renamed from: o  reason: collision with root package name */
        int f6728o;

        /* renamed from: p  reason: collision with root package name */
        int f6729p;
        int q;

        /* renamed from: a  reason: collision with root package name */
        int f6714a = -1;

        /* renamed from: c  reason: collision with root package name */
        int f6716c = 0;

        /* renamed from: d  reason: collision with root package name */
        int f6717d = 0;

        /* renamed from: e  reason: collision with root package name */
        int f6718e = 1;

        /* renamed from: f  reason: collision with root package name */
        int f6719f = 0;

        /* renamed from: g  reason: collision with root package name */
        boolean f6720g = false;

        /* renamed from: h  reason: collision with root package name */
        boolean f6721h = false;

        /* renamed from: i  reason: collision with root package name */
        boolean f6722i = false;

        /* renamed from: j  reason: collision with root package name */
        boolean f6723j = false;

        /* renamed from: k  reason: collision with root package name */
        boolean f6724k = false;

        /* renamed from: l  reason: collision with root package name */
        boolean f6725l = false;

        void a(int i8) {
            if ((this.f6718e & i8) != 0) {
                return;
            }
            throw new IllegalStateException("Layout state should be one of " + Integer.toBinaryString(i8) + " but it is " + Integer.toBinaryString(this.f6718e));
        }

        public int b() {
            return this.f6721h ? this.f6716c - this.f6717d : this.f6719f;
        }

        public int c() {
            return this.f6714a;
        }

        public boolean d() {
            return this.f6714a != -1;
        }

        public boolean e() {
            return this.f6721h;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void f(g gVar) {
            this.f6718e = 1;
            this.f6719f = gVar.c();
            this.f6721h = false;
            this.f6722i = false;
            this.f6723j = false;
        }

        public boolean g() {
            return this.f6725l;
        }

        public String toString() {
            return "State{mTargetPosition=" + this.f6714a + ", mData=" + this.f6715b + ", mItemCount=" + this.f6719f + ", mIsMeasuring=" + this.f6723j + ", mPreviousLayoutItemCount=" + this.f6716c + ", mDeletedInvisibleItemCountSincePreviousLayout=" + this.f6717d + ", mStructureChanged=" + this.f6720g + ", mInPreLayout=" + this.f6721h + ", mRunSimpleAnimations=" + this.f6724k + ", mRunPredictiveAnimations=" + this.f6725l + '}';
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class z {
    }

    static {
        int i8 = Build.VERSION.SDK_INT;
        N0 = i8 == 18 || i8 == 19 || i8 == 20;
        O0 = i8 >= 23;
        P0 = i8 >= 16;
        Q0 = i8 >= 21;
        R0 = i8 <= 15;
        S0 = i8 <= 15;
        Class<?> cls = Integer.TYPE;
        T0 = new Class[]{Context.class, AttributeSet.class, cls, cls};
        U0 = new c();
    }

    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, p1.a.f22264a);
    }

    public RecyclerView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f6568a = new w();
        this.f6570b = new u();
        this.f6578f = new androidx.recyclerview.widget.b0();
        this.f6582h = new a();
        this.f6585j = new Rect();
        this.f6587k = new Rect();
        this.f6589l = new RectF();
        this.q = new ArrayList<>();
        this.f6601t = new ArrayList<>();
        this.B = 0;
        this.O = false;
        this.P = false;
        this.Q = 0;
        this.R = 0;
        this.T = new k();
        this.f6575d0 = new androidx.recyclerview.widget.g();
        this.f6577e0 = 0;
        this.f6579f0 = -1;
        this.f6597p0 = Float.MIN_VALUE;
        this.f6598q0 = Float.MIN_VALUE;
        boolean z4 = true;
        this.f6599r0 = true;
        this.f6600s0 = new a0();
        this.f6603u0 = Q0 ? new k.b() : null;
        this.f6604v0 = new y();
        this.f6610y0 = false;
        this.f6612z0 = false;
        this.A0 = new m();
        this.B0 = false;
        this.E0 = new int[2];
        this.G0 = new int[2];
        this.H0 = new int[2];
        this.I0 = new int[2];
        this.J0 = new ArrayList();
        this.K0 = new b();
        this.L0 = new d();
        setScrollContainer(true);
        setFocusableInTouchMode(true);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.f6590l0 = viewConfiguration.getScaledTouchSlop();
        this.f6597p0 = e0.b(viewConfiguration, context);
        this.f6598q0 = e0.d(viewConfiguration, context);
        this.f6594n0 = viewConfiguration.getScaledMinimumFlingVelocity();
        this.f6595o0 = viewConfiguration.getScaledMaximumFlingVelocity();
        setWillNotDraw(getOverScrollMode() == 2);
        this.f6575d0.w(this.A0);
        r0();
        t0();
        s0();
        if (c0.C(this) == 0) {
            c0.E0(this, 1);
        }
        this.K = (AccessibilityManager) getContext().getSystemService("accessibility");
        setAccessibilityDelegateCompat(new androidx.recyclerview.widget.w(this));
        int[] iArr = p1.d.f22277f;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i8, 0);
        int i9 = Build.VERSION.SDK_INT;
        if (i9 >= 29) {
            saveAttributeDataForStyleable(context, iArr, attributeSet, obtainStyledAttributes, i8, 0);
        }
        String string = obtainStyledAttributes.getString(p1.d.f22286o);
        if (obtainStyledAttributes.getInt(p1.d.f22280i, -1) == -1) {
            setDescendantFocusability(262144);
        }
        this.f6580g = obtainStyledAttributes.getBoolean(p1.d.f22279h, true);
        boolean z8 = obtainStyledAttributes.getBoolean(p1.d.f22281j, false);
        this.f6611z = z8;
        if (z8) {
            u0((StateListDrawable) obtainStyledAttributes.getDrawable(p1.d.f22284m), obtainStyledAttributes.getDrawable(p1.d.f22285n), (StateListDrawable) obtainStyledAttributes.getDrawable(p1.d.f22282k), obtainStyledAttributes.getDrawable(p1.d.f22283l));
        }
        obtainStyledAttributes.recycle();
        w(context, string, attributeSet, i8, 0);
        if (i9 >= 21) {
            int[] iArr2 = M0;
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, iArr2, i8, 0);
            if (i9 >= 29) {
                saveAttributeDataForStyleable(context, iArr2, attributeSet, obtainStyledAttributes2, i8, 0);
            }
            z4 = obtainStyledAttributes2.getBoolean(0, true);
            obtainStyledAttributes2.recycle();
        }
        setNestedScrollingEnabled(z4);
    }

    private void B() {
        int i8 = this.G;
        this.G = 0;
        if (i8 == 0 || !w0()) {
            return;
        }
        AccessibilityEvent obtain = AccessibilityEvent.obtain();
        obtain.setEventType(RecognitionOptions.PDF417);
        androidx.core.view.accessibility.b.b(obtain, i8);
        sendAccessibilityEventUnchecked(obtain);
    }

    private void D() {
        boolean z4 = true;
        this.f6604v0.a(1);
        R(this.f6604v0);
        this.f6604v0.f6723j = false;
        u1();
        this.f6578f.f();
        J0();
        R0();
        h1();
        y yVar = this.f6604v0;
        yVar.f6722i = (yVar.f6724k && this.f6612z0) ? false : false;
        this.f6612z0 = false;
        this.f6610y0 = false;
        yVar.f6721h = yVar.f6725l;
        yVar.f6719f = this.f6591m.c();
        W(this.E0);
        if (this.f6604v0.f6724k) {
            int g8 = this.f6576e.g();
            for (int i8 = 0; i8 < g8; i8++) {
                b0 i02 = i0(this.f6576e.f(i8));
                if (!i02.J() && (!i02.t() || this.f6591m.g())) {
                    this.f6578f.e(i02, this.f6575d0.u(this.f6604v0, i02, l.e(i02), i02.o()));
                    if (this.f6604v0.f6722i && i02.y() && !i02.v() && !i02.J() && !i02.t()) {
                        this.f6578f.c(e0(i02), i02);
                    }
                }
            }
        }
        if (this.f6604v0.f6725l) {
            i1();
            y yVar2 = this.f6604v0;
            boolean z8 = yVar2.f6720g;
            yVar2.f6720g = false;
            this.f6593n.Y0(this.f6570b, yVar2);
            this.f6604v0.f6720g = z8;
            for (int i9 = 0; i9 < this.f6576e.g(); i9++) {
                b0 i03 = i0(this.f6576e.f(i9));
                if (!i03.J() && !this.f6578f.i(i03)) {
                    int e8 = l.e(i03);
                    boolean p8 = i03.p(8192);
                    if (!p8) {
                        e8 |= RecognitionOptions.AZTEC;
                    }
                    l.c u8 = this.f6575d0.u(this.f6604v0, i03, e8, i03.o());
                    if (p8) {
                        U0(i03, u8);
                    } else {
                        this.f6578f.a(i03, u8);
                    }
                }
            }
        }
        t();
        K0();
        w1(false);
        this.f6604v0.f6718e = 2;
    }

    private void E() {
        u1();
        J0();
        this.f6604v0.a(6);
        this.f6574d.j();
        this.f6604v0.f6719f = this.f6591m.c();
        y yVar = this.f6604v0;
        yVar.f6717d = 0;
        yVar.f6721h = false;
        this.f6593n.Y0(this.f6570b, yVar);
        y yVar2 = this.f6604v0;
        yVar2.f6720g = false;
        this.f6572c = null;
        yVar2.f6724k = yVar2.f6724k && this.f6575d0 != null;
        yVar2.f6718e = 4;
        K0();
        w1(false);
    }

    private void F() {
        this.f6604v0.a(4);
        u1();
        J0();
        y yVar = this.f6604v0;
        yVar.f6718e = 1;
        if (yVar.f6724k) {
            for (int g8 = this.f6576e.g() - 1; g8 >= 0; g8--) {
                b0 i02 = i0(this.f6576e.f(g8));
                if (!i02.J()) {
                    long e02 = e0(i02);
                    l.c t8 = this.f6575d0.t(this.f6604v0, i02);
                    b0 g9 = this.f6578f.g(e02);
                    if (g9 != null && !g9.J()) {
                        boolean h8 = this.f6578f.h(g9);
                        boolean h9 = this.f6578f.h(i02);
                        if (!h8 || g9 != i02) {
                            l.c n8 = this.f6578f.n(g9);
                            this.f6578f.d(i02, t8);
                            l.c m8 = this.f6578f.m(i02);
                            if (n8 == null) {
                                o0(e02, i02, g9);
                            } else {
                                n(g9, i02, n8, m8, h8, h9);
                            }
                        }
                    }
                    this.f6578f.d(i02, t8);
                }
            }
            this.f6578f.o(this.L0);
        }
        this.f6593n.m1(this.f6570b);
        y yVar2 = this.f6604v0;
        yVar2.f6716c = yVar2.f6719f;
        this.O = false;
        this.P = false;
        yVar2.f6724k = false;
        yVar2.f6725l = false;
        this.f6593n.f6668h = false;
        ArrayList<b0> arrayList = this.f6570b.f6691b;
        if (arrayList != null) {
            arrayList.clear();
        }
        o oVar = this.f6593n;
        if (oVar.f6674n) {
            oVar.f6673m = 0;
            oVar.f6674n = false;
            this.f6570b.K();
        }
        this.f6593n.Z0(this.f6604v0);
        K0();
        w1(false);
        this.f6578f.f();
        int[] iArr = this.E0;
        if (y(iArr[0], iArr[1])) {
            J(0, 0);
        }
        V0();
        f1();
    }

    private boolean L(MotionEvent motionEvent) {
        r rVar = this.f6605w;
        if (rVar == null) {
            if (motionEvent.getAction() == 0) {
                return false;
            }
            return V(motionEvent);
        }
        rVar.a(this, motionEvent);
        int action = motionEvent.getAction();
        if (action == 3 || action == 1) {
            this.f6605w = null;
        }
        return true;
    }

    private void M0(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.f6579f0) {
            int i8 = actionIndex == 0 ? 1 : 0;
            this.f6579f0 = motionEvent.getPointerId(i8);
            int x8 = (int) (motionEvent.getX(i8) + 0.5f);
            this.f6586j0 = x8;
            this.f6583h0 = x8;
            int y8 = (int) (motionEvent.getY(i8) + 0.5f);
            this.f6588k0 = y8;
            this.f6584i0 = y8;
        }
    }

    private boolean Q0() {
        return this.f6575d0 != null && this.f6593n.M1();
    }

    private void R0() {
        boolean z4;
        if (this.O) {
            this.f6574d.y();
            if (this.P) {
                this.f6593n.T0(this);
            }
        }
        if (Q0()) {
            this.f6574d.w();
        } else {
            this.f6574d.j();
        }
        boolean z8 = false;
        boolean z9 = this.f6610y0 || this.f6612z0;
        this.f6604v0.f6724k = this.A && this.f6575d0 != null && ((z4 = this.O) || z9 || this.f6593n.f6668h) && (!z4 || this.f6591m.g());
        y yVar = this.f6604v0;
        if (yVar.f6724k && z9 && !this.O && Q0()) {
            z8 = true;
        }
        yVar.f6725l = z8;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003d  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0053  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void T0(float r7, float r8, float r9, float r10) {
        /*
            r6 = this;
            r0 = 0
            int r1 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            r2 = 1065353216(0x3f800000, float:1.0)
            r3 = 1
            if (r1 >= 0) goto L21
            r6.N()
            android.widget.EdgeEffect r1 = r6.W
            float r4 = -r8
            int r5 = r6.getWidth()
            float r5 = (float) r5
            float r4 = r4 / r5
            int r5 = r6.getHeight()
            float r5 = (float) r5
            float r9 = r9 / r5
            float r9 = r2 - r9
        L1c:
            androidx.core.widget.f.c(r1, r4, r9)
            r9 = r3
            goto L39
        L21:
            int r1 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r1 <= 0) goto L38
            r6.O()
            android.widget.EdgeEffect r1 = r6.f6571b0
            int r4 = r6.getWidth()
            float r4 = (float) r4
            float r4 = r8 / r4
            int r5 = r6.getHeight()
            float r5 = (float) r5
            float r9 = r9 / r5
            goto L1c
        L38:
            r9 = 0
        L39:
            int r1 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
            if (r1 >= 0) goto L53
            r6.P()
            android.widget.EdgeEffect r9 = r6.f6569a0
            float r1 = -r10
            int r2 = r6.getHeight()
            float r2 = (float) r2
            float r1 = r1 / r2
            int r2 = r6.getWidth()
            float r2 = (float) r2
            float r7 = r7 / r2
            androidx.core.widget.f.c(r9, r1, r7)
            goto L6f
        L53:
            int r1 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
            if (r1 <= 0) goto L6e
            r6.M()
            android.widget.EdgeEffect r9 = r6.f6573c0
            int r1 = r6.getHeight()
            float r1 = (float) r1
            float r1 = r10 / r1
            int r4 = r6.getWidth()
            float r4 = (float) r4
            float r7 = r7 / r4
            float r2 = r2 - r7
            androidx.core.widget.f.c(r9, r1, r2)
            goto L6f
        L6e:
            r3 = r9
        L6f:
            if (r3 != 0) goto L79
            int r7 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
            if (r7 != 0) goto L79
            int r7 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
            if (r7 == 0) goto L7c
        L79:
            androidx.core.view.c0.j0(r6)
        L7c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.T0(float, float, float, float):void");
    }

    private boolean V(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int size = this.f6601t.size();
        for (int i8 = 0; i8 < size; i8++) {
            r rVar = this.f6601t.get(i8);
            if (rVar.c(this, motionEvent) && action != 3) {
                this.f6605w = rVar;
                return true;
            }
        }
        return false;
    }

    private void V0() {
        View findViewById;
        if (!this.f6599r0 || this.f6591m == null || !hasFocus() || getDescendantFocusability() == 393216) {
            return;
        }
        if (getDescendantFocusability() == 131072 && isFocused()) {
            return;
        }
        if (!isFocused()) {
            View focusedChild = getFocusedChild();
            if (!S0 || (focusedChild.getParent() != null && focusedChild.hasFocus())) {
                if (!this.f6576e.n(focusedChild)) {
                    return;
                }
            } else if (this.f6576e.g() == 0) {
                requestFocus();
                return;
            }
        }
        View view = null;
        b0 a02 = (this.f6604v0.f6727n == -1 || !this.f6591m.g()) ? null : a0(this.f6604v0.f6727n);
        if (a02 != null && !this.f6576e.n(a02.f6628a) && a02.f6628a.hasFocusable()) {
            view = a02.f6628a;
        } else if (this.f6576e.g() > 0) {
            view = Y();
        }
        if (view != null) {
            int i8 = this.f6604v0.f6728o;
            if (i8 != -1 && (findViewById = view.findViewById(i8)) != null && findViewById.isFocusable()) {
                view = findViewById;
            }
            view.requestFocus();
        }
    }

    private void W(int[] iArr) {
        int g8 = this.f6576e.g();
        if (g8 == 0) {
            iArr[0] = -1;
            iArr[1] = -1;
            return;
        }
        int i8 = Integer.MAX_VALUE;
        int i9 = Integer.MIN_VALUE;
        for (int i10 = 0; i10 < g8; i10++) {
            b0 i02 = i0(this.f6576e.f(i10));
            if (!i02.J()) {
                int m8 = i02.m();
                if (m8 < i8) {
                    i8 = m8;
                }
                if (m8 > i9) {
                    i9 = m8;
                }
            }
        }
        iArr[0] = i8;
        iArr[1] = i9;
    }

    private void W0() {
        boolean z4;
        EdgeEffect edgeEffect = this.W;
        if (edgeEffect != null) {
            edgeEffect.onRelease();
            z4 = this.W.isFinished();
        } else {
            z4 = false;
        }
        EdgeEffect edgeEffect2 = this.f6569a0;
        if (edgeEffect2 != null) {
            edgeEffect2.onRelease();
            z4 |= this.f6569a0.isFinished();
        }
        EdgeEffect edgeEffect3 = this.f6571b0;
        if (edgeEffect3 != null) {
            edgeEffect3.onRelease();
            z4 |= this.f6571b0.isFinished();
        }
        EdgeEffect edgeEffect4 = this.f6573c0;
        if (edgeEffect4 != null) {
            edgeEffect4.onRelease();
            z4 |= this.f6573c0.isFinished();
        }
        if (z4) {
            c0.j0(this);
        }
    }

    static RecyclerView X(View view) {
        if (view instanceof ViewGroup) {
            if (view instanceof RecyclerView) {
                return (RecyclerView) view;
            }
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                RecyclerView X = X(viewGroup.getChildAt(i8));
                if (X != null) {
                    return X;
                }
            }
            return null;
        }
        return null;
    }

    private View Y() {
        b0 Z;
        y yVar = this.f6604v0;
        int i8 = yVar.f6726m;
        if (i8 == -1) {
            i8 = 0;
        }
        int b9 = yVar.b();
        for (int i9 = i8; i9 < b9; i9++) {
            b0 Z2 = Z(i9);
            if (Z2 == null) {
                break;
            } else if (Z2.f6628a.hasFocusable()) {
                return Z2.f6628a;
            }
        }
        int min = Math.min(b9, i8);
        while (true) {
            min--;
            if (min < 0 || (Z = Z(min)) == null) {
                return null;
            }
            if (Z.f6628a.hasFocusable()) {
                return Z.f6628a;
            }
        }
    }

    private void e1(View view, View view2) {
        View view3 = view2 != null ? view2 : view;
        this.f6585j.set(0, 0, view3.getWidth(), view3.getHeight());
        ViewGroup.LayoutParams layoutParams = view3.getLayoutParams();
        if (layoutParams instanceof LayoutParams) {
            LayoutParams layoutParams2 = (LayoutParams) layoutParams;
            if (!layoutParams2.f6615c) {
                Rect rect = layoutParams2.f6614b;
                Rect rect2 = this.f6585j;
                rect2.left -= rect.left;
                rect2.right += rect.right;
                rect2.top -= rect.top;
                rect2.bottom += rect.bottom;
            }
        }
        if (view2 != null) {
            offsetDescendantRectToMyCoords(view2, this.f6585j);
            offsetRectIntoDescendantCoords(view, this.f6585j);
        }
        this.f6593n.t1(this, view, this.f6585j, !this.A, view2 == null);
    }

    private void f1() {
        y yVar = this.f6604v0;
        yVar.f6727n = -1L;
        yVar.f6726m = -1;
        yVar.f6728o = -1;
    }

    private void g(b0 b0Var) {
        View view = b0Var.f6628a;
        boolean z4 = view.getParent() == this;
        this.f6570b.J(h0(view));
        if (b0Var.x()) {
            this.f6576e.c(view, -1, view.getLayoutParams(), true);
            return;
        }
        androidx.recyclerview.widget.f fVar = this.f6576e;
        if (z4) {
            fVar.k(view);
        } else {
            fVar.b(view, true);
        }
    }

    private void g1() {
        VelocityTracker velocityTracker = this.f6581g0;
        if (velocityTracker != null) {
            velocityTracker.clear();
        }
        x1(0);
        W0();
    }

    private androidx.core.view.q getScrollingChildHelper() {
        if (this.F0 == null) {
            this.F0 = new androidx.core.view.q(this);
        }
        return this.F0;
    }

    private void h1() {
        View focusedChild = (this.f6599r0 && hasFocus() && this.f6591m != null) ? getFocusedChild() : null;
        b0 U = focusedChild != null ? U(focusedChild) : null;
        if (U == null) {
            f1();
            return;
        }
        this.f6604v0.f6727n = this.f6591m.g() ? U.k() : -1L;
        this.f6604v0.f6726m = this.O ? -1 : U.v() ? U.f6631d : U.j();
        this.f6604v0.f6728o = l0(U.f6628a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static b0 i0(View view) {
        if (view == null) {
            return null;
        }
        return ((LayoutParams) view.getLayoutParams()).f6613a;
    }

    static void k0(View view, Rect rect) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Rect rect2 = layoutParams.f6614b;
        rect.set((view.getLeft() - rect2.left) - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, (view.getTop() - rect2.top) - ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, view.getRight() + rect2.right + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, view.getBottom() + rect2.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin);
    }

    private int l0(View view) {
        int id;
        loop0: while (true) {
            id = view.getId();
            while (!view.isFocused() && (view instanceof ViewGroup) && view.hasFocus()) {
                view = ((ViewGroup) view).getFocusedChild();
                if (view.getId() != -1) {
                    break;
                }
            }
        }
        return id;
    }

    private String m0(Context context, String str) {
        if (str.charAt(0) == '.') {
            return context.getPackageName() + str;
        } else if (str.contains(".")) {
            return str;
        } else {
            return RecyclerView.class.getPackage().getName() + '.' + str;
        }
    }

    private void m1(g gVar, boolean z4, boolean z8) {
        g gVar2 = this.f6591m;
        if (gVar2 != null) {
            gVar2.B(this.f6568a);
            this.f6591m.u(this);
        }
        if (!z4 || z8) {
            X0();
        }
        this.f6574d.y();
        g gVar3 = this.f6591m;
        this.f6591m = gVar;
        if (gVar != null) {
            gVar.z(this.f6568a);
            gVar.q(this);
        }
        o oVar = this.f6593n;
        if (oVar != null) {
            oVar.F0(gVar3, this.f6591m);
        }
        this.f6570b.x(gVar3, this.f6591m, z4);
        this.f6604v0.f6720g = true;
    }

    private void n(b0 b0Var, b0 b0Var2, l.c cVar, l.c cVar2, boolean z4, boolean z8) {
        b0Var.G(false);
        if (z4) {
            g(b0Var);
        }
        if (b0Var != b0Var2) {
            if (z8) {
                g(b0Var2);
            }
            b0Var.f6635h = b0Var2;
            g(b0Var);
            this.f6570b.J(b0Var);
            b0Var2.G(false);
            b0Var2.f6636i = b0Var;
        }
        if (this.f6575d0.b(b0Var, b0Var2, cVar, cVar2)) {
            P0();
        }
    }

    private void o0(long j8, b0 b0Var, b0 b0Var2) {
        int g8 = this.f6576e.g();
        for (int i8 = 0; i8 < g8; i8++) {
            b0 i02 = i0(this.f6576e.f(i8));
            if (i02 != b0Var && e0(i02) == j8) {
                g gVar = this.f6591m;
                if (gVar == null || !gVar.g()) {
                    throw new IllegalStateException("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:" + i02 + " \n View Holder 2:" + b0Var + Q());
                }
                throw new IllegalStateException("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:" + i02 + " \n View Holder 2:" + b0Var + Q());
            }
        }
        Log.e("RecyclerView", "Problem while matching changed view holders with the newones. The pre-layout information for the change holder " + b0Var2 + " cannot be found but it is necessary for " + b0Var + Q());
    }

    private boolean q0() {
        int g8 = this.f6576e.g();
        for (int i8 = 0; i8 < g8; i8++) {
            b0 i02 = i0(this.f6576e.f(i8));
            if (i02 != null && !i02.J() && i02.y()) {
                return true;
            }
        }
        return false;
    }

    private void r() {
        g1();
        setScrollState(0);
    }

    static void s(b0 b0Var) {
        WeakReference<RecyclerView> weakReference = b0Var.f6629b;
        if (weakReference != null) {
            ViewParent viewParent = weakReference.get();
            while (true) {
                for (View view = (View) viewParent; view != null; view = null) {
                    if (view == b0Var.f6628a) {
                        return;
                    }
                    viewParent = view.getParent();
                    if (viewParent instanceof View) {
                        break;
                    }
                }
                b0Var.f6629b = null;
                return;
            }
        }
    }

    @SuppressLint({"InlinedApi"})
    private void s0() {
        if (c0.D(this) == 0) {
            c0.F0(this, 8);
        }
    }

    private void t0() {
        this.f6576e = new androidx.recyclerview.widget.f(new e());
    }

    private void w(Context context, String str, AttributeSet attributeSet, int i8, int i9) {
        Constructor constructor;
        if (str != null) {
            String trim = str.trim();
            if (trim.isEmpty()) {
                return;
            }
            String m02 = m0(context, trim);
            try {
                Class<? extends U> asSubclass = Class.forName(m02, false, isInEditMode() ? getClass().getClassLoader() : context.getClassLoader()).asSubclass(o.class);
                Object[] objArr = null;
                try {
                    constructor = asSubclass.getConstructor(T0);
                    objArr = new Object[]{context, attributeSet, Integer.valueOf(i8), Integer.valueOf(i9)};
                } catch (NoSuchMethodException e8) {
                    try {
                        constructor = asSubclass.getConstructor(new Class[0]);
                    } catch (NoSuchMethodException e9) {
                        e9.initCause(e8);
                        throw new IllegalStateException(attributeSet.getPositionDescription() + ": Error creating LayoutManager " + m02, e9);
                    }
                }
                constructor.setAccessible(true);
                setLayoutManager((o) constructor.newInstance(objArr));
            } catch (ClassCastException e10) {
                throw new IllegalStateException(attributeSet.getPositionDescription() + ": Class is not a LayoutManager " + m02, e10);
            } catch (ClassNotFoundException e11) {
                throw new IllegalStateException(attributeSet.getPositionDescription() + ": Unable to find LayoutManager " + m02, e11);
            } catch (IllegalAccessException e12) {
                throw new IllegalStateException(attributeSet.getPositionDescription() + ": Cannot access non-public constructor " + m02, e12);
            } catch (InstantiationException e13) {
                throw new IllegalStateException(attributeSet.getPositionDescription() + ": Could not instantiate the LayoutManager: " + m02, e13);
            } catch (InvocationTargetException e14) {
                throw new IllegalStateException(attributeSet.getPositionDescription() + ": Could not instantiate the LayoutManager: " + m02, e14);
            }
        }
    }

    private boolean y(int i8, int i9) {
        W(this.E0);
        int[] iArr = this.E0;
        return (iArr[0] == i8 && iArr[1] == i9) ? false : true;
    }

    private boolean y0(View view, View view2, int i8) {
        int i9;
        if (view2 == null || view2 == this || T(view2) == null) {
            return false;
        }
        if (view == null || T(view) == null) {
            return true;
        }
        this.f6585j.set(0, 0, view.getWidth(), view.getHeight());
        this.f6587k.set(0, 0, view2.getWidth(), view2.getHeight());
        offsetDescendantRectToMyCoords(view, this.f6585j);
        offsetDescendantRectToMyCoords(view2, this.f6587k);
        char c9 = 65535;
        int i10 = this.f6593n.a0() == 1 ? -1 : 1;
        Rect rect = this.f6585j;
        int i11 = rect.left;
        Rect rect2 = this.f6587k;
        int i12 = rect2.left;
        if ((i11 < i12 || rect.right <= i12) && rect.right < rect2.right) {
            i9 = 1;
        } else {
            int i13 = rect.right;
            int i14 = rect2.right;
            i9 = ((i13 > i14 || i11 >= i14) && i11 > i12) ? -1 : 0;
        }
        int i15 = rect.top;
        int i16 = rect2.top;
        if ((i15 < i16 || rect.bottom <= i16) && rect.bottom < rect2.bottom) {
            c9 = 1;
        } else {
            int i17 = rect.bottom;
            int i18 = rect2.bottom;
            if ((i17 <= i18 && i15 < i18) || i15 <= i16) {
                c9 = 0;
            }
        }
        if (i8 == 1) {
            return c9 < 0 || (c9 == 0 && i9 * i10 <= 0);
        } else if (i8 == 2) {
            return c9 > 0 || (c9 == 0 && i9 * i10 >= 0);
        } else if (i8 == 17) {
            return i9 < 0;
        } else if (i8 == 33) {
            return c9 < 0;
        } else if (i8 == 66) {
            return i9 > 0;
        } else if (i8 == 130) {
            return c9 > 0;
        } else {
            throw new IllegalArgumentException("Invalid direction: " + i8 + Q());
        }
    }

    private void z1() {
        this.f6600s0.g();
        o oVar = this.f6593n;
        if (oVar != null) {
            oVar.L1();
        }
    }

    void A(View view) {
        b0 i02 = i0(view);
        I0(view);
        g gVar = this.f6591m;
        if (gVar != null && i02 != null) {
            gVar.x(i02);
        }
        List<p> list = this.L;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.L.get(size).b(view);
            }
        }
    }

    void A0() {
        int j8 = this.f6576e.j();
        for (int i8 = 0; i8 < j8; i8++) {
            ((LayoutParams) this.f6576e.i(i8).getLayoutParams()).f6615c = true;
        }
        this.f6570b.s();
    }

    void A1(int i8, int i9, Object obj) {
        int i10;
        int j8 = this.f6576e.j();
        int i11 = i8 + i9;
        for (int i12 = 0; i12 < j8; i12++) {
            View i13 = this.f6576e.i(i12);
            b0 i02 = i0(i13);
            if (i02 != null && !i02.J() && (i10 = i02.f6630c) >= i8 && i10 < i11) {
                i02.b(2);
                i02.a(obj);
                ((LayoutParams) i13.getLayoutParams()).f6615c = true;
            }
        }
        this.f6570b.M(i8, i9);
    }

    void B0() {
        int j8 = this.f6576e.j();
        for (int i8 = 0; i8 < j8; i8++) {
            b0 i02 = i0(this.f6576e.i(i8));
            if (i02 != null && !i02.J()) {
                i02.b(6);
            }
        }
        A0();
        this.f6570b.t();
    }

    void C() {
        String str;
        if (this.f6591m == null) {
            str = "No adapter attached; skipping layout";
        } else if (this.f6593n != null) {
            y yVar = this.f6604v0;
            yVar.f6723j = false;
            if (yVar.f6718e == 1) {
                D();
            } else if (!this.f6574d.q() && this.f6593n.p0() == getWidth() && this.f6593n.X() == getHeight()) {
                this.f6593n.A1(this);
                F();
                return;
            }
            this.f6593n.A1(this);
            E();
            F();
            return;
        } else {
            str = "No layout manager attached; skipping layout";
        }
        Log.e("RecyclerView", str);
    }

    public void C0(int i8) {
        int g8 = this.f6576e.g();
        for (int i9 = 0; i9 < g8; i9++) {
            this.f6576e.f(i9).offsetLeftAndRight(i8);
        }
    }

    public void D0(int i8) {
        int g8 = this.f6576e.g();
        for (int i9 = 0; i9 < g8; i9++) {
            this.f6576e.f(i9).offsetTopAndBottom(i8);
        }
    }

    void E0(int i8, int i9) {
        int j8 = this.f6576e.j();
        for (int i10 = 0; i10 < j8; i10++) {
            b0 i02 = i0(this.f6576e.i(i10));
            if (i02 != null && !i02.J() && i02.f6630c >= i8) {
                i02.A(i9, false);
                this.f6604v0.f6720g = true;
            }
        }
        this.f6570b.u(i8, i9);
        requestLayout();
    }

    void F0(int i8, int i9) {
        int i10;
        int i11;
        int i12;
        int i13;
        int j8 = this.f6576e.j();
        if (i8 < i9) {
            i12 = -1;
            i11 = i8;
            i10 = i9;
        } else {
            i10 = i8;
            i11 = i9;
            i12 = 1;
        }
        for (int i14 = 0; i14 < j8; i14++) {
            b0 i02 = i0(this.f6576e.i(i14));
            if (i02 != null && (i13 = i02.f6630c) >= i11 && i13 <= i10) {
                if (i13 == i8) {
                    i02.A(i9 - i8, false);
                } else {
                    i02.A(i12, false);
                }
                this.f6604v0.f6720g = true;
            }
        }
        this.f6570b.v(i8, i9);
        requestLayout();
    }

    public boolean G(int i8, int i9, int[] iArr, int[] iArr2, int i10) {
        return getScrollingChildHelper().d(i8, i9, iArr, iArr2, i10);
    }

    void G0(int i8, int i9, boolean z4) {
        int i10 = i8 + i9;
        int j8 = this.f6576e.j();
        for (int i11 = 0; i11 < j8; i11++) {
            b0 i02 = i0(this.f6576e.i(i11));
            if (i02 != null && !i02.J()) {
                int i12 = i02.f6630c;
                if (i12 >= i10) {
                    i02.A(-i9, z4);
                } else if (i12 >= i8) {
                    i02.i(i8 - 1, -i9, z4);
                }
                this.f6604v0.f6720g = true;
            }
        }
        this.f6570b.w(i8, i9, z4);
        requestLayout();
    }

    public final void H(int i8, int i9, int i10, int i11, int[] iArr, int i12, int[] iArr2) {
        getScrollingChildHelper().e(i8, i9, i10, i11, iArr, i12, iArr2);
    }

    public void H0(View view) {
    }

    void I(int i8) {
        o oVar = this.f6593n;
        if (oVar != null) {
            oVar.f1(i8);
        }
        N0(i8);
        s sVar = this.f6606w0;
        if (sVar != null) {
            sVar.a(this, i8);
        }
        List<s> list = this.f6608x0;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.f6608x0.get(size).a(this, i8);
            }
        }
    }

    public void I0(View view) {
    }

    void J(int i8, int i9) {
        this.R++;
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        onScrollChanged(scrollX, scrollY, scrollX - i8, scrollY - i9);
        O0(i8, i9);
        s sVar = this.f6606w0;
        if (sVar != null) {
            sVar.b(this, i8, i9);
        }
        List<s> list = this.f6608x0;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.f6608x0.get(size).b(this, i8, i9);
            }
        }
        this.R--;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J0() {
        this.Q++;
    }

    void K() {
        int i8;
        for (int size = this.J0.size() - 1; size >= 0; size--) {
            b0 b0Var = this.J0.get(size);
            if (b0Var.f6628a.getParent() == this && !b0Var.J() && (i8 = b0Var.q) != -1) {
                c0.E0(b0Var.f6628a, i8);
                b0Var.q = -1;
            }
        }
        this.J0.clear();
    }

    void K0() {
        L0(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L0(boolean z4) {
        int i8 = this.Q - 1;
        this.Q = i8;
        if (i8 < 1) {
            this.Q = 0;
            if (z4) {
                B();
                K();
            }
        }
    }

    void M() {
        int measuredWidth;
        int measuredHeight;
        if (this.f6573c0 != null) {
            return;
        }
        EdgeEffect a9 = this.T.a(this, 3);
        this.f6573c0 = a9;
        if (this.f6580g) {
            measuredWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
            measuredHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        } else {
            measuredWidth = getMeasuredWidth();
            measuredHeight = getMeasuredHeight();
        }
        a9.setSize(measuredWidth, measuredHeight);
    }

    void N() {
        int measuredHeight;
        int measuredWidth;
        if (this.W != null) {
            return;
        }
        EdgeEffect a9 = this.T.a(this, 0);
        this.W = a9;
        if (this.f6580g) {
            measuredHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
            measuredWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
        } else {
            measuredHeight = getMeasuredHeight();
            measuredWidth = getMeasuredWidth();
        }
        a9.setSize(measuredHeight, measuredWidth);
    }

    public void N0(int i8) {
    }

    void O() {
        int measuredHeight;
        int measuredWidth;
        if (this.f6571b0 != null) {
            return;
        }
        EdgeEffect a9 = this.T.a(this, 2);
        this.f6571b0 = a9;
        if (this.f6580g) {
            measuredHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
            measuredWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
        } else {
            measuredHeight = getMeasuredHeight();
            measuredWidth = getMeasuredWidth();
        }
        a9.setSize(measuredHeight, measuredWidth);
    }

    public void O0(int i8, int i9) {
    }

    void P() {
        int measuredWidth;
        int measuredHeight;
        if (this.f6569a0 != null) {
            return;
        }
        EdgeEffect a9 = this.T.a(this, 1);
        this.f6569a0 = a9;
        if (this.f6580g) {
            measuredWidth = (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
            measuredHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        } else {
            measuredWidth = getMeasuredWidth();
            measuredHeight = getMeasuredHeight();
        }
        a9.setSize(measuredWidth, measuredHeight);
    }

    void P0() {
        if (this.B0 || !this.f6607x) {
            return;
        }
        c0.l0(this, this.K0);
        this.B0 = true;
    }

    String Q() {
        return " " + super.toString() + ", adapter:" + this.f6591m + ", layout:" + this.f6593n + ", context:" + getContext();
    }

    final void R(y yVar) {
        if (getScrollState() != 2) {
            yVar.f6729p = 0;
            yVar.q = 0;
            return;
        }
        OverScroller overScroller = this.f6600s0.f6621c;
        yVar.f6729p = overScroller.getFinalX() - overScroller.getCurrX();
        yVar.q = overScroller.getFinalY() - overScroller.getCurrY();
    }

    public View S(float f5, float f8) {
        for (int g8 = this.f6576e.g() - 1; g8 >= 0; g8--) {
            View f9 = this.f6576e.f(g8);
            float translationX = f9.getTranslationX();
            float translationY = f9.getTranslationY();
            if (f5 >= f9.getLeft() + translationX && f5 <= f9.getRight() + translationX && f8 >= f9.getTop() + translationY && f8 <= f9.getBottom() + translationY) {
                return f9;
            }
        }
        return null;
    }

    void S0(boolean z4) {
        this.P = z4 | this.P;
        this.O = true;
        B0();
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:?, code lost:
        return r3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.view.View T(android.view.View r3) {
        /*
            r2 = this;
        L0:
            android.view.ViewParent r0 = r3.getParent()
            if (r0 == 0) goto L10
            if (r0 == r2) goto L10
            boolean r1 = r0 instanceof android.view.View
            if (r1 == 0) goto L10
            r3 = r0
            android.view.View r3 = (android.view.View) r3
            goto L0
        L10:
            if (r0 != r2) goto L13
            goto L14
        L13:
            r3 = 0
        L14:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.T(android.view.View):android.view.View");
    }

    public b0 U(View view) {
        View T = T(view);
        if (T == null) {
            return null;
        }
        return h0(T);
    }

    void U0(b0 b0Var, l.c cVar) {
        b0Var.F(0, 8192);
        if (this.f6604v0.f6722i && b0Var.y() && !b0Var.v() && !b0Var.J()) {
            this.f6578f.c(e0(b0Var), b0Var);
        }
        this.f6578f.e(b0Var, cVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void X0() {
        l lVar = this.f6575d0;
        if (lVar != null) {
            lVar.k();
        }
        o oVar = this.f6593n;
        if (oVar != null) {
            oVar.l1(this.f6570b);
            this.f6593n.m1(this.f6570b);
        }
        this.f6570b.c();
    }

    boolean Y0(View view) {
        u1();
        boolean r4 = this.f6576e.r(view);
        if (r4) {
            b0 i02 = i0(view);
            this.f6570b.J(i02);
            this.f6570b.C(i02);
        }
        w1(!r4);
        return r4;
    }

    public b0 Z(int i8) {
        b0 b0Var = null;
        if (this.O) {
            return null;
        }
        int j8 = this.f6576e.j();
        for (int i9 = 0; i9 < j8; i9++) {
            b0 i02 = i0(this.f6576e.i(i9));
            if (i02 != null && !i02.v() && d0(i02) == i8) {
                if (!this.f6576e.n(i02.f6628a)) {
                    return i02;
                }
                b0Var = i02;
            }
        }
        return b0Var;
    }

    public void Z0(n nVar) {
        o oVar = this.f6593n;
        if (oVar != null) {
            oVar.h("Cannot remove item decoration during a scroll  or layout");
        }
        this.q.remove(nVar);
        if (this.q.isEmpty()) {
            setWillNotDraw(getOverScrollMode() == 2);
        }
        A0();
        requestLayout();
    }

    void a(int i8, int i9) {
        if (i8 < 0) {
            N();
            if (this.W.isFinished()) {
                this.W.onAbsorb(-i8);
            }
        } else if (i8 > 0) {
            O();
            if (this.f6571b0.isFinished()) {
                this.f6571b0.onAbsorb(i8);
            }
        }
        if (i9 < 0) {
            P();
            if (this.f6569a0.isFinished()) {
                this.f6569a0.onAbsorb(-i9);
            }
        } else if (i9 > 0) {
            M();
            if (this.f6573c0.isFinished()) {
                this.f6573c0.onAbsorb(i9);
            }
        }
        if (i8 == 0 && i9 == 0) {
            return;
        }
        c0.j0(this);
    }

    public b0 a0(long j8) {
        g gVar = this.f6591m;
        b0 b0Var = null;
        if (gVar != null && gVar.g()) {
            int j9 = this.f6576e.j();
            for (int i8 = 0; i8 < j9; i8++) {
                b0 i02 = i0(this.f6576e.i(i8));
                if (i02 != null && !i02.v() && i02.k() == j8) {
                    if (!this.f6576e.n(i02.f6628a)) {
                        return i02;
                    }
                    b0Var = i02;
                }
            }
        }
        return b0Var;
    }

    public void a1(p pVar) {
        List<p> list = this.L;
        if (list == null) {
            return;
        }
        list.remove(pVar);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> arrayList, int i8, int i9) {
        o oVar = this.f6593n;
        if (oVar == null || !oVar.G0(this, arrayList, i8, i9)) {
            super.addFocusables(arrayList, i8, i9);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0034  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0036 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    androidx.recyclerview.widget.RecyclerView.b0 b0(int r6, boolean r7) {
        /*
            r5 = this;
            androidx.recyclerview.widget.f r0 = r5.f6576e
            int r0 = r0.j()
            r1 = 0
            r2 = 0
        L8:
            if (r2 >= r0) goto L3a
            androidx.recyclerview.widget.f r3 = r5.f6576e
            android.view.View r3 = r3.i(r2)
            androidx.recyclerview.widget.RecyclerView$b0 r3 = i0(r3)
            if (r3 == 0) goto L37
            boolean r4 = r3.v()
            if (r4 != 0) goto L37
            if (r7 == 0) goto L23
            int r4 = r3.f6630c
            if (r4 == r6) goto L2a
            goto L37
        L23:
            int r4 = r3.m()
            if (r4 == r6) goto L2a
            goto L37
        L2a:
            androidx.recyclerview.widget.f r1 = r5.f6576e
            android.view.View r4 = r3.f6628a
            boolean r1 = r1.n(r4)
            if (r1 == 0) goto L36
            r1 = r3
            goto L37
        L36:
            return r3
        L37:
            int r2 = r2 + 1
            goto L8
        L3a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.b0(int, boolean):androidx.recyclerview.widget.RecyclerView$b0");
    }

    public void b1(r rVar) {
        this.f6601t.remove(rVar);
        if (this.f6605w == rVar) {
            this.f6605w = null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v6 */
    public boolean c0(int i8, int i9) {
        o oVar = this.f6593n;
        if (oVar == null) {
            Log.e("RecyclerView", "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return false;
        } else if (this.E) {
            return false;
        } else {
            int l8 = oVar.l();
            boolean m8 = this.f6593n.m();
            if (l8 == 0 || Math.abs(i8) < this.f6594n0) {
                i8 = 0;
            }
            if (!m8 || Math.abs(i9) < this.f6594n0) {
                i9 = 0;
            }
            if (i8 == 0 && i9 == 0) {
                return false;
            }
            float f5 = i8;
            float f8 = i9;
            if (!dispatchNestedPreFling(f5, f8)) {
                boolean z4 = l8 != 0 || m8;
                dispatchNestedFling(f5, f8, z4);
                q qVar = this.f6592m0;
                if (qVar != null && qVar.a(i8, i9)) {
                    return true;
                }
                if (z4) {
                    if (m8) {
                        l8 = (l8 == true ? 1 : 0) | 2;
                    }
                    v1(l8, 1);
                    int i10 = this.f6595o0;
                    int max = Math.max(-i10, Math.min(i8, i10));
                    int i11 = this.f6595o0;
                    this.f6600s0.c(max, Math.max(-i11, Math.min(i9, i11)));
                    return true;
                }
            }
            return false;
        }
    }

    public void c1(s sVar) {
        List<s> list = this.f6608x0;
        if (list != null) {
            list.remove(sVar);
        }
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && this.f6593n.n((LayoutParams) layoutParams);
    }

    @Override // android.view.View
    public int computeHorizontalScrollExtent() {
        o oVar = this.f6593n;
        if (oVar != null && oVar.l()) {
            return this.f6593n.r(this.f6604v0);
        }
        return 0;
    }

    @Override // android.view.View
    public int computeHorizontalScrollOffset() {
        o oVar = this.f6593n;
        if (oVar != null && oVar.l()) {
            return this.f6593n.s(this.f6604v0);
        }
        return 0;
    }

    @Override // android.view.View
    public int computeHorizontalScrollRange() {
        o oVar = this.f6593n;
        if (oVar != null && oVar.l()) {
            return this.f6593n.t(this.f6604v0);
        }
        return 0;
    }

    @Override // android.view.View
    public int computeVerticalScrollExtent() {
        o oVar = this.f6593n;
        if (oVar != null && oVar.m()) {
            return this.f6593n.u(this.f6604v0);
        }
        return 0;
    }

    @Override // android.view.View
    public int computeVerticalScrollOffset() {
        o oVar = this.f6593n;
        if (oVar != null && oVar.m()) {
            return this.f6593n.v(this.f6604v0);
        }
        return 0;
    }

    @Override // android.view.View
    public int computeVerticalScrollRange() {
        o oVar = this.f6593n;
        if (oVar != null && oVar.m()) {
            return this.f6593n.w(this.f6604v0);
        }
        return 0;
    }

    int d0(b0 b0Var) {
        if (b0Var.p(524) || !b0Var.s()) {
            return -1;
        }
        return this.f6574d.e(b0Var.f6630c);
    }

    void d1() {
        b0 b0Var;
        int g8 = this.f6576e.g();
        for (int i8 = 0; i8 < g8; i8++) {
            View f5 = this.f6576e.f(i8);
            b0 h02 = h0(f5);
            if (h02 != null && (b0Var = h02.f6636i) != null) {
                View view = b0Var.f6628a;
                int left = f5.getLeft();
                int top = f5.getTop();
                if (left != view.getLeft() || top != view.getTop()) {
                    view.layout(left, top, view.getWidth() + left, view.getHeight() + top);
                }
            }
        }
    }

    @Override // android.view.View
    public boolean dispatchNestedFling(float f5, float f8, boolean z4) {
        return getScrollingChildHelper().a(f5, f8, z4);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreFling(float f5, float f8) {
        return getScrollingChildHelper().b(f5, f8);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreScroll(int i8, int i9, int[] iArr, int[] iArr2) {
        return getScrollingChildHelper().c(i8, i9, iArr, iArr2);
    }

    @Override // android.view.View
    public boolean dispatchNestedScroll(int i8, int i9, int i10, int i11, int[] iArr) {
        return getScrollingChildHelper().f(i8, i9, i10, i11, iArr);
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchThawSelfOnly(sparseArray);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> sparseArray) {
        dispatchFreezeSelfOnly(sparseArray);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        boolean z4;
        float f5;
        int i8;
        super.draw(canvas);
        int size = this.q.size();
        boolean z8 = false;
        for (int i9 = 0; i9 < size; i9++) {
            this.q.get(i9).k(canvas, this, this.f6604v0);
        }
        EdgeEffect edgeEffect = this.W;
        boolean z9 = true;
        if (edgeEffect == null || edgeEffect.isFinished()) {
            z4 = false;
        } else {
            int save = canvas.save();
            int paddingBottom = this.f6580g ? getPaddingBottom() : 0;
            canvas.rotate(270.0f);
            canvas.translate((-getHeight()) + paddingBottom, 0.0f);
            EdgeEffect edgeEffect2 = this.W;
            z4 = edgeEffect2 != null && edgeEffect2.draw(canvas);
            canvas.restoreToCount(save);
        }
        EdgeEffect edgeEffect3 = this.f6569a0;
        if (edgeEffect3 != null && !edgeEffect3.isFinished()) {
            int save2 = canvas.save();
            if (this.f6580g) {
                canvas.translate(getPaddingLeft(), getPaddingTop());
            }
            EdgeEffect edgeEffect4 = this.f6569a0;
            z4 |= edgeEffect4 != null && edgeEffect4.draw(canvas);
            canvas.restoreToCount(save2);
        }
        EdgeEffect edgeEffect5 = this.f6571b0;
        if (edgeEffect5 != null && !edgeEffect5.isFinished()) {
            int save3 = canvas.save();
            int width = getWidth();
            int paddingTop = this.f6580g ? getPaddingTop() : 0;
            canvas.rotate(90.0f);
            canvas.translate(-paddingTop, -width);
            EdgeEffect edgeEffect6 = this.f6571b0;
            z4 |= edgeEffect6 != null && edgeEffect6.draw(canvas);
            canvas.restoreToCount(save3);
        }
        EdgeEffect edgeEffect7 = this.f6573c0;
        if (edgeEffect7 != null && !edgeEffect7.isFinished()) {
            int save4 = canvas.save();
            canvas.rotate(180.0f);
            if (this.f6580g) {
                f5 = (-getWidth()) + getPaddingRight();
                i8 = (-getHeight()) + getPaddingBottom();
            } else {
                f5 = -getWidth();
                i8 = -getHeight();
            }
            canvas.translate(f5, i8);
            EdgeEffect edgeEffect8 = this.f6573c0;
            if (edgeEffect8 != null && edgeEffect8.draw(canvas)) {
                z8 = true;
            }
            z4 |= z8;
            canvas.restoreToCount(save4);
        }
        if (z4 || this.f6575d0 == null || this.q.size() <= 0 || !this.f6575d0.p()) {
            z9 = z4;
        }
        if (z9) {
            c0.j0(this);
        }
    }

    @Override // android.view.ViewGroup
    public boolean drawChild(Canvas canvas, View view, long j8) {
        return super.drawChild(canvas, view, j8);
    }

    long e0(b0 b0Var) {
        return this.f6591m.g() ? b0Var.k() : b0Var.f6630c;
    }

    public int f0(View view) {
        b0 i02 = i0(view);
        if (i02 != null) {
            return i02.j();
        }
        return -1;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public View focusSearch(View view, int i8) {
        View view2;
        boolean z4;
        View R02 = this.f6593n.R0(view, i8);
        if (R02 != null) {
            return R02;
        }
        boolean z8 = (this.f6591m == null || this.f6593n == null || x0() || this.E) ? false : true;
        FocusFinder focusFinder = FocusFinder.getInstance();
        if (z8 && (i8 == 2 || i8 == 1)) {
            if (this.f6593n.m()) {
                int i9 = i8 == 2 ? 130 : 33;
                z4 = focusFinder.findNextFocus(this, view, i9) == null;
                if (R0) {
                    i8 = i9;
                }
            } else {
                z4 = false;
            }
            if (!z4 && this.f6593n.l()) {
                int i10 = (this.f6593n.a0() == 1) ^ (i8 == 2) ? 66 : 17;
                boolean z9 = focusFinder.findNextFocus(this, view, i10) == null;
                if (R0) {
                    i8 = i10;
                }
                z4 = z9;
            }
            if (z4) {
                v();
                if (T(view) == null) {
                    return null;
                }
                u1();
                this.f6593n.K0(view, i8, this.f6570b, this.f6604v0);
                w1(false);
            }
            view2 = focusFinder.findNextFocus(this, view, i8);
        } else {
            View findNextFocus = focusFinder.findNextFocus(this, view, i8);
            if (findNextFocus == null && z8) {
                v();
                if (T(view) == null) {
                    return null;
                }
                u1();
                view2 = this.f6593n.K0(view, i8, this.f6570b, this.f6604v0);
                w1(false);
            } else {
                view2 = findNextFocus;
            }
        }
        if (view2 == null || view2.hasFocusable()) {
            return y0(view, view2, i8) ? view2 : super.focusSearch(view, i8);
        } else if (getFocusedChild() == null) {
            return super.focusSearch(view, i8);
        } else {
            e1(view2, null);
            return view;
        }
    }

    public int g0(View view) {
        b0 i02 = i0(view);
        if (i02 != null) {
            return i02.m();
        }
        return -1;
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        o oVar = this.f6593n;
        if (oVar != null) {
            return oVar.E();
        }
        throw new IllegalStateException("RecyclerView has no LayoutManager" + Q());
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        o oVar = this.f6593n;
        if (oVar != null) {
            return oVar.F(getContext(), attributeSet);
        }
        throw new IllegalStateException("RecyclerView has no LayoutManager" + Q());
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        o oVar = this.f6593n;
        if (oVar != null) {
            return oVar.G(layoutParams);
        }
        throw new IllegalStateException("RecyclerView has no LayoutManager" + Q());
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return "androidx.recyclerview.widget.RecyclerView";
    }

    public g getAdapter() {
        return this.f6591m;
    }

    @Override // android.view.View
    public int getBaseline() {
        o oVar = this.f6593n;
        return oVar != null ? oVar.H() : super.getBaseline();
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int i8, int i9) {
        j jVar = this.D0;
        return jVar == null ? super.getChildDrawingOrder(i8, i9) : jVar.a(i8, i9);
    }

    @Override // android.view.ViewGroup
    public boolean getClipToPadding() {
        return this.f6580g;
    }

    public androidx.recyclerview.widget.w getCompatAccessibilityDelegate() {
        return this.C0;
    }

    public k getEdgeEffectFactory() {
        return this.T;
    }

    public l getItemAnimator() {
        return this.f6575d0;
    }

    public int getItemDecorationCount() {
        return this.q.size();
    }

    public o getLayoutManager() {
        return this.f6593n;
    }

    public int getMaxFlingVelocity() {
        return this.f6595o0;
    }

    public int getMinFlingVelocity() {
        return this.f6594n0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long getNanoTime() {
        if (Q0) {
            return System.nanoTime();
        }
        return 0L;
    }

    public q getOnFlingListener() {
        return this.f6592m0;
    }

    public boolean getPreserveFocusAfterLayout() {
        return this.f6599r0;
    }

    public t getRecycledViewPool() {
        return this.f6570b.i();
    }

    public int getScrollState() {
        return this.f6577e0;
    }

    public void h(n nVar) {
        i(nVar, -1);
    }

    public b0 h0(View view) {
        ViewParent parent = view.getParent();
        if (parent == null || parent == this) {
            return i0(view);
        }
        throw new IllegalArgumentException("View " + view + " is not a direct child of " + this);
    }

    @Override // android.view.View
    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().j();
    }

    public void i(n nVar, int i8) {
        o oVar = this.f6593n;
        if (oVar != null) {
            oVar.h("Cannot add item decoration during a scroll  or layout");
        }
        if (this.q.isEmpty()) {
            setWillNotDraw(false);
        }
        if (i8 < 0) {
            this.q.add(nVar);
        } else {
            this.q.add(i8, nVar);
        }
        A0();
        requestLayout();
    }

    void i1() {
        int j8 = this.f6576e.j();
        for (int i8 = 0; i8 < j8; i8++) {
            b0 i02 = i0(this.f6576e.i(i8));
            if (!i02.J()) {
                i02.E();
            }
        }
    }

    @Override // android.view.View
    public boolean isAttachedToWindow() {
        return this.f6607x;
    }

    @Override // android.view.ViewGroup
    public final boolean isLayoutSuppressed() {
        return this.E;
    }

    @Override // android.view.View, androidx.core.view.p
    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().l();
    }

    public void j(p pVar) {
        if (this.L == null) {
            this.L = new ArrayList();
        }
        this.L.add(pVar);
    }

    public void j0(View view, Rect rect) {
        k0(view, rect);
    }

    boolean j1(int i8, int i9, MotionEvent motionEvent) {
        int i10;
        int i11;
        int i12;
        int i13;
        v();
        if (this.f6591m != null) {
            int[] iArr = this.I0;
            iArr[0] = 0;
            iArr[1] = 0;
            k1(i8, i9, iArr);
            int[] iArr2 = this.I0;
            int i14 = iArr2[0];
            int i15 = iArr2[1];
            i10 = i15;
            i11 = i14;
            i12 = i8 - i14;
            i13 = i9 - i15;
        } else {
            i10 = 0;
            i11 = 0;
            i12 = 0;
            i13 = 0;
        }
        if (!this.q.isEmpty()) {
            invalidate();
        }
        int[] iArr3 = this.I0;
        iArr3[0] = 0;
        iArr3[1] = 0;
        H(i11, i10, i12, i13, this.G0, 0, iArr3);
        int[] iArr4 = this.I0;
        int i16 = i12 - iArr4[0];
        int i17 = i13 - iArr4[1];
        boolean z4 = (iArr4[0] == 0 && iArr4[1] == 0) ? false : true;
        int i18 = this.f6586j0;
        int[] iArr5 = this.G0;
        this.f6586j0 = i18 - iArr5[0];
        this.f6588k0 -= iArr5[1];
        int[] iArr6 = this.H0;
        iArr6[0] = iArr6[0] + iArr5[0];
        iArr6[1] = iArr6[1] + iArr5[1];
        if (getOverScrollMode() != 2) {
            if (motionEvent != null && !androidx.core.view.o.b(motionEvent, 8194)) {
                T0(motionEvent.getX(), i16, motionEvent.getY(), i17);
            }
            u(i8, i9);
        }
        if (i11 != 0 || i10 != 0) {
            J(i11, i10);
        }
        if (!awakenScrollBars()) {
            invalidate();
        }
        return (!z4 && i11 == 0 && i10 == 0) ? false : true;
    }

    public void k(r rVar) {
        this.f6601t.add(rVar);
    }

    void k1(int i8, int i9, int[] iArr) {
        u1();
        J0();
        androidx.core.os.o.a("RV Scroll");
        R(this.f6604v0);
        int x12 = i8 != 0 ? this.f6593n.x1(i8, this.f6570b, this.f6604v0) : 0;
        int z12 = i9 != 0 ? this.f6593n.z1(i9, this.f6570b, this.f6604v0) : 0;
        androidx.core.os.o.b();
        d1();
        K0();
        w1(false);
        if (iArr != null) {
            iArr[0] = x12;
            iArr[1] = z12;
        }
    }

    public void l(s sVar) {
        if (this.f6608x0 == null) {
            this.f6608x0 = new ArrayList();
        }
        this.f6608x0.add(sVar);
    }

    public void l1(int i8) {
        if (this.E) {
            return;
        }
        y1();
        o oVar = this.f6593n;
        if (oVar == null) {
            Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        oVar.y1(i8);
        awakenScrollBars();
    }

    void m(b0 b0Var, l.c cVar, l.c cVar2) {
        b0Var.G(false);
        if (this.f6575d0.a(b0Var, cVar, cVar2)) {
            P0();
        }
    }

    Rect n0(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (layoutParams.f6615c) {
            if (this.f6604v0.e() && (layoutParams.b() || layoutParams.d())) {
                return layoutParams.f6614b;
            }
            Rect rect = layoutParams.f6614b;
            rect.set(0, 0, 0, 0);
            int size = this.q.size();
            for (int i8 = 0; i8 < size; i8++) {
                this.f6585j.set(0, 0, 0, 0);
                this.q.get(i8).g(this.f6585j, view, this, this.f6604v0);
                int i9 = rect.left;
                Rect rect2 = this.f6585j;
                rect.left = i9 + rect2.left;
                rect.top += rect2.top;
                rect.right += rect2.right;
                rect.bottom += rect2.bottom;
            }
            layoutParams.f6615c = false;
            return rect;
        }
        return layoutParams.f6614b;
    }

    boolean n1(b0 b0Var, int i8) {
        if (!x0()) {
            c0.E0(b0Var.f6628a, i8);
            return true;
        }
        b0Var.q = i8;
        this.J0.add(b0Var);
        return false;
    }

    void o(b0 b0Var, l.c cVar, l.c cVar2) {
        g(b0Var);
        b0Var.G(false);
        if (this.f6575d0.c(b0Var, cVar, cVar2)) {
            P0();
        }
    }

    boolean o1(AccessibilityEvent accessibilityEvent) {
        if (x0()) {
            int a9 = accessibilityEvent != null ? androidx.core.view.accessibility.b.a(accessibilityEvent) : 0;
            this.G |= a9 != 0 ? a9 : 0;
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.Q = 0;
        boolean z4 = true;
        this.f6607x = true;
        if (!this.A || isLayoutRequested()) {
            z4 = false;
        }
        this.A = z4;
        o oVar = this.f6593n;
        if (oVar != null) {
            oVar.A(this);
        }
        this.B0 = false;
        if (Q0) {
            ThreadLocal<androidx.recyclerview.widget.k> threadLocal = androidx.recyclerview.widget.k.f6935e;
            androidx.recyclerview.widget.k kVar = threadLocal.get();
            this.f6602t0 = kVar;
            if (kVar == null) {
                this.f6602t0 = new androidx.recyclerview.widget.k();
                Display x8 = c0.x(this);
                float f5 = 60.0f;
                if (!isInEditMode() && x8 != null) {
                    float refreshRate = x8.getRefreshRate();
                    if (refreshRate >= 30.0f) {
                        f5 = refreshRate;
                    }
                }
                androidx.recyclerview.widget.k kVar2 = this.f6602t0;
                kVar2.f6939c = 1.0E9f / f5;
                threadLocal.set(kVar2);
            }
            this.f6602t0.a(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        androidx.recyclerview.widget.k kVar;
        super.onDetachedFromWindow();
        l lVar = this.f6575d0;
        if (lVar != null) {
            lVar.k();
        }
        y1();
        this.f6607x = false;
        o oVar = this.f6593n;
        if (oVar != null) {
            oVar.B(this, this.f6570b);
        }
        this.J0.clear();
        removeCallbacks(this.K0);
        this.f6578f.j();
        if (!Q0 || (kVar = this.f6602t0) == null) {
            return;
        }
        kVar.j(this);
        this.f6602t0 = null;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = this.q.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.q.get(i8).i(canvas, this, this.f6604v0);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0068  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onGenericMotionEvent(android.view.MotionEvent r6) {
        /*
            r5 = this;
            androidx.recyclerview.widget.RecyclerView$o r0 = r5.f6593n
            r1 = 0
            if (r0 != 0) goto L6
            return r1
        L6:
            boolean r0 = r5.E
            if (r0 == 0) goto Lb
            return r1
        Lb:
            int r0 = r6.getAction()
            r2 = 8
            if (r0 != r2) goto L77
            int r0 = r6.getSource()
            r0 = r0 & 2
            r2 = 0
            if (r0 == 0) goto L3e
            androidx.recyclerview.widget.RecyclerView$o r0 = r5.f6593n
            boolean r0 = r0.m()
            if (r0 == 0) goto L2c
            r0 = 9
            float r0 = r6.getAxisValue(r0)
            float r0 = -r0
            goto L2d
        L2c:
            r0 = r2
        L2d:
            androidx.recyclerview.widget.RecyclerView$o r3 = r5.f6593n
            boolean r3 = r3.l()
            if (r3 == 0) goto L3c
            r3 = 10
            float r3 = r6.getAxisValue(r3)
            goto L64
        L3c:
            r3 = r2
            goto L64
        L3e:
            int r0 = r6.getSource()
            r3 = 4194304(0x400000, float:5.877472E-39)
            r0 = r0 & r3
            if (r0 == 0) goto L62
            r0 = 26
            float r0 = r6.getAxisValue(r0)
            androidx.recyclerview.widget.RecyclerView$o r3 = r5.f6593n
            boolean r3 = r3.m()
            if (r3 == 0) goto L57
            float r0 = -r0
            goto L3c
        L57:
            androidx.recyclerview.widget.RecyclerView$o r3 = r5.f6593n
            boolean r3 = r3.l()
            if (r3 == 0) goto L62
            r3 = r0
            r0 = r2
            goto L64
        L62:
            r0 = r2
            r3 = r0
        L64:
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L6c
            int r2 = (r3 > r2 ? 1 : (r3 == r2 ? 0 : -1))
            if (r2 == 0) goto L77
        L6c:
            float r2 = r5.f6597p0
            float r3 = r3 * r2
            int r2 = (int) r3
            float r3 = r5.f6598q0
            float r0 = r0 * r3
            int r0 = (int) r0
            r5.j1(r2, r0, r6)
        L77:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.onGenericMotionEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z4;
        if (this.E) {
            return false;
        }
        this.f6605w = null;
        if (V(motionEvent)) {
            r();
            return true;
        }
        o oVar = this.f6593n;
        if (oVar == null) {
            return false;
        }
        boolean l8 = oVar.l();
        boolean m8 = this.f6593n.m();
        if (this.f6581g0 == null) {
            this.f6581g0 = VelocityTracker.obtain();
        }
        this.f6581g0.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            if (this.F) {
                this.F = false;
            }
            this.f6579f0 = motionEvent.getPointerId(0);
            int x8 = (int) (motionEvent.getX() + 0.5f);
            this.f6586j0 = x8;
            this.f6583h0 = x8;
            int y8 = (int) (motionEvent.getY() + 0.5f);
            this.f6588k0 = y8;
            this.f6584i0 = y8;
            if (this.f6577e0 == 2) {
                getParent().requestDisallowInterceptTouchEvent(true);
                setScrollState(1);
                x1(1);
            }
            int[] iArr = this.H0;
            iArr[1] = 0;
            iArr[0] = 0;
            int i8 = l8;
            if (m8) {
                i8 = (l8 ? 1 : 0) | 2;
            }
            v1(i8, 0);
        } else if (actionMasked == 1) {
            this.f6581g0.clear();
            x1(0);
        } else if (actionMasked == 2) {
            int findPointerIndex = motionEvent.findPointerIndex(this.f6579f0);
            if (findPointerIndex < 0) {
                Log.e("RecyclerView", "Error processing scroll; pointer index for id " + this.f6579f0 + " not found. Did any MotionEvents get skipped?");
                return false;
            }
            int x9 = (int) (motionEvent.getX(findPointerIndex) + 0.5f);
            int y9 = (int) (motionEvent.getY(findPointerIndex) + 0.5f);
            if (this.f6577e0 != 1) {
                int i9 = x9 - this.f6583h0;
                int i10 = y9 - this.f6584i0;
                if (!l8 || Math.abs(i9) <= this.f6590l0) {
                    z4 = false;
                } else {
                    this.f6586j0 = x9;
                    z4 = true;
                }
                if (m8 && Math.abs(i10) > this.f6590l0) {
                    this.f6588k0 = y9;
                    z4 = true;
                }
                if (z4) {
                    setScrollState(1);
                }
            }
        } else if (actionMasked == 3) {
            r();
        } else if (actionMasked == 5) {
            this.f6579f0 = motionEvent.getPointerId(actionIndex);
            int x10 = (int) (motionEvent.getX(actionIndex) + 0.5f);
            this.f6586j0 = x10;
            this.f6583h0 = x10;
            int y10 = (int) (motionEvent.getY(actionIndex) + 0.5f);
            this.f6588k0 = y10;
            this.f6584i0 = y10;
        } else if (actionMasked == 6) {
            M0(motionEvent);
        }
        return this.f6577e0 == 1;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        androidx.core.os.o.a("RV OnLayout");
        C();
        androidx.core.os.o.b();
        this.A = true;
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        o oVar = this.f6593n;
        if (oVar == null) {
            x(i8, i9);
            return;
        }
        boolean z4 = false;
        if (oVar.t0()) {
            int mode = View.MeasureSpec.getMode(i8);
            int mode2 = View.MeasureSpec.getMode(i9);
            this.f6593n.a1(this.f6570b, this.f6604v0, i8, i9);
            if (mode == 1073741824 && mode2 == 1073741824) {
                z4 = true;
            }
            if (z4 || this.f6591m == null) {
                return;
            }
            if (this.f6604v0.f6718e == 1) {
                D();
            }
            this.f6593n.B1(i8, i9);
            this.f6604v0.f6723j = true;
            E();
            this.f6593n.E1(i8, i9);
            if (this.f6593n.H1()) {
                this.f6593n.B1(View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824));
                this.f6604v0.f6723j = true;
                E();
                this.f6593n.E1(i8, i9);
            }
        } else if (this.f6609y) {
            this.f6593n.a1(this.f6570b, this.f6604v0, i8, i9);
        } else {
            if (this.H) {
                u1();
                J0();
                R0();
                K0();
                y yVar = this.f6604v0;
                if (yVar.f6725l) {
                    yVar.f6721h = true;
                } else {
                    this.f6574d.j();
                    this.f6604v0.f6721h = false;
                }
                this.H = false;
                w1(false);
            } else if (this.f6604v0.f6725l) {
                setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
                return;
            }
            g gVar = this.f6591m;
            if (gVar != null) {
                this.f6604v0.f6719f = gVar.c();
            } else {
                this.f6604v0.f6719f = 0;
            }
            u1();
            this.f6593n.a1(this.f6570b, this.f6604v0, i8, i9);
            w1(false);
            this.f6604v0.f6721h = false;
        }
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int i8, Rect rect) {
        if (x0()) {
            return false;
        }
        return super.onRequestFocusInDescendants(i8, rect);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        Parcelable parcelable2;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        this.f6572c = savedState;
        super.onRestoreInstanceState(savedState.a());
        o oVar = this.f6593n;
        if (oVar == null || (parcelable2 = this.f6572c.f6617c) == null) {
            return;
        }
        oVar.d1(parcelable2);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        SavedState savedState2 = this.f6572c;
        if (savedState2 != null) {
            savedState.b(savedState2);
        } else {
            o oVar = this.f6593n;
            savedState.f6617c = oVar != null ? oVar.e1() : null;
        }
        return savedState;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        super.onSizeChanged(i8, i9, i10, i11);
        if (i8 == i10 && i9 == i11) {
            return;
        }
        v0();
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x00e0  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00f4  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r18) {
        /*
            Method dump skipped, instructions count: 476
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.RecyclerView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    void p(String str) {
        if (x0()) {
            if (str != null) {
                throw new IllegalStateException(str);
            }
            throw new IllegalStateException("Cannot call this method while RecyclerView is computing a layout or scrolling" + Q());
        } else if (this.R > 0) {
            Log.w("RecyclerView", "Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", new IllegalStateException(BuildConfig.FLAVOR + Q()));
        }
    }

    public boolean p0() {
        return !this.A || this.O || this.f6574d.p();
    }

    public void p1(int i8, int i9) {
        q1(i8, i9, null);
    }

    boolean q(b0 b0Var) {
        l lVar = this.f6575d0;
        return lVar == null || lVar.g(b0Var, b0Var.o());
    }

    public void q1(int i8, int i9, Interpolator interpolator) {
        r1(i8, i9, interpolator, Integer.MIN_VALUE);
    }

    void r0() {
        this.f6574d = new androidx.recyclerview.widget.a(new f());
    }

    public void r1(int i8, int i9, Interpolator interpolator, int i10) {
        s1(i8, i9, interpolator, i10, false);
    }

    @Override // android.view.ViewGroup
    protected void removeDetachedView(View view, boolean z4) {
        b0 i02 = i0(view);
        if (i02 != null) {
            if (i02.x()) {
                i02.f();
            } else if (!i02.J()) {
                throw new IllegalArgumentException("Called removeDetachedView with a view which is not flagged as tmp detached." + i02 + Q());
            }
        }
        view.clearAnimation();
        A(view);
        super.removeDetachedView(view, z4);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestChildFocus(View view, View view2) {
        if (!this.f6593n.c1(this, this.f6604v0, view, view2) && view2 != null) {
            e1(view, view2);
        }
        super.requestChildFocus(view, view2);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z4) {
        return this.f6593n.s1(this, view, rect, z4);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z4) {
        int size = this.f6601t.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.f6601t.get(i8).e(z4);
        }
        super.requestDisallowInterceptTouchEvent(z4);
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.B != 0 || this.E) {
            this.C = true;
        } else {
            super.requestLayout();
        }
    }

    void s1(int i8, int i9, Interpolator interpolator, int i10, boolean z4) {
        o oVar = this.f6593n;
        if (oVar == null) {
            Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        } else if (this.E) {
        } else {
            if (!oVar.l()) {
                i8 = 0;
            }
            if (!this.f6593n.m()) {
                i9 = 0;
            }
            if (i8 == 0 && i9 == 0) {
                return;
            }
            if (!(i10 == Integer.MIN_VALUE || i10 > 0)) {
                scrollBy(i8, i9);
                return;
            }
            if (z4) {
                int i11 = i8 != 0 ? 1 : 0;
                if (i9 != 0) {
                    i11 |= 2;
                }
                v1(i11, 1);
            }
            this.f6600s0.f(i8, i9, i10, interpolator);
        }
    }

    @Override // android.view.View
    public void scrollBy(int i8, int i9) {
        o oVar = this.f6593n;
        if (oVar == null) {
            Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        } else if (this.E) {
        } else {
            boolean l8 = oVar.l();
            boolean m8 = this.f6593n.m();
            if (l8 || m8) {
                if (!l8) {
                    i8 = 0;
                }
                if (!m8) {
                    i9 = 0;
                }
                j1(i8, i9, null);
            }
        }
    }

    @Override // android.view.View
    public void scrollTo(int i8, int i9) {
        Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
    }

    @Override // android.view.View, android.view.accessibility.AccessibilityEventSource
    public void sendAccessibilityEventUnchecked(AccessibilityEvent accessibilityEvent) {
        if (o1(accessibilityEvent)) {
            return;
        }
        super.sendAccessibilityEventUnchecked(accessibilityEvent);
    }

    public void setAccessibilityDelegateCompat(androidx.recyclerview.widget.w wVar) {
        this.C0 = wVar;
        c0.t0(this, wVar);
    }

    public void setAdapter(g gVar) {
        setLayoutFrozen(false);
        m1(gVar, false, true);
        S0(false);
        requestLayout();
    }

    public void setChildDrawingOrderCallback(j jVar) {
        if (jVar == this.D0) {
            return;
        }
        this.D0 = jVar;
        setChildrenDrawingOrderEnabled(jVar != null);
    }

    @Override // android.view.ViewGroup
    public void setClipToPadding(boolean z4) {
        if (z4 != this.f6580g) {
            v0();
        }
        this.f6580g = z4;
        super.setClipToPadding(z4);
        if (this.A) {
            requestLayout();
        }
    }

    public void setEdgeEffectFactory(k kVar) {
        androidx.core.util.h.h(kVar);
        this.T = kVar;
        v0();
    }

    public void setHasFixedSize(boolean z4) {
        this.f6609y = z4;
    }

    public void setItemAnimator(l lVar) {
        l lVar2 = this.f6575d0;
        if (lVar2 != null) {
            lVar2.k();
            this.f6575d0.w(null);
        }
        this.f6575d0 = lVar;
        if (lVar != null) {
            lVar.w(this.A0);
        }
    }

    public void setItemViewCacheSize(int i8) {
        this.f6570b.G(i8);
    }

    @Deprecated
    public void setLayoutFrozen(boolean z4) {
        suppressLayout(z4);
    }

    public void setLayoutManager(o oVar) {
        if (oVar == this.f6593n) {
            return;
        }
        y1();
        if (this.f6593n != null) {
            l lVar = this.f6575d0;
            if (lVar != null) {
                lVar.k();
            }
            this.f6593n.l1(this.f6570b);
            this.f6593n.m1(this.f6570b);
            this.f6570b.c();
            if (this.f6607x) {
                this.f6593n.B(this, this.f6570b);
            }
            this.f6593n.F1(null);
            this.f6593n = null;
        } else {
            this.f6570b.c();
        }
        this.f6576e.o();
        this.f6593n = oVar;
        if (oVar != null) {
            if (oVar.f6662b != null) {
                throw new IllegalArgumentException("LayoutManager " + oVar + " is already attached to a RecyclerView:" + oVar.f6662b.Q());
            }
            oVar.F1(this);
            if (this.f6607x) {
                this.f6593n.A(this);
            }
        }
        this.f6570b.K();
        requestLayout();
    }

    @Override // android.view.ViewGroup
    @Deprecated
    public void setLayoutTransition(LayoutTransition layoutTransition) {
        if (Build.VERSION.SDK_INT < 18) {
            if (layoutTransition == null) {
                suppressLayout(false);
                return;
            } else if (layoutTransition.getAnimator(0) == null && layoutTransition.getAnimator(1) == null && layoutTransition.getAnimator(2) == null && layoutTransition.getAnimator(3) == null && layoutTransition.getAnimator(4) == null) {
                suppressLayout(true);
                return;
            }
        }
        if (layoutTransition != null) {
            throw new IllegalArgumentException("Providing a LayoutTransition into RecyclerView is not supported. Please use setItemAnimator() instead for animating changes to the items in this RecyclerView");
        }
        super.setLayoutTransition(null);
    }

    @Override // android.view.View
    public void setNestedScrollingEnabled(boolean z4) {
        getScrollingChildHelper().m(z4);
    }

    public void setOnFlingListener(q qVar) {
        this.f6592m0 = qVar;
    }

    @Deprecated
    public void setOnScrollListener(s sVar) {
        this.f6606w0 = sVar;
    }

    public void setPreserveFocusAfterLayout(boolean z4) {
        this.f6599r0 = z4;
    }

    public void setRecycledViewPool(t tVar) {
        this.f6570b.E(tVar);
    }

    public void setRecyclerListener(v vVar) {
        this.f6596p = vVar;
    }

    void setScrollState(int i8) {
        if (i8 == this.f6577e0) {
            return;
        }
        this.f6577e0 = i8;
        if (i8 != 2) {
            z1();
        }
        I(i8);
    }

    public void setScrollingTouchSlop(int i8) {
        int scaledTouchSlop;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        if (i8 != 0) {
            if (i8 == 1) {
                scaledTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
                this.f6590l0 = scaledTouchSlop;
            }
            Log.w("RecyclerView", "setScrollingTouchSlop(): bad argument constant " + i8 + "; using default value");
        }
        scaledTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.f6590l0 = scaledTouchSlop;
    }

    public void setViewCacheExtension(z zVar) {
        this.f6570b.F(zVar);
    }

    @Override // android.view.View
    public boolean startNestedScroll(int i8) {
        return getScrollingChildHelper().o(i8);
    }

    @Override // android.view.View, androidx.core.view.p
    public void stopNestedScroll() {
        getScrollingChildHelper().q();
    }

    @Override // android.view.ViewGroup
    public final void suppressLayout(boolean z4) {
        if (z4 != this.E) {
            p("Do not suppressLayout in layout or scroll");
            if (z4) {
                long uptimeMillis = SystemClock.uptimeMillis();
                onTouchEvent(MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0));
                this.E = true;
                this.F = true;
                y1();
                return;
            }
            this.E = false;
            if (this.C && this.f6593n != null && this.f6591m != null) {
                requestLayout();
            }
            this.C = false;
        }
    }

    void t() {
        int j8 = this.f6576e.j();
        for (int i8 = 0; i8 < j8; i8++) {
            b0 i02 = i0(this.f6576e.i(i8));
            if (!i02.J()) {
                i02.c();
            }
        }
        this.f6570b.d();
    }

    public void t1(int i8) {
        if (this.E) {
            return;
        }
        o oVar = this.f6593n;
        if (oVar == null) {
            Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
        } else {
            oVar.J1(this, this.f6604v0, i8);
        }
    }

    void u(int i8, int i9) {
        boolean z4;
        EdgeEffect edgeEffect = this.W;
        if (edgeEffect == null || edgeEffect.isFinished() || i8 <= 0) {
            z4 = false;
        } else {
            this.W.onRelease();
            z4 = this.W.isFinished();
        }
        EdgeEffect edgeEffect2 = this.f6571b0;
        if (edgeEffect2 != null && !edgeEffect2.isFinished() && i8 < 0) {
            this.f6571b0.onRelease();
            z4 |= this.f6571b0.isFinished();
        }
        EdgeEffect edgeEffect3 = this.f6569a0;
        if (edgeEffect3 != null && !edgeEffect3.isFinished() && i9 > 0) {
            this.f6569a0.onRelease();
            z4 |= this.f6569a0.isFinished();
        }
        EdgeEffect edgeEffect4 = this.f6573c0;
        if (edgeEffect4 != null && !edgeEffect4.isFinished() && i9 < 0) {
            this.f6573c0.onRelease();
            z4 |= this.f6573c0.isFinished();
        }
        if (z4) {
            c0.j0(this);
        }
    }

    void u0(StateListDrawable stateListDrawable, Drawable drawable, StateListDrawable stateListDrawable2, Drawable drawable2) {
        if (stateListDrawable != null && drawable != null && stateListDrawable2 != null && drawable2 != null) {
            Resources resources = getContext().getResources();
            new androidx.recyclerview.widget.j(this, stateListDrawable, drawable, stateListDrawable2, drawable2, resources.getDimensionPixelSize(p1.b.f22265a), resources.getDimensionPixelSize(p1.b.f22267c), resources.getDimensionPixelOffset(p1.b.f22266b));
            return;
        }
        throw new IllegalArgumentException("Trying to set fast scroller without both required drawables." + Q());
    }

    void u1() {
        int i8 = this.B + 1;
        this.B = i8;
        if (i8 != 1 || this.E) {
            return;
        }
        this.C = false;
    }

    void v() {
        if (!this.A || this.O) {
            androidx.core.os.o.a("RV FullInvalidate");
            C();
            androidx.core.os.o.b();
        } else if (this.f6574d.p()) {
            if (this.f6574d.o(4) && !this.f6574d.o(11)) {
                androidx.core.os.o.a("RV PartialInvalidate");
                u1();
                J0();
                this.f6574d.w();
                if (!this.C) {
                    if (q0()) {
                        C();
                    } else {
                        this.f6574d.i();
                    }
                }
                w1(true);
                K0();
            } else if (!this.f6574d.p()) {
                return;
            } else {
                androidx.core.os.o.a("RV FullInvalidate");
                C();
            }
            androidx.core.os.o.b();
        }
    }

    void v0() {
        this.f6573c0 = null;
        this.f6569a0 = null;
        this.f6571b0 = null;
        this.W = null;
    }

    public boolean v1(int i8, int i9) {
        return getScrollingChildHelper().p(i8, i9);
    }

    boolean w0() {
        AccessibilityManager accessibilityManager = this.K;
        return accessibilityManager != null && accessibilityManager.isEnabled();
    }

    void w1(boolean z4) {
        if (this.B < 1) {
            this.B = 1;
        }
        if (!z4 && !this.E) {
            this.C = false;
        }
        if (this.B == 1) {
            if (z4 && this.C && !this.E && this.f6593n != null && this.f6591m != null) {
                C();
            }
            if (!this.E) {
                this.C = false;
            }
        }
        this.B--;
    }

    void x(int i8, int i9) {
        setMeasuredDimension(o.o(i8, getPaddingLeft() + getPaddingRight(), c0.G(this)), o.o(i9, getPaddingTop() + getPaddingBottom(), c0.F(this)));
    }

    public boolean x0() {
        return this.Q > 0;
    }

    public void x1(int i8) {
        getScrollingChildHelper().r(i8);
    }

    public void y1() {
        setScrollState(0);
        z1();
    }

    void z(View view) {
        b0 i02 = i0(view);
        H0(view);
        g gVar = this.f6591m;
        if (gVar != null && i02 != null) {
            gVar.w(i02);
        }
        List<p> list = this.L;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.L.get(size).d(view);
            }
        }
    }

    void z0(int i8) {
        if (this.f6593n == null) {
            return;
        }
        setScrollState(2);
        this.f6593n.y1(i8);
        awakenScrollBars();
    }
}
