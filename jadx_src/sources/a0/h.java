package a0;

import androidx.concurrent.futures.c;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h<V> implements com.google.common.util.concurrent.d<List<V>> {

    /* renamed from: a  reason: collision with root package name */
    List<? extends com.google.common.util.concurrent.d<? extends V>> f27a;

    /* renamed from: b  reason: collision with root package name */
    List<V> f28b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f29c;

    /* renamed from: d  reason: collision with root package name */
    private final AtomicInteger f30d;

    /* renamed from: e  reason: collision with root package name */
    private final com.google.common.util.concurrent.d<List<V>> f31e = androidx.concurrent.futures.c.a(new a());

    /* renamed from: f  reason: collision with root package name */
    c.a<List<V>> f32f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements c.InterfaceC0024c<List<V>> {
        a() {
        }

        @Override // androidx.concurrent.futures.c.InterfaceC0024c
        public Object a(c.a<List<V>> aVar) {
            androidx.core.util.h.k(h.this.f32f == null, "The result can only set once!");
            h.this.f32f = aVar;
            return "ListFuture[" + this + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            h hVar = h.this;
            hVar.f28b = null;
            hVar.f27a = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f35a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ com.google.common.util.concurrent.d f36b;

        c(int i8, com.google.common.util.concurrent.d dVar) {
            this.f35a = i8;
            this.f36b = dVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            h.this.f(this.f35a, this.f36b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(List<? extends com.google.common.util.concurrent.d<? extends V>> list, boolean z4, Executor executor) {
        this.f27a = (List) androidx.core.util.h.h(list);
        this.f28b = new ArrayList(list.size());
        this.f29c = z4;
        this.f30d = new AtomicInteger(list.size());
        e(executor);
    }

    private void a() {
        List<? extends com.google.common.util.concurrent.d<? extends V>> list = this.f27a;
        if (list == null || isDone()) {
            return;
        }
        for (com.google.common.util.concurrent.d<? extends V> dVar : list) {
            while (!dVar.isDone()) {
                try {
                    dVar.get();
                } catch (Error e8) {
                    throw e8;
                } catch (InterruptedException e9) {
                    throw e9;
                } catch (Throwable unused) {
                    if (this.f29c) {
                        return;
                    }
                }
            }
        }
    }

    private void e(Executor executor) {
        c(new b(), z.a.a());
        if (this.f27a.isEmpty()) {
            this.f32f.c(new ArrayList(this.f28b));
            return;
        }
        for (int i8 = 0; i8 < this.f27a.size(); i8++) {
            this.f28b.add(null);
        }
        List<? extends com.google.common.util.concurrent.d<? extends V>> list = this.f27a;
        for (int i9 = 0; i9 < list.size(); i9++) {
            com.google.common.util.concurrent.d<? extends V> dVar = list.get(i9);
            dVar.c(new c(i9, dVar), executor);
        }
    }

    @Override // java.util.concurrent.Future
    /* renamed from: b */
    public List<V> get() {
        a();
        return this.f31e.get();
    }

    @Override // com.google.common.util.concurrent.d
    public void c(Runnable runnable, Executor executor) {
        this.f31e.c(runnable, executor);
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z4) {
        List<? extends com.google.common.util.concurrent.d<? extends V>> list = this.f27a;
        if (list != null) {
            for (com.google.common.util.concurrent.d<? extends V> dVar : list) {
                dVar.cancel(z4);
            }
        }
        return this.f31e.cancel(z4);
    }

    @Override // java.util.concurrent.Future
    /* renamed from: d */
    public List<V> get(long j8, TimeUnit timeUnit) {
        return this.f31e.get(j8, timeUnit);
    }

    /* JADX WARN: Multi-variable type inference failed */
    void f(int i8, Future<? extends V> future) {
        c.a<List<V>> aVar;
        ArrayList arrayList;
        int decrementAndGet;
        List<V> list = this.f28b;
        if (isDone() || list == 0) {
            androidx.core.util.h.k(this.f29c, "Future was done before all dependencies completed");
            return;
        }
        try {
            try {
                try {
                    try {
                        androidx.core.util.h.k(future.isDone(), "Tried to set value from future which is not done");
                        list.set(i8, f.e(future));
                        decrementAndGet = this.f30d.decrementAndGet();
                        androidx.core.util.h.k(decrementAndGet >= 0, "Less than 0 remaining futures");
                    } catch (ExecutionException e8) {
                        if (this.f29c) {
                            this.f32f.f(e8.getCause());
                        }
                        int decrementAndGet2 = this.f30d.decrementAndGet();
                        androidx.core.util.h.k(decrementAndGet2 >= 0, "Less than 0 remaining futures");
                        if (decrementAndGet2 != 0) {
                            return;
                        }
                        List<V> list2 = this.f28b;
                        if (list2 != null) {
                            aVar = this.f32f;
                            arrayList = new ArrayList(list2);
                        }
                    }
                } catch (RuntimeException e9) {
                    if (this.f29c) {
                        this.f32f.f(e9);
                    }
                    int decrementAndGet3 = this.f30d.decrementAndGet();
                    androidx.core.util.h.k(decrementAndGet3 >= 0, "Less than 0 remaining futures");
                    if (decrementAndGet3 != 0) {
                        return;
                    }
                    List<V> list3 = this.f28b;
                    if (list3 != null) {
                        aVar = this.f32f;
                        arrayList = new ArrayList(list3);
                    }
                }
            } catch (Error e10) {
                this.f32f.f(e10);
                int decrementAndGet4 = this.f30d.decrementAndGet();
                androidx.core.util.h.k(decrementAndGet4 >= 0, "Less than 0 remaining futures");
                if (decrementAndGet4 != 0) {
                    return;
                }
                List<V> list4 = this.f28b;
                if (list4 != null) {
                    aVar = this.f32f;
                    arrayList = new ArrayList(list4);
                }
            } catch (CancellationException unused) {
                if (this.f29c) {
                    cancel(false);
                }
                int decrementAndGet5 = this.f30d.decrementAndGet();
                androidx.core.util.h.k(decrementAndGet5 >= 0, "Less than 0 remaining futures");
                if (decrementAndGet5 != 0) {
                    return;
                }
                List<V> list5 = this.f28b;
                if (list5 != null) {
                    aVar = this.f32f;
                    arrayList = new ArrayList(list5);
                }
            }
            if (decrementAndGet == 0) {
                List<V> list6 = this.f28b;
                if (list6 != null) {
                    aVar = this.f32f;
                    arrayList = new ArrayList(list6);
                    aVar.c(arrayList);
                    return;
                }
                androidx.core.util.h.j(isDone());
            }
        } catch (Throwable th) {
            int decrementAndGet6 = this.f30d.decrementAndGet();
            androidx.core.util.h.k(decrementAndGet6 >= 0, "Less than 0 remaining futures");
            if (decrementAndGet6 == 0) {
                List<V> list7 = this.f28b;
                if (list7 != null) {
                    this.f32f.c(new ArrayList(list7));
                } else {
                    androidx.core.util.h.j(isDone());
                }
            }
            throw th;
        }
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.f31e.isCancelled();
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.f31e.isDone();
    }
}
