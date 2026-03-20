package androidx.camera.core;

import android.os.Build;
import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p1 {

    /* renamed from: a  reason: collision with root package name */
    private static int f2773a = 3;

    public static void a(String str, String str2) {
        String j8 = j(str);
        if (g(j8, 3)) {
            Log.d(j8, str2);
        }
    }

    public static void b(String str, String str2, Throwable th) {
        String j8 = j(str);
        if (g(j8, 3)) {
            Log.d(j8, str2, th);
        }
    }

    public static void c(String str, String str2) {
        String j8 = j(str);
        if (g(j8, 6)) {
            Log.e(j8, str2);
        }
    }

    public static void d(String str, String str2, Throwable th) {
        String j8 = j(str);
        if (g(j8, 6)) {
            Log.e(j8, str2, th);
        }
    }

    public static void e(String str, String str2) {
        String j8 = j(str);
        if (g(j8, 4)) {
            Log.i(j8, str2);
        }
    }

    public static boolean f(String str) {
        return g(j(str), 3);
    }

    private static boolean g(String str, int i8) {
        return f2773a <= i8 || Log.isLoggable(str, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void h() {
        f2773a = 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void i(int i8) {
        f2773a = i8;
    }

    private static String j(String str) {
        return (Build.VERSION.SDK_INT > 25 || 23 >= str.length()) ? str : str.substring(0, 23);
    }

    public static void k(String str, String str2) {
        String j8 = j(str);
        if (g(j8, 5)) {
            Log.w(j8, str2);
        }
    }

    public static void l(String str, String str2, Throwable th) {
        String j8 = j(str);
        if (g(j8, 5)) {
            Log.w(j8, str2, th);
        }
    }
}
