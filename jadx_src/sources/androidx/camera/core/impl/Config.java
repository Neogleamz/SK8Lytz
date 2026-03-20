package androidx.camera.core.impl;

import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface Config {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum OptionPriority {
        ALWAYS_OVERRIDE,
        REQUIRED,
        OPTIONAL
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a<T> {
        public static <T> a<T> a(String str, Class<?> cls) {
            return b(str, cls, null);
        }

        public static <T> a<T> b(String str, Class<?> cls, Object obj) {
            return new androidx.camera.core.impl.a(str, cls, obj);
        }

        public abstract String c();

        public abstract Object d();

        public abstract Class<T> e();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        boolean a(a<?> aVar);
    }

    static Config A(Config config, Config config2) {
        if (config == null && config2 == null) {
            return o.M();
        }
        n Q = config2 != null ? n.Q(config2) : n.P();
        if (config != null) {
            for (a<?> aVar : config.e()) {
                Q.o(aVar, config.g(aVar), config.a(aVar));
            }
        }
        return o.N(Q);
    }

    static boolean C(OptionPriority optionPriority, OptionPriority optionPriority2) {
        OptionPriority optionPriority3 = OptionPriority.ALWAYS_OVERRIDE;
        if (optionPriority == optionPriority3 && optionPriority2 == optionPriority3) {
            return true;
        }
        OptionPriority optionPriority4 = OptionPriority.REQUIRED;
        return optionPriority == optionPriority4 && optionPriority2 == optionPriority4;
    }

    <ValueT> ValueT a(a<ValueT> aVar);

    boolean b(a<?> aVar);

    void c(String str, b bVar);

    <ValueT> ValueT d(a<ValueT> aVar, OptionPriority optionPriority);

    Set<a<?>> e();

    <ValueT> ValueT f(a<ValueT> aVar, ValueT valuet);

    OptionPriority g(a<?> aVar);

    Set<OptionPriority> h(a<?> aVar);
}
