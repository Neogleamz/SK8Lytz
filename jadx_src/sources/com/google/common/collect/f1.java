package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f1 {

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a<T> extends i0<T> {

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Iterable f19264b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ com.google.common.base.m f19265c;

        a(Iterable iterable, com.google.common.base.m mVar) {
            this.f19264b = iterable;
            this.f19265c = mVar;
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            return g1.k(this.f19264b.iterator(), this.f19265c);
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b<T> extends i0<T> {

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Iterable f19266b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ com.google.common.base.g f19267c;

        b(Iterable iterable, com.google.common.base.g gVar) {
            this.f19266b = iterable;
            this.f19267c = gVar;
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            return g1.w(this.f19266b.iterator(), this.f19267c);
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c<T> extends i0<T> {

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Iterable f19268b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ int f19269c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Iterator<T> {

            /* renamed from: a  reason: collision with root package name */
            boolean f19270a = true;

            /* renamed from: b  reason: collision with root package name */
            final /* synthetic */ Iterator f19271b;

            a(c cVar, Iterator it) {
                this.f19271b = it;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.f19271b.hasNext();
            }

            @Override // java.util.Iterator
            public T next() {
                T t8 = (T) this.f19271b.next();
                this.f19270a = false;
                return t8;
            }

            @Override // java.util.Iterator
            public void remove() {
                t.d(!this.f19270a);
                this.f19271b.remove();
            }
        }

        c(Iterable iterable, int i8) {
            this.f19268b = iterable;
            this.f19269c = i8;
        }

        @Override // java.lang.Iterable
        public Iterator<T> iterator() {
            Iterable iterable = this.f19268b;
            if (iterable instanceof List) {
                List list = (List) iterable;
                return list.subList(Math.min(list.size(), this.f19269c), list.size()).iterator();
            }
            Iterator<T> it = iterable.iterator();
            g1.b(it, this.f19269c);
            return new a(this, it);
        }
    }

    public static <T> boolean a(Collection<T> collection, Iterable<? extends T> iterable) {
        return iterable instanceof Collection ? collection.addAll((Collection) iterable) : g1.a(collection, ((Iterable) com.google.common.base.l.n(iterable)).iterator());
    }

    public static <T> boolean b(Iterable<T> iterable, com.google.common.base.m<? super T> mVar) {
        return g1.c(iterable.iterator(), mVar);
    }

    private static <E> Collection<E> c(Iterable<E> iterable) {
        return iterable instanceof Collection ? (Collection) iterable : j1.i(iterable.iterator());
    }

    public static <T> Iterable<T> d(Iterable<T> iterable, com.google.common.base.m<? super T> mVar) {
        com.google.common.base.l.n(iterable);
        com.google.common.base.l.n(mVar);
        return new a(iterable, mVar);
    }

    public static <T> T e(Iterable<? extends T> iterable, T t8) {
        return (T) g1.o(iterable.iterator(), t8);
    }

    public static <T> T f(Iterable<T> iterable) {
        if (iterable instanceof List) {
            List list = (List) iterable;
            if (list.isEmpty()) {
                throw new NoSuchElementException();
            }
            return (T) h(list);
        }
        return (T) g1.m(iterable.iterator());
    }

    public static <T> T g(Iterable<? extends T> iterable, T t8) {
        if (iterable instanceof Collection) {
            if (((Collection) iterable).isEmpty()) {
                return t8;
            }
            if (iterable instanceof List) {
                return (T) h(j1.a(iterable));
            }
        }
        return (T) g1.n(iterable.iterator(), t8);
    }

    private static <T> T h(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static <T> boolean i(Iterable<T> iterable, com.google.common.base.m<? super T> mVar) {
        return ((iterable instanceof RandomAccess) && (iterable instanceof List)) ? j((List) iterable, (com.google.common.base.m) com.google.common.base.l.n(mVar)) : g1.s(iterable.iterator(), mVar);
    }

    private static <T> boolean j(List<T> list, com.google.common.base.m<? super T> mVar) {
        int i8 = 0;
        int i9 = 0;
        while (i8 < list.size()) {
            T t8 = list.get(i8);
            if (!mVar.apply(t8)) {
                if (i8 > i9) {
                    try {
                        list.set(i9, t8);
                    } catch (IllegalArgumentException unused) {
                        l(list, mVar, i9, i8);
                        return true;
                    } catch (UnsupportedOperationException unused2) {
                        l(list, mVar, i9, i8);
                        return true;
                    }
                }
                i9++;
            }
            i8++;
        }
        list.subList(i9, list.size()).clear();
        return i8 != i9;
    }

    public static <T> Iterable<T> k(Iterable<T> iterable, int i8) {
        com.google.common.base.l.n(iterable);
        com.google.common.base.l.e(i8 >= 0, "number to skip cannot be negative");
        return new c(iterable, i8);
    }

    private static <T> void l(List<T> list, com.google.common.base.m<? super T> mVar, int i8, int i9) {
        for (int size = list.size() - 1; size > i9; size--) {
            if (mVar.apply(list.get(size))) {
                list.remove(size);
            }
        }
        for (int i10 = i9 - 1; i10 >= i8; i10--) {
            list.remove(i10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Object[] m(Iterable<?> iterable) {
        return c(iterable).toArray();
    }

    public static String n(Iterable<?> iterable) {
        return g1.v(iterable.iterator());
    }

    public static <F, T> Iterable<T> o(Iterable<F> iterable, com.google.common.base.g<? super F, ? extends T> gVar) {
        com.google.common.base.l.n(iterable);
        com.google.common.base.l.n(gVar);
        return new b(iterable, gVar);
    }
}
