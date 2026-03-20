package androidx.camera.camera2.internal;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import androidx.camera.camera2.internal.o0;
import androidx.camera.camera2.internal.t;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.impl.CameraCaptureFailure;
import androidx.camera.core.impl.CameraCaptureMetaData$AeState;
import androidx.camera.core.impl.CameraCaptureMetaData$AfMode;
import androidx.camera.core.impl.CameraCaptureMetaData$AfState;
import androidx.camera.core.impl.CameraCaptureMetaData$AwbState;
import androidx.camera.core.impl.f;
import androidx.concurrent.futures.c;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import r.a;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o0 {

    /* renamed from: g  reason: collision with root package name */
    private static final Set<CameraCaptureMetaData$AfState> f1976g = Collections.unmodifiableSet(EnumSet.of(CameraCaptureMetaData$AfState.PASSIVE_FOCUSED, CameraCaptureMetaData$AfState.PASSIVE_NOT_FOCUSED, CameraCaptureMetaData$AfState.LOCKED_FOCUSED, CameraCaptureMetaData$AfState.LOCKED_NOT_FOCUSED));

    /* renamed from: h  reason: collision with root package name */
    private static final Set<CameraCaptureMetaData$AwbState> f1977h = Collections.unmodifiableSet(EnumSet.of(CameraCaptureMetaData$AwbState.CONVERGED, CameraCaptureMetaData$AwbState.UNKNOWN));

    /* renamed from: i  reason: collision with root package name */
    private static final Set<CameraCaptureMetaData$AeState> f1978i;

    /* renamed from: j  reason: collision with root package name */
    private static final Set<CameraCaptureMetaData$AeState> f1979j;

    /* renamed from: a  reason: collision with root package name */
    private final t f1980a;

    /* renamed from: b  reason: collision with root package name */
    private final v.s f1981b;

    /* renamed from: c  reason: collision with root package name */
    private final y.t0 f1982c;

    /* renamed from: d  reason: collision with root package name */
    private final Executor f1983d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f1984e;

    /* renamed from: f  reason: collision with root package name */
    private int f1985f = 1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements d {

        /* renamed from: a  reason: collision with root package name */
        private final t f1986a;

        /* renamed from: b  reason: collision with root package name */
        private final v.l f1987b;

        /* renamed from: c  reason: collision with root package name */
        private final int f1988c;

        /* renamed from: d  reason: collision with root package name */
        private boolean f1989d = false;

        a(t tVar, int i8, v.l lVar) {
            this.f1986a = tVar;
            this.f1988c = i8;
            this.f1987b = lVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Object f(c.a aVar) {
            this.f1986a.z().q(aVar);
            this.f1987b.b();
            return "AePreCapture";
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Boolean g(Void r02) {
            return Boolean.TRUE;
        }

        @Override // androidx.camera.camera2.internal.o0.d
        public com.google.common.util.concurrent.d<Boolean> a(TotalCaptureResult totalCaptureResult) {
            if (o0.b(this.f1988c, totalCaptureResult)) {
                androidx.camera.core.p1.a("Camera2CapturePipeline", "Trigger AE");
                this.f1989d = true;
                return a0.d.a(androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.camera2.internal.m0
                    @Override // androidx.concurrent.futures.c.InterfaceC0024c
                    public final Object a(c.a aVar) {
                        Object f5;
                        f5 = o0.a.this.f(aVar);
                        return f5;
                    }
                })).e(new n.a() { // from class: androidx.camera.camera2.internal.n0
                    @Override // n.a
                    public final Object apply(Object obj) {
                        Boolean g8;
                        g8 = o0.a.g((Void) obj);
                        return g8;
                    }
                }, z.a.a());
            }
            return a0.f.h(Boolean.FALSE);
        }

        @Override // androidx.camera.camera2.internal.o0.d
        public boolean b() {
            return this.f1988c == 0;
        }

        @Override // androidx.camera.camera2.internal.o0.d
        public void c() {
            if (this.f1989d) {
                androidx.camera.core.p1.a("Camera2CapturePipeline", "cancel TriggerAePreCapture");
                this.f1986a.z().c(false, true);
                this.f1987b.a();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements d {

        /* renamed from: a  reason: collision with root package name */
        private final t f1990a;

        /* renamed from: b  reason: collision with root package name */
        private boolean f1991b = false;

        b(t tVar) {
            this.f1990a = tVar;
        }

        @Override // androidx.camera.camera2.internal.o0.d
        public com.google.common.util.concurrent.d<Boolean> a(TotalCaptureResult totalCaptureResult) {
            Integer num;
            com.google.common.util.concurrent.d<Boolean> h8 = a0.f.h(Boolean.TRUE);
            if (totalCaptureResult == null || (num = (Integer) totalCaptureResult.get(CaptureResult.CONTROL_AF_MODE)) == null) {
                return h8;
            }
            int intValue = num.intValue();
            if (intValue == 1 || intValue == 2) {
                androidx.camera.core.p1.a("Camera2CapturePipeline", "TriggerAf? AF mode auto");
                Integer num2 = (Integer) totalCaptureResult.get(CaptureResult.CONTROL_AF_STATE);
                if (num2 != null && num2.intValue() == 0) {
                    androidx.camera.core.p1.a("Camera2CapturePipeline", "Trigger AF");
                    this.f1991b = true;
                    this.f1990a.z().r(null, false);
                }
            }
            return h8;
        }

        @Override // androidx.camera.camera2.internal.o0.d
        public boolean b() {
            return true;
        }

        @Override // androidx.camera.camera2.internal.o0.d
        public void c() {
            if (this.f1991b) {
                androidx.camera.core.p1.a("Camera2CapturePipeline", "cancel TriggerAF");
                this.f1990a.z().c(true, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: i  reason: collision with root package name */
        private static final long f1992i;

        /* renamed from: j  reason: collision with root package name */
        private static final long f1993j;

        /* renamed from: a  reason: collision with root package name */
        private final int f1994a;

        /* renamed from: b  reason: collision with root package name */
        private final Executor f1995b;

        /* renamed from: c  reason: collision with root package name */
        private final t f1996c;

        /* renamed from: d  reason: collision with root package name */
        private final v.l f1997d;

        /* renamed from: e  reason: collision with root package name */
        private final boolean f1998e;

        /* renamed from: f  reason: collision with root package name */
        private long f1999f = f1992i;

        /* renamed from: g  reason: collision with root package name */
        final List<d> f2000g = new ArrayList();

        /* renamed from: h  reason: collision with root package name */
        private final d f2001h = new a();

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements d {
            a() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static /* synthetic */ Boolean e(List list) {
                return Boolean.valueOf(list.contains(Boolean.TRUE));
            }

            @Override // androidx.camera.camera2.internal.o0.d
            public com.google.common.util.concurrent.d<Boolean> a(TotalCaptureResult totalCaptureResult) {
                ArrayList arrayList = new ArrayList();
                for (d dVar : c.this.f2000g) {
                    arrayList.add(dVar.a(totalCaptureResult));
                }
                return a0.f.o(a0.f.c(arrayList), new n.a() { // from class: androidx.camera.camera2.internal.v0
                    @Override // n.a
                    public final Object apply(Object obj) {
                        Boolean e8;
                        e8 = o0.c.a.e((List) obj);
                        return e8;
                    }
                }, z.a.a());
            }

            @Override // androidx.camera.camera2.internal.o0.d
            public boolean b() {
                for (d dVar : c.this.f2000g) {
                    if (dVar.b()) {
                        return true;
                    }
                }
                return false;
            }

            @Override // androidx.camera.camera2.internal.o0.d
            public void c() {
                for (d dVar : c.this.f2000g) {
                    dVar.c();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class b extends y.h {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ c.a f2003a;

            b(c.a aVar) {
                this.f2003a = aVar;
            }

            @Override // y.h
            public void a() {
                this.f2003a.f(new ImageCaptureException(3, "Capture request is cancelled because camera is closed", null));
            }

            @Override // y.h
            public void b(y.j jVar) {
                this.f2003a.c(null);
            }

            @Override // y.h
            public void c(CameraCaptureFailure cameraCaptureFailure) {
                this.f2003a.f(new ImageCaptureException(2, "Capture request failed with reason " + cameraCaptureFailure.a(), null));
            }
        }

        static {
            TimeUnit timeUnit = TimeUnit.SECONDS;
            f1992i = timeUnit.toNanos(1L);
            f1993j = timeUnit.toNanos(5L);
        }

        c(int i8, Executor executor, t tVar, boolean z4, v.l lVar) {
            this.f1994a = i8;
            this.f1995b = executor;
            this.f1996c = tVar;
            this.f1998e = z4;
            this.f1997d = lVar;
        }

        private void g(f.a aVar) {
            a.C0201a c0201a = new a.C0201a();
            c0201a.e(CaptureRequest.CONTROL_AE_MODE, 3);
            aVar.e(c0201a.c());
        }

        private void h(f.a aVar, androidx.camera.core.impl.f fVar) {
            int i8 = (this.f1994a != 3 || this.f1998e) ? (fVar.g() == -1 || fVar.g() == 5) ? 2 : -1 : 4;
            if (i8 != -1) {
                aVar.p(i8);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.google.common.util.concurrent.d j(int i8, TotalCaptureResult totalCaptureResult) {
            if (o0.b(i8, totalCaptureResult)) {
                o(f1993j);
            }
            return this.f2001h.a(totalCaptureResult);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.google.common.util.concurrent.d l(Boolean bool) {
            return Boolean.TRUE.equals(bool) ? o0.f(this.f1999f, this.f1996c, new e.a() { // from class: androidx.camera.camera2.internal.s0
                @Override // androidx.camera.camera2.internal.o0.e.a
                public final boolean a(TotalCaptureResult totalCaptureResult) {
                    boolean a9;
                    a9 = o0.a(totalCaptureResult, false);
                    return a9;
                }
            }) : a0.f.h(null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.google.common.util.concurrent.d m(List list, int i8, TotalCaptureResult totalCaptureResult) {
            return p(list, i8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Object n(f.a aVar, c.a aVar2) {
            aVar.c(new b(aVar2));
            return "submitStillCapture";
        }

        private void o(long j8) {
            this.f1999f = j8;
        }

        void f(d dVar) {
            this.f2000g.add(dVar);
        }

        com.google.common.util.concurrent.d<List<Void>> i(final List<androidx.camera.core.impl.f> list, final int i8) {
            com.google.common.util.concurrent.d h8 = a0.f.h(null);
            if (!this.f2000g.isEmpty()) {
                h8 = a0.d.a(this.f2001h.b() ? o0.f(0L, this.f1996c, null) : a0.f.h(null)).f(new a0.a() { // from class: androidx.camera.camera2.internal.q0
                    @Override // a0.a
                    public final com.google.common.util.concurrent.d apply(Object obj) {
                        com.google.common.util.concurrent.d j8;
                        j8 = o0.c.this.j(i8, (TotalCaptureResult) obj);
                        return j8;
                    }
                }, this.f1995b).f(new a0.a() { // from class: androidx.camera.camera2.internal.p0
                    @Override // a0.a
                    public final com.google.common.util.concurrent.d apply(Object obj) {
                        com.google.common.util.concurrent.d l8;
                        l8 = o0.c.this.l((Boolean) obj);
                        return l8;
                    }
                }, this.f1995b);
            }
            a0.d f5 = a0.d.a(h8).f(new a0.a() { // from class: androidx.camera.camera2.internal.r0
                @Override // a0.a
                public final com.google.common.util.concurrent.d apply(Object obj) {
                    com.google.common.util.concurrent.d m8;
                    m8 = o0.c.this.m(list, i8, (TotalCaptureResult) obj);
                    return m8;
                }
            }, this.f1995b);
            final d dVar = this.f2001h;
            Objects.requireNonNull(dVar);
            f5.c(new Runnable() { // from class: androidx.camera.camera2.internal.u0
                @Override // java.lang.Runnable
                public final void run() {
                    o0.d.this.c();
                }
            }, this.f1995b);
            return f5;
        }

        com.google.common.util.concurrent.d<List<Void>> p(List<androidx.camera.core.impl.f> list, int i8) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (androidx.camera.core.impl.f fVar : list) {
                final f.a k8 = f.a.k(fVar);
                y.j jVar = null;
                if (fVar.g() == 5 && !this.f1996c.I().c() && !this.f1996c.I().b()) {
                    androidx.camera.core.l1 f5 = this.f1996c.I().f();
                    if (f5 != null && this.f1996c.I().g(f5)) {
                        jVar = y.k.a(f5.e1());
                    }
                }
                if (jVar != null) {
                    k8.n(jVar);
                } else {
                    h(k8, fVar);
                }
                if (this.f1997d.c(i8)) {
                    g(k8);
                }
                arrayList.add(androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.camera2.internal.t0
                    @Override // androidx.concurrent.futures.c.InterfaceC0024c
                    public final Object a(c.a aVar) {
                        Object n8;
                        n8 = o0.c.this.n(k8, aVar);
                        return n8;
                    }
                }));
                arrayList2.add(k8.h());
            }
            this.f1996c.f0(arrayList2);
            return a0.f.c(arrayList);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        com.google.common.util.concurrent.d<Boolean> a(TotalCaptureResult totalCaptureResult);

        boolean b();

        void c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e implements t.c {

        /* renamed from: a  reason: collision with root package name */
        private c.a<TotalCaptureResult> f2005a;

        /* renamed from: c  reason: collision with root package name */
        private final long f2007c;

        /* renamed from: d  reason: collision with root package name */
        private final a f2008d;

        /* renamed from: b  reason: collision with root package name */
        private final com.google.common.util.concurrent.d<TotalCaptureResult> f2006b = androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.camera2.internal.w0
            @Override // androidx.concurrent.futures.c.InterfaceC0024c
            public final Object a(c.a aVar) {
                Object d8;
                d8 = o0.e.this.d(aVar);
                return d8;
            }
        });

        /* renamed from: e  reason: collision with root package name */
        private volatile Long f2009e = null;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public interface a {
            boolean a(TotalCaptureResult totalCaptureResult);
        }

        e(long j8, a aVar) {
            this.f2007c = j8;
            this.f2008d = aVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Object d(c.a aVar) {
            this.f2005a = aVar;
            return "waitFor3AResult";
        }

        @Override // androidx.camera.camera2.internal.t.c
        public boolean a(TotalCaptureResult totalCaptureResult) {
            Long l8 = (Long) totalCaptureResult.get(CaptureResult.SENSOR_TIMESTAMP);
            if (l8 != null && this.f2009e == null) {
                this.f2009e = l8;
            }
            Long l9 = this.f2009e;
            if (0 == this.f2007c || l9 == null || l8 == null || l8.longValue() - l9.longValue() <= this.f2007c) {
                a aVar = this.f2008d;
                if (aVar == null || aVar.a(totalCaptureResult)) {
                    this.f2005a.c(totalCaptureResult);
                    return true;
                }
                return false;
            }
            this.f2005a.c(null);
            androidx.camera.core.p1.a("Camera2CapturePipeline", "Wait for capture result timeout, current:" + l8 + " first: " + l9);
            return true;
        }

        public com.google.common.util.concurrent.d<TotalCaptureResult> c() {
            return this.f2006b;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f implements d {

        /* renamed from: e  reason: collision with root package name */
        private static final long f2010e = TimeUnit.SECONDS.toNanos(2);

        /* renamed from: a  reason: collision with root package name */
        private final t f2011a;

        /* renamed from: b  reason: collision with root package name */
        private final int f2012b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f2013c = false;

        /* renamed from: d  reason: collision with root package name */
        private final Executor f2014d;

        f(t tVar, int i8, Executor executor) {
            this.f2011a = tVar;
            this.f2012b = i8;
            this.f2014d = executor;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Object h(c.a aVar) {
            this.f2011a.F().g(aVar, true);
            return "TorchOn";
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.google.common.util.concurrent.d j(Void r4) {
            return o0.f(f2010e, this.f2011a, new e.a() { // from class: androidx.camera.camera2.internal.y0
                @Override // androidx.camera.camera2.internal.o0.e.a
                public final boolean a(TotalCaptureResult totalCaptureResult) {
                    boolean a9;
                    a9 = o0.a(totalCaptureResult, true);
                    return a9;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Boolean k(TotalCaptureResult totalCaptureResult) {
            return Boolean.FALSE;
        }

        @Override // androidx.camera.camera2.internal.o0.d
        public com.google.common.util.concurrent.d<Boolean> a(TotalCaptureResult totalCaptureResult) {
            if (o0.b(this.f2012b, totalCaptureResult)) {
                if (!this.f2011a.N()) {
                    androidx.camera.core.p1.a("Camera2CapturePipeline", "Turn on torch");
                    this.f2013c = true;
                    return a0.d.a(androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.camera2.internal.z0
                        @Override // androidx.concurrent.futures.c.InterfaceC0024c
                        public final Object a(c.a aVar) {
                            Object h8;
                            h8 = o0.f.this.h(aVar);
                            return h8;
                        }
                    })).f(new a0.a() { // from class: androidx.camera.camera2.internal.x0
                        @Override // a0.a
                        public final com.google.common.util.concurrent.d apply(Object obj) {
                            com.google.common.util.concurrent.d j8;
                            j8 = o0.f.this.j((Void) obj);
                            return j8;
                        }
                    }, this.f2014d).e(new n.a() { // from class: androidx.camera.camera2.internal.a1
                        @Override // n.a
                        public final Object apply(Object obj) {
                            Boolean k8;
                            k8 = o0.f.k((TotalCaptureResult) obj);
                            return k8;
                        }
                    }, z.a.a());
                }
                androidx.camera.core.p1.a("Camera2CapturePipeline", "Torch already on, not turn on");
            }
            return a0.f.h(Boolean.FALSE);
        }

        @Override // androidx.camera.camera2.internal.o0.d
        public boolean b() {
            return this.f2012b == 0;
        }

        @Override // androidx.camera.camera2.internal.o0.d
        public void c() {
            if (this.f2013c) {
                this.f2011a.F().g(null, false);
                androidx.camera.core.p1.a("Camera2CapturePipeline", "Turn off torch");
            }
        }
    }

    static {
        CameraCaptureMetaData$AeState cameraCaptureMetaData$AeState = CameraCaptureMetaData$AeState.CONVERGED;
        CameraCaptureMetaData$AeState cameraCaptureMetaData$AeState2 = CameraCaptureMetaData$AeState.FLASH_REQUIRED;
        CameraCaptureMetaData$AeState cameraCaptureMetaData$AeState3 = CameraCaptureMetaData$AeState.UNKNOWN;
        Set<CameraCaptureMetaData$AeState> unmodifiableSet = Collections.unmodifiableSet(EnumSet.of(cameraCaptureMetaData$AeState, cameraCaptureMetaData$AeState2, cameraCaptureMetaData$AeState3));
        f1978i = unmodifiableSet;
        EnumSet copyOf = EnumSet.copyOf((Collection) unmodifiableSet);
        copyOf.remove(cameraCaptureMetaData$AeState2);
        copyOf.remove(cameraCaptureMetaData$AeState3);
        f1979j = Collections.unmodifiableSet(copyOf);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public o0(t tVar, s.y yVar, y.t0 t0Var, Executor executor) {
        boolean z4 = true;
        this.f1980a = tVar;
        Integer num = (Integer) yVar.a(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        this.f1984e = (num == null || num.intValue() != 2) ? false : false;
        this.f1983d = executor;
        this.f1982c = t0Var;
        this.f1981b = new v.s(t0Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(TotalCaptureResult totalCaptureResult, boolean z4) {
        if (totalCaptureResult == null) {
            return false;
        }
        androidx.camera.camera2.internal.e eVar = new androidx.camera.camera2.internal.e(totalCaptureResult);
        boolean z8 = eVar.i() == CameraCaptureMetaData$AfMode.OFF || eVar.i() == CameraCaptureMetaData$AfMode.UNKNOWN || f1976g.contains(eVar.h());
        boolean z9 = ((Integer) totalCaptureResult.get(CaptureResult.CONTROL_AE_MODE)).intValue() == 0;
        boolean z10 = !z4 ? !(z9 || f1978i.contains(eVar.f())) : !(z9 || f1979j.contains(eVar.f()));
        boolean z11 = (((Integer) totalCaptureResult.get(CaptureResult.CONTROL_AWB_MODE)).intValue() == 0) || f1977h.contains(eVar.b());
        androidx.camera.core.p1.a("Camera2CapturePipeline", "checkCaptureResult, AE=" + eVar.f() + " AF =" + eVar.h() + " AWB=" + eVar.b());
        return z8 && z10 && z11;
    }

    static boolean b(int i8, TotalCaptureResult totalCaptureResult) {
        if (i8 == 0) {
            Integer num = totalCaptureResult != null ? (Integer) totalCaptureResult.get(CaptureResult.CONTROL_AE_STATE) : null;
            return num != null && num.intValue() == 4;
        } else if (i8 != 1) {
            if (i8 == 2) {
                return false;
            }
            throw new AssertionError(i8);
        } else {
            return true;
        }
    }

    private boolean c(int i8) {
        return this.f1981b.a() || this.f1985f == 3 || i8 == 1;
    }

    static com.google.common.util.concurrent.d<TotalCaptureResult> f(long j8, t tVar, e.a aVar) {
        e eVar = new e(j8, aVar);
        tVar.u(eVar);
        return eVar.c();
    }

    public void d(int i8) {
        this.f1985f = i8;
    }

    public com.google.common.util.concurrent.d<List<Void>> e(List<androidx.camera.core.impl.f> list, int i8, int i9, int i10) {
        v.l lVar = new v.l(this.f1982c);
        c cVar = new c(this.f1985f, this.f1983d, this.f1980a, this.f1984e, lVar);
        if (i8 == 0) {
            cVar.f(new b(this.f1980a));
        }
        cVar.f(c(i10) ? new f(this.f1980a, i9, this.f1983d) : new a(this.f1980a, i9, lVar));
        return a0.f.j(cVar.i(list, i9));
    }
}
