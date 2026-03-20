package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class m0<K, V> extends p0 implements Map<K, V> {
    public void clear() {
        h().clear();
    }

    public boolean containsKey(Object obj) {
        return h().containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        return h().containsValue(obj);
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return h().entrySet();
    }

    public boolean equals(Object obj) {
        return obj == this || h().equals(obj);
    }

    public V get(Object obj) {
        return h().get(obj);
    }

    public int hashCode() {
        return h().hashCode();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.p0
    /* renamed from: i */
    public abstract Map<K, V> h();

    public boolean isEmpty() {
        return h().isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean k(Object obj) {
        return m1.d(this, obj);
    }

    public Set<K> keySet() {
        return h().keySet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean n(Object obj) {
        return m1.e(this, obj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int p() {
        return p2.d(entrySet());
    }

    public V put(K k8, V v8) {
        return h().put(k8, v8);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        h().putAll(map);
    }

    public V remove(Object obj) {
        return h().remove(obj);
    }

    public int size() {
        return h().size();
    }

    public Collection<V> values() {
        return h().values();
    }
}
