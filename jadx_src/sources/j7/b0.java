package j7;

import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b0 implements g0 {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f20801a;

    /* renamed from: b  reason: collision with root package name */
    private final Object f20802b = new Object();

    /* renamed from: c  reason: collision with root package name */
    private f f20803c;

    public b0(Executor executor, f fVar) {
        this.f20801a = executor;
        this.f20803c = fVar;
    }

    @Override // j7.g0
    public final void d(j jVar) {
        if (jVar.p() || jVar.n()) {
            return;
        }
        synchronized (this.f20802b) {
            if (this.f20803c == null) {
                return;
            }
            this.f20801a.execute(new a0(this, jVar));
        }
    }
}
