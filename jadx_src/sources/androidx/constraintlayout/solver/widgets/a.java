package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a extends n0.b {
    private int I0 = 0;
    private boolean J0 = true;
    private int K0 = 0;

    public boolean L0() {
        return this.J0;
    }

    public int M0() {
        return this.I0;
    }

    public int N0() {
        return this.K0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void O0() {
        for (int i8 = 0; i8 < this.H0; i8++) {
            ConstraintWidget constraintWidget = this.G0[i8];
            int i9 = this.I0;
            if (i9 == 0 || i9 == 1) {
                constraintWidget.p0(0, true);
            } else if (i9 == 2 || i9 == 3) {
                constraintWidget.p0(1, true);
            }
        }
    }

    public void P0(boolean z4) {
        this.J0 = z4;
    }

    public void Q0(int i8) {
        this.I0 = i8;
    }

    public void R0(int i8) {
        this.K0 = i8;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void f(androidx.constraintlayout.solver.d dVar) {
        Object[] objArr;
        boolean z4;
        SolverVariable solverVariable;
        ConstraintAnchor constraintAnchor;
        int i8;
        int i9;
        int i10;
        ConstraintAnchor[] constraintAnchorArr = this.L;
        constraintAnchorArr[0] = this.D;
        constraintAnchorArr[2] = this.E;
        constraintAnchorArr[1] = this.F;
        constraintAnchorArr[3] = this.G;
        int i11 = 0;
        while (true) {
            objArr = this.L;
            if (i11 >= objArr.length) {
                break;
            }
            objArr[i11].f3652g = dVar.q(objArr[i11]);
            i11++;
        }
        int i12 = this.I0;
        if (i12 < 0 || i12 >= 4) {
            return;
        }
        ConstraintAnchor constraintAnchor2 = objArr[i12];
        for (int i13 = 0; i13 < this.H0; i13++) {
            ConstraintWidget constraintWidget = this.G0[i13];
            if ((this.J0 || constraintWidget.g()) && ((((i9 = this.I0) == 0 || i9 == 1) && constraintWidget.z() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.D.f3649d != null && constraintWidget.F.f3649d != null) || (((i10 = this.I0) == 2 || i10 == 3) && constraintWidget.N() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.E.f3649d != null && constraintWidget.G.f3649d != null))) {
                z4 = true;
                break;
            }
        }
        z4 = false;
        boolean z8 = this.D.i() || this.F.i();
        boolean z9 = this.E.i() || this.G.i();
        int i14 = !z4 && (((i8 = this.I0) == 0 && z8) || ((i8 == 2 && z9) || ((i8 == 1 && z8) || (i8 == 3 && z9)))) ? 5 : 4;
        for (int i15 = 0; i15 < this.H0; i15++) {
            ConstraintWidget constraintWidget2 = this.G0[i15];
            if (this.J0 || constraintWidget2.g()) {
                SolverVariable q = dVar.q(constraintWidget2.L[this.I0]);
                ConstraintAnchor[] constraintAnchorArr2 = constraintWidget2.L;
                int i16 = this.I0;
                constraintAnchorArr2[i16].f3652g = q;
                int i17 = (constraintAnchorArr2[i16].f3649d == null || constraintAnchorArr2[i16].f3649d.f3647b != this) ? 0 : constraintAnchorArr2[i16].f3650e + 0;
                if (i16 == 0 || i16 == 2) {
                    dVar.i(constraintAnchor2.f3652g, q, this.K0 - i17, z4);
                } else {
                    dVar.g(constraintAnchor2.f3652g, q, this.K0 + i17, z4);
                }
                dVar.e(constraintAnchor2.f3652g, q, this.K0 + i17, i14);
            }
        }
        int i18 = this.I0;
        if (i18 == 0) {
            dVar.e(this.F.f3652g, this.D.f3652g, 0, 8);
            dVar.e(this.D.f3652g, this.P.F.f3652g, 0, 4);
            solverVariable = this.D.f3652g;
            constraintAnchor = this.P.D;
        } else if (i18 == 1) {
            dVar.e(this.D.f3652g, this.F.f3652g, 0, 8);
            dVar.e(this.D.f3652g, this.P.D.f3652g, 0, 4);
            solverVariable = this.D.f3652g;
            constraintAnchor = this.P.F;
        } else if (i18 == 2) {
            dVar.e(this.G.f3652g, this.E.f3652g, 0, 8);
            dVar.e(this.E.f3652g, this.P.G.f3652g, 0, 4);
            solverVariable = this.E.f3652g;
            constraintAnchor = this.P.E;
        } else if (i18 != 3) {
            return;
        } else {
            dVar.e(this.E.f3652g, this.G.f3652g, 0, 8);
            dVar.e(this.E.f3652g, this.P.E.f3652g, 0, 4);
            solverVariable = this.E.f3652g;
            constraintAnchor = this.P.G;
        }
        dVar.e(solverVariable, constraintAnchor.f3652g, 0, 0);
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public boolean g() {
        return true;
    }

    @Override // n0.b, androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void l(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.l(constraintWidget, hashMap);
        a aVar = (a) constraintWidget;
        this.I0 = aVar.I0;
        this.J0 = aVar.J0;
        this.K0 = aVar.K0;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public String toString() {
        String str = "[Barrier] " + s() + " {";
        for (int i8 = 0; i8 < this.H0; i8++) {
            ConstraintWidget constraintWidget = this.G0[i8];
            if (i8 > 0) {
                str = str + ", ";
            }
            str = str + constraintWidget.s();
        }
        return str + "}";
    }
}
