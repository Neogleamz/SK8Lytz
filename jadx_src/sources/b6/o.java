package b6;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import b6.k;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o<T> {

    /* renamed from: a  reason: collision with root package name */
    private final d f8082a;

    /* renamed from: b  reason: collision with root package name */
    private final l f8083b;

    /* renamed from: c  reason: collision with root package name */
    private final b<T> f8084c;

    /* renamed from: d  reason: collision with root package name */
    private final CopyOnWriteArraySet<c<T>> f8085d;

    /* renamed from: e  reason: collision with root package name */
    private final ArrayDeque<Runnable> f8086e;

    /* renamed from: f  reason: collision with root package name */
    private final ArrayDeque<Runnable> f8087f;

    /* renamed from: g  reason: collision with root package name */
    private final Object f8088g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f8089h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f8090i;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a<T> {
        void invoke(T t8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b<T> {
        void a(T t8, k kVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c<T> {

        /* renamed from: a  reason: collision with root package name */
        public final T f8091a;

        /* renamed from: b  reason: collision with root package name */
        private k.b f8092b = new k.b();

        /* renamed from: c  reason: collision with root package name */
        private boolean f8093c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f8094d;

        public c(T t8) {
            this.f8091a = t8;
        }

        public void a(int i8, a<T> aVar) {
            if (this.f8094d) {
                return;
            }
            if (i8 != -1) {
                this.f8092b.a(i8);
            }
            this.f8093c = true;
            aVar.invoke(this.f8091a);
        }

        public void b(b<T> bVar) {
            if (this.f8094d || !this.f8093c) {
                return;
            }
            k e8 = this.f8092b.e();
            this.f8092b = new k.b();
            this.f8093c = false;
            bVar.a(this.f8091a, e8);
        }

        public void c(b<T> bVar) {
            this.f8094d = true;
            if (this.f8093c) {
                this.f8093c = false;
                bVar.a(this.f8091a, this.f8092b.e());
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || c.class != obj.getClass()) {
                return false;
            }
            return this.f8091a.equals(((c) obj).f8091a);
        }

        public int hashCode() {
            return this.f8091a.hashCode();
        }
    }

    public o(Looper looper, d dVar, b<T> bVar) {
        this(new CopyOnWriteArraySet(), looper, dVar, bVar);
    }

    private o(CopyOnWriteArraySet<c<T>> copyOnWriteArraySet, Looper looper, d dVar, b<T> bVar) {
        this.f8082a = dVar;
        this.f8085d = copyOnWriteArraySet;
        this.f8084c = bVar;
        this.f8088g = new Object();
        this.f8086e = new ArrayDeque<>();
        this.f8087f = new ArrayDeque<>();
        this.f8083b = dVar.d(looper, new Handler.Callback() { // from class: b6.m
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(Message message) {
                boolean g8;
                g8 = o.this.g(message);
                return g8;
            }
        });
        this.f8090i = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean g(Message message) {
        Iterator<c<T>> it = this.f8085d.iterator();
        while (it.hasNext()) {
            it.next().b(this.f8084c);
            if (this.f8083b.d(0)) {
                return true;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void h(CopyOnWriteArraySet copyOnWriteArraySet, int i8, a aVar) {
        Iterator it = copyOnWriteArraySet.iterator();
        while (it.hasNext()) {
            ((c) it.next()).a(i8, aVar);
        }
    }

    private void l() {
        if (this.f8090i) {
            b6.a.f(Thread.currentThread() == this.f8083b.l().getThread());
        }
    }

    public void c(T t8) {
        b6.a.e(t8);
        synchronized (this.f8088g) {
            if (this.f8089h) {
                return;
            }
            this.f8085d.add(new c<>(t8));
        }
    }

    public o<T> d(Looper looper, d dVar, b<T> bVar) {
        return new o<>(this.f8085d, looper, dVar, bVar);
    }

    public o<T> e(Looper looper, b<T> bVar) {
        return d(looper, this.f8082a, bVar);
    }

    public void f() {
        l();
        if (this.f8087f.isEmpty()) {
            return;
        }
        if (!this.f8083b.d(0)) {
            l lVar = this.f8083b;
            lVar.h(lVar.c(0));
        }
        boolean z4 = !this.f8086e.isEmpty();
        this.f8086e.addAll(this.f8087f);
        this.f8087f.clear();
        if (z4) {
            return;
        }
        while (!this.f8086e.isEmpty()) {
            this.f8086e.peekFirst().run();
            this.f8086e.removeFirst();
        }
    }

    public void i(final int i8, final a<T> aVar) {
        l();
        final CopyOnWriteArraySet copyOnWriteArraySet = new CopyOnWriteArraySet(this.f8085d);
        this.f8087f.add(new Runnable() { // from class: b6.n
            @Override // java.lang.Runnable
            public final void run() {
                o.h(copyOnWriteArraySet, i8, aVar);
            }
        });
    }

    public void j() {
        l();
        synchronized (this.f8088g) {
            this.f8089h = true;
        }
        Iterator<c<T>> it = this.f8085d.iterator();
        while (it.hasNext()) {
            it.next().c(this.f8084c);
        }
        this.f8085d.clear();
    }

    public void k(int i8, a<T> aVar) {
        i(i8, aVar);
        f();
    }
}
