package androidx.camera.core.impl;

import android.util.ArrayMap;
import androidx.camera.core.impl.Config;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import y.q0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class o implements Config {
    protected static final Comparator<Config.a<?>> B;
    private static final o C;
    protected final TreeMap<Config.a<?>, Map<Config.OptionPriority, Object>> A;

    static {
        q0 q0Var = q0.a;
        B = q0Var;
        C = new o(new TreeMap((Comparator) q0Var));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(TreeMap<Config.a<?>, Map<Config.OptionPriority, Object>> treeMap) {
        this.A = treeMap;
    }

    public static o M() {
        return C;
    }

    public static o N(Config config) {
        if (o.class.equals(config.getClass())) {
            return (o) config;
        }
        TreeMap treeMap = new TreeMap(B);
        for (Config.a<?> aVar : config.e()) {
            Set<Config.OptionPriority> h8 = config.h(aVar);
            ArrayMap arrayMap = new ArrayMap();
            for (Config.OptionPriority optionPriority : h8) {
                arrayMap.put(optionPriority, config.d(aVar, optionPriority));
            }
            treeMap.put(aVar, arrayMap);
        }
        return new o(treeMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int O(Config.a aVar, Config.a aVar2) {
        return aVar.c().compareTo(aVar2.c());
    }

    @Override // androidx.camera.core.impl.Config
    public <ValueT> ValueT a(Config.a<ValueT> aVar) {
        Map<Config.OptionPriority, Object> map = this.A.get(aVar);
        if (map != null) {
            return (ValueT) map.get((Config.OptionPriority) Collections.min(map.keySet()));
        }
        throw new IllegalArgumentException("Option does not exist: " + aVar);
    }

    @Override // androidx.camera.core.impl.Config
    public boolean b(Config.a<?> aVar) {
        return this.A.containsKey(aVar);
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x001a  */
    @Override // androidx.camera.core.impl.Config
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void c(java.lang.String r4, androidx.camera.core.impl.Config.b r5) {
        /*
            r3 = this;
            java.lang.Class<java.lang.Void> r0 = java.lang.Void.class
            androidx.camera.core.impl.Config$a r0 = androidx.camera.core.impl.Config.a.a(r4, r0)
            java.util.TreeMap<androidx.camera.core.impl.Config$a<?>, java.util.Map<androidx.camera.core.impl.Config$OptionPriority, java.lang.Object>> r1 = r3.A
            java.util.SortedMap r0 = r1.tailMap(r0)
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
        L14:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L3d
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            java.lang.Object r2 = r1.getKey()
            androidx.camera.core.impl.Config$a r2 = (androidx.camera.core.impl.Config.a) r2
            java.lang.String r2 = r2.c()
            boolean r2 = r2.startsWith(r4)
            if (r2 != 0) goto L31
            goto L3d
        L31:
            java.lang.Object r1 = r1.getKey()
            androidx.camera.core.impl.Config$a r1 = (androidx.camera.core.impl.Config.a) r1
            boolean r1 = r5.a(r1)
            if (r1 != 0) goto L14
        L3d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.core.impl.o.c(java.lang.String, androidx.camera.core.impl.Config$b):void");
    }

    @Override // androidx.camera.core.impl.Config
    public <ValueT> ValueT d(Config.a<ValueT> aVar, Config.OptionPriority optionPriority) {
        Map<Config.OptionPriority, Object> map = this.A.get(aVar);
        if (map == null) {
            throw new IllegalArgumentException("Option does not exist: " + aVar);
        } else if (map.containsKey(optionPriority)) {
            return (ValueT) map.get(optionPriority);
        } else {
            throw new IllegalArgumentException("Option does not exist: " + aVar + " with priority=" + optionPriority);
        }
    }

    @Override // androidx.camera.core.impl.Config
    public Set<Config.a<?>> e() {
        return Collections.unmodifiableSet(this.A.keySet());
    }

    @Override // androidx.camera.core.impl.Config
    public <ValueT> ValueT f(Config.a<ValueT> aVar, ValueT valuet) {
        try {
            return (ValueT) a(aVar);
        } catch (IllegalArgumentException unused) {
            return valuet;
        }
    }

    @Override // androidx.camera.core.impl.Config
    public Config.OptionPriority g(Config.a<?> aVar) {
        Map<Config.OptionPriority, Object> map = this.A.get(aVar);
        if (map != null) {
            return (Config.OptionPriority) Collections.min(map.keySet());
        }
        throw new IllegalArgumentException("Option does not exist: " + aVar);
    }

    @Override // androidx.camera.core.impl.Config
    public Set<Config.OptionPriority> h(Config.a<?> aVar) {
        Map<Config.OptionPriority, Object> map = this.A.get(aVar);
        return map == null ? Collections.emptySet() : Collections.unmodifiableSet(map.keySet());
    }
}
