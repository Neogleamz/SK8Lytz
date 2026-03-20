package androidx.camera.core;

import android.media.ImageReader;
import android.util.Size;
import android.view.Surface;
import androidx.concurrent.futures.c;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import y.g0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f0 implements y.v {

    /* renamed from: a  reason: collision with root package name */
    private final y.v f2346a;

    /* renamed from: b  reason: collision with root package name */
    private final y.v f2347b;

    /* renamed from: c  reason: collision with root package name */
    private final com.google.common.util.concurrent.d<List<Void>> f2348c;

    /* renamed from: d  reason: collision with root package name */
    final Executor f2349d;

    /* renamed from: e  reason: collision with root package name */
    private final int f2350e;

    /* renamed from: f  reason: collision with root package name */
    private y.g0 f2351f = null;

    /* renamed from: g  reason: collision with root package name */
    private i1 f2352g = null;

    /* renamed from: h  reason: collision with root package name */
    private final Object f2353h = new Object();

    /* renamed from: i  reason: collision with root package name */
    private boolean f2354i = false;

    /* renamed from: j  reason: collision with root package name */
    private boolean f2355j = false;

    /* renamed from: k  reason: collision with root package name */
    c.a<Void> f2356k;

    /* renamed from: l  reason: collision with root package name */
    private com.google.common.util.concurrent.d<Void> f2357l;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f0(y.v vVar, int i8, y.v vVar2, Executor executor) {
        this.f2346a = vVar;
        this.f2347b = vVar2;
        ArrayList arrayList = new ArrayList();
        arrayList.add(vVar.b());
        arrayList.add(vVar2.b());
        this.f2348c = a0.f.c(arrayList);
        this.f2349d = executor;
        this.f2350e = i8;
    }

    private void j() {
        boolean z4;
        boolean z8;
        final c.a<Void> aVar;
        synchronized (this.f2353h) {
            z4 = this.f2354i;
            z8 = this.f2355j;
            aVar = this.f2356k;
            if (z4 && !z8) {
                this.f2351f.close();
            }
        }
        if (!z4 || z8 || aVar == null) {
            return;
        }
        this.f2348c.c(new Runnable() { // from class: androidx.camera.core.c0
            @Override // java.lang.Runnable
            public final void run() {
                c.a.this.c(null);
            }
        }, z.a.a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Void l(List list) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object m(c.a aVar) {
        synchronized (this.f2353h) {
            this.f2356k = aVar;
        }
        return "CaptureProcessorPipeline-close";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o(y.g0 g0Var) {
        final l1 f5 = g0Var.f();
        try {
            this.f2349d.execute(new Runnable() { // from class: androidx.camera.core.b0
                @Override // java.lang.Runnable
                public final void run() {
                    f0.this.n(f5);
                }
            });
        } catch (RejectedExecutionException unused) {
            p1.c("CaptureProcessorPipeline", "The executor for post-processing might have been shutting down or terminated!");
            f5.close();
        }
    }

    @Override // y.v
    public void a(Surface surface, int i8) {
        this.f2347b.a(surface, i8);
    }

    @Override // y.v
    public com.google.common.util.concurrent.d<Void> b() {
        com.google.common.util.concurrent.d<Void> j8;
        synchronized (this.f2353h) {
            if (!this.f2354i || this.f2355j) {
                if (this.f2357l == null) {
                    this.f2357l = androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.core.a0
                        @Override // androidx.concurrent.futures.c.InterfaceC0024c
                        public final Object a(c.a aVar) {
                            Object m8;
                            m8 = f0.this.m(aVar);
                            return m8;
                        }
                    });
                }
                j8 = a0.f.j(this.f2357l);
            } else {
                j8 = a0.f.o(this.f2348c, new n.a() { // from class: androidx.camera.core.d0
                    @Override // n.a
                    public final Object apply(Object obj) {
                        Void l8;
                        l8 = f0.l((List) obj);
                        return l8;
                    }
                }, z.a.a());
            }
        }
        return j8;
    }

    @Override // y.v
    public void c(y.f0 f0Var) {
        synchronized (this.f2353h) {
            if (this.f2354i) {
                return;
            }
            this.f2355j = true;
            com.google.common.util.concurrent.d<l1> a9 = f0Var.a(f0Var.b().get(0).intValue());
            androidx.core.util.h.a(a9.isDone());
            try {
                this.f2352g = a9.get().e1();
                this.f2346a.c(f0Var);
            } catch (InterruptedException | ExecutionException unused) {
                throw new IllegalArgumentException("Can not successfully extract ImageProxy from the ImageProxyBundle.");
            }
        }
    }

    @Override // y.v
    public void close() {
        synchronized (this.f2353h) {
            if (this.f2354i) {
                return;
            }
            this.f2354i = true;
            this.f2346a.close();
            this.f2347b.close();
            j();
        }
    }

    @Override // y.v
    public void d(Size size) {
        d dVar = new d(ImageReader.newInstance(size.getWidth(), size.getHeight(), 35, this.f2350e));
        this.f2351f = dVar;
        this.f2346a.a(dVar.getSurface(), 35);
        this.f2346a.d(size);
        this.f2347b.d(size);
        this.f2351f.a(new g0.a() { // from class: androidx.camera.core.e0
            @Override // y.g0.a
            public final void a(y.g0 g0Var) {
                f0.this.o(g0Var);
            }
        }, z.a.a());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: p */
    public void n(l1 l1Var) {
        boolean z4;
        synchronized (this.f2353h) {
            z4 = this.f2354i;
        }
        if (!z4) {
            Size size = new Size(l1Var.getWidth(), l1Var.getHeight());
            androidx.core.util.h.h(this.f2352g);
            String next = this.f2352g.a().d().iterator().next();
            int intValue = ((Integer) this.f2352g.a().c(next)).intValue();
            n2 n2Var = new n2(l1Var, size, this.f2352g);
            this.f2352g = null;
            o2 o2Var = new o2(Collections.singletonList(Integer.valueOf(intValue)), next);
            o2Var.c(n2Var);
            try {
                this.f2347b.c(o2Var);
            } catch (Exception e8) {
                p1.c("CaptureProcessorPipeline", "Post processing image failed! " + e8.getMessage());
            }
        }
        synchronized (this.f2353h) {
            this.f2355j = false;
        }
        j();
    }
}
