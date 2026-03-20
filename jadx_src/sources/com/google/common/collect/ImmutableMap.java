package com.google.common.collect;

import com.google.common.collect.ImmutableCollection;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {

    /* renamed from: d  reason: collision with root package name */
    static final Map.Entry<?, ?>[] f18961d = new Map.Entry[0];

    /* renamed from: a  reason: collision with root package name */
    private transient ImmutableSet<Map.Entry<K, V>> f18962a;

    /* renamed from: b  reason: collision with root package name */
    private transient ImmutableSet<K> f18963b;

    /* renamed from: c  reason: collision with root package name */
    private transient ImmutableCollection<V> f18964c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends d3<K> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ d3 f18965a;

        a(ImmutableMap immutableMap, d3 d3Var) {
            this.f18965a = d3Var;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f18965a.hasNext();
        }

        @Override // java.util.Iterator
        public K next() {
            return (K) ((Map.Entry) this.f18965a.next()).getKey();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b<K, V> {

        /* renamed from: a  reason: collision with root package name */
        Comparator<? super V> f18966a;

        /* renamed from: b  reason: collision with root package name */
        Object[] f18967b;

        /* renamed from: c  reason: collision with root package name */
        int f18968c;

        /* renamed from: d  reason: collision with root package name */
        boolean f18969d;

        /* renamed from: e  reason: collision with root package name */
        a f18970e;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class a {

            /* renamed from: a  reason: collision with root package name */
            private final Object f18971a;

            /* renamed from: b  reason: collision with root package name */
            private final Object f18972b;

            /* renamed from: c  reason: collision with root package name */
            private final Object f18973c;

            /* JADX INFO: Access modifiers changed from: package-private */
            public a(Object obj, Object obj2, Object obj3) {
                this.f18971a = obj;
                this.f18972b = obj2;
                this.f18973c = obj3;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public IllegalArgumentException a() {
                String valueOf = String.valueOf(this.f18971a);
                String valueOf2 = String.valueOf(this.f18972b);
                String valueOf3 = String.valueOf(this.f18971a);
                String valueOf4 = String.valueOf(this.f18973c);
                StringBuilder sb = new StringBuilder(valueOf.length() + 39 + valueOf2.length() + valueOf3.length() + valueOf4.length());
                sb.append("Multiple entries with same key: ");
                sb.append(valueOf);
                sb.append("=");
                sb.append(valueOf2);
                sb.append(" and ");
                sb.append(valueOf3);
                sb.append("=");
                sb.append(valueOf4);
                return new IllegalArgumentException(sb.toString());
            }
        }

        public b() {
            this(4);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public b(int i8) {
            this.f18967b = new Object[i8 * 2];
            this.f18968c = 0;
            this.f18969d = false;
        }

        private ImmutableMap<K, V> b(boolean z4) {
            Object[] objArr;
            a aVar;
            a aVar2;
            if (!z4 || (aVar2 = this.f18970e) == null) {
                int i8 = this.f18968c;
                if (this.f18966a == null) {
                    objArr = this.f18967b;
                } else {
                    if (this.f18969d) {
                        this.f18967b = Arrays.copyOf(this.f18967b, i8 * 2);
                    }
                    objArr = this.f18967b;
                    if (!z4) {
                        objArr = f(objArr, this.f18968c);
                        if (objArr.length < this.f18967b.length) {
                            i8 = objArr.length >>> 1;
                        }
                    }
                    k(objArr, i8, this.f18966a);
                }
                this.f18969d = true;
                f2 r4 = f2.r(i8, objArr, this);
                if (!z4 || (aVar = this.f18970e) == null) {
                    return r4;
                }
                throw aVar.a();
            }
            throw aVar2.a();
        }

        private void e(int i8) {
            int i9 = i8 * 2;
            Object[] objArr = this.f18967b;
            if (i9 > objArr.length) {
                this.f18967b = Arrays.copyOf(objArr, ImmutableCollection.b.c(objArr.length, i9));
                this.f18969d = false;
            }
        }

        private Object[] f(Object[] objArr, int i8) {
            HashSet hashSet = new HashSet();
            BitSet bitSet = new BitSet();
            for (int i9 = i8 - 1; i9 >= 0; i9--) {
                Object obj = objArr[i9 * 2];
                Objects.requireNonNull(obj);
                if (!hashSet.add(obj)) {
                    bitSet.set(i9);
                }
            }
            if (bitSet.isEmpty()) {
                return objArr;
            }
            Object[] objArr2 = new Object[(i8 - bitSet.cardinality()) * 2];
            int i10 = 0;
            int i11 = 0;
            while (i10 < i8 * 2) {
                if (bitSet.get(i10 >>> 1)) {
                    i10 += 2;
                } else {
                    int i12 = i11 + 1;
                    int i13 = i10 + 1;
                    Object obj2 = objArr[i10];
                    Objects.requireNonNull(obj2);
                    objArr2[i11] = obj2;
                    i11 = i12 + 1;
                    i10 = i13 + 1;
                    Object obj3 = objArr[i13];
                    Objects.requireNonNull(obj3);
                    objArr2[i12] = obj3;
                }
            }
            return objArr2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static <V> void k(Object[] objArr, int i8, Comparator<? super V> comparator) {
            Map.Entry[] entryArr = new Map.Entry[i8];
            for (int i9 = 0; i9 < i8; i9++) {
                int i10 = i9 * 2;
                Object obj = objArr[i10];
                Objects.requireNonNull(obj);
                Object obj2 = objArr[i10 + 1];
                Objects.requireNonNull(obj2);
                entryArr[i9] = new AbstractMap.SimpleImmutableEntry(obj, obj2);
            }
            Arrays.sort(entryArr, 0, i8, y1.a(comparator).e(m1.s()));
            for (int i11 = 0; i11 < i8; i11++) {
                int i12 = i11 * 2;
                objArr[i12] = entryArr[i11].getKey();
                objArr[i12 + 1] = entryArr[i11].getValue();
            }
        }

        public ImmutableMap<K, V> a() {
            return d();
        }

        public ImmutableMap<K, V> c() {
            return b(false);
        }

        public ImmutableMap<K, V> d() {
            return b(true);
        }

        public b<K, V> g(K k8, V v8) {
            e(this.f18968c + 1);
            t.a(k8, v8);
            Object[] objArr = this.f18967b;
            int i8 = this.f18968c;
            objArr[i8 * 2] = k8;
            objArr[(i8 * 2) + 1] = v8;
            this.f18968c = i8 + 1;
            return this;
        }

        public b<K, V> h(Map.Entry<? extends K, ? extends V> entry) {
            return g(entry.getKey(), entry.getValue());
        }

        public b<K, V> i(Iterable<? extends Map.Entry<? extends K, ? extends V>> iterable) {
            if (iterable instanceof Collection) {
                e(this.f18968c + ((Collection) iterable).size());
            }
            for (Map.Entry<? extends K, ? extends V> entry : iterable) {
                h(entry);
            }
            return this;
        }

        public b<K, V> j(Map<? extends K, ? extends V> map) {
            return i(map.entrySet());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class c<K, V> extends ImmutableMap<K, V> {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a extends x0<K, V> {
            a() {
            }

            @Override // com.google.common.collect.x0
            ImmutableMap<K, V> Q() {
                return c.this;
            }

            @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            /* renamed from: p */
            public d3<Map.Entry<K, V>> iterator() {
                return c.this.q();
            }
        }

        @Override // com.google.common.collect.ImmutableMap
        ImmutableSet<Map.Entry<K, V>> d() {
            return new a();
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public /* bridge */ /* synthetic */ Set entrySet() {
            return super.entrySet();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableMap
        public ImmutableSet<K> f() {
            return new y0(this);
        }

        @Override // com.google.common.collect.ImmutableMap
        ImmutableCollection<V> h() {
            return new z0(this);
        }

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public /* bridge */ /* synthetic */ Set keySet() {
            return super.keySet();
        }

        abstract d3<Map.Entry<K, V>> q();

        @Override // com.google.common.collect.ImmutableMap, java.util.Map
        public /* bridge */ /* synthetic */ Collection values() {
            return super.values();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class d<K, V> implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final Object f18975a;

        /* renamed from: b  reason: collision with root package name */
        private final Object f18976b;

        /* JADX INFO: Access modifiers changed from: package-private */
        public d(ImmutableMap<K, V> immutableMap) {
            Object[] objArr = new Object[immutableMap.size()];
            Object[] objArr2 = new Object[immutableMap.size()];
            d3<Map.Entry<K, V>> it = immutableMap.entrySet().iterator();
            int i8 = 0;
            while (it.hasNext()) {
                Map.Entry<K, V> next = it.next();
                objArr[i8] = next.getKey();
                objArr2[i8] = next.getValue();
                i8++;
            }
            this.f18975a = objArr;
            this.f18976b = objArr2;
        }

        final Object a() {
            Object[] objArr = (Object[]) this.f18975a;
            Object[] objArr2 = (Object[]) this.f18976b;
            b<K, V> b9 = b(objArr.length);
            for (int i8 = 0; i8 < objArr.length; i8++) {
                b9.g((K) objArr[i8], (V) objArr2[i8]);
            }
            return b9.d();
        }

        b<K, V> b(int i8) {
            return new b<>(i8);
        }

        final Object readResolve() {
            Object obj = this.f18975a;
            if (obj instanceof ImmutableSet) {
                ImmutableSet immutableSet = (ImmutableSet) obj;
                b<K, V> b9 = b(immutableSet.size());
                d3 it = immutableSet.iterator();
                d3 it2 = ((ImmutableCollection) this.f18976b).iterator();
                while (it.hasNext()) {
                    b9.g((K) it.next(), (V) it2.next());
                }
                return b9.d();
            }
            return a();
        }
    }

    public static <K, V> b<K, V> a() {
        return new b<>();
    }

    public static <K, V> ImmutableMap<K, V> b(Iterable<? extends Map.Entry<? extends K, ? extends V>> iterable) {
        b bVar = new b(iterable instanceof Collection ? ((Collection) iterable).size() : 4);
        bVar.i(iterable);
        return bVar.a();
    }

    public static <K, V> ImmutableMap<K, V> c(Map<? extends K, ? extends V> map) {
        if ((map instanceof ImmutableMap) && !(map instanceof SortedMap)) {
            ImmutableMap<K, V> immutableMap = (ImmutableMap) map;
            if (!immutableMap.k()) {
                return immutableMap;
            }
        }
        return b(map.entrySet());
    }

    public static <K, V> ImmutableMap<K, V> n() {
        return (ImmutableMap<K, V>) f2.f19272h;
    }

    public static <K, V> ImmutableMap<K, V> o(K k8, V v8) {
        t.a(k8, v8);
        return f2.q(1, new Object[]{k8, v8});
    }

    @Override // java.util.Map
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return get(obj) != null;
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return values().contains(obj);
    }

    abstract ImmutableSet<Map.Entry<K, V>> d();

    @Override // java.util.Map
    public boolean equals(Object obj) {
        return m1.e(this, obj);
    }

    abstract ImmutableSet<K> f();

    @Override // java.util.Map
    public abstract V get(Object obj);

    @Override // java.util.Map
    public final V getOrDefault(Object obj, V v8) {
        V v9 = get(obj);
        return v9 != null ? v9 : v8;
    }

    abstract ImmutableCollection<V> h();

    @Override // java.util.Map
    public int hashCode() {
        return p2.d(entrySet());
    }

    @Override // java.util.Map
    /* renamed from: i */
    public ImmutableSet<Map.Entry<K, V>> entrySet() {
        ImmutableSet<Map.Entry<K, V>> immutableSet = this.f18962a;
        if (immutableSet == null) {
            ImmutableSet<Map.Entry<K, V>> d8 = d();
            this.f18962a = d8;
            return d8;
        }
        return immutableSet;
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean j() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean k();

    /* JADX INFO: Access modifiers changed from: package-private */
    public d3<K> l() {
        return new a(this, entrySet().iterator());
    }

    @Override // java.util.Map
    /* renamed from: m */
    public ImmutableSet<K> keySet() {
        ImmutableSet<K> immutableSet = this.f18963b;
        if (immutableSet == null) {
            ImmutableSet<K> f5 = f();
            this.f18963b = f5;
            return f5;
        }
        return immutableSet;
    }

    @Override // java.util.Map
    /* renamed from: p */
    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.f18964c;
        if (immutableCollection == null) {
            ImmutableCollection<V> h8 = h();
            this.f18964c = h8;
            return h8;
        }
        return immutableCollection;
    }

    @Override // java.util.Map
    @Deprecated
    public final V put(K k8, V v8) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    @Deprecated
    public final V remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return m1.q(this);
    }

    Object writeReplace() {
        return new d(this);
    }
}
