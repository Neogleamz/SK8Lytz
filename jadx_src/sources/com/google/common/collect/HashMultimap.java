package com.google.common.collect;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class HashMultimap<K, V> extends u0<K, V> {
    private static final long serialVersionUID = 0;

    /* renamed from: g  reason: collision with root package name */
    transient int f18947g;

    private HashMultimap() {
        this(12, 2);
    }

    private HashMultimap(int i8, int i9) {
        super(z1.c(i8));
        this.f18947g = 2;
        com.google.common.base.l.d(i9 >= 0);
        this.f18947g = i9;
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.f18947g = 2;
        int h8 = m2.h(objectInputStream);
        A(z1.c(12));
        m2.e(this, objectInputStream, h8);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        m2.j(this, objectOutputStream);
    }

    @Override // com.google.common.collect.k
    public /* bridge */ /* synthetic */ Set G() {
        return super.i();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.k
    public /* bridge */ /* synthetic */ Set H(Object obj) {
        return super.get(obj);
    }

    @Override // com.google.common.collect.k
    public /* bridge */ /* synthetic */ Set I(Object obj) {
        return super.a(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.e
    /* renamed from: J */
    public Set<V> t() {
        return z1.d(this.f18947g);
    }

    @Override // com.google.common.collect.k, com.google.common.collect.h, com.google.common.collect.n1
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

    @Override // com.google.common.collect.k, com.google.common.collect.h
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.h
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
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
    @Override // com.google.common.collect.k, com.google.common.collect.e, com.google.common.collect.h, com.google.common.collect.n1
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
