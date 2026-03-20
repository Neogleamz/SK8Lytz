package androidx.coordinatorlayout.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.core.view.r;
import androidx.core.view.s;
import androidx.core.view.u;
import androidx.core.view.v;
import androidx.customview.view.AbsSavedState;
import com.example.seedpoint.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class CoordinatorLayout extends ViewGroup implements r, s {
    static final String A;
    static final Class<?>[] B;
    static final ThreadLocal<Map<String, Constructor<Behavior>>> C;
    static final Comparator<View> E;
    private static final androidx.core.util.e<Rect> F;

    /* renamed from: a  reason: collision with root package name */
    private final List<View> f4362a;

    /* renamed from: b  reason: collision with root package name */
    private final androidx.coordinatorlayout.widget.a<View> f4363b;

    /* renamed from: c  reason: collision with root package name */
    private final List<View> f4364c;

    /* renamed from: d  reason: collision with root package name */
    private final List<View> f4365d;

    /* renamed from: e  reason: collision with root package name */
    private Paint f4366e;

    /* renamed from: f  reason: collision with root package name */
    private final int[] f4367f;

    /* renamed from: g  reason: collision with root package name */
    private final int[] f4368g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f4369h;

    /* renamed from: j  reason: collision with root package name */
    private boolean f4370j;

    /* renamed from: k  reason: collision with root package name */
    private int[] f4371k;

    /* renamed from: l  reason: collision with root package name */
    private View f4372l;

    /* renamed from: m  reason: collision with root package name */
    private View f4373m;

    /* renamed from: n  reason: collision with root package name */
    private f f4374n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f4375p;
    private m0 q;

    /* renamed from: t  reason: collision with root package name */
    private boolean f4376t;

    /* renamed from: w  reason: collision with root package name */
    private Drawable f4377w;

    /* renamed from: x  reason: collision with root package name */
    ViewGroup.OnHierarchyChangeListener f4378x;

    /* renamed from: y  reason: collision with root package name */
    private v f4379y;

    /* renamed from: z  reason: collision with root package name */
    private final u f4380z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class Behavior<V extends View> {
        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attributeSet) {
        }

        public boolean A(CoordinatorLayout coordinatorLayout, V v8, View view, View view2, int i8, int i9) {
            if (i9 == 0) {
                return z(coordinatorLayout, v8, view, view2, i8);
            }
            return false;
        }

        @Deprecated
        public void B(CoordinatorLayout coordinatorLayout, V v8, View view) {
        }

        public void C(CoordinatorLayout coordinatorLayout, V v8, View view, int i8) {
            if (i8 == 0) {
                B(coordinatorLayout, v8, view);
            }
        }

        public boolean D(CoordinatorLayout coordinatorLayout, V v8, MotionEvent motionEvent) {
            return false;
        }

        public boolean a(CoordinatorLayout coordinatorLayout, V v8) {
            return d(coordinatorLayout, v8) > 0.0f;
        }

        public boolean b(CoordinatorLayout coordinatorLayout, V v8, Rect rect) {
            return false;
        }

        public int c(CoordinatorLayout coordinatorLayout, V v8) {
            return -16777216;
        }

        public float d(CoordinatorLayout coordinatorLayout, V v8) {
            return 0.0f;
        }

        public boolean e(CoordinatorLayout coordinatorLayout, V v8, View view) {
            return false;
        }

        public m0 f(CoordinatorLayout coordinatorLayout, V v8, m0 m0Var) {
            return m0Var;
        }

        public void g(e eVar) {
        }

        public boolean h(CoordinatorLayout coordinatorLayout, V v8, View view) {
            return false;
        }

        public void i(CoordinatorLayout coordinatorLayout, V v8, View view) {
        }

        public void j() {
        }

        public boolean k(CoordinatorLayout coordinatorLayout, V v8, MotionEvent motionEvent) {
            return false;
        }

        public boolean l(CoordinatorLayout coordinatorLayout, V v8, int i8) {
            return false;
        }

        public boolean m(CoordinatorLayout coordinatorLayout, V v8, int i8, int i9, int i10, int i11) {
            return false;
        }

        public boolean n(CoordinatorLayout coordinatorLayout, V v8, View view, float f5, float f8, boolean z4) {
            return false;
        }

        public boolean o(CoordinatorLayout coordinatorLayout, V v8, View view, float f5, float f8) {
            return false;
        }

        @Deprecated
        public void p(CoordinatorLayout coordinatorLayout, V v8, View view, int i8, int i9, int[] iArr) {
        }

        public void q(CoordinatorLayout coordinatorLayout, V v8, View view, int i8, int i9, int[] iArr, int i10) {
            if (i10 == 0) {
                p(coordinatorLayout, v8, view, i8, i9, iArr);
            }
        }

        @Deprecated
        public void r(CoordinatorLayout coordinatorLayout, V v8, View view, int i8, int i9, int i10, int i11) {
        }

        @Deprecated
        public void s(CoordinatorLayout coordinatorLayout, V v8, View view, int i8, int i9, int i10, int i11, int i12) {
            if (i12 == 0) {
                r(coordinatorLayout, v8, view, i8, i9, i10, i11);
            }
        }

        public void t(CoordinatorLayout coordinatorLayout, V v8, View view, int i8, int i9, int i10, int i11, int i12, int[] iArr) {
            iArr[0] = iArr[0] + i10;
            iArr[1] = iArr[1] + i11;
            s(coordinatorLayout, v8, view, i8, i9, i10, i11, i12);
        }

        @Deprecated
        public void u(CoordinatorLayout coordinatorLayout, V v8, View view, View view2, int i8) {
        }

        public void v(CoordinatorLayout coordinatorLayout, V v8, View view, View view2, int i8, int i9) {
            if (i9 == 0) {
                u(coordinatorLayout, v8, view, view2, i8);
            }
        }

        public boolean w(CoordinatorLayout coordinatorLayout, V v8, Rect rect, boolean z4) {
            return false;
        }

        public void x(CoordinatorLayout coordinatorLayout, V v8, Parcelable parcelable) {
        }

        public Parcelable y(CoordinatorLayout coordinatorLayout, V v8) {
            return View.BaseSavedState.EMPTY_STATE;
        }

        @Deprecated
        public boolean z(CoordinatorLayout coordinatorLayout, V v8, View view, View view2, int i8) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        SparseArray<Parcelable> f4381c;

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
            int readInt = parcel.readInt();
            int[] iArr = new int[readInt];
            parcel.readIntArray(iArr);
            Parcelable[] readParcelableArray = parcel.readParcelableArray(classLoader);
            this.f4381c = new SparseArray<>(readInt);
            for (int i8 = 0; i8 < readInt; i8++) {
                this.f4381c.append(iArr[i8], readParcelableArray[i8]);
            }
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            SparseArray<Parcelable> sparseArray = this.f4381c;
            int size = sparseArray != null ? sparseArray.size() : 0;
            parcel.writeInt(size);
            int[] iArr = new int[size];
            Parcelable[] parcelableArr = new Parcelable[size];
            for (int i9 = 0; i9 < size; i9++) {
                iArr[i9] = this.f4381c.keyAt(i9);
                parcelableArr[i9] = this.f4381c.valueAt(i9);
            }
            parcel.writeIntArray(iArr);
            parcel.writeParcelableArray(parcelableArr, i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements v {
        a() {
        }

        @Override // androidx.core.view.v
        public m0 a(View view, m0 m0Var) {
            return CoordinatorLayout.this.a0(m0Var);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        Behavior getBehavior();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Deprecated
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public @interface c {
        Class<? extends Behavior> value();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class d implements ViewGroup.OnHierarchyChangeListener {
        d() {
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewAdded(View view, View view2) {
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = CoordinatorLayout.this.f4378x;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewAdded(view, view2);
            }
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewRemoved(View view, View view2) {
            CoordinatorLayout.this.L(2);
            ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener = CoordinatorLayout.this.f4378x;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewRemoved(view, view2);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e extends ViewGroup.MarginLayoutParams {

        /* renamed from: a  reason: collision with root package name */
        Behavior f4384a;

        /* renamed from: b  reason: collision with root package name */
        boolean f4385b;

        /* renamed from: c  reason: collision with root package name */
        public int f4386c;

        /* renamed from: d  reason: collision with root package name */
        public int f4387d;

        /* renamed from: e  reason: collision with root package name */
        public int f4388e;

        /* renamed from: f  reason: collision with root package name */
        int f4389f;

        /* renamed from: g  reason: collision with root package name */
        public int f4390g;

        /* renamed from: h  reason: collision with root package name */
        public int f4391h;

        /* renamed from: i  reason: collision with root package name */
        int f4392i;

        /* renamed from: j  reason: collision with root package name */
        int f4393j;

        /* renamed from: k  reason: collision with root package name */
        View f4394k;

        /* renamed from: l  reason: collision with root package name */
        View f4395l;

        /* renamed from: m  reason: collision with root package name */
        private boolean f4396m;

        /* renamed from: n  reason: collision with root package name */
        private boolean f4397n;

        /* renamed from: o  reason: collision with root package name */
        private boolean f4398o;

        /* renamed from: p  reason: collision with root package name */
        private boolean f4399p;
        final Rect q;

        /* renamed from: r  reason: collision with root package name */
        Object f4400r;

        public e(int i8, int i9) {
            super(i8, i9);
            this.f4385b = false;
            this.f4386c = 0;
            this.f4387d = 0;
            this.f4388e = -1;
            this.f4389f = -1;
            this.f4390g = 0;
            this.f4391h = 0;
            this.q = new Rect();
        }

        e(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f4385b = false;
            this.f4386c = 0;
            this.f4387d = 0;
            this.f4388e = -1;
            this.f4389f = -1;
            this.f4390g = 0;
            this.f4391h = 0;
            this.q = new Rect();
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, p0.c.f22252e);
            this.f4386c = obtainStyledAttributes.getInteger(p0.c.f22253f, 0);
            this.f4389f = obtainStyledAttributes.getResourceId(p0.c.f22254g, -1);
            this.f4387d = obtainStyledAttributes.getInteger(p0.c.f22255h, 0);
            this.f4388e = obtainStyledAttributes.getInteger(p0.c.f22259l, -1);
            this.f4390g = obtainStyledAttributes.getInt(p0.c.f22258k, 0);
            this.f4391h = obtainStyledAttributes.getInt(p0.c.f22257j, 0);
            int i8 = p0.c.f22256i;
            boolean hasValue = obtainStyledAttributes.hasValue(i8);
            this.f4385b = hasValue;
            if (hasValue) {
                this.f4384a = CoordinatorLayout.O(context, attributeSet, obtainStyledAttributes.getString(i8));
            }
            obtainStyledAttributes.recycle();
            Behavior behavior = this.f4384a;
            if (behavior != null) {
                behavior.g(this);
            }
        }

        public e(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f4385b = false;
            this.f4386c = 0;
            this.f4387d = 0;
            this.f4388e = -1;
            this.f4389f = -1;
            this.f4390g = 0;
            this.f4391h = 0;
            this.q = new Rect();
        }

        public e(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f4385b = false;
            this.f4386c = 0;
            this.f4387d = 0;
            this.f4388e = -1;
            this.f4389f = -1;
            this.f4390g = 0;
            this.f4391h = 0;
            this.q = new Rect();
        }

        public e(e eVar) {
            super((ViewGroup.MarginLayoutParams) eVar);
            this.f4385b = false;
            this.f4386c = 0;
            this.f4387d = 0;
            this.f4388e = -1;
            this.f4389f = -1;
            this.f4390g = 0;
            this.f4391h = 0;
            this.q = new Rect();
        }

        private void n(View view, CoordinatorLayout coordinatorLayout) {
            View findViewById = coordinatorLayout.findViewById(this.f4389f);
            this.f4394k = findViewById;
            if (findViewById != null) {
                if (findViewById != coordinatorLayout) {
                    for (ViewParent parent = findViewById.getParent(); parent != coordinatorLayout && parent != null; parent = parent.getParent()) {
                        if (parent != view) {
                            if (parent instanceof View) {
                                findViewById = (View) parent;
                            }
                        } else if (!coordinatorLayout.isInEditMode()) {
                            throw new IllegalStateException("Anchor must not be a descendant of the anchored view");
                        }
                    }
                    this.f4395l = findViewById;
                    return;
                } else if (!coordinatorLayout.isInEditMode()) {
                    throw new IllegalStateException("View can not be anchored to the the parent CoordinatorLayout");
                }
            } else if (!coordinatorLayout.isInEditMode()) {
                throw new IllegalStateException("Could not find CoordinatorLayout descendant view with id " + coordinatorLayout.getResources().getResourceName(this.f4389f) + " to anchor view " + view);
            }
            this.f4395l = null;
            this.f4394k = null;
        }

        private boolean s(View view, int i8) {
            int b9 = androidx.core.view.f.b(((e) view.getLayoutParams()).f4390g, i8);
            return b9 != 0 && (androidx.core.view.f.b(this.f4391h, i8) & b9) == b9;
        }

        private boolean t(View view, CoordinatorLayout coordinatorLayout) {
            if (this.f4394k.getId() != this.f4389f) {
                return false;
            }
            View view2 = this.f4394k;
            for (ViewParent parent = view2.getParent(); parent != coordinatorLayout; parent = parent.getParent()) {
                if (parent == null || parent == view) {
                    this.f4395l = null;
                    this.f4394k = null;
                    return false;
                }
                if (parent instanceof View) {
                    view2 = (View) parent;
                }
            }
            this.f4395l = view2;
            return true;
        }

        boolean a() {
            return this.f4394k == null && this.f4389f != -1;
        }

        boolean b(CoordinatorLayout coordinatorLayout, View view, View view2) {
            Behavior behavior;
            return view2 == this.f4395l || s(view2, c0.E(coordinatorLayout)) || ((behavior = this.f4384a) != null && behavior.e(coordinatorLayout, view, view2));
        }

        boolean c() {
            if (this.f4384a == null) {
                this.f4396m = false;
            }
            return this.f4396m;
        }

        View d(CoordinatorLayout coordinatorLayout, View view) {
            if (this.f4389f == -1) {
                this.f4395l = null;
                this.f4394k = null;
                return null;
            }
            if (this.f4394k == null || !t(view, coordinatorLayout)) {
                n(view, coordinatorLayout);
            }
            return this.f4394k;
        }

        public int e() {
            return this.f4389f;
        }

        public Behavior f() {
            return this.f4384a;
        }

        boolean g() {
            return this.f4399p;
        }

        Rect h() {
            return this.q;
        }

        boolean i(CoordinatorLayout coordinatorLayout, View view) {
            boolean z4 = this.f4396m;
            if (z4) {
                return true;
            }
            Behavior behavior = this.f4384a;
            boolean a9 = (behavior != null ? behavior.a(coordinatorLayout, view) : false) | z4;
            this.f4396m = a9;
            return a9;
        }

        boolean j(int i8) {
            if (i8 != 0) {
                if (i8 != 1) {
                    return false;
                }
                return this.f4398o;
            }
            return this.f4397n;
        }

        void k() {
            this.f4399p = false;
        }

        void l(int i8) {
            r(i8, false);
        }

        void m() {
            this.f4396m = false;
        }

        public void o(Behavior behavior) {
            Behavior behavior2 = this.f4384a;
            if (behavior2 != behavior) {
                if (behavior2 != null) {
                    behavior2.j();
                }
                this.f4384a = behavior;
                this.f4400r = null;
                this.f4385b = true;
                if (behavior != null) {
                    behavior.g(this);
                }
            }
        }

        void p(boolean z4) {
            this.f4399p = z4;
        }

        void q(Rect rect) {
            this.q.set(rect);
        }

        void r(int i8, boolean z4) {
            if (i8 == 0) {
                this.f4397n = z4;
            } else if (i8 != 1) {
            } else {
                this.f4398o = z4;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f implements ViewTreeObserver.OnPreDrawListener {
        f() {
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            CoordinatorLayout.this.L(0);
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class g implements Comparator<View> {
        g() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(View view, View view2) {
            float Q = c0.Q(view);
            float Q2 = c0.Q(view2);
            if (Q > Q2) {
                return -1;
            }
            return Q < Q2 ? 1 : 0;
        }
    }

    static {
        Package r02 = CoordinatorLayout.class.getPackage();
        A = r02 != null ? r02.getName() : null;
        if (Build.VERSION.SDK_INT >= 21) {
            E = new g();
        } else {
            E = null;
        }
        B = new Class[]{Context.class, AttributeSet.class};
        C = new ThreadLocal<>();
        F = new androidx.core.util.g(12);
    }

    public CoordinatorLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, p0.a.f22246a);
    }

    public CoordinatorLayout(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f4362a = new ArrayList();
        this.f4363b = new androidx.coordinatorlayout.widget.a<>();
        this.f4364c = new ArrayList();
        this.f4365d = new ArrayList();
        this.f4367f = new int[2];
        this.f4368g = new int[2];
        this.f4380z = new u(this);
        int[] iArr = p0.c.f22249b;
        TypedArray obtainStyledAttributes = i8 == 0 ? context.obtainStyledAttributes(attributeSet, iArr, 0, p0.b.f22247a) : context.obtainStyledAttributes(attributeSet, iArr, i8, 0);
        if (Build.VERSION.SDK_INT >= 29) {
            int[] iArr2 = p0.c.f22249b;
            if (i8 == 0) {
                saveAttributeDataForStyleable(context, iArr2, attributeSet, obtainStyledAttributes, 0, p0.b.f22247a);
            } else {
                saveAttributeDataForStyleable(context, iArr2, attributeSet, obtainStyledAttributes, i8, 0);
            }
        }
        int resourceId = obtainStyledAttributes.getResourceId(p0.c.f22250c, 0);
        if (resourceId != 0) {
            Resources resources = context.getResources();
            this.f4371k = resources.getIntArray(resourceId);
            float f5 = resources.getDisplayMetrics().density;
            int length = this.f4371k.length;
            for (int i9 = 0; i9 < length; i9++) {
                int[] iArr3 = this.f4371k;
                iArr3[i9] = (int) (iArr3[i9] * f5);
            }
        }
        this.f4377w = obtainStyledAttributes.getDrawable(p0.c.f22251d);
        obtainStyledAttributes.recycle();
        b0();
        super.setOnHierarchyChangeListener(new d());
        if (c0.C(this) == 0) {
            c0.E0(this, 1);
        }
    }

    private int A(int i8) {
        StringBuilder sb;
        int[] iArr = this.f4371k;
        if (iArr == null) {
            sb = new StringBuilder();
            sb.append("No keylines defined for ");
            sb.append(this);
            sb.append(" - attempted index lookup ");
            sb.append(i8);
        } else if (i8 >= 0 && i8 < iArr.length) {
            return iArr[i8];
        } else {
            sb = new StringBuilder();
            sb.append("Keyline index ");
            sb.append(i8);
            sb.append(" out of range for ");
            sb.append(this);
        }
        Log.e("CoordinatorLayout", sb.toString());
        return 0;
    }

    private void D(List<View> list) {
        list.clear();
        boolean isChildrenDrawingOrderEnabled = isChildrenDrawingOrderEnabled();
        int childCount = getChildCount();
        for (int i8 = childCount - 1; i8 >= 0; i8--) {
            list.add(getChildAt(isChildrenDrawingOrderEnabled ? getChildDrawingOrder(childCount, i8) : i8));
        }
        Comparator<View> comparator = E;
        if (comparator != null) {
            Collections.sort(list, comparator);
        }
    }

    private boolean E(View view) {
        return this.f4363b.j(view);
    }

    private void G(View view, int i8) {
        e eVar = (e) view.getLayoutParams();
        Rect e8 = e();
        e8.set(getPaddingLeft() + ((ViewGroup.MarginLayoutParams) eVar).leftMargin, getPaddingTop() + ((ViewGroup.MarginLayoutParams) eVar).topMargin, (getWidth() - getPaddingRight()) - ((ViewGroup.MarginLayoutParams) eVar).rightMargin, (getHeight() - getPaddingBottom()) - ((ViewGroup.MarginLayoutParams) eVar).bottomMargin);
        if (this.q != null && c0.B(this) && !c0.B(view)) {
            e8.left += this.q.k();
            e8.top += this.q.m();
            e8.right -= this.q.l();
            e8.bottom -= this.q.j();
        }
        Rect e9 = e();
        androidx.core.view.f.a(W(eVar.f4386c), view.getMeasuredWidth(), view.getMeasuredHeight(), e8, e9, i8);
        view.layout(e9.left, e9.top, e9.right, e9.bottom);
        S(e8);
        S(e9);
    }

    private void H(View view, View view2, int i8) {
        Rect e8 = e();
        Rect e9 = e();
        try {
            x(view2, e8);
            y(view, i8, e8, e9);
            view.layout(e9.left, e9.top, e9.right, e9.bottom);
        } finally {
            S(e8);
            S(e9);
        }
    }

    private void I(View view, int i8, int i9) {
        e eVar = (e) view.getLayoutParams();
        int b9 = androidx.core.view.f.b(X(eVar.f4386c), i9);
        int i10 = b9 & 7;
        int i11 = b9 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
        int width = getWidth();
        int height = getHeight();
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        if (i9 == 1) {
            i8 = width - i8;
        }
        int A2 = A(i8) - measuredWidth;
        int i12 = 0;
        if (i10 == 1) {
            A2 += measuredWidth / 2;
        } else if (i10 == 5) {
            A2 += measuredWidth;
        }
        if (i11 == 16) {
            i12 = 0 + (measuredHeight / 2);
        } else if (i11 == 80) {
            i12 = measuredHeight + 0;
        }
        int max = Math.max(getPaddingLeft() + ((ViewGroup.MarginLayoutParams) eVar).leftMargin, Math.min(A2, ((width - getPaddingRight()) - measuredWidth) - ((ViewGroup.MarginLayoutParams) eVar).rightMargin));
        int max2 = Math.max(getPaddingTop() + ((ViewGroup.MarginLayoutParams) eVar).topMargin, Math.min(i12, ((height - getPaddingBottom()) - measuredHeight) - ((ViewGroup.MarginLayoutParams) eVar).bottomMargin));
        view.layout(max, max2, measuredWidth + max, measuredHeight + max2);
    }

    private void J(View view, Rect rect, int i8) {
        boolean z4;
        boolean z8;
        int width;
        int i9;
        int i10;
        int i11;
        int height;
        int i12;
        int i13;
        int i14;
        if (c0.W(view) && view.getWidth() > 0 && view.getHeight() > 0) {
            e eVar = (e) view.getLayoutParams();
            Behavior f5 = eVar.f();
            Rect e8 = e();
            Rect e9 = e();
            e9.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
            if (f5 == null || !f5.b(this, view, e8)) {
                e8.set(e9);
            } else if (!e9.contains(e8)) {
                throw new IllegalArgumentException("Rect should be within the child's bounds. Rect:" + e8.toShortString() + " | Bounds:" + e9.toShortString());
            }
            S(e9);
            if (e8.isEmpty()) {
                S(e8);
                return;
            }
            int b9 = androidx.core.view.f.b(eVar.f4391h, i8);
            boolean z9 = true;
            if ((b9 & 48) != 48 || (i13 = (e8.top - ((ViewGroup.MarginLayoutParams) eVar).topMargin) - eVar.f4393j) >= (i14 = rect.top)) {
                z4 = false;
            } else {
                Z(view, i14 - i13);
                z4 = true;
            }
            if ((b9 & 80) == 80 && (height = ((getHeight() - e8.bottom) - ((ViewGroup.MarginLayoutParams) eVar).bottomMargin) + eVar.f4393j) < (i12 = rect.bottom)) {
                Z(view, height - i12);
                z4 = true;
            }
            if (!z4) {
                Z(view, 0);
            }
            if ((b9 & 3) != 3 || (i10 = (e8.left - ((ViewGroup.MarginLayoutParams) eVar).leftMargin) - eVar.f4392i) >= (i11 = rect.left)) {
                z8 = false;
            } else {
                Y(view, i11 - i10);
                z8 = true;
            }
            if ((b9 & 5) != 5 || (width = ((getWidth() - e8.right) - ((ViewGroup.MarginLayoutParams) eVar).rightMargin) + eVar.f4392i) >= (i9 = rect.right)) {
                z9 = z8;
            } else {
                Y(view, width - i9);
            }
            if (!z9) {
                Y(view, 0);
            }
            S(e8);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static Behavior O(Context context, AttributeSet attributeSet, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.startsWith(".")) {
            str = context.getPackageName() + str;
        } else if (str.indexOf(46) < 0) {
            String str2 = A;
            if (!TextUtils.isEmpty(str2)) {
                str = str2 + '.' + str;
            }
        }
        try {
            ThreadLocal<Map<String, Constructor<Behavior>>> threadLocal = C;
            Map<String, Constructor<Behavior>> map = threadLocal.get();
            if (map == null) {
                map = new HashMap<>();
                threadLocal.set(map);
            }
            Constructor<Behavior> constructor = map.get(str);
            if (constructor == null) {
                constructor = Class.forName(str, false, context.getClassLoader()).getConstructor(B);
                constructor.setAccessible(true);
                map.put(str, constructor);
            }
            return constructor.newInstance(context, attributeSet);
        } catch (Exception e8) {
            throw new RuntimeException("Could not inflate Behavior subclass " + str, e8);
        }
    }

    private boolean P(MotionEvent motionEvent, int i8) {
        int actionMasked = motionEvent.getActionMasked();
        List<View> list = this.f4364c;
        D(list);
        int size = list.size();
        MotionEvent motionEvent2 = null;
        boolean z4 = false;
        boolean z8 = false;
        for (int i9 = 0; i9 < size; i9++) {
            View view = list.get(i9);
            e eVar = (e) view.getLayoutParams();
            Behavior f5 = eVar.f();
            if (!(z4 || z8) || actionMasked == 0) {
                if (!z4 && f5 != null) {
                    if (i8 == 0) {
                        z4 = f5.k(this, view, motionEvent);
                    } else if (i8 == 1) {
                        z4 = f5.D(this, view, motionEvent);
                    }
                    if (z4) {
                        this.f4372l = view;
                    }
                }
                boolean c9 = eVar.c();
                boolean i10 = eVar.i(this, view);
                z8 = i10 && !c9;
                if (i10 && !z8) {
                    break;
                }
            } else if (f5 != null) {
                if (motionEvent2 == null) {
                    long uptimeMillis = SystemClock.uptimeMillis();
                    motionEvent2 = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                }
                if (i8 == 0) {
                    f5.k(this, view, motionEvent2);
                } else if (i8 == 1) {
                    f5.D(this, view, motionEvent2);
                }
            }
        }
        list.clear();
        return z4;
    }

    private void Q() {
        this.f4362a.clear();
        this.f4363b.c();
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            e C2 = C(childAt);
            C2.d(this, childAt);
            this.f4363b.b(childAt);
            for (int i9 = 0; i9 < childCount; i9++) {
                if (i9 != i8) {
                    View childAt2 = getChildAt(i9);
                    if (C2.b(this, childAt, childAt2)) {
                        if (!this.f4363b.d(childAt2)) {
                            this.f4363b.b(childAt2);
                        }
                        this.f4363b.a(childAt2, childAt);
                    }
                }
            }
        }
        this.f4362a.addAll(this.f4363b.i());
        Collections.reverse(this.f4362a);
    }

    private static void S(Rect rect) {
        rect.setEmpty();
        F.a(rect);
    }

    private void U(boolean z4) {
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            Behavior f5 = ((e) childAt.getLayoutParams()).f();
            if (f5 != null) {
                long uptimeMillis = SystemClock.uptimeMillis();
                MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                if (z4) {
                    f5.k(this, childAt, obtain);
                } else {
                    f5.D(this, childAt, obtain);
                }
                obtain.recycle();
            }
        }
        for (int i9 = 0; i9 < childCount; i9++) {
            ((e) getChildAt(i9).getLayoutParams()).m();
        }
        this.f4372l = null;
        this.f4369h = false;
    }

    private static int V(int i8) {
        if (i8 == 0) {
            return 17;
        }
        return i8;
    }

    private static int W(int i8) {
        if ((i8 & 7) == 0) {
            i8 |= 8388611;
        }
        return (i8 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle) == 0 ? i8 | 48 : i8;
    }

    private static int X(int i8) {
        if (i8 == 0) {
            return 8388661;
        }
        return i8;
    }

    private void Y(View view, int i8) {
        e eVar = (e) view.getLayoutParams();
        int i9 = eVar.f4392i;
        if (i9 != i8) {
            c0.c0(view, i8 - i9);
            eVar.f4392i = i8;
        }
    }

    private void Z(View view, int i8) {
        e eVar = (e) view.getLayoutParams();
        int i9 = eVar.f4393j;
        if (i9 != i8) {
            c0.d0(view, i8 - i9);
            eVar.f4393j = i8;
        }
    }

    private void b0() {
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        if (!c0.B(this)) {
            c0.I0(this, null);
            return;
        }
        if (this.f4379y == null) {
            this.f4379y = new a();
        }
        c0.I0(this, this.f4379y);
        setSystemUiVisibility(1280);
    }

    private static Rect e() {
        Rect b9 = F.b();
        return b9 == null ? new Rect() : b9;
    }

    private static int g(int i8, int i9, int i10) {
        return i8 < i9 ? i9 : i8 > i10 ? i10 : i8;
    }

    private void h(e eVar, Rect rect, int i8, int i9) {
        int width = getWidth();
        int height = getHeight();
        int max = Math.max(getPaddingLeft() + ((ViewGroup.MarginLayoutParams) eVar).leftMargin, Math.min(rect.left, ((width - getPaddingRight()) - i8) - ((ViewGroup.MarginLayoutParams) eVar).rightMargin));
        int max2 = Math.max(getPaddingTop() + ((ViewGroup.MarginLayoutParams) eVar).topMargin, Math.min(rect.top, ((height - getPaddingBottom()) - i9) - ((ViewGroup.MarginLayoutParams) eVar).bottomMargin));
        rect.set(max, max2, i8 + max, i9 + max2);
    }

    private m0 i(m0 m0Var) {
        Behavior f5;
        if (m0Var.q()) {
            return m0Var;
        }
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            if (c0.B(childAt) && (f5 = ((e) childAt.getLayoutParams()).f()) != null) {
                m0Var = f5.f(this, childAt, m0Var);
                if (m0Var.q()) {
                    break;
                }
            }
        }
        return m0Var;
    }

    private void z(View view, int i8, Rect rect, Rect rect2, e eVar, int i9, int i10) {
        int b9 = androidx.core.view.f.b(V(eVar.f4386c), i8);
        int b10 = androidx.core.view.f.b(W(eVar.f4387d), i8);
        int i11 = b9 & 7;
        int i12 = b9 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
        int i13 = b10 & 7;
        int i14 = b10 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
        int width = i13 != 1 ? i13 != 5 ? rect.left : rect.right : rect.left + (rect.width() / 2);
        int height = i14 != 16 ? i14 != 80 ? rect.top : rect.bottom : rect.top + (rect.height() / 2);
        if (i11 == 1) {
            width -= i9 / 2;
        } else if (i11 != 5) {
            width -= i9;
        }
        if (i12 == 16) {
            height -= i10 / 2;
        } else if (i12 != 80) {
            height -= i10;
        }
        rect2.set(width, height, i9 + width, i10 + height);
    }

    void B(View view, Rect rect) {
        rect.set(((e) view.getLayoutParams()).h());
    }

    e C(View view) {
        e eVar = (e) view.getLayoutParams();
        if (!eVar.f4385b) {
            if (view instanceof b) {
                Behavior behavior = ((b) view).getBehavior();
                if (behavior == null) {
                    Log.e("CoordinatorLayout", "Attached behavior class is null");
                }
                eVar.o(behavior);
            } else {
                c cVar = null;
                for (Class<?> cls = view.getClass(); cls != null; cls = cls.getSuperclass()) {
                    cVar = (c) cls.getAnnotation(c.class);
                    if (cVar != null) {
                        break;
                    }
                }
                if (cVar != null) {
                    try {
                        eVar.o(cVar.value().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
                    } catch (Exception e8) {
                        Log.e("CoordinatorLayout", "Default behavior class " + cVar.value().getName() + " could not be instantiated. Did you forget a default constructor?", e8);
                    }
                }
            }
            eVar.f4385b = true;
        }
        return eVar;
    }

    public boolean F(View view, int i8, int i9) {
        Rect e8 = e();
        x(view, e8);
        try {
            return e8.contains(i8, i9);
        } finally {
            S(e8);
        }
    }

    void K(View view, int i8) {
        Behavior f5;
        e eVar = (e) view.getLayoutParams();
        if (eVar.f4394k != null) {
            Rect e8 = e();
            Rect e9 = e();
            Rect e10 = e();
            x(eVar.f4394k, e8);
            boolean z4 = false;
            u(view, false, e9);
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();
            z(view, i8, e8, e10, eVar, measuredWidth, measuredHeight);
            z4 = (e10.left == e9.left && e10.top == e9.top) ? true : true;
            h(eVar, e10, measuredWidth, measuredHeight);
            int i9 = e10.left - e9.left;
            int i10 = e10.top - e9.top;
            if (i9 != 0) {
                c0.c0(view, i9);
            }
            if (i10 != 0) {
                c0.d0(view, i10);
            }
            if (z4 && (f5 = eVar.f()) != null) {
                f5.h(this, view, eVar.f4394k);
            }
            S(e8);
            S(e9);
            S(e10);
        }
    }

    final void L(int i8) {
        boolean z4;
        int E2 = c0.E(this);
        int size = this.f4362a.size();
        Rect e8 = e();
        Rect e9 = e();
        Rect e10 = e();
        for (int i9 = 0; i9 < size; i9++) {
            View view = this.f4362a.get(i9);
            e eVar = (e) view.getLayoutParams();
            if (i8 != 0 || view.getVisibility() != 8) {
                for (int i10 = 0; i10 < i9; i10++) {
                    if (eVar.f4395l == this.f4362a.get(i10)) {
                        K(view, E2);
                    }
                }
                u(view, true, e9);
                if (eVar.f4390g != 0 && !e9.isEmpty()) {
                    int b9 = androidx.core.view.f.b(eVar.f4390g, E2);
                    int i11 = b9 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
                    if (i11 == 48) {
                        e8.top = Math.max(e8.top, e9.bottom);
                    } else if (i11 == 80) {
                        e8.bottom = Math.max(e8.bottom, getHeight() - e9.top);
                    }
                    int i12 = b9 & 7;
                    if (i12 == 3) {
                        e8.left = Math.max(e8.left, e9.right);
                    } else if (i12 == 5) {
                        e8.right = Math.max(e8.right, getWidth() - e9.left);
                    }
                }
                if (eVar.f4391h != 0 && view.getVisibility() == 0) {
                    J(view, e8, E2);
                }
                if (i8 != 2) {
                    B(view, e10);
                    if (!e10.equals(e9)) {
                        R(view, e9);
                    }
                }
                for (int i13 = i9 + 1; i13 < size; i13++) {
                    View view2 = this.f4362a.get(i13);
                    e eVar2 = (e) view2.getLayoutParams();
                    Behavior f5 = eVar2.f();
                    if (f5 != null && f5.e(this, view2, view)) {
                        if (i8 == 0 && eVar2.g()) {
                            eVar2.k();
                        } else {
                            if (i8 != 2) {
                                z4 = f5.h(this, view2, view);
                            } else {
                                f5.i(this, view2, view);
                                z4 = true;
                            }
                            if (i8 == 1) {
                                eVar2.p(z4);
                            }
                        }
                    }
                }
            }
        }
        S(e8);
        S(e9);
        S(e10);
    }

    public void M(View view, int i8) {
        e eVar = (e) view.getLayoutParams();
        if (eVar.a()) {
            throw new IllegalStateException("An anchor may not be changed after CoordinatorLayout measurement begins before layout is complete.");
        }
        View view2 = eVar.f4394k;
        if (view2 != null) {
            H(view, view2, i8);
            return;
        }
        int i9 = eVar.f4388e;
        if (i9 >= 0) {
            I(view, i9, i8);
        } else {
            G(view, i8);
        }
    }

    public void N(View view, int i8, int i9, int i10, int i11) {
        measureChildWithMargins(view, i8, i9, i10, i11);
    }

    void R(View view, Rect rect) {
        ((e) view.getLayoutParams()).q(rect);
    }

    void T() {
        if (this.f4370j && this.f4374n != null) {
            getViewTreeObserver().removeOnPreDrawListener(this.f4374n);
        }
        this.f4375p = false;
    }

    final m0 a0(m0 m0Var) {
        if (androidx.core.util.c.a(this.q, m0Var)) {
            return m0Var;
        }
        this.q = m0Var;
        boolean z4 = true;
        boolean z8 = m0Var != null && m0Var.m() > 0;
        this.f4376t = z8;
        if (z8 || getBackground() != null) {
            z4 = false;
        }
        setWillNotDraw(z4);
        m0 i8 = i(m0Var);
        requestLayout();
        return i8;
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof e) && super.checkLayoutParams(layoutParams);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j8) {
        e eVar = (e) view.getLayoutParams();
        Behavior behavior = eVar.f4384a;
        if (behavior != null) {
            float d8 = behavior.d(this, view);
            if (d8 > 0.0f) {
                if (this.f4366e == null) {
                    this.f4366e = new Paint();
                }
                this.f4366e.setColor(eVar.f4384a.c(this, view));
                this.f4366e.setAlpha(g(Math.round(d8 * 255.0f), 0, 255));
                int save = canvas.save();
                if (view.isOpaque()) {
                    canvas.clipRect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom(), Region.Op.DIFFERENCE);
                }
                canvas.drawRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom(), this.f4366e);
                canvas.restoreToCount(save);
            }
        }
        return super.drawChild(canvas, view, j8);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.f4377w;
        boolean z4 = false;
        if (drawable != null && drawable.isStateful()) {
            z4 = false | drawable.setState(drawableState);
        }
        if (z4) {
            invalidate();
        }
    }

    void f() {
        if (this.f4370j) {
            if (this.f4374n == null) {
                this.f4374n = new f();
            }
            getViewTreeObserver().addOnPreDrawListener(this.f4374n);
        }
        this.f4375p = true;
    }

    final List<View> getDependencySortedChildren() {
        Q();
        return Collections.unmodifiableList(this.f4362a);
    }

    public final m0 getLastWindowInsets() {
        return this.q;
    }

    @Override // android.view.ViewGroup
    public int getNestedScrollAxes() {
        return this.f4380z.a();
    }

    public Drawable getStatusBarBackground() {
        return this.f4377w;
    }

    @Override // android.view.View
    protected int getSuggestedMinimumHeight() {
        return Math.max(super.getSuggestedMinimumHeight(), getPaddingTop() + getPaddingBottom());
    }

    @Override // android.view.View
    protected int getSuggestedMinimumWidth() {
        return Math.max(super.getSuggestedMinimumWidth(), getPaddingLeft() + getPaddingRight());
    }

    @Override // androidx.core.view.s
    public void j(View view, int i8, int i9, int i10, int i11, int i12, int[] iArr) {
        Behavior f5;
        boolean z4;
        int min;
        int childCount = getChildCount();
        boolean z8 = false;
        int i13 = 0;
        int i14 = 0;
        for (int i15 = 0; i15 < childCount; i15++) {
            View childAt = getChildAt(i15);
            if (childAt.getVisibility() != 8) {
                e eVar = (e) childAt.getLayoutParams();
                if (eVar.j(i12) && (f5 = eVar.f()) != null) {
                    int[] iArr2 = this.f4367f;
                    iArr2[0] = 0;
                    iArr2[1] = 0;
                    f5.t(this, childAt, view, i8, i9, i10, i11, i12, iArr2);
                    int[] iArr3 = this.f4367f;
                    i13 = i10 > 0 ? Math.max(i13, iArr3[0]) : Math.min(i13, iArr3[0]);
                    if (i11 > 0) {
                        z4 = true;
                        min = Math.max(i14, this.f4367f[1]);
                    } else {
                        z4 = true;
                        min = Math.min(i14, this.f4367f[1]);
                    }
                    i14 = min;
                    z8 = z4;
                }
            }
        }
        iArr[0] = iArr[0] + i13;
        iArr[1] = iArr[1] + i14;
        if (z8) {
            L(1);
        }
    }

    @Override // androidx.core.view.r
    public void k(View view, int i8, int i9, int i10, int i11, int i12) {
        j(view, i8, i9, i10, i11, 0, this.f4368g);
    }

    @Override // androidx.core.view.r
    public boolean l(View view, View view2, int i8, int i9) {
        int childCount = getChildCount();
        boolean z4 = false;
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (childAt.getVisibility() != 8) {
                e eVar = (e) childAt.getLayoutParams();
                Behavior f5 = eVar.f();
                if (f5 != null) {
                    boolean A2 = f5.A(this, childAt, view, view2, i8, i9);
                    z4 |= A2;
                    eVar.r(i9, A2);
                } else {
                    eVar.r(i9, false);
                }
            }
        }
        return z4;
    }

    @Override // androidx.core.view.r
    public void m(View view, View view2, int i8, int i9) {
        Behavior f5;
        this.f4380z.c(view, view2, i8, i9);
        this.f4373m = view2;
        int childCount = getChildCount();
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            e eVar = (e) childAt.getLayoutParams();
            if (eVar.j(i9) && (f5 = eVar.f()) != null) {
                f5.v(this, childAt, view, view2, i8, i9);
            }
        }
    }

    @Override // androidx.core.view.r
    public void n(View view, int i8) {
        this.f4380z.e(view, i8);
        int childCount = getChildCount();
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = getChildAt(i9);
            e eVar = (e) childAt.getLayoutParams();
            if (eVar.j(i8)) {
                Behavior f5 = eVar.f();
                if (f5 != null) {
                    f5.C(this, childAt, view, i8);
                }
                eVar.l(i8);
                eVar.k();
            }
        }
        this.f4373m = null;
    }

    @Override // androidx.core.view.r
    public void o(View view, int i8, int i9, int[] iArr, int i10) {
        Behavior f5;
        int childCount = getChildCount();
        boolean z4 = false;
        int i11 = 0;
        int i12 = 0;
        for (int i13 = 0; i13 < childCount; i13++) {
            View childAt = getChildAt(i13);
            if (childAt.getVisibility() != 8) {
                e eVar = (e) childAt.getLayoutParams();
                if (eVar.j(i10) && (f5 = eVar.f()) != null) {
                    int[] iArr2 = this.f4367f;
                    iArr2[0] = 0;
                    iArr2[1] = 0;
                    f5.q(this, childAt, view, i8, i9, iArr2, i10);
                    int[] iArr3 = this.f4367f;
                    i11 = i8 > 0 ? Math.max(i11, iArr3[0]) : Math.min(i11, iArr3[0]);
                    int[] iArr4 = this.f4367f;
                    i12 = i9 > 0 ? Math.max(i12, iArr4[1]) : Math.min(i12, iArr4[1]);
                    z4 = true;
                }
            }
        }
        iArr[0] = i11;
        iArr[1] = i12;
        if (z4) {
            L(1);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        U(false);
        if (this.f4375p) {
            if (this.f4374n == null) {
                this.f4374n = new f();
            }
            getViewTreeObserver().addOnPreDrawListener(this.f4374n);
        }
        if (this.q == null && c0.B(this)) {
            c0.q0(this);
        }
        this.f4370j = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        U(false);
        if (this.f4375p && this.f4374n != null) {
            getViewTreeObserver().removeOnPreDrawListener(this.f4374n);
        }
        View view = this.f4373m;
        if (view != null) {
            onStopNestedScroll(view);
        }
        this.f4370j = false;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.f4376t || this.f4377w == null) {
            return;
        }
        m0 m0Var = this.q;
        int m8 = m0Var != null ? m0Var.m() : 0;
        if (m8 > 0) {
            this.f4377w.setBounds(0, 0, getWidth(), m8);
            this.f4377w.draw(canvas);
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            U(true);
        }
        boolean P = P(motionEvent, 0);
        if (actionMasked == 1 || actionMasked == 3) {
            U(true);
        }
        return P;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        Behavior f5;
        int E2 = c0.E(this);
        int size = this.f4362a.size();
        for (int i12 = 0; i12 < size; i12++) {
            View view = this.f4362a.get(i12);
            if (view.getVisibility() != 8 && ((f5 = ((e) view.getLayoutParams()).f()) == null || !f5.l(this, view, E2))) {
                M(view, E2);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x011a, code lost:
        if (r0.m(r30, r20, r11, r21, r23, 0) == false) goto L33;
     */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x011d  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onMeasure(int r31, int r32) {
        /*
            Method dump skipped, instructions count: 391
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.coordinatorlayout.widget.CoordinatorLayout.onMeasure(int, int):void");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onNestedFling(View view, float f5, float f8, boolean z4) {
        Behavior f9;
        int childCount = getChildCount();
        boolean z8 = false;
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            if (childAt.getVisibility() != 8) {
                e eVar = (e) childAt.getLayoutParams();
                if (eVar.j(0) && (f9 = eVar.f()) != null) {
                    z8 |= f9.n(this, childAt, view, f5, f8, z4);
                }
            }
        }
        if (z8) {
            L(1);
        }
        return z8;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onNestedPreFling(View view, float f5, float f8) {
        Behavior f9;
        int childCount = getChildCount();
        boolean z4 = false;
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            if (childAt.getVisibility() != 8) {
                e eVar = (e) childAt.getLayoutParams();
                if (eVar.j(0) && (f9 = eVar.f()) != null) {
                    z4 |= f9.o(this, childAt, view, f5, f8);
                }
            }
        }
        return z4;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedPreScroll(View view, int i8, int i9, int[] iArr) {
        o(view, i8, i9, iArr, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedScroll(View view, int i8, int i9, int i10, int i11) {
        k(view, i8, i9, i10, i11, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onNestedScrollAccepted(View view, View view2, int i8) {
        m(view, view2, i8, 0);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        Parcelable parcelable2;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        SparseArray<Parcelable> sparseArray = savedState.f4381c;
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            int id = childAt.getId();
            Behavior f5 = C(childAt).f();
            if (id != -1 && f5 != null && (parcelable2 = sparseArray.get(id)) != null) {
                f5.x(this, childAt, parcelable2);
            }
        }
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Parcelable y8;
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            int id = childAt.getId();
            Behavior f5 = ((e) childAt.getLayoutParams()).f();
            if (id != -1 && f5 != null && (y8 = f5.y(this, childAt)) != null) {
                sparseArray.append(id, y8);
            }
        }
        savedState.f4381c = sparseArray;
        return savedState;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onStartNestedScroll(View view, View view2, int i8) {
        return l(view, view2, i8, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public void onStopNestedScroll(View view) {
        n(view, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0012, code lost:
        if (r3 != false) goto L19;
     */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0037  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x004c  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r18) {
        /*
            r17 = this;
            r0 = r17
            r1 = r18
            int r2 = r18.getActionMasked()
            android.view.View r3 = r0.f4372l
            r4 = 1
            r5 = 0
            if (r3 != 0) goto L15
            boolean r3 = r0.P(r1, r4)
            if (r3 == 0) goto L2b
            goto L16
        L15:
            r3 = r5
        L16:
            android.view.View r6 = r0.f4372l
            android.view.ViewGroup$LayoutParams r6 = r6.getLayoutParams()
            androidx.coordinatorlayout.widget.CoordinatorLayout$e r6 = (androidx.coordinatorlayout.widget.CoordinatorLayout.e) r6
            androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior r6 = r6.f()
            if (r6 == 0) goto L2b
            android.view.View r7 = r0.f4372l
            boolean r6 = r6.D(r0, r7, r1)
            goto L2c
        L2b:
            r6 = r5
        L2c:
            android.view.View r7 = r0.f4372l
            r8 = 0
            if (r7 != 0) goto L37
            boolean r1 = super.onTouchEvent(r18)
            r6 = r6 | r1
            goto L4a
        L37:
            if (r3 == 0) goto L4a
            long r11 = android.os.SystemClock.uptimeMillis()
            r13 = 3
            r14 = 0
            r15 = 0
            r16 = 0
            r9 = r11
            android.view.MotionEvent r8 = android.view.MotionEvent.obtain(r9, r11, r13, r14, r15, r16)
            super.onTouchEvent(r8)
        L4a:
            if (r8 == 0) goto L4f
            r8.recycle()
        L4f:
            if (r2 == r4) goto L54
            r1 = 3
            if (r2 != r1) goto L57
        L54:
            r0.U(r5)
        L57:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.coordinatorlayout.widget.CoordinatorLayout.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void p(View view) {
        List g8 = this.f4363b.g(view);
        if (g8 == null || g8.isEmpty()) {
            return;
        }
        for (int i8 = 0; i8 < g8.size(); i8++) {
            View view2 = (View) g8.get(i8);
            Behavior f5 = ((e) view2.getLayoutParams()).f();
            if (f5 != null) {
                f5.h(this, view2, view);
            }
        }
    }

    void q() {
        int childCount = getChildCount();
        boolean z4 = false;
        int i8 = 0;
        while (true) {
            if (i8 >= childCount) {
                break;
            } else if (E(getChildAt(i8))) {
                z4 = true;
                break;
            } else {
                i8++;
            }
        }
        if (z4 != this.f4375p) {
            if (z4) {
                f();
            } else {
                T();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: r */
    public e generateDefaultLayoutParams() {
        return new e(-2, -2);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z4) {
        Behavior f5 = ((e) view.getLayoutParams()).f();
        if (f5 == null || !f5.w(this, view, rect, z4)) {
            return super.requestChildRectangleOnScreen(view, rect, z4);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z4) {
        super.requestDisallowInterceptTouchEvent(z4);
        if (!z4 || this.f4369h) {
            return;
        }
        U(false);
        this.f4369h = true;
    }

    @Override // android.view.ViewGroup
    /* renamed from: s */
    public e generateLayoutParams(AttributeSet attributeSet) {
        return new e(getContext(), attributeSet);
    }

    @Override // android.view.View
    public void setFitsSystemWindows(boolean z4) {
        super.setFitsSystemWindows(z4);
        b0();
    }

    @Override // android.view.ViewGroup
    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
        this.f4378x = onHierarchyChangeListener;
    }

    public void setStatusBarBackground(Drawable drawable) {
        Drawable drawable2 = this.f4377w;
        if (drawable2 != drawable) {
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            Drawable mutate = drawable != null ? drawable.mutate() : null;
            this.f4377w = mutate;
            if (mutate != null) {
                if (mutate.isStateful()) {
                    this.f4377w.setState(getDrawableState());
                }
                androidx.core.graphics.drawable.a.m(this.f4377w, c0.E(this));
                this.f4377w.setVisible(getVisibility() == 0, false);
                this.f4377w.setCallback(this);
            }
            c0.j0(this);
        }
    }

    public void setStatusBarBackgroundColor(int i8) {
        setStatusBarBackground(new ColorDrawable(i8));
    }

    public void setStatusBarBackgroundResource(int i8) {
        setStatusBarBackground(i8 != 0 ? androidx.core.content.a.f(getContext(), i8) : null);
    }

    @Override // android.view.View
    public void setVisibility(int i8) {
        super.setVisibility(i8);
        boolean z4 = i8 == 0;
        Drawable drawable = this.f4377w;
        if (drawable == null || drawable.isVisible() == z4) {
            return;
        }
        this.f4377w.setVisible(z4, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    /* renamed from: t */
    public e generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof e ? new e((e) layoutParams) : layoutParams instanceof ViewGroup.MarginLayoutParams ? new e((ViewGroup.MarginLayoutParams) layoutParams) : new e(layoutParams);
    }

    void u(View view, boolean z4, Rect rect) {
        if (view.isLayoutRequested() || view.getVisibility() == 8) {
            rect.setEmpty();
        } else if (z4) {
            x(view, rect);
        } else {
            rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        }
    }

    public List<View> v(View view) {
        List<View> h8 = this.f4363b.h(view);
        this.f4365d.clear();
        if (h8 != null) {
            this.f4365d.addAll(h8);
        }
        return this.f4365d;
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.f4377w;
    }

    public List<View> w(View view) {
        List g8 = this.f4363b.g(view);
        this.f4365d.clear();
        if (g8 != null) {
            this.f4365d.addAll(g8);
        }
        return this.f4365d;
    }

    void x(View view, Rect rect) {
        androidx.coordinatorlayout.widget.b.a(this, view, rect);
    }

    void y(View view, int i8, Rect rect, Rect rect2) {
        e eVar = (e) view.getLayoutParams();
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        z(view, i8, rect, rect2, eVar, measuredWidth, measuredHeight);
        h(eVar, rect2, measuredWidth, measuredHeight);
    }
}
