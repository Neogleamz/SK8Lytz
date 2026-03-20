package androidx.core.os;

import android.os.Build;
import android.os.Trace;
import android.util.Log;
import java.lang.reflect.Method;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o {

    /* renamed from: a  reason: collision with root package name */
    private static long f4783a;

    /* renamed from: b  reason: collision with root package name */
    private static Method f4784b;

    /* renamed from: c  reason: collision with root package name */
    private static Method f4785c;

    /* renamed from: d  reason: collision with root package name */
    private static Method f4786d;

    /* renamed from: e  reason: collision with root package name */
    private static Method f4787e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static void a(String str) {
            Trace.beginSection(str);
        }

        static void b() {
            Trace.endSection();
        }
    }

    static {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 18 || i8 >= 29) {
            return;
        }
        try {
            f4783a = Trace.class.getField("TRACE_TAG_APP").getLong(null);
            Class cls = Long.TYPE;
            f4784b = Trace.class.getMethod("isTagEnabled", cls);
            Class cls2 = Integer.TYPE;
            f4785c = Trace.class.getMethod("asyncTraceBegin", cls, String.class, cls2);
            f4786d = Trace.class.getMethod("asyncTraceEnd", cls, String.class, cls2);
            f4787e = Trace.class.getMethod("traceCounter", cls, String.class, cls2);
        } catch (Exception e8) {
            Log.i("TraceCompat", "Unable to initialize via reflection.", e8);
        }
    }

    public static void a(String str) {
        if (Build.VERSION.SDK_INT >= 18) {
            a.a(str);
        }
    }

    public static void b() {
        if (Build.VERSION.SDK_INT >= 18) {
            a.b();
        }
    }
}
