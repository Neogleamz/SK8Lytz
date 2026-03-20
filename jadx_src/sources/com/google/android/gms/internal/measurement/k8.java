package com.google.android.gms.internal.measurement;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k8 extends l7<Double> implements sa, RandomAccess {

    /* renamed from: d  reason: collision with root package name */
    private static final k8 f12283d = new k8(new double[0], 0, false);

    /* renamed from: b  reason: collision with root package name */
    private double[] f12284b;

    /* renamed from: c  reason: collision with root package name */
    private int f12285c;

    k8() {
        this(new double[10], 0, true);
    }

    private k8(double[] dArr, int i8, boolean z4) {
        super(z4);
        this.f12284b = dArr;
        this.f12285c = i8;
    }

    private final String h(int i8) {
        int i9 = this.f12285c;
        return "Index:" + i8 + ", Size:" + i9;
    }

    private final void i(int i8) {
        if (i8 < 0 || i8 >= this.f12285c) {
            throw new IndexOutOfBoundsException(h(i8));
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ void add(int i8, Object obj) {
        int i9;
        double doubleValue = ((Double) obj).doubleValue();
        e();
        if (i8 < 0 || i8 > (i9 = this.f12285c)) {
            throw new IndexOutOfBoundsException(h(i8));
        }
        double[] dArr = this.f12284b;
        if (i9 < dArr.length) {
            System.arraycopy(dArr, i8, dArr, i8 + 1, i9 - i8);
        } else {
            double[] dArr2 = new double[((i9 * 3) / 2) + 1];
            System.arraycopy(dArr, 0, dArr2, 0, i8);
            System.arraycopy(this.f12284b, i8, dArr2, i8 + 1, this.f12285c - i8);
            this.f12284b = dArr2;
        }
        this.f12284b[i8] = doubleValue;
        this.f12285c++;
        ((AbstractList) this).modCount++;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final /* synthetic */ boolean add(Object obj) {
        g(((Double) obj).doubleValue());
        return true;
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractCollection, java.util.Collection, java.util.List
    public final boolean addAll(Collection<? extends Double> collection) {
        e();
        a9.e(collection);
        if (collection instanceof k8) {
            k8 k8Var = (k8) collection;
            int i8 = k8Var.f12285c;
            if (i8 == 0) {
                return false;
            }
            int i9 = this.f12285c;
            if (Integer.MAX_VALUE - i9 >= i8) {
                int i10 = i9 + i8;
                double[] dArr = this.f12284b;
                if (i10 > dArr.length) {
                    this.f12284b = Arrays.copyOf(dArr, i10);
                }
                System.arraycopy(k8Var.f12284b, 0, this.f12284b, this.f12285c, k8Var.f12285c);
                this.f12285c = i10;
                ((AbstractList) this).modCount++;
                return true;
            }
            throw new OutOfMemoryError();
        }
        return super.addAll(collection);
    }

    @Override // com.google.android.gms.internal.measurement.g9
    public final /* synthetic */ g9 c(int i8) {
        if (i8 >= this.f12285c) {
            return new k8(Arrays.copyOf(this.f12284b, i8), this.f12285c, true);
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
        if (obj instanceof k8) {
            k8 k8Var = (k8) obj;
            if (this.f12285c != k8Var.f12285c) {
                return false;
            }
            double[] dArr = k8Var.f12284b;
            for (int i8 = 0; i8 < this.f12285c; i8++) {
                if (Double.doubleToLongBits(this.f12284b[i8]) != Double.doubleToLongBits(dArr[i8])) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(obj);
    }

    public final void g(double d8) {
        e();
        int i8 = this.f12285c;
        double[] dArr = this.f12284b;
        if (i8 == dArr.length) {
            double[] dArr2 = new double[((i8 * 3) / 2) + 1];
            System.arraycopy(dArr, 0, dArr2, 0, i8);
            this.f12284b = dArr2;
        }
        double[] dArr3 = this.f12284b;
        int i9 = this.f12285c;
        this.f12285c = i9 + 1;
        dArr3[i9] = d8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object get(int i8) {
        i(i8);
        return Double.valueOf(this.f12284b[i8]);
    }

    @Override // com.google.android.gms.internal.measurement.l7, java.util.AbstractList, java.util.Collection, java.util.List
    public final int hashCode() {
        int i8 = 1;
        for (int i9 = 0; i9 < this.f12285c; i9++) {
            i8 = (i8 * 31) + a9.b(Double.doubleToLongBits(this.f12284b[i9]));
        }
        return i8;
    }

    @Override // java.util.AbstractList, java.util.List
    public final int indexOf(Object obj) {
        if (obj instanceof Double) {
            double doubleValue = ((Double) obj).doubleValue();
            int size = size();
            for (int i8 = 0; i8 < size; i8++) {
                if (this.f12284b[i8] == doubleValue) {
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
        double[] dArr = this.f12284b;
        double d8 = dArr[i8];
        if (i8 < this.f12285c - 1) {
            System.arraycopy(dArr, i8 + 1, dArr, i8, (i9 - i8) - 1);
        }
        this.f12285c--;
        ((AbstractList) this).modCount++;
        return Double.valueOf(d8);
    }

    @Override // java.util.AbstractList
    protected final void removeRange(int i8, int i9) {
        e();
        if (i9 < i8) {
            throw new IndexOutOfBoundsException("toIndex < fromIndex");
        }
        double[] dArr = this.f12284b;
        System.arraycopy(dArr, i9, dArr, i8, this.f12285c - i9);
        this.f12285c -= i9 - i8;
        ((AbstractList) this).modCount++;
    }

    @Override // java.util.AbstractList, java.util.List
    public final /* synthetic */ Object set(int i8, Object obj) {
        double doubleValue = ((Double) obj).doubleValue();
        e();
        i(i8);
        double[] dArr = this.f12284b;
        double d8 = dArr[i8];
        dArr[i8] = doubleValue;
        return Double.valueOf(d8);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.f12285c;
    }
}
