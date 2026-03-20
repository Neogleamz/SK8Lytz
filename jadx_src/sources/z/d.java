package z;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d implements Executor {

    /* renamed from: b  reason: collision with root package name */
    private static volatile Executor f24494b;

    /* renamed from: a  reason: collision with root package name */
    private final ExecutorService f24495a = Executors.newSingleThreadExecutor(new a());

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements ThreadFactory {
        a() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setPriority(10);
            thread.setName("CameraX-camerax_high_priority");
            return thread;
        }
    }

    d() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Executor a() {
        if (f24494b != null) {
            return f24494b;
        }
        synchronized (d.class) {
            if (f24494b == null) {
                f24494b = new d();
            }
        }
        return f24494b;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        this.f24495a.execute(runnable);
    }
}
