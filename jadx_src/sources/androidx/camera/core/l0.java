package androidx.camera.core;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Size;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.impl.v;
import androidx.camera.core.l0;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l0 extends a3 {
    public static final d q = new d();

    /* renamed from: r  reason: collision with root package name */
    private static final Boolean f2704r = null;

    /* renamed from: m  reason: collision with root package name */
    final o0 f2705m;

    /* renamed from: n  reason: collision with root package name */
    private final Object f2706n;

    /* renamed from: o  reason: collision with root package name */
    private a f2707o;

    /* renamed from: p  reason: collision with root package name */
    private DeferrableSurface f2708p;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        default Size a() {
            return null;
        }

        void b(l1 l1Var);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public @interface b {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c implements v.a<l0, androidx.camera.core.impl.i, c> {

        /* renamed from: a  reason: collision with root package name */
        private final androidx.camera.core.impl.n f2709a;

        public c() {
            this(androidx.camera.core.impl.n.P());
        }

        private c(androidx.camera.core.impl.n nVar) {
            this.f2709a = nVar;
            Class cls = (Class) nVar.f(b0.h.f7926x, null);
            if (cls == null || cls.equals(l0.class)) {
                j(l0.class);
                return;
            }
            throw new IllegalArgumentException("Invalid target class configuration for " + this + ": " + cls);
        }

        static c d(Config config) {
            return new c(androidx.camera.core.impl.n.Q(config));
        }

        @Override // androidx.camera.core.g0
        public androidx.camera.core.impl.m a() {
            return this.f2709a;
        }

        public l0 c() {
            if (a().f(androidx.camera.core.impl.l.f2576g, null) == null || a().f(androidx.camera.core.impl.l.f2579j, null) == null) {
                return new l0(b());
            }
            throw new IllegalArgumentException("Cannot use both setTargetResolution and setTargetAspectRatio on the same config.");
        }

        @Override // androidx.camera.core.impl.v.a
        /* renamed from: e */
        public androidx.camera.core.impl.i b() {
            return new androidx.camera.core.impl.i(androidx.camera.core.impl.o.N(this.f2709a));
        }

        public c f(int i8) {
            a().s(androidx.camera.core.impl.i.B, Integer.valueOf(i8));
            return this;
        }

        public c g(Size size) {
            a().s(androidx.camera.core.impl.l.f2580k, size);
            return this;
        }

        public c h(int i8) {
            a().s(androidx.camera.core.impl.v.f2662r, Integer.valueOf(i8));
            return this;
        }

        public c i(int i8) {
            a().s(androidx.camera.core.impl.l.f2576g, Integer.valueOf(i8));
            return this;
        }

        public c j(Class<l0> cls) {
            a().s(b0.h.f7926x, cls);
            if (a().f(b0.h.f7925w, null) == null) {
                k(cls.getCanonicalName() + "-" + UUID.randomUUID());
            }
            return this;
        }

        public c k(String str) {
            a().s(b0.h.f7925w, str);
            return this;
        }

        public c l(Size size) {
            a().s(androidx.camera.core.impl.l.f2579j, size);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        private static final Size f2710a;

        /* renamed from: b  reason: collision with root package name */
        private static final androidx.camera.core.impl.i f2711b;

        static {
            Size size = new Size(640, 480);
            f2710a = size;
            f2711b = new c().g(size).h(1).i(0).b();
        }

        public androidx.camera.core.impl.i a() {
            return f2711b;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public @interface e {
    }

    l0(androidx.camera.core.impl.i iVar) {
        super(iVar);
        this.f2706n = new Object();
        if (((androidx.camera.core.impl.i) g()).L(0) == 1) {
            this.f2705m = new p0();
        } else {
            this.f2705m = new q0(iVar.G(z.a.b()));
        }
        this.f2705m.t(U());
        this.f2705m.u(W());
    }

    private boolean V(CameraInternal cameraInternal) {
        return W() && k(cameraInternal) % 180 != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void X(m2 m2Var, m2 m2Var2) {
        m2Var.k();
        if (m2Var2 != null) {
            m2Var2.k();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void Y(String str, androidx.camera.core.impl.i iVar, Size size, SessionConfig sessionConfig, SessionConfig.SessionError sessionError) {
        P();
        this.f2705m.g();
        if (r(str)) {
            K(Q(str, iVar, size).m());
            v();
        }
    }

    private void b0() {
        CameraInternal d8 = d();
        if (d8 != null) {
            this.f2705m.w(k(d8));
        }
    }

    @Override // androidx.camera.core.a3
    public void C() {
        P();
        this.f2705m.j();
    }

    /* JADX WARN: Type inference failed for: r3v5, types: [androidx.camera.core.impl.v<?>, androidx.camera.core.impl.v] */
    /* JADX WARN: Type inference failed for: r3v6, types: [androidx.camera.core.impl.q, androidx.camera.core.impl.v] */
    @Override // androidx.camera.core.a3
    protected androidx.camera.core.impl.v<?> D(y.q qVar, v.a<?, ?, ?> aVar) {
        Size a9;
        Boolean T = T();
        boolean a10 = qVar.j().a(d0.d.class);
        o0 o0Var = this.f2705m;
        if (T != null) {
            a10 = T.booleanValue();
        }
        o0Var.s(a10);
        synchronized (this.f2706n) {
            a aVar2 = this.f2707o;
            a9 = aVar2 != null ? aVar2.a() : null;
        }
        if (a9 != null) {
            ?? b9 = aVar.b();
            Config.a<Size> aVar3 = androidx.camera.core.impl.l.f2579j;
            if (!b9.b(aVar3)) {
                aVar.a().s(aVar3, a9);
            }
        }
        return aVar.b();
    }

    @Override // androidx.camera.core.a3
    protected Size G(Size size) {
        K(Q(f(), (androidx.camera.core.impl.i) g(), size).m());
        return size;
    }

    @Override // androidx.camera.core.a3
    public void I(Matrix matrix) {
        super.I(matrix);
        this.f2705m.x(matrix);
    }

    @Override // androidx.camera.core.a3
    public void J(Rect rect) {
        super.J(rect);
        this.f2705m.y(rect);
    }

    void P() {
        androidx.camera.core.impl.utils.m.a();
        DeferrableSurface deferrableSurface = this.f2708p;
        if (deferrableSurface != null) {
            deferrableSurface.c();
            this.f2708p = null;
        }
    }

    SessionConfig.b Q(final String str, final androidx.camera.core.impl.i iVar, final Size size) {
        androidx.camera.core.impl.utils.m.a();
        Executor executor = (Executor) androidx.core.util.h.h(iVar.G(z.a.b()));
        boolean z4 = true;
        int S = R() == 1 ? S() : 4;
        final m2 m2Var = iVar.N() != null ? new m2(iVar.N().a(size.getWidth(), size.getHeight(), i(), S, 0L)) : new m2(n1.a(size.getWidth(), size.getHeight(), i(), S));
        boolean V = d() != null ? V(d()) : false;
        int height = V ? size.getHeight() : size.getWidth();
        int width = V ? size.getWidth() : size.getHeight();
        int i8 = U() == 2 ? 1 : 35;
        boolean z8 = i() == 35 && U() == 2;
        if (i() != 35 || ((d() == null || k(d()) == 0) && !Boolean.TRUE.equals(T()))) {
            z4 = false;
        }
        final m2 m2Var2 = (z8 || z4) ? new m2(n1.a(height, width, i8, m2Var.e())) : null;
        if (m2Var2 != null) {
            this.f2705m.v(m2Var2);
        }
        b0();
        m2Var.a(this.f2705m, executor);
        SessionConfig.b o5 = SessionConfig.b.o(iVar);
        DeferrableSurface deferrableSurface = this.f2708p;
        if (deferrableSurface != null) {
            deferrableSurface.c();
        }
        y.h0 h0Var = new y.h0(m2Var.getSurface(), size, i());
        this.f2708p = h0Var;
        h0Var.i().c(new Runnable() { // from class: androidx.camera.core.k0
            @Override // java.lang.Runnable
            public final void run() {
                l0.X(m2.this, m2Var2);
            }
        }, z.a.d());
        o5.k(this.f2708p);
        o5.f(new SessionConfig.c() { // from class: androidx.camera.core.j0
            @Override // androidx.camera.core.impl.SessionConfig.c
            public final void a(SessionConfig sessionConfig, SessionConfig.SessionError sessionError) {
                l0.this.Y(str, iVar, size, sessionConfig, sessionError);
            }
        });
        return o5;
    }

    public int R() {
        return ((androidx.camera.core.impl.i) g()).L(0);
    }

    public int S() {
        return ((androidx.camera.core.impl.i) g()).M(6);
    }

    public Boolean T() {
        return ((androidx.camera.core.impl.i) g()).O(f2704r);
    }

    public int U() {
        return ((androidx.camera.core.impl.i) g()).P(1);
    }

    public boolean W() {
        return ((androidx.camera.core.impl.i) g()).Q(Boolean.FALSE).booleanValue();
    }

    public void a0(Executor executor, final a aVar) {
        synchronized (this.f2706n) {
            this.f2705m.r(executor, new a() { // from class: androidx.camera.core.i0
                @Override // androidx.camera.core.l0.a
                public final void b(l1 l1Var) {
                    l0.a.this.b(l1Var);
                }
            });
            if (this.f2707o == null) {
                t();
            }
            this.f2707o = aVar;
        }
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [androidx.camera.core.impl.v<?>, androidx.camera.core.impl.v] */
    @Override // androidx.camera.core.a3
    public androidx.camera.core.impl.v<?> h(boolean z4, UseCaseConfigFactory useCaseConfigFactory) {
        Config a9 = useCaseConfigFactory.a(UseCaseConfigFactory.CaptureType.IMAGE_ANALYSIS, 1);
        if (z4) {
            a9 = Config.A(a9, q.a());
        }
        if (a9 == null) {
            return null;
        }
        return p(a9).b();
    }

    @Override // androidx.camera.core.a3
    public j2 l() {
        return super.l();
    }

    @Override // androidx.camera.core.a3
    public v.a<?, ?, ?> p(Config config) {
        return c.d(config);
    }

    public String toString() {
        return "ImageAnalysis:" + j();
    }

    @Override // androidx.camera.core.a3
    public void z() {
        this.f2705m.f();
    }
}
