package j7;

import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d0 implements g0 {

    /* renamed from: a  reason: collision with root package name */
    private final Executor f20806a;

    /* renamed from: b  reason: collision with root package name */
    private final Object f20807b = new Object();

    /* renamed from: c  reason: collision with root package name */
    private g f20808c;

    public d0(Executor executor, g gVar) {
        this.f20806a = executor;
        this.f20808c = gVar;
    }

    @Override // j7.g0
    public final void d(j jVar) {
        if (jVar.p()) {
            synchronized (this.f20807b) {
                if (this.f20808c == null) {
                    return;
                }
                this.f20806a.execute(new c0(this, jVar));
            }
        }
    }
}
