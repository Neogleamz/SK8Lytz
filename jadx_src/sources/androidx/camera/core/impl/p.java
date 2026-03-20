package androidx.camera.core.impl;

import androidx.camera.core.impl.Config;
import androidx.camera.core.y1;
import y.e0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p implements v<y1>, l, b0.i {
    public static final Config.a<e0> B = Config.a.a("camerax.core.preview.imageInfoProcessor", e0.class);
    public static final Config.a<y.v> C = Config.a.a("camerax.core.preview.captureProcessor", y.v.class);
    public static final Config.a<Boolean> D = Config.a.a("camerax.core.preview.isRgba8888SurfaceRequired", Boolean.class);
    private final o A;

    public p(o oVar) {
        this.A = oVar;
    }

    public y.v L(y.v vVar) {
        return (y.v) f(C, vVar);
    }

    public e0 M(e0 e0Var) {
        return (e0) f(B, e0Var);
    }

    public boolean N(boolean z4) {
        return ((Boolean) f(D, Boolean.valueOf(z4))).booleanValue();
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
