package com.google.common.collect;

import com.google.common.collect.m1;
import com.google.common.collect.p2;
import com.google.common.collect.z2;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class y2<R, C, V> extends o<R, C, V> implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: c  reason: collision with root package name */
    final Map<R, Map<C, V>> f19517c;

    /* renamed from: d  reason: collision with root package name */
    final com.google.common.base.r<? extends Map<C, V>> f19518d;

    /* renamed from: e  reason: collision with root package name */
    private transient Map<R, Map<C, V>> f19519e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class b implements Iterator<z2.a<R, C, V>> {

        /* renamed from: a  reason: collision with root package name */
        final Iterator<Map.Entry<R, Map<C, V>>> f19520a;

        /* renamed from: b  reason: collision with root package name */
        Map.Entry<R, Map<C, V>> f19521b;

        /* renamed from: c  reason: collision with root package name */
        Iterator<Map.Entry<C, V>> f19522c;

        private b() {
            this.f19520a = y2.this.f19517c.entrySet().iterator();
            this.f19522c = g1.j();
        }

        @Override // java.util.Iterator
        /* renamed from: a */
        public z2.a<R, C, V> next() {
            if (!this.f19522c.hasNext()) {
                Map.Entry<R, Map<C, V>> next = this.f19520a.next();
                this.f19521b = next;
                this.f19522c = next.getValue().entrySet().iterator();
            }
            Objects.requireNonNull(this.f19521b);
            Map.Entry<C, V> next2 = this.f19522c.next();
            return a3.b(this.f19521b.getKey(), next2.getKey(), next2.getValue());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f19520a.hasNext() || this.f19522c.hasNext();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.f19522c.remove();
            Map.Entry<R, Map<C, V>> entry = this.f19521b;
            Objects.requireNonNull(entry);
            if (entry.getValue().isEmpty()) {
                this.f19520a.remove();
                this.f19521b = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends m1.g<C, V> {

        /* renamed from: a  reason: collision with root package name */
        final R f19524a;

        /* renamed from: b  reason: collision with root package name */
        Map<C, V> f19525b;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Iterator<Map.Entry<C, V>> {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ Iterator f19527a;

            a(Iterator it) {
                this.f19527a = it;
            }

            @Override // java.util.Iterator
            /* renamed from: a */
            public Map.Entry<C, V> next() {
                return c.this.f((Map.Entry) this.f19527a.next());
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.f19527a.hasNext();
            }

            @Override // java.util.Iterator
            public void remove() {
                this.f19527a.remove();
                c.this.c();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class b extends n0<C, V> {

            /* renamed from: a  reason: collision with root package name */
            final /* synthetic */ Map.Entry f19529a;

            b(c cVar, Map.Entry entry) {
                this.f19529a = entry;
            }

            @Override // com.google.common.collect.n0, java.util.Map.Entry
            public boolean equals(Object obj) {
                return k(obj);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.p0
            /* renamed from: i */
            public Map.Entry<C, V> h() {
                return this.f19529a;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.google.common.collect.n0, java.util.Map.Entry
            public V setValue(V v8) {
                return (V) super.setValue(com.google.common.base.l.n(v8));
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public c(R r4) {
            this.f19524a = (R) com.google.common.base.l.n(r4);
        }

        @Override // com.google.common.collect.m1.g
        Iterator<Map.Entry<C, V>> a() {
            d();
            Map<C, V> map = this.f19525b;
            return map == null ? g1.j() : new a(map.entrySet().iterator());
        }

        Map<C, V> b() {
            return y2.this.f19517c.get(this.f19524a);
        }

        void c() {
            d();
            Map<C, V> map = this.f19525b;
            if (map == null || !map.isEmpty()) {
                return;
            }
            y2.this.f19517c.remove(this.f19524a);
            this.f19525b = null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            d();
            Map<C, V> map = this.f19525b;
            if (map != null) {
                map.clear();
            }
            c();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            Map<C, V> map;
            d();
            return (obj == null || (map = this.f19525b) == null || !m1.n(map, obj)) ? false : true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void d() {
            Map<C, V> map = this.f19525b;
            if (map == null || (map.isEmpty() && y2.this.f19517c.containsKey(this.f19524a))) {
                this.f19525b = b();
            }
        }

        Map.Entry<C, V> f(Map.Entry<C, V> entry) {
            return new b(this, entry);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V get(Object obj) {
            Map<C, V> map;
            d();
            if (obj == null || (map = this.f19525b) == null) {
                return null;
            }
            return (V) m1.o(map, obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V put(C c9, V v8) {
            com.google.common.base.l.n(c9);
            com.google.common.base.l.n(v8);
            Map<C, V> map = this.f19525b;
            return (map == null || map.isEmpty()) ? (V) y2.this.o(this.f19524a, c9, v8) : this.f19525b.put(c9, v8);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public V remove(Object obj) {
            d();
            Map<C, V> map = this.f19525b;
            if (map == null) {
                return null;
            }
            V v8 = (V) m1.p(map, obj);
            c();
            return v8;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            d();
            Map<C, V> map = this.f19525b;
            if (map == null) {
                return 0;
            }
            return map.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends m1.k<R, Map<C, V>> {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a extends y2<R, C, V>.e<Map.Entry<R, Map<C, V>>> {

            /* renamed from: com.google.common.collect.y2$d$a$a  reason: collision with other inner class name */
            /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
            class C0156a implements com.google.common.base.g<R, Map<C, V>> {
                C0156a() {
                }

                @Override // com.google.common.base.g
                /* renamed from: a */
                public Map<C, V> apply(R r4) {
                    return y2.this.p(r4);
                }
            }

            a() {
                super();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                if (obj instanceof Map.Entry) {
                    Map.Entry entry = (Map.Entry) obj;
                    return entry.getKey() != null && (entry.getValue() instanceof Map) && u.c(y2.this.f19517c.entrySet(), entry);
                }
                return false;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
                return m1.a(y2.this.f19517c.keySet(), new C0156a());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object obj) {
                if (obj instanceof Map.Entry) {
                    Map.Entry entry = (Map.Entry) obj;
                    return entry.getKey() != null && (entry.getValue() instanceof Map) && y2.this.f19517c.entrySet().remove(entry);
                }
                return false;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return y2.this.f19517c.size();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public d() {
        }

        @Override // com.google.common.collect.m1.k
        protected Set<Map.Entry<R, Map<C, V>>> a() {
            return new a();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            return y2.this.l(obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        /* renamed from: d */
        public Map<C, V> get(Object obj) {
            if (y2.this.l(obj)) {
                y2 y2Var = y2.this;
                Objects.requireNonNull(obj);
                return y2Var.p(obj);
            }
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        /* renamed from: f */
        public Map<C, V> remove(Object obj) {
            if (obj == null) {
                return null;
            }
            return y2.this.f19517c.remove(obj);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private abstract class e<T> extends p2.d<T> {
        private e() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            y2.this.f19517c.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return y2.this.f19517c.isEmpty();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public y2(Map<R, Map<C, V>> map, com.google.common.base.r<? extends Map<C, V>> rVar) {
        this.f19517c = map;
        this.f19518d = rVar;
    }

    private Map<C, V> n(R r4) {
        Map<C, V> map = this.f19517c.get(r4);
        if (map == null) {
            Map<C, V> map2 = this.f19518d.get();
            this.f19517c.put(r4, map2);
            return map2;
        }
        return map;
    }

    @Override // com.google.common.collect.o, com.google.common.collect.z2
    public Set<z2.a<R, C, V>> a() {
        return super.a();
    }

    @Override // com.google.common.collect.z2
    public Map<R, Map<C, V>> b() {
        Map<R, Map<C, V>> map = this.f19519e;
        if (map == null) {
            Map<R, Map<C, V>> m8 = m();
            this.f19519e = m8;
            return m8;
        }
        return map;
    }

    @Override // com.google.common.collect.o
    Iterator<z2.a<R, C, V>> c() {
        return new b();
    }

    @Override // com.google.common.collect.o
    public void d() {
        this.f19517c.clear();
    }

    @Override // com.google.common.collect.o
    public boolean e(Object obj) {
        return obj != null && super.e(obj);
    }

    public boolean l(Object obj) {
        return obj != null && m1.n(this.f19517c, obj);
    }

    Map<R, Map<C, V>> m() {
        return new d();
    }

    public V o(R r4, C c9, V v8) {
        com.google.common.base.l.n(r4);
        com.google.common.base.l.n(c9);
        com.google.common.base.l.n(v8);
        return n(r4).put(c9, v8);
    }

    public Map<C, V> p(R r4) {
        return new c(r4);
    }

    @Override // com.google.common.collect.z2
    public int size() {
        int i8 = 0;
        for (Map<C, V> map : this.f19517c.values()) {
            i8 += map.size();
        }
        return i8;
    }
}
