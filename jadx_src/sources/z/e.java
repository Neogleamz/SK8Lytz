package z;

import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e implements Executor {

    /* renamed from: b  reason: collision with root package name */
    private static volatile Executor f24497b;

    /* renamed from: a  reason: collision with root package name */
    private final ExecutorService f24498a = Executors.newFixedThreadPool(2, new a());

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements ThreadFactory {

        /* renamed from: a  reason: collision with root package name */
        private final AtomicInteger f24499a = new AtomicInteger(0);

        a() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName(String.format(Locale.US, "CameraX-camerax_io_%d", Integer.valueOf(this.f24499a.getAndIncrement())));
            return thread;
        }
    }

    e() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Executor a() {
        if (f24497b != null) {
            return f24497b;
        }
        synchronized (e.class) {
            if (f24497b == null) {
                f24497b = new e();
            }
        }
        return f24497b;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        this.f24498a.execute(runnable);
    }
}
