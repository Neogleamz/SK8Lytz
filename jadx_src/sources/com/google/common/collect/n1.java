package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface n1<K, V> {
    Collection<V> a(Object obj);

    Map<K, Collection<V>> b();

    boolean c(Object obj, Object obj2);

    void clear();

    boolean containsKey(Object obj);

    Collection<V> get(K k8);

    boolean isEmpty();

    Set<K> keySet();

    boolean put(K k8, V v8);

    boolean remove(Object obj, Object obj2);

    int size();

    Collection<V> values();
}
