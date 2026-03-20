package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class y1 extends a1 implements RandomAccess, f4 {

    /* renamed from: d  reason: collision with root package name */
    private static final y1 f14882d = new y1(new double[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private double[] f14883b;

    /* renamed from: c  reason: collision with root package name */
    private int f14884c;

    y1() {
        this(new double[10], 0, true);
    }

    private y1(double[] dArr, int i8, boolean z4) {
        super(z4);
        this.f14883b = dArr;
        this.f14884c = i8;
    }

    private final String h(int i8) {
        int i9 = this.f14884c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void i(int i8) {
        if (i8 < 0 || i8 >= this.f14884c) {
            throw new IndexOutOfBoundsException(h(i8));
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i8, Object obj) {
        int i9;
        double doubleValue = ((Double) obj).doubleValue();
        e();
        if (i8 < 0 || i8 > (i9 = this.f14884c)) {
            throw new IndexOutOfBoundsException(h(i8));
        }
        double[] dArr = this.f14883b;
        if (i9 < dArr.length) {
            System.arraycopy(dArr, i8, dArr, i8 + 1, i9 - i8);
        } else {
            double[] dArr2 = new double[((i9 * 3) / 2) + 1];
            System.arraycopy(dArr, 0, dArr2, 0, i8);
            System.arraycopy(this.f14883b, i8, dArr2, i8 + 1, this.f14884c - i8);
            this.f14883b = dArr2;
        }
        this.f14883b[i8] = doubleValue;
        this.f14884c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* bridge */ /* synthetic */ boolean add(Object obj) {
        g(((Double) obj).doubleValue());
        return true;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection collection) {
        e();
        byte[] bArr = y2.f14888d;
        Objects.requireNonNull(collection);
        if (collection instanceof y1) {
            y1 y1Var = (y1) collection;
            int i8 = y1Var.f14884c;
            if (i8 == 0) {
                return false;
            }
            int i9 = this.f14884c;
            if (Integer.MAX_VALUE - i9 >= i8) {
                int i10 = i9 + i8;
                double[] dArr = this.f14883b;
                if (i10 > dArr.length) {
                    this.f14883b = Arrays.copyOf(dArr, i10);
                }
                System.arraycopy(y1Var.f14883b, 0, this.f14883b, this.f14884c, y1Var.f14884c);
                this.f14884c = i10;
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

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.Collection, java.util.List
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof y1) {
            y1 y1Var = (y1) obj;
            if (this.f14884c != y1Var.f14884c) {
                return false;
            }
            double[] dArr = y1Var.f14883b;
            for (int i8 = 0; i8 < this.f14884c; i8++) {
                if (Double.doubleToLongBits(this.f14883b[i8]) != Double.doubleToLongBits(dArr[i8])) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    public final void g(double d8) {
        e();
        int i8 = this.f14884c;
        double[] dArr = this.f14883b;
        if (i8 == dArr.length) {
            double[] dArr2 = new double[((i8 * 3) / 2) + 1];
            System.arraycopy(dArr, 0, dArr2, 0, i8);
            this.f14883b = dArr2;
        }
        double[] dArr3 = this.f14883b;
        int i9 = this.f14884c;
        this.f14884c = i9 + 1;
        dArr3[i9] = d8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        i(i8);
        return Double.valueOf(this.f14883b[i8]);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.Collection, java.util.List
    public final int hashCode() {
        int i8 = 1;
        for (int i9 = 0; i9 < this.f14884c; i9++) {
            long doubleToLongBits = Double.doubleToLongBits(this.f14883b[i9]);
            byte[] bArr = y2.f14888d;
            i8 = (i8 * 31) + ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32)));
        }
        return i8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        if (obj instanceof Double) {
            double doubleValue = ((Double) obj).doubleValue();
            int i8 = this.f14884c;
            for (int i9 = 0; i9 < i8; i9++) {
                if (this.f14883b[i9] == doubleValue) {
                    return i9;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.a1, java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object remove(int i8) {
        int i9;
        e();
        i(i8);
        double[] dArr = this.f14883b;
        double d8 = dArr[i8];
        if (i8 < this.f14884c - 1) {
            System.arraycopy(dArr, i8 + 1, dArr, i8, (i9 - i8) - 1);
        }
        this.f14884c--;
        ((AbstractList) this).modCount++;
        return Double.valueOf(d8);
    }

    @Override // java.util.AbstractList
    protected final void removeRange(int i8, int i9) {
        e();
        if (i9 < i8) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        double[] dArr = this.f14883b;
        System.arraycopy(dArr, i9, dArr, i8, this.f14884c - i9);
        this.f14884c -= i9 - i8;
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* bridge */ /* synthetic */ Object set(int i8, Object obj) {
        double doubleValue = ((Double) obj).doubleValue();
        e();
        i(i8);
        double[] dArr = this.f14883b;
        double d8 = dArr[i8];
        dArr[i8] = doubleValue;
        return Double.valueOf(d8);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f14884c;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.x2
    public final /* bridge */ /* synthetic */ x2 z(int i8) {
        if (i8 >= this.f14884c) {
            return new y1(Arrays.copyOf(this.f14883b, i8), this.f14884c, true);
        }
        throw new IllegalArgumentException();
    }
}
