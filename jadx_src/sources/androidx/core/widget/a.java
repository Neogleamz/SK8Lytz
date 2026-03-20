package androidx.core.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import androidx.core.view.c0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a implements View.OnTouchListener {

    /* renamed from: x  reason: collision with root package name */
    private static final int f5116x = ViewConfiguration.getTapTimeout();

    /* renamed from: c  reason: collision with root package name */
    final View f5119c;

    /* renamed from: d  reason: collision with root package name */
    private Runnable f5120d;

    /* renamed from: g  reason: collision with root package name */
    private int f5123g;

    /* renamed from: h  reason: collision with root package name */
    private int f5124h;

    /* renamed from: m  reason: collision with root package name */
    private boolean f5128m;

    /* renamed from: n  reason: collision with root package name */
    boolean f5129n;

    /* renamed from: p  reason: collision with root package name */
    boolean f5130p;
    boolean q;

    /* renamed from: t  reason: collision with root package name */
    private boolean f5131t;

    /* renamed from: w  reason: collision with root package name */
    private boolean f5132w;

    /* renamed from: a  reason: collision with root package name */
    final C0047a f5117a = new C0047a();

    /* renamed from: b  reason: collision with root package name */
    private final Interpolator f5118b = new AccelerateInterpolator();

    /* renamed from: e  reason: collision with root package name */
    private float[] f5121e = {0.0f, 0.0f};

    /* renamed from: f  reason: collision with root package name */
    private float[] f5122f = {Float.MAX_VALUE, Float.MAX_VALUE};

    /* renamed from: j  reason: collision with root package name */
    private float[] f5125j = {0.0f, 0.0f};

    /* renamed from: k  reason: collision with root package name */
    private float[] f5126k = {0.0f, 0.0f};

    /* renamed from: l  reason: collision with root package name */
    private float[] f5127l = {Float.MAX_VALUE, Float.MAX_VALUE};

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: androidx.core.widget.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class C0047a {

        /* renamed from: a  reason: collision with root package name */
        private int f5133a;

        /* renamed from: b  reason: collision with root package name */
        private int f5134b;

        /* renamed from: c  reason: collision with root package name */
        private float f5135c;

        /* renamed from: d  reason: collision with root package name */
        private float f5136d;

        /* renamed from: j  reason: collision with root package name */
        private float f5142j;

        /* renamed from: k  reason: collision with root package name */
        private int f5143k;

        /* renamed from: e  reason: collision with root package name */
        private long f5137e = Long.MIN_VALUE;

        /* renamed from: i  reason: collision with root package name */
        private long f5141i = -1;

        /* renamed from: f  reason: collision with root package name */
        private long f5138f = 0;

        /* renamed from: g  reason: collision with root package name */
        private int f5139g = 0;

        /* renamed from: h  reason: collision with root package name */
        private int f5140h = 0;

        C0047a() {
        }

        private float e(long j8) {
            long j9 = this.f5137e;
            if (j8 < j9) {
                return 0.0f;
            }
            long j10 = this.f5141i;
            if (j10 < 0 || j8 < j10) {
                return a.e(((float) (j8 - j9)) / this.f5133a, 0.0f, 1.0f) * 0.5f;
            }
            float f5 = this.f5142j;
            return (1.0f - f5) + (f5 * a.e(((float) (j8 - j10)) / this.f5143k, 0.0f, 1.0f));
        }

        private float g(float f5) {
            return ((-4.0f) * f5 * f5) + (f5 * 4.0f);
        }

        public void a() {
            if (this.f5138f == 0) {
                throw new RuntimeException("Cannot compute scroll delta before calling start()");
            }
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            float g8 = g(e(currentAnimationTimeMillis));
            this.f5138f = currentAnimationTimeMillis;
            float f5 = ((float) (currentAnimationTimeMillis - this.f5138f)) * g8;
            this.f5139g = (int) (this.f5135c * f5);
            this.f5140h = (int) (f5 * this.f5136d);
        }

        public int b() {
            return this.f5139g;
        }

        public int c() {
            return this.f5140h;
        }

        public int d() {
            float f5 = this.f5135c;
            return (int) (f5 / Math.abs(f5));
        }

        public int f() {
            float f5 = this.f5136d;
            return (int) (f5 / Math.abs(f5));
        }

        public boolean h() {
            return this.f5141i > 0 && AnimationUtils.currentAnimationTimeMillis() > this.f5141i + ((long) this.f5143k);
        }

        public void i() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.f5143k = a.f((int) (currentAnimationTimeMillis - this.f5137e), 0, this.f5134b);
            this.f5142j = e(currentAnimationTimeMillis);
            this.f5141i = currentAnimationTimeMillis;
        }

        public void j(int i8) {
            this.f5134b = i8;
        }

        public void k(int i8) {
            this.f5133a = i8;
        }

        public void l(float f5, float f8) {
            this.f5135c = f5;
            this.f5136d = f8;
        }

        public void m() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.f5137e = currentAnimationTimeMillis;
            this.f5141i = -1L;
            this.f5138f = currentAnimationTimeMillis;
            this.f5142j = 0.5f;
            this.f5139g = 0;
            this.f5140h = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            a aVar = a.this;
            if (aVar.q) {
                if (aVar.f5129n) {
                    aVar.f5129n = false;
                    aVar.f5117a.m();
                }
                C0047a c0047a = a.this.f5117a;
                if (c0047a.h() || !a.this.u()) {
                    a.this.q = false;
                    return;
                }
                a aVar2 = a.this;
                if (aVar2.f5130p) {
                    aVar2.f5130p = false;
                    aVar2.c();
                }
                c0047a.a();
                a.this.j(c0047a.b(), c0047a.c());
                c0.l0(a.this.f5119c, this);
            }
        }
    }

    public a(View view) {
        this.f5119c = view;
        float f5 = Resources.getSystem().getDisplayMetrics().density;
        float f8 = (int) ((1575.0f * f5) + 0.5f);
        o(f8, f8);
        float f9 = (int) ((f5 * 315.0f) + 0.5f);
        p(f9, f9);
        l(1);
        n(Float.MAX_VALUE, Float.MAX_VALUE);
        s(0.2f, 0.2f);
        t(1.0f, 1.0f);
        k(f5116x);
        r(500);
        q(500);
    }

    private float d(int i8, float f5, float f8, float f9) {
        float h8 = h(this.f5121e[i8], f8, this.f5122f[i8], f5);
        int i9 = (h8 > 0.0f ? 1 : (h8 == 0.0f ? 0 : -1));
        if (i9 == 0) {
            return 0.0f;
        }
        float f10 = this.f5125j[i8];
        float f11 = this.f5126k[i8];
        float f12 = this.f5127l[i8];
        float f13 = f10 * f9;
        return i9 > 0 ? e(h8 * f13, f11, f12) : -e((-h8) * f13, f11, f12);
    }

    static float e(float f5, float f8, float f9) {
        return f5 > f9 ? f9 : f5 < f8 ? f8 : f5;
    }

    static int f(int i8, int i9, int i10) {
        return i8 > i10 ? i10 : i8 < i9 ? i9 : i8;
    }

    private float g(float f5, float f8) {
        if (f8 == 0.0f) {
            return 0.0f;
        }
        int i8 = this.f5123g;
        if (i8 == 0 || i8 == 1) {
            if (f5 < f8) {
                if (f5 >= 0.0f) {
                    return 1.0f - (f5 / f8);
                }
                if (this.q && i8 == 1) {
                    return 1.0f;
                }
            }
        } else if (i8 == 2 && f5 < 0.0f) {
            return f5 / (-f8);
        }
        return 0.0f;
    }

    private float h(float f5, float f8, float f9, float f10) {
        float interpolation;
        float e8 = e(f5 * f8, 0.0f, f9);
        float g8 = g(f8 - f10, e8) - g(f10, e8);
        if (g8 < 0.0f) {
            interpolation = -this.f5118b.getInterpolation(-g8);
        } else if (g8 <= 0.0f) {
            return 0.0f;
        } else {
            interpolation = this.f5118b.getInterpolation(g8);
        }
        return e(interpolation, -1.0f, 1.0f);
    }

    private void i() {
        if (this.f5129n) {
            this.q = false;
        } else {
            this.f5117a.i();
        }
    }

    private void v() {
        int i8;
        if (this.f5120d == null) {
            this.f5120d = new b();
        }
        this.q = true;
        this.f5129n = true;
        if (this.f5128m || (i8 = this.f5124h) <= 0) {
            this.f5120d.run();
        } else {
            c0.m0(this.f5119c, this.f5120d, i8);
        }
        this.f5128m = true;
    }

    public abstract boolean a(int i8);

    public abstract boolean b(int i8);

    void c() {
        long uptimeMillis = SystemClock.uptimeMillis();
        MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        this.f5119c.onTouchEvent(obtain);
        obtain.recycle();
    }

    public abstract void j(int i8, int i9);

    public a k(int i8) {
        this.f5124h = i8;
        return this;
    }

    public a l(int i8) {
        this.f5123g = i8;
        return this;
    }

    public a m(boolean z4) {
        if (this.f5131t && !z4) {
            i();
        }
        this.f5131t = z4;
        return this;
    }

    public a n(float f5, float f8) {
        float[] fArr = this.f5122f;
        fArr[0] = f5;
        fArr[1] = f8;
        return this;
    }

    public a o(float f5, float f8) {
        float[] fArr = this.f5127l;
        fArr[0] = f5 / 1000.0f;
        fArr[1] = f8 / 1000.0f;
        return this;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0013, code lost:
        if (r0 != 3) goto L12;
     */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouch(android.view.View r6, android.view.MotionEvent r7) {
        /*
            r5 = this;
            boolean r0 = r5.f5131t
            r1 = 0
            if (r0 != 0) goto L6
            return r1
        L6:
            int r0 = r7.getActionMasked()
            r2 = 1
            if (r0 == 0) goto L1a
            if (r0 == r2) goto L16
            r3 = 2
            if (r0 == r3) goto L1e
            r6 = 3
            if (r0 == r6) goto L16
            goto L58
        L16:
            r5.i()
            goto L58
        L1a:
            r5.f5130p = r2
            r5.f5128m = r1
        L1e:
            float r0 = r7.getX()
            int r3 = r6.getWidth()
            float r3 = (float) r3
            android.view.View r4 = r5.f5119c
            int r4 = r4.getWidth()
            float r4 = (float) r4
            float r0 = r5.d(r1, r0, r3, r4)
            float r7 = r7.getY()
            int r6 = r6.getHeight()
            float r6 = (float) r6
            android.view.View r3 = r5.f5119c
            int r3 = r3.getHeight()
            float r3 = (float) r3
            float r6 = r5.d(r2, r7, r6, r3)
            androidx.core.widget.a$a r7 = r5.f5117a
            r7.l(r0, r6)
            boolean r6 = r5.q
            if (r6 != 0) goto L58
            boolean r6 = r5.u()
            if (r6 == 0) goto L58
            r5.v()
        L58:
            boolean r6 = r5.f5132w
            if (r6 == 0) goto L61
            boolean r6 = r5.q
            if (r6 == 0) goto L61
            r1 = r2
        L61:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.widget.a.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    public a p(float f5, float f8) {
        float[] fArr = this.f5126k;
        fArr[0] = f5 / 1000.0f;
        fArr[1] = f8 / 1000.0f;
        return this;
    }

    public a q(int i8) {
        this.f5117a.j(i8);
        return this;
    }

    public a r(int i8) {
        this.f5117a.k(i8);
        return this;
    }

    public a s(float f5, float f8) {
        float[] fArr = this.f5121e;
        fArr[0] = f5;
        fArr[1] = f8;
        return this;
    }

    public a t(float f5, float f8) {
        float[] fArr = this.f5125j;
        fArr[0] = f5 / 1000.0f;
        fArr[1] = f8 / 1000.0f;
        return this;
    }

    boolean u() {
        C0047a c0047a = this.f5117a;
        int f5 = c0047a.f();
        int d8 = c0047a.d();
        return (f5 != 0 && b(f5)) || (d8 != 0 && a(d8));
    }
}
