package androidx.camera.core.impl;

import android.util.Range;
import androidx.camera.core.a3;
import androidx.camera.core.g0;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface v<T extends a3> extends b0.h<T>, b0.j, k {

    /* renamed from: n  reason: collision with root package name */
    public static final Config.a<SessionConfig> f2659n = Config.a.a("camerax.core.useCase.defaultSessionConfig", SessionConfig.class);

    /* renamed from: o  reason: collision with root package name */
    public static final Config.a<f> f2660o = Config.a.a("camerax.core.useCase.defaultCaptureConfig", f.class);

    /* renamed from: p  reason: collision with root package name */
    public static final Config.a<SessionConfig.d> f2661p = Config.a.a("camerax.core.useCase.sessionConfigUnpacker", SessionConfig.d.class);
    public static final Config.a<f.b> q = Config.a.a("camerax.core.useCase.captureConfigUnpacker", f.b.class);

    /* renamed from: r  reason: collision with root package name */
    public static final Config.a<Integer> f2662r = Config.a.a("camerax.core.useCase.surfaceOccupancyPriority", Integer.TYPE);

    /* renamed from: s  reason: collision with root package name */
    public static final Config.a<androidx.camera.core.t> f2663s = Config.a.a("camerax.core.useCase.cameraSelector", androidx.camera.core.t.class);

    /* renamed from: t  reason: collision with root package name */
    public static final Config.a<Range<Integer>> f2664t = Config.a.a("camerax.core.useCase.targetFrameRate", androidx.camera.core.t.class);

    /* renamed from: u  reason: collision with root package name */
    public static final Config.a<Boolean> f2665u = Config.a.a("camerax.core.useCase.zslDisabled", Boolean.TYPE);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a<T extends a3, C extends v<T>, B> extends g0<T> {
        C b();
    }

    default int D(int i8) {
        return ((Integer) f(f2662r, Integer.valueOf(i8))).intValue();
    }

    default androidx.camera.core.t H(androidx.camera.core.t tVar) {
        return (androidx.camera.core.t) f(f2663s, tVar);
    }

    default SessionConfig.d J(SessionConfig.d dVar) {
        return (SessionConfig.d) f(f2661p, dVar);
    }

    default SessionConfig n(SessionConfig sessionConfig) {
        return (SessionConfig) f(f2659n, sessionConfig);
    }

    default f.b p(f.b bVar) {
        return (f.b) f(q, bVar);
    }

    default boolean r(boolean z4) {
        return ((Boolean) f(f2665u, Boolean.valueOf(z4))).booleanValue();
    }

    default f t(f fVar) {
        return (f) f(f2660o, fVar);
    }

    default Range<Integer> z(Range<Integer> range) {
        return (Range) f(f2664t, range);
    }
}
