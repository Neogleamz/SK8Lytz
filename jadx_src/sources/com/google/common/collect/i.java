package com.google.common.collect;

import com.google.common.collect.r1;
import com.google.common.collect.s1;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class i<E> extends AbstractCollection<E> implements r1<E> {

    /* renamed from: a  reason: collision with root package name */
    private transient Set<E> f19321a;

    /* renamed from: b  reason: collision with root package name */
    private transient Set<r1.a<E>> f19322b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends s1.c<E> {
        a() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            return i.this.i();
        }

        @Override // com.google.common.collect.s1.c
        r1<E> k() {
            return i.this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends s1.d<E> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public b() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<r1.a<E>> iterator() {
            return i.this.k();
        }

        @Override // com.google.common.collect.s1.d
        r1<E> k() {
            return i.this;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return i.this.h();
        }
    }

    public int B(Object obj, int i8) {
        throw new UnsupportedOperationException();
    }

    public int J(E e8, int i8) {
        throw new UnsupportedOperationException();
    }

    public int U(E e8, int i8) {
        return s1.k(this, e8, i8);
    }

    public boolean Y(E e8, int i8, int i9) {
        return s1.l(this, e8, i8, i9);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean add(E e8) {
        J(e8, 1);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean addAll(Collection<? extends E> collection) {
        return s1.c(this, collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.r1
    public boolean contains(Object obj) {
        return m0(obj) > 0;
    }

    Set<E> e() {
        return new a();
    }

    public Set<r1.a<E>> entrySet() {
        Set<r1.a<E>> set = this.f19322b;
        if (set == null) {
            Set<r1.a<E>> g8 = g();
            this.f19322b = g8;
            return g8;
        }
        return set;
    }

    @Override // java.util.Collection, com.google.common.collect.r1
    public final boolean equals(Object obj) {
        return s1.f(this, obj);
    }

    Set<r1.a<E>> g() {
        return new b();
    }

    abstract int h();

    @Override // java.util.Collection, com.google.common.collect.r1
    public final int hashCode() {
        return entrySet().hashCode();
    }

    abstract Iterator<E> i();

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return entrySet().isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Iterator<r1.a<E>> k();

    public Set<E> l() {
        Set<E> set = this.f19321a;
        if (set == null) {
            Set<E> e8 = e();
            this.f19321a = e8;
            return e8;
        }
        return set;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.r1
    public final boolean remove(Object obj) {
        return B(obj, 1) > 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean removeAll(Collection<?> collection) {
        return s1.i(this, collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean retainAll(Collection<?> collection) {
        return s1.j(this, collection);
    }

    @Override // java.util.AbstractCollection
    public final String toString() {
        return entrySet().toString();
    }
}
