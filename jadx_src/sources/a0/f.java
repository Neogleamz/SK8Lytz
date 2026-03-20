package a0;

import a0.g;
import androidx.concurrent.futures.c;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* renamed from: a  reason: collision with root package name */
    private static final n.a<?, ?> f17a = new b();

    /* JADX INFO: Add missing generic type declarations: [I, O] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a<I, O> implements a0.a<I, O> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ n.a f18a;

        a(n.a aVar) {
            this.f18a = aVar;
        }

        @Override // a0.a
        public com.google.common.util.concurrent.d<O> apply(I i8) {
            return f.h(this.f18a.apply(i8));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements n.a<Object, Object> {
        b() {
        }

        @Override // n.a
        public Object apply(Object obj) {
            return obj;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [I] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c<I> implements a0.c<I> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ c.a f19a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ n.a f20b;

        c(c.a aVar, n.a aVar2) {
            this.f19a = aVar;
            this.f20b = aVar2;
        }

        @Override // a0.c
        public void c(I i8) {
            try {
                this.f19a.c(this.f20b.apply(i8));
            } catch (Throwable th) {
                this.f19a.f(th);
            }
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            this.f19a.f(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ com.google.common.util.concurrent.d f21a;

        d(com.google.common.util.concurrent.d dVar) {
            this.f21a = dVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f21a.cancel(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e<V> implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final Future<V> f22a;

        /* renamed from: b  reason: collision with root package name */
        final a0.c<? super V> f23b;

        e(Future<V> future, a0.c<? super V> cVar) {
            this.f22a = future;
            this.f23b = cVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.f23b.c(f.d(this.f22a));
            } catch (Error e8) {
                e = e8;
                this.f23b.onFailure(e);
            } catch (RuntimeException e9) {
                e = e9;
                this.f23b.onFailure(e);
            } catch (ExecutionException e10) {
                Throwable cause = e10.getCause();
                if (cause == null) {
                    this.f23b.onFailure(e10);
                } else {
                    this.f23b.onFailure(cause);
                }
            }
        }

        public String toString() {
            return e.class.getSimpleName() + "," + this.f23b;
        }
    }

    public static <V> void b(com.google.common.util.concurrent.d<V> dVar, a0.c<? super V> cVar, Executor executor) {
        androidx.core.util.h.h(cVar);
        dVar.c(new e(dVar, cVar), executor);
    }

    public static <V> com.google.common.util.concurrent.d<List<V>> c(Collection<? extends com.google.common.util.concurrent.d<? extends V>> collection) {
        return new h(new ArrayList(collection), true, z.a.a());
    }

    public static <V> V d(Future<V> future) {
        boolean isDone = future.isDone();
        androidx.core.util.h.k(isDone, "Future was expected to be done, " + future);
        return (V) e(future);
    }

    public static <V> V e(Future<V> future) {
        V v8;
        boolean z4 = false;
        while (true) {
            try {
                v8 = future.get();
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
        return v8;
    }

    public static <V> com.google.common.util.concurrent.d<V> f(Throwable th) {
        return new g.a(th);
    }

    public static <V> ScheduledFuture<V> g(Throwable th) {
        return new g.b(th);
    }

    public static <V> com.google.common.util.concurrent.d<V> h(V v8) {
        return v8 == null ? g.f() : new g.c(v8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object i(com.google.common.util.concurrent.d dVar, c.a aVar) {
        m(false, dVar, f17a, aVar, z.a.a());
        return "nonCancellationPropagating[" + dVar + "]";
    }

    public static <V> com.google.common.util.concurrent.d<V> j(final com.google.common.util.concurrent.d<V> dVar) {
        androidx.core.util.h.h(dVar);
        return dVar.isDone() ? dVar : androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: a0.e
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar) {
                Object i8;
                i8 = f.i(com.google.common.util.concurrent.d.this, aVar);
                return i8;
            }
        });
    }

    public static <V> void k(com.google.common.util.concurrent.d<V> dVar, c.a<V> aVar) {
        l(dVar, f17a, aVar, z.a.a());
    }

    public static <I, O> void l(com.google.common.util.concurrent.d<I> dVar, n.a<? super I, ? extends O> aVar, c.a<O> aVar2, Executor executor) {
        m(true, dVar, aVar, aVar2, executor);
    }

    private static <I, O> void m(boolean z4, com.google.common.util.concurrent.d<I> dVar, n.a<? super I, ? extends O> aVar, c.a<O> aVar2, Executor executor) {
        androidx.core.util.h.h(dVar);
        androidx.core.util.h.h(aVar);
        androidx.core.util.h.h(aVar2);
        androidx.core.util.h.h(executor);
        b(dVar, new c(aVar2, aVar), executor);
        if (z4) {
            aVar2.a(new d(dVar), z.a.a());
        }
    }

    public static <V> com.google.common.util.concurrent.d<List<V>> n(Collection<? extends com.google.common.util.concurrent.d<? extends V>> collection) {
        return new h(new ArrayList(collection), false, z.a.a());
    }

    public static <I, O> com.google.common.util.concurrent.d<O> o(com.google.common.util.concurrent.d<I> dVar, n.a<? super I, ? extends O> aVar, Executor executor) {
        androidx.core.util.h.h(aVar);
        return p(dVar, new a(aVar), executor);
    }

    public static <I, O> com.google.common.util.concurrent.d<O> p(com.google.common.util.concurrent.d<I> dVar, a0.a<? super I, ? extends O> aVar, Executor executor) {
        a0.b bVar = new a0.b(aVar, dVar);
        dVar.c(bVar, executor);
        return bVar;
    }
}
