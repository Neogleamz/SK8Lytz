package j7;

import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class z implements g0 {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f20849a;

    /* renamed from: b  reason: collision with root package name */
    private final Object f20850b = new Object();

    /* renamed from: c  reason: collision with root package name */
    private e f20851c;

    public z(Executor executor, e eVar) {
        this.f20849a = executor;
        this.f20851c = eVar;
    }

    @Override // j7.g0
    public final void d(j jVar) {
        synchronized (this.f20850b) {
            if (this.f20851c == null) {
                return;
            }
            this.f20849a.execute(new y(this, jVar));
        }
    }
}
