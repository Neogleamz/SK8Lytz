package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.util.Size;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.SessionConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import r.a;
import w.j;
import y.v0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k2 implements t1 {
    private static List<DeferrableSurface> q = new ArrayList();

    /* renamed from: r  reason: collision with root package name */
    private static int f1914r = 0;

    /* renamed from: a  reason: collision with root package name */
    private final y.v0 f1915a;

    /* renamed from: b  reason: collision with root package name */
    private final j0 f1916b;

    /* renamed from: c  reason: collision with root package name */
    final Executor f1917c;

    /* renamed from: d  reason: collision with root package name */
    private final ScheduledExecutorService f1918d;

    /* renamed from: g  reason: collision with root package name */
    private SessionConfig f1921g;

    /* renamed from: h  reason: collision with root package name */
    private d1 f1922h;

    /* renamed from: i  reason: collision with root package name */
    private SessionConfig f1923i;

    /* renamed from: p  reason: collision with root package name */
    private int f1930p;

    /* renamed from: f  reason: collision with root package name */
    private List<DeferrableSurface> f1920f = new ArrayList();

    /* renamed from: k  reason: collision with root package name */
    private volatile androidx.camera.core.impl.f f1925k = null;

    /* renamed from: l  reason: collision with root package name */
    volatile boolean f1926l = false;

    /* renamed from: n  reason: collision with root package name */
    private w.j f1928n = new j.a().d();

    /* renamed from: o  reason: collision with root package name */
    private w.j f1929o = new j.a().d();

    /* renamed from: e  reason: collision with root package name */
    private final s1 f1919e = new s1();

    /* renamed from: j  reason: collision with root package name */
    private d f1924j = d.UNINITIALIZED;

    /* renamed from: m  reason: collision with root package name */
    private final e f1927m = new e();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements a0.c<Void> {
        a() {
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r12) {
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            androidx.camera.core.p1.d("ProcessingCaptureSession", "open session failed ", th);
            k2.this.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements v0.a {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ androidx.camera.core.impl.f f1932a;

        b(androidx.camera.core.impl.f fVar) {
            this.f1932a = fVar;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class c {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f1934a;

        static {
            int[] iArr = new int[d.values().length];
            f1934a = iArr;
            try {
                iArr[d.UNINITIALIZED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f1934a[d.SESSION_INITIALIZED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f1934a[d.ON_CAPTURE_SESSION_STARTED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f1934a[d.ON_CAPTURE_SESSION_ENDED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f1934a[d.CLOSED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum d {
        UNINITIALIZED,
        SESSION_INITIALIZED,
        ON_CAPTURE_SESSION_STARTED,
        ON_CAPTURE_SESSION_ENDED,
        CLOSED
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e implements v0.a {
        e() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k2(y.v0 v0Var, j0 j0Var, Executor executor, ScheduledExecutorService scheduledExecutorService) {
        this.f1930p = 0;
        this.f1915a = v0Var;
        this.f1916b = j0Var;
        this.f1917c = executor;
        this.f1918d = scheduledExecutorService;
        int i8 = f1914r;
        f1914r = i8 + 1;
        this.f1930p = i8;
        androidx.camera.core.p1.a("ProcessingCaptureSession", "New ProcessingCaptureSession (id=" + this.f1930p + ")");
    }

    private static void l(List<androidx.camera.core.impl.f> list) {
        for (androidx.camera.core.impl.f fVar : list) {
            for (y.h hVar : fVar.b()) {
                hVar.a();
            }
        }
    }

    private static List<y.w0> m(List<DeferrableSurface> list) {
        ArrayList arrayList = new ArrayList();
        for (DeferrableSurface deferrableSurface : list) {
            androidx.core.util.h.b(deferrableSurface instanceof y.w0, "Surface must be SessionProcessorSurface");
            arrayList.add((y.w0) deferrableSurface);
        }
        return arrayList;
    }

    private boolean n(List<androidx.camera.core.impl.f> list) {
        if (list.isEmpty()) {
            return false;
        }
        for (androidx.camera.core.impl.f fVar : list) {
            if (fVar.g() != 2) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o() {
        androidx.camera.core.impl.h.e(this.f1920f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void p(DeferrableSurface deferrableSurface) {
        q.remove(deferrableSurface);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.google.common.util.concurrent.d q(SessionConfig sessionConfig, CameraDevice cameraDevice, z2 z2Var, List list) {
        androidx.camera.core.p1.a("ProcessingCaptureSession", "-- getSurfaces done, start init (id=" + this.f1930p + ")");
        if (this.f1924j == d.CLOSED) {
            return a0.f.f(new IllegalStateException("SessionProcessorCaptureSession is closed."));
        }
        y.r0 r0Var = null;
        if (list.contains(null)) {
            return a0.f.f(new DeferrableSurface.SurfaceClosedException("Surface closed", sessionConfig.k().get(list.indexOf(null))));
        }
        try {
            androidx.camera.core.impl.h.f(this.f1920f);
            y.r0 r0Var2 = null;
            y.r0 r0Var3 = null;
            for (int i8 = 0; i8 < sessionConfig.k().size(); i8++) {
                DeferrableSurface deferrableSurface = sessionConfig.k().get(i8);
                if (Objects.equals(deferrableSurface.e(), androidx.camera.core.y1.class)) {
                    r0Var = y.r0.a(deferrableSurface.h().get(), new Size(deferrableSurface.f().getWidth(), deferrableSurface.f().getHeight()), deferrableSurface.g());
                } else if (Objects.equals(deferrableSurface.e(), androidx.camera.core.e1.class)) {
                    r0Var2 = y.r0.a(deferrableSurface.h().get(), new Size(deferrableSurface.f().getWidth(), deferrableSurface.f().getHeight()), deferrableSurface.g());
                } else if (Objects.equals(deferrableSurface.e(), androidx.camera.core.l0.class)) {
                    r0Var3 = y.r0.a(deferrableSurface.h().get(), new Size(deferrableSurface.f().getWidth(), deferrableSurface.f().getHeight()), deferrableSurface.g());
                }
            }
            this.f1924j = d.SESSION_INITIALIZED;
            androidx.camera.core.p1.k("ProcessingCaptureSession", "== initSession (id=" + this.f1930p + ")");
            SessionConfig f5 = this.f1915a.f(this.f1916b, r0Var, r0Var2, r0Var3);
            this.f1923i = f5;
            f5.k().get(0).i().c(new Runnable() { // from class: androidx.camera.camera2.internal.h2
                @Override // java.lang.Runnable
                public final void run() {
                    k2.this.o();
                }
            }, z.a.a());
            for (final DeferrableSurface deferrableSurface2 : this.f1923i.k()) {
                q.add(deferrableSurface2);
                deferrableSurface2.i().c(new Runnable() { // from class: androidx.camera.camera2.internal.i2
                    @Override // java.lang.Runnable
                    public final void run() {
                        k2.p(DeferrableSurface.this);
                    }
                }, this.f1917c);
            }
            SessionConfig.f fVar = new SessionConfig.f();
            fVar.a(sessionConfig);
            fVar.d();
            fVar.a(this.f1923i);
            androidx.core.util.h.b(fVar.f(), "Cannot transform the SessionConfig");
            com.google.common.util.concurrent.d<Void> g8 = this.f1919e.g(fVar.c(), (CameraDevice) androidx.core.util.h.h(cameraDevice), z2Var);
            a0.f.b(g8, new a(), this.f1917c);
            return g8;
        } catch (DeferrableSurface.SurfaceClosedException e8) {
            return a0.f.f(e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Void r(Void r12) {
        s(this.f1919e);
        return null;
    }

    private void t(w.j jVar, w.j jVar2) {
        a.C0201a c0201a = new a.C0201a();
        c0201a.d(jVar);
        c0201a.d(jVar2);
        this.f1915a.b(c0201a.c());
    }

    @Override // androidx.camera.camera2.internal.t1
    public void a() {
        androidx.camera.core.p1.a("ProcessingCaptureSession", "cancelIssuedCaptureRequests (id=" + this.f1930p + ")");
        if (this.f1925k != null) {
            for (y.h hVar : this.f1925k.b()) {
                hVar.a();
            }
            this.f1925k = null;
        }
    }

    @Override // androidx.camera.camera2.internal.t1
    public com.google.common.util.concurrent.d<Void> b(boolean z4) {
        androidx.core.util.h.k(this.f1924j == d.CLOSED, "release() can only be called in CLOSED state");
        androidx.camera.core.p1.a("ProcessingCaptureSession", "release (id=" + this.f1930p + ")");
        return this.f1919e.b(z4);
    }

    @Override // androidx.camera.camera2.internal.t1
    public List<androidx.camera.core.impl.f> c() {
        return this.f1925k != null ? Arrays.asList(this.f1925k) : Collections.emptyList();
    }

    @Override // androidx.camera.camera2.internal.t1
    public void close() {
        androidx.camera.core.p1.a("ProcessingCaptureSession", "close (id=" + this.f1930p + ") state=" + this.f1924j);
        int i8 = c.f1934a[this.f1924j.ordinal()];
        if (i8 != 2) {
            if (i8 == 3) {
                this.f1915a.c();
                d1 d1Var = this.f1922h;
                if (d1Var != null) {
                    d1Var.a();
                }
                this.f1924j = d.ON_CAPTURE_SESSION_ENDED;
            } else if (i8 != 4) {
                if (i8 == 5) {
                    return;
                }
                this.f1924j = d.CLOSED;
                this.f1919e.close();
            }
        }
        this.f1915a.d();
        this.f1924j = d.CLOSED;
        this.f1919e.close();
    }

    @Override // androidx.camera.camera2.internal.t1
    public void d(List<androidx.camera.core.impl.f> list) {
        if (list.isEmpty()) {
            return;
        }
        if (list.size() > 1 || !n(list)) {
            l(list);
        } else if (this.f1925k != null || this.f1926l) {
            l(list);
        } else {
            androidx.camera.core.impl.f fVar = list.get(0);
            androidx.camera.core.p1.a("ProcessingCaptureSession", "issueCaptureRequests (id=" + this.f1930p + ") + state =" + this.f1924j);
            int i8 = c.f1934a[this.f1924j.ordinal()];
            if (i8 == 1 || i8 == 2) {
                this.f1925k = fVar;
            } else if (i8 != 3) {
                if (i8 == 4 || i8 == 5) {
                    androidx.camera.core.p1.a("ProcessingCaptureSession", "Run issueCaptureRequests in wrong state, state = " + this.f1924j);
                    l(list);
                }
            } else {
                this.f1926l = true;
                j.a e8 = j.a.e(fVar.d());
                Config d8 = fVar.d();
                Config.a<Integer> aVar = androidx.camera.core.impl.f.f2555h;
                if (d8.b(aVar)) {
                    e8.g(CaptureRequest.JPEG_ORIENTATION, (Integer) fVar.d().a(aVar));
                }
                Config d9 = fVar.d();
                Config.a<Integer> aVar2 = androidx.camera.core.impl.f.f2556i;
                if (d9.b(aVar2)) {
                    e8.g(CaptureRequest.JPEG_QUALITY, Byte.valueOf(((Integer) fVar.d().a(aVar2)).byteValue()));
                }
                w.j d10 = e8.d();
                this.f1929o = d10;
                t(this.f1928n, d10);
                this.f1915a.a(new b(fVar));
            }
        }
    }

    @Override // androidx.camera.camera2.internal.t1
    public SessionConfig e() {
        return this.f1921g;
    }

    @Override // androidx.camera.camera2.internal.t1
    public void f(SessionConfig sessionConfig) {
        androidx.camera.core.p1.a("ProcessingCaptureSession", "setSessionConfig (id=" + this.f1930p + ")");
        this.f1921g = sessionConfig;
        if (sessionConfig == null) {
            return;
        }
        d1 d1Var = this.f1922h;
        if (d1Var != null) {
            d1Var.b(sessionConfig);
        }
        if (this.f1924j == d.ON_CAPTURE_SESSION_STARTED) {
            w.j d8 = j.a.e(sessionConfig.d()).d();
            this.f1928n = d8;
            t(d8, this.f1929o);
            this.f1915a.g(this.f1927m);
        }
    }

    @Override // androidx.camera.camera2.internal.t1
    public com.google.common.util.concurrent.d<Void> g(final SessionConfig sessionConfig, final CameraDevice cameraDevice, final z2 z2Var) {
        boolean z4 = this.f1924j == d.UNINITIALIZED;
        androidx.core.util.h.b(z4, "Invalid state state:" + this.f1924j);
        androidx.core.util.h.b(sessionConfig.k().isEmpty() ^ true, "SessionConfig contains no surfaces");
        androidx.camera.core.p1.a("ProcessingCaptureSession", "open (id=" + this.f1930p + ")");
        List<DeferrableSurface> k8 = sessionConfig.k();
        this.f1920f = k8;
        return a0.d.a(androidx.camera.core.impl.h.k(k8, false, 5000L, this.f1917c, this.f1918d)).f(new a0.a() { // from class: androidx.camera.camera2.internal.g2
            @Override // a0.a
            public final com.google.common.util.concurrent.d apply(Object obj) {
                com.google.common.util.concurrent.d q8;
                q8 = k2.this.q(sessionConfig, cameraDevice, z2Var, (List) obj);
                return q8;
            }
        }, this.f1917c).e(new n.a() { // from class: androidx.camera.camera2.internal.j2
            @Override // n.a
            public final Object apply(Object obj) {
                Void r4;
                r4 = k2.this.r((Void) obj);
                return r4;
            }
        }, this.f1917c);
    }

    void s(s1 s1Var) {
        boolean z4 = this.f1924j == d.SESSION_INITIALIZED;
        androidx.core.util.h.b(z4, "Invalid state state:" + this.f1924j);
        d1 d1Var = new d1(s1Var, m(this.f1923i.k()));
        this.f1922h = d1Var;
        this.f1915a.e(d1Var);
        this.f1924j = d.ON_CAPTURE_SESSION_STARTED;
        SessionConfig sessionConfig = this.f1921g;
        if (sessionConfig != null) {
            f(sessionConfig);
        }
        if (this.f1925k != null) {
            List<androidx.camera.core.impl.f> asList = Arrays.asList(this.f1925k);
            this.f1925k = null;
            d(asList);
        }
    }
}
