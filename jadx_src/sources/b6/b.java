package b6;

import android.os.Bundle;
import android.os.IBinder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private static Method f8018a;

    public static IBinder a(Bundle bundle, String str) {
        return l0.f8063a >= 18 ? bundle.getBinder(str) : b(bundle, str);
    }

    private static IBinder b(Bundle bundle, String str) {
        String str2;
        Method method = f8018a;
        if (method == null) {
            try {
                Method method2 = Bundle.class.getMethod("getIBinder", String.class);
                f8018a = method2;
                method2.setAccessible(true);
                method = f8018a;
            } catch (NoSuchMethodException e8) {
                e = e8;
                str2 = "Failed to retrieve getIBinder method";
                p.g("BundleUtil", str2, e);
                return null;
            }
        }
        try {
            return (IBinder) method.invoke(bundle, str);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e9) {
            e = e9;
            str2 = "Failed to invoke getIBinder via reflection";
            p.g("BundleUtil", str2, e);
            return null;
        }
    }
}
