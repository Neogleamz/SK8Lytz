package com.google.common.collect;

import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class n0<K, V> extends p0 implements Map.Entry<K, V> {
    @Override // java.util.Map.Entry
    public boolean equals(Object obj) {
        return i().equals(obj);
    }

    @Override // java.util.Map.Entry
    public K getKey() {
        return i().getKey();
    }

    @Override // java.util.Map.Entry
    public V getValue() {
        return i().getValue();
    }

    @Override // java.util.Map.Entry
    public int hashCode() {
        return i().hashCode();
    }

    protected abstract Map.Entry<K, V> i();

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean k(Object obj) {
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            return com.google.common.base.k.a(getKey(), entry.getKey()) && com.google.common.base.k.a(getValue(), entry.getValue());
        }
        return false;
    }

    public V setValue(V v8) {
        return i().setValue(v8);
    }
}
