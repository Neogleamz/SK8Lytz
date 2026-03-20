package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.b;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.Arrays;
import java.util.Comparator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g extends androidx.constraintlayout.solver.b {

    /* renamed from: g  reason: collision with root package name */
    private int f3583g;

    /* renamed from: h  reason: collision with root package name */
    private SolverVariable[] f3584h;

    /* renamed from: i  reason: collision with root package name */
    private SolverVariable[] f3585i;

    /* renamed from: j  reason: collision with root package name */
    private int f3586j;

    /* renamed from: k  reason: collision with root package name */
    b f3587k;

    /* renamed from: l  reason: collision with root package name */
    c f3588l;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Comparator<SolverVariable> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(SolverVariable solverVariable, SolverVariable solverVariable2) {
            return solverVariable.f3520c - solverVariable2.f3520c;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Comparable {

        /* renamed from: a  reason: collision with root package name */
        SolverVariable f3590a;

        /* renamed from: b  reason: collision with root package name */
        g f3591b;

        public b(g gVar) {
            this.f3591b = gVar;
        }

        public boolean c(SolverVariable solverVariable, float f5) {
            boolean z4 = true;
            if (!this.f3590a.f3518a) {
                for (int i8 = 0; i8 < 9; i8++) {
                    float f8 = solverVariable.f3526i[i8];
                    if (f8 != 0.0f) {
                        float f9 = f8 * f5;
                        if (Math.abs(f9) < 1.0E-4f) {
                            f9 = 0.0f;
                        }
                        this.f3590a.f3526i[i8] = f9;
                    } else {
                        this.f3590a.f3526i[i8] = 0.0f;
                    }
                }
                return true;
            }
            for (int i9 = 0; i9 < 9; i9++) {
                float[] fArr = this.f3590a.f3526i;
                fArr[i9] = fArr[i9] + (solverVariable.f3526i[i9] * f5);
                if (Math.abs(fArr[i9]) < 1.0E-4f) {
                    this.f3590a.f3526i[i9] = 0.0f;
                } else {
                    z4 = false;
                }
            }
            if (z4) {
                g.this.G(this.f3590a);
            }
            return false;
        }

        @Override // java.lang.Comparable
        public int compareTo(Object obj) {
            return this.f3590a.f3520c - ((SolverVariable) obj).f3520c;
        }

        public void f(SolverVariable solverVariable) {
            this.f3590a = solverVariable;
        }

        public final boolean h() {
            for (int i8 = 8; i8 >= 0; i8--) {
                float f5 = this.f3590a.f3526i[i8];
                if (f5 > 0.0f) {
                    return false;
                }
                if (f5 < 0.0f) {
                    return true;
                }
            }
            return false;
        }

        public final boolean i(SolverVariable solverVariable) {
            int i8 = 8;
            while (true) {
                if (i8 < 0) {
                    break;
                }
                float f5 = solverVariable.f3526i[i8];
                float f8 = this.f3590a.f3526i[i8];
                if (f8 == f5) {
                    i8--;
                } else if (f8 < f5) {
                    return true;
                }
            }
            return false;
        }

        public void j() {
            Arrays.fill(this.f3590a.f3526i, 0.0f);
        }

        public String toString() {
            String str = "[ ";
            if (this.f3590a != null) {
                for (int i8 = 0; i8 < 9; i8++) {
                    str = str + this.f3590a.f3526i[i8] + " ";
                }
            }
            return str + "] " + this.f3590a;
        }
    }

    public g(c cVar) {
        super(cVar);
        this.f3583g = RecognitionOptions.ITF;
        this.f3584h = new SolverVariable[RecognitionOptions.ITF];
        this.f3585i = new SolverVariable[RecognitionOptions.ITF];
        this.f3586j = 0;
        this.f3587k = new b(this);
        this.f3588l = cVar;
    }

    private final void F(SolverVariable solverVariable) {
        int i8;
        int i9 = this.f3586j + 1;
        SolverVariable[] solverVariableArr = this.f3584h;
        if (i9 > solverVariableArr.length) {
            SolverVariable[] solverVariableArr2 = (SolverVariable[]) Arrays.copyOf(solverVariableArr, solverVariableArr.length * 2);
            this.f3584h = solverVariableArr2;
            this.f3585i = (SolverVariable[]) Arrays.copyOf(solverVariableArr2, solverVariableArr2.length * 2);
        }
        SolverVariable[] solverVariableArr3 = this.f3584h;
        int i10 = this.f3586j;
        solverVariableArr3[i10] = solverVariable;
        int i11 = i10 + 1;
        this.f3586j = i11;
        if (i11 > 1 && solverVariableArr3[i11 - 1].f3520c > solverVariable.f3520c) {
            int i12 = 0;
            while (true) {
                i8 = this.f3586j;
                if (i12 >= i8) {
                    break;
                }
                this.f3585i[i12] = this.f3584h[i12];
                i12++;
            }
            Arrays.sort(this.f3585i, 0, i8, new a());
            for (int i13 = 0; i13 < this.f3586j; i13++) {
                this.f3584h[i13] = this.f3585i[i13];
            }
        }
        solverVariable.f3518a = true;
        solverVariable.a(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void G(SolverVariable solverVariable) {
        int i8 = 0;
        while (i8 < this.f3586j) {
            if (this.f3584h[i8] == solverVariable) {
                while (true) {
                    int i9 = this.f3586j;
                    if (i8 >= i9 - 1) {
                        this.f3586j = i9 - 1;
                        solverVariable.f3518a = false;
                        return;
                    }
                    SolverVariable[] solverVariableArr = this.f3584h;
                    int i10 = i8 + 1;
                    solverVariableArr[i8] = solverVariableArr[i10];
                    i8 = i10;
                }
            } else {
                i8++;
            }
        }
    }

    @Override // androidx.constraintlayout.solver.b
    public void C(androidx.constraintlayout.solver.b bVar, boolean z4) {
        SolverVariable solverVariable = bVar.f3550a;
        if (solverVariable == null) {
            return;
        }
        b.a aVar = bVar.f3554e;
        int a9 = aVar.a();
        for (int i8 = 0; i8 < a9; i8++) {
            SolverVariable b9 = aVar.b(i8);
            float c9 = aVar.c(i8);
            this.f3587k.f(b9);
            if (this.f3587k.c(solverVariable, c9)) {
                F(b9);
            }
            this.f3551b += bVar.f3551b * c9;
        }
        G(solverVariable);
    }

    @Override // androidx.constraintlayout.solver.b, androidx.constraintlayout.solver.d.a
    public void a(SolverVariable solverVariable) {
        this.f3587k.f(solverVariable);
        this.f3587k.j();
        solverVariable.f3526i[solverVariable.f3522e] = 1.0f;
        F(solverVariable);
    }

    @Override // androidx.constraintlayout.solver.b, androidx.constraintlayout.solver.d.a
    public SolverVariable b(d dVar, boolean[] zArr) {
        int i8 = -1;
        for (int i9 = 0; i9 < this.f3586j; i9++) {
            SolverVariable solverVariable = this.f3584h[i9];
            if (!zArr[solverVariable.f3520c]) {
                this.f3587k.f(solverVariable);
                b bVar = this.f3587k;
                if (i8 == -1) {
                    if (!bVar.h()) {
                    }
                    i8 = i9;
                } else {
                    if (!bVar.i(this.f3584h[i8])) {
                    }
                    i8 = i9;
                }
            }
        }
        if (i8 == -1) {
            return null;
        }
        return this.f3584h[i8];
    }

    @Override // androidx.constraintlayout.solver.b, androidx.constraintlayout.solver.d.a
    public void clear() {
        this.f3586j = 0;
        this.f3551b = 0.0f;
    }

    @Override // androidx.constraintlayout.solver.b
    public String toString() {
        String str = BuildConfig.FLAVOR + " goal -> (" + this.f3551b + ") : ";
        for (int i8 = 0; i8 < this.f3586j; i8++) {
            this.f3587k.f(this.f3584h[i8]);
            str = str + this.f3587k + " ";
        }
        return str;
    }
}
