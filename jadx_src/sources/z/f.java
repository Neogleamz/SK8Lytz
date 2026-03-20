package z;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ScheduledExecutorService;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* renamed from: a  reason: collision with root package name */
    private static volatile ScheduledExecutorService f24501a;

    private f() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ScheduledExecutorService a() {
        if (f24501a != null) {
            return f24501a;
        }
        synchronized (f.class) {
            if (f24501a == null) {
                f24501a = new c(new Handler(Looper.getMainLooper()));
            }
        }
        return f24501a;
    }
}
