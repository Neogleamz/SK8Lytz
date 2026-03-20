package androidx.core.os;

import android.annotation.SuppressLint;
import android.os.Build;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {
    protected static boolean a(String str, String str2) {
        if ("REL".equals(str2)) {
            return false;
        }
        Locale locale = Locale.ROOT;
        return str2.toUpperCase(locale).compareTo(str.toUpperCase(locale)) >= 0;
    }

    @Deprecated
    public static boolean b() {
        return Build.VERSION.SDK_INT >= 30;
    }

    @SuppressLint({"RestrictedApi"})
    @Deprecated
    public static boolean c() {
        int i8 = Build.VERSION.SDK_INT;
        return i8 >= 31 || (i8 >= 30 && a("S", Build.VERSION.CODENAME));
    }

    public static boolean d() {
        int i8 = Build.VERSION.SDK_INT;
        return i8 >= 33 || (i8 >= 32 && a("Tiramisu", Build.VERSION.CODENAME));
    }
}
