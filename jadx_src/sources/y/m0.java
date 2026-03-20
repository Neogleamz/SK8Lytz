package y;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import y.p0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m0<T> implements p0<T> {

    /* renamed from: a  reason: collision with root package name */
    final androidx.lifecycle.p<b<T>> f24302a = new androidx.lifecycle.p<>();

    /* renamed from: b  reason: collision with root package name */
    private final Map<p0.a<? super T>, a<T>> f24303b = new HashMap();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a<T> implements androidx.lifecycle.q<b<T>> {

        /* renamed from: a  reason: collision with root package name */
        final AtomicBoolean f24304a = new AtomicBoolean(true);

        /* renamed from: b  reason: collision with root package name */
        final p0.a<? super T> f24305b;

        /* renamed from: c  reason: collision with root package name */
        final Executor f24306c;

        a(Executor executor, p0.a<? super T> aVar) {
            this.f24306c = executor;
            this.f24305b = aVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void d(b bVar) {
            if (this.f24304a.get()) {
                if (bVar.a()) {
                    this.f24305b.a((Object) bVar.d());
                    return;
                }
                androidx.core.util.h.h(bVar.c());
                this.f24305b.onError(bVar.c());
            }
        }

        void c() {
            this.f24304a.set(false);
        }

        @Override // androidx.lifecycle.q
        /* renamed from: e */
        public void b(b<T> bVar) {
            this.f24306c.execute(new l0(this, bVar));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b<T> {

        /* renamed from: a  reason: collision with root package name */
        private final T f24307a;

        /* renamed from: b  reason: collision with root package name */
        private final Throwable f24308b;

        private b(T t8, Throwable th) {
            this.f24307a = t8;
            this.f24308b = th;
        }

        static <T> b<T> b(T t8) {
            return new b<>(t8, null);
        }

        public boolean a() {
            return this.f24308b == null;
        }

        public Throwable c() {
            return this.f24308b;
        }

        public T d() {
            if (a()) {
                return this.f24307a;
            }
            throw new IllegalStateException("Result contains an error. Does not contain a value.");
        }

        public String toString() {
            StringBuilder sb;
            Object obj;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("[Result: <");
            if (a()) {
                sb = new StringBuilder();
                sb.append("Value: ");
                obj = this.f24307a;
            } else {
                sb = new StringBuilder();
                sb.append("Error: ");
                obj = this.f24308b;
            }
            sb.append(obj);
            sb2.append(sb.toString());
            sb2.append(">]");
            return sb2.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void e(a aVar, a aVar2) {
        if (aVar != null) {
            this.f24302a.m(aVar);
        }
        this.f24302a.i(aVar2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void f(a aVar) {
        this.f24302a.m(aVar);
    }

    @Override // y.p0
    public void a(Executor executor, p0.a<? super T> aVar) {
        synchronized (this.f24303b) {
            a<T> aVar2 = this.f24303b.get(aVar);
            if (aVar2 != null) {
                aVar2.c();
            }
            a<T> aVar3 = new a<>(executor, aVar);
            this.f24303b.put(aVar, aVar3);
            z.a.d().execute(new k0(this, aVar2, aVar3));
        }
    }

    @Override // y.p0
    public void b(p0.a<? super T> aVar) {
        synchronized (this.f24303b) {
            a<T> remove = this.f24303b.remove(aVar);
            if (remove != null) {
                remove.c();
                z.a.d().execute(new j0(this, remove));
            }
        }
    }

    public void g(T t8) {
        this.f24302a.l(b.b(t8));
    }
}
