package z;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.concurrent.futures.c;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c extends AbstractExecutorService implements ScheduledExecutorService {

    /* renamed from: b  reason: collision with root package name */
    private static ThreadLocal<ScheduledExecutorService> f24482b = new a();

    /* renamed from: a  reason: collision with root package name */
    private final Handler f24483a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends ThreadLocal<ScheduledExecutorService> {
        a() {
        }

        @Override // java.lang.ThreadLocal
        /* renamed from: a */
        public ScheduledExecutorService initialValue() {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                return z.a.d();
            }
            if (Looper.myLooper() != null) {
                return new c(new Handler(Looper.myLooper()));
            }
            return null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements Callable<Void> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Runnable f24484a;

        b(Runnable runnable) {
            this.f24484a = runnable;
        }

        @Override // java.util.concurrent.Callable
        /* renamed from: a */
        public Void call() {
            this.f24484a.run();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: z.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class RunnableScheduledFutureC0233c<V> implements RunnableScheduledFuture<V> {

        /* renamed from: a  reason: collision with root package name */
        final AtomicReference<c.a<V>> f24486a = new AtomicReference<>(null);

        /* renamed from: b  reason: collision with root package name */
        private final long f24487b;

        /* renamed from: c  reason: collision with root package name */
        private final Callable<V> f24488c;

        /* renamed from: d  reason: collision with root package name */
        private final com.google.common.util.concurrent.d<V> f24489d;

        /* renamed from: z.c$c$a */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements c.InterfaceC0024c<V> {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ Handler f24490a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ Callable f24491b;

            /* renamed from: z.c$c$a$a  reason: collision with other inner class name */
            /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
            class RunnableC0234a implements Runnable {
                RunnableC0234a() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (RunnableScheduledFutureC0233c.this.f24486a.getAndSet(null) != null) {
                        a aVar = a.this;
                        aVar.f24490a.removeCallbacks(RunnableScheduledFutureC0233c.this);
                    }
                }
            }

            a(Handler handler, Callable callable) {
                this.f24490a = handler;
                this.f24491b = callable;
            }

            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public Object a(c.a<V> aVar) {
                aVar.a(new RunnableC0234a(), z.a.a());
                RunnableScheduledFutureC0233c.this.f24486a.set(aVar);
                return "HandlerScheduledFuture-" + this.f24491b.toString();
            }
        }

        RunnableScheduledFutureC0233c(Handler handler, long j8, Callable<V> callable) {
            this.f24487b = j8;
            this.f24488c = callable;
            this.f24489d = androidx.concurrent.futures.c.a(new a(handler, callable));
        }

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean z4) {
            return this.f24489d.cancel(z4);
        }

        @Override // java.lang.Comparable
        /* renamed from: f */
        public int compareTo(Delayed delayed) {
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            return Long.compare(getDelay(timeUnit), delayed.getDelay(timeUnit));
        }

        @Override // java.util.concurrent.Future
        public V get() {
            return this.f24489d.get();
        }

        @Override // java.util.concurrent.Future
        public V get(long j8, TimeUnit timeUnit) {
            return this.f24489d.get(j8, timeUnit);
        }

        @Override // java.util.concurrent.Delayed
        public long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(this.f24487b - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return this.f24489d.isCancelled();
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return this.f24489d.isDone();
        }

        @Override // java.util.concurrent.RunnableScheduledFuture
        public boolean isPeriodic() {
            return false;
        }

        @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
        public void run() {
            c.a<V> andSet = this.f24486a.getAndSet(null);
            if (andSet != null) {
                try {
                    andSet.c(this.f24488c.call());
                } catch (Exception e8) {
                    andSet.f(e8);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(Handler handler) {
        this.f24483a = handler;
    }

    private RejectedExecutionException a() {
        return new RejectedExecutionException(this.f24483a + " is shutting down");
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long j8, TimeUnit timeUnit) {
        throw new UnsupportedOperationException(c.class.getSimpleName() + " cannot be shut down. Use Looper.quitSafely().");
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        if (!this.f24483a.post(runnable)) {
            throw a();
        }
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return false;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return false;
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> schedule(Runnable runnable, long j8, TimeUnit timeUnit) {
        return schedule(new b(runnable), j8, timeUnit);
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long j8, TimeUnit timeUnit) {
        long uptimeMillis = SystemClock.uptimeMillis() + TimeUnit.MILLISECONDS.convert(j8, timeUnit);
        RunnableScheduledFutureC0233c runnableScheduledFutureC0233c = new RunnableScheduledFutureC0233c(this.f24483a, uptimeMillis, callable);
        return this.f24483a.postAtTime(runnableScheduledFutureC0233c, uptimeMillis) ? runnableScheduledFutureC0233c : a0.f.g(a());
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j8, long j9, TimeUnit timeUnit) {
        throw new UnsupportedOperationException(c.class.getSimpleName() + " does not yet support fixed-rate scheduling.");
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j8, long j9, TimeUnit timeUnit) {
        throw new UnsupportedOperationException(c.class.getSimpleName() + " does not yet support fixed-delay scheduling.");
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        throw new UnsupportedOperationException(c.class.getSimpleName() + " cannot be shut down. Use Looper.quitSafely().");
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        throw new UnsupportedOperationException(c.class.getSimpleName() + " cannot be shut down. Use Looper.quitSafely().");
    }
}
