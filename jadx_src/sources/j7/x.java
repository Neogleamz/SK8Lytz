package j7;

import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class x implements g0 {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f20844a;

    /* renamed from: b  reason: collision with root package name */
    private final Object f20845b = new Object();

    /* renamed from: c  reason: collision with root package name */
    private d f20846c;

    public x(Executor executor, d dVar) {
        this.f20844a = executor;
        this.f20846c = dVar;
    }

    @Override // j7.g0
    public final void d(j jVar) {
        if (jVar.n()) {
            synchronized (this.f20845b) {
                if (this.f20846c == null) {
                    return;
                }
                this.f20844a.execute(new w(this));
            }
        }
    }
}
