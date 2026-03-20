package com.google.common.collect;

import com.google.common.collect.r1;
import com.google.common.collect.s1;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class v2 {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<E> extends s1.c<E> implements SortedSet<E> {

        /* renamed from: a  reason: collision with root package name */
        private final u2<E> f19478a;

        a(u2<E> u2Var) {
            this.f19478a = u2Var;
        }

        @Override // java.util.SortedSet
        public Comparator<? super E> comparator() {
            return k().comparator();
        }

        @Override // java.util.SortedSet
        public E first() {
            return (E) v2.d(k().firstEntry());
        }

        @Override // java.util.SortedSet
        public SortedSet<E> headSet(E e8) {
            return k().g0(e8, BoundType.OPEN).l();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<E> iterator() {
            return s1.e(k().entrySet().iterator());
        }

        @Override // java.util.SortedSet
        public E last() {
            return (E) v2.d(k().lastEntry());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.s1.c
        /* renamed from: n */
        public final u2<E> k() {
            return this.f19478a;
        }

        @Override // java.util.SortedSet
        public SortedSet<E> subSet(E e8, E e9) {
            return k().P0(e8, BoundType.CLOSED, e9, BoundType.OPEN).l();
        }

        @Override // java.util.SortedSet
        public SortedSet<E> tailSet(E e8) {
            return k().n0(e8, BoundType.CLOSED).l();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b<E> extends a<E> implements NavigableSet<E> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public b(u2<E> u2Var) {
            super(u2Var);
        }

        @Override // java.util.NavigableSet
        public E ceiling(E e8) {
            return (E) v2.c(k().n0(e8, BoundType.CLOSED).firstEntry());
        }

        @Override // java.util.NavigableSet
        public Iterator<E> descendingIterator() {
            return descendingSet().iterator();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> descendingSet() {
            return new b(k().P());
        }

        @Override // java.util.NavigableSet
        public E floor(E e8) {
            return (E) v2.c(k().g0(e8, BoundType.CLOSED).lastEntry());
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> headSet(E e8, boolean z4) {
            return new b(k().g0(e8, BoundType.f(z4)));
        }

        @Override // java.util.NavigableSet
        public E higher(E e8) {
            return (E) v2.c(k().n0(e8, BoundType.OPEN).firstEntry());
        }

        @Override // java.util.NavigableSet
        public E lower(E e8) {
            return (E) v2.c(k().g0(e8, BoundType.OPEN).lastEntry());
        }

        @Override // java.util.NavigableSet
        public E pollFirst() {
            return (E) v2.c(k().pollFirstEntry());
        }

        @Override // java.util.NavigableSet
        public E pollLast() {
            return (E) v2.c(k().pollLastEntry());
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> subSet(E e8, boolean z4, E e9, boolean z8) {
            return new b(k().P0(e8, BoundType.f(z4), e9, BoundType.f(z8)));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> tailSet(E e8, boolean z4) {
            return new b(k().n0(e8, BoundType.f(z4)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> E c(r1.a<E> aVar) {
        if (aVar == null) {
            return null;
        }
        return aVar.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> E d(r1.a<E> aVar) {
        if (aVar != null) {
            return aVar.a();
        }
        throw new NoSuchElementException();
    }
}
