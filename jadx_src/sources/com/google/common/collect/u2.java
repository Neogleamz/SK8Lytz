package com.google.common.collect;

import com.google.common.collect.r1;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface u2<E> extends r1, s2<E> {
    u2<E> P();

    u2<E> P0(E e8, BoundType boundType, E e9, BoundType boundType2);

    Comparator<? super E> comparator();

    @Override // com.google.common.collect.r1
    Set<r1.a<E>> entrySet();

    r1.a<E> firstEntry();

    u2<E> g0(E e8, BoundType boundType);

    @Override // com.google.common.collect.r1
    NavigableSet<E> l();

    r1.a<E> lastEntry();

    u2<E> n0(E e8, BoundType boundType);

    r1.a<E> pollFirstEntry();

    r1.a<E> pollLastEntry();
}
