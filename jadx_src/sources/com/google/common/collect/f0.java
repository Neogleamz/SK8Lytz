package com.google.common.collect;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f0<E> extends ImmutableSortedSet<E> {

    /* renamed from: e  reason: collision with root package name */
    private final ImmutableSortedSet<E> f19263e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f0(ImmutableSortedSet<E> immutableSortedSet) {
        super(y1.a(immutableSortedSet.comparator()).f());
        this.f19263e = immutableSortedSet;
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> T() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    /* renamed from: V */
    public d3<E> descendingIterator() {
        return this.f19263e.iterator();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    /* renamed from: W */
    public ImmutableSortedSet<E> descendingSet() {
        return this.f19263e;
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public E ceiling(E e8) {
        return this.f19263e.floor(e8);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        return this.f19263e.contains(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableSortedSet
    public ImmutableSortedSet<E> e0(E e8, boolean z4) {
        return this.f19263e.tailSet(e8, z4).descendingSet();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public E floor(E e8) {
        return this.f19263e.ceiling(e8);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public E higher(E e8) {
        return this.f19263e.lower(e8);
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> i0(E e8, boolean z4, E e9, boolean z8) {
        return this.f19263e.subSet(e9, z8, e8, z4).descendingSet();
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> l0(E e8, boolean z4) {
        return this.f19263e.headSet(e8, z4).descendingSet();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public E lower(E e8) {
        return this.f19263e.higher(e8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean n() {
        return this.f19263e.n();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: p */
    public d3<E> iterator() {
        return this.f19263e.descendingIterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.f19263e.size();
    }
}
