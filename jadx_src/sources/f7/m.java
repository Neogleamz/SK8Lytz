package f7;

import android.os.Bundle;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m {
    public static <T> T a(Bundle bundle, String str, Class<T> cls, T t8) {
        T t9 = (T) bundle.get(str);
        if (t9 == null) {
            return t8;
        }
        if (cls.isAssignableFrom(t9.getClass())) {
            return t9;
        }
        throw new IllegalStateException(String.format("Invalid conditional user property field type. '%s' expected [%s] but was [%s]", str, cls.getCanonicalName(), t9.getClass().getCanonicalName()));
    }

    public static void b(Bundle bundle, Object obj) {
        if (obj instanceof Double) {
            bundle.putDouble("value", ((Double) obj).doubleValue());
        } else if (obj instanceof Long) {
            bundle.putLong("value", ((Long) obj).longValue());
        } else {
            bundle.putString("value", obj.toString());
        }
    }
}
