package androidx.constraintlayout.motion.widget;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class g {

    /* renamed from: a  reason: collision with root package name */
    private l0.b f3289a;

    /* renamed from: b  reason: collision with root package name */
    private d f3290b;

    /* renamed from: c  reason: collision with root package name */
    protected ConstraintAttribute f3291c;

    /* renamed from: d  reason: collision with root package name */
    private String f3292d;

    /* renamed from: e  reason: collision with root package name */
    private int f3293e = 0;

    /* renamed from: f  reason: collision with root package name */
    public int f3294f = 0;

    /* renamed from: g  reason: collision with root package name */
    ArrayList<p> f3295g = new ArrayList<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Comparator<p> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(p pVar, p pVar2) {
            return Integer.compare(pVar.f3312a, pVar2.f3312a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b extends g {
        b() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            view.setAlpha(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c extends g {

        /* renamed from: h  reason: collision with root package name */
        float[] f3297h = new float[1];

        c() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            this.f3297h[0] = a(f5);
            this.f3291c.i(view, this.f3297h);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {

        /* renamed from: a  reason: collision with root package name */
        private final int f3298a;

        /* renamed from: c  reason: collision with root package name */
        float[] f3300c;

        /* renamed from: d  reason: collision with root package name */
        double[] f3301d;

        /* renamed from: e  reason: collision with root package name */
        float[] f3302e;

        /* renamed from: f  reason: collision with root package name */
        float[] f3303f;

        /* renamed from: g  reason: collision with root package name */
        float[] f3304g;

        /* renamed from: h  reason: collision with root package name */
        int f3305h;

        /* renamed from: i  reason: collision with root package name */
        l0.b f3306i;

        /* renamed from: j  reason: collision with root package name */
        double[] f3307j;

        /* renamed from: k  reason: collision with root package name */
        double[] f3308k;

        /* renamed from: l  reason: collision with root package name */
        float f3309l;

        /* renamed from: b  reason: collision with root package name */
        l0.f f3299b = new l0.f();

        /* renamed from: m  reason: collision with root package name */
        public HashMap<String, ConstraintAttribute> f3310m = new HashMap<>();

        d(int i8, int i9, int i10) {
            this.f3305h = i8;
            this.f3298a = i9;
            this.f3299b.g(i8);
            this.f3300c = new float[i10];
            this.f3301d = new double[i10];
            this.f3302e = new float[i10];
            this.f3303f = new float[i10];
            this.f3304g = new float[i10];
        }

        public double a(float f5) {
            l0.b bVar = this.f3306i;
            if (bVar != null) {
                double d8 = f5;
                bVar.g(d8, this.f3308k);
                this.f3306i.d(d8, this.f3307j);
            } else {
                double[] dArr = this.f3308k;
                dArr[0] = 0.0d;
                dArr[1] = 0.0d;
            }
            double d9 = f5;
            double e8 = this.f3299b.e(d9);
            double d10 = this.f3299b.d(d9);
            double[] dArr2 = this.f3308k;
            return dArr2[0] + (e8 * dArr2[1]) + (d10 * this.f3307j[1]);
        }

        public double b(float f5) {
            l0.b bVar = this.f3306i;
            if (bVar != null) {
                bVar.d(f5, this.f3307j);
            } else {
                double[] dArr = this.f3307j;
                dArr[0] = this.f3303f[0];
                dArr[1] = this.f3300c[0];
            }
            return this.f3307j[0] + (this.f3299b.e(f5) * this.f3307j[1]);
        }

        public void c(int i8, int i9, float f5, float f8, float f9) {
            this.f3301d[i8] = i9 / 100.0d;
            this.f3302e[i8] = f5;
            this.f3303f[i8] = f8;
            this.f3300c[i8] = f9;
        }

        public void d(float f5) {
            float[] fArr;
            this.f3309l = f5;
            double[][] dArr = (double[][]) Array.newInstance(double.class, this.f3301d.length, 2);
            float[] fArr2 = this.f3300c;
            this.f3307j = new double[fArr2.length + 1];
            this.f3308k = new double[fArr2.length + 1];
            if (this.f3301d[0] > 0.0d) {
                this.f3299b.a(0.0d, this.f3302e[0]);
            }
            double[] dArr2 = this.f3301d;
            int length = dArr2.length - 1;
            if (dArr2[length] < 1.0d) {
                this.f3299b.a(1.0d, this.f3302e[length]);
            }
            for (int i8 = 0; i8 < dArr.length; i8++) {
                dArr[i8][0] = this.f3303f[i8];
                int i9 = 0;
                while (true) {
                    if (i9 < this.f3300c.length) {
                        dArr[i9][1] = fArr[i9];
                        i9++;
                    }
                }
                this.f3299b.a(this.f3301d[i8], this.f3302e[i8]);
            }
            this.f3299b.f();
            double[] dArr3 = this.f3301d;
            this.f3306i = dArr3.length > 1 ? l0.b.a(0, dArr3, dArr) : null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e extends g {
        e() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setElevation(a(f5));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f extends g {
        f() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
        }

        public void j(View view, float f5, double d8, double d9) {
            view.setRotation(a(f5) + ((float) Math.toDegrees(Math.atan2(d9, d8))));
        }
    }

    /* renamed from: androidx.constraintlayout.motion.widget.g$g  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class C0025g extends g {

        /* renamed from: h  reason: collision with root package name */
        boolean f3311h = false;

        C0025g() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            if (view instanceof MotionLayout) {
                ((MotionLayout) view).setProgress(a(f5));
            } else if (this.f3311h) {
            } else {
                Method method = null;
                try {
                    method = view.getClass().getMethod("setProgress", Float.TYPE);
                } catch (NoSuchMethodException unused) {
                    this.f3311h = true;
                }
                if (method != null) {
                    try {
                        method.invoke(view, Float.valueOf(a(f5)));
                    } catch (IllegalAccessException | InvocationTargetException e8) {
                        Log.e("KeyCycleOscillator", "unable to setProgress", e8);
                    }
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class h extends g {
        h() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            view.setRotation(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class i extends g {
        i() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            view.setRotationX(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class j extends g {
        j() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            view.setRotationY(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class k extends g {
        k() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            view.setScaleX(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class l extends g {
        l() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            view.setScaleY(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class m extends g {
        m() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            view.setTranslationX(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class n extends g {
        n() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            view.setTranslationY(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class o extends g {
        o() {
        }

        @Override // androidx.constraintlayout.motion.widget.g
        public void f(View view, float f5) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setTranslationZ(a(f5));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class p {

        /* renamed from: a  reason: collision with root package name */
        int f3312a;

        /* renamed from: b  reason: collision with root package name */
        float f3313b;

        /* renamed from: c  reason: collision with root package name */
        float f3314c;

        /* renamed from: d  reason: collision with root package name */
        float f3315d;

        public p(int i8, float f5, float f8, float f9) {
            this.f3312a = i8;
            this.f3313b = f9;
            this.f3314c = f8;
            this.f3315d = f5;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static g c(String str) {
        if (str.startsWith("CUSTOM")) {
            return new c();
        }
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1249320806:
                if (str.equals("rotationX")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1249320805:
                if (str.equals("rotationY")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1225497657:
                if (str.equals("translationX")) {
                    c9 = 2;
                    break;
                }
                break;
            case -1225497656:
                if (str.equals("translationY")) {
                    c9 = 3;
                    break;
                }
                break;
            case -1225497655:
                if (str.equals("translationZ")) {
                    c9 = 4;
                    break;
                }
                break;
            case -1001078227:
                if (str.equals("progress")) {
                    c9 = 5;
                    break;
                }
                break;
            case -908189618:
                if (str.equals("scaleX")) {
                    c9 = 6;
                    break;
                }
                break;
            case -908189617:
                if (str.equals("scaleY")) {
                    c9 = 7;
                    break;
                }
                break;
            case -797520672:
                if (str.equals("waveVariesBy")) {
                    c9 = '\b';
                    break;
                }
                break;
            case -40300674:
                if (str.equals("rotation")) {
                    c9 = '\t';
                    break;
                }
                break;
            case -4379043:
                if (str.equals("elevation")) {
                    c9 = '\n';
                    break;
                }
                break;
            case 37232917:
                if (str.equals("transitionPathRotate")) {
                    c9 = 11;
                    break;
                }
                break;
            case 92909918:
                if (str.equals("alpha")) {
                    c9 = '\f';
                    break;
                }
                break;
            case 156108012:
                if (str.equals("waveOffset")) {
                    c9 = '\r';
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return new i();
            case 1:
                return new j();
            case 2:
                return new m();
            case 3:
                return new n();
            case 4:
                return new o();
            case 5:
                return new C0025g();
            case 6:
                return new k();
            case 7:
                return new l();
            case '\b':
                return new b();
            case '\t':
                return new h();
            case '\n':
                return new e();
            case 11:
                return new f();
            case '\f':
                return new b();
            case '\r':
                return new b();
            default:
                return null;
        }
    }

    public float a(float f5) {
        return (float) this.f3290b.b(f5);
    }

    public float b(float f5) {
        return (float) this.f3290b.a(f5);
    }

    public void d(int i8, int i9, int i10, float f5, float f8, float f9) {
        this.f3295g.add(new p(i8, f5, f8, f9));
        if (i10 != -1) {
            this.f3294f = i10;
        }
        this.f3293e = i9;
    }

    public void e(int i8, int i9, int i10, float f5, float f8, float f9, ConstraintAttribute constraintAttribute) {
        this.f3295g.add(new p(i8, f5, f8, f9));
        if (i10 != -1) {
            this.f3294f = i10;
        }
        this.f3293e = i9;
        this.f3291c = constraintAttribute;
    }

    public abstract void f(View view, float f5);

    public void g(String str) {
        this.f3292d = str;
    }

    @TargetApi(19)
    public void h(float f5) {
        int size = this.f3295g.size();
        if (size == 0) {
            return;
        }
        Collections.sort(this.f3295g, new a());
        double[] dArr = new double[size];
        double[][] dArr2 = (double[][]) Array.newInstance(double.class, size, 2);
        this.f3290b = new d(this.f3293e, this.f3294f, size);
        Iterator<p> it = this.f3295g.iterator();
        int i8 = 0;
        while (it.hasNext()) {
            p next = it.next();
            float f8 = next.f3315d;
            dArr[i8] = f8 * 0.01d;
            double[] dArr3 = dArr2[i8];
            float f9 = next.f3313b;
            dArr3[0] = f9;
            double[] dArr4 = dArr2[i8];
            float f10 = next.f3314c;
            dArr4[1] = f10;
            this.f3290b.c(i8, next.f3312a, f8, f10, f9);
            i8++;
        }
        this.f3290b.d(f5);
        this.f3289a = l0.b.a(0, dArr, dArr2);
    }

    public boolean i() {
        return this.f3294f == 1;
    }

    public String toString() {
        String str = this.f3292d;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        Iterator<p> it = this.f3295g.iterator();
        while (it.hasNext()) {
            p next = it.next();
            str = str + "[" + next.f3312a + " , " + decimalFormat.format(next.f3313b) + "] ";
        }
        return str;
    }
}
