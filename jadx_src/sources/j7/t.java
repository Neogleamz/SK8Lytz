package j7;

import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class t implements g0 {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f20835a;

    /* renamed from: b  reason: collision with root package name */
    private final c f20836b;

    /* renamed from: c  reason: collision with root package name */
    private final l0 f20837c;

    public t(Executor executor, c cVar, l0 l0Var) {
        this.f20835a = executor;
        this.f20836b = cVar;
        this.f20837c = l0Var;
    }

    @Override // j7.g0
    public final void d(j jVar) {
        this.f20835a.execute(new s(this, jVar));
    }
}
