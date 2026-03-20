package androidx.camera.core;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import androidx.camera.core.SurfaceOutput;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.impl.g;
import androidx.camera.core.impl.v;
import androidx.camera.core.y1;
import androidx.camera.core.z2;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y1 extends a3 {

    /* renamed from: t  reason: collision with root package name */
    public static final c f2879t = new c();

    /* renamed from: u  reason: collision with root package name */
    private static final Executor f2880u = z.a.d();

    /* renamed from: m  reason: collision with root package name */
    private d f2881m;

    /* renamed from: n  reason: collision with root package name */
    private Executor f2882n;

    /* renamed from: o  reason: collision with root package name */
    private DeferrableSurface f2883o;

    /* renamed from: p  reason: collision with root package name */
    z2 f2884p;
    private Size q;

    /* renamed from: r  reason: collision with root package name */
    private g0.p f2885r;

    /* renamed from: s  reason: collision with root package name */
    private g0.s f2886s;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends y.h {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ y.e0 f2887a;

        a(y.e0 e0Var) {
            this.f2887a = e0Var;
        }

        @Override // y.h
        public void b(y.j jVar) {
            super.b(jVar);
            if (this.f2887a.a(new b0.c(jVar))) {
                y1.this.x();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements v.a<y1, androidx.camera.core.impl.p, b> {

        /* renamed from: a  reason: collision with root package name */
        private final androidx.camera.core.impl.n f2889a;

        public b() {
            this(androidx.camera.core.impl.n.P());
        }

        private b(androidx.camera.core.impl.n nVar) {
            this.f2889a = nVar;
            Class cls = (Class) nVar.f(b0.h.f7926x, null);
            if (cls == null || cls.equals(y1.class)) {
                h(y1.class);
                return;
            }
            throw new IllegalArgumentException("Invalid target class configuration for " + this + ": " + cls);
        }

        static b d(Config config) {
            return new b(androidx.camera.core.impl.n.Q(config));
        }

        @Override // androidx.camera.core.g0
        public androidx.camera.core.impl.m a() {
            return this.f2889a;
        }

        public y1 c() {
            if (a().f(androidx.camera.core.impl.l.f2576g, null) == null || a().f(androidx.camera.core.impl.l.f2579j, null) == null) {
                return new y1(b());
            }
            throw new IllegalArgumentException("Cannot use both setTargetResolution and setTargetAspectRatio on the same config.");
        }

        @Override // androidx.camera.core.impl.v.a
        /* renamed from: e */
        public androidx.camera.core.impl.p b() {
            return new androidx.camera.core.impl.p(androidx.camera.core.impl.o.N(this.f2889a));
        }

        public b f(int i8) {
            a().s(androidx.camera.core.impl.v.f2662r, Integer.valueOf(i8));
            return this;
        }

        public b g(int i8) {
            a().s(androidx.camera.core.impl.l.f2576g, Integer.valueOf(i8));
            return this;
        }

        public b h(Class<y1> cls) {
            a().s(b0.h.f7926x, cls);
            if (a().f(b0.h.f7925w, null) == null) {
                i(cls.getCanonicalName() + "-" + UUID.randomUUID());
            }
            return this;
        }

        public b i(String str) {
            a().s(b0.h.f7925w, str);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {

        /* renamed from: a  reason: collision with root package name */
        private static final androidx.camera.core.impl.p f2890a = new b().f(2).g(0).b();

        public androidx.camera.core.impl.p a() {
            return f2890a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        void a(z2 z2Var);
    }

    y1(androidx.camera.core.impl.p pVar) {
        super(pVar);
        this.f2882n = f2880u;
    }

    private void O(SessionConfig.b bVar, final String str, final androidx.camera.core.impl.p pVar, final Size size) {
        if (this.f2881m != null) {
            bVar.k(this.f2883o);
        }
        bVar.f(new SessionConfig.c() { // from class: androidx.camera.core.v1
            @Override // androidx.camera.core.impl.SessionConfig.c
            public final void a(SessionConfig sessionConfig, SessionConfig.SessionError sessionError) {
                y1.this.T(str, pVar, size, sessionConfig, sessionError);
            }
        });
    }

    private void P() {
        DeferrableSurface deferrableSurface = this.f2883o;
        if (deferrableSurface != null) {
            deferrableSurface.c();
            this.f2883o = null;
        }
        g0.s sVar = this.f2886s;
        if (sVar != null) {
            sVar.f();
            this.f2886s = null;
        }
        this.f2884p = null;
    }

    private SessionConfig.b R(String str, androidx.camera.core.impl.p pVar, Size size) {
        androidx.camera.core.impl.utils.m.a();
        androidx.core.util.h.h(this.f2885r);
        CameraInternal d8 = d();
        androidx.core.util.h.h(d8);
        P();
        this.f2886s = new g0.s(d8, SurfaceOutput.GlTransformOptions.USE_SURFACE_TEXTURE_TRANSFORM, this.f2885r);
        Matrix matrix = new Matrix();
        Rect S = S(size);
        Objects.requireNonNull(S);
        g0.k kVar = new g0.k(1, size, 34, matrix, true, S, k(d8), false);
        g0.l a9 = g0.l.a(Collections.singletonList(kVar));
        this.f2883o = kVar;
        this.f2884p = this.f2886s.i(a9).b().get(0).u(d8);
        if (this.f2881m != null) {
            V();
        }
        SessionConfig.b o5 = SessionConfig.b.o(pVar);
        O(o5, str, pVar, size);
        return o5;
    }

    private Rect S(Size size) {
        if (q() != null) {
            return q();
        }
        if (size != null) {
            return new Rect(0, 0, size.getWidth(), size.getHeight());
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void T(String str, androidx.camera.core.impl.p pVar, Size size, SessionConfig sessionConfig, SessionConfig.SessionError sessionError) {
        if (r(str)) {
            K(Q(str, pVar, size).m());
            v();
        }
    }

    private void V() {
        final d dVar = (d) androidx.core.util.h.h(this.f2881m);
        final z2 z2Var = (z2) androidx.core.util.h.h(this.f2884p);
        this.f2882n.execute(new Runnable() { // from class: androidx.camera.core.x1
            @Override // java.lang.Runnable
            public final void run() {
                y1.d.this.a(z2Var);
            }
        });
        W();
    }

    private void W() {
        CameraInternal d8 = d();
        d dVar = this.f2881m;
        Rect S = S(this.q);
        z2 z2Var = this.f2884p;
        if (d8 == null || dVar == null || S == null || z2Var == null) {
            return;
        }
        z2Var.x(z2.g.d(S, k(d8), b()));
    }

    private void a0(String str, androidx.camera.core.impl.p pVar, Size size) {
        K(Q(str, pVar, size).m());
    }

    @Override // androidx.camera.core.a3
    public void C() {
        P();
    }

    /* JADX WARN: Type inference failed for: r3v5, types: [androidx.camera.core.impl.v<?>, androidx.camera.core.impl.v] */
    @Override // androidx.camera.core.a3
    protected androidx.camera.core.impl.v<?> D(y.q qVar, v.a<?, ?, ?> aVar) {
        androidx.camera.core.impl.m a9;
        Config.a<Integer> aVar2;
        int i8;
        if (aVar.a().f(androidx.camera.core.impl.p.C, null) != null) {
            a9 = aVar.a();
            aVar2 = androidx.camera.core.impl.k.f2575f;
            i8 = 35;
        } else {
            a9 = aVar.a();
            aVar2 = androidx.camera.core.impl.k.f2575f;
            i8 = 34;
        }
        a9.s(aVar2, Integer.valueOf(i8));
        return aVar.b();
    }

    @Override // androidx.camera.core.a3
    protected Size G(Size size) {
        this.q = size;
        a0(f(), (androidx.camera.core.impl.p) g(), this.q);
        return size;
    }

    @Override // androidx.camera.core.a3
    public void J(Rect rect) {
        super.J(rect);
        W();
    }

    SessionConfig.b Q(String str, androidx.camera.core.impl.p pVar, Size size) {
        if (this.f2885r != null) {
            return R(str, pVar, size);
        }
        androidx.camera.core.impl.utils.m.a();
        SessionConfig.b o5 = SessionConfig.b.o(pVar);
        y.v L = pVar.L(null);
        P();
        z2 z2Var = new z2(size, d(), pVar.N(false));
        this.f2884p = z2Var;
        if (this.f2881m != null) {
            V();
        }
        if (L != null) {
            g.a aVar = new g.a();
            final HandlerThread handlerThread = new HandlerThread("CameraX-preview_processing");
            handlerThread.start();
            String num = Integer.toString(aVar.hashCode());
            i2 i2Var = new i2(size.getWidth(), size.getHeight(), pVar.m(), new Handler(handlerThread.getLooper()), aVar, L, z2Var.k(), num);
            o5.d(i2Var.s());
            i2Var.i().c(new Runnable() { // from class: androidx.camera.core.w1
                @Override // java.lang.Runnable
                public final void run() {
                    handlerThread.quitSafely();
                }
            }, z.a.a());
            this.f2883o = i2Var;
            o5.l(num, Integer.valueOf(aVar.e()));
        } else {
            y.e0 M = pVar.M(null);
            if (M != null) {
                o5.d(new a(M));
            }
            this.f2883o = z2Var.k();
        }
        O(o5, str, pVar, size);
        return o5;
    }

    public void X(g0.p pVar) {
        this.f2885r = pVar;
    }

    public void Y(d dVar) {
        Z(f2880u, dVar);
    }

    public void Z(Executor executor, d dVar) {
        androidx.camera.core.impl.utils.m.a();
        if (dVar == null) {
            this.f2881m = null;
            u();
            return;
        }
        this.f2881m = dVar;
        this.f2882n = executor;
        t();
        if (c() != null) {
            a0(f(), (androidx.camera.core.impl.p) g(), c());
            v();
        }
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [androidx.camera.core.impl.v<?>, androidx.camera.core.impl.v] */
    @Override // androidx.camera.core.a3
    public androidx.camera.core.impl.v<?> h(boolean z4, UseCaseConfigFactory useCaseConfigFactory) {
        Config a9 = useCaseConfigFactory.a(UseCaseConfigFactory.CaptureType.PREVIEW, 1);
        if (z4) {
            a9 = Config.A(a9, f2879t.a());
        }
        if (a9 == null) {
            return null;
        }
        return p(a9).b();
    }

    @Override // androidx.camera.core.a3
    public v.a<?, ?, ?> p(Config config) {
        return b.d(config);
    }

    public String toString() {
        return "Preview:" + j();
    }
}
