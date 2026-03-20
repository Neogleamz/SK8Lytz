package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class LiveData<T> {

    /* renamed from: k  reason: collision with root package name */
    static final Object f5817k = new Object();

    /* renamed from: a  reason: collision with root package name */
    final Object f5818a;

    /* renamed from: b  reason: collision with root package name */
    private m.b<q<? super T>, LiveData<T>.c> f5819b;

    /* renamed from: c  reason: collision with root package name */
    int f5820c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f5821d;

    /* renamed from: e  reason: collision with root package name */
    private volatile Object f5822e;

    /* renamed from: f  reason: collision with root package name */
    volatile Object f5823f;

    /* renamed from: g  reason: collision with root package name */
    private int f5824g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f5825h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f5826i;

    /* renamed from: j  reason: collision with root package name */
    private final Runnable f5827j;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class LifecycleBoundObserver extends LiveData<T>.c implements h {

        /* renamed from: e  reason: collision with root package name */
        final j f5828e;

        LifecycleBoundObserver(j jVar, q<? super T> qVar) {
            super(qVar);
            this.f5828e = jVar;
        }

        @Override // androidx.lifecycle.LiveData.c
        void b() {
            this.f5828e.getLifecycle().c(this);
        }

        @Override // androidx.lifecycle.h
        public void c(j jVar, Lifecycle.Event event) {
            Lifecycle.State b9 = this.f5828e.getLifecycle().b();
            if (b9 == Lifecycle.State.DESTROYED) {
                LiveData.this.m(this.f5832a);
                return;
            }
            Lifecycle.State state = null;
            while (state != b9) {
                a(e());
                state = b9;
                b9 = this.f5828e.getLifecycle().b();
            }
        }

        @Override // androidx.lifecycle.LiveData.c
        boolean d(j jVar) {
            return this.f5828e == jVar;
        }

        @Override // androidx.lifecycle.LiveData.c
        boolean e() {
            return this.f5828e.getLifecycle().b().f(Lifecycle.State.STARTED);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Runnable
        public void run() {
            Object obj;
            synchronized (LiveData.this.f5818a) {
                obj = LiveData.this.f5823f;
                LiveData.this.f5823f = LiveData.f5817k;
            }
            LiveData.this.o(obj);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class b extends LiveData<T>.c {
        b(q<? super T> qVar) {
            super(qVar);
        }

        @Override // androidx.lifecycle.LiveData.c
        boolean e() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public abstract class c {

        /* renamed from: a  reason: collision with root package name */
        final q<? super T> f5832a;

        /* renamed from: b  reason: collision with root package name */
        boolean f5833b;

        /* renamed from: c  reason: collision with root package name */
        int f5834c = -1;

        c(q<? super T> qVar) {
            this.f5832a = qVar;
        }

        void a(boolean z4) {
            if (z4 == this.f5833b) {
                return;
            }
            this.f5833b = z4;
            LiveData.this.b(z4 ? 1 : -1);
            if (this.f5833b) {
                LiveData.this.d(this);
            }
        }

        void b() {
        }

        boolean d(j jVar) {
            return false;
        }

        abstract boolean e();
    }

    public LiveData() {
        this.f5818a = new Object();
        this.f5819b = new m.b<>();
        this.f5820c = 0;
        Object obj = f5817k;
        this.f5823f = obj;
        this.f5827j = new a();
        this.f5822e = obj;
        this.f5824g = -1;
    }

    public LiveData(T t8) {
        this.f5818a = new Object();
        this.f5819b = new m.b<>();
        this.f5820c = 0;
        this.f5823f = f5817k;
        this.f5827j = new a();
        this.f5822e = t8;
        this.f5824g = 0;
    }

    static void a(String str) {
        if (l.c.g().b()) {
            return;
        }
        throw new IllegalStateException("Cannot invoke " + str + " on a background thread");
    }

    private void c(LiveData<T>.c cVar) {
        if (cVar.f5833b) {
            if (!cVar.e()) {
                cVar.a(false);
                return;
            }
            int i8 = cVar.f5834c;
            int i9 = this.f5824g;
            if (i8 >= i9) {
                return;
            }
            cVar.f5834c = i9;
            cVar.f5832a.b((Object) this.f5822e);
        }
    }

    void b(int i8) {
        int i9 = this.f5820c;
        this.f5820c = i8 + i9;
        if (this.f5821d) {
            return;
        }
        this.f5821d = true;
        while (true) {
            try {
                int i10 = this.f5820c;
                if (i9 == i10) {
                    return;
                }
                boolean z4 = i9 == 0 && i10 > 0;
                boolean z8 = i9 > 0 && i10 == 0;
                if (z4) {
                    j();
                } else if (z8) {
                    k();
                }
                i9 = i10;
            } finally {
                this.f5821d = false;
            }
        }
    }

    void d(LiveData<T>.c cVar) {
        if (this.f5825h) {
            this.f5826i = true;
            return;
        }
        this.f5825h = true;
        do {
            this.f5826i = false;
            if (cVar == null) {
                m.b<q<? super T>, LiveData<T>.c>.d h8 = this.f5819b.h();
                while (h8.hasNext()) {
                    c((c) h8.next().getValue());
                    if (this.f5826i) {
                        break;
                    }
                }
            } else {
                c(cVar);
                cVar = null;
            }
        } while (this.f5826i);
        this.f5825h = false;
    }

    public T e() {
        T t8 = (T) this.f5822e;
        if (t8 != f5817k) {
            return t8;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int f() {
        return this.f5824g;
    }

    public boolean g() {
        return this.f5820c > 0;
    }

    public void h(j jVar, q<? super T> qVar) {
        a("observe");
        if (jVar.getLifecycle().b() == Lifecycle.State.DESTROYED) {
            return;
        }
        LifecycleBoundObserver lifecycleBoundObserver = new LifecycleBoundObserver(jVar, qVar);
        LiveData<T>.c n8 = this.f5819b.n(qVar, lifecycleBoundObserver);
        if (n8 != null && !n8.d(jVar)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (n8 != null) {
            return;
        }
        jVar.getLifecycle().a(lifecycleBoundObserver);
    }

    public void i(q<? super T> qVar) {
        a("observeForever");
        b bVar = new b(qVar);
        LiveData<T>.c n8 = this.f5819b.n(qVar, bVar);
        if (n8 instanceof LifecycleBoundObserver) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (n8 != null) {
            return;
        }
        bVar.a(true);
    }

    protected void j() {
    }

    protected void k() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void l(T t8) {
        boolean z4;
        synchronized (this.f5818a) {
            z4 = this.f5823f == f5817k;
            this.f5823f = t8;
        }
        if (z4) {
            l.c.g().c(this.f5827j);
        }
    }

    public void m(q<? super T> qVar) {
        a("removeObserver");
        LiveData<T>.c p8 = this.f5819b.p(qVar);
        if (p8 == null) {
            return;
        }
        p8.b();
        p8.a(false);
    }

    public void n(j jVar) {
        a("removeObservers");
        Iterator<Map.Entry<q<? super T>, LiveData<T>.c>> it = this.f5819b.iterator();
        while (it.hasNext()) {
            Map.Entry<q<? super T>, LiveData<T>.c> next = it.next();
            if (next.getValue().d(jVar)) {
                m(next.getKey());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void o(T t8) {
        a("setValue");
        this.f5824g++;
        this.f5822e = t8;
        d(null);
    }
}
