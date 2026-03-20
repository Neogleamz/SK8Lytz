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
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class s {

    /* renamed from: k  reason: collision with root package name */
    private static float f3476k = 6.2831855f;

    /* renamed from: a  reason: collision with root package name */
    protected l0.b f3477a;

    /* renamed from: e  reason: collision with root package name */
    private int f3481e;

    /* renamed from: f  reason: collision with root package name */
    private String f3482f;

    /* renamed from: i  reason: collision with root package name */
    long f3485i;

    /* renamed from: b  reason: collision with root package name */
    protected int f3478b = 0;

    /* renamed from: c  reason: collision with root package name */
    protected int[] f3479c = new int[10];

    /* renamed from: d  reason: collision with root package name */
    protected float[][] f3480d = (float[][]) Array.newInstance(float.class, 10, 3);

    /* renamed from: g  reason: collision with root package name */
    private float[] f3483g = new float[3];

    /* renamed from: h  reason: collision with root package name */
    protected boolean f3484h = false;

    /* renamed from: j  reason: collision with root package name */
    float f3486j = Float.NaN;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends s {
        a() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            view.setAlpha(b(f5, j8, view, eVar));
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends s {

        /* renamed from: l  reason: collision with root package name */
        String f3487l;

        /* renamed from: m  reason: collision with root package name */
        SparseArray<ConstraintAttribute> f3488m;

        /* renamed from: n  reason: collision with root package name */
        SparseArray<float[]> f3489n = new SparseArray<>();

        /* renamed from: o  reason: collision with root package name */
        float[] f3490o;

        /* renamed from: p  reason: collision with root package name */
        float[] f3491p;

        public b(String str, SparseArray<ConstraintAttribute> sparseArray) {
            this.f3487l = str.split(",")[1];
            this.f3488m = sparseArray;
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public void e(int i8, float f5, float f8, int i9, float f9) {
            throw new RuntimeException("don't call for custom attribute call setPoint(pos, ConstraintAttribute,...)");
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            this.f3477a.e(f5, this.f3490o);
            float[] fArr = this.f3490o;
            float f8 = fArr[fArr.length - 2];
            float f9 = fArr[fArr.length - 1];
            float f10 = (float) ((this.f3486j + (((j8 - this.f3485i) * 1.0E-9d) * f8)) % 1.0d);
            this.f3486j = f10;
            this.f3485i = j8;
            float a9 = a(f10);
            this.f3484h = false;
            int i8 = 0;
            while (true) {
                float[] fArr2 = this.f3491p;
                if (i8 >= fArr2.length) {
                    break;
                }
                boolean z4 = this.f3484h;
                float[] fArr3 = this.f3490o;
                this.f3484h = z4 | (((double) fArr3[i8]) != 0.0d);
                fArr2[i8] = (fArr3[i8] * a9) + f9;
                i8++;
            }
            this.f3488m.valueAt(0).i(view, this.f3491p);
            if (f8 != 0.0f) {
                this.f3484h = true;
            }
            return this.f3484h;
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public void i(int i8) {
            float[] fArr;
            int size = this.f3488m.size();
            int f5 = this.f3488m.valueAt(0).f();
            double[] dArr = new double[size];
            int i9 = f5 + 2;
            this.f3490o = new float[i9];
            this.f3491p = new float[f5];
            double[][] dArr2 = (double[][]) Array.newInstance(double.class, size, i9);
            for (int i10 = 0; i10 < size; i10++) {
                int keyAt = this.f3488m.keyAt(i10);
                float[] valueAt = this.f3489n.valueAt(i10);
                dArr[i10] = keyAt * 0.01d;
                this.f3488m.valueAt(i10).e(this.f3490o);
                int i11 = 0;
                while (true) {
                    if (i11 < this.f3490o.length) {
                        dArr2[i10][i11] = fArr[i11];
                        i11++;
                    }
                }
                dArr2[i10][f5] = valueAt[0];
                dArr2[i10][f5 + 1] = valueAt[1];
            }
            this.f3477a = l0.b.a(i8, dArr, dArr2);
        }

        public void j(int i8, ConstraintAttribute constraintAttribute, float f5, int i9, float f8) {
            this.f3488m.append(i8, constraintAttribute);
            this.f3489n.append(i8, new float[]{f5, f8});
            this.f3478b = Math.max(this.f3478b, i9);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c extends s {
        c() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setElevation(b(f5, j8, view, eVar));
            }
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d extends s {
        d() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            return this.f3484h;
        }

        public boolean j(View view, androidx.constraintlayout.motion.widget.e eVar, float f5, long j8, double d8, double d9) {
            view.setRotation(b(f5, j8, view, eVar) + ((float) Math.toDegrees(Math.atan2(d9, d8))));
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e extends s {

        /* renamed from: l  reason: collision with root package name */
        boolean f3492l = false;

        e() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            if (view instanceof MotionLayout) {
                ((MotionLayout) view).setProgress(b(f5, j8, view, eVar));
            } else if (this.f3492l) {
                return false;
            } else {
                Method method = null;
                try {
                    method = view.getClass().getMethod("setProgress", Float.TYPE);
                } catch (NoSuchMethodException unused) {
                    this.f3492l = true;
                }
                Method method2 = method;
                if (method2 != null) {
                    try {
                        method2.invoke(view, Float.valueOf(b(f5, j8, view, eVar)));
                    } catch (IllegalAccessException | InvocationTargetException e8) {
                        Log.e("SplineSet", "unable to setProgress", e8);
                    }
                }
            }
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f extends s {
        f() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            view.setRotation(b(f5, j8, view, eVar));
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class g extends s {
        g() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            view.setRotationX(b(f5, j8, view, eVar));
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h extends s {
        h() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            view.setRotationY(b(f5, j8, view, eVar));
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class i extends s {
        i() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            view.setScaleX(b(f5, j8, view, eVar));
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class j extends s {
        j() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            view.setScaleY(b(f5, j8, view, eVar));
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class k {
        static void a(int[] iArr, float[][] fArr, int i8, int i9) {
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

        private static int b(int[] iArr, float[][] fArr, int i8, int i9) {
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

        private static void c(int[] iArr, float[][] fArr, int i8, int i9) {
            int i10 = iArr[i8];
            iArr[i8] = iArr[i9];
            iArr[i9] = i10;
            float[] fArr2 = fArr[i8];
            fArr[i8] = fArr[i9];
            fArr[i9] = fArr2;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class l extends s {
        l() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            view.setTranslationX(b(f5, j8, view, eVar));
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class m extends s {
        m() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            view.setTranslationY(b(f5, j8, view, eVar));
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class n extends s {
        n() {
        }

        @Override // androidx.constraintlayout.motion.widget.s
        public boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setTranslationZ(b(f5, j8, view, eVar));
            }
            return this.f3484h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static s c(String str, SparseArray<ConstraintAttribute> sparseArray) {
        return new b(str, sparseArray);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static s d(String str, long j8) {
        s gVar;
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
            case -40300674:
                if (str.equals("rotation")) {
                    c9 = '\b';
                    break;
                }
                break;
            case -4379043:
                if (str.equals("elevation")) {
                    c9 = '\t';
                    break;
                }
                break;
            case 37232917:
                if (str.equals("transitionPathRotate")) {
                    c9 = '\n';
                    break;
                }
                break;
            case 92909918:
                if (str.equals("alpha")) {
                    c9 = 11;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                gVar = new g();
                break;
            case 1:
                gVar = new h();
                break;
            case 2:
                gVar = new l();
                break;
            case 3:
                gVar = new m();
                break;
            case 4:
                gVar = new n();
                break;
            case 5:
                gVar = new e();
                break;
            case 6:
                gVar = new i();
                break;
            case 7:
                gVar = new j();
                break;
            case '\b':
                gVar = new f();
                break;
            case '\t':
                gVar = new c();
                break;
            case '\n':
                gVar = new d();
                break;
            case 11:
                gVar = new a();
                break;
            default:
                return null;
        }
        gVar.g(j8);
        return gVar;
    }

    protected float a(float f5) {
        float abs;
        switch (this.f3478b) {
            case 1:
                return Math.signum(f5 * f3476k);
            case 2:
                abs = Math.abs(f5);
                break;
            case 3:
                return (((f5 * 2.0f) + 1.0f) % 2.0f) - 1.0f;
            case 4:
                abs = ((f5 * 2.0f) + 1.0f) % 2.0f;
                break;
            case 5:
                return (float) Math.cos(f5 * f3476k);
            case 6:
                float abs2 = 1.0f - Math.abs(((f5 * 4.0f) % 4.0f) - 2.0f);
                abs = abs2 * abs2;
                break;
            default:
                return (float) Math.sin(f5 * f3476k);
        }
        return 1.0f - abs;
    }

    public float b(float f5, long j8, View view, androidx.constraintlayout.motion.widget.e eVar) {
        this.f3477a.e(f5, this.f3483g);
        float[] fArr = this.f3483g;
        float f8 = fArr[1];
        int i8 = (f8 > 0.0f ? 1 : (f8 == 0.0f ? 0 : -1));
        if (i8 == 0) {
            this.f3484h = false;
            return fArr[2];
        }
        if (Float.isNaN(this.f3486j)) {
            float a9 = eVar.a(view, this.f3482f, 0);
            this.f3486j = a9;
            if (Float.isNaN(a9)) {
                this.f3486j = 0.0f;
            }
        }
        float f9 = (float) ((this.f3486j + (((j8 - this.f3485i) * 1.0E-9d) * f8)) % 1.0d);
        this.f3486j = f9;
        eVar.b(view, this.f3482f, 0, f9);
        this.f3485i = j8;
        float f10 = this.f3483g[0];
        float a10 = (a(this.f3486j) * f10) + this.f3483g[2];
        this.f3484h = (f10 == 0.0f && i8 == 0) ? false : true;
        return a10;
    }

    public void e(int i8, float f5, float f8, int i9, float f9) {
        int[] iArr = this.f3479c;
        int i10 = this.f3481e;
        iArr[i10] = i8;
        float[][] fArr = this.f3480d;
        fArr[i10][0] = f5;
        fArr[i10][1] = f8;
        fArr[i10][2] = f9;
        this.f3478b = Math.max(this.f3478b, i9);
        this.f3481e++;
    }

    public abstract boolean f(View view, float f5, long j8, androidx.constraintlayout.motion.widget.e eVar);

    protected void g(long j8) {
        this.f3485i = j8;
    }

    public void h(String str) {
        this.f3482f = str;
    }

    public void i(int i8) {
        int i9;
        int i10 = this.f3481e;
        if (i10 == 0) {
            Log.e("SplineSet", "Error no points added to " + this.f3482f);
            return;
        }
        k.a(this.f3479c, this.f3480d, 0, i10 - 1);
        int i11 = 1;
        int i12 = 0;
        while (true) {
            int[] iArr = this.f3479c;
            if (i11 >= iArr.length) {
                break;
            }
            if (iArr[i11] != iArr[i11 - 1]) {
                i12++;
            }
            i11++;
        }
        if (i12 == 0) {
            i12 = 1;
        }
        double[] dArr = new double[i12];
        double[][] dArr2 = (double[][]) Array.newInstance(double.class, i12, 3);
        int i13 = 0;
        for (i9 = 0; i9 < this.f3481e; i9 = i9 + 1) {
            if (i9 > 0) {
                int[] iArr2 = this.f3479c;
                i9 = iArr2[i9] == iArr2[i9 - 1] ? i9 + 1 : 0;
            }
            dArr[i13] = this.f3479c[i9] * 0.01d;
            double[] dArr3 = dArr2[i13];
            float[][] fArr = this.f3480d;
            dArr3[0] = fArr[i9][0];
            dArr2[i13][1] = fArr[i9][1];
            dArr2[i13][2] = fArr[i9][2];
            i13++;
        }
        this.f3477a = l0.b.a(i8, dArr, dArr2);
    }

    public String toString() {
        String str = this.f3482f;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        for (int i8 = 0; i8 < this.f3481e; i8++) {
            str = str + "[" + this.f3479c[i8] + " , " + decimalFormat.format(this.f3480d[i8]) + "] ";
        }
        return str;
    }
}
