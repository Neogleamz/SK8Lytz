package com.google.common.collect;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Enum;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>> extends a<K, V> {
    private static final long serialVersionUID = 0;

    /* renamed from: f  reason: collision with root package name */
    private transient Class<K> f18898f;

    /* renamed from: g  reason: collision with root package name */
    private transient Class<V> f18899g;

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.f18898f = (Class) objectInputStream.readObject();
        this.f18899g = (Class) objectInputStream.readObject();
        H(new EnumMap(this.f18898f), new EnumMap(this.f18899g));
        m2.b(this, objectInputStream);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.f18898f);
        objectOutputStream.writeObject(this.f18899g);
        m2.i(this, objectOutputStream);
    }

    @Override // com.google.common.collect.a
    public /* bridge */ /* synthetic */ Set L() {
        return super.values();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.a
    /* renamed from: M */
    public K x(K k8) {
        return (K) com.google.common.base.l.n(k8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.a
    /* renamed from: N */
    public V y(V v8) {
        return (V) com.google.common.base.l.n(v8);
    }

    @Override // com.google.common.collect.a, com.google.common.collect.m0, java.util.Map
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // com.google.common.collect.a, com.google.common.collect.m0, java.util.Map
    public /* bridge */ /* synthetic */ boolean containsValue(Object obj) {
        return super.containsValue(obj);
    }

    @Override // com.google.common.collect.a, com.google.common.collect.m0, java.util.Map
    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.a, com.google.common.collect.r
    public /* bridge */ /* synthetic */ r g() {
        return super.g();
    }

    @Override // com.google.common.collect.a, com.google.common.collect.m0, java.util.Map
    public /* bridge */ /* synthetic */ Set keySet() {
        return super.keySet();
    }

    @Override // com.google.common.collect.a, com.google.common.collect.m0, java.util.Map
    public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
        return super.put(obj, obj2);
    }

    @Override // com.google.common.collect.a, com.google.common.collect.m0, java.util.Map
    public /* bridge */ /* synthetic */ void putAll(Map map) {
        super.putAll(map);
    }

    @Override // com.google.common.collect.a, com.google.common.collect.m0, java.util.Map
    public /* bridge */ /* synthetic */ Object remove(Object obj) {
        return super.remove(obj);
    }
}
