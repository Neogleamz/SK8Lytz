package com.google.common.collect;

import com.google.common.collect.z2;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class o<R, C, V> implements z2<R, C, V> {

    /* renamed from: a  reason: collision with root package name */
    private transient Set<z2.a<R, C, V>> f19412a;

    /* renamed from: b  reason: collision with root package name */
    private transient Collection<V> f19413b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends b3<z2.a<R, C, V>, V> {
        a(o oVar, Iterator it) {
            super(it);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.b3
        /* renamed from: b */
        public V a(z2.a<R, C, V> aVar) {
            return aVar.getValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends AbstractSet<z2.a<R, C, V>> {
        b() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            o.this.d();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (obj instanceof z2.a) {
                z2.a aVar = (z2.a) obj;
                Map map = (Map) m1.o(o.this.b(), aVar.b());
                return map != null && u.c(map.entrySet(), m1.f(aVar.a(), aVar.getValue()));
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<z2.a<R, C, V>> iterator() {
            return o.this.c();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (obj instanceof z2.a) {
                z2.a aVar = (z2.a) obj;
                Map map = (Map) m1.o(o.this.b(), aVar.b());
                return map != null && u.d(map.entrySet(), m1.f(aVar.a(), aVar.getValue()));
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return o.this.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends AbstractCollection<V> {
        c() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            o.this.d();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return o.this.e(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return o.this.k();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return o.this.size();
        }
    }

    @Override // com.google.common.collect.z2
    public Set<z2.a<R, C, V>> a() {
        Set<z2.a<R, C, V>> set = this.f19412a;
        if (set == null) {
            Set<z2.a<R, C, V>> f5 = f();
            this.f19412a = f5;
            return f5;
        }
        return set;
    }

    abstract Iterator<z2.a<R, C, V>> c();

    public void d() {
        g1.e(a().iterator());
    }

    public boolean e(Object obj) {
        for (Map<C, V> map : b().values()) {
            if (map.containsValue(obj)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        return a3.a(this, obj);
    }

    Set<z2.a<R, C, V>> f() {
        return new b();
    }

    Collection<V> g() {
        return new c();
    }

    public V h(Object obj, Object obj2) {
        Map map = (Map) m1.o(b(), obj);
        if (map == null) {
            return null;
        }
        return (V) m1.o(map, obj2);
    }

    public int hashCode() {
        return a().hashCode();
    }

    public boolean i() {
        return size() == 0;
    }

    public Collection<V> j() {
        Collection<V> collection = this.f19413b;
        if (collection == null) {
            Collection<V> g8 = g();
            this.f19413b = g8;
            return g8;
        }
        return collection;
    }

    Iterator<V> k() {
        return new a(this, a().iterator());
    }

    public String toString() {
        return b().toString();
    }
}
