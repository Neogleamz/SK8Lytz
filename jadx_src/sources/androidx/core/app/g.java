package androidx.core.app;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {
        static IBinder a(Bundle bundle, String str) {
            return bundle.getBinder(str);
        }

        static void b(Bundle bundle, String str, IBinder iBinder) {
            bundle.putBinder(str, iBinder);
        }
    }

    @SuppressLint({"BanUncheckedReflection"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {

        /* renamed from: a  reason: collision with root package name */
        private static Method f4473a;

        /* renamed from: b  reason: collision with root package name */
        private static boolean f4474b;

        /* renamed from: c  reason: collision with root package name */
        private static Method f4475c;

        /* renamed from: d  reason: collision with root package name */
        private static boolean f4476d;

        public static IBinder a(Bundle bundle, String str) {
            if (!f4474b) {
                try {
                    Method method = Bundle.class.getMethod("getIBinder", String.class);
                    f4473a = method;
                    method.setAccessible(true);
                } catch (NoSuchMethodException e8) {
                    Log.i("BundleCompatBaseImpl", "Failed to retrieve getIBinder method", e8);
                }
                f4474b = true;
            }
            Method method2 = f4473a;
            if (method2 != null) {
                try {
                    return (IBinder) method2.invoke(bundle, str);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e9) {
                    Log.i("BundleCompatBaseImpl", "Failed to invoke getIBinder via reflection", e9);
                    f4473a = null;
                }
            }
            return null;
        }

        public static void b(Bundle bundle, String str, IBinder iBinder) {
            if (!f4476d) {
                try {
                    Method method = Bundle.class.getMethod("putIBinder", String.class, IBinder.class);
                    f4475c = method;
                    method.setAccessible(true);
                } catch (NoSuchMethodException e8) {
                    Log.i("BundleCompatBaseImpl", "Failed to retrieve putIBinder method", e8);
                }
                f4476d = true;
            }
            Method method2 = f4475c;
            if (method2 != null) {
                try {
                    method2.invoke(bundle, str, iBinder);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e9) {
                    Log.i("BundleCompatBaseImpl", "Failed to invoke putIBinder via reflection", e9);
                    f4475c = null;
                }
            }
        }
    }

    public static IBinder a(Bundle bundle, String str) {
        return Build.VERSION.SDK_INT >= 18 ? a.a(bundle, str) : b.a(bundle, str);
    }

    public static void b(Bundle bundle, String str, IBinder iBinder) {
        if (Build.VERSION.SDK_INT >= 18) {
            a.b(bundle, str, iBinder);
        } else {
            b.b(bundle, str, iBinder);
        }
    }
}
