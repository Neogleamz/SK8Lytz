package com.google.common.collect;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Enum;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class EnumHashBiMap<K extends Enum<K>, V> extends a<K, V> {
    private static final long serialVersionUID = 0;

    /* renamed from: f  reason: collision with root package name */
    private transient Class<K> f18900f;

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.f18900f = (Class) objectInputStream.readObject();
        H(new EnumMap(this.f18900f), new HashMap((this.f18900f.getEnumConstants().length * 3) / 2));
        m2.b(this, objectInputStream);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.f18900f);
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

    @Override // com.google.common.collect.a, com.google.common.collect.m0, java.util.Map
    /* renamed from: N */
    public V put(K k8, V v8) {
        return (V) super.put(k8, v8);
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
    public /* bridge */ /* synthetic */ void putAll(Map map) {
        super.putAll(map);
    }

    @Override // com.google.common.collect.a, com.google.common.collect.m0, java.util.Map
    public /* bridge */ /* synthetic */ Object remove(Object obj) {
        return super.remove(obj);
    }
}
