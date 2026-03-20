package n6;

import android.util.Log;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    private final String f22177a;

    /* renamed from: b  reason: collision with root package name */
    private final String f22178b;

    public d(String str, String str2) {
        j.m(str, "log tag cannot be null");
        j.c(str.length() <= 23, "tag \"%s\" is longer than the %d character maximum", str, 23);
        this.f22177a = str;
        this.f22178b = (str2 == null || str2.length() <= 0) ? null : str2;
    }

    private final String g(String str) {
        String str2 = this.f22178b;
        return str2 == null ? str : str2.concat(str);
    }

    public boolean a(int i8) {
        return Log.isLoggable(this.f22177a, i8);
    }

    public void b(String str, String str2) {
        if (a(3)) {
            Log.d(str, g(str2));
        }
    }

    public void c(String str, String str2) {
        if (a(6)) {
            Log.e(str, g(str2));
        }
    }

    public void d(String str, String str2, Throwable th) {
        if (a(6)) {
            Log.e(str, g(str2), th);
        }
    }

    public void e(String str, String str2) {
        if (a(2)) {
            Log.v(str, g(str2));
        }
    }

    public void f(String str, String str2) {
        if (a(5)) {
            Log.w(str, g(str2));
        }
    }
}
