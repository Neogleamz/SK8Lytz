package com.google.common.collect;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.p2;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m1 {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [V, K] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a<K, V> extends b3<Map.Entry<K, V>, K> {
        a(Iterator it) {
            super(it);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.b3
        /* renamed from: b */
        public K a(Map.Entry<K, V> entry) {
            return entry.getKey();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [V, K] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b<K, V> extends b3<Map.Entry<K, V>, V> {
        b(Iterator it) {
            super(it);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.b3
        /* renamed from: b */
        public V a(Map.Entry<K, V> entry) {
            return entry.getValue();
        }
    }

    /* JADX INFO: Add missing generic type declarations: [V, K] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c<K, V> extends b3<K, Map.Entry<K, V>> {

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ com.google.common.base.g f19400b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(Iterator it, com.google.common.base.g gVar) {
            super(it);
            this.f19400b = gVar;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.b3
        /* renamed from: b */
        public Map.Entry<K, V> a(K k8) {
            return m1.f(k8, this.f19400b.apply(k8));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [V, K] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d<K, V> extends com.google.common.collect.g<K, V> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Map.Entry f19401a;

        d(Map.Entry entry) {
            this.f19401a = entry;
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public K getKey() {
            return (K) this.f19401a.getKey();
        }

        @Override // com.google.common.collect.g, java.util.Map.Entry
        public V getValue() {
            return (V) this.f19401a.getValue();
        }
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
    /* JADX WARN: Unknown enum class pattern. Please report as an issue! */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static abstract class e implements com.google.common.base.g<Map.Entry<?, ?>, Object> {

        /* renamed from: a  reason: collision with root package name */
        public static final e f19402a = new a("KEY", 0);

        /* renamed from: b  reason: collision with root package name */
        public static final e f19403b = new b("VALUE", 1);

        /* renamed from: c  reason: collision with root package name */
        private static final /* synthetic */ e[] f19404c = c();

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum a extends e {
            a(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.base.g
            /* renamed from: f */
            public Object apply(Map.Entry<?, ?> entry) {
                return entry.getKey();
            }
        }

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        enum b extends e {
            b(String str, int i8) {
                super(str, i8, null);
            }

            @Override // com.google.common.base.g
            /* renamed from: f */
            public Object apply(Map.Entry<?, ?> entry) {
                return entry.getValue();
            }
        }

        private e(String str, int i8) {
        }

        /* synthetic */ e(String str, int i8, a aVar) {
            this(str, i8);
        }

        private static /* synthetic */ e[] c() {
            return new e[]{f19402a, f19403b};
        }

        public static e valueOf(String str) {
            return (e) Enum.valueOf(e.class, str);
        }

        public static e[] values() {
            return (e[]) f19404c.clone();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class f<K, V> extends p2.d<Map.Entry<K, V>> {
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            k().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                Object key = entry.getKey();
                Object o5 = m1.o(k(), key);
                if (com.google.common.base.k.a(o5, entry.getValue())) {
                    return o5 != null || k().containsKey(key);
                }
                return false;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return k().isEmpty();
        }

        abstract Map<K, V> k();

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (contains(obj) && (obj instanceof Map.Entry)) {
                return k().keySet().remove(((Map.Entry) obj).getKey());
            }
            return false;
        }

        @Override // com.google.common.collect.p2.d, java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> collection) {
            try {
                return super.removeAll((Collection) com.google.common.base.l.n(collection));
            } catch (UnsupportedOperationException unused) {
                return p2.j(this, collection.iterator());
            }
        }

        @Override // com.google.common.collect.p2.d, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> collection) {
            try {
                return super.retainAll((Collection) com.google.common.base.l.n(collection));
            } catch (UnsupportedOperationException unused) {
                HashSet g8 = p2.g(collection.size());
                for (Object obj : collection) {
                    if (contains(obj) && (obj instanceof Map.Entry)) {
                        g8.add(((Map.Entry) obj).getKey());
                    }
                }
                return k().keySet().retainAll(g8);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return k().size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class g<K, V> extends AbstractMap<K, V> {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends f<K, V> {
            a() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<K, V>> iterator() {
                return g.this.a();
            }

            @Override // com.google.common.collect.m1.f
            Map<K, V> k() {
                return g.this;
            }
        }

        abstract Iterator<Map.Entry<K, V>> a();

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            return new a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class h<K, V> extends p2.d<K> {

        /* renamed from: a  reason: collision with root package name */
        final Map<K, V> f19406a;

        /* JADX INFO: Access modifiers changed from: package-private */
        public h(Map<K, V> map) {
            this.f19406a = (Map) com.google.common.base.l.n(map);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            k().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return k().containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return k().isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return m1.i(k().entrySet().iterator());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Map<K, V> k() {
            return this.f19406a;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (contains(obj)) {
                k().remove(obj);
                return true;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return k().size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class i<K, V> extends h<K, V> implements SortedSet<K> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public i(SortedMap<K, V> sortedMap) {
            super(sortedMap);
        }

        @Override // java.util.SortedSet
        public Comparator<? super K> comparator() {
            return k().comparator();
        }

        @Override // java.util.SortedSet
        public K first() {
            return k().firstKey();
        }

        @Override // java.util.SortedSet
        public SortedSet<K> headSet(K k8) {
            return new i(k().headMap(k8));
        }

        @Override // java.util.SortedSet
        public K last() {
            return k().lastKey();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.m1.h
        /* renamed from: n */
        public SortedMap<K, V> k() {
            return (SortedMap) super.k();
        }

        @Override // java.util.SortedSet
        public SortedSet<K> subSet(K k8, K k9) {
            return new i(k().subMap(k8, k9));
        }

        @Override // java.util.SortedSet
        public SortedSet<K> tailSet(K k8) {
            return new i(k().tailMap(k8));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class j<K, V> extends AbstractCollection<V> {

        /* renamed from: a  reason: collision with root package name */
        final Map<K, V> f19407a;

        j(Map<K, V> map) {
            this.f19407a = (Map) com.google.common.base.l.n(map);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            e().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return e().containsValue(obj);
        }

        final Map<K, V> e() {
            return this.f19407a;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return e().isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return m1.t(e().entrySet().iterator());
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object obj) {
            try {
                return super.remove(obj);
            } catch (UnsupportedOperationException unused) {
                for (Map.Entry<K, V> entry : e().entrySet()) {
                    if (com.google.common.base.k.a(obj, entry.getValue())) {
                        e().remove(entry.getKey());
                        return true;
                    }
                }
                return false;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            try {
                return super.removeAll((Collection) com.google.common.base.l.n(collection));
            } catch (UnsupportedOperationException unused) {
                HashSet f5 = p2.f();
                for (Map.Entry<K, V> entry : e().entrySet()) {
                    if (collection.contains(entry.getValue())) {
                        f5.add(entry.getKey());
                    }
                }
                return e().keySet().removeAll(f5);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            try {
                return super.retainAll((Collection) com.google.common.base.l.n(collection));
            } catch (UnsupportedOperationException unused) {
                HashSet f5 = p2.f();
                for (Map.Entry<K, V> entry : e().entrySet()) {
                    if (collection.contains(entry.getValue())) {
                        f5.add(entry.getKey());
                    }
                }
                return e().keySet().retainAll(f5);
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return e().size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class k<K, V> extends AbstractMap<K, V> {

        /* renamed from: a  reason: collision with root package name */
        private transient Set<Map.Entry<K, V>> f19408a;

        /* renamed from: b  reason: collision with root package name */
        private transient Set<K> f19409b;

        /* renamed from: c  reason: collision with root package name */
        private transient Collection<V> f19410c;

        abstract Set<Map.Entry<K, V>> a();

        Set<K> b() {
            return new h(this);
        }

        Collection<V> c() {
            return new j(this);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> set = this.f19408a;
            if (set == null) {
                Set<Map.Entry<K, V>> a9 = a();
                this.f19408a = a9;
                return a9;
            }
            return set;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> keySet() {
            Set<K> set = this.f19409b;
            if (set == null) {
                Set<K> b9 = b();
                this.f19409b = b9;
                return b9;
            }
            return set;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<V> values() {
            Collection<V> collection = this.f19410c;
            if (collection == null) {
                Collection<V> c9 = c();
                this.f19410c = c9;
                return c9;
            }
            return collection;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Iterator<Map.Entry<K, V>> a(Set<K> set, com.google.common.base.g<? super K, V> gVar) {
        return new c(set.iterator(), gVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(int i8) {
        if (i8 < 3) {
            t.b(i8, "expectedSize");
            return i8 + 1;
        } else if (i8 < 1073741824) {
            return (int) ((i8 / 0.75f) + 1.0f);
        } else {
            return Integer.MAX_VALUE;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> boolean c(Collection<Map.Entry<K, V>> collection, Object obj) {
        if (obj instanceof Map.Entry) {
            return collection.contains(r((Map.Entry) obj));
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean d(Map<?, ?> map, Object obj) {
        return g1.f(t(map.entrySet().iterator()), obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean e(Map<?, ?> map, Object obj) {
        if (map == obj) {
            return true;
        }
        if (obj instanceof Map) {
            return map.entrySet().equals(((Map) obj).entrySet());
        }
        return false;
    }

    public static <K, V> Map.Entry<K, V> f(K k8, V v8) {
        return new w0(k8, v8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> ImmutableMap<E, Integer> g(Collection<E> collection) {
        ImmutableMap.b bVar = new ImmutableMap.b(collection.size());
        int i8 = 0;
        for (E e8 : collection) {
            bVar.g(e8, Integer.valueOf(i8));
            i8++;
        }
        return bVar.d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K> com.google.common.base.g<Map.Entry<K, ?>, K> h() {
        return e.f19402a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Iterator<K> i(Iterator<Map.Entry<K, V>> it) {
        return new a(it);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K> K j(Map.Entry<K, ?> entry) {
        if (entry == null) {
            return null;
        }
        return entry.getKey();
    }

    public static <K, V> HashMap<K, V> k() {
        return new HashMap<>();
    }

    public static <K, V> IdentityHashMap<K, V> l() {
        return new IdentityHashMap<>();
    }

    public static <K, V> LinkedHashMap<K, V> m() {
        return new LinkedHashMap<>();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean n(Map<?, ?> map, Object obj) {
        com.google.common.base.l.n(map);
        try {
            return map.containsKey(obj);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <V> V o(Map<?, V> map, Object obj) {
        com.google.common.base.l.n(map);
        try {
            return map.get(obj);
        } catch (ClassCastException | NullPointerException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <V> V p(Map<?, V> map, Object obj) {
        com.google.common.base.l.n(map);
        try {
            return map.remove(obj);
        } catch (ClassCastException | NullPointerException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String q(Map<?, ?> map) {
        StringBuilder b9 = u.b(map.size());
        b9.append('{');
        boolean z4 = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!z4) {
                b9.append(", ");
            }
            z4 = false;
            b9.append(entry.getKey());
            b9.append('=');
            b9.append(entry.getValue());
        }
        b9.append('}');
        return b9.toString();
    }

    static <K, V> Map.Entry<K, V> r(Map.Entry<? extends K, ? extends V> entry) {
        com.google.common.base.l.n(entry);
        return new d(entry);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <V> com.google.common.base.g<Map.Entry<?, V>, V> s() {
        return e.f19403b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> Iterator<V> t(Iterator<Map.Entry<K, V>> it) {
        return new b(it);
    }
}
