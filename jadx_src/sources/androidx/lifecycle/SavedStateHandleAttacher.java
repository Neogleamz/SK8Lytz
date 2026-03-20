package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SavedStateHandleAttacher implements h {

    /* renamed from: a  reason: collision with root package name */
    private final y f5838a;

    public SavedStateHandleAttacher(y yVar) {
        kotlin.jvm.internal.p.e(yVar, "provider");
        this.f5838a = yVar;
    }

    @Override // androidx.lifecycle.h
    public void c(j jVar, Lifecycle.Event event) {
        kotlin.jvm.internal.p.e(jVar, "source");
        kotlin.jvm.internal.p.e(event, "event");
        if (event == Lifecycle.Event.ON_CREATE) {
            jVar.getLifecycle().c(this);
            this.f5838a.d();
            return;
        }
        throw new IllegalStateException(("Next event must be ON_CREATE, it was " + event).toString());
    }
}
