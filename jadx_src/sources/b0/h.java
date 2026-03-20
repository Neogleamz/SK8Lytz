package b0;

import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.q;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface h<T> extends q {

    /* renamed from: w  reason: collision with root package name */
    public static final Config.a<String> f7925w = Config.a.a("camerax.core.target.name", String.class);

    /* renamed from: x  reason: collision with root package name */
    public static final Config.a<Class<?>> f7926x = Config.a.a("camerax.core.target.class", Class.class);

    default String w(String str) {
        return (String) f(f7925w, str);
    }
}
