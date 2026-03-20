package y3;

import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {
    public static <T> void a(T t8, Class<T> cls) {
        if (t8 != null) {
            return;
        }
        throw new IllegalStateException(cls.getCanonicalName() + " must be set");
    }

    public static <T> T b(T t8) {
        Objects.requireNonNull(t8);
        return t8;
    }

    public static <T> T c(T t8, String str) {
        Objects.requireNonNull(t8, str);
        return t8;
    }
}
