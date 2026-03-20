package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import java.util.concurrent.CancellationException;
import xj.n1;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class LifecycleCoroutineScopeImpl extends f implements h {

    /* renamed from: a  reason: collision with root package name */
    private final Lifecycle f5814a;

    /* renamed from: b  reason: collision with root package name */
    private final fj.f f5815b;

    public Lifecycle a() {
        return this.f5814a;
    }

    @Override // androidx.lifecycle.h
    public void c(j jVar, Lifecycle.Event event) {
        kotlin.jvm.internal.p.e(jVar, "source");
        kotlin.jvm.internal.p.e(event, "event");
        if (a().b().compareTo(Lifecycle.State.DESTROYED) <= 0) {
            a().c(this);
            n1.d(e(), (CancellationException) null, 1, (Object) null);
        }
    }

    public fj.f e() {
        return this.f5815b;
    }
}
