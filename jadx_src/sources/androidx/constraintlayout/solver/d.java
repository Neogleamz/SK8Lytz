package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.Arrays;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d {
    private static int q = 1000;

    /* renamed from: r  reason: collision with root package name */
    public static m0.a f3560r = null;

    /* renamed from: s  reason: collision with root package name */
    public static boolean f3561s = true;

    /* renamed from: t  reason: collision with root package name */
    public static long f3562t;

    /* renamed from: u  reason: collision with root package name */
    public static long f3563u;

    /* renamed from: c  reason: collision with root package name */
    private a f3566c;

    /* renamed from: f  reason: collision with root package name */
    androidx.constraintlayout.solver.b[] f3569f;

    /* renamed from: m  reason: collision with root package name */
    final c f3576m;

    /* renamed from: p  reason: collision with root package name */
    private a f3579p;

    /* renamed from: a  reason: collision with root package name */
    int f3564a = 0;

    /* renamed from: b  reason: collision with root package name */
    private HashMap<String, SolverVariable> f3565b = null;

    /* renamed from: d  reason: collision with root package name */
    private int f3567d = 32;

    /* renamed from: e  reason: collision with root package name */
    private int f3568e = 32;

    /* renamed from: g  reason: collision with root package name */
    public boolean f3570g = false;

    /* renamed from: h  reason: collision with root package name */
    public boolean f3571h = false;

    /* renamed from: i  reason: collision with root package name */
    private boolean[] f3572i = new boolean[32];

    /* renamed from: j  reason: collision with root package name */
    int f3573j = 1;

    /* renamed from: k  reason: collision with root package name */
    int f3574k = 0;

    /* renamed from: l  reason: collision with root package name */
    private int f3575l = 32;

    /* renamed from: n  reason: collision with root package name */
    private SolverVariable[] f3577n = new SolverVariable[q];

    /* renamed from: o  reason: collision with root package name */
    private int f3578o = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(SolverVariable solverVariable);

        SolverVariable b(d dVar, boolean[] zArr);

        void c(a aVar);

        void clear();

        SolverVariable getKey();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends androidx.constraintlayout.solver.b {
        public b(c cVar) {
            this.f3554e = new h(this, cVar);
        }
    }

    public d() {
        this.f3569f = null;
        this.f3569f = new androidx.constraintlayout.solver.b[32];
        C();
        c cVar = new c();
        this.f3576m = cVar;
        this.f3566c = new g(cVar);
        this.f3579p = f3561s ? new b(cVar) : new androidx.constraintlayout.solver.b(cVar);
    }

    private final int B(a aVar, boolean z4) {
        for (int i8 = 0; i8 < this.f3573j; i8++) {
            this.f3572i[i8] = false;
        }
        boolean z8 = false;
        int i9 = 0;
        while (!z8) {
            i9++;
            if (i9 >= this.f3573j * 2) {
                return i9;
            }
            if (aVar.getKey() != null) {
                this.f3572i[aVar.getKey().f3520c] = true;
            }
            SolverVariable b9 = aVar.b(this, this.f3572i);
            if (b9 != null) {
                boolean[] zArr = this.f3572i;
                int i10 = b9.f3520c;
                if (zArr[i10]) {
                    return i9;
                }
                zArr[i10] = true;
            }
            if (b9 != null) {
                float f5 = Float.MAX_VALUE;
                int i11 = -1;
                for (int i12 = 0; i12 < this.f3574k; i12++) {
                    androidx.constraintlayout.solver.b bVar = this.f3569f[i12];
                    if (bVar.f3550a.f3527j != SolverVariable.Type.UNRESTRICTED && !bVar.f3555f && bVar.t(b9)) {
                        float e8 = bVar.f3554e.e(b9);
                        if (e8 < 0.0f) {
                            float f8 = (-bVar.f3551b) / e8;
                            if (f8 < f5) {
                                i11 = i12;
                                f5 = f8;
                            }
                        }
                    }
                }
                if (i11 > -1) {
                    androidx.constraintlayout.solver.b bVar2 = this.f3569f[i11];
                    bVar2.f3550a.f3521d = -1;
                    bVar2.y(b9);
                    SolverVariable solverVariable = bVar2.f3550a;
                    solverVariable.f3521d = i11;
                    solverVariable.g(bVar2);
                }
            } else {
                z8 = true;
            }
        }
        return i9;
    }

    private void C() {
        int i8 = 0;
        if (f3561s) {
            while (true) {
                androidx.constraintlayout.solver.b[] bVarArr = this.f3569f;
                if (i8 >= bVarArr.length) {
                    return;
                }
                androidx.constraintlayout.solver.b bVar = bVarArr[i8];
                if (bVar != null) {
                    this.f3576m.f3556a.a(bVar);
                }
                this.f3569f[i8] = null;
                i8++;
            }
        } else {
            while (true) {
                androidx.constraintlayout.solver.b[] bVarArr2 = this.f3569f;
                if (i8 >= bVarArr2.length) {
                    return;
                }
                androidx.constraintlayout.solver.b bVar2 = bVarArr2[i8];
                if (bVar2 != null) {
                    this.f3576m.f3557b.a(bVar2);
                }
                this.f3569f[i8] = null;
                i8++;
            }
        }
    }

    private SolverVariable a(SolverVariable.Type type, String str) {
        SolverVariable b9 = this.f3576m.f3558c.b();
        if (b9 == null) {
            b9 = new SolverVariable(type, str);
        } else {
            b9.d();
        }
        b9.f(type, str);
        int i8 = this.f3578o;
        int i9 = q;
        if (i8 >= i9) {
            int i10 = i9 * 2;
            q = i10;
            this.f3577n = (SolverVariable[]) Arrays.copyOf(this.f3577n, i10);
        }
        SolverVariable[] solverVariableArr = this.f3577n;
        int i11 = this.f3578o;
        this.f3578o = i11 + 1;
        solverVariableArr[i11] = b9;
        return b9;
    }

    private final void l(androidx.constraintlayout.solver.b bVar) {
        e<androidx.constraintlayout.solver.b> eVar;
        androidx.constraintlayout.solver.b bVar2;
        if (f3561s) {
            androidx.constraintlayout.solver.b[] bVarArr = this.f3569f;
            int i8 = this.f3574k;
            if (bVarArr[i8] != null) {
                eVar = this.f3576m.f3556a;
                bVar2 = bVarArr[i8];
                eVar.a(bVar2);
            }
        } else {
            androidx.constraintlayout.solver.b[] bVarArr2 = this.f3569f;
            int i9 = this.f3574k;
            if (bVarArr2[i9] != null) {
                eVar = this.f3576m.f3557b;
                bVar2 = bVarArr2[i9];
                eVar.a(bVar2);
            }
        }
        androidx.constraintlayout.solver.b[] bVarArr3 = this.f3569f;
        int i10 = this.f3574k;
        bVarArr3[i10] = bVar;
        SolverVariable solverVariable = bVar.f3550a;
        solverVariable.f3521d = i10;
        this.f3574k = i10 + 1;
        solverVariable.g(bVar);
    }

    private void n() {
        for (int i8 = 0; i8 < this.f3574k; i8++) {
            androidx.constraintlayout.solver.b bVar = this.f3569f[i8];
            bVar.f3550a.f3523f = bVar.f3551b;
        }
    }

    public static androidx.constraintlayout.solver.b s(d dVar, SolverVariable solverVariable, SolverVariable solverVariable2, float f5) {
        return dVar.r().j(solverVariable, solverVariable2, f5);
    }

    private int u(a aVar) {
        float f5;
        boolean z4;
        int i8 = 0;
        while (true) {
            f5 = 0.0f;
            if (i8 >= this.f3574k) {
                z4 = false;
                break;
            }
            androidx.constraintlayout.solver.b[] bVarArr = this.f3569f;
            if (bVarArr[i8].f3550a.f3527j != SolverVariable.Type.UNRESTRICTED && bVarArr[i8].f3551b < 0.0f) {
                z4 = true;
                break;
            }
            i8++;
        }
        if (z4) {
            boolean z8 = false;
            int i9 = 0;
            while (!z8) {
                i9++;
                float f8 = Float.MAX_VALUE;
                int i10 = -1;
                int i11 = -1;
                int i12 = 0;
                int i13 = 0;
                while (i12 < this.f3574k) {
                    androidx.constraintlayout.solver.b bVar = this.f3569f[i12];
                    if (bVar.f3550a.f3527j != SolverVariable.Type.UNRESTRICTED && !bVar.f3555f && bVar.f3551b < f5) {
                        int i14 = 1;
                        while (i14 < this.f3573j) {
                            SolverVariable solverVariable = this.f3576m.f3559d[i14];
                            float e8 = bVar.f3554e.e(solverVariable);
                            if (e8 > f5) {
                                for (int i15 = 0; i15 < 9; i15++) {
                                    float f9 = solverVariable.f3525h[i15] / e8;
                                    if ((f9 < f8 && i15 == i13) || i15 > i13) {
                                        i13 = i15;
                                        f8 = f9;
                                        i10 = i12;
                                        i11 = i14;
                                    }
                                }
                            }
                            i14++;
                            f5 = 0.0f;
                        }
                    }
                    i12++;
                    f5 = 0.0f;
                }
                if (i10 != -1) {
                    androidx.constraintlayout.solver.b bVar2 = this.f3569f[i10];
                    bVar2.f3550a.f3521d = -1;
                    bVar2.y(this.f3576m.f3559d[i11]);
                    SolverVariable solverVariable2 = bVar2.f3550a;
                    solverVariable2.f3521d = i10;
                    solverVariable2.g(bVar2);
                } else {
                    z8 = true;
                }
                if (i9 > this.f3573j / 2) {
                    z8 = true;
                }
                f5 = 0.0f;
            }
            return i9;
        }
        return 0;
    }

    public static m0.a w() {
        return f3560r;
    }

    private void y() {
        int i8 = this.f3567d * 2;
        this.f3567d = i8;
        this.f3569f = (androidx.constraintlayout.solver.b[]) Arrays.copyOf(this.f3569f, i8);
        c cVar = this.f3576m;
        cVar.f3559d = (SolverVariable[]) Arrays.copyOf(cVar.f3559d, this.f3567d);
        int i9 = this.f3567d;
        this.f3572i = new boolean[i9];
        this.f3568e = i9;
        this.f3575l = i9;
    }

    void A(a aVar) {
        u(aVar);
        B(aVar, false);
        n();
    }

    public void D() {
        c cVar;
        int i8 = 0;
        while (true) {
            cVar = this.f3576m;
            SolverVariable[] solverVariableArr = cVar.f3559d;
            if (i8 >= solverVariableArr.length) {
                break;
            }
            SolverVariable solverVariable = solverVariableArr[i8];
            if (solverVariable != null) {
                solverVariable.d();
            }
            i8++;
        }
        cVar.f3558c.c(this.f3577n, this.f3578o);
        this.f3578o = 0;
        Arrays.fill(this.f3576m.f3559d, (Object) null);
        HashMap<String, SolverVariable> hashMap = this.f3565b;
        if (hashMap != null) {
            hashMap.clear();
        }
        this.f3564a = 0;
        this.f3566c.clear();
        this.f3573j = 1;
        for (int i9 = 0; i9 < this.f3574k; i9++) {
            this.f3569f[i9].f3552c = false;
        }
        C();
        this.f3574k = 0;
        this.f3579p = f3561s ? new b(this.f3576m) : new androidx.constraintlayout.solver.b(this.f3576m);
    }

    public void b(ConstraintWidget constraintWidget, ConstraintWidget constraintWidget2, float f5, int i8) {
        ConstraintAnchor.Type type = ConstraintAnchor.Type.LEFT;
        SolverVariable q8 = q(constraintWidget.n(type));
        ConstraintAnchor.Type type2 = ConstraintAnchor.Type.TOP;
        SolverVariable q9 = q(constraintWidget.n(type2));
        ConstraintAnchor.Type type3 = ConstraintAnchor.Type.RIGHT;
        SolverVariable q10 = q(constraintWidget.n(type3));
        ConstraintAnchor.Type type4 = ConstraintAnchor.Type.BOTTOM;
        SolverVariable q11 = q(constraintWidget.n(type4));
        SolverVariable q12 = q(constraintWidget2.n(type));
        SolverVariable q13 = q(constraintWidget2.n(type2));
        SolverVariable q14 = q(constraintWidget2.n(type3));
        SolverVariable q15 = q(constraintWidget2.n(type4));
        androidx.constraintlayout.solver.b r4 = r();
        double d8 = f5;
        double d9 = i8;
        r4.q(q9, q11, q13, q15, (float) (Math.sin(d8) * d9));
        d(r4);
        androidx.constraintlayout.solver.b r8 = r();
        r8.q(q8, q10, q12, q14, (float) (Math.cos(d8) * d9));
        d(r8);
    }

    public void c(SolverVariable solverVariable, SolverVariable solverVariable2, int i8, float f5, SolverVariable solverVariable3, SolverVariable solverVariable4, int i9, int i10) {
        androidx.constraintlayout.solver.b r4 = r();
        r4.h(solverVariable, solverVariable2, i8, f5, solverVariable3, solverVariable4, i9);
        if (i10 != 8) {
            r4.d(this, i10);
        }
        d(r4);
    }

    public void d(androidx.constraintlayout.solver.b bVar) {
        SolverVariable w8;
        if (bVar == null) {
            return;
        }
        boolean z4 = true;
        if (this.f3574k + 1 >= this.f3575l || this.f3573j + 1 >= this.f3568e) {
            y();
        }
        boolean z8 = false;
        if (!bVar.f3555f) {
            bVar.D(this);
            if (bVar.u()) {
                return;
            }
            bVar.r();
            if (bVar.f(this)) {
                SolverVariable p8 = p();
                bVar.f3550a = p8;
                l(bVar);
                this.f3579p.c(bVar);
                B(this.f3579p, true);
                if (p8.f3521d == -1) {
                    if (bVar.f3550a == p8 && (w8 = bVar.w(p8)) != null) {
                        bVar.y(w8);
                    }
                    if (!bVar.f3555f) {
                        bVar.f3550a.g(bVar);
                    }
                    this.f3574k--;
                }
            } else {
                z4 = false;
            }
            if (!bVar.s()) {
                return;
            }
            z8 = z4;
        }
        if (z8) {
            return;
        }
        l(bVar);
    }

    public androidx.constraintlayout.solver.b e(SolverVariable solverVariable, SolverVariable solverVariable2, int i8, int i9) {
        if (i9 == 8 && solverVariable2.f3524g && solverVariable.f3521d == -1) {
            solverVariable.e(this, solverVariable2.f3523f + i8);
            return null;
        }
        androidx.constraintlayout.solver.b r4 = r();
        r4.n(solverVariable, solverVariable2, i8);
        if (i9 != 8) {
            r4.d(this, i9);
        }
        d(r4);
        return r4;
    }

    public void f(SolverVariable solverVariable, int i8) {
        androidx.constraintlayout.solver.b r4;
        int i9 = solverVariable.f3521d;
        if (i9 == -1) {
            solverVariable.e(this, i8);
            return;
        }
        if (i9 != -1) {
            androidx.constraintlayout.solver.b bVar = this.f3569f[i9];
            if (!bVar.f3555f) {
                if (bVar.f3554e.a() == 0) {
                    bVar.f3555f = true;
                } else {
                    r4 = r();
                    r4.m(solverVariable, i8);
                }
            }
            bVar.f3551b = i8;
            return;
        }
        r4 = r();
        r4.i(solverVariable, i8);
        d(r4);
    }

    public void g(SolverVariable solverVariable, SolverVariable solverVariable2, int i8, boolean z4) {
        androidx.constraintlayout.solver.b r4 = r();
        SolverVariable t8 = t();
        t8.f3522e = 0;
        r4.o(solverVariable, solverVariable2, t8, i8);
        d(r4);
    }

    public void h(SolverVariable solverVariable, SolverVariable solverVariable2, int i8, int i9) {
        androidx.constraintlayout.solver.b r4 = r();
        SolverVariable t8 = t();
        t8.f3522e = 0;
        r4.o(solverVariable, solverVariable2, t8, i8);
        if (i9 != 8) {
            m(r4, (int) (r4.f3554e.e(t8) * (-1.0f)), i9);
        }
        d(r4);
    }

    public void i(SolverVariable solverVariable, SolverVariable solverVariable2, int i8, boolean z4) {
        androidx.constraintlayout.solver.b r4 = r();
        SolverVariable t8 = t();
        t8.f3522e = 0;
        r4.p(solverVariable, solverVariable2, t8, i8);
        d(r4);
    }

    public void j(SolverVariable solverVariable, SolverVariable solverVariable2, int i8, int i9) {
        androidx.constraintlayout.solver.b r4 = r();
        SolverVariable t8 = t();
        t8.f3522e = 0;
        r4.p(solverVariable, solverVariable2, t8, i8);
        if (i9 != 8) {
            m(r4, (int) (r4.f3554e.e(t8) * (-1.0f)), i9);
        }
        d(r4);
    }

    public void k(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f5, int i8) {
        androidx.constraintlayout.solver.b r4 = r();
        r4.k(solverVariable, solverVariable2, solverVariable3, solverVariable4, f5);
        if (i8 != 8) {
            r4.d(this, i8);
        }
        d(r4);
    }

    void m(androidx.constraintlayout.solver.b bVar, int i8, int i9) {
        bVar.e(o(i9, null), i8);
    }

    public SolverVariable o(int i8, String str) {
        if (this.f3573j + 1 >= this.f3568e) {
            y();
        }
        SolverVariable a9 = a(SolverVariable.Type.ERROR, str);
        int i9 = this.f3564a + 1;
        this.f3564a = i9;
        this.f3573j++;
        a9.f3520c = i9;
        a9.f3522e = i8;
        this.f3576m.f3559d[i9] = a9;
        this.f3566c.a(a9);
        return a9;
    }

    public SolverVariable p() {
        if (this.f3573j + 1 >= this.f3568e) {
            y();
        }
        SolverVariable a9 = a(SolverVariable.Type.SLACK, null);
        int i8 = this.f3564a + 1;
        this.f3564a = i8;
        this.f3573j++;
        a9.f3520c = i8;
        this.f3576m.f3559d[i8] = a9;
        return a9;
    }

    public SolverVariable q(Object obj) {
        SolverVariable solverVariable = null;
        if (obj == null) {
            return null;
        }
        if (this.f3573j + 1 >= this.f3568e) {
            y();
        }
        if (obj instanceof ConstraintAnchor) {
            ConstraintAnchor constraintAnchor = (ConstraintAnchor) obj;
            solverVariable = constraintAnchor.f();
            if (solverVariable == null) {
                constraintAnchor.m(this.f3576m);
                solverVariable = constraintAnchor.f();
            }
            int i8 = solverVariable.f3520c;
            if (i8 == -1 || i8 > this.f3564a || this.f3576m.f3559d[i8] == null) {
                if (i8 != -1) {
                    solverVariable.d();
                }
                int i9 = this.f3564a + 1;
                this.f3564a = i9;
                this.f3573j++;
                solverVariable.f3520c = i9;
                solverVariable.f3527j = SolverVariable.Type.UNRESTRICTED;
                this.f3576m.f3559d[i9] = solverVariable;
            }
        }
        return solverVariable;
    }

    public androidx.constraintlayout.solver.b r() {
        androidx.constraintlayout.solver.b b9;
        if (f3561s) {
            b9 = this.f3576m.f3556a.b();
            if (b9 == null) {
                b9 = new b(this.f3576m);
                f3563u++;
            }
            b9.z();
        } else {
            b9 = this.f3576m.f3557b.b();
            if (b9 == null) {
                b9 = new androidx.constraintlayout.solver.b(this.f3576m);
                f3562t++;
            }
            b9.z();
        }
        SolverVariable.b();
        return b9;
    }

    public SolverVariable t() {
        if (this.f3573j + 1 >= this.f3568e) {
            y();
        }
        SolverVariable a9 = a(SolverVariable.Type.SLACK, null);
        int i8 = this.f3564a + 1;
        this.f3564a = i8;
        this.f3573j++;
        a9.f3520c = i8;
        this.f3576m.f3559d[i8] = a9;
        return a9;
    }

    public c v() {
        return this.f3576m;
    }

    public int x(Object obj) {
        SolverVariable f5 = ((ConstraintAnchor) obj).f();
        if (f5 != null) {
            return (int) (f5.f3523f + 0.5f);
        }
        return 0;
    }

    public void z() {
        if (this.f3570g || this.f3571h) {
            boolean z4 = false;
            int i8 = 0;
            while (true) {
                if (i8 >= this.f3574k) {
                    z4 = true;
                    break;
                } else if (!this.f3569f[i8].f3555f) {
                    break;
                } else {
                    i8++;
                }
            }
            if (z4) {
                n();
                return;
            }
        }
        A(this.f3566c);
    }
}
