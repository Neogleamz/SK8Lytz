package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import androidx.constraintlayout.motion.widget.q;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MotionLayout extends ConstraintLayout implements androidx.core.view.s {

    /* renamed from: b1  reason: collision with root package name */
    public static boolean f3162b1;
    private boolean A0;
    private ArrayList<MotionHelper> B0;
    private ArrayList<MotionHelper> C0;
    private ArrayList<i> D0;
    q E;
    private int E0;
    Interpolator F;
    private long F0;
    float G;
    private float G0;
    private int H;
    private int H0;
    private float I0;
    boolean J0;
    int K;
    protected boolean K0;
    private int L;
    int L0;
    int M0;
    int N0;
    private int O;
    int O0;
    private int P;
    int P0;
    private boolean Q;
    int Q0;
    HashMap<View, n> R;
    float R0;
    private androidx.constraintlayout.motion.widget.e S0;
    private long T;
    private boolean T0;
    private h U0;
    j V0;
    private float W;
    e W0;
    private boolean X0;
    private RectF Y0;
    private View Z0;

    /* renamed from: a0  reason: collision with root package name */
    float f3163a0;

    /* renamed from: a1  reason: collision with root package name */
    ArrayList<Integer> f3164a1;

    /* renamed from: b0  reason: collision with root package name */
    float f3165b0;

    /* renamed from: c0  reason: collision with root package name */
    private long f3166c0;

    /* renamed from: d0  reason: collision with root package name */
    float f3167d0;

    /* renamed from: e0  reason: collision with root package name */
    private boolean f3168e0;

    /* renamed from: f0  reason: collision with root package name */
    boolean f3169f0;

    /* renamed from: g0  reason: collision with root package name */
    boolean f3170g0;

    /* renamed from: h0  reason: collision with root package name */
    private i f3171h0;

    /* renamed from: i0  reason: collision with root package name */
    private float f3172i0;

    /* renamed from: j0  reason: collision with root package name */
    private float f3173j0;

    /* renamed from: k0  reason: collision with root package name */
    int f3174k0;

    /* renamed from: l0  reason: collision with root package name */
    d f3175l0;

    /* renamed from: m0  reason: collision with root package name */
    private boolean f3176m0;

    /* renamed from: n0  reason: collision with root package name */
    private l0.g f3177n0;

    /* renamed from: o0  reason: collision with root package name */
    private c f3178o0;

    /* renamed from: p0  reason: collision with root package name */
    private androidx.constraintlayout.motion.widget.b f3179p0;

    /* renamed from: q0  reason: collision with root package name */
    boolean f3180q0;

    /* renamed from: r0  reason: collision with root package name */
    int f3181r0;

    /* renamed from: s0  reason: collision with root package name */
    int f3182s0;

    /* renamed from: t0  reason: collision with root package name */
    int f3183t0;

    /* renamed from: u0  reason: collision with root package name */
    int f3184u0;

    /* renamed from: v0  reason: collision with root package name */
    boolean f3185v0;

    /* renamed from: w0  reason: collision with root package name */
    float f3186w0;

    /* renamed from: x0  reason: collision with root package name */
    float f3187x0;

    /* renamed from: y0  reason: collision with root package name */
    long f3188y0;

    /* renamed from: z0  reason: collision with root package name */
    float f3189z0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f3190a;

        a(View view) {
            this.f3190a = view;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f3190a.setNestedScrollingEnabled(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class b {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f3192a;

        static {
            int[] iArr = new int[j.values().length];
            f3192a = iArr;
            try {
                iArr[j.UNDEFINED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3192a[j.SETUP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3192a[j.MOVING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3192a[j.FINISHED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends o {

        /* renamed from: a  reason: collision with root package name */
        float f3193a = 0.0f;

        /* renamed from: b  reason: collision with root package name */
        float f3194b = 0.0f;

        /* renamed from: c  reason: collision with root package name */
        float f3195c;

        c() {
        }

        @Override // androidx.constraintlayout.motion.widget.o
        public float a() {
            return MotionLayout.this.G;
        }

        public void b(float f5, float f8, float f9) {
            this.f3193a = f5;
            this.f3194b = f8;
            this.f3195c = f9;
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f5) {
            float f8;
            float f9 = this.f3193a;
            if (f9 > 0.0f) {
                float f10 = this.f3195c;
                if (f9 / f10 < f5) {
                    f5 = f9 / f10;
                }
                MotionLayout.this.G = f9 - (f10 * f5);
                f8 = (f9 * f5) - (((f10 * f5) * f5) / 2.0f);
            } else {
                float f11 = this.f3195c;
                if ((-f9) / f11 < f5) {
                    f5 = (-f9) / f11;
                }
                MotionLayout.this.G = (f11 * f5) + f9;
                f8 = (f9 * f5) + (((f11 * f5) * f5) / 2.0f);
            }
            return f8 + this.f3194b;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class d {

        /* renamed from: a  reason: collision with root package name */
        float[] f3197a;

        /* renamed from: b  reason: collision with root package name */
        int[] f3198b;

        /* renamed from: c  reason: collision with root package name */
        float[] f3199c;

        /* renamed from: d  reason: collision with root package name */
        Path f3200d;

        /* renamed from: e  reason: collision with root package name */
        Paint f3201e;

        /* renamed from: f  reason: collision with root package name */
        Paint f3202f;

        /* renamed from: g  reason: collision with root package name */
        Paint f3203g;

        /* renamed from: h  reason: collision with root package name */
        Paint f3204h;

        /* renamed from: i  reason: collision with root package name */
        Paint f3205i;

        /* renamed from: j  reason: collision with root package name */
        private float[] f3206j;

        /* renamed from: p  reason: collision with root package name */
        DashPathEffect f3212p;
        int q;

        /* renamed from: t  reason: collision with root package name */
        int f3215t;

        /* renamed from: k  reason: collision with root package name */
        final int f3207k = -21965;

        /* renamed from: l  reason: collision with root package name */
        final int f3208l = -2067046;

        /* renamed from: m  reason: collision with root package name */
        final int f3209m = -13391360;

        /* renamed from: n  reason: collision with root package name */
        final int f3210n = 1996488704;

        /* renamed from: o  reason: collision with root package name */
        final int f3211o = 10;

        /* renamed from: r  reason: collision with root package name */
        Rect f3213r = new Rect();

        /* renamed from: s  reason: collision with root package name */
        boolean f3214s = false;

        public d() {
            this.f3215t = 1;
            Paint paint = new Paint();
            this.f3201e = paint;
            paint.setAntiAlias(true);
            this.f3201e.setColor(-21965);
            this.f3201e.setStrokeWidth(2.0f);
            this.f3201e.setStyle(Paint.Style.STROKE);
            Paint paint2 = new Paint();
            this.f3202f = paint2;
            paint2.setAntiAlias(true);
            this.f3202f.setColor(-2067046);
            this.f3202f.setStrokeWidth(2.0f);
            this.f3202f.setStyle(Paint.Style.STROKE);
            Paint paint3 = new Paint();
            this.f3203g = paint3;
            paint3.setAntiAlias(true);
            this.f3203g.setColor(-13391360);
            this.f3203g.setStrokeWidth(2.0f);
            this.f3203g.setStyle(Paint.Style.STROKE);
            Paint paint4 = new Paint();
            this.f3204h = paint4;
            paint4.setAntiAlias(true);
            this.f3204h.setColor(-13391360);
            this.f3204h.setTextSize(MotionLayout.this.getContext().getResources().getDisplayMetrics().density * 12.0f);
            this.f3206j = new float[8];
            Paint paint5 = new Paint();
            this.f3205i = paint5;
            paint5.setAntiAlias(true);
            DashPathEffect dashPathEffect = new DashPathEffect(new float[]{4.0f, 8.0f}, 0.0f);
            this.f3212p = dashPathEffect;
            this.f3203g.setPathEffect(dashPathEffect);
            this.f3199c = new float[100];
            this.f3198b = new int[50];
            if (this.f3214s) {
                this.f3201e.setStrokeWidth(8.0f);
                this.f3205i.setStrokeWidth(8.0f);
                this.f3202f.setStrokeWidth(8.0f);
                this.f3215t = 4;
            }
        }

        private void c(Canvas canvas) {
            canvas.drawLines(this.f3197a, this.f3201e);
        }

        private void d(Canvas canvas) {
            boolean z4 = false;
            boolean z8 = false;
            for (int i8 = 0; i8 < this.q; i8++) {
                int[] iArr = this.f3198b;
                if (iArr[i8] == 1) {
                    z4 = true;
                }
                if (iArr[i8] == 2) {
                    z8 = true;
                }
            }
            if (z4) {
                g(canvas);
            }
            if (z8) {
                e(canvas);
            }
        }

        private void e(Canvas canvas) {
            float[] fArr = this.f3197a;
            float f5 = fArr[0];
            float f8 = fArr[1];
            float f9 = fArr[fArr.length - 2];
            float f10 = fArr[fArr.length - 1];
            canvas.drawLine(Math.min(f5, f9), Math.max(f8, f10), Math.max(f5, f9), Math.max(f8, f10), this.f3203g);
            canvas.drawLine(Math.min(f5, f9), Math.min(f8, f10), Math.min(f5, f9), Math.max(f8, f10), this.f3203g);
        }

        private void f(Canvas canvas, float f5, float f8) {
            float[] fArr = this.f3197a;
            float f9 = fArr[0];
            float f10 = fArr[1];
            float f11 = fArr[fArr.length - 2];
            float f12 = fArr[fArr.length - 1];
            float min = Math.min(f9, f11);
            float max = Math.max(f10, f12);
            float min2 = f5 - Math.min(f9, f11);
            float max2 = Math.max(f10, f12) - f8;
            String str = BuildConfig.FLAVOR + (((int) (((min2 * 100.0f) / Math.abs(f11 - f9)) + 0.5d)) / 100.0f);
            l(str, this.f3204h);
            canvas.drawText(str, ((min2 / 2.0f) - (this.f3213r.width() / 2)) + min, f8 - 20.0f, this.f3204h);
            canvas.drawLine(f5, f8, Math.min(f9, f11), f8, this.f3203g);
            String str2 = BuildConfig.FLAVOR + (((int) (((max2 * 100.0f) / Math.abs(f12 - f10)) + 0.5d)) / 100.0f);
            l(str2, this.f3204h);
            canvas.drawText(str2, f5 + 5.0f, max - ((max2 / 2.0f) - (this.f3213r.height() / 2)), this.f3204h);
            canvas.drawLine(f5, f8, f5, Math.max(f10, f12), this.f3203g);
        }

        private void g(Canvas canvas) {
            float[] fArr = this.f3197a;
            canvas.drawLine(fArr[0], fArr[1], fArr[fArr.length - 2], fArr[fArr.length - 1], this.f3203g);
        }

        private void h(Canvas canvas, float f5, float f8) {
            float[] fArr = this.f3197a;
            float f9 = fArr[0];
            float f10 = fArr[1];
            float f11 = fArr[fArr.length - 2];
            float f12 = fArr[fArr.length - 1];
            float hypot = (float) Math.hypot(f9 - f11, f10 - f12);
            float f13 = f11 - f9;
            float f14 = f12 - f10;
            float f15 = (((f5 - f9) * f13) + ((f8 - f10) * f14)) / (hypot * hypot);
            float f16 = f9 + (f13 * f15);
            float f17 = f10 + (f15 * f14);
            Path path = new Path();
            path.moveTo(f5, f8);
            path.lineTo(f16, f17);
            float hypot2 = (float) Math.hypot(f16 - f5, f17 - f8);
            String str = BuildConfig.FLAVOR + (((int) ((hypot2 * 100.0f) / hypot)) / 100.0f);
            l(str, this.f3204h);
            canvas.drawTextOnPath(str, path, (hypot2 / 2.0f) - (this.f3213r.width() / 2), -20.0f, this.f3204h);
            canvas.drawLine(f5, f8, f16, f17, this.f3203g);
        }

        private void i(Canvas canvas, float f5, float f8, int i8, int i9) {
            String str = BuildConfig.FLAVOR + (((int) ((((f5 - (i8 / 2)) * 100.0f) / (MotionLayout.this.getWidth() - i8)) + 0.5d)) / 100.0f);
            l(str, this.f3204h);
            canvas.drawText(str, ((f5 / 2.0f) - (this.f3213r.width() / 2)) + 0.0f, f8 - 20.0f, this.f3204h);
            canvas.drawLine(f5, f8, Math.min(0.0f, 1.0f), f8, this.f3203g);
            String str2 = BuildConfig.FLAVOR + (((int) ((((f8 - (i9 / 2)) * 100.0f) / (MotionLayout.this.getHeight() - i9)) + 0.5d)) / 100.0f);
            l(str2, this.f3204h);
            canvas.drawText(str2, f5 + 5.0f, 0.0f - ((f8 / 2.0f) - (this.f3213r.height() / 2)), this.f3204h);
            canvas.drawLine(f5, f8, f5, Math.max(0.0f, 1.0f), this.f3203g);
        }

        private void j(Canvas canvas, n nVar) {
            this.f3200d.reset();
            for (int i8 = 0; i8 <= 50; i8++) {
                nVar.e(i8 / 50, this.f3206j, 0);
                Path path = this.f3200d;
                float[] fArr = this.f3206j;
                path.moveTo(fArr[0], fArr[1]);
                Path path2 = this.f3200d;
                float[] fArr2 = this.f3206j;
                path2.lineTo(fArr2[2], fArr2[3]);
                Path path3 = this.f3200d;
                float[] fArr3 = this.f3206j;
                path3.lineTo(fArr3[4], fArr3[5]);
                Path path4 = this.f3200d;
                float[] fArr4 = this.f3206j;
                path4.lineTo(fArr4[6], fArr4[7]);
                this.f3200d.close();
            }
            this.f3201e.setColor(1140850688);
            canvas.translate(2.0f, 2.0f);
            canvas.drawPath(this.f3200d, this.f3201e);
            canvas.translate(-2.0f, -2.0f);
            this.f3201e.setColor(-65536);
            canvas.drawPath(this.f3200d, this.f3201e);
        }

        private void k(Canvas canvas, int i8, int i9, n nVar) {
            int i10;
            int i11;
            int i12;
            float f5;
            float f8;
            View view = nVar.f3387a;
            if (view != null) {
                i10 = view.getWidth();
                i11 = nVar.f3387a.getHeight();
            } else {
                i10 = 0;
                i11 = 0;
            }
            for (int i13 = 1; i13 < i9 - 1; i13++) {
                if (i8 != 4 || this.f3198b[i13 - 1] != 0) {
                    float[] fArr = this.f3199c;
                    int i14 = i13 * 2;
                    float f9 = fArr[i14];
                    float f10 = fArr[i14 + 1];
                    this.f3200d.reset();
                    this.f3200d.moveTo(f9, f10 + 10.0f);
                    this.f3200d.lineTo(f9 + 10.0f, f10);
                    this.f3200d.lineTo(f9, f10 - 10.0f);
                    this.f3200d.lineTo(f9 - 10.0f, f10);
                    this.f3200d.close();
                    int i15 = i13 - 1;
                    nVar.k(i15);
                    if (i8 == 4) {
                        int[] iArr = this.f3198b;
                        if (iArr[i15] == 1) {
                            h(canvas, f9 - 0.0f, f10 - 0.0f);
                        } else if (iArr[i15] == 2) {
                            f(canvas, f9 - 0.0f, f10 - 0.0f);
                        } else if (iArr[i15] == 3) {
                            i12 = 3;
                            f5 = f10;
                            f8 = f9;
                            i(canvas, f9 - 0.0f, f10 - 0.0f, i10, i11);
                            canvas.drawPath(this.f3200d, this.f3205i);
                        }
                        i12 = 3;
                        f5 = f10;
                        f8 = f9;
                        canvas.drawPath(this.f3200d, this.f3205i);
                    } else {
                        i12 = 3;
                        f5 = f10;
                        f8 = f9;
                    }
                    if (i8 == 2) {
                        h(canvas, f8 - 0.0f, f5 - 0.0f);
                    }
                    if (i8 == i12) {
                        f(canvas, f8 - 0.0f, f5 - 0.0f);
                    }
                    if (i8 == 6) {
                        i(canvas, f8 - 0.0f, f5 - 0.0f, i10, i11);
                    }
                    canvas.drawPath(this.f3200d, this.f3205i);
                }
            }
            float[] fArr2 = this.f3197a;
            if (fArr2.length > 1) {
                canvas.drawCircle(fArr2[0], fArr2[1], 8.0f, this.f3202f);
                float[] fArr3 = this.f3197a;
                canvas.drawCircle(fArr3[fArr3.length - 2], fArr3[fArr3.length - 1], 8.0f, this.f3202f);
            }
        }

        public void a(Canvas canvas, HashMap<View, n> hashMap, int i8, int i9) {
            if (hashMap == null || hashMap.size() == 0) {
                return;
            }
            canvas.save();
            if (!MotionLayout.this.isInEditMode() && (i9 & 1) == 2) {
                String str = MotionLayout.this.getContext().getResources().getResourceName(MotionLayout.this.L) + ":" + MotionLayout.this.getProgress();
                canvas.drawText(str, 10.0f, MotionLayout.this.getHeight() - 30, this.f3204h);
                canvas.drawText(str, 11.0f, MotionLayout.this.getHeight() - 29, this.f3201e);
            }
            for (n nVar : hashMap.values()) {
                int h8 = nVar.h();
                if (i9 > 0 && h8 == 0) {
                    h8 = 1;
                }
                if (h8 != 0) {
                    this.q = nVar.c(this.f3199c, this.f3198b);
                    if (h8 >= 1) {
                        int i10 = i8 / 16;
                        float[] fArr = this.f3197a;
                        if (fArr == null || fArr.length != i10 * 2) {
                            this.f3197a = new float[i10 * 2];
                            this.f3200d = new Path();
                        }
                        int i11 = this.f3215t;
                        canvas.translate(i11, i11);
                        this.f3201e.setColor(1996488704);
                        this.f3205i.setColor(1996488704);
                        this.f3202f.setColor(1996488704);
                        this.f3203g.setColor(1996488704);
                        nVar.d(this.f3197a, i10);
                        b(canvas, h8, this.q, nVar);
                        this.f3201e.setColor(-21965);
                        this.f3202f.setColor(-2067046);
                        this.f3205i.setColor(-2067046);
                        this.f3203g.setColor(-13391360);
                        int i12 = this.f3215t;
                        canvas.translate(-i12, -i12);
                        b(canvas, h8, this.q, nVar);
                        if (h8 == 5) {
                            j(canvas, nVar);
                        }
                    }
                }
            }
            canvas.restore();
        }

        public void b(Canvas canvas, int i8, int i9, n nVar) {
            if (i8 == 4) {
                d(canvas);
            }
            if (i8 == 2) {
                g(canvas);
            }
            if (i8 == 3) {
                e(canvas);
            }
            c(canvas);
            k(canvas, i8, i9, nVar);
        }

        void l(String str, Paint paint) {
            paint.getTextBounds(str, 0, str.length(), this.f3213r);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e {

        /* renamed from: a  reason: collision with root package name */
        androidx.constraintlayout.solver.widgets.d f3217a = new androidx.constraintlayout.solver.widgets.d();

        /* renamed from: b  reason: collision with root package name */
        androidx.constraintlayout.solver.widgets.d f3218b = new androidx.constraintlayout.solver.widgets.d();

        /* renamed from: c  reason: collision with root package name */
        androidx.constraintlayout.widget.b f3219c = null;

        /* renamed from: d  reason: collision with root package name */
        androidx.constraintlayout.widget.b f3220d = null;

        /* renamed from: e  reason: collision with root package name */
        int f3221e;

        /* renamed from: f  reason: collision with root package name */
        int f3222f;

        e() {
        }

        private void i(androidx.constraintlayout.solver.widgets.d dVar, androidx.constraintlayout.widget.b bVar) {
            SparseArray<ConstraintWidget> sparseArray = new SparseArray<>();
            Constraints.LayoutParams layoutParams = new Constraints.LayoutParams(-2, -2);
            sparseArray.clear();
            sparseArray.put(0, dVar);
            sparseArray.put(MotionLayout.this.getId(), dVar);
            Iterator<ConstraintWidget> it = dVar.L0().iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                sparseArray.put(((View) next.r()).getId(), next);
            }
            Iterator<ConstraintWidget> it2 = dVar.L0().iterator();
            while (it2.hasNext()) {
                ConstraintWidget next2 = it2.next();
                View view = (View) next2.r();
                bVar.g(view.getId(), layoutParams);
                next2.F0(bVar.v(view.getId()));
                next2.i0(bVar.q(view.getId()));
                if (view instanceof ConstraintHelper) {
                    bVar.e((ConstraintHelper) view, next2, layoutParams, sparseArray);
                    if (view instanceof Barrier) {
                        ((Barrier) view).u();
                    }
                }
                if (Build.VERSION.SDK_INT >= 17) {
                    layoutParams.resolveLayoutDirection(MotionLayout.this.getLayoutDirection());
                } else {
                    layoutParams.resolveLayoutDirection(0);
                }
                MotionLayout.this.c(false, view, next2, layoutParams, sparseArray);
                next2.E0(bVar.u(view.getId()) == 1 ? view.getVisibility() : bVar.t(view.getId()));
            }
            Iterator<ConstraintWidget> it3 = dVar.L0().iterator();
            while (it3.hasNext()) {
                ConstraintWidget next3 = it3.next();
                if (next3 instanceof androidx.constraintlayout.solver.widgets.h) {
                    n0.a aVar = (n0.a) next3;
                    ((ConstraintHelper) next3.r()).s(dVar, aVar, sparseArray);
                    ((androidx.constraintlayout.solver.widgets.h) aVar).M0();
                }
            }
        }

        public void a() {
            int childCount = MotionLayout.this.getChildCount();
            MotionLayout.this.R.clear();
            for (int i8 = 0; i8 < childCount; i8++) {
                View childAt = MotionLayout.this.getChildAt(i8);
                MotionLayout.this.R.put(childAt, new n(childAt));
            }
            for (int i9 = 0; i9 < childCount; i9++) {
                View childAt2 = MotionLayout.this.getChildAt(i9);
                n nVar = MotionLayout.this.R.get(childAt2);
                if (nVar != null) {
                    if (this.f3219c != null) {
                        ConstraintWidget c9 = c(this.f3217a, childAt2);
                        if (c9 != null) {
                            nVar.t(c9, this.f3219c);
                        } else if (MotionLayout.this.f3174k0 != 0) {
                            Log.e("MotionLayout", androidx.constraintlayout.motion.widget.a.a() + "no widget for  " + androidx.constraintlayout.motion.widget.a.c(childAt2) + " (" + childAt2.getClass().getName() + ")");
                        }
                    }
                    if (this.f3220d != null) {
                        ConstraintWidget c10 = c(this.f3218b, childAt2);
                        if (c10 != null) {
                            nVar.q(c10, this.f3220d);
                        } else if (MotionLayout.this.f3174k0 != 0) {
                            Log.e("MotionLayout", androidx.constraintlayout.motion.widget.a.a() + "no widget for  " + androidx.constraintlayout.motion.widget.a.c(childAt2) + " (" + childAt2.getClass().getName() + ")");
                        }
                    }
                }
            }
        }

        void b(androidx.constraintlayout.solver.widgets.d dVar, androidx.constraintlayout.solver.widgets.d dVar2) {
            ArrayList<ConstraintWidget> L0 = dVar.L0();
            HashMap<ConstraintWidget, ConstraintWidget> hashMap = new HashMap<>();
            hashMap.put(dVar, dVar2);
            dVar2.L0().clear();
            dVar2.l(dVar, hashMap);
            Iterator<ConstraintWidget> it = L0.iterator();
            while (it.hasNext()) {
                ConstraintWidget next = it.next();
                ConstraintWidget aVar = next instanceof androidx.constraintlayout.solver.widgets.a ? new androidx.constraintlayout.solver.widgets.a() : next instanceof androidx.constraintlayout.solver.widgets.f ? new androidx.constraintlayout.solver.widgets.f() : next instanceof androidx.constraintlayout.solver.widgets.e ? new androidx.constraintlayout.solver.widgets.e() : next instanceof n0.a ? new n0.b() : new ConstraintWidget();
                dVar2.a(aVar);
                hashMap.put(next, aVar);
            }
            Iterator<ConstraintWidget> it2 = L0.iterator();
            while (it2.hasNext()) {
                ConstraintWidget next2 = it2.next();
                hashMap.get(next2).l(next2, hashMap);
            }
        }

        ConstraintWidget c(androidx.constraintlayout.solver.widgets.d dVar, View view) {
            if (dVar.r() == view) {
                return dVar;
            }
            ArrayList<ConstraintWidget> L0 = dVar.L0();
            int size = L0.size();
            for (int i8 = 0; i8 < size; i8++) {
                ConstraintWidget constraintWidget = L0.get(i8);
                if (constraintWidget.r() == view) {
                    return constraintWidget;
                }
            }
            return null;
        }

        void d(androidx.constraintlayout.solver.widgets.d dVar, androidx.constraintlayout.widget.b bVar, androidx.constraintlayout.widget.b bVar2) {
            this.f3219c = bVar;
            this.f3220d = bVar2;
            this.f3217a = new androidx.constraintlayout.solver.widgets.d();
            this.f3218b = new androidx.constraintlayout.solver.widgets.d();
            this.f3217a.h1(((ConstraintLayout) MotionLayout.this).f3939c.W0());
            this.f3218b.h1(((ConstraintLayout) MotionLayout.this).f3939c.W0());
            this.f3217a.O0();
            this.f3218b.O0();
            b(((ConstraintLayout) MotionLayout.this).f3939c, this.f3217a);
            b(((ConstraintLayout) MotionLayout.this).f3939c, this.f3218b);
            if (MotionLayout.this.f3165b0 > 0.5d) {
                if (bVar != null) {
                    i(this.f3217a, bVar);
                }
                i(this.f3218b, bVar2);
            } else {
                i(this.f3218b, bVar2);
                if (bVar != null) {
                    i(this.f3217a, bVar);
                }
            }
            this.f3217a.j1(MotionLayout.this.q());
            this.f3217a.l1();
            this.f3218b.j1(MotionLayout.this.q());
            this.f3218b.l1();
            ViewGroup.LayoutParams layoutParams = MotionLayout.this.getLayoutParams();
            if (layoutParams != null) {
                if (layoutParams.width == -2) {
                    androidx.constraintlayout.solver.widgets.d dVar2 = this.f3217a;
                    ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    dVar2.m0(dimensionBehaviour);
                    this.f3218b.m0(dimensionBehaviour);
                }
                if (layoutParams.height == -2) {
                    androidx.constraintlayout.solver.widgets.d dVar3 = this.f3217a;
                    ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    dVar3.B0(dimensionBehaviour2);
                    this.f3218b.B0(dimensionBehaviour2);
                }
            }
        }

        public boolean e(int i8, int i9) {
            return (i8 == this.f3221e && i9 == this.f3222f) ? false : true;
        }

        public void f(int i8, int i9) {
            int mode = View.MeasureSpec.getMode(i8);
            int mode2 = View.MeasureSpec.getMode(i9);
            MotionLayout motionLayout = MotionLayout.this;
            motionLayout.P0 = mode;
            motionLayout.Q0 = mode2;
            int optimizationLevel = motionLayout.getOptimizationLevel();
            MotionLayout motionLayout2 = MotionLayout.this;
            if (motionLayout2.K == motionLayout2.getStartState()) {
                MotionLayout.this.u(this.f3218b, optimizationLevel, i8, i9);
                if (this.f3219c != null) {
                    MotionLayout.this.u(this.f3217a, optimizationLevel, i8, i9);
                }
            } else {
                if (this.f3219c != null) {
                    MotionLayout.this.u(this.f3217a, optimizationLevel, i8, i9);
                }
                MotionLayout.this.u(this.f3218b, optimizationLevel, i8, i9);
            }
            boolean z4 = false;
            if (((MotionLayout.this.getParent() instanceof MotionLayout) && mode == 1073741824 && mode2 == 1073741824) ? false : true) {
                MotionLayout motionLayout3 = MotionLayout.this;
                motionLayout3.P0 = mode;
                motionLayout3.Q0 = mode2;
                if (motionLayout3.K == motionLayout3.getStartState()) {
                    MotionLayout.this.u(this.f3218b, optimizationLevel, i8, i9);
                    if (this.f3219c != null) {
                        MotionLayout.this.u(this.f3217a, optimizationLevel, i8, i9);
                    }
                } else {
                    if (this.f3219c != null) {
                        MotionLayout.this.u(this.f3217a, optimizationLevel, i8, i9);
                    }
                    MotionLayout.this.u(this.f3218b, optimizationLevel, i8, i9);
                }
                MotionLayout.this.L0 = this.f3217a.Q();
                MotionLayout.this.M0 = this.f3217a.w();
                MotionLayout.this.N0 = this.f3218b.Q();
                MotionLayout.this.O0 = this.f3218b.w();
                MotionLayout motionLayout4 = MotionLayout.this;
                motionLayout4.K0 = (motionLayout4.L0 == motionLayout4.N0 && motionLayout4.M0 == motionLayout4.O0) ? false : true;
            }
            MotionLayout motionLayout5 = MotionLayout.this;
            int i10 = motionLayout5.L0;
            int i11 = motionLayout5.M0;
            int i12 = motionLayout5.P0;
            if (i12 == Integer.MIN_VALUE || i12 == 0) {
                i10 = (int) (i10 + (motionLayout5.R0 * (motionLayout5.N0 - i10)));
            }
            int i13 = motionLayout5.Q0;
            if (i13 == Integer.MIN_VALUE || i13 == 0) {
                i11 = (int) (i11 + (motionLayout5.R0 * (motionLayout5.O0 - i11)));
            }
            boolean z8 = this.f3217a.d1() || this.f3218b.d1();
            if (this.f3217a.b1() || this.f3218b.b1()) {
                z4 = true;
            }
            MotionLayout.this.t(i8, i9, i10, i11, z8, z4);
        }

        public void g() {
            f(MotionLayout.this.O, MotionLayout.this.P);
            MotionLayout.this.r0();
        }

        public void h(int i8, int i9) {
            this.f3221e = i8;
            this.f3222f = i9;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        void a();

        void b(MotionEvent motionEvent);

        float c();

        float d();

        void e(int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class g implements f {

        /* renamed from: b  reason: collision with root package name */
        private static g f3224b = new g();

        /* renamed from: a  reason: collision with root package name */
        VelocityTracker f3225a;

        private g() {
        }

        public static g f() {
            f3224b.f3225a = VelocityTracker.obtain();
            return f3224b;
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.f
        public void a() {
            this.f3225a.recycle();
            this.f3225a = null;
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.f
        public void b(MotionEvent motionEvent) {
            VelocityTracker velocityTracker = this.f3225a;
            if (velocityTracker != null) {
                velocityTracker.addMovement(motionEvent);
            }
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.f
        public float c() {
            return this.f3225a.getYVelocity();
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.f
        public float d() {
            return this.f3225a.getXVelocity();
        }

        @Override // androidx.constraintlayout.motion.widget.MotionLayout.f
        public void e(int i8) {
            this.f3225a.computeCurrentVelocity(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h {

        /* renamed from: a  reason: collision with root package name */
        float f3226a = Float.NaN;

        /* renamed from: b  reason: collision with root package name */
        float f3227b = Float.NaN;

        /* renamed from: c  reason: collision with root package name */
        int f3228c = -1;

        /* renamed from: d  reason: collision with root package name */
        int f3229d = -1;

        /* renamed from: e  reason: collision with root package name */
        final String f3230e = "motion.progress";

        /* renamed from: f  reason: collision with root package name */
        final String f3231f = "motion.velocity";

        /* renamed from: g  reason: collision with root package name */
        final String f3232g = "motion.StartState";

        /* renamed from: h  reason: collision with root package name */
        final String f3233h = "motion.EndState";

        h() {
        }

        void a() {
            int i8 = this.f3228c;
            if (i8 != -1 || this.f3229d != -1) {
                if (i8 == -1) {
                    MotionLayout.this.v0(this.f3229d);
                } else {
                    int i9 = this.f3229d;
                    if (i9 == -1) {
                        MotionLayout.this.p0(i8, -1, -1);
                    } else {
                        MotionLayout.this.q0(i8, i9);
                    }
                }
                MotionLayout.this.setState(j.SETUP);
            }
            if (Float.isNaN(this.f3227b)) {
                if (Float.isNaN(this.f3226a)) {
                    return;
                }
                MotionLayout.this.setProgress(this.f3226a);
                return;
            }
            MotionLayout.this.o0(this.f3226a, this.f3227b);
            this.f3226a = Float.NaN;
            this.f3227b = Float.NaN;
            this.f3228c = -1;
            this.f3229d = -1;
        }

        public Bundle b() {
            Bundle bundle = new Bundle();
            bundle.putFloat("motion.progress", this.f3226a);
            bundle.putFloat("motion.velocity", this.f3227b);
            bundle.putInt("motion.StartState", this.f3228c);
            bundle.putInt("motion.EndState", this.f3229d);
            return bundle;
        }

        public void c() {
            this.f3229d = MotionLayout.this.L;
            this.f3228c = MotionLayout.this.H;
            this.f3227b = MotionLayout.this.getVelocity();
            this.f3226a = MotionLayout.this.getProgress();
        }

        public void d(int i8) {
            this.f3229d = i8;
        }

        public void e(float f5) {
            this.f3226a = f5;
        }

        public void f(int i8) {
            this.f3228c = i8;
        }

        public void g(Bundle bundle) {
            this.f3226a = bundle.getFloat("motion.progress");
            this.f3227b = bundle.getFloat("motion.velocity");
            this.f3228c = bundle.getInt("motion.StartState");
            this.f3229d = bundle.getInt("motion.EndState");
        }

        public void h(float f5) {
            this.f3227b = f5;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface i {
        void a(MotionLayout motionLayout, int i8, int i9, float f5);

        void b(MotionLayout motionLayout, int i8, int i9);

        void c(MotionLayout motionLayout, int i8, boolean z4, float f5);

        void d(MotionLayout motionLayout, int i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum j {
        UNDEFINED,
        SETUP,
        MOVING,
        FINISHED
    }

    public MotionLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.G = 0.0f;
        this.H = -1;
        this.K = -1;
        this.L = -1;
        this.O = 0;
        this.P = 0;
        this.Q = true;
        this.R = new HashMap<>();
        this.T = 0L;
        this.W = 1.0f;
        this.f3163a0 = 0.0f;
        this.f3165b0 = 0.0f;
        this.f3167d0 = 0.0f;
        this.f3169f0 = false;
        this.f3170g0 = false;
        this.f3174k0 = 0;
        this.f3176m0 = false;
        this.f3177n0 = new l0.g();
        this.f3178o0 = new c();
        this.f3180q0 = true;
        this.f3185v0 = false;
        this.A0 = false;
        this.B0 = null;
        this.C0 = null;
        this.D0 = null;
        this.E0 = 0;
        this.F0 = -1L;
        this.G0 = 0.0f;
        this.H0 = 0;
        this.I0 = 0.0f;
        this.J0 = false;
        this.K0 = false;
        this.S0 = new androidx.constraintlayout.motion.widget.e();
        this.T0 = false;
        this.V0 = j.UNDEFINED;
        this.W0 = new e();
        this.X0 = false;
        this.Y0 = new RectF();
        this.Z0 = null;
        this.f3164a1 = new ArrayList<>();
        i0(attributeSet);
    }

    public MotionLayout(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.G = 0.0f;
        this.H = -1;
        this.K = -1;
        this.L = -1;
        this.O = 0;
        this.P = 0;
        this.Q = true;
        this.R = new HashMap<>();
        this.T = 0L;
        this.W = 1.0f;
        this.f3163a0 = 0.0f;
        this.f3165b0 = 0.0f;
        this.f3167d0 = 0.0f;
        this.f3169f0 = false;
        this.f3170g0 = false;
        this.f3174k0 = 0;
        this.f3176m0 = false;
        this.f3177n0 = new l0.g();
        this.f3178o0 = new c();
        this.f3180q0 = true;
        this.f3185v0 = false;
        this.A0 = false;
        this.B0 = null;
        this.C0 = null;
        this.D0 = null;
        this.E0 = 0;
        this.F0 = -1L;
        this.G0 = 0.0f;
        this.H0 = 0;
        this.I0 = 0.0f;
        this.J0 = false;
        this.K0 = false;
        this.S0 = new androidx.constraintlayout.motion.widget.e();
        this.T0 = false;
        this.V0 = j.UNDEFINED;
        this.W0 = new e();
        this.X0 = false;
        this.Y0 = new RectF();
        this.Z0 = null;
        this.f3164a1 = new ArrayList<>();
        i0(attributeSet);
    }

    private void V() {
        q qVar = this.E;
        if (qVar == null) {
            Log.e("MotionLayout", "CHECK: motion scene not set! set \"app:layoutDescription=\"@xml/file\"");
            return;
        }
        int x8 = qVar.x();
        q qVar2 = this.E;
        W(x8, qVar2.i(qVar2.x()));
        SparseIntArray sparseIntArray = new SparseIntArray();
        SparseIntArray sparseIntArray2 = new SparseIntArray();
        Iterator<q.b> it = this.E.l().iterator();
        while (it.hasNext()) {
            q.b next = it.next();
            if (next == this.E.f3429c) {
                Log.v("MotionLayout", "CHECK: CURRENT");
            }
            X(next);
            int A = next.A();
            int y8 = next.y();
            String b9 = androidx.constraintlayout.motion.widget.a.b(getContext(), A);
            String b10 = androidx.constraintlayout.motion.widget.a.b(getContext(), y8);
            if (sparseIntArray.get(A) == y8) {
                Log.e("MotionLayout", "CHECK: two transitions with the same start and end " + b9 + "->" + b10);
            }
            if (sparseIntArray2.get(y8) == A) {
                Log.e("MotionLayout", "CHECK: you can't have reverse transitions" + b9 + "->" + b10);
            }
            sparseIntArray.put(A, y8);
            sparseIntArray2.put(y8, A);
            if (this.E.i(A) == null) {
                Log.e("MotionLayout", " no such constraintSetStart " + b9);
            }
            if (this.E.i(y8) == null) {
                Log.e("MotionLayout", " no such constraintSetEnd " + b9);
            }
        }
    }

    private void W(int i8, androidx.constraintlayout.widget.b bVar) {
        View childAt;
        String b9 = androidx.constraintlayout.motion.widget.a.b(getContext(), i8);
        int childCount = getChildCount();
        for (int i9 = 0; i9 < childCount; i9++) {
            int id = getChildAt(i9).getId();
            if (id == -1) {
                Log.w("MotionLayout", "CHECK: " + b9 + " ALL VIEWS SHOULD HAVE ID's " + childAt.getClass().getName() + " does not!");
            }
            if (bVar.p(id) == null) {
                Log.w("MotionLayout", "CHECK: " + b9 + " NO CONSTRAINTS for " + androidx.constraintlayout.motion.widget.a.c(childAt));
            }
        }
        int[] r4 = bVar.r();
        for (int i10 = 0; i10 < r4.length; i10++) {
            int i11 = r4[i10];
            String b10 = androidx.constraintlayout.motion.widget.a.b(getContext(), i11);
            if (findViewById(r4[i10]) == null) {
                Log.w("MotionLayout", "CHECK: " + b9 + " NO View matches id " + b10);
            }
            if (bVar.q(i11) == -1) {
                Log.w("MotionLayout", "CHECK: " + b9 + "(" + b10 + ") no LAYOUT_HEIGHT");
            }
            if (bVar.v(i11) == -1) {
                Log.w("MotionLayout", "CHECK: " + b9 + "(" + b10 + ") no LAYOUT_HEIGHT");
            }
        }
    }

    private void X(q.b bVar) {
        Log.v("MotionLayout", "CHECK: transition = " + bVar.u(getContext()));
        Log.v("MotionLayout", "CHECK: transition.setDuration = " + bVar.x());
        if (bVar.A() == bVar.y()) {
            Log.e("MotionLayout", "CHECK: start and end constraint set should not be the same!");
        }
    }

    private void Y() {
        int childCount = getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = getChildAt(i8);
            n nVar = this.R.get(childAt);
            if (nVar != null) {
                nVar.s(childAt);
            }
        }
    }

    private void a0() {
        boolean z4;
        float signum = Math.signum(this.f3167d0 - this.f3165b0);
        long nanoTime = getNanoTime();
        Interpolator interpolator = this.F;
        float f5 = this.f3165b0 + (!(interpolator instanceof l0.g) ? ((((float) (nanoTime - this.f3166c0)) * signum) * 1.0E-9f) / this.W : 0.0f);
        if (this.f3168e0) {
            f5 = this.f3167d0;
        }
        int i8 = (signum > 0.0f ? 1 : (signum == 0.0f ? 0 : -1));
        if ((i8 <= 0 || f5 < this.f3167d0) && (signum > 0.0f || f5 > this.f3167d0)) {
            z4 = false;
        } else {
            f5 = this.f3167d0;
            z4 = true;
        }
        if (interpolator != null && !z4) {
            f5 = this.f3176m0 ? interpolator.getInterpolation(((float) (nanoTime - this.T)) * 1.0E-9f) : interpolator.getInterpolation(f5);
        }
        if ((i8 > 0 && f5 >= this.f3167d0) || (signum <= 0.0f && f5 <= this.f3167d0)) {
            f5 = this.f3167d0;
        }
        this.R0 = f5;
        int childCount = getChildCount();
        long nanoTime2 = getNanoTime();
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = getChildAt(i9);
            n nVar = this.R.get(childAt);
            if (nVar != null) {
                nVar.o(childAt, f5, nanoTime2, this.S0);
            }
        }
        if (this.K0) {
            requestLayout();
        }
    }

    private void b0() {
        ArrayList<i> arrayList;
        if ((this.f3171h0 == null && ((arrayList = this.D0) == null || arrayList.isEmpty())) || this.I0 == this.f3163a0) {
            return;
        }
        if (this.H0 != -1) {
            i iVar = this.f3171h0;
            if (iVar != null) {
                iVar.b(this, this.H, this.L);
            }
            ArrayList<i> arrayList2 = this.D0;
            if (arrayList2 != null) {
                Iterator<i> it = arrayList2.iterator();
                while (it.hasNext()) {
                    it.next().b(this, this.H, this.L);
                }
            }
            this.J0 = true;
        }
        this.H0 = -1;
        float f5 = this.f3163a0;
        this.I0 = f5;
        i iVar2 = this.f3171h0;
        if (iVar2 != null) {
            iVar2.a(this, this.H, this.L, f5);
        }
        ArrayList<i> arrayList3 = this.D0;
        if (arrayList3 != null) {
            Iterator<i> it2 = arrayList3.iterator();
            while (it2.hasNext()) {
                it2.next().a(this, this.H, this.L, this.f3163a0);
            }
        }
        this.J0 = true;
    }

    private boolean h0(float f5, float f8, View view, MotionEvent motionEvent) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i8 = 0; i8 < childCount; i8++) {
                if (h0(view.getLeft() + f5, view.getTop() + f8, viewGroup.getChildAt(i8), motionEvent)) {
                    return true;
                }
            }
        }
        this.Y0.set(view.getLeft() + f5, view.getTop() + f8, f5 + view.getRight(), f8 + view.getBottom());
        if (motionEvent.getAction() == 0) {
            if (this.Y0.contains(motionEvent.getX(), motionEvent.getY()) && view.onTouchEvent(motionEvent)) {
                return true;
            }
        } else if (view.onTouchEvent(motionEvent)) {
            return true;
        }
        return false;
    }

    private void i0(AttributeSet attributeSet) {
        q qVar;
        int i8;
        f3162b1 = isInEditMode();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, androidx.constraintlayout.widget.e.H6);
            int indexCount = obtainStyledAttributes.getIndexCount();
            boolean z4 = true;
            for (int i9 = 0; i9 < indexCount; i9++) {
                int index = obtainStyledAttributes.getIndex(i9);
                if (index == androidx.constraintlayout.widget.e.K6) {
                    this.E = new q(getContext(), this, obtainStyledAttributes.getResourceId(index, -1));
                } else if (index == androidx.constraintlayout.widget.e.J6) {
                    this.K = obtainStyledAttributes.getResourceId(index, -1);
                } else if (index == androidx.constraintlayout.widget.e.M6) {
                    this.f3167d0 = obtainStyledAttributes.getFloat(index, 0.0f);
                    this.f3169f0 = true;
                } else if (index == androidx.constraintlayout.widget.e.I6) {
                    z4 = obtainStyledAttributes.getBoolean(index, z4);
                } else if (index == androidx.constraintlayout.widget.e.N6) {
                    if (this.f3174k0 == 0) {
                        i8 = obtainStyledAttributes.getBoolean(index, false) ? 2 : 0;
                        this.f3174k0 = i8;
                    }
                } else if (index == androidx.constraintlayout.widget.e.L6) {
                    i8 = obtainStyledAttributes.getInt(index, 0);
                    this.f3174k0 = i8;
                }
            }
            obtainStyledAttributes.recycle();
            if (this.E == null) {
                Log.e("MotionLayout", "WARNING NO app:layoutDescription tag");
            }
            if (!z4) {
                this.E = null;
            }
        }
        if (this.f3174k0 != 0) {
            V();
        }
        if (this.K != -1 || (qVar = this.E) == null) {
            return;
        }
        this.K = qVar.x();
        this.H = this.E.x();
        this.L = this.E.n();
    }

    private void l0() {
        q qVar = this.E;
        if (qVar == null) {
            return;
        }
        if (qVar.f(this, this.K)) {
            requestLayout();
            return;
        }
        int i8 = this.K;
        if (i8 != -1) {
            this.E.e(this, i8);
        }
        if (this.E.Q()) {
            this.E.O();
        }
    }

    private void m0() {
        ArrayList<i> arrayList;
        if (this.f3171h0 == null && ((arrayList = this.D0) == null || arrayList.isEmpty())) {
            return;
        }
        this.J0 = false;
        Iterator<Integer> it = this.f3164a1.iterator();
        while (it.hasNext()) {
            Integer next = it.next();
            i iVar = this.f3171h0;
            if (iVar != null) {
                iVar.d(this, next.intValue());
            }
            ArrayList<i> arrayList2 = this.D0;
            if (arrayList2 != null) {
                Iterator<i> it2 = arrayList2.iterator();
                while (it2.hasNext()) {
                    it2.next().d(this, next.intValue());
                }
            }
        }
        this.f3164a1.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r0() {
        int childCount = getChildCount();
        this.W0.a();
        boolean z4 = true;
        this.f3169f0 = true;
        int width = getWidth();
        int height = getHeight();
        int h8 = this.E.h();
        int i8 = 0;
        if (h8 != -1) {
            for (int i9 = 0; i9 < childCount; i9++) {
                n nVar = this.R.get(getChildAt(i9));
                if (nVar != null) {
                    nVar.r(h8);
                }
            }
        }
        for (int i10 = 0; i10 < childCount; i10++) {
            n nVar2 = this.R.get(getChildAt(i10));
            if (nVar2 != null) {
                this.E.q(nVar2);
                nVar2.v(width, height, this.W, getNanoTime());
            }
        }
        float w8 = this.E.w();
        if (w8 != 0.0f) {
            boolean z8 = ((double) w8) < 0.0d;
            float abs = Math.abs(w8);
            float f5 = -3.4028235E38f;
            float f8 = Float.MAX_VALUE;
            float f9 = -3.4028235E38f;
            float f10 = Float.MAX_VALUE;
            int i11 = 0;
            while (true) {
                if (i11 >= childCount) {
                    z4 = false;
                    break;
                }
                n nVar3 = this.R.get(getChildAt(i11));
                if (!Float.isNaN(nVar3.f3397k)) {
                    break;
                }
                float i12 = nVar3.i();
                float j8 = nVar3.j();
                float f11 = z8 ? j8 - i12 : j8 + i12;
                f10 = Math.min(f10, f11);
                f9 = Math.max(f9, f11);
                i11++;
            }
            if (!z4) {
                while (i8 < childCount) {
                    n nVar4 = this.R.get(getChildAt(i8));
                    float i13 = nVar4.i();
                    float j9 = nVar4.j();
                    float f12 = z8 ? j9 - i13 : j9 + i13;
                    nVar4.f3399m = 1.0f / (1.0f - abs);
                    nVar4.f3398l = abs - (((f12 - f10) * abs) / (f9 - f10));
                    i8++;
                }
                return;
            }
            for (int i14 = 0; i14 < childCount; i14++) {
                n nVar5 = this.R.get(getChildAt(i14));
                if (!Float.isNaN(nVar5.f3397k)) {
                    f8 = Math.min(f8, nVar5.f3397k);
                    f5 = Math.max(f5, nVar5.f3397k);
                }
            }
            while (i8 < childCount) {
                n nVar6 = this.R.get(getChildAt(i8));
                if (!Float.isNaN(nVar6.f3397k)) {
                    nVar6.f3399m = 1.0f / (1.0f - abs);
                    float f13 = nVar6.f3397k;
                    nVar6.f3398l = abs - (z8 ? ((f5 - f13) / (f5 - f8)) * abs : ((f13 - f8) * abs) / (f5 - f8));
                }
                i8++;
            }
        }
    }

    private static boolean x0(float f5, float f8, float f9) {
        if (f5 > 0.0f) {
            float f10 = f5 / f9;
            return f8 + ((f5 * f10) - (((f9 * f10) * f10) / 2.0f)) > 1.0f;
        }
        float f11 = (-f5) / f9;
        return f8 + ((f5 * f11) + (((f9 * f11) * f11) / 2.0f)) < 0.0f;
    }

    void U(float f5) {
        q qVar = this.E;
        if (qVar == null) {
            return;
        }
        float f8 = this.f3165b0;
        float f9 = this.f3163a0;
        if (f8 != f9 && this.f3168e0) {
            this.f3165b0 = f9;
        }
        float f10 = this.f3165b0;
        if (f10 == f5) {
            return;
        }
        this.f3176m0 = false;
        this.f3167d0 = f5;
        this.W = qVar.m() / 1000.0f;
        setProgress(this.f3167d0);
        this.F = this.E.p();
        this.f3168e0 = false;
        this.T = getNanoTime();
        this.f3169f0 = true;
        this.f3163a0 = f10;
        this.f3165b0 = f10;
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x0205, code lost:
        if (r1 != r2) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0208, code lost:
        r6 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0209, code lost:
        r22.K = r2;
        r7 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x0215, code lost:
        if (r1 != r2) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void Z(boolean r23) {
        /*
            Method dump skipped, instructions count: 555
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionLayout.Z(boolean):void");
    }

    protected void c0() {
        int i8;
        ArrayList<Integer> arrayList;
        ArrayList<i> arrayList2;
        if ((this.f3171h0 != null || ((arrayList2 = this.D0) != null && !arrayList2.isEmpty())) && this.H0 == -1) {
            this.H0 = this.K;
            if (this.f3164a1.isEmpty()) {
                i8 = -1;
            } else {
                i8 = this.f3164a1.get(arrayList.size() - 1).intValue();
            }
            int i9 = this.K;
            if (i8 != i9 && i9 != -1) {
                this.f3164a1.add(Integer.valueOf(i9));
            }
        }
        m0();
    }

    public void d0(int i8, boolean z4, float f5) {
        i iVar = this.f3171h0;
        if (iVar != null) {
            iVar.c(this, i8, z4, f5);
        }
        ArrayList<i> arrayList = this.D0;
        if (arrayList != null) {
            Iterator<i> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().c(this, i8, z4, f5);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x00a4  */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void dispatchDraw(android.graphics.Canvas r10) {
        /*
            Method dump skipped, instructions count: 239
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionLayout.dispatchDraw(android.graphics.Canvas):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e0(int i8, float f5, float f8, float f9, float[] fArr) {
        String resourceName;
        HashMap<View, n> hashMap = this.R;
        View h8 = h(i8);
        n nVar = hashMap.get(h8);
        if (nVar != null) {
            nVar.g(f5, f8, f9, fArr);
            float y8 = h8.getY();
            this.f3172i0 = f5;
            this.f3173j0 = y8;
            return;
        }
        if (h8 == null) {
            resourceName = BuildConfig.FLAVOR + i8;
        } else {
            resourceName = h8.getContext().getResources().getResourceName(i8);
        }
        Log.w("MotionLayout", "WARNING could not find view id " + resourceName);
    }

    public q.b f0(int i8) {
        return this.E.y(i8);
    }

    public void g0(View view, float f5, float f8, float[] fArr, int i8) {
        float f9;
        float f10 = this.G;
        float f11 = this.f3165b0;
        if (this.F != null) {
            float signum = Math.signum(this.f3167d0 - f11);
            float interpolation = this.F.getInterpolation(this.f3165b0 + 1.0E-5f);
            float interpolation2 = this.F.getInterpolation(this.f3165b0);
            f10 = (signum * ((interpolation - interpolation2) / 1.0E-5f)) / this.W;
            f9 = interpolation2;
        } else {
            f9 = f11;
        }
        Interpolator interpolator = this.F;
        if (interpolator instanceof o) {
            f10 = ((o) interpolator).a();
        }
        n nVar = this.R.get(view);
        if ((i8 & 1) == 0) {
            nVar.l(f9, view.getWidth(), view.getHeight(), f5, f8, fArr);
        } else {
            nVar.g(f9, f5, f8, fArr);
        }
        if (i8 < 2) {
            fArr[0] = fArr[0] * f10;
            fArr[1] = fArr[1] * f10;
        }
    }

    public int[] getConstraintSetIds() {
        q qVar = this.E;
        if (qVar == null) {
            return null;
        }
        return qVar.k();
    }

    public int getCurrentState() {
        return this.K;
    }

    public ArrayList<q.b> getDefinedTransitions() {
        q qVar = this.E;
        if (qVar == null) {
            return null;
        }
        return qVar.l();
    }

    public androidx.constraintlayout.motion.widget.b getDesignTool() {
        if (this.f3179p0 == null) {
            this.f3179p0 = new androidx.constraintlayout.motion.widget.b(this);
        }
        return this.f3179p0;
    }

    public int getEndState() {
        return this.L;
    }

    protected long getNanoTime() {
        return System.nanoTime();
    }

    public float getProgress() {
        return this.f3165b0;
    }

    public int getStartState() {
        return this.H;
    }

    public float getTargetPosition() {
        return this.f3167d0;
    }

    public Bundle getTransitionState() {
        if (this.U0 == null) {
            this.U0 = new h();
        }
        this.U0.c();
        return this.U0.b();
    }

    public long getTransitionTimeMs() {
        q qVar = this.E;
        if (qVar != null) {
            this.W = qVar.m() / 1000.0f;
        }
        return this.W * 1000.0f;
    }

    public float getVelocity() {
        return this.G;
    }

    @Override // android.view.View
    public boolean isAttachedToWindow() {
        return Build.VERSION.SDK_INT >= 19 ? super.isAttachedToWindow() : getWindowToken() != null;
    }

    @Override // androidx.core.view.s
    public void j(View view, int i8, int i9, int i10, int i11, int i12, int[] iArr) {
        if (this.f3185v0 || i8 != 0 || i9 != 0) {
            iArr[0] = iArr[0] + i10;
            iArr[1] = iArr[1] + i11;
        }
        this.f3185v0 = false;
    }

    public boolean j0() {
        return this.Q;
    }

    @Override // androidx.core.view.r
    public void k(View view, int i8, int i9, int i10, int i11, int i12) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public f k0() {
        return g.f();
    }

    @Override // androidx.core.view.r
    public boolean l(View view, View view2, int i8, int i9) {
        q.b bVar;
        q qVar = this.E;
        return (qVar == null || (bVar = qVar.f3429c) == null || bVar.B() == null || (this.E.f3429c.B().d() & 2) != 0) ? false : true;
    }

    @Override // androidx.core.view.r
    public void m(View view, View view2, int i8, int i9) {
    }

    @Override // androidx.core.view.r
    public void n(View view, int i8) {
        q qVar = this.E;
        if (qVar == null) {
            return;
        }
        float f5 = this.f3186w0;
        float f8 = this.f3189z0;
        qVar.G(f5 / f8, this.f3187x0 / f8);
    }

    public void n0() {
        this.W0.g();
        invalidate();
    }

    @Override // androidx.core.view.r
    public void o(View view, int i8, int i9, int[] iArr, int i10) {
        q.b bVar;
        t B;
        int k8;
        q qVar = this.E;
        if (qVar == null || (bVar = qVar.f3429c) == null || !bVar.C()) {
            return;
        }
        q.b bVar2 = this.E.f3429c;
        if (bVar2 == null || !bVar2.C() || (B = bVar2.B()) == null || (k8 = B.k()) == -1 || view.getId() == k8) {
            q qVar2 = this.E;
            if (qVar2 != null && qVar2.t()) {
                float f5 = this.f3163a0;
                if ((f5 == 1.0f || f5 == 0.0f) && view.canScrollVertically(-1)) {
                    return;
                }
            }
            if (bVar2.B() != null && (this.E.f3429c.B().d() & 1) != 0) {
                float u8 = this.E.u(i8, i9);
                float f8 = this.f3165b0;
                if ((f8 <= 0.0f && u8 < 0.0f) || (f8 >= 1.0f && u8 > 0.0f)) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        view.setNestedScrollingEnabled(false);
                        view.post(new a(view));
                        return;
                    }
                    return;
                }
            }
            float f9 = this.f3163a0;
            long nanoTime = getNanoTime();
            float f10 = i8;
            this.f3186w0 = f10;
            float f11 = i9;
            this.f3187x0 = f11;
            this.f3189z0 = (float) ((nanoTime - this.f3188y0) * 1.0E-9d);
            this.f3188y0 = nanoTime;
            this.E.F(f10, f11);
            if (f9 != this.f3163a0) {
                iArr[0] = i8;
                iArr[1] = i9;
            }
            Z(false);
            if (iArr[0] == 0 && iArr[1] == 0) {
                return;
            }
            this.f3185v0 = true;
        }
    }

    public void o0(float f5, float f8) {
        if (isAttachedToWindow()) {
            setProgress(f5);
            setState(j.MOVING);
            this.G = f8;
            U(1.0f);
            return;
        }
        if (this.U0 == null) {
            this.U0 = new h();
        }
        this.U0.e(f5);
        this.U0.h(f8);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        int i8;
        super.onAttachedToWindow();
        q qVar = this.E;
        if (qVar != null && (i8 = this.K) != -1) {
            androidx.constraintlayout.widget.b i9 = qVar.i(i8);
            this.E.J(this);
            if (i9 != null) {
                i9.d(this);
            }
            this.H = this.K;
        }
        l0();
        h hVar = this.U0;
        if (hVar != null) {
            hVar.a();
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        q.b bVar;
        t B;
        int k8;
        RectF j8;
        q qVar = this.E;
        if (qVar != null && this.Q && (bVar = qVar.f3429c) != null && bVar.C() && (B = bVar.B()) != null && ((motionEvent.getAction() != 0 || (j8 = B.j(this, new RectF())) == null || j8.contains(motionEvent.getX(), motionEvent.getY())) && (k8 = B.k()) != -1)) {
            View view = this.Z0;
            if (view == null || view.getId() != k8) {
                this.Z0 = findViewById(k8);
            }
            View view2 = this.Z0;
            if (view2 != null) {
                this.Y0.set(view2.getLeft(), this.Z0.getTop(), this.Z0.getRight(), this.Z0.getBottom());
                if (this.Y0.contains(motionEvent.getX(), motionEvent.getY()) && !h0(0.0f, 0.0f, this.Z0, motionEvent)) {
                    return onTouchEvent(motionEvent);
                }
            }
        }
        return false;
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        this.T0 = true;
        try {
            if (this.E == null) {
                super.onLayout(z4, i8, i9, i10, i11);
                return;
            }
            int i12 = i10 - i8;
            int i13 = i11 - i9;
            if (this.f3183t0 != i12 || this.f3184u0 != i13) {
                n0();
                Z(true);
            }
            this.f3183t0 = i12;
            this.f3184u0 = i13;
            this.f3181r0 = i12;
            this.f3182s0 = i13;
        } finally {
            this.T0 = false;
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        int i10;
        int i11;
        if (this.E == null) {
            super.onMeasure(i8, i9);
            return;
        }
        boolean z4 = false;
        boolean z8 = (this.O == i8 && this.P == i9) ? false : true;
        if (this.X0) {
            this.X0 = false;
            l0();
            m0();
            z8 = true;
        }
        if (this.f3944h) {
            z8 = true;
        }
        this.O = i8;
        this.P = i9;
        int x8 = this.E.x();
        int n8 = this.E.n();
        if ((z8 || this.W0.e(x8, n8)) && this.H != -1) {
            super.onMeasure(i8, i9);
            this.W0.d(this.f3939c, this.E.i(x8), this.E.i(n8));
            this.W0.g();
            this.W0.h(x8, n8);
        } else {
            z4 = true;
        }
        if (this.K0 || z4) {
            int paddingTop = getPaddingTop() + getPaddingBottom();
            int Q = this.f3939c.Q() + getPaddingLeft() + getPaddingRight();
            int w8 = this.f3939c.w() + paddingTop;
            int i12 = this.P0;
            if (i12 == Integer.MIN_VALUE || i12 == 0) {
                Q = (int) (this.L0 + (this.R0 * (this.N0 - i10)));
                requestLayout();
            }
            int i13 = this.Q0;
            if (i13 == Integer.MIN_VALUE || i13 == 0) {
                w8 = (int) (this.M0 + (this.R0 * (this.O0 - i11)));
                requestLayout();
            }
            setMeasuredDimension(Q, w8);
        }
        a0();
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onNestedFling(View view, float f5, float f8, boolean z4) {
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.t
    public boolean onNestedPreFling(View view, float f5, float f8) {
        return false;
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i8) {
        q qVar = this.E;
        if (qVar != null) {
            qVar.L(q());
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        q qVar = this.E;
        if (qVar != null && this.Q && qVar.Q()) {
            q.b bVar = this.E.f3429c;
            if (bVar == null || bVar.C()) {
                this.E.H(motionEvent, getCurrentState(), this);
                return true;
            }
            return super.onTouchEvent(motionEvent);
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public void onViewAdded(View view) {
        super.onViewAdded(view);
        if (view instanceof MotionHelper) {
            MotionHelper motionHelper = (MotionHelper) view;
            if (this.D0 == null) {
                this.D0 = new ArrayList<>();
            }
            this.D0.add(motionHelper);
            if (motionHelper.w()) {
                if (this.B0 == null) {
                    this.B0 = new ArrayList<>();
                }
                this.B0.add(motionHelper);
            }
            if (motionHelper.v()) {
                if (this.C0 == null) {
                    this.C0 = new ArrayList<>();
                }
                this.C0.add(motionHelper);
            }
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        ArrayList<MotionHelper> arrayList = this.B0;
        if (arrayList != null) {
            arrayList.remove(view);
        }
        ArrayList<MotionHelper> arrayList2 = this.C0;
        if (arrayList2 != null) {
            arrayList2.remove(view);
        }
    }

    public void p0(int i8, int i9, int i10) {
        setState(j.SETUP);
        this.K = i8;
        this.H = -1;
        this.L = -1;
        androidx.constraintlayout.widget.a aVar = this.f3947l;
        if (aVar != null) {
            aVar.d(i8, i9, i10);
            return;
        }
        q qVar = this.E;
        if (qVar != null) {
            qVar.i(i8).d(this);
        }
    }

    public void q0(int i8, int i9) {
        if (!isAttachedToWindow()) {
            if (this.U0 == null) {
                this.U0 = new h();
            }
            this.U0.f(i8);
            this.U0.d(i9);
            return;
        }
        q qVar = this.E;
        if (qVar != null) {
            this.H = i8;
            this.L = i9;
            qVar.M(i8, i9);
            this.W0.d(this.f3939c, this.E.i(i8), this.E.i(i9));
            n0();
            this.f3165b0 = 0.0f;
            u0();
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.View, android.view.ViewParent
    public void requestLayout() {
        q qVar;
        q.b bVar;
        if (this.K0 || this.K != -1 || (qVar = this.E) == null || (bVar = qVar.f3429c) == null || bVar.z() != 0) {
            super.requestLayout();
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout
    protected void s(int i8) {
        this.f3947l = null;
    }

    public void s0(int i8, float f5, float f8) {
        Interpolator interpolator;
        if (this.E == null || this.f3165b0 == f5) {
            return;
        }
        this.f3176m0 = true;
        this.T = getNanoTime();
        float m8 = this.E.m() / 1000.0f;
        this.W = m8;
        this.f3167d0 = f5;
        this.f3169f0 = true;
        if (i8 != 0 && i8 != 1 && i8 != 2) {
            if (i8 != 4) {
                if (i8 == 5) {
                    if (!x0(f8, this.f3165b0, this.E.r())) {
                        this.f3177n0.c(this.f3165b0, f5, f8, this.W, this.E.r(), this.E.s());
                        this.G = 0.0f;
                    }
                }
                this.f3168e0 = false;
                this.T = getNanoTime();
                invalidate();
            }
            this.f3178o0.b(f8, this.f3165b0, this.E.r());
            interpolator = this.f3178o0;
            this.F = interpolator;
            this.f3168e0 = false;
            this.T = getNanoTime();
            invalidate();
        }
        if (i8 == 1) {
            f5 = 0.0f;
        } else if (i8 == 2) {
            f5 = 1.0f;
        }
        this.f3177n0.c(this.f3165b0, f5, f8, m8, this.E.r(), this.E.s());
        int i9 = this.K;
        this.f3167d0 = f5;
        this.K = i9;
        interpolator = this.f3177n0;
        this.F = interpolator;
        this.f3168e0 = false;
        this.T = getNanoTime();
        invalidate();
    }

    public void setDebugMode(int i8) {
        this.f3174k0 = i8;
        invalidate();
    }

    public void setInteractionEnabled(boolean z4) {
        this.Q = z4;
    }

    public void setInterpolatedProgress(float f5) {
        if (this.E != null) {
            setState(j.MOVING);
            Interpolator p8 = this.E.p();
            if (p8 != null) {
                setProgress(p8.getInterpolation(f5));
                return;
            }
        }
        setProgress(f5);
    }

    public void setOnHide(float f5) {
        ArrayList<MotionHelper> arrayList = this.C0;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i8 = 0; i8 < size; i8++) {
                this.C0.get(i8).setProgress(f5);
            }
        }
    }

    public void setOnShow(float f5) {
        ArrayList<MotionHelper> arrayList = this.B0;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i8 = 0; i8 < size; i8++) {
                this.B0.get(i8).setProgress(f5);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0024, code lost:
        if (r3.f3165b0 == 0.0f) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0026, code lost:
        r0 = androidx.constraintlayout.motion.widget.MotionLayout.j.f3238d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0037, code lost:
        if (r3.f3165b0 == 1.0f) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setProgress(float r4) {
        /*
            r3 = this;
            boolean r0 = r3.isAttachedToWindow()
            if (r0 != 0) goto L17
            androidx.constraintlayout.motion.widget.MotionLayout$h r0 = r3.U0
            if (r0 != 0) goto L11
            androidx.constraintlayout.motion.widget.MotionLayout$h r0 = new androidx.constraintlayout.motion.widget.MotionLayout$h
            r0.<init>()
            r3.U0 = r0
        L11:
            androidx.constraintlayout.motion.widget.MotionLayout$h r0 = r3.U0
            r0.e(r4)
            return
        L17:
            r0 = 0
            int r1 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r1 > 0) goto L29
            int r1 = r3.H
            r3.K = r1
            float r1 = r3.f3165b0
            int r0 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1))
            if (r0 != 0) goto L42
        L26:
            androidx.constraintlayout.motion.widget.MotionLayout$j r0 = androidx.constraintlayout.motion.widget.MotionLayout.j.FINISHED
            goto L3f
        L29:
            r0 = 1065353216(0x3f800000, float:1.0)
            int r1 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r1 < 0) goto L3a
            int r1 = r3.L
            r3.K = r1
            float r1 = r3.f3165b0
            int r0 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1))
            if (r0 != 0) goto L42
            goto L26
        L3a:
            r0 = -1
            r3.K = r0
            androidx.constraintlayout.motion.widget.MotionLayout$j r0 = androidx.constraintlayout.motion.widget.MotionLayout.j.MOVING
        L3f:
            r3.setState(r0)
        L42:
            androidx.constraintlayout.motion.widget.q r0 = r3.E
            if (r0 != 0) goto L47
            return
        L47:
            r0 = 1
            r3.f3168e0 = r0
            r3.f3167d0 = r4
            r3.f3163a0 = r4
            r1 = -1
            r3.f3166c0 = r1
            r3.T = r1
            r4 = 0
            r3.F = r4
            r3.f3169f0 = r0
            r3.invalidate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.motion.widget.MotionLayout.setProgress(float):void");
    }

    public void setScene(q qVar) {
        this.E = qVar;
        qVar.L(q());
        n0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setState(j jVar) {
        j jVar2 = j.FINISHED;
        if (jVar == jVar2 && this.K == -1) {
            return;
        }
        j jVar3 = this.V0;
        this.V0 = jVar;
        j jVar4 = j.MOVING;
        if (jVar3 == jVar4 && jVar == jVar4) {
            b0();
        }
        int i8 = b.f3192a[jVar3.ordinal()];
        if (i8 == 1 || i8 == 2) {
            if (jVar == jVar4) {
                b0();
            }
            if (jVar != jVar2) {
                return;
            }
        } else if (i8 != 3 || jVar != jVar2) {
            return;
        }
        c0();
    }

    public void setTransition(int i8) {
        if (this.E != null) {
            q.b f02 = f0(i8);
            this.H = f02.A();
            this.L = f02.y();
            if (!isAttachedToWindow()) {
                if (this.U0 == null) {
                    this.U0 = new h();
                }
                this.U0.f(this.H);
                this.U0.d(this.L);
                return;
            }
            float f5 = Float.NaN;
            int i9 = this.K;
            if (i9 == this.H) {
                f5 = 0.0f;
            } else if (i9 == this.L) {
                f5 = 1.0f;
            }
            this.E.N(f02);
            this.W0.d(this.f3939c, this.E.i(this.H), this.E.i(this.L));
            n0();
            this.f3165b0 = Float.isNaN(f5) ? 0.0f : f5;
            if (!Float.isNaN(f5)) {
                setProgress(f5);
                return;
            }
            Log.v("MotionLayout", androidx.constraintlayout.motion.widget.a.a() + " transitionToStart ");
            u0();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTransition(q.b bVar) {
        this.E.N(bVar);
        setState(j.SETUP);
        float f5 = this.K == this.E.n() ? 1.0f : 0.0f;
        this.f3165b0 = f5;
        this.f3163a0 = f5;
        this.f3167d0 = f5;
        this.f3166c0 = bVar.D(1) ? -1L : getNanoTime();
        int x8 = this.E.x();
        int n8 = this.E.n();
        if (x8 == this.H && n8 == this.L) {
            return;
        }
        this.H = x8;
        this.L = n8;
        this.E.M(x8, n8);
        this.W0.d(this.f3939c, this.E.i(this.H), this.E.i(this.L));
        this.W0.h(this.H, this.L);
        this.W0.g();
        n0();
    }

    public void setTransitionDuration(int i8) {
        q qVar = this.E;
        if (qVar == null) {
            Log.e("MotionLayout", "MotionScene not defined");
        } else {
            qVar.K(i8);
        }
    }

    public void setTransitionListener(i iVar) {
        this.f3171h0 = iVar;
    }

    public void setTransitionState(Bundle bundle) {
        if (this.U0 == null) {
            this.U0 = new h();
        }
        this.U0.g(bundle);
        if (isAttachedToWindow()) {
            this.U0.a();
        }
    }

    public void t0() {
        U(1.0f);
    }

    @Override // android.view.View
    public String toString() {
        Context context = getContext();
        return androidx.constraintlayout.motion.widget.a.b(context, this.H) + "->" + androidx.constraintlayout.motion.widget.a.b(context, this.L) + " (pos:" + this.f3165b0 + " Dpos/Dt:" + this.G;
    }

    public void u0() {
        U(0.0f);
    }

    public void v0(int i8) {
        if (isAttachedToWindow()) {
            w0(i8, -1, -1);
            return;
        }
        if (this.U0 == null) {
            this.U0 = new h();
        }
        this.U0.d(i8);
    }

    public void w0(int i8, int i9, int i10) {
        androidx.constraintlayout.widget.f fVar;
        int a9;
        q qVar = this.E;
        if (qVar != null && (fVar = qVar.f3428b) != null && (a9 = fVar.a(this.K, i8, i9, i10)) != -1) {
            i8 = a9;
        }
        int i11 = this.K;
        if (i11 == i8) {
            return;
        }
        if (this.H == i8) {
            U(0.0f);
        } else if (this.L == i8) {
            U(1.0f);
        } else {
            this.L = i8;
            if (i11 != -1) {
                q0(i11, i8);
                U(1.0f);
                this.f3165b0 = 0.0f;
                t0();
                return;
            }
            this.f3176m0 = false;
            this.f3167d0 = 1.0f;
            this.f3163a0 = 0.0f;
            this.f3165b0 = 0.0f;
            this.f3166c0 = getNanoTime();
            this.T = getNanoTime();
            this.f3168e0 = false;
            this.F = null;
            this.W = this.E.m() / 1000.0f;
            this.H = -1;
            this.E.M(-1, this.L);
            this.E.x();
            int childCount = getChildCount();
            this.R.clear();
            for (int i12 = 0; i12 < childCount; i12++) {
                View childAt = getChildAt(i12);
                this.R.put(childAt, new n(childAt));
            }
            this.f3169f0 = true;
            this.W0.d(this.f3939c, null, this.E.i(i8));
            n0();
            this.W0.a();
            Y();
            int width = getWidth();
            int height = getHeight();
            for (int i13 = 0; i13 < childCount; i13++) {
                n nVar = this.R.get(getChildAt(i13));
                this.E.q(nVar);
                nVar.v(width, height, this.W, getNanoTime());
            }
            float w8 = this.E.w();
            if (w8 != 0.0f) {
                float f5 = Float.MAX_VALUE;
                float f8 = -3.4028235E38f;
                for (int i14 = 0; i14 < childCount; i14++) {
                    n nVar2 = this.R.get(getChildAt(i14));
                    float j8 = nVar2.j() + nVar2.i();
                    f5 = Math.min(f5, j8);
                    f8 = Math.max(f8, j8);
                }
                for (int i15 = 0; i15 < childCount; i15++) {
                    n nVar3 = this.R.get(getChildAt(i15));
                    float i16 = nVar3.i();
                    float j9 = nVar3.j();
                    nVar3.f3399m = 1.0f / (1.0f - w8);
                    nVar3.f3398l = w8 - ((((i16 + j9) - f5) * w8) / (f8 - f5));
                }
            }
            this.f3163a0 = 0.0f;
            this.f3165b0 = 0.0f;
            this.f3169f0 = true;
            invalidate();
        }
    }
}
