package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y8 extends l7<Integer> implements f9, sa, RandomAccess {

    /* renamed from: d  reason: collision with root package name */
    private static final y8 f12683d = new y8(new int[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private int[] f12684b;

    /* renamed from: c  reason: collision with root package name */
    private int f12685c;

    y8() {
        this(new int[10], 0, true);
    }

    private y8(int[] iArr, int i8, boolean z4) {
        super(z4);
        this.f12684b = iArr;
        this.f12685c = i8;
    }

    public static y8 h() {
        return f12683d;
    }

    private final String k(int i8) {
        int i9 = this.f12685c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void n(int i8) {
        if (i8 < 0 || i8 >= this.f12685c) {
            throw new IndexOutOfBoundsException(k(i8));
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i8, Object obj) {
        int i9;
        int intValue = ((Integer) obj).intValue();
        e();
        if (i8 < 0 || i8 > (i9 = this.f12685c)) {
            throw new IndexOutOfBoundsException(k(i8));
        }
        int[] iArr = this.f12684b;
        if (i9 < iArr.length) {
            System.arraycopy(iArr, i8, iArr, i8 + 1, i9 - i8);
        } else {
            int[] iArr2 = new int[((i9 * 3) / 2) + 1];
            System.arraycopy(iArr, 0, iArr2, 0, i8);
            System.arraycopy(this.f12684b, i8, iArr2, i8 + 1, this.f12685c - i8);
            this.f12684b = iArr2;
        }
        this.f12684b[i8] = intValue;
        this.f12685c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* synthetic */ boolean add(Object obj) {
        i(((Integer) obj).intValue());
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection<? extends Integer> collection) {
        e();
        a9.e(collection);
        if (collection instanceof y8) {
            y8 y8Var = (y8) collection;
            int i8 = y8Var.f12685c;
            if (i8 == 0) {
                return false;
            }
            int i9 = this.f12685c;
            if (Integer.MAX_VALUE - i9 >= i8) {
                int i10 = i9 + i8;
                int[] iArr = this.f12684b;
                if (i10 > iArr.length) {
                    this.f12684b = Arrays.copyOf(iArr, i10);
                }
                System.arraycopy(y8Var.f12684b, 0, this.f12684b, this.f12685c, y8Var.f12685c);
                this.f12685c = i10;
                ((AbstractList) this).modCount++;
                return true;
            }
            throw new OutOfMemoryError();
        }
        return super.addAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.Collection, java.util.List
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof y8) {
            y8 y8Var = (y8) obj;
            if (this.f12685c != y8Var.f12685c) {
                return false;
            }
            int[] iArr = y8Var.f12684b;
            for (int i8 = 0; i8 < this.f12685c; i8++) {
                if (this.f12684b[i8] != iArr[i8]) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    public final int g(int i8) {
        n(i8);
        return this.f12684b[i8];
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        return Integer.valueOf(g(i8));
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.Collection, java.util.List
    public final int hashCode() {
        int i8 = 1;
        for (int i9 = 0; i9 < this.f12685c; i9++) {
            i8 = (i8 * 31) + this.f12684b[i9];
        }
        return i8;
    }

    public final void i(int i8) {
        e();
        int i9 = this.f12685c;
        int[] iArr = this.f12684b;
        if (i9 == iArr.length) {
            int[] iArr2 = new int[((i9 * 3) / 2) + 1];
            System.arraycopy(iArr, 0, iArr2, 0, i9);
            this.f12684b = iArr2;
        }
        int[] iArr3 = this.f12684b;
        int i10 = this.f12685c;
        this.f12685c = i10 + 1;
        iArr3[i10] = i8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        if (obj instanceof Integer) {
            int intValue = ((Integer) obj).intValue();
            int size = size();
            for (int i8 = 0; i8 < size; i8++) {
                if (this.f12684b[i8] == intValue) {
                    return i8;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.measurement.g9
    /* renamed from: o */
    public final f9 c(int i8) {
        if (i8 >= this.f12685c) {
            return new y8(Arrays.copyOf(this.f12684b, i8), this.f12685c, true);
        }
        throw new IllegalArgumentException();
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.List
    public final /* synthetic */ Object remove(int i8) {
        int i9;
        e();
        n(i8);
        int[] iArr = this.f12684b;
        int i10 = iArr[i8];
        if (i8 < this.f12685c - 1) {
            System.arraycopy(iArr, i8 + 1, iArr, i8, (i9 - i8) - 1);
        }
        this.f12685c--;
        ((AbstractList) this).modCount++;
        return Integer.valueOf(i10);
    }

    @Override // java.util.AbstractList
    protected final void removeRange(int i8, int i9) {
        e();
        if (i9 < i8) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        int[] iArr = this.f12684b;
        System.arraycopy(iArr, i9, iArr, i8, this.f12685c - i9);
        this.f12685c -= i9 - i8;
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object set(int i8, Object obj) {
        int intValue = ((Integer) obj).intValue();
        e();
        n(i8);
        int[] iArr = this.f12684b;
        int i9 = iArr[i8];
        iArr[i8] = intValue;
        return Integer.valueOf(i9);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f12685c;
    }
}
