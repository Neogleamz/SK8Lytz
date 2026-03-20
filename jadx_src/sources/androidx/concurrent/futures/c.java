package androidx.concurrent.futures;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a<T> {

        /* renamed from: a  reason: collision with root package name */
        Object f3139a;

        /* renamed from: b  reason: collision with root package name */
        d<T> f3140b;

        /* renamed from: c  reason: collision with root package name */
        private androidx.concurrent.futures.d<Void> f3141c = androidx.concurrent.futures.d.C();

        /* renamed from: d  reason: collision with root package name */
        private boolean f3142d;

        a() {
        }

        private void e() {
            this.f3139a = null;
            this.f3140b = null;
            this.f3141c = null;
        }

        public void a(Runnable runnable, Executor executor) {
            androidx.concurrent.futures.d<Void> dVar = this.f3141c;
            if (dVar != null) {
                dVar.c(runnable, executor);
            }
        }

        void b() {
            this.f3139a = null;
            this.f3140b = null;
            this.f3141c.y(null);
        }

        public boolean c(T t8) {
            boolean z4 = true;
            this.f3142d = true;
            d<T> dVar = this.f3140b;
            z4 = (dVar == null || !dVar.b(t8)) ? false : false;
            if (z4) {
                e();
            }
            return z4;
        }

        public boolean d() {
            boolean z4 = true;
            this.f3142d = true;
            d<T> dVar = this.f3140b;
            z4 = (dVar == null || !dVar.a(true)) ? false : false;
            if (z4) {
                e();
            }
            return z4;
        }

        public boolean f(Throwable th) {
            boolean z4 = true;
            this.f3142d = true;
            d<T> dVar = this.f3140b;
            z4 = (dVar == null || !dVar.d(th)) ? false : false;
            if (z4) {
                e();
            }
            return z4;
        }

        protected void finalize() {
            androidx.concurrent.futures.d<Void> dVar;
            d<T> dVar2 = this.f3140b;
            if (dVar2 != null && !dVar2.isDone()) {
                dVar2.d(new b("The completer object was garbage collected - this future would otherwise never complete. The tag was: " + this.f3139a));
            }
            if (this.f3142d || (dVar = this.f3141c) == null) {
                return;
            }
            dVar.y(null);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b extends Throwable {
        b(String str) {
            super(str);
        }

        @Override // java.lang.Throwable
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    /* renamed from: androidx.concurrent.futures.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0024c<T> {
        Object a(a<T> aVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d<T> implements com.google.common.util.concurrent.d<T> {

        /* renamed from: a  reason: collision with root package name */
        final WeakReference<a<T>> f3143a;

        /* renamed from: b  reason: collision with root package name */
        private final androidx.concurrent.futures.a<T> f3144b = new a();

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends androidx.concurrent.futures.a<T> {
            a() {
            }

            @Override // androidx.concurrent.futures.a
            protected String v() {
                a<T> aVar = d.this.f3143a.get();
                if (aVar == null) {
                    return "Completer object has been garbage collected, future will fail soon";
                }
                return "tag=[" + aVar.f3139a + "]";
            }
        }

        d(a<T> aVar) {
            this.f3143a = new WeakReference<>(aVar);
        }

        boolean a(boolean z4) {
            return this.f3144b.cancel(z4);
        }

        boolean b(T t8) {
            return this.f3144b.y(t8);
        }

        @Override // com.google.common.util.concurrent.d
        public void c(Runnable runnable, Executor executor) {
            this.f3144b.c(runnable, executor);
        }

        @Override // java.util.concurrent.Future
        public boolean cancel(boolean z4) {
            a<T> aVar = this.f3143a.get();
            boolean cancel = this.f3144b.cancel(z4);
            if (cancel && aVar != null) {
                aVar.b();
            }
            return cancel;
        }

        boolean d(Throwable th) {
            return this.f3144b.z(th);
        }

        @Override // java.util.concurrent.Future
        public T get() {
            return this.f3144b.get();
        }

        @Override // java.util.concurrent.Future
        public T get(long j8, TimeUnit timeUnit) {
            return this.f3144b.get(j8, timeUnit);
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return this.f3144b.isCancelled();
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return this.f3144b.isDone();
        }

        public String toString() {
            return this.f3144b.toString();
        }
    }

    public static <T> com.google.common.util.concurrent.d<T> a(InterfaceC0024c<T> interfaceC0024c) {
        a<T> aVar = new a<>();
        d<T> dVar = new d<>(aVar);
        aVar.f3140b = dVar;
        aVar.f3139a = interfaceC0024c.getClass();
        try {
            Object a9 = interfaceC0024c.a(aVar);
            if (a9 != null) {
                aVar.f3139a = a9;
            }
        } catch (Exception e8) {
            dVar.d(e8);
        }
        return dVar;
    }
}
