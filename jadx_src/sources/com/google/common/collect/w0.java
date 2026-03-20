package com.google.common.collect;

import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class w0<K, V> extends g<K, V> implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    final K f19488a;

    /* renamed from: b  reason: collision with root package name */
    final V f19489b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w0(K k8, V v8) {
        this.f19488a = k8;
        this.f19489b = v8;
    }

    @Override // com.google.common.collect.g, java.util.Map.Entry
    public final K getKey() {
        return this.f19488a;
    }

    @Override // com.google.common.collect.g, java.util.Map.Entry
    public final V getValue() {
        return this.f19489b;
    }

    @Override // com.google.common.collect.g, java.util.Map.Entry
    public final V setValue(V v8) {
        throw new UnsupportedOperationException();
    }
}
