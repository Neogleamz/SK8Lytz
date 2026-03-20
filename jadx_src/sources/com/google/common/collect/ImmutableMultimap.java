package com.google.common.collect;

import com.google.common.collect.m2;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ImmutableMultimap<K, V> extends q<K, V> implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: e  reason: collision with root package name */
    final transient ImmutableMap<K, ? extends ImmutableCollection<V>> f18977e;

    /* renamed from: f  reason: collision with root package name */
    final transient int f18978f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends d3<Map.Entry<K, V>> {

        /* renamed from: a  reason: collision with root package name */
        final Iterator<? extends Map.Entry<K, ? extends ImmutableCollection<V>>> f18979a;

        /* renamed from: b  reason: collision with root package name */
        K f18980b = null;

        /* renamed from: c  reason: collision with root package name */
        Iterator<V> f18981c = g1.h();

        a() {
            this.f18979a = ImmutableMultimap.this.f18977e.entrySet().iterator();
        }

        @Override // java.util.Iterator
        /* renamed from: a */
        public Map.Entry<K, V> next() {
            if (!this.f18981c.hasNext()) {
                Map.Entry<K, ? extends ImmutableCollection<V>> next = this.f18979a.next();
                this.f18980b = next.getKey();
                this.f18981c = next.getValue().iterator();
            }
            K k8 = this.f18980b;
            Objects.requireNonNull(k8);
            return m1.f(k8, this.f18981c.next());
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f18981c.hasNext() || this.f18979a.hasNext();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends d3<V> {

        /* renamed from: a  reason: collision with root package name */
        Iterator<? extends ImmutableCollection<V>> f18983a;

        /* renamed from: b  reason: collision with root package name */
        Iterator<V> f18984b = g1.h();

        b() {
            this.f18983a = ImmutableMultimap.this.f18977e.values().iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f18984b.hasNext() || this.f18983a.hasNext();
        }

        @Override // java.util.Iterator
        public V next() {
            if (!this.f18984b.hasNext()) {
                this.f18984b = this.f18983a.next().iterator();
            }
            return this.f18984b.next();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c<K, V> {

        /* renamed from: a  reason: collision with root package name */
        final Map<K, Collection<V>> f18986a = z1.g();

        /* renamed from: b  reason: collision with root package name */
        Comparator<? super K> f18987b;

        /* renamed from: c  reason: collision with root package name */
        Comparator<? super V> f18988c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d<K, V> extends ImmutableCollection<Map.Entry<K, V>> {
        private static final long serialVersionUID = 0;

        /* renamed from: b  reason: collision with root package name */
        final ImmutableMultimap<K, V> f18989b;

        d(ImmutableMultimap<K, V> immutableMultimap) {
            this.f18989b = immutableMultimap;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry) obj;
                return this.f18989b.c(entry.getKey(), entry.getValue());
            }
            return false;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        /* renamed from: p */
        public d3<Map.Entry<K, V>> iterator() {
            return this.f18989b.j();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.f18989b.size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e {

        /* renamed from: a  reason: collision with root package name */
        static final m2.b<ImmutableMultimap> f18990a = m2.a(ImmutableMultimap.class, "map");

        /* renamed from: b  reason: collision with root package name */
        static final m2.b<ImmutableMultimap> f18991b = m2.a(ImmutableMultimap.class, "size");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f<K, V> extends ImmutableCollection<V> {
        private static final long serialVersionUID = 0;

        /* renamed from: b  reason: collision with root package name */
        private final transient ImmutableMultimap<K, V> f18992b;

        f(ImmutableMultimap<K, V> immutableMultimap) {
            this.f18992b = immutableMultimap;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return this.f18992b.d(obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public int g(Object[] objArr, int i8) {
            d3<? extends ImmutableCollection<V>> it = this.f18992b.f18977e.values().iterator();
            while (it.hasNext()) {
                i8 = it.next().g(objArr, i8);
            }
            return i8;
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        /* renamed from: p */
        public d3<V> iterator() {
            return this.f18992b.k();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.f18992b.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> immutableMap, int i8) {
        this.f18977e = immutableMap;
        this.f18978f = i8;
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean c(Object obj, Object obj2) {
        return super.c(obj, obj2);
    }

    @Override // com.google.common.collect.n1
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.n1
    public boolean containsKey(Object obj) {
        return this.f18977e.containsKey(obj);
    }

    @Override // com.google.common.collect.h
    public boolean d(Object obj) {
        return obj != null && super.d(obj);
    }

    @Override // com.google.common.collect.h
    Map<K, Collection<V>> e() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.h
    Set<K> g() {
        throw new AssertionError("unreachable");
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    /* renamed from: l */
    public ImmutableMap<K, Collection<V>> b() {
        return (ImmutableMap<K, ? extends ImmutableCollection<V>>) this.f18977e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.h
    /* renamed from: m */
    public ImmutableCollection<Map.Entry<K, V>> f() {
        return new d(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.h
    /* renamed from: n */
    public ImmutableCollection<V> h() {
        return new f(this);
    }

    @Override // com.google.common.collect.h
    /* renamed from: o */
    public ImmutableCollection<Map.Entry<K, V>> i() {
        return (ImmutableCollection) super.i();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.h
    /* renamed from: p */
    public d3<Map.Entry<K, V>> j() {
        return new a();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    @Deprecated
    public final boolean put(K k8, V v8) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.n1
    /* renamed from: q */
    public abstract ImmutableCollection<V> get(K k8);

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    /* renamed from: r */
    public ImmutableSet<K> keySet() {
        return this.f18977e.keySet();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    @Deprecated
    public final boolean remove(Object obj, Object obj2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.n1
    @Deprecated
    /* renamed from: s */
    public ImmutableCollection<V> a(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.n1
    public int size() {
        return this.f18978f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.h
    /* renamed from: t */
    public d3<V> k() {
        return new b();
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    /* renamed from: u */
    public ImmutableCollection<V> values() {
        return (ImmutableCollection) super.values();
    }
}
