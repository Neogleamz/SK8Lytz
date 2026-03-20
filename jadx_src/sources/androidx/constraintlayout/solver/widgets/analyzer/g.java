package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.analyzer.d;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g extends k {
    public g(ConstraintWidget constraintWidget) {
        super(constraintWidget);
    }

    private void q(d dVar) {
        this.f3789h.f3757k.add(dVar);
        dVar.f3758l.add(this.f3789h);
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.k, o0.a
    public void a(o0.a aVar) {
        androidx.constraintlayout.solver.widgets.a aVar2 = (androidx.constraintlayout.solver.widgets.a) this.f3783b;
        int M0 = aVar2.M0();
        int i8 = 0;
        int i9 = -1;
        for (d dVar : this.f3789h.f3758l) {
            int i10 = dVar.f3753g;
            if (i9 == -1 || i10 < i9) {
                i9 = i10;
            }
            if (i8 < i10) {
                i8 = i10;
            }
        }
        if (M0 == 0 || M0 == 2) {
            this.f3789h.d(i9 + aVar2.N0());
        } else {
            this.f3789h.d(i8 + aVar2.N0());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void d() {
        k kVar;
        ConstraintWidget constraintWidget = this.f3783b;
        if (constraintWidget instanceof androidx.constraintlayout.solver.widgets.a) {
            this.f3789h.f3748b = true;
            androidx.constraintlayout.solver.widgets.a aVar = (androidx.constraintlayout.solver.widgets.a) constraintWidget;
            int M0 = aVar.M0();
            boolean L0 = aVar.L0();
            int i8 = 0;
            if (M0 == 0) {
                this.f3789h.f3751e = d.a.LEFT;
                while (i8 < aVar.H0) {
                    ConstraintWidget constraintWidget2 = aVar.G0[i8];
                    if (L0 || constraintWidget2.P() != 8) {
                        d dVar = constraintWidget2.f3672e.f3789h;
                        dVar.f3757k.add(this.f3789h);
                        this.f3789h.f3758l.add(dVar);
                    }
                    i8++;
                }
            } else if (M0 != 1) {
                if (M0 == 2) {
                    this.f3789h.f3751e = d.a.TOP;
                    while (i8 < aVar.H0) {
                        ConstraintWidget constraintWidget3 = aVar.G0[i8];
                        if (L0 || constraintWidget3.P() != 8) {
                            d dVar2 = constraintWidget3.f3674f.f3789h;
                            dVar2.f3757k.add(this.f3789h);
                            this.f3789h.f3758l.add(dVar2);
                        }
                        i8++;
                    }
                } else if (M0 != 3) {
                    return;
                } else {
                    this.f3789h.f3751e = d.a.BOTTOM;
                    while (i8 < aVar.H0) {
                        ConstraintWidget constraintWidget4 = aVar.G0[i8];
                        if (L0 || constraintWidget4.P() != 8) {
                            d dVar3 = constraintWidget4.f3674f.f3790i;
                            dVar3.f3757k.add(this.f3789h);
                            this.f3789h.f3758l.add(dVar3);
                        }
                        i8++;
                    }
                }
                q(this.f3783b.f3674f.f3789h);
                kVar = this.f3783b.f3674f;
                q(kVar.f3790i);
            } else {
                this.f3789h.f3751e = d.a.RIGHT;
                while (i8 < aVar.H0) {
                    ConstraintWidget constraintWidget5 = aVar.G0[i8];
                    if (L0 || constraintWidget5.P() != 8) {
                        d dVar4 = constraintWidget5.f3672e.f3790i;
                        dVar4.f3757k.add(this.f3789h);
                        this.f3789h.f3758l.add(dVar4);
                    }
                    i8++;
                }
            }
            q(this.f3783b.f3672e.f3789h);
            kVar = this.f3783b.f3672e;
            q(kVar.f3790i);
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void e() {
        ConstraintWidget constraintWidget = this.f3783b;
        if (constraintWidget instanceof androidx.constraintlayout.solver.widgets.a) {
            int M0 = ((androidx.constraintlayout.solver.widgets.a) constraintWidget).M0();
            if (M0 == 0 || M0 == 1) {
                this.f3783b.G0(this.f3789h.f3753g);
            } else {
                this.f3783b.H0(this.f3789h.f3753g);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void f() {
        this.f3784c = null;
        this.f3789h.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public boolean m() {
        return false;
    }
}
