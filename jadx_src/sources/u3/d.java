package u3;

import com.google.android.datatransport.Priority;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class d<T> {
    public static <T> d<T> e(T t8) {
        return new a(null, t8, Priority.DEFAULT, null);
    }

    public static <T> d<T> f(T t8, e eVar) {
        return new a(null, t8, Priority.DEFAULT, eVar);
    }

    public static <T> d<T> g(T t8) {
        return new a(null, t8, Priority.VERY_LOW, null);
    }

    public abstract Integer a();

    public abstract T b();

    public abstract Priority c();

    public abstract e d();
}
