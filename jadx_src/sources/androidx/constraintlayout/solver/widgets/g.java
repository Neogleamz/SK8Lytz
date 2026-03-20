package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g {

    /* renamed from: a  reason: collision with root package name */
    static boolean[] f3861a = new boolean[3];

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(d dVar, androidx.constraintlayout.solver.d dVar2, ConstraintWidget constraintWidget) {
        constraintWidget.f3682j = -1;
        constraintWidget.f3684k = -1;
        ConstraintWidget.DimensionBehaviour dimensionBehaviour = dVar.O[0];
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (dimensionBehaviour != dimensionBehaviour2 && constraintWidget.O[0] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            int i8 = constraintWidget.D.f3650e;
            int Q = dVar.Q() - constraintWidget.F.f3650e;
            ConstraintAnchor constraintAnchor = constraintWidget.D;
            constraintAnchor.f3652g = dVar2.q(constraintAnchor);
            ConstraintAnchor constraintAnchor2 = constraintWidget.F;
            constraintAnchor2.f3652g = dVar2.q(constraintAnchor2);
            dVar2.f(constraintWidget.D.f3652g, i8);
            dVar2.f(constraintWidget.F.f3652g, Q);
            constraintWidget.f3682j = 2;
            constraintWidget.l0(i8, Q);
        }
        if (dVar.O[1] == dimensionBehaviour2 || constraintWidget.O[1] != ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            return;
        }
        int i9 = constraintWidget.E.f3650e;
        int w8 = dVar.w() - constraintWidget.G.f3650e;
        ConstraintAnchor constraintAnchor3 = constraintWidget.E;
        constraintAnchor3.f3652g = dVar2.q(constraintAnchor3);
        ConstraintAnchor constraintAnchor4 = constraintWidget.G;
        constraintAnchor4.f3652g = dVar2.q(constraintAnchor4);
        dVar2.f(constraintWidget.E.f3652g, i9);
        dVar2.f(constraintWidget.G.f3652g, w8);
        if (constraintWidget.f3665a0 > 0 || constraintWidget.P() == 8) {
            ConstraintAnchor constraintAnchor5 = constraintWidget.H;
            constraintAnchor5.f3652g = dVar2.q(constraintAnchor5);
            dVar2.f(constraintWidget.H.f3652g, constraintWidget.f3665a0 + i9);
        }
        constraintWidget.f3684k = 2;
        constraintWidget.A0(i9, w8);
    }

    public static final boolean b(int i8, int i9) {
        return (i8 & i9) == i9;
    }
}
