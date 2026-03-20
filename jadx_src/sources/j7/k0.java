package j7;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k0 implements Executor {

    /* renamed from: a  reason: collision with root package name */
    private final Handler f20819a = new d7.a(Looper.getMainLooper());

    @Override // java.util.concurrent.Executor
    public final void execute(Runnable runnable) {
        this.f20819a.post(runnable);
    }
}
