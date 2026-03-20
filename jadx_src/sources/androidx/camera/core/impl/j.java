package androidx.camera.core.impl;

import androidx.camera.core.e1;
import androidx.camera.core.impl.Config;
import androidx.camera.core.m1;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j implements v<e1>, l, b0.g {
    public static final Config.a<Integer> B;
    public static final Config.a<Integer> C;
    public static final Config.a<y.u> D;
    public static final Config.a<y.v> E;
    public static final Config.a<Integer> F;
    public static final Config.a<Integer> G;
    public static final Config.a<m1> H;
    public static final Config.a<Boolean> I;
    public static final Config.a<Integer> J;
    public static final Config.a<Integer> K;
    private final o A;

    static {
        Class cls = Integer.TYPE;
        B = Config.a.a("camerax.core.imageCapture.captureMode", cls);
        C = Config.a.a("camerax.core.imageCapture.flashMode", cls);
        D = Config.a.a("camerax.core.imageCapture.captureBundle", y.u.class);
        E = Config.a.a("camerax.core.imageCapture.captureProcessor", y.v.class);
        F = Config.a.a("camerax.core.imageCapture.bufferFormat", Integer.class);
        G = Config.a.a("camerax.core.imageCapture.maxCaptureStages", Integer.class);
        H = Config.a.a("camerax.core.imageCapture.imageReaderProxyProvider", m1.class);
        I = Config.a.a("camerax.core.imageCapture.useSoftwareJpegEncoder", Boolean.TYPE);
        J = Config.a.a("camerax.core.imageCapture.flashType", cls);
        K = Config.a.a("camerax.core.imageCapture.jpegCompressionQuality", cls);
    }

    public j(o oVar) {
        this.A = oVar;
    }

    public y.u L(y.u uVar) {
        return (y.u) f(D, uVar);
    }

    public int M() {
        return ((Integer) a(B)).intValue();
    }

    public y.v N(y.v vVar) {
        return (y.v) f(E, vVar);
    }

    public int O(int i8) {
        return ((Integer) f(C, Integer.valueOf(i8))).intValue();
    }

    public int P(int i8) {
        return ((Integer) f(J, Integer.valueOf(i8))).intValue();
    }

    public m1 Q() {
        return (m1) f(H, null);
    }

    public Executor R(Executor executor) {
        return (Executor) f(b0.g.f7924v, executor);
    }

    public int S() {
        return ((Integer) a(K)).intValue();
    }

    public int T(int i8) {
        return ((Integer) f(G, Integer.valueOf(i8))).intValue();
    }

    public boolean U() {
        return b(B);
    }

    public boolean V() {
        return ((Boolean) f(I, Boolean.FALSE)).booleanValue();
    }

    @Override // androidx.camera.core.impl.q
    public Config l() {
        return this.A;
    }

    @Override // androidx.camera.core.impl.k
    public int m() {
        return ((Integer) a(k.f2575f)).intValue();
    }
}
