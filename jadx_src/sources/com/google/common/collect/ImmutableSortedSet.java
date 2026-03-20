package com.google.common.collect;

import com.google.common.collect.ImmutableSet;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ImmutableSortedSet<E> extends d1<E> implements NavigableSet<E>, s2<E> {

    /* renamed from: c  reason: collision with root package name */
    final transient Comparator<? super E> f19040c;

    /* renamed from: d  reason: collision with root package name */
    transient ImmutableSortedSet<E> f19041d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a<E> extends ImmutableSet.a<E> {

        /* renamed from: f  reason: collision with root package name */
        private final Comparator<? super E> f19042f;

        public a(Comparator<? super E> comparator) {
            this.f19042f = (Comparator) com.google.common.base.l.n(comparator);
        }

        @Override // com.google.common.collect.ImmutableSet.a
        /* renamed from: m */
        public a<E> h(E e8) {
            super.a(e8);
            return this;
        }

        public a<E> n(E... eArr) {
            super.i(eArr);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.a
        /* renamed from: o */
        public a<E> j(Iterator<? extends E> it) {
            super.j(it);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.a
        /* renamed from: p */
        public ImmutableSortedSet<E> l() {
            ImmutableSortedSet<E> Q = ImmutableSortedSet.Q(this.f19042f, this.f18952b, this.f18951a);
            this.f18952b = Q.size();
            this.f18953c = true;
            return Q;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b<E> implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        final Comparator<? super E> f19043a;

        /* renamed from: b  reason: collision with root package name */
        final Object[] f19044b;

        public b(Comparator<? super E> comparator, Object[] objArr) {
            this.f19043a = comparator;
            this.f19044b = objArr;
        }

        /* JADX WARN: Multi-variable type inference failed */
        Object readResolve() {
            return new a(this.f19043a).n(this.f19044b).l();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImmutableSortedSet(Comparator<? super E> comparator) {
        this.f19040c = comparator;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static <E> ImmutableSortedSet<E> Q(Comparator<? super E> comparator, int i8, E... eArr) {
        if (i8 == 0) {
            return X(comparator);
        }
        v1.c(eArr, i8);
        Arrays.sort(eArr, 0, i8, comparator);
        int i9 = 1;
        for (int i10 = 1; i10 < i8; i10++) {
            Object obj = (Object) eArr[i10];
            if (comparator.compare(obj, (Object) eArr[i9 - 1]) != 0) {
                eArr[i9] = obj;
                i9++;
            }
        }
        Arrays.fill(eArr, i9, i8, (Object) null);
        if (i9 < eArr.length / 2) {
            eArr = (E[]) Arrays.copyOf(eArr, i9);
        }
        return new i2(ImmutableList.t(eArr, i9), comparator);
    }

    public static <E> ImmutableSortedSet<E> R(Comparator<? super E> comparator, Iterable<? extends E> iterable) {
        com.google.common.base.l.n(comparator);
        if (t2.b(comparator, iterable) && (iterable instanceof ImmutableSortedSet)) {
            ImmutableSortedSet<E> immutableSortedSet = (ImmutableSortedSet) iterable;
            if (!immutableSortedSet.n()) {
                return immutableSortedSet;
            }
        }
        Object[] m8 = f1.m(iterable);
        return Q(comparator, m8.length, m8);
    }

    public static <E> ImmutableSortedSet<E> S(Comparator<? super E> comparator, Collection<? extends E> collection) {
        return R(comparator, collection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> i2<E> X(Comparator<? super E> comparator) {
        return y1.c().equals(comparator) ? (i2<E>) i2.f19327f : new i2<>(ImmutableList.E(), comparator);
    }

    static int p0(Comparator<?> comparator, Object obj, Object obj2) {
        return comparator.compare(obj, obj2);
    }

    private void readObject(ObjectInputStream objectInputStream) {
        throw new InvalidObjectException("Use SerializedForm");
    }

    abstract ImmutableSortedSet<E> T();

    @Override // java.util.NavigableSet
    /* renamed from: V */
    public abstract d3<E> descendingIterator();

    @Override // java.util.NavigableSet
    /* renamed from: W */
    public ImmutableSortedSet<E> descendingSet() {
        ImmutableSortedSet<E> immutableSortedSet = this.f19041d;
        if (immutableSortedSet == null) {
            ImmutableSortedSet<E> T = T();
            this.f19041d = T;
            T.f19041d = this;
            return T;
        }
        return immutableSortedSet;
    }

    @Override // java.util.NavigableSet, java.util.SortedSet
    /* renamed from: Z */
    public ImmutableSortedSet<E> headSet(E e8) {
        return headSet(e8, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet
    /* renamed from: b0 */
    public ImmutableSortedSet<E> headSet(E e8, boolean z4) {
        return e0(com.google.common.base.l.n(e8), z4);
    }

    @Override // java.util.NavigableSet
    public E ceiling(E e8) {
        return (E) f1.e(tailSet(e8, true), null);
    }

    @Override // java.util.SortedSet, com.google.common.collect.s2
    public Comparator<? super E> comparator() {
        return this.f19040c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract ImmutableSortedSet<E> e0(E e8, boolean z4);

    @Override // java.util.NavigableSet, java.util.SortedSet
    /* renamed from: f0 */
    public ImmutableSortedSet<E> subSet(E e8, E e9) {
        return subSet(e8, true, e9, false);
    }

    @Override // java.util.SortedSet
    public E first() {
        return iterator().next();
    }

    @Override // java.util.NavigableSet
    public E floor(E e8) {
        return (E) g1.o(headSet(e8, true).descendingIterator(), null);
    }

    @Override // java.util.NavigableSet
    /* renamed from: h0 */
    public ImmutableSortedSet<E> subSet(E e8, boolean z4, E e9, boolean z8) {
        com.google.common.base.l.n(e8);
        com.google.common.base.l.n(e9);
        com.google.common.base.l.d(this.f19040c.compare(e8, e9) <= 0);
        return i0(e8, z4, e9, z8);
    }

    @Override // java.util.NavigableSet
    public E higher(E e8) {
        return (E) f1.e(tailSet(e8, false), null);
    }

    abstract ImmutableSortedSet<E> i0(E e8, boolean z4, E e9, boolean z8);

    @Override // java.util.NavigableSet, java.util.SortedSet
    /* renamed from: j0 */
    public ImmutableSortedSet<E> tailSet(E e8) {
        return tailSet(e8, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet
    /* renamed from: k0 */
    public ImmutableSortedSet<E> tailSet(E e8, boolean z4) {
        return l0(com.google.common.base.l.n(e8), z4);
    }

    abstract ImmutableSortedSet<E> l0(E e8, boolean z4);

    @Override // java.util.SortedSet
    public E last() {
        return descendingIterator().next();
    }

    @Override // java.util.NavigableSet
    public E lower(E e8) {
        return (E) g1.o(headSet(e8, false).descendingIterator(), null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int o0(Object obj, Object obj2) {
        return p0(this.f19040c, obj, obj2);
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: p */
    public abstract d3<E> iterator();

    @Override // java.util.NavigableSet
    @Deprecated
    public final E pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableSet
    @Deprecated
    public final E pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new b(this.f19040c, toArray());
    }
}
