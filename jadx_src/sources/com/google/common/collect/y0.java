package com.google.common.collect;

import java.io.Serializable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y0<K, V> extends e1<K> {

    /* renamed from: c  reason: collision with root package name */
    private final ImmutableMap<K, V> f19515c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a<K> implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        final ImmutableMap<K, ?> f19516a;

        a(ImmutableMap<K, ?> immutableMap) {
            this.f19516a = immutableMap;
        }

        Object readResolve() {
            return this.f19516a.keySet();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public y0(ImmutableMap<K, V> immutableMap) {
        this.f19515c = immutableMap;
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        return this.f19515c.containsKey(obj);
    }

    @Override // com.google.common.collect.e1
    K get(int i8) {
        return this.f19515c.entrySet().e().get(i8).getKey();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean n() {
        return true;
    }

    @Override // com.google.common.collect.e1, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: p */
    public d3<K> iterator() {
        return this.f19515c.l();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.f19515c.size();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new a(this.f19515c);
    }
}
