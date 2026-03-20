package com.google.common.collect;

import com.google.common.collect.p2;
import com.google.common.collect.r1;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s1 {

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a<E> extends b3<r1.a<E>, E> {
        a(Iterator it) {
            super(it);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.b3
        /* renamed from: b */
        public E a(r1.a<E> aVar) {
            return aVar.a();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class b<E> implements r1.a<E> {
        public boolean equals(Object obj) {
            if (obj instanceof r1.a) {
                r1.a aVar = (r1.a) obj;
                return getCount() == aVar.getCount() && com.google.common.base.k.a(a(), aVar.a());
            }
            return false;
        }

        public int hashCode() {
            E a9 = a();
            return (a9 == null ? 0 : a9.hashCode()) ^ getCount();
        }

        @Override // com.google.common.collect.r1.a
        public String toString() {
            String valueOf = String.valueOf(a());
            int count = getCount();
            if (count == 1) {
                return valueOf;
            }
            StringBuilder sb = new StringBuilder(valueOf.length() + 14);
            sb.append(valueOf);
            sb.append(" x ");
            sb.append(count);
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class c<E> extends p2.d<E> {
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            k().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return k().contains(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean containsAll(Collection<?> collection) {
            return k().containsAll(collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return k().isEmpty();
        }

        abstract r1<E> k();

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return k().B(obj, Integer.MAX_VALUE) > 0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return k().entrySet().size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class d<E> extends p2.d<r1.a<E>> {
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            k().clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (obj instanceof r1.a) {
                r1.a aVar = (r1.a) obj;
                return aVar.getCount() > 0 && k().m0(aVar.a()) == aVar.getCount();
            }
            return false;
        }

        abstract r1<E> k();

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (obj instanceof r1.a) {
                r1.a aVar = (r1.a) obj;
                E e8 = (E) aVar.a();
                int count = aVar.getCount();
                if (count != 0) {
                    return k().Y(e8, count, 0);
                }
            }
            return false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class e<E> extends b<E> implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        private final E f19437a;

        /* renamed from: b  reason: collision with root package name */
        private final int f19438b;

        e(E e8, int i8) {
            this.f19437a = e8;
            this.f19438b = i8;
            t.b(i8, "count");
        }

        @Override // com.google.common.collect.r1.a
        public final E a() {
            return this.f19437a;
        }

        @Override // com.google.common.collect.r1.a
        public final int getCount() {
            return this.f19438b;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class f<E> implements Iterator<E> {

        /* renamed from: a  reason: collision with root package name */
        private final r1<E> f19439a;

        /* renamed from: b  reason: collision with root package name */
        private final Iterator<r1.a<E>> f19440b;

        /* renamed from: c  reason: collision with root package name */
        private r1.a<E> f19441c;

        /* renamed from: d  reason: collision with root package name */
        private int f19442d;

        /* renamed from: e  reason: collision with root package name */
        private int f19443e;

        /* renamed from: f  reason: collision with root package name */
        private boolean f19444f;

        f(r1<E> r1Var, Iterator<r1.a<E>> it) {
            this.f19439a = r1Var;
            this.f19440b = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f19442d > 0 || this.f19440b.hasNext();
        }

        @Override // java.util.Iterator
        public E next() {
            if (hasNext()) {
                if (this.f19442d == 0) {
                    r1.a<E> next = this.f19440b.next();
                    this.f19441c = next;
                    int count = next.getCount();
                    this.f19442d = count;
                    this.f19443e = count;
                }
                this.f19442d--;
                this.f19444f = true;
                r1.a<E> aVar = this.f19441c;
                Objects.requireNonNull(aVar);
                return aVar.a();
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            t.d(this.f19444f);
            if (this.f19443e == 1) {
                this.f19440b.remove();
            } else {
                r1<E> r1Var = this.f19439a;
                r1.a<E> aVar = this.f19441c;
                Objects.requireNonNull(aVar);
                r1Var.remove(aVar.a());
            }
            this.f19443e--;
            this.f19444f = false;
        }
    }

    private static <E> boolean a(r1<E> r1Var, com.google.common.collect.f<? extends E> fVar) {
        if (fVar.isEmpty()) {
            return false;
        }
        fVar.n(r1Var);
        return true;
    }

    private static <E> boolean b(r1<E> r1Var, r1<? extends E> r1Var2) {
        if (r1Var2 instanceof com.google.common.collect.f) {
            return a(r1Var, (com.google.common.collect.f) r1Var2);
        }
        if (r1Var2.isEmpty()) {
            return false;
        }
        for (r1.a<? extends E> aVar : r1Var2.entrySet()) {
            r1Var.J(aVar.a(), aVar.getCount());
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> boolean c(r1<E> r1Var, Collection<? extends E> collection) {
        com.google.common.base.l.n(r1Var);
        com.google.common.base.l.n(collection);
        if (collection instanceof r1) {
            return b(r1Var, d(collection));
        }
        if (collection.isEmpty()) {
            return false;
        }
        return g1.a(r1Var, collection.iterator());
    }

    static <T> r1<T> d(Iterable<T> iterable) {
        return (r1) iterable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> Iterator<E> e(Iterator<r1.a<E>> it) {
        return new a(it);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean f(r1<?> r1Var, Object obj) {
        if (obj == r1Var) {
            return true;
        }
        if (obj instanceof r1) {
            r1 r1Var2 = (r1) obj;
            if (r1Var.size() == r1Var2.size() && r1Var.entrySet().size() == r1Var2.entrySet().size()) {
                for (r1.a aVar : r1Var2.entrySet()) {
                    if (r1Var.m0(aVar.a()) != aVar.getCount()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static <E> r1.a<E> g(E e8, int i8) {
        return new e(e8, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> Iterator<E> h(r1<E> r1Var) {
        return new f(r1Var, r1Var.entrySet().iterator());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean i(r1<?> r1Var, Collection<?> collection) {
        if (collection instanceof r1) {
            collection = ((r1) collection).l();
        }
        return r1Var.l().removeAll(collection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean j(r1<?> r1Var, Collection<?> collection) {
        com.google.common.base.l.n(collection);
        if (collection instanceof r1) {
            collection = ((r1) collection).l();
        }
        return r1Var.l().retainAll(collection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> int k(r1<E> r1Var, E e8, int i8) {
        t.b(i8, "count");
        int m02 = r1Var.m0(e8);
        int i9 = i8 - m02;
        if (i9 > 0) {
            r1Var.J(e8, i9);
        } else if (i9 < 0) {
            r1Var.B(e8, -i9);
        }
        return m02;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> boolean l(r1<E> r1Var, E e8, int i8, int i9) {
        t.b(i8, "oldCount");
        t.b(i9, "newCount");
        if (r1Var.m0(e8) == i8) {
            r1Var.U(e8, i9);
            return true;
        }
        return false;
    }
}
