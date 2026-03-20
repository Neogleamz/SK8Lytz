package androidx.camera.core;

import android.media.ImageReader;
import android.util.LongSparseArray;
import android.view.Surface;
import androidx.camera.core.h0;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import y.g0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class s1 implements y.g0, h0.a {

    /* renamed from: a  reason: collision with root package name */
    private final Object f2794a;

    /* renamed from: b  reason: collision with root package name */
    private y.h f2795b;

    /* renamed from: c  reason: collision with root package name */
    private int f2796c;

    /* renamed from: d  reason: collision with root package name */
    private g0.a f2797d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f2798e;

    /* renamed from: f  reason: collision with root package name */
    private final y.g0 f2799f;

    /* renamed from: g  reason: collision with root package name */
    g0.a f2800g;

    /* renamed from: h  reason: collision with root package name */
    private Executor f2801h;

    /* renamed from: i  reason: collision with root package name */
    private final LongSparseArray<i1> f2802i;

    /* renamed from: j  reason: collision with root package name */
    private final LongSparseArray<l1> f2803j;

    /* renamed from: k  reason: collision with root package name */
    private int f2804k;

    /* renamed from: l  reason: collision with root package name */
    private final List<l1> f2805l;

    /* renamed from: m  reason: collision with root package name */
    private final List<l1> f2806m;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends y.h {
        a() {
        }

        @Override // y.h
        public void b(y.j jVar) {
            super.b(jVar);
            s1.this.r(jVar);
        }
    }

    public s1(int i8, int i9, int i10, int i11) {
        this(i(i8, i9, i10, i11));
    }

    s1(y.g0 g0Var) {
        this.f2794a = new Object();
        this.f2795b = new a();
        this.f2796c = 0;
        this.f2797d = new g0.a() { // from class: androidx.camera.core.r1
            @Override // y.g0.a
            public final void a(y.g0 g0Var2) {
                s1.this.o(g0Var2);
            }
        };
        this.f2798e = false;
        this.f2802i = new LongSparseArray<>();
        this.f2803j = new LongSparseArray<>();
        this.f2806m = new ArrayList();
        this.f2799f = g0Var;
        this.f2804k = 0;
        this.f2805l = new ArrayList(e());
    }

    private static y.g0 i(int i8, int i9, int i10, int i11) {
        return new d(ImageReader.newInstance(i8, i9, i10, i11));
    }

    private void j(l1 l1Var) {
        synchronized (this.f2794a) {
            int indexOf = this.f2805l.indexOf(l1Var);
            if (indexOf >= 0) {
                this.f2805l.remove(indexOf);
                int i8 = this.f2804k;
                if (indexOf <= i8) {
                    this.f2804k = i8 - 1;
                }
            }
            this.f2806m.remove(l1Var);
            if (this.f2796c > 0) {
                m(this.f2799f);
            }
        }
    }

    private void k(n2 n2Var) {
        final g0.a aVar;
        Executor executor;
        synchronized (this.f2794a) {
            aVar = null;
            if (this.f2805l.size() < e()) {
                n2Var.a(this);
                this.f2805l.add(n2Var);
                aVar = this.f2800g;
                executor = this.f2801h;
            } else {
                p1.a("TAG", "Maximum image number reached.");
                n2Var.close();
                executor = null;
            }
        }
        if (aVar != null) {
            if (executor != null) {
                executor.execute(new Runnable() { // from class: androidx.camera.core.q1
                    @Override // java.lang.Runnable
                    public final void run() {
                        s1.this.n(aVar);
                    }
                });
            } else {
                aVar.a(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void n(g0.a aVar) {
        aVar.a(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o(y.g0 g0Var) {
        synchronized (this.f2794a) {
            this.f2796c++;
        }
        m(g0Var);
    }

    private void p() {
        synchronized (this.f2794a) {
            for (int size = this.f2802i.size() - 1; size >= 0; size--) {
                i1 valueAt = this.f2802i.valueAt(size);
                long d8 = valueAt.d();
                l1 l1Var = this.f2803j.get(d8);
                if (l1Var != null) {
                    this.f2803j.remove(d8);
                    this.f2802i.removeAt(size);
                    k(new n2(l1Var, valueAt));
                }
            }
            q();
        }
    }

    private void q() {
        synchronized (this.f2794a) {
            if (this.f2803j.size() != 0 && this.f2802i.size() != 0) {
                Long valueOf = Long.valueOf(this.f2803j.keyAt(0));
                Long valueOf2 = Long.valueOf(this.f2802i.keyAt(0));
                androidx.core.util.h.a(valueOf2.equals(valueOf) ? false : true);
                if (valueOf2.longValue() > valueOf.longValue()) {
                    for (int size = this.f2803j.size() - 1; size >= 0; size--) {
                        if (this.f2803j.keyAt(size) < valueOf2.longValue()) {
                            this.f2803j.valueAt(size).close();
                            this.f2803j.removeAt(size);
                        }
                    }
                } else {
                    for (int size2 = this.f2802i.size() - 1; size2 >= 0; size2--) {
                        if (this.f2802i.keyAt(size2) < valueOf.longValue()) {
                            this.f2802i.removeAt(size2);
                        }
                    }
                }
            }
        }
    }

    @Override // y.g0
    public void a(g0.a aVar, Executor executor) {
        synchronized (this.f2794a) {
            this.f2800g = (g0.a) androidx.core.util.h.h(aVar);
            this.f2801h = (Executor) androidx.core.util.h.h(executor);
            this.f2799f.a(this.f2797d, executor);
        }
    }

    @Override // y.g0
    public l1 acquireLatestImage() {
        synchronized (this.f2794a) {
            if (this.f2805l.isEmpty()) {
                return null;
            }
            if (this.f2804k < this.f2805l.size()) {
                ArrayList<l1> arrayList = new ArrayList();
                for (int i8 = 0; i8 < this.f2805l.size() - 1; i8++) {
                    if (!this.f2806m.contains(this.f2805l.get(i8))) {
                        arrayList.add(this.f2805l.get(i8));
                    }
                }
                for (l1 l1Var : arrayList) {
                    l1Var.close();
                }
                int size = this.f2805l.size() - 1;
                this.f2804k = size;
                List<l1> list = this.f2805l;
                this.f2804k = size + 1;
                l1 l1Var2 = list.get(size);
                this.f2806m.add(l1Var2);
                return l1Var2;
            }
            throw new IllegalStateException("Maximum image number reached.");
        }
    }

    @Override // androidx.camera.core.h0.a
    public void b(l1 l1Var) {
        synchronized (this.f2794a) {
            j(l1Var);
        }
    }

    @Override // y.g0
    public int c() {
        int c9;
        synchronized (this.f2794a) {
            c9 = this.f2799f.c();
        }
        return c9;
    }

    @Override // y.g0
    public void close() {
        synchronized (this.f2794a) {
            if (this.f2798e) {
                return;
            }
            for (l1 l1Var : new ArrayList(this.f2805l)) {
                l1Var.close();
            }
            this.f2805l.clear();
            this.f2799f.close();
            this.f2798e = true;
        }
    }

    @Override // y.g0
    public void d() {
        synchronized (this.f2794a) {
            this.f2799f.d();
            this.f2800g = null;
            this.f2801h = null;
            this.f2796c = 0;
        }
    }

    @Override // y.g0
    public int e() {
        int e8;
        synchronized (this.f2794a) {
            e8 = this.f2799f.e();
        }
        return e8;
    }

    @Override // y.g0
    public l1 f() {
        synchronized (this.f2794a) {
            if (this.f2805l.isEmpty()) {
                return null;
            }
            if (this.f2804k < this.f2805l.size()) {
                List<l1> list = this.f2805l;
                int i8 = this.f2804k;
                this.f2804k = i8 + 1;
                l1 l1Var = list.get(i8);
                this.f2806m.add(l1Var);
                return l1Var;
            }
            throw new IllegalStateException("Maximum image number reached.");
        }
    }

    @Override // y.g0
    public int getHeight() {
        int height;
        synchronized (this.f2794a) {
            height = this.f2799f.getHeight();
        }
        return height;
    }

    @Override // y.g0
    public Surface getSurface() {
        Surface surface;
        synchronized (this.f2794a) {
            surface = this.f2799f.getSurface();
        }
        return surface;
    }

    @Override // y.g0
    public int getWidth() {
        int width;
        synchronized (this.f2794a) {
            width = this.f2799f.getWidth();
        }
        return width;
    }

    public y.h l() {
        return this.f2795b;
    }

    void m(y.g0 g0Var) {
        synchronized (this.f2794a) {
            if (this.f2798e) {
                return;
            }
            int size = this.f2803j.size() + this.f2805l.size();
            if (size >= g0Var.e()) {
                p1.a("MetadataImageReader", "Skip to acquire the next image because the acquired image count has reached the max images count.");
                return;
            }
            do {
                l1 l1Var = null;
                try {
                    l1Var = g0Var.f();
                    if (l1Var != null) {
                        this.f2796c--;
                        size++;
                        this.f2803j.put(l1Var.e1().d(), l1Var);
                        p();
                    }
                } catch (IllegalStateException e8) {
                    p1.b("MetadataImageReader", "Failed to acquire next image.", e8);
                }
                if (l1Var == null || this.f2796c <= 0) {
                    break;
                }
            } while (size < g0Var.e());
        }
    }

    void r(y.j jVar) {
        synchronized (this.f2794a) {
            if (this.f2798e) {
                return;
            }
            this.f2802i.put(jVar.d(), new b0.c(jVar));
            p();
        }
    }
}
