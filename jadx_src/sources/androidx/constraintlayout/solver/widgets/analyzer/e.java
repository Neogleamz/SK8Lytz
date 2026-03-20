package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.analyzer.d;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e extends d {

    /* renamed from: m  reason: collision with root package name */
    public int f3768m;

    public e(k kVar) {
        super(kVar);
        this.f3751e = kVar instanceof h ? d.a.HORIZONTAL_DIMENSION : d.a.VERTICAL_DIMENSION;
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.d
    public void d(int i8) {
        if (this.f3756j) {
            return;
        }
        this.f3756j = true;
        this.f3753g = i8;
        for (o0.a aVar : this.f3757k) {
            aVar.a(aVar);
        }
    }
}
