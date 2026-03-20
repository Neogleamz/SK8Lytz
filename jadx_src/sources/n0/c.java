package n0;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c extends ConstraintWidget {
    public ArrayList<ConstraintWidget> G0 = new ArrayList<>();

    public ArrayList<ConstraintWidget> L0() {
        return this.G0;
    }

    public void M0() {
        ArrayList<ConstraintWidget> arrayList = this.G0;
        if (arrayList == null) {
            return;
        }
        int size = arrayList.size();
        for (int i8 = 0; i8 < size; i8++) {
            ConstraintWidget constraintWidget = this.G0.get(i8);
            if (constraintWidget instanceof c) {
                ((c) constraintWidget).M0();
            }
        }
    }

    public void N0(ConstraintWidget constraintWidget) {
        this.G0.remove(constraintWidget);
        constraintWidget.x0(null);
    }

    public void O0() {
        this.G0.clear();
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void Z() {
        this.G0.clear();
        super.Z();
    }

    public void a(ConstraintWidget constraintWidget) {
        this.G0.add(constraintWidget);
        if (constraintWidget.H() != null) {
            ((c) constraintWidget.H()).N0(constraintWidget);
        }
        constraintWidget.x0(this);
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void b0(androidx.constraintlayout.solver.c cVar) {
        super.b0(cVar);
        int size = this.G0.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.G0.get(i8).b0(cVar);
        }
    }
}
