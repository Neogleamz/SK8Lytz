package com.google.common.collect;

import java.io.Serializable;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class z0<K, V> extends ImmutableCollection<V> {

    /* renamed from: b  reason: collision with root package name */
    private final ImmutableMap<K, V> f19538b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends d3<V> {

        /* renamed from: a  reason: collision with root package name */
        final d3<Map.Entry<K, V>> f19539a;

        a() {
            this.f19539a = z0.this.f19538b.entrySet().iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f19539a.hasNext();
        }

        @Override // java.util.Iterator
        public V next() {
            return this.f19539a.next().getValue();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends ImmutableList<V> {

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ ImmutableList f19541c;

        b(z0 z0Var, ImmutableList immutableList) {
            this.f19541c = immutableList;
        }

        @Override // java.util.List
        public V get(int i8) {
            return (V) ((Map.Entry) this.f19541c.get(i8)).getValue();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean n() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.f19541c.size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c<V> implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        final ImmutableMap<?, V> f19542a;

        c(ImmutableMap<?, V> immutableMap) {
            this.f19542a = immutableMap;
        }

        Object readResolve() {
            return this.f19542a.values();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public z0(ImmutableMap<K, V> immutableMap) {
        this.f19538b = immutableMap;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        return obj != null && g1.f(iterator(), obj);
    }

    @Override // com.google.common.collect.ImmutableCollection
    public ImmutableList<V> e() {
        return new b(this, this.f19538b.entrySet().e());
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: p */
    public d3<V> iterator() {
        return new a();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.f19538b.size();
    }

    @Override // com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new c(this.f19538b);
    }
}
