package com.google.common.collect;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a<K, V> extends m0<K, V> implements r<K, V>, Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    private transient Map<K, V> f19152a;

    /* renamed from: b  reason: collision with root package name */
    transient a<V, K> f19153b;

    /* renamed from: c  reason: collision with root package name */
    private transient Set<K> f19154c;

    /* renamed from: d  reason: collision with root package name */
    private transient Set<V> f19155d;

    /* renamed from: e  reason: collision with root package name */
    private transient Set<Map.Entry<K, V>> f19156e;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.collect.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0150a implements Iterator<Map.Entry<K, V>> {

        /* renamed from: a  reason: collision with root package name */
        Map.Entry<K, V> f19157a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Iterator f19158b;

        C0150a(Iterator it) {
            this.f19158b = it;
        }

        @Override // java.util.Iterator
        /* renamed from: a */
        public Map.Entry<K, V> next() {
            Map.Entry<K, V> entry = (Map.Entry) this.f19158b.next();
            this.f19157a = entry;
            return new b(entry);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f19158b.hasNext();
        }

        @Override // java.util.Iterator
        public void remove() {
            Map.Entry<K, V> entry = this.f19157a;
            if (entry == null) {
                throw new IllegalStateException("no calls to next() since the last call to remove()");
            }
            V value = entry.getValue();
            this.f19158b.remove();
            a.this.G(value);
            this.f19157a = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends n0<K, V> {

        /* renamed from: a  reason: collision with root package name */
        private final Map.Entry<K, V> f19160a;

        b(Map.Entry<K, V> entry) {
            this.f19160a = entry;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.p0
        /* renamed from: i */
        public Map.Entry<K, V> h() {
            return this.f19160a;
        }

        @Override // com.google.common.collect.n0, java.util.Map.Entry
        public V setValue(V v8) {
            a.this.y(v8);
            com.google.common.base.l.t(a.this.entrySet().contains(this), "entry no longer in map");
            if (com.google.common.base.k.a(v8, getValue())) {
                return v8;
            }
            com.google.common.base.l.i(!a.this.containsValue(v8), "value already present: %s", v8);
            V value = this.f19160a.setValue(v8);
            com.google.common.base.l.t(com.google.common.base.k.a(v8, a.this.get(getKey())), "entry no longer in map");
            a.this.K(getKey(), true, value, v8);
            return value;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends r0<Map.Entry<K, V>> {

        /* renamed from: a  reason: collision with root package name */
        final Set<Map.Entry<K, V>> f19162a;

        private c() {
            this.f19162a = a.this.f19152a.entrySet();
        }

        /* synthetic */ c(a aVar, C0150a c0150a) {
            this();
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public void clear() {
            a.this.clear();
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return m1.c(i(), obj);
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> collection) {
            return n(collection);
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return a.this.A();
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (this.f19162a.contains(obj) && (obj instanceof Map.Entry)) {
                Map.Entry entry = (Map.Entry) obj;
                ((a) a.this.f19153b).f19152a.remove(entry.getValue());
                this.f19162a.remove(entry);
                return true;
            }
            return false;
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> collection) {
            return x(collection);
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> collection) {
            return p(collection);
        }

        @Override // com.google.common.collect.j0, java.util.Collection
        public Object[] toArray() {
            return q();
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] tArr) {
            return (T[]) t(tArr);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.r0, com.google.common.collect.j0
        /* renamed from: v */
        public Set<Map.Entry<K, V>> i() {
            return this.f19162a;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d<K, V> extends a<K, V> {
        private static final long serialVersionUID = 0;

        d(Map<K, V> map, a<V, K> aVar) {
            super(map, aVar, null);
        }

        private void readObject(ObjectInputStream objectInputStream) {
            objectInputStream.defaultReadObject();
            I((a) objectInputStream.readObject());
        }

        private void writeObject(ObjectOutputStream objectOutputStream) {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeObject(g());
        }

        @Override // com.google.common.collect.a, com.google.common.collect.m0, com.google.common.collect.p0
        protected /* bridge */ /* synthetic */ Object h() {
            return super.h();
        }

        Object readResolve() {
            return g().g();
        }

        @Override // com.google.common.collect.a, com.google.common.collect.m0, java.util.Map
        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }

        @Override // com.google.common.collect.a
        K x(K k8) {
            return this.f19153b.y(k8);
        }

        @Override // com.google.common.collect.a
        V y(V v8) {
            return this.f19153b.x(v8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e extends r0<K> {
        private e() {
        }

        /* synthetic */ e(a aVar, C0150a c0150a) {
            this();
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public void clear() {
            a.this.clear();
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return m1.i(a.this.entrySet().iterator());
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (contains(obj)) {
                a.this.F(obj);
                return true;
            }
            return false;
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> collection) {
            return x(collection);
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> collection) {
            return p(collection);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.r0, com.google.common.collect.j0
        /* renamed from: v */
        public Set<K> i() {
            return a.this.f19152a.keySet();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class f extends r0<V> {

        /* renamed from: a  reason: collision with root package name */
        final Set<V> f19165a;

        private f() {
            this.f19165a = a.this.f19153b.keySet();
        }

        /* synthetic */ f(a aVar, C0150a c0150a) {
            this();
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<V> iterator() {
            return m1.t(a.this.entrySet().iterator());
        }

        @Override // com.google.common.collect.j0, java.util.Collection
        public Object[] toArray() {
            return q();
        }

        @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] tArr) {
            return (T[]) t(tArr);
        }

        @Override // com.google.common.collect.p0
        public String toString() {
            return u();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.r0, com.google.common.collect.j0
        /* renamed from: v */
        public Set<V> i() {
            return this.f19165a;
        }
    }

    private a(Map<K, V> map, a<V, K> aVar) {
        this.f19152a = map;
        this.f19153b = aVar;
    }

    /* synthetic */ a(Map map, a aVar, C0150a c0150a) {
        this(map, aVar);
    }

    private V E(K k8, V v8, boolean z4) {
        x(k8);
        y(v8);
        boolean containsKey = containsKey(k8);
        if (containsKey && com.google.common.base.k.a(v8, get(k8))) {
            return v8;
        }
        if (z4) {
            g().remove(v8);
        } else {
            com.google.common.base.l.i(!containsValue(v8), "value already present: %s", v8);
        }
        V put = this.f19152a.put(k8, v8);
        K(k8, containsKey, put, v8);
        return put;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public V F(Object obj) {
        V v8 = (V) u1.a(this.f19152a.remove(obj));
        G(v8);
        return v8;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void G(V v8) {
        this.f19153b.f19152a.remove(v8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void K(K k8, boolean z4, V v8, V v9) {
        if (z4) {
            G(u1.a(v8));
        }
        this.f19153b.f19152a.put(v9, k8);
    }

    Iterator<Map.Entry<K, V>> A() {
        return new C0150a(this.f19152a.entrySet().iterator());
    }

    a<V, K> D(Map<V, K> map) {
        return new d(map, this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H(Map<K, V> map, Map<V, K> map2) {
        com.google.common.base.l.s(this.f19152a == null);
        com.google.common.base.l.s(this.f19153b == null);
        com.google.common.base.l.d(map.isEmpty());
        com.google.common.base.l.d(map2.isEmpty());
        com.google.common.base.l.d(map != map2);
        this.f19152a = map;
        this.f19153b = D(map2);
    }

    void I(a<V, K> aVar) {
        this.f19153b = aVar;
    }

    @Override // com.google.common.collect.m0, java.util.Map
    /* renamed from: L */
    public Set<V> values() {
        Set<V> set = this.f19155d;
        if (set == null) {
            f fVar = new f(this, null);
            this.f19155d = fVar;
            return fVar;
        }
        return set;
    }

    @Override // com.google.common.collect.m0, java.util.Map
    public void clear() {
        this.f19152a.clear();
        this.f19153b.f19152a.clear();
    }

    @Override // com.google.common.collect.m0, java.util.Map
    public boolean containsValue(Object obj) {
        return this.f19153b.containsKey(obj);
    }

    @Override // com.google.common.collect.m0, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.f19156e;
        if (set == null) {
            c cVar = new c(this, null);
            this.f19156e = cVar;
            return cVar;
        }
        return set;
    }

    public r<V, K> g() {
        return this.f19153b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.m0, com.google.common.collect.p0
    /* renamed from: i */
    public Map<K, V> h() {
        return this.f19152a;
    }

    @Override // com.google.common.collect.m0, java.util.Map
    public Set<K> keySet() {
        Set<K> set = this.f19154c;
        if (set == null) {
            e eVar = new e(this, null);
            this.f19154c = eVar;
            return eVar;
        }
        return set;
    }

    @Override // com.google.common.collect.m0, java.util.Map
    public V put(K k8, V v8) {
        return E(k8, v8, false);
    }

    @Override // com.google.common.collect.m0, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // com.google.common.collect.m0, java.util.Map
    public V remove(Object obj) {
        if (containsKey(obj)) {
            return F(obj);
        }
        return null;
    }

    K x(K k8) {
        return k8;
    }

    V y(V v8) {
        return v8;
    }
}
