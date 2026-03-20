package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.d;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b implements d.a {

    /* renamed from: e  reason: collision with root package name */
    public a f3554e;

    /* renamed from: a  reason: collision with root package name */
    SolverVariable f3550a = null;

    /* renamed from: b  reason: collision with root package name */
    float f3551b = 0.0f;

    /* renamed from: c  reason: collision with root package name */
    boolean f3552c = false;

    /* renamed from: d  reason: collision with root package name */
    ArrayList<SolverVariable> f3553d = new ArrayList<>();

    /* renamed from: f  reason: collision with root package name */
    boolean f3555f = false;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        int a();

        SolverVariable b(int i8);

        float c(int i8);

        void clear();

        void d(SolverVariable solverVariable, float f5, boolean z4);

        float e(SolverVariable solverVariable);

        boolean f(SolverVariable solverVariable);

        float g(b bVar, boolean z4);

        void h(SolverVariable solverVariable, float f5);

        float i(SolverVariable solverVariable, boolean z4);

        void invert();

        void j(float f5);
    }

    public b() {
    }

    public b(c cVar) {
        this.f3554e = new androidx.constraintlayout.solver.a(this, cVar);
    }

    private boolean v(SolverVariable solverVariable, d dVar) {
        return solverVariable.f3530m <= 1;
    }

    private SolverVariable x(boolean[] zArr, SolverVariable solverVariable) {
        SolverVariable.Type type;
        int a9 = this.f3554e.a();
        SolverVariable solverVariable2 = null;
        float f5 = 0.0f;
        for (int i8 = 0; i8 < a9; i8++) {
            float c9 = this.f3554e.c(i8);
            if (c9 < 0.0f) {
                SolverVariable b9 = this.f3554e.b(i8);
                if ((zArr == null || !zArr[b9.f3520c]) && b9 != solverVariable && (((type = b9.f3527j) == SolverVariable.Type.SLACK || type == SolverVariable.Type.ERROR) && c9 < f5)) {
                    f5 = c9;
                    solverVariable2 = b9;
                }
            }
        }
        return solverVariable2;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00b6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    java.lang.String A() {
        /*
            r10 = this;
            androidx.constraintlayout.solver.SolverVariable r0 = r10.f3550a
            java.lang.String r1 = ""
            if (r0 != 0) goto L14
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r1)
            java.lang.String r1 = "0"
            r0.append(r1)
            goto L21
        L14:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r1)
            androidx.constraintlayout.solver.SolverVariable r1 = r10.f3550a
            r0.append(r1)
        L21:
            java.lang.String r0 = r0.toString()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = " = "
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            float r1 = r10.f3551b
            r2 = 0
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            r3 = 0
            r4 = 1
            if (r1 == 0) goto L52
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            float r0 = r10.f3551b
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            r1 = r4
            goto L53
        L52:
            r1 = r3
        L53:
            androidx.constraintlayout.solver.b$a r5 = r10.f3554e
            int r5 = r5.a()
        L59:
            if (r3 >= r5) goto Ld1
            androidx.constraintlayout.solver.b$a r6 = r10.f3554e
            androidx.constraintlayout.solver.SolverVariable r6 = r6.b(r3)
            if (r6 != 0) goto L64
            goto Lce
        L64:
            androidx.constraintlayout.solver.b$a r7 = r10.f3554e
            float r7 = r7.c(r3)
            int r8 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r8 != 0) goto L6f
            goto Lce
        L6f:
            java.lang.String r6 = r6.toString()
            r9 = -1082130432(0xffffffffbf800000, float:-1.0)
            if (r1 != 0) goto L86
            int r1 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r1 >= 0) goto Laa
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = "- "
            goto La2
        L86:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            if (r8 <= 0) goto L9a
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = " + "
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            goto Laa
        L9a:
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = " - "
        La2:
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            float r7 = r7 * r9
        Laa:
            r1 = 1065353216(0x3f800000, float:1.0)
            int r1 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1))
            if (r1 != 0) goto Lb6
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            goto Lc3
        Lb6:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            r1.append(r7)
            java.lang.String r0 = " "
        Lc3:
            r1.append(r0)
            r1.append(r6)
            java.lang.String r0 = r1.toString()
            r1 = r4
        Lce:
            int r3 = r3 + 1
            goto L59
        Ld1:
            if (r1 != 0) goto Le4
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = "0.0"
            r1.append(r0)
            java.lang.String r0 = r1.toString()
        Le4:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.b.A():java.lang.String");
    }

    public void B(d dVar, SolverVariable solverVariable, boolean z4) {
        if (solverVariable.f3524g) {
            this.f3551b += solverVariable.f3523f * this.f3554e.e(solverVariable);
            this.f3554e.i(solverVariable, z4);
            if (z4) {
                solverVariable.c(this);
            }
        }
    }

    public void C(b bVar, boolean z4) {
        this.f3551b += bVar.f3551b * this.f3554e.g(bVar, z4);
        if (z4) {
            bVar.f3550a.c(this);
        }
    }

    public void D(d dVar) {
        if (dVar.f3569f.length == 0) {
            return;
        }
        boolean z4 = false;
        while (!z4) {
            int a9 = this.f3554e.a();
            for (int i8 = 0; i8 < a9; i8++) {
                SolverVariable b9 = this.f3554e.b(i8);
                if (b9.f3521d != -1 || b9.f3524g) {
                    this.f3553d.add(b9);
                }
            }
            if (this.f3553d.size() > 0) {
                Iterator<SolverVariable> it = this.f3553d.iterator();
                while (it.hasNext()) {
                    SolverVariable next = it.next();
                    if (next.f3524g) {
                        B(dVar, next, true);
                    } else {
                        C(dVar.f3569f[next.f3521d], true);
                    }
                }
                this.f3553d.clear();
            } else {
                z4 = true;
            }
        }
    }

    @Override // androidx.constraintlayout.solver.d.a
    public void a(SolverVariable solverVariable) {
        int i8 = solverVariable.f3522e;
        float f5 = 1.0f;
        if (i8 != 1) {
            if (i8 == 2) {
                f5 = 1000.0f;
            } else if (i8 == 3) {
                f5 = 1000000.0f;
            } else if (i8 == 4) {
                f5 = 1.0E9f;
            } else if (i8 == 5) {
                f5 = 1.0E12f;
            }
        }
        this.f3554e.h(solverVariable, f5);
    }

    @Override // androidx.constraintlayout.solver.d.a
    public SolverVariable b(d dVar, boolean[] zArr) {
        return x(zArr, null);
    }

    @Override // androidx.constraintlayout.solver.d.a
    public void c(d.a aVar) {
        if (aVar instanceof b) {
            b bVar = (b) aVar;
            this.f3550a = null;
            this.f3554e.clear();
            for (int i8 = 0; i8 < bVar.f3554e.a(); i8++) {
                this.f3554e.d(bVar.f3554e.b(i8), bVar.f3554e.c(i8), true);
            }
        }
    }

    @Override // androidx.constraintlayout.solver.d.a
    public void clear() {
        this.f3554e.clear();
        this.f3550a = null;
        this.f3551b = 0.0f;
    }

    public b d(d dVar, int i8) {
        this.f3554e.h(dVar.o(i8, "ep"), 1.0f);
        this.f3554e.h(dVar.o(i8, "em"), -1.0f);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b e(SolverVariable solverVariable, int i8) {
        this.f3554e.h(solverVariable, i8);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f(d dVar) {
        boolean z4;
        SolverVariable g8 = g(dVar);
        if (g8 == null) {
            z4 = true;
        } else {
            y(g8);
            z4 = false;
        }
        if (this.f3554e.a() == 0) {
            this.f3555f = true;
        }
        return z4;
    }

    SolverVariable g(d dVar) {
        int a9 = this.f3554e.a();
        SolverVariable solverVariable = null;
        boolean z4 = false;
        boolean z8 = false;
        float f5 = 0.0f;
        float f8 = 0.0f;
        SolverVariable solverVariable2 = null;
        for (int i8 = 0; i8 < a9; i8++) {
            float c9 = this.f3554e.c(i8);
            SolverVariable b9 = this.f3554e.b(i8);
            if (b9.f3527j == SolverVariable.Type.UNRESTRICTED) {
                if (solverVariable == null || f5 > c9) {
                    z4 = v(b9, dVar);
                    f5 = c9;
                    solverVariable = b9;
                } else if (!z4 && v(b9, dVar)) {
                    f5 = c9;
                    solverVariable = b9;
                    z4 = true;
                }
            } else if (solverVariable == null && c9 < 0.0f) {
                if (solverVariable2 == null || f8 > c9) {
                    z8 = v(b9, dVar);
                    f8 = c9;
                    solverVariable2 = b9;
                } else if (!z8 && v(b9, dVar)) {
                    f8 = c9;
                    solverVariable2 = b9;
                    z8 = true;
                }
            }
        }
        return solverVariable != null ? solverVariable : solverVariable2;
    }

    @Override // androidx.constraintlayout.solver.d.a
    public SolverVariable getKey() {
        return this.f3550a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b h(SolverVariable solverVariable, SolverVariable solverVariable2, int i8, float f5, SolverVariable solverVariable3, SolverVariable solverVariable4, int i9) {
        float f8;
        int i10;
        if (solverVariable2 == solverVariable3) {
            this.f3554e.h(solverVariable, 1.0f);
            this.f3554e.h(solverVariable4, 1.0f);
            this.f3554e.h(solverVariable2, -2.0f);
            return this;
        }
        if (f5 == 0.5f) {
            this.f3554e.h(solverVariable, 1.0f);
            this.f3554e.h(solverVariable2, -1.0f);
            this.f3554e.h(solverVariable3, -1.0f);
            this.f3554e.h(solverVariable4, 1.0f);
            if (i8 > 0 || i9 > 0) {
                i10 = (-i8) + i9;
                f8 = i10;
            }
            return this;
        } else if (f5 <= 0.0f) {
            this.f3554e.h(solverVariable, -1.0f);
            this.f3554e.h(solverVariable2, 1.0f);
            f8 = i8;
        } else if (f5 < 1.0f) {
            float f9 = 1.0f - f5;
            this.f3554e.h(solverVariable, f9 * 1.0f);
            this.f3554e.h(solverVariable2, f9 * (-1.0f));
            this.f3554e.h(solverVariable3, (-1.0f) * f5);
            this.f3554e.h(solverVariable4, 1.0f * f5);
            if (i8 > 0 || i9 > 0) {
                f8 = ((-i8) * f9) + (i9 * f5);
            }
            return this;
        } else {
            this.f3554e.h(solverVariable4, -1.0f);
            this.f3554e.h(solverVariable3, 1.0f);
            i10 = -i9;
            f8 = i10;
        }
        this.f3551b = f8;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b i(SolverVariable solverVariable, int i8) {
        this.f3550a = solverVariable;
        float f5 = i8;
        solverVariable.f3523f = f5;
        this.f3551b = f5;
        this.f3555f = true;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b j(SolverVariable solverVariable, SolverVariable solverVariable2, float f5) {
        this.f3554e.h(solverVariable, -1.0f);
        this.f3554e.h(solverVariable2, f5);
        return this;
    }

    public b k(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f5) {
        this.f3554e.h(solverVariable, -1.0f);
        this.f3554e.h(solverVariable2, 1.0f);
        this.f3554e.h(solverVariable3, f5);
        this.f3554e.h(solverVariable4, -f5);
        return this;
    }

    public b l(float f5, float f8, float f9, SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4) {
        this.f3551b = 0.0f;
        if (f8 == 0.0f || f5 == f9) {
            this.f3554e.h(solverVariable, 1.0f);
            this.f3554e.h(solverVariable2, -1.0f);
            this.f3554e.h(solverVariable4, 1.0f);
            this.f3554e.h(solverVariable3, -1.0f);
        } else if (f5 == 0.0f) {
            this.f3554e.h(solverVariable, 1.0f);
            this.f3554e.h(solverVariable2, -1.0f);
        } else if (f9 == 0.0f) {
            this.f3554e.h(solverVariable3, 1.0f);
            this.f3554e.h(solverVariable4, -1.0f);
        } else {
            float f10 = (f5 / f8) / (f9 / f8);
            this.f3554e.h(solverVariable, 1.0f);
            this.f3554e.h(solverVariable2, -1.0f);
            this.f3554e.h(solverVariable4, f10);
            this.f3554e.h(solverVariable3, -f10);
        }
        return this;
    }

    public b m(SolverVariable solverVariable, int i8) {
        a aVar;
        float f5;
        if (i8 < 0) {
            this.f3551b = i8 * (-1);
            aVar = this.f3554e;
            f5 = 1.0f;
        } else {
            this.f3551b = i8;
            aVar = this.f3554e;
            f5 = -1.0f;
        }
        aVar.h(solverVariable, f5);
        return this;
    }

    public b n(SolverVariable solverVariable, SolverVariable solverVariable2, int i8) {
        boolean z4 = false;
        if (i8 != 0) {
            if (i8 < 0) {
                i8 *= -1;
                z4 = true;
            }
            this.f3551b = i8;
        }
        if (z4) {
            this.f3554e.h(solverVariable, 1.0f);
            this.f3554e.h(solverVariable2, -1.0f);
        } else {
            this.f3554e.h(solverVariable, -1.0f);
            this.f3554e.h(solverVariable2, 1.0f);
        }
        return this;
    }

    public b o(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int i8) {
        boolean z4 = false;
        if (i8 != 0) {
            if (i8 < 0) {
                i8 *= -1;
                z4 = true;
            }
            this.f3551b = i8;
        }
        if (z4) {
            this.f3554e.h(solverVariable, 1.0f);
            this.f3554e.h(solverVariable2, -1.0f);
            this.f3554e.h(solverVariable3, -1.0f);
        } else {
            this.f3554e.h(solverVariable, -1.0f);
            this.f3554e.h(solverVariable2, 1.0f);
            this.f3554e.h(solverVariable3, 1.0f);
        }
        return this;
    }

    public b p(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int i8) {
        boolean z4 = false;
        if (i8 != 0) {
            if (i8 < 0) {
                i8 *= -1;
                z4 = true;
            }
            this.f3551b = i8;
        }
        if (z4) {
            this.f3554e.h(solverVariable, 1.0f);
            this.f3554e.h(solverVariable2, -1.0f);
            this.f3554e.h(solverVariable3, 1.0f);
        } else {
            this.f3554e.h(solverVariable, -1.0f);
            this.f3554e.h(solverVariable2, 1.0f);
            this.f3554e.h(solverVariable3, -1.0f);
        }
        return this;
    }

    public b q(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f5) {
        this.f3554e.h(solverVariable3, 0.5f);
        this.f3554e.h(solverVariable4, 0.5f);
        this.f3554e.h(solverVariable, -0.5f);
        this.f3554e.h(solverVariable2, -0.5f);
        this.f3551b = -f5;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r() {
        float f5 = this.f3551b;
        if (f5 < 0.0f) {
            this.f3551b = f5 * (-1.0f);
            this.f3554e.invert();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean s() {
        SolverVariable solverVariable = this.f3550a;
        return solverVariable != null && (solverVariable.f3527j == SolverVariable.Type.UNRESTRICTED || this.f3551b >= 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean t(SolverVariable solverVariable) {
        return this.f3554e.f(solverVariable);
    }

    public String toString() {
        return A();
    }

    public boolean u() {
        return this.f3550a == null && this.f3551b == 0.0f && this.f3554e.a() == 0;
    }

    public SolverVariable w(SolverVariable solverVariable) {
        return x(null, solverVariable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y(SolverVariable solverVariable) {
        SolverVariable solverVariable2 = this.f3550a;
        if (solverVariable2 != null) {
            this.f3554e.h(solverVariable2, -1.0f);
            this.f3550a = null;
        }
        float i8 = this.f3554e.i(solverVariable, true) * (-1.0f);
        this.f3550a = solverVariable;
        if (i8 == 1.0f) {
            return;
        }
        this.f3551b /= i8;
        this.f3554e.j(i8);
    }

    public void z() {
        this.f3550a = null;
        this.f3554e.clear();
        this.f3551b = 0.0f;
        this.f3555f = false;
    }
}
