package com.google.common.collect;

import com.google.common.collect.ImmutableCollection;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {

    /* renamed from: b  reason: collision with root package name */
    private transient ImmutableList<E> f19012b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<E> extends ImmutableCollection.a<E> {

        /* renamed from: d  reason: collision with root package name */
        Object[] f19013d;

        /* renamed from: e  reason: collision with root package name */
        private int f19014e;

        public a() {
            super(4);
        }

        private void k(E e8) {
            Objects.requireNonNull(this.f19013d);
            int length = this.f19013d.length - 1;
            int hashCode = e8.hashCode();
            int c9 = v0.c(hashCode);
            while (true) {
                int i8 = c9 & length;
                Object[] objArr = this.f19013d;
                Object obj = objArr[i8];
                if (obj == null) {
                    objArr[i8] = e8;
                    this.f19014e += hashCode;
                    super.d(e8);
                    return;
                } else if (obj.equals(e8)) {
                    return;
                } else {
                    c9 = i8 + 1;
                }
            }
        }

        @Override // com.google.common.collect.ImmutableCollection.b
        /* renamed from: h */
        public a<E> a(E e8) {
            com.google.common.base.l.n(e8);
            if (this.f19013d != null && ImmutableSet.v(this.f18952b) <= this.f19013d.length) {
                k(e8);
                return this;
            }
            this.f19013d = null;
            super.d(e8);
            return this;
        }

        public a<E> i(E... eArr) {
            if (this.f19013d != null) {
                for (E e8 : eArr) {
                    a(e8);
                }
            } else {
                super.e(eArr);
            }
            return this;
        }

        public a<E> j(Iterator<? extends E> it) {
            com.google.common.base.l.n(it);
            while (it.hasNext()) {
                a(it.next());
            }
            return this;
        }

        public ImmutableSet<E> l() {
            ImmutableSet<E> x8;
            int i8 = this.f18952b;
            if (i8 != 0) {
                if (i8 == 1) {
                    Object obj = this.f18951a[0];
                    Objects.requireNonNull(obj);
                    return ImmutableSet.I(obj);
                }
                if (this.f19013d == null || ImmutableSet.v(i8) != this.f19013d.length) {
                    x8 = ImmutableSet.x(this.f18952b, this.f18951a);
                    this.f18952b = x8.size();
                } else {
                    Object[] copyOf = ImmutableSet.O(this.f18952b, this.f18951a.length) ? Arrays.copyOf(this.f18951a, this.f18952b) : this.f18951a;
                    int i9 = this.f19014e;
                    Object[] objArr = this.f19013d;
                    x8 = new g2<>(copyOf, i9, objArr, objArr.length - 1, this.f18952b);
                }
                this.f18953c = true;
                this.f19013d = null;
                return x8;
            }
            return ImmutableSet.H();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b implements Serializable {
        private static final long serialVersionUID = 0;

        /* renamed from: a  reason: collision with root package name */
        final Object[] f19015a;

        b(Object[] objArr) {
            this.f19015a = objArr;
        }

        Object readResolve() {
            return ImmutableSet.E(this.f19015a);
        }
    }

    public static <E> ImmutableSet<E> A(Collection<? extends E> collection) {
        if ((collection instanceof ImmutableSet) && !(collection instanceof SortedSet)) {
            ImmutableSet<E> immutableSet = (ImmutableSet) collection;
            if (!immutableSet.n()) {
                return immutableSet;
            }
        }
        Object[] array = collection.toArray();
        return x(array.length, array);
    }

    public static <E> ImmutableSet<E> D(Iterator<? extends E> it) {
        if (it.hasNext()) {
            E next = it.next();
            return !it.hasNext() ? I(next) : new a().a(next).j(it).l();
        }
        return H();
    }

    public static <E> ImmutableSet<E> E(E[] eArr) {
        int length = eArr.length;
        return length != 0 ? length != 1 ? x(eArr.length, (Object[]) eArr.clone()) : I(eArr[0]) : H();
    }

    public static <E> ImmutableSet<E> H() {
        return g2.f19302j;
    }

    public static <E> ImmutableSet<E> I(E e8) {
        return new q2(e8);
    }

    public static <E> ImmutableSet<E> K(E e8, E e9) {
        return x(2, e8, e9);
    }

    public static <E> ImmutableSet<E> L(E e8, E e9, E e10) {
        return x(3, e8, e9, e10);
    }

    public static <E> ImmutableSet<E> M(E e8, E e9, E e10, E e11, E e12) {
        return x(5, e8, e9, e10, e11, e12);
    }

    @SafeVarargs
    public static <E> ImmutableSet<E> N(E e8, E e9, E e10, E e11, E e12, E e13, E... eArr) {
        com.google.common.base.l.e(eArr.length <= 2147483641, "the total number of elements must fit in an int");
        int length = eArr.length + 6;
        Object[] objArr = new Object[length];
        objArr[0] = e8;
        objArr[1] = e9;
        objArr[2] = e10;
        objArr[3] = e11;
        objArr[4] = e12;
        objArr[5] = e13;
        System.arraycopy(eArr, 0, objArr, 6, eArr.length);
        return x(length, objArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean O(int i8, int i9) {
        return i8 < (i9 >> 1) + (i9 >> 2);
    }

    public static <E> a<E> u() {
        return new a<>();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int v(int i8) {
        int max = Math.max(i8, 2);
        if (max >= 751619276) {
            com.google.common.base.l.e(max < 1073741824, "collection too large");
            return 1073741824;
        }
        int highestOneBit = Integer.highestOneBit(max - 1) << 1;
        while (highestOneBit * 0.7d < max) {
            highestOneBit <<= 1;
        }
        return highestOneBit;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <E> ImmutableSet<E> x(int i8, Object... objArr) {
        if (i8 != 0) {
            if (i8 == 1) {
                Object obj = objArr[0];
                Objects.requireNonNull(obj);
                return I(obj);
            }
            int v8 = v(i8);
            Object[] objArr2 = new Object[v8];
            int i9 = v8 - 1;
            int i10 = 0;
            int i11 = 0;
            for (int i12 = 0; i12 < i8; i12++) {
                Object a9 = v1.a(objArr[i12], i12);
                int hashCode = a9.hashCode();
                int c9 = v0.c(hashCode);
                while (true) {
                    int i13 = c9 & i9;
                    Object obj2 = objArr2[i13];
                    if (obj2 == null) {
                        objArr[i11] = a9;
                        objArr2[i13] = a9;
                        i10 += hashCode;
                        i11++;
                        break;
                    } else if (obj2.equals(a9)) {
                        break;
                    } else {
                        c9++;
                    }
                }
            }
            Arrays.fill(objArr, i11, i8, (Object) null);
            if (i11 == 1) {
                Object obj3 = objArr[0];
                Objects.requireNonNull(obj3);
                return new q2(obj3);
            } else if (v(i11) < v8 / 2) {
                return x(i11, objArr);
            } else {
                if (O(i11, objArr.length)) {
                    objArr = Arrays.copyOf(objArr, i11);
                }
                return new g2(objArr, i10, objArr2, i9, i11);
            }
        }
        return H();
    }

    public static <E> ImmutableSet<E> y(Iterable<? extends E> iterable) {
        return iterable instanceof Collection ? A((Collection) iterable) : D(iterable.iterator());
    }

    ImmutableList<E> F() {
        return ImmutableList.q(toArray());
    }

    boolean G() {
        return false;
    }

    @Override // com.google.common.collect.ImmutableCollection
    public ImmutableList<E> e() {
        ImmutableList<E> immutableList = this.f19012b;
        if (immutableList == null) {
            ImmutableList<E> F = F();
            this.f19012b = F;
            return F;
        }
        return immutableList;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if ((obj instanceof ImmutableSet) && G() && ((ImmutableSet) obj).G() && hashCode() != obj.hashCode()) {
            return false;
        }
        return p2.a(this, obj);
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        return p2.d(this);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: p */
    public abstract d3<E> iterator();

    @Override // com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new b(toArray());
    }
}
