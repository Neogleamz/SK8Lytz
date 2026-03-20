package com.google.android.material.appbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.accessibility.c;
import androidx.core.view.accessibility.f;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.core.view.p;
import androidx.core.view.v;
import androidx.customview.view.AbsSavedState;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import k7.g;
import k7.k;
import k7.l;
import x7.h;
import x7.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppBarLayout extends LinearLayout implements CoordinatorLayout.b {

    /* renamed from: x  reason: collision with root package name */
    private static final int f17327x = k.f21237h;

    /* renamed from: a  reason: collision with root package name */
    private int f17328a;

    /* renamed from: b  reason: collision with root package name */
    private int f17329b;

    /* renamed from: c  reason: collision with root package name */
    private int f17330c;

    /* renamed from: d  reason: collision with root package name */
    private int f17331d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f17332e;

    /* renamed from: f  reason: collision with root package name */
    private int f17333f;

    /* renamed from: g  reason: collision with root package name */
    private m0 f17334g;

    /* renamed from: h  reason: collision with root package name */
    private List<c> f17335h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f17336j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f17337k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f17338l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f17339m;

    /* renamed from: n  reason: collision with root package name */
    private int f17340n;

    /* renamed from: p  reason: collision with root package name */
    private WeakReference<View> f17341p;
    private ValueAnimator q;

    /* renamed from: t  reason: collision with root package name */
    private int[] f17342t;

    /* renamed from: w  reason: collision with root package name */
    private Drawable f17343w;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class BaseBehavior<T extends AppBarLayout> extends HeaderBehavior<T> {

        /* renamed from: k  reason: collision with root package name */
        private int f17344k;

        /* renamed from: l  reason: collision with root package name */
        private int f17345l;

        /* renamed from: m  reason: collision with root package name */
        private ValueAnimator f17346m;

        /* renamed from: n  reason: collision with root package name */
        private int f17347n;

        /* renamed from: o  reason: collision with root package name */
        private boolean f17348o;

        /* renamed from: p  reason: collision with root package name */
        private float f17349p;
        private WeakReference<View> q;

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class SavedState extends AbsSavedState {
            public static final Parcelable.Creator<SavedState> CREATOR = new a();

            /* renamed from: c  reason: collision with root package name */
            int f17350c;

            /* renamed from: d  reason: collision with root package name */
            float f17351d;

            /* renamed from: e  reason: collision with root package name */
            boolean f17352e;

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

            public SavedState(Parcel parcel, ClassLoader classLoader) {
                super(parcel, classLoader);
                this.f17350c = parcel.readInt();
                this.f17351d = parcel.readFloat();
                this.f17352e = parcel.readByte() != 0;
            }

            public SavedState(Parcelable parcelable) {
                super(parcelable);
            }

            @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
            public void writeToParcel(Parcel parcel, int i8) {
                super.writeToParcel(parcel, i8);
                parcel.writeInt(this.f17350c);
                parcel.writeFloat(this.f17351d);
                parcel.writeByte(this.f17352e ? (byte) 1 : (byte) 0);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a implements ValueAnimator.AnimatorUpdateListener {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ CoordinatorLayout f17353a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ AppBarLayout f17354b;

            a(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout) {
                this.f17353a = coordinatorLayout;
                this.f17354b = appBarLayout;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BaseBehavior.this.P(this.f17353a, this.f17354b, ((Integer) valueAnimator.getAnimatedValue()).intValue());
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class b implements f {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ CoordinatorLayout f17356a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ AppBarLayout f17357b;

            /* renamed from: c  reason: collision with root package name */
            final /* synthetic */ View f17358c;

            /* renamed from: d  reason: collision with root package name */
            final /* synthetic */ int f17359d;

            b(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i8) {
                this.f17356a = coordinatorLayout;
                this.f17357b = appBarLayout;
                this.f17358c = view;
                this.f17359d = i8;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // androidx.core.view.accessibility.f
            public boolean a(View view, f.a aVar) {
                BaseBehavior.this.q(this.f17356a, this.f17357b, this.f17358c, 0, this.f17359d, new int[]{0, 0}, 1);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class c implements f {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ AppBarLayout f17361a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ boolean f17362b;

            c(AppBarLayout appBarLayout, boolean z4) {
                this.f17361a = appBarLayout;
                this.f17362b = z4;
            }

            @Override // androidx.core.view.accessibility.f
            public boolean a(View view, f.a aVar) {
                this.f17361a.setExpanded(this.f17362b);
                return true;
            }
        }

        public BaseBehavior() {
            this.f17347n = -1;
        }

        public BaseBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f17347n = -1;
        }

        private void S(CoordinatorLayout coordinatorLayout, T t8, View view) {
            if (M() != (-t8.getTotalScrollRange()) && view.canScrollVertically(1)) {
                T(coordinatorLayout, t8, c.a.q, false);
            }
            if (M() != 0) {
                if (!view.canScrollVertically(-1)) {
                    T(coordinatorLayout, t8, c.a.f4919r, true);
                    return;
                }
                int i8 = -t8.getDownNestedPreScrollRange();
                if (i8 != 0) {
                    c0.p0(coordinatorLayout, c.a.f4919r, null, new b(coordinatorLayout, t8, view, i8));
                }
            }
        }

        private void T(CoordinatorLayout coordinatorLayout, T t8, c.a aVar, boolean z4) {
            c0.p0(coordinatorLayout, aVar, null, new c(t8, z4));
        }

        private void U(CoordinatorLayout coordinatorLayout, T t8, int i8, float f5) {
            int abs = Math.abs(M() - i8);
            float abs2 = Math.abs(f5);
            V(coordinatorLayout, t8, i8, abs2 > 0.0f ? Math.round((abs / abs2) * 1000.0f) * 3 : (int) (((abs / t8.getHeight()) + 1.0f) * 150.0f));
        }

        private void V(CoordinatorLayout coordinatorLayout, T t8, int i8, int i9) {
            int M = M();
            if (M == i8) {
                ValueAnimator valueAnimator = this.f17346m;
                if (valueAnimator == null || !valueAnimator.isRunning()) {
                    return;
                }
                this.f17346m.cancel();
                return;
            }
            ValueAnimator valueAnimator2 = this.f17346m;
            if (valueAnimator2 == null) {
                ValueAnimator valueAnimator3 = new ValueAnimator();
                this.f17346m = valueAnimator3;
                valueAnimator3.setInterpolator(l7.a.f21790e);
                this.f17346m.addUpdateListener(new a(coordinatorLayout, t8));
            } else {
                valueAnimator2.cancel();
            }
            this.f17346m.setDuration(Math.min(i9, 600));
            this.f17346m.setIntValues(M, i8);
            this.f17346m.start();
        }

        private boolean X(CoordinatorLayout coordinatorLayout, T t8, View view) {
            return t8.j() && coordinatorLayout.getHeight() - view.getHeight() <= t8.getHeight();
        }

        private static boolean Y(int i8, int i9) {
            return (i8 & i9) == i9;
        }

        private View Z(CoordinatorLayout coordinatorLayout) {
            int childCount = coordinatorLayout.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = coordinatorLayout.getChildAt(i8);
                if ((childAt instanceof p) || (childAt instanceof ListView) || (childAt instanceof ScrollView)) {
                    return childAt;
                }
            }
            return null;
        }

        private static View a0(AppBarLayout appBarLayout, int i8) {
            int abs = Math.abs(i8);
            int childCount = appBarLayout.getChildCount();
            for (int i9 = 0; i9 < childCount; i9++) {
                View childAt = appBarLayout.getChildAt(i9);
                if (abs >= childAt.getTop() && abs <= childAt.getBottom()) {
                    return childAt;
                }
            }
            return null;
        }

        private int b0(T t8, int i8) {
            int childCount = t8.getChildCount();
            for (int i9 = 0; i9 < childCount; i9++) {
                View childAt = t8.getChildAt(i9);
                int top = childAt.getTop();
                int bottom = childAt.getBottom();
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (Y(layoutParams.a(), 32)) {
                    top -= ((LinearLayout.LayoutParams) layoutParams).topMargin;
                    bottom += ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                }
                int i10 = -i8;
                if (top <= i10 && bottom >= i10) {
                    return i9;
                }
            }
            return -1;
        }

        private int e0(T t8, int i8) {
            int abs = Math.abs(i8);
            int childCount = t8.getChildCount();
            int i9 = 0;
            int i10 = 0;
            while (true) {
                if (i10 >= childCount) {
                    break;
                }
                View childAt = t8.getChildAt(i10);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                Interpolator b9 = layoutParams.b();
                if (abs < childAt.getTop() || abs > childAt.getBottom()) {
                    i10++;
                } else if (b9 != null) {
                    int a9 = layoutParams.a();
                    if ((a9 & 1) != 0) {
                        i9 = 0 + childAt.getHeight() + ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                        if ((a9 & 2) != 0) {
                            i9 -= c0.F(childAt);
                        }
                    }
                    if (c0.B(childAt)) {
                        i9 -= t8.getTopInset();
                    }
                    if (i9 > 0) {
                        float f5 = i9;
                        return Integer.signum(i8) * (childAt.getTop() + Math.round(f5 * b9.getInterpolation((abs - childAt.getTop()) / f5)));
                    }
                }
            }
            return i8;
        }

        private boolean p0(CoordinatorLayout coordinatorLayout, T t8) {
            List<View> w8 = coordinatorLayout.w(t8);
            int size = w8.size();
            for (int i8 = 0; i8 < size; i8++) {
                CoordinatorLayout.Behavior f5 = ((CoordinatorLayout.e) w8.get(i8).getLayoutParams()).f();
                if (f5 instanceof ScrollingViewBehavior) {
                    return ((ScrollingViewBehavior) f5).K() != 0;
                }
            }
            return false;
        }

        private void q0(CoordinatorLayout coordinatorLayout, T t8) {
            int M = M();
            int b02 = b0(t8, M);
            if (b02 >= 0) {
                View childAt = t8.getChildAt(b02);
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int a9 = layoutParams.a();
                if ((a9 & 17) == 17) {
                    int i8 = -childAt.getTop();
                    int i9 = -childAt.getBottom();
                    if (b02 == t8.getChildCount() - 1) {
                        i9 += t8.getTopInset();
                    }
                    if (Y(a9, 2)) {
                        i9 += c0.F(childAt);
                    } else if (Y(a9, 5)) {
                        int F = c0.F(childAt) + i9;
                        if (M < F) {
                            i8 = F;
                        } else {
                            i9 = F;
                        }
                    }
                    if (Y(a9, 32)) {
                        i8 += ((LinearLayout.LayoutParams) layoutParams).topMargin;
                        i9 -= ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                    }
                    if (M < (i9 + i8) / 2) {
                        i8 = i9;
                    }
                    U(coordinatorLayout, t8, t0.a.c(i8, -t8.getTotalScrollRange(), 0), 0.0f);
                }
            }
        }

        private void r0(CoordinatorLayout coordinatorLayout, T t8) {
            c0.n0(coordinatorLayout, c.a.q.b());
            c0.n0(coordinatorLayout, c.a.f4919r.b());
            View Z = Z(coordinatorLayout);
            if (Z == null || t8.getTotalScrollRange() == 0 || !(((CoordinatorLayout.e) Z.getLayoutParams()).f() instanceof ScrollingViewBehavior)) {
                return;
            }
            S(coordinatorLayout, t8, Z);
        }

        private void s0(CoordinatorLayout coordinatorLayout, T t8, int i8, int i9, boolean z4) {
            View a02 = a0(t8, i8);
            if (a02 != null) {
                int a9 = ((LayoutParams) a02.getLayoutParams()).a();
                boolean z8 = false;
                if ((a9 & 1) != 0) {
                    int F = c0.F(a02);
                    if (i9 <= 0 || (a9 & 12) == 0 ? !((a9 & 2) == 0 || (-i8) < (a02.getBottom() - F) - t8.getTopInset()) : (-i8) >= (a02.getBottom() - F) - t8.getTopInset()) {
                        z8 = true;
                    }
                }
                if (t8.l()) {
                    z8 = t8.w(Z(coordinatorLayout));
                }
                boolean u8 = t8.u(z8);
                if (z4 || (u8 && p0(coordinatorLayout, t8))) {
                    t8.jumpDrawablesToCurrentState();
                }
            }
        }

        @Override // com.google.android.material.appbar.HeaderBehavior
        int M() {
            return E() + this.f17344k;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.android.material.appbar.HeaderBehavior
        /* renamed from: W */
        public boolean H(T t8) {
            WeakReference<View> weakReference = this.q;
            if (weakReference != null) {
                View view = weakReference.get();
                return (view == null || !view.isShown() || view.canScrollVertically(-1)) ? false : true;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.android.material.appbar.HeaderBehavior
        /* renamed from: c0 */
        public int K(T t8) {
            return -t8.getDownNestedScrollRange();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.android.material.appbar.HeaderBehavior
        /* renamed from: d0 */
        public int L(T t8) {
            return t8.getTotalScrollRange();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.android.material.appbar.HeaderBehavior
        /* renamed from: f0 */
        public void N(CoordinatorLayout coordinatorLayout, T t8) {
            q0(coordinatorLayout, t8);
            if (t8.l()) {
                t8.u(t8.w(Z(coordinatorLayout)));
            }
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: g0 */
        public boolean l(CoordinatorLayout coordinatorLayout, T t8, int i8) {
            boolean l8 = super.l(coordinatorLayout, t8, i8);
            int pendingAction = t8.getPendingAction();
            int i9 = this.f17347n;
            if (i9 >= 0 && (pendingAction & 8) == 0) {
                View childAt = t8.getChildAt(i9);
                P(coordinatorLayout, t8, (-childAt.getBottom()) + (this.f17348o ? c0.F(childAt) + t8.getTopInset() : Math.round(childAt.getHeight() * this.f17349p)));
            } else if (pendingAction != 0) {
                boolean z4 = (pendingAction & 4) != 0;
                if ((pendingAction & 2) != 0) {
                    int i10 = -t8.getUpNestedPreScrollRange();
                    if (z4) {
                        U(coordinatorLayout, t8, i10, 0.0f);
                    } else {
                        P(coordinatorLayout, t8, i10);
                    }
                } else if ((pendingAction & 1) != 0) {
                    if (z4) {
                        U(coordinatorLayout, t8, 0, 0.0f);
                    } else {
                        P(coordinatorLayout, t8, 0);
                    }
                }
            }
            t8.q();
            this.f17347n = -1;
            G(t0.a.c(E(), -t8.getTotalScrollRange(), 0));
            s0(coordinatorLayout, t8, E(), 0, true);
            t8.m(E());
            r0(coordinatorLayout, t8);
            return l8;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: h0 */
        public boolean m(CoordinatorLayout coordinatorLayout, T t8, int i8, int i9, int i10, int i11) {
            if (((ViewGroup.MarginLayoutParams) ((CoordinatorLayout.e) t8.getLayoutParams())).height == -2) {
                coordinatorLayout.N(t8, i8, i9, View.MeasureSpec.makeMeasureSpec(0, 0), i11);
                return true;
            }
            return super.m(coordinatorLayout, t8, i8, i9, i10, i11);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: i0 */
        public void q(CoordinatorLayout coordinatorLayout, T t8, View view, int i8, int i9, int[] iArr, int i10) {
            int i11;
            int i12;
            if (i9 != 0) {
                if (i9 < 0) {
                    i11 = -t8.getTotalScrollRange();
                    i12 = t8.getDownNestedPreScrollRange() + i11;
                } else {
                    i11 = -t8.getUpNestedPreScrollRange();
                    i12 = 0;
                }
                int i13 = i11;
                int i14 = i12;
                if (i13 != i14) {
                    iArr[1] = O(coordinatorLayout, t8, i9, i13, i14);
                }
            }
            if (t8.l()) {
                t8.u(t8.w(view));
            }
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: j0 */
        public void t(CoordinatorLayout coordinatorLayout, T t8, View view, int i8, int i9, int i10, int i11, int i12, int[] iArr) {
            if (i11 < 0) {
                iArr[1] = O(coordinatorLayout, t8, i11, -t8.getDownNestedScrollRange(), 0);
            }
            if (i11 == 0) {
                r0(coordinatorLayout, t8);
            }
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: k0 */
        public void x(CoordinatorLayout coordinatorLayout, T t8, Parcelable parcelable) {
            if (!(parcelable instanceof SavedState)) {
                super.x(coordinatorLayout, t8, parcelable);
                this.f17347n = -1;
                return;
            }
            SavedState savedState = (SavedState) parcelable;
            super.x(coordinatorLayout, t8, savedState.a());
            this.f17347n = savedState.f17350c;
            this.f17349p = savedState.f17351d;
            this.f17348o = savedState.f17352e;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: l0 */
        public Parcelable y(CoordinatorLayout coordinatorLayout, T t8) {
            Parcelable y8 = super.y(coordinatorLayout, t8);
            int E = E();
            int childCount = t8.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = t8.getChildAt(i8);
                int bottom = childAt.getBottom() + E;
                if (childAt.getTop() + E <= 0 && bottom >= 0) {
                    SavedState savedState = new SavedState(y8);
                    savedState.f17350c = i8;
                    savedState.f17352e = bottom == c0.F(childAt) + t8.getTopInset();
                    savedState.f17351d = bottom / childAt.getHeight();
                    return savedState;
                }
            }
            return y8;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: m0 */
        public boolean A(CoordinatorLayout coordinatorLayout, T t8, View view, View view2, int i8, int i9) {
            ValueAnimator valueAnimator;
            boolean z4 = (i8 & 2) != 0 && (t8.l() || X(coordinatorLayout, t8, view));
            if (z4 && (valueAnimator = this.f17346m) != null) {
                valueAnimator.cancel();
            }
            this.q = null;
            this.f17345l = i9;
            return z4;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        /* renamed from: n0 */
        public void C(CoordinatorLayout coordinatorLayout, T t8, View view, int i8) {
            if (this.f17345l == 0 || i8 == 1) {
                q0(coordinatorLayout, t8);
                if (t8.l()) {
                    t8.u(t8.w(view));
                }
            }
            this.q = new WeakReference<>(view);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.android.material.appbar.HeaderBehavior
        /* renamed from: o0 */
        public int Q(CoordinatorLayout coordinatorLayout, T t8, int i8, int i9, int i10) {
            int M = M();
            int i11 = 0;
            if (i9 == 0 || M < i9 || M > i10) {
                this.f17344k = 0;
            } else {
                int c9 = t0.a.c(i8, i9, i10);
                if (M != c9) {
                    int e02 = t8.h() ? e0(t8, c9) : c9;
                    boolean G = G(e02);
                    i11 = M - c9;
                    this.f17344k = c9 - e02;
                    if (!G && t8.h()) {
                        coordinatorLayout.p(t8);
                    }
                    t8.m(E());
                    s0(coordinatorLayout, t8, c9, c9 < M ? -1 : 1, false);
                }
            }
            r0(coordinatorLayout, t8);
            return i11;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class Behavior extends BaseBehavior<AppBarLayout> {
        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ int E() {
            return super.E();
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior
        public /* bridge */ /* synthetic */ boolean G(int i8) {
            return super.G(i8);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ boolean g0(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i8) {
            return super.l(coordinatorLayout, appBarLayout, i8);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ boolean h0(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, int i8, int i9, int i10, int i11) {
            return super.m(coordinatorLayout, appBarLayout, i8, i9, i10, i11);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void i0(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i8, int i9, int[] iArr, int i10) {
            super.q(coordinatorLayout, appBarLayout, view, i8, i9, iArr, i10);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void j0(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i8, int i9, int i10, int i11, int i12, int[] iArr) {
            super.t(coordinatorLayout, appBarLayout, view, i8, i9, i10, i11, i12, iArr);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void k0(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, Parcelable parcelable) {
            super.x(coordinatorLayout, appBarLayout, parcelable);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ Parcelable l0(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout) {
            return super.y(coordinatorLayout, appBarLayout);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ boolean m0(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, View view2, int i8, int i9) {
            return super.A(coordinatorLayout, appBarLayout, view, view2, i8, i9);
        }

        @Override // com.google.android.material.appbar.AppBarLayout.BaseBehavior
        public /* bridge */ /* synthetic */ void n0(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, int i8) {
            super.C(coordinatorLayout, appBarLayout, view, i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends LinearLayout.LayoutParams {

        /* renamed from: a  reason: collision with root package name */
        int f17364a;

        /* renamed from: b  reason: collision with root package name */
        Interpolator f17365b;

        public LayoutParams(int i8, int i9) {
            super(i8, i9);
            this.f17364a = 1;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f17364a = 1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, l.f21434u);
            this.f17364a = obtainStyledAttributes.getInt(l.f21443v, 0);
            int i8 = l.f21452w;
            if (obtainStyledAttributes.hasValue(i8)) {
                this.f17365b = AnimationUtils.loadInterpolator(context, obtainStyledAttributes.getResourceId(i8, 0));
            }
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f17364a = 1;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f17364a = 1;
        }

        public LayoutParams(LinearLayout.LayoutParams layoutParams) {
            super(layoutParams);
            this.f17364a = 1;
        }

        public int a() {
            return this.f17364a;
        }

        public Interpolator b() {
            return this.f17365b;
        }

        boolean c() {
            int i8 = this.f17364a;
            return (i8 & 1) == 1 && (i8 & 10) != 0;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class ScrollingViewBehavior extends HeaderScrollingViewBehavior {
        public ScrollingViewBehavior() {
        }

        public ScrollingViewBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, l.f21318g6);
            O(obtainStyledAttributes.getDimensionPixelSize(l.f21327h6, 0));
            obtainStyledAttributes.recycle();
        }

        private static int R(AppBarLayout appBarLayout) {
            CoordinatorLayout.Behavior f5 = ((CoordinatorLayout.e) appBarLayout.getLayoutParams()).f();
            if (f5 instanceof BaseBehavior) {
                return ((BaseBehavior) f5).M();
            }
            return 0;
        }

        private void S(View view, View view2) {
            CoordinatorLayout.Behavior f5 = ((CoordinatorLayout.e) view2.getLayoutParams()).f();
            if (f5 instanceof BaseBehavior) {
                c0.d0(view, (((view2.getBottom() - view.getTop()) + ((BaseBehavior) f5).f17344k) + M()) - I(view2));
            }
        }

        private void T(View view, View view2) {
            if (view2 instanceof AppBarLayout) {
                AppBarLayout appBarLayout = (AppBarLayout) view2;
                if (appBarLayout.l()) {
                    appBarLayout.u(appBarLayout.w(view));
                }
            }
        }

        @Override // com.google.android.material.appbar.HeaderScrollingViewBehavior
        float J(View view) {
            int i8;
            if (view instanceof AppBarLayout) {
                AppBarLayout appBarLayout = (AppBarLayout) view;
                int totalScrollRange = appBarLayout.getTotalScrollRange();
                int downNestedPreScrollRange = appBarLayout.getDownNestedPreScrollRange();
                int R = R(appBarLayout);
                if ((downNestedPreScrollRange == 0 || totalScrollRange + R > downNestedPreScrollRange) && (i8 = totalScrollRange - downNestedPreScrollRange) != 0) {
                    return (R / i8) + 1.0f;
                }
            }
            return 0.0f;
        }

        @Override // com.google.android.material.appbar.HeaderScrollingViewBehavior
        int L(View view) {
            return view instanceof AppBarLayout ? ((AppBarLayout) view).getTotalScrollRange() : super.L(view);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.android.material.appbar.HeaderScrollingViewBehavior
        /* renamed from: Q */
        public AppBarLayout H(List<View> list) {
            int size = list.size();
            for (int i8 = 0; i8 < size; i8++) {
                View view = list.get(i8);
                if (view instanceof AppBarLayout) {
                    return (AppBarLayout) view;
                }
            }
            return null;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean e(CoordinatorLayout coordinatorLayout, View view, View view2) {
            return view2 instanceof AppBarLayout;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean h(CoordinatorLayout coordinatorLayout, View view, View view2) {
            S(view, view2);
            T(view, view2);
            return false;
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public void i(CoordinatorLayout coordinatorLayout, View view, View view2) {
            if (view2 instanceof AppBarLayout) {
                c0.n0(coordinatorLayout, c.a.q.b());
                c0.n0(coordinatorLayout, c.a.f4919r.b());
            }
        }

        @Override // com.google.android.material.appbar.ViewOffsetBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean l(CoordinatorLayout coordinatorLayout, View view, int i8) {
            return super.l(coordinatorLayout, view, i8);
        }

        @Override // com.google.android.material.appbar.HeaderScrollingViewBehavior, androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public /* bridge */ /* synthetic */ boolean m(CoordinatorLayout coordinatorLayout, View view, int i8, int i9, int i10, int i11) {
            return super.m(coordinatorLayout, view, i8, i9, i10, i11);
        }

        @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
        public boolean w(CoordinatorLayout coordinatorLayout, View view, Rect rect, boolean z4) {
            AppBarLayout H = H(coordinatorLayout.v(view));
            if (H != null) {
                rect.offset(view.getLeft(), view.getTop());
                Rect rect2 = this.f17403d;
                rect2.set(0, 0, coordinatorLayout.getWidth(), coordinatorLayout.getHeight());
                if (!rect2.contains(rect)) {
                    H.r(false, !z4);
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements v {
        a() {
        }

        @Override // androidx.core.view.v
        public m0 a(View view, m0 m0Var) {
            return AppBarLayout.this.n(m0Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ h f17367a;

        b(h hVar) {
            this.f17367a = hVar;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.f17367a.Z(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c<T extends AppBarLayout> {
        void a(T t8, int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d extends c<AppBarLayout> {
    }

    public AppBarLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, k7.b.f21046a);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public AppBarLayout(android.content.Context r11, android.util.AttributeSet r12, int r13) {
        /*
            r10 = this;
            int r4 = com.google.android.material.appbar.AppBarLayout.f17327x
            android.content.Context r11 = y7.a.c(r11, r12, r13, r4)
            r10.<init>(r11, r12, r13)
            r11 = -1
            r10.f17329b = r11
            r10.f17330c = r11
            r10.f17331d = r11
            r6 = 0
            r10.f17333f = r6
            android.content.Context r7 = r10.getContext()
            r0 = 1
            r10.setOrientation(r0)
            int r8 = android.os.Build.VERSION.SDK_INT
            r9 = 21
            if (r8 < r9) goto L27
            com.google.android.material.appbar.b.a(r10)
            com.google.android.material.appbar.b.c(r10, r12, r13, r4)
        L27:
            int[] r2 = k7.l.f21347k
            int[] r5 = new int[r6]
            r0 = r7
            r1 = r12
            r3 = r13
            android.content.res.TypedArray r12 = com.google.android.material.internal.m.h(r0, r1, r2, r3, r4, r5)
            int r13 = k7.l.f21356l
            android.graphics.drawable.Drawable r13 = r12.getDrawable(r13)
            androidx.core.view.c0.x0(r10, r13)
            android.graphics.drawable.Drawable r13 = r10.getBackground()
            boolean r13 = r13 instanceof android.graphics.drawable.ColorDrawable
            if (r13 == 0) goto L5f
            android.graphics.drawable.Drawable r13 = r10.getBackground()
            android.graphics.drawable.ColorDrawable r13 = (android.graphics.drawable.ColorDrawable) r13
            x7.h r0 = new x7.h
            r0.<init>()
            int r13 = r13.getColor()
            android.content.res.ColorStateList r13 = android.content.res.ColorStateList.valueOf(r13)
            r0.a0(r13)
            r0.P(r7)
            androidx.core.view.c0.x0(r10, r0)
        L5f:
            int r13 = k7.l.f21391p
            boolean r0 = r12.hasValue(r13)
            if (r0 == 0) goto L6e
            boolean r13 = r12.getBoolean(r13, r6)
            r10.s(r13, r6, r6)
        L6e:
            if (r8 < r9) goto L80
            int r13 = k7.l.f21383o
            boolean r0 = r12.hasValue(r13)
            if (r0 == 0) goto L80
            int r13 = r12.getDimensionPixelSize(r13, r6)
            float r13 = (float) r13
            com.google.android.material.appbar.b.b(r10, r13)
        L80:
            r13 = 26
            if (r8 < r13) goto La2
            int r13 = k7.l.f21374n
            boolean r0 = r12.hasValue(r13)
            if (r0 == 0) goto L93
            boolean r13 = r12.getBoolean(r13, r6)
            r10.setKeyboardNavigationCluster(r13)
        L93:
            int r13 = k7.l.f21365m
            boolean r0 = r12.hasValue(r13)
            if (r0 == 0) goto La2
            boolean r13 = r12.getBoolean(r13, r6)
            r10.setTouchscreenBlocksFocus(r13)
        La2:
            int r13 = k7.l.q
            boolean r13 = r12.getBoolean(r13, r6)
            r10.f17339m = r13
            int r13 = k7.l.f21408r
            int r11 = r12.getResourceId(r13, r11)
            r10.f17340n = r11
            int r11 = k7.l.f21416s
            android.graphics.drawable.Drawable r11 = r12.getDrawable(r11)
            r10.setStatusBarForeground(r11)
            r12.recycle()
            com.google.android.material.appbar.AppBarLayout$a r11 = new com.google.android.material.appbar.AppBarLayout$a
            r11.<init>()
            androidx.core.view.c0.I0(r10, r11)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.appbar.AppBarLayout.<init>(android.content.Context, android.util.AttributeSet, int):void");
    }

    private void c() {
        WeakReference<View> weakReference = this.f17341p;
        if (weakReference != null) {
            weakReference.clear();
        }
        this.f17341p = null;
    }

    private View d(View view) {
        int i8;
        if (this.f17341p == null && (i8 = this.f17340n) != -1) {
            View findViewById = view != null ? view.findViewById(i8) : null;
            if (findViewById == null && (getParent() instanceof ViewGroup)) {
                findViewById = ((ViewGroup) getParent()).findViewById(this.f17340n);
            }
            if (findViewById != null) {
                this.f17341p = new WeakReference<>(findViewById);
            }
        }
        WeakReference<View> weakReference = this.f17341p;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    private boolean i() {
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            if (((LayoutParams) getChildAt(i8).getLayoutParams()).c()) {
                return true;
            }
        }
        return false;
    }

    private void k() {
        this.f17329b = -1;
        this.f17330c = -1;
        this.f17331d = -1;
    }

    private void s(boolean z4, boolean z8, boolean z9) {
        this.f17333f = (z4 ? 1 : 2) | (z8 ? 4 : 0) | (z9 ? 8 : 0);
        requestLayout();
    }

    private boolean t(boolean z4) {
        if (this.f17337k != z4) {
            this.f17337k = z4;
            refreshDrawableState();
            return true;
        }
        return false;
    }

    private boolean v() {
        return this.f17343w != null && getTopInset() > 0;
    }

    private boolean x() {
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            return (childAt.getVisibility() == 8 || c0.B(childAt)) ? false : true;
        }
        return false;
    }

    private void y(h hVar, boolean z4) {
        float dimension = getResources().getDimension(k7.d.f21090a);
        float f5 = z4 ? 0.0f : dimension;
        if (!z4) {
            dimension = 0.0f;
        }
        ValueAnimator valueAnimator = this.q;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(f5, dimension);
        this.q = ofFloat;
        ofFloat.setDuration(getResources().getInteger(g.f21177a));
        this.q.setInterpolator(l7.a.f21786a);
        this.q.addUpdateListener(new b(hVar));
        this.q.start();
    }

    private void z() {
        setWillNotDraw(!v());
    }

    public void a(c cVar) {
        if (this.f17335h == null) {
            this.f17335h = new ArrayList();
        }
        if (cVar == null || this.f17335h.contains(cVar)) {
            return;
        }
        this.f17335h.add(cVar);
    }

    public void b(d dVar) {
        a(dVar);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (v()) {
            int save = canvas.save();
            canvas.translate(0.0f, -this.f17328a);
            this.f17343w.draw(canvas);
            canvas.restoreToCount(save);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.f17343w;
        if (drawable != null && drawable.isStateful() && drawable.setState(drawableState)) {
            invalidateDrawable(drawable);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup
    /* renamed from: e */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    /* renamed from: f */
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup
    /* renamed from: g */
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (Build.VERSION.SDK_INT < 19 || !(layoutParams instanceof LinearLayout.LayoutParams)) ? layoutParams instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams) : new LayoutParams(layoutParams) : new LayoutParams((LinearLayout.LayoutParams) layoutParams);
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.b
    public CoordinatorLayout.Behavior<AppBarLayout> getBehavior() {
        return new Behavior();
    }

    int getDownNestedPreScrollRange() {
        int i8;
        int F;
        int i9 = this.f17330c;
        if (i9 != -1) {
            return i9;
        }
        int i10 = 0;
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = getChildAt(childCount);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int measuredHeight = childAt.getMeasuredHeight();
            int i11 = layoutParams.f17364a;
            if ((i11 & 5) == 5) {
                int i12 = ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
                if ((i11 & 8) != 0) {
                    F = c0.F(childAt);
                } else if ((i11 & 2) != 0) {
                    F = measuredHeight - c0.F(childAt);
                } else {
                    i8 = i12 + measuredHeight;
                    if (childCount == 0 && c0.B(childAt)) {
                        i8 = Math.min(i8, measuredHeight - getTopInset());
                    }
                    i10 += i8;
                }
                i8 = i12 + F;
                if (childCount == 0) {
                    i8 = Math.min(i8, measuredHeight - getTopInset());
                }
                i10 += i8;
            } else if (i10 > 0) {
                break;
            }
        }
        int max = Math.max(0, i10);
        this.f17330c = max;
        return max;
    }

    int getDownNestedScrollRange() {
        int i8 = this.f17331d;
        if (i8 != -1) {
            return i8;
        }
        int childCount = getChildCount();
        int i9 = 0;
        int i10 = 0;
        while (true) {
            if (i9 >= childCount) {
                break;
            }
            View childAt = getChildAt(i9);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int measuredHeight = childAt.getMeasuredHeight() + ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
            int i11 = layoutParams.f17364a;
            if ((i11 & 1) == 0) {
                break;
            }
            i10 += measuredHeight;
            if ((i11 & 2) != 0) {
                i10 -= c0.F(childAt);
                break;
            }
            i9++;
        }
        int max = Math.max(0, i10);
        this.f17331d = max;
        return max;
    }

    public int getLiftOnScrollTargetViewId() {
        return this.f17340n;
    }

    public final int getMinimumHeightForVisibleOverlappingContent() {
        int topInset = getTopInset();
        int F = c0.F(this);
        if (F == 0) {
            int childCount = getChildCount();
            F = childCount >= 1 ? c0.F(getChildAt(childCount - 1)) : 0;
            if (F == 0) {
                return getHeight() / 3;
            }
        }
        return (F * 2) + topInset;
    }

    int getPendingAction() {
        return this.f17333f;
    }

    public Drawable getStatusBarForeground() {
        return this.f17343w;
    }

    @Deprecated
    public float getTargetElevation() {
        return 0.0f;
    }

    final int getTopInset() {
        m0 m0Var = this.f17334g;
        if (m0Var != null) {
            return m0Var.m();
        }
        return 0;
    }

    public final int getTotalScrollRange() {
        int i8 = this.f17329b;
        if (i8 != -1) {
            return i8;
        }
        int childCount = getChildCount();
        int i9 = 0;
        int i10 = 0;
        while (true) {
            if (i9 >= childCount) {
                break;
            }
            View childAt = getChildAt(i9);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            int measuredHeight = childAt.getMeasuredHeight();
            int i11 = layoutParams.f17364a;
            if ((i11 & 1) == 0) {
                break;
            }
            i10 += measuredHeight + ((LinearLayout.LayoutParams) layoutParams).topMargin + ((LinearLayout.LayoutParams) layoutParams).bottomMargin;
            if (i9 == 0 && c0.B(childAt)) {
                i10 -= getTopInset();
            }
            if ((i11 & 2) != 0) {
                i10 -= c0.F(childAt);
                break;
            }
            i9++;
        }
        int max = Math.max(0, i10);
        this.f17329b = max;
        return max;
    }

    int getUpNestedPreScrollRange() {
        return getTotalScrollRange();
    }

    boolean h() {
        return this.f17332e;
    }

    boolean j() {
        return getTotalScrollRange() != 0;
    }

    public boolean l() {
        return this.f17339m;
    }

    void m(int i8) {
        this.f17328a = i8;
        if (!willNotDraw()) {
            c0.j0(this);
        }
        List<c> list = this.f17335h;
        if (list != null) {
            int size = list.size();
            for (int i9 = 0; i9 < size; i9++) {
                c cVar = this.f17335h.get(i9);
                if (cVar != null) {
                    cVar.a(this, i8);
                }
            }
        }
    }

    m0 n(m0 m0Var) {
        m0 m0Var2 = c0.B(this) ? m0Var : null;
        if (!androidx.core.util.c.a(this.f17334g, m0Var2)) {
            this.f17334g = m0Var2;
            z();
            requestLayout();
        }
        return m0Var;
    }

    public void o(c cVar) {
        List<c> list = this.f17335h;
        if (list == null || cVar == null) {
            return;
        }
        list.remove(cVar);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        i.e(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected int[] onCreateDrawableState(int i8) {
        if (this.f17342t == null) {
            this.f17342t = new int[4];
        }
        int[] iArr = this.f17342t;
        int[] onCreateDrawableState = super.onCreateDrawableState(i8 + iArr.length);
        boolean z4 = this.f17337k;
        int i9 = k7.b.U;
        if (!z4) {
            i9 = -i9;
        }
        iArr[0] = i9;
        iArr[1] = (z4 && this.f17338l) ? k7.b.V : -k7.b.V;
        int i10 = k7.b.S;
        if (!z4) {
            i10 = -i10;
        }
        iArr[2] = i10;
        iArr[3] = (z4 && this.f17338l) ? k7.b.R : -k7.b.R;
        return LinearLayout.mergeDrawableStates(onCreateDrawableState, iArr);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        c();
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        boolean z8 = true;
        if (c0.B(this) && x()) {
            int topInset = getTopInset();
            for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
                c0.d0(getChildAt(childCount), topInset);
            }
        }
        k();
        this.f17332e = false;
        int childCount2 = getChildCount();
        int i12 = 0;
        while (true) {
            if (i12 >= childCount2) {
                break;
            } else if (((LayoutParams) getChildAt(i12).getLayoutParams()).b() != null) {
                this.f17332e = true;
                break;
            } else {
                i12++;
            }
        }
        Drawable drawable = this.f17343w;
        if (drawable != null) {
            drawable.setBounds(0, 0, getWidth(), getTopInset());
        }
        if (this.f17336j) {
            return;
        }
        if (!this.f17339m && !i()) {
            z8 = false;
        }
        t(z8);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        super.onMeasure(i8, i9);
        int mode = View.MeasureSpec.getMode(i9);
        if (mode != 1073741824 && c0.B(this) && x()) {
            int measuredHeight = getMeasuredHeight();
            if (mode == Integer.MIN_VALUE) {
                measuredHeight = t0.a.c(getMeasuredHeight() + getTopInset(), 0, View.MeasureSpec.getSize(i9));
            } else if (mode == 0) {
                measuredHeight += getTopInset();
            }
            setMeasuredDimension(getMeasuredWidth(), measuredHeight);
        }
        k();
    }

    public void p(d dVar) {
        o(dVar);
    }

    void q() {
        this.f17333f = 0;
    }

    public void r(boolean z4, boolean z8) {
        s(z4, z8, true);
    }

    @Override // android.view.View
    public void setElevation(float f5) {
        super.setElevation(f5);
        i.d(this, f5);
    }

    public void setExpanded(boolean z4) {
        r(z4, c0.W(this));
    }

    public void setLiftOnScroll(boolean z4) {
        this.f17339m = z4;
    }

    public void setLiftOnScrollTargetViewId(int i8) {
        this.f17340n = i8;
        c();
    }

    @Override // android.widget.LinearLayout
    public void setOrientation(int i8) {
        if (i8 != 1) {
            throw new IllegalArgumentException("AppBarLayout is always vertical and does not support horizontal orientation");
        }
        super.setOrientation(i8);
    }

    public void setStatusBarForeground(Drawable drawable) {
        Drawable drawable2 = this.f17343w;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            Drawable mutate = drawable != null ? drawable.mutate() : null;
            this.f17343w = mutate;
            if (mutate != null) {
                if (mutate.isStateful()) {
                    this.f17343w.setState(getDrawableState());
                }
                androidx.core.graphics.drawable.a.m(this.f17343w, c0.E(this));
                this.f17343w.setVisible(getVisibility() == 0, false);
                this.f17343w.setCallback(this);
            }
            z();
            c0.j0(this);
        }
    }

    public void setStatusBarForegroundColor(int i8) {
        setStatusBarForeground(new ColorDrawable(i8));
    }

    public void setStatusBarForegroundResource(int i8) {
        setStatusBarForeground(h.a.b(getContext(), i8));
    }

    @Deprecated
    public void setTargetElevation(float f5) {
        if (Build.VERSION.SDK_INT >= 21) {
            com.google.android.material.appbar.b.b(this, f5);
        }
    }

    @Override // android.view.View
    public void setVisibility(int i8) {
        super.setVisibility(i8);
        boolean z4 = i8 == 0;
        Drawable drawable = this.f17343w;
        if (drawable != null) {
            drawable.setVisible(z4, false);
        }
    }

    boolean u(boolean z4) {
        if (this.f17338l != z4) {
            this.f17338l = z4;
            refreshDrawableState();
            if (this.f17339m && (getBackground() instanceof h)) {
                y((h) getBackground(), z4);
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.f17343w;
    }

    boolean w(View view) {
        View d8 = d(view);
        if (d8 != null) {
            view = d8;
        }
        return view != null && (view.canScrollVertically(-1) || view.getScrollY() > 0);
    }
}
