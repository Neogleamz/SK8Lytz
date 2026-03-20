package androidx.camera.camera2.internal;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.camera.camera2.internal.g0;
import androidx.camera.camera2.internal.z2;
import androidx.camera.core.CameraState;
import androidx.camera.core.impl.CameraControlInternal;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.e;
import androidx.camera.core.impl.f;
import androidx.concurrent.futures.c;
import com.google.android.gms.dynamite.descriptors.com.google.mlkit.dynamite.barcode.ModuleDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g0 implements CameraInternal {
    private final v1 A;
    private final z2.a B;
    private final Set<String> C;
    private androidx.camera.core.impl.d E;
    final Object F;
    private y.v0 G;
    boolean H;
    private final x1 K;

    /* renamed from: a  reason: collision with root package name */
    private final androidx.camera.core.impl.u f1814a;

    /* renamed from: b  reason: collision with root package name */
    private final s.l0 f1815b;

    /* renamed from: c  reason: collision with root package name */
    private final Executor f1816c;

    /* renamed from: d  reason: collision with root package name */
    private final ScheduledExecutorService f1817d;

    /* renamed from: e  reason: collision with root package name */
    volatile f f1818e = f.INITIALIZED;

    /* renamed from: f  reason: collision with root package name */
    private final y.m0<CameraInternal.State> f1819f;

    /* renamed from: g  reason: collision with root package name */
    private final k1 f1820g;

    /* renamed from: h  reason: collision with root package name */
    private final t f1821h;

    /* renamed from: j  reason: collision with root package name */
    private final g f1822j;

    /* renamed from: k  reason: collision with root package name */
    final j0 f1823k;

    /* renamed from: l  reason: collision with root package name */
    CameraDevice f1824l;

    /* renamed from: m  reason: collision with root package name */
    int f1825m;

    /* renamed from: n  reason: collision with root package name */
    t1 f1826n;

    /* renamed from: p  reason: collision with root package name */
    final AtomicInteger f1827p;
    c.a<Void> q;

    /* renamed from: t  reason: collision with root package name */
    final Map<t1, com.google.common.util.concurrent.d<Void>> f1828t;

    /* renamed from: w  reason: collision with root package name */
    private final d f1829w;

    /* renamed from: x  reason: collision with root package name */
    private final androidx.camera.core.impl.e f1830x;

    /* renamed from: y  reason: collision with root package name */
    final Set<s1> f1831y;

    /* renamed from: z  reason: collision with root package name */
    private f2 f1832z;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements a0.c<Void> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ t1 f1833a;

        a(t1 t1Var) {
            this.f1833a = t1Var;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r22) {
            CameraDevice cameraDevice;
            g0.this.f1828t.remove(this.f1833a);
            int i8 = c.f1836a[g0.this.f1818e.ordinal()];
            if (i8 != 3) {
                if (i8 != 6) {
                    if (i8 != 7) {
                        return;
                    }
                } else if (g0.this.f1825m == 0) {
                    return;
                }
            }
            if (!g0.this.M() || (cameraDevice = g0.this.f1824l) == null) {
                return;
            }
            s.a.a(cameraDevice);
            g0.this.f1824l = null;
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements a0.c<Void> {
        b() {
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r12) {
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            if (th instanceof DeferrableSurface.SurfaceClosedException) {
                SessionConfig H = g0.this.H(((DeferrableSurface.SurfaceClosedException) th).a());
                if (H != null) {
                    g0.this.d0(H);
                }
            } else if (th instanceof CancellationException) {
                g0.this.F("Unable to configure camera cancelled");
            } else {
                f fVar = g0.this.f1818e;
                f fVar2 = f.OPENED;
                if (fVar == fVar2) {
                    g0.this.j0(fVar2, CameraState.a.b(4, th));
                }
                if (th instanceof CameraAccessException) {
                    g0 g0Var = g0.this;
                    g0Var.F("Unable to configure camera due to " + th.getMessage());
                } else if (th instanceof TimeoutException) {
                    androidx.camera.core.p1.c("Camera2CameraImpl", "Unable to configure camera " + g0.this.f1823k.c() + ", timeout!");
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class c {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f1836a;

        static {
            int[] iArr = new int[f.values().length];
            f1836a = iArr;
            try {
                iArr[f.INITIALIZED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f1836a[f.PENDING_OPEN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f1836a[f.CLOSING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f1836a[f.OPENED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f1836a[f.OPENING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f1836a[f.REOPENING.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f1836a[f.RELEASING.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f1836a[f.RELEASED.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class d extends CameraManager.AvailabilityCallback implements e.b {

        /* renamed from: a  reason: collision with root package name */
        private final String f1837a;

        /* renamed from: b  reason: collision with root package name */
        private boolean f1838b = true;

        d(String str) {
            this.f1837a = str;
        }

        @Override // androidx.camera.core.impl.e.b
        public void a() {
            if (g0.this.f1818e == f.PENDING_OPEN) {
                g0.this.q0(false);
            }
        }

        boolean b() {
            return this.f1838b;
        }

        @Override // android.hardware.camera2.CameraManager.AvailabilityCallback
        public void onCameraAvailable(String str) {
            if (this.f1837a.equals(str)) {
                this.f1838b = true;
                if (g0.this.f1818e == f.PENDING_OPEN) {
                    g0.this.q0(false);
                }
            }
        }

        @Override // android.hardware.camera2.CameraManager.AvailabilityCallback
        public void onCameraUnavailable(String str) {
            if (this.f1837a.equals(str)) {
                this.f1838b = false;
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    final class e implements CameraControlInternal.b {
        e() {
        }

        @Override // androidx.camera.core.impl.CameraControlInternal.b
        public void a() {
            g0.this.r0();
        }

        @Override // androidx.camera.core.impl.CameraControlInternal.b
        public void b(List<androidx.camera.core.impl.f> list) {
            g0.this.l0((List) androidx.core.util.h.h(list));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum f {
        INITIALIZED,
        PENDING_OPEN,
        OPENING,
        OPENED,
        CLOSING,
        REOPENING,
        RELEASING,
        RELEASED
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class g extends CameraDevice.StateCallback {

        /* renamed from: a  reason: collision with root package name */
        private final Executor f1850a;

        /* renamed from: b  reason: collision with root package name */
        private final ScheduledExecutorService f1851b;

        /* renamed from: c  reason: collision with root package name */
        private b f1852c;

        /* renamed from: d  reason: collision with root package name */
        ScheduledFuture<?> f1853d;

        /* renamed from: e  reason: collision with root package name */
        private final a f1854e = new a();

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a {

            /* renamed from: a  reason: collision with root package name */
            private long f1856a = -1;

            a() {
            }

            boolean a() {
                if (b() >= ((long) d())) {
                    e();
                    return false;
                }
                return true;
            }

            long b() {
                long uptimeMillis = SystemClock.uptimeMillis();
                if (this.f1856a == -1) {
                    this.f1856a = uptimeMillis;
                }
                return uptimeMillis - this.f1856a;
            }

            int c() {
                if (g.this.f()) {
                    long b9 = b();
                    if (b9 <= 120000) {
                        return 1000;
                    }
                    return b9 <= 300000 ? 2000 : 4000;
                }
                return 700;
            }

            int d() {
                if (g.this.f()) {
                    return 1800000;
                }
                return ModuleDescriptor.MODULE_VERSION;
            }

            void e() {
                this.f1856a = -1L;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class b implements Runnable {

            /* renamed from: a  reason: collision with root package name */
            private Executor f1858a;

            /* renamed from: b  reason: collision with root package name */
            private boolean f1859b = false;

            b(Executor executor) {
                this.f1858a = executor;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void c() {
                if (this.f1859b) {
                    return;
                }
                androidx.core.util.h.j(g0.this.f1818e == f.REOPENING);
                if (g.this.f()) {
                    g0.this.p0(true);
                } else {
                    g0.this.q0(true);
                }
            }

            void b() {
                this.f1859b = true;
            }

            @Override // java.lang.Runnable
            public void run() {
                this.f1858a.execute(new Runnable() { // from class: androidx.camera.camera2.internal.h0
                    @Override // java.lang.Runnable
                    public final void run() {
                        g0.g.b.this.c();
                    }
                });
            }
        }

        g(Executor executor, ScheduledExecutorService scheduledExecutorService) {
            this.f1850a = executor;
            this.f1851b = scheduledExecutorService;
        }

        private void b(CameraDevice cameraDevice, int i8) {
            boolean z4 = g0.this.f1818e == f.OPENING || g0.this.f1818e == f.OPENED || g0.this.f1818e == f.REOPENING;
            androidx.core.util.h.k(z4, "Attempt to handle open error from non open state: " + g0.this.f1818e);
            if (i8 == 1 || i8 == 2 || i8 == 4) {
                androidx.camera.core.p1.a("Camera2CameraImpl", String.format("Attempt to reopen camera[%s] after error[%s]", cameraDevice.getId(), g0.J(i8)));
                c(i8);
                return;
            }
            androidx.camera.core.p1.c("Camera2CameraImpl", "Error observed on open (or opening) camera device " + cameraDevice.getId() + ": " + g0.J(i8) + " closing camera.");
            g0.this.j0(f.CLOSING, CameraState.a.a(i8 == 3 ? 5 : 6));
            g0.this.B(false);
        }

        private void c(int i8) {
            int i9 = 1;
            androidx.core.util.h.k(g0.this.f1825m != 0, "Can only reopen camera device after error if the camera device is actually in an error state.");
            if (i8 == 1) {
                i9 = 2;
            } else if (i8 != 2) {
                i9 = 3;
            }
            g0.this.j0(f.REOPENING, CameraState.a.a(i9));
            g0.this.B(false);
        }

        boolean a() {
            if (this.f1853d != null) {
                g0 g0Var = g0.this;
                g0Var.F("Cancelling scheduled re-open: " + this.f1852c);
                this.f1852c.b();
                this.f1852c = null;
                this.f1853d.cancel(false);
                this.f1853d = null;
                return true;
            }
            return false;
        }

        void d() {
            this.f1854e.e();
        }

        void e() {
            androidx.core.util.h.j(this.f1852c == null);
            androidx.core.util.h.j(this.f1853d == null);
            if (!this.f1854e.a()) {
                androidx.camera.core.p1.c("Camera2CameraImpl", "Camera reopening attempted for " + this.f1854e.d() + "ms without success.");
                g0.this.k0(f.PENDING_OPEN, null, false);
                return;
            }
            this.f1852c = new b(this.f1850a);
            g0 g0Var = g0.this;
            g0Var.F("Attempting camera re-open in " + this.f1854e.c() + "ms: " + this.f1852c + " activeResuming = " + g0.this.H);
            this.f1853d = this.f1851b.schedule(this.f1852c, (long) this.f1854e.c(), TimeUnit.MILLISECONDS);
        }

        boolean f() {
            int i8;
            g0 g0Var = g0.this;
            return g0Var.H && ((i8 = g0Var.f1825m) == 1 || i8 == 2);
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onClosed(CameraDevice cameraDevice) {
            g0.this.F("CameraDevice.onClosed()");
            boolean z4 = g0.this.f1824l == null;
            androidx.core.util.h.k(z4, "Unexpected onClose callback on camera device: " + cameraDevice);
            int i8 = c.f1836a[g0.this.f1818e.ordinal()];
            if (i8 != 3) {
                if (i8 == 6) {
                    g0 g0Var = g0.this;
                    if (g0Var.f1825m == 0) {
                        g0Var.q0(false);
                        return;
                    }
                    g0Var.F("Camera closed due to error: " + g0.J(g0.this.f1825m));
                    e();
                    return;
                } else if (i8 != 7) {
                    throw new IllegalStateException("Camera closed while in state: " + g0.this.f1818e);
                }
            }
            androidx.core.util.h.j(g0.this.M());
            g0.this.I();
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onDisconnected(CameraDevice cameraDevice) {
            g0.this.F("CameraDevice.onDisconnected()");
            onError(cameraDevice, 1);
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onError(CameraDevice cameraDevice, int i8) {
            g0 g0Var = g0.this;
            g0Var.f1824l = cameraDevice;
            g0Var.f1825m = i8;
            int i9 = c.f1836a[g0Var.f1818e.ordinal()];
            if (i9 != 3) {
                if (i9 == 4 || i9 == 5 || i9 == 6) {
                    androidx.camera.core.p1.a("Camera2CameraImpl", String.format("CameraDevice.onError(): %s failed with %s while in %s state. Will attempt recovering from error.", cameraDevice.getId(), g0.J(i8), g0.this.f1818e.name()));
                    b(cameraDevice, i8);
                    return;
                } else if (i9 != 7) {
                    throw new IllegalStateException("onError() should not be possible from state: " + g0.this.f1818e);
                }
            }
            androidx.camera.core.p1.c("Camera2CameraImpl", String.format("CameraDevice.onError(): %s failed with %s while in %s state. Will finish closing camera.", cameraDevice.getId(), g0.J(i8), g0.this.f1818e.name()));
            g0.this.B(false);
        }

        @Override // android.hardware.camera2.CameraDevice.StateCallback
        public void onOpened(CameraDevice cameraDevice) {
            g0.this.F("CameraDevice.onOpened()");
            g0 g0Var = g0.this;
            g0Var.f1824l = cameraDevice;
            g0Var.f1825m = 0;
            d();
            int i8 = c.f1836a[g0.this.f1818e.ordinal()];
            if (i8 != 3) {
                if (i8 == 5 || i8 == 6) {
                    g0.this.i0(f.OPENED);
                    g0.this.b0();
                    return;
                } else if (i8 != 7) {
                    throw new IllegalStateException("onOpened() should not be possible from state: " + g0.this.f1818e);
                }
            }
            androidx.core.util.h.j(g0.this.M());
            g0.this.f1824l.close();
            g0.this.f1824l = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class h {
        static h a(String str, Class<?> cls, SessionConfig sessionConfig, androidx.camera.core.impl.v<?> vVar, Size size) {
            return new androidx.camera.camera2.internal.b(str, cls, sessionConfig, vVar, size);
        }

        static h b(androidx.camera.core.a3 a3Var) {
            return a(g0.K(a3Var), a3Var.getClass(), a3Var.n(), a3Var.g(), a3Var.c());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract SessionConfig c();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Size d();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract androidx.camera.core.impl.v<?> e();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract String f();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Class<?> g();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g0(s.l0 l0Var, String str, j0 j0Var, androidx.camera.core.impl.e eVar, Executor executor, Handler handler, x1 x1Var) {
        y.m0<CameraInternal.State> m0Var = new y.m0<>();
        this.f1819f = m0Var;
        this.f1825m = 0;
        this.f1827p = new AtomicInteger(0);
        this.f1828t = new LinkedHashMap();
        this.f1831y = new HashSet();
        this.C = new HashSet();
        this.E = y.n.a();
        this.F = new Object();
        this.H = false;
        this.f1815b = l0Var;
        this.f1830x = eVar;
        ScheduledExecutorService e8 = z.a.e(handler);
        this.f1817d = e8;
        Executor f5 = z.a.f(executor);
        this.f1816c = f5;
        this.f1822j = new g(f5, e8);
        this.f1814a = new androidx.camera.core.impl.u(str);
        m0Var.g(CameraInternal.State.CLOSED);
        k1 k1Var = new k1(eVar);
        this.f1820g = k1Var;
        v1 v1Var = new v1(f5);
        this.A = v1Var;
        this.K = x1Var;
        this.f1826n = X();
        try {
            t tVar = new t(l0Var.c(str), e8, f5, new e(), j0Var.j());
            this.f1821h = tVar;
            this.f1823k = j0Var;
            j0Var.o(tVar);
            j0Var.r(k1Var.a());
            this.B = new z2.a(f5, e8, handler, v1Var, j0Var.j(), u.l.b());
            d dVar = new d(str);
            this.f1829w = dVar;
            eVar.e(this, f5, dVar);
            l0Var.f(f5, dVar);
        } catch (CameraAccessExceptionCompat e9) {
            throw l1.a(e9);
        }
    }

    private boolean A(f.a aVar) {
        String str;
        if (aVar.l().isEmpty()) {
            for (SessionConfig sessionConfig : this.f1814a.e()) {
                List<DeferrableSurface> e8 = sessionConfig.h().e();
                if (!e8.isEmpty()) {
                    for (DeferrableSurface deferrableSurface : e8) {
                        aVar.f(deferrableSurface);
                    }
                }
            }
            if (!aVar.l().isEmpty()) {
                return true;
            }
            str = "Unable to find a repeating surface to attach to CaptureConfig";
        } else {
            str = "The capture config builder already has surface inside.";
        }
        androidx.camera.core.p1.k("Camera2CameraImpl", str);
        return false;
    }

    private void C() {
        F("Closing camera.");
        int i8 = c.f1836a[this.f1818e.ordinal()];
        if (i8 == 2) {
            androidx.core.util.h.j(this.f1824l == null);
            i0(f.INITIALIZED);
        } else if (i8 == 4) {
            i0(f.CLOSING);
            B(false);
        } else if (i8 != 5 && i8 != 6) {
            F("close() ignored due to being in state: " + this.f1818e);
        } else {
            boolean a9 = this.f1822j.a();
            i0(f.CLOSING);
            if (a9) {
                androidx.core.util.h.j(M());
                I();
            }
        }
    }

    private void D(boolean z4) {
        final s1 s1Var = new s1();
        this.f1831y.add(s1Var);
        h0(z4);
        final SurfaceTexture surfaceTexture = new SurfaceTexture(0);
        surfaceTexture.setDefaultBufferSize(640, 480);
        final Surface surface = new Surface(surfaceTexture);
        final Runnable runnable = new Runnable() { // from class: androidx.camera.camera2.internal.w
            @Override // java.lang.Runnable
            public final void run() {
                g0.O(surface, surfaceTexture);
            }
        };
        SessionConfig.b bVar = new SessionConfig.b();
        final y.h0 h0Var = new y.h0(surface);
        bVar.h(h0Var);
        bVar.s(1);
        F("Start configAndClose.");
        s1Var.g(bVar.m(), (CameraDevice) androidx.core.util.h.h(this.f1824l), this.B.a()).c(new Runnable() { // from class: androidx.camera.camera2.internal.x
            @Override // java.lang.Runnable
            public final void run() {
                g0.this.P(s1Var, h0Var, runnable);
            }
        }, this.f1816c);
    }

    private CameraDevice.StateCallback E() {
        ArrayList arrayList = new ArrayList(this.f1814a.f().c().b());
        arrayList.add(this.A.c());
        arrayList.add(this.f1822j);
        return i1.a(arrayList);
    }

    private void G(String str, Throwable th) {
        androidx.camera.core.p1.b("Camera2CameraImpl", String.format("{%s} %s", toString(), str), th);
    }

    static String J(int i8) {
        return i8 != 0 ? i8 != 1 ? i8 != 2 ? i8 != 3 ? i8 != 4 ? i8 != 5 ? "UNKNOWN ERROR" : "ERROR_CAMERA_SERVICE" : "ERROR_CAMERA_DEVICE" : "ERROR_CAMERA_DISABLED" : "ERROR_MAX_CAMERAS_IN_USE" : "ERROR_CAMERA_IN_USE" : "ERROR_NONE";
    }

    static String K(androidx.camera.core.a3 a3Var) {
        return a3Var.j() + a3Var.hashCode();
    }

    private boolean L() {
        return ((j0) m()).n() == 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void N(List list) {
        try {
            n0(list);
        } finally {
            this.f1821h.w();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void O(Surface surface, SurfaceTexture surfaceTexture) {
        surface.release();
        surfaceTexture.release();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void R(String str, SessionConfig sessionConfig, androidx.camera.core.impl.v vVar) {
        F("Use case " + str + " ACTIVE");
        this.f1814a.q(str, sessionConfig, vVar);
        this.f1814a.u(str, sessionConfig, vVar);
        r0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void S(String str) {
        F("Use case " + str + " INACTIVE");
        this.f1814a.t(str);
        r0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void T(String str, SessionConfig sessionConfig, androidx.camera.core.impl.v vVar) {
        F("Use case " + str + " RESET");
        this.f1814a.u(str, sessionConfig, vVar);
        z();
        h0(false);
        r0();
        if (this.f1818e == f.OPENED) {
            b0();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void U(String str, SessionConfig sessionConfig, androidx.camera.core.impl.v vVar) {
        F("Use case " + str + " UPDATED");
        this.f1814a.u(str, sessionConfig, vVar);
        r0();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void V(SessionConfig.c cVar, SessionConfig sessionConfig) {
        cVar.a(sessionConfig, SessionConfig.SessionError.SESSION_ERROR_SURFACE_NEEDS_RESET);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void W(boolean z4) {
        this.H = z4;
        if (z4 && this.f1818e == f.PENDING_OPEN) {
            p0(false);
        }
    }

    private t1 X() {
        synchronized (this.F) {
            if (this.G == null) {
                return new s1();
            }
            return new k2(this.G, this.f1823k, this.f1816c, this.f1817d);
        }
    }

    private void Y(List<androidx.camera.core.a3> list) {
        for (androidx.camera.core.a3 a3Var : list) {
            String K = K(a3Var);
            if (!this.C.contains(K)) {
                this.C.add(K);
                a3Var.E();
            }
        }
    }

    private void Z(List<androidx.camera.core.a3> list) {
        for (androidx.camera.core.a3 a3Var : list) {
            String K = K(a3Var);
            if (this.C.contains(K)) {
                a3Var.F();
                this.C.remove(K);
            }
        }
    }

    @SuppressLint({"MissingPermission"})
    private void a0(boolean z4) {
        if (!z4) {
            this.f1822j.d();
        }
        this.f1822j.a();
        F("Opening camera.");
        i0(f.OPENING);
        try {
            this.f1815b.e(this.f1823k.c(), this.f1816c, E());
        } catch (CameraAccessExceptionCompat e8) {
            F("Unable to open camera due to " + e8.getMessage());
            if (e8.d() != 10001) {
                return;
            }
            j0(f.INITIALIZED, CameraState.a.b(7, e8));
        } catch (SecurityException e9) {
            F("Unable to open camera due to " + e9.getMessage());
            i0(f.REOPENING);
            this.f1822j.e();
        }
    }

    private void c0() {
        int i8 = c.f1836a[this.f1818e.ordinal()];
        if (i8 == 1 || i8 == 2) {
            p0(false);
        } else if (i8 != 3) {
            F("open() ignored due to being in state: " + this.f1818e);
        } else {
            i0(f.REOPENING);
            if (M() || this.f1825m != 0) {
                return;
            }
            androidx.core.util.h.k(this.f1824l != null, "Camera Device should be open if session close is not complete");
            i0(f.OPENED);
            b0();
        }
    }

    private void g0() {
        if (this.f1832z != null) {
            androidx.camera.core.impl.u uVar = this.f1814a;
            uVar.s(this.f1832z.c() + this.f1832z.hashCode());
            androidx.camera.core.impl.u uVar2 = this.f1814a;
            uVar2.t(this.f1832z.c() + this.f1832z.hashCode());
            this.f1832z.b();
            this.f1832z = null;
        }
    }

    private Collection<h> m0(Collection<androidx.camera.core.a3> collection) {
        ArrayList arrayList = new ArrayList();
        for (androidx.camera.core.a3 a3Var : collection) {
            arrayList.add(h.b(a3Var));
        }
        return arrayList;
    }

    private void n0(Collection<h> collection) {
        Size d8;
        boolean isEmpty = this.f1814a.g().isEmpty();
        ArrayList arrayList = new ArrayList();
        Rational rational = null;
        for (h hVar : collection) {
            if (!this.f1814a.l(hVar.f())) {
                this.f1814a.r(hVar.f(), hVar.c(), hVar.e());
                arrayList.add(hVar.f());
                if (hVar.g() == androidx.camera.core.y1.class && (d8 = hVar.d()) != null) {
                    rational = new Rational(d8.getWidth(), d8.getHeight());
                }
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        F("Use cases [" + TextUtils.join(", ", arrayList) + "] now ATTACHED");
        if (isEmpty) {
            this.f1821h.b0(true);
            this.f1821h.J();
        }
        z();
        s0();
        r0();
        h0(false);
        if (this.f1818e == f.OPENED) {
            b0();
        } else {
            c0();
        }
        if (rational != null) {
            this.f1821h.c0(rational);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: o0 */
    public void Q(Collection<h> collection) {
        ArrayList arrayList = new ArrayList();
        boolean z4 = false;
        for (h hVar : collection) {
            if (this.f1814a.l(hVar.f())) {
                this.f1814a.p(hVar.f());
                arrayList.add(hVar.f());
                if (hVar.g() == androidx.camera.core.y1.class) {
                    z4 = true;
                }
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        F("Use cases [" + TextUtils.join(", ", arrayList) + "] now DETACHED for camera");
        if (z4) {
            this.f1821h.c0(null);
        }
        z();
        if (this.f1814a.h().isEmpty()) {
            this.f1821h.e0(false);
        } else {
            s0();
        }
        if (this.f1814a.g().isEmpty()) {
            this.f1821h.w();
            h0(false);
            this.f1821h.b0(false);
            this.f1826n = X();
            C();
            return;
        }
        r0();
        h0(false);
        if (this.f1818e == f.OPENED) {
            b0();
        }
    }

    private void s0() {
        boolean z4 = false;
        for (androidx.camera.core.impl.v<?> vVar : this.f1814a.h()) {
            z4 |= vVar.r(false);
        }
        this.f1821h.e0(z4);
    }

    private void y() {
        if (this.f1832z != null) {
            androidx.camera.core.impl.u uVar = this.f1814a;
            uVar.r(this.f1832z.c() + this.f1832z.hashCode(), this.f1832z.e(), this.f1832z.f());
            androidx.camera.core.impl.u uVar2 = this.f1814a;
            uVar2.q(this.f1832z.c() + this.f1832z.hashCode(), this.f1832z.e(), this.f1832z.f());
        }
    }

    private void z() {
        SessionConfig c9 = this.f1814a.f().c();
        androidx.camera.core.impl.f h8 = c9.h();
        int size = h8.e().size();
        int size2 = c9.k().size();
        if (c9.k().isEmpty()) {
            return;
        }
        if (h8.e().isEmpty()) {
            if (this.f1832z == null) {
                this.f1832z = new f2(this.f1823k.l(), this.K);
            }
            y();
        } else if ((size2 == 1 && size == 1) || size >= 2) {
            g0();
        } else {
            androidx.camera.core.p1.a("Camera2CameraImpl", "mMeteringRepeating is ATTACHED, SessionConfig Surfaces: " + size2 + ", CaptureConfig Surfaces: " + size);
        }
    }

    void B(boolean z4) {
        boolean z8 = this.f1818e == f.CLOSING || this.f1818e == f.RELEASING || (this.f1818e == f.REOPENING && this.f1825m != 0);
        androidx.core.util.h.k(z8, "closeCamera should only be called in a CLOSING, RELEASING or REOPENING (with error) state. Current state: " + this.f1818e + " (error: " + J(this.f1825m) + ")");
        int i8 = Build.VERSION.SDK_INT;
        if (i8 <= 23 || i8 >= 29 || !L() || this.f1825m != 0) {
            h0(z4);
        } else {
            D(z4);
        }
        this.f1826n.a();
    }

    void F(String str) {
        G(str, null);
    }

    SessionConfig H(DeferrableSurface deferrableSurface) {
        for (SessionConfig sessionConfig : this.f1814a.g()) {
            if (sessionConfig.k().contains(deferrableSurface)) {
                return sessionConfig;
            }
        }
        return null;
    }

    void I() {
        androidx.core.util.h.j(this.f1818e == f.RELEASING || this.f1818e == f.CLOSING);
        androidx.core.util.h.j(this.f1828t.isEmpty());
        this.f1824l = null;
        if (this.f1818e == f.CLOSING) {
            i0(f.INITIALIZED);
            return;
        }
        this.f1815b.g(this.f1829w);
        i0(f.RELEASED);
        c.a<Void> aVar = this.q;
        if (aVar != null) {
            aVar.c(null);
            this.q = null;
        }
    }

    boolean M() {
        return this.f1828t.isEmpty() && this.f1831y.isEmpty();
    }

    void b0() {
        androidx.core.util.h.j(this.f1818e == f.OPENED);
        SessionConfig.f f5 = this.f1814a.f();
        if (!f5.f()) {
            F("Unable to create capture session due to conflicting configurations");
            return;
        }
        Config d8 = f5.c().d();
        Config.a<Long> aVar = r.a.C;
        if (!d8.b(aVar)) {
            f5.b(aVar, Long.valueOf(l2.a(this.f1814a.h(), this.f1814a.g())));
        }
        a0.f.b(this.f1826n.g(f5.c(), (CameraDevice) androidx.core.util.h.h(this.f1824l), this.B.a()), new b(), this.f1816c);
    }

    @Override // androidx.camera.core.a3.d
    public void c(androidx.camera.core.a3 a3Var) {
        androidx.core.util.h.h(a3Var);
        final String K = K(a3Var);
        final SessionConfig n8 = a3Var.n();
        final androidx.camera.core.impl.v<?> g8 = a3Var.g();
        this.f1816c.execute(new Runnable() { // from class: androidx.camera.camera2.internal.b0
            @Override // java.lang.Runnable
            public final void run() {
                g0.this.R(K, n8, g8);
            }
        });
    }

    @Override // androidx.camera.core.a3.d
    public void d(androidx.camera.core.a3 a3Var) {
        androidx.core.util.h.h(a3Var);
        final String K = K(a3Var);
        final SessionConfig n8 = a3Var.n();
        final androidx.camera.core.impl.v<?> g8 = a3Var.g();
        this.f1816c.execute(new Runnable() { // from class: androidx.camera.camera2.internal.a0
            @Override // java.lang.Runnable
            public final void run() {
                g0.this.T(K, n8, g8);
            }
        });
    }

    void d0(final SessionConfig sessionConfig) {
        ScheduledExecutorService d8 = z.a.d();
        List<SessionConfig.c> c9 = sessionConfig.c();
        if (c9.isEmpty()) {
            return;
        }
        final SessionConfig.c cVar = c9.get(0);
        G("Posting surface closed", new Throwable());
        d8.execute(new Runnable() { // from class: androidx.camera.camera2.internal.f0
            @Override // java.lang.Runnable
            public final void run() {
                g0.V(SessionConfig.c.this, sessionConfig);
            }
        });
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public void e(androidx.camera.core.impl.d dVar) {
        if (dVar == null) {
            dVar = y.n.a();
        }
        y.v0 y8 = dVar.y(null);
        this.E = dVar;
        synchronized (this.F) {
            this.G = y8;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: e0 */
    public void P(s1 s1Var, DeferrableSurface deferrableSurface, Runnable runnable) {
        this.f1831y.remove(s1Var);
        com.google.common.util.concurrent.d<Void> f02 = f0(s1Var, false);
        deferrableSurface.c();
        a0.f.n(Arrays.asList(f02, deferrableSurface.i())).c(runnable, z.a.a());
    }

    @Override // androidx.camera.core.a3.d
    public void f(androidx.camera.core.a3 a3Var) {
        androidx.core.util.h.h(a3Var);
        final String K = K(a3Var);
        final SessionConfig n8 = a3Var.n();
        final androidx.camera.core.impl.v<?> g8 = a3Var.g();
        this.f1816c.execute(new Runnable() { // from class: androidx.camera.camera2.internal.z
            @Override // java.lang.Runnable
            public final void run() {
                g0.this.U(K, n8, g8);
            }
        });
    }

    com.google.common.util.concurrent.d<Void> f0(t1 t1Var, boolean z4) {
        t1Var.close();
        com.google.common.util.concurrent.d<Void> b9 = t1Var.b(z4);
        F("Releasing session in state " + this.f1818e.name());
        this.f1828t.put(t1Var, b9);
        a0.f.b(b9, new a(t1Var), z.a.a());
        return b9;
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public y.p0<CameraInternal.State> g() {
        return this.f1819f;
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public CameraControlInternal h() {
        return this.f1821h;
    }

    void h0(boolean z4) {
        androidx.core.util.h.j(this.f1826n != null);
        F("Resetting Capture Session");
        t1 t1Var = this.f1826n;
        SessionConfig e8 = t1Var.e();
        List<androidx.camera.core.impl.f> c9 = t1Var.c();
        t1 X = X();
        this.f1826n = X;
        X.f(e8);
        this.f1826n.d(c9);
        f0(t1Var, z4);
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public androidx.camera.core.impl.d i() {
        return this.E;
    }

    void i0(f fVar) {
        j0(fVar, null);
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public void j(final boolean z4) {
        this.f1816c.execute(new Runnable() { // from class: androidx.camera.camera2.internal.e0
            @Override // java.lang.Runnable
            public final void run() {
                g0.this.W(z4);
            }
        });
    }

    void j0(f fVar, CameraState.a aVar) {
        k0(fVar, aVar, true);
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public void k(Collection<androidx.camera.core.a3> collection) {
        ArrayList arrayList = new ArrayList(collection);
        if (arrayList.isEmpty()) {
            return;
        }
        this.f1821h.J();
        Y(new ArrayList(arrayList));
        final ArrayList arrayList2 = new ArrayList(m0(arrayList));
        try {
            this.f1816c.execute(new Runnable() { // from class: androidx.camera.camera2.internal.c0
                @Override // java.lang.Runnable
                public final void run() {
                    g0.this.N(arrayList2);
                }
            });
        } catch (RejectedExecutionException e8) {
            G("Unable to attach use cases.", e8);
            this.f1821h.w();
        }
    }

    void k0(f fVar, CameraState.a aVar, boolean z4) {
        CameraInternal.State state;
        F("Transitioning camera internal state: " + this.f1818e + " --> " + fVar);
        this.f1818e = fVar;
        switch (c.f1836a[fVar.ordinal()]) {
            case 1:
                state = CameraInternal.State.CLOSED;
                break;
            case 2:
                state = CameraInternal.State.PENDING_OPEN;
                break;
            case 3:
                state = CameraInternal.State.CLOSING;
                break;
            case 4:
                state = CameraInternal.State.OPEN;
                break;
            case 5:
            case 6:
                state = CameraInternal.State.OPENING;
                break;
            case 7:
                state = CameraInternal.State.RELEASING;
                break;
            case 8:
                state = CameraInternal.State.RELEASED;
                break;
            default:
                throw new IllegalStateException("Unknown state: " + fVar);
        }
        this.f1830x.c(this, state, z4);
        this.f1819f.g(state);
        this.f1820g.c(state, aVar);
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public void l(Collection<androidx.camera.core.a3> collection) {
        ArrayList arrayList = new ArrayList(collection);
        if (arrayList.isEmpty()) {
            return;
        }
        final ArrayList arrayList2 = new ArrayList(m0(arrayList));
        Z(new ArrayList(arrayList));
        this.f1816c.execute(new Runnable() { // from class: androidx.camera.camera2.internal.d0
            @Override // java.lang.Runnable
            public final void run() {
                g0.this.Q(arrayList2);
            }
        });
    }

    void l0(List<androidx.camera.core.impl.f> list) {
        ArrayList arrayList = new ArrayList();
        for (androidx.camera.core.impl.f fVar : list) {
            f.a k8 = f.a.k(fVar);
            if (fVar.g() == 5 && fVar.c() != null) {
                k8.n(fVar.c());
            }
            if (!fVar.e().isEmpty() || !fVar.h() || A(k8)) {
                arrayList.add(k8.h());
            }
        }
        F("Issue capture request");
        this.f1826n.d(arrayList);
    }

    @Override // androidx.camera.core.impl.CameraInternal
    public y.q m() {
        return this.f1823k;
    }

    @Override // androidx.camera.core.a3.d
    public void n(androidx.camera.core.a3 a3Var) {
        androidx.core.util.h.h(a3Var);
        final String K = K(a3Var);
        this.f1816c.execute(new Runnable() { // from class: androidx.camera.camera2.internal.y
            @Override // java.lang.Runnable
            public final void run() {
                g0.this.S(K);
            }
        });
    }

    void p0(boolean z4) {
        F("Attempting to force open the camera.");
        if (this.f1830x.f(this)) {
            a0(z4);
            return;
        }
        F("No cameras available. Waiting for available camera before opening camera.");
        i0(f.PENDING_OPEN);
    }

    void q0(boolean z4) {
        F("Attempting to open the camera.");
        if (this.f1829w.b() && this.f1830x.f(this)) {
            a0(z4);
            return;
        }
        F("No cameras available. Waiting for available camera before opening camera.");
        i0(f.PENDING_OPEN);
    }

    void r0() {
        SessionConfig.f d8 = this.f1814a.d();
        if (!d8.f()) {
            this.f1821h.a0();
            this.f1826n.f(this.f1821h.A());
            return;
        }
        this.f1821h.d0(d8.c().l());
        d8.a(this.f1821h.A());
        this.f1826n.f(d8.c());
    }

    public String toString() {
        return String.format(Locale.US, "Camera@%x[id=%s]", Integer.valueOf(hashCode()), this.f1823k.c());
    }
}
