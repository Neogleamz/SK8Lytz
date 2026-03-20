package androidx.drawerlayout.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.accessibility.c;
import androidx.core.view.accessibility.f;
import androidx.core.view.c0;
import androidx.core.view.m0;
import androidx.customview.view.AbsSavedState;
import com.example.seedpoint.R;
import java.util.ArrayList;
import java.util.List;
import w0.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class DrawerLayout extends ViewGroup {

    /* renamed from: c0  reason: collision with root package name */
    private static final int[] f5168c0 = {16843828};

    /* renamed from: d0  reason: collision with root package name */
    static final int[] f5169d0 = {16842931};

    /* renamed from: e0  reason: collision with root package name */
    static final boolean f5170e0;

    /* renamed from: f0  reason: collision with root package name */
    private static final boolean f5171f0;

    /* renamed from: g0  reason: collision with root package name */
    private static boolean f5172g0;
    private float A;
    private float B;
    private Drawable C;
    private Drawable E;
    private Drawable F;
    private CharSequence G;
    private CharSequence H;
    private Object K;
    private boolean L;
    private Drawable O;
    private Drawable P;
    private Drawable Q;
    private Drawable R;
    private final ArrayList<View> T;
    private Rect W;

    /* renamed from: a  reason: collision with root package name */
    private final d f5173a;

    /* renamed from: a0  reason: collision with root package name */
    private Matrix f5174a0;

    /* renamed from: b  reason: collision with root package name */
    private float f5175b;

    /* renamed from: b0  reason: collision with root package name */
    private final androidx.core.view.accessibility.f f5176b0;

    /* renamed from: c  reason: collision with root package name */
    private int f5177c;

    /* renamed from: d  reason: collision with root package name */
    private int f5178d;

    /* renamed from: e  reason: collision with root package name */
    private float f5179e;

    /* renamed from: f  reason: collision with root package name */
    private Paint f5180f;

    /* renamed from: g  reason: collision with root package name */
    private final w0.c f5181g;

    /* renamed from: h  reason: collision with root package name */
    private final w0.c f5182h;

    /* renamed from: j  reason: collision with root package name */
    private final f f5183j;

    /* renamed from: k  reason: collision with root package name */
    private final f f5184k;

    /* renamed from: l  reason: collision with root package name */
    private int f5185l;

    /* renamed from: m  reason: collision with root package name */
    private boolean f5186m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f5187n;

    /* renamed from: p  reason: collision with root package name */
    private int f5188p;
    private int q;

    /* renamed from: t  reason: collision with root package name */
    private int f5189t;

    /* renamed from: w  reason: collision with root package name */
    private int f5190w;

    /* renamed from: x  reason: collision with root package name */
    private boolean f5191x;

    /* renamed from: y  reason: collision with root package name */
    private e f5192y;

    /* renamed from: z  reason: collision with root package name */
    private List<e> f5193z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        /* renamed from: a  reason: collision with root package name */
        public int f5194a;

        /* renamed from: b  reason: collision with root package name */
        float f5195b;

        /* renamed from: c  reason: collision with root package name */
        boolean f5196c;

        /* renamed from: d  reason: collision with root package name */
        int f5197d;

        public LayoutParams(int i8, int i9) {
            super(i8, i9);
            this.f5194a = 0;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.f5194a = 0;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, DrawerLayout.f5169d0);
            this.f5194a = obtainStyledAttributes.getInt(0, 0);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.f5194a = 0;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.f5194a = 0;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams) layoutParams);
            this.f5194a = 0;
            this.f5194a = layoutParams.f5194a;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        int f5198c;

        /* renamed from: d  reason: collision with root package name */
        int f5199d;

        /* renamed from: e  reason: collision with root package name */
        int f5200e;

        /* renamed from: f  reason: collision with root package name */
        int f5201f;

        /* renamed from: g  reason: collision with root package name */
        int f5202g;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.ClassLoaderCreator<SavedState> {
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
            this.f5198c = 0;
            this.f5198c = parcel.readInt();
            this.f5199d = parcel.readInt();
            this.f5200e = parcel.readInt();
            this.f5201f = parcel.readInt();
            this.f5202g = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
            this.f5198c = 0;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeInt(this.f5198c);
            parcel.writeInt(this.f5199d);
            parcel.writeInt(this.f5200e);
            parcel.writeInt(this.f5201f);
            parcel.writeInt(this.f5202g);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements androidx.core.view.accessibility.f {
        a() {
        }

        @Override // androidx.core.view.accessibility.f
        public boolean a(View view, f.a aVar) {
            if (!DrawerLayout.this.D(view) || DrawerLayout.this.r(view) == 2) {
                return false;
            }
            DrawerLayout.this.f(view);
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements View.OnApplyWindowInsetsListener {
        b() {
        }

        @Override // android.view.View.OnApplyWindowInsetsListener
        public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            ((DrawerLayout) view).S(windowInsets, windowInsets.getSystemWindowInsetTop() > 0);
            return windowInsets.consumeSystemWindowInsets();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends androidx.core.view.a {

        /* renamed from: d  reason: collision with root package name */
        private final Rect f5205d = new Rect();

        c() {
        }

        private void n(androidx.core.view.accessibility.c cVar, ViewGroup viewGroup) {
            int childCount = viewGroup.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = viewGroup.getChildAt(i8);
                if (DrawerLayout.A(childAt)) {
                    cVar.c(childAt);
                }
            }
        }

        private void o(androidx.core.view.accessibility.c cVar, androidx.core.view.accessibility.c cVar2) {
            Rect rect = this.f5205d;
            cVar2.n(rect);
            cVar.Y(rect);
            cVar.G0(cVar2.N());
            cVar.r0(cVar2.v());
            cVar.c0(cVar2.p());
            cVar.g0(cVar2.r());
            cVar.i0(cVar2.F());
            cVar.l0(cVar2.H());
            cVar.V(cVar2.B());
            cVar.z0(cVar2.L());
            cVar.a(cVar2.k());
        }

        @Override // androidx.core.view.a
        public boolean a(View view, AccessibilityEvent accessibilityEvent) {
            if (accessibilityEvent.getEventType() == 32) {
                List<CharSequence> text = accessibilityEvent.getText();
                View p8 = DrawerLayout.this.p();
                if (p8 != null) {
                    CharSequence s8 = DrawerLayout.this.s(DrawerLayout.this.t(p8));
                    if (s8 != null) {
                        text.add(s8);
                        return true;
                    }
                    return true;
                }
                return true;
            }
            return super.a(view, accessibilityEvent);
        }

        @Override // androidx.core.view.a
        public void f(View view, AccessibilityEvent accessibilityEvent) {
            super.f(view, accessibilityEvent);
            accessibilityEvent.setClassName("androidx.drawerlayout.widget.DrawerLayout");
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            if (DrawerLayout.f5170e0) {
                super.g(view, cVar);
            } else {
                androidx.core.view.accessibility.c Q = androidx.core.view.accessibility.c.Q(cVar);
                super.g(view, Q);
                cVar.B0(view);
                ViewParent K = c0.K(view);
                if (K instanceof View) {
                    cVar.t0((View) K);
                }
                o(cVar, Q);
                Q.S();
                n(cVar, (ViewGroup) view);
            }
            cVar.c0("androidx.drawerlayout.widget.DrawerLayout");
            cVar.k0(false);
            cVar.l0(false);
            cVar.T(c.a.f4907e);
            cVar.T(c.a.f4908f);
        }

        @Override // androidx.core.view.a
        public boolean i(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (DrawerLayout.f5170e0 || DrawerLayout.A(view)) {
                return super.i(viewGroup, view, accessibilityEvent);
            }
            return false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class d extends androidx.core.view.a {
        d() {
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            super.g(view, cVar);
            if (DrawerLayout.A(view)) {
                return;
            }
            cVar.t0(null);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface e {
        void a(View view);

        void b(View view);

        void c(int i8);

        void d(View view, float f5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f extends c.AbstractC0221c {

        /* renamed from: a  reason: collision with root package name */
        private final int f5207a;

        /* renamed from: b  reason: collision with root package name */
        private w0.c f5208b;

        /* renamed from: c  reason: collision with root package name */
        private final Runnable f5209c = new a();

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {
            a() {
            }

            @Override // java.lang.Runnable
            public void run() {
                f.this.o();
            }
        }

        f(int i8) {
            this.f5207a = i8;
        }

        private void n() {
            View n8 = DrawerLayout.this.n(this.f5207a == 3 ? 5 : 3);
            if (n8 != null) {
                DrawerLayout.this.f(n8);
            }
        }

        @Override // w0.c.AbstractC0221c
        public int a(View view, int i8, int i9) {
            int width;
            int width2;
            if (DrawerLayout.this.c(view, 3)) {
                width2 = -view.getWidth();
                width = 0;
            } else {
                width = DrawerLayout.this.getWidth();
                width2 = width - view.getWidth();
            }
            return Math.max(width2, Math.min(i8, width));
        }

        @Override // w0.c.AbstractC0221c
        public int b(View view, int i8, int i9) {
            return view.getTop();
        }

        @Override // w0.c.AbstractC0221c
        public int d(View view) {
            if (DrawerLayout.this.E(view)) {
                return view.getWidth();
            }
            return 0;
        }

        @Override // w0.c.AbstractC0221c
        public void f(int i8, int i9) {
            DrawerLayout drawerLayout;
            int i10;
            if ((i8 & 1) == 1) {
                drawerLayout = DrawerLayout.this;
                i10 = 3;
            } else {
                drawerLayout = DrawerLayout.this;
                i10 = 5;
            }
            View n8 = drawerLayout.n(i10);
            if (n8 == null || DrawerLayout.this.r(n8) != 0) {
                return;
            }
            this.f5208b.c(n8, i9);
        }

        @Override // w0.c.AbstractC0221c
        public boolean g(int i8) {
            return false;
        }

        @Override // w0.c.AbstractC0221c
        public void h(int i8, int i9) {
            DrawerLayout.this.postDelayed(this.f5209c, 160L);
        }

        @Override // w0.c.AbstractC0221c
        public void i(View view, int i8) {
            ((LayoutParams) view.getLayoutParams()).f5196c = false;
            n();
        }

        @Override // w0.c.AbstractC0221c
        public void j(int i8) {
            DrawerLayout.this.X(i8, this.f5208b.w());
        }

        @Override // w0.c.AbstractC0221c
        public void k(View view, int i8, int i9, int i10, int i11) {
            int width = view.getWidth();
            float width2 = (DrawerLayout.this.c(view, 3) ? i8 + width : DrawerLayout.this.getWidth() - i8) / width;
            DrawerLayout.this.U(view, width2);
            view.setVisibility(width2 == 0.0f ? 4 : 0);
            DrawerLayout.this.invalidate();
        }

        @Override // w0.c.AbstractC0221c
        public void l(View view, float f5, float f8) {
            int i8;
            float u8 = DrawerLayout.this.u(view);
            int width = view.getWidth();
            if (DrawerLayout.this.c(view, 3)) {
                int i9 = (f5 > 0.0f ? 1 : (f5 == 0.0f ? 0 : -1));
                i8 = (i9 > 0 || (i9 == 0 && u8 > 0.5f)) ? 0 : -width;
            } else {
                int width2 = DrawerLayout.this.getWidth();
                if (f5 < 0.0f || (f5 == 0.0f && u8 > 0.5f)) {
                    width2 -= width;
                }
                i8 = width2;
            }
            this.f5208b.P(i8, view.getTop());
            DrawerLayout.this.invalidate();
        }

        @Override // w0.c.AbstractC0221c
        public boolean m(View view, int i8) {
            return DrawerLayout.this.E(view) && DrawerLayout.this.c(view, this.f5207a) && DrawerLayout.this.r(view) == 0;
        }

        void o() {
            View n8;
            int width;
            int y8 = this.f5208b.y();
            boolean z4 = this.f5207a == 3;
            if (z4) {
                n8 = DrawerLayout.this.n(3);
                width = (n8 != null ? -n8.getWidth() : 0) + y8;
            } else {
                n8 = DrawerLayout.this.n(5);
                width = DrawerLayout.this.getWidth() - y8;
            }
            if (n8 != null) {
                if (((!z4 || n8.getLeft() >= width) && (z4 || n8.getLeft() <= width)) || DrawerLayout.this.r(n8) != 0) {
                    return;
                }
                this.f5208b.R(n8, width, n8.getTop());
                ((LayoutParams) n8.getLayoutParams()).f5196c = true;
                DrawerLayout.this.invalidate();
                n();
                DrawerLayout.this.b();
            }
        }

        public void p() {
            DrawerLayout.this.removeCallbacks(this.f5209c);
        }

        public void q(w0.c cVar) {
            this.f5208b = cVar;
        }
    }

    static {
        int i8 = Build.VERSION.SDK_INT;
        f5170e0 = i8 >= 19;
        f5171f0 = i8 >= 21;
        f5172g0 = i8 >= 29;
    }

    public DrawerLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, x0.a.f23750a);
    }

    public DrawerLayout(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f5173a = new d();
        this.f5178d = -1728053248;
        this.f5180f = new Paint();
        this.f5187n = true;
        this.f5188p = 3;
        this.q = 3;
        this.f5189t = 3;
        this.f5190w = 3;
        this.O = null;
        this.P = null;
        this.Q = null;
        this.R = null;
        this.f5176b0 = new a();
        setDescendantFocusability(262144);
        float f5 = getResources().getDisplayMetrics().density;
        this.f5177c = (int) ((64.0f * f5) + 0.5f);
        float f8 = f5 * 400.0f;
        f fVar = new f(3);
        this.f5183j = fVar;
        f fVar2 = new f(5);
        this.f5184k = fVar2;
        w0.c o5 = w0.c.o(this, 1.0f, fVar);
        this.f5181g = o5;
        o5.N(1);
        o5.O(f8);
        fVar.q(o5);
        w0.c o8 = w0.c.o(this, 1.0f, fVar2);
        this.f5182h = o8;
        o8.N(2);
        o8.O(f8);
        fVar2.q(o8);
        setFocusableInTouchMode(true);
        c0.E0(this, 1);
        c0.t0(this, new c());
        setMotionEventSplittingEnabled(false);
        if (c0.B(this)) {
            if (Build.VERSION.SDK_INT >= 21) {
                setOnApplyWindowInsetsListener(new b());
                setSystemUiVisibility(1280);
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(f5168c0);
                try {
                    this.C = obtainStyledAttributes.getDrawable(0);
                } finally {
                    obtainStyledAttributes.recycle();
                }
            } else {
                this.C = null;
            }
        }
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, x0.c.f23753b, i8, 0);
        try {
            int i9 = x0.c.f23754c;
            this.f5175b = obtainStyledAttributes2.hasValue(i9) ? obtainStyledAttributes2.getDimension(i9, 0.0f) : getResources().getDimension(x0.b.f23751a);
            obtainStyledAttributes2.recycle();
            this.T = new ArrayList<>();
        } catch (Throwable th) {
            obtainStyledAttributes2.recycle();
            throw th;
        }
    }

    static boolean A(View view) {
        return (c0.C(view) == 4 || c0.C(view) == 2) ? false : true;
    }

    private boolean H(float f5, float f8, View view) {
        if (this.W == null) {
            this.W = new Rect();
        }
        view.getHitRect(this.W);
        return this.W.contains((int) f5, (int) f8);
    }

    private void I(Drawable drawable, int i8) {
        if (drawable == null || !androidx.core.graphics.drawable.a.h(drawable)) {
            return;
        }
        androidx.core.graphics.drawable.a.m(drawable, i8);
    }

    private Drawable P() {
        int E = c0.E(this);
        if (E == 0) {
            Drawable drawable = this.O;
            if (drawable != null) {
                I(drawable, E);
                return this.O;
            }
        } else {
            Drawable drawable2 = this.P;
            if (drawable2 != null) {
                I(drawable2, E);
                return this.P;
            }
        }
        return this.Q;
    }

    private Drawable Q() {
        int E = c0.E(this);
        if (E == 0) {
            Drawable drawable = this.P;
            if (drawable != null) {
                I(drawable, E);
                return this.P;
            }
        } else {
            Drawable drawable2 = this.O;
            if (drawable2 != null) {
                I(drawable2, E);
                return this.O;
            }
        }
        return this.R;
    }

    private void R() {
        if (f5171f0) {
            return;
        }
        this.E = P();
        this.F = Q();
    }

    private void V(View view) {
        c.a aVar = c.a.f4926y;
        c0.n0(view, aVar.b());
        if (!D(view) || r(view) == 2) {
            return;
        }
        c0.p0(view, aVar, null, this.f5176b0);
    }

    private void W(View view, boolean z4) {
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            c0.E0(childAt, ((z4 || E(childAt)) && !(z4 && childAt == view)) ? 4 : 1);
        }
    }

    private boolean m(MotionEvent motionEvent, View view) {
        if (!view.getMatrix().isIdentity()) {
            MotionEvent v8 = v(motionEvent, view);
            boolean dispatchGenericMotionEvent = view.dispatchGenericMotionEvent(v8);
            v8.recycle();
            return dispatchGenericMotionEvent;
        }
        float scrollX = getScrollX() - view.getLeft();
        float scrollY = getScrollY() - view.getTop();
        motionEvent.offsetLocation(scrollX, scrollY);
        boolean dispatchGenericMotionEvent2 = view.dispatchGenericMotionEvent(motionEvent);
        motionEvent.offsetLocation(-scrollX, -scrollY);
        return dispatchGenericMotionEvent2;
    }

    private MotionEvent v(MotionEvent motionEvent, View view) {
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.offsetLocation(getScrollX() - view.getLeft(), getScrollY() - view.getTop());
        Matrix matrix = view.getMatrix();
        if (!matrix.isIdentity()) {
            if (this.f5174a0 == null) {
                this.f5174a0 = new Matrix();
            }
            matrix.invert(this.f5174a0);
            obtain.transform(this.f5174a0);
        }
        return obtain;
    }

    static String w(int i8) {
        return (i8 & 3) == 3 ? "LEFT" : (i8 & 5) == 5 ? "RIGHT" : Integer.toHexString(i8);
    }

    private static boolean x(View view) {
        Drawable background = view.getBackground();
        return background != null && background.getOpacity() == -1;
    }

    private boolean y() {
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            if (((LayoutParams) getChildAt(i8).getLayoutParams()).f5196c) {
                return true;
            }
        }
        return false;
    }

    private boolean z() {
        return p() != null;
    }

    boolean B(View view) {
        return ((LayoutParams) view.getLayoutParams()).f5194a == 0;
    }

    public boolean C(int i8) {
        View n8 = n(i8);
        if (n8 != null) {
            return D(n8);
        }
        return false;
    }

    public boolean D(View view) {
        if (E(view)) {
            return (((LayoutParams) view.getLayoutParams()).f5197d & 1) == 1;
        }
        throw new IllegalArgumentException("View " + view + " is not a drawer");
    }

    boolean E(View view) {
        int b9 = androidx.core.view.f.b(((LayoutParams) view.getLayoutParams()).f5194a, c0.E(view));
        return ((b9 & 3) == 0 && (b9 & 5) == 0) ? false : true;
    }

    public boolean F(int i8) {
        View n8 = n(i8);
        if (n8 != null) {
            return G(n8);
        }
        return false;
    }

    public boolean G(View view) {
        if (E(view)) {
            return ((LayoutParams) view.getLayoutParams()).f5195b > 0.0f;
        }
        throw new IllegalArgumentException("View " + view + " is not a drawer");
    }

    void J(View view, float f5) {
        float u8 = u(view);
        float width = view.getWidth();
        int i8 = ((int) (width * f5)) - ((int) (u8 * width));
        if (!c(view, 3)) {
            i8 = -i8;
        }
        view.offsetLeftAndRight(i8);
        U(view, f5);
    }

    public void K(int i8) {
        L(i8, true);
    }

    public void L(int i8, boolean z4) {
        View n8 = n(i8);
        if (n8 != null) {
            N(n8, z4);
            return;
        }
        throw new IllegalArgumentException("No drawer view found with gravity " + w(i8));
    }

    public void M(View view) {
        N(view, true);
    }

    public void N(View view, boolean z4) {
        if (!E(view)) {
            throw new IllegalArgumentException("View " + view + " is not a sliding drawer");
        }
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (this.f5187n) {
            layoutParams.f5195b = 1.0f;
            layoutParams.f5197d = 1;
            W(view, true);
            V(view);
        } else if (z4) {
            layoutParams.f5197d |= 2;
            if (c(view, 3)) {
                this.f5181g.R(view, 0, view.getTop());
            } else {
                this.f5182h.R(view, getWidth() - view.getWidth(), view.getTop());
            }
        } else {
            J(view, 1.0f);
            X(0, view);
            view.setVisibility(0);
        }
        invalidate();
    }

    public void O(e eVar) {
        List<e> list;
        if (eVar == null || (list = this.f5193z) == null) {
            return;
        }
        list.remove(eVar);
    }

    public void S(Object obj, boolean z4) {
        this.K = obj;
        this.L = z4;
        setWillNotDraw(!z4 && getBackground() == null);
        requestLayout();
    }

    public void T(int i8, int i9) {
        View n8;
        int b9 = androidx.core.view.f.b(i9, c0.E(this));
        if (i9 == 3) {
            this.f5188p = i8;
        } else if (i9 == 5) {
            this.q = i8;
        } else if (i9 == 8388611) {
            this.f5189t = i8;
        } else if (i9 == 8388613) {
            this.f5190w = i8;
        }
        if (i8 != 0) {
            (b9 == 3 ? this.f5181g : this.f5182h).b();
        }
        if (i8 != 1) {
            if (i8 == 2 && (n8 = n(b9)) != null) {
                M(n8);
                return;
            }
            return;
        }
        View n9 = n(b9);
        if (n9 != null) {
            f(n9);
        }
    }

    void U(View view, float f5) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (f5 == layoutParams.f5195b) {
            return;
        }
        layoutParams.f5195b = f5;
        l(view, f5);
    }

    void X(int i8, View view) {
        int B = this.f5181g.B();
        int B2 = this.f5182h.B();
        int i9 = 2;
        if (B == 1 || B2 == 1) {
            i9 = 1;
        } else if (B != 2 && B2 != 2) {
            i9 = 0;
        }
        if (view != null && i8 == 0) {
            float f5 = ((LayoutParams) view.getLayoutParams()).f5195b;
            if (f5 == 0.0f) {
                j(view);
            } else if (f5 == 1.0f) {
                k(view);
            }
        }
        if (i9 != this.f5185l) {
            this.f5185l = i9;
            List<e> list = this.f5193z;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.f5193z.get(size).c(i9);
                }
            }
        }
    }

    public void a(e eVar) {
        if (eVar == null) {
            return;
        }
        if (this.f5193z == null) {
            this.f5193z = new ArrayList();
        }
        this.f5193z.add(eVar);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> arrayList, int i8, int i9) {
        if (getDescendantFocusability() == 393216) {
            return;
        }
        int childCount = getChildCount();
        boolean z4 = false;
        for (int i10 = 0; i10 < childCount; i10++) {
            View childAt = getChildAt(i10);
            if (!E(childAt)) {
                this.T.add(childAt);
            } else if (D(childAt)) {
                childAt.addFocusables(arrayList, i8, i9);
                z4 = true;
            }
        }
        if (!z4) {
            int size = this.T.size();
            for (int i11 = 0; i11 < size; i11++) {
                View view = this.T.get(i11);
                if (view.getVisibility() == 0) {
                    view.addFocusables(arrayList, i8, i9);
                }
            }
        }
        this.T.clear();
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i8, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i8, layoutParams);
        c0.E0(view, (o() != null || E(view)) ? 4 : 1);
        if (f5170e0) {
            return;
        }
        c0.t0(view, this.f5173a);
    }

    void b() {
        if (this.f5191x) {
            return;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            getChildAt(i8).dispatchTouchEvent(obtain);
        }
        obtain.recycle();
        this.f5191x = true;
    }

    boolean c(View view, int i8) {
        return (t(view) & i8) == i8;
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && super.checkLayoutParams(layoutParams);
    }

    @Override // android.view.View
    public void computeScroll() {
        int childCount = getChildCount();
        float f5 = 0.0f;
        for (int i8 = 0; i8 < childCount; i8++) {
            f5 = Math.max(f5, ((LayoutParams) getChildAt(i8).getLayoutParams()).f5195b);
        }
        this.f5179e = f5;
        boolean n8 = this.f5181g.n(true);
        boolean n9 = this.f5182h.n(true);
        if (n8 || n9) {
            c0.j0(this);
        }
    }

    public void d(int i8) {
        e(i8, true);
    }

    @Override // android.view.View
    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if ((motionEvent.getSource() & 2) == 0 || motionEvent.getAction() == 10 || this.f5179e <= 0.0f) {
            return super.dispatchGenericMotionEvent(motionEvent);
        }
        int childCount = getChildCount();
        if (childCount != 0) {
            float x8 = motionEvent.getX();
            float y8 = motionEvent.getY();
            for (int i8 = childCount - 1; i8 >= 0; i8--) {
                View childAt = getChildAt(i8);
                if (H(x8, y8, childAt) && !B(childAt) && m(motionEvent, childAt)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j8) {
        Drawable drawable;
        int i8;
        int height = getHeight();
        boolean B = B(view);
        int width = getWidth();
        int save = canvas.save();
        int i9 = 0;
        if (B) {
            int childCount = getChildCount();
            int i10 = 0;
            for (int i11 = 0; i11 < childCount; i11++) {
                View childAt = getChildAt(i11);
                if (childAt != view && childAt.getVisibility() == 0 && x(childAt) && E(childAt) && childAt.getHeight() >= height) {
                    if (c(childAt, 3)) {
                        int right = childAt.getRight();
                        if (right > i10) {
                            i10 = right;
                        }
                    } else {
                        int left = childAt.getLeft();
                        if (left < width) {
                            width = left;
                        }
                    }
                }
            }
            canvas.clipRect(i10, 0, width, getHeight());
            i9 = i10;
        }
        boolean drawChild = super.drawChild(canvas, view, j8);
        canvas.restoreToCount(save);
        float f5 = this.f5179e;
        if (f5 <= 0.0f || !B) {
            if (this.E != null && c(view, 3)) {
                int intrinsicWidth = this.E.getIntrinsicWidth();
                int right2 = view.getRight();
                float max = Math.max(0.0f, Math.min(right2 / this.f5181g.y(), 1.0f));
                this.E.setBounds(right2, view.getTop(), intrinsicWidth + right2, view.getBottom());
                this.E.setAlpha((int) (max * 255.0f));
                drawable = this.E;
            } else if (this.F != null && c(view, 5)) {
                int intrinsicWidth2 = this.F.getIntrinsicWidth();
                int left2 = view.getLeft();
                float max2 = Math.max(0.0f, Math.min((getWidth() - left2) / this.f5182h.y(), 1.0f));
                this.F.setBounds(left2 - intrinsicWidth2, view.getTop(), left2, view.getBottom());
                this.F.setAlpha((int) (max2 * 255.0f));
                drawable = this.F;
            }
            drawable.draw(canvas);
        } else {
            this.f5180f.setColor((this.f5178d & 16777215) | (((int) ((((-16777216) & i8) >>> 24) * f5)) << 24));
            canvas.drawRect(i9, 0.0f, width, getHeight(), this.f5180f);
        }
        return drawChild;
    }

    public void e(int i8, boolean z4) {
        View n8 = n(i8);
        if (n8 != null) {
            g(n8, z4);
            return;
        }
        throw new IllegalArgumentException("No drawer view found with gravity " + w(i8));
    }

    public void f(View view) {
        g(view, true);
    }

    public void g(View view, boolean z4) {
        w0.c cVar;
        int width;
        if (!E(view)) {
            throw new IllegalArgumentException("View " + view + " is not a sliding drawer");
        }
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if (this.f5187n) {
            layoutParams.f5195b = 0.0f;
            layoutParams.f5197d = 0;
        } else if (z4) {
            layoutParams.f5197d |= 4;
            if (c(view, 3)) {
                cVar = this.f5181g;
                width = -view.getWidth();
            } else {
                cVar = this.f5182h;
                width = getWidth();
            }
            cVar.R(view, width, view.getTop());
        } else {
            J(view, 0.0f);
            X(0, view);
            view.setVisibility(4);
        }
        invalidate();
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams ? new LayoutParams((LayoutParams) layoutParams) : layoutParams instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams) : new LayoutParams(layoutParams);
    }

    public float getDrawerElevation() {
        if (f5171f0) {
            return this.f5175b;
        }
        return 0.0f;
    }

    public Drawable getStatusBarBackgroundDrawable() {
        return this.C;
    }

    public void h() {
        i(false);
    }

    void i(boolean z4) {
        int childCount = getChildCount();
        boolean z8 = false;
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
            if (E(childAt) && (!z4 || layoutParams.f5196c)) {
                z8 |= c(childAt, 3) ? this.f5181g.R(childAt, -childAt.getWidth(), childAt.getTop()) : this.f5182h.R(childAt, getWidth(), childAt.getTop());
                layoutParams.f5196c = false;
            }
        }
        this.f5183j.p();
        this.f5184k.p();
        if (z8) {
            invalidate();
        }
    }

    void j(View view) {
        View rootView;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if ((layoutParams.f5197d & 1) == 1) {
            layoutParams.f5197d = 0;
            List<e> list = this.f5193z;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.f5193z.get(size).b(view);
                }
            }
            W(view, false);
            V(view);
            if (!hasWindowFocus() || (rootView = getRootView()) == null) {
                return;
            }
            rootView.sendAccessibilityEvent(32);
        }
    }

    void k(View view) {
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        if ((layoutParams.f5197d & 1) == 0) {
            layoutParams.f5197d = 1;
            List<e> list = this.f5193z;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.f5193z.get(size).a(view);
                }
            }
            W(view, true);
            V(view);
            if (hasWindowFocus()) {
                sendAccessibilityEvent(32);
            }
        }
    }

    void l(View view, float f5) {
        List<e> list = this.f5193z;
        if (list != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.f5193z.get(size).d(view, f5);
            }
        }
    }

    View n(int i8) {
        int b9 = androidx.core.view.f.b(i8, c0.E(this)) & 7;
        int childCount = getChildCount();
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = getChildAt(i9);
            if ((t(childAt) & 7) == b9) {
                return childAt;
            }
        }
        return null;
    }

    View o() {
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            if ((((LayoutParams) childAt.getLayoutParams()).f5197d & 1) == 1) {
                return childAt;
            }
        }
        return null;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f5187n = true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f5187n = true;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        Object obj;
        super.onDraw(canvas);
        if (!this.L || this.C == null) {
            return;
        }
        int systemWindowInsetTop = (Build.VERSION.SDK_INT < 21 || (obj = this.K) == null) ? 0 : ((WindowInsets) obj).getSystemWindowInsetTop();
        if (systemWindowInsetTop > 0) {
            this.C.setBounds(0, 0, getWidth(), systemWindowInsetTop);
            this.C.draw(canvas);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x001b, code lost:
        if (r0 != 3) goto L7;
     */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            int r0 = r7.getActionMasked()
            w0.c r1 = r6.f5181g
            boolean r1 = r1.Q(r7)
            w0.c r2 = r6.f5182h
            boolean r2 = r2.Q(r7)
            r1 = r1 | r2
            r2 = 1
            r3 = 0
            if (r0 == 0) goto L38
            if (r0 == r2) goto L31
            r7 = 2
            r4 = 3
            if (r0 == r7) goto L1e
            if (r0 == r4) goto L31
            goto L36
        L1e:
            w0.c r7 = r6.f5181g
            boolean r7 = r7.e(r4)
            if (r7 == 0) goto L36
            androidx.drawerlayout.widget.DrawerLayout$f r7 = r6.f5183j
            r7.p()
            androidx.drawerlayout.widget.DrawerLayout$f r7 = r6.f5184k
            r7.p()
            goto L36
        L31:
            r6.i(r2)
            r6.f5191x = r3
        L36:
            r7 = r3
            goto L60
        L38:
            float r0 = r7.getX()
            float r7 = r7.getY()
            r6.A = r0
            r6.B = r7
            float r4 = r6.f5179e
            r5 = 0
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 <= 0) goto L5d
            w0.c r4 = r6.f5181g
            int r0 = (int) r0
            int r7 = (int) r7
            android.view.View r7 = r4.u(r0, r7)
            if (r7 == 0) goto L5d
            boolean r7 = r6.B(r7)
            if (r7 == 0) goto L5d
            r7 = r2
            goto L5e
        L5d:
            r7 = r3
        L5e:
            r6.f5191x = r3
        L60:
            if (r1 != 0) goto L70
            if (r7 != 0) goto L70
            boolean r7 = r6.y()
            if (r7 != 0) goto L70
            boolean r7 = r6.f5191x
            if (r7 == 0) goto L6f
            goto L70
        L6f:
            r2 = r3
        L70:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.drawerlayout.widget.DrawerLayout.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i8, KeyEvent keyEvent) {
        if (i8 == 4 && z()) {
            keyEvent.startTracking();
            return true;
        }
        return super.onKeyDown(i8, keyEvent);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i8, KeyEvent keyEvent) {
        if (i8 == 4) {
            View p8 = p();
            if (p8 != null && r(p8) == 0) {
                h();
            }
            return p8 != null;
        }
        return super.onKeyUp(i8, keyEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        WindowInsets rootWindowInsets;
        int i12;
        float f5;
        int i13;
        int measuredHeight;
        int i14;
        int i15;
        boolean z8 = true;
        this.f5186m = true;
        int i16 = i10 - i8;
        int childCount = getChildCount();
        int i17 = 0;
        while (i17 < childCount) {
            View childAt = getChildAt(i17);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (B(childAt)) {
                    int i18 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                    childAt.layout(i18, ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, childAt.getMeasuredWidth() + i18, ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + childAt.getMeasuredHeight());
                } else {
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight2 = childAt.getMeasuredHeight();
                    if (c(childAt, 3)) {
                        float f8 = measuredWidth;
                        i13 = (-measuredWidth) + ((int) (layoutParams.f5195b * f8));
                        f5 = (measuredWidth + i13) / f8;
                    } else {
                        float f9 = measuredWidth;
                        f5 = (i16 - i12) / f9;
                        i13 = i16 - ((int) (layoutParams.f5195b * f9));
                    }
                    boolean z9 = f5 != layoutParams.f5195b ? z8 : false;
                    int i19 = layoutParams.f5194a & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
                    if (i19 != 16) {
                        if (i19 != 80) {
                            measuredHeight = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                            i14 = measuredWidth + i13;
                            i15 = measuredHeight2 + measuredHeight;
                        } else {
                            int i20 = i11 - i9;
                            measuredHeight = (i20 - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin) - childAt.getMeasuredHeight();
                            i14 = measuredWidth + i13;
                            i15 = i20 - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                        }
                        childAt.layout(i13, measuredHeight, i14, i15);
                    } else {
                        int i21 = i11 - i9;
                        int i22 = (i21 - measuredHeight2) / 2;
                        int i23 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                        if (i22 < i23) {
                            i22 = i23;
                        } else {
                            int i24 = i22 + measuredHeight2;
                            int i25 = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                            if (i24 > i21 - i25) {
                                i22 = (i21 - i25) - measuredHeight2;
                            }
                        }
                        childAt.layout(i13, i22, measuredWidth + i13, measuredHeight2 + i22);
                    }
                    if (z9) {
                        U(childAt, f5);
                    }
                    int i26 = layoutParams.f5195b > 0.0f ? 0 : 4;
                    if (childAt.getVisibility() != i26) {
                        childAt.setVisibility(i26);
                    }
                }
            }
            i17++;
            z8 = true;
        }
        if (f5172g0 && (rootWindowInsets = getRootWindowInsets()) != null) {
            androidx.core.graphics.c i27 = m0.y(rootWindowInsets).i();
            w0.c cVar = this.f5181g;
            cVar.M(Math.max(cVar.x(), i27.f4708a));
            w0.c cVar2 = this.f5182h;
            cVar2.M(Math.max(cVar2.x(), i27.f4710c));
        }
        this.f5186m = false;
        this.f5187n = false;
    }

    @Override // android.view.View
    @SuppressLint({"WrongConstant"})
    protected void onMeasure(int i8, int i9) {
        int t8;
        int mode = View.MeasureSpec.getMode(i8);
        int mode2 = View.MeasureSpec.getMode(i9);
        int size = View.MeasureSpec.getSize(i8);
        int size2 = View.MeasureSpec.getSize(i9);
        if (mode != 1073741824 || mode2 != 1073741824) {
            if (!isInEditMode()) {
                throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
            }
            if (mode == 0) {
                size = 300;
            }
            if (mode2 == 0) {
                size2 = 300;
            }
        }
        setMeasuredDimension(size, size2);
        int i10 = 0;
        boolean z4 = this.K != null && c0.B(this);
        int E = c0.E(this);
        int childCount = getChildCount();
        int i11 = 0;
        boolean z8 = false;
        boolean z9 = false;
        while (i11 < childCount) {
            View childAt = getChildAt(i11);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (z4) {
                    int b9 = androidx.core.view.f.b(layoutParams.f5194a, E);
                    boolean B = c0.B(childAt);
                    int i12 = Build.VERSION.SDK_INT;
                    if (B) {
                        if (i12 >= 21) {
                            WindowInsets windowInsets = (WindowInsets) this.K;
                            if (b9 == 3) {
                                windowInsets = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), i10, windowInsets.getSystemWindowInsetBottom());
                            } else if (b9 == 5) {
                                windowInsets = windowInsets.replaceSystemWindowInsets(i10, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
                            }
                            childAt.dispatchApplyWindowInsets(windowInsets);
                        }
                    } else if (i12 >= 21) {
                        WindowInsets windowInsets2 = (WindowInsets) this.K;
                        if (b9 == 3) {
                            windowInsets2 = windowInsets2.replaceSystemWindowInsets(windowInsets2.getSystemWindowInsetLeft(), windowInsets2.getSystemWindowInsetTop(), i10, windowInsets2.getSystemWindowInsetBottom());
                        } else if (b9 == 5) {
                            windowInsets2 = windowInsets2.replaceSystemWindowInsets(i10, windowInsets2.getSystemWindowInsetTop(), windowInsets2.getSystemWindowInsetRight(), windowInsets2.getSystemWindowInsetBottom());
                        }
                        ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin = windowInsets2.getSystemWindowInsetLeft();
                        ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = windowInsets2.getSystemWindowInsetTop();
                        ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = windowInsets2.getSystemWindowInsetRight();
                        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = windowInsets2.getSystemWindowInsetBottom();
                    }
                }
                if (B(childAt)) {
                    childAt.measure(View.MeasureSpec.makeMeasureSpec((size - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin) - ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, 1073741824), View.MeasureSpec.makeMeasureSpec((size2 - ((ViewGroup.MarginLayoutParams) layoutParams).topMargin) - ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin, 1073741824));
                } else if (!E(childAt)) {
                    throw new IllegalStateException("Child " + childAt + " at index " + i11 + " does not have a valid layout_gravity - must be Gravity.LEFT, Gravity.RIGHT or Gravity.NO_GRAVITY");
                } else {
                    if (f5171f0) {
                        float y8 = c0.y(childAt);
                        float f5 = this.f5175b;
                        if (y8 != f5) {
                            c0.B0(childAt, f5);
                        }
                    }
                    int i13 = (t(childAt) & 7) == 3 ? 1 : i10;
                    if ((i13 != 0 && z8) || (i13 == 0 && z9)) {
                        throw new IllegalStateException("Child drawer has absolute gravity " + w(t8) + " but this DrawerLayout already has a drawer view along that edge");
                    }
                    if (i13 != 0) {
                        z8 = true;
                    } else {
                        z9 = true;
                    }
                    childAt.measure(ViewGroup.getChildMeasureSpec(i8, this.f5177c + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, ((ViewGroup.MarginLayoutParams) layoutParams).width), ViewGroup.getChildMeasureSpec(i9, ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin, ((ViewGroup.MarginLayoutParams) layoutParams).height));
                    i11++;
                    i10 = 0;
                }
            }
            i11++;
            i10 = 0;
        }
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        View n8;
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        int i8 = savedState.f5198c;
        if (i8 != 0 && (n8 = n(i8)) != null) {
            M(n8);
        }
        int i9 = savedState.f5199d;
        if (i9 != 3) {
            T(i9, 3);
        }
        int i10 = savedState.f5200e;
        if (i10 != 3) {
            T(i10, 5);
        }
        int i11 = savedState.f5201f;
        if (i11 != 3) {
            T(i11, 8388611);
        }
        int i12 = savedState.f5202g;
        if (i12 != 3) {
            T(i12, 8388613);
        }
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i8) {
        R();
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            LayoutParams layoutParams = (LayoutParams) getChildAt(i8).getLayoutParams();
            int i9 = layoutParams.f5197d;
            boolean z4 = i9 == 1;
            boolean z8 = i9 == 2;
            if (z4 || z8) {
                savedState.f5198c = layoutParams.f5194a;
                break;
            }
        }
        savedState.f5199d = this.f5188p;
        savedState.f5200e = this.q;
        savedState.f5201f = this.f5189t;
        savedState.f5202g = this.f5190w;
        return savedState;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0056, code lost:
        if (r(r7) != 2) goto L20;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            w0.c r0 = r6.f5181g
            r0.G(r7)
            w0.c r0 = r6.f5182h
            r0.G(r7)
            int r0 = r7.getAction()
            r0 = r0 & 255(0xff, float:3.57E-43)
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L5d
            if (r0 == r2) goto L1e
            r7 = 3
            if (r0 == r7) goto L1a
            goto L6b
        L1a:
            r6.i(r2)
            goto L69
        L1e:
            float r0 = r7.getX()
            float r7 = r7.getY()
            w0.c r3 = r6.f5181g
            int r4 = (int) r0
            int r5 = (int) r7
            android.view.View r3 = r3.u(r4, r5)
            if (r3 == 0) goto L58
            boolean r3 = r6.B(r3)
            if (r3 == 0) goto L58
            float r3 = r6.A
            float r0 = r0 - r3
            float r3 = r6.B
            float r7 = r7 - r3
            w0.c r3 = r6.f5181g
            int r3 = r3.A()
            float r0 = r0 * r0
            float r7 = r7 * r7
            float r0 = r0 + r7
            int r3 = r3 * r3
            float r7 = (float) r3
            int r7 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r7 >= 0) goto L58
            android.view.View r7 = r6.o()
            if (r7 == 0) goto L58
            int r7 = r6.r(r7)
            r0 = 2
            if (r7 != r0) goto L59
        L58:
            r1 = r2
        L59:
            r6.i(r1)
            goto L6b
        L5d:
            float r0 = r7.getX()
            float r7 = r7.getY()
            r6.A = r0
            r6.B = r7
        L69:
            r6.f5191x = r1
        L6b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.drawerlayout.widget.DrawerLayout.onTouchEvent(android.view.MotionEvent):boolean");
    }

    View p() {
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            if (E(childAt) && G(childAt)) {
                return childAt;
            }
        }
        return null;
    }

    public int q(int i8) {
        int E = c0.E(this);
        if (i8 == 3) {
            int i9 = this.f5188p;
            if (i9 != 3) {
                return i9;
            }
            int i10 = E == 0 ? this.f5189t : this.f5190w;
            if (i10 != 3) {
                return i10;
            }
            return 0;
        } else if (i8 == 5) {
            int i11 = this.q;
            if (i11 != 3) {
                return i11;
            }
            int i12 = E == 0 ? this.f5190w : this.f5189t;
            if (i12 != 3) {
                return i12;
            }
            return 0;
        } else if (i8 == 8388611) {
            int i13 = this.f5189t;
            if (i13 != 3) {
                return i13;
            }
            int i14 = E == 0 ? this.f5188p : this.q;
            if (i14 != 3) {
                return i14;
            }
            return 0;
        } else if (i8 != 8388613) {
            return 0;
        } else {
            int i15 = this.f5190w;
            if (i15 != 3) {
                return i15;
            }
            int i16 = E == 0 ? this.q : this.f5188p;
            if (i16 != 3) {
                return i16;
            }
            return 0;
        }
    }

    public int r(View view) {
        if (E(view)) {
            return q(((LayoutParams) view.getLayoutParams()).f5194a);
        }
        throw new IllegalArgumentException("View " + view + " is not a drawer");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z4) {
        super.requestDisallowInterceptTouchEvent(z4);
        if (z4) {
            i(true);
        }
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.f5186m) {
            return;
        }
        super.requestLayout();
    }

    public CharSequence s(int i8) {
        int b9 = androidx.core.view.f.b(i8, c0.E(this));
        if (b9 == 3) {
            return this.G;
        }
        if (b9 == 5) {
            return this.H;
        }
        return null;
    }

    public void setDrawerElevation(float f5) {
        this.f5175b = f5;
        for (int i8 = 0; i8 < getChildCount(); i8++) {
            View childAt = getChildAt(i8);
            if (E(childAt)) {
                c0.B0(childAt, this.f5175b);
            }
        }
    }

    @Deprecated
    public void setDrawerListener(e eVar) {
        e eVar2 = this.f5192y;
        if (eVar2 != null) {
            O(eVar2);
        }
        if (eVar != null) {
            a(eVar);
        }
        this.f5192y = eVar;
    }

    public void setDrawerLockMode(int i8) {
        T(i8, 3);
        T(i8, 5);
    }

    public void setScrimColor(int i8) {
        this.f5178d = i8;
        invalidate();
    }

    public void setStatusBarBackground(int i8) {
        this.C = i8 != 0 ? androidx.core.content.a.f(getContext(), i8) : null;
        invalidate();
    }

    public void setStatusBarBackground(Drawable drawable) {
        this.C = drawable;
        invalidate();
    }

    public void setStatusBarBackgroundColor(int i8) {
        this.C = new ColorDrawable(i8);
        invalidate();
    }

    int t(View view) {
        return androidx.core.view.f.b(((LayoutParams) view.getLayoutParams()).f5194a, c0.E(this));
    }

    float u(View view) {
        return ((LayoutParams) view.getLayoutParams()).f5195b;
    }
}
