package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.view.Surface;
import androidx.camera.camera2.internal.a3;
import androidx.camera.camera2.internal.g1;
import androidx.camera.camera2.internal.n2;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.f;
import androidx.concurrent.futures.c;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CancellationException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s1 implements t1 {

    /* renamed from: e  reason: collision with root package name */
    z2 f2058e;

    /* renamed from: f  reason: collision with root package name */
    n2 f2059f;

    /* renamed from: g  reason: collision with root package name */
    SessionConfig f2060g;

    /* renamed from: l  reason: collision with root package name */
    e f2065l;

    /* renamed from: m  reason: collision with root package name */
    com.google.common.util.concurrent.d<Void> f2066m;

    /* renamed from: n  reason: collision with root package name */
    c.a<Void> f2067n;

    /* renamed from: a  reason: collision with root package name */
    final Object f2054a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private final List<androidx.camera.core.impl.f> f2055b = new ArrayList();

    /* renamed from: c  reason: collision with root package name */
    private final CameraCaptureSession.CaptureCallback f2056c = new a();

    /* renamed from: h  reason: collision with root package name */
    Config f2061h = androidx.camera.core.impl.o.M();

    /* renamed from: i  reason: collision with root package name */
    r.c f2062i = r.c.e();

    /* renamed from: j  reason: collision with root package name */
    private final Map<DeferrableSurface, Surface> f2063j = new HashMap();

    /* renamed from: k  reason: collision with root package name */
    List<DeferrableSurface> f2064k = Collections.emptyList();

    /* renamed from: o  reason: collision with root package name */
    final v.o f2068o = new v.o();

    /* renamed from: p  reason: collision with root package name */
    final v.r f2069p = new v.r();

    /* renamed from: d  reason: collision with root package name */
    private final f f2057d = new f();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends CameraCaptureSession.CaptureCallback {
        a() {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureCompleted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, TotalCaptureResult totalCaptureResult) {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements a0.c<Void> {
        b() {
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r12) {
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            synchronized (s1.this.f2054a) {
                s1.this.f2058e.e();
                int i8 = d.f2073a[s1.this.f2065l.ordinal()];
                if ((i8 == 4 || i8 == 6 || i8 == 7) && !(th instanceof CancellationException)) {
                    androidx.camera.core.p1.l("CaptureSession", "Opening session with fail " + s1.this.f2065l, th);
                    s1.this.l();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends CameraCaptureSession.CaptureCallback {
        c() {
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureCompleted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, TotalCaptureResult totalCaptureResult) {
            synchronized (s1.this.f2054a) {
                SessionConfig sessionConfig = s1.this.f2060g;
                if (sessionConfig == null) {
                    return;
                }
                androidx.camera.core.impl.f h8 = sessionConfig.h();
                androidx.camera.core.p1.a("CaptureSession", "Submit FLASH_MODE_OFF request");
                s1 s1Var = s1.this;
                s1Var.d(Collections.singletonList(s1Var.f2069p.a(h8)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class d {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f2073a;

        static {
            int[] iArr = new int[e.values().length];
            f2073a = iArr;
            try {
                iArr[e.UNINITIALIZED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f2073a[e.INITIALIZED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f2073a[e.GET_SURFACE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f2073a[e.OPENING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f2073a[e.OPENED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f2073a[e.CLOSED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f2073a[e.RELEASING.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                f2073a[e.RELEASED.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum e {
        UNINITIALIZED,
        INITIALIZED,
        GET_SURFACE,
        OPENING,
        OPENED,
        CLOSED,
        RELEASING,
        RELEASED
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public final class f extends n2.a {
        f() {
        }

        @Override // androidx.camera.camera2.internal.n2.a
        public void q(n2 n2Var) {
            synchronized (s1.this.f2054a) {
                switch (d.f2073a[s1.this.f2065l.ordinal()]) {
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                        throw new IllegalStateException("onConfigureFailed() should not be possible in state: " + s1.this.f2065l);
                    case 4:
                    case 6:
                    case 7:
                        s1.this.l();
                        break;
                    case 8:
                        androidx.camera.core.p1.a("CaptureSession", "ConfigureFailed callback after change to RELEASED state");
                        break;
                }
                androidx.camera.core.p1.c("CaptureSession", "CameraCaptureSession.onConfigureFailed() " + s1.this.f2065l);
            }
        }

        @Override // androidx.camera.camera2.internal.n2.a
        public void r(n2 n2Var) {
            synchronized (s1.this.f2054a) {
                switch (d.f2073a[s1.this.f2065l.ordinal()]) {
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                    case 8:
                        throw new IllegalStateException("onConfigured() should not be possible in state: " + s1.this.f2065l);
                    case 4:
                        s1 s1Var = s1.this;
                        s1Var.f2065l = e.OPENED;
                        s1Var.f2059f = n2Var;
                        if (s1Var.f2060g != null) {
                            List<androidx.camera.core.impl.f> c9 = s1Var.f2062i.d().c();
                            if (!c9.isEmpty()) {
                                s1 s1Var2 = s1.this;
                                s1Var2.o(s1Var2.w(c9));
                            }
                        }
                        androidx.camera.core.p1.a("CaptureSession", "Attempting to send capture request onConfigured");
                        s1 s1Var3 = s1.this;
                        s1Var3.q(s1Var3.f2060g);
                        s1.this.p();
                        break;
                    case 6:
                        s1.this.f2059f = n2Var;
                        break;
                    case 7:
                        n2Var.close();
                        break;
                }
                androidx.camera.core.p1.a("CaptureSession", "CameraCaptureSession.onConfigured() mState=" + s1.this.f2065l);
            }
        }

        @Override // androidx.camera.camera2.internal.n2.a
        public void s(n2 n2Var) {
            synchronized (s1.this.f2054a) {
                if (d.f2073a[s1.this.f2065l.ordinal()] == 1) {
                    throw new IllegalStateException("onReady() should not be possible in state: " + s1.this.f2065l);
                }
                androidx.camera.core.p1.a("CaptureSession", "CameraCaptureSession.onReady() " + s1.this.f2065l);
            }
        }

        @Override // androidx.camera.camera2.internal.n2.a
        public void t(n2 n2Var) {
            synchronized (s1.this.f2054a) {
                if (s1.this.f2065l == e.UNINITIALIZED) {
                    throw new IllegalStateException("onSessionFinished() should not be possible in state: " + s1.this.f2065l);
                }
                androidx.camera.core.p1.a("CaptureSession", "onSessionFinished()");
                s1.this.l();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public s1() {
        this.f2065l = e.UNINITIALIZED;
        this.f2065l = e.INITIALIZED;
    }

    private CameraCaptureSession.CaptureCallback k(List<y.h> list, CameraCaptureSession.CaptureCallback... captureCallbackArr) {
        ArrayList arrayList = new ArrayList(list.size() + captureCallbackArr.length);
        for (y.h hVar : list) {
            arrayList.add(o1.a(hVar));
        }
        Collections.addAll(arrayList, captureCallbackArr);
        return k0.a(arrayList);
    }

    private t.b m(SessionConfig.e eVar, Map<DeferrableSurface, Surface> map, String str) {
        Surface surface = map.get(eVar.d());
        androidx.core.util.h.i(surface, "Surface in OutputConfig not found in configuredSurfaceMap.");
        t.b bVar = new t.b(eVar.e(), surface);
        if (str == null) {
            str = eVar.b();
        }
        bVar.e(str);
        if (!eVar.c().isEmpty()) {
            bVar.b();
            for (DeferrableSurface deferrableSurface : eVar.c()) {
                Surface surface2 = map.get(deferrableSurface);
                androidx.core.util.h.i(surface2, "Surface in OutputConfig not found in configuredSurfaceMap.");
                bVar.a(surface2);
            }
        }
        return bVar;
    }

    private List<t.b> n(List<t.b> list) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (t.b bVar : list) {
            if (!arrayList.contains(bVar.d())) {
                arrayList.add(bVar.d());
                arrayList2.add(bVar);
            }
        }
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void r(CameraCaptureSession cameraCaptureSession, int i8, boolean z4) {
        synchronized (this.f2054a) {
            if (this.f2065l == e.OPENED) {
                q(this.f2060g);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object t(c.a aVar) {
        String str;
        synchronized (this.f2054a) {
            androidx.core.util.h.k(this.f2067n == null, "Release completer expected to be null");
            this.f2067n = aVar;
            str = "Release[session=" + this + "]";
        }
        return str;
    }

    private static Config u(List<androidx.camera.core.impl.f> list) {
        androidx.camera.core.impl.n P = androidx.camera.core.impl.n.P();
        for (androidx.camera.core.impl.f fVar : list) {
            Config d8 = fVar.d();
            for (Config.a<?> aVar : d8.e()) {
                Object f5 = d8.f(aVar, null);
                if (P.b(aVar)) {
                    Object f8 = P.f(aVar, null);
                    if (!Objects.equals(f8, f5)) {
                        androidx.camera.core.p1.a("CaptureSession", "Detect conflicting option " + aVar.c() + " : " + f5 + " != " + f8);
                    }
                } else {
                    P.s(aVar, f5);
                }
            }
        }
        return P;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: v */
    public com.google.common.util.concurrent.d<Void> s(List<Surface> list, SessionConfig sessionConfig, CameraDevice cameraDevice) {
        synchronized (this.f2054a) {
            int i8 = d.f2073a[this.f2065l.ordinal()];
            if (i8 != 1 && i8 != 2) {
                if (i8 == 3) {
                    this.f2063j.clear();
                    for (int i9 = 0; i9 < list.size(); i9++) {
                        this.f2063j.put(this.f2064k.get(i9), list.get(i9));
                    }
                    this.f2065l = e.OPENING;
                    androidx.camera.core.p1.a("CaptureSession", "Opening capture session.");
                    n2.a v8 = a3.v(this.f2057d, new a3.a(sessionConfig.i()));
                    r.a aVar = new r.a(sessionConfig.d());
                    r.c M = aVar.M(r.c.e());
                    this.f2062i = M;
                    List<androidx.camera.core.impl.f> d8 = M.d().d();
                    f.a k8 = f.a.k(sessionConfig.h());
                    for (androidx.camera.core.impl.f fVar : d8) {
                        k8.e(fVar.d());
                    }
                    ArrayList arrayList = new ArrayList();
                    String R = aVar.R(null);
                    for (SessionConfig.e eVar : sessionConfig.f()) {
                        t.b m8 = m(eVar, this.f2063j, R);
                        Config d9 = sessionConfig.d();
                        Config.a<Long> aVar2 = r.a.C;
                        if (d9.b(aVar2)) {
                            m8.f(((Long) sessionConfig.d().a(aVar2)).longValue());
                        }
                        arrayList.add(m8);
                    }
                    t.h a9 = this.f2058e.a(0, n(arrayList), v8);
                    if (sessionConfig.l() == 5 && sessionConfig.e() != null) {
                        a9.f(t.a.b(sessionConfig.e()));
                    }
                    try {
                        CaptureRequest c9 = b1.c(k8.h(), cameraDevice);
                        if (c9 != null) {
                            a9.g(c9);
                        }
                        return this.f2058e.c(cameraDevice, a9, this.f2064k);
                    } catch (CameraAccessException e8) {
                        return a0.f.f(e8);
                    }
                } else if (i8 != 5) {
                    return a0.f.f(new CancellationException("openCaptureSession() not execute in state: " + this.f2065l));
                }
            }
            return a0.f.f(new IllegalStateException("openCaptureSession() should not be possible in state: " + this.f2065l));
        }
    }

    @Override // androidx.camera.camera2.internal.t1
    public void a() {
        ArrayList<androidx.camera.core.impl.f> arrayList;
        synchronized (this.f2054a) {
            if (this.f2055b.isEmpty()) {
                arrayList = null;
            } else {
                arrayList = new ArrayList(this.f2055b);
                this.f2055b.clear();
            }
        }
        if (arrayList != null) {
            for (androidx.camera.core.impl.f fVar : arrayList) {
                for (y.h hVar : fVar.b()) {
                    hVar.a();
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0056 A[Catch: all -> 0x00af, TryCatch #1 {, blocks: (B:4:0x0003, B:5:0x000d, B:28:0x00a8, B:7:0x0012, B:10:0x0018, B:14:0x0024, B:13:0x001d, B:15:0x0029, B:17:0x0056, B:18:0x005a, B:20:0x005e, B:21:0x0069, B:22:0x006b, B:24:0x006d, B:25:0x008a, B:26:0x008f, B:27:0x00a7), top: B:36:0x0003, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005e A[Catch: all -> 0x00af, TryCatch #1 {, blocks: (B:4:0x0003, B:5:0x000d, B:28:0x00a8, B:7:0x0012, B:10:0x0018, B:14:0x0024, B:13:0x001d, B:15:0x0029, B:17:0x0056, B:18:0x005a, B:20:0x005e, B:21:0x0069, B:22:0x006b, B:24:0x006d, B:25:0x008a, B:26:0x008f, B:27:0x00a7), top: B:36:0x0003, inners: #0 }] */
    @Override // androidx.camera.camera2.internal.t1
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.google.common.util.concurrent.d<java.lang.Void> b(boolean r4) {
        /*
            r3 = this;
            java.lang.Object r0 = r3.f2054a
            monitor-enter(r0)
            int[] r1 = androidx.camera.camera2.internal.s1.d.f2073a     // Catch: java.lang.Throwable -> Laf
            androidx.camera.camera2.internal.s1$e r2 = r3.f2065l     // Catch: java.lang.Throwable -> Laf
            int r2 = r2.ordinal()     // Catch: java.lang.Throwable -> Laf
            r1 = r1[r2]     // Catch: java.lang.Throwable -> Laf
            switch(r1) {
                case 1: goto L8f;
                case 2: goto L8a;
                case 3: goto L6d;
                case 4: goto L29;
                case 5: goto L12;
                case 6: goto L12;
                case 7: goto L5a;
                default: goto L10;
            }     // Catch: java.lang.Throwable -> Laf
        L10:
            goto La8
        L12:
            androidx.camera.camera2.internal.n2 r1 = r3.f2059f     // Catch: java.lang.Throwable -> Laf
            if (r1 == 0) goto L29
            if (r4 == 0) goto L24
            r1.g()     // Catch: android.hardware.camera2.CameraAccessException -> L1c java.lang.Throwable -> Laf
            goto L24
        L1c:
            r4 = move-exception
            java.lang.String r1 = "CaptureSession"
            java.lang.String r2 = "Unable to abort captures."
            androidx.camera.core.p1.d(r1, r2, r4)     // Catch: java.lang.Throwable -> Laf
        L24:
            androidx.camera.camera2.internal.n2 r4 = r3.f2059f     // Catch: java.lang.Throwable -> Laf
            r4.close()     // Catch: java.lang.Throwable -> Laf
        L29:
            r.c r4 = r3.f2062i     // Catch: java.lang.Throwable -> Laf
            r.c$a r4 = r4.d()     // Catch: java.lang.Throwable -> Laf
            r4.a()     // Catch: java.lang.Throwable -> Laf
            androidx.camera.camera2.internal.s1$e r4 = androidx.camera.camera2.internal.s1.e.RELEASING     // Catch: java.lang.Throwable -> Laf
            r3.f2065l = r4     // Catch: java.lang.Throwable -> Laf
            androidx.camera.camera2.internal.z2 r4 = r3.f2058e     // Catch: java.lang.Throwable -> Laf
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Laf
            r1.<init>()     // Catch: java.lang.Throwable -> Laf
            java.lang.String r2 = "The Opener shouldn't null in state:"
            r1.append(r2)     // Catch: java.lang.Throwable -> Laf
            androidx.camera.camera2.internal.s1$e r2 = r3.f2065l     // Catch: java.lang.Throwable -> Laf
            r1.append(r2)     // Catch: java.lang.Throwable -> Laf
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> Laf
            androidx.core.util.h.i(r4, r1)     // Catch: java.lang.Throwable -> Laf
            androidx.camera.camera2.internal.z2 r4 = r3.f2058e     // Catch: java.lang.Throwable -> Laf
            boolean r4 = r4.e()     // Catch: java.lang.Throwable -> Laf
            if (r4 == 0) goto L5a
            r3.l()     // Catch: java.lang.Throwable -> Laf
            goto La8
        L5a:
            com.google.common.util.concurrent.d<java.lang.Void> r4 = r3.f2066m     // Catch: java.lang.Throwable -> Laf
            if (r4 != 0) goto L69
            androidx.camera.camera2.internal.r1 r4 = new androidx.camera.camera2.internal.r1     // Catch: java.lang.Throwable -> Laf
            r4.<init>()     // Catch: java.lang.Throwable -> Laf
            com.google.common.util.concurrent.d r4 = androidx.concurrent.futures.c.a(r4)     // Catch: java.lang.Throwable -> Laf
            r3.f2066m = r4     // Catch: java.lang.Throwable -> Laf
        L69:
            com.google.common.util.concurrent.d<java.lang.Void> r4 = r3.f2066m     // Catch: java.lang.Throwable -> Laf
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Laf
            return r4
        L6d:
            androidx.camera.camera2.internal.z2 r4 = r3.f2058e     // Catch: java.lang.Throwable -> Laf
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Laf
            r1.<init>()     // Catch: java.lang.Throwable -> Laf
            java.lang.String r2 = "The Opener shouldn't null in state:"
            r1.append(r2)     // Catch: java.lang.Throwable -> Laf
            androidx.camera.camera2.internal.s1$e r2 = r3.f2065l     // Catch: java.lang.Throwable -> Laf
            r1.append(r2)     // Catch: java.lang.Throwable -> Laf
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> Laf
            androidx.core.util.h.i(r4, r1)     // Catch: java.lang.Throwable -> Laf
            androidx.camera.camera2.internal.z2 r4 = r3.f2058e     // Catch: java.lang.Throwable -> Laf
            r4.e()     // Catch: java.lang.Throwable -> Laf
        L8a:
            androidx.camera.camera2.internal.s1$e r4 = androidx.camera.camera2.internal.s1.e.RELEASED     // Catch: java.lang.Throwable -> Laf
            r3.f2065l = r4     // Catch: java.lang.Throwable -> Laf
            goto La8
        L8f:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> Laf
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Laf
            r1.<init>()     // Catch: java.lang.Throwable -> Laf
            java.lang.String r2 = "release() should not be possible in state: "
            r1.append(r2)     // Catch: java.lang.Throwable -> Laf
            androidx.camera.camera2.internal.s1$e r2 = r3.f2065l     // Catch: java.lang.Throwable -> Laf
            r1.append(r2)     // Catch: java.lang.Throwable -> Laf
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> Laf
            r4.<init>(r1)     // Catch: java.lang.Throwable -> Laf
            throw r4     // Catch: java.lang.Throwable -> Laf
        La8:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Laf
            r4 = 0
            com.google.common.util.concurrent.d r4 = a0.f.h(r4)
            return r4
        Laf:
            r4 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Laf
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.internal.s1.b(boolean):com.google.common.util.concurrent.d");
    }

    @Override // androidx.camera.camera2.internal.t1
    public List<androidx.camera.core.impl.f> c() {
        List<androidx.camera.core.impl.f> unmodifiableList;
        synchronized (this.f2054a) {
            unmodifiableList = Collections.unmodifiableList(this.f2055b);
        }
        return unmodifiableList;
    }

    @Override // androidx.camera.camera2.internal.t1
    public void close() {
        synchronized (this.f2054a) {
            int i8 = d.f2073a[this.f2065l.ordinal()];
            if (i8 == 1) {
                throw new IllegalStateException("close() should not be possible in state: " + this.f2065l);
            }
            if (i8 != 2) {
                if (i8 != 3) {
                    if (i8 != 4) {
                        if (i8 == 5) {
                            if (this.f2060g != null) {
                                List<androidx.camera.core.impl.f> b9 = this.f2062i.d().b();
                                if (!b9.isEmpty()) {
                                    try {
                                        d(w(b9));
                                    } catch (IllegalStateException e8) {
                                        androidx.camera.core.p1.d("CaptureSession", "Unable to issue the request before close the capture session", e8);
                                    }
                                }
                            }
                        }
                    }
                    z2 z2Var = this.f2058e;
                    androidx.core.util.h.i(z2Var, "The Opener shouldn't null in state:" + this.f2065l);
                    this.f2058e.e();
                    this.f2065l = e.CLOSED;
                    this.f2060g = null;
                } else {
                    z2 z2Var2 = this.f2058e;
                    androidx.core.util.h.i(z2Var2, "The Opener shouldn't null in state:" + this.f2065l);
                    this.f2058e.e();
                }
            }
            this.f2065l = e.RELEASED;
        }
    }

    @Override // androidx.camera.camera2.internal.t1
    public void d(List<androidx.camera.core.impl.f> list) {
        synchronized (this.f2054a) {
            switch (d.f2073a[this.f2065l.ordinal()]) {
                case 1:
                    throw new IllegalStateException("issueCaptureRequests() should not be possible in state: " + this.f2065l);
                case 2:
                case 3:
                case 4:
                    this.f2055b.addAll(list);
                    break;
                case 5:
                    this.f2055b.addAll(list);
                    p();
                    break;
                case 6:
                case 7:
                case 8:
                    throw new IllegalStateException("Cannot issue capture request on a closed/released session.");
            }
        }
    }

    @Override // androidx.camera.camera2.internal.t1
    public SessionConfig e() {
        SessionConfig sessionConfig;
        synchronized (this.f2054a) {
            sessionConfig = this.f2060g;
        }
        return sessionConfig;
    }

    @Override // androidx.camera.camera2.internal.t1
    public void f(SessionConfig sessionConfig) {
        synchronized (this.f2054a) {
            switch (d.f2073a[this.f2065l.ordinal()]) {
                case 1:
                    throw new IllegalStateException("setSessionConfig() should not be possible in state: " + this.f2065l);
                case 2:
                case 3:
                case 4:
                    this.f2060g = sessionConfig;
                    break;
                case 5:
                    this.f2060g = sessionConfig;
                    if (sessionConfig != null) {
                        if (!this.f2063j.keySet().containsAll(sessionConfig.k())) {
                            androidx.camera.core.p1.c("CaptureSession", "Does not have the proper configured lists");
                            return;
                        }
                        androidx.camera.core.p1.a("CaptureSession", "Attempting to submit CaptureRequest after setting");
                        q(this.f2060g);
                        break;
                    } else {
                        return;
                    }
                case 6:
                case 7:
                case 8:
                    throw new IllegalStateException("Session configuration cannot be set on a closed/released session.");
            }
        }
    }

    @Override // androidx.camera.camera2.internal.t1
    public com.google.common.util.concurrent.d<Void> g(final SessionConfig sessionConfig, final CameraDevice cameraDevice, z2 z2Var) {
        synchronized (this.f2054a) {
            if (d.f2073a[this.f2065l.ordinal()] == 2) {
                this.f2065l = e.GET_SURFACE;
                ArrayList arrayList = new ArrayList(sessionConfig.k());
                this.f2064k = arrayList;
                this.f2058e = z2Var;
                a0.d f5 = a0.d.a(z2Var.d(arrayList, 5000L)).f(new a0.a() { // from class: androidx.camera.camera2.internal.p1
                    @Override // a0.a
                    public final com.google.common.util.concurrent.d apply(Object obj) {
                        com.google.common.util.concurrent.d s8;
                        s8 = s1.this.s(sessionConfig, cameraDevice, (List) obj);
                        return s8;
                    }
                }, this.f2058e.b());
                a0.f.b(f5, new b(), this.f2058e.b());
                return a0.f.j(f5);
            }
            androidx.camera.core.p1.c("CaptureSession", "Open not allowed in state: " + this.f2065l);
            return a0.f.f(new IllegalStateException("open() should not allow the state: " + this.f2065l));
        }
    }

    void l() {
        e eVar = this.f2065l;
        e eVar2 = e.RELEASED;
        if (eVar == eVar2) {
            androidx.camera.core.p1.a("CaptureSession", "Skipping finishClose due to being state RELEASED.");
            return;
        }
        this.f2065l = eVar2;
        this.f2059f = null;
        c.a<Void> aVar = this.f2067n;
        if (aVar != null) {
            aVar.c(null);
            this.f2067n = null;
        }
    }

    int o(List<androidx.camera.core.impl.f> list) {
        g1 g1Var;
        ArrayList arrayList;
        boolean z4;
        boolean z8;
        synchronized (this.f2054a) {
            if (list.isEmpty()) {
                return -1;
            }
            try {
                g1Var = new g1();
                arrayList = new ArrayList();
                androidx.camera.core.p1.a("CaptureSession", "Issuing capture request.");
                z4 = false;
                for (androidx.camera.core.impl.f fVar : list) {
                    if (fVar.e().isEmpty()) {
                        androidx.camera.core.p1.a("CaptureSession", "Skipping issuing empty capture request.");
                    } else {
                        Iterator<DeferrableSurface> it = fVar.e().iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                z8 = true;
                                break;
                            }
                            DeferrableSurface next = it.next();
                            if (!this.f2063j.containsKey(next)) {
                                androidx.camera.core.p1.a("CaptureSession", "Skipping capture request with invalid surface: " + next);
                                z8 = false;
                                break;
                            }
                        }
                        if (z8) {
                            if (fVar.g() == 2) {
                                z4 = true;
                            }
                            f.a k8 = f.a.k(fVar);
                            if (fVar.g() == 5 && fVar.c() != null) {
                                k8.n(fVar.c());
                            }
                            SessionConfig sessionConfig = this.f2060g;
                            if (sessionConfig != null) {
                                k8.e(sessionConfig.h().d());
                            }
                            k8.e(this.f2061h);
                            k8.e(fVar.d());
                            CaptureRequest b9 = b1.b(k8.h(), this.f2059f.h(), this.f2063j);
                            if (b9 == null) {
                                androidx.camera.core.p1.a("CaptureSession", "Skipping issuing request without surface.");
                                return -1;
                            }
                            ArrayList arrayList2 = new ArrayList();
                            for (y.h hVar : fVar.b()) {
                                o1.b(hVar, arrayList2);
                            }
                            g1Var.a(b9, arrayList2);
                            arrayList.add(b9);
                        }
                    }
                }
            } catch (CameraAccessException e8) {
                androidx.camera.core.p1.c("CaptureSession", "Unable to access camera: " + e8.getMessage());
                Thread.dumpStack();
            }
            if (arrayList.isEmpty()) {
                androidx.camera.core.p1.a("CaptureSession", "Skipping issuing burst request due to no valid request elements");
                return -1;
            }
            if (this.f2068o.a(arrayList, z4)) {
                this.f2059f.k();
                g1Var.c(new g1.a() { // from class: androidx.camera.camera2.internal.q1
                    @Override // androidx.camera.camera2.internal.g1.a
                    public final void a(CameraCaptureSession cameraCaptureSession, int i8, boolean z9) {
                        s1.this.r(cameraCaptureSession, i8, z9);
                    }
                });
            }
            if (this.f2069p.b(arrayList, z4)) {
                g1Var.a((CaptureRequest) arrayList.get(arrayList.size() - 1), Collections.singletonList(new c()));
            }
            return this.f2059f.e(arrayList, g1Var);
        }
    }

    void p() {
        if (this.f2055b.isEmpty()) {
            return;
        }
        try {
            o(this.f2055b);
        } finally {
            this.f2055b.clear();
        }
    }

    int q(SessionConfig sessionConfig) {
        synchronized (this.f2054a) {
            if (sessionConfig == null) {
                androidx.camera.core.p1.a("CaptureSession", "Skipping issueRepeatingCaptureRequests for no configuration case.");
                return -1;
            }
            androidx.camera.core.impl.f h8 = sessionConfig.h();
            if (h8.e().isEmpty()) {
                androidx.camera.core.p1.a("CaptureSession", "Skipping issueRepeatingCaptureRequests for no surface.");
                try {
                    this.f2059f.k();
                } catch (CameraAccessException e8) {
                    androidx.camera.core.p1.c("CaptureSession", "Unable to access camera: " + e8.getMessage());
                    Thread.dumpStack();
                }
                return -1;
            }
            try {
                androidx.camera.core.p1.a("CaptureSession", "Issuing request for session.");
                f.a k8 = f.a.k(h8);
                Config u8 = u(this.f2062i.d().e());
                this.f2061h = u8;
                k8.e(u8);
                CaptureRequest b9 = b1.b(k8.h(), this.f2059f.h(), this.f2063j);
                if (b9 == null) {
                    androidx.camera.core.p1.a("CaptureSession", "Skipping issuing empty request for session.");
                    return -1;
                }
                return this.f2059f.i(b9, k(h8.b(), this.f2056c));
            } catch (CameraAccessException e9) {
                androidx.camera.core.p1.c("CaptureSession", "Unable to access camera: " + e9.getMessage());
                Thread.dumpStack();
                return -1;
            }
        }
    }

    List<androidx.camera.core.impl.f> w(List<androidx.camera.core.impl.f> list) {
        ArrayList arrayList = new ArrayList();
        for (androidx.camera.core.impl.f fVar : list) {
            f.a k8 = f.a.k(fVar);
            k8.p(1);
            for (DeferrableSurface deferrableSurface : this.f2060g.h().e()) {
                k8.f(deferrableSurface);
            }
            arrayList.add(k8.h());
        }
        return arrayList;
    }
}
