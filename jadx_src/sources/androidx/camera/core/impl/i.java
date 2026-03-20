package androidx.camera.core.impl;

import androidx.camera.core.impl.Config;
import androidx.camera.core.l0;
import androidx.camera.core.m1;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i implements v<l0>, l, b0.i {
    public static final Config.a<Integer> B = Config.a.a("camerax.core.imageAnalysis.backpressureStrategy", l0.b.class);
    public static final Config.a<Integer> C = Config.a.a("camerax.core.imageAnalysis.imageQueueDepth", Integer.TYPE);
    public static final Config.a<m1> D = Config.a.a("camerax.core.imageAnalysis.imageReaderProxyProvider", m1.class);
    public static final Config.a<Integer> E = Config.a.a("camerax.core.imageAnalysis.outputImageFormat", l0.e.class);
    public static final Config.a<Boolean> F = Config.a.a("camerax.core.imageAnalysis.onePixelShiftEnabled", Boolean.class);
    public static final Config.a<Boolean> G = Config.a.a("camerax.core.imageAnalysis.outputImageRotationEnabled", Boolean.class);
    private final o A;

    public i(o oVar) {
        this.A = oVar;
    }

    public int L(int i8) {
        return ((Integer) f(B, Integer.valueOf(i8))).intValue();
    }

    public int M(int i8) {
        return ((Integer) f(C, Integer.valueOf(i8))).intValue();
    }

    public m1 N() {
        return (m1) f(D, null);
    }

    public Boolean O(Boolean bool) {
        return (Boolean) f(F, bool);
    }

    public int P(int i8) {
        return ((Integer) f(E, Integer.valueOf(i8))).intValue();
    }

    public Boolean Q(Boolean bool) {
        return (Boolean) f(G, bool);
    }

    @Override // androidx.camera.core.impl.q
    public Config l() {
        return this.A;
    }

    @Override // androidx.camera.core.impl.k
    public int m() {
        return 35;
    }
}
