package e6;

import com.google.android.gms.common.util.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
@VisibleForTesting
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c extends Thread {

    /* renamed from: a  reason: collision with root package name */
    private final WeakReference<a> f19811a;

    /* renamed from: b  reason: collision with root package name */
    private final long f19812b;

    /* renamed from: c  reason: collision with root package name */
    final CountDownLatch f19813c = new CountDownLatch(1);

    /* renamed from: d  reason: collision with root package name */
    boolean f19814d = false;

    public c(a aVar, long j8) {
        this.f19811a = new WeakReference<>(aVar);
        this.f19812b = j8;
        start();
    }

    private final void a() {
        a aVar = this.f19811a.get();
        if (aVar != null) {
            aVar.c();
            this.f19814d = true;
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public final void run() {
        try {
            if (this.f19813c.await(this.f19812b, TimeUnit.MILLISECONDS)) {
                return;
            }
            a();
        } catch (InterruptedException unused) {
            a();
        }
    }
}
