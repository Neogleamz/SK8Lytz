package androidx.constraintlayout.solver;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Arrays;
import java.util.HashSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SolverVariable {

    /* renamed from: o  reason: collision with root package name */
    private static int f3517o = 1;

    /* renamed from: a  reason: collision with root package name */
    public boolean f3518a;

    /* renamed from: b  reason: collision with root package name */
    private String f3519b;

    /* renamed from: f  reason: collision with root package name */
    public float f3523f;

    /* renamed from: j  reason: collision with root package name */
    Type f3527j;

    /* renamed from: c  reason: collision with root package name */
    public int f3520c = -1;

    /* renamed from: d  reason: collision with root package name */
    int f3521d = -1;

    /* renamed from: e  reason: collision with root package name */
    public int f3522e = 0;

    /* renamed from: g  reason: collision with root package name */
    public boolean f3524g = false;

    /* renamed from: h  reason: collision with root package name */
    float[] f3525h = new float[9];

    /* renamed from: i  reason: collision with root package name */
    float[] f3526i = new float[9];

    /* renamed from: k  reason: collision with root package name */
    b[] f3528k = new b[16];

    /* renamed from: l  reason: collision with root package name */
    int f3529l = 0;

    /* renamed from: m  reason: collision with root package name */
    public int f3530m = 0;

    /* renamed from: n  reason: collision with root package name */
    HashSet<b> f3531n = null;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum Type {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN
    }

    public SolverVariable(Type type, String str) {
        this.f3527j = type;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b() {
        f3517o++;
    }

    public final void a(b bVar) {
        int i8 = 0;
        while (true) {
            int i9 = this.f3529l;
            if (i8 >= i9) {
                b[] bVarArr = this.f3528k;
                if (i9 >= bVarArr.length) {
                    this.f3528k = (b[]) Arrays.copyOf(bVarArr, bVarArr.length * 2);
                }
                b[] bVarArr2 = this.f3528k;
                int i10 = this.f3529l;
                bVarArr2[i10] = bVar;
                this.f3529l = i10 + 1;
                return;
            } else if (this.f3528k[i8] == bVar) {
                return;
            } else {
                i8++;
            }
        }
    }

    public final void c(b bVar) {
        int i8 = this.f3529l;
        int i9 = 0;
        while (i9 < i8) {
            if (this.f3528k[i9] == bVar) {
                while (i9 < i8 - 1) {
                    b[] bVarArr = this.f3528k;
                    int i10 = i9 + 1;
                    bVarArr[i9] = bVarArr[i10];
                    i9 = i10;
                }
                this.f3529l--;
                return;
            }
            i9++;
        }
    }

    public void d() {
        this.f3519b = null;
        this.f3527j = Type.UNKNOWN;
        this.f3522e = 0;
        this.f3520c = -1;
        this.f3521d = -1;
        this.f3523f = 0.0f;
        this.f3524g = false;
        int i8 = this.f3529l;
        for (int i9 = 0; i9 < i8; i9++) {
            this.f3528k[i9] = null;
        }
        this.f3529l = 0;
        this.f3530m = 0;
        this.f3518a = false;
        Arrays.fill(this.f3526i, 0.0f);
    }

    public void e(d dVar, float f5) {
        this.f3523f = f5;
        this.f3524g = true;
        int i8 = this.f3529l;
        for (int i9 = 0; i9 < i8; i9++) {
            this.f3528k[i9].B(dVar, this, false);
        }
        this.f3529l = 0;
    }

    public void f(Type type, String str) {
        this.f3527j = type;
    }

    public final void g(b bVar) {
        int i8 = this.f3529l;
        for (int i9 = 0; i9 < i8; i9++) {
            this.f3528k[i9].C(bVar, false);
        }
        this.f3529l = 0;
    }

    public String toString() {
        StringBuilder sb;
        if (this.f3519b != null) {
            sb = new StringBuilder();
            sb.append(BuildConfig.FLAVOR);
            sb.append(this.f3519b);
        } else {
            sb = new StringBuilder();
            sb.append(BuildConfig.FLAVOR);
            sb.append(this.f3520c);
        }
        return sb.toString();
    }
}
