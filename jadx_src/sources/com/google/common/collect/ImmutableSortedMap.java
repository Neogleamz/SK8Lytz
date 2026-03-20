package com.google.common.collect;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ImmutableSortedMap<K, V> extends b1<K, V> implements NavigableMap<K, V> {

    /* renamed from: h  reason: collision with root package name */
    private static final Comparator<Comparable> f19020h = y1.c();

    /* renamed from: j  reason: collision with root package name */
    private static final ImmutableSortedMap<Comparable, Object> f19021j = new ImmutableSortedMap<>(ImmutableSortedSet.X(y1.c()), ImmutableList.E());
    private static final long serialVersionUID = 0;

    /* renamed from: e  reason: collision with root package name */
    private final transient i2<K> f19022e;

    /* renamed from: f  reason: collision with root package name */
    private final transient ImmutableList<V> f19023f;

    /* renamed from: g  reason: collision with root package name */
    private transient ImmutableSortedMap<K, V> f19024g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends x0<K, V> {

        /* renamed from: com.google.common.collect.ImmutableSortedMap$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class C0149a extends ImmutableList<Map.Entry<K, V>> {
            C0149a() {
            }

            @Override // java.util.List
            /* renamed from: Q */
            public Map.Entry<K, V> get(int i8) {
                return new AbstractMap.SimpleImmutableEntry(ImmutableSortedMap.this.f19022e.e().get(i8), ImmutableSortedMap.this.f19023f.get(i8));
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // com.google.common.collect.ImmutableCollection
            public boolean n() {
                return true;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
            public int size() {
                return ImmutableSortedMap.this.size();
            }
        }

        a() {
        }

        @Override // com.google.common.collect.ImmutableSet
        ImmutableList<Map.Entry<K, V>> F() {
            return new C0149a();
        }

        @Override // com.google.common.collect.x0
        ImmutableMap<K, V> Q() {
            return ImmutableSortedMap.this;
        }

        @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        /* renamed from: p */
        public d3<Map.Entry<K, V>> iterator() {
            return e().iterator();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b<K, V> extends ImmutableMap.b<K, V> {

        /* renamed from: f  reason: collision with root package name */
        private transient Object[] f19027f;

        /* renamed from: g  reason: collision with root package name */
        private transient Object[] f19028g;

        /* renamed from: h  reason: collision with root package name */
        private final Comparator<? super K> f19029h;

        public b(Comparator<? super K> comparator) {
            this(comparator, 4);
        }

        private b(Comparator<? super K> comparator, int i8) {
            this.f19029h = (Comparator) com.google.common.base.l.n(comparator);
            this.f19027f = new Object[i8];
            this.f19028g = new Object[i8];
        }

        private void e(int i8) {
            Object[] objArr = this.f19027f;
            if (i8 > objArr.length) {
                int c9 = ImmutableCollection.b.c(objArr.length, i8);
                this.f19027f = Arrays.copyOf(this.f19027f, c9);
                this.f19028g = Arrays.copyOf(this.f19028g, c9);
            }
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: l */
        public ImmutableSortedMap<K, V> a() {
            return d();
        }

        @Override // com.google.common.collect.ImmutableMap.b
        @Deprecated
        /* renamed from: m */
        public final ImmutableSortedMap<K, V> c() {
            throw new UnsupportedOperationException("ImmutableSortedMap.Builder does not yet implement buildKeepingLast()");
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: n */
        public ImmutableSortedMap<K, V> d() {
            int i8 = this.f18968c;
            if (i8 != 0) {
                if (i8 == 1) {
                    Comparator<? super K> comparator = this.f19029h;
                    Object obj = this.f19027f[0];
                    Objects.requireNonNull(obj);
                    Object obj2 = this.f19028g[0];
                    Objects.requireNonNull(obj2);
                    return ImmutableSortedMap.D(comparator, obj, obj2);
                }
                Object[] copyOf = Arrays.copyOf(this.f19027f, i8);
                Arrays.sort(copyOf, this.f19029h);
                Object[] objArr = new Object[this.f18968c];
                for (int i9 = 0; i9 < this.f18968c; i9++) {
                    if (i9 > 0) {
                        int i10 = i9 - 1;
                        if (this.f19029h.compare(copyOf[i10], copyOf[i9]) == 0) {
                            String valueOf = String.valueOf(copyOf[i10]);
                            String valueOf2 = String.valueOf(copyOf[i9]);
                            StringBuilder sb = new StringBuilder(valueOf.length() + 57 + valueOf2.length());
                            sb.append("keys required to be distinct but compared as equal: ");
                            sb.append(valueOf);
                            sb.append(" and ");
                            sb.append(valueOf2);
                            throw new IllegalArgumentException(sb.toString());
                        }
                    }
                    Object obj3 = this.f19027f[i9];
                    Objects.requireNonNull(obj3);
                    int binarySearch = Arrays.binarySearch(copyOf, obj3, this.f19029h);
                    Object obj4 = this.f19028g[i9];
                    Objects.requireNonNull(obj4);
                    objArr[binarySearch] = obj4;
                }
                return new ImmutableSortedMap<>(new i2(ImmutableList.q(copyOf), this.f19029h), ImmutableList.q(objArr));
            }
            return ImmutableSortedMap.v(this.f19029h);
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: o */
        public b<K, V> g(K k8, V v8) {
            e(this.f18968c + 1);
            t.a(k8, v8);
            Object[] objArr = this.f19027f;
            int i8 = this.f18968c;
            objArr[i8] = k8;
            this.f19028g[i8] = v8;
            this.f18968c = i8 + 1;
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: p */
        public b<K, V> h(Map.Entry<? extends K, ? extends V> entry) {
            super.h(entry);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: q */
        public b<K, V> i(Iterable<? extends Map.Entry<? extends K, ? extends V>> iterable) {
            super.i(iterable);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.b
        /* renamed from: r */
        public b<K, V> j(Map<? extends K, ? extends V> map) {
            super.j(map);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c<K, V> extends ImmutableMap.d<K, V> {
        private static final long serialVersionUID = 0;

        /* renamed from: c  reason: collision with root package name */
        private final Comparator<? super K> f19030c;

        c(ImmutableSortedMap<K, V> immutableSortedMap) {
            super(immutableSortedMap);
            this.f19030c = immutableSortedMap.comparator();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableMap.d
        /* renamed from: c */
        public b<K, V> b(int i8) {
            return new b<>(this.f19030c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableSortedMap(i2<K> i2Var, ImmutableList<V> immutableList) {
        this(i2Var, immutableList, null);
    }

    ImmutableSortedMap(i2<K> i2Var, ImmutableList<V> immutableList, ImmutableSortedMap<K, V> immutableSortedMap) {
        this.f19022e = i2Var;
        this.f19023f = immutableList;
        this.f19024g = immutableSortedMap;
    }

    public static <K, V> ImmutableSortedMap<K, V> B() {
        return (ImmutableSortedMap<K, V>) f19021j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> ImmutableSortedMap<K, V> D(Comparator<? super K> comparator, K k8, V v8) {
        return new ImmutableSortedMap<>(new i2(ImmutableList.F(k8), (Comparator) com.google.common.base.l.n(comparator)), ImmutableList.F(v8));
    }

    static <K, V> ImmutableSortedMap<K, V> v(Comparator<? super K> comparator) {
        return y1.c().equals(comparator) ? B() : new ImmutableSortedMap<>(ImmutableSortedSet.X(comparator), ImmutableList.E());
    }

    private ImmutableSortedMap<K, V> w(int i8, int i9) {
        return (i8 == 0 && i9 == size()) ? this : i8 == i9 ? v(comparator()) : new ImmutableSortedMap<>(this.f19022e.s0(i8, i9), this.f19023f.subList(i8, i9));
    }

    @Override // java.util.NavigableMap
    /* renamed from: A */
    public ImmutableSortedSet<K> navigableKeySet() {
        return this.f19022e;
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    /* renamed from: E */
    public ImmutableSortedMap<K, V> subMap(K k8, K k9) {
        return subMap(k8, true, k9, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    /* renamed from: F */
    public ImmutableSortedMap<K, V> subMap(K k8, boolean z4, K k9, boolean z8) {
        com.google.common.base.l.n(k8);
        com.google.common.base.l.n(k9);
        com.google.common.base.l.j(comparator().compare(k8, k9) <= 0, "expected fromKey <= toKey but %s > %s", k8, k9);
        return headMap(k9, z8).tailMap(k8, z4);
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    /* renamed from: G */
    public ImmutableSortedMap<K, V> tailMap(K k8) {
        return tailMap(k8, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    /* renamed from: H */
    public ImmutableSortedMap<K, V> tailMap(K k8, boolean z4) {
        return w(this.f19022e.v0(com.google.common.base.l.n(k8), z4), size());
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> ceilingEntry(K k8) {
        return tailMap(k8, true).firstEntry();
    }

    @Override // java.util.NavigableMap
    public K ceilingKey(K k8) {
        return (K) m1.j(ceilingEntry(k8));
    }

    @Override // java.util.SortedMap
    public Comparator<? super K> comparator() {
        return m().comparator();
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<Map.Entry<K, V>> d() {
        return isEmpty() ? ImmutableSet.H() : new a();
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet<K> f() {
        throw new AssertionError("should never be called");
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> firstEntry() {
        if (isEmpty()) {
            return null;
        }
        return entrySet().e().get(0);
    }

    @Override // java.util.SortedMap
    public K firstKey() {
        return m().first();
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> floorEntry(K k8) {
        return headMap(k8, true).lastEntry();
    }

    @Override // java.util.NavigableMap
    public K floorKey(K k8) {
        return (K) m1.j(floorEntry(k8));
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public V get(Object obj) {
        int indexOf = this.f19022e.indexOf(obj);
        if (indexOf == -1) {
            return null;
        }
        return this.f19023f.get(indexOf);
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableCollection<V> h() {
        throw new AssertionError("should never be called");
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> higherEntry(K k8) {
        return tailMap(k8, false).firstEntry();
    }

    @Override // java.util.NavigableMap
    public K higherKey(K k8) {
        return (K) m1.j(higherEntry(k8));
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    /* renamed from: i */
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        return super.entrySet();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableMap
    public boolean k() {
        return this.f19022e.n() || this.f19023f.n();
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> lastEntry() {
        if (isEmpty()) {
            return null;
        }
        return entrySet().e().get(size() - 1);
    }

    @Override // java.util.SortedMap
    public K lastKey() {
        return m().last();
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> lowerEntry(K k8) {
        return headMap(k8, false).lastEntry();
    }

    @Override // java.util.NavigableMap
    public K lowerKey(K k8) {
        return (K) m1.j(lowerEntry(k8));
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    /* renamed from: p */
    public ImmutableCollection<V> values() {
        return this.f19023f;
    }

    @Override // java.util.NavigableMap
    @Deprecated
    public final Map.Entry<K, V> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableMap
    @Deprecated
    public final Map.Entry<K, V> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public int size() {
        return this.f19023f.size();
    }

    @Override // java.util.NavigableMap
    /* renamed from: t */
    public ImmutableSortedSet<K> descendingKeySet() {
        return this.f19022e.descendingSet();
    }

    @Override // java.util.NavigableMap
    /* renamed from: u */
    public ImmutableSortedMap<K, V> descendingMap() {
        ImmutableSortedMap<K, V> immutableSortedMap = this.f19024g;
        return immutableSortedMap == null ? isEmpty() ? v(y1.a(comparator()).f()) : new ImmutableSortedMap<>((i2) this.f19022e.descendingSet(), this.f19023f.L(), this) : immutableSortedMap;
    }

    @Override // com.google.common.collect.ImmutableMap
    Object writeReplace() {
        return new c(this);
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    /* renamed from: x */
    public ImmutableSortedMap<K, V> headMap(K k8) {
        return headMap(k8, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    /* renamed from: y */
    public ImmutableSortedMap<K, V> headMap(K k8, boolean z4) {
        return w(0, this.f19022e.u0(com.google.common.base.l.n(k8), z4));
    }

    @Override // com.google.common.collect.ImmutableMap
    /* renamed from: z */
    public ImmutableSortedSet<K> m() {
        return this.f19022e;
    }
}
