package com.google.common.collect;

import com.google.common.collect.r1;
import java.util.Comparator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h2<E> extends ImmutableSortedMultiset<E> {

    /* renamed from: j  reason: collision with root package name */
    private static final long[] f19315j = {0};

    /* renamed from: k  reason: collision with root package name */
    static final ImmutableSortedMultiset<Comparable> f19316k = new h2(y1.c());

    /* renamed from: e  reason: collision with root package name */
    final transient i2<E> f19317e;

    /* renamed from: f  reason: collision with root package name */
    private final transient long[] f19318f;

    /* renamed from: g  reason: collision with root package name */
    private final transient int f19319g;

    /* renamed from: h  reason: collision with root package name */
    private final transient int f19320h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h2(i2<E> i2Var, long[] jArr, int i8, int i9) {
        this.f19317e = i2Var;
        this.f19318f = jArr;
        this.f19319g = i8;
        this.f19320h = i9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h2(Comparator<? super E> comparator) {
        this.f19317e = ImmutableSortedSet.X(comparator);
        this.f19318f = f19315j;
        this.f19319g = 0;
        this.f19320h = 0;
    }

    private int G(int i8) {
        long[] jArr = this.f19318f;
        int i9 = this.f19319g;
        return (int) (jArr[(i9 + i8) + 1] - jArr[i9 + i8]);
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.u2
    /* renamed from: D */
    public ImmutableSortedMultiset<E> g0(E e8, BoundType boundType) {
        return H(0, this.f19317e.u0(e8, com.google.common.base.l.n(boundType) == BoundType.CLOSED));
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.u2
    /* renamed from: F */
    public ImmutableSortedMultiset<E> n0(E e8, BoundType boundType) {
        return H(this.f19317e.v0(e8, com.google.common.base.l.n(boundType) == BoundType.CLOSED), this.f19320h);
    }

    ImmutableSortedMultiset<E> H(int i8, int i9) {
        com.google.common.base.l.r(i8, i9, this.f19320h);
        return i8 == i9 ? ImmutableSortedMultiset.A(comparator()) : (i8 == 0 && i9 == this.f19320h) ? this : new h2(this.f19317e.s0(i8, i9), this.f19318f, this.f19319g + i8, i9 - i8);
    }

    @Override // com.google.common.collect.u2
    public r1.a<E> firstEntry() {
        if (isEmpty()) {
            return null;
        }
        return v(0);
    }

    @Override // com.google.common.collect.u2
    public r1.a<E> lastEntry() {
        if (isEmpty()) {
            return null;
        }
        return v(this.f19320h - 1);
    }

    @Override // com.google.common.collect.r1
    public int m0(Object obj) {
        int indexOf = this.f19317e.indexOf(obj);
        if (indexOf >= 0) {
            return G(indexOf);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableCollection
    public boolean n() {
        return this.f19319g > 0 || this.f19320h < this.f19318f.length - 1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, com.google.common.collect.r1
    public int size() {
        long[] jArr = this.f19318f;
        int i8 = this.f19319g;
        return com.google.common.primitives.g.k(jArr[this.f19320h + i8] - jArr[i8]);
    }

    @Override // com.google.common.collect.ImmutableMultiset
    r1.a<E> v(int i8) {
        return s1.g(this.f19317e.e().get(i8), G(i8));
    }

    @Override // com.google.common.collect.ImmutableSortedMultiset, com.google.common.collect.ImmutableMultiset
    /* renamed from: y */
    public ImmutableSortedSet<E> t() {
        return this.f19317e;
    }
}
