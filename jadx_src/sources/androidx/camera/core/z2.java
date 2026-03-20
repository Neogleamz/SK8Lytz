package androidx.camera.core;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.util.Range;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.z2;
import androidx.concurrent.futures.c;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z2 {

    /* renamed from: a  reason: collision with root package name */
    private final Object f2897a;

    /* renamed from: b  reason: collision with root package name */
    private final Size f2898b;

    /* renamed from: c  reason: collision with root package name */
    private final Range<Integer> f2899c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f2900d;

    /* renamed from: e  reason: collision with root package name */
    private final CameraInternal f2901e;

    /* renamed from: f  reason: collision with root package name */
    final com.google.common.util.concurrent.d<Surface> f2902f;

    /* renamed from: g  reason: collision with root package name */
    private final c.a<Surface> f2903g;

    /* renamed from: h  reason: collision with root package name */
    private final com.google.common.util.concurrent.d<Void> f2904h;

    /* renamed from: i  reason: collision with root package name */
    private final c.a<Void> f2905i;

    /* renamed from: j  reason: collision with root package name */
    private final DeferrableSurface f2906j;

    /* renamed from: k  reason: collision with root package name */
    private g f2907k;

    /* renamed from: l  reason: collision with root package name */
    private h f2908l;

    /* renamed from: m  reason: collision with root package name */
    private Executor f2909m;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements a0.c<Void> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ c.a f2910a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ com.google.common.util.concurrent.d f2911b;

        a(c.a aVar, com.google.common.util.concurrent.d dVar) {
            this.f2910a = aVar;
            this.f2911b = dVar;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r22) {
            androidx.core.util.h.j(this.f2910a.c(null));
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            androidx.core.util.h.j(th instanceof e ? this.f2911b.cancel(false) : this.f2910a.c(null));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends DeferrableSurface {
        b(Size size, int i8) {
            super(size, i8);
        }

        @Override // androidx.camera.core.impl.DeferrableSurface
        protected com.google.common.util.concurrent.d<Surface> n() {
            return z2.this.f2902f;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c implements a0.c<Surface> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ com.google.common.util.concurrent.d f2914a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ c.a f2915b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ String f2916c;

        c(com.google.common.util.concurrent.d dVar, c.a aVar, String str) {
            this.f2914a = dVar;
            this.f2915b = aVar;
            this.f2916c = str;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Surface surface) {
            a0.f.k(this.f2914a, this.f2915b);
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            if (!(th instanceof CancellationException)) {
                this.f2915b.c(null);
                return;
            }
            c.a aVar = this.f2915b;
            androidx.core.util.h.j(aVar.f(new e(this.f2916c + " cancelled.", th)));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d implements a0.c<Void> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ androidx.core.util.a f2918a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Surface f2919b;

        d(androidx.core.util.a aVar, Surface surface) {
            this.f2918a = aVar;
            this.f2919b = surface;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r32) {
            this.f2918a.accept(f.c(0, this.f2919b));
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            androidx.core.util.h.k(th instanceof e, "Camera surface session should only fail with request cancellation. Instead failed due to:\n" + th);
            this.f2918a.accept(f.c(1, this.f2919b));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class e extends RuntimeException {
        e(String str, Throwable th) {
            super(str, th);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class f {
        static f c(int i8, Surface surface) {
            return new k(i8, surface);
        }

        public abstract int a();

        public abstract Surface b();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class g {
        public static g d(Rect rect, int i8, int i9) {
            return new l(rect, i8, i9);
        }

        public abstract Rect a();

        public abstract int b();

        public abstract int c();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface h {
        void a(g gVar);
    }

    public z2(Size size, CameraInternal cameraInternal, boolean z4) {
        this(size, cameraInternal, z4, null);
    }

    public z2(Size size, CameraInternal cameraInternal, boolean z4, Range<Integer> range) {
        this.f2897a = new Object();
        this.f2898b = size;
        this.f2901e = cameraInternal;
        this.f2900d = z4;
        this.f2899c = range;
        final String str = "SurfaceRequest[size: " + size + ", id: " + hashCode() + "]";
        final AtomicReference atomicReference = new AtomicReference(null);
        com.google.common.util.concurrent.d a9 = androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.core.s2
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar) {
                Object n8;
                n8 = z2.n(atomicReference, str, aVar);
                return n8;
            }
        });
        c.a<Void> aVar = (c.a) androidx.core.util.h.h((c.a) atomicReference.get());
        this.f2905i = aVar;
        final AtomicReference atomicReference2 = new AtomicReference(null);
        com.google.common.util.concurrent.d<Void> a10 = androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.core.t2
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar2) {
                Object o5;
                o5 = z2.o(atomicReference2, str, aVar2);
                return o5;
            }
        });
        this.f2904h = a10;
        a0.f.b(a10, new a(aVar, a9), z.a.a());
        final AtomicReference atomicReference3 = new AtomicReference(null);
        com.google.common.util.concurrent.d<Surface> a11 = androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.core.r2
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar2) {
                Object p8;
                p8 = z2.p(atomicReference3, str, aVar2);
                return p8;
            }
        });
        this.f2902f = a11;
        this.f2903g = (c.a) androidx.core.util.h.h((c.a) atomicReference3.get());
        b bVar = new b(size, 34);
        this.f2906j = bVar;
        com.google.common.util.concurrent.d<Void> i8 = bVar.i();
        a0.f.b(a11, new c(i8, (c.a) androidx.core.util.h.h((c.a) atomicReference2.get()), str), z.a.a());
        i8.c(new Runnable() { // from class: androidx.camera.core.w2
            @Override // java.lang.Runnable
            public final void run() {
                z2.this.q();
            }
        }, z.a.a());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object n(AtomicReference atomicReference, String str, c.a aVar) {
        atomicReference.set(aVar);
        return str + "-cancellation";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object o(AtomicReference atomicReference, String str, c.a aVar) {
        atomicReference.set(aVar);
        return str + "-status";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Object p(AtomicReference atomicReference, String str, c.a aVar) {
        atomicReference.set(aVar);
        return str + "-Surface";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void q() {
        this.f2902f.cancel(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void r(androidx.core.util.a aVar, Surface surface) {
        aVar.accept(f.c(3, surface));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void s(androidx.core.util.a aVar, Surface surface) {
        aVar.accept(f.c(4, surface));
    }

    @SuppressLint({"PairedRegistration"})
    public void i(Executor executor, Runnable runnable) {
        this.f2905i.a(runnable, executor);
    }

    public CameraInternal j() {
        return this.f2901e;
    }

    public DeferrableSurface k() {
        return this.f2906j;
    }

    public Size l() {
        return this.f2898b;
    }

    public boolean m() {
        return this.f2900d;
    }

    public void v(final Surface surface, Executor executor, final androidx.core.util.a<f> aVar) {
        if (this.f2903g.c(surface) || this.f2902f.isCancelled()) {
            a0.f.b(this.f2904h, new d(aVar, surface), executor);
            return;
        }
        androidx.core.util.h.j(this.f2902f.isDone());
        try {
            this.f2902f.get();
            executor.execute(new Runnable() { // from class: androidx.camera.core.x2
                @Override // java.lang.Runnable
                public final void run() {
                    z2.r(androidx.core.util.a.this, surface);
                }
            });
        } catch (InterruptedException | ExecutionException unused) {
            executor.execute(new Runnable() { // from class: androidx.camera.core.y2
                @Override // java.lang.Runnable
                public final void run() {
                    z2.s(androidx.core.util.a.this, surface);
                }
            });
        }
    }

    public void w(Executor executor, final h hVar) {
        final g gVar;
        synchronized (this.f2897a) {
            this.f2908l = hVar;
            this.f2909m = executor;
            gVar = this.f2907k;
        }
        if (gVar != null) {
            executor.execute(new Runnable() { // from class: androidx.camera.core.u2
                @Override // java.lang.Runnable
                public final void run() {
                    z2.h.this.a(gVar);
                }
            });
        }
    }

    public void x(final g gVar) {
        final h hVar;
        Executor executor;
        synchronized (this.f2897a) {
            this.f2907k = gVar;
            hVar = this.f2908l;
            executor = this.f2909m;
        }
        if (hVar == null || executor == null) {
            return;
        }
        executor.execute(new Runnable() { // from class: androidx.camera.core.v2
            @Override // java.lang.Runnable
            public final void run() {
                z2.h.this.a(gVar);
            }
        });
    }

    public boolean y() {
        return this.f2903g.f(new DeferrableSurface.SurfaceUnavailableException("Surface request will not complete."));
    }
}
