package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h extends n0.b {
    private int I0 = 0;
    private int J0 = 0;
    private int K0 = 0;
    private int L0 = 0;
    private int M0 = 0;
    private int N0 = 0;
    private int O0 = 0;
    private int P0 = 0;
    private boolean Q0 = false;
    private int R0 = 0;
    private int S0 = 0;
    protected BasicMeasure.a T0 = new BasicMeasure.a();
    BasicMeasure.b U0 = null;

    public void L0(boolean z4) {
        int i8 = this.M0;
        if (i8 > 0 || this.N0 > 0) {
            if (z4) {
                this.O0 = this.N0;
                this.P0 = i8;
                return;
            }
            this.O0 = i8;
            this.P0 = this.N0;
        }
    }

    public void M0() {
        for (int i8 = 0; i8 < this.H0; i8++) {
            ConstraintWidget constraintWidget = this.G0[i8];
            if (constraintWidget != null) {
                constraintWidget.r0(true);
            }
        }
    }

    public int N0() {
        return this.S0;
    }

    public int O0() {
        return this.R0;
    }

    public int P0() {
        return this.J0;
    }

    public int Q0() {
        return this.O0;
    }

    public int R0() {
        return this.P0;
    }

    public int S0() {
        return this.I0;
    }

    public void T0(int i8, int i9, int i10, int i11) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void U0(ConstraintWidget constraintWidget, ConstraintWidget.DimensionBehaviour dimensionBehaviour, int i8, ConstraintWidget.DimensionBehaviour dimensionBehaviour2, int i9) {
        while (this.U0 == null && H() != null) {
            this.U0 = ((d) H()).W0();
        }
        BasicMeasure.a aVar = this.T0;
        aVar.f3726a = dimensionBehaviour;
        aVar.f3727b = dimensionBehaviour2;
        aVar.f3728c = i8;
        aVar.f3729d = i9;
        this.U0.b(constraintWidget, aVar);
        constraintWidget.F0(this.T0.f3730e);
        constraintWidget.i0(this.T0.f3731f);
        constraintWidget.h0(this.T0.f3733h);
        constraintWidget.c0(this.T0.f3732g);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean V0() {
        ConstraintWidget constraintWidget = this.P;
        BasicMeasure.b W0 = constraintWidget != null ? ((d) constraintWidget).W0() : null;
        if (W0 == null) {
            return false;
        }
        int i8 = 0;
        while (true) {
            boolean z4 = true;
            if (i8 >= this.H0) {
                return true;
            }
            ConstraintWidget constraintWidget2 = this.G0[i8];
            if (constraintWidget2 != null && !(constraintWidget2 instanceof f)) {
                ConstraintWidget.DimensionBehaviour t8 = constraintWidget2.t(0);
                ConstraintWidget.DimensionBehaviour t9 = constraintWidget2.t(1);
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                if (t8 != dimensionBehaviour || constraintWidget2.f3686l == 1 || t9 != dimensionBehaviour || constraintWidget2.f3688m == 1) {
                    z4 = false;
                }
                if (!z4) {
                    if (t8 == dimensionBehaviour) {
                        t8 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    }
                    if (t9 == dimensionBehaviour) {
                        t9 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                    }
                    BasicMeasure.a aVar = this.T0;
                    aVar.f3726a = t8;
                    aVar.f3727b = t9;
                    aVar.f3728c = constraintWidget2.Q();
                    this.T0.f3729d = constraintWidget2.w();
                    W0.b(constraintWidget2, this.T0);
                    constraintWidget2.F0(this.T0.f3730e);
                    constraintWidget2.i0(this.T0.f3731f);
                    constraintWidget2.c0(this.T0.f3732g);
                }
            }
            i8++;
        }
    }

    public boolean W0() {
        return this.Q0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void X0(boolean z4) {
        this.Q0 = z4;
    }

    public void Y0(int i8, int i9) {
        this.R0 = i8;
        this.S0 = i9;
    }

    public void Z0(int i8) {
        this.K0 = i8;
        this.I0 = i8;
        this.L0 = i8;
        this.J0 = i8;
        this.M0 = i8;
        this.N0 = i8;
    }

    public void a1(int i8) {
        this.J0 = i8;
    }

    public void b1(int i8) {
        this.N0 = i8;
    }

    @Override // n0.b, n0.a
    public void c(d dVar) {
        M0();
    }

    public void c1(int i8) {
        this.K0 = i8;
        this.O0 = i8;
    }

    public void d1(int i8) {
        this.L0 = i8;
        this.P0 = i8;
    }

    public void e1(int i8) {
        this.M0 = i8;
        this.O0 = i8;
        this.P0 = i8;
    }

    public void f1(int i8) {
        this.I0 = i8;
    }
}
