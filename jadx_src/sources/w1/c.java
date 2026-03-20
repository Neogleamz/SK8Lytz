package w1;

import android.os.Trace;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c {
    public static void a(String str, int i8) {
        Trace.beginAsyncSection(str, i8);
    }

    public static void b(String str, int i8) {
        Trace.endAsyncSection(str, i8);
    }
}
