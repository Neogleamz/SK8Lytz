package com.google.common.collect;

import com.google.common.collect.ImmutableList;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ImmutableCollection<E> extends AbstractCollection<E> implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    private static final Object[] f18950a = new Object[0];

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class a<E> extends b<E> {

        /* renamed from: a  reason: collision with root package name */
        Object[] f18951a;

        /* renamed from: b  reason: collision with root package name */
        int f18952b;

        /* renamed from: c  reason: collision with root package name */
        boolean f18953c;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(int i8) {
            t.b(i8, "initialCapacity");
            this.f18951a = new Object[i8];
            this.f18952b = 0;
        }

        private void g(int i8) {
            Object[] objArr = this.f18951a;
            if (objArr.length < i8) {
                this.f18951a = Arrays.copyOf(objArr, b.c(objArr.length, i8));
            } else if (!this.f18953c) {
                return;
            } else {
                this.f18951a = (Object[]) objArr.clone();
            }
            this.f18953c = false;
        }

        @Override // com.google.common.collect.ImmutableCollection.b
        public b<E> b(Iterable<? extends E> iterable) {
            if (iterable instanceof Collection) {
                Collection collection = (Collection) iterable;
                g(this.f18952b + collection.size());
                if (collection instanceof ImmutableCollection) {
                    this.f18952b = ((ImmutableCollection) collection).g(this.f18951a, this.f18952b);
                    return this;
                }
            }
            super.b(iterable);
            return this;
        }

        public a<E> d(E e8) {
            com.google.common.base.l.n(e8);
            g(this.f18952b + 1);
            Object[] objArr = this.f18951a;
            int i8 = this.f18952b;
            this.f18952b = i8 + 1;
            objArr[i8] = e8;
            return this;
        }

        public b<E> e(E... eArr) {
            f(eArr, eArr.length);
            return this;
        }

        final void f(Object[] objArr, int i8) {
            v1.c(objArr, i8);
            g(this.f18952b + i8);
            System.arraycopy(objArr, 0, this.f18951a, this.f18952b, i8);
            this.f18952b += i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b<E> {
        /* JADX INFO: Access modifiers changed from: package-private */
        public static int c(int i8, int i9) {
            if (i9 >= 0) {
                int i10 = i8 + (i8 >> 1) + 1;
                if (i10 < i9) {
                    i10 = Integer.highestOneBit(i9 - 1) << 1;
                }
                if (i10 < 0) {
                    return Integer.MAX_VALUE;
                }
                return i10;
            }
            throw new AssertionError("cannot store more than MAX_VALUE elements");
        }

        public abstract b<E> a(E e8);

        public b<E> b(Iterable<? extends E> iterable) {
            for (E e8 : iterable) {
                a(e8);
            }
            return this;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final boolean add(E e8) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final boolean addAll(Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public abstract boolean contains(Object obj);

    public ImmutableList<E> e() {
        return isEmpty() ? ImmutableList.E() : ImmutableList.q(toArray());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int g(Object[] objArr, int i8) {
        d3<E> it = iterator();
        while (it.hasNext()) {
            objArr[i8] = it.next();
            i8++;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object[] h() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int i() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int k() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean n();

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: p */
    public abstract d3<E> iterator();

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final boolean remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final boolean removeAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    @Deprecated
    public final boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final Object[] toArray() {
        return toArray(f18950a);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final <T> T[] toArray(T[] tArr) {
        com.google.common.base.l.n(tArr);
        int size = size();
        if (tArr.length < size) {
            Object[] h8 = h();
            if (h8 != null) {
                return (T[]) z1.a(h8, k(), i(), tArr);
            }
            tArr = (T[]) v1.e(tArr, size);
        } else if (tArr.length > size) {
            tArr[size] = null;
        }
        g(tArr, 0);
        return tArr;
    }

    Object writeReplace() {
        return new ImmutableList.d(toArray());
    }
}
