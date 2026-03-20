package androidx.constraintlayout.motion.widget;

import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class r {

    /* renamed from: a  reason: collision with root package name */
    protected l0.b f3467a;

    /* renamed from: b  reason: collision with root package name */
    protected int[] f3468b = new int[10];

    /* renamed from: c  reason: collision with root package name */
    protected float[] f3469c = new float[10];

    /* renamed from: d  reason: collision with root package name */
    private int f3470d;

    /* renamed from: e  reason: collision with root package name */
    private String f3471e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a extends r {
        a() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            view.setAlpha(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b extends r {

        /* renamed from: f  reason: collision with root package name */
        String f3472f;

        /* renamed from: g  reason: collision with root package name */
        SparseArray<ConstraintAttribute> f3473g;

        /* renamed from: h  reason: collision with root package name */
        float[] f3474h;

        public b(String str, SparseArray<ConstraintAttribute> sparseArray) {
            this.f3472f = str.split(",")[1];
            this.f3473g = sparseArray;
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void e(int i8, float f5) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute)");
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            this.f3467a.e(f5, this.f3474h);
            this.f3473g.valueAt(0).i(view, this.f3474h);
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void h(int i8) {
            float[] fArr;
            int size = this.f3473g.size();
            int f5 = this.f3473g.valueAt(0).f();
            double[] dArr = new double[size];
            this.f3474h = new float[f5];
            double[][] dArr2 = (double[][]) Array.newInstance(double.class, size, f5);
            for (int i9 = 0; i9 < size; i9++) {
                dArr[i9] = this.f3473g.keyAt(i9) * 0.01d;
                this.f3473g.valueAt(i9).e(this.f3474h);
                int i10 = 0;
                while (true) {
                    if (i10 < this.f3474h.length) {
                        dArr2[i9][i10] = fArr[i10];
                        i10++;
                    }
                }
            }
            this.f3467a = l0.b.a(i8, dArr, dArr2);
        }

        public void i(int i8, ConstraintAttribute constraintAttribute) {
            this.f3473g.append(i8, constraintAttribute);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c extends r {
        c() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setElevation(a(f5));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d extends r {
        d() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
        }

        public void i(View view, float f5, double d8, double d9) {
            view.setRotation(a(f5) + ((float) Math.toDegrees(Math.atan2(d9, d8))));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e extends r {
        e() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            view.setPivotX(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class f extends r {
        f() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            view.setPivotY(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class g extends r {

        /* renamed from: f  reason: collision with root package name */
        boolean f3475f = false;

        g() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            if (view instanceof MotionLayout) {
                ((MotionLayout) view).setProgress(a(f5));
            } else if (this.f3475f) {
            } else {
                Method method = null;
                try {
                    method = view.getClass().getMethod("setProgress", Float.TYPE);
                } catch (NoSuchMethodException unused) {
                    this.f3475f = true;
                }
                if (method != null) {
                    try {
                        method.invoke(view, Float.valueOf(a(f5)));
                    } catch (IllegalAccessException | InvocationTargetException e8) {
                        Log.e("SplineSet", "unable to setProgress", e8);
                    }
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class h extends r {
        h() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            view.setRotation(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class i extends r {
        i() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            view.setRotationX(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class j extends r {
        j() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            view.setRotationY(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class k extends r {
        k() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            view.setScaleX(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class l extends r {
        l() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            view.setScaleY(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class m {
        static void a(int[] iArr, float[] fArr, int i8, int i9) {
            int[] iArr2 = new int[iArr.length + 10];
            iArr2[0] = i9;
            iArr2[1] = i8;
            int i10 = 2;
            while (i10 > 0) {
                int i11 = i10 - 1;
                int i12 = iArr2[i11];
                i10 = i11 - 1;
                int i13 = iArr2[i10];
                if (i12 < i13) {
                    int b9 = b(iArr, fArr, i12, i13);
                    int i14 = i10 + 1;
                    iArr2[i10] = b9 - 1;
                    int i15 = i14 + 1;
                    iArr2[i14] = i12;
                    int i16 = i15 + 1;
                    iArr2[i15] = i13;
                    i10 = i16 + 1;
                    iArr2[i16] = b9 + 1;
                }
            }
        }

        private static int b(int[] iArr, float[] fArr, int i8, int i9) {
            int i10 = iArr[i9];
            int i11 = i8;
            while (i8 < i9) {
                if (iArr[i8] <= i10) {
                    c(iArr, fArr, i11, i8);
                    i11++;
                }
                i8++;
            }
            c(iArr, fArr, i11, i9);
            return i11;
        }

        private static void c(int[] iArr, float[] fArr, int i8, int i9) {
            int i10 = iArr[i8];
            iArr[i8] = iArr[i9];
            iArr[i9] = i10;
            float f5 = fArr[i8];
            fArr[i8] = fArr[i9];
            fArr[i9] = f5;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class n extends r {
        n() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            view.setTranslationX(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class o extends r {
        o() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            view.setTranslationY(a(f5));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class p extends r {
        p() {
        }

        @Override // androidx.constraintlayout.motion.widget.r
        public void f(View view, float f5) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setTranslationZ(a(f5));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static r c(String str, SparseArray<ConstraintAttribute> sparseArray) {
        return new b(str, sparseArray);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static r d(String str) {
        str.hashCode();
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
            case -760884510:
                if (str.equals("transformPivotX")) {
                    c9 = '\t';
                    break;
                }
                break;
            case -760884509:
                if (str.equals("transformPivotY")) {
                    c9 = '\n';
                    break;
                }
                break;
            case -40300674:
                if (str.equals("rotation")) {
                    c9 = 11;
                    break;
                }
                break;
            case -4379043:
                if (str.equals("elevation")) {
                    c9 = '\f';
                    break;
                }
                break;
            case 37232917:
                if (str.equals("transitionPathRotate")) {
                    c9 = '\r';
                    break;
                }
                break;
            case 92909918:
                if (str.equals("alpha")) {
                    c9 = 14;
                    break;
                }
                break;
            case 156108012:
                if (str.equals("waveOffset")) {
                    c9 = 15;
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
                return new n();
            case 3:
                return new o();
            case 4:
                return new p();
            case 5:
                return new g();
            case 6:
                return new k();
            case 7:
                return new l();
            case '\b':
                return new a();
            case '\t':
                return new e();
            case '\n':
                return new f();
            case 11:
                return new h();
            case '\f':
                return new c();
            case '\r':
                return new d();
            case 14:
                return new a();
            case 15:
                return new a();
            default:
                return null;
        }
    }

    public float a(float f5) {
        return (float) this.f3467a.c(f5, 0);
    }

    public float b(float f5) {
        return (float) this.f3467a.f(f5, 0);
    }

    public void e(int i8, float f5) {
        int[] iArr = this.f3468b;
        if (iArr.length < this.f3470d + 1) {
            this.f3468b = Arrays.copyOf(iArr, iArr.length * 2);
            float[] fArr = this.f3469c;
            this.f3469c = Arrays.copyOf(fArr, fArr.length * 2);
        }
        int[] iArr2 = this.f3468b;
        int i9 = this.f3470d;
        iArr2[i9] = i8;
        this.f3469c[i9] = f5;
        this.f3470d = i9 + 1;
    }

    public abstract void f(View view, float f5);

    public void g(String str) {
        this.f3471e = str;
    }

    public void h(int i8) {
        int i9;
        int i10 = this.f3470d;
        if (i10 == 0) {
            return;
        }
        m.a(this.f3468b, this.f3469c, 0, i10 - 1);
        int i11 = 1;
        for (int i12 = 1; i12 < this.f3470d; i12++) {
            int[] iArr = this.f3468b;
            if (iArr[i12 - 1] != iArr[i12]) {
                i11++;
            }
        }
        double[] dArr = new double[i11];
        double[][] dArr2 = (double[][]) Array.newInstance(double.class, i11, 1);
        int i13 = 0;
        for (i9 = 0; i9 < this.f3470d; i9 = i9 + 1) {
            if (i9 > 0) {
                int[] iArr2 = this.f3468b;
                i9 = iArr2[i9] == iArr2[i9 - 1] ? i9 + 1 : 0;
            }
            dArr[i13] = this.f3468b[i9] * 0.01d;
            dArr2[i13][0] = this.f3469c[i9];
            i13++;
        }
        this.f3467a = l0.b.a(i8, dArr, dArr2);
    }

    public String toString() {
        String str = this.f3471e;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        for (int i8 = 0; i8 < this.f3470d; i8++) {
            str = str + "[" + this.f3468b[i8] + " , " + decimalFormat.format(this.f3469c[i8]) + "] ";
        }
        return str;
    }
}
