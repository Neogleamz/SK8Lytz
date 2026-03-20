package a0;

import androidx.camera.core.p1;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class g<V> implements com.google.common.util.concurrent.d<V> {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a<V> extends g<V> {

        /* renamed from: a  reason: collision with root package name */
        private final Throwable f24a;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(Throwable th) {
            this.f24a = th;
        }

        @Override // a0.g, java.util.concurrent.Future
        public V get() {
            throw new ExecutionException(this.f24a);
        }

        public String toString() {
            return super.toString() + "[status=FAILURE, cause=[" + this.f24a + "]]";
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b<V> extends a<V> implements ScheduledFuture<V> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public b(Throwable th) {
            super(th);
        }

        @Override // java.util.concurrent.Delayed
        public long getDelay(TimeUnit timeUnit) {
            return 0L;
        }

        @Override // java.lang.Comparable
        /* renamed from: h */
        public int compareTo(Delayed delayed) {
            return -1;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class c<V> extends g<V> {

        /* renamed from: b  reason: collision with root package name */
        static final g<Object> f25b = new c(null);

        /* renamed from: a  reason: collision with root package name */
        private final V f26a;

        /* JADX INFO: Access modifiers changed from: package-private */
        public c(V v8) {
            this.f26a = v8;
        }

        @Override // a0.g, java.util.concurrent.Future
        public V get() {
            return this.f26a;
        }

        public String toString() {
            return super.toString() + "[status=SUCCESS, result=[" + this.f26a + "]]";
        }
    }

    g() {
    }

    public static <V> com.google.common.util.concurrent.d<V> f() {
        return c.f25b;
    }

    @Override // com.google.common.util.concurrent.d
    public void c(Runnable runnable, Executor executor) {
        androidx.core.util.h.h(runnable);
        androidx.core.util.h.h(executor);
        try {
            executor.execute(runnable);
        } catch (RuntimeException e8) {
            p1.d("ImmediateFuture", "Experienced RuntimeException while attempting to notify " + runnable + " on Executor " + executor, e8);
        }
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z4) {
        return false;
    }

    @Override // java.util.concurrent.Future
    public abstract V get();

    @Override // java.util.concurrent.Future
    public V get(long j8, TimeUnit timeUnit) {
        androidx.core.util.h.h(timeUnit);
        return get();
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return true;
    }
}
