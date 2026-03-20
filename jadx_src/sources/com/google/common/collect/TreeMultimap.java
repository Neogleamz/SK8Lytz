package com.google.common.collect;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class TreeMultimap<K, V> extends l<K, V> {
    private static final long serialVersionUID = 0;

    /* renamed from: g  reason: collision with root package name */
    private transient Comparator<? super K> f19122g;

    /* renamed from: h  reason: collision with root package name */
    private transient Comparator<? super V> f19123h;

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.f19122g = (Comparator) com.google.common.base.l.n((Comparator) objectInputStream.readObject());
        this.f19123h = (Comparator) com.google.common.base.l.n((Comparator) objectInputStream.readObject());
        A(new TreeMap(this.f19122g));
        m2.d(this, objectInputStream);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(S());
        objectOutputStream.writeObject(U());
        m2.j(this, objectOutputStream);
    }

    @Override // com.google.common.collect.k
    public /* bridge */ /* synthetic */ Set G() {
        return super.i();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.e
    /* renamed from: J */
    public SortedSet<V> t() {
        return new TreeSet(this.f19123h);
    }

    @Override // com.google.common.collect.n
    public /* bridge */ /* synthetic */ SortedSet M(Object obj) {
        return super.a(obj);
    }

    @Override // com.google.common.collect.n, com.google.common.collect.k, com.google.common.collect.h, com.google.common.collect.n1
    /* renamed from: Q */
    public NavigableMap<K, Collection<V>> b() {
        return (NavigableMap) super.O();
    }

    @Override // com.google.common.collect.k, com.google.common.collect.e, com.google.common.collect.n1
    /* renamed from: R */
    public NavigableSet<V> get(K k8) {
        return (NavigableSet) super.L(k8);
    }

    @Deprecated
    public Comparator<? super K> S() {
        return this.f19122g;
    }

    @Override // com.google.common.collect.h, com.google.common.collect.n1
    /* renamed from: T */
    public NavigableSet<K> keySet() {
        return (NavigableSet) super.P();
    }

    public Comparator<? super V> U() {
        return this.f19123h;
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

    @Override // com.google.common.collect.e, com.google.common.collect.h
    Map<K, Collection<V>> e() {
        return v();
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.e
    public Collection<V> u(K k8) {
        if (k8 == 0) {
            S().compare(k8, k8);
        }
        return super.u(k8);
    }

    @Override // com.google.common.collect.n, com.google.common.collect.e, com.google.common.collect.h, com.google.common.collect.n1
    public /* bridge */ /* synthetic */ Collection values() {
        return super.values();
    }
}
