package com.google.common.collect;

import com.google.common.collect.ImmutableCollection;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {

    /* renamed from: b  reason: collision with root package name */
    private static final e3<Object> f18954b = new b(e2.f19252e, 0);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a<E> extends ImmutableCollection.a<E> {
        public a() {
            this(4);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(int i8) {
            super(i8);
        }

        @Override // com.google.common.collect.ImmutableCollection.b
        /* renamed from: h */
        public a<E> a(E e8) {
            super.d(e8);
            return this;
        }

        public a<E> i(E... eArr) {
            super.e(eArr);
            return this;
        }

        public a<E> j(Iterable<? extends E> iterable) {
            super.b(iterable);
            return this;
        }

        public ImmutableList<E> k() {
            this.f18953c = true;
            return ImmutableList.t(this.f18951a, this.f18952b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b<E> extends com.google.common.collect.b<E> {

        /* renamed from: c  reason: collision with root package name */
        private final ImmutableList<E> f18955c;

        b(ImmutableList<E> immutableList, int i8) {
            super(immutableList.size(), i8);
            this.f18955c = immutableList;
        }

        @Override // com.google.common.collect.b
        protected E a(int i8) {
            return this.f18955c.get(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c<E> extends ImmutableList<E> {

        /* renamed from: c  reason: collision with root package name */
        private final transient ImmutableList<E> f18956c;

        c(ImmutableList<E> immutableList) {
            this.f18956c = immutableList;
        }

        private int Q(int i8) {
            return (size() - 1) - i8;
        }

        private int R(int i8) {
            return size() - i8;
        }

        @Override // com.google.common.collect.ImmutableList
        public ImmutableList<E> L() {
            return this.f18956c;
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        /* renamed from: N */
        public ImmutableList<E> subList(int i8, int i9) {
            com.google.common.base.l.r(i8, i9, size());
            return this.f18956c.subList(R(i9), R(i8)).L();
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return this.f18956c.contains(obj);
        }

        @Override // java.util.List
        public E get(int i8) {
            com.google.common.base.l.l(i8, size());
            return this.f18956c.get(Q(i8));
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public int indexOf(Object obj) {
            int lastIndexOf = this.f18956c.lastIndexOf(obj);
            if (lastIndexOf >= 0) {
                return Q(lastIndexOf);
            }
            return -1;
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public int lastIndexOf(Object obj) {
            int indexOf = this.f18956c.indexOf(obj);
            if (indexOf >= 0) {
                return Q(indexOf);
            }
            return -1;
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator listIterator(int i8) {
            return super.listIterator(i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean n() {
            return this.f18956c.n();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.f18956c.size();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class d implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        final Object[] f18957a;

        /* JADX INFO: Access modifiers changed from: package-private */
        public d(Object[] objArr) {
            this.f18957a = objArr;
        }

        Object readResolve() {
            return ImmutableList.y(this.f18957a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e extends ImmutableList<E> {

        /* renamed from: c  reason: collision with root package name */
        final transient int f18958c;

        /* renamed from: d  reason: collision with root package name */
        final transient int f18959d;

        e(int i8, int i9) {
            this.f18958c = i8;
            this.f18959d = i9;
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        /* renamed from: N */
        public ImmutableList<E> subList(int i8, int i9) {
            com.google.common.base.l.r(i8, i9, this.f18959d);
            ImmutableList immutableList = ImmutableList.this;
            int i10 = this.f18958c;
            return immutableList.subList(i8 + i10, i9 + i10);
        }

        @Override // java.util.List
        public E get(int i8) {
            com.google.common.base.l.l(i8, this.f18959d);
            return ImmutableList.this.get(i8 + this.f18958c);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public Object[] h() {
            return ImmutableList.this.h();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public int i() {
            return ImmutableList.this.k() + this.f18958c + this.f18959d;
        }

        @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public /* bridge */ /* synthetic */ Iterator iterator() {
            return super.iterator();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public int k() {
            return ImmutableList.this.k() + this.f18958c;
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator listIterator() {
            return super.listIterator();
        }

        @Override // com.google.common.collect.ImmutableList, java.util.List
        public /* bridge */ /* synthetic */ ListIterator listIterator(int i8) {
            return super.listIterator(i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean n() {
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.f18959d;
        }
    }

    public static <E> ImmutableList<E> E() {
        return (ImmutableList<E>) e2.f19252e;
    }

    public static <E> ImmutableList<E> F(E e8) {
        return v(e8);
    }

    public static <E> ImmutableList<E> G(E e8, E e9) {
        return v(e8, e9);
    }

    public static <E> ImmutableList<E> H(E e8, E e9, E e10) {
        return v(e8, e9, e10);
    }

    public static <E> ImmutableList<E> I(E e8, E e9, E e10, E e11, E e12) {
        return v(e8, e9, e10, e11, e12);
    }

    public static <E> ImmutableList<E> K(E e8, E e9, E e10, E e11, E e12, E e13, E e14) {
        return v(e8, e9, e10, e11, e12, e13, e14);
    }

    public static <E> ImmutableList<E> M(Comparator<? super E> comparator, Iterable<? extends E> iterable) {
        com.google.common.base.l.n(comparator);
        Object[] m8 = f1.m(iterable);
        v1.b(m8);
        Arrays.sort(m8, comparator);
        return q(m8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> ImmutableList<E> q(Object[] objArr) {
        return t(objArr, objArr.length);
    }

    private void readObject(ObjectInputStream objectInputStream) {
        throw new InvalidObjectException("Use SerializedForm");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> ImmutableList<E> t(Object[] objArr, int i8) {
        return i8 == 0 ? E() : new e2(objArr, i8);
    }

    public static <E> a<E> u() {
        return new a<>();
    }

    private static <E> ImmutableList<E> v(Object... objArr) {
        return q(v1.b(objArr));
    }

    public static <E> ImmutableList<E> x(Collection<? extends E> collection) {
        if (collection instanceof ImmutableCollection) {
            ImmutableList<E> e8 = ((ImmutableCollection) collection).e();
            return e8.n() ? q(e8.toArray()) : e8;
        }
        return v(collection.toArray());
    }

    public static <E> ImmutableList<E> y(E[] eArr) {
        return eArr.length == 0 ? E() : v((Object[]) eArr.clone());
    }

    @Override // java.util.List
    /* renamed from: A */
    public e3<E> listIterator() {
        return listIterator(0);
    }

    @Override // java.util.List
    /* renamed from: D */
    public e3<E> listIterator(int i8) {
        com.google.common.base.l.p(i8, size());
        return isEmpty() ? (e3<E>) f18954b : new b(this, i8);
    }

    public ImmutableList<E> L() {
        return size() <= 1 ? this : new c(this);
    }

    @Override // java.util.List
    /* renamed from: N */
    public ImmutableList<E> subList(int i8, int i9) {
        com.google.common.base.l.r(i8, i9, size());
        int i10 = i9 - i8;
        return i10 == size() ? this : i10 == 0 ? E() : O(i8, i9);
    }

    ImmutableList<E> O(int i8, int i9) {
        return new e(i8, i9 - i8);
    }

    @Override // java.util.List
    @Deprecated
    public final void add(int i8, E e8) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    @Deprecated
    public final boolean addAll(int i8, Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    @Override // com.google.common.collect.ImmutableCollection
    @Deprecated
    public final ImmutableList<E> e() {
        return this;
    }

    @Override // java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        return j1.c(this, obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public int g(Object[] objArr, int i8) {
        int size = size();
        for (int i9 = 0; i9 < size; i9++) {
            objArr[i8 + i9] = get(i9);
        }
        return i8 + size;
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        int size = size();
        int i8 = 1;
        for (int i9 = 0; i9 < size; i9++) {
            i8 = ~(~((i8 * 31) + get(i9).hashCode()));
        }
        return i8;
    }

    @Override // java.util.List
    public int indexOf(Object obj) {
        if (obj == null) {
            return -1;
        }
        return j1.d(this, obj);
    }

    @Override // java.util.List
    public int lastIndexOf(Object obj) {
        if (obj == null) {
            return -1;
        }
        return j1.f(this, obj);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: p */
    public d3<E> iterator() {
        return listIterator();
    }

    @Override // java.util.List
    @Deprecated
    public final E remove(int i8) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List
    @Deprecated
    public final E set(int i8, E e8) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new d(toArray());
    }
}
