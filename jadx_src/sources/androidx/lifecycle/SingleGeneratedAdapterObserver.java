package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SingleGeneratedAdapterObserver implements h {

    /* renamed from: a  reason: collision with root package name */
    private final d f5842a;

    public SingleGeneratedAdapterObserver(d dVar) {
        kotlin.jvm.internal.p.e(dVar, "generatedAdapter");
        this.f5842a = dVar;
    }

    @Override // androidx.lifecycle.h
    public void c(j jVar, Lifecycle.Event event) {
        kotlin.jvm.internal.p.e(jVar, "source");
        kotlin.jvm.internal.p.e(event, "event");
        this.f5842a.a(jVar, event, false, null);
        this.f5842a.a(jVar, event, true, null);
    }
}
