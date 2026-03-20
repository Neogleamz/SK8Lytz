package com.google.common.collect;

import com.google.common.collect.q1;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class h<K, V> implements n1<K, V> {

    /* renamed from: a  reason: collision with root package name */
    private transient Collection<Map.Entry<K, V>> f19308a;

    /* renamed from: b  reason: collision with root package name */
    private transient Set<K> f19309b;

    /* renamed from: c  reason: collision with root package name */
    private transient Collection<V> f19310c;

    /* renamed from: d  reason: collision with root package name */
    private transient Map<K, Collection<V>> f19311d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends q1.c<K, V> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public a() {
        }

        @Override // com.google.common.collect.q1.c
        n1<K, V> e() {
            return h.this;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            return h.this.j();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends h<K, V>.a implements Set<Map.Entry<K, V>> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public b(h hVar) {
            super();
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(Object obj) {
            return p2.a(this, obj);
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            return p2.d(this);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends AbstractCollection<V> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public c() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            h.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return h.this.d(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return h.this.k();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return h.this.size();
        }
    }

    @Override // com.google.common.collect.n1
    public Map<K, Collection<V>> b() {
        Map<K, Collection<V>> map = this.f19311d;
        if (map == null) {
            Map<K, Collection<V>> e8 = e();
            this.f19311d = e8;
            return e8;
        }
        return map;
    }

    @Override // com.google.common.collect.n1
    public boolean c(Object obj, Object obj2) {
        Collection<V> collection = b().get(obj);
        return collection != null && collection.contains(obj2);
    }

    public boolean d(Object obj) {
        for (Collection<V> collection : b().values()) {
            if (collection.contains(obj)) {
                return true;
            }
        }
        return false;
    }

    abstract Map<K, Collection<V>> e();

    public boolean equals(Object obj) {
        return q1.a(this, obj);
    }

    abstract Collection<Map.Entry<K, V>> f();

    abstract Set<K> g();

    abstract Collection<V> h();

    public int hashCode() {
        return b().hashCode();
    }

    public Collection<Map.Entry<K, V>> i() {
        Collection<Map.Entry<K, V>> collection = this.f19308a;
        if (collection == null) {
            Collection<Map.Entry<K, V>> f5 = f();
            this.f19308a = f5;
            return f5;
        }
        return collection;
    }

    @Override // com.google.common.collect.n1
    public boolean isEmpty() {
        return size() == 0;
    }

    abstract Iterator<Map.Entry<K, V>> j();

    Iterator<V> k() {
        return m1.t(i().iterator());
    }

    @Override // com.google.common.collect.n1
    public Set<K> keySet() {
        Set<K> set = this.f19309b;
        if (set == null) {
            Set<K> g8 = g();
            this.f19309b = g8;
            return g8;
        }
        return set;
    }

    @Override // com.google.common.collect.n1
    public boolean put(K k8, V v8) {
        return get(k8).add(v8);
    }

    @Override // com.google.common.collect.n1
    public boolean remove(Object obj, Object obj2) {
        Collection<V> collection = b().get(obj);
        return collection != null && collection.remove(obj2);
    }

    public String toString() {
        return b().toString();
    }

    @Override // com.google.common.collect.n1
    public Collection<V> values() {
        Collection<V> collection = this.f19310c;
        if (collection == null) {
            Collection<V> h8 = h();
            this.f19310c = h8;
            return h8;
        }
        return collection;
    }
}
