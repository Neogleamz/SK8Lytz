package androidx.camera.core;

import android.media.ImageReader;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.c2;
import androidx.concurrent.futures.c;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import y.g0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c2 implements y.g0 {

    /* renamed from: g  reason: collision with root package name */
    final y.g0 f2266g;

    /* renamed from: h  reason: collision with root package name */
    final y.g0 f2267h;

    /* renamed from: i  reason: collision with root package name */
    g0.a f2268i;

    /* renamed from: j  reason: collision with root package name */
    Executor f2269j;

    /* renamed from: k  reason: collision with root package name */
    c.a<Void> f2270k;

    /* renamed from: l  reason: collision with root package name */
    private com.google.common.util.concurrent.d<Void> f2271l;

    /* renamed from: m  reason: collision with root package name */
    final Executor f2272m;

    /* renamed from: n  reason: collision with root package name */
    final y.v f2273n;

    /* renamed from: o  reason: collision with root package name */
    private final com.google.common.util.concurrent.d<Void> f2274o;

    /* renamed from: t  reason: collision with root package name */
    f f2278t;

    /* renamed from: u  reason: collision with root package name */
    Executor f2279u;

    /* renamed from: a  reason: collision with root package name */
    final Object f2260a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private g0.a f2261b = new a();

    /* renamed from: c  reason: collision with root package name */
    private g0.a f2262c = new b();

    /* renamed from: d  reason: collision with root package name */
    private a0.c<List<l1>> f2263d = new c();

    /* renamed from: e  reason: collision with root package name */
    boolean f2264e = false;

    /* renamed from: f  reason: collision with root package name */
    boolean f2265f = false;

    /* renamed from: p  reason: collision with root package name */
    private String f2275p = new String();
    o2 q = new o2(Collections.emptyList(), this.f2275p);

    /* renamed from: r  reason: collision with root package name */
    private final List<Integer> f2276r = new ArrayList();

    /* renamed from: s  reason: collision with root package name */
    private com.google.common.util.concurrent.d<List<l1>> f2277s = a0.f.h(new ArrayList());

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements g0.a {
        a() {
        }

        @Override // y.g0.a
        public void a(y.g0 g0Var) {
            c2.this.n(g0Var);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements g0.a {
        b() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void c(g0.a aVar) {
            aVar.a(c2.this);
        }

        @Override // y.g0.a
        public void a(y.g0 g0Var) {
            final g0.a aVar;
            Executor executor;
            synchronized (c2.this.f2260a) {
                c2 c2Var = c2.this;
                aVar = c2Var.f2268i;
                executor = c2Var.f2269j;
                c2Var.q.e();
                c2.this.t();
            }
            if (aVar != null) {
                if (executor != null) {
                    executor.execute(new Runnable() { // from class: androidx.camera.core.d2
                        @Override // java.lang.Runnable
                        public final void run() {
                            c2.b.this.c(aVar);
                        }
                    });
                } else {
                    aVar.a(c2.this);
                }
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements a0.c<List<l1>> {
        c() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void b(f fVar, Exception exc) {
            fVar.a(exc.getMessage(), exc.getCause());
        }

        @Override // a0.c
        /* renamed from: d */
        public void c(List<l1> list) {
            c2 c2Var;
            synchronized (c2.this.f2260a) {
                c2 c2Var2 = c2.this;
                if (c2Var2.f2264e) {
                    return;
                }
                c2Var2.f2265f = true;
                o2 o2Var = c2Var2.q;
                final f fVar = c2Var2.f2278t;
                Executor executor = c2Var2.f2279u;
                try {
                    c2Var2.f2273n.c(o2Var);
                } catch (Exception e8) {
                    synchronized (c2.this.f2260a) {
                        c2.this.q.e();
                        if (fVar != null && executor != null) {
                            executor.execute(new Runnable() { // from class: androidx.camera.core.e2
                                @Override // java.lang.Runnable
                                public final void run() {
                                    c2.c.b(c2.f.this, e8);
                                }
                            });
                        }
                    }
                }
                synchronized (c2.this.f2260a) {
                    c2Var = c2.this;
                    c2Var.f2265f = false;
                }
                c2Var.j();
            }
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends y.h {
        d() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        protected final y.g0 f2284a;

        /* renamed from: b  reason: collision with root package name */
        protected final y.u f2285b;

        /* renamed from: c  reason: collision with root package name */
        protected final y.v f2286c;

        /* renamed from: d  reason: collision with root package name */
        protected int f2287d;

        /* renamed from: e  reason: collision with root package name */
        protected Executor f2288e;

        /* JADX INFO: Access modifiers changed from: package-private */
        public e(int i8, int i9, int i10, int i11, y.u uVar, y.v vVar) {
            this(new s1(i8, i9, i10, i11), uVar, vVar);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public e(y.g0 g0Var, y.u uVar, y.v vVar) {
            this.f2288e = Executors.newSingleThreadExecutor();
            this.f2284a = g0Var;
            this.f2285b = uVar;
            this.f2286c = vVar;
            this.f2287d = g0Var.c();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public c2 a() {
            return new c2(this);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public e b(int i8) {
            this.f2287d = i8;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public e c(Executor executor) {
            this.f2288e = executor;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface f {
        void a(String str, Throwable th);
    }

    c2(e eVar) {
        if (eVar.f2284a.e() < eVar.f2285b.a().size()) {
            throw new IllegalArgumentException("MetadataImageReader is smaller than CaptureBundle.");
        }
        y.g0 g0Var = eVar.f2284a;
        this.f2266g = g0Var;
        int width = g0Var.getWidth();
        int height = g0Var.getHeight();
        int i8 = eVar.f2287d;
        if (i8 == 256) {
            width = ((int) (width * height * 1.5f)) + 64000;
            height = 1;
        }
        androidx.camera.core.d dVar = new androidx.camera.core.d(ImageReader.newInstance(width, height, i8, g0Var.e()));
        this.f2267h = dVar;
        this.f2272m = eVar.f2288e;
        y.v vVar = eVar.f2286c;
        this.f2273n = vVar;
        vVar.a(dVar.getSurface(), eVar.f2287d);
        vVar.d(new Size(g0Var.getWidth(), g0Var.getHeight()));
        this.f2274o = vVar.b();
        r(eVar.f2285b);
    }

    private void i() {
        synchronized (this.f2260a) {
            if (!this.f2277s.isDone()) {
                this.f2277s.cancel(true);
            }
            this.q.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o(c.a aVar) {
        i();
        if (aVar != null) {
            aVar.c(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Void p(Void r02) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object q(c.a aVar) {
        synchronized (this.f2260a) {
            this.f2270k = aVar;
        }
        return "ProcessingImageReader-close";
    }

    @Override // y.g0
    public void a(g0.a aVar, Executor executor) {
        synchronized (this.f2260a) {
            this.f2268i = (g0.a) androidx.core.util.h.h(aVar);
            this.f2269j = (Executor) androidx.core.util.h.h(executor);
            this.f2266g.a(this.f2261b, executor);
            this.f2267h.a(this.f2262c, executor);
        }
    }

    @Override // y.g0
    public l1 acquireLatestImage() {
        l1 acquireLatestImage;
        synchronized (this.f2260a) {
            acquireLatestImage = this.f2267h.acquireLatestImage();
        }
        return acquireLatestImage;
    }

    @Override // y.g0
    public int c() {
        int c9;
        synchronized (this.f2260a) {
            c9 = this.f2267h.c();
        }
        return c9;
    }

    @Override // y.g0
    public void close() {
        synchronized (this.f2260a) {
            if (this.f2264e) {
                return;
            }
            this.f2266g.d();
            this.f2267h.d();
            this.f2264e = true;
            this.f2273n.close();
            j();
        }
    }

    @Override // y.g0
    public void d() {
        synchronized (this.f2260a) {
            this.f2268i = null;
            this.f2269j = null;
            this.f2266g.d();
            this.f2267h.d();
            if (!this.f2265f) {
                this.q.d();
            }
        }
    }

    @Override // y.g0
    public int e() {
        int e8;
        synchronized (this.f2260a) {
            e8 = this.f2266g.e();
        }
        return e8;
    }

    @Override // y.g0
    public l1 f() {
        l1 f5;
        synchronized (this.f2260a) {
            f5 = this.f2267h.f();
        }
        return f5;
    }

    @Override // y.g0
    public int getHeight() {
        int height;
        synchronized (this.f2260a) {
            height = this.f2266g.getHeight();
        }
        return height;
    }

    @Override // y.g0
    public Surface getSurface() {
        Surface surface;
        synchronized (this.f2260a) {
            surface = this.f2266g.getSurface();
        }
        return surface;
    }

    @Override // y.g0
    public int getWidth() {
        int width;
        synchronized (this.f2260a) {
            width = this.f2266g.getWidth();
        }
        return width;
    }

    void j() {
        boolean z4;
        boolean z8;
        final c.a<Void> aVar;
        synchronized (this.f2260a) {
            z4 = this.f2264e;
            z8 = this.f2265f;
            aVar = this.f2270k;
            if (z4 && !z8) {
                this.f2266g.close();
                this.q.d();
                this.f2267h.close();
            }
        }
        if (!z4 || z8) {
            return;
        }
        this.f2274o.c(new Runnable() { // from class: androidx.camera.core.a2
            @Override // java.lang.Runnable
            public final void run() {
                c2.this.o(aVar);
            }
        }, z.a.a());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public y.h k() {
        synchronized (this.f2260a) {
            y.g0 g0Var = this.f2266g;
            if (g0Var instanceof s1) {
                return ((s1) g0Var).l();
            }
            return new d();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public com.google.common.util.concurrent.d<Void> l() {
        com.google.common.util.concurrent.d<Void> j8;
        synchronized (this.f2260a) {
            if (!this.f2264e || this.f2265f) {
                if (this.f2271l == null) {
                    this.f2271l = androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.core.z1
                        @Override // androidx.concurrent.futures.c.InterfaceC0024c
                        public final Object a(c.a aVar) {
                            Object q;
                            q = c2.this.q(aVar);
                            return q;
                        }
                    });
                }
                j8 = a0.f.j(this.f2271l);
            } else {
                j8 = a0.f.o(this.f2274o, new n.a() { // from class: androidx.camera.core.b2
                    @Override // n.a
                    public final Object apply(Object obj) {
                        Void p8;
                        p8 = c2.p((Void) obj);
                        return p8;
                    }
                }, z.a.a());
            }
        }
        return j8;
    }

    public String m() {
        return this.f2275p;
    }

    void n(y.g0 g0Var) {
        synchronized (this.f2260a) {
            if (this.f2264e) {
                return;
            }
            try {
                l1 f5 = g0Var.f();
                if (f5 != null) {
                    Integer num = (Integer) f5.e1().a().c(this.f2275p);
                    if (this.f2276r.contains(num)) {
                        this.q.c(f5);
                    } else {
                        p1.k("ProcessingImageReader", "ImageProxyBundle does not contain this id: " + num);
                        f5.close();
                    }
                }
            } catch (IllegalStateException e8) {
                p1.d("ProcessingImageReader", "Failed to acquire latest image.", e8);
            }
        }
    }

    public void r(y.u uVar) {
        synchronized (this.f2260a) {
            if (this.f2264e) {
                return;
            }
            i();
            if (uVar.a() != null) {
                if (this.f2266g.e() < uVar.a().size()) {
                    throw new IllegalArgumentException("CaptureBundle is larger than InputImageReader.");
                }
                this.f2276r.clear();
                for (androidx.camera.core.impl.g gVar : uVar.a()) {
                    if (gVar != null) {
                        this.f2276r.add(Integer.valueOf(gVar.e()));
                    }
                }
            }
            String num = Integer.toString(uVar.hashCode());
            this.f2275p = num;
            this.q = new o2(this.f2276r, num);
            t();
        }
    }

    public void s(Executor executor, f fVar) {
        synchronized (this.f2260a) {
            this.f2279u = executor;
            this.f2278t = fVar;
        }
    }

    void t() {
        ArrayList arrayList = new ArrayList();
        for (Integer num : this.f2276r) {
            arrayList.add(this.q.a(num.intValue()));
        }
        this.f2277s = a0.f.c(arrayList);
        a0.f.b(a0.f.c(arrayList), this.f2263d, this.f2272m);
    }
}
