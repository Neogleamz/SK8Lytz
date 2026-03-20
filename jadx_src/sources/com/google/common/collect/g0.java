package com.google.common.collect;

import com.google.common.collect.r1;
import com.google.common.collect.s1;
import com.google.common.collect.v2;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class g0<E> extends o0<E> implements u2<E> {

    /* renamed from: a  reason: collision with root package name */
    private transient Comparator<? super E> f19286a;

    /* renamed from: b  reason: collision with root package name */
    private transient NavigableSet<E> f19287b;

    /* renamed from: c  reason: collision with root package name */
    private transient Set<r1.a<E>> f19288c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends s1.d<E> {
        a() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<r1.a<E>> iterator() {
            return g0.this.y();
        }

        @Override // com.google.common.collect.s1.d
        r1<E> k() {
            return g0.this;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return g0.this.A().entrySet().size();
        }
    }

    abstract u2<E> A();

    @Override // com.google.common.collect.u2
    public u2<E> P() {
        return A();
    }

    @Override // com.google.common.collect.u2
    public u2<E> P0(E e8, BoundType boundType, E e9, BoundType boundType2) {
        return A().P0(e9, boundType2, e8, boundType).P();
    }

    @Override // com.google.common.collect.u2, com.google.common.collect.s2
    public Comparator<? super E> comparator() {
        Comparator<? super E> comparator = this.f19286a;
        if (comparator == null) {
            y1 f5 = y1.a(A().comparator()).f();
            this.f19286a = f5;
            return f5;
        }
        return comparator;
    }

    @Override // com.google.common.collect.r1
    public Set<r1.a<E>> entrySet() {
        Set<r1.a<E>> set = this.f19288c;
        if (set == null) {
            Set<r1.a<E>> x8 = x();
            this.f19288c = x8;
            return x8;
        }
        return set;
    }

    @Override // com.google.common.collect.u2
    public r1.a<E> firstEntry() {
        return A().lastEntry();
    }

    @Override // com.google.common.collect.u2
    public u2<E> g0(E e8, BoundType boundType) {
        return A().n0(e8, boundType).P();
    }

    @Override // com.google.common.collect.r1
    public NavigableSet<E> l() {
        NavigableSet<E> navigableSet = this.f19287b;
        if (navigableSet == null) {
            v2.b bVar = new v2.b(this);
            this.f19287b = bVar;
            return bVar;
        }
        return navigableSet;
    }

    @Override // com.google.common.collect.u2
    public r1.a<E> lastEntry() {
        return A().firstEntry();
    }

    @Override // com.google.common.collect.u2
    public u2<E> n0(E e8, BoundType boundType) {
        return A().g0(e8, boundType).P();
    }

    @Override // com.google.common.collect.u2
    public r1.a<E> pollFirstEntry() {
        return A().pollLastEntry();
    }

    @Override // com.google.common.collect.u2
    public r1.a<E> pollLastEntry() {
        return A().pollFirstEntry();
    }

    @Override // com.google.common.collect.j0, java.util.Collection
    public Object[] toArray() {
        return q();
    }

    @Override // com.google.common.collect.j0, java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        return (T[]) t(tArr);
    }

    @Override // com.google.common.collect.p0
    public String toString() {
        return entrySet().toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.collect.j0
    /* renamed from: v */
    public r1<E> i() {
        return A();
    }

    Set<r1.a<E>> x() {
        return new a();
    }

    abstract Iterator<r1.a<E>> y();
}
