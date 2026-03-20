package androidx.camera.core.impl.utils;

import android.os.Handler;
import android.os.Looper;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k {

    /* renamed from: a  reason: collision with root package name */
    private static volatile Handler f2656a;

    private k() {
    }

    public static Handler a() {
        if (f2656a != null) {
            return f2656a;
        }
        synchronized (k.class) {
            if (f2656a == null) {
                f2656a = androidx.core.os.i.a(Looper.getMainLooper());
            }
        }
        return f2656a;
    }
}
