package androidx.camera.core.impl;

import androidx.camera.core.impl.Config;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface q extends Config {
    @Override // androidx.camera.core.impl.Config
    default <ValueT> ValueT a(Config.a<ValueT> aVar) {
        return (ValueT) l().a(aVar);
    }

    @Override // androidx.camera.core.impl.Config
    default boolean b(Config.a<?> aVar) {
        return l().b(aVar);
    }

    @Override // androidx.camera.core.impl.Config
    default void c(String str, Config.b bVar) {
        l().c(str, bVar);
    }

    @Override // androidx.camera.core.impl.Config
    default <ValueT> ValueT d(Config.a<ValueT> aVar, Config.OptionPriority optionPriority) {
        return (ValueT) l().d(aVar, optionPriority);
    }

    @Override // androidx.camera.core.impl.Config
    default Set<Config.a<?>> e() {
        return l().e();
    }

    @Override // androidx.camera.core.impl.Config
    default <ValueT> ValueT f(Config.a<ValueT> aVar, ValueT valuet) {
        return (ValueT) l().f(aVar, valuet);
    }

    @Override // androidx.camera.core.impl.Config
    default Config.OptionPriority g(Config.a<?> aVar) {
        return l().g(aVar);
    }

    @Override // androidx.camera.core.impl.Config
    default Set<Config.OptionPriority> h(Config.a<?> aVar) {
        return l().h(aVar);
    }

    Config l();
}
