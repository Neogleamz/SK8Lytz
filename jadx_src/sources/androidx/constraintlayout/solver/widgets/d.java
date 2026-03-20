package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayList;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends n0.c {
    int M0;
    int N0;
    int O0;
    int P0;
    BasicMeasure H0 = new BasicMeasure(this);
    public androidx.constraintlayout.solver.widgets.analyzer.c I0 = new androidx.constraintlayout.solver.widgets.analyzer.c(this);
    protected BasicMeasure.b J0 = null;
    private boolean K0 = false;
    protected androidx.constraintlayout.solver.d L0 = new androidx.constraintlayout.solver.d();
    int Q0 = 0;
    int R0 = 0;
    c[] S0 = new c[4];
    c[] T0 = new c[4];
    public boolean U0 = false;
    public boolean V0 = false;
    public boolean W0 = false;
    public int X0 = 0;
    public int Y0 = 0;
    private int Z0 = 263;

    /* renamed from: a1  reason: collision with root package name */
    public boolean f3819a1 = false;

    /* renamed from: b1  reason: collision with root package name */
    private boolean f3820b1 = false;

    /* renamed from: c1  reason: collision with root package name */
    private boolean f3821c1 = false;

    /* renamed from: d1  reason: collision with root package name */
    int f3822d1 = 0;

    private void R0(ConstraintWidget constraintWidget) {
        int i8 = this.Q0 + 1;
        c[] cVarArr = this.T0;
        if (i8 >= cVarArr.length) {
            this.T0 = (c[]) Arrays.copyOf(cVarArr, cVarArr.length * 2);
        }
        this.T0[this.Q0] = new c(constraintWidget, 0, c1());
        this.Q0++;
    }

    private void S0(ConstraintWidget constraintWidget) {
        int i8 = this.R0 + 1;
        c[] cVarArr = this.S0;
        if (i8 >= cVarArr.length) {
            this.S0 = (c[]) Arrays.copyOf(cVarArr, cVarArr.length * 2);
        }
        this.S0[this.R0] = new c(constraintWidget, 1, c1());
        this.R0++;
    }

    private void g1() {
        this.Q0 = 0;
        this.R0 = 0;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void J0(boolean z4, boolean z8) {
        super.J0(z4, z8);
        int size = this.G0.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.G0.get(i8).J0(z4, z8);
        }
    }

    /* JADX WARN: Type inference failed for: r11v13 */
    /* JADX WARN: Type inference failed for: r11v8 */
    /* JADX WARN: Type inference failed for: r11v9, types: [boolean] */
    @Override // n0.c
    public void M0() {
        boolean z4;
        ?? r11;
        boolean z8;
        this.U = 0;
        this.V = 0;
        int max = Math.max(0, Q());
        int max2 = Math.max(0, w());
        this.f3820b1 = false;
        this.f3821c1 = false;
        boolean z9 = f1(64) || f1(RecognitionOptions.ITF);
        androidx.constraintlayout.solver.d dVar = this.L0;
        dVar.f3570g = false;
        dVar.f3571h = false;
        if (this.Z0 != 0 && z9) {
            dVar.f3571h = true;
        }
        ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr = this.O;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = dimensionBehaviourArr[1];
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = dimensionBehaviourArr[0];
        ArrayList<ConstraintWidget> arrayList = this.G0;
        ConstraintWidget.DimensionBehaviour z10 = z();
        ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        boolean z11 = z10 == dimensionBehaviour3 || N() == dimensionBehaviour3;
        g1();
        int size = this.G0.size();
        for (int i8 = 0; i8 < size; i8++) {
            ConstraintWidget constraintWidget = this.G0.get(i8);
            if (constraintWidget instanceof n0.c) {
                ((n0.c) constraintWidget).M0();
            }
        }
        int i9 = 0;
        boolean z12 = false;
        boolean z13 = true;
        while (z13) {
            int i10 = i9 + 1;
            try {
                this.L0.D();
                g1();
                m(this.L0);
                for (int i11 = 0; i11 < size; i11++) {
                    this.G0.get(i11).m(this.L0);
                }
                z13 = Q0(this.L0);
                if (z13) {
                    this.L0.z();
                }
            } catch (Exception e8) {
                e8.printStackTrace();
                System.out.println("EXCEPTION : " + e8);
            }
            androidx.constraintlayout.solver.d dVar2 = this.L0;
            if (z13) {
                k1(dVar2, g.f3861a);
            } else {
                K0(dVar2);
                for (int i12 = 0; i12 < size; i12++) {
                    this.G0.get(i12).K0(this.L0);
                }
            }
            if (z11 && i10 < 8 && g.f3861a[2]) {
                int i13 = 0;
                int i14 = 0;
                for (int i15 = 0; i15 < size; i15++) {
                    ConstraintWidget constraintWidget2 = this.G0.get(i15);
                    i13 = Math.max(i13, constraintWidget2.U + constraintWidget2.Q());
                    i14 = Math.max(i14, constraintWidget2.V + constraintWidget2.w());
                }
                int max3 = Math.max(this.f3667b0, i13);
                int max4 = Math.max(this.f3669c0, i14);
                ConstraintWidget.DimensionBehaviour dimensionBehaviour4 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                if (dimensionBehaviour2 != dimensionBehaviour4 || Q() >= max3) {
                    z4 = false;
                } else {
                    F0(max3);
                    this.O[0] = dimensionBehaviour4;
                    z4 = true;
                    z12 = true;
                }
                if (dimensionBehaviour == dimensionBehaviour4 && w() < max4) {
                    i0(max4);
                    this.O[1] = dimensionBehaviour4;
                    z4 = true;
                    z12 = true;
                }
            } else {
                z4 = false;
            }
            int max5 = Math.max(this.f3667b0, Q());
            if (max5 > Q()) {
                F0(max5);
                this.O[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                z4 = true;
                z12 = true;
            }
            int max6 = Math.max(this.f3669c0, w());
            if (max6 > w()) {
                i0(max6);
                r11 = 1;
                this.O[1] = ConstraintWidget.DimensionBehaviour.FIXED;
                z4 = true;
                z8 = true;
            } else {
                r11 = 1;
                z8 = z12;
            }
            if (!z8) {
                ConstraintWidget.DimensionBehaviour dimensionBehaviour5 = this.O[0];
                ConstraintWidget.DimensionBehaviour dimensionBehaviour6 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                if (dimensionBehaviour5 == dimensionBehaviour6 && max > 0 && Q() > max) {
                    this.f3820b1 = r11;
                    this.O[0] = ConstraintWidget.DimensionBehaviour.FIXED;
                    F0(max);
                    z4 = r11;
                    z8 = z4;
                }
                if (this.O[r11] == dimensionBehaviour6 && max2 > 0 && w() > max2) {
                    this.f3821c1 = r11;
                    this.O[r11] = ConstraintWidget.DimensionBehaviour.FIXED;
                    i0(max2);
                    z13 = true;
                    z12 = true;
                    i9 = i10;
                }
            }
            z13 = z4;
            z12 = z8;
            i9 = i10;
        }
        this.G0 = arrayList;
        if (z12) {
            ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr2 = this.O;
            dimensionBehaviourArr2[0] = dimensionBehaviour2;
            dimensionBehaviourArr2[1] = dimensionBehaviour;
        }
        b0(this.L0.v());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void P0(ConstraintWidget constraintWidget, int i8) {
        if (i8 == 0) {
            R0(constraintWidget);
        } else if (i8 == 1) {
            S0(constraintWidget);
        }
    }

    public boolean Q0(androidx.constraintlayout.solver.d dVar) {
        f(dVar);
        int size = this.G0.size();
        boolean z4 = false;
        for (int i8 = 0; i8 < size; i8++) {
            ConstraintWidget constraintWidget = this.G0.get(i8);
            constraintWidget.p0(0, false);
            constraintWidget.p0(1, false);
            if (constraintWidget instanceof a) {
                z4 = true;
            }
        }
        if (z4) {
            for (int i9 = 0; i9 < size; i9++) {
                ConstraintWidget constraintWidget2 = this.G0.get(i9);
                if (constraintWidget2 instanceof a) {
                    ((a) constraintWidget2).O0();
                }
            }
        }
        for (int i10 = 0; i10 < size; i10++) {
            ConstraintWidget constraintWidget3 = this.G0.get(i10);
            if (constraintWidget3.e()) {
                constraintWidget3.f(dVar);
            }
        }
        for (int i11 = 0; i11 < size; i11++) {
            ConstraintWidget constraintWidget4 = this.G0.get(i11);
            if (constraintWidget4 instanceof d) {
                ConstraintWidget.DimensionBehaviour[] dimensionBehaviourArr = constraintWidget4.O;
                ConstraintWidget.DimensionBehaviour dimensionBehaviour = dimensionBehaviourArr[0];
                ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = dimensionBehaviourArr[1];
                ConstraintWidget.DimensionBehaviour dimensionBehaviour3 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
                if (dimensionBehaviour == dimensionBehaviour3) {
                    constraintWidget4.m0(ConstraintWidget.DimensionBehaviour.FIXED);
                }
                if (dimensionBehaviour2 == dimensionBehaviour3) {
                    constraintWidget4.B0(ConstraintWidget.DimensionBehaviour.FIXED);
                }
                constraintWidget4.f(dVar);
                if (dimensionBehaviour == dimensionBehaviour3) {
                    constraintWidget4.m0(dimensionBehaviour);
                }
                if (dimensionBehaviour2 == dimensionBehaviour3) {
                    constraintWidget4.B0(dimensionBehaviour2);
                }
            } else {
                g.a(this, dVar, constraintWidget4);
                if (!constraintWidget4.e()) {
                    constraintWidget4.f(dVar);
                }
            }
        }
        if (this.Q0 > 0) {
            b.a(this, dVar, 0);
        }
        if (this.R0 > 0) {
            b.a(this, dVar, 1);
        }
        return true;
    }

    public boolean T0(boolean z4) {
        return this.I0.f(z4);
    }

    public boolean U0(boolean z4) {
        return this.I0.g(z4);
    }

    public boolean V0(boolean z4, int i8) {
        return this.I0.h(z4, i8);
    }

    public BasicMeasure.b W0() {
        return this.J0;
    }

    public int X0() {
        return this.Z0;
    }

    public boolean Y0() {
        return false;
    }

    @Override // n0.c, androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void Z() {
        this.L0.D();
        this.M0 = 0;
        this.O0 = 0;
        this.N0 = 0;
        this.P0 = 0;
        this.f3819a1 = false;
        super.Z();
    }

    public void Z0() {
        this.I0.j();
    }

    public void a1() {
        this.I0.k();
    }

    public boolean b1() {
        return this.f3821c1;
    }

    public boolean c1() {
        return this.K0;
    }

    public boolean d1() {
        return this.f3820b1;
    }

    public long e1(int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16) {
        this.M0 = i15;
        this.N0 = i16;
        return this.H0.d(this, i8, i15, i16, i9, i10, i11, i12, i13, i14);
    }

    public boolean f1(int i8) {
        return (this.Z0 & i8) == i8;
    }

    public void h1(BasicMeasure.b bVar) {
        this.J0 = bVar;
        this.I0.n(bVar);
    }

    public void i1(int i8) {
        this.Z0 = i8;
        androidx.constraintlayout.solver.d.f3561s = g.b(i8, RecognitionOptions.QR_CODE);
    }

    public void j1(boolean z4) {
        this.K0 = z4;
    }

    public void k1(androidx.constraintlayout.solver.d dVar, boolean[] zArr) {
        zArr[2] = false;
        K0(dVar);
        int size = this.G0.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.G0.get(i8).K0(dVar);
        }
    }

    public void l1() {
        this.H0.e(this);
    }
}
