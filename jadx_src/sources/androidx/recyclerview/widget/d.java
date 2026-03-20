package androidx.recyclerview.widget;

import android.os.Handler;
import android.os.Looper;
import androidx.recyclerview.widget.h;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d<T> {

    /* renamed from: h  reason: collision with root package name */
    private static final Executor f6804h = new c();

    /* renamed from: a  reason: collision with root package name */
    private final s f6805a;

    /* renamed from: b  reason: collision with root package name */
    final androidx.recyclerview.widget.c<T> f6806b;

    /* renamed from: c  reason: collision with root package name */
    Executor f6807c;

    /* renamed from: e  reason: collision with root package name */
    private List<T> f6809e;

    /* renamed from: g  reason: collision with root package name */
    int f6811g;

    /* renamed from: d  reason: collision with root package name */
    private final List<b<T>> f6808d = new CopyOnWriteArrayList();

    /* renamed from: f  reason: collision with root package name */
    private List<T> f6810f = Collections.emptyList();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ List f6812a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ List f6813b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ int f6814c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ Runnable f6815d;

        /* renamed from: androidx.recyclerview.widget.d$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class C0073a extends h.b {
            C0073a() {
            }

            @Override // androidx.recyclerview.widget.h.b
            public boolean a(int i8, int i9) {
                Object obj = a.this.f6812a.get(i8);
                Object obj2 = a.this.f6813b.get(i9);
                if (obj == null || obj2 == null) {
                    if (obj == null && obj2 == null) {
                        return true;
                    }
                    throw new AssertionError();
                }
                return d.this.f6806b.b().a(obj, obj2);
            }

            @Override // androidx.recyclerview.widget.h.b
            public boolean b(int i8, int i9) {
                Object obj = a.this.f6812a.get(i8);
                Object obj2 = a.this.f6813b.get(i9);
                return (obj == null || obj2 == null) ? obj == null && obj2 == null : d.this.f6806b.b().b(obj, obj2);
            }

            @Override // androidx.recyclerview.widget.h.b
            public Object c(int i8, int i9) {
                Object obj = a.this.f6812a.get(i8);
                Object obj2 = a.this.f6813b.get(i9);
                if (obj == null || obj2 == null) {
                    throw new AssertionError();
                }
                return d.this.f6806b.b().c(obj, obj2);
            }

            @Override // androidx.recyclerview.widget.h.b
            public int d() {
                return a.this.f6813b.size();
            }

            @Override // androidx.recyclerview.widget.h.b
            public int e() {
                return a.this.f6812a.size();
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class b implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ h.c f6818a;

            b(h.c cVar) {
                this.f6818a = cVar;
            }

            @Override // java.lang.Runnable
            public void run() {
                a aVar = a.this;
                d dVar = d.this;
                if (dVar.f6811g == aVar.f6814c) {
                    dVar.c(aVar.f6813b, this.f6818a, aVar.f6815d);
                }
            }
        }

        a(List list, List list2, int i8, Runnable runnable) {
            this.f6812a = list;
            this.f6813b = list2;
            this.f6814c = i8;
            this.f6815d = runnable;
        }

        @Override // java.lang.Runnable
        public void run() {
            d.this.f6807c.execute(new b(h.a(new C0073a())));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b<T> {
        void a(List<T> list, List<T> list2);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c implements Executor {

        /* renamed from: a  reason: collision with root package name */
        final Handler f6820a = new Handler(Looper.getMainLooper());

        c() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            this.f6820a.post(runnable);
        }
    }

    public d(s sVar, androidx.recyclerview.widget.c<T> cVar) {
        this.f6805a = sVar;
        this.f6806b = cVar;
        this.f6807c = cVar.c() != null ? cVar.c() : f6804h;
    }

    private void d(List<T> list, Runnable runnable) {
        for (b<T> bVar : this.f6808d) {
            bVar.a(list, this.f6810f);
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    public void a(b<T> bVar) {
        this.f6808d.add(bVar);
    }

    public List<T> b() {
        return this.f6810f;
    }

    void c(List<T> list, h.c cVar, Runnable runnable) {
        List<T> list2 = this.f6810f;
        this.f6809e = list;
        this.f6810f = Collections.unmodifiableList(list);
        cVar.d(this.f6805a);
        d(list2, runnable);
    }

    public void e(List<T> list) {
        f(list, null);
    }

    public void f(List<T> list, Runnable runnable) {
        int i8 = this.f6811g + 1;
        this.f6811g = i8;
        List<T> list2 = this.f6809e;
        if (list == list2) {
            if (runnable != null) {
                runnable.run();
                return;
            }
            return;
        }
        List<T> list3 = this.f6810f;
        if (list == null) {
            int size = list2.size();
            this.f6809e = null;
            this.f6810f = Collections.emptyList();
            this.f6805a.c(0, size);
            d(list3, runnable);
        } else if (list2 != null) {
            this.f6806b.a().execute(new a(list2, list, i8, runnable));
        } else {
            this.f6809e = list;
            this.f6810f = Collections.unmodifiableList(list);
            this.f6805a.b(0, list.size());
            d(list3, runnable);
        }
    }
}
