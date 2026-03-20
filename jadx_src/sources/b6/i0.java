package b6;

import android.os.Trace;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i0 {
    public static void a(String str) {
        if (l0.f8063a >= 18) {
            b(str);
        }
    }

    private static void b(String str) {
        Trace.beginSection(str);
    }

    public static void c() {
        if (l0.f8063a >= 18) {
            d();
        }
    }

    private static void d() {
        Trace.endSection();
    }
}
