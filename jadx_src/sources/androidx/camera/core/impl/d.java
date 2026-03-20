package androidx.camera.core.impl;

import androidx.camera.core.impl.Config;
import y.d0;
import y.v0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface d extends q {

    /* renamed from: a  reason: collision with root package name */
    public static final Config.a<UseCaseConfigFactory> f2542a = Config.a.a("camerax.core.camera.useCaseConfigFactory", UseCaseConfigFactory.class);

    /* renamed from: b  reason: collision with root package name */
    public static final Config.a<d0> f2543b = Config.a.a("camerax.core.camera.compatibilityId", d0.class);

    /* renamed from: c  reason: collision with root package name */
    public static final Config.a<Integer> f2544c = Config.a.a("camerax.core.camera.useCaseCombinationRequiredRule", Integer.class);

    /* renamed from: d  reason: collision with root package name */
    public static final Config.a<v0> f2545d = Config.a.a("camerax.core.camera.SessionProcessor", v0.class);

    /* renamed from: e  reason: collision with root package name */
    public static final Config.a<Boolean> f2546e = Config.a.a("camerax.core.camera.isZslDisabled", Boolean.class);

    d0 F();

    default UseCaseConfigFactory j() {
        return (UseCaseConfigFactory) f(f2542a, UseCaseConfigFactory.f2523a);
    }

    default int x() {
        return ((Integer) f(f2544c, 0)).intValue();
    }

    default v0 y(v0 v0Var) {
        return (v0) f(f2545d, v0Var);
    }
}
