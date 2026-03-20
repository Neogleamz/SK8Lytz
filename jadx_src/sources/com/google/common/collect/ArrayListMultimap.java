package com.google.common.collect;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ArrayListMultimap<K, V> extends p<K, V> {
    private static final long serialVersionUID = 0;

    /* renamed from: g  reason: collision with root package name */
    transient int f18865g;

    private ArrayListMultimap() {
        this(12, 3);
    }

    private ArrayListMultimap(int i8, int i9) {
        super(z1.c(i8));
        t.b(i9, "expectedValuesPerKey");
        this.f18865g = i9;
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.f18865g = 3;
        int h8 = m2.h(objectInputStream);
        A(v.t());
        m2.e(this, objectInputStream, h8);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        m2.j(this, objectOutputStream);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.d
    public /* bridge */ /* synthetic */ List G(Object obj) {
        return super.get(obj);
    }

    @Override // com.google.common.collect.d
    public /* bridge */ /* synthetic */ List H(Object obj) {
        return super.a(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.e
    /* renamed from: I */
    public List<V> t() {
        return new ArrayList(this.f18865g);
    }

    @Override // com.google.common.collect.d, com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ Map b() {
        return super.b();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean c(Object obj, Object obj2) {
        return super.c(obj, obj2);
    }

    @Override // com.google.common.collect.e, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // com.google.common.collect.e, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean containsKey(Object obj) {
        return super.containsKey(obj);
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ boolean d(Object obj) {
        return super.d(obj);
    }

    @Override // com.google.common.collect.d, com.google.common.collect.h
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.e, com.google.common.collect.h
    public /* bridge */ /* synthetic */ Collection i() {
        return super.i();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ Set keySet() {
        return super.keySet();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.d, com.google.common.collect.e, com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean put(Object obj, Object obj2) {
        return super.put(obj, obj2);
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ boolean remove(Object obj, Object obj2) {
        return super.remove(obj, obj2);
    }

    @Override // com.google.common.collect.e, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.google.common.collect.e, com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ Collection values() {
        return super.values();
    }
}
