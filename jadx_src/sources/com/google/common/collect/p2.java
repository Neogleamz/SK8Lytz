package com.google.common.collect;

import com.google.common.collect.u;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p2 {

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a<E> extends e<E> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Set f19420a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Set f19421b;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: com.google.common.collect.p2$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public class C0153a extends com.google.common.collect.c<E> {

            /* renamed from: c  reason: collision with root package name */
            final Iterator<E> f19422c;

            C0153a() {
                this.f19422c = a.this.f19420a.iterator();
            }

            @Override // com.google.common.collect.c
            protected E a() {
                while (this.f19422c.hasNext()) {
                    E next = this.f19422c.next();
                    if (a.this.f19421b.contains(next)) {
                        return next;
                    }
                }
                return b();
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        a(Set set, Set set2) {
            super(null);
            this.f19420a = set;
            this.f19421b = set2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f19420a.contains(obj) && this.f19421b.contains(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> collection) {
            return this.f19420a.containsAll(collection) && this.f19421b.containsAll(collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        /* renamed from: e */
        public d3<E> iterator() {
            return new C0153a();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return Collections.disjoint(this.f19421b, this.f19420a);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            int i8 = 0;
            for (E e8 : this.f19420a) {
                if (this.f19421b.contains(e8)) {
                    i8++;
                }
            }
            return i8;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b<E> extends u.a<E> implements Set<E> {
        b(Set<E> set, com.google.common.base.m<? super E> mVar) {
            super(set, mVar);
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(Object obj) {
            return p2.a(this, obj);
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            return p2.d(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c<E> extends b<E> implements SortedSet<E> {
        c(SortedSet<E> sortedSet, com.google.common.base.m<? super E> mVar) {
            super(sortedSet, mVar);
        }

        @Override // java.util.SortedSet
        public Comparator<? super E> comparator() {
            return ((SortedSet) this.f19453a).comparator();
        }

        @Override // java.util.SortedSet
        public E first() {
            return (E) g1.l(this.f19453a.iterator(), this.f19454b);
        }

        @Override // java.util.SortedSet
        public SortedSet<E> headSet(E e8) {
            return new c(((SortedSet) this.f19453a).headSet(e8), this.f19454b);
        }

        /* JADX WARN: Type inference failed for: r1v0, types: [E, java.lang.Object] */
        @Override // java.util.SortedSet
        public E last() {
            SortedSet sortedSet = (SortedSet) this.f19453a;
            while (true) {
                ?? r12 = (Object) sortedSet.last();
                if (this.f19454b.apply(r12)) {
                    return r12;
                }
                sortedSet = sortedSet.headSet(r12);
            }
        }

        @Override // java.util.SortedSet
        public SortedSet<E> subSet(E e8, E e9) {
            return new c(((SortedSet) this.f19453a).subSet(e8, e9), this.f19454b);
        }

        @Override // java.util.SortedSet
        public SortedSet<E> tailSet(E e8) {
            return new c(((SortedSet) this.f19453a).tailSet(e8), this.f19454b);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class d<E> extends AbstractSet<E> {
        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> collection) {
            return p2.i(this, collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> collection) {
            return super.retainAll((Collection) com.google.common.base.l.n(collection));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class e<E> extends AbstractSet<E> {
        private e() {
        }

        /* synthetic */ e(o2 o2Var) {
            this();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final boolean add(E e8) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final boolean addAll(Collection<? extends E> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        @Deprecated
        public final boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f<E> extends s0<E> implements NavigableSet<E>, Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final NavigableSet<E> f19424a;

        /* renamed from: b  reason: collision with root package name */
        private final SortedSet<E> f19425b;

        /* renamed from: c  reason: collision with root package name */
        private transient f<E> f19426c;

        f(NavigableSet<E> navigableSet) {
            this.f19424a = (NavigableSet) com.google.common.base.l.n(navigableSet);
            this.f19425b = Collections.unmodifiableSortedSet(navigableSet);
        }

        @Override // java.util.NavigableSet
        public E ceiling(E e8) {
            return this.f19424a.ceiling(e8);
        }

        @Override // java.util.NavigableSet
        public Iterator<E> descendingIterator() {
            return g1.x(this.f19424a.descendingIterator());
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> descendingSet() {
            f<E> fVar = this.f19426c;
            if (fVar == null) {
                f<E> fVar2 = new f<>(this.f19424a.descendingSet());
                this.f19426c = fVar2;
                fVar2.f19426c = this;
                return fVar2;
            }
            return fVar;
        }

        @Override // java.util.NavigableSet
        public E floor(E e8) {
            return this.f19424a.floor(e8);
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> headSet(E e8, boolean z4) {
            return p2.k(this.f19424a.headSet(e8, z4));
        }

        @Override // java.util.NavigableSet
        public E higher(E e8) {
            return this.f19424a.higher(e8);
        }

        @Override // java.util.NavigableSet
        public E lower(E e8) {
            return this.f19424a.lower(e8);
        }

        @Override // java.util.NavigableSet
        public E pollFirst() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.NavigableSet
        public E pollLast() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> subSet(E e8, boolean z4, E e9, boolean z8) {
            return p2.k(this.f19424a.subSet(e8, z4, e9, z8));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> tailSet(E e8, boolean z4) {
            return p2.k(this.f19424a.tailSet(e8, z4));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.common.collect.r0
        /* renamed from: y */
        public SortedSet<E> v() {
            return this.f19425b;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(Set<?> set, Object obj) {
        if (set == obj) {
            return true;
        }
        if (obj instanceof Set) {
            Set set2 = (Set) obj;
            try {
                if (set.size() == set2.size()) {
                    if (set.containsAll(set2)) {
                        return true;
                    }
                }
                return false;
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    public static <E> Set<E> b(Set<E> set, com.google.common.base.m<? super E> mVar) {
        if (set instanceof SortedSet) {
            return c((SortedSet) set, mVar);
        }
        if (set instanceof b) {
            b bVar = (b) set;
            return new b((Set) bVar.f19453a, com.google.common.base.n.b(bVar.f19454b, mVar));
        }
        return new b((Set) com.google.common.base.l.n(set), (com.google.common.base.m) com.google.common.base.l.n(mVar));
    }

    public static <E> SortedSet<E> c(SortedSet<E> sortedSet, com.google.common.base.m<? super E> mVar) {
        if (sortedSet instanceof b) {
            b bVar = (b) sortedSet;
            return new c((SortedSet) bVar.f19453a, com.google.common.base.n.b(bVar.f19454b, mVar));
        }
        return new c((SortedSet) com.google.common.base.l.n(sortedSet), (com.google.common.base.m) com.google.common.base.l.n(mVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int d(Set<?> set) {
        Iterator<?> it = set.iterator();
        int i8 = 0;
        while (it.hasNext()) {
            Object next = it.next();
            i8 = ~(~(i8 + (next != null ? next.hashCode() : 0)));
        }
        return i8;
    }

    public static <E> e<E> e(Set<E> set, Set<?> set2) {
        com.google.common.base.l.o(set, "set1");
        com.google.common.base.l.o(set2, "set2");
        return new a(set, set2);
    }

    public static <E> HashSet<E> f() {
        return new HashSet<>();
    }

    public static <E> HashSet<E> g(int i8) {
        return new HashSet<>(m1.b(i8));
    }

    public static <E> Set<E> h() {
        return Collections.newSetFromMap(m1.l());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean i(Set<?> set, Collection<?> collection) {
        com.google.common.base.l.n(collection);
        if (collection instanceof r1) {
            collection = ((r1) collection).l();
        }
        return (!(collection instanceof Set) || collection.size() <= set.size()) ? j(set, collection.iterator()) : g1.r(set.iterator(), collection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean j(Set<?> set, Iterator<?> it) {
        boolean z4 = false;
        while (it.hasNext()) {
            z4 |= set.remove(it.next());
        }
        return z4;
    }

    public static <E> NavigableSet<E> k(NavigableSet<E> navigableSet) {
        return ((navigableSet instanceof ImmutableCollection) || (navigableSet instanceof f)) ? navigableSet : new f(navigableSet);
    }
}
