package a0;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b<I, O> extends d<O> implements Runnable {

    /* renamed from: c  reason: collision with root package name */
    private a0.a<? super I, ? extends O> f6c;

    /* renamed from: d  reason: collision with root package name */
    private final BlockingQueue<Boolean> f7d = new LinkedBlockingQueue(1);

    /* renamed from: e  reason: collision with root package name */
    private final CountDownLatch f8e = new CountDownLatch(1);

    /* renamed from: f  reason: collision with root package name */
    private com.google.common.util.concurrent.d<? extends I> f9f;

    /* renamed from: g  reason: collision with root package name */
    volatile com.google.common.util.concurrent.d<? extends O> f10g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ com.google.common.util.concurrent.d f11a;

        a(com.google.common.util.concurrent.d dVar) {
            this.f11a = dVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                try {
                    b.this.b(f.e(this.f11a));
                } catch (CancellationException unused) {
                    b.this.cancel(false);
                    b.this.f10g = null;
                    return;
                } catch (ExecutionException e8) {
                    b.this.d(e8.getCause());
                }
                b.this.f10g = null;
            } catch (Throwable th) {
                b.this.f10g = null;
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(a0.a<? super I, ? extends O> aVar, com.google.common.util.concurrent.d<? extends I> dVar) {
        this.f6c = (a0.a) androidx.core.util.h.h(aVar);
        this.f9f = (com.google.common.util.concurrent.d) androidx.core.util.h.h(dVar);
    }

    private void g(Future<?> future, boolean z4) {
        if (future != null) {
            future.cancel(z4);
        }
    }

    private <E> void h(BlockingQueue<E> blockingQueue, E e8) {
        boolean z4 = false;
        while (true) {
            try {
                blockingQueue.put(e8);
                break;
            } catch (InterruptedException unused) {
                z4 = true;
            } catch (Throwable th) {
                if (z4) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (z4) {
            Thread.currentThread().interrupt();
        }
    }

    private <E> E i(BlockingQueue<E> blockingQueue) {
        E take;
        boolean z4 = false;
        while (true) {
            try {
                take = blockingQueue.take();
                break;
            } catch (InterruptedException unused) {
                z4 = true;
            } catch (Throwable th) {
                if (z4) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (z4) {
            Thread.currentThread().interrupt();
        }
        return take;
    }

    @Override // a0.d, java.util.concurrent.Future
    public boolean cancel(boolean z4) {
        if (super.cancel(z4)) {
            h(this.f7d, Boolean.valueOf(z4));
            g(this.f9f, z4);
            g(this.f10g, z4);
            return true;
        }
        return false;
    }

    @Override // a0.d, java.util.concurrent.Future
    public O get() {
        if (!isDone()) {
            com.google.common.util.concurrent.d<? extends I> dVar = this.f9f;
            if (dVar != null) {
                dVar.get();
            }
            this.f8e.await();
            com.google.common.util.concurrent.d<? extends O> dVar2 = this.f10g;
            if (dVar2 != null) {
                dVar2.get();
            }
        }
        return (O) super.get();
    }

    @Override // a0.d, java.util.concurrent.Future
    public O get(long j8, TimeUnit timeUnit) {
        if (!isDone()) {
            TimeUnit timeUnit2 = TimeUnit.NANOSECONDS;
            if (timeUnit != timeUnit2) {
                j8 = timeUnit2.convert(j8, timeUnit);
                timeUnit = timeUnit2;
            }
            com.google.common.util.concurrent.d<? extends I> dVar = this.f9f;
            if (dVar != null) {
                long nanoTime = System.nanoTime();
                dVar.get(j8, timeUnit);
                j8 -= Math.max(0L, System.nanoTime() - nanoTime);
            }
            long nanoTime2 = System.nanoTime();
            if (!this.f8e.await(j8, timeUnit)) {
                throw new TimeoutException();
            }
            j8 -= Math.max(0L, System.nanoTime() - nanoTime2);
            com.google.common.util.concurrent.d<? extends O> dVar2 = this.f10g;
            if (dVar2 != null) {
                dVar2.get(j8, timeUnit);
            }
        }
        return (O) super.get(j8, timeUnit);
    }

    @Override // java.lang.Runnable
    public void run() {
        com.google.common.util.concurrent.d<? extends O> apply;
        try {
            try {
                try {
                    apply = this.f6c.apply(f.e(this.f9f));
                    this.f10g = apply;
                } catch (Throwable th) {
                    this.f6c = null;
                    this.f9f = null;
                    this.f8e.countDown();
                    throw th;
                }
            } catch (CancellationException unused) {
                cancel(false);
            } catch (ExecutionException e8) {
                d(e8.getCause());
            }
        } catch (Error e9) {
            e = e9;
            d(e);
            this.f6c = null;
            this.f9f = null;
            this.f8e.countDown();
            return;
        } catch (UndeclaredThrowableException e10) {
            e = e10.getCause();
            d(e);
            this.f6c = null;
            this.f9f = null;
            this.f8e.countDown();
            return;
        } catch (Exception e11) {
            e = e11;
            d(e);
            this.f6c = null;
            this.f9f = null;
            this.f8e.countDown();
            return;
        }
        if (!isCancelled()) {
            apply.c(new a(apply), z.a.a());
            this.f6c = null;
            this.f9f = null;
            this.f8e.countDown();
            return;
        }
        apply.cancel(((Boolean) i(this.f7d)).booleanValue());
        this.f10g = null;
        this.f6c = null;
        this.f9f = null;
        this.f8e.countDown();
    }
}
