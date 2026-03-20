package a0;

import androidx.concurrent.futures.c;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d<V> implements com.google.common.util.concurrent.d<V> {

    /* renamed from: a  reason: collision with root package name */
    private final com.google.common.util.concurrent.d<V> f13a;

    /* renamed from: b  reason: collision with root package name */
    c.a<V> f14b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements c.InterfaceC0024c<V> {
        a() {
        }

        @Override // androidx.concurrent.futures.c.InterfaceC0024c
        public Object a(c.a<V> aVar) {
            androidx.core.util.h.k(d.this.f14b == null, "The result can only set once!");
            d.this.f14b = aVar;
            return "FutureChain[" + d.this + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public d() {
        this.f13a = androidx.concurrent.futures.c.a(new a());
    }

    d(com.google.common.util.concurrent.d<V> dVar) {
        this.f13a = (com.google.common.util.concurrent.d) androidx.core.util.h.h(dVar);
    }

    public static <V> d<V> a(com.google.common.util.concurrent.d<V> dVar) {
        return dVar instanceof d ? (d) dVar : new d<>(dVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(V v8) {
        c.a<V> aVar = this.f14b;
        if (aVar != null) {
            return aVar.c(v8);
        }
        return false;
    }

    @Override // com.google.common.util.concurrent.d
    public void c(Runnable runnable, Executor executor) {
        this.f13a.c(runnable, executor);
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z4) {
        return this.f13a.cancel(z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d(Throwable th) {
        c.a<V> aVar = this.f14b;
        if (aVar != null) {
            return aVar.f(th);
        }
        return false;
    }

    public final <T> d<T> e(n.a<? super V, T> aVar, Executor executor) {
        return (d) f.o(this, aVar, executor);
    }

    public final <T> d<T> f(a0.a<? super V, T> aVar, Executor executor) {
        return (d) f.p(this, aVar, executor);
    }

    @Override // java.util.concurrent.Future
    public V get() {
        return this.f13a.get();
    }

    @Override // java.util.concurrent.Future
    public V get(long j8, TimeUnit timeUnit) {
        return this.f13a.get(j8, timeUnit);
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.f13a.isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.f13a.isDone();
    }
}
