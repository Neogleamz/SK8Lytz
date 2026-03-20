package androidx.camera.core;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Size;
import androidx.camera.core.impl.CameraControlInternal;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.impl.v;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a3 {

    /* renamed from: d  reason: collision with root package name */
    private androidx.camera.core.impl.v<?> f2235d;

    /* renamed from: e  reason: collision with root package name */
    private androidx.camera.core.impl.v<?> f2236e;

    /* renamed from: f  reason: collision with root package name */
    private androidx.camera.core.impl.v<?> f2237f;

    /* renamed from: g  reason: collision with root package name */
    private Size f2238g;

    /* renamed from: h  reason: collision with root package name */
    private androidx.camera.core.impl.v<?> f2239h;

    /* renamed from: i  reason: collision with root package name */
    private Rect f2240i;

    /* renamed from: k  reason: collision with root package name */
    private CameraInternal f2242k;

    /* renamed from: a  reason: collision with root package name */
    private final Set<d> f2232a = new HashSet();

    /* renamed from: b  reason: collision with root package name */
    private final Object f2233b = new Object();

    /* renamed from: c  reason: collision with root package name */
    private c f2234c = c.INACTIVE;

    /* renamed from: j  reason: collision with root package name */
    private Matrix f2241j = new Matrix();

    /* renamed from: l  reason: collision with root package name */
    private SessionConfig f2243l = SessionConfig.a();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f2244a;

        static {
            int[] iArr = new int[c.values().length];
            f2244a = iArr;
            try {
                iArr[c.INACTIVE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f2244a[c.ACTIVE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a();

        void b(s sVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum c {
        ACTIVE,
        INACTIVE
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        void c(a3 a3Var);

        void d(a3 a3Var);

        void f(a3 a3Var);

        void n(a3 a3Var);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public a3(androidx.camera.core.impl.v<?> vVar) {
        this.f2236e = vVar;
        this.f2237f = vVar;
    }

    private void H(d dVar) {
        this.f2232a.remove(dVar);
    }

    private void a(d dVar) {
        this.f2232a.add(dVar);
    }

    protected void A() {
    }

    public void B(CameraInternal cameraInternal) {
        C();
        b I = this.f2237f.I(null);
        if (I != null) {
            I.a();
        }
        synchronized (this.f2233b) {
            androidx.core.util.h.a(cameraInternal == this.f2242k);
            H(this.f2242k);
            this.f2242k = null;
        }
        this.f2238g = null;
        this.f2240i = null;
        this.f2237f = this.f2236e;
        this.f2235d = null;
        this.f2239h = null;
    }

    public void C() {
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [androidx.camera.core.impl.v<?>, androidx.camera.core.impl.v] */
    protected androidx.camera.core.impl.v<?> D(y.q qVar, v.a<?, ?, ?> aVar) {
        return aVar.b();
    }

    public void E() {
        A();
    }

    public void F() {
    }

    protected abstract Size G(Size size);

    public void I(Matrix matrix) {
        this.f2241j = new Matrix(matrix);
    }

    public void J(Rect rect) {
        this.f2240i = rect;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void K(SessionConfig sessionConfig) {
        this.f2243l = sessionConfig;
        for (DeferrableSurface deferrableSurface : sessionConfig.k()) {
            if (deferrableSurface.e() == null) {
                deferrableSurface.o(getClass());
            }
        }
    }

    public void L(Size size) {
        this.f2238g = G(size);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int b() {
        return ((androidx.camera.core.impl.l) this.f2237f).v(-1);
    }

    public Size c() {
        return this.f2238g;
    }

    public CameraInternal d() {
        CameraInternal cameraInternal;
        synchronized (this.f2233b) {
            cameraInternal = this.f2242k;
        }
        return cameraInternal;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CameraControlInternal e() {
        synchronized (this.f2233b) {
            CameraInternal cameraInternal = this.f2242k;
            if (cameraInternal == null) {
                return CameraControlInternal.f2459a;
            }
            return cameraInternal.h();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String f() {
        CameraInternal d8 = d();
        return ((CameraInternal) androidx.core.util.h.i(d8, "No camera attached to use case: " + this)).m().c();
    }

    public androidx.camera.core.impl.v<?> g() {
        return this.f2237f;
    }

    public abstract androidx.camera.core.impl.v<?> h(boolean z4, UseCaseConfigFactory useCaseConfigFactory);

    public int i() {
        return this.f2237f.m();
    }

    public String j() {
        androidx.camera.core.impl.v<?> vVar = this.f2237f;
        String w8 = vVar.w("<UnknownUseCase-" + hashCode() + ">");
        Objects.requireNonNull(w8);
        return w8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int k(CameraInternal cameraInternal) {
        return cameraInternal.m().g(o());
    }

    public j2 l() {
        return m();
    }

    protected j2 m() {
        CameraInternal d8 = d();
        Size c9 = c();
        if (d8 == null || c9 == null) {
            return null;
        }
        Rect q = q();
        if (q == null) {
            q = new Rect(0, 0, c9.getWidth(), c9.getHeight());
        }
        return j2.a(c9, q, k(d8));
    }

    public SessionConfig n() {
        return this.f2243l;
    }

    @SuppressLint({"WrongConstant"})
    protected int o() {
        return ((androidx.camera.core.impl.l) this.f2237f).K(0);
    }

    public abstract v.a<?, ?, ?> p(Config config);

    public Rect q() {
        return this.f2240i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean r(String str) {
        if (d() == null) {
            return false;
        }
        return Objects.equals(str, f());
    }

    public androidx.camera.core.impl.v<?> s(y.q qVar, androidx.camera.core.impl.v<?> vVar, androidx.camera.core.impl.v<?> vVar2) {
        androidx.camera.core.impl.n P;
        if (vVar2 != null) {
            P = androidx.camera.core.impl.n.Q(vVar2);
            P.R(b0.h.f7925w);
        } else {
            P = androidx.camera.core.impl.n.P();
        }
        for (Config.a<?> aVar : this.f2236e.e()) {
            P.o(aVar, this.f2236e.g(aVar), this.f2236e.a(aVar));
        }
        if (vVar != null) {
            for (Config.a<?> aVar2 : vVar.e()) {
                if (!aVar2.c().equals(b0.h.f7925w.c())) {
                    P.o(aVar2, vVar.g(aVar2), vVar.a(aVar2));
                }
            }
        }
        if (P.b(androidx.camera.core.impl.l.f2579j)) {
            Config.a<Integer> aVar3 = androidx.camera.core.impl.l.f2576g;
            if (P.b(aVar3)) {
                P.R(aVar3);
            }
        }
        return D(qVar, p(P));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void t() {
        this.f2234c = c.ACTIVE;
        w();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void u() {
        this.f2234c = c.INACTIVE;
        w();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void v() {
        for (d dVar : this.f2232a) {
            dVar.d(this);
        }
    }

    public final void w() {
        int i8 = a.f2244a[this.f2234c.ordinal()];
        if (i8 == 1) {
            for (d dVar : this.f2232a) {
                dVar.n(this);
            }
        } else if (i8 == 2) {
            for (d dVar2 : this.f2232a) {
                dVar2.c(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void x() {
        for (d dVar : this.f2232a) {
            dVar.f(this);
        }
    }

    @SuppressLint({"WrongConstant"})
    public void y(CameraInternal cameraInternal, androidx.camera.core.impl.v<?> vVar, androidx.camera.core.impl.v<?> vVar2) {
        synchronized (this.f2233b) {
            this.f2242k = cameraInternal;
            a(cameraInternal);
        }
        this.f2235d = vVar;
        this.f2239h = vVar2;
        androidx.camera.core.impl.v<?> s8 = s(cameraInternal.m(), this.f2235d, this.f2239h);
        this.f2237f = s8;
        b I = s8.I(null);
        if (I != null) {
            I.b(cameraInternal.m());
        }
        z();
    }

    public void z() {
    }
}
