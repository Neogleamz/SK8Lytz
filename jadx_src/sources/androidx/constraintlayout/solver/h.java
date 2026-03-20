package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.b;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h implements b.a {

    /* renamed from: n  reason: collision with root package name */
    private static float f3593n = 0.001f;

    /* renamed from: a  reason: collision with root package name */
    private final int f3594a = -1;

    /* renamed from: b  reason: collision with root package name */
    private int f3595b = 16;

    /* renamed from: c  reason: collision with root package name */
    private int f3596c = 16;

    /* renamed from: d  reason: collision with root package name */
    int[] f3597d = new int[16];

    /* renamed from: e  reason: collision with root package name */
    int[] f3598e = new int[16];

    /* renamed from: f  reason: collision with root package name */
    int[] f3599f = new int[16];

    /* renamed from: g  reason: collision with root package name */
    float[] f3600g = new float[16];

    /* renamed from: h  reason: collision with root package name */
    int[] f3601h = new int[16];

    /* renamed from: i  reason: collision with root package name */
    int[] f3602i = new int[16];

    /* renamed from: j  reason: collision with root package name */
    int f3603j = 0;

    /* renamed from: k  reason: collision with root package name */
    int f3604k = -1;

    /* renamed from: l  reason: collision with root package name */
    private final b f3605l;

    /* renamed from: m  reason: collision with root package name */
    protected final c f3606m;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(b bVar, c cVar) {
        this.f3605l = bVar;
        this.f3606m = cVar;
        clear();
    }

    private void k(SolverVariable solverVariable, int i8) {
        int[] iArr;
        int i9 = solverVariable.f3520c % this.f3596c;
        int[] iArr2 = this.f3597d;
        int i10 = iArr2[i9];
        if (i10 == -1) {
            iArr2[i9] = i8;
        } else {
            while (true) {
                iArr = this.f3598e;
                if (iArr[i10] == -1) {
                    break;
                }
                i10 = iArr[i10];
            }
            iArr[i10] = i8;
        }
        this.f3598e[i8] = -1;
    }

    private void l(int i8, SolverVariable solverVariable, float f5) {
        this.f3599f[i8] = solverVariable.f3520c;
        this.f3600g[i8] = f5;
        this.f3601h[i8] = -1;
        this.f3602i[i8] = -1;
        solverVariable.a(this.f3605l);
        solverVariable.f3530m++;
        this.f3603j++;
    }

    private int m() {
        for (int i8 = 0; i8 < this.f3595b; i8++) {
            if (this.f3599f[i8] == -1) {
                return i8;
            }
        }
        return -1;
    }

    private void n() {
        int i8 = this.f3595b * 2;
        this.f3599f = Arrays.copyOf(this.f3599f, i8);
        this.f3600g = Arrays.copyOf(this.f3600g, i8);
        this.f3601h = Arrays.copyOf(this.f3601h, i8);
        this.f3602i = Arrays.copyOf(this.f3602i, i8);
        this.f3598e = Arrays.copyOf(this.f3598e, i8);
        for (int i9 = this.f3595b; i9 < i8; i9++) {
            this.f3599f[i9] = -1;
            this.f3598e[i9] = -1;
        }
        this.f3595b = i8;
    }

    private void p(int i8, SolverVariable solverVariable, float f5) {
        int m8 = m();
        l(m8, solverVariable, f5);
        if (i8 != -1) {
            this.f3601h[m8] = i8;
            int[] iArr = this.f3602i;
            iArr[m8] = iArr[i8];
            iArr[i8] = m8;
        } else {
            this.f3601h[m8] = -1;
            if (this.f3603j > 0) {
                this.f3602i[m8] = this.f3604k;
                this.f3604k = m8;
            } else {
                this.f3602i[m8] = -1;
            }
        }
        int[] iArr2 = this.f3602i;
        if (iArr2[m8] != -1) {
            this.f3601h[iArr2[m8]] = m8;
        }
        k(solverVariable, m8);
    }

    private void q(SolverVariable solverVariable) {
        int[] iArr;
        int i8 = solverVariable.f3520c;
        int i9 = i8 % this.f3596c;
        int[] iArr2 = this.f3597d;
        int i10 = iArr2[i9];
        if (i10 == -1) {
            return;
        }
        if (this.f3599f[i10] == i8) {
            int[] iArr3 = this.f3598e;
            iArr2[i9] = iArr3[i10];
            iArr3[i10] = -1;
            return;
        }
        while (true) {
            iArr = this.f3598e;
            if (iArr[i10] == -1 || this.f3599f[iArr[i10]] == i8) {
                break;
            }
            i10 = iArr[i10];
        }
        int i11 = iArr[i10];
        if (i11 == -1 || this.f3599f[i11] != i8) {
            return;
        }
        iArr[i10] = iArr[i11];
        iArr[i11] = -1;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public int a() {
        return this.f3603j;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public SolverVariable b(int i8) {
        int i9 = this.f3603j;
        if (i9 == 0) {
            return null;
        }
        int i10 = this.f3604k;
        for (int i11 = 0; i11 < i9; i11++) {
            if (i11 == i8 && i10 != -1) {
                return this.f3606m.f3559d[this.f3599f[i10]];
            }
            i10 = this.f3602i[i10];
            if (i10 == -1) {
                break;
            }
        }
        return null;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public float c(int i8) {
        int i9 = this.f3603j;
        int i10 = this.f3604k;
        for (int i11 = 0; i11 < i9; i11++) {
            if (i11 == i8) {
                return this.f3600g[i10];
            }
            i10 = this.f3602i[i10];
            if (i10 == -1) {
                return 0.0f;
            }
        }
        return 0.0f;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public void clear() {
        int i8 = this.f3603j;
        for (int i9 = 0; i9 < i8; i9++) {
            SolverVariable b9 = b(i9);
            if (b9 != null) {
                b9.c(this.f3605l);
            }
        }
        for (int i10 = 0; i10 < this.f3595b; i10++) {
            this.f3599f[i10] = -1;
            this.f3598e[i10] = -1;
        }
        for (int i11 = 0; i11 < this.f3596c; i11++) {
            this.f3597d[i11] = -1;
        }
        this.f3603j = 0;
        this.f3604k = -1;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public void d(SolverVariable solverVariable, float f5, boolean z4) {
        float f8 = f3593n;
        if (f5 <= (-f8) || f5 >= f8) {
            int o5 = o(solverVariable);
            if (o5 == -1) {
                h(solverVariable, f5);
                return;
            }
            float[] fArr = this.f3600g;
            fArr[o5] = fArr[o5] + f5;
            float f9 = fArr[o5];
            float f10 = f3593n;
            if (f9 <= (-f10) || fArr[o5] >= f10) {
                return;
            }
            fArr[o5] = 0.0f;
            i(solverVariable, z4);
        }
    }

    @Override // androidx.constraintlayout.solver.b.a
    public float e(SolverVariable solverVariable) {
        int o5 = o(solverVariable);
        if (o5 != -1) {
            return this.f3600g[o5];
        }
        return 0.0f;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public boolean f(SolverVariable solverVariable) {
        return o(solverVariable) != -1;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public float g(b bVar, boolean z4) {
        float e8 = e(bVar.f3550a);
        i(bVar.f3550a, z4);
        h hVar = (h) bVar.f3554e;
        int a9 = hVar.a();
        int i8 = 0;
        int i9 = 0;
        while (i8 < a9) {
            int[] iArr = hVar.f3599f;
            if (iArr[i9] != -1) {
                d(this.f3606m.f3559d[iArr[i9]], hVar.f3600g[i9] * e8, z4);
                i8++;
            }
            i9++;
        }
        return e8;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public void h(SolverVariable solverVariable, float f5) {
        float f8 = f3593n;
        if (f5 > (-f8) && f5 < f8) {
            i(solverVariable, true);
            return;
        }
        if (this.f3603j == 0) {
            l(0, solverVariable, f5);
            k(solverVariable, 0);
            this.f3604k = 0;
            return;
        }
        int o5 = o(solverVariable);
        if (o5 != -1) {
            this.f3600g[o5] = f5;
            return;
        }
        if (this.f3603j + 1 >= this.f3595b) {
            n();
        }
        int i8 = this.f3603j;
        int i9 = this.f3604k;
        int i10 = -1;
        for (int i11 = 0; i11 < i8; i11++) {
            int[] iArr = this.f3599f;
            int i12 = iArr[i9];
            int i13 = solverVariable.f3520c;
            if (i12 == i13) {
                this.f3600g[i9] = f5;
                return;
            }
            if (iArr[i9] < i13) {
                i10 = i9;
            }
            i9 = this.f3602i[i9];
            if (i9 == -1) {
                break;
            }
        }
        p(i10, solverVariable, f5);
    }

    @Override // androidx.constraintlayout.solver.b.a
    public float i(SolverVariable solverVariable, boolean z4) {
        int o5 = o(solverVariable);
        if (o5 == -1) {
            return 0.0f;
        }
        q(solverVariable);
        float f5 = this.f3600g[o5];
        if (this.f3604k == o5) {
            this.f3604k = this.f3602i[o5];
        }
        this.f3599f[o5] = -1;
        int[] iArr = this.f3601h;
        if (iArr[o5] != -1) {
            int[] iArr2 = this.f3602i;
            iArr2[iArr[o5]] = iArr2[o5];
        }
        int[] iArr3 = this.f3602i;
        if (iArr3[o5] != -1) {
            iArr[iArr3[o5]] = iArr[o5];
        }
        this.f3603j--;
        solverVariable.f3530m--;
        if (z4) {
            solverVariable.c(this.f3605l);
        }
        return f5;
    }

    @Override // androidx.constraintlayout.solver.b.a
    public void invert() {
        int i8 = this.f3603j;
        int i9 = this.f3604k;
        for (int i10 = 0; i10 < i8; i10++) {
            float[] fArr = this.f3600g;
            fArr[i9] = fArr[i9] * (-1.0f);
            i9 = this.f3602i[i9];
            if (i9 == -1) {
                return;
            }
        }
    }

    @Override // androidx.constraintlayout.solver.b.a
    public void j(float f5) {
        int i8 = this.f3603j;
        int i9 = this.f3604k;
        for (int i10 = 0; i10 < i8; i10++) {
            float[] fArr = this.f3600g;
            fArr[i9] = fArr[i9] / f5;
            i9 = this.f3602i[i9];
            if (i9 == -1) {
                return;
            }
        }
    }

    public int o(SolverVariable solverVariable) {
        int[] iArr;
        if (this.f3603j == 0) {
            return -1;
        }
        int i8 = solverVariable.f3520c;
        int i9 = this.f3597d[i8 % this.f3596c];
        if (i9 == -1) {
            return -1;
        }
        if (this.f3599f[i9] == i8) {
            return i9;
        }
        while (true) {
            iArr = this.f3598e;
            if (iArr[i9] == -1 || this.f3599f[iArr[i9]] == i8) {
                break;
            }
            i9 = iArr[i9];
        }
        if (iArr[i9] != -1 && this.f3599f[iArr[i9]] == i8) {
            return iArr[i9];
        }
        return -1;
    }

    public String toString() {
        StringBuilder sb;
        String str = hashCode() + " { ";
        int i8 = this.f3603j;
        for (int i9 = 0; i9 < i8; i9++) {
            SolverVariable b9 = b(i9);
            if (b9 != null) {
                String str2 = str + b9 + " = " + c(i9) + " ";
                int o5 = o(b9);
                String str3 = str2 + "[p: ";
                if (this.f3601h[o5] != -1) {
                    sb = new StringBuilder();
                    sb.append(str3);
                    sb.append(this.f3606m.f3559d[this.f3599f[this.f3601h[o5]]]);
                } else {
                    sb = new StringBuilder();
                    sb.append(str3);
                    sb.append("none");
                }
                String str4 = sb.toString() + ", n: ";
                str = (this.f3602i[o5] != -1 ? str4 + this.f3606m.f3559d[this.f3599f[this.f3602i[o5]]] : str4 + "none") + "]";
            }
        }
        return str + " }";
    }
}
