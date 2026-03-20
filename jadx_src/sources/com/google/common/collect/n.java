package com.google.common.collect;

import com.google.common.collect.e;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableSet;
import java.util.SortedSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class n<K, V> extends k<K, V> {
    private static final long serialVersionUID = 430848587173315748L;

    @Override // com.google.common.collect.k, com.google.common.collect.e
    Collection<V> D(K k8, Collection<V> collection) {
        return collection instanceof NavigableSet ? new e.m(k8, (NavigableSet) collection, null) : new e.o(k8, (SortedSet) collection, null);
    }

    abstract SortedSet<V> J();

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.k, com.google.common.collect.e
    /* renamed from: K */
    public SortedSet<V> x() {
        return (SortedSet<V>) B(J());
    }

    public SortedSet<V> L(K k8) {
        return (SortedSet) super.get(k8);
    }

    @Override // com.google.common.collect.k, com.google.common.collect.e, com.google.common.collect.n1
    /* renamed from: M */
    public SortedSet<V> a(Object obj) {
        return (SortedSet) super.a(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.k, com.google.common.collect.e
    /* renamed from: N */
    public <E> SortedSet<E> B(Collection<E> collection) {
        return collection instanceof NavigableSet ? p2.k((NavigableSet) collection) : Collections.unmodifiableSortedSet((SortedSet) collection);
    }

    @Override // com.google.common.collect.k, com.google.common.collect.h, com.google.common.collect.n1
    public Map<K, Collection<V>> b() {
        return super.b();
    }

    @Override // com.google.common.collect.e, com.google.common.collect.h, com.google.common.collect.n1
    public Collection<V> values() {
        return super.values();
    }
}
