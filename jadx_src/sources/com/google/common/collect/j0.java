package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class j0<E> extends p0 implements Collection<E> {
    public boolean add(E e8) {
        return h().add(e8);
    }

    public boolean addAll(Collection<? extends E> collection) {
        return h().addAll(collection);
    }

    public void clear() {
        h().clear();
    }

    public boolean contains(Object obj) {
        return h().contains(obj);
    }

    public boolean containsAll(Collection<?> collection) {
        return h().containsAll(collection);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.p0
    /* renamed from: i */
    public abstract Collection<E> h();

    @Override // java.util.Collection
    public boolean isEmpty() {
        return h().isEmpty();
    }

    public Iterator<E> iterator() {
        return h().iterator();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean k(Collection<? extends E> collection) {
        return g1.a(this, collection.iterator());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean n(Collection<?> collection) {
        return u.a(this, collection);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean p(Collection<?> collection) {
        return g1.t(iterator(), collection);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object[] q() {
        return toArray(new Object[size()]);
    }

    public boolean remove(Object obj) {
        return h().remove(obj);
    }

    public boolean removeAll(Collection<?> collection) {
        return h().removeAll(collection);
    }

    public boolean retainAll(Collection<?> collection) {
        return h().retainAll(collection);
    }

    @Override // java.util.Collection
    public int size() {
        return h().size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T> T[] t(T[] tArr) {
        return (T[]) v1.g(this, tArr);
    }

    public Object[] toArray() {
        return h().toArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return (T[]) h().toArray(tArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String u() {
        return u.e(this);
    }
}
