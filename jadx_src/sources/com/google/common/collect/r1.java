package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface r1<E> extends Collection<E> {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a<E> {
        E a();

        int getCount();

        String toString();
    }

    int B(Object obj, int i8);

    int J(E e8, int i8);

    int U(E e8, int i8);

    boolean Y(E e8, int i8, int i9);

    @Override // java.util.Collection, com.google.common.collect.r1
    boolean contains(Object obj);

    @Override // java.util.Collection
    boolean containsAll(Collection<?> collection);

    Set<a<E>> entrySet();

    @Override // com.google.common.collect.r1
    boolean equals(Object obj);

    @Override // com.google.common.collect.r1
    int hashCode();

    Set<E> l();

    int m0(Object obj);

    @Override // java.util.Collection, com.google.common.collect.r1
    boolean remove(Object obj);

    @Override // java.util.Collection, com.google.common.collect.r1
    int size();
}
