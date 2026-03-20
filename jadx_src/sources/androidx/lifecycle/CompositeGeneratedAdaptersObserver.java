package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class CompositeGeneratedAdaptersObserver implements h {

    /* renamed from: a  reason: collision with root package name */
    private final d[] f5798a;

    public CompositeGeneratedAdaptersObserver(d[] dVarArr) {
        kotlin.jvm.internal.p.e(dVarArr, "generatedAdapters");
        this.f5798a = dVarArr;
    }

    @Override // androidx.lifecycle.h
    public void c(j jVar, Lifecycle.Event event) {
        kotlin.jvm.internal.p.e(jVar, "source");
        kotlin.jvm.internal.p.e(event, "event");
        o oVar = new o();
        for (d dVar : this.f5798a) {
            dVar.a(jVar, event, false, oVar);
        }
        for (d dVar2 : this.f5798a) {
            dVar2.a(jVar, event, true, oVar);
        }
    }
}
