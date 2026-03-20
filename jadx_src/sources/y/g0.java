package y;

import android.view.Surface;
import androidx.camera.core.l1;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface g0 {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(g0 g0Var);
    }

    void a(a aVar, Executor executor);

    l1 acquireLatestImage();

    int c();

    void close();

    void d();

    int e();

    l1 f();

    int getHeight();

    Surface getSurface();

    int getWidth();
}
