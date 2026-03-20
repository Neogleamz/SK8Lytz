package com.google.common.collect;

import java.io.Serializable;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class x0<K, V> extends ImmutableSet<Map.Entry<K, V>> {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a<K, V> implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        final ImmutableMap<K, V> f19506a;

        a(ImmutableMap<K, V> immutableMap) {
            this.f19506a = immutableMap;
        }

        Object readResolve() {
            return this.f19506a.entrySet();
        }
    }

    @Override // com.google.common.collect.ImmutableSet
    boolean G() {
        return Q().j();
    }

    abstract ImmutableMap<K, V> Q();

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            V v8 = Q().get(entry.getKey());
            return v8 != null && v8.equals(entry.getValue());
        }
        return false;
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public int hashCode() {
        return Q().hashCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean n() {
        return Q().k();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return Q().size();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new a(Q());
    }
}
