package j7;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class p<T> implements q<T> {

    /* renamed from: a  reason: collision with root package name */
    private final CountDownLatch f20831a = new CountDownLatch(1);

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ p(o oVar) {
    }

    @Override // j7.d
    public final void a() {
        this.f20831a.countDown();
    }

    @Override // j7.f
    public final void b(Exception exc) {
        this.f20831a.countDown();
    }

    @Override // j7.g
    public final void c(T t8) {
        this.f20831a.countDown();
    }

    public final void d() {
        this.f20831a.await();
    }

    public final boolean e(long j8, TimeUnit timeUnit) {
        return this.f20831a.await(j8, timeUnit);
    }
}
