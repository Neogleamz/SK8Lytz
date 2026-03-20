package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SavedStateHandleController implements h {

    /* renamed from: a  reason: collision with root package name */
    private final String f5839a;

    /* renamed from: b  reason: collision with root package name */
    private final w f5840b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f5841c;

    public SavedStateHandleController(String str, w wVar) {
        kotlin.jvm.internal.p.e(str, "key");
        kotlin.jvm.internal.p.e(wVar, "handle");
        this.f5839a = str;
        this.f5840b = wVar;
    }

    public final void a(androidx.savedstate.a aVar, Lifecycle lifecycle) {
        kotlin.jvm.internal.p.e(aVar, "registry");
        kotlin.jvm.internal.p.e(lifecycle, "lifecycle");
        if (!(!this.f5841c)) {
            throw new IllegalStateException("Already attached to lifecycleOwner".toString());
        }
        this.f5841c = true;
        lifecycle.a(this);
        aVar.h(this.f5839a, this.f5840b.c());
    }

    public final w b() {
        return this.f5840b;
    }

    @Override // androidx.lifecycle.h
    public void c(j jVar, Lifecycle.Event event) {
        kotlin.jvm.internal.p.e(jVar, "source");
        kotlin.jvm.internal.p.e(event, "event");
        if (event == Lifecycle.Event.ON_DESTROY) {
            this.f5841c = false;
            jVar.getLifecycle().c(this);
        }
    }

    public final boolean d() {
        return this.f5841c;
    }
}
