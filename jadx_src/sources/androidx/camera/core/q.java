package androidx.camera.core;

import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class q implements Executor {

    /* renamed from: c  reason: collision with root package name */
    private static final ThreadFactory f2775c = new a();

    /* renamed from: a  reason: collision with root package name */
    private final Object f2776a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private ThreadPoolExecutor f2777b = b();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements ThreadFactory {

        /* renamed from: a  reason: collision with root package name */
        private final AtomicInteger f2778a = new AtomicInteger(0);

        a() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName(String.format(Locale.US, "CameraX-core_camera_%d", Integer.valueOf(this.f2778a.getAndIncrement())));
            return thread;
        }
    }

    private static ThreadPoolExecutor b() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(), f2775c);
        threadPoolExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() { // from class: androidx.camera.core.p
            @Override // java.util.concurrent.RejectedExecutionHandler
            public final void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor2) {
                p1.c("CameraExecutor", "A rejected execution occurred in CameraExecutor!");
            }
        });
        return threadPoolExecutor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(y.p pVar) {
        ThreadPoolExecutor threadPoolExecutor;
        androidx.core.util.h.h(pVar);
        synchronized (this.f2776a) {
            if (this.f2777b.isShutdown()) {
                this.f2777b = b();
            }
            threadPoolExecutor = this.f2777b;
        }
        int max = Math.max(1, pVar.a().size());
        threadPoolExecutor.setMaximumPoolSize(max);
        threadPoolExecutor.setCorePoolSize(max);
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        androidx.core.util.h.h(runnable);
        synchronized (this.f2776a) {
            this.f2777b.execute(runnable);
        }
    }
}
