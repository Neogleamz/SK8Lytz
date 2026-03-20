package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.SolverVariable;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f extends ConstraintWidget {
    protected float G0 = -1.0f;
    protected int H0 = -1;
    protected int I0 = -1;
    private ConstraintAnchor J0 = this.E;
    private int K0 = 0;
    private int L0 = 0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f3860a;

        static {
            int[] iArr = new int[ConstraintAnchor.Type.values().length];
            f3860a = iArr;
            try {
                iArr[ConstraintAnchor.Type.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3860a[ConstraintAnchor.Type.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3860a[ConstraintAnchor.Type.TOP.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3860a[ConstraintAnchor.Type.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f3860a[ConstraintAnchor.Type.BASELINE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f3860a[ConstraintAnchor.Type.CENTER.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f3860a[ConstraintAnchor.Type.CENTER_X.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f3860a[ConstraintAnchor.Type.CENTER_Y.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                f3860a[ConstraintAnchor.Type.NONE.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    public f() {
        this.M.clear();
        this.M.add(this.J0);
        int length = this.L.length;
        for (int i8 = 0; i8 < length; i8++) {
            this.L[i8] = this.J0;
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void K0(androidx.constraintlayout.solver.d dVar) {
        if (H() == null) {
            return;
        }
        int x8 = dVar.x(this.J0);
        if (this.K0 == 1) {
            G0(x8);
            H0(0);
            i0(H().w());
            F0(0);
            return;
        }
        G0(0);
        H0(x8);
        F0(H().Q());
        i0(0);
    }

    public int L0() {
        return this.K0;
    }

    public int M0() {
        return this.H0;
    }

    public int N0() {
        return this.I0;
    }

    public float O0() {
        return this.G0;
    }

    public void P0(int i8) {
        if (i8 > -1) {
            this.G0 = -1.0f;
            this.H0 = i8;
            this.I0 = -1;
        }
    }

    public void Q0(int i8) {
        if (i8 > -1) {
            this.G0 = -1.0f;
            this.H0 = -1;
            this.I0 = i8;
        }
    }

    public void R0(float f5) {
        if (f5 > -1.0f) {
            this.G0 = f5;
            this.H0 = -1;
            this.I0 = -1;
        }
    }

    public void S0(int i8) {
        if (this.K0 == i8) {
            return;
        }
        this.K0 = i8;
        this.M.clear();
        this.J0 = this.K0 == 1 ? this.D : this.E;
        this.M.add(this.J0);
        int length = this.L.length;
        for (int i9 = 0; i9 < length; i9++) {
            this.L[i9] = this.J0;
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void f(androidx.constraintlayout.solver.d dVar) {
        d dVar2 = (d) H();
        if (dVar2 == null) {
            return;
        }
        ConstraintAnchor n8 = dVar2.n(ConstraintAnchor.Type.LEFT);
        ConstraintAnchor n9 = dVar2.n(ConstraintAnchor.Type.RIGHT);
        ConstraintWidget constraintWidget = this.P;
        boolean z4 = true;
        boolean z8 = constraintWidget != null && constraintWidget.O[0] == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (this.K0 == 0) {
            n8 = dVar2.n(ConstraintAnchor.Type.TOP);
            n9 = dVar2.n(ConstraintAnchor.Type.BOTTOM);
            ConstraintWidget constraintWidget2 = this.P;
            if (constraintWidget2 == null || constraintWidget2.O[1] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                z4 = false;
            }
            z8 = z4;
        }
        if (this.H0 != -1) {
            SolverVariable q = dVar.q(this.J0);
            dVar.e(q, dVar.q(n8), this.H0, 8);
            if (z8) {
                dVar.h(dVar.q(n9), q, 0, 5);
            }
        } else if (this.I0 == -1) {
            if (this.G0 != -1.0f) {
                dVar.d(androidx.constraintlayout.solver.d.s(dVar, dVar.q(this.J0), dVar.q(n9), this.G0));
            }
        } else {
            SolverVariable q8 = dVar.q(this.J0);
            SolverVariable q9 = dVar.q(n9);
            dVar.e(q8, q9, -this.I0, 8);
            if (z8) {
                dVar.h(q8, dVar.q(n8), 0, 5);
                dVar.h(q9, q8, 0, 5);
            }
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public boolean g() {
        return true;
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void l(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.l(constraintWidget, hashMap);
        f fVar = (f) constraintWidget;
        this.G0 = fVar.G0;
        this.H0 = fVar.H0;
        this.I0 = fVar.I0;
        S0(fVar.K0);
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public ConstraintAnchor n(ConstraintAnchor.Type type) {
        switch (a.f3860a[type.ordinal()]) {
            case 1:
            case 2:
                if (this.K0 == 1) {
                    return this.J0;
                }
                break;
            case 3:
            case 4:
                if (this.K0 == 0) {
                    return this.J0;
                }
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return null;
        }
        throw new AssertionError(type.name());
    }
}
