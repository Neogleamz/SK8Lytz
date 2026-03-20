package com.google.common.collect;

import java.util.Comparator;
import java.util.SortedSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class s0<E> extends r0<E> implements SortedSet<E> {
    @Override // java.util.SortedSet
    public Comparator<? super E> comparator() {
        return y().comparator();
    }

    @Override // java.util.SortedSet
    public E first() {
        return y().first();
    }

    @Override // java.util.SortedSet
    public SortedSet<E> headSet(E e8) {
        return y().headSet(e8);
    }

    @Override // java.util.SortedSet
    public E last() {
        return y().last();
    }

    @Override // java.util.SortedSet
    public SortedSet<E> subSet(E e8, E e9) {
        return y().subSet(e8, e9);
    }

    @Override // java.util.SortedSet
    public SortedSet<E> tailSet(E e8) {
        return y().tailSet(e8);
    }

    protected abstract SortedSet<E> y();
}
