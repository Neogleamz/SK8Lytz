package j7;

import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class v<TResult, TContinuationResult> implements g<TContinuationResult>, f, d, g0 {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f20840a;

    /* renamed from: b  reason: collision with root package name */
    private final c f20841b;

    /* renamed from: c  reason: collision with root package name */
    private final l0 f20842c;

    public v(Executor executor, c cVar, l0 l0Var) {
        this.f20840a = executor;
        this.f20841b = cVar;
        this.f20842c = l0Var;
    }

    @Override // j7.d
    public final void a() {
        this.f20842c.u();
    }

    @Override // j7.f
    public final void b(Exception exc) {
        this.f20842c.s(exc);
    }

    @Override // j7.g
    public final void c(TContinuationResult tcontinuationresult) {
        this.f20842c.t(tcontinuationresult);
    }

    @Override // j7.g0
    public final void d(j jVar) {
        this.f20840a.execute(new u(this, jVar));
    }
}
