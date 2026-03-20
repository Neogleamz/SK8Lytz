package androidx.camera.core.impl.utils;

import android.os.Looper;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m {
    public static void a() {
        androidx.core.util.h.k(b(), "Not in application's main thread");
    }

    public static boolean b() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
