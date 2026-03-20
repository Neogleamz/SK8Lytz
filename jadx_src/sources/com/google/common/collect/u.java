package com.google.common.collect;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<E> extends AbstractCollection<E> {

        /* renamed from: a  reason: collision with root package name */
        final Collection<E> f19453a;

        /* renamed from: b  reason: collision with root package name */
        final com.google.common.base.m<? super E> f19454b;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(Collection<E> collection, com.google.common.base.m<? super E> mVar) {
            this.f19453a = collection;
            this.f19454b = mVar;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean add(E e8) {
            com.google.common.base.l.d(this.f19454b.apply(e8));
            return this.f19453a.add(e8);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> collection) {
            for (E e8 : collection) {
                com.google.common.base.l.d(this.f19454b.apply(e8));
            }
            return this.f19453a.addAll(collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            f1.i(this.f19453a, this.f19454b);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            if (u.c(this.f19453a, obj)) {
                return this.f19454b.apply(obj);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            return u.a(this, collection);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return !f1.b(this.f19453a, this.f19454b);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<E> iterator() {
            return g1.k(this.f19453a.iterator(), this.f19454b);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object obj) {
            return contains(obj) && this.f19453a.remove(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            Iterator<E> it = this.f19453a.iterator();
            boolean z4 = false;
            while (it.hasNext()) {
                E next = it.next();
                if (this.f19454b.apply(next) && collection.contains(next)) {
                    it.remove();
                    z4 = true;
                }
            }
            return z4;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            Iterator<E> it = this.f19453a.iterator();
            boolean z4 = false;
            while (it.hasNext()) {
                E next = it.next();
                if (this.f19454b.apply(next) && !collection.contains(next)) {
                    it.remove();
                    z4 = true;
                }
            }
            return z4;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            int i8 = 0;
            for (E e8 : this.f19453a) {
                if (this.f19454b.apply(e8)) {
                    i8++;
                }
            }
            return i8;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public Object[] toArray() {
            return j1.i(iterator()).toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) j1.i(iterator()).toArray(tArr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(Collection<?> collection, Collection<?> collection2) {
        Iterator<?> it = collection2.iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static StringBuilder b(int i8) {
        t.b(i8, "size");
        return new StringBuilder((int) Math.min(i8 * 8, 1073741824L));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean c(Collection<?> collection, Object obj) {
        com.google.common.base.l.n(collection);
        try {
            return collection.contains(obj);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean d(Collection<?> collection, Object obj) {
        com.google.common.base.l.n(collection);
        try {
            return collection.remove(obj);
        } catch (ClassCastException | NullPointerException unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String e(Collection<?> collection) {
        StringBuilder b9 = b(collection.size());
        b9.append('[');
        boolean z4 = true;
        for (Object obj : collection) {
            if (!z4) {
                b9.append(", ");
            }
            z4 = false;
            if (obj == collection) {
                b9.append("(this Collection)");
            } else {
                b9.append(obj);
            }
        }
        b9.append(']');
        return b9.toString();
    }
}
