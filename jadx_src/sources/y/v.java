package y;

import android.util.Size;
import android.view.Surface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface v {
    void a(Surface surface, int i8);

    default com.google.common.util.concurrent.d<Void> b() {
        return a0.f.h(null);
    }

    void c(f0 f0Var);

    default void close() {
    }

    void d(Size size);
}
