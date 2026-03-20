package com.google.common.collect;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.r1;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ImmutableSortedMultiset<E> extends c1<E> implements u2<E> {

    /* renamed from: d  reason: collision with root package name */
    transient ImmutableSortedMultiset<E> f19031d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<E> extends ImmutableMultiset.b<E> {

        /* renamed from: d  reason: collision with root package name */
        private final Comparator<? super E> f19032d;

        /* renamed from: e  reason: collision with root package name */
        E[] f19033e;

        /* renamed from: f  reason: collision with root package name */
        private int[] f19034f;

        /* renamed from: g  reason: collision with root package name */
        private int f19035g;

        /* renamed from: h  reason: collision with root package name */
        private boolean f19036h;

        public a(Comparator<? super E> comparator) {
            super(true);
            this.f19032d = (Comparator) com.google.common.base.l.n(comparator);
            this.f19033e = (E[]) new Object[4];
            this.f19034f = new int[4];
        }

        private void g(boolean z4) {
            int i8 = this.f19035g;
            if (i8 == 0) {
                return;
            }
            E[] eArr = (E[]) Arrays.copyOf(this.f19033e, i8);
            Arrays.sort(eArr, this.f19032d);
            int i9 = 1;
            for (int i10 = 1; i10 < eArr.length; i10++) {
                if (this.f19032d.compare((Object) eArr[i9 - 1], (Object) eArr[i10]) < 0) {
                    eArr[i9] = eArr[i10];
                    i9++;
                }
            }
            Arrays.fill(eArr, i9, this.f19035g, (Object) null);
            if (z4) {
                int i11 = i9 * 4;
                int i12 = this.f19035g;
                if (i11 > i12 * 3) {
                    eArr = (E[]) Arrays.copyOf(eArr, b8.b.c(i12, (i12 / 2) + 1));
                }
            }
            int[] iArr = new int[eArr.length];
            for (int i13 = 0; i13 < this.f19035g; i13++) {
                int binarySearch = Arrays.binarySearch(eArr, 0, i9, this.f19033e[i13], this.f19032d);
                int[] iArr2 = this.f19034f;
                if (iArr2[i13] >= 0) {
                    iArr[binarySearch] = iArr[binarySearch] + iArr2[i13];
                } else {
                    iArr[binarySearch] = ~iArr2[i13];
                }
            }
            this.f19033e = eArr;
            this.f19034f = iArr;
            this.f19035g = i9;
        }

        private void h() {
            g(false);
            int i8 = 0;
            int i9 = 0;
            while (true) {
                int i10 = this.f19035g;
                if (i8 >= i10) {
                    Arrays.fill(this.f19033e, i9, i10, (Object) null);
                    Arrays.fill(this.f19034f, i9, this.f19035g, 0);
                    this.f19035g = i9;
                    return;
                }
                int[] iArr = this.f19034f;
                if (iArr[i8] > 0) {
                    E[] eArr = this.f19033e;
                    eArr[i9] = eArr[i8];
                    iArr[i9] = iArr[i8];
                    i9++;
                }
                i8++;
            }
        }

        private void i() {
            int i8 = this.f19035g;
            E[] eArr = this.f19033e;
            if (i8 == eArr.length) {
                g(true);
            } else if (this.f19036h) {
                this.f19033e = (E[]) Arrays.copyOf(eArr, eArr.length);
            }
            this.f19036h = false;
        }

        @Override // com.google.common.collect.ImmutableCollection.b
        /* renamed from: d */
        public a<E> a(E e8) {
            return e(e8, 1);
        }

        public a<E> e(E e8, int i8) {
            com.google.common.base.l.n(e8);
            t.b(i8, "occurrences");
            if (i8 == 0) {
                return this;
            }
            i();
            E[] eArr = this.f19033e;
            int i9 = this.f19035g;
            eArr[i9] = e8;
            this.f19034f[i9] = i8;
            this.f19035g = i9 + 1;
            return this;
        }

        public ImmutableSortedMultiset<E> f() {
            h();
            int i8 = this.f19035g;
            if (i8 == 0) {
                return ImmutableSortedMultiset.A(this.f19032d);
            }
            i2 i2Var = (i2) ImmutableSortedSet.Q(this.f19032d, i8, this.f19033e);
            long[] jArr = new long[this.f19035g + 1];
            int i9 = 0;
            while (i9 < this.f19035g) {
                int i10 = i9 + 1;
                jArr[i10] = jArr[i9] + this.f19034f[i9];
                i9 = i10;
            }
            this.f19036h = true;
            return new h2(i2Var, jArr, 0, this.f19035g);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b<E> implements Serializable {

        /* renamed from: a  reason: collision with root package name */
        final Comparator<? super E> f19037a;

        /* renamed from: b  reason: collision with root package name */
        final E[] f19038b;

        /* renamed from: c  reason: collision with root package name */
        final int[] f19039c;

        b(u2<E> u2Var) {
            this.f19037a = u2Var.comparator();
            int size = u2Var.entrySet().size();
            this.f19038b = (E[]) new Object[size];
            this.f19039c = new int[size];
            int i8 = 0;
            for (r1.a<E> aVar : u2Var.entrySet()) {
                this.f19038b[i8] = aVar.a();
                this.f19039c[i8] = aVar.getCount();
                i8++;
            }
        }

        Object readResolve() {
            int length = this.f19038b.length;
            a aVar = new a(this.f19037a);
            for (int i8 = 0; i8 < length; i8++) {
                aVar.e(this.f19038b[i8], this.f19039c[i8]);
            }
            return aVar.f();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> ImmutableSortedMultiset<E> A(Comparator<? super E> comparator) {
        return y1.c().equals(comparator) ? (ImmutableSortedMultiset<E>) h2.f19316k : new h2(comparator);
    }

    @Override // com.google.common.collect.u2
    /* renamed from: D */
    public abstract ImmutableSortedMultiset<E> g0(E e8, BoundType boundType);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.common.collect.u2
    /* renamed from: E */
    public ImmutableSortedMultiset<E> P0(E e8, BoundType boundType, E e9, BoundType boundType2) {
        com.google.common.base.l.j(comparator().compare(e8, e9) <= 0, "Expected lowerBound <= upperBound but %s > %s", e8, e9);
        return n0(e8, boundType).g0(e9, boundType2);
    }

    @Override // com.google.common.collect.u2
    /* renamed from: F */
    public abstract ImmutableSortedMultiset<E> n0(E e8, BoundType boundType);

    @Override // com.google.common.collect.u2, com.google.common.collect.s2
    public final Comparator<? super E> comparator() {
        return t().comparator();
    }

    @Override // com.google.common.collect.u2
    @Deprecated
    public final r1.a<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.u2
    @Deprecated
    public final r1.a<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.ImmutableMultiset, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new b(this);
    }

    @Override // com.google.common.collect.u2
    /* renamed from: x */
    public ImmutableSortedMultiset<E> P() {
        ImmutableSortedMultiset<E> immutableSortedMultiset = this.f19031d;
        if (immutableSortedMultiset == null) {
            immutableSortedMultiset = isEmpty() ? A(y1.a(comparator()).f()) : new e0<>(this);
            this.f19031d = immutableSortedMultiset;
        }
        return immutableSortedMultiset;
    }

    @Override // com.google.common.collect.ImmutableMultiset
    /* renamed from: y */
    public abstract ImmutableSortedSet<E> t();
}
