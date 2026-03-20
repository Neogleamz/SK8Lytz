package androidx.camera.camera2.internal;

import android.graphics.Rect;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Rational;
import androidx.camera.camera2.internal.t;
import androidx.camera.core.CameraControl;
import androidx.camera.core.impl.CameraCaptureFailure;
import androidx.camera.core.impl.CameraControlInternal;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.f;
import androidx.concurrent.futures.c;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import r.a;
import w.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class t implements CameraControlInternal {

    /* renamed from: b  reason: collision with root package name */
    final b f2086b;

    /* renamed from: c  reason: collision with root package name */
    final Executor f2087c;

    /* renamed from: d  reason: collision with root package name */
    private final Object f2088d = new Object();

    /* renamed from: e  reason: collision with root package name */
    private final s.y f2089e;

    /* renamed from: f  reason: collision with root package name */
    private final CameraControlInternal.b f2090f;

    /* renamed from: g  reason: collision with root package name */
    private final SessionConfig.b f2091g;

    /* renamed from: h  reason: collision with root package name */
    private final b2 f2092h;

    /* renamed from: i  reason: collision with root package name */
    private final j3 f2093i;

    /* renamed from: j  reason: collision with root package name */
    private final e3 f2094j;

    /* renamed from: k  reason: collision with root package name */
    private final y1 f2095k;

    /* renamed from: l  reason: collision with root package name */
    l3 f2096l;

    /* renamed from: m  reason: collision with root package name */
    private final w.g f2097m;

    /* renamed from: n  reason: collision with root package name */
    private final o0 f2098n;

    /* renamed from: o  reason: collision with root package name */
    private int f2099o;

    /* renamed from: p  reason: collision with root package name */
    private volatile boolean f2100p;
    private volatile int q;

    /* renamed from: r  reason: collision with root package name */
    private final v.a f2101r;

    /* renamed from: s  reason: collision with root package name */
    private final v.b f2102s;

    /* renamed from: t  reason: collision with root package name */
    private final AtomicLong f2103t;

    /* renamed from: u  reason: collision with root package name */
    private volatile com.google.common.util.concurrent.d<Void> f2104u;

    /* renamed from: v  reason: collision with root package name */
    private int f2105v;

    /* renamed from: w  reason: collision with root package name */
    private long f2106w;

    /* renamed from: x  reason: collision with root package name */
    private final a f2107x;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a extends y.h {

        /* renamed from: a  reason: collision with root package name */
        Set<y.h> f2108a = new HashSet();

        /* renamed from: b  reason: collision with root package name */
        Map<y.h, Executor> f2109b = new ArrayMap();

        a() {
        }

        @Override // y.h
        public void a() {
            for (final y.h hVar : this.f2108a) {
                try {
                    this.f2109b.get(hVar).execute(new Runnable() { // from class: androidx.camera.camera2.internal.q
                        @Override // java.lang.Runnable
                        public final void run() {
                            y.h.this.a();
                        }
                    });
                } catch (RejectedExecutionException e8) {
                    androidx.camera.core.p1.d("Camera2CameraControlImp", "Executor rejected to invoke onCaptureCancelled.", e8);
                }
            }
        }

        @Override // y.h
        public void b(final y.j jVar) {
            for (final y.h hVar : this.f2108a) {
                try {
                    this.f2109b.get(hVar).execute(new Runnable() { // from class: androidx.camera.camera2.internal.s
                        @Override // java.lang.Runnable
                        public final void run() {
                            y.h.this.b(jVar);
                        }
                    });
                } catch (RejectedExecutionException e8) {
                    androidx.camera.core.p1.d("Camera2CameraControlImp", "Executor rejected to invoke onCaptureCompleted.", e8);
                }
            }
        }

        @Override // y.h
        public void c(final CameraCaptureFailure cameraCaptureFailure) {
            for (final y.h hVar : this.f2108a) {
                try {
                    this.f2109b.get(hVar).execute(new Runnable() { // from class: androidx.camera.camera2.internal.r
                        @Override // java.lang.Runnable
                        public final void run() {
                            y.h.this.c(cameraCaptureFailure);
                        }
                    });
                } catch (RejectedExecutionException e8) {
                    androidx.camera.core.p1.d("Camera2CameraControlImp", "Executor rejected to invoke onCaptureFailed.", e8);
                }
            }
        }

        void g(Executor executor, y.h hVar) {
            this.f2108a.add(hVar);
            this.f2109b.put(hVar, executor);
        }

        void k(y.h hVar) {
            this.f2108a.remove(hVar);
            this.f2109b.remove(hVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends CameraCaptureSession.CaptureCallback {

        /* renamed from: a  reason: collision with root package name */
        final Set<c> f2110a = new HashSet();

        /* renamed from: b  reason: collision with root package name */
        private final Executor f2111b;

        b(Executor executor) {
            this.f2111b = executor;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void c(TotalCaptureResult totalCaptureResult) {
            HashSet hashSet = new HashSet();
            for (c cVar : this.f2110a) {
                if (cVar.a(totalCaptureResult)) {
                    hashSet.add(cVar);
                }
            }
            if (hashSet.isEmpty()) {
                return;
            }
            this.f2110a.removeAll(hashSet);
        }

        void b(c cVar) {
            this.f2110a.add(cVar);
        }

        void d(c cVar) {
            this.f2110a.remove(cVar);
        }

        @Override // android.hardware.camera2.CameraCaptureSession.CaptureCallback
        public void onCaptureCompleted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, final TotalCaptureResult totalCaptureResult) {
            this.f2111b.execute(new Runnable() { // from class: androidx.camera.camera2.internal.u
                @Override // java.lang.Runnable
                public final void run() {
                    t.b.this.c(totalCaptureResult);
                }
            });
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        boolean a(TotalCaptureResult totalCaptureResult);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public t(s.y yVar, ScheduledExecutorService scheduledExecutorService, Executor executor, CameraControlInternal.b bVar, y.t0 t0Var) {
        SessionConfig.b bVar2 = new SessionConfig.b();
        this.f2091g = bVar2;
        this.f2099o = 0;
        this.f2100p = false;
        this.q = 2;
        this.f2103t = new AtomicLong(0L);
        this.f2104u = a0.f.h(null);
        this.f2105v = 1;
        this.f2106w = 0L;
        a aVar = new a();
        this.f2107x = aVar;
        this.f2089e = yVar;
        this.f2090f = bVar;
        this.f2087c = executor;
        b bVar3 = new b(executor);
        this.f2086b = bVar3;
        bVar2.s(this.f2105v);
        bVar2.i(n1.d(bVar3));
        bVar2.i(aVar);
        this.f2095k = new y1(this, yVar, executor);
        this.f2092h = new b2(this, scheduledExecutorService, executor, t0Var);
        this.f2093i = new j3(this, yVar, executor);
        this.f2094j = new e3(this, yVar, executor);
        this.f2096l = Build.VERSION.SDK_INT >= 23 ? new p3(yVar) : new q3();
        this.f2101r = new v.a(t0Var);
        this.f2102s = new v.b(t0Var);
        this.f2097m = new w.g(this, executor);
        this.f2098n = new o0(this, yVar, t0Var, executor);
        executor.execute(new Runnable() { // from class: androidx.camera.camera2.internal.l
            @Override // java.lang.Runnable
            public final void run() {
                t.this.R();
            }
        });
    }

    private int E(int i8) {
        int[] iArr = (int[]) this.f2089e.a(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
        if (iArr == null) {
            return 0;
        }
        return L(i8, iArr) ? i8 : L(1, iArr) ? 1 : 0;
    }

    private boolean K() {
        return G() > 0;
    }

    private boolean L(int i8, int[] iArr) {
        for (int i9 : iArr) {
            if (i8 == i9) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean M(TotalCaptureResult totalCaptureResult, long j8) {
        Long l8;
        if (totalCaptureResult.getRequest() == null) {
            return false;
        }
        Object tag = totalCaptureResult.getRequest().getTag();
        return (tag instanceof y.a1) && (l8 = (Long) ((y.a1) tag).c("CameraControlSessionUpdateId")) != null && l8.longValue() >= j8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void O() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void P(Executor executor, y.h hVar) {
        this.f2107x.g(executor, hVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void Q() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void R() {
        u(this.f2097m.l());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void S(y.h hVar) {
        this.f2107x.k(hVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.google.common.util.concurrent.d T(List list, int i8, int i9, int i10, Void r52) {
        return this.f2098n.e(list, i8, i9, i10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void U(c.a aVar) {
        a0.f.k(j0(i0()), aVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object V(final c.a aVar) {
        this.f2087c.execute(new Runnable() { // from class: androidx.camera.camera2.internal.m
            @Override // java.lang.Runnable
            public final void run() {
                t.this.U(aVar);
            }
        });
        return "updateSessionConfigAsync";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean W(long j8, c.a aVar, TotalCaptureResult totalCaptureResult) {
        if (M(totalCaptureResult, j8)) {
            aVar.c(null);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object X(final long j8, final c.a aVar) {
        u(new c() { // from class: androidx.camera.camera2.internal.h
            @Override // androidx.camera.camera2.internal.t.c
            public final boolean a(TotalCaptureResult totalCaptureResult) {
                boolean W;
                W = t.W(j8, aVar, totalCaptureResult);
                return W;
            }
        });
        return "waitForSessionUpdateId:" + j8;
    }

    private com.google.common.util.concurrent.d<Void> j0(final long j8) {
        return androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.camera2.internal.j
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar) {
                Object X;
                X = t.this.X(j8, aVar);
                return X;
            }
        });
    }

    public SessionConfig A() {
        this.f2091g.s(this.f2105v);
        this.f2091g.q(B());
        Object O = this.f2097m.k().O(null);
        if (O != null && (O instanceof Integer)) {
            this.f2091g.l("Camera2CameraControl", O);
        }
        this.f2091g.l("CameraControlSessionUpdateId", Long.valueOf(this.f2106w));
        return this.f2091g.m();
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0070 A[LOOP:0: B:12:0x006a->B:14:0x0070, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    androidx.camera.core.impl.Config B() {
        /*
            r7 = this;
            r.a$a r0 = new r.a$a
            r0.<init>()
            android.hardware.camera2.CaptureRequest$Key r1 = android.hardware.camera2.CaptureRequest.CONTROL_MODE
            r2 = 1
            java.lang.Integer r3 = java.lang.Integer.valueOf(r2)
            r0.e(r1, r3)
            androidx.camera.camera2.internal.b2 r1 = r7.f2092h
            r1.b(r0)
            v.a r1 = r7.f2101r
            r1.a(r0)
            androidx.camera.camera2.internal.j3 r1 = r7.f2093i
            r1.e(r0)
            boolean r1 = r7.f2100p
            r3 = 2
            if (r1 == 0) goto L2d
            android.hardware.camera2.CaptureRequest$Key r1 = android.hardware.camera2.CaptureRequest.FLASH_MODE
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r0.e(r1, r3)
            goto L33
        L2d:
            int r1 = r7.q
            if (r1 == 0) goto L37
            if (r1 == r2) goto L35
        L33:
            r1 = r2
            goto L3d
        L35:
            r1 = 3
            goto L3d
        L37:
            v.b r1 = r7.f2102s
            int r1 = r1.a(r3)
        L3d:
            android.hardware.camera2.CaptureRequest$Key r3 = android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE
            int r1 = r7.C(r1)
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            r0.e(r3, r1)
            android.hardware.camera2.CaptureRequest$Key r1 = android.hardware.camera2.CaptureRequest.CONTROL_AWB_MODE
            int r2 = r7.E(r2)
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r0.e(r1, r2)
            androidx.camera.camera2.internal.y1 r1 = r7.f2095k
            r1.c(r0)
            w.g r1 = r7.f2097m
            r.a r1 = r1.k()
            java.util.Set r2 = r1.e()
            java.util.Iterator r2 = r2.iterator()
        L6a:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L84
            java.lang.Object r3 = r2.next()
            androidx.camera.core.impl.Config$a r3 = (androidx.camera.core.impl.Config.a) r3
            androidx.camera.core.impl.m r4 = r0.a()
            androidx.camera.core.impl.Config$OptionPriority r5 = androidx.camera.core.impl.Config.OptionPriority.ALWAYS_OVERRIDE
            java.lang.Object r6 = r1.a(r3)
            r4.o(r3, r5, r6)
            goto L6a
        L84:
            r.a r0 = r0.c()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.internal.t.B():androidx.camera.core.impl.Config");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int C(int i8) {
        int[] iArr = (int[]) this.f2089e.a(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
        if (iArr == null) {
            return 0;
        }
        return L(i8, iArr) ? i8 : L(1, iArr) ? 1 : 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int D(int i8) {
        int[] iArr = (int[]) this.f2089e.a(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        if (iArr == null) {
            return 0;
        }
        if (L(i8, iArr)) {
            return i8;
        }
        if (L(4, iArr)) {
            return 4;
        }
        return L(1, iArr) ? 1 : 0;
    }

    public e3 F() {
        return this.f2094j;
    }

    int G() {
        int i8;
        synchronized (this.f2088d) {
            i8 = this.f2099o;
        }
        return i8;
    }

    public j3 H() {
        return this.f2093i;
    }

    public l3 I() {
        return this.f2096l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void J() {
        synchronized (this.f2088d) {
            this.f2099o++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean N() {
        return this.f2100p;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Y(c cVar) {
        this.f2086b.d(cVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Z(final y.h hVar) {
        this.f2087c.execute(new Runnable() { // from class: androidx.camera.camera2.internal.o
            @Override // java.lang.Runnable
            public final void run() {
                t.this.S(hVar);
            }
        });
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public void a(SessionConfig.b bVar) {
        this.f2096l.a(bVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a0() {
        d0(1);
    }

    @Override // androidx.camera.core.CameraControl
    public com.google.common.util.concurrent.d<Void> b(float f5) {
        return !K() ? a0.f.f(new CameraControl.OperationCanceledException("Camera is not active.")) : a0.f.j(this.f2093i.p(f5));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b0(boolean z4) {
        this.f2092h.m(z4);
        this.f2093i.o(z4);
        this.f2094j.j(z4);
        this.f2095k.b(z4);
        this.f2097m.s(z4);
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public com.google.common.util.concurrent.d<List<Void>> c(final List<androidx.camera.core.impl.f> list, final int i8, final int i9) {
        if (K()) {
            final int y8 = y();
            return a0.d.a(a0.f.j(this.f2104u)).f(new a0.a() { // from class: androidx.camera.camera2.internal.f
                @Override // a0.a
                public final com.google.common.util.concurrent.d apply(Object obj) {
                    com.google.common.util.concurrent.d T;
                    T = t.this.T(list, i8, y8, i9, (Void) obj);
                    return T;
                }
            }, this.f2087c);
        }
        androidx.camera.core.p1.k("Camera2CameraControlImp", "Camera is not active.");
        return a0.f.f(new CameraControl.OperationCanceledException("Camera is not active."));
    }

    public void c0(Rational rational) {
        this.f2092h.n(rational);
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public void d(Config config) {
        this.f2097m.g(j.a.e(config).d()).c(new Runnable() { // from class: androidx.camera.camera2.internal.g
            @Override // java.lang.Runnable
            public final void run() {
                t.O();
            }
        }, z.a.a());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d0(int i8) {
        this.f2105v = i8;
        this.f2092h.o(i8);
        this.f2098n.d(this.f2105v);
    }

    @Override // androidx.camera.core.CameraControl
    public com.google.common.util.concurrent.d<Void> e(float f5) {
        return !K() ? a0.f.f(new CameraControl.OperationCanceledException("Camera is not active.")) : a0.f.j(this.f2093i.q(f5));
    }

    public void e0(boolean z4) {
        this.f2096l.e(z4);
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public Rect f() {
        return (Rect) androidx.core.util.h.h((Rect) this.f2089e.a(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f0(List<androidx.camera.core.impl.f> list) {
        this.f2090f.b(list);
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public void g(int i8) {
        if (!K()) {
            androidx.camera.core.p1.k("Camera2CameraControlImp", "Camera is not active.");
            return;
        }
        this.q = i8;
        l3 l3Var = this.f2096l;
        boolean z4 = true;
        if (this.q != 1 && this.q != 0) {
            z4 = false;
        }
        l3Var.d(z4);
        this.f2104u = h0();
    }

    public void g0() {
        this.f2087c.execute(new Runnable() { // from class: androidx.camera.camera2.internal.k
            @Override // java.lang.Runnable
            public final void run() {
                t.this.i0();
            }
        });
    }

    @Override // androidx.camera.core.CameraControl
    public com.google.common.util.concurrent.d<Void> h(boolean z4) {
        return !K() ? a0.f.f(new CameraControl.OperationCanceledException("Camera is not active.")) : a0.f.j(this.f2094j.d(z4));
    }

    com.google.common.util.concurrent.d<Void> h0() {
        return a0.f.j(androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.camera2.internal.i
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar) {
                Object V;
                V = t.this.V(aVar);
                return V;
            }
        }));
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public Config i() {
        return this.f2097m.k();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long i0() {
        this.f2106w = this.f2103t.getAndIncrement();
        this.f2090f.a();
        return this.f2106w;
    }

    @Override // androidx.camera.core.impl.CameraControlInternal
    public void j() {
        this.f2097m.i().c(new Runnable() { // from class: androidx.camera.camera2.internal.p
            @Override // java.lang.Runnable
            public final void run() {
                t.Q();
            }
        }, z.a.a());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(c cVar) {
        this.f2086b.b(cVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(final Executor executor, final y.h hVar) {
        this.f2087c.execute(new Runnable() { // from class: androidx.camera.camera2.internal.n
            @Override // java.lang.Runnable
            public final void run() {
                t.this.P(executor, hVar);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w() {
        synchronized (this.f2088d) {
            int i8 = this.f2099o;
            if (i8 == 0) {
                throw new IllegalStateException("Decrementing use count occurs more times than incrementing");
            }
            this.f2099o = i8 - 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(boolean z4) {
        this.f2100p = z4;
        if (!z4) {
            f.a aVar = new f.a();
            aVar.p(this.f2105v);
            aVar.q(true);
            a.C0201a c0201a = new a.C0201a();
            c0201a.e(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(C(1)));
            c0201a.e(CaptureRequest.FLASH_MODE, 0);
            aVar.e(c0201a.c());
            f0(Collections.singletonList(aVar.h()));
        }
        i0();
    }

    public int y() {
        return this.q;
    }

    public b2 z() {
        return this.f2092h;
    }
}
