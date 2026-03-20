package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g1 {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [T] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a<T> extends d3<T> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Iterator f19290a;

        a(Iterator it) {
            this.f19290a = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f19290a.hasNext();
        }

        @Override // java.util.Iterator
        public T next() {
            return (T) this.f19290a.next();
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b<T> extends com.google.common.collect.c<T> {

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ Iterator f19291c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ com.google.common.base.m f19292d;

        b(Iterator it, com.google.common.base.m mVar) {
            this.f19291c = it;
            this.f19292d = mVar;
        }

        @Override // com.google.common.collect.c
        protected T a() {
            while (this.f19291c.hasNext()) {
                T t8 = (T) this.f19291c.next();
                if (this.f19292d.apply(t8)) {
                    return t8;
                }
            }
            return b();
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T, F] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c<F, T> extends b3<F, T> {

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ com.google.common.base.g f19293b;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(Iterator it, com.google.common.base.g gVar) {
            super(it);
            this.f19293b = gVar;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.b3
        public T a(F f5) {
            return (T) this.f19293b.apply(f5);
        }
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d<T> extends d3<T> {

        /* renamed from: a  reason: collision with root package name */
        boolean f19294a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ Object f19295b;

        d(Object obj) {
            this.f19295b = obj;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !this.f19294a;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.f19294a) {
                throw new NoSuchElementException();
            }
            this.f19294a = true;
            return (T) this.f19295b;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e<T> extends com.google.common.collect.b<T> {

        /* renamed from: e  reason: collision with root package name */
        static final e3<Object> f19296e = new e(new Object[0], 0, 0, 0);

        /* renamed from: c  reason: collision with root package name */
        private final T[] f19297c;

        /* renamed from: d  reason: collision with root package name */
        private final int f19298d;

        e(T[] tArr, int i8, int i9, int i10) {
            super(i9, i10);
            this.f19297c = tArr;
            this.f19298d = i8;
        }

        @Override // com.google.common.collect.b
        protected T a(int i8) {
            return this.f19297c[this.f19298d + i8];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum f implements Iterator<Object> {
        INSTANCE;

        @Override // java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.Iterator
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            t.d(false);
        }
    }

    public static <T> boolean a(Collection<T> collection, Iterator<? extends T> it) {
        com.google.common.base.l.n(collection);
        com.google.common.base.l.n(it);
        boolean z4 = false;
        while (it.hasNext()) {
            z4 |= collection.add(it.next());
        }
        return z4;
    }

    public static int b(Iterator<?> it, int i8) {
        com.google.common.base.l.n(it);
        int i9 = 0;
        com.google.common.base.l.e(i8 >= 0, "numberToAdvance must be nonnegative");
        while (i9 < i8 && it.hasNext()) {
            it.next();
            i9++;
        }
        return i9;
    }

    public static <T> boolean c(Iterator<T> it, com.google.common.base.m<? super T> mVar) {
        return p(it, mVar) != -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> ListIterator<T> d(Iterator<T> it) {
        return (ListIterator) it;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void e(Iterator<?> it) {
        com.google.common.base.l.n(it);
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    public static boolean f(Iterator<?> it, Object obj) {
        if (obj == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    return true;
                }
            }
            return false;
        }
        while (it.hasNext()) {
            if (obj.equals(it.next())) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:4:0x0006  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean g(java.util.Iterator<?> r3, java.util.Iterator<?> r4) {
        /*
        L0:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L1d
            boolean r0 = r4.hasNext()
            r1 = 0
            if (r0 != 0) goto Le
            return r1
        Le:
            java.lang.Object r0 = r3.next()
            java.lang.Object r2 = r4.next()
            boolean r0 = com.google.common.base.k.a(r0, r2)
            if (r0 != 0) goto L0
            return r1
        L1d:
            boolean r3 = r4.hasNext()
            r3 = r3 ^ 1
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.g1.g(java.util.Iterator, java.util.Iterator):boolean");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> d3<T> h() {
        return i();
    }

    static <T> e3<T> i() {
        return (e3<T>) e.f19296e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> Iterator<T> j() {
        return f.INSTANCE;
    }

    public static <T> d3<T> k(Iterator<T> it, com.google.common.base.m<? super T> mVar) {
        com.google.common.base.l.n(it);
        com.google.common.base.l.n(mVar);
        return new b(it, mVar);
    }

    public static <T> T l(Iterator<T> it, com.google.common.base.m<? super T> mVar) {
        com.google.common.base.l.n(it);
        com.google.common.base.l.n(mVar);
        while (it.hasNext()) {
            T next = it.next();
            if (mVar.apply(next)) {
                return next;
            }
        }
        throw new NoSuchElementException();
    }

    public static <T> T m(Iterator<T> it) {
        T next;
        do {
            next = it.next();
        } while (it.hasNext());
        return next;
    }

    public static <T> T n(Iterator<? extends T> it, T t8) {
        return it.hasNext() ? (T) m(it) : t8;
    }

    public static <T> T o(Iterator<? extends T> it, T t8) {
        return it.hasNext() ? it.next() : t8;
    }

    public static <T> int p(Iterator<T> it, com.google.common.base.m<? super T> mVar) {
        com.google.common.base.l.o(mVar, "predicate");
        int i8 = 0;
        while (it.hasNext()) {
            if (mVar.apply(it.next())) {
                return i8;
            }
            i8++;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> T q(Iterator<T> it) {
        if (it.hasNext()) {
            T next = it.next();
            it.remove();
            return next;
        }
        return null;
    }

    public static boolean r(Iterator<?> it, Collection<?> collection) {
        com.google.common.base.l.n(collection);
        boolean z4 = false;
        while (it.hasNext()) {
            if (collection.contains(it.next())) {
                it.remove();
                z4 = true;
            }
        }
        return z4;
    }

    public static <T> boolean s(Iterator<T> it, com.google.common.base.m<? super T> mVar) {
        com.google.common.base.l.n(mVar);
        boolean z4 = false;
        while (it.hasNext()) {
            if (mVar.apply(it.next())) {
                it.remove();
                z4 = true;
            }
        }
        return z4;
    }

    public static boolean t(Iterator<?> it, Collection<?> collection) {
        com.google.common.base.l.n(collection);
        boolean z4 = false;
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
                z4 = true;
            }
        }
        return z4;
    }

    public static <T> d3<T> u(T t8) {
        return new d(t8);
    }

    public static String v(Iterator<?> it) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        boolean z4 = true;
        while (it.hasNext()) {
            if (!z4) {
                sb.append(", ");
            }
            z4 = false;
            sb.append(it.next());
        }
        sb.append(']');
        return sb.toString();
    }

    public static <F, T> Iterator<T> w(Iterator<F> it, com.google.common.base.g<? super F, ? extends T> gVar) {
        com.google.common.base.l.n(gVar);
        return new c(it, gVar);
    }

    public static <T> d3<T> x(Iterator<? extends T> it) {
        com.google.common.base.l.n(it);
        return it instanceof d3 ? (d3) it : new a(it);
    }
}
