package b6;

import android.text.TextUtils;
import android.util.Log;
import java.net.UnknownHostException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p {

    /* renamed from: b  reason: collision with root package name */
    private static int f8096b = 0;

    /* renamed from: c  reason: collision with root package name */
    private static boolean f8097c = true;

    /* renamed from: a  reason: collision with root package name */
    private static final Object f8095a = new Object();

    /* renamed from: d  reason: collision with root package name */
    private static a f8098d = a.f8099a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {

        /* renamed from: a  reason: collision with root package name */
        public static final a f8099a = new C0093a();

        /* renamed from: b6.p$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class C0093a implements a {
            C0093a() {
            }

            @Override // b6.p.a
            public void a(String str, String str2) {
                Log.w(str, str2);
            }

            @Override // b6.p.a
            public void b(String str, String str2) {
                Log.e(str, str2);
            }

            @Override // b6.p.a
            public void c(String str, String str2) {
                Log.d(str, str2);
            }

            @Override // b6.p.a
            public void d(String str, String str2) {
                Log.i(str, str2);
            }
        }

        void a(String str, String str2);

        void b(String str, String str2);

        void c(String str, String str2);

        void d(String str, String str2);
    }

    private static String a(String str, Throwable th) {
        String e8 = e(th);
        if (TextUtils.isEmpty(e8)) {
            return str;
        }
        return str + "\n  " + e8.replace("\n", "\n  ") + '\n';
    }

    public static void b(String str, String str2) {
        synchronized (f8095a) {
            if (f8096b == 0) {
                f8098d.c(str, str2);
            }
        }
    }

    public static void c(String str, String str2) {
        synchronized (f8095a) {
            if (f8096b <= 3) {
                f8098d.b(str, str2);
            }
        }
    }

    public static void d(String str, String str2, Throwable th) {
        c(str, a(str2, th));
    }

    public static String e(Throwable th) {
        synchronized (f8095a) {
            if (th == null) {
                return null;
            }
            if (h(th)) {
                return "UnknownHostException (no network)";
            }
            if (f8097c) {
                return Log.getStackTraceString(th).trim().replace("\t", "    ");
            }
            return th.getMessage();
        }
    }

    public static void f(String str, String str2) {
        synchronized (f8095a) {
            if (f8096b <= 1) {
                f8098d.d(str, str2);
            }
        }
    }

    public static void g(String str, String str2, Throwable th) {
        f(str, a(str2, th));
    }

    private static boolean h(Throwable th) {
        while (th != null) {
            if (th instanceof UnknownHostException) {
                return true;
            }
            th = th.getCause();
        }
        return false;
    }

    public static void i(String str, String str2) {
        synchronized (f8095a) {
            if (f8096b <= 2) {
                f8098d.a(str, str2);
            }
        }
    }

    public static void j(String str, String str2, Throwable th) {
        i(str, a(str2, th));
    }
}
