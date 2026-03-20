package com.google.common.collect;

import com.google.common.collect.e;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class k<K, V> extends e<K, V> implements n2<K, V> {
    private static final long serialVersionUID = 7431625294878419160L;

    /* JADX INFO: Access modifiers changed from: protected */
    public k(Map<K, Collection<V>> map) {
        super(map);
    }

    @Override // com.google.common.collect.e
    <E> Collection<E> B(Collection<E> collection) {
        return Collections.unmodifiableSet((Set) collection);
    }

    @Override // com.google.common.collect.e
    Collection<V> D(K k8, Collection<V> collection) {
        return new e.n(k8, (Set) collection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.e
    /* renamed from: F */
    public Set<V> x() {
        return Collections.emptySet();
    }

    @Override // com.google.common.collect.e, com.google.common.collect.h
    /* renamed from: G */
    public Set<Map.Entry<K, V>> i() {
        return (Set) super.i();
    }

    @Override // com.google.common.collect.e, com.google.common.collect.n1
    /* renamed from: H */
    public Set<V> get(K k8) {
        return (Set) super.get(k8);
    }

    @Override // com.google.common.collect.e, com.google.common.collect.n1
    /* renamed from: I */
    public Set<V> a(Object obj) {
        return (Set) super.a(obj);
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
