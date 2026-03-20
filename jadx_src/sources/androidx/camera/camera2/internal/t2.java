package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.view.Surface;
import androidx.camera.camera2.internal.n2;
import androidx.camera.camera2.internal.z2;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.concurrent.futures.c;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class t2 extends n2.a implements n2, z2.b {

    /* renamed from: b  reason: collision with root package name */
    final v1 f2115b;

    /* renamed from: c  reason: collision with root package name */
    final Handler f2116c;

    /* renamed from: d  reason: collision with root package name */
    final Executor f2117d;

    /* renamed from: e  reason: collision with root package name */
    private final ScheduledExecutorService f2118e;

    /* renamed from: f  reason: collision with root package name */
    n2.a f2119f;

    /* renamed from: g  reason: collision with root package name */
    s.f f2120g;

    /* renamed from: h  reason: collision with root package name */
    com.google.common.util.concurrent.d<Void> f2121h;

    /* renamed from: i  reason: collision with root package name */
    c.a<Void> f2122i;

    /* renamed from: j  reason: collision with root package name */
    private com.google.common.util.concurrent.d<List<Surface>> f2123j;

    /* renamed from: a  reason: collision with root package name */
    final Object f2114a = new Object();

    /* renamed from: k  reason: collision with root package name */
    private List<DeferrableSurface> f2124k = null;

    /* renamed from: l  reason: collision with root package name */
    private boolean f2125l = false;

    /* renamed from: m  reason: collision with root package name */
    private boolean f2126m = false;

    /* renamed from: n  reason: collision with root package name */
    private boolean f2127n = false;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements a0.c<Void> {
        a() {
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r12) {
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            t2.this.d();
            t2 t2Var = t2.this;
            t2Var.f2115b.j(t2Var);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends CameraCaptureSession.StateCallback {
        b() {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onActive(CameraCaptureSession cameraCaptureSession) {
            t2.this.A(cameraCaptureSession);
            t2 t2Var = t2.this;
            t2Var.a(t2Var);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onCaptureQueueEmpty(CameraCaptureSession cameraCaptureSession) {
            t2.this.A(cameraCaptureSession);
            t2 t2Var = t2.this;
            t2Var.o(t2Var);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onClosed(CameraCaptureSession cameraCaptureSession) {
            t2.this.A(cameraCaptureSession);
            t2 t2Var = t2.this;
            t2Var.p(t2Var);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
            c.a<Void> aVar;
            try {
                t2.this.A(cameraCaptureSession);
                t2 t2Var = t2.this;
                t2Var.q(t2Var);
                synchronized (t2.this.f2114a) {
                    androidx.core.util.h.i(t2.this.f2122i, "OpenCaptureSession completer should not null");
                    t2 t2Var2 = t2.this;
                    aVar = t2Var2.f2122i;
                    t2Var2.f2122i = null;
                }
                aVar.f(new IllegalStateException("onConfigureFailed"));
            } catch (Throwable th) {
                synchronized (t2.this.f2114a) {
                    androidx.core.util.h.i(t2.this.f2122i, "OpenCaptureSession completer should not null");
                    t2 t2Var3 = t2.this;
                    c.a<Void> aVar2 = t2Var3.f2122i;
                    t2Var3.f2122i = null;
                    aVar2.f(new IllegalStateException("onConfigureFailed"));
                    throw th;
                }
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
            c.a<Void> aVar;
            try {
                t2.this.A(cameraCaptureSession);
                t2 t2Var = t2.this;
                t2Var.r(t2Var);
                synchronized (t2.this.f2114a) {
                    androidx.core.util.h.i(t2.this.f2122i, "OpenCaptureSession completer should not null");
                    t2 t2Var2 = t2.this;
                    aVar = t2Var2.f2122i;
                    t2Var2.f2122i = null;
                }
                aVar.c(null);
            } catch (Throwable th) {
                synchronized (t2.this.f2114a) {
                    androidx.core.util.h.i(t2.this.f2122i, "OpenCaptureSession completer should not null");
                    t2 t2Var3 = t2.this;
                    c.a<Void> aVar2 = t2Var3.f2122i;
                    t2Var3.f2122i = null;
                    aVar2.c(null);
                    throw th;
                }
            }
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onReady(CameraCaptureSession cameraCaptureSession) {
            t2.this.A(cameraCaptureSession);
            t2 t2Var = t2.this;
            t2Var.s(t2Var);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.StateCallback
        public void onSurfacePrepared(CameraCaptureSession cameraCaptureSession, Surface surface) {
            t2.this.A(cameraCaptureSession);
            t2 t2Var = t2.this;
            t2Var.u(t2Var, surface);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public t2(v1 v1Var, Executor executor, ScheduledExecutorService scheduledExecutorService, Handler handler) {
        this.f2115b = v1Var;
        this.f2116c = handler;
        this.f2117d = executor;
        this.f2118e = scheduledExecutorService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void D() {
        t(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void E(n2 n2Var) {
        this.f2115b.h(this);
        t(n2Var);
        Objects.requireNonNull(this.f2119f);
        this.f2119f.p(n2Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void F(n2 n2Var) {
        Objects.requireNonNull(this.f2119f);
        this.f2119f.t(n2Var);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object G(List list, s.z zVar, t.h hVar, c.a aVar) {
        String str;
        synchronized (this.f2114a) {
            B(list);
            androidx.core.util.h.k(this.f2122i == null, "The openCaptureSessionCompleter can only set once!");
            this.f2122i = aVar;
            zVar.a(hVar);
            str = "openCaptureSession[session=" + this + "]";
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.google.common.util.concurrent.d H(List list, List list2) {
        androidx.camera.core.p1.a("SyncCaptureSessionBase", "[" + this + "] getSurface...done");
        return list2.contains(null) ? a0.f.f(new DeferrableSurface.SurfaceClosedException("Surface closed", (DeferrableSurface) list.get(list2.indexOf(null)))) : list2.isEmpty() ? a0.f.f(new IllegalArgumentException("Unable to open capture session without surfaces")) : a0.f.h(list2);
    }

    void A(CameraCaptureSession cameraCaptureSession) {
        if (this.f2120g == null) {
            this.f2120g = s.f.d(cameraCaptureSession, this.f2116c);
        }
    }

    void B(List<DeferrableSurface> list) {
        synchronized (this.f2114a) {
            I();
            androidx.camera.core.impl.h.f(list);
            this.f2124k = list;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean C() {
        boolean z4;
        synchronized (this.f2114a) {
            z4 = this.f2121h != null;
        }
        return z4;
    }

    void I() {
        synchronized (this.f2114a) {
            List<DeferrableSurface> list = this.f2124k;
            if (list != null) {
                androidx.camera.core.impl.h.e(list);
                this.f2124k = null;
            }
        }
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void a(n2 n2Var) {
        Objects.requireNonNull(this.f2119f);
        this.f2119f.a(n2Var);
    }

    @Override // androidx.camera.camera2.internal.z2.b
    public Executor b() {
        return this.f2117d;
    }

    @Override // androidx.camera.camera2.internal.n2
    public n2.a c() {
        return this;
    }

    @Override // androidx.camera.camera2.internal.n2
    public void close() {
        androidx.core.util.h.i(this.f2120g, "Need to call openCaptureSession before using this API.");
        this.f2115b.i(this);
        this.f2120g.c().close();
        b().execute(new Runnable() { // from class: androidx.camera.camera2.internal.q2
            @Override // java.lang.Runnable
            public final void run() {
                t2.this.D();
            }
        });
    }

    @Override // androidx.camera.camera2.internal.n2
    public void d() {
        I();
    }

    @Override // androidx.camera.camera2.internal.n2
    public int e(List<CaptureRequest> list, CameraCaptureSession.CaptureCallback captureCallback) {
        androidx.core.util.h.i(this.f2120g, "Need to call openCaptureSession before using this API.");
        return this.f2120g.a(list, b(), captureCallback);
    }

    @Override // androidx.camera.camera2.internal.n2
    public s.f f() {
        androidx.core.util.h.h(this.f2120g);
        return this.f2120g;
    }

    @Override // androidx.camera.camera2.internal.n2
    public void g() {
        androidx.core.util.h.i(this.f2120g, "Need to call openCaptureSession before using this API.");
        this.f2120g.c().abortCaptures();
    }

    @Override // androidx.camera.camera2.internal.n2
    public CameraDevice h() {
        androidx.core.util.h.h(this.f2120g);
        return this.f2120g.c().getDevice();
    }

    @Override // androidx.camera.camera2.internal.n2
    public int i(CaptureRequest captureRequest, CameraCaptureSession.CaptureCallback captureCallback) {
        androidx.core.util.h.i(this.f2120g, "Need to call openCaptureSession before using this API.");
        return this.f2120g.b(captureRequest, b(), captureCallback);
    }

    @Override // androidx.camera.camera2.internal.z2.b
    public t.h j(int i8, List<t.b> list, n2.a aVar) {
        this.f2119f = aVar;
        return new t.h(i8, list, b(), new b());
    }

    @Override // androidx.camera.camera2.internal.n2
    public void k() {
        androidx.core.util.h.i(this.f2120g, "Need to call openCaptureSession before using this API.");
        this.f2120g.c().stopRepeating();
    }

    @Override // androidx.camera.camera2.internal.z2.b
    public com.google.common.util.concurrent.d<List<Surface>> l(final List<DeferrableSurface> list, long j8) {
        synchronized (this.f2114a) {
            if (this.f2126m) {
                return a0.f.f(new CancellationException("Opener is disabled"));
            }
            a0.d f5 = a0.d.a(androidx.camera.core.impl.h.k(list, false, j8, b(), this.f2118e)).f(new a0.a() { // from class: androidx.camera.camera2.internal.o2
                @Override // a0.a
                public final com.google.common.util.concurrent.d apply(Object obj) {
                    com.google.common.util.concurrent.d H;
                    H = t2.this.H(list, (List) obj);
                    return H;
                }
            }, b());
            this.f2123j = f5;
            return a0.f.j(f5);
        }
    }

    @Override // androidx.camera.camera2.internal.n2
    public com.google.common.util.concurrent.d<Void> m() {
        return a0.f.h(null);
    }

    @Override // androidx.camera.camera2.internal.z2.b
    public com.google.common.util.concurrent.d<Void> n(CameraDevice cameraDevice, final t.h hVar, final List<DeferrableSurface> list) {
        synchronized (this.f2114a) {
            if (this.f2126m) {
                return a0.f.f(new CancellationException("Opener is disabled"));
            }
            this.f2115b.l(this);
            final s.z b9 = s.z.b(cameraDevice, this.f2116c);
            com.google.common.util.concurrent.d<Void> a9 = androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.camera2.internal.p2
                @Override // androidx.concurrent.futures.c.InterfaceC0024c
                public final Object a(c.a aVar) {
                    Object G;
                    G = t2.this.G(list, b9, hVar, aVar);
                    return G;
                }
            });
            this.f2121h = a9;
            a0.f.b(a9, new a(), z.a.a());
            return a0.f.j(this.f2121h);
        }
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void o(n2 n2Var) {
        Objects.requireNonNull(this.f2119f);
        this.f2119f.o(n2Var);
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void p(final n2 n2Var) {
        com.google.common.util.concurrent.d<Void> dVar;
        synchronized (this.f2114a) {
            if (this.f2125l) {
                dVar = null;
            } else {
                this.f2125l = true;
                androidx.core.util.h.i(this.f2121h, "Need to call openCaptureSession before using this API.");
                dVar = this.f2121h;
            }
        }
        d();
        if (dVar != null) {
            dVar.c(new Runnable() { // from class: androidx.camera.camera2.internal.s2
                @Override // java.lang.Runnable
                public final void run() {
                    t2.this.E(n2Var);
                }
            }, z.a.a());
        }
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void q(n2 n2Var) {
        Objects.requireNonNull(this.f2119f);
        d();
        this.f2115b.j(this);
        this.f2119f.q(n2Var);
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void r(n2 n2Var) {
        Objects.requireNonNull(this.f2119f);
        this.f2115b.k(this);
        this.f2119f.r(n2Var);
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void s(n2 n2Var) {
        Objects.requireNonNull(this.f2119f);
        this.f2119f.s(n2Var);
    }

    @Override // androidx.camera.camera2.internal.z2.b
    public boolean stop() {
        boolean z4;
        try {
            synchronized (this.f2114a) {
                if (!this.f2126m) {
                    com.google.common.util.concurrent.d<List<Surface>> dVar = this.f2123j;
                    r1 = dVar != null ? dVar : null;
                    this.f2126m = true;
                }
                z4 = !C();
            }
            return z4;
        } finally {
            if (r1 != null) {
                r1.cancel(true);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.camera.camera2.internal.n2.a
    public void t(final n2 n2Var) {
        com.google.common.util.concurrent.d<Void> dVar;
        synchronized (this.f2114a) {
            if (this.f2127n) {
                dVar = null;
            } else {
                this.f2127n = true;
                androidx.core.util.h.i(this.f2121h, "Need to call openCaptureSession before using this API.");
                dVar = this.f2121h;
            }
        }
        if (dVar != null) {
            dVar.c(new Runnable() { // from class: androidx.camera.camera2.internal.r2
                @Override // java.lang.Runnable
                public final void run() {
                    t2.this.F(n2Var);
                }
            }, z.a.a());
        }
    }

    @Override // androidx.camera.camera2.internal.n2.a
    public void u(n2 n2Var, Surface surface) {
        Objects.requireNonNull(this.f2119f);
        this.f2119f.u(n2Var, surface);
    }
}
