package androidx.camera.core;

import android.os.Handler;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.UseCaseConfigFactory;
import java.util.UUID;
import java.util.concurrent.Executor;
import y.o;
import y.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y implements b0.h<x> {
    static final Config.a<p.a> B = Config.a.a("camerax.core.appConfig.cameraFactoryProvider", p.a.class);
    static final Config.a<o.a> C = Config.a.a("camerax.core.appConfig.deviceSurfaceManagerProvider", o.a.class);
    static final Config.a<UseCaseConfigFactory.b> D = Config.a.a("camerax.core.appConfig.useCaseConfigFactoryProvider", UseCaseConfigFactory.b.class);
    static final Config.a<Executor> E = Config.a.a("camerax.core.appConfig.cameraExecutor", Executor.class);
    static final Config.a<Handler> F = Config.a.a("camerax.core.appConfig.schedulerHandler", Handler.class);
    static final Config.a<Integer> G = Config.a.a("camerax.core.appConfig.minimumLoggingLevel", Integer.TYPE);
    static final Config.a<t> H = Config.a.a("camerax.core.appConfig.availableCamerasLimiter", t.class);
    private final androidx.camera.core.impl.o A;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final androidx.camera.core.impl.n f2874a;

        public a() {
            this(androidx.camera.core.impl.n.P());
        }

        private a(androidx.camera.core.impl.n nVar) {
            this.f2874a = nVar;
            Class cls = (Class) nVar.f(b0.h.f7926x, null);
            if (cls == null || cls.equals(x.class)) {
                e(x.class);
                return;
            }
            throw new IllegalArgumentException("Invalid target class configuration for " + this + ": " + cls);
        }

        private androidx.camera.core.impl.m b() {
            return this.f2874a;
        }

        public y a() {
            return new y(androidx.camera.core.impl.o.N(this.f2874a));
        }

        public a c(p.a aVar) {
            b().s(y.B, aVar);
            return this;
        }

        public a d(o.a aVar) {
            b().s(y.C, aVar);
            return this;
        }

        public a e(Class<x> cls) {
            b().s(b0.h.f7926x, cls);
            if (b().f(b0.h.f7925w, null) == null) {
                f(cls.getCanonicalName() + "-" + UUID.randomUUID());
            }
            return this;
        }

        public a f(String str) {
            b().s(b0.h.f7925w, str);
            return this;
        }

        public a g(UseCaseConfigFactory.b bVar) {
            b().s(y.D, bVar);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        y getCameraXConfig();
    }

    y(androidx.camera.core.impl.o oVar) {
        this.A = oVar;
    }

    public t L(t tVar) {
        return (t) this.A.f(H, tVar);
    }

    public Executor M(Executor executor) {
        return (Executor) this.A.f(E, executor);
    }

    public p.a N(p.a aVar) {
        return (p.a) this.A.f(B, aVar);
    }

    public o.a O(o.a aVar) {
        return (o.a) this.A.f(C, aVar);
    }

    public Handler P(Handler handler) {
        return (Handler) this.A.f(F, handler);
    }

    public UseCaseConfigFactory.b Q(UseCaseConfigFactory.b bVar) {
        return (UseCaseConfigFactory.b) this.A.f(D, bVar);
    }

    @Override // androidx.camera.core.impl.q
    public Config l() {
        return this.A;
    }
}
