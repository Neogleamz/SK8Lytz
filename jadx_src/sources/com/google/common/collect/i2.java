package com.google.common.collect;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i2<E> extends ImmutableSortedSet<E> {

    /* renamed from: f  reason: collision with root package name */
    static final i2<Comparable> f19327f = new i2<>(ImmutableList.E(), y1.c());

    /* renamed from: e  reason: collision with root package name */
    final transient ImmutableList<E> f19328e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i2(ImmutableList<E> immutableList, Comparator<? super E> comparator) {
        super(comparator);
        this.f19328e = immutableList;
    }

    private int w0(Object obj) {
        return Collections.binarySearch(this.f19328e, obj, x0());
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> T() {
        Comparator reverseOrder = Collections.reverseOrder(this.f19040c);
        return isEmpty() ? ImmutableSortedSet.X(reverseOrder) : new i2(this.f19328e.L(), reverseOrder);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    /* renamed from: V */
    public d3<E> descendingIterator() {
        return this.f19328e.L().iterator();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public E ceiling(E e8) {
        int v02 = v0(e8, true);
        if (v02 == size()) {
            return null;
        }
        return this.f19328e.get(v02);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        if (obj != null) {
            try {
                return w0(obj) >= 0;
            } catch (ClassCastException unused) {
                return false;
            }
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof r1) {
            collection = ((r1) collection).l();
        }
        if (!t2.b(comparator(), collection) || collection.size() <= 1) {
            return super.containsAll(collection);
        }
        d3<E> it = iterator();
        Iterator<?> it2 = collection.iterator();
        if (!it.hasNext()) {
            return false;
        }
        Object next = it2.next();
        E next2 = it.next();
        while (true) {
            try {
                int o02 = o0(next2, next);
                if (o02 < 0) {
                    if (!it.hasNext()) {
                        return false;
                    }
                    next2 = it.next();
                } else if (o02 == 0) {
                    if (!it2.hasNext()) {
                        return true;
                    }
                    next = it2.next();
                } else if (o02 > 0) {
                    break;
                }
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    public ImmutableList<E> e() {
        return this.f19328e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableSortedSet
    public ImmutableSortedSet<E> e0(E e8, boolean z4) {
        return s0(0, u0(e8, z4));
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0034 A[Catch: ClassCastException | NoSuchElementException -> 0x0046, TryCatch #0 {ClassCastException | NoSuchElementException -> 0x0046, blocks: (B:17:0x002a, B:18:0x002e, B:20:0x0034, B:22:0x003e), top: B:29:0x002a }] */
    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean equals(java.lang.Object r6) {
        /*
            r5 = this;
            r0 = 1
            if (r6 != r5) goto L4
            return r0
        L4:
            boolean r1 = r6 instanceof java.util.Set
            r2 = 0
            if (r1 != 0) goto La
            return r2
        La:
            java.util.Set r6 = (java.util.Set) r6
            int r1 = r5.size()
            int r3 = r6.size()
            if (r1 == r3) goto L17
            return r2
        L17:
            boolean r1 = r5.isEmpty()
            if (r1 == 0) goto L1e
            return r0
        L1e:
            java.util.Comparator<? super E> r1 = r5.f19040c
            boolean r1 = com.google.common.collect.t2.b(r1, r6)
            if (r1 == 0) goto L47
            java.util.Iterator r6 = r6.iterator()
            com.google.common.collect.d3 r1 = r5.iterator()     // Catch: java.lang.Throwable -> L46
        L2e:
            boolean r3 = r1.hasNext()     // Catch: java.lang.Throwable -> L46
            if (r3 == 0) goto L45
            java.lang.Object r3 = r1.next()     // Catch: java.lang.Throwable -> L46
            java.lang.Object r4 = r6.next()     // Catch: java.lang.Throwable -> L46
            if (r4 == 0) goto L44
            int r3 = r5.o0(r3, r4)     // Catch: java.lang.Throwable -> L46
            if (r3 == 0) goto L2e
        L44:
            return r2
        L45:
            return r0
        L46:
            return r2
        L47:
            boolean r6 = r5.containsAll(r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.i2.equals(java.lang.Object):boolean");
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.SortedSet
    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.f19328e.get(0);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public E floor(E e8) {
        int u02 = u0(e8, true) - 1;
        if (u02 == -1) {
            return null;
        }
        return this.f19328e.get(u02);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int g(Object[] objArr, int i8) {
        return this.f19328e.g(objArr, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public Object[] h() {
        return this.f19328e.h();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public E higher(E e8) {
        int v02 = v0(e8, false);
        if (v02 == size()) {
            return null;
        }
        return this.f19328e.get(v02);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int i() {
        return this.f19328e.i();
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> i0(E e8, boolean z4, E e9, boolean z8) {
        return l0(e8, z4).e0(e9, z8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int indexOf(Object obj) {
        if (obj == null) {
            return -1;
        }
        try {
            int binarySearch = Collections.binarySearch(this.f19328e, obj, x0());
            if (binarySearch >= 0) {
                return binarySearch;
            }
            return -1;
        } catch (ClassCastException unused) {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int k() {
        return this.f19328e.k();
    }

    @Override // com.google.common.collect.ImmutableSortedSet
    ImmutableSortedSet<E> l0(E e8, boolean z4) {
        return s0(v0(e8, z4), size());
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.SortedSet
    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.f19328e.get(size() - 1);
    }

    @Override // com.google.common.collect.ImmutableSortedSet, java.util.NavigableSet
    public E lower(E e8) {
        int u02 = u0(e8, false) - 1;
        if (u02 == -1) {
            return null;
        }
        return this.f19328e.get(u02);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean n() {
        return this.f19328e.n();
    }

    @Override // com.google.common.collect.ImmutableSortedSet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: p */
    public d3<E> iterator() {
        return this.f19328e.iterator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i2<E> s0(int i8, int i9) {
        return (i8 == 0 && i9 == size()) ? this : i8 < i9 ? new i2<>(this.f19328e.subList(i8, i9), this.f19040c) : ImmutableSortedSet.X(this.f19040c);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.f19328e.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int u0(E e8, boolean z4) {
        int binarySearch = Collections.binarySearch(this.f19328e, com.google.common.base.l.n(e8), comparator());
        return binarySearch >= 0 ? z4 ? binarySearch + 1 : binarySearch : ~binarySearch;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int v0(E e8, boolean z4) {
        int binarySearch = Collections.binarySearch(this.f19328e, com.google.common.base.l.n(e8), comparator());
        return binarySearch >= 0 ? z4 ? binarySearch : binarySearch + 1 : ~binarySearch;
    }

    Comparator<Object> x0() {
        return this.f19040c;
    }
}
