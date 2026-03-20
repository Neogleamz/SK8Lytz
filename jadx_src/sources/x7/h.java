package x7;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import java.util.BitSet;
import x7.m;
import x7.n;
import x7.o;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h extends Drawable implements androidx.core.graphics.drawable.b, p {
    private static final String C = h.class.getSimpleName();
    private static final Paint E = new Paint(1);
    private final RectF A;
    private boolean B;

    /* renamed from: a  reason: collision with root package name */
    private c f24164a;

    /* renamed from: b  reason: collision with root package name */
    private final o.g[] f24165b;

    /* renamed from: c  reason: collision with root package name */
    private final o.g[] f24166c;

    /* renamed from: d  reason: collision with root package name */
    private final BitSet f24167d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f24168e;

    /* renamed from: f  reason: collision with root package name */
    private final Matrix f24169f;

    /* renamed from: g  reason: collision with root package name */
    private final Path f24170g;

    /* renamed from: h  reason: collision with root package name */
    private final Path f24171h;

    /* renamed from: j  reason: collision with root package name */
    private final RectF f24172j;

    /* renamed from: k  reason: collision with root package name */
    private final RectF f24173k;

    /* renamed from: l  reason: collision with root package name */
    private final Region f24174l;

    /* renamed from: m  reason: collision with root package name */
    private final Region f24175m;

    /* renamed from: n  reason: collision with root package name */
    private m f24176n;

    /* renamed from: p  reason: collision with root package name */
    private final Paint f24177p;
    private final Paint q;

    /* renamed from: t  reason: collision with root package name */
    private final w7.a f24178t;

    /* renamed from: w  reason: collision with root package name */
    private final n.b f24179w;

    /* renamed from: x  reason: collision with root package name */
    private final n f24180x;

    /* renamed from: y  reason: collision with root package name */
    private PorterDuffColorFilter f24181y;

    /* renamed from: z  reason: collision with root package name */
    private PorterDuffColorFilter f24182z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements n.b {
        a() {
        }

        @Override // x7.n.b
        public void a(o oVar, Matrix matrix, int i8) {
            h.this.f24167d.set(i8 + 4, oVar.e());
            h.this.f24166c[i8] = oVar.f(matrix);
        }

        @Override // x7.n.b
        public void b(o oVar, Matrix matrix, int i8) {
            h.this.f24167d.set(i8, oVar.e());
            h.this.f24165b[i8] = oVar.f(matrix);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements m.c {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ float f24184a;

        b(float f5) {
            this.f24184a = f5;
        }

        @Override // x7.m.c
        public x7.c a(x7.c cVar) {
            return cVar instanceof k ? cVar : new x7.b(this.f24184a, cVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c extends Drawable.ConstantState {

        /* renamed from: a  reason: collision with root package name */
        public m f24186a;

        /* renamed from: b  reason: collision with root package name */
        public q7.a f24187b;

        /* renamed from: c  reason: collision with root package name */
        public ColorFilter f24188c;

        /* renamed from: d  reason: collision with root package name */
        public ColorStateList f24189d;

        /* renamed from: e  reason: collision with root package name */
        public ColorStateList f24190e;

        /* renamed from: f  reason: collision with root package name */
        public ColorStateList f24191f;

        /* renamed from: g  reason: collision with root package name */
        public ColorStateList f24192g;

        /* renamed from: h  reason: collision with root package name */
        public PorterDuff.Mode f24193h;

        /* renamed from: i  reason: collision with root package name */
        public Rect f24194i;

        /* renamed from: j  reason: collision with root package name */
        public float f24195j;

        /* renamed from: k  reason: collision with root package name */
        public float f24196k;

        /* renamed from: l  reason: collision with root package name */
        public float f24197l;

        /* renamed from: m  reason: collision with root package name */
        public int f24198m;

        /* renamed from: n  reason: collision with root package name */
        public float f24199n;

        /* renamed from: o  reason: collision with root package name */
        public float f24200o;

        /* renamed from: p  reason: collision with root package name */
        public float f24201p;
        public int q;

        /* renamed from: r  reason: collision with root package name */
        public int f24202r;

        /* renamed from: s  reason: collision with root package name */
        public int f24203s;

        /* renamed from: t  reason: collision with root package name */
        public int f24204t;

        /* renamed from: u  reason: collision with root package name */
        public boolean f24205u;

        /* renamed from: v  reason: collision with root package name */
        public Paint.Style f24206v;

        public c(c cVar) {
            this.f24189d = null;
            this.f24190e = null;
            this.f24191f = null;
            this.f24192g = null;
            this.f24193h = PorterDuff.Mode.SRC_IN;
            this.f24194i = null;
            this.f24195j = 1.0f;
            this.f24196k = 1.0f;
            this.f24198m = 255;
            this.f24199n = 0.0f;
            this.f24200o = 0.0f;
            this.f24201p = 0.0f;
            this.q = 0;
            this.f24202r = 0;
            this.f24203s = 0;
            this.f24204t = 0;
            this.f24205u = false;
            this.f24206v = Paint.Style.FILL_AND_STROKE;
            this.f24186a = cVar.f24186a;
            this.f24187b = cVar.f24187b;
            this.f24197l = cVar.f24197l;
            this.f24188c = cVar.f24188c;
            this.f24189d = cVar.f24189d;
            this.f24190e = cVar.f24190e;
            this.f24193h = cVar.f24193h;
            this.f24192g = cVar.f24192g;
            this.f24198m = cVar.f24198m;
            this.f24195j = cVar.f24195j;
            this.f24203s = cVar.f24203s;
            this.q = cVar.q;
            this.f24205u = cVar.f24205u;
            this.f24196k = cVar.f24196k;
            this.f24199n = cVar.f24199n;
            this.f24200o = cVar.f24200o;
            this.f24201p = cVar.f24201p;
            this.f24202r = cVar.f24202r;
            this.f24204t = cVar.f24204t;
            this.f24191f = cVar.f24191f;
            this.f24206v = cVar.f24206v;
            if (cVar.f24194i != null) {
                this.f24194i = new Rect(cVar.f24194i);
            }
        }

        public c(m mVar, q7.a aVar) {
            this.f24189d = null;
            this.f24190e = null;
            this.f24191f = null;
            this.f24192g = null;
            this.f24193h = PorterDuff.Mode.SRC_IN;
            this.f24194i = null;
            this.f24195j = 1.0f;
            this.f24196k = 1.0f;
            this.f24198m = 255;
            this.f24199n = 0.0f;
            this.f24200o = 0.0f;
            this.f24201p = 0.0f;
            this.q = 0;
            this.f24202r = 0;
            this.f24203s = 0;
            this.f24204t = 0;
            this.f24205u = false;
            this.f24206v = Paint.Style.FILL_AND_STROKE;
            this.f24186a = mVar;
            this.f24187b = aVar;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            h hVar = new h(this, null);
            hVar.f24168e = true;
            return hVar;
        }
    }

    public h() {
        this(new m());
    }

    public h(Context context, AttributeSet attributeSet, int i8, int i9) {
        this(m.e(context, attributeSet, i8, i9).m());
    }

    private h(c cVar) {
        this.f24165b = new o.g[4];
        this.f24166c = new o.g[4];
        this.f24167d = new BitSet(8);
        this.f24169f = new Matrix();
        this.f24170g = new Path();
        this.f24171h = new Path();
        this.f24172j = new RectF();
        this.f24173k = new RectF();
        this.f24174l = new Region();
        this.f24175m = new Region();
        Paint paint = new Paint(1);
        this.f24177p = paint;
        Paint paint2 = new Paint(1);
        this.q = paint2;
        this.f24178t = new w7.a();
        this.f24180x = Looper.getMainLooper().getThread() == Thread.currentThread() ? n.k() : new n();
        this.A = new RectF();
        this.B = true;
        this.f24164a = cVar;
        paint2.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.FILL);
        Paint paint3 = E;
        paint3.setColor(-1);
        paint3.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        o0();
        n0(getState());
        this.f24179w = new a();
    }

    /* synthetic */ h(c cVar, a aVar) {
        this(cVar);
    }

    public h(m mVar) {
        this(new c(mVar, null));
    }

    private float F() {
        if (O()) {
            return this.q.getStrokeWidth() / 2.0f;
        }
        return 0.0f;
    }

    private boolean M() {
        c cVar = this.f24164a;
        int i8 = cVar.q;
        return i8 != 1 && cVar.f24202r > 0 && (i8 == 2 || W());
    }

    private boolean N() {
        Paint.Style style = this.f24164a.f24206v;
        return style == Paint.Style.FILL_AND_STROKE || style == Paint.Style.FILL;
    }

    private boolean O() {
        Paint.Style style = this.f24164a.f24206v;
        return (style == Paint.Style.FILL_AND_STROKE || style == Paint.Style.STROKE) && this.q.getStrokeWidth() > 0.0f;
    }

    private void Q() {
        super.invalidateSelf();
    }

    private void T(Canvas canvas) {
        if (M()) {
            canvas.save();
            V(canvas);
            if (this.B) {
                int width = (int) (this.A.width() - getBounds().width());
                int height = (int) (this.A.height() - getBounds().height());
                if (width < 0 || height < 0) {
                    throw new IllegalStateException("Invalid shadow bounds. Check that the treatments result in a valid path.");
                }
                Bitmap createBitmap = Bitmap.createBitmap(((int) this.A.width()) + (this.f24164a.f24202r * 2) + width, ((int) this.A.height()) + (this.f24164a.f24202r * 2) + height, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(createBitmap);
                float f5 = (getBounds().left - this.f24164a.f24202r) - width;
                float f8 = (getBounds().top - this.f24164a.f24202r) - height;
                canvas2.translate(-f5, -f8);
                n(canvas2);
                canvas.drawBitmap(createBitmap, f5, f8, (Paint) null);
                createBitmap.recycle();
            } else {
                n(canvas);
            }
            canvas.restore();
        }
    }

    private static int U(int i8, int i9) {
        return (i8 * (i9 + (i9 >>> 7))) >>> 8;
    }

    private void V(Canvas canvas) {
        int A = A();
        int B = B();
        if (Build.VERSION.SDK_INT < 21 && this.B) {
            Rect clipBounds = canvas.getClipBounds();
            int i8 = this.f24164a.f24202r;
            clipBounds.inset(-i8, -i8);
            clipBounds.offset(A, B);
            canvas.clipRect(clipBounds, Region.Op.REPLACE);
        }
        canvas.translate(A, B);
    }

    private PorterDuffColorFilter f(Paint paint, boolean z4) {
        int color;
        int l8;
        if (!z4 || (l8 = l((color = paint.getColor()))) == color) {
            return null;
        }
        return new PorterDuffColorFilter(l8, PorterDuff.Mode.SRC_IN);
    }

    private void g(RectF rectF, Path path) {
        h(rectF, path);
        if (this.f24164a.f24195j != 1.0f) {
            this.f24169f.reset();
            Matrix matrix = this.f24169f;
            float f5 = this.f24164a.f24195j;
            matrix.setScale(f5, f5, rectF.width() / 2.0f, rectF.height() / 2.0f);
            path.transform(this.f24169f);
        }
        path.computeBounds(this.A, true);
    }

    private void i() {
        m y8 = D().y(new b(-F()));
        this.f24176n = y8;
        this.f24180x.d(y8, this.f24164a.f24196k, v(), this.f24171h);
    }

    private PorterDuffColorFilter j(ColorStateList colorStateList, PorterDuff.Mode mode, boolean z4) {
        int colorForState = colorStateList.getColorForState(getState(), 0);
        if (z4) {
            colorForState = l(colorForState);
        }
        return new PorterDuffColorFilter(colorForState, mode);
    }

    private PorterDuffColorFilter k(ColorStateList colorStateList, PorterDuff.Mode mode, Paint paint, boolean z4) {
        return (colorStateList == null || mode == null) ? f(paint, z4) : j(colorStateList, mode, z4);
    }

    public static h m(Context context, float f5) {
        int c9 = n7.a.c(context, k7.b.f21066s, h.class.getSimpleName());
        h hVar = new h();
        hVar.P(context);
        hVar.a0(ColorStateList.valueOf(c9));
        hVar.Z(f5);
        return hVar;
    }

    private void n(Canvas canvas) {
        if (this.f24167d.cardinality() > 0) {
            Log.w(C, "Compatibility shadow requested but can't be drawn for all operations in this shape.");
        }
        if (this.f24164a.f24203s != 0) {
            canvas.drawPath(this.f24170g, this.f24178t.c());
        }
        for (int i8 = 0; i8 < 4; i8++) {
            this.f24165b[i8].b(this.f24178t, this.f24164a.f24202r, canvas);
            this.f24166c[i8].b(this.f24178t, this.f24164a.f24202r, canvas);
        }
        if (this.B) {
            int A = A();
            int B = B();
            canvas.translate(-A, -B);
            canvas.drawPath(this.f24170g, E);
            canvas.translate(A, B);
        }
    }

    private boolean n0(int[] iArr) {
        boolean z4;
        int color;
        int colorForState;
        int color2;
        int colorForState2;
        if (this.f24164a.f24189d == null || color2 == (colorForState2 = this.f24164a.f24189d.getColorForState(iArr, (color2 = this.f24177p.getColor())))) {
            z4 = false;
        } else {
            this.f24177p.setColor(colorForState2);
            z4 = true;
        }
        if (this.f24164a.f24190e == null || color == (colorForState = this.f24164a.f24190e.getColorForState(iArr, (color = this.q.getColor())))) {
            return z4;
        }
        this.q.setColor(colorForState);
        return true;
    }

    private void o(Canvas canvas) {
        q(canvas, this.f24177p, this.f24170g, this.f24164a.f24186a, u());
    }

    private boolean o0() {
        PorterDuffColorFilter porterDuffColorFilter = this.f24181y;
        PorterDuffColorFilter porterDuffColorFilter2 = this.f24182z;
        c cVar = this.f24164a;
        this.f24181y = k(cVar.f24192g, cVar.f24193h, this.f24177p, true);
        c cVar2 = this.f24164a;
        this.f24182z = k(cVar2.f24191f, cVar2.f24193h, this.q, false);
        c cVar3 = this.f24164a;
        if (cVar3.f24205u) {
            this.f24178t.d(cVar3.f24192g.getColorForState(getState(), 0));
        }
        return (androidx.core.util.c.a(porterDuffColorFilter, this.f24181y) && androidx.core.util.c.a(porterDuffColorFilter2, this.f24182z)) ? false : true;
    }

    private void p0() {
        float L = L();
        this.f24164a.f24202r = (int) Math.ceil(0.75f * L);
        this.f24164a.f24203s = (int) Math.ceil(L * 0.25f);
        o0();
        Q();
    }

    private void q(Canvas canvas, Paint paint, Path path, m mVar, RectF rectF) {
        if (!mVar.u(rectF)) {
            canvas.drawPath(path, paint);
            return;
        }
        float a9 = mVar.t().a(rectF) * this.f24164a.f24196k;
        canvas.drawRoundRect(rectF, a9, a9, paint);
    }

    private void r(Canvas canvas) {
        q(canvas, this.q, this.f24171h, this.f24176n, v());
    }

    private RectF v() {
        this.f24173k.set(u());
        float F = F();
        this.f24173k.inset(F, F);
        return this.f24173k;
    }

    public int A() {
        c cVar = this.f24164a;
        return (int) (cVar.f24203s * Math.sin(Math.toRadians(cVar.f24204t)));
    }

    public int B() {
        c cVar = this.f24164a;
        return (int) (cVar.f24203s * Math.cos(Math.toRadians(cVar.f24204t)));
    }

    public int C() {
        return this.f24164a.f24202r;
    }

    public m D() {
        return this.f24164a.f24186a;
    }

    public ColorStateList E() {
        return this.f24164a.f24190e;
    }

    public float G() {
        return this.f24164a.f24197l;
    }

    public ColorStateList H() {
        return this.f24164a.f24192g;
    }

    public float I() {
        return this.f24164a.f24186a.r().a(u());
    }

    public float J() {
        return this.f24164a.f24186a.t().a(u());
    }

    public float K() {
        return this.f24164a.f24201p;
    }

    public float L() {
        return w() + K();
    }

    public void P(Context context) {
        this.f24164a.f24187b = new q7.a(context);
        p0();
    }

    public boolean R() {
        q7.a aVar = this.f24164a.f24187b;
        return aVar != null && aVar.e();
    }

    public boolean S() {
        return this.f24164a.f24186a.u(u());
    }

    public boolean W() {
        int i8 = Build.VERSION.SDK_INT;
        return i8 < 21 || !(S() || this.f24170g.isConvex() || i8 >= 29);
    }

    public void X(float f5) {
        setShapeAppearanceModel(this.f24164a.f24186a.w(f5));
    }

    public void Y(x7.c cVar) {
        setShapeAppearanceModel(this.f24164a.f24186a.x(cVar));
    }

    public void Z(float f5) {
        c cVar = this.f24164a;
        if (cVar.f24200o != f5) {
            cVar.f24200o = f5;
            p0();
        }
    }

    public void a0(ColorStateList colorStateList) {
        c cVar = this.f24164a;
        if (cVar.f24189d != colorStateList) {
            cVar.f24189d = colorStateList;
            onStateChange(getState());
        }
    }

    public void b0(float f5) {
        c cVar = this.f24164a;
        if (cVar.f24196k != f5) {
            cVar.f24196k = f5;
            this.f24168e = true;
            invalidateSelf();
        }
    }

    public void c0(int i8, int i9, int i10, int i11) {
        c cVar = this.f24164a;
        if (cVar.f24194i == null) {
            cVar.f24194i = new Rect();
        }
        this.f24164a.f24194i.set(i8, i9, i10, i11);
        invalidateSelf();
    }

    public void d0(Paint.Style style) {
        this.f24164a.f24206v = style;
        Q();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        this.f24177p.setColorFilter(this.f24181y);
        int alpha = this.f24177p.getAlpha();
        this.f24177p.setAlpha(U(alpha, this.f24164a.f24198m));
        this.q.setColorFilter(this.f24182z);
        this.q.setStrokeWidth(this.f24164a.f24197l);
        int alpha2 = this.q.getAlpha();
        this.q.setAlpha(U(alpha2, this.f24164a.f24198m));
        if (this.f24168e) {
            i();
            g(u(), this.f24170g);
            this.f24168e = false;
        }
        T(canvas);
        if (N()) {
            o(canvas);
        }
        if (O()) {
            r(canvas);
        }
        this.f24177p.setAlpha(alpha);
        this.q.setAlpha(alpha2);
    }

    public void e0(float f5) {
        c cVar = this.f24164a;
        if (cVar.f24199n != f5) {
            cVar.f24199n = f5;
            p0();
        }
    }

    public void f0(boolean z4) {
        this.B = z4;
    }

    public void g0(int i8) {
        this.f24178t.d(i8);
        this.f24164a.f24205u = false;
        Q();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.f24164a;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    @TargetApi(21)
    public void getOutline(Outline outline) {
        if (this.f24164a.q == 2) {
            return;
        }
        if (S()) {
            outline.setRoundRect(getBounds(), I() * this.f24164a.f24196k);
            return;
        }
        g(u(), this.f24170g);
        if (this.f24170g.isConvex() || Build.VERSION.SDK_INT >= 29) {
            try {
                outline.setConvexPath(this.f24170g);
            } catch (IllegalArgumentException unused) {
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        Rect rect2 = this.f24164a.f24194i;
        if (rect2 != null) {
            rect.set(rect2);
            return true;
        }
        return super.getPadding(rect);
    }

    @Override // android.graphics.drawable.Drawable
    public Region getTransparentRegion() {
        this.f24174l.set(getBounds());
        g(u(), this.f24170g);
        this.f24175m.setPath(this.f24170g, this.f24174l);
        this.f24174l.op(this.f24175m, Region.Op.DIFFERENCE);
        return this.f24174l;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void h(RectF rectF, Path path) {
        n nVar = this.f24180x;
        c cVar = this.f24164a;
        nVar.e(cVar.f24186a, cVar.f24196k, rectF, this.f24179w, path);
    }

    public void h0(int i8) {
        c cVar = this.f24164a;
        if (cVar.f24204t != i8) {
            cVar.f24204t = i8;
            Q();
        }
    }

    public void i0(int i8) {
        c cVar = this.f24164a;
        if (cVar.q != i8) {
            cVar.q = i8;
            Q();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        this.f24168e = true;
        super.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        ColorStateList colorStateList3;
        ColorStateList colorStateList4;
        return super.isStateful() || ((colorStateList = this.f24164a.f24192g) != null && colorStateList.isStateful()) || (((colorStateList2 = this.f24164a.f24191f) != null && colorStateList2.isStateful()) || (((colorStateList3 = this.f24164a.f24190e) != null && colorStateList3.isStateful()) || ((colorStateList4 = this.f24164a.f24189d) != null && colorStateList4.isStateful())));
    }

    public void j0(float f5, int i8) {
        m0(f5);
        l0(ColorStateList.valueOf(i8));
    }

    public void k0(float f5, ColorStateList colorStateList) {
        m0(f5);
        l0(colorStateList);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int l(int i8) {
        float L = L() + z();
        q7.a aVar = this.f24164a.f24187b;
        return aVar != null ? aVar.c(i8, L) : i8;
    }

    public void l0(ColorStateList colorStateList) {
        c cVar = this.f24164a;
        if (cVar.f24190e != colorStateList) {
            cVar.f24190e = colorStateList;
            onStateChange(getState());
        }
    }

    public void m0(float f5) {
        this.f24164a.f24197l = f5;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        this.f24164a = new c(this.f24164a);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect rect) {
        this.f24168e = true;
        super.onBoundsChange(rect);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable, com.google.android.material.internal.j.b
    public boolean onStateChange(int[] iArr) {
        boolean z4 = n0(iArr) || o0();
        if (z4) {
            invalidateSelf();
        }
        return z4;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void p(Canvas canvas, Paint paint, Path path, RectF rectF) {
        q(canvas, paint, path, this.f24164a.f24186a, rectF);
    }

    public float s() {
        return this.f24164a.f24186a.j().a(u());
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        c cVar = this.f24164a;
        if (cVar.f24198m != i8) {
            cVar.f24198m = i8;
            Q();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f24164a.f24188c = colorFilter;
        Q();
    }

    @Override // x7.p
    public void setShapeAppearanceModel(m mVar) {
        this.f24164a.f24186a = mVar;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTint(int i8) {
        setTintList(ColorStateList.valueOf(i8));
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintList(ColorStateList colorStateList) {
        this.f24164a.f24192g = colorStateList;
        o0();
        Q();
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintMode(PorterDuff.Mode mode) {
        c cVar = this.f24164a;
        if (cVar.f24193h != mode) {
            cVar.f24193h = mode;
            o0();
            Q();
        }
    }

    public float t() {
        return this.f24164a.f24186a.l().a(u());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RectF u() {
        this.f24172j.set(getBounds());
        return this.f24172j;
    }

    public float w() {
        return this.f24164a.f24200o;
    }

    public ColorStateList x() {
        return this.f24164a.f24189d;
    }

    public float y() {
        return this.f24164a.f24196k;
    }

    public float z() {
        return this.f24164a.f24199n;
    }
}
