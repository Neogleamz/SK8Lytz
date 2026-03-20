package com.google.common.collect;

import java.util.concurrent.ConcurrentMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class k0<K, V> extends m0<K, V> implements ConcurrentMap<K, V> {
    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V putIfAbsent(K k8, V v8) {
        return q().putIfAbsent(k8, v8);
    }

    protected abstract ConcurrentMap<K, V> q();

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean remove(Object obj, Object obj2) {
        return q().remove(obj, obj2);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V replace(K k8, V v8) {
        return q().replace(k8, v8);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean replace(K k8, V v8, V v9) {
        return q().replace(k8, v8, v9);
    }
}
