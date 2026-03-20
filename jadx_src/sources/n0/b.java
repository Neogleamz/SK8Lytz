package n0;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.d;
import java.util.Arrays;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b extends ConstraintWidget implements a {
    public ConstraintWidget[] G0 = new ConstraintWidget[4];
    public int H0 = 0;

    @Override // n0.a
    public void a(ConstraintWidget constraintWidget) {
        if (constraintWidget == this || constraintWidget == null) {
            return;
        }
        int i8 = this.H0 + 1;
        ConstraintWidget[] constraintWidgetArr = this.G0;
        if (i8 > constraintWidgetArr.length) {
            this.G0 = (ConstraintWidget[]) Arrays.copyOf(constraintWidgetArr, constraintWidgetArr.length * 2);
        }
        ConstraintWidget[] constraintWidgetArr2 = this.G0;
        int i9 = this.H0;
        constraintWidgetArr2[i9] = constraintWidget;
        this.H0 = i9 + 1;
    }

    @Override // n0.a
    public void b() {
        this.H0 = 0;
        Arrays.fill(this.G0, (Object) null);
    }

    public void c(d dVar) {
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void l(ConstraintWidget constraintWidget, HashMap<ConstraintWidget, ConstraintWidget> hashMap) {
        super.l(constraintWidget, hashMap);
        b bVar = (b) constraintWidget;
        this.H0 = 0;
        int i8 = bVar.H0;
        for (int i9 = 0; i9 < i8; i9++) {
            a(hashMap.get(bVar.G0[i9]));
        }
    }
}
