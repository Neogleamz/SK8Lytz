package k0;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a<K, V> extends g<K, V> implements Map<K, V> {

    /* renamed from: h  reason: collision with root package name */
    f<K, V> f20853h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: k0.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0178a extends f<K, V> {
        C0178a() {
        }

        @Override // k0.f
        protected void a() {
            a.this.clear();
        }

        @Override // k0.f
        protected Object b(int i8, int i9) {
            return a.this.f20902b[(i8 << 1) + i9];
        }

        @Override // k0.f
        protected Map<K, V> c() {
            return a.this;
        }

        @Override // k0.f
        protected int d() {
            return a.this.f20903c;
        }

        @Override // k0.f
        protected int e(Object obj) {
            return a.this.h(obj);
        }

        @Override // k0.f
        protected int f(Object obj) {
            return a.this.j(obj);
        }

        @Override // k0.f
        protected void g(K k8, V v8) {
            a.this.put(k8, v8);
        }

        @Override // k0.f
        protected void h(int i8) {
            a.this.m(i8);
        }

        @Override // k0.f
        protected V i(int i8, V v8) {
            return a.this.n(i8, v8);
        }
    }

    public a() {
    }

    public a(int i8) {
        super(i8);
    }

    public a(g gVar) {
        super(gVar);
    }

    private f<K, V> p() {
        if (this.f20853h == null) {
            this.f20853h = new C0178a();
        }
        return this.f20853h;
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return p().l();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return p().m();
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        c(this.f20903c + map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public boolean q(Collection<?> collection) {
        return f.p(this, collection);
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return p().n();
    }
}
