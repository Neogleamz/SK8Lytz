package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import androidx.appcompat.widget.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g {

    /* renamed from: b  reason: collision with root package name */
    private static final PorterDuff.Mode f1483b = PorterDuff.Mode.SRC_IN;

    /* renamed from: c  reason: collision with root package name */
    private static g f1484c;

    /* renamed from: a  reason: collision with root package name */
    private z f1485a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements z.f {

        /* renamed from: a  reason: collision with root package name */
        private final int[] f1486a = {g.e.R, g.e.P, g.e.f19911a};

        /* renamed from: b  reason: collision with root package name */
        private final int[] f1487b = {g.e.f19925o, g.e.B, g.e.f19929t, g.e.f19926p, g.e.q, g.e.f19928s, g.e.f19927r};

        /* renamed from: c  reason: collision with root package name */
        private final int[] f1488c = {g.e.O, g.e.Q, g.e.f19921k, g.e.K, g.e.L, g.e.M, g.e.N};

        /* renamed from: d  reason: collision with root package name */
        private final int[] f1489d = {g.e.f19932w, g.e.f19919i, g.e.f19931v};

        /* renamed from: e  reason: collision with root package name */
        private final int[] f1490e = {g.e.J, g.e.S};

        /* renamed from: f  reason: collision with root package name */
        private final int[] f1491f = {g.e.f19913c, g.e.f19917g, g.e.f19914d, g.e.f19918h};

        a() {
        }

        private boolean f(int[] iArr, int i8) {
            for (int i9 : iArr) {
                if (i9 == i8) {
                    return true;
                }
            }
            return false;
        }

        private ColorStateList g(Context context) {
            return h(context, 0);
        }

        private ColorStateList h(Context context, int i8) {
            int c9 = e0.c(context, g.a.f19883w);
            return new ColorStateList(new int[][]{e0.f1468b, e0.f1471e, e0.f1469c, e0.f1475i}, new int[]{e0.b(context, g.a.f19881u), androidx.core.graphics.b.k(c9, i8), androidx.core.graphics.b.k(c9, i8), i8});
        }

        private ColorStateList i(Context context) {
            return h(context, e0.c(context, g.a.f19880t));
        }

        private ColorStateList j(Context context) {
            return h(context, e0.c(context, g.a.f19881u));
        }

        private ColorStateList k(Context context) {
            int[][] iArr = new int[3];
            int[] iArr2 = new int[3];
            int i8 = g.a.f19886z;
            ColorStateList e8 = e0.e(context, i8);
            if (e8 == null || !e8.isStateful()) {
                iArr[0] = e0.f1468b;
                iArr2[0] = e0.b(context, i8);
                iArr[1] = e0.f1472f;
                iArr2[1] = e0.c(context, g.a.f19882v);
                iArr[2] = e0.f1475i;
                iArr2[2] = e0.c(context, i8);
            } else {
                iArr[0] = e0.f1468b;
                iArr2[0] = e8.getColorForState(iArr[0], 0);
                iArr[1] = e0.f1472f;
                iArr2[1] = e0.c(context, g.a.f19882v);
                iArr[2] = e0.f1475i;
                iArr2[2] = e8.getDefaultColor();
            }
            return new ColorStateList(iArr, iArr2);
        }

        private LayerDrawable l(z zVar, Context context, int i8) {
            BitmapDrawable bitmapDrawable;
            BitmapDrawable bitmapDrawable2;
            BitmapDrawable bitmapDrawable3;
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(i8);
            Drawable j8 = zVar.j(context, g.e.F);
            Drawable j9 = zVar.j(context, g.e.G);
            if ((j8 instanceof BitmapDrawable) && j8.getIntrinsicWidth() == dimensionPixelSize && j8.getIntrinsicHeight() == dimensionPixelSize) {
                bitmapDrawable = (BitmapDrawable) j8;
                bitmapDrawable2 = new BitmapDrawable(bitmapDrawable.getBitmap());
            } else {
                Bitmap createBitmap = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                j8.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                j8.draw(canvas);
                bitmapDrawable = new BitmapDrawable(createBitmap);
                bitmapDrawable2 = new BitmapDrawable(createBitmap);
            }
            bitmapDrawable2.setTileModeX(Shader.TileMode.REPEAT);
            if ((j9 instanceof BitmapDrawable) && j9.getIntrinsicWidth() == dimensionPixelSize && j9.getIntrinsicHeight() == dimensionPixelSize) {
                bitmapDrawable3 = (BitmapDrawable) j9;
            } else {
                Bitmap createBitmap2 = Bitmap.createBitmap(dimensionPixelSize, dimensionPixelSize, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(createBitmap2);
                j9.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                j9.draw(canvas2);
                bitmapDrawable3 = new BitmapDrawable(createBitmap2);
            }
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{bitmapDrawable, bitmapDrawable3, bitmapDrawable2});
            layerDrawable.setId(0, 16908288);
            layerDrawable.setId(1, 16908303);
            layerDrawable.setId(2, 16908301);
            return layerDrawable;
        }

        private void m(Drawable drawable, int i8, PorterDuff.Mode mode) {
            if (t.a(drawable)) {
                drawable = drawable.mutate();
            }
            if (mode == null) {
                mode = g.f1483b;
            }
            drawable.setColorFilter(g.e(i8, mode));
        }

        /* JADX WARN: Removed duplicated region for block: B:21:0x0046  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x0061 A[RETURN] */
        @Override // androidx.appcompat.widget.z.f
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean a(android.content.Context r7, int r8, android.graphics.drawable.Drawable r9) {
            /*
                r6 = this;
                android.graphics.PorterDuff$Mode r0 = androidx.appcompat.widget.g.a()
                int[] r1 = r6.f1486a
                boolean r1 = r6.f(r1, r8)
                r2 = 16842801(0x1010031, float:2.3693695E-38)
                r3 = -1
                r4 = 0
                r5 = 1
                if (r1 == 0) goto L17
                int r2 = g.a.f19884x
            L14:
                r8 = r3
            L15:
                r1 = r5
                goto L44
            L17:
                int[] r1 = r6.f1488c
                boolean r1 = r6.f(r1, r8)
                if (r1 == 0) goto L22
                int r2 = g.a.f19882v
                goto L14
            L22:
                int[] r1 = r6.f1489d
                boolean r1 = r6.f(r1, r8)
                if (r1 == 0) goto L2d
                android.graphics.PorterDuff$Mode r0 = android.graphics.PorterDuff.Mode.MULTIPLY
                goto L14
            L2d:
                int r1 = g.e.f19930u
                if (r8 != r1) goto L3c
                r2 = 16842800(0x1010030, float:2.3693693E-38)
                r8 = 1109603123(0x42233333, float:40.8)
                int r8 = java.lang.Math.round(r8)
                goto L15
            L3c:
                int r1 = g.e.f19922l
                if (r8 != r1) goto L41
                goto L14
            L41:
                r8 = r3
                r1 = r4
                r2 = r1
            L44:
                if (r1 == 0) goto L61
                boolean r1 = androidx.appcompat.widget.t.a(r9)
                if (r1 == 0) goto L50
                android.graphics.drawable.Drawable r9 = r9.mutate()
            L50:
                int r7 = androidx.appcompat.widget.e0.c(r7, r2)
                android.graphics.PorterDuffColorFilter r7 = androidx.appcompat.widget.g.e(r7, r0)
                r9.setColorFilter(r7)
                if (r8 == r3) goto L60
                r9.setAlpha(r8)
            L60:
                return r5
            L61:
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.g.a.a(android.content.Context, int, android.graphics.drawable.Drawable):boolean");
        }

        @Override // androidx.appcompat.widget.z.f
        public PorterDuff.Mode b(int i8) {
            if (i8 == g.e.H) {
                return PorterDuff.Mode.MULTIPLY;
            }
            return null;
        }

        @Override // androidx.appcompat.widget.z.f
        public Drawable c(z zVar, Context context, int i8) {
            int i9;
            if (i8 == g.e.f19920j) {
                return new LayerDrawable(new Drawable[]{zVar.j(context, g.e.f19919i), zVar.j(context, g.e.f19921k)});
            }
            if (i8 == g.e.f19934y) {
                i9 = g.d.f19904i;
            } else if (i8 == g.e.f19933x) {
                i9 = g.d.f19905j;
            } else if (i8 != g.e.f19935z) {
                return null;
            } else {
                i9 = g.d.f19906k;
            }
            return l(zVar, context, i9);
        }

        @Override // androidx.appcompat.widget.z.f
        public ColorStateList d(Context context, int i8) {
            if (i8 == g.e.f19923m) {
                return h.a.a(context, g.c.f19892e);
            }
            if (i8 == g.e.I) {
                return h.a.a(context, g.c.f19895h);
            }
            if (i8 == g.e.H) {
                return k(context);
            }
            if (i8 == g.e.f19916f) {
                return j(context);
            }
            if (i8 == g.e.f19912b) {
                return g(context);
            }
            if (i8 == g.e.f19915e) {
                return i(context);
            }
            if (i8 == g.e.D || i8 == g.e.E) {
                return h.a.a(context, g.c.f19894g);
            }
            if (f(this.f1487b, i8)) {
                return e0.e(context, g.a.f19884x);
            }
            if (f(this.f1490e, i8)) {
                return h.a.a(context, g.c.f19891d);
            }
            if (f(this.f1491f, i8)) {
                return h.a.a(context, g.c.f19890c);
            }
            if (i8 == g.e.A) {
                return h.a.a(context, g.c.f19893f);
            }
            return null;
        }

        @Override // androidx.appcompat.widget.z.f
        public boolean e(Context context, int i8, Drawable drawable) {
            Drawable findDrawableByLayerId;
            int c9;
            if (i8 == g.e.C) {
                LayerDrawable layerDrawable = (LayerDrawable) drawable;
                Drawable findDrawableByLayerId2 = layerDrawable.findDrawableByLayerId(16908288);
                int i9 = g.a.f19884x;
                m(findDrawableByLayerId2, e0.c(context, i9), g.f1483b);
                m(layerDrawable.findDrawableByLayerId(16908303), e0.c(context, i9), g.f1483b);
                findDrawableByLayerId = layerDrawable.findDrawableByLayerId(16908301);
                c9 = e0.c(context, g.a.f19882v);
            } else if (i8 != g.e.f19934y && i8 != g.e.f19933x && i8 != g.e.f19935z) {
                return false;
            } else {
                LayerDrawable layerDrawable2 = (LayerDrawable) drawable;
                m(layerDrawable2.findDrawableByLayerId(16908288), e0.b(context, g.a.f19884x), g.f1483b);
                Drawable findDrawableByLayerId3 = layerDrawable2.findDrawableByLayerId(16908303);
                int i10 = g.a.f19882v;
                m(findDrawableByLayerId3, e0.c(context, i10), g.f1483b);
                findDrawableByLayerId = layerDrawable2.findDrawableByLayerId(16908301);
                c9 = e0.c(context, i10);
            }
            m(findDrawableByLayerId, c9, g.f1483b);
            return true;
        }
    }

    public static synchronized g b() {
        g gVar;
        synchronized (g.class) {
            if (f1484c == null) {
                h();
            }
            gVar = f1484c;
        }
        return gVar;
    }

    public static synchronized PorterDuffColorFilter e(int i8, PorterDuff.Mode mode) {
        PorterDuffColorFilter l8;
        synchronized (g.class) {
            l8 = z.l(i8, mode);
        }
        return l8;
    }

    public static synchronized void h() {
        synchronized (g.class) {
            if (f1484c == null) {
                g gVar = new g();
                f1484c = gVar;
                gVar.f1485a = z.h();
                f1484c.f1485a.u(new a());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void i(Drawable drawable, h0 h0Var, int[] iArr) {
        z.w(drawable, h0Var, iArr);
    }

    public synchronized Drawable c(Context context, int i8) {
        return this.f1485a.j(context, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Drawable d(Context context, int i8, boolean z4) {
        return this.f1485a.k(context, i8, z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ColorStateList f(Context context, int i8) {
        return this.f1485a.m(context, i8);
    }

    public synchronized void g(Context context) {
        this.f1485a.s(context);
    }
}
