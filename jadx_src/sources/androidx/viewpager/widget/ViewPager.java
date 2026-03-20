package androidx.viewpager.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.core.view.v;
import androidx.customview.view.AbsSavedState;
import com.example.seedpoint.R;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ViewPager extends ViewGroup {

    /* renamed from: v0  reason: collision with root package name */
    static final int[] f7771v0 = {16842931};

    /* renamed from: w0  reason: collision with root package name */
    private static final Comparator<f> f7772w0 = new a();

    /* renamed from: x0  reason: collision with root package name */
    private static final Interpolator f7773x0 = new b();

    /* renamed from: y0  reason: collision with root package name */
    private static final l f7774y0 = new l();
    private boolean A;
    private boolean B;
    private boolean C;
    private int E;
    private boolean F;
    private boolean G;
    private int H;
    private int K;
    private int L;
    private float O;
    private float P;
    private float Q;
    private float R;
    private int T;
    private VelocityTracker W;

    /* renamed from: a  reason: collision with root package name */
    private int f7775a;

    /* renamed from: a0  reason: collision with root package name */
    private int f7776a0;

    /* renamed from: b  reason: collision with root package name */
    private final ArrayList<f> f7777b;

    /* renamed from: b0  reason: collision with root package name */
    private int f7778b0;

    /* renamed from: c  reason: collision with root package name */
    private final f f7779c;

    /* renamed from: c0  reason: collision with root package name */
    private int f7780c0;

    /* renamed from: d  reason: collision with root package name */
    private final Rect f7781d;

    /* renamed from: d0  reason: collision with root package name */
    private int f7782d0;

    /* renamed from: e  reason: collision with root package name */
    androidx.viewpager.widget.a f7783e;

    /* renamed from: e0  reason: collision with root package name */
    private boolean f7784e0;

    /* renamed from: f  reason: collision with root package name */
    int f7785f;

    /* renamed from: f0  reason: collision with root package name */
    private EdgeEffect f7786f0;

    /* renamed from: g  reason: collision with root package name */
    private int f7787g;

    /* renamed from: g0  reason: collision with root package name */
    private EdgeEffect f7788g0;

    /* renamed from: h  reason: collision with root package name */
    private Parcelable f7789h;

    /* renamed from: h0  reason: collision with root package name */
    private boolean f7790h0;

    /* renamed from: i0  reason: collision with root package name */
    private boolean f7791i0;

    /* renamed from: j  reason: collision with root package name */
    private ClassLoader f7792j;

    /* renamed from: j0  reason: collision with root package name */
    private boolean f7793j0;

    /* renamed from: k  reason: collision with root package name */
    private Scroller f7794k;

    /* renamed from: k0  reason: collision with root package name */
    private int f7795k0;

    /* renamed from: l  reason: collision with root package name */
    private boolean f7796l;

    /* renamed from: l0  reason: collision with root package name */
    private List<i> f7797l0;

    /* renamed from: m  reason: collision with root package name */
    private k f7798m;

    /* renamed from: m0  reason: collision with root package name */
    private i f7799m0;

    /* renamed from: n  reason: collision with root package name */
    private int f7800n;

    /* renamed from: n0  reason: collision with root package name */
    private i f7801n0;

    /* renamed from: o0  reason: collision with root package name */
    private List<h> f7802o0;

    /* renamed from: p  reason: collision with root package name */
    private Drawable f7803p;

    /* renamed from: p0  reason: collision with root package name */
    private j f7804p0;
    private int q;

    /* renamed from: q0  reason: collision with root package name */
    private int f7805q0;

    /* renamed from: r0  reason: collision with root package name */
    private int f7806r0;

    /* renamed from: s0  reason: collision with root package name */
    private ArrayList<View> f7807s0;

    /* renamed from: t  reason: collision with root package name */
    private int f7808t;

    /* renamed from: t0  reason: collision with root package name */
    private final Runnable f7809t0;

    /* renamed from: u0  reason: collision with root package name */
    private int f7810u0;

    /* renamed from: w  reason: collision with root package name */
    private float f7811w;

    /* renamed from: x  reason: collision with root package name */
    private float f7812x;

    /* renamed from: y  reason: collision with root package name */
    private int f7813y;

    /* renamed from: z  reason: collision with root package name */
    private int f7814z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends ViewGroup.LayoutParams {

        /* renamed from: a  reason: collision with root package name */
        public boolean f7815a;

        /* renamed from: b  reason: collision with root package name */
        public int f7816b;

        /* renamed from: c  reason: collision with root package name */
        float f7817c;

        /* renamed from: d  reason: collision with root package name */
        boolean f7818d;

        /* renamed from: e  reason: collision with root package name */
        int f7819e;

        /* renamed from: f  reason: collision with root package name */
        int f7820f;

        public LayoutParams() {
            super(-1, -1);
            this.f7817c = 0.0f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f7817c = 0.0f;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ViewPager.f7771v0);
            this.f7816b = obtainStyledAttributes.getInteger(0, 48);
            obtainStyledAttributes.recycle();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        int f7821c;

        /* renamed from: d  reason: collision with root package name */
        Parcelable f7822d;

        /* renamed from: e  reason: collision with root package name */
        ClassLoader f7823e;

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
            classLoader = classLoader == null ? getClass().getClassLoader() : classLoader;
            this.f7821c = parcel.readInt();
            this.f7822d = parcel.readParcelable(classLoader);
            this.f7823e = classLoader;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.f7821c + "}";
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeInt(this.f7821c);
            parcel.writeParcelable(this.f7822d, i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements Comparator<f> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(f fVar, f fVar2) {
            return fVar.f7828b - fVar2.f7828b;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b implements Interpolator {
        b() {
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f5) {
            float f8 = f5 - 1.0f;
            return (f8 * f8 * f8 * f8 * f8) + 1.0f;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements Runnable {
        c() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewPager.this.setScrollState(0);
            ViewPager.this.E();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements v {

        /* renamed from: a  reason: collision with root package name */
        private final Rect f7825a = new Rect();

        d() {
        }

        @Override // androidx.core.view.v
        public m0 a(View view, m0 m0Var) {
            m0 e02 = c0.e0(view, m0Var);
            if (e02.q()) {
                return e02;
            }
            Rect rect = this.f7825a;
            rect.left = e02.k();
            rect.top = e02.m();
            rect.right = e02.l();
            rect.bottom = e02.j();
            int childCount = ViewPager.this.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                m0 i9 = c0.i(ViewPager.this.getChildAt(i8), e02);
                rect.left = Math.min(i9.k(), rect.left);
                rect.top = Math.min(i9.m(), rect.top);
                rect.right = Math.min(i9.l(), rect.right);
                rect.bottom = Math.min(i9.j(), rect.bottom);
            }
            return e02.s(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    @Target({ElementType.TYPE})
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public @interface e {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f {

        /* renamed from: a  reason: collision with root package name */
        Object f7827a;

        /* renamed from: b  reason: collision with root package name */
        int f7828b;

        /* renamed from: c  reason: collision with root package name */
        boolean f7829c;

        /* renamed from: d  reason: collision with root package name */
        float f7830d;

        /* renamed from: e  reason: collision with root package name */
        float f7831e;

        f() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g extends androidx.core.view.a {
        g() {
        }

        private boolean n() {
            androidx.viewpager.widget.a aVar = ViewPager.this.f7783e;
            return aVar != null && aVar.e() > 1;
        }

        @Override // androidx.core.view.a
        public void f(View view, AccessibilityEvent accessibilityEvent) {
            androidx.viewpager.widget.a aVar;
            super.f(view, accessibilityEvent);
            accessibilityEvent.setClassName(ViewPager.class.getName());
            accessibilityEvent.setScrollable(n());
            if (accessibilityEvent.getEventType() != 4096 || (aVar = ViewPager.this.f7783e) == null) {
                return;
            }
            accessibilityEvent.setItemCount(aVar.e());
            accessibilityEvent.setFromIndex(ViewPager.this.f7785f);
            accessibilityEvent.setToIndex(ViewPager.this.f7785f);
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            super.g(view, cVar);
            cVar.c0(ViewPager.class.getName());
            cVar.y0(n());
            if (ViewPager.this.canScrollHorizontally(1)) {
                cVar.a(RecognitionOptions.AZTEC);
            }
            if (ViewPager.this.canScrollHorizontally(-1)) {
                cVar.a(8192);
            }
        }

        @Override // androidx.core.view.a
        public boolean j(View view, int i8, Bundle bundle) {
            ViewPager viewPager;
            int i9;
            if (super.j(view, i8, bundle)) {
                return true;
            }
            if (i8 != 4096) {
                if (i8 != 8192 || !ViewPager.this.canScrollHorizontally(-1)) {
                    return false;
                }
                viewPager = ViewPager.this;
                i9 = viewPager.f7785f - 1;
            } else if (!ViewPager.this.canScrollHorizontally(1)) {
                return false;
            } else {
                viewPager = ViewPager.this;
                i9 = viewPager.f7785f + 1;
            }
            viewPager.setCurrentItem(i9);
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface h {
        void c(ViewPager viewPager, androidx.viewpager.widget.a aVar, androidx.viewpager.widget.a aVar2);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface i {
        void a(int i8);

        void b(int i8, float f5, int i9);

        void d(int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface j {
        void a(View view, float f5);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class k extends DataSetObserver {
        k() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            ViewPager.this.h();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            ViewPager.this.h();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class l implements Comparator<View> {
        l() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(View view, View view2) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            LayoutParams layoutParams2 = (LayoutParams) view2.getLayoutParams();
            boolean z4 = layoutParams.f7815a;
            return z4 != layoutParams2.f7815a ? z4 ? 1 : -1 : layoutParams.f7819e - layoutParams2.f7819e;
        }
    }

    public ViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f7777b = new ArrayList<>();
        this.f7779c = new f();
        this.f7781d = new Rect();
        this.f7787g = -1;
        this.f7789h = null;
        this.f7792j = null;
        this.f7811w = -3.4028235E38f;
        this.f7812x = Float.MAX_VALUE;
        this.E = 1;
        this.T = -1;
        this.f7790h0 = true;
        this.f7791i0 = false;
        this.f7809t0 = new c();
        this.f7810u0 = 0;
        v();
    }

    private boolean C(int i8) {
        if (this.f7777b.size() == 0) {
            if (this.f7790h0) {
                return false;
            }
            this.f7793j0 = false;
            y(0, 0.0f, 0);
            if (this.f7793j0) {
                return false;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
        f t8 = t();
        int clientWidth = getClientWidth();
        int i9 = this.f7800n;
        int i10 = clientWidth + i9;
        float f5 = clientWidth;
        int i11 = t8.f7828b;
        float f8 = ((i8 / f5) - t8.f7831e) / (t8.f7830d + (i9 / f5));
        this.f7793j0 = false;
        y(i11, f8, (int) (i10 * f8));
        if (this.f7793j0) {
            return true;
        }
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    }

    private boolean D(float f5) {
        boolean z4;
        boolean z8;
        float f8 = this.O - f5;
        this.O = f5;
        float scrollX = getScrollX() + f8;
        float clientWidth = getClientWidth();
        float f9 = this.f7811w * clientWidth;
        float f10 = this.f7812x * clientWidth;
        boolean z9 = false;
        f fVar = this.f7777b.get(0);
        ArrayList<f> arrayList = this.f7777b;
        f fVar2 = arrayList.get(arrayList.size() - 1);
        if (fVar.f7828b != 0) {
            f9 = fVar.f7831e * clientWidth;
            z4 = false;
        } else {
            z4 = true;
        }
        if (fVar2.f7828b != this.f7783e.e() - 1) {
            f10 = fVar2.f7831e * clientWidth;
            z8 = false;
        } else {
            z8 = true;
        }
        if (scrollX < f9) {
            if (z4) {
                this.f7786f0.onPull(Math.abs(f9 - scrollX) / clientWidth);
                z9 = true;
            }
            scrollX = f9;
        } else if (scrollX > f10) {
            if (z8) {
                this.f7788g0.onPull(Math.abs(scrollX - f10) / clientWidth);
                z9 = true;
            }
            scrollX = f10;
        }
        int i8 = (int) scrollX;
        this.O += scrollX - i8;
        scrollTo(i8, getScrollY());
        C(i8);
        return z9;
    }

    private void G(int i8, int i9, int i10, int i11) {
        int min;
        if (i9 <= 0 || this.f7777b.isEmpty()) {
            f u8 = u(this.f7785f);
            min = (int) ((u8 != null ? Math.min(u8.f7831e, this.f7812x) : 0.0f) * ((i8 - getPaddingLeft()) - getPaddingRight()));
            if (min == getScrollX()) {
                return;
            }
            g(false);
        } else if (!this.f7794k.isFinished()) {
            this.f7794k.setFinalX(getCurrentItem() * getClientWidth());
            return;
        } else {
            min = (int) ((getScrollX() / (((i9 - getPaddingLeft()) - getPaddingRight()) + i11)) * (((i8 - getPaddingLeft()) - getPaddingRight()) + i10));
        }
        scrollTo(min, getScrollY());
    }

    private void H() {
        int i8 = 0;
        while (i8 < getChildCount()) {
            if (!((LayoutParams) getChildAt(i8).getLayoutParams()).f7815a) {
                removeViewAt(i8);
                i8--;
            }
            i8++;
        }
    }

    private void K(boolean z4) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z4);
        }
    }

    private boolean L() {
        this.T = -1;
        o();
        this.f7786f0.onRelease();
        this.f7788g0.onRelease();
        return this.f7786f0.isFinished() || this.f7788g0.isFinished();
    }

    private void M(int i8, boolean z4, int i9, boolean z8) {
        f u8 = u(i8);
        int clientWidth = u8 != null ? (int) (getClientWidth() * Math.max(this.f7811w, Math.min(u8.f7831e, this.f7812x))) : 0;
        if (z4) {
            R(clientWidth, 0, i9);
            if (z8) {
                k(i8);
                return;
            }
            return;
        }
        if (z8) {
            k(i8);
        }
        g(false);
        scrollTo(clientWidth, 0);
        C(clientWidth);
    }

    private void S() {
        if (this.f7806r0 != 0) {
            ArrayList<View> arrayList = this.f7807s0;
            if (arrayList == null) {
                this.f7807s0 = new ArrayList<>();
            } else {
                arrayList.clear();
            }
            int childCount = getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                this.f7807s0.add(getChildAt(i8));
            }
            Collections.sort(this.f7807s0, f7774y0);
        }
    }

    private void e(f fVar, int i8, f fVar2) {
        int i9;
        int i10;
        f fVar3;
        f fVar4;
        int e8 = this.f7783e.e();
        int clientWidth = getClientWidth();
        float f5 = clientWidth > 0 ? this.f7800n / clientWidth : 0.0f;
        if (fVar2 != null) {
            int i11 = fVar2.f7828b;
            int i12 = fVar.f7828b;
            if (i11 < i12) {
                float f8 = fVar2.f7831e + fVar2.f7830d + f5;
                int i13 = i11 + 1;
                int i14 = 0;
                while (i13 <= fVar.f7828b && i14 < this.f7777b.size()) {
                    while (true) {
                        fVar4 = this.f7777b.get(i14);
                        if (i13 <= fVar4.f7828b || i14 >= this.f7777b.size() - 1) {
                            break;
                        }
                        i14++;
                    }
                    while (i13 < fVar4.f7828b) {
                        f8 += this.f7783e.h(i13) + f5;
                        i13++;
                    }
                    fVar4.f7831e = f8;
                    f8 += fVar4.f7830d + f5;
                    i13++;
                }
            } else if (i11 > i12) {
                int size = this.f7777b.size() - 1;
                float f9 = fVar2.f7831e;
                while (true) {
                    i11--;
                    if (i11 < fVar.f7828b || size < 0) {
                        break;
                    }
                    while (true) {
                        fVar3 = this.f7777b.get(size);
                        if (i11 >= fVar3.f7828b || size <= 0) {
                            break;
                        }
                        size--;
                    }
                    while (i11 > fVar3.f7828b) {
                        f9 -= this.f7783e.h(i11) + f5;
                        i11--;
                    }
                    f9 -= fVar3.f7830d + f5;
                    fVar3.f7831e = f9;
                }
            }
        }
        int size2 = this.f7777b.size();
        float f10 = fVar.f7831e;
        int i15 = fVar.f7828b;
        int i16 = i15 - 1;
        this.f7811w = i15 == 0 ? f10 : -3.4028235E38f;
        int i17 = e8 - 1;
        this.f7812x = i15 == i17 ? (fVar.f7830d + f10) - 1.0f : Float.MAX_VALUE;
        int i18 = i8 - 1;
        while (i18 >= 0) {
            f fVar5 = this.f7777b.get(i18);
            while (true) {
                i10 = fVar5.f7828b;
                if (i16 <= i10) {
                    break;
                }
                f10 -= this.f7783e.h(i16) + f5;
                i16--;
            }
            f10 -= fVar5.f7830d + f5;
            fVar5.f7831e = f10;
            if (i10 == 0) {
                this.f7811w = f10;
            }
            i18--;
            i16--;
        }
        float f11 = fVar.f7831e + fVar.f7830d + f5;
        int i19 = fVar.f7828b + 1;
        int i20 = i8 + 1;
        while (i20 < size2) {
            f fVar6 = this.f7777b.get(i20);
            while (true) {
                i9 = fVar6.f7828b;
                if (i19 >= i9) {
                    break;
                }
                f11 += this.f7783e.h(i19) + f5;
                i19++;
            }
            if (i9 == i17) {
                this.f7812x = (fVar6.f7830d + f11) - 1.0f;
            }
            fVar6.f7831e = f11;
            f11 += fVar6.f7830d + f5;
            i20++;
            i19++;
        }
        this.f7791i0 = false;
    }

    private void g(boolean z4) {
        boolean z8 = this.f7810u0 == 2;
        if (z8) {
            setScrollingCacheEnabled(false);
            if (!this.f7794k.isFinished()) {
                this.f7794k.abortAnimation();
                int scrollX = getScrollX();
                int scrollY = getScrollY();
                int currX = this.f7794k.getCurrX();
                int currY = this.f7794k.getCurrY();
                if (scrollX != currX || scrollY != currY) {
                    scrollTo(currX, currY);
                    if (currX != scrollX) {
                        C(currX);
                    }
                }
            }
        }
        this.C = false;
        for (int i8 = 0; i8 < this.f7777b.size(); i8++) {
            f fVar = this.f7777b.get(i8);
            if (fVar.f7829c) {
                fVar.f7829c = false;
                z8 = true;
            }
        }
        if (z8) {
            if (z4) {
                c0.l0(this, this.f7809t0);
            } else {
                this.f7809t0.run();
            }
        }
    }

    private int getClientWidth() {
        return (getMeasuredWidth() - getPaddingLeft()) - getPaddingRight();
    }

    private int i(int i8, float f5, int i9, int i10) {
        if (Math.abs(i10) <= this.f7780c0 || Math.abs(i9) <= this.f7776a0) {
            i8 += (int) (f5 + (i8 >= this.f7785f ? 0.4f : 0.6f));
        } else if (i9 <= 0) {
            i8++;
        }
        if (this.f7777b.size() > 0) {
            ArrayList<f> arrayList = this.f7777b;
            return Math.max(this.f7777b.get(0).f7828b, Math.min(i8, arrayList.get(arrayList.size() - 1).f7828b));
        }
        return i8;
    }

    private void j(int i8, float f5, int i9) {
        i iVar = this.f7799m0;
        if (iVar != null) {
            iVar.b(i8, f5, i9);
        }
        List<i> list = this.f7797l0;
        if (list != null) {
            int size = list.size();
            for (int i10 = 0; i10 < size; i10++) {
                i iVar2 = this.f7797l0.get(i10);
                if (iVar2 != null) {
                    iVar2.b(i8, f5, i9);
                }
            }
        }
        i iVar3 = this.f7801n0;
        if (iVar3 != null) {
            iVar3.b(i8, f5, i9);
        }
    }

    private void k(int i8) {
        i iVar = this.f7799m0;
        if (iVar != null) {
            iVar.a(i8);
        }
        List<i> list = this.f7797l0;
        if (list != null) {
            int size = list.size();
            for (int i9 = 0; i9 < size; i9++) {
                i iVar2 = this.f7797l0.get(i9);
                if (iVar2 != null) {
                    iVar2.a(i8);
                }
            }
        }
        i iVar3 = this.f7801n0;
        if (iVar3 != null) {
            iVar3.a(i8);
        }
    }

    private void l(int i8) {
        i iVar = this.f7799m0;
        if (iVar != null) {
            iVar.d(i8);
        }
        List<i> list = this.f7797l0;
        if (list != null) {
            int size = list.size();
            for (int i9 = 0; i9 < size; i9++) {
                i iVar2 = this.f7797l0.get(i9);
                if (iVar2 != null) {
                    iVar2.d(i8);
                }
            }
        }
        i iVar3 = this.f7801n0;
        if (iVar3 != null) {
            iVar3.d(i8);
        }
    }

    private void n(boolean z4) {
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            getChildAt(i8).setLayerType(z4 ? this.f7805q0 : 0, null);
        }
    }

    private void o() {
        this.F = false;
        this.G = false;
        VelocityTracker velocityTracker = this.W;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.W = null;
        }
    }

    private Rect q(Rect rect, View view) {
        if (rect == null) {
            rect = new Rect();
        }
        if (view == null) {
            rect.set(0, 0, 0, 0);
            return rect;
        }
        rect.left = view.getLeft();
        rect.right = view.getRight();
        rect.top = view.getTop();
        rect.bottom = view.getBottom();
        ViewParent parent = view.getParent();
        while ((parent instanceof ViewGroup) && parent != this) {
            ViewGroup viewGroup = (ViewGroup) parent;
            rect.left += viewGroup.getLeft();
            rect.right += viewGroup.getRight();
            rect.top += viewGroup.getTop();
            rect.bottom += viewGroup.getBottom();
            parent = viewGroup.getParent();
        }
        return rect;
    }

    private void setScrollingCacheEnabled(boolean z4) {
        if (this.B != z4) {
            this.B = z4;
        }
    }

    private f t() {
        int i8;
        int clientWidth = getClientWidth();
        float f5 = 0.0f;
        float scrollX = clientWidth > 0 ? getScrollX() / clientWidth : 0.0f;
        float f8 = clientWidth > 0 ? this.f7800n / clientWidth : 0.0f;
        f fVar = null;
        int i9 = 0;
        int i10 = -1;
        boolean z4 = true;
        float f9 = 0.0f;
        while (i9 < this.f7777b.size()) {
            f fVar2 = this.f7777b.get(i9);
            if (!z4 && fVar2.f7828b != (i8 = i10 + 1)) {
                fVar2 = this.f7779c;
                fVar2.f7831e = f5 + f9 + f8;
                fVar2.f7828b = i8;
                fVar2.f7830d = this.f7783e.h(i8);
                i9--;
            }
            f5 = fVar2.f7831e;
            float f10 = fVar2.f7830d + f5 + f8;
            if (!z4 && scrollX < f5) {
                return fVar;
            }
            if (scrollX < f10 || i9 == this.f7777b.size() - 1) {
                return fVar2;
            }
            i10 = fVar2.f7828b;
            f9 = fVar2.f7830d;
            i9++;
            z4 = false;
            fVar = fVar2;
        }
        return fVar;
    }

    private static boolean w(View view) {
        return view.getClass().getAnnotation(e.class) != null;
    }

    private boolean x(float f5, float f8) {
        return (f5 < ((float) this.K) && f8 > 0.0f) || (f5 > ((float) (getWidth() - this.K)) && f8 < 0.0f);
    }

    private void z(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.T) {
            int i8 = actionIndex == 0 ? 1 : 0;
            this.O = motionEvent.getX(i8);
            this.T = motionEvent.getPointerId(i8);
            VelocityTracker velocityTracker = this.W;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    boolean A() {
        int i8 = this.f7785f;
        if (i8 > 0) {
            N(i8 - 1, true);
            return true;
        }
        return false;
    }

    boolean B() {
        androidx.viewpager.widget.a aVar = this.f7783e;
        if (aVar == null || this.f7785f >= aVar.e() - 1) {
            return false;
        }
        N(this.f7785f + 1, true);
        return true;
    }

    void E() {
        F(this.f7785f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0060, code lost:
        if (r9 == r10) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0066, code lost:
        r8 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00c0, code lost:
        if (r10 >= 0) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00ce, code lost:
        if (r10 >= 0) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00dc, code lost:
        if (r10 >= 0) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00de, code lost:
        r5 = r17.f7777b.get(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00e7, code lost:
        r5 = null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void F(int r18) {
        /*
            Method dump skipped, instructions count: 583
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.viewpager.widget.ViewPager.F(int):void");
    }

    public void I(h hVar) {
        List<h> list = this.f7802o0;
        if (list != null) {
            list.remove(hVar);
        }
    }

    public void J(i iVar) {
        List<i> list = this.f7797l0;
        if (list != null) {
            list.remove(iVar);
        }
    }

    public void N(int i8, boolean z4) {
        this.C = false;
        O(i8, z4, false);
    }

    void O(int i8, boolean z4, boolean z8) {
        P(i8, z4, z8, 0);
    }

    void P(int i8, boolean z4, boolean z8, int i9) {
        androidx.viewpager.widget.a aVar = this.f7783e;
        if (aVar == null || aVar.e() <= 0) {
            setScrollingCacheEnabled(false);
        } else if (z8 || this.f7785f != i8 || this.f7777b.size() == 0) {
            if (i8 < 0) {
                i8 = 0;
            } else if (i8 >= this.f7783e.e()) {
                i8 = this.f7783e.e() - 1;
            }
            int i10 = this.E;
            int i11 = this.f7785f;
            if (i8 > i11 + i10 || i8 < i11 - i10) {
                for (int i12 = 0; i12 < this.f7777b.size(); i12++) {
                    this.f7777b.get(i12).f7829c = true;
                }
            }
            boolean z9 = this.f7785f != i8;
            if (!this.f7790h0) {
                F(i8);
                M(i8, z4, i9, z9);
                return;
            }
            this.f7785f = i8;
            if (z9) {
                k(i8);
            }
            requestLayout();
        } else {
            setScrollingCacheEnabled(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i Q(i iVar) {
        i iVar2 = this.f7801n0;
        this.f7801n0 = iVar;
        return iVar2;
    }

    void R(int i8, int i9, int i10) {
        int scrollX;
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        Scroller scroller = this.f7794k;
        if ((scroller == null || scroller.isFinished()) ? false : true) {
            scrollX = this.f7796l ? this.f7794k.getCurrX() : this.f7794k.getStartX();
            this.f7794k.abortAnimation();
            setScrollingCacheEnabled(false);
        } else {
            scrollX = getScrollX();
        }
        int i11 = scrollX;
        int scrollY = getScrollY();
        int i12 = i8 - i11;
        int i13 = i9 - scrollY;
        if (i12 == 0 && i13 == 0) {
            g(false);
            E();
            setScrollState(0);
            return;
        }
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int clientWidth = getClientWidth();
        int i14 = clientWidth / 2;
        float f5 = clientWidth;
        float f8 = i14;
        float m8 = f8 + (m(Math.min(1.0f, (Math.abs(i12) * 1.0f) / f5)) * f8);
        int abs = Math.abs(i10);
        int min = Math.min(abs > 0 ? Math.round(Math.abs(m8 / abs) * 1000.0f) * 4 : (int) (((Math.abs(i12) / ((f5 * this.f7783e.h(this.f7785f)) + this.f7800n)) + 1.0f) * 100.0f), 600);
        this.f7796l = false;
        this.f7794k.startScroll(i11, scrollY, i12, i13, min);
        c0.j0(this);
    }

    f a(int i8, int i9) {
        f fVar = new f();
        fVar.f7828b = i8;
        fVar.f7827a = this.f7783e.j(this, i8);
        fVar.f7830d = this.f7783e.h(i8);
        if (i9 < 0 || i9 >= this.f7777b.size()) {
            this.f7777b.add(fVar);
        } else {
            this.f7777b.add(i9, fVar);
        }
        return fVar;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> arrayList, int i8, int i9) {
        f s8;
        int size = arrayList.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i10 = 0; i10 < getChildCount(); i10++) {
                View childAt = getChildAt(i10);
                if (childAt.getVisibility() == 0 && (s8 = s(childAt)) != null && s8.f7828b == this.f7785f) {
                    childAt.addFocusables(arrayList, i8, i9);
                }
            }
        }
        if ((descendantFocusability != 262144 || size == arrayList.size()) && isFocusable()) {
            if ((i9 & 1) == 1 && isInTouchMode() && !isFocusableInTouchMode()) {
                return;
            }
            arrayList.add(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addTouchables(ArrayList<View> arrayList) {
        f s8;
        for (int i8 = 0; i8 < getChildCount(); i8++) {
            View childAt = getChildAt(i8);
            if (childAt.getVisibility() == 0 && (s8 = s(childAt)) != null && s8.f7828b == this.f7785f) {
                childAt.addTouchables(arrayList);
            }
        }
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i8, ViewGroup.LayoutParams layoutParams) {
        if (!checkLayoutParams(layoutParams)) {
            layoutParams = generateLayoutParams(layoutParams);
        }
        LayoutParams layoutParams2 = (LayoutParams) layoutParams;
        boolean w8 = layoutParams2.f7815a | w(view);
        layoutParams2.f7815a = w8;
        if (!this.A) {
            super.addView(view, i8, layoutParams);
        } else if (w8) {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        } else {
            layoutParams2.f7818d = true;
            addViewInLayout(view, i8, layoutParams);
        }
    }

    public void b(h hVar) {
        if (this.f7802o0 == null) {
            this.f7802o0 = new ArrayList();
        }
        this.f7802o0.add(hVar);
    }

    public void c(i iVar) {
        if (this.f7797l0 == null) {
            this.f7797l0 = new ArrayList();
        }
        this.f7797l0.add(iVar);
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int i8) {
        if (this.f7783e == null) {
            return false;
        }
        int clientWidth = getClientWidth();
        int scrollX = getScrollX();
        return i8 < 0 ? scrollX > ((int) (((float) clientWidth) * this.f7811w)) : i8 > 0 && scrollX < ((int) (((float) clientWidth) * this.f7812x));
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && super.checkLayoutParams(layoutParams);
    }

    @Override // android.view.View
    public void computeScroll() {
        this.f7796l = true;
        if (this.f7794k.isFinished() || !this.f7794k.computeScrollOffset()) {
            g(true);
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int currX = this.f7794k.getCurrX();
        int currY = this.f7794k.getCurrY();
        if (scrollX != currX || scrollY != currY) {
            scrollTo(currX, currY);
            if (!C(currX)) {
                this.f7794k.abortAnimation();
                scrollTo(0, currY);
            }
        }
        c0.j0(this);
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x00ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean d(int r7) {
        /*
            r6 = this;
            android.view.View r0 = r6.findFocus()
            r1 = 1
            r2 = 0
            r3 = 0
            if (r0 != r6) goto Lb
        L9:
            r0 = r3
            goto L69
        Lb:
            if (r0 == 0) goto L69
            android.view.ViewParent r4 = r0.getParent()
        L11:
            boolean r5 = r4 instanceof android.view.ViewGroup
            if (r5 == 0) goto L1e
            if (r4 != r6) goto L19
            r4 = r1
            goto L1f
        L19:
            android.view.ViewParent r4 = r4.getParent()
            goto L11
        L1e:
            r4 = r2
        L1f:
            if (r4 != 0) goto L69
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.Class r5 = r0.getClass()
            java.lang.String r5 = r5.getSimpleName()
            r4.append(r5)
            android.view.ViewParent r0 = r0.getParent()
        L35:
            boolean r5 = r0 instanceof android.view.ViewGroup
            if (r5 == 0) goto L4e
            java.lang.String r5 = " => "
            r4.append(r5)
            java.lang.Class r5 = r0.getClass()
            java.lang.String r5 = r5.getSimpleName()
            r4.append(r5)
            android.view.ViewParent r0 = r0.getParent()
            goto L35
        L4e:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r5 = "arrowScroll tried to find focus based on non-child current focused view "
            r0.append(r5)
            java.lang.String r4 = r4.toString()
            r0.append(r4)
            java.lang.String r0 = r0.toString()
            java.lang.String r4 = "ViewPager"
            android.util.Log.e(r4, r0)
            goto L9
        L69:
            android.view.FocusFinder r3 = android.view.FocusFinder.getInstance()
            android.view.View r3 = r3.findNextFocus(r6, r0, r7)
            r4 = 66
            r5 = 17
            if (r3 == 0) goto Lb5
            if (r3 == r0) goto Lb5
            if (r7 != r5) goto L9a
            android.graphics.Rect r1 = r6.f7781d
            android.graphics.Rect r1 = r6.q(r1, r3)
            int r1 = r1.left
            android.graphics.Rect r2 = r6.f7781d
            android.graphics.Rect r2 = r6.q(r2, r0)
            int r2 = r2.left
            if (r0 == 0) goto L94
            if (r1 < r2) goto L94
            boolean r0 = r6.A()
            goto L98
        L94:
            boolean r0 = r3.requestFocus()
        L98:
            r2 = r0
            goto Lc8
        L9a:
            if (r7 != r4) goto Lc8
            android.graphics.Rect r1 = r6.f7781d
            android.graphics.Rect r1 = r6.q(r1, r3)
            int r1 = r1.left
            android.graphics.Rect r2 = r6.f7781d
            android.graphics.Rect r2 = r6.q(r2, r0)
            int r2 = r2.left
            if (r0 == 0) goto L94
            if (r1 > r2) goto L94
            boolean r0 = r6.B()
            goto L98
        Lb5:
            if (r7 == r5) goto Lc4
            if (r7 != r1) goto Lba
            goto Lc4
        Lba:
            if (r7 == r4) goto Lbf
            r0 = 2
            if (r7 != r0) goto Lc8
        Lbf:
            boolean r2 = r6.B()
            goto Lc8
        Lc4:
            boolean r2 = r6.A()
        Lc8:
            if (r2 == 0) goto Ld1
            int r7 = android.view.SoundEffectConstants.getContantForFocusDirection(r7)
            r6.playSoundEffect(r7)
        Ld1:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.viewpager.widget.ViewPager.d(int):boolean");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || p(keyEvent);
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        f s8;
        if (accessibilityEvent.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        }
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            if (childAt.getVisibility() == 0 && (s8 = s(childAt)) != null && s8.f7828b == this.f7785f && childAt.dispatchPopulateAccessibilityEvent(accessibilityEvent)) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        androidx.viewpager.widget.a aVar;
        super.draw(canvas);
        int overScrollMode = getOverScrollMode();
        boolean z4 = false;
        if (overScrollMode == 0 || (overScrollMode == 1 && (aVar = this.f7783e) != null && aVar.e() > 1)) {
            if (!this.f7786f0.isFinished()) {
                int save = canvas.save();
                int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                int width = getWidth();
                canvas.rotate(270.0f);
                canvas.translate((-height) + getPaddingTop(), this.f7811w * width);
                this.f7786f0.setSize(height, width);
                z4 = false | this.f7786f0.draw(canvas);
                canvas.restoreToCount(save);
            }
            if (!this.f7788g0.isFinished()) {
                int save2 = canvas.save();
                int width2 = getWidth();
                int height2 = (getHeight() - getPaddingTop()) - getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate(-getPaddingTop(), (-(this.f7812x + 1.0f)) * width2);
                this.f7788g0.setSize(height2, width2);
                z4 |= this.f7788g0.draw(canvas);
                canvas.restoreToCount(save2);
            }
        } else {
            this.f7786f0.finish();
            this.f7788g0.finish();
        }
        if (z4) {
            c0.j0(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.f7803p;
        if (drawable == null || !drawable.isStateful()) {
            return;
        }
        drawable.setState(getDrawableState());
    }

    protected boolean f(View view, boolean z4, int i8, int i9, int i10) {
        int i11;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int scrollX = view.getScrollX();
            int scrollY = view.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                int i12 = i9 + scrollX;
                if (i12 >= childAt.getLeft() && i12 < childAt.getRight() && (i11 = i10 + scrollY) >= childAt.getTop() && i11 < childAt.getBottom() && f(childAt, true, i8, i12 - childAt.getLeft(), i11 - childAt.getTop())) {
                    return true;
                }
            }
        }
        return z4 && view.canScrollHorizontally(-i8);
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
        return generateDefaultLayoutParams();
    }

    public androidx.viewpager.widget.a getAdapter() {
        return this.f7783e;
    }

    @Override // android.view.ViewGroup
    protected int getChildDrawingOrder(int i8, int i9) {
        if (this.f7806r0 == 2) {
            i9 = (i8 - 1) - i9;
        }
        return ((LayoutParams) this.f7807s0.get(i9).getLayoutParams()).f7820f;
    }

    public int getCurrentItem() {
        return this.f7785f;
    }

    public int getOffscreenPageLimit() {
        return this.E;
    }

    public int getPageMargin() {
        return this.f7800n;
    }

    void h() {
        int e8 = this.f7783e.e();
        this.f7775a = e8;
        boolean z4 = this.f7777b.size() < (this.E * 2) + 1 && this.f7777b.size() < e8;
        int i8 = this.f7785f;
        int i9 = 0;
        boolean z8 = false;
        while (i9 < this.f7777b.size()) {
            f fVar = this.f7777b.get(i9);
            int f5 = this.f7783e.f(fVar.f7827a);
            if (f5 != -1) {
                if (f5 == -2) {
                    this.f7777b.remove(i9);
                    i9--;
                    if (!z8) {
                        this.f7783e.t(this);
                        z8 = true;
                    }
                    this.f7783e.b(this, fVar.f7828b, fVar.f7827a);
                    int i10 = this.f7785f;
                    if (i10 == fVar.f7828b) {
                        i8 = Math.max(0, Math.min(i10, e8 - 1));
                    }
                } else {
                    int i11 = fVar.f7828b;
                    if (i11 != f5) {
                        if (i11 == this.f7785f) {
                            i8 = f5;
                        }
                        fVar.f7828b = f5;
                    }
                }
                z4 = true;
            }
            i9++;
        }
        if (z8) {
            this.f7783e.d(this);
        }
        Collections.sort(this.f7777b, f7772w0);
        if (z4) {
            int childCount = getChildCount();
            for (int i12 = 0; i12 < childCount; i12++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i12).getLayoutParams();
                if (!layoutParams.f7815a) {
                    layoutParams.f7817c = 0.0f;
                }
            }
            O(i8, false, true);
            requestLayout();
        }
    }

    float m(float f5) {
        return (float) Math.sin((f5 - 0.5f) * 0.47123894f);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f7790h0 = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        removeCallbacks(this.f7809t0);
        Scroller scroller = this.f7794k;
        if (scroller != null && !scroller.isFinished()) {
            this.f7794k.abortAnimation();
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int width;
        int i8;
        float f5;
        float f8;
        super.onDraw(canvas);
        if (this.f7800n <= 0 || this.f7803p == null || this.f7777b.size() <= 0 || this.f7783e == null) {
            return;
        }
        int scrollX = getScrollX();
        float width2 = getWidth();
        float f9 = this.f7800n / width2;
        int i9 = 0;
        f fVar = this.f7777b.get(0);
        float f10 = fVar.f7831e;
        int size = this.f7777b.size();
        int i10 = fVar.f7828b;
        int i11 = this.f7777b.get(size - 1).f7828b;
        while (i10 < i11) {
            while (true) {
                i8 = fVar.f7828b;
                if (i10 <= i8 || i9 >= size) {
                    break;
                }
                i9++;
                fVar = this.f7777b.get(i9);
            }
            if (i10 == i8) {
                float f11 = fVar.f7831e;
                float f12 = fVar.f7830d;
                f5 = (f11 + f12) * width2;
                f10 = f11 + f12 + f9;
            } else {
                float h8 = this.f7783e.h(i10);
                f5 = (f10 + h8) * width2;
                f10 += h8 + f9;
            }
            if (this.f7800n + f5 > scrollX) {
                f8 = f9;
                this.f7803p.setBounds(Math.round(f5), this.q, Math.round(this.f7800n + f5), this.f7808t);
                this.f7803p.draw(canvas);
            } else {
                f8 = f9;
            }
            if (f5 > scrollX + width) {
                return;
            }
            i10++;
            f9 = f8;
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action == 3 || action == 1) {
            L();
            return false;
        }
        if (action != 0) {
            if (this.F) {
                return true;
            }
            if (this.G) {
                return false;
            }
        }
        if (action == 0) {
            float x8 = motionEvent.getX();
            this.Q = x8;
            this.O = x8;
            float y8 = motionEvent.getY();
            this.R = y8;
            this.P = y8;
            this.T = motionEvent.getPointerId(0);
            this.G = false;
            this.f7796l = true;
            this.f7794k.computeScrollOffset();
            if (this.f7810u0 != 2 || Math.abs(this.f7794k.getFinalX() - this.f7794k.getCurrX()) <= this.f7782d0) {
                g(false);
                this.F = false;
            } else {
                this.f7794k.abortAnimation();
                this.C = false;
                E();
                this.F = true;
                K(true);
                setScrollState(1);
            }
        } else if (action == 2) {
            int i8 = this.T;
            if (i8 != -1) {
                int findPointerIndex = motionEvent.findPointerIndex(i8);
                float x9 = motionEvent.getX(findPointerIndex);
                float f5 = x9 - this.O;
                float abs = Math.abs(f5);
                float y9 = motionEvent.getY(findPointerIndex);
                float abs2 = Math.abs(y9 - this.R);
                int i9 = (f5 > 0.0f ? 1 : (f5 == 0.0f ? 0 : -1));
                if (i9 != 0 && !x(this.O, f5) && f(this, false, (int) f5, (int) x9, (int) y9)) {
                    this.O = x9;
                    this.P = y9;
                    this.G = true;
                    return false;
                }
                int i10 = this.L;
                if (abs > i10 && abs * 0.5f > abs2) {
                    this.F = true;
                    K(true);
                    setScrollState(1);
                    float f8 = this.Q;
                    float f9 = this.L;
                    this.O = i9 > 0 ? f8 + f9 : f8 - f9;
                    this.P = y9;
                    setScrollingCacheEnabled(true);
                } else if (abs2 > i10) {
                    this.G = true;
                }
                if (this.F && D(x9)) {
                    c0.j0(this);
                }
            }
        } else if (action == 6) {
            z(motionEvent);
        }
        if (this.W == null) {
            this.W = VelocityTracker.obtain();
        }
        this.W.addMovement(motionEvent);
        return this.F;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x008e  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onLayout(boolean r19, int r20, int r21, int r22, int r23) {
        /*
            Method dump skipped, instructions count: 284
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.viewpager.widget.ViewPager.onLayout(boolean, int, int, int, int):void");
    }

    @Override // android.view.View
    protected void onMeasure(int i8, int i9) {
        LayoutParams layoutParams;
        LayoutParams layoutParams2;
        int i10;
        setMeasuredDimension(ViewGroup.getDefaultSize(0, i8), ViewGroup.getDefaultSize(0, i9));
        int measuredWidth = getMeasuredWidth();
        this.K = Math.min(measuredWidth / 10, this.H);
        int paddingLeft = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int measuredHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int childCount = getChildCount();
        int i11 = 0;
        while (true) {
            boolean z4 = true;
            int i12 = 1073741824;
            if (i11 >= childCount) {
                break;
            }
            View childAt = getChildAt(i11);
            if (childAt.getVisibility() != 8 && (layoutParams2 = (LayoutParams) childAt.getLayoutParams()) != null && layoutParams2.f7815a) {
                int i13 = layoutParams2.f7816b;
                int i14 = i13 & 7;
                int i15 = i13 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
                boolean z8 = i15 == 48 || i15 == 80;
                if (i14 != 3 && i14 != 5) {
                    z4 = false;
                }
                int i16 = Integer.MIN_VALUE;
                if (z8) {
                    i10 = Integer.MIN_VALUE;
                    i16 = 1073741824;
                } else {
                    i10 = z4 ? 1073741824 : Integer.MIN_VALUE;
                }
                int i17 = ((ViewGroup.LayoutParams) layoutParams2).width;
                if (i17 != -2) {
                    if (i17 == -1) {
                        i17 = paddingLeft;
                    }
                    i16 = 1073741824;
                } else {
                    i17 = paddingLeft;
                }
                int i18 = ((ViewGroup.LayoutParams) layoutParams2).height;
                if (i18 == -2) {
                    i18 = measuredHeight;
                    i12 = i10;
                } else if (i18 == -1) {
                    i18 = measuredHeight;
                }
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i17, i16), View.MeasureSpec.makeMeasureSpec(i18, i12));
                if (z8) {
                    measuredHeight -= childAt.getMeasuredHeight();
                } else if (z4) {
                    paddingLeft -= childAt.getMeasuredWidth();
                }
            }
            i11++;
        }
        this.f7813y = View.MeasureSpec.makeMeasureSpec(paddingLeft, 1073741824);
        this.f7814z = View.MeasureSpec.makeMeasureSpec(measuredHeight, 1073741824);
        this.A = true;
        E();
        this.A = false;
        int childCount2 = getChildCount();
        for (int i19 = 0; i19 < childCount2; i19++) {
            View childAt2 = getChildAt(i19);
            if (childAt2.getVisibility() != 8 && ((layoutParams = (LayoutParams) childAt2.getLayoutParams()) == null || !layoutParams.f7815a)) {
                childAt2.measure(View.MeasureSpec.makeMeasureSpec((int) (paddingLeft * layoutParams.f7817c), 1073741824), this.f7814z);
            }
        }
    }

    @Override // android.view.ViewGroup
    protected boolean onRequestFocusInDescendants(int i8, Rect rect) {
        int i9;
        int i10;
        f s8;
        int childCount = getChildCount();
        int i11 = -1;
        if ((i8 & 2) != 0) {
            i11 = childCount;
            i9 = 0;
            i10 = 1;
        } else {
            i9 = childCount - 1;
            i10 = -1;
        }
        while (i9 != i11) {
            View childAt = getChildAt(i9);
            if (childAt.getVisibility() == 0 && (s8 = s(childAt)) != null && s8.f7828b == this.f7785f && childAt.requestFocus(i8, rect)) {
                return true;
            }
            i9 += i10;
        }
        return false;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        androidx.viewpager.widget.a aVar = this.f7783e;
        if (aVar != null) {
            aVar.n(savedState.f7822d, savedState.f7823e);
            O(savedState.f7821c, false, true);
            return;
        }
        this.f7787g = savedState.f7821c;
        this.f7789h = savedState.f7822d;
        this.f7792j = savedState.f7823e;
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f7821c = this.f7785f;
        androidx.viewpager.widget.a aVar = this.f7783e;
        if (aVar != null) {
            savedState.f7822d = aVar.o();
        }
        return savedState;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i8, int i9, int i10, int i11) {
        super.onSizeChanged(i8, i9, i10, i11);
        if (i8 != i10) {
            int i12 = this.f7800n;
            G(i8, i10, i12, i12);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x0151  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r8) {
        /*
            Method dump skipped, instructions count: 342
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.viewpager.widget.ViewPager.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean p(KeyEvent keyEvent) {
        int i8;
        if (keyEvent.getAction() == 0) {
            int keyCode = keyEvent.getKeyCode();
            if (keyCode != 21) {
                if (keyCode != 22) {
                    if (keyCode == 61) {
                        if (keyEvent.hasNoModifiers()) {
                            return d(2);
                        }
                        if (keyEvent.hasModifiers(1)) {
                            return d(1);
                        }
                    }
                } else if (keyEvent.hasModifiers(2)) {
                    return B();
                } else {
                    i8 = 66;
                }
            } else if (keyEvent.hasModifiers(2)) {
                return A();
            } else {
                i8 = 17;
            }
            return d(i8);
        }
        return false;
    }

    f r(View view) {
        while (true) {
            ViewParent parent = view.getParent();
            if (parent == this) {
                return s(view);
            }
            if (parent == null || !(parent instanceof View)) {
                return null;
            }
            view = (View) parent;
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        if (this.A) {
            removeViewInLayout(view);
        } else {
            super.removeView(view);
        }
    }

    f s(View view) {
        for (int i8 = 0; i8 < this.f7777b.size(); i8++) {
            f fVar = this.f7777b.get(i8);
            if (this.f7783e.k(view, fVar.f7827a)) {
                return fVar;
            }
        }
        return null;
    }

    public void setAdapter(androidx.viewpager.widget.a aVar) {
        androidx.viewpager.widget.a aVar2 = this.f7783e;
        if (aVar2 != null) {
            aVar2.r(null);
            this.f7783e.t(this);
            for (int i8 = 0; i8 < this.f7777b.size(); i8++) {
                f fVar = this.f7777b.get(i8);
                this.f7783e.b(this, fVar.f7828b, fVar.f7827a);
            }
            this.f7783e.d(this);
            this.f7777b.clear();
            H();
            this.f7785f = 0;
            scrollTo(0, 0);
        }
        androidx.viewpager.widget.a aVar3 = this.f7783e;
        this.f7783e = aVar;
        this.f7775a = 0;
        if (aVar != null) {
            if (this.f7798m == null) {
                this.f7798m = new k();
            }
            this.f7783e.r(this.f7798m);
            this.C = false;
            boolean z4 = this.f7790h0;
            this.f7790h0 = true;
            this.f7775a = this.f7783e.e();
            if (this.f7787g >= 0) {
                this.f7783e.n(this.f7789h, this.f7792j);
                O(this.f7787g, false, true);
                this.f7787g = -1;
                this.f7789h = null;
                this.f7792j = null;
            } else if (z4) {
                requestLayout();
            } else {
                E();
            }
        }
        List<h> list = this.f7802o0;
        if (list == null || list.isEmpty()) {
            return;
        }
        int size = this.f7802o0.size();
        for (int i9 = 0; i9 < size; i9++) {
            this.f7802o0.get(i9).c(this, aVar3, aVar);
        }
    }

    public void setCurrentItem(int i8) {
        this.C = false;
        O(i8, !this.f7790h0, false);
    }

    public void setOffscreenPageLimit(int i8) {
        if (i8 < 1) {
            Log.w("ViewPager", "Requested offscreen page limit " + i8 + " too small; defaulting to 1");
            i8 = 1;
        }
        if (i8 != this.E) {
            this.E = i8;
            E();
        }
    }

    @Deprecated
    public void setOnPageChangeListener(i iVar) {
        this.f7799m0 = iVar;
    }

    public void setPageMargin(int i8) {
        int i9 = this.f7800n;
        this.f7800n = i8;
        int width = getWidth();
        G(width, width, i8, i9);
        requestLayout();
    }

    public void setPageMarginDrawable(int i8) {
        setPageMarginDrawable(androidx.core.content.a.f(getContext(), i8));
    }

    public void setPageMarginDrawable(Drawable drawable) {
        this.f7803p = drawable;
        if (drawable != null) {
            refreshDrawableState();
        }
        setWillNotDraw(drawable == null);
        invalidate();
    }

    void setScrollState(int i8) {
        if (this.f7810u0 == i8) {
            return;
        }
        this.f7810u0 = i8;
        if (this.f7804p0 != null) {
            n(i8 != 0);
        }
        l(i8);
    }

    f u(int i8) {
        for (int i9 = 0; i9 < this.f7777b.size(); i9++) {
            f fVar = this.f7777b.get(i9);
            if (fVar.f7828b == i8) {
                return fVar;
            }
        }
        return null;
    }

    void v() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context context = getContext();
        this.f7794k = new Scroller(context, f7773x0);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        float f5 = context.getResources().getDisplayMetrics().density;
        this.L = viewConfiguration.getScaledPagingTouchSlop();
        this.f7776a0 = (int) (400.0f * f5);
        this.f7778b0 = viewConfiguration.getScaledMaximumFlingVelocity();
        this.f7786f0 = new EdgeEffect(context);
        this.f7788g0 = new EdgeEffect(context);
        this.f7780c0 = (int) (25.0f * f5);
        this.f7782d0 = (int) (2.0f * f5);
        this.H = (int) (f5 * 16.0f);
        c0.t0(this, new g());
        if (c0.C(this) == 0) {
            c0.E0(this, 1);
        }
        c0.I0(this, new d());
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.f7803p;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0064  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void y(int r13, float r14, int r15) {
        /*
            r12 = this;
            int r0 = r12.f7795k0
            r1 = 0
            r2 = 1
            if (r0 <= 0) goto L6b
            int r0 = r12.getScrollX()
            int r3 = r12.getPaddingLeft()
            int r4 = r12.getPaddingRight()
            int r5 = r12.getWidth()
            int r6 = r12.getChildCount()
            r7 = r1
        L1b:
            if (r7 >= r6) goto L6b
            android.view.View r8 = r12.getChildAt(r7)
            android.view.ViewGroup$LayoutParams r9 = r8.getLayoutParams()
            androidx.viewpager.widget.ViewPager$LayoutParams r9 = (androidx.viewpager.widget.ViewPager.LayoutParams) r9
            boolean r10 = r9.f7815a
            if (r10 != 0) goto L2c
            goto L68
        L2c:
            int r9 = r9.f7816b
            r9 = r9 & 7
            if (r9 == r2) goto L4d
            r10 = 3
            if (r9 == r10) goto L47
            r10 = 5
            if (r9 == r10) goto L3a
            r9 = r3
            goto L5c
        L3a:
            int r9 = r5 - r4
            int r10 = r8.getMeasuredWidth()
            int r9 = r9 - r10
            int r10 = r8.getMeasuredWidth()
            int r4 = r4 + r10
            goto L59
        L47:
            int r9 = r8.getWidth()
            int r9 = r9 + r3
            goto L5c
        L4d:
            int r9 = r8.getMeasuredWidth()
            int r9 = r5 - r9
            int r9 = r9 / 2
            int r9 = java.lang.Math.max(r9, r3)
        L59:
            r11 = r9
            r9 = r3
            r3 = r11
        L5c:
            int r3 = r3 + r0
            int r10 = r8.getLeft()
            int r3 = r3 - r10
            if (r3 == 0) goto L67
            r8.offsetLeftAndRight(r3)
        L67:
            r3 = r9
        L68:
            int r7 = r7 + 1
            goto L1b
        L6b:
            r12.j(r13, r14, r15)
            androidx.viewpager.widget.ViewPager$j r13 = r12.f7804p0
            if (r13 == 0) goto L9f
            int r13 = r12.getScrollX()
            int r14 = r12.getChildCount()
        L7a:
            if (r1 >= r14) goto L9f
            android.view.View r15 = r12.getChildAt(r1)
            android.view.ViewGroup$LayoutParams r0 = r15.getLayoutParams()
            androidx.viewpager.widget.ViewPager$LayoutParams r0 = (androidx.viewpager.widget.ViewPager.LayoutParams) r0
            boolean r0 = r0.f7815a
            if (r0 == 0) goto L8b
            goto L9c
        L8b:
            int r0 = r15.getLeft()
            int r0 = r0 - r13
            float r0 = (float) r0
            int r3 = r12.getClientWidth()
            float r3 = (float) r3
            float r0 = r0 / r3
            androidx.viewpager.widget.ViewPager$j r3 = r12.f7804p0
            r3.a(r15, r0)
        L9c:
            int r1 = r1 + 1
            goto L7a
        L9f:
            r12.f7793j0 = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.viewpager.widget.ViewPager.y(int, float, int):void");
    }
}
