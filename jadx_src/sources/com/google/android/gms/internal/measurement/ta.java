package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class ta<E> extends l7<E> implements RandomAccess {

    /* renamed from: d  reason: collision with root package name */
    private static final ta<Object> f12529d = new ta<>(new Object[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private E[] f12530b;

    /* renamed from: c  reason: collision with root package name */
    private int f12531c;

    private ta(E[] eArr, int i8, boolean z4) {
        super(z4);
        this.f12530b = eArr;
        this.f12531c = i8;
    }

    private final String g(int i8) {
        int i9 = this.f12531c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void h(int i8) {
        if (i8 < 0 || i8 >= this.f12531c) {
            throw new IndexOutOfBoundsException(g(i8));
        }
    }

    public static <E> ta<E> i() {
        return (ta<E>) f12529d;
    }

    @Override // java.util.AbstractList, java.util.List
    public final void add(int i8, E e8) {
        int i9;
        e();
        if (i8 < 0 || i8 > (i9 = this.f12531c)) {
            throw new IndexOutOfBoundsException(g(i8));
        }
        E[] eArr = this.f12530b;
        if (i9 < eArr.length) {
            System.arraycopy(eArr, i8, eArr, i8 + 1, i9 - i8);
        } else {
            E[] eArr2 = (E[]) new Object[((i9 * 3) / 2) + 1];
            System.arraycopy(eArr, 0, eArr2, 0, i8);
            System.arraycopy(this.f12530b, i8, eArr2, i8 + 1, this.f12531c - i8);
            this.f12530b = eArr2;
        }
        this.f12530b[i8] = e8;
        this.f12531c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean add(E e8) {
        e();
        int i8 = this.f12531c;
        E[] eArr = this.f12530b;
        if (i8 == eArr.length) {
            this.f12530b = (E[]) Arrays.copyOf(eArr, ((i8 * 3) / 2) + 1);
        }
        E[] eArr2 = this.f12530b;
        int i9 = this.f12531c;
        this.f12531c = i9 + 1;
        eArr2[i9] = e8;
        ((AbstractList) this).modCount++;
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.g9
    public final /* synthetic */ g9 c(int i8) {
        if (i8 >= this.f12531c) {
            return new ta(Arrays.copyOf(this.f12530b, i8), this.f12531c, true);
        }
        throw new IllegalArgumentException();
    }

    @Override // java.util.AbstractList, java.util.List
    public final E get(int i8) {
        h(i8);
        return this.f12530b[i8];
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.List
    public final E remove(int i8) {
        int i9;
        e();
        h(i8);
        E[] eArr = this.f12530b;
        E e8 = eArr[i8];
        if (i8 < this.f12531c - 1) {
            System.arraycopy(eArr, i8 + 1, eArr, i8, (i9 - i8) - 1);
        }
        this.f12531c--;
        ((AbstractList) this).modCount++;
        return e8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final E set(int i8, E e8) {
        e();
        h(i8);
        E[] eArr = this.f12530b;
        E e9 = eArr[i8];
        eArr[i8] = e8;
        ((AbstractList) this).modCount++;
        return e9;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f12531c;
    }
}
