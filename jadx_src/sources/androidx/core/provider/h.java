package androidx.core.provider;

import android.os.Handler;
import android.os.Process;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class h {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a implements ThreadFactory {

        /* renamed from: a  reason: collision with root package name */
        private String f4827a;

        /* renamed from: b  reason: collision with root package name */
        private int f4828b;

        /* renamed from: androidx.core.provider.h$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private static class C0037a extends Thread {

            /* renamed from: a  reason: collision with root package name */
            private final int f4829a;

            C0037a(Runnable runnable, String str, int i8) {
                super(runnable, str);
                this.f4829a = i8;
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Process.setThreadPriority(this.f4829a);
                super.run();
            }
        }

        a(String str, int i8) {
            this.f4827a = str;
            this.f4828b = i8;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            return new C0037a(runnable, this.f4827a, this.f4828b);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b<T> implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        private Callable<T> f4830a;

        /* renamed from: b  reason: collision with root package name */
        private androidx.core.util.a<T> f4831b;

        /* renamed from: c  reason: collision with root package name */
        private Handler f4832c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ androidx.core.util.a f4833a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ Object f4834b;

            a(androidx.core.util.a aVar, Object obj) {
                this.f4833a = aVar;
                this.f4834b = obj;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.lang.Runnable
            public void run() {
                this.f4833a.accept(this.f4834b);
            }
        }

        b(Handler handler, Callable<T> callable, androidx.core.util.a<T> aVar) {
            this.f4830a = callable;
            this.f4831b = aVar;
            this.f4832c = handler;
        }

        @Override // java.lang.Runnable
        public void run() {
            T t8;
            try {
                t8 = this.f4830a.call();
            } catch (Exception unused) {
                t8 = null;
            }
            this.f4832c.post(new a(this.f4831b, t8));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ThreadPoolExecutor a(String str, int i8, int i9) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, i9, TimeUnit.MILLISECONDS, new LinkedBlockingDeque(), new a(str, i8));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> void b(Executor executor, Callable<T> callable, androidx.core.util.a<T> aVar) {
        executor.execute(new b(androidx.core.provider.b.a(), callable, aVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T c(ExecutorService executorService, Callable<T> callable, int i8) {
        try {
            return executorService.submit(callable).get(i8, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e8) {
            throw e8;
        } catch (ExecutionException e9) {
            throw new RuntimeException(e9);
        } catch (TimeoutException unused) {
            throw new InterruptedException("timeout");
        }
    }
}
