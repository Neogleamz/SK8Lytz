package v6;

import android.os.Process;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final Runnable f23348a;

    public c(Runnable runnable, int i8) {
        this.f23348a = runnable;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Process.setThreadPriority(0);
        this.f23348a.run();
    }
}
