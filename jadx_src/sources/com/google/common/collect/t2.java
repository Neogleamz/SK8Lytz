package com.google.common.collect;

import java.util.Comparator;
import java.util.SortedSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class t2 {
    public static <E> Comparator<? super E> a(SortedSet<E> sortedSet) {
        Comparator<? super E> comparator = sortedSet.comparator();
        return comparator == null ? y1.c() : comparator;
    }

    public static boolean b(Comparator<?> comparator, Iterable<?> iterable) {
        Comparator comparator2;
        com.google.common.base.l.n(comparator);
        com.google.common.base.l.n(iterable);
        if (iterable instanceof SortedSet) {
            comparator2 = a((SortedSet) iterable);
        } else if (!(iterable instanceof s2)) {
            return false;
        } else {
            comparator2 = ((s2) iterable).comparator();
        }
        return comparator.equals(comparator2);
    }
}
