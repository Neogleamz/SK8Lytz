package i;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.SparseArray;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b extends Drawable implements Drawable.Callback {

    /* renamed from: a  reason: collision with root package name */
    private d f20359a;

    /* renamed from: b  reason: collision with root package name */
    private Rect f20360b;

    /* renamed from: c  reason: collision with root package name */
    private Drawable f20361c;

    /* renamed from: d  reason: collision with root package name */
    private Drawable f20362d;

    /* renamed from: f  reason: collision with root package name */
    private boolean f20364f;

    /* renamed from: h  reason: collision with root package name */
    private boolean f20366h;

    /* renamed from: j  reason: collision with root package name */
    private Runnable f20367j;

    /* renamed from: k  reason: collision with root package name */
    private long f20368k;

    /* renamed from: l  reason: collision with root package name */
    private long f20369l;

    /* renamed from: m  reason: collision with root package name */
    private c f20370m;

    /* renamed from: e  reason: collision with root package name */
    private int f20363e = 255;

    /* renamed from: g  reason: collision with root package name */
    private int f20365g = -1;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            b.this.a(true);
            b.this.invalidateSelf();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: i.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0173b {
        public static boolean a(Drawable.ConstantState constantState) {
            return constantState.canApplyTheme();
        }

        public static void b(Drawable drawable, Outline outline) {
            drawable.getOutline(outline);
        }

        public static Resources c(Resources.Theme theme) {
            return theme.getResources();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c implements Drawable.Callback {

        /* renamed from: a  reason: collision with root package name */
        private Drawable.Callback f20372a;

        c() {
        }

        public Drawable.Callback a() {
            Drawable.Callback callback = this.f20372a;
            this.f20372a = null;
            return callback;
        }

        public c b(Drawable.Callback callback) {
            this.f20372a = callback;
            return this;
        }

        @Override // android.graphics.drawable.Drawable.Callback
        public void invalidateDrawable(Drawable drawable) {
        }

        @Override // android.graphics.drawable.Drawable.Callback
        public void scheduleDrawable(Drawable drawable, Runnable runnable, long j8) {
            Drawable.Callback callback = this.f20372a;
            if (callback != null) {
                callback.scheduleDrawable(drawable, runnable, j8);
            }
        }

        @Override // android.graphics.drawable.Drawable.Callback
        public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
            Drawable.Callback callback = this.f20372a;
            if (callback != null) {
                callback.unscheduleDrawable(drawable, runnable);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class d extends Drawable.ConstantState {
        int A;
        int B;
        boolean C;
        ColorFilter D;
        boolean E;
        ColorStateList F;
        PorterDuff.Mode G;
        boolean H;
        boolean I;

        /* renamed from: a  reason: collision with root package name */
        final b f20373a;

        /* renamed from: b  reason: collision with root package name */
        Resources f20374b;

        /* renamed from: c  reason: collision with root package name */
        int f20375c;

        /* renamed from: d  reason: collision with root package name */
        int f20376d;

        /* renamed from: e  reason: collision with root package name */
        int f20377e;

        /* renamed from: f  reason: collision with root package name */
        SparseArray<Drawable.ConstantState> f20378f;

        /* renamed from: g  reason: collision with root package name */
        Drawable[] f20379g;

        /* renamed from: h  reason: collision with root package name */
        int f20380h;

        /* renamed from: i  reason: collision with root package name */
        boolean f20381i;

        /* renamed from: j  reason: collision with root package name */
        boolean f20382j;

        /* renamed from: k  reason: collision with root package name */
        Rect f20383k;

        /* renamed from: l  reason: collision with root package name */
        boolean f20384l;

        /* renamed from: m  reason: collision with root package name */
        boolean f20385m;

        /* renamed from: n  reason: collision with root package name */
        int f20386n;

        /* renamed from: o  reason: collision with root package name */
        int f20387o;

        /* renamed from: p  reason: collision with root package name */
        int f20388p;
        int q;

        /* renamed from: r  reason: collision with root package name */
        boolean f20389r;

        /* renamed from: s  reason: collision with root package name */
        int f20390s;

        /* renamed from: t  reason: collision with root package name */
        boolean f20391t;

        /* renamed from: u  reason: collision with root package name */
        boolean f20392u;

        /* renamed from: v  reason: collision with root package name */
        boolean f20393v;

        /* renamed from: w  reason: collision with root package name */
        boolean f20394w;

        /* renamed from: x  reason: collision with root package name */
        boolean f20395x;

        /* renamed from: y  reason: collision with root package name */
        boolean f20396y;

        /* renamed from: z  reason: collision with root package name */
        int f20397z;

        /* JADX INFO: Access modifiers changed from: package-private */
        public d(d dVar, b bVar, Resources resources) {
            this.f20381i = false;
            this.f20384l = false;
            this.f20395x = true;
            this.A = 0;
            this.B = 0;
            this.f20373a = bVar;
            this.f20374b = resources != null ? resources : dVar != null ? dVar.f20374b : null;
            int f5 = b.f(resources, dVar != null ? dVar.f20375c : 0);
            this.f20375c = f5;
            if (dVar == null) {
                this.f20379g = new Drawable[10];
                this.f20380h = 0;
                return;
            }
            this.f20376d = dVar.f20376d;
            this.f20377e = dVar.f20377e;
            this.f20393v = true;
            this.f20394w = true;
            this.f20381i = dVar.f20381i;
            this.f20384l = dVar.f20384l;
            this.f20395x = dVar.f20395x;
            this.f20396y = dVar.f20396y;
            this.f20397z = dVar.f20397z;
            this.A = dVar.A;
            this.B = dVar.B;
            this.C = dVar.C;
            this.D = dVar.D;
            this.E = dVar.E;
            this.F = dVar.F;
            this.G = dVar.G;
            this.H = dVar.H;
            this.I = dVar.I;
            if (dVar.f20375c == f5) {
                if (dVar.f20382j) {
                    this.f20383k = dVar.f20383k != null ? new Rect(dVar.f20383k) : null;
                    this.f20382j = true;
                }
                if (dVar.f20385m) {
                    this.f20386n = dVar.f20386n;
                    this.f20387o = dVar.f20387o;
                    this.f20388p = dVar.f20388p;
                    this.q = dVar.q;
                    this.f20385m = true;
                }
            }
            if (dVar.f20389r) {
                this.f20390s = dVar.f20390s;
                this.f20389r = true;
            }
            if (dVar.f20391t) {
                this.f20392u = dVar.f20392u;
                this.f20391t = true;
            }
            Drawable[] drawableArr = dVar.f20379g;
            this.f20379g = new Drawable[drawableArr.length];
            this.f20380h = dVar.f20380h;
            SparseArray<Drawable.ConstantState> sparseArray = dVar.f20378f;
            this.f20378f = sparseArray != null ? sparseArray.clone() : new SparseArray<>(this.f20380h);
            int i8 = this.f20380h;
            for (int i9 = 0; i9 < i8; i9++) {
                if (drawableArr[i9] != null) {
                    Drawable.ConstantState constantState = drawableArr[i9].getConstantState();
                    if (constantState != null) {
                        this.f20378f.put(i9, constantState);
                    } else {
                        this.f20379g[i9] = drawableArr[i9];
                    }
                }
            }
        }

        private void e() {
            SparseArray<Drawable.ConstantState> sparseArray = this.f20378f;
            if (sparseArray != null) {
                int size = sparseArray.size();
                for (int i8 = 0; i8 < size; i8++) {
                    this.f20379g[this.f20378f.keyAt(i8)] = s(this.f20378f.valueAt(i8).newDrawable(this.f20374b));
                }
                this.f20378f = null;
            }
        }

        private Drawable s(Drawable drawable) {
            if (Build.VERSION.SDK_INT >= 23) {
                androidx.core.graphics.drawable.a.m(drawable, this.f20397z);
            }
            Drawable mutate = drawable.mutate();
            mutate.setCallback(this.f20373a);
            return mutate;
        }

        public final int a(Drawable drawable) {
            int i8 = this.f20380h;
            if (i8 >= this.f20379g.length) {
                o(i8, i8 + 10);
            }
            drawable.mutate();
            drawable.setVisible(false, true);
            drawable.setCallback(this.f20373a);
            this.f20379g[i8] = drawable;
            this.f20380h++;
            this.f20377e = drawable.getChangingConfigurations() | this.f20377e;
            p();
            this.f20383k = null;
            this.f20382j = false;
            this.f20385m = false;
            this.f20393v = false;
            return i8;
        }

        final void b(Resources.Theme theme) {
            if (theme != null) {
                e();
                int i8 = this.f20380h;
                Drawable[] drawableArr = this.f20379g;
                for (int i9 = 0; i9 < i8; i9++) {
                    if (drawableArr[i9] != null && androidx.core.graphics.drawable.a.b(drawableArr[i9])) {
                        androidx.core.graphics.drawable.a.a(drawableArr[i9], theme);
                        this.f20377e |= drawableArr[i9].getChangingConfigurations();
                    }
                }
                y(C0173b.c(theme));
            }
        }

        public boolean c() {
            if (this.f20393v) {
                return this.f20394w;
            }
            e();
            this.f20393v = true;
            int i8 = this.f20380h;
            Drawable[] drawableArr = this.f20379g;
            for (int i9 = 0; i9 < i8; i9++) {
                if (drawableArr[i9].getConstantState() == null) {
                    this.f20394w = false;
                    return false;
                }
            }
            this.f20394w = true;
            return true;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            int i8 = this.f20380h;
            Drawable[] drawableArr = this.f20379g;
            for (int i9 = 0; i9 < i8; i9++) {
                Drawable drawable = drawableArr[i9];
                if (drawable == null) {
                    Drawable.ConstantState constantState = this.f20378f.get(i9);
                    if (constantState != null && C0173b.a(constantState)) {
                        return true;
                    }
                } else if (androidx.core.graphics.drawable.a.b(drawable)) {
                    return true;
                }
            }
            return false;
        }

        protected void d() {
            this.f20385m = true;
            e();
            int i8 = this.f20380h;
            Drawable[] drawableArr = this.f20379g;
            this.f20387o = -1;
            this.f20386n = -1;
            this.q = 0;
            this.f20388p = 0;
            for (int i9 = 0; i9 < i8; i9++) {
                Drawable drawable = drawableArr[i9];
                int intrinsicWidth = drawable.getIntrinsicWidth();
                if (intrinsicWidth > this.f20386n) {
                    this.f20386n = intrinsicWidth;
                }
                int intrinsicHeight = drawable.getIntrinsicHeight();
                if (intrinsicHeight > this.f20387o) {
                    this.f20387o = intrinsicHeight;
                }
                int minimumWidth = drawable.getMinimumWidth();
                if (minimumWidth > this.f20388p) {
                    this.f20388p = minimumWidth;
                }
                int minimumHeight = drawable.getMinimumHeight();
                if (minimumHeight > this.q) {
                    this.q = minimumHeight;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final int f() {
            return this.f20379g.length;
        }

        public final Drawable g(int i8) {
            int indexOfKey;
            Drawable drawable = this.f20379g[i8];
            if (drawable != null) {
                return drawable;
            }
            SparseArray<Drawable.ConstantState> sparseArray = this.f20378f;
            if (sparseArray == null || (indexOfKey = sparseArray.indexOfKey(i8)) < 0) {
                return null;
            }
            Drawable s8 = s(this.f20378f.valueAt(indexOfKey).newDrawable(this.f20374b));
            this.f20379g[i8] = s8;
            this.f20378f.removeAt(indexOfKey);
            if (this.f20378f.size() == 0) {
                this.f20378f = null;
            }
            return s8;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.f20376d | this.f20377e;
        }

        public final int h() {
            return this.f20380h;
        }

        public final int i() {
            if (!this.f20385m) {
                d();
            }
            return this.f20387o;
        }

        public final int j() {
            if (!this.f20385m) {
                d();
            }
            return this.q;
        }

        public final int k() {
            if (!this.f20385m) {
                d();
            }
            return this.f20388p;
        }

        public final Rect l() {
            Rect rect = null;
            if (this.f20381i) {
                return null;
            }
            Rect rect2 = this.f20383k;
            if (rect2 != null || this.f20382j) {
                return rect2;
            }
            e();
            Rect rect3 = new Rect();
            int i8 = this.f20380h;
            Drawable[] drawableArr = this.f20379g;
            for (int i9 = 0; i9 < i8; i9++) {
                if (drawableArr[i9].getPadding(rect3)) {
                    if (rect == null) {
                        rect = new Rect(0, 0, 0, 0);
                    }
                    int i10 = rect3.left;
                    if (i10 > rect.left) {
                        rect.left = i10;
                    }
                    int i11 = rect3.top;
                    if (i11 > rect.top) {
                        rect.top = i11;
                    }
                    int i12 = rect3.right;
                    if (i12 > rect.right) {
                        rect.right = i12;
                    }
                    int i13 = rect3.bottom;
                    if (i13 > rect.bottom) {
                        rect.bottom = i13;
                    }
                }
            }
            this.f20382j = true;
            this.f20383k = rect;
            return rect;
        }

        public final int m() {
            if (!this.f20385m) {
                d();
            }
            return this.f20386n;
        }

        public final int n() {
            if (this.f20389r) {
                return this.f20390s;
            }
            e();
            int i8 = this.f20380h;
            Drawable[] drawableArr = this.f20379g;
            int opacity = i8 > 0 ? drawableArr[0].getOpacity() : -2;
            for (int i9 = 1; i9 < i8; i9++) {
                opacity = Drawable.resolveOpacity(opacity, drawableArr[i9].getOpacity());
            }
            this.f20390s = opacity;
            this.f20389r = true;
            return opacity;
        }

        public void o(int i8, int i9) {
            Drawable[] drawableArr = new Drawable[i9];
            Drawable[] drawableArr2 = this.f20379g;
            if (drawableArr2 != null) {
                System.arraycopy(drawableArr2, 0, drawableArr, 0, i8);
            }
            this.f20379g = drawableArr;
        }

        void p() {
            this.f20389r = false;
            this.f20391t = false;
        }

        public final boolean q() {
            return this.f20384l;
        }

        abstract void r();

        public final void t(boolean z4) {
            this.f20384l = z4;
        }

        public final void u(int i8) {
            this.A = i8;
        }

        public final void v(int i8) {
            this.B = i8;
        }

        final boolean w(int i8, int i9) {
            int i10 = this.f20380h;
            Drawable[] drawableArr = this.f20379g;
            boolean z4 = false;
            for (int i11 = 0; i11 < i10; i11++) {
                if (drawableArr[i11] != null) {
                    boolean m8 = Build.VERSION.SDK_INT >= 23 ? androidx.core.graphics.drawable.a.m(drawableArr[i11], i8) : false;
                    if (i11 == i9) {
                        z4 = m8;
                    }
                }
            }
            this.f20397z = i8;
            return z4;
        }

        public final void x(boolean z4) {
            this.f20381i = z4;
        }

        final void y(Resources resources) {
            if (resources != null) {
                this.f20374b = resources;
                int f5 = b.f(resources, this.f20375c);
                int i8 = this.f20375c;
                this.f20375c = f5;
                if (i8 != f5) {
                    this.f20385m = false;
                    this.f20382j = false;
                }
            }
        }
    }

    private void d(Drawable drawable) {
        if (this.f20370m == null) {
            this.f20370m = new c();
        }
        drawable.setCallback(this.f20370m.b(drawable.getCallback()));
        try {
            if (this.f20359a.A <= 0 && this.f20364f) {
                drawable.setAlpha(this.f20363e);
            }
            d dVar = this.f20359a;
            if (dVar.E) {
                drawable.setColorFilter(dVar.D);
            } else {
                if (dVar.H) {
                    androidx.core.graphics.drawable.a.o(drawable, dVar.F);
                }
                d dVar2 = this.f20359a;
                if (dVar2.I) {
                    androidx.core.graphics.drawable.a.p(drawable, dVar2.G);
                }
            }
            drawable.setVisible(isVisible(), true);
            drawable.setDither(this.f20359a.f20395x);
            drawable.setState(getState());
            drawable.setLevel(getLevel());
            drawable.setBounds(getBounds());
            int i8 = Build.VERSION.SDK_INT;
            if (i8 >= 23) {
                androidx.core.graphics.drawable.a.m(drawable, androidx.core.graphics.drawable.a.f(this));
            }
            if (i8 >= 19) {
                androidx.core.graphics.drawable.a.j(drawable, this.f20359a.C);
            }
            Rect rect = this.f20360b;
            if (i8 >= 21 && rect != null) {
                androidx.core.graphics.drawable.a.l(drawable, rect.left, rect.top, rect.right, rect.bottom);
            }
        } finally {
            drawable.setCallback(this.f20370m.a());
        }
    }

    private boolean e() {
        return isAutoMirrored() && androidx.core.graphics.drawable.a.f(this) == 1;
    }

    static int f(Resources resources, int i8) {
        if (resources != null) {
            i8 = resources.getDisplayMetrics().densityDpi;
        }
        if (i8 == 0) {
            return 160;
        }
        return i8;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0062 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:26:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void a(boolean r14) {
        /*
            r13 = this;
            r0 = 1
            r13.f20364f = r0
            long r1 = android.os.SystemClock.uptimeMillis()
            android.graphics.drawable.Drawable r3 = r13.f20361c
            r4 = 255(0xff, double:1.26E-321)
            r6 = 0
            r7 = 0
            if (r3 == 0) goto L34
            long r9 = r13.f20368k
            int r11 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1))
            if (r11 == 0) goto L36
            int r11 = (r9 > r1 ? 1 : (r9 == r1 ? 0 : -1))
            if (r11 > 0) goto L20
            int r9 = r13.f20363e
            r3.setAlpha(r9)
            goto L34
        L20:
            long r9 = r9 - r1
            long r9 = r9 * r4
            int r9 = (int) r9
            i.b$d r10 = r13.f20359a
            int r10 = r10.A
            int r9 = r9 / r10
            int r9 = 255 - r9
            int r10 = r13.f20363e
            int r9 = r9 * r10
            int r9 = r9 / 255
            r3.setAlpha(r9)
            r3 = r0
            goto L37
        L34:
            r13.f20368k = r7
        L36:
            r3 = r6
        L37:
            android.graphics.drawable.Drawable r9 = r13.f20362d
            if (r9 == 0) goto L5d
            long r10 = r13.f20369l
            int r12 = (r10 > r7 ? 1 : (r10 == r7 ? 0 : -1))
            if (r12 == 0) goto L5f
            int r12 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1))
            if (r12 > 0) goto L4c
            r9.setVisible(r6, r6)
            r0 = 0
            r13.f20362d = r0
            goto L5d
        L4c:
            long r10 = r10 - r1
            long r10 = r10 * r4
            int r3 = (int) r10
            i.b$d r4 = r13.f20359a
            int r4 = r4.B
            int r3 = r3 / r4
            int r4 = r13.f20363e
            int r3 = r3 * r4
            int r3 = r3 / 255
            r9.setAlpha(r3)
            goto L60
        L5d:
            r13.f20369l = r7
        L5f:
            r0 = r3
        L60:
            if (r14 == 0) goto L6c
            if (r0 == 0) goto L6c
            java.lang.Runnable r14 = r13.f20367j
            r3 = 16
            long r1 = r1 + r3
            r13.scheduleSelf(r14, r1)
        L6c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: i.b.a(boolean):void");
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme theme) {
        this.f20359a.b(theme);
    }

    d b() {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int c() {
        return this.f20365g;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return this.f20359a.canApplyTheme();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Drawable drawable = this.f20361c;
        if (drawable != null) {
            drawable.draw(canvas);
        }
        Drawable drawable2 = this.f20362d;
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0073  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean g(int r10) {
        /*
            r9 = this;
            int r0 = r9.f20365g
            r1 = 0
            if (r10 != r0) goto L6
            return r1
        L6:
            long r2 = android.os.SystemClock.uptimeMillis()
            i.b$d r0 = r9.f20359a
            int r0 = r0.B
            r4 = 0
            r5 = 0
            if (r0 <= 0) goto L2e
            android.graphics.drawable.Drawable r0 = r9.f20362d
            if (r0 == 0) goto L1a
            r0.setVisible(r1, r1)
        L1a:
            android.graphics.drawable.Drawable r0 = r9.f20361c
            if (r0 == 0) goto L29
            r9.f20362d = r0
            i.b$d r0 = r9.f20359a
            int r0 = r0.B
            long r0 = (long) r0
            long r0 = r0 + r2
            r9.f20369l = r0
            goto L35
        L29:
            r9.f20362d = r4
            r9.f20369l = r5
            goto L35
        L2e:
            android.graphics.drawable.Drawable r0 = r9.f20361c
            if (r0 == 0) goto L35
            r0.setVisible(r1, r1)
        L35:
            if (r10 < 0) goto L55
            i.b$d r0 = r9.f20359a
            int r1 = r0.f20380h
            if (r10 >= r1) goto L55
            android.graphics.drawable.Drawable r0 = r0.g(r10)
            r9.f20361c = r0
            r9.f20365g = r10
            if (r0 == 0) goto L5a
            i.b$d r10 = r9.f20359a
            int r10 = r10.A
            if (r10 <= 0) goto L51
            long r7 = (long) r10
            long r2 = r2 + r7
            r9.f20368k = r2
        L51:
            r9.d(r0)
            goto L5a
        L55:
            r9.f20361c = r4
            r10 = -1
            r9.f20365g = r10
        L5a:
            long r0 = r9.f20368k
            int r10 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            r0 = 1
            if (r10 != 0) goto L67
            long r1 = r9.f20369l
            int r10 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r10 == 0) goto L79
        L67:
            java.lang.Runnable r10 = r9.f20367j
            if (r10 != 0) goto L73
            i.b$a r10 = new i.b$a
            r10.<init>()
            r9.f20367j = r10
            goto L76
        L73:
            r9.unscheduleSelf(r10)
        L76:
            r9.a(r0)
        L79:
            r9.invalidateSelf()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: i.b.g(int):boolean");
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f20363e;
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.f20359a.getChangingConfigurations();
    }

    @Override // android.graphics.drawable.Drawable
    public final Drawable.ConstantState getConstantState() {
        if (this.f20359a.c()) {
            this.f20359a.f20376d = getChangingConfigurations();
            return this.f20359a;
        }
        return null;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable getCurrent() {
        return this.f20361c;
    }

    @Override // android.graphics.drawable.Drawable
    public void getHotspotBounds(Rect rect) {
        Rect rect2 = this.f20360b;
        if (rect2 != null) {
            rect.set(rect2);
        } else {
            super.getHotspotBounds(rect);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        if (this.f20359a.q()) {
            return this.f20359a.i();
        }
        Drawable drawable = this.f20361c;
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        if (this.f20359a.q()) {
            return this.f20359a.m();
        }
        Drawable drawable = this.f20361c;
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumHeight() {
        if (this.f20359a.q()) {
            return this.f20359a.j();
        }
        Drawable drawable = this.f20361c;
        if (drawable != null) {
            return drawable.getMinimumHeight();
        }
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public int getMinimumWidth() {
        if (this.f20359a.q()) {
            return this.f20359a.k();
        }
        Drawable drawable = this.f20361c;
        if (drawable != null) {
            return drawable.getMinimumWidth();
        }
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        Drawable drawable = this.f20361c;
        if (drawable == null || !drawable.isVisible()) {
            return -2;
        }
        return this.f20359a.n();
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        Drawable drawable = this.f20361c;
        if (drawable != null) {
            C0173b.b(drawable, outline);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        boolean padding;
        Rect l8 = this.f20359a.l();
        if (l8 != null) {
            rect.set(l8);
            padding = (l8.right | ((l8.left | l8.top) | l8.bottom)) != 0;
        } else {
            Drawable drawable = this.f20361c;
            padding = drawable != null ? drawable.getPadding(rect) : super.getPadding(rect);
        }
        if (e()) {
            int i8 = rect.left;
            rect.left = rect.right;
            rect.right = i8;
        }
        return padding;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(d dVar) {
        this.f20359a = dVar;
        int i8 = this.f20365g;
        if (i8 >= 0) {
            Drawable g8 = dVar.g(i8);
            this.f20361c = g8;
            if (g8 != null) {
                d(g8);
            }
        }
        this.f20362d = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void i(Resources resources) {
        this.f20359a.y(resources);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        d dVar = this.f20359a;
        if (dVar != null) {
            dVar.p();
        }
        if (drawable != this.f20361c || getCallback() == null) {
            return;
        }
        getCallback().invalidateDrawable(this);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return this.f20359a.C;
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        boolean z4;
        Drawable drawable = this.f20362d;
        boolean z8 = true;
        if (drawable != null) {
            drawable.jumpToCurrentState();
            this.f20362d = null;
            z4 = true;
        } else {
            z4 = false;
        }
        Drawable drawable2 = this.f20361c;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
            if (this.f20364f) {
                this.f20361c.setAlpha(this.f20363e);
            }
        }
        if (this.f20369l != 0) {
            this.f20369l = 0L;
            z4 = true;
        }
        if (this.f20368k != 0) {
            this.f20368k = 0L;
        } else {
            z8 = z4;
        }
        if (z8) {
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.f20366h && super.mutate() == this) {
            d b9 = b();
            b9.r();
            h(b9);
            this.f20366h = true;
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        Drawable drawable = this.f20362d;
        if (drawable != null) {
            drawable.setBounds(rect);
        }
        Drawable drawable2 = this.f20361c;
        if (drawable2 != null) {
            drawable2.setBounds(rect);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onLayoutDirectionChanged(int i8) {
        return this.f20359a.w(i8, c());
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onLevelChange(int i8) {
        Drawable drawable = this.f20362d;
        if (drawable != null) {
            return drawable.setLevel(i8);
        }
        Drawable drawable2 = this.f20361c;
        if (drawable2 != null) {
            return drawable2.setLevel(i8);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        Drawable drawable = this.f20362d;
        if (drawable != null) {
            return drawable.setState(iArr);
        }
        Drawable drawable2 = this.f20361c;
        if (drawable2 != null) {
            return drawable2.setState(iArr);
        }
        return false;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j8) {
        if (drawable != this.f20361c || getCallback() == null) {
            return;
        }
        getCallback().scheduleDrawable(this, runnable, j8);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        if (this.f20364f && this.f20363e == i8) {
            return;
        }
        this.f20364f = true;
        this.f20363e = i8;
        Drawable drawable = this.f20361c;
        if (drawable != null) {
            if (this.f20368k == 0) {
                drawable.setAlpha(i8);
            } else {
                a(false);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean z4) {
        d dVar = this.f20359a;
        if (dVar.C != z4) {
            dVar.C = z4;
            Drawable drawable = this.f20361c;
            if (drawable != null) {
                androidx.core.graphics.drawable.a.j(drawable, z4);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        d dVar = this.f20359a;
        dVar.E = true;
        if (dVar.D != colorFilter) {
            dVar.D = colorFilter;
            Drawable drawable = this.f20361c;
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean z4) {
        d dVar = this.f20359a;
        if (dVar.f20395x != z4) {
            dVar.f20395x = z4;
            Drawable drawable = this.f20361c;
            if (drawable != null) {
                drawable.setDither(z4);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspot(float f5, float f8) {
        Drawable drawable = this.f20361c;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.k(drawable, f5, f8);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspotBounds(int i8, int i9, int i10, int i11) {
        Rect rect = this.f20360b;
        if (rect == null) {
            this.f20360b = new Rect(i8, i9, i10, i11);
        } else {
            rect.set(i8, i9, i10, i11);
        }
        Drawable drawable = this.f20361c;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.l(drawable, i8, i9, i10, i11);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setTint(int i8) {
        setTintList(ColorStateList.valueOf(i8));
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList colorStateList) {
        d dVar = this.f20359a;
        dVar.H = true;
        if (dVar.F != colorStateList) {
            dVar.F = colorStateList;
            androidx.core.graphics.drawable.a.o(this.f20361c, colorStateList);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode mode) {
        d dVar = this.f20359a;
        dVar.I = true;
        if (dVar.G != mode) {
            dVar.G = mode;
            androidx.core.graphics.drawable.a.p(this.f20361c, mode);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z4, boolean z8) {
        boolean visible = super.setVisible(z4, z8);
        Drawable drawable = this.f20362d;
        if (drawable != null) {
            drawable.setVisible(z4, z8);
        }
        Drawable drawable2 = this.f20361c;
        if (drawable2 != null) {
            drawable2.setVisible(z4, z8);
        }
        return visible;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        if (drawable != this.f20361c || getCallback() == null) {
            return;
        }
        getCallback().unscheduleDrawable(this, runnable);
    }
}
