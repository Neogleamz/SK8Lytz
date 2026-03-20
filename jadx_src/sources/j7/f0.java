package j7;

import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f0<TResult, TContinuationResult> implements g<TContinuationResult>, f, d, g0 {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f20811a;

    /* renamed from: b  reason: collision with root package name */
    private final i f20812b;

    /* renamed from: c  reason: collision with root package name */
    private final l0 f20813c;

    public f0(Executor executor, i iVar, l0 l0Var) {
        this.f20811a = executor;
        this.f20812b = iVar;
        this.f20813c = l0Var;
    }

    @Override // j7.d
    public final void a() {
        this.f20813c.u();
    }

    @Override // j7.f
    public final void b(Exception exc) {
        this.f20813c.s(exc);
    }

    @Override // j7.g
    public final void c(TContinuationResult tcontinuationresult) {
        this.f20813c.t(tcontinuationresult);
    }

    @Override // j7.g0
    public final void d(j jVar) {
        this.f20811a.execute(new e0(this, jVar));
    }
}
