package y;

import android.os.Handler;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class t {
    public static t a(Executor executor, Handler handler) {
        return new c(executor, handler);
    }

    public abstract Executor b();

    public abstract Handler c();
}
