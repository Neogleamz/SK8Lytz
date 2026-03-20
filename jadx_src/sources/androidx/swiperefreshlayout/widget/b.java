package androidx.swiperefreshlayout.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.core.util.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b extends Drawable implements Animatable {

    /* renamed from: g  reason: collision with root package name */
    private static final Interpolator f7336g = new LinearInterpolator();

    /* renamed from: h  reason: collision with root package name */
    private static final Interpolator f7337h = new d1.b();

    /* renamed from: j  reason: collision with root package name */
    private static final int[] f7338j = {-16777216};

    /* renamed from: a  reason: collision with root package name */
    private final c f7339a;

    /* renamed from: b  reason: collision with root package name */
    private float f7340b;

    /* renamed from: c  reason: collision with root package name */
    private Resources f7341c;

    /* renamed from: d  reason: collision with root package name */
    private Animator f7342d;

    /* renamed from: e  reason: collision with root package name */
    float f7343e;

    /* renamed from: f  reason: collision with root package name */
    boolean f7344f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ c f7345a;

        a(c cVar) {
            this.f7345a = cVar;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            b.this.n(floatValue, this.f7345a);
            b.this.b(floatValue, this.f7345a, false);
            b.this.invalidateSelf();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.swiperefreshlayout.widget.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0081b implements Animator.AnimatorListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ c f7347a;

        C0081b(c cVar) {
            this.f7347a = cVar;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
            b.this.b(1.0f, this.f7347a, true);
            this.f7347a.A();
            this.f7347a.l();
            b bVar = b.this;
            if (!bVar.f7344f) {
                bVar.f7343e += 1.0f;
                return;
            }
            bVar.f7344f = false;
            animator.cancel();
            animator.setDuration(1332L);
            animator.start();
            this.f7347a.x(false);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            b.this.f7343e = 0.0f;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        final RectF f7349a = new RectF();

        /* renamed from: b  reason: collision with root package name */
        final Paint f7350b;

        /* renamed from: c  reason: collision with root package name */
        final Paint f7351c;

        /* renamed from: d  reason: collision with root package name */
        final Paint f7352d;

        /* renamed from: e  reason: collision with root package name */
        float f7353e;

        /* renamed from: f  reason: collision with root package name */
        float f7354f;

        /* renamed from: g  reason: collision with root package name */
        float f7355g;

        /* renamed from: h  reason: collision with root package name */
        float f7356h;

        /* renamed from: i  reason: collision with root package name */
        int[] f7357i;

        /* renamed from: j  reason: collision with root package name */
        int f7358j;

        /* renamed from: k  reason: collision with root package name */
        float f7359k;

        /* renamed from: l  reason: collision with root package name */
        float f7360l;

        /* renamed from: m  reason: collision with root package name */
        float f7361m;

        /* renamed from: n  reason: collision with root package name */
        boolean f7362n;

        /* renamed from: o  reason: collision with root package name */
        Path f7363o;

        /* renamed from: p  reason: collision with root package name */
        float f7364p;
        float q;

        /* renamed from: r  reason: collision with root package name */
        int f7365r;

        /* renamed from: s  reason: collision with root package name */
        int f7366s;

        /* renamed from: t  reason: collision with root package name */
        int f7367t;

        /* renamed from: u  reason: collision with root package name */
        int f7368u;

        c() {
            Paint paint = new Paint();
            this.f7350b = paint;
            Paint paint2 = new Paint();
            this.f7351c = paint2;
            Paint paint3 = new Paint();
            this.f7352d = paint3;
            this.f7353e = 0.0f;
            this.f7354f = 0.0f;
            this.f7355g = 0.0f;
            this.f7356h = 5.0f;
            this.f7364p = 1.0f;
            this.f7367t = 255;
            paint.setStrokeCap(Paint.Cap.SQUARE);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint2.setStyle(Paint.Style.FILL);
            paint2.setAntiAlias(true);
            paint3.setColor(0);
        }

        void A() {
            this.f7359k = this.f7353e;
            this.f7360l = this.f7354f;
            this.f7361m = this.f7355g;
        }

        void a(Canvas canvas, Rect rect) {
            RectF rectF = this.f7349a;
            float f5 = this.q;
            float f8 = (this.f7356h / 2.0f) + f5;
            if (f5 <= 0.0f) {
                f8 = (Math.min(rect.width(), rect.height()) / 2.0f) - Math.max((this.f7365r * this.f7364p) / 2.0f, this.f7356h / 2.0f);
            }
            rectF.set(rect.centerX() - f8, rect.centerY() - f8, rect.centerX() + f8, rect.centerY() + f8);
            float f9 = this.f7353e;
            float f10 = this.f7355g;
            float f11 = (f9 + f10) * 360.0f;
            float f12 = ((this.f7354f + f10) * 360.0f) - f11;
            this.f7350b.setColor(this.f7368u);
            this.f7350b.setAlpha(this.f7367t);
            float f13 = this.f7356h / 2.0f;
            rectF.inset(f13, f13);
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2.0f, this.f7352d);
            float f14 = -f13;
            rectF.inset(f14, f14);
            canvas.drawArc(rectF, f11, f12, false, this.f7350b);
            b(canvas, f11, f12, rectF);
        }

        void b(Canvas canvas, float f5, float f8, RectF rectF) {
            if (this.f7362n) {
                Path path = this.f7363o;
                if (path == null) {
                    Path path2 = new Path();
                    this.f7363o = path2;
                    path2.setFillType(Path.FillType.EVEN_ODD);
                } else {
                    path.reset();
                }
                this.f7363o.moveTo(0.0f, 0.0f);
                this.f7363o.lineTo(this.f7365r * this.f7364p, 0.0f);
                Path path3 = this.f7363o;
                float f9 = this.f7364p;
                path3.lineTo((this.f7365r * f9) / 2.0f, this.f7366s * f9);
                this.f7363o.offset(((Math.min(rectF.width(), rectF.height()) / 2.0f) + rectF.centerX()) - ((this.f7365r * this.f7364p) / 2.0f), rectF.centerY() + (this.f7356h / 2.0f));
                this.f7363o.close();
                this.f7351c.setColor(this.f7368u);
                this.f7351c.setAlpha(this.f7367t);
                canvas.save();
                canvas.rotate(f5 + f8, rectF.centerX(), rectF.centerY());
                canvas.drawPath(this.f7363o, this.f7351c);
                canvas.restore();
            }
        }

        int c() {
            return this.f7367t;
        }

        float d() {
            return this.f7354f;
        }

        int e() {
            return this.f7357i[f()];
        }

        int f() {
            return (this.f7358j + 1) % this.f7357i.length;
        }

        float g() {
            return this.f7353e;
        }

        int h() {
            return this.f7357i[this.f7358j];
        }

        float i() {
            return this.f7360l;
        }

        float j() {
            return this.f7361m;
        }

        float k() {
            return this.f7359k;
        }

        void l() {
            t(f());
        }

        void m() {
            this.f7359k = 0.0f;
            this.f7360l = 0.0f;
            this.f7361m = 0.0f;
            y(0.0f);
            v(0.0f);
            w(0.0f);
        }

        void n(int i8) {
            this.f7367t = i8;
        }

        void o(float f5, float f8) {
            this.f7365r = (int) f5;
            this.f7366s = (int) f8;
        }

        void p(float f5) {
            if (f5 != this.f7364p) {
                this.f7364p = f5;
            }
        }

        void q(float f5) {
            this.q = f5;
        }

        void r(int i8) {
            this.f7368u = i8;
        }

        void s(ColorFilter colorFilter) {
            this.f7350b.setColorFilter(colorFilter);
        }

        void t(int i8) {
            this.f7358j = i8;
            this.f7368u = this.f7357i[i8];
        }

        void u(int[] iArr) {
            this.f7357i = iArr;
            t(0);
        }

        void v(float f5) {
            this.f7354f = f5;
        }

        void w(float f5) {
            this.f7355g = f5;
        }

        void x(boolean z4) {
            if (this.f7362n != z4) {
                this.f7362n = z4;
            }
        }

        void y(float f5) {
            this.f7353e = f5;
        }

        void z(float f5) {
            this.f7356h = f5;
            this.f7350b.setStrokeWidth(f5);
        }
    }

    public b(Context context) {
        this.f7341c = ((Context) h.h(context)).getResources();
        c cVar = new c();
        this.f7339a = cVar;
        cVar.u(f7338j);
        k(2.5f);
        m();
    }

    private void a(float f5, c cVar) {
        n(f5, cVar);
        cVar.y(cVar.k() + (((cVar.i() - 0.01f) - cVar.k()) * f5));
        cVar.v(cVar.i());
        cVar.w(cVar.j() + ((((float) (Math.floor(cVar.j() / 0.8f) + 1.0d)) - cVar.j()) * f5));
    }

    private int c(float f5, int i8, int i9) {
        int i10 = (i8 >> 24) & 255;
        int i11 = (i8 >> 16) & 255;
        int i12 = (i8 >> 8) & 255;
        int i13 = i8 & 255;
        return ((i10 + ((int) ((((i9 >> 24) & 255) - i10) * f5))) << 24) | ((i11 + ((int) ((((i9 >> 16) & 255) - i11) * f5))) << 16) | ((i12 + ((int) ((((i9 >> 8) & 255) - i12) * f5))) << 8) | (i13 + ((int) (f5 * ((i9 & 255) - i13))));
    }

    private void h(float f5) {
        this.f7340b = f5;
    }

    private void i(float f5, float f8, float f9, float f10) {
        c cVar = this.f7339a;
        float f11 = this.f7341c.getDisplayMetrics().density;
        cVar.z(f8 * f11);
        cVar.q(f5 * f11);
        cVar.t(0);
        cVar.o(f9 * f11, f10 * f11);
    }

    private void m() {
        c cVar = this.f7339a;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new a(cVar));
        ofFloat.setRepeatCount(-1);
        ofFloat.setRepeatMode(1);
        ofFloat.setInterpolator(f7336g);
        ofFloat.addListener(new C0081b(cVar));
        this.f7342d = ofFloat;
    }

    void b(float f5, c cVar, boolean z4) {
        float interpolation;
        float f8;
        if (this.f7344f) {
            a(f5, cVar);
        } else if (f5 != 1.0f || z4) {
            float j8 = cVar.j();
            if (f5 < 0.5f) {
                interpolation = cVar.k();
                f8 = (f7337h.getInterpolation(f5 / 0.5f) * 0.79f) + 0.01f + interpolation;
            } else {
                float k8 = cVar.k() + 0.79f;
                interpolation = k8 - (((1.0f - f7337h.getInterpolation((f5 - 0.5f) / 0.5f)) * 0.79f) + 0.01f);
                f8 = k8;
            }
            cVar.y(interpolation);
            cVar.v(f8);
            cVar.w(j8 + (0.20999998f * f5));
            h((f5 + this.f7343e) * 216.0f);
        }
    }

    public void d(boolean z4) {
        this.f7339a.x(z4);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.save();
        canvas.rotate(this.f7340b, bounds.exactCenterX(), bounds.exactCenterY());
        this.f7339a.a(canvas, bounds);
        canvas.restore();
    }

    public void e(float f5) {
        this.f7339a.p(f5);
        invalidateSelf();
    }

    public void f(int... iArr) {
        this.f7339a.u(iArr);
        this.f7339a.t(0);
        invalidateSelf();
    }

    public void g(float f5) {
        this.f7339a.w(f5);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f7339a.c();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Animatable
    public boolean isRunning() {
        return this.f7342d.isRunning();
    }

    public void j(float f5, float f8) {
        this.f7339a.y(f5);
        this.f7339a.v(f8);
        invalidateSelf();
    }

    public void k(float f5) {
        this.f7339a.z(f5);
        invalidateSelf();
    }

    public void l(int i8) {
        float f5;
        float f8;
        float f9;
        float f10;
        if (i8 == 0) {
            f5 = 11.0f;
            f8 = 3.0f;
            f9 = 12.0f;
            f10 = 6.0f;
        } else {
            f5 = 7.5f;
            f8 = 2.5f;
            f9 = 10.0f;
            f10 = 5.0f;
        }
        i(f5, f8, f9, f10);
        invalidateSelf();
    }

    void n(float f5, c cVar) {
        cVar.r(f5 > 0.75f ? c((f5 - 0.75f) / 0.25f, cVar.h(), cVar.e()) : cVar.h());
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        this.f7339a.n(i8);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.f7339a.s(colorFilter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Animatable
    public void start() {
        Animator animator;
        long j8;
        this.f7342d.cancel();
        this.f7339a.A();
        if (this.f7339a.d() != this.f7339a.g()) {
            this.f7344f = true;
            animator = this.f7342d;
            j8 = 666;
        } else {
            this.f7339a.t(0);
            this.f7339a.m();
            animator = this.f7342d;
            j8 = 1332;
        }
        animator.setDuration(j8);
        this.f7342d.start();
    }

    @Override // android.graphics.drawable.Animatable
    public void stop() {
        this.f7342d.cancel();
        h(0.0f);
        this.f7339a.x(false);
        this.f7339a.t(0);
        this.f7339a.m();
        invalidateSelf();
    }
}
