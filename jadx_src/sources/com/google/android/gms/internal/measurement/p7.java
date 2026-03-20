package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class p7 extends l7<Boolean> implements sa, RandomAccess {

    /* renamed from: d  reason: collision with root package name */
    private static final p7 f12439d = new p7(new boolean[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private boolean[] f12440b;

    /* renamed from: c  reason: collision with root package name */
    private int f12441c;

    p7() {
        this(new boolean[10], 0, true);
    }

    private p7(boolean[] zArr, int i8, boolean z4) {
        super(z4);
        this.f12440b = zArr;
        this.f12441c = i8;
    }

    private final String h(int i8) {
        int i9 = this.f12441c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void i(int i8) {
        if (i8 < 0 || i8 >= this.f12441c) {
            throw new IndexOutOfBoundsException(h(i8));
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i8, Object obj) {
        int i9;
        boolean booleanValue = ((Boolean) obj).booleanValue();
        e();
        if (i8 < 0 || i8 > (i9 = this.f12441c)) {
            throw new IndexOutOfBoundsException(h(i8));
        }
        boolean[] zArr = this.f12440b;
        if (i9 < zArr.length) {
            System.arraycopy(zArr, i8, zArr, i8 + 1, i9 - i8);
        } else {
            boolean[] zArr2 = new boolean[((i9 * 3) / 2) + 1];
            System.arraycopy(zArr, 0, zArr2, 0, i8);
            System.arraycopy(this.f12440b, i8, zArr2, i8 + 1, this.f12441c - i8);
            this.f12440b = zArr2;
        }
        this.f12440b[i8] = booleanValue;
        this.f12441c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* synthetic */ boolean add(Object obj) {
        g(((Boolean) obj).booleanValue());
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection<? extends Boolean> collection) {
        e();
        a9.e(collection);
        if (collection instanceof p7) {
            p7 p7Var = (p7) collection;
            int i8 = p7Var.f12441c;
            if (i8 == 0) {
                return false;
            }
            int i9 = this.f12441c;
            if (Integer.MAX_VALUE - i9 >= i8) {
                int i10 = i9 + i8;
                boolean[] zArr = this.f12440b;
                if (i10 > zArr.length) {
                    this.f12440b = Arrays.copyOf(zArr, i10);
                }
                System.arraycopy(p7Var.f12440b, 0, this.f12440b, this.f12441c, p7Var.f12441c);
                this.f12441c = i10;
                ((AbstractList) this).modCount++;
                return true;
            }
            throw new OutOfMemoryError();
        }
        return super.addAll(collection);
    }

    @Override // com.google.android.gms.internal.measurement.g9
    public final /* synthetic */ g9 c(int i8) {
        if (i8 >= this.f12441c) {
            return new p7(Arrays.copyOf(this.f12440b, i8), this.f12441c, true);
        }
        throw new IllegalArgumentException();
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
        if (obj instanceof p7) {
            p7 p7Var = (p7) obj;
            if (this.f12441c != p7Var.f12441c) {
                return false;
            }
            boolean[] zArr = p7Var.f12440b;
            for (int i8 = 0; i8 < this.f12441c; i8++) {
                if (this.f12440b[i8] != zArr[i8]) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    public final void g(boolean z4) {
        e();
        int i8 = this.f12441c;
        boolean[] zArr = this.f12440b;
        if (i8 == zArr.length) {
            boolean[] zArr2 = new boolean[((i8 * 3) / 2) + 1];
            System.arraycopy(zArr, 0, zArr2, 0, i8);
            this.f12440b = zArr2;
        }
        boolean[] zArr3 = this.f12440b;
        int i9 = this.f12441c;
        this.f12441c = i9 + 1;
        zArr3[i9] = z4;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        i(i8);
        return Boolean.valueOf(this.f12440b[i8]);
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.Collection, java.util.List
    public final int hashCode() {
        int i8 = 1;
        for (int i9 = 0; i9 < this.f12441c; i9++) {
            i8 = (i8 * 31) + a9.c(this.f12440b[i9]);
        }
        return i8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        if (obj instanceof Boolean) {
            boolean booleanValue = ((Boolean) obj).booleanValue();
            int size = size();
            for (int i8 = 0; i8 < size; i8++) {
                if (this.f12440b[i8] == booleanValue) {
                    return i8;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.List
    public final /* synthetic */ Object remove(int i8) {
        int i9;
        e();
        i(i8);
        boolean[] zArr = this.f12440b;
        boolean z4 = zArr[i8];
        if (i8 < this.f12441c - 1) {
            System.arraycopy(zArr, i8 + 1, zArr, i8, (i9 - i8) - 1);
        }
        this.f12441c--;
        ((AbstractList) this).modCount++;
        return Boolean.valueOf(z4);
    }

    @Override // java.util.AbstractList
    protected final void removeRange(int i8, int i9) {
        e();
        if (i9 < i8) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        boolean[] zArr = this.f12440b;
        System.arraycopy(zArr, i9, zArr, i8, this.f12441c - i9);
        this.f12441c -= i9 - i8;
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object set(int i8, Object obj) {
        boolean booleanValue = ((Boolean) obj).booleanValue();
        e();
        i(i8);
        boolean[] zArr = this.f12440b;
        boolean z4 = zArr[i8];
        zArr[i8] = booleanValue;
        return Boolean.valueOf(z4);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f12441c;
    }
}
