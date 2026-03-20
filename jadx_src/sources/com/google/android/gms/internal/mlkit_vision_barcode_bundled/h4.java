package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h4 extends a1 implements RandomAccess {

    /* renamed from: d  reason: collision with root package name */
    private static final h4 f14777d = new h4(new Object[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private Object[] f14778b;

    /* renamed from: c  reason: collision with root package name */
    private int f14779c;

    private h4(Object[] objArr, int i8, boolean z4) {
        super(z4);
        this.f14778b = objArr;
        this.f14779c = i8;
    }

    public static h4 g() {
        return f14777d;
    }

    private final String h(int i8) {
        int i9 = this.f14779c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void i(int i8) {
        if (i8 < 0 || i8 >= this.f14779c) {
            throw new IndexOutOfBoundsException(h(i8));
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final void add(int i8, Object obj) {
        int i9;
        e();
        if (i8 < 0 || i8 > (i9 = this.f14779c)) {
            throw new IndexOutOfBoundsException(h(i8));
        }
        Object[] objArr = this.f14778b;
        if (i9 < objArr.length) {
            System.arraycopy(objArr, i8, objArr, i8 + 1, i9 - i8);
        } else {
            Object[] objArr2 = new Object[((i9 * 3) / 2) + 1];
            System.arraycopy(objArr, 0, objArr2, 0, i8);
            System.arraycopy(this.f14778b, i8, objArr2, i8 + 1, this.f14779c - i8);
            this.f14778b = objArr2;
        }
        this.f14778b[i8] = obj;
        this.f14779c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean add(Object obj) {
        e();
        int i8 = this.f14779c;
        Object[] objArr = this.f14778b;
        if (i8 == objArr.length) {
            this.f14778b = Arrays.copyOf(objArr, ((i8 * 3) / 2) + 1);
        }
        Object[] objArr2 = this.f14778b;
        int i9 = this.f14779c;
        this.f14779c = i9 + 1;
        objArr2[i9] = obj;
        ((AbstractList) this).modCount++;
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public final Object get(int i8) {
        i(i8);
        return this.f14778b[i8];
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.List
    public final Object remove(int i8) {
        int i9;
        e();
        i(i8);
        Object[] objArr = this.f14778b;
        Object obj = objArr[i8];
        if (i8 < this.f14779c - 1) {
            System.arraycopy(objArr, i8 + 1, objArr, i8, (i9 - i8) - 1);
        }
        this.f14779c--;
        ((AbstractList) this).modCount++;
        return obj;
    }

    @Override // java.util.AbstractList, java.util.List
    public final Object set(int i8, Object obj) {
        e();
        i(i8);
        Object[] objArr = this.f14778b;
        Object obj2 = objArr[i8];
        objArr[i8] = obj;
        ((AbstractList) this).modCount++;
        return obj2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f14779c;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2
    public final /* bridge */ /* synthetic */ x2 z(int i8) {
        if (i8 >= this.f14779c) {
            return new h4(Arrays.copyOf(this.f14778b, i8), this.f14779c, true);
        }
        throw new IllegalArgumentException();
    }
}
