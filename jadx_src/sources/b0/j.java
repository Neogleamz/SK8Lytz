package b0;

import androidx.camera.core.a3;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.q;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface j extends q {

    /* renamed from: z  reason: collision with root package name */
    public static final Config.a<a3.b> f7928z = Config.a.a("camerax.core.useCaseEventCallback", a3.b.class);

    default a3.b I(a3.b bVar) {
        return (a3.b) f(f7928z, bVar);
    }
}
