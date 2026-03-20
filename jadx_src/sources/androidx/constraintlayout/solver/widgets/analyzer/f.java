package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintWidget;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f extends k {
    public f(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        constraintWidget.f3672e.f();
        constraintWidget.f3674f.f();
        this.f3787f = ((androidx.constraintlayout.solver.widgets.f) constraintWidget).L0();
    }

    private void q(d dVar) {
        this.f3789h.f3757k.add(dVar);
        dVar.f3758l.add(this.f3789h);
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.k, o0.a
    public void a(o0.a aVar) {
        d dVar = this.f3789h;
        if (dVar.f3749c && !dVar.f3756j) {
            this.f3789h.d((int) ((dVar.f3758l.get(0).f3753g * ((androidx.constraintlayout.solver.widgets.f) this.f3783b).O0()) + 0.5f));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void d() {
        d dVar;
        k kVar;
        d dVar2;
        androidx.constraintlayout.solver.widgets.f fVar = (androidx.constraintlayout.solver.widgets.f) this.f3783b;
        int M0 = fVar.M0();
        int N0 = fVar.N0();
        fVar.O0();
        if (fVar.L0() == 1) {
            d dVar3 = this.f3789h;
            if (M0 != -1) {
                dVar3.f3758l.add(this.f3783b.P.f3672e.f3789h);
                this.f3783b.P.f3672e.f3789h.f3757k.add(this.f3789h);
                dVar2 = this.f3789h;
            } else if (N0 != -1) {
                dVar3.f3758l.add(this.f3783b.P.f3672e.f3790i);
                this.f3783b.P.f3672e.f3790i.f3757k.add(this.f3789h);
                dVar2 = this.f3789h;
                M0 = -N0;
            } else {
                dVar3.f3748b = true;
                dVar3.f3758l.add(this.f3783b.P.f3672e.f3790i);
                this.f3783b.P.f3672e.f3790i.f3757k.add(this.f3789h);
                q(this.f3783b.f3672e.f3789h);
                kVar = this.f3783b.f3672e;
            }
            dVar2.f3752f = M0;
            q(this.f3783b.f3672e.f3789h);
            kVar = this.f3783b.f3672e;
        } else {
            d dVar4 = this.f3789h;
            if (M0 != -1) {
                dVar4.f3758l.add(this.f3783b.P.f3674f.f3789h);
                this.f3783b.P.f3674f.f3789h.f3757k.add(this.f3789h);
                dVar = this.f3789h;
            } else if (N0 != -1) {
                dVar4.f3758l.add(this.f3783b.P.f3674f.f3790i);
                this.f3783b.P.f3674f.f3790i.f3757k.add(this.f3789h);
                dVar = this.f3789h;
                M0 = -N0;
            } else {
                dVar4.f3748b = true;
                dVar4.f3758l.add(this.f3783b.P.f3674f.f3790i);
                this.f3783b.P.f3674f.f3790i.f3757k.add(this.f3789h);
                q(this.f3783b.f3674f.f3789h);
                kVar = this.f3783b.f3674f;
            }
            dVar.f3752f = M0;
            q(this.f3783b.f3674f.f3789h);
            kVar = this.f3783b.f3674f;
        }
        q(kVar.f3790i);
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void e() {
        if (((androidx.constraintlayout.solver.widgets.f) this.f3783b).L0() == 1) {
            this.f3783b.G0(this.f3789h.f3753g);
        } else {
            this.f3783b.H0(this.f3789h.f3753g);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public void f() {
        this.f3789h.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.k
    public boolean m() {
        return false;
    }
}
