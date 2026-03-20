package com.google.common.collect;

import com.google.common.collect.h;
import com.google.common.collect.m1;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class e<K, V> extends com.google.common.collect.h<K, V> implements Serializable {
    private static final long serialVersionUID = 2447537837011683357L;

    /* renamed from: e  reason: collision with root package name */
    private transient Map<K, Collection<V>> f19215e;

    /* renamed from: f  reason: collision with root package name */
    private transient int f19216f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends e<K, V>.d<V> {
        a(e eVar) {
            super();
        }

        @Override // com.google.common.collect.e.d
        V a(K k8, V v8) {
            return v8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends e<K, V>.d<Map.Entry<K, V>> {
        b(e eVar) {
            super();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.e.d
        /* renamed from: b */
        public Map.Entry<K, V> a(K k8, V v8) {
            return m1.f(k8, v8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends m1.k<K, Collection<V>> {

        /* renamed from: d  reason: collision with root package name */
        final transient Map<K, Collection<V>> f19217d;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends m1.f<K, Collection<V>> {
            a() {
            }

            @Override // com.google.common.collect.m1.f, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                return u.c(c.this.f19217d.entrySet(), obj);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, Collection<V>>> iterator() {
                return new b();
            }

            @Override // com.google.common.collect.m1.f
            Map<K, Collection<V>> k() {
                return c.this;
            }

            @Override // com.google.common.collect.m1.f, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object obj) {
                if (contains(obj)) {
                    Map.Entry entry = (Map.Entry) obj;
                    Objects.requireNonNull(entry);
                    e.this.z(entry.getKey());
                    return true;
                }
                return false;
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class b implements Iterator<Map.Entry<K, Collection<V>>> {

            /* renamed from: a  reason: collision with root package name */
            final Iterator<Map.Entry<K, Collection<V>>> f19220a;

            /* renamed from: b  reason: collision with root package name */
            Collection<V> f19221b;

            b() {
                this.f19220a = c.this.f19217d.entrySet().iterator();
            }

            @Override // java.util.Iterator
            /* renamed from: a */
            public Map.Entry<K, Collection<V>> next() {
                Map.Entry<K, Collection<V>> next = this.f19220a.next();
                this.f19221b = next.getValue();
                return c.this.h(next);
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.f19220a.hasNext();
            }

            @Override // java.util.Iterator
            public void remove() {
                com.google.common.base.l.t(this.f19221b != null, "no calls to next() since the last call to remove()");
                this.f19220a.remove();
                e.q(e.this, this.f19221b.size());
                this.f19221b.clear();
                this.f19221b = null;
            }
        }

        c(Map<K, Collection<V>> map) {
            this.f19217d = map;
        }

        @Override // com.google.common.collect.m1.k
        protected Set<Map.Entry<K, Collection<V>>> a() {
            return new a();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            if (this.f19217d == e.this.f19215e) {
                e.this.clear();
            } else {
                g1.e(new b());
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object obj) {
            return m1.n(this.f19217d, obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        /* renamed from: d */
        public Collection<V> get(Object obj) {
            Collection<V> collection = (Collection) m1.o(this.f19217d, obj);
            if (collection == null) {
                return null;
            }
            return e.this.D(obj, collection);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean equals(Object obj) {
            return this == obj || this.f19217d.equals(obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        /* renamed from: f */
        public Collection<V> remove(Object obj) {
            Collection<V> remove = this.f19217d.remove(obj);
            if (remove == null) {
                return null;
            }
            Collection<V> t8 = e.this.t();
            t8.addAll(remove);
            e.q(e.this, remove.size());
            remove.clear();
            return t8;
        }

        Map.Entry<K, Collection<V>> h(Map.Entry<K, Collection<V>> entry) {
            K key = entry.getKey();
            return m1.f(key, e.this.D(key, entry.getValue()));
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int hashCode() {
            return this.f19217d.hashCode();
        }

        @Override // com.google.common.collect.m1.k, java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            return e.this.keySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return this.f19217d.size();
        }

        @Override // java.util.AbstractMap
        public String toString() {
            return this.f19217d.toString();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private abstract class d<T> implements Iterator<T> {

        /* renamed from: a  reason: collision with root package name */
        final Iterator<Map.Entry<K, Collection<V>>> f19223a;

        /* renamed from: b  reason: collision with root package name */
        K f19224b = null;

        /* renamed from: c  reason: collision with root package name */
        Collection<V> f19225c = null;

        /* renamed from: d  reason: collision with root package name */
        Iterator<V> f19226d = g1.j();

        d() {
            this.f19223a = (Iterator<Map.Entry<K, V>>) e.this.f19215e.entrySet().iterator();
        }

        abstract T a(K k8, V v8);

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f19223a.hasNext() || this.f19226d.hasNext();
        }

        @Override // java.util.Iterator
        public T next() {
            if (!this.f19226d.hasNext()) {
                Map.Entry<K, Collection<V>> next = this.f19223a.next();
                this.f19224b = next.getKey();
                Collection<V> value = next.getValue();
                this.f19225c = value;
                this.f19226d = value.iterator();
            }
            return a(u1.a(this.f19224b), this.f19226d.next());
        }

        @Override // java.util.Iterator
        public void remove() {
            this.f19226d.remove();
            Collection<V> collection = this.f19225c;
            Objects.requireNonNull(collection);
            if (collection.isEmpty()) {
                this.f19223a.remove();
            }
            e.o(e.this);
        }
    }

    /* renamed from: com.google.common.collect.e$e  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class C0151e extends m1.h<K, Collection<V>> {

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.google.common.collect.e$e$a */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a implements Iterator<K> {

            /* renamed from: a  reason: collision with root package name */
            Map.Entry<K, Collection<V>> f19229a;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ Iterator f19230b;

            a(Iterator it) {
                this.f19230b = it;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.f19230b.hasNext();
            }

            @Override // java.util.Iterator
            public K next() {
                Map.Entry<K, Collection<V>> entry = (Map.Entry) this.f19230b.next();
                this.f19229a = entry;
                return entry.getKey();
            }

            @Override // java.util.Iterator
            public void remove() {
                com.google.common.base.l.t(this.f19229a != null, "no calls to next() since the last call to remove()");
                Collection<V> value = this.f19229a.getValue();
                this.f19230b.remove();
                e.q(e.this, value.size());
                value.clear();
                this.f19229a = null;
            }
        }

        C0151e(Map<K, Collection<V>> map) {
            super(map);
        }

        @Override // com.google.common.collect.m1.h, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            g1.e(iterator());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> collection) {
            return k().keySet().containsAll(collection);
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
        public boolean equals(Object obj) {
            return this == obj || k().keySet().equals(obj);
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.Set
        public int hashCode() {
            return k().keySet().hashCode();
        }

        @Override // com.google.common.collect.m1.h, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new a(k().entrySet().iterator());
        }

        @Override // com.google.common.collect.m1.h, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int i8;
            Collection<V> remove = k().remove(obj);
            if (remove != null) {
                i8 = remove.size();
                remove.clear();
                e.q(e.this, i8);
            } else {
                i8 = 0;
            }
            return i8 > 0;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class f extends e<K, V>.i implements NavigableMap<K, Collection<V>> {
        f(NavigableMap<K, Collection<V>> navigableMap) {
            super(navigableMap);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, Collection<V>> ceilingEntry(K k8) {
            Map.Entry<K, Collection<V>> ceilingEntry = k().ceilingEntry(k8);
            if (ceilingEntry == null) {
                return null;
            }
            return h(ceilingEntry);
        }

        @Override // java.util.NavigableMap
        public K ceilingKey(K k8) {
            return k().ceilingKey(k8);
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, Collection<V>> descendingMap() {
            return new f(k().descendingMap());
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, Collection<V>> firstEntry() {
            Map.Entry<K, Collection<V>> firstEntry = k().firstEntry();
            if (firstEntry == null) {
                return null;
            }
            return h(firstEntry);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, Collection<V>> floorEntry(K k8) {
            Map.Entry<K, Collection<V>> floorEntry = k().floorEntry(k8);
            if (floorEntry == null) {
                return null;
            }
            return h(floorEntry);
        }

        @Override // java.util.NavigableMap
        public K floorKey(K k8) {
            return k().floorKey(k8);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, Collection<V>> headMap(K k8, boolean z4) {
            return new f(k().headMap(k8, z4));
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, Collection<V>> higherEntry(K k8) {
            Map.Entry<K, Collection<V>> higherEntry = k().higherEntry(k8);
            if (higherEntry == null) {
                return null;
            }
            return h(higherEntry);
        }

        @Override // java.util.NavigableMap
        public K higherKey(K k8) {
            return k().higherKey(k8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.e.i
        /* renamed from: l */
        public NavigableSet<K> i() {
            return new g(k());
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, Collection<V>> lastEntry() {
            Map.Entry<K, Collection<V>> lastEntry = k().lastEntry();
            if (lastEntry == null) {
                return null;
            }
            return h(lastEntry);
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, Collection<V>> lowerEntry(K k8) {
            Map.Entry<K, Collection<V>> lowerEntry = k().lowerEntry(k8);
            if (lowerEntry == null) {
                return null;
            }
            return h(lowerEntry);
        }

        @Override // java.util.NavigableMap
        public K lowerKey(K k8) {
            return k().lowerKey(k8);
        }

        @Override // com.google.common.collect.e.i, java.util.SortedMap, java.util.NavigableMap
        /* renamed from: m */
        public NavigableMap<K, Collection<V>> headMap(K k8) {
            return headMap(k8, false);
        }

        @Override // com.google.common.collect.e.i, com.google.common.collect.e.c, com.google.common.collect.m1.k, java.util.AbstractMap, java.util.Map
        /* renamed from: n */
        public NavigableSet<K> keySet() {
            return (NavigableSet) super.keySet();
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> navigableKeySet() {
            return keySet();
        }

        Map.Entry<K, Collection<V>> o(Iterator<Map.Entry<K, Collection<V>>> it) {
            if (it.hasNext()) {
                Map.Entry<K, Collection<V>> next = it.next();
                Collection<V> t8 = e.this.t();
                t8.addAll(next.getValue());
                it.remove();
                return m1.f(next.getKey(), e.this.B(t8));
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.e.i
        /* renamed from: p */
        public NavigableMap<K, Collection<V>> k() {
            return (NavigableMap) super.k();
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, Collection<V>> pollFirstEntry() {
            return o(entrySet().iterator());
        }

        @Override // java.util.NavigableMap
        public Map.Entry<K, Collection<V>> pollLastEntry() {
            return o(descendingMap().entrySet().iterator());
        }

        @Override // com.google.common.collect.e.i, java.util.SortedMap, java.util.NavigableMap
        /* renamed from: q */
        public NavigableMap<K, Collection<V>> subMap(K k8, K k9) {
            return subMap(k8, true, k9, false);
        }

        @Override // com.google.common.collect.e.i, java.util.SortedMap, java.util.NavigableMap
        /* renamed from: r */
        public NavigableMap<K, Collection<V>> tailMap(K k8) {
            return tailMap(k8, true);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, Collection<V>> subMap(K k8, boolean z4, K k9, boolean z8) {
            return new f(k().subMap(k8, z4, k9, z8));
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, Collection<V>> tailMap(K k8, boolean z4) {
            return new f(k().tailMap(k8, z4));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g extends e<K, V>.j implements NavigableSet<K> {
        g(NavigableMap<K, Collection<V>> navigableMap) {
            super(navigableMap);
        }

        @Override // java.util.NavigableSet
        public K ceiling(K k8) {
            return n().ceilingKey(k8);
        }

        @Override // java.util.NavigableSet
        public Iterator<K> descendingIterator() {
            return descendingSet().iterator();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> descendingSet() {
            return new g(n().descendingMap());
        }

        @Override // java.util.NavigableSet
        public K floor(K k8) {
            return n().floorKey(k8);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> headSet(K k8, boolean z4) {
            return new g(n().headMap(k8, z4));
        }

        @Override // java.util.NavigableSet
        public K higher(K k8) {
            return n().higherKey(k8);
        }

        @Override // java.util.NavigableSet
        public K lower(K k8) {
            return n().lowerKey(k8);
        }

        @Override // com.google.common.collect.e.j, java.util.SortedSet, java.util.NavigableSet
        /* renamed from: p */
        public NavigableSet<K> headSet(K k8) {
            return headSet(k8, false);
        }

        @Override // java.util.NavigableSet
        public K pollFirst() {
            return (K) g1.q(iterator());
        }

        @Override // java.util.NavigableSet
        public K pollLast() {
            return (K) g1.q(descendingIterator());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.e.j
        /* renamed from: q */
        public NavigableMap<K, Collection<V>> n() {
            return (NavigableMap) super.n();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> subSet(K k8, boolean z4, K k9, boolean z8) {
            return new g(n().subMap(k8, z4, k9, z8));
        }

        @Override // com.google.common.collect.e.j, java.util.SortedSet, java.util.NavigableSet
        /* renamed from: t */
        public NavigableSet<K> subSet(K k8, K k9) {
            return subSet(k8, true, k9, false);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<K> tailSet(K k8, boolean z4) {
            return new g(n().tailMap(k8, z4));
        }

        @Override // com.google.common.collect.e.j, java.util.SortedSet, java.util.NavigableSet
        /* renamed from: u */
        public NavigableSet<K> tailSet(K k8) {
            return tailSet(k8, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h extends e<K, V>.l implements RandomAccess {
        h(e eVar, K k8, List<V> list, e<K, V>.k kVar) {
            super(k8, list, kVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class i extends e<K, V>.c implements SortedMap<K, Collection<V>> {

        /* renamed from: f  reason: collision with root package name */
        SortedSet<K> f19234f;

        i(SortedMap<K, Collection<V>> sortedMap) {
            super(sortedMap);
        }

        @Override // java.util.SortedMap
        public Comparator<? super K> comparator() {
            return k().comparator();
        }

        @Override // java.util.SortedMap
        public K firstKey() {
            return k().firstKey();
        }

        public SortedMap<K, Collection<V>> headMap(K k8) {
            return new i(k().headMap(k8));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.m1.k
        /* renamed from: i */
        public SortedSet<K> b() {
            return new j(k());
        }

        @Override // com.google.common.collect.e.c, com.google.common.collect.m1.k, java.util.AbstractMap, java.util.Map
        /* renamed from: j */
        public SortedSet<K> keySet() {
            SortedSet<K> sortedSet = this.f19234f;
            if (sortedSet == null) {
                SortedSet<K> b9 = b();
                this.f19234f = b9;
                return b9;
            }
            return sortedSet;
        }

        SortedMap<K, Collection<V>> k() {
            return (SortedMap) this.f19217d;
        }

        @Override // java.util.SortedMap
        public K lastKey() {
            return k().lastKey();
        }

        public SortedMap<K, Collection<V>> subMap(K k8, K k9) {
            return new i(k().subMap(k8, k9));
        }

        public SortedMap<K, Collection<V>> tailMap(K k8) {
            return new i(k().tailMap(k8));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class j extends e<K, V>.C0151e implements SortedSet<K> {
        j(SortedMap<K, Collection<V>> sortedMap) {
            super(sortedMap);
        }

        @Override // java.util.SortedSet
        public Comparator<? super K> comparator() {
            return n().comparator();
        }

        @Override // java.util.SortedSet
        public K first() {
            return n().firstKey();
        }

        public SortedSet<K> headSet(K k8) {
            return new j(n().headMap(k8));
        }

        @Override // java.util.SortedSet
        public K last() {
            return n().lastKey();
        }

        SortedMap<K, Collection<V>> n() {
            return (SortedMap) super.k();
        }

        public SortedSet<K> subSet(K k8, K k9) {
            return new j(n().subMap(k8, k9));
        }

        public SortedSet<K> tailSet(K k8) {
            return new j(n().tailMap(k8));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class k extends AbstractCollection<V> {

        /* renamed from: a  reason: collision with root package name */
        final K f19237a;

        /* renamed from: b  reason: collision with root package name */
        Collection<V> f19238b;

        /* renamed from: c  reason: collision with root package name */
        final e<K, V>.k f19239c;

        /* renamed from: d  reason: collision with root package name */
        final Collection<V> f19240d;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class a implements Iterator<V> {

            /* renamed from: a  reason: collision with root package name */
            final Iterator<V> f19242a;

            /* renamed from: b  reason: collision with root package name */
            final Collection<V> f19243b;

            a() {
                Collection<V> collection = k.this.f19238b;
                this.f19243b = collection;
                this.f19242a = e.y(collection);
            }

            a(Iterator<V> it) {
                this.f19243b = k.this.f19238b;
                this.f19242a = it;
            }

            Iterator<V> a() {
                b();
                return this.f19242a;
            }

            void b() {
                k.this.k();
                if (k.this.f19238b != this.f19243b) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                b();
                return this.f19242a.hasNext();
            }

            @Override // java.util.Iterator
            public V next() {
                b();
                return this.f19242a.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                this.f19242a.remove();
                e.o(e.this);
                k.this.n();
            }
        }

        k(K k8, Collection<V> collection, e<K, V>.k kVar) {
            this.f19237a = k8;
            this.f19238b = collection;
            this.f19239c = kVar;
            this.f19240d = kVar == null ? null : kVar.h();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean add(V v8) {
            k();
            boolean isEmpty = this.f19238b.isEmpty();
            boolean add = this.f19238b.add(v8);
            if (add) {
                e.n(e.this);
                if (isEmpty) {
                    e();
                }
            }
            return add;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            boolean addAll = this.f19238b.addAll(collection);
            if (addAll) {
                e.p(e.this, this.f19238b.size() - size);
                if (size == 0) {
                    e();
                }
            }
            return addAll;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            int size = size();
            if (size == 0) {
                return;
            }
            this.f19238b.clear();
            e.q(e.this, size);
            n();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            k();
            return this.f19238b.contains(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            k();
            return this.f19238b.containsAll(collection);
        }

        void e() {
            e<K, V>.k kVar = this.f19239c;
            if (kVar != null) {
                kVar.e();
            } else {
                e.this.f19215e.put(this.f19237a, this.f19238b);
            }
        }

        @Override // java.util.Collection
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            k();
            return this.f19238b.equals(obj);
        }

        e<K, V>.k g() {
            return this.f19239c;
        }

        Collection<V> h() {
            return this.f19238b;
        }

        @Override // java.util.Collection
        public int hashCode() {
            k();
            return this.f19238b.hashCode();
        }

        K i() {
            return this.f19237a;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            k();
            return new a();
        }

        void k() {
            Collection<V> collection;
            e<K, V>.k kVar = this.f19239c;
            if (kVar != null) {
                kVar.k();
                if (this.f19239c.h() != this.f19240d) {
                    throw new ConcurrentModificationException();
                }
            } else if (!this.f19238b.isEmpty() || (collection = (Collection) e.this.f19215e.get(this.f19237a)) == null) {
            } else {
                this.f19238b = collection;
            }
        }

        void n() {
            e<K, V>.k kVar = this.f19239c;
            if (kVar != null) {
                kVar.n();
            } else if (this.f19238b.isEmpty()) {
                e.this.f19215e.remove(this.f19237a);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object obj) {
            k();
            boolean remove = this.f19238b.remove(obj);
            if (remove) {
                e.o(e.this);
                n();
            }
            return remove;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            boolean removeAll = this.f19238b.removeAll(collection);
            if (removeAll) {
                e.p(e.this, this.f19238b.size() - size);
                n();
            }
            return removeAll;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            com.google.common.base.l.n(collection);
            int size = size();
            boolean retainAll = this.f19238b.retainAll(collection);
            if (retainAll) {
                e.p(e.this, this.f19238b.size() - size);
                n();
            }
            return retainAll;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            k();
            return this.f19238b.size();
        }

        @Override // java.util.AbstractCollection
        public String toString() {
            k();
            return this.f19238b.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class l extends e<K, V>.k implements List<V> {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private class a extends e<K, V>.k.a implements ListIterator<V> {
            a() {
                super();
            }

            public a(int i8) {
                super(l.this.p().listIterator(i8));
            }

            private ListIterator<V> c() {
                return (ListIterator) a();
            }

            @Override // java.util.ListIterator
            public void add(V v8) {
                boolean isEmpty = l.this.isEmpty();
                c().add(v8);
                e.n(e.this);
                if (isEmpty) {
                    l.this.e();
                }
            }

            @Override // java.util.ListIterator
            public boolean hasPrevious() {
                return c().hasPrevious();
            }

            @Override // java.util.ListIterator
            public int nextIndex() {
                return c().nextIndex();
            }

            @Override // java.util.ListIterator
            public V previous() {
                return c().previous();
            }

            @Override // java.util.ListIterator
            public int previousIndex() {
                return c().previousIndex();
            }

            @Override // java.util.ListIterator
            public void set(V v8) {
                c().set(v8);
            }
        }

        l(K k8, List<V> list, e<K, V>.k kVar) {
            super(k8, list, kVar);
        }

        @Override // java.util.List
        public void add(int i8, V v8) {
            k();
            boolean isEmpty = h().isEmpty();
            p().add(i8, v8);
            e.n(e.this);
            if (isEmpty) {
                e();
            }
        }

        @Override // java.util.List
        public boolean addAll(int i8, Collection<? extends V> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            boolean addAll = p().addAll(i8, collection);
            if (addAll) {
                e.p(e.this, h().size() - size);
                if (size == 0) {
                    e();
                }
            }
            return addAll;
        }

        @Override // java.util.List
        public V get(int i8) {
            k();
            return p().get(i8);
        }

        @Override // java.util.List
        public int indexOf(Object obj) {
            k();
            return p().indexOf(obj);
        }

        @Override // java.util.List
        public int lastIndexOf(Object obj) {
            k();
            return p().lastIndexOf(obj);
        }

        @Override // java.util.List
        public ListIterator<V> listIterator() {
            k();
            return new a();
        }

        @Override // java.util.List
        public ListIterator<V> listIterator(int i8) {
            k();
            return new a(i8);
        }

        List<V> p() {
            return (List) h();
        }

        @Override // java.util.List
        public V remove(int i8) {
            k();
            V remove = p().remove(i8);
            e.o(e.this);
            n();
            return remove;
        }

        @Override // java.util.List
        public V set(int i8, V v8) {
            k();
            return p().set(i8, v8);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.List
        public List<V> subList(int i8, int i9) {
            k();
            return e.this.E(i(), p().subList(i8, i9), g() == null ? this : g());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class m extends e<K, V>.o implements NavigableSet<V> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public m(K k8, NavigableSet<V> navigableSet, e<K, V>.k kVar) {
            super(k8, navigableSet, kVar);
        }

        private NavigableSet<V> t(NavigableSet<V> navigableSet) {
            return new m(this.f19237a, navigableSet, g() == null ? this : g());
        }

        @Override // java.util.NavigableSet
        public V ceiling(V v8) {
            return p().ceiling(v8);
        }

        @Override // java.util.NavigableSet
        public Iterator<V> descendingIterator() {
            return new k.a(p().descendingIterator());
        }

        @Override // java.util.NavigableSet
        public NavigableSet<V> descendingSet() {
            return t(p().descendingSet());
        }

        @Override // java.util.NavigableSet
        public V floor(V v8) {
            return p().floor(v8);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<V> headSet(V v8, boolean z4) {
            return t(p().headSet(v8, z4));
        }

        @Override // java.util.NavigableSet
        public V higher(V v8) {
            return p().higher(v8);
        }

        @Override // java.util.NavigableSet
        public V lower(V v8) {
            return p().lower(v8);
        }

        @Override // java.util.NavigableSet
        public V pollFirst() {
            return (V) g1.q(iterator());
        }

        @Override // java.util.NavigableSet
        public V pollLast() {
            return (V) g1.q(descendingIterator());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.e.o
        /* renamed from: q */
        public NavigableSet<V> p() {
            return (NavigableSet) super.p();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<V> subSet(V v8, boolean z4, V v9, boolean z8) {
            return t(p().subSet(v8, z4, v9, z8));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<V> tailSet(V v8, boolean z4) {
            return t(p().tailSet(v8, z4));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class n extends e<K, V>.k implements Set<V> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public n(K k8, Set<V> set) {
            super(k8, set, null);
        }

        @Override // com.google.common.collect.e.k, java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            if (collection.isEmpty()) {
                return false;
            }
            int size = size();
            boolean i8 = p2.i((Set) this.f19238b, collection);
            if (i8) {
                e.p(e.this, this.f19238b.size() - size);
                n();
            }
            return i8;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class o extends e<K, V>.k implements SortedSet<V> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public o(K k8, SortedSet<V> sortedSet, e<K, V>.k kVar) {
            super(k8, sortedSet, kVar);
        }

        @Override // java.util.SortedSet
        public Comparator<? super V> comparator() {
            return p().comparator();
        }

        @Override // java.util.SortedSet
        public V first() {
            k();
            return p().first();
        }

        @Override // java.util.SortedSet
        public SortedSet<V> headSet(V v8) {
            k();
            return new o(i(), p().headSet(v8), g() == null ? this : g());
        }

        @Override // java.util.SortedSet
        public V last() {
            k();
            return p().last();
        }

        SortedSet<V> p() {
            return (SortedSet) h();
        }

        @Override // java.util.SortedSet
        public SortedSet<V> subSet(V v8, V v9) {
            k();
            return new o(i(), p().subSet(v8, v9), g() == null ? this : g());
        }

        @Override // java.util.SortedSet
        public SortedSet<V> tailSet(V v8) {
            k();
            return new o(i(), p().tailSet(v8), g() == null ? this : g());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public e(Map<K, Collection<V>> map) {
        com.google.common.base.l.d(map.isEmpty());
        this.f19215e = map;
    }

    static /* synthetic */ int n(e eVar) {
        int i8 = eVar.f19216f;
        eVar.f19216f = i8 + 1;
        return i8;
    }

    static /* synthetic */ int o(e eVar) {
        int i8 = eVar.f19216f;
        eVar.f19216f = i8 - 1;
        return i8;
    }

    static /* synthetic */ int p(e eVar, int i8) {
        int i9 = eVar.f19216f + i8;
        eVar.f19216f = i9;
        return i9;
    }

    static /* synthetic */ int q(e eVar, int i8) {
        int i9 = eVar.f19216f - i8;
        eVar.f19216f = i9;
        return i9;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> Iterator<E> y(Collection<E> collection) {
        return collection instanceof List ? ((List) collection).listIterator() : collection.iterator();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void z(Object obj) {
        Collection collection = (Collection) m1.p(this.f19215e, obj);
        if (collection != null) {
            int size = collection.size();
            collection.clear();
            this.f19216f -= size;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void A(Map<K, Collection<V>> map) {
        this.f19215e = map;
        this.f19216f = 0;
        for (Collection<V> collection : map.values()) {
            com.google.common.base.l.d(!collection.isEmpty());
            this.f19216f += collection.size();
        }
    }

    abstract <E> Collection<E> B(Collection<E> collection);

    abstract Collection<V> D(K k8, Collection<V> collection);

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<V> E(K k8, List<V> list, e<K, V>.k kVar) {
        return list instanceof RandomAccess ? new h(this, k8, list, kVar) : new l(k8, list, kVar);
    }

    @Override // com.google.common.collect.n1
    public Collection<V> a(Object obj) {
        Collection<V> remove = this.f19215e.remove(obj);
        if (remove == null) {
            return x();
        }
        Collection t8 = t();
        t8.addAll(remove);
        this.f19216f -= remove.size();
        remove.clear();
        return (Collection<V>) B(t8);
    }

    @Override // com.google.common.collect.n1
    public void clear() {
        for (Collection<V> collection : this.f19215e.values()) {
            collection.clear();
        }
        this.f19215e.clear();
        this.f19216f = 0;
    }

    @Override // com.google.common.collect.n1
    public boolean containsKey(Object obj) {
        return this.f19215e.containsKey(obj);
    }

    @Override // com.google.common.collect.h
    Map<K, Collection<V>> e() {
        return new c(this.f19215e);
    }

    @Override // com.google.common.collect.h
    Collection<Map.Entry<K, V>> f() {
        return this instanceof n2 ? new h.b(this) : new h.a();
    }

    @Override // com.google.common.collect.h
    Set<K> g() {
        return new C0151e(this.f19215e);
    }

    @Override // com.google.common.collect.n1
    public Collection<V> get(K k8) {
        Collection<V> collection = this.f19215e.get(k8);
        if (collection == null) {
            collection = u(k8);
        }
        return D(k8, collection);
    }

    @Override // com.google.common.collect.h
    Collection<V> h() {
        return new h.c();
    }

    @Override // com.google.common.collect.h
    public Collection<Map.Entry<K, V>> i() {
        return super.i();
    }

    @Override // com.google.common.collect.h
    Iterator<Map.Entry<K, V>> j() {
        return new b(this);
    }

    @Override // com.google.common.collect.h
    Iterator<V> k() {
        return new a(this);
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public boolean put(K k8, V v8) {
        Collection<V> collection = this.f19215e.get(k8);
        if (collection != null) {
            if (collection.add(v8)) {
                this.f19216f++;
                return true;
            }
            return false;
        }
        Collection<V> u8 = u(k8);
        if (u8.add(v8)) {
            this.f19216f++;
            this.f19215e.put(k8, u8);
            return true;
        }
        throw new AssertionError("New Collection violated the Collection spec");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<K, Collection<V>> s() {
        return this.f19215e;
    }

    @Override // com.google.common.collect.n1
    public int size() {
        return this.f19216f;
    }

    abstract Collection<V> t();

    /* JADX INFO: Access modifiers changed from: package-private */
    public Collection<V> u(K k8) {
        return t();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map<K, Collection<V>> v() {
        Map<K, Collection<V>> map = this.f19215e;
        return map instanceof NavigableMap ? new f((NavigableMap) this.f19215e) : map instanceof SortedMap ? new i((SortedMap) this.f19215e) : new c(this.f19215e);
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public Collection<V> values() {
        return super.values();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Set<K> w() {
        Map<K, Collection<V>> map = this.f19215e;
        return map instanceof NavigableMap ? new g((NavigableMap) this.f19215e) : map instanceof SortedMap ? new j((SortedMap) this.f19215e) : new C0151e(this.f19215e);
    }

    abstract Collection<V> x();
}
