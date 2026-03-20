package b0;

import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.q;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface i extends q {

    /* renamed from: y  reason: collision with root package name */
    public static final Config.a<Executor> f7927y = Config.a.a("camerax.core.thread.backgroundExecutor", Executor.class);

    default Executor G(Executor executor) {
        return (Executor) f(f7927y, executor);
    }
}
