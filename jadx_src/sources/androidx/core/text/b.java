package androidx.core.text;

import android.annotation.SuppressLint;
import android.icu.util.ULocale;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private static Method f4853a;

    /* renamed from: b  reason: collision with root package name */
    private static Method f4854b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static String a(Locale locale) {
            return locale.getScript();
        }
    }

    /* renamed from: androidx.core.text.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class C0039b {
        static ULocale a(Object obj) {
            return ULocale.addLikelySubtags((ULocale) obj);
        }

        static ULocale b(Locale locale) {
            return ULocale.forLocale(locale);
        }

        static String c(Object obj) {
            return ((ULocale) obj).getScript();
        }
    }

    static {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 21) {
            if (i8 < 24) {
                try {
                    f4854b = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
                    return;
                } catch (Exception e8) {
                    throw new IllegalStateException(e8);
                }
            }
            return;
        }
        try {
            Class<?> cls = Class.forName("libcore.icu.ICU");
            f4853a = cls.getMethod("getScript", String.class);
            f4854b = cls.getMethod("addLikelySubtags", String.class);
        } catch (Exception e9) {
            f4853a = null;
            f4854b = null;
            Log.w("ICUCompat", e9);
        }
    }

    @SuppressLint({"BanUncheckedReflection"})
    private static String a(Locale locale) {
        String locale2 = locale.toString();
        try {
            Method method = f4854b;
            if (method != null) {
                return (String) method.invoke(null, locale2);
            }
        } catch (IllegalAccessException | InvocationTargetException e8) {
            Log.w("ICUCompat", e8);
        }
        return locale2;
    }

    @SuppressLint({"BanUncheckedReflection"})
    private static String b(String str) {
        try {
            Method method = f4853a;
            if (method != null) {
                return (String) method.invoke(null, str);
            }
        } catch (IllegalAccessException | InvocationTargetException e8) {
            Log.w("ICUCompat", e8);
        }
        return null;
    }

    public static String c(Locale locale) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 24) {
            return C0039b.c(C0039b.a(C0039b.b(locale)));
        }
        if (i8 >= 21) {
            try {
                return a.a((Locale) f4854b.invoke(null, locale));
            } catch (IllegalAccessException | InvocationTargetException e8) {
                Log.w("ICUCompat", e8);
                return a.a(locale);
            }
        }
        String a9 = a(locale);
        if (a9 != null) {
            return b(a9);
        }
        return null;
    }
}
