package w1;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Trace;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    private static long f23416a;

    /* renamed from: b  reason: collision with root package name */
    private static Method f23417b;

    /* renamed from: c  reason: collision with root package name */
    private static Method f23418c;

    /* renamed from: d  reason: collision with root package name */
    private static Method f23419d;

    @SuppressLint({"NewApi"})
    public static void a(String str, int i8) {
        try {
            if (f23418c == null) {
                c.a(str, i8);
                return;
            }
        } catch (NoClassDefFoundError | NoSuchMethodError unused) {
        }
        b(str, i8);
    }

    private static void b(String str, int i8) {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                if (f23418c == null) {
                    f23418c = Trace.class.getMethod("asyncTraceBegin", Long.TYPE, String.class, Integer.TYPE);
                }
                f23418c.invoke(null, Long.valueOf(f23416a), str, Integer.valueOf(i8));
            } catch (Exception e8) {
                g("asyncTraceBegin", e8);
            }
        }
    }

    public static void c(String str) {
        if (Build.VERSION.SDK_INT >= 18) {
            b.a(str);
        }
    }

    @SuppressLint({"NewApi"})
    public static void d(String str, int i8) {
        try {
            if (f23419d == null) {
                c.b(str, i8);
                return;
            }
        } catch (NoClassDefFoundError | NoSuchMethodError unused) {
        }
        e(str, i8);
    }

    private static void e(String str, int i8) {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                if (f23419d == null) {
                    f23419d = Trace.class.getMethod("asyncTraceEnd", Long.TYPE, String.class, Integer.TYPE);
                }
                f23419d.invoke(null, Long.valueOf(f23416a), str, Integer.valueOf(i8));
            } catch (Exception e8) {
                g("asyncTraceEnd", e8);
            }
        }
    }

    public static void f() {
        if (Build.VERSION.SDK_INT >= 18) {
            b.b();
        }
    }

    private static void g(String str, Exception exc) {
        if (exc instanceof InvocationTargetException) {
            Throwable cause = exc.getCause();
            if (!(cause instanceof RuntimeException)) {
                throw new RuntimeException(cause);
            }
            throw ((RuntimeException) cause);
        }
        Log.v("Trace", "Unable to call " + str + " via reflection", exc);
    }

    @SuppressLint({"NewApi"})
    public static boolean h() {
        try {
            if (f23417b == null) {
                return Trace.isEnabled();
            }
        } catch (NoClassDefFoundError | NoSuchMethodError unused) {
        }
        return i();
    }

    private static boolean i() {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                if (f23417b == null) {
                    f23416a = Trace.class.getField("TRACE_TAG_APP").getLong(null);
                    f23417b = Trace.class.getMethod("isTagEnabled", Long.TYPE);
                }
                return ((Boolean) f23417b.invoke(null, Long.valueOf(f23416a))).booleanValue();
            } catch (Exception e8) {
                g("isTagEnabled", e8);
            }
        }
        return false;
    }
}
