package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class l<K, V> extends n<K, V> {
    public SortedMap<K, Collection<V>> O() {
        return (SortedMap) super.b();
    }

    public SortedSet<K> P() {
        return (SortedSet) super.keySet();
    }

    @Override // com.google.common.collect.e, com.google.common.collect.h
    Set<K> g() {
        return w();
    }
}
