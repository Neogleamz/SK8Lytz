package com.google.common.collect;

import com.google.common.collect.r1;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e0<E> extends ImmutableSortedMultiset<E> {

    /* renamed from: e  reason: collision with root package name */
    private final transient ImmutableSortedMultiset<E> f19250e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e0(ImmutableSortedMultiset<E> immutableSortedMultiset) {
        this.f19250e = immutableSortedMultiset;
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.u2
    /* renamed from: D */
    public ImmutableSortedMultiset<E> g0(E e8, BoundType boundType) {
        return this.f19250e.n0(e8, boundType).P();
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.u2
    /* renamed from: F */
    public ImmutableSortedMultiset<E> n0(E e8, BoundType boundType) {
        return this.f19250e.g0(e8, boundType).P();
    }

    @Override // com.google.common.collect.u2
    public r1.a<E> firstEntry() {
        return this.f19250e.lastEntry();
    }

    @Override // com.google.common.collect.u2
    public r1.a<E> lastEntry() {
        return this.f19250e.firstEntry();
    }

    @Override // com.google.common.collect.r1
    public int m0(Object obj) {
        return this.f19250e.m0(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean n() {
        return this.f19250e.n();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.r1
    public int size() {
        return this.f19250e.size();
    }

    @Override // com.google.common.collect.ImmutableMultiset
    r1.a<E> v(int i8) {
        return this.f19250e.entrySet().e().L().get(i8);
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.u2
    /* renamed from: x */
    public ImmutableSortedMultiset<E> P() {
        return this.f19250e;
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.ImmutableMultiset
    /* renamed from: y */
    public ImmutableSortedSet<E> t() {
        return this.f19250e.t().descendingSet();
    }
}
