package androidx.camera.core.impl;

import android.util.ArrayMap;
import androidx.camera.core.impl.Config;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n extends o implements m {
    private static final Config.OptionPriority D = Config.OptionPriority.OPTIONAL;

    private n(TreeMap<Config.a<?>, Map<Config.OptionPriority, Object>> treeMap) {
        super(treeMap);
    }

    public static n P() {
        return new n(new TreeMap(o.B));
    }

    public static n Q(Config config) {
        TreeMap treeMap = new TreeMap(o.B);
        for (Config.a<?> aVar : config.e()) {
            Set<Config.OptionPriority> h8 = config.h(aVar);
            ArrayMap arrayMap = new ArrayMap();
            for (Config.OptionPriority optionPriority : h8) {
                arrayMap.put(optionPriority, config.d(aVar, optionPriority));
            }
            treeMap.put(aVar, arrayMap);
        }
        return new n(treeMap);
    }

    public <ValueT> ValueT R(Config.a<ValueT> aVar) {
        return (ValueT) this.A.remove(aVar);
    }

    @Override // androidx.camera.core.impl.m
    public <ValueT> void o(Config.a<ValueT> aVar, Config.OptionPriority optionPriority, ValueT valuet) {
        Map<Config.OptionPriority, Object> map = this.A.get(aVar);
        if (map == null) {
            ArrayMap arrayMap = new ArrayMap();
            this.A.put(aVar, arrayMap);
            arrayMap.put(optionPriority, valuet);
            return;
        }
        Config.OptionPriority optionPriority2 = (Config.OptionPriority) Collections.min(map.keySet());
        if (Objects.equals(map.get(optionPriority2), valuet) || !Config.C(optionPriority2, optionPriority)) {
            map.put(optionPriority, valuet);
            return;
        }
        throw new IllegalArgumentException("Option values conflicts: " + aVar.c() + ", existing value (" + optionPriority2 + ")=" + map.get(optionPriority2) + ", conflicting (" + optionPriority + ")=" + valuet);
    }

    @Override // androidx.camera.core.impl.m
    public <ValueT> void s(Config.a<ValueT> aVar, ValueT valuet) {
        o(aVar, D, valuet);
    }
}
