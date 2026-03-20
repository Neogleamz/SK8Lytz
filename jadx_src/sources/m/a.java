package m;

import java.util.HashMap;
import java.util.Map;
import m.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a<K, V> extends b<K, V> {

    /* renamed from: e  reason: collision with root package name */
    private final HashMap<K, b.c<K, V>> f21809e = new HashMap<>();

    public boolean contains(K k8) {
        return this.f21809e.containsKey(k8);
    }

    @Override // m.b
    protected b.c<K, V> g(K k8) {
        return this.f21809e.get(k8);
    }

    @Override // m.b
    public V n(K k8, V v8) {
        b.c<K, V> g8 = g(k8);
        if (g8 != null) {
            return g8.f21815b;
        }
        this.f21809e.put(k8, k(k8, v8));
        return null;
    }

    @Override // m.b
    public V p(K k8) {
        V v8 = (V) super.p(k8);
        this.f21809e.remove(k8);
        return v8;
    }

    public Map.Entry<K, V> q(K k8) {
        if (contains(k8)) {
            return this.f21809e.get(k8).f21817d;
        }
        return null;
    }
}
