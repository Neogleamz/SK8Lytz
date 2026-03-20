package com.google.common.collect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class d<K, V> extends e<K, V> implements i1<K, V> {
    private static final long serialVersionUID = 6588350623831699109L;

    /* JADX INFO: Access modifiers changed from: protected */
    public d(Map<K, Collection<V>> map) {
        super(map);
    }

    @Override // com.google.common.collect.e
    <E> Collection<E> B(Collection<E> collection) {
        return Collections.unmodifiableList((List) collection);
    }

    @Override // com.google.common.collect.e
    Collection<V> D(K k8, Collection<V> collection) {
        return E(k8, (List) collection, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.e
    /* renamed from: F */
    public List<V> x() {
        return Collections.emptyList();
    }

    @Override // com.google.common.collect.e, com.google.common.collect.n1
    /* renamed from: G */
    public List<V> get(K k8) {
        return (List) super.get(k8);
    }

    @Override // com.google.common.collect.e, com.google.common.collect.n1
    /* renamed from: H */
    public List<V> a(Object obj) {
        return (List) super.a(obj);
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public Map<K, Collection<V>> b() {
        return super.b();
    }

    @Override // com.google.common.collect.h
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.e, com.google.common.collect.h, com.google.common.collect.n1
    public boolean put(K k8, V v8) {
        return super.put(k8, v8);
    }
}
